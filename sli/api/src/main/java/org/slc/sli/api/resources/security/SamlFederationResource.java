package org.slc.sli.api.resources.security;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URI;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.jdom.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;

import org.slc.sli.api.security.SLIPrincipal;
import org.slc.sli.api.security.oauth.MongoAuthorizationCodeServices;
import org.slc.sli.api.security.resolve.UserLocator;
import org.slc.sli.api.security.saml.SamlAttributeTransformer;
import org.slc.sli.api.security.saml.SamlHelper;
import org.slc.sli.domain.Entity;
import org.slc.sli.domain.NeutralCriteria;
import org.slc.sli.domain.NeutralQuery;
import org.slc.sli.domain.Repository;

/**
 * Process SAML assertions
 * 
 * @author dkornishev
 */
@Component
@Path("saml")
public class SamlFederationResource {
    
    private static final Logger LOG = LoggerFactory.getLogger(SamlFederationResource.class);
    
    @Autowired
    private SamlHelper saml;
    
    @Autowired
    private Repository<Entity> repo;
    
    @Autowired
    private UserLocator users;
    
    @Autowired
    private SamlAttributeTransformer transformer;
    
    @Autowired
    private MongoAuthorizationCodeServices authCodeServices;
    
    @Value("${sli.security.sp.issuerName}")
    private String metadataSpIssuerName;
    
    @Value("classpath:saml/samlMetadata.xml.template")
    private Resource metadataTemplateResource;
    
    private String metadata;
    
    @SuppressWarnings("unused")
    @PostConstruct
    private void processMetadata() throws IOException {
        InputStream is = metadataTemplateResource.getInputStream();
        StringWriter writer = new StringWriter();
        IOUtils.copy(is, writer);
        is.close();
        
        metadata = writer.toString();
        metadata = metadata.replaceAll("\\$\\{sli\\.security\\.sp.issuerName\\}", metadataSpIssuerName);
    }
    
    @POST
    @Path("sso/post")
    @SuppressWarnings("unchecked")
    public Response consume(@FormParam("SAMLResponse") String postData, 
            @Context HttpServletResponse response) throws Exception {
        
        LOG.info("Received a SAML post for SSO...");
        
        Document doc = saml.decodeSamlPost(postData);
        
        String inResponseTo = doc.getRootElement().getAttributeValue("InResponseTo");
        String samlMessageId = doc.getRootElement().getAttributeValue("ID");
        String issuer = doc.getRootElement().getChildText("Issuer", SamlHelper.SAML_NS);
        
        NeutralQuery neutralQuery = new NeutralQuery();
        neutralQuery.setOffset(0);
        neutralQuery.setLimit(1);
        neutralQuery.addCriteria(new NeutralCriteria("idp.id", "=", issuer));
        Entity realm = fetchOne("realm", neutralQuery);
        
        if (realm == null) {
            throw new IllegalStateException("Failed to locate realm: " + issuer);
        }
        
        org.jdom.Element stmt = doc.getRootElement().getChild("Assertion", SamlHelper.SAML_NS).getChild("AttributeStatement", SamlHelper.SAML_NS);
        List<org.jdom.Element> attributeNodes = stmt.getChildren("Attribute", SamlHelper.SAML_NS);
        
        LinkedMultiValueMap<String, String> attributes = new LinkedMultiValueMap<String, String>();
        for (org.jdom.Element attributeNode : attributeNodes) {
            String samlAttributeName = attributeNode.getAttributeValue("Name");
            List<org.jdom.Element> valueNodes = attributeNode.getChildren("AttributeValue", SamlHelper.SAML_NS);
            for (org.jdom.Element valueNode : valueNodes) {
                attributes.add(samlAttributeName, valueNode.getText());
            }
        }
        
        // Apply transforms
        attributes = transformer.apply(realm, attributes);
        
        SLIPrincipal principal = users.locate((String) realm.getBody().get("tenantId"), attributes.getFirst("userId"));
        principal.setName(attributes.getFirst("userName"));
        principal.setRoles(attributes.get("roles"));
        principal.setRealm(realm.getEntityId());
        principal.setAdminRealm(attributes.getFirst("adminRealm"));
        
        // create sessionIndex --> this should probably be more advanced in the future
        String sessionIndex = samlMessageId;        
        String redirect = authCodeServices.createAuthorizationCodeForMessageId(inResponseTo, principal, sessionIndex);
        
        // create cookie here corresponding to session passed into authorization code above
        // TODO: make this into an http-only cookie by updating the javax.servlet.api-servlet version in the sli/pom.xml to 3.0+
        Cookie cookie = new Cookie("_tla", sessionIndex);
        cookie.setDomain(".slidev.org");
        cookie.setPath("/");       
        response.addCookie(cookie);
        
        String edOrg = attributes.getFirst("edOrg");
        
        // TODO: This is a temporary hack because of how we're storing the edOrg in LDAP.
        // Once we have a dedicated edOrg attribute, we can strip out this part
        if (edOrg != null && edOrg.indexOf(' ') > -1) {
            edOrg = edOrg.substring(0, edOrg.indexOf(' '));
        }
        principal.setEdOrg(edOrg);
        
        return Response.temporaryRedirect(URI.create(redirect)).build();
    }
    
    private Entity fetchOne(String collection, NeutralQuery neutralQuery) {
        Iterable<Entity> results = repo.findAll(collection, neutralQuery);
        
        if (!results.iterator().hasNext()) {
            throw new RuntimeException("Not found");
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
    public Response getMetadata() {
        
        if (!metadata.isEmpty()) {
            return Response.ok(metadata).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
        
    }
}
