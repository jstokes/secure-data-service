package org.slc.sli.api.security.context.resolver;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slc.sli.api.constants.EntityNames;
import org.slc.sli.api.security.context.PagingRepositoryDelegate;
import org.slc.sli.api.security.context.traversal.cache.impl.SessionSecurityCache;
import org.slc.sli.domain.Entity;
import org.slc.sli.domain.NeutralCriteria;
import org.slc.sli.domain.NeutralQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TeacherGradingPeriodResolver implements EntityContextResolver {

    @Autowired
    private SessionSecurityCache securityCache;
    
    @Autowired
    private TeacherSessionResolver sessionResolver;

    @Autowired
    
    private PagingRepositoryDelegate<Entity> repository;

    @Override
    public boolean canResolve(String fromEntityType, String toEntityType) {
        return EntityNames.TEACHER.equals(fromEntityType) && EntityNames.GRADING_PERIOD.equals(toEntityType);
    }

    @Override
    public List<String> findAccessible(Entity principal) {
        
        // Get the sessions
        List<String> sessionIds = new ArrayList<String>();
        if (!securityCache.contains(EntityNames.SESSION)) {
            sessionIds = sessionResolver.findAccessible(principal);
        } else {
            sessionIds = new ArrayList<String>(securityCache.retrieve(EntityNames.SESSION));
        }


        Set<String> gperiodIds = new HashSet<String>();

        NeutralQuery neutralQuery = new NeutralQuery();
        neutralQuery.addCriteria(new NeutralCriteria("_id", "in", sessionIds));

        Iterable<Entity> entities = repository.findAll(EntityNames.SESSION, neutralQuery);

        for (Entity e : entities) {
            Object ref = e.getBody().get("gradingPeriodReference");
            if (ref instanceof String) {
                gperiodIds.add((String) ref);
            } else {    //list
                gperiodIds.addAll((List) ref);
            }
        }

        return new ArrayList<String>(gperiodIds);
    }

}
