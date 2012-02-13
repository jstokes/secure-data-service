package org.slc.sli.domain;

import java.util.Map;

/**
 * Define the entity interface that provides getter method to retrieve the fields
 * for entities including core entities and association entities
 *
 * @author Dong Liu dliu@wgen.net
 *
 */
public interface Entity {

    /**
     * @return the entity type as string, can be entity type for
     *         core entity or association entity
     */
    public String getType();

    /**
     * @return the global unique id of the entity as string
     */
    public String getEntityId();

    /**
     * @return the entity body wrapped by a map
     */
    public Map<String, Object> getBody();

    /**
     * Returns the metaData.
     */
     public Map<String, Object> getMetaData();
    
}
