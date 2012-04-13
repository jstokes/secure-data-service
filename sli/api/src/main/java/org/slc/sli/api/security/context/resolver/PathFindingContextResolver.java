/**
 * 
 */
package org.slc.sli.api.security.context.resolver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slc.sli.api.config.AssociationDefinition;
import org.slc.sli.api.config.EntityDefinitionStore;
import org.slc.sli.api.security.context.AssociativeContextHelper;
import org.slc.sli.api.security.context.traversal.BrutePathFinder;
import org.slc.sli.api.security.context.traversal.graph.SecurityNode;
import org.slc.sli.domain.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Class to adapt our path finding technique to our context resolving system.
 * 
 * @author rlatta
 */
@Component
public class PathFindingContextResolver implements EntityContextResolver {
    
    @Autowired
    private BrutePathFinder pathFinder;
    
    @Autowired
    private AssociativeContextHelper helper;
    
    @Autowired
    private EntityDefinitionStore store;

    private String fromEntity;
    private String toEntity;
    
    /*
     * @see
     * org.slc.sli.api.security.context.resolver.EntityContextResolver#canResolve(java.lang.String,
     * java.lang.String)
     */
    @Override
    public boolean canResolve(String fromEntityType, String toEntityType) {
        this.fromEntity = fromEntityType;
        this.toEntity = toEntityType;
        Set<String> entities = pathFinder.getNodeMap().keySet();
        return (entities.contains(fromEntityType) && entities.contains(toEntityType));
    }
    
    /*
     * @see
     * org.slc.sli.api.security.context.resolver.EntityContextResolver#findAccessible(org.slc.sli
     * .domain.Entity)
     */
    @Override
    public List<String> findAccessible(Entity principal) {
        List<SecurityNode> path = new ArrayList<SecurityNode>();
        path = pathFinder.getPreDefinedPath(fromEntity, toEntity);
        if (path.size() == 0) {
            path = pathFinder.find(fromEntity, toEntity);
        }
        List<String> ids = new ArrayList<String>(Arrays.asList(principal.getEntityId()));
        SecurityNode current = path.get(0);
        for (int i = 1; i< path.size(); ++i) {
            SecurityNode next = path.get(i);
            Map<String, String> connection = current.getConnectionForEntity(next.getName());
            Iterable<String> idSet = new ArrayList<String>();
            String repoName = getResourceName(next, connection);
            debug("Getting Ids From {}", repoName);
            if (isAssociative(next, connection)) {
                AssociationDefinition ad = (AssociationDefinition) store.lookupByEntityType(repoName);
                List<String> keys = helper.getAssocKeys(current.getName(), ad);
                debug("Association keys found {}", keys.toString());
                idSet = helper.findEntitiesContainingReference(repoName, keys.get(0),
                        connection.get(SecurityNode.CONNECTION_FIELD_NAME), ids);
            } else {
                idSet = helper.findEntitiesContainingReference(repoName,
                        connection.get(SecurityNode.CONNECTION_FIELD_NAME), ids);
            }

            fixIds(ids, idSet);
            current = path.get(i);
        }
        debug("We found {} ids", ids);
        return ids;
    }

    private boolean isAssociative(SecurityNode next, Map<String, String> connection) {
        return connection.get(SecurityNode.CONNECTION_ASSOCIATION).length() != 0;
    }

    private String getResourceName(SecurityNode next, Map<String, String> connection) {
        return isAssociative(next, connection) ? (String) connection
                .get(SecurityNode.CONNECTION_ASSOCIATION) : next.getName();
    }

    private void fixIds(List<String> ids, Iterable<String> idSet) {
        ids.clear();
        for (String id : idSet) {
            ids.add(id);
        }
    }
    
    /**
     * @param pathFinder
     *            the pathFinder to set
     */
    public void setPathFinder(BrutePathFinder pathFinder) {
        this.pathFinder = pathFinder;
    }
    
    /**
     * @param helper
     *            the helper to set
     */
    public void setHelper(AssociativeContextHelper helper) {
        this.helper = helper;
    }

}
