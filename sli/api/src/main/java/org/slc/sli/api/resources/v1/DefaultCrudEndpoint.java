package org.slc.sli.api.resources.v1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import org.slc.sli.api.config.EntityDefinition;
import org.slc.sli.api.config.EntityDefinitionStore;
import org.slc.sli.api.representation.EntityBody;
import org.slc.sli.api.representation.ErrorResponse;
import org.slc.sli.api.resources.util.ResourceConstants;
import org.slc.sli.api.resources.util.ResourceUtil;
import org.slc.sli.api.service.EntityService;
import org.slc.sli.api.service.query.ApiQuery;
import org.slc.sli.domain.NeutralCriteria;
import org.slc.sli.domain.NeutralQuery;

/**
 * Prototype new api end points and versioning base class
 * 
 * @author srupasinghe
 * @author kmyers
 * 
 */
@Component
@Scope("request")
public class DefaultCrudEndpoint implements CrudEndpoint {
    /* The maximum number of values allowed in a comma separated string */
    public static final int MAX_MULTIPLE_UUIDS = 100;
    
    /* Access to entity definitions */
    private final EntityDefinitionStore entityDefs;
    
    private final String typeName;
    
    /* Logger utility to use to output debug, warning, or other messages to the "console" */
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultCrudEndpoint.class);
    
    /**
     * Encapsulates each ReST method's logic to allow for less duplication of precondition and
     * exception handling code.
     */
    protected static interface ResourceLogic {
        public Response run(EntityDefinition entityDef);
    }
    
    /**
     * Constructor.
     * 
     * @param entityDefs
     *            access to entity definitions
     */
    @Deprecated
    public DefaultCrudEndpoint(EntityDefinitionStore entityDefs) {
        this(entityDefs, "");
    }
    
    /**
     * Constructor.
     * 
     * @param entityDefs
     *            access to entity definitions
     * @param logger
     *            Logger utility to use to output debug, warning, or other messages to the "console"
     */
    public DefaultCrudEndpoint(final EntityDefinitionStore entityDefs, String typeName) {
        if (entityDefs == null) {
            throw new NullPointerException("entityDefs");
        }
        if (typeName == null) {
            throw new NullPointerException("typeName");
        }
        this.entityDefs = entityDefs;
        this.typeName = typeName;
    }
    
    /**
     * Creates a new entity in a specific location or collection.
     * 
     * @param collectionName
     *            where the entity should be located
     * @param newEntityBody
     *            new map of keys/values for entity
     * @param headers
     *            HTTP header information (which includes request headers)
     * @param uriInfo
     *            URI information including path and query parameters
     * @return resulting status from request
     */
    @Override
    public Response create(final String collectionName, final EntityBody newEntityBody, final HttpHeaders headers,
            final UriInfo uriInfo) {
        return handle(collectionName, entityDefs, new ResourceLogic() {
            @Override
            public Response run(EntityDefinition entityDef) {
                String id = entityDef.getService().create(newEntityBody);
                String uri = ResourceUtil.getURI(uriInfo, PathConstants.V1,
                        PathConstants.TEMP_MAP.get(entityDef.getResourceName()), id).toString();
                return Response.status(Status.CREATED).header("Location", uri).build();
            }
        });
    }
    
    /**
     * Reads one or more entities from a specific location or collection.
     * 
     * @param resourceName
     *            where the entity should be located
     * @param key
     *            field to be queried against
     * @param value
     *            comma separated list of values to be found in the key
     * @param headers
     *            HTTP header information (which includes request headers)
     * @param uriInfo
     *            URI information including path and query parameters
     * @return requested information or error status
     */
    @Override
    public Response read(final String resourceName, final String key, final String value, final HttpHeaders headers,
            final UriInfo uriInfo) {
        // /v1/entity/{id}/associations
        return handle(resourceName, entityDefs, new ResourceLogic() {
            @Override
            public Response run(final EntityDefinition entityDef) {
                LOGGER.debug("Attempting to read from {} where {} = {}",
                        new Object[] { entityDef.getStoredCollectionName(), key, value });
                
                NeutralQuery neutralQuery = new ApiQuery(uriInfo);
                List<String> valueList = Arrays.asList(value.split(","));
                neutralQuery.addCriteria(new NeutralCriteria(key, "in", valueList));
                
                // a new list to store results
                List<EntityBody> results = new ArrayList<EntityBody>();
                
                // list all entities matching query parameters and iterate over results
                for (EntityBody entityBody : entityDef.getService().list(neutralQuery)) {
                    entityBody.put(ResourceConstants.LINKS, ResourceUtil.getAssociationAndReferenceLinksForEntity(
                            entityDefs, entityDef, entityBody, uriInfo));
                    // add entity to resulting response
                    results.add(entityBody);
                }
                
                long pagingHeaderTotalCount = getTotalCount(entityDef.getService(), neutralQuery);
                return addPagingHeaders(Response.ok(results), pagingHeaderTotalCount, uriInfo).build();
            }
        });
    }
    
    /**
     * Searches "resourceName" for entries where "key" equals "value", then for each result
     * uses "idkey" field's value to query "resolutionResourceName" against the ID field.
     * 
     * @param resourceName
     *            where the entity should be located
     * @param key
     *            field to be queried against (when searching resources)
     * @param value
     *            comma separated list of expected values to be found for the key
     * @param idKey
     *            field in resource that contains the ID to be resolved
     * @param resolutionResourceName
     *            where to query for the entity with the ID taken from the "idKey" field
     * @param headers
     *            HTTP header information (which includes request headers)
     * @param uriInfo
     *            URI information including path and query parameters
     * @return requested information or error status
     */
    @Override
    public Response read(final String resourceName, final String key, final String value, final String idKey,
            final String resolutionResourceName, final HttpHeaders headers, final UriInfo uriInfo) {
        // /v1/entity/{id}/associations/entity
        return handle(resourceName, entityDefs, new ResourceLogic() {
            @Override
            public Response run(final EntityDefinition entityDef) {
                // look up information on association
                EntityDefinition endpointEntity = entityDefs.lookupByResourceName(resolutionResourceName);
                String resource1 = entityDef.getStoredCollectionName();
                String resource2 = endpointEntity.getStoredCollectionName();
                
                // write some information to debug
                LOGGER.debug("Attempting to list from {} where {} = {}", new Object[] {resource1, key, value});
                LOGGER.debug("Then for each result, ");
                LOGGER.debug(" going to read from {} where \"_id\" = {}.{}", new Object[] {
                        resource2, resource1, idKey});
                
                NeutralQuery endpointNeutralQuery = new ApiQuery(uriInfo);
                NeutralQuery associationNeutralQuery = createAssociationNeutralQuery(endpointNeutralQuery, key, value,
                        idKey);
                
                // final/resulting information
                List<EntityBody> finalResults = new ArrayList<EntityBody>();
                
                List<String> ids = new ArrayList<String>();
                // for each association
                for (EntityBody entityBody : entityDef.getService().list(associationNeutralQuery)) {
                    ids.add((String) entityBody.get(idKey));
                }
                
                if (ids.size() == 0) {
                    return Response.ok(finalResults).build();
                }
                
                endpointNeutralQuery.addCriteria(new NeutralCriteria("_id", "in", ids));
                for (EntityBody result : endpointEntity.getService().list(endpointNeutralQuery)) {
                    result.put(
                            ResourceConstants.LINKS,
                            ResourceUtil.getAssociationAndReferenceLinksForEntity(entityDefs,
                                    entityDefs.lookupByResourceName(resolutionResourceName), result, uriInfo));
                    finalResults.add(result);
                }
                
                long pagingHeaderTotalCount = getTotalCount(endpointEntity.getService(), endpointNeutralQuery);
                return addPagingHeaders(Response.ok(finalResults), pagingHeaderTotalCount, uriInfo).build();
            }
        });
    }
    
    /**
     * Reads one or more entities from a specific location or collection.
     * 
     * @param resourceName
     *            where the entity should be located
     * @param idList
     *            a single ID or a comma separated list of IDs
     * @param headers
     *            HTTP header information (which includes request headers)
     * @param uriInfo
     *            URI information including path and query parameters
     * @return requested information or error status
     */
    @Override
    public Response read(final String resourceName, final String idList, final HttpHeaders headers,
            final UriInfo uriInfo) {
        // /v1/entity/{id}
        return handle(resourceName, entityDefs, new ResourceLogic() {
            @Override
            public Response run(EntityDefinition entityDef) {
                int idLength = idList.split(",").length;
                
                if (idLength > DefaultCrudEndpoint.MAX_MULTIPLE_UUIDS) {
                    Status errorStatus = Status.PRECONDITION_FAILED;
                    String errorMessage = "Too many GUIDs: " + idLength + " (input) vs "
                            + DefaultCrudEndpoint.MAX_MULTIPLE_UUIDS + " (allowed)";
                    return Response
                            .status(errorStatus)
                            .entity(new ErrorResponse(errorStatus.getStatusCode(), errorStatus.getReasonPhrase(),
                                    errorMessage)).build();
                }
                
                List<String> ids = new ArrayList<String>();
                
                for (String id : idList.split(",")) {
                    ids.add(id);
                }
                
                NeutralQuery neutralQuery = new ApiQuery(uriInfo);
                neutralQuery.addCriteria(new NeutralCriteria("_id", "in", ids));
                
                // final/resulting information
                List<EntityBody> finalResults = new ArrayList<EntityBody>();
                
                Iterable<EntityBody> entities;
                if (idLength == 1) {
                    entities = Arrays.asList(new EntityBody[] { entityDef.getService().get(idList, neutralQuery) });
                } else {
                    entities = entityDef.getService().list(neutralQuery);
                }
                
                for (EntityBody result : entities) {
                    if (result != null) {
                        result.put(ResourceConstants.LINKS, ResourceUtil.getAssociationAndReferenceLinksForEntity(
                                entityDefs, entityDef, result, uriInfo));
                    }
                    finalResults.add(result);
                }
                
                // Return results as an array if multiple IDs were requested (comma separated list),
                // single entity otherwise
                if (finalResults.isEmpty()) {
                    return Response.status(Status.NOT_FOUND).build();
                } else if (finalResults.size() == 1) {
                    return addPagingHeaders(Response.ok(finalResults.get(0)), 1, uriInfo).build();
                } else {
                    long pagingHeaderTotalCount = getTotalCount(entityDef.getService(), neutralQuery);
                    return addPagingHeaders(Response.ok(finalResults), pagingHeaderTotalCount, uriInfo).build();
                }
            }
        });
    }
    
    /**
     * Deletes a given entity from a specific location or collection.
     * 
     * @param resourceName
     *            where the entity should be located
     * @param id
     *            ID of object being deleted
     * @param headers
     *            HTTP header information (which includes request headers)
     * @param uriInfo
     *            URI information including path and query parameters
     * @return resulting status from request
     */
    @Override
    public Response delete(final String resourceName, final String id, final HttpHeaders headers, final UriInfo uriInfo) {
        return handle(resourceName, entityDefs, new ResourceLogic() {
            @Override
            public Response run(final EntityDefinition entityDef) {
                entityDef.getService().delete(id);
                return Response.status(Status.NO_CONTENT).build();
            }
        });
    }
    
    /**
     * Updates a given entity in a specific location or collection.
     * 
     * @param resourceName
     *            where the entity should be located
     * @param id
     *            ID of object being updated
     * @param newEntityBody
     *            new map of keys/values for entity
     * @param headers
     *            HTTP header information (which includes request headers)
     * @param uriInfo
     *            URI information including path and query parameters
     * @return resulting status from request
     */
    @Override
    public Response update(final String resourceName, final String id, final EntityBody newEntityBody,
            final HttpHeaders headers, final UriInfo uriInfo) {
        return handle(resourceName, entityDefs, new ResourceLogic() {
            @Override
            public Response run(EntityDefinition entityDef) {
                EntityBody copy = new EntityBody(newEntityBody);
                copy.remove(ResourceConstants.LINKS);
                LOGGER.debug("updating entity {}", copy);
                entityDef.getService().update(id, copy);
                LOGGER.debug("updating entity {}", copy);
                return Response.status(Status.NO_CONTENT).build();
            }
        });
    }
    
    /**
     * Reads all entities from a specific location or collection.
     * 
     * @param collectionName
     *            where the entity should be located
     * @param headers
     *            HTTP header information (which includes request headers)
     * @param uriInfo
     *            URI information including path and query parameters
     * @return requested information or error status
     */
    @Override
    public Response readAll(final String collectionName, final HttpHeaders headers, final UriInfo uriInfo) {
        return handle(collectionName, entityDefs, new ResourceLogic() {
            // v1/entity
            @Override
            public Response run(final EntityDefinition entityDef) {
                // final/resulting information
                List<EntityBody> results = new ArrayList<EntityBody>();
                
                for (EntityBody entityBody : entityDef.getService().list(new ApiQuery(uriInfo))) {
                    // if links should be included then put them in the entity body
                    entityBody.put(ResourceConstants.LINKS, ResourceUtil.getAssociationAndReferenceLinksForEntity(
                            entityDefs, entityDef, entityBody, uriInfo));
                    results.add(entityBody);
                }
                
                long pagingHeaderTotalCount = getTotalCount(entityDef.getService(), new ApiQuery(uriInfo));
                return addPagingHeaders(Response.ok(results), pagingHeaderTotalCount, uriInfo).build();
            }
        });
    }
    
    /**
     * Returns the sub-resource responsible for responding to requests for custom entity data
     */
    @Path("{id}/" + PathConstants.CUSTOM_ENTITIES)
    @Produces({ MediaType.APPLICATION_JSON, HypermediaType.VENDOR_SLC_JSON })
    @Override
    public CustomEntityResource getCustomEntityResource(@PathParam("id") String id) {
        EntityDefinition entityDef = entityDefs.lookupByResourceName(this.typeName);
        return new CustomEntityResource(id, entityDef);
    }
    
    /* Utility methods */
    
    protected static long getTotalCount(EntityService basicService, NeutralQuery neutralQuery) {
        
        if (basicService == null) {
            return 0;
        }
        
        if (neutralQuery == null) {
            return basicService.count(new NeutralQuery());
        }
        
        int originalLimit = neutralQuery.getLimit();
        int originalOffset = neutralQuery.getOffset();
        neutralQuery.setLimit(0);
        neutralQuery.setOffset(0);
        long count = basicService.count(neutralQuery);
        neutralQuery.setLimit(originalLimit);
        neutralQuery.setOffset(originalOffset);
        return count;
    }
    
    /**
     * Handle preconditions and exceptions.
     */
    private static Response handle(final String resourceName, final EntityDefinitionStore entityDefs,
            final ResourceLogic logic) {
        EntityDefinition entityDef = entityDefs.lookupByResourceName(resourceName);
        if (entityDef == null) {
            return Response
                    .status(Status.NOT_FOUND)
                    .entity(new ErrorResponse(Status.NOT_FOUND.getStatusCode(), Status.NOT_FOUND.getReasonPhrase(),
                            "Invalid resource path: " + resourceName)).build();
        }
        return logic.run(entityDef);
    }
    
    /**
     * Creates a query that looks up an association where key = value and only returns the specified
     * field.
     * A convenience method for querying for associations when resolving their endpoints.
     * 
     * @param endpointNeutralQuery
     * @param key
     * @param value
     *            a comma separated list of values
     * @param includeField
     * @return
     */
    private NeutralQuery createAssociationNeutralQuery(NeutralQuery endpointNeutralQuery, String key, String value,
            String includeField) {
        NeutralQuery neutralQuery = new NeutralQuery();
        List<String> list = new ArrayList<String>(Arrays.asList(value.split(",")));
        neutralQuery.addCriteria(new NeutralCriteria(key, NeutralCriteria.CRITERIA_IN, list));
        neutralQuery.setIncludeFields(includeField);
        return neutralQuery;
    }
    
    private Response.ResponseBuilder addPagingHeaders(Response.ResponseBuilder resp, long total, UriInfo info) {
        if (info != null && resp != null) {
            NeutralQuery neutralQuery = new ApiQuery(info);
            int offset = neutralQuery.getOffset();
            int limit = neutralQuery.getLimit();
            
            int nextStart = offset + limit;
            if (nextStart < total) {
                neutralQuery.setOffset(nextStart);
                
                String nextLink = info.getRequestUriBuilder().replaceQuery(neutralQuery.toString()).build().toString();
                resp.header(ParameterConstants.HEADER_LINK, "<" + nextLink + ">; rel=next");
            }
            
            if (offset > 0) {
                int prevStart = Math.max(offset - limit, 0);
                neutralQuery.setOffset(prevStart);
                
                String prevLink = info.getRequestUriBuilder().replaceQuery(neutralQuery.toString()).build().toString();
                resp.header(ParameterConstants.HEADER_LINK, "<" + prevLink + ">; rel=prev");
            }
            
            resp.header(ParameterConstants.HEADER_TOTAL_COUNT, total);
        }
        
        return resp;
    }

    /**
     * Returns all entities for which the logged in User has permission and context.
     * 
     * @param offset
     *            starting position in results to return to user
     * @param limit
     *            maximum number of results to return to user (starting from offset)
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *            URI information including path and query parameters
     * @return result of CRUD operation
     */
    public Response readAll(final int offset, final int limit, HttpHeaders headers, final UriInfo uriInfo) {
        ResourceUtil.putValue(headers.getRequestHeaders(), ParameterConstants.LIMIT, limit);
        ResourceUtil.putValue(headers.getRequestHeaders(), ParameterConstants.OFFSET, offset);
        return this.readAll(typeName, headers, uriInfo);
    }

    /**
     * Create a new entity.
     * 
     * @param newEntityBody
     *            entity data
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *              URI information including path and query parameters
     * @return result of CRUD operation
     * @response.param {@name Location} {@style header} {@type
     *                 {http://www.w3.org/2001/XMLSchema}anyURI} {@doc The URI where the created
     *                 item is accessable.}
     */
    public Response create(final EntityBody newEntityBody, HttpHeaders headers, final UriInfo uriInfo) {
        return this.create(typeName, newEntityBody, headers, uriInfo);
    }

    /**
     * Get a single entity
     * 
     * @param id
     *            The Id of the entity
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *            URI information including path and query parameters
     * @return A single entity
     */
    public Response read(final String id, HttpHeaders headers, final UriInfo uriInfo) {
        return this.read(typeName, id, headers, uriInfo);
    }

    /**
     * Delete a entity
     * 
     * @param id
     *            The Id of the entity
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *            URI information including path and query parameters
     * @return Returns a NOT_CONTENT status code
     * @response.representation.204.mediaType HTTP headers with a Not-Content status code.
     */
    public Response delete(final String id, HttpHeaders headers, final UriInfo uriInfo) {
        return this.delete(typeName, id, headers, uriInfo);
    }

    /**
     * Update an existing entity.
     * 
     * @param id
     *            The id of the entity
     * @param newEntityBody
     *            entity data
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *            URI information including path and query parameters
     * @return Response with a NOT_CONTENT status code
     * @response.representation.204.mediaType HTTP headers with a Not-Content status code.
     */
    public Response update(final String id, final EntityBody newEntityBody, HttpHeaders headers, final UriInfo uriInfo) {
        return this.update(typeName, id, newEntityBody, headers, uriInfo);
    }
}
