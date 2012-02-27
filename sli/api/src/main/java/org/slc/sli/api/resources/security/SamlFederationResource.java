package org.slc.sli.api.resources.security;

import org.apache.commons.lang3.tuple.Pair;
import org.jdom.Document;
import org.jdom.Element;
import org.slc.sli.api.security.SLIPrincipal;
import org.slc.sli.api.security.oauth.OAuthSessionService;
import org.slc.sli.api.security.resolve.UserLocator;
import org.slc.sli.api.security.saml.SamlAttributeTransformer;
import org.slc.sli.api.security.saml.SamlHelper;
import org.slc.sli.dal.convert.IdConverter;
import org.slc.sli.domain.Entity;
import org.slc.sli.domain.EntityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.xml.sax.InputSource;

import javax.annotation.PostConstruct;
import javax.persistence.EntityNotFoundException;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.Map;

/**
 * Process SAML assertions
 *
 * @author dkornishev
 */
@Component
@Path("/saml")
public class SamlFederationResource {

    private static final Logger LOG = LoggerFactory.getLogger(SamlFederationResource.class);

    @Autowired
    private SamlHelper saml;

    @Autowired
    private EntityRepository repo;

    @Autowired
    private IdConverter bin;

    @Autowired
    private UserLocator users;

    @Autowired
    private SamlAttributeTransformer transformer;

    @Value("${sli.security.sp.issuerName}")
    private String metadataSpIssuerName;

    @Autowired
    private OAuthSessionService oauth;
    
    @Value("classpath:saml/samlMetadata.xml.template")
    private Resource metadataTemplateResource;
    
    private String metadata;
    
    @PostConstruct
    private void processMetadata() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        InputStream is = metadataTemplateResource.getInputStream();
        byte[] buf = new byte[1024];
        int r = is.read(buf);
        while (r > -1) {
            baos.write(buf, 0, r);
            r = is.read(buf);
        }
        metadata = new String(baos.toByteArray());
        metadata = metadata.replaceAll("\\$\\{sli\\.security\\.sp.issuerName\\}", metadataSpIssuerName);
        is.close();
        
    }
    
    @GET
    @Path("ssoInit")
    public String getSsoRedirect(@QueryParam("realmId") final String realmId, @QueryParam("requestToken") String requestToken) {

        if (realmId == null) {
            throw new IllegalArgumentException("Must provide authentication realm id");
        }

        String idp = fetchIdpEndpoint(realmId);

        // {messageId,encodedSAML}
        Pair<String, String> tuple = saml.createSamlAuthnRequestForRedirect(idp);

        oauth.storeSamlMessageId(requestToken, tuple.getLeft());

        return idp + "?SAMLRequest=" + tuple.getRight();
    }

    @POST
    @Path("sso/post")
    @SuppressWarnings("unchecked")
    public Response consume(@FormParam("SAMLResponse") String postData) throws Exception {

        LOG.info("Received a SAML post...");

        Document doc = saml.decodeSamlPost(postData);

        String msgId = doc.getRootElement().getAttributeValue("ID");
        String inResponseTo = doc.getRootElement().getAttributeValue("InResponseTo");
        String issuer = doc.getRootElement().getChildText("Issuer", SamlHelper.SAML_NS);

        Entity realm = fetchOne("realm", new Query(Criteria.where("body.idp.id").is(issuer)));

        Element stmt = doc.getRootElement().getChild("Assertion", SamlHelper.SAML_NS).getChild("AttributeStatement", SamlHelper.SAML_NS);
        List<Element> attributeNodes = stmt.getChildren("Attribute", SamlHelper.SAML_NS);

        LinkedMultiValueMap<String, String> attributes = new LinkedMultiValueMap<String, String>();
        for (Element attributeNode : attributeNodes) {
            String samlAttributeName = attributeNode.getAttributeValue("Name");
            List<Element> valueNodes = attributeNode.getChildren("AttributeValue", SamlHelper.SAML_NS);
            for (Element valueNode : valueNodes) {
                attributes.add(samlAttributeName, valueNode.getText());
            }
        }

        // Apply transforms
        attributes = transformer.apply(realm, attributes);

        // TODO change everything authRealm to use issuer instead of authRealm

        final SLIPrincipal principal = users.locate(issuer, attributes.getFirst("userId"));
        principal.setName(attributes.getFirst("userName"));
        principal.setRoles(attributes.get("roles"));

        String redirect = oauth.userAuthenticated(inResponseTo, msgId, principal);

        return Response.temporaryRedirect(URI.create(redirect)).build();
    }

    @POST
    @Path("slo/post")
    public void processSingleLogoutPost() {
        // TODO slo will post something here, what? Need those arguments
    }

    @SuppressWarnings("unchecked")
    private String fetchIdpEndpoint(final String realmId) {
        Iterable<Entity> idpEntity = repo.findByQuery("realm", new Query(Criteria.where("_id").is(bin.toDatabaseId(realmId))), 0, 1);

        if (!idpEntity.iterator().hasNext()) {
            throw new IllegalArgumentException("Couldn't locate idp for realm: " + realmId);
        }

        Map<String, String> idpNode = (Map<String, String>) idpEntity.iterator().next().getBody().get("idp");
        String idp = (String) idpNode.get("redirectEndpoint");

        if (idp == null) {
            throw new IllegalStateException("Realm " + realmId + " doesn't have an idp endpoint");
        }

        return idp;
    }

    private Entity fetchOne(String collection, Query query) {
        Iterable<Entity> results = repo.findByQuery(collection, query, 0, 1);

        if (!results.iterator().hasNext()) {
            throw new EntityNotFoundException("Not found");
        }

        return results.iterator().next();
    }

    /**
     * Get metadata describing saml federation.
     * This is an unsecured (public) resource.
     *
     * @return Response containing saml metadata
     */
    @GET
    @Path("metadata")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public Response getMetadata() {

        if (!metadata.isEmpty()) {
            return Response.ok(metadata).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();

    }
}
