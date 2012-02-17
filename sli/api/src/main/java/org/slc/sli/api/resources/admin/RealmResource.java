package org.slc.sli.api.resources.admin;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.slc.sli.api.config.EntityDefinition;
import org.slc.sli.api.config.EntityDefinitionStore;
import org.slc.sli.api.representation.EntityBody;
import org.slc.sli.api.resources.Resource;
import org.slc.sli.api.security.resolve.IdpResolver;
import org.slc.sli.api.service.EntityService;
import org.slc.sli.api.util.SecurityUtil;
import org.slc.sli.api.util.SecurityUtil.SecurityTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Realm handling api
 * Used by realm selector to present user with choices and given
 * User selection, provides URL of where SSO can be initiated
 *
 * @author dkornishev
 *
 *
 */
@Component
@Path("/pub")
@Scope("request")
@Produces({ Resource.JSON_MEDIA_TYPE })
public class RealmResource implements IdpResolver {

    private static final Logger   LOG        = LoggerFactory.getLogger(RealmResource.class);
    private static final int      MAX_REALMS = 9999;

    @Autowired
    private EntityDefinitionStore store;
    @Autowired
    @Value("${sli.security.sso.url}")
    private String                ssoInitUrl;

    /**
     * Provides a list of all available realms
     */
    @GET
    @Path("/realms")
    @Override
    public Set<EntityBody> getRealms() {

        Iterable<EntityBody> entities = SecurityUtil.sudoRun(new SecurityTask<Iterable<EntityBody>>() {
            @Override
            public Iterable<EntityBody> execute() {
                return getService().get(getService().list(0, MAX_REALMS));
            }
        });

        Set<EntityBody> set = new HashSet<EntityBody>();
        for (EntityBody eb : entities) {
            eb.remove("mappings");
            set.add(eb);
        }

        return set;
    }

    /**
     * Provides the SSO initiation url given the idp of the user
     *
     * @param realmId
     *            id of the user preferred idp
     */
    @GET
    @Path("/realms/ssoInit")
    @Override
    public String getSsoInitUrl(@QueryParam("id") final String realmId) {

        String idp = SecurityUtil.sudoRun(new SecurityTask<String>() {

            @SuppressWarnings("unchecked")
			@Override
            public String execute() {
                EntityBody eb = getService().get(realmId);

                if (eb == null) {
                    throw new IllegalArgumentException("Couldn't locate idp for realm: " + realmId);
                }

                Map<String,String> idpData = (Map<String, String>) eb.get("idp");
				return (String) idpData.get("id");
            }
        });

        if (idp == null) {
            throw new IllegalArgumentException("Realm " + realmId + " doesn't have an idp");
        }

        String idpId = null;

        try {
            idpId = URLEncoder.encode(idp, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            LOG.error("Error encoding idp id", e);
            throw new IllegalStateException("IdpId could not be encoded", e);
        }

        return this.ssoInitUrl.replaceAll("\\{idpId\\}", idpId);
    }

    private EntityService getService() {
        EntityDefinition edf = store.lookupByResourceName("realm");
        return edf.getService();
    }

    public void setStore(EntityDefinitionStore store) {
        this.store = store;
    }

}
