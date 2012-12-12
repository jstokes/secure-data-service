package org.slc.sli.api.security.context;

import org.slc.sli.api.config.EntityDefinition;
import org.slc.sli.api.config.EntityDefinitionStore;
import org.slc.sli.api.constants.EntityNames;
import org.slc.sli.api.constants.ParameterConstants;
import org.slc.sli.api.constants.PathConstants;
import org.slc.sli.api.representation.EntityBody;
import org.slc.sli.api.security.SLIPrincipal;
import org.slc.sli.domain.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.ws.rs.core.UriInfo;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Verify the user has access to write to an entity. We do this by determining the entities edOrg and the principals edOrgs
 * Verify the user has access to the entity once their changes are applied.
 */
@Component
public class WriteValidator {

    private HashMap<String, String> ENTITIES_NEEDING_ED_ORG_WRITE_VALIDATION;
    private List<ComplexValidation> complexValidationList;
    private Map<String, ComplexValidation> complexValidationMap;


    @Autowired
    private EntityDefinitionStore store;

    @Autowired
    private PagingRepositoryDelegate<Entity> repo;

    private class ComplexValidation {
        private String entityType;
        private String validationReferenceName;
        private String validationReferenceType;

        public ComplexValidation(String entityType, String validationReferenceName, String validationReferenceType) {
            this.entityType = entityType;
            this.validationReferenceName = validationReferenceName;
            this.validationReferenceType = validationReferenceType;
        }

        public String getEntityType() {
            return entityType;
        }

        public String getValidationReferenceName() {
            return validationReferenceName;
        }

        public String getValidationReferenceType() {
            return validationReferenceType;
        }
    }


    @PostConstruct
    private void init() {
        ENTITIES_NEEDING_ED_ORG_WRITE_VALIDATION = new HashMap<String, String>() {{
            put(EntityNames.ATTENDANCE, "schoolId");
            put(EntityNames.COHORT, "educationOrgId");
            put(EntityNames.COURSE, "schoolId");
            put(EntityNames.COURSE_OFFERING, "schoolId");
            put(EntityNames.DISCIPLINE_INCIDENT, "schoolId");
            put(EntityNames.GRADUATION_PLAN, "educationOrganizationId");
            put(EntityNames.SECTION, ParameterConstants.SCHOOL_ID);
            put(EntityNames.SESSION, "schoolId");
            put(EntityNames.STUDENT_PROGRAM_ASSOCIATION, "educationOrganizationId");
            put(EntityNames.STUDENT_SCHOOL_ASSOCIATION, "schoolId");
        }};

        complexValidationList = Arrays.asList(
                new ComplexValidation(EntityNames.STUDENT_SECTION_ASSOCIATION, ParameterConstants.SECTION_ID, EntityNames.SECTION),
                new ComplexValidation(EntityNames.STUDENT_GRADEBOOK_ENTRY, ParameterConstants.GRADEBOOK_ENTRY_ID, EntityNames.GRADEBOOK_ENTRY),
                new ComplexValidation(EntityNames.GRADEBOOK_ENTRY, ParameterConstants.SECTION_ID, EntityNames.SECTION),
                new ComplexValidation(EntityNames.COURSE_TRANSCRIPT, ParameterConstants.COURSE_ID, EntityNames.COURSE),
                new ComplexValidation(EntityNames.DISCIPLINE_ACTION, ParameterConstants.DISCIPLINE_INCIDENT_ID, EntityNames.DISCIPLINE_INCIDENT)
        );

        complexValidationMap = new HashMap<String, ComplexValidation>();
        for (ComplexValidation validationRule: complexValidationList) {
            complexValidationMap.put(validationRule.getEntityType(), validationRule);
        }

    }


    public void validateWriteRequest(EntityBody entityBody, UriInfo uriInfo, SLIPrincipal principal) {

        if (!isValidForEdOrgWrite(entityBody, uriInfo, principal)) {
            throw new AccessDeniedException("Invalid reference. No association to referenced entity.");
        }

    }

    private boolean isValidForEdOrgWrite(EntityBody entityBody, UriInfo uriInfo, SLIPrincipal principal) {
        boolean isValid = true;

        int RESOURCE_SEGMENT_INDEX = 1;
        int VERSION_INDEX = 0;
        if (uriInfo.getPathSegments().size() > RESOURCE_SEGMENT_INDEX
                && uriInfo.getPathSegments().get(VERSION_INDEX).getPath().equals(PathConstants.V1)) {

            String resourceName = uriInfo.getPathSegments().get(RESOURCE_SEGMENT_INDEX).getPath();
            EntityDefinition def = store.lookupByResourceName(resourceName);

            int IDS_SEGMENT_INDEX = 2;
            if (uriInfo.getPathSegments().size() > IDS_SEGMENT_INDEX) {
                // look if we have ed org write context to already existing entity
                String id = uriInfo.getPathSegments().get(IDS_SEGMENT_INDEX).getPath();
                Entity existingEntity = repo.findById(def.getStoredCollectionName(), id);
                if (existingEntity != null) {
                    isValid = isEntityValidForEdOrgWrite(existingEntity.getBody(), existingEntity.getType(), principal);
                }
            }

            if (isValid && entityBody != null) {
                isValid = isEntityValidForEdOrgWrite(entityBody, def.getType(), principal);
            }
        }
        return isValid;
    }


    private boolean isEntityValidForEdOrgWrite(Map<String, Object> entityBody, String entityType, SLIPrincipal principal) {
        boolean isValid = true;
        if (ENTITIES_NEEDING_ED_ORG_WRITE_VALIDATION.get(entityType) != null) {
            String edOrgId = (String) entityBody.get(ENTITIES_NEEDING_ED_ORG_WRITE_VALIDATION.get(entityType));
            isValid = principal.getSubEdOrgHierarchy().contains(edOrgId);
        } else if (complexValidationMap.containsKey(entityType)) {
            ComplexValidation validation = complexValidationMap.get(entityType);
            EntityDefinition definition = store.lookupByEntityType(validation.getValidationReferenceType());
            String id = (String) entityBody.get(validation.getValidationReferenceName());
            if (id != null) {
                final Entity entity = repo.findById(definition.getStoredCollectionName(), id);
                isValid = isEntityValidForEdOrgWrite(entity.getBody(), definition.getType(), principal);
            }
        }
        return isValid;
    }

    public void setRepo(PagingRepositoryDelegate<Entity> repo) {
        this.repo = repo;
    }
}
