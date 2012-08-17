package org.slc.sli.api.resources.generic.service;

import org.codehaus.plexus.util.StringUtils;
import org.slc.sli.api.config.EntityDefinition;
import org.slc.sli.api.config.EntityDefinitionStore;
import org.slc.sli.api.constants.ResourceConstants;
import org.slc.sli.api.model.ModelProvider;
import org.slc.sli.api.representation.EntityBody;
import org.slc.sli.api.resources.generic.PreConditionFailedException;
import org.slc.sli.api.resources.util.ResourceUtil;
import org.slc.sli.api.selectors.LogicalEntity;
import org.slc.sli.api.selectors.UnsupportedSelectorException;
import org.slc.sli.api.service.query.ApiQuery;
import org.slc.sli.api.service.query.UriInfoToApiQueryConverter;
import org.slc.sli.api.util.SecurityUtil;
import org.slc.sli.domain.NeutralCriteria;
import org.slc.sli.api.resources.generic.util.ResourceHelper;
import org.slc.sli.domain.NeutralQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Default implementation of the resource service.
 *
 * @author srupasinghe
 */

@Component
public class DefaultResourceService implements ResourceService {

    @Autowired
    private EntityDefinitionStore entityDefinitionStore;

    @Autowired
    private LogicalEntity logicalEntity;

    @Autowired
    private ModelProvider provider;

    @Autowired
    private ResourceHelper resourceHelper;

    public static final int MAX_MULTIPLE_UUIDS = 100;

    @Override
    public List<EntityBody> getEntitiesByIds(final String resource, final String idList, final UriInfo uriInfo) {
        EntityDefinition definition = getEntityDefinition(resource);
        final int idLength = idList.split(",").length;

        if (idLength > MAX_MULTIPLE_UUIDS) {
            String errorMessage = "Too many GUIDs: " + idLength + " (input) vs "
                    + MAX_MULTIPLE_UUIDS + " (allowed)";
            throw new PreConditionFailedException(errorMessage);
        }

        final List<String> ids = Arrays.asList(StringUtils.split(idList));

        ApiQuery apiQuery = getApiQuery(definition, uriInfo);

        apiQuery.addCriteria(new NeutralCriteria("_id", "in", ids));
        apiQuery.setLimit(0);
        apiQuery.setOffset(0);

        // final/resulting information
        List<EntityBody> finalResults = null;
        try {
            finalResults = logicalEntity.getEntities(apiQuery, resource);
        } catch (UnsupportedSelectorException e) {
            finalResults = (List<EntityBody>) definition.getService().list(apiQuery);
        }

        return finalResults;
    }

    @Override
    public List<EntityBody> getEntities(final String resource, final UriInfo uriInfo) {
        EntityDefinition definition = getEntityDefinition(resource);

        List<EntityBody> results = new ArrayList<EntityBody>();

        Iterable<EntityBody> entityBodies = null;
        final ApiQuery apiQuery = getApiQuery(definition, uriInfo);

        if (shouldReadAll()) {
            entityBodies = SecurityUtil.sudoRun(new SecurityUtil.SecurityTask<Iterable<EntityBody>>() {

                @Override
                public Iterable<EntityBody> execute() {
                    return logicalEntity.getEntities(apiQuery, new Constraint(), resource);
                }
            });
        } else {
            try {
                entityBodies = logicalEntity.getEntities(apiQuery, new Constraint(), resource);
            } catch (UnsupportedSelectorException e) {
                entityBodies = definition.getService().list(apiQuery);
            }
        }
        for (EntityBody entityBody : entityBodies) {

            // if links should be included then put them in the entity body
            entityBody.put(ResourceConstants.LINKS,
                    ResourceUtil.getLinks(entityDefinitionStore, definition, entityBody, uriInfo));

            results.add(entityBody);
        }

        return results;
    }

    protected boolean shouldReadAll() {
        return false;
    }

    protected ApiQuery addTypeCriteria(EntityDefinition entityDefinition, ApiQuery apiQuery) {

        if (apiQuery != null && entityDefinition != null
                && !entityDefinition.getType().equals(entityDefinition.getStoredCollectionName())) {
            apiQuery.addCriteria(new NeutralCriteria("type", NeutralCriteria.CRITERIA_IN, Arrays.asList(entityDefinition
                    .getType()), false));
        }

        return apiQuery;
    }

    @Override
    public long getEntityCount(String resource, final UriInfo uriInfo) {
        EntityDefinition definition = getEntityDefinition(resource);
        ApiQuery apiQuery = getApiQuery(definition, uriInfo);

        if (definition.getService() == null) {
            return 0;
        }

        if (apiQuery == null) {
            return definition.getService().count(new NeutralQuery());
        }

        int originalLimit = apiQuery.getLimit();
        int originalOffset = apiQuery.getOffset();
        apiQuery.setLimit(0);
        apiQuery.setOffset(0);
        long count = definition.getService().count(apiQuery);
        apiQuery.setLimit(originalLimit);
        apiQuery.setOffset(originalOffset);
        return count;
    }

    protected ApiQuery getApiQuery(EntityDefinition definition, final UriInfo uriInfo) {
        ApiQuery apiQuery = new ApiQuery(uriInfo);
        addTypeCriteria(definition, apiQuery);

        return apiQuery;
    }

    @Override
    public String postEntity(final String resource, EntityBody entity) {
        EntityDefinition definition = getEntityDefinition(resource);

        return definition.getService().create(entity);
    }

    public EntityDefinition getEntityDefinition(final String resource) {
        //FIXME TODO
        final String resourceType = resource.split("/")[1];
        return entityDefinitionStore.lookupByResourceName(resourceType);
    }

    @Override
    public String getEntityType(String resource) {
        return entityDefinitionStore.lookupByResourceName(resource).getType();
    }

    @Override
    public List<EntityBody> getEntities(String base, String id, String resource, UriInfo uriInfo) {
        EntityDefinition definition = getEntityDefinition(resource);
        List<EntityBody> results = new ArrayList<EntityBody>();
        List<EntityBody> entityBodyList = null;
        final ApiQuery apiQuery = getApiQuery(definition,uriInfo);
        try {
            entityBodyList = logicalEntity.getEntities(apiQuery, definition.getResourceName());
        }   catch (UnsupportedSelectorException e) {
            entityBodyList = (List<EntityBody>) definition.getService().list(apiQuery);
        }
        for (EntityBody entityBody : entityBodies) {

            // if links should be included then put them in the entity body
            entityBody.put(ResourceConstants.LINKS,
                    ResourceUtil.getLinks(entityDefinitionStore, definition, entityBody, uriInfo));

            results.add(entityBody);
        }

        return results;
    }

}
