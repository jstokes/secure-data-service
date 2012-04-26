package org.slc.sli.api.config;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.slc.sli.common.constants.EntityNames;
import org.slc.sli.common.constants.ResourceNames;
import org.slc.sli.validation.SchemaRepository;
import org.slc.sli.validation.schema.ReferenceSchema;

/**
 * Default implementation of the entity definition store
 *
 * Instructions on adding entities:
 *
 * If the entity that needs to be exposed to the api is identical to something in the database, most
 * of the defaults should work for you.
 * factory.makeEntity(nameOfEntityType, pathInURI).buildAndRegister(this);
 *
 * If your entity requires some processing when it is fetched from the db and stored back in, this
 * can be handled by adding one or more treatments
 * factory.makeEntity(...).withTreatments(firstTransformation,
 * anotherTransformation).buildAndRegister(this);
 *
 * If it needs to be stored in a collection other than the one named by the entity type (if it is
 * sharing a collection with another type):
 * factory.makeEntity(...).storeAs(collectionName).buildAndRegister(this);
 *
 * If it needs to be stored somewhere other than the default db (for instance if this is something
 * that should be kept in memory), define your own repo and use with this:
 * factory.makeEntity(...).storeIn(newRepo).buildAndRegister(this);
 *
 *
 * @author nbrown
 *
 */
@Component
public class BasicDefinitionStore implements EntityDefinitionStore {
    private static final Logger LOG = LoggerFactory.getLogger(BasicDefinitionStore.class);

    private Map<String, EntityDefinition> mapping = new HashMap<String, EntityDefinition>();

    @Autowired
    private DefinitionFactory factory;

    @Autowired
    private SchemaRepository repo;

    @Override
    public EntityDefinition lookupByResourceName(String resourceName) {
        return mapping.get(resourceName);
    }

    @Override
    public EntityDefinition lookupByEntityType(String entityType) {
        return mapping.get(ResourceNames.ENTITY_RESOURCE_NAME_MAPPING.get(entityType));
    }

    @Override
    public Collection<EntityDefinition> getLinked(EntityDefinition defn) {
        return defn.getReferencingEntities();
    }

    @PostConstruct
    @Autowired
    public void init() {

        // adding the entity definitions
        EntityDefinition assessment = factory.makeEntity(EntityNames.ASSESSMENT, ResourceNames.ASSESSMENTS)
                .buildAndRegister(this);
        factory.makeEntity(EntityNames.ATTENDANCE, ResourceNames.ATTENDANCES).buildAndRegister(this);
        factory.makeEntity(EntityNames.BELL_SCHEDULE, ResourceNames.BELL_SCHEDULES).buildAndRegister(this);
        EntityDefinition cohort = factory.makeEntity(EntityNames.COHORT, ResourceNames.COHORTS).buildAndRegister(this);
        EntityDefinition course = factory.makeEntity(EntityNames.COURSE, ResourceNames.COURSES).buildAndRegister(this);
        EntityDefinition disciplineIncident = factory.makeEntity(EntityNames.DISCIPLINE_INCIDENT,
                ResourceNames.DISCIPLINE_INCIDENTS).buildAndRegister(this);
        factory.makeEntity(EntityNames.DISCIPLINE_ACTION, ResourceNames.DISCIPLINE_ACTIONS).buildAndRegister(this);
        EntityDefinition educationOrganization = factory.makeEntity(EntityNames.EDUCATION_ORGANIZATION,
                ResourceNames.EDUCATION_ORGANIZATIONS).buildAndRegister(this);
        factory.makeEntity(EntityNames.GRADEBOOK_ENTRY, ResourceNames.GRADEBOOK_ENTRIES).buildAndRegister(this);
        EntityDefinition program = factory.makeEntity(EntityNames.PROGRAM, ResourceNames.PROGRAMS).buildAndRegister(this);
        EntityDefinition school = factory.makeEntity(EntityNames.SCHOOL, ResourceNames.SCHOOLS)
                .storeAs(EntityNames.EDUCATION_ORGANIZATION).buildAndRegister(this);
        EntityDefinition section = factory.makeEntity(EntityNames.SECTION, ResourceNames.SECTIONS).buildAndRegister(
                this);
        EntityDefinition session = factory.makeEntity(EntityNames.SESSION, ResourceNames.SESSIONS).buildAndRegister(
                this);
        EntityDefinition staff = factory.makeEntity(EntityNames.STAFF, ResourceNames.STAFF).buildAndRegister(this);
        EntityDefinition student = factory.makeEntity(EntityNames.STUDENT, ResourceNames.STUDENTS).buildAndRegister(
                this);
        factory.makeEntity(EntityNames.STUDENT_SECTION_GRADEBOOK_ENTRY, ResourceNames.STUDENT_SECTION_GRADEBOOK_ENTRIES)
                .buildAndRegister(this);
        EntityDefinition teacher = factory.makeEntity(EntityNames.TEACHER, ResourceNames.TEACHERS)
                .storeAs(EntityNames.STAFF).buildAndRegister(this);
        EntityDefinition parent = factory.makeEntity(EntityNames.PARENT, ResourceNames.PARENTS).buildAndRegister(this);

        factory.makeEntity(EntityNames.AGGREGATION, ResourceNames.AGGREGATIONS).buildAndRegister(this);
        factory.makeEntity(EntityNames.AGGREGATION_DEFINITION, ResourceNames.AGGREGATION_DEFINITIONS).buildAndRegister(
                this);

        factory.makeEntity(EntityNames.LEARNINGOBJECTIVE, ResourceNames.LEARNINGOBJECTIVES).buildAndRegister(this);
        factory.makeEntity(EntityNames.LEARNINGSTANDARD, ResourceNames.LEARNINGSTANDARDS).buildAndRegister(this);

        // adding the association definitions
        AssociationDefinition studentSchoolAssociation = factory.makeAssoc("studentSchoolAssociation", "studentSchoolAssociations")
                .exposeAs(ResourceNames.STUDENT_SCHOOL_ASSOCIATIONS).storeAs("studentSchoolAssociation")
                .from(student, "getStudent", "getStudents").to(school, "getSchool", "getSchools")
                .calledFromSource("getStudentSchoolAssociations").calledFromTarget("getStudentSchoolAssociations")
                .build();
        addDefinition(studentSchoolAssociation);

        AssociationDefinition teacherSectionAssociation = factory.makeAssoc("teacherSectionAssociation", "teacherSectionAssociations")
                .exposeAs(ResourceNames.TEACHER_SECTION_ASSOCIATIONS).storeAs("teacherSectionAssociation")
                .from(teacher, "getTeacher", "getTeachers").to(section, "getSection", "getSections")
                .calledFromSource("getTeacherSectionAssociations").calledFromTarget("getTeacherSectionAssociations")
                .build();
        addDefinition(teacherSectionAssociation);

        AssociationDefinition studentAssessment = factory.makeAssoc("studentAssessmentAssociation", "studentAssessments")
                .exposeAs(ResourceNames.STUDENT_ASSESSMENT_ASSOCIATIONS).storeAs("studentAssessmentAssociation")
                .from(student, "getStudent", "getStudents").to(assessment, "getAssessment", "getAssessments")
                .calledFromSource("getStudentAssessments")
                .calledFromTarget("getStudentAssessments").build();
        addDefinition(studentAssessment);

        AssociationDefinition studentSectionAssociation = factory.makeAssoc("studentSectionAssociation", "studentSectionAssociations")
                .exposeAs(ResourceNames.STUDENT_SECTION_ASSOCIATIONS).storeAs("studentSectionAssociation")
                .from(student, "getStudent", "getStudents").to(section, "getSection", "getSections")
                .calledFromSource("getStudentSectionAssociations").calledFromTarget("getStudentSectionAssociations")
                .build();
        addDefinition(studentSectionAssociation);

        AssociationDefinition teacherSchoolAssociation = factory.makeAssoc("teacherSchoolAssociation", "teacherSchoolAssociations")
                .exposeAs(ResourceNames.TEACHER_SCHOOL_ASSOCIATIONS).storeAs("teacherSchoolAssociation")
                .from(teacher, "getTeacher", "getTeachers").to(school, "getSchool", "getSchools")
                .calledFromSource("getTeacherSchoolAssociations").calledFromTarget("getTeacherSchoolAssociations")
                .build();
        addDefinition(teacherSchoolAssociation);

        AssociationDefinition staffEducationOrgAssignmentAssociation = factory
                .makeAssoc("staffEducationOrganizationAssociation", "staffEducationOrgAssignmentAssociations")
                .exposeAs(ResourceNames.STAFF_EDUCATION_ORGANIZATION_ASSOCIATIONS)
                .storeAs("staffEducationOrganizationAssociation")
                .from(staff, "getStaff", "getStaff", "staffReference")
                .to(educationOrganization, "getEducationOrganization", "getEducationOrganizations",
                        "educationOrganizationReference").calledFromSource("getStaffEducationOrgAssignmentAssociations")
                .calledFromTarget("getStaffEducationOrgAssignmentAssociations").build();
        addDefinition(staffEducationOrgAssignmentAssociation);

        AssociationDefinition sectionAssessmentAssociation = factory.makeAssoc("sectionAssessmentAssociation", "sectionAssessmentAssociations")
                .exposeAs(ResourceNames.SECTION_ASSESSMENT_ASSOCIATIONS).storeAs("sectionAssessmentAssociation")
                .from(section, "getSection", "getSections").to(assessment, "getAssessment", "getAssessments")
                .calledFromSource("getSectionAssessmentAssociations")
                .calledFromTarget("getSectionAssessmentAssociations").build();
        addDefinition(sectionAssessmentAssociation);

        AssociationDefinition educationOrganizationAssociation = factory
                .makeAssoc("educationOrganizationAssociation", "educationOrganizationAssociations")
                .exposeAs("educationOrganization-associations")
                .storeAs("educationOrganizationAssociation")
                .from(educationOrganization, "getEducationOrganizationParent", "getEducationOrganizationParents",
                        "educationOrganizationParentId")
                .to(educationOrganization, "getEducationOrganizationChild", "getEducationOrganizationChilds",
                        "educationOrganizationChildId").calledFromSource("getEducationOrganizationAssociations")
                .calledFromTarget("getEducationOrganizationAssociations").build();
        addDefinition(educationOrganizationAssociation);

        AssociationDefinition schoolSessionAssociation = factory.makeAssoc("schoolSessionAssociation", "schoolSessionAssociations")
                .exposeAs(ResourceNames.SCHOOL_SESSION_ASSOCIATIONS).storeAs("schoolSessionAssociation")
                .from(school, "getSchool", "getSchools").to(session, "getSession", "getSessions")
                .calledFromSource("getSchoolSessionAssociations").calledFromTarget("getSchoolSessionAssociations")
                .build();
        addDefinition(schoolSessionAssociation);

        AssociationDefinition courseOffering = factory.makeAssoc("sessionCourseAssociation", "courseOfferings")
                .exposeAs(ResourceNames.SESSION_COURSE_ASSOCIATIONS).storeAs("sessionCourseAssociation")
                .from(session, "getSession", "getSessions").to(course, "getCourse", "getCourses")
                .calledFromSource("getCourseOfferings").calledFromTarget("getCourseOfferings")
                .build();
        addDefinition(courseOffering);

        AssociationDefinition courseTranscript = factory.makeAssoc("studentTranscriptAssociation", "courseTranscripts")
                .exposeAs(ResourceNames.STUDENT_TRANSCRIPT_ASSOCIATIONS).storeAs("studentTranscriptAssociation")
                .from(student, "getStudent", "getStudents").to(course, "getCourse", "getCourses")
                .calledFromSource("getCourseTranscripts")
                .calledFromTarget("getCourseTranscripts").build();
        addDefinition(courseTranscript);

        AssociationDefinition studentParentAssociation = factory.makeAssoc(EntityNames.STUDENT_PARENT_ASSOCIATION, "studentParentAssociations")
                .exposeAs(ResourceNames.STUDENT_PARENT_ASSOCIATIONS).storeAs(EntityNames.STUDENT_PARENT_ASSOCIATION)
                .from(student, "getStudent", "getStudents").to(parent, "getParent", "getParents")
                .calledFromSource("getStudentParentAssociations").calledFromTarget("getStudentParentAssociations")
                .build();
        addDefinition(studentParentAssociation);

        AssociationDefinition studentDisciplineIncidentAssociation = factory
                .makeAssoc(EntityNames.STUDENT_DISCIPLINE_INCIDENT_ASSOCIATION, "studentDisciplineIncidentAssociations")
                .exposeAs(ResourceNames.STUDENT_DISCIPLINE_INCIDENT_ASSOCIATIONS)
                .storeAs(EntityNames.STUDENT_DISCIPLINE_INCIDENT_ASSOCIATION)
                .from(student, "getStudent", "getStudents")
                .to(disciplineIncident, "getDisciplineIncident", "getDisciplineIncidents")
                .calledFromSource("getStudentDisciplineIncidentAssociations")
                .calledFromTarget("getStudentDisciplineIncidentAssociations").build();
        addDefinition(studentDisciplineIncidentAssociation);

        AssociationDefinition studentProgramAssociation = factory.makeAssoc(EntityNames.STUDENT_PROGRAM_ASSOCIATION, "studentProgramAssociations")
                .exposeAs(ResourceNames.STUDENT_PROGRAM_ASSOCIATIONS).storeAs(EntityNames.STUDENT_PROGRAM_ASSOCIATION)
                .from(student, "getStudent", "getStudents").to(program, "getProgram", "getPrograms")
                .calledFromSource("getStudentProgramAssociations").calledFromTarget("getStudentProgramAssociations")
                .build();
        addDefinition(studentProgramAssociation);

        AssociationDefinition staffProgramAssociation = factory.makeAssoc(EntityNames.STAFF_PROGRAM_ASSOCIATION, "staffProgramAssociations")
                .exposeAs(ResourceNames.STAFF_PROGRAM_ASSOCIATIONS).storeAs(EntityNames.STAFF_PROGRAM_ASSOCIATION)
                .from(staff, "getStaff", "getStaff").to(program, "getProgram", "getPrograms")
                .calledFromSource("getStaffProgramAssociations").calledFromTarget("getStaffProgramAssociations")
                .build();
        addDefinition(staffProgramAssociation);

        AssociationDefinition studentCohortAssociation = factory.makeAssoc(EntityNames.STUDENT_COHORT_ASSOCIATION, "studentCohortAssociations")
                .exposeAs(ResourceNames.STUDENT_COHORT_ASSOCIATIONS).storeAs(EntityNames.STUDENT_COHORT_ASSOCIATION)
                .from(student, "getStudent", "getStudents").to(cohort, ResourceNames.COHORT_GETTER, "getCohorts")
                .calledFromSource(ResourceNames.STUDENT_COHORT_ASSOCIATIONS_GETTER)
                .calledFromTarget(ResourceNames.STUDENT_COHORT_ASSOCIATIONS_GETTER)
                .build();
        addDefinition(studentCohortAssociation);

        AssociationDefinition staffCohortAssociation = factory.makeAssoc(EntityNames.STAFF_COHORT_ASSOCIATION, "staffCohortAssociations")
                .exposeAs(ResourceNames.STAFF_COHORT_ASSOCIATIONS).storeAs(EntityNames.STAFF_COHORT_ASSOCIATION)
                .from(staff, "getStaff", "getStaff").to(cohort, ResourceNames.COHORT_GETTER, "getCohorts")
                .calledFromSource(ResourceNames.STAFF_COHORT_ASSOCIATIONS_GETTER)
                .calledFromTarget(ResourceNames.STAFF_COHORT_ASSOCIATIONS_GETTER)
                .build();
        addDefinition(staffCohortAssociation);

        // Adding the security collection
        EntityDefinition roles = factory.makeEntity("roles").storeAs("roles").build();
        addDefinition(roles);
        addDefinition(factory.makeEntity("realm").storeAs("realm").build());
        addDefinition(factory.makeEntity("authSession").build());

        // Adding the application collection
        addDefinition(factory.makeEntity("application").storeAs("application").build());
        addDefinition(factory.makeEntity("applicationAuthorization").storeAs("applicationAuthorization").build());

        this.registerDirectReferences();
    }

    /**
     * done last to avoid existence issues
     *
     */
    private void registerDirectReferences() {

        //
        LOG.debug("Registering direct entity references");

        int referencesLoaded = 0;

        // loop for each entity that is defined
        for (EntityDefinition referringDefinition : this.mapping.values()) {

            // loop for each reference field on the entity
            for (Entry<String, ReferenceSchema> fieldSchema : referringDefinition.getReferenceFields().entrySet()) {
                ReferenceSchema schema = fieldSchema.getValue(); // access to the reference schema
                String resource = ResourceNames.ENTITY_RESOURCE_NAME_MAPPING.get(schema.getResourceName());
                EntityDefinition referencedEntity = this.mapping.get(resource);
                LOG.debug(
                        "* New reference: {}.{} -> {}._id",
                        new Object[] { referringDefinition.getStoredCollectionName(), fieldSchema.getKey(),
                                schema.getResourceName() });
                // tell the referenced entity that some entity definition refers to it

                if (referencedEntity != null) {
                    referencedEntity.addReferencingEntity(referringDefinition);
                    referencesLoaded++;
                } else {
                    LOG.warn("* Failed to add, null entity: {}.{} -> {}._id",
                        new Object[] { referringDefinition.getStoredCollectionName(), fieldSchema.getKey(),
                                schema.getResourceName() });
                }
            }
        }

        // print stats
        LOG.debug("{} direct references loaded.", referencesLoaded);
    }

    public void addDefinition(EntityDefinition defn) {
        LOG.debug("adding definition for {}", defn.getResourceName());
        defn.setSchema(repo.getSchema(defn.getStoredCollectionName()));
        ResourceNames.ENTITY_RESOURCE_NAME_MAPPING.put(defn.getType(), defn.getResourceName());
        this.mapping.put(defn.getResourceName(), defn);
    }
}