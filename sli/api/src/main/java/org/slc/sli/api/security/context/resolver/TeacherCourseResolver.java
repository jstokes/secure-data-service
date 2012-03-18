package org.slc.sli.api.security.context.resolver;

import org.slc.sli.api.config.EntityNames;
import org.slc.sli.api.config.ResourceNames;
import org.slc.sli.api.security.context.AssociativeContextHelper;
import org.slc.sli.domain.Entity;
import org.slc.sli.domain.EntityQuery;
import org.slc.sli.domain.EntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Resolves which teachers a given teacher is allowed to see
 * 
 * @author dkornishev
 * 
 */
@Component
public class TeacherCourseResolver implements EntityContextResolver {

    @Autowired
    private AssociativeContextHelper helper;

    @Autowired
    private EntityRepository repository;

    @Override
    public boolean canResolve(String fromEntityType, String toEntityType) {
        return EntityNames.TEACHER.equals(fromEntityType) && EntityNames.COURSE.equals(toEntityType);
    }
    
    @Override
    public List<String> findAccessible(Entity principal) {
        List<String> teacherSectionIds = helper.findAccessible(principal, Arrays.asList(
                ResourceNames.TEACHER_SECTION_ASSOCIATIONS));

        List<String> studentSectionIds = helper.findAccessible(principal, Arrays.asList(
                ResourceNames.TEACHER_SECTION_ASSOCIATIONS,
                ResourceNames.STUDENT_SECTION_ASSOCIATIONS,
                ResourceNames.STUDENT_SECTION_ASSOCIATIONS));

        Set<String> sectionIds = new HashSet<String>();
        sectionIds.addAll(teacherSectionIds);
        sectionIds.addAll(studentSectionIds);

        StringBuilder query = new StringBuilder();
        String separator = "";

        for (String s : sectionIds) {
            query.append(separator);
            separator = ",";
            query.append(s);
        }

        EntityQuery.EntityQueryBuilder queryBuilder = new EntityQuery.EntityQueryBuilder();
        queryBuilder.addField("_id", query.toString());

        Iterable<Entity> entities = repository.findAll(EntityNames.SECTION, queryBuilder.build());

        Set<String> sessionIds = new HashSet<String>();

        for (Entity e : entities) {
            String courseId = (String) e.getBody().get("courseId");
            if (courseId != null) {
                sessionIds.add(courseId);
            }
        }

        return new ArrayList<String>(sessionIds);
    }

}
