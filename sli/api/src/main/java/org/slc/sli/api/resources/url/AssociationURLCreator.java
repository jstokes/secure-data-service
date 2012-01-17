package org.slc.sli.api.resources.url;

import static org.slc.sli.api.resources.util.ResourceConstants.RESOURCE_PATH_AGG;
import static org.slc.sli.api.resources.util.ResourceConstants.ENTITY_ID_MAPPINGS;
import static org.slc.sli.api.resources.util.ResourceConstants.RESOURCE_PATH_MAPPINGS;
import static org.slc.sli.api.resources.util.ResourceConstants.ASSOC_ENTITY_NAME_MAPPINGS;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.UriInfo;

import org.slc.sli.api.config.AssociationDefinition;
import org.slc.sli.api.config.EntityDefinition;
import org.slc.sli.api.representation.EmbeddedLink;
import org.slc.sli.api.resources.util.ResourceUtil;
import org.slc.sli.domain.Entity;

/**
 * Creates URL sets for associations.
 * Return URL format /agg/district/{JUUID}, /agg/school/{JUUID}
 * 
 * @author srupasinghe
 * 
 */
public class AssociationURLCreator extends URLCreator {
    @Override
    /**
     * Returns a list association links for the logged in user
     */
    public List<EmbeddedLink> getUrls(final UriInfo uriInfo, Map<String, String> params) {
        List<EmbeddedLink> results = new ArrayList<EmbeddedLink>();
        
        // remove user's type from map
        String userId = params.remove("id");
        String userType = params.remove("type");
        //clear the map
        params.clear();
        
        //get the userType entity definition
        EntityDefinition eDef = store.lookupByEntityType(userType);
        
        //get the association links
        getAssociationUrls(eDef, userId, results, uriInfo);
        
        return results;
    }
    
    /**
     * Builds the association urls for the given entity definition
     * @param entityDef The definition to build the association urls
     * @param entityId The id of the entity
     * @param urls The association urls
     * @param uriInfo The context
     */
    protected void getAssociationUrls(EntityDefinition entityDef, String entityId, List<EmbeddedLink> urls, final UriInfo uriInfo) {          
        //get the association definitions
        Collection<AssociationDefinition> associations = store.getLinked(entityDef);
        
        for (AssociationDefinition assoc : associations) {            
            if (assoc.getSourceEntity().getType().equals(entityDef.getType())) {
                
                //build the param map
                Map<String, String> params = new HashMap<String, String>();
                params.put(ENTITY_ID_MAPPINGS.get(entityDef.getType()), entityId);
                
                //get the actual associations
                Iterable<Entity> entityList = repo.findByFields(ASSOC_ENTITY_NAME_MAPPINGS.get(assoc.getType()), params);
                
                for (Entity e : entityList) { 
                    //add the link to the list
                    urls.add(new EmbeddedLink(ResourceUtil.LINKS, e.getType(), 
                            uriInfo.getBaseUriBuilder().path(RESOURCE_PATH_AGG).path(RESOURCE_PATH_MAPPINGS.get(assoc.getTargetEntity().getType())).
                            path((String) e.getBody().get(ENTITY_ID_MAPPINGS.get(assoc.getTargetEntity().getType()))).build().toString()));
                    
                    //try and get the associations under the entity                    
                    getAssociationUrls(assoc.getTargetEntity(), (String) e.getBody().get(ENTITY_ID_MAPPINGS.get(assoc.getTargetEntity().getType())), urls, uriInfo);
                }
            }
        }
    }
    
}
