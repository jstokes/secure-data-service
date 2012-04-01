package org.slc.sli.api.resources.v1.view;

import org.slc.sli.api.config.EntityDefinition;
import org.slc.sli.api.representation.EntityBody;
import org.slc.sli.api.resources.v1.ParameterConstants;
import org.slc.sli.domain.NeutralCriteria;
import org.slc.sli.domain.NeutralQuery;
import org.springframework.beans.factory.annotation.Autowired;

import org.slc.sli.api.config.EntityDefinitionStore;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Helper class for all the strategy implementations for the custom
 * views returned by the api
 * @author srupasinghe
 *
 */
@Component
public class OptionalFieldAppenderHelper {
    
    @Autowired
    protected EntityDefinitionStore entityDefs;

    /**
     * Queries the database with a given collection for a key value pair
     * @param resourceName The collection
     * @param key The key to search on
     * @param values The value to search on
     * @return
     */
    public List<EntityBody> queryEntities(String resourceName, String key, List<String> values) {

        EntityDefinition entityDef = entityDefs.lookupByResourceName(resourceName);
        NeutralQuery neutralQuery = new NeutralQuery();
        neutralQuery.addCriteria(new NeutralCriteria(key, NeutralCriteria.CRITERIA_IN, values));

        return (List<EntityBody>) entityDef.getService().list(neutralQuery);
    }

    public List<EntityBody> queryEntities(String resourceName, NeutralQuery neutralQuery) {

        EntityDefinition entityDef = entityDefs.lookupByResourceName(resourceName);

        return (List<EntityBody>) entityDef.getService().list(neutralQuery);
    }

    /**
     * Returns a single entity from a list for a given key value pair
     * @param list List of entities
     * @param field The field to search for
     * @param value The value to search for
     * @return
     */
    public EntityBody getEntityFromList(List<EntityBody> list, String field, String value) {

        if (list == null || field == null || value == null) return null;

        for (EntityBody e : list) {
            if (value.equals(e.get(field))) {
                return e;
            }
        }

        return null;
    }

    /**
     * Returns a list of entities for a given key value pair
     * @param list List of entities
     * @param field The field to search for
     * @param value The value to search for
     * @return
     */
    public List<EntityBody> getEntitySubList(List<EntityBody> list, String field, String value) {
        List<EntityBody> results = new ArrayList<EntityBody>();

        if (list == null || field == null || value == null) return results;

        for (EntityBody e : list) {
            if (value.equals(e.get(field))) {
                results.add(e);
            }
        }

        return results;
    }

    /**
     * Returns a list of ids for a given key
     * @param list
     * @param field
     * @return
     */
    public List<String> getIdList(List<EntityBody> list, String field) {
        List<String> ids = new ArrayList<String>();

        if (list == null || field == null) return ids;

        for (EntityBody e : list) {
            if (e.get(field) != null)
                ids.add((String) e.get(field));
        }

        return ids;
    }

    /**
     * Returns a list of section ids by examining a list of students
     * @param entities
     * @return
     */
    public Set<String> getSectionIds(List<EntityBody> entities) {
        Set<String> sectionIds = new HashSet<String>();
        for (EntityBody e : entities) {
            List<EntityBody> associations = (List<EntityBody>) e.get("studentSectionAssociation");

            if (associations == null) continue;

            for (EntityBody association : associations) {
                sectionIds.add((String) association.get(ParameterConstants.SECTION_ID));
            }
        }

        return sectionIds;
    }
}
