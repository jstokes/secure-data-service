package org.slc.sli.api.resources.util;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.slc.sli.api.client.constants.ResourceConstants;
import org.slc.sli.api.client.constants.ResourceNames;
import org.slc.sli.api.client.constants.v1.PathConstants;
import org.slc.sli.api.config.AssociationDefinition;
import org.slc.sli.api.config.EntityDefinition;
import org.slc.sli.api.config.EntityDefinitionStore;
import org.slc.sli.api.representation.EmbeddedLink;
import org.slc.sli.api.representation.EntityBody;
import org.slc.sli.api.security.SLIPrincipal;
import org.slc.sli.validation.schema.ReferenceSchema;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Performs tasks common to both Resource and HomeResource to eliminate code-duplication. These
 * tasks include creating a list of embedded links, adding links to a list regarding associations,
 * and resolving a new URI based on parameters.
 * 
 * @author kmyers <kmyers@wgen.net>
 * 
 */
public class ResourceUtil {

    private static final String REFERENCE = "REF";
    private static final String LINK = "LINK";
    private static final String BLANK = "";
    private static final  Map<String, String> LINK_NAMES = new HashMap<String, String>();
    static {
        LINK_NAMES.put(ResourceNames.EDUCATION_ORGANIZATIONS + ResourceNames.EDUCATION_ORGANIZATIONS + REFERENCE, "getParentEducationOrganization");
        LINK_NAMES.put(ResourceNames.EDUCATION_ORGANIZATIONS + ResourceNames.SCHOOLS + LINK, "getFeederSchools");
        LINK_NAMES.put(ResourceNames.EDUCATION_ORGANIZATIONS + ResourceNames.EDUCATION_ORGANIZATIONS + LINK, "getFeederEducationOrganizations");
        LINK_NAMES.put(ResourceNames.SCHOOLS + ResourceNames.EDUCATION_ORGANIZATIONS + REFERENCE, "getParentEducationOrganization");
        LINK_NAMES.put(ResourceNames.LEARNINGOBJECTIVES + ResourceNames.LEARNINGOBJECTIVES + REFERENCE, "getParentLearningObjective");
        LINK_NAMES.put(ResourceNames.LEARNINGOBJECTIVES + ResourceNames.LEARNINGOBJECTIVES + LINK, "getChildLearningObjectives");
        LINK_NAMES.put(ResourceNames.SCHOOLS + ResourceNames.SCHOOLS + REFERENCE, BLANK);
        LINK_NAMES.put(ResourceNames.SCHOOLS + ResourceNames.SCHOOLS + LINK, BLANK);
        LINK_NAMES.put(ResourceNames.SCHOOLS + ResourceNames.EDUCATION_ORGANIZATIONS + LINK, BLANK);
        LINK_NAMES.put(ResourceNames.EDUCATION_ORGANIZATIONS + ResourceNames.SCHOOLS + REFERENCE, BLANK);
        LINK_NAMES.put(ResourceNames.SCHOOLS + ResourceNames.DISCIPLINE_ACTIONS + "responsibilitySchoolId" + LINK, "getDisciplineActionsAsResponsibleSchool");
        LINK_NAMES.put(ResourceNames.SCHOOLS + ResourceNames.DISCIPLINE_ACTIONS + "assignmentSchoolId" + LINK, "getDisciplineActionsAsAssignedSchool");
        LINK_NAMES.put(ResourceNames.EDUCATION_ORGANIZATIONS + ResourceNames.DISCIPLINE_ACTIONS + LINK, BLANK);
    }
    /**
     * Creates a new LinkedList and adds a link for self, then returns that list. When not creating
     * a self link, all other parameters can be null.
     * 
     * @param uriInfo
     *            base URI
     * @param userId
     *            unique identifier of user/object
     * @param defn
     *            entity definition for user/object
     * @param createSelfLink
     *            whether or not to include a self link
     * @return
     */
    @Deprecated
    public static List<EmbeddedLink> getSelfLink(final UriInfo uriInfo, final String userId, final EntityDefinition defn) {
        
        // create a new linkedlist
        LinkedList<EmbeddedLink> links = new LinkedList<EmbeddedLink>();
        
        // add a "self" link
        if (defn != null) {
            links.add(new EmbeddedLink(ResourceConstants.SELF, defn.getType(), ResourceUtil.getURI(uriInfo,
                    defn.getResourceName(), userId).toString()));
        }
        
        // return
        return links;
    }
    
    /**
     * Create a self link
     * 
     * @param uriInfo
     *            base URI
     * @param entityId
     *            unique identifier of entity
     * @param defn
     *            entity definition for entity
     * @return the self link
     */
    public static EmbeddedLink getSelfLinkForEntity(final UriInfo uriInfo, final String entityId,
            final EntityDefinition defn) {
        return new EmbeddedLink(ResourceConstants.SELF, defn.getType(), getURI(uriInfo, PathConstants.V1,
                PathConstants.TEMP_MAP.get(defn.getResourceName()), entityId).toString());
    }
    
    /**
     * Create the custom entity link
     * 
     * @param uriInfo
     *            base uri
     * @param entityId
     *            unique id of the identity
     * @param defn
     *            definition of the entity
     * @return the custom entity link
     */
    public static EmbeddedLink getCustomLink(final UriInfo uriInfo, final String entityId, final EntityDefinition defn) {
        return new EmbeddedLink(ResourceConstants.CUSTOM, defn.getType(), getURI(uriInfo, PathConstants.V1,
                PathConstants.TEMP_MAP.get(defn.getResourceName()), entityId, PathConstants.CUSTOM_ENTITIES).toString());
    }
    
    /**
     * Looks up associations for the given entity (definition) and adds embedded links for each
     * association for the given user ID.
     * 
     * @param entityDefs
     *            all entity definitions
     * @param defn
     *            entity whose associations are being looked up
     * @param links
     *            list to add associations links to
     * @param id
     *            specific ID to append to links to create specific lookup link
     * @param uriInfo
     *            base URI
     */
    @Deprecated
    public static List<EmbeddedLink> getAssociationsLinks(final EntityDefinitionStore entityDefs,
            final EntityDefinition defn, final String id, final UriInfo uriInfo) {
        
        LinkedList<EmbeddedLink> links = new LinkedList<EmbeddedLink>();
        
        // look up all associations for supplied entity
        Collection<EntityDefinition> entitiesThatReferenceDefinition = entityDefs.getLinked(defn);
        
        // loop through all associations to supplied entity type
        for (EntityDefinition definition : entitiesThatReferenceDefinition) {
            if (definition instanceof AssociationDefinition) {
                AssociationDefinition assoc = (AssociationDefinition) definition;
                if (assoc.getSourceEntity().equals(defn)) {
                    links.add(new EmbeddedLink(assoc.getRelNameFromSource(), assoc.getType(), ResourceUtil.getURI(
                            uriInfo, assoc.getResourceName(), id).toString()));
                    links.add(new EmbeddedLink(assoc.getHoppedTargetLink(), assoc.getTargetEntity().getType(),
                            ResourceUtil.getURI(uriInfo, assoc.getResourceName(), id).toString() + "/targets"));
                } else if (assoc.getTargetEntity().equals(defn)) {
                    links.add(new EmbeddedLink(assoc.getRelNameFromTarget(), assoc.getType(), ResourceUtil.getURI(
                            uriInfo, assoc.getResourceName(), id).toString()));
                    links.add(new EmbeddedLink(assoc.getHoppedSourceLink(), assoc.getSourceEntity().getType(),
                            ResourceUtil.getURI(uriInfo, assoc.getResourceName(), id).toString() + "/targets"));
                }
            }
        }
        return links;
    }
    
    /**
     * Looks up associations for the given entity (definition) and adds embedded links for each
     * association for the given user ID.
     * 
     * @param entityDefs
     *            all entity definitions
     * @param defn
     *            entity whose associations are being looked up
     * @param links
     *            list to add associations links to
     * @param entityBody
     *            object whose links to/from are being requested
     * @param uriInfo
     *            base URI
     */
    public static List<EmbeddedLink> getLinks(final EntityDefinitionStore entityDefs, final EntityDefinition defn,
            final EntityBody entityBody, final UriInfo uriInfo) {
        
        String id = (String) entityBody.get("id");
        
        // start with self link
        List<EmbeddedLink> links = new LinkedList<EmbeddedLink>();
        if (defn != null) {
            links.add(getSelfLinkForEntity(uriInfo, id, defn));
            links.add(getCustomLink(uriInfo, id, defn));
        }
        
        links.addAll(getReferenceLinks(defn, entityBody, uriInfo));
        
        if (defn instanceof AssociationDefinition) {
            links.addAll(getLinksForAssociation(uriInfo, id, (AssociationDefinition) defn));
        }
        
        links.addAll(getLinkedDefinitions(entityDefs, defn, uriInfo, id));
        
        return links;
    }
    
    private static List<EmbeddedLink> getLinkedDefinitions(final EntityDefinitionStore entityDefs,
            final EntityDefinition defn, final UriInfo uriInfo, String id) {
        List<EmbeddedLink> links = new LinkedList<EmbeddedLink>();
        // loop through all entities with references to supplied entity type
        for (EntityDefinition definition : entityDefs.getLinked(defn)) {

            // if the entity that has a reference to the defn parameter is an association
            if (definition instanceof AssociationDefinition) {
                
                AssociationDefinition assoc = (AssociationDefinition) definition;
                if (assoc.getSourceEntity().getType().equals(defn.getType())) {
                    links.add(new EmbeddedLink(assoc.getRelNameFromSource(), assoc.getType(), getURI(uriInfo,
                            PathConstants.V1, defn.getResourceName(), id,
                            PathConstants.TEMP_MAP.get(assoc.getResourceName())).toString()));
                    
                    links.add(new EmbeddedLink(assoc.getHoppedTargetLink(), assoc.getTargetEntity().getType(), getURI(
                            uriInfo, PathConstants.V1, defn.getResourceName(), id,
                            PathConstants.TEMP_MAP.get(assoc.getResourceName()),
                            assoc.getTargetEntity().getResourceName()).toString()));
                    
                } else if (assoc.getTargetEntity().getType().equals(defn.getType())) {
                    links.add(new EmbeddedLink(assoc.getRelNameFromTarget(), assoc.getType(), getURI(uriInfo,
                            PathConstants.V1, defn.getResourceName(), id,
                            PathConstants.TEMP_MAP.get(assoc.getResourceName())).toString()));
                    
                    links.add(new EmbeddedLink(assoc.getHoppedSourceLink(), assoc.getSourceEntity().getType(), getURI(
                            uriInfo, PathConstants.V1, defn.getResourceName(), id,
                            PathConstants.TEMP_MAP.get(assoc.getResourceName()),
                            assoc.getSourceEntity().getResourceName()).toString()));
                }
            } else {
                // loop through all reference fields, display as ? links
                for (String referenceFieldName : definition.getReferenceFieldNames(defn.getStoredCollectionName())) {
                    String linkName = getLinkName(defn.getResourceName(), definition.getResourceName(), referenceFieldName, false);

                    if (!linkName.isEmpty()) {
                        links.add(new EmbeddedLink(linkName, "type", getURI(uriInfo, PathConstants.V1,
                                PathConstants.TEMP_MAP.get(definition.getResourceName())).toString()
                                + "?" + referenceFieldName + "=" + id));
                    }

                }
            }
        }
        return links;
    }
    
    private static List<EmbeddedLink> getLinksForAssociation(final UriInfo uriInfo, String id,
            AssociationDefinition assoc) {
        List<EmbeddedLink> links = new LinkedList<EmbeddedLink>();
        
        String sourceLink = ResourceNames.PLURAL_LINK_NAMES.get(assoc.getSourceEntity().getResourceName());
        links.add(new EmbeddedLink(sourceLink, assoc.getSourceEntity().getType(), getURI(uriInfo, PathConstants.V1,
                PathConstants.TEMP_MAP.get(assoc.getResourceName()), id,
                PathConstants.TEMP_MAP.get(assoc.getSourceEntity().getResourceName())).toString()));
        
        String targetLink = ResourceNames.PLURAL_LINK_NAMES.get(assoc.getTargetEntity().getResourceName());
        links.add(new EmbeddedLink(targetLink, assoc.getTargetEntity().getType(), getURI(uriInfo, PathConstants.V1,
                PathConstants.TEMP_MAP.get(assoc.getResourceName()), id,
                PathConstants.TEMP_MAP.get(assoc.getTargetEntity().getResourceName())).toString()));
        return links;
    }
    
    private static List<EmbeddedLink> getReferenceLinks(final EntityDefinition defn, final EntityBody entityBody,
            final UriInfo uriInfo) {
        List<EmbeddedLink> links = new LinkedList<EmbeddedLink>();
        
        if (defn == null || entityBody == null || uriInfo == null) {
            return links;
        }
        
        // loop through all reference fields on supplied entity type
        for (Entry<String, ReferenceSchema> referenceField : defn.getReferenceFields().entrySet()) {
            // see what GUID is stored in the reference field
            for (String referenceGuid : entityBody.getId(referenceField.getKey())) {
                // if a value (GUID) was stored there
                if (referenceGuid != null) {
                    Set<String> resourceNames = ResourceNames.ENTITY_RESOURCE_NAME_MAPPING.get(referenceField
                            .getValue().getResourceName());
                    
                    for (String resourceName : resourceNames) {
                        String linkName = getLinkName(defn.getResourceName(), resourceName, BLANK, true);
                        if (!linkName.isEmpty()) {
                            links.add(new EmbeddedLink(linkName, "type", getURI(uriInfo, PathConstants.V1,
                                    PathConstants.TEMP_MAP.get(resourceName), referenceGuid).toString()));
                        }

                    }
                }
            }
        }
        return links;
    }
    
    /**
     * Returns the URI for aggregations
     * 
     * @param uriInfo
     *            The base URI
     * @return A list of links pointing to the base Url for aggregations
     */
    public static List<EmbeddedLink> getAggregateLink(final UriInfo uriInfo) {
        List<EmbeddedLink> links = new ArrayList<EmbeddedLink>();
        
        links.add(new EmbeddedLink(ResourceConstants.LINKS, ResourceConstants.ENTITY_EXPOSE_TYPE_AGGREGATIONS, uriInfo
                .getBaseUriBuilder().path(ResourceConstants.RESOURCE_PATH_AGG).build().toString()));
        
        return links;
    }
    
    /**
     * Adds the value to a list and then puts the list into the query parameters associated to the
     * given key.
     * 
     * @param queryParameters
     *            where to put the value once added to a list
     * @param key
     *            key value for new parameter
     * @param value
     *            resulting value for new parameter
     */
    public static void putValue(MultivaluedMap<String, String> queryParameters, String key, String value) {
        List<String> values = new ArrayList<String>();
        values.add(value);
        queryParameters.put(key, values);
    }
    
    /**
     * Adds the value to a list and then puts the list into the query parameters associated to the
     * given key.
     * 
     * @param queryParameters
     *            where to put the value once added to a list
     * @param key
     *            key value for new parameter
     * @param value
     *            resulting value for new parameter
     */
    public static void putValue(MultivaluedMap<String, String> queryParameters, String key, int value) {
        ResourceUtil.putValue(queryParameters, key, "" + value);
    }
    
    /**
     * Helper method to convert MultivaluedMap to a Map
     * 
     * @param map
     * @return
     */
    public static Map<String, String> convertToMap(Map<String, List<String>> map) {
        Map<String, String> results = new HashMap<String, String>();
        
        if (map != null) {
            for (Map.Entry<String, List<String>> e : map.entrySet()) {
                results.put(e.getKey(), e.getValue().get(0));
            }
        }
        
        return results;
    }
    
    /**
     * Returns a URI based on the supplied URI with the paths appended to the base URI.
     * 
     * @param uriInfo
     *            URI of current actions
     * @param paths
     *            Paths that need to be appended
     * @return
     */
    public static URI getURI(UriInfo uriInfo, String... paths) {
        UriBuilder builder = uriInfo.getBaseUriBuilder();
        
        for (String path : paths) {
            if (path != null) {
                builder.path(path);
            }
        }
        
        return builder.build();
    }
    
    /**
     * Analyzes security context to get SLIPrincipal for user.
     * 
     * @return SLIPrincipal from security context
     */
    public static SLIPrincipal getSLIPrincipalFromSecurityContext() {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        if (auth instanceof AnonymousAuthenticationToken || auth.getPrincipal() instanceof String) {
            throw new InsufficientAuthenticationException("Login Required");
        }
        
        // lookup security/login information
        SLIPrincipal principal = (SLIPrincipal) auth.getPrincipal();
        return principal;
    }

    /**
     * Finds the link name based on the entity type and the reference entity name
     * @param resourceName
     *          Entity name for which the links are generated
     * @param referenceName
     *          Referenced entity name
     * @param referenceField
     *          Referenced field Name
     * @param isReferenceEntity
     *          indicates whether its a referenced entity
     * @return
     */
    public static String getLinkName(String resourceName, String referenceName, String referenceField, boolean isReferenceEntity) {

        boolean bSelfReference = false;
        String  linkName = "";
        String key = resourceName + referenceName;
        String keyWithRefField = key + referenceField + LINK;
       if (isReferenceEntity) {
            key = key + REFERENCE;
        } else {
            key = key + LINK;
        }

        if (LINK_NAMES.containsKey(key)) {
            linkName = LINK_NAMES.get(key);
        } else if (LINK_NAMES.containsKey(keyWithRefField)) {
            linkName = LINK_NAMES.get(keyWithRefField);
        } else if (isReferenceEntity) {
          linkName = ResourceNames.SINGULAR_LINK_NAMES.get(referenceName);
        } else {
          linkName = ResourceNames.PLURAL_LINK_NAMES.get(referenceName);
        }
        return  linkName;
    }

    
}
