package org.slc.sli.api.security.context.resolver;

import org.slc.sli.api.client.constants.EntityNames;
import org.slc.sli.api.client.constants.ResourceNames;
import org.slc.sli.api.security.context.AssociativeContextHelper;
import org.slc.sli.domain.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Resolves which StudentProgramAssociations a given teacher is allowed to see.
 *
 * @author vmcglaughlin
 */
@Component
public class TeacherToStudentProgramAssociationResolver implements EntityContextResolver {

    @Autowired
    private AssociativeContextHelper helper;

    @Override
    public boolean canResolve(String fromEntityType, String toEntityType) {
        return false;
        //return EntityNames.TEACHER.equals(fromEntityType) && EntityNames.STUDENT_PROGRAM_ASSOCIATION.equals(toEntityType);
    }

    @Override
    public List<String> findAccessible(Entity principal) {
        List<String> studentIds = helper.findAccessible(principal, Arrays.asList(
                ResourceNames.TEACHER_SECTION_ASSOCIATIONS, ResourceNames.STUDENT_SECTION_ASSOCIATIONS));

        return helper.findEntitiesContainingReference(EntityNames.STUDENT_PROGRAM_ASSOCIATION, "studentId", studentIds);
    }
}
