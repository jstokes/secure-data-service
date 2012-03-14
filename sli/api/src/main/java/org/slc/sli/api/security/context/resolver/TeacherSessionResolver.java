package org.slc.sli.api.security.context.resolver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.slc.sli.api.config.EntityNames;
import org.slc.sli.api.config.ResourceNames;
import org.slc.sli.api.security.context.AssociativeContextHelper;
import org.slc.sli.domain.Entity;
import org.slc.sli.domain.SmartQuery;
import org.slc.sli.domain.Repository;

/**
 * Resolves which teachers a given teacher is allowed to see
 *
 * @author dkornishev
 *
 */
@Component
public class TeacherSessionResolver implements EntityContextResolver {

    @Autowired
    private AssociativeContextHelper helper;

    @Autowired
    private Repository<Entity> repository;

    @Override
    public boolean canResolve(String fromEntityType, String toEntityType) {
        return EntityNames.TEACHER.equals(fromEntityType) && EntityNames.SESSION.equals(toEntityType);
    }

    @Override
    public List<String> findAccessible(Entity principal) {
        List<String> ids = helper.findAccessible(principal, Arrays.asList(
                ResourceNames.TEACHER_SECTION_ASSOCIATIONS,
                ResourceNames.STUDENT_SECTION_ASSOCIATIONS,
                ResourceNames.STUDENT_SECTION_ASSOCIATIONS));

        StringBuilder query = new StringBuilder();
        String separator = "";

        for (String s : ids) {
            query.append(separator);
            separator = ",";
            query.append(s);
        }

        SmartQuery.SmartQueryBuilder queryBuilder = new SmartQuery.SmartQueryBuilder();
        queryBuilder.addField("_id", query.toString());

        Iterable<Entity> entities = repository.findAll(EntityNames.SECTION, queryBuilder.build());

        List<String> sessionIds = new ArrayList<String>();

        for (Entity e : entities) {
            sessionIds.add((String) e.getBody().get("sessionId"));
        }

        return sessionIds;
    }

}
