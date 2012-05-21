package org.slc.sli.api.resources.v1.entity;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import org.slc.sli.api.config.EntityDefinitionStore;
import org.slc.sli.api.resources.v1.DefaultCrudResource;
import org.slc.sli.common.constants.ResourceNames;
import org.slc.sli.common.constants.v1.PathConstants;

/**
 * Responds to create, read, update, and delete operations for gradebook entries.
 *
 * If you're looking for grades on a specific gradebook entry,
 * use StudentSectionGradebookEntryResource instead.
 *
 * @author kmyers
 *
 */
@Path(PathConstants.V1 + "/" + PathConstants.GRADEBOOK_ENTRIES)
@Component
@Scope("request")
public class GradebookEntryResource extends DefaultCrudResource {

    /**
     * Logging utility.
     */
    private static final Logger LOG = LoggerFactory.getLogger(GradebookEntryResource.class);

    @Autowired
        public GradebookEntryResource(EntityDefinitionStore entityDefs) {
            super(entityDefs, ResourceNames.GRADEBOOK_ENTRIES);
        LOG.debug("Initialized a new {}", GradebookEntryResource.class);
    }

}
