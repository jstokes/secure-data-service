package org.slc.sli.api.security.context.resolver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.slc.sli.api.security.context.AssociativeContextHelper;
import org.slc.sli.common.constants.EntityNames;
import org.slc.sli.domain.Entity;
/**
 * Resolves which discipline incident a given staff is allowed to see
 *
 * @author syau
 *
 */
@Component
public class StaffDisciplineIncidentResolver implements EntityContextResolver {

    @Autowired
    private AssociativeContextHelper helper;

    @Override
    public boolean canResolve(String fromEntityType, String toEntityType) {
        return EntityNames.STAFF.equals(fromEntityType) && EntityNames.DISCIPLINE_INCIDENT.equals(toEntityType);
    }

    // Staff should have access to discipline incident records for which their discipline actions are associated
    @SuppressWarnings("unchecked")
    @Override
    public List<String> findAccessible(Entity principal) {

        // special privilege for demo user
        if (principal.getBody().get("staffUniqueStateId").equals("demo")) {
            info("Resolver override for demo user.");
            return AllowAllEntityContextResolver.SUPER_LIST;
        }

        // find the discipline actions this user has access to
        List<String> referenceIds = new ArrayList<String>();
        referenceIds.add(principal.getEntityId());
        Iterable<Entity> disciplineActions = helper.getReferenceEntities(EntityNames.DISCIPLINE_ACTION, "staffId", referenceIds);

        // extract the discipline incident ids from the discipline actions
        Set<String> disciplineIncidentIds = new HashSet<String>();
        for (Entity disciplineAction : disciplineActions) {
            Collection<String> ids = (Collection<String>) disciplineAction.getBody().get("disciplineIncidentId");
            disciplineIncidentIds.addAll(ids);
        }

        // add in incidents the staff is directly referenced
        List<String> directlyReferencedBy = helper.findEntitiesContainingReference(EntityNames.DISCIPLINE_INCIDENT, "staffId", referenceIds);
        disciplineIncidentIds.addAll(directlyReferencedBy);

        return new ArrayList<String>(disciplineIncidentIds);
    }
}
