package org.slc.sli.api.resources.v1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import org.slc.sli.api.config.AssociationDefinition;
import org.slc.sli.api.config.EntityDefinition;
import org.slc.sli.api.config.EntityDefinitionStore;
import org.slc.sli.api.representation.EmbeddedLink;
import org.slc.sli.api.representation.EntityBody;
import org.slc.sli.api.representation.ErrorResponse;
import org.slc.sli.api.resources.util.ResourceConstants;
import org.slc.sli.api.resources.util.ResourceUtil;
import org.springframework.web.servlet.mvc.annotation.ResponseStatusExceptionResolver;

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
    private EntityDefinitionStore entityDefs;
    
    /* Logger utility to use to output debug, warning, or other messages to the "console" */
    private final Logger logger;
    
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
     * @param entityDefs access to entity definitions
     */
    @Autowired
    public DefaultCrudEndpoint(EntityDefinitionStore entityDefs) {
        this(entityDefs, LoggerFactory.getLogger(DefaultCrudEndpoint.class));
    }
    
    /**
     * Constructor.
     * 
     * @param entityDefs access to entity definitions
     * @param logger Logger utility to use to output debug, warning, or other messages to the "console"
     */
    public DefaultCrudEndpoint(final EntityDefinitionStore entityDefs, final Logger logger) {
        if (entityDefs == null) {
            throw new NullPointerException("entityDefs");
        }
        if (logger == null) {
            throw new NullPointerException("logger");
        }
        this.entityDefs = entityDefs;
        this.logger = logger;
    }
    
    /**
     * Creates a new entity in a specific location or collection.
     * 
     * @param collectionName
     *      where the entity should be located
     * @param newEntityBody 
     *      new map of keys/values for entity
     * @param headers 
     *      HTTP header information (which includes request headers) 
     * @param uriInfo 
     *      URI information including path and query parameters
     * @return resulting status from request
     */
    @Override
    public Response create(final String collectionName, final EntityBody newEntityBody, final HttpHeaders headers, final UriInfo uriInfo) {
        return handle(collectionName, entityDefs, new ResourceLogic() {
            @Override
            public Response run(EntityDefinition entityDef) {
                String id = entityDef.getService().create(newEntityBody);
                String uri = ResourceUtil.getURI(uriInfo, entityDef.getResourceName(), id).toString();
                return Response.status(Status.CREATED).header("Location", uri).build();
            }
        });
    }
    
    /**
     * Reads one or more entities from a specific location or collection.
     * 
     * @param resourceName
     *      where the entity should be located
     * @param key 
     *      field to be queried against
     * @param value 
     *      expected value to be found in the key 
     * @param headers 
     *      HTTP header information (which includes request headers) 
     * @param uriInfo 
     *      URI information including path and query parameters
     * @return requested information or error status
     */
    @Override
    public Response read(final String resourceName, final String key, final String value, final HttpHeaders headers, final UriInfo uriInfo) {
        return handle(resourceName, entityDefs, new ResourceLogic() {
            @Override
            public Response run(final EntityDefinition entityDef) {
                logger.debug("Attempting to read from " + entityDef.getStoredCollectionName() + " where " + key + " = " + value);
                //get references to query parameters
                Map<String, String> queryParameters = ResourceUtil.convertToMap(uriInfo.getQueryParameters());
                //add additional query key/value pair
                queryParameters.put(key, value);
                
                //a new list to store results
                List<EntityBody> results = new ArrayList<EntityBody>();
                boolean shouldIncludeLinks = shouldIncludeLinks(headers);
                
                //list all entities matching query parameters and iterate over results
                for (EntityBody entityBody : entityDef.getService().list(queryParameters)) {
                    //if links should be included then put them in the entity body
                    if (shouldIncludeLinks) {
                        String id = (String) entityBody.get("id");
                        entityBody.put(ResourceConstants.LINKS, getLinks(uriInfo, entityDef, id, entityBody, entityDefs));
                    }
                    //add entity to resulting response
                    results.add(entityBody);
                }
                
                //turn results into response
                return Response.ok(results).build();
            }
        });
    }
    
    /**
     * Searches "resourceName" for entries where "key" equals "value", then for each result 
     * uses "idkey" field's value to query "resolutionResourceName" against the ID field. 
     * 
     * @param resourceName
     *      where the entity should be located
     * @param key 
     *      field to be queried against (when searching resources)
     * @param value 
     *      expected value to be found in the key 
     * @param idKey 
     *      field in resource that contains the ID to be resolved
     * @param resolutionResourceName 
     *      where to query for the entity with the ID taken from the "idKey" field
     * @param headers 
     *      HTTP header information (which includes request headers) 
     * @param uriInfo 
     *      URI information including path and query parameters
     * @return requested information or error status
     */
    @Override
    public Response read(final String resourceName, final String key, final String value, final String idKey, final String resolutionResourceName, 
            final HttpHeaders headers, final UriInfo uriInfo) {
        return handle(resourceName, entityDefs, new ResourceLogic() {
            @Override
            public Response run(final EntityDefinition entityDef) {
                //look up information on association
                EntityDefinition endpointEntity = entityDefs.lookupByResourceName(resolutionResourceName);
                boolean shouldIncludeLinks = shouldIncludeLinks(headers);
                String resource1 = entityDef.getStoredCollectionName();
                String resource2 = endpointEntity.getStoredCollectionName();
                
                //write some information to debug
                logger.debug("Attempting to list from " + resource1 + " where " + key + " = " + value);
                logger.debug("Then for each result, ");
                logger.debug(" going to read from " + resource2 + " where \"_id\" = " + resource1 + "." + idKey);
                logger.debug("Final results will " + (shouldIncludeLinks ? "" : "NOT ") + "include links.");
                
                //query parameters for association and resolution lookups
                Map<String, String> queryParameters = ResourceUtil.convertToMap(uriInfo.getQueryParameters());
                Map<String, String> associationQueryParameters =
                       createAssociationQueryParameters(queryParameters, key, value, idKey);
                
                //final/resulting information
                List<EntityBody> finalResults = new ArrayList<EntityBody>();

                StringBuilder ids = new StringBuilder();
                //for each association
                for (EntityBody entityBody : entityDef.getService().list(associationQueryParameters)) {
                    ids.append((String) entityBody.get(idKey));
                    ids.append(",");
                }
                
                String entityIds = ids.toString();
                logger.debug("entityIds = " + entityIds);

                if (entityIds.length() == 0) {
                    return Response.ok(finalResults).build();
                }
                entityIds = entityIds.substring(0, entityIds.length() - 1); //remove trailing comma
                
                queryParameters.put("_id", entityIds);
                for (EntityBody result : endpointEntity.getService().list(queryParameters)) {
                    //if links should be included then put them in the entity body
                    if (shouldIncludeLinks) {
                        String id = (String) result.get("id");
                        result.put(ResourceConstants.LINKS, getLinks(uriInfo, endpointEntity, id, result, entityDefs));
                    }
                    finalResults.add(result);
                }
                
                return Response.ok(finalResults).build();
            }
        });
    }
    
    /**
     * Reads one or more entities from a specific location or collection.
     * 
     * @param resourceName
     *      where the entity should be located
     * @param idList 
     *      a single ID or a comma separated list of IDs
     * @param headers 
     *      HTTP header information (which includes request headers) 
     * @param uriInfo 
     *      URI information including path and query parameters
     * @return requested information or error status
     */
    @Override
    public Response read(final String resourceName, final String idList, final HttpHeaders headers, final UriInfo uriInfo) {
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

                Map<String, String> queryParameters = ResourceUtil.convertToMap(uriInfo.getQueryParameters());
                queryParameters.put("_id", idList);

                //final/resulting information
                List<EntityBody> finalResults = new ArrayList<EntityBody>();
                boolean shouldIncludeLinks = shouldIncludeLinks(headers);
                
                
                for (EntityBody result : entityDef.getService().list(queryParameters)) {
                    if (result != null) {
                        if (shouldIncludeLinks) {
                            result.put(ResourceConstants.LINKS, getLinks(uriInfo, entityDef, (String)
                                    result.get("id"), result, entityDefs));
                        }
                    }
                    finalResults.add(result);
                }

                // Return results as an array if multiple IDs were requested (comma separated list),
                // single entity otherwise
                if (finalResults.isEmpty()) {
                    return Response.status(Status.NOT_FOUND).build();
                } else if (finalResults.size() == 1) {
                    return Response.ok(finalResults.get(0)).build();
                } else {
                    return Response.ok(finalResults).build();
                }
            }
        });
    }
    
    /**
     * Deletes a given entity from a specific location or collection.
     * 
     * @param resourceName
     *      where the entity should be located
     * @param id
     *      ID of object being deleted
     * @param headers 
     *      HTTP header information (which includes request headers) 
     * @param uriInfo 
     *      URI information including path and query parameters
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
     *      where the entity should be located
     * @param id
     *      ID of object being updated
     * @param newEntityBody 
     *      new map of keys/values for entity
     * @param headers 
     *      HTTP header information (which includes request headers) 
     * @param uriInfo 
     *      URI information including path and query parameters
     * @return resulting status from request
     */
    @Override
    public Response update(final String resourceName, final String id, final EntityBody newEntityBody, final HttpHeaders headers,
            final UriInfo uriInfo) {
        return handle(resourceName, entityDefs, new ResourceLogic() {
            @Override
            public Response run(EntityDefinition entityDef) {
                EntityBody copy = new EntityBody(newEntityBody);
                copy.remove(ResourceConstants.LINKS);
                logger.debug("updating entity {}", copy);
                entityDef.getService().update(id, copy);
                logger.debug("updating entity {}", copy);
                return Response.status(Status.NO_CONTENT).build();
            }
        });
    }
    
    /**
     * Reads all entities from a specific location or collection.
     * 
     * @param collectionName
     *      where the entity should be located
     * @param headers 
     *      HTTP header information (which includes request headers) 
     * @param uriInfo 
     *      URI information including path and query parameters
     * @return requested information or error status
     */
    @Override
    public Response readAll(final String collectionName, final HttpHeaders headers, final UriInfo uriInfo) {
        return handle(collectionName, entityDefs, new ResourceLogic() {
            @Override
            public Response run(final EntityDefinition entityDef) {
                //final/resulting information
                List<EntityBody> results = new ArrayList<EntityBody>();
                boolean shouldIncludeLinks = shouldIncludeLinks(headers);
                
                //loop for each entity returned by performing a list operation
                for (EntityBody entityBody : entityDef.getService().list(ResourceUtil.convertToMap(uriInfo.getQueryParameters()))) {
                    //if links should be included then put them in the entity body
                    if (shouldIncludeLinks) {
                        String id = (String) entityBody.get("id");
                        entityBody.put(ResourceConstants.LINKS, getLinks(uriInfo, entityDef, id, entityBody, entityDefs));
                    }
                    results.add(entityBody);
                }
                
                return Response.ok(results).build();
            }
        });
    }
    
    /* Utility methods */
    
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
     * Gets the links that should be included for the given resource
     * 
     * @param uriInfo
     *            the uri info for the request
     * @param defn
     *            the definition of the resource to look up the links for
     * @param id
     *            the id of the resource to include in the links
     * @param entityBody
     *            the entity making the links for
     * @return the list of links that the resource should include
     */
    private static List<EmbeddedLink> getLinks(final UriInfo uriInfo, final EntityDefinition defn, final String id,
            final EntityBody entityBody, final EntityDefinitionStore entityDefs) {
        List<EmbeddedLink> links = ResourceUtil.getSelfLinkForEntity(uriInfo, id, defn);
        if (defn instanceof AssociationDefinition) {
            AssociationDefinition assocDef = (AssociationDefinition) defn;
            EntityDefinition sourceEntity = assocDef.getSourceEntity();
            String sourceId = (String) entityBody.get(assocDef.getSourceKey());
            if (sourceId != null) {
                links.add(new EmbeddedLink(assocDef.getSourceLink(), sourceEntity.getType(), ResourceUtil.getURI(
                        uriInfo, PathConstants.V1, sourceEntity.getResourceName(), sourceId).toString()));
            }
            EntityDefinition targetEntity = assocDef.getTargetEntity();
            String targetId = (String) entityBody.get(assocDef.getTargetKey());
            if (targetId != null) {
                links.add(new EmbeddedLink(assocDef.getTargetLink(), targetEntity.getType(), ResourceUtil.getURI(
                        uriInfo, PathConstants.V1, targetEntity.getResourceName(), targetId).toString()));
            }
            
        } else {
            links.addAll(ResourceUtil.getAssociationLinksForEntity(entityDefs, defn, id, uriInfo));
            links.addAll(ResourceUtil.getReferenceLinks(uriInfo, entityDefs, defn, entityBody));
        }
        return links;
    }
    
    /**
     * Returns true if the headers contain an "accept" request header with a HypermediaType.VENDOR_SLC_JSON value, false otherwise
     * 
     * @param headers headers from HTTP request
     * @return true if the headers contain an "accept" request header with a HypermediaType.VENDOR_SLC_JSON value, false otherwise
     */
    protected boolean shouldIncludeLinks(final HttpHeaders headers) {
        //get the request headers for ACCEPT
        List<String> acceptRequestHeaders = headers.getRequestHeader("accept");
        
        //confirm request headers were found
        if (acceptRequestHeaders != null) {
            //return true if specific media type was listed
            return (acceptRequestHeaders.contains(HypermediaType.VENDOR_SLC_JSON));
        }
        
        //no request headers for ACCEPT at all? no links then
        return false;
    }
    
    /**
     * Creates a new map with they key mapped to the specified value, "includeFields" mapped to the specified includeField, and with "limit" and
     * "offset" inherited from the supplied resolutionQueryParameters (if those keys exist in that map).
     * 
     * @param resolutionQueryParameters query parameters from HTTP request
     * @param key key portion of key/value pair to add to new map
     * @param value value portion of key/value pair to add to new map
     * @param includeField field to be specified as the only field(s) to be returned in the results
     * @return map containing specified key (and value), "includeFields" (and value), and possibly "limit" and "offset" (with values) 
     */
    protected Map<String, String> createAssociationQueryParameters(Map<String, String> resolutionQueryParameters, 
            String key, String value, String includeField) {
        //create a new map
        Map<String, String> associationQueryParameters = new HashMap<String, String>();
        //put the new query parameter key and value
        associationQueryParameters.put(key, value);
        //put that the only field to be returned is/are the specified field(s)
        associationQueryParameters.put("includeFields", includeField);
        
        //inherit "limit", if key existed in original query parameters
        String limit = resolutionQueryParameters.get("limit");
        if (limit != null) {
            associationQueryParameters.put("limit", limit);
        }
        
        //inherit "offset", if key existed in original query parameters
        String offset = resolutionQueryParameters.get("offset");
        if (offset != null) {
            associationQueryParameters.put("offset", offset);
        }
        
        //return map
        return associationQueryParameters;
    }
    
}
