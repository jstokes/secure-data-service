/**
 *
 */
package org.slc.sli.api.security.context.resolver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import org.slc.sli.api.config.AssociationDefinition;
import org.slc.sli.api.config.EntityDefinitionStore;
import org.slc.sli.api.security.SLIPrincipal;
import org.slc.sli.api.security.context.AssociativeContextHelper;
import org.slc.sli.api.security.context.traversal.BrutePathFinder;
import org.slc.sli.api.security.context.traversal.graph.SecurityNode;
import org.slc.sli.api.security.context.traversal.graph.SecurityNodeConnection;
import org.slc.sli.domain.Entity;
import org.slc.sli.domain.NeutralCriteria;
import org.slc.sli.domain.NeutralQuery;
import org.slc.sli.domain.Repository;

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

    @Autowired
    private Repository<Entity> repo;
    
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
        if (pathFinder.isPathExcluded(fromEntityType, toEntityType)) {
            return false;
        }
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
        /*if (principal.getBody().get("staffUniqueStateId").equals("demo")) {
            info("Resolver override for demo user.");
            return AllowAllEntityContextResolver.SUPER_LIST;
        }*/

        List<SecurityNode> path = new ArrayList<SecurityNode>();
        path = pathFinder.getPreDefinedPath(fromEntity, toEntity);
        if (path.size() == 0) {
            path = pathFinder.find(fromEntity, toEntity);
        }

        Set<String> ids = new HashSet<String>(Arrays.asList(principal.getEntityId()));
        SecurityNode current = path.get(0);
        for (int i = 1; i < path.size(); ++i) {
            SecurityNode next = path.get(i);
            SecurityNodeConnection connection = current.getConnectionForEntity(next.getName());
            List<String> idSet = new ArrayList<String>();
            String repoName = getResourceName(next, connection);
            debug("Getting Ids From {}", repoName);
            if (isAssociative(next, connection)) {
                AssociationDefinition ad = (AssociationDefinition) store.lookupByResourceName(repoName);
                List<String> keys = helper.getAssocKeys(current.getName(), ad);
                idSet = helper.findEntitiesContainingReference(ad.getStoredCollectionName(), keys.get(0),
                        connection.getFieldName(), new ArrayList<String>(ids));
            } else if (!isAssociative(next, connection) && connection.getAssociationNode().length() != 0) {
                idSet = helper.findEntitiesContainingReference(repoName, "_id", connection.getFieldName(),
                        new ArrayList<String>(ids));

            } else {
                idSet = helper.findEntitiesContainingReference(repoName, connection.getFieldName(),
                        new ArrayList<String>(ids));

            }

            if (connection.getFilter() != null) {
                idSet = connection.getFilter().filterIds(idSet);
            }
           // ids.clear();
            ids.addAll(idSet);
            current = path.get(i);
        }
        debug("We found {} ids", ids);
        
        //  Allow creator access
        ids.addAll(getAllowedForCreator());
        return new ArrayList<String>(ids);
    }

    private List<String> getAllowedForCreator() {
        SLIPrincipal user = (SLIPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = user.getEntity().getEntityId();
        NeutralQuery nq = new NeutralQuery(new NeutralCriteria("metadata.createdBy", "=", userId, false));
        nq.addCriteria(new NeutralCriteria("metadata.isOrphaned", "=", "true", false));
        List<String> createdIds = (List<String>) repo.findAllIds(toEntity, nq);
        
        return createdIds;
    }

    private boolean isAssociative(SecurityNode next, SecurityNodeConnection connection) {
        return connection.getAssociationNode().length() != 0 && connection.getAssociationNode().endsWith("ssociations");
    }

    private String getResourceName(SecurityNode next, SecurityNodeConnection connection) {
        return connection.getAssociationNode().length() != 0 ? connection.getAssociationNode() : next.getName();
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
