package org.slc.sli.api.resources.v1.entity;

import javax.ws.rs.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import org.slc.sli.api.config.EntityDefinitionStore;
import org.slc.sli.api.resources.v1.DefaultCrudResource;
import org.slc.sli.common.constants.ResourceNames;
import org.slc.sli.common.constants.v1.PathConstants;

/**
 * Represents the student competency objective evaluated for the student.
 *
 * @author chung
 *
 */
@Path(PathConstants.V1 + "/" + PathConstants.STUDENT_COMPETENCY_OBJECTIVES)
@Component
@Scope("request")
public class StudentCompetencyObjectiveResource extends DefaultCrudResource {

    @Autowired
    public StudentCompetencyObjectiveResource(EntityDefinitionStore entityDefs) {
        super(entityDefs, ResourceNames.STUDENT_COMPETENCY_OBJECTIVES);
    }

}
