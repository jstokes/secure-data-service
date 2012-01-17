package org.slc.sli.api.security.context;

import org.slc.sli.domain.Entity;

/**
 *  TODO: add javadoc to this file
 *  this must be done prior to committing code to prevent checkstyle breaks
 */
public interface EntityContextResolver {
    public String getSourceType();

    public String getTargetType();

    public boolean hasPermission(Entity source, Entity target);

}
