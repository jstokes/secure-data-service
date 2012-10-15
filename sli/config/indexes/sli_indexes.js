//
// Copyright 2012 Shared Learning Collaborative, LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//


//
// Run the indexing script with data in collections to test for errors.
//
// Indexing Gotchas:
// - Long index names
// - Parallel indexes: creating an index key with more than one field that is an array
// - Redundant indexes: {a,b,c} makes {a,b}, {a} redundant
//
// Known problem fields for parallel indexes: (no index key with more
// than one of these)
// These can be found in ComplexTypex.xsd
// xpath=//xs:element[@type="reference"][@maxOccurs="unbounded"]
// - *:metaData.edOrgs
// - *:metaData.teacherContext
// - cohort:body.programId
// - disciplineAction:body.disciplineIncidentId
// - disciplineAction:body.staffId
// - disciplineAction:body.studentId
// - learningObjective:body.learningStandards
// - reportCard:body.grades
// - reportCard:body.studentCompetencyId
// - section:body.programReference
// - section:body.assessmentReference
// - session:body.gradingPeriodreference
// - staffCohortAssociation:body.cohortId
// - staffCohortAssociation:body.staffId
// - staffProgramAssociation:body.programId
// - staffProgramAssociation:body.staffId
//


//app, auth, realm
db["adminDelegation"].ensureIndex({"metaData.tenantId":1,"_id":1});
db["adminDelegation"].ensureIndex({"metaData.tenantId":1,"body.localEdOrgId":1,"body.appApprovalEnabled":1});

db["application"].ensureIndex({"body.admin_visible":1});
db["application"].ensureIndex({"body.allowed_for_all_edorgs":1});
db["application"].ensureIndex({"body.authorized_ed_orgs":1});
db["application"].ensureIndex({"body.authorized_for_all_edorgs":1});
db["application"].ensureIndex({"body.client_secret":1,"body.client_id":1});
db["application"].ensureIndex({"body.name":1});

db["applicationAuthorization"].ensureIndex({"body.appIds":1});
db["applicationAuthorization"].ensureIndex({"body.authId":1});
db["applicationAuthorization"].ensureIndex({"metaData.tenantId":1,"body.authId":1,"body.authType":1});

db["customRole"].ensureIndex({"metaData.tenantId":1,"_id":1});
db["customRole"].ensureIndex({"metaData.tenantId":1,"body.realmId":1});

db["realm"].ensureIndex({"body.idp.id":1});
db["realm"].ensureIndex({"body.uniqueIdentifier":1});
db["realm"].ensureIndex({"metaData.tenantId":1,"body.edOrg":1});

db["securityEvent"].ensureIndex({"metaData.tenantId":1,"body.targetEdOrg":1,"body.roles":1});

db["tenant"].ensureIndex({"body.landingZone.ingestionServer":1,"body.landingZone.preload.status":1});
db["tenant"].ensureIndex({"body.landingZone.path":1});
db["tenant"].ensureIndex({"body.tenantId":1});
db["tenant"].ensureIndex({"type":1});

db["userSession"].ensureIndex({"body.appSession.code.value":1});
db["userSession"].ensureIndex({"body.appSession.samlId":1});
db["userSession"].ensureIndex({"body.appSession.token":1});
db["userSession"].ensureIndex({"body.expiration":1,"body.hardLogout":1,"body.appSession.token":1});
db["userSession"].ensureIndex({"body.principal.externalId":1});
db["userSession"].ensureIndex({"body.principal.realm":1});


//custom entities
db["custom_entities"].ensureIndex({"metaData.entityId":1});
db["custom_entities"].ensureIndex({"metaData.tenantId":1,"metaData.entityId":1,"metaData.clientId":1});


//direct references - index on each direct reference
//edOrgs field can be removed when staff stamper goes away
db["attendance"].ensureIndex({"metaData.tenantId":1,"body.schoolId":1,"metaData.edOrgs":1});
db["attendance"].ensureIndex({"metaData.tenantId":1,"body.studentId":1,"metaData.edOrgs":1});
db["cohort"].ensureIndex({"metaData.tenantId":1,"body.educationOrgId":1,"metaData.edOrgs":1});
db["cohort"].ensureIndex({"metaData.tenantId":1,"body.programId":1,"metaData.edOrgs":1});
db["course"].ensureIndex({"metaData.tenantId":1,"body.schoolId":1,"metaData.edOrgs":1});
db["courseOffering"].ensureIndex({"metaData.tenantId":1,"body.courseId":1,"metaData.edOrgs":1});
db["courseOffering"].ensureIndex({"metaData.tenantId":1,"body.schoolId":1,"metaData.edOrgs":1});
db["courseOffering"].ensureIndex({"metaData.tenantId":1,"body.sessionId":1,"metaData.edOrgs":1});
db["disciplineAction"].ensureIndex({"metaData.tenantId":1,"body.assignmentSchoolId":1,"metaData.edOrgs":1});
db["disciplineAction"].ensureIndex({"metaData.tenantId":1,"body.disciplineIncidentId":1,"metaData.edOrgs":1});
db["disciplineAction"].ensureIndex({"metaData.tenantId":1,"body.responsibilitySchoolId":1,"metaData.edOrgs":1});
db["disciplineAction"].ensureIndex({"metaData.tenantId":1,"body.staffId":1,"metaData.edOrgs":1});
db["disciplineAction"].ensureIndex({"metaData.tenantId":1,"body.studentId":1,"metaData.edOrgs":1});
db["disciplineIncident"].ensureIndex({"metaData.tenantId":1,"body.schoolId":1,"metaData.edOrgs":1});
db["disciplineIncident"].ensureIndex({"metaData.tenantId":1,"body.staffId":1,"metaData.edOrgs":1});
db["educationOrganization"].ensureIndex({"metaData.tenantId":1,"body.parentEducationAgencyReference":1,"metaData.edOrgs":1});
db["grade"].ensureIndex({"metaData.tenantId":1,"body.gradingPeriodId":1,"metaData.edOrgs":1});
db["grade"].ensureIndex({"metaData.tenantId":1,"body.studentSectionAssociationId":1,"metaData.edOrgs":1});
db["gradebookEntry"].ensureIndex({"metaData.tenantId":1,"body.sectionId":1,"metaData.edOrgs":1});
db["graduationPlan"].ensureIndex({"metaData.tenantId":1,"body.educationOrganizationId":1,"metaData.edOrgs":1});
db["learningObjective"].ensureIndex({"metaData.tenantId":1,"body.learningStandards":1,"metaData.edOrgs":1});
db["learningObjective"].ensureIndex({"metaData.tenantId":1,"body.parentLearningObjective":1,"metaData.edOrgs":1});
db["reportCard"].ensureIndex({"metaData.tenantId":1,"body.grades":1,"metaData.edOrgs":1});
db["reportCard"].ensureIndex({"metaData.tenantId":1,"body.gradingPeriodId":1,"metaData.edOrgs":1});
db["reportCard"].ensureIndex({"metaData.tenantId":1,"body.studentCompetencyId":1,"metaData.edOrgs":1});
db["reportCard"].ensureIndex({"metaData.tenantId":1,"body.studentId":1,"metaData.edOrgs":1});
db["section"].ensureIndex({"metaData.tenantId":1,"body.assessmentReferences":1,"metaData.edOrgs":1});
db["section"].ensureIndex({"metaData.tenantId":1,"body.courseOfferingId":1,"metaData.edOrgs":1});
db["section"].ensureIndex({"metaData.tenantId":1,"body.programReference":1,"metaData.edOrgs":1});
db["section"].ensureIndex({"metaData.tenantId":1,"body.schoolId":1,"metaData.edOrgs":1});
db["section"].ensureIndex({"metaData.tenantId":1,"body.sessionId":1,"metaData.edOrgs":1});
db["session"].ensureIndex({"metaData.tenantId":1,"body.gradingPeriodReference":1,"metaData.edOrgs":1});
db["session"].ensureIndex({"metaData.tenantId":1,"body.schoolId":1,"metaData.edOrgs":1});
db["staffCohortAssociation"].ensureIndex({"metaData.tenantId":1,"body.cohortId":1,"metaData.edOrgs":1});
db["staffCohortAssociation"].ensureIndex({"metaData.tenantId":1,"body.staffId":1,"metaData.edOrgs":1});
db["staffEducationOrganizationAssociation"].ensureIndex({"metaData.tenantId":1,"body.educationOrganizationReference":1,"metaData.edOrgs":1});
db["staffEducationOrganizationAssociation"].ensureIndex({"metaData.tenantId":1,"body.staffReference":1,"metaData.edOrgs":1});
db["staffProgramAssociation"].ensureIndex({"metaData.tenantId":1,"body.programId":1,"metaData.edOrgs":1});
db["staffProgramAssociation"].ensureIndex({"metaData.tenantId":1,"body.staffId":1,"metaData.edOrgs":1});
db["studentAcademicRecord"].ensureIndex({"metaData.tenantId":1,"body.reportCards":1,"metaData.edOrgs":1});
db["studentAcademicRecord"].ensureIndex({"metaData.tenantId":1,"body.sessionId":1,"metaData.edOrgs":1});
db["studentAcademicRecord"].ensureIndex({"metaData.tenantId":1,"body.studentId":1,"metaData.edOrgs":1});
//studentAssessmentAssociation is embedded into student
db["student"].ensureIndex({"metaData.tenantId":1,"studentAssessmentAssociation.assessmentId":1,"metaData.edOrgs":1});
db["student"].ensureIndex({"metaData.tenantId":1,"studentAssessmentAssociation.studentId":1,"metaData.edOrgs":1});
db["studentCohortAssociation"].ensureIndex({"metaData.tenantId":1,"body.cohortId":1,"metaData.edOrgs":1});
db["studentCohortAssociation"].ensureIndex({"metaData.tenantId":1,"body.studentId":1,"metaData.edOrgs":1});
db["studentCompetency"].ensureIndex({"metaData.tenantId":1,"body.learningObjectiveId":1,"metaData.edOrgs":1});
db["studentCompetency"].ensureIndex({"metaData.tenantId":1,"body.studentCompetencyObjectiveId":1,"metaData.edOrgs":1});
db["studentCompetency"].ensureIndex({"metaData.tenantId":1,"body.studentSectionAssociationId":1,"metaData.edOrgs":1});
db["studentCompetencyObjective"].ensureIndex({"metaData.tenantId":1,"body.educationOrganizationId":1,"metaData.edOrgs":1});
db["studentDisciplineIncidentAssociation"].ensureIndex({"metaData.tenantId":1,"body.disciplineIncidentId":1,"metaData.edOrgs":1});
db["studentDisciplineIncidentAssociation"].ensureIndex({"metaData.tenantId":1,"body.studentId":1,"metaData.edOrgs":1});
db["studentGradebookEntry"].ensureIndex({"metaData.tenantId":1,"body.gradebookEntryId":1,"metaData.edOrgs":1});
db["studentGradebookEntry"].ensureIndex({"metaData.tenantId":1,"body.sectionId":1,"metaData.edOrgs":1});
db["studentGradebookEntry"].ensureIndex({"metaData.tenantId":1,"body.studentId":1,"metaData.edOrgs":1});
db["studentGradebookEntry"].ensureIndex({"metaData.tenantId":1,"body.studentSectionAssociationId":1,"metaData.edOrgs":1});
db["studentParentAssociation"].ensureIndex({"metaData.tenantId":1,"body.parentId":1,"metaData.edOrgs":1});
db["studentParentAssociation"].ensureIndex({"metaData.tenantId":1,"body.studentId":1,"metaData.edOrgs":1});
db["studentProgramAssociation"].ensureIndex({"metaData.tenantId":1,"body.educationOrganizationId":1,"metaData.edOrgs":1});
db["studentProgramAssociation"].ensureIndex({"metaData.tenantId":1,"body.programId":1,"metaData.edOrgs":1});
db["studentProgramAssociation"].ensureIndex({"metaData.tenantId":1,"body.studentId":1,"metaData.edOrgs":1});
db["studentSchoolAssociation"].ensureIndex({"metaData.tenantId":1,"body.graduationPlanId":1,"metaData.edOrgs":1});
db["studentSchoolAssociation"].ensureIndex({"metaData.tenantId":1,"body.schoolId":1,"metaData.edOrgs":1});
db["studentSchoolAssociation"].ensureIndex({"metaData.tenantId":1,"body.studentId":1,"metaData.edOrgs":1});
//studentSectionAssociation is embedded into section
db["section"].ensureIndex({"metaData.tenantId":1,"studentSectionAssociation.sectionId":1,"metaData.edOrgs":1});
db["section"].ensureIndex({"metaData.tenantId":1,"studentSectionAssociation.studentId":1,"metaData.edOrgs":1});
db["studentTranscriptAssociation"].ensureIndex({"metaData.tenantId":1,"body.courseId":1,"metaData.edOrgs":1});
db["studentTranscriptAssociation"].ensureIndex({"metaData.tenantId":1,"body.studentAcademicRecordId":1,"metaData.edOrgs":1});
db["studentTranscriptAssociation"].ensureIndex({"metaData.tenantId":1,"body.studentId":1,"metaData.edOrgs":1});
db["teacherSchoolAssociation"].ensureIndex({"metaData.tenantId":1,"body.schoolId":1,"metaData.edOrgs":1});
db["teacherSchoolAssociation"].ensureIndex({"metaData.tenantId":1,"body.teacherId":1,"metaData.edOrgs":1});
db["teacherSectionAssociation"].ensureIndex({"metaData.tenantId":1,"body.sectionId":1,"metaData.edOrgs":1});
db["teacherSectionAssociation"].ensureIndex({"metaData.tenantId":1,"body.teacherId":1,"metaData.edOrgs":1});


//sharding --> sharded on { metaData.tenantId, _id }
//most of these are redundant, but REQUIRED for sharding
db["assessment"].ensureIndex({"metaData.tenantId":1,"_id":1});
db["attendance"].ensureIndex({"metaData.tenantId":1,"_id":1});
db["calendarDate"].ensureIndex({"metaData.tenantId":1,"_id":1});
db["cohort"].ensureIndex({"metaData.tenantId":1,"_id":1});
db["competencyLevelDescriptor"].ensureIndex({"metaData.tenantId":1,"_id":1});
db["course"].ensureIndex({"metaData.tenantId":1,"_id":1});
db["courseOffering"].ensureIndex({"metaData.tenantId":1,"_id":1});
db["courseSectionAssociation"].ensureIndex({"metaData.tenantId":1,"_id":1});
db["disciplineAction"].ensureIndex({"metaData.tenantId":1,"_id":1});
db["disciplineIncident"].ensureIndex({"metaData.tenantId":1,"_id":1});
db["educationOrganization"].ensureIndex({"metaData.tenantId":1,"_id":1});
db["educationOrganizationAssociation"].ensureIndex({"metaData.tenantId":1,"_id":1});
db["educationOrganizationSchoolAssociation"].ensureIndex({"metaData.tenantId":1,"_id":1});
db["grade"].ensureIndex({"metaData.tenantId":1,"_id":1});
db["gradebookEntry"].ensureIndex({"metaData.tenantId":1,"_id":1});
db["gradingPeriod"].ensureIndex({"metaData.tenantId":1,"_id":1});
db["graduationPlan"].ensureIndex({"metaData.tenantId":1,"_id":1});
db["learningObjective"].ensureIndex({"metaData.tenantId":1,"_id":1});
db["learningStandard"].ensureIndex({"metaData.tenantId":1,"_id":1});
db["parent"].ensureIndex({"metaData.tenantId":1,"_id":1});
db["program"].ensureIndex({"metaData.tenantId":1,"_id":1});
db["reportCard"].ensureIndex({"metaData.tenantId":1,"_id":1});
db["section"].ensureIndex({"metaData.tenantId":1,"_id":1});
db["session"].ensureIndex({"metaData.tenantId":1,"_id":1});
db["staff"].ensureIndex({"metaData.tenantId":1,"_id":1});
db["staffCohortAssociation"].ensureIndex({"metaData.tenantId":1,"_id":1});
db["staffEducationOrganizationAssociation"].ensureIndex({"metaData.tenantId":1,"_id":1});
db["staffProgramAssociation"].ensureIndex({"metaData.tenantId":1,"_id":1});
db["student"].ensureIndex({"metaData.tenantId":1,"_id":1});
db["studentAcademicRecord"].ensureIndex({"metaData.tenantId":1,"_id":1});
//studentAssessmentAssociation not sharded, but index on _id
db["student"].ensureIndex({"metaData.tenantId":1,"studentAssessmentAssociation._id":1});
db["studentCohortAssociation"].ensureIndex({"metaData.tenantId":1,"_id":1});
db["studentCompetency"].ensureIndex({"metaData.tenantId":1,"_id":1});
db["studentCompetencyObjective"].ensureIndex({"metaData.tenantId":1,"_id":1});
db["studentDisciplineIncidentAssociation"].ensureIndex({"metaData.tenantId":1,"_id":1});
db["studentGradebookEntry"].ensureIndex({"metaData.tenantId":1,"_id":1});
db["studentParentAssociation"].ensureIndex({"metaData.tenantId":1,"_id":1});
db["studentProgramAssociation"].ensureIndex({"metaData.tenantId":1,"_id":1});
db["studentSchoolAssociation"].ensureIndex({"metaData.tenantId":1,"_id":1});
//studentSectionAssociation is not sharded, but index on _id
db["section"].ensureIndex({"metaData.tenantId":1,"studentSectionAssociation._id":1});
db["studentTranscriptAssociation"].ensureIndex({"metaData.tenantId":1,"_id":1});
db["teacherSchoolAssociation"].ensureIndex({"metaData.tenantId":1,"_id":1});
db["teacherSectionAssociation"].ensureIndex({"metaData.tenantId":1,"_id":1});


//staff context resolver access - stamped edOrgs
//can be removed when staff stamper goes away
db["assessment"].ensureIndex({"metaData.tenantId":1,"metaData.edOrgs":1});
db["attendance"].ensureIndex({"metaData.tenantId":1,"metaData.edOrgs":1});
db["calendarDate"].ensureIndex({"metaData.tenantId":1,"metaData.edOrgs":1});
db["cohort"].ensureIndex({"metaData.tenantId":1,"metaData.edOrgs":1});
db["competencyLevelDescriptor"].ensureIndex({"metaData.tenantId":1,"metaData.edOrgs":1});
db["course"].ensureIndex({"metaData.tenantId":1,"metaData.edOrgs":1});
db["courseOffering"].ensureIndex({"metaData.tenantId":1,"metaData.edOrgs":1});
db["courseSectionAssociation"].ensureIndex({"metaData.tenantId":1,"metaData.edOrgs":1});
db["disciplineAction"].ensureIndex({"metaData.tenantId":1,"metaData.edOrgs":1});
db["disciplineIncident"].ensureIndex({"metaData.tenantId":1,"metaData.edOrgs":1});
db["educationOrganization"].ensureIndex({"metaData.tenantId":1,"metaData.edOrgs":1});
db["educationOrganizationAssociation"].ensureIndex({"metaData.tenantId":1,"metaData.edOrgs":1});
db["educationOrganizationSchoolAssociation"].ensureIndex({"metaData.tenantId":1,"metaData.edOrgs":1});
db["grade"].ensureIndex({"metaData.tenantId":1,"metaData.edOrgs":1});
db["gradebookEntry"].ensureIndex({"metaData.tenantId":1,"metaData.edOrgs":1});
db["gradingPeriod"].ensureIndex({"metaData.tenantId":1,"metaData.edOrgs":1});
db["graduationPlan"].ensureIndex({"metaData.tenantId":1,"metaData.edOrgs":1});
db["learningObjective"].ensureIndex({"metaData.tenantId":1,"metaData.edOrgs":1});
db["learningStandard"].ensureIndex({"metaData.tenantId":1,"metaData.edOrgs":1});
db["parent"].ensureIndex({"metaData.tenantId":1,"metaData.edOrgs":1});
db["program"].ensureIndex({"metaData.tenantId":1,"metaData.edOrgs":1});
db["reportCard"].ensureIndex({"metaData.tenantId":1,"metaData.edOrgs":1});
db["section"].ensureIndex({"metaData.tenantId":1,"metaData.edOrgs":1});
db["session"].ensureIndex({"metaData.tenantId":1,"metaData.edOrgs":1});
db["staff"].ensureIndex({"metaData.tenantId":1,"metaData.edOrgs":1});
db["staffCohortAssociation"].ensureIndex({"metaData.tenantId":1,"metaData.edOrgs":1});
db["staffEducationOrganizationAssociation"].ensureIndex({"metaData.tenantId":1,"metaData.edOrgs":1});
db["staffProgramAssociation"].ensureIndex({"metaData.tenantId":1,"metaData.edOrgs":1});
db["student"].ensureIndex({"metaData.tenantId":1,"metaData.edOrgs":1});
db["studentAcademicRecord"].ensureIndex({"metaData.tenantId":1,"metaData.edOrgs":1});
db["studentCohortAssociation"].ensureIndex({"metaData.tenantId":1,"metaData.edOrgs":1});
db["studentCompetency"].ensureIndex({"metaData.tenantId":1,"metaData.edOrgs":1});
db["studentCompetencyObjective"].ensureIndex({"metaData.tenantId":1,"metaData.edOrgs":1});
db["studentDisciplineIncidentAssociation"].ensureIndex({"metaData.tenantId":1,"metaData.edOrgs":1});
db["studentGradebookEntry"].ensureIndex({"metaData.tenantId":1,"metaData.edOrgs":1});
db["studentParentAssociation"].ensureIndex({"metaData.tenantId":1,"metaData.edOrgs":1});
db["studentProgramAssociation"].ensureIndex({"metaData.tenantId":1,"metaData.edOrgs":1});
db["studentSchoolAssociation"].ensureIndex({"metaData.tenantId":1,"metaData.edOrgs":1});
db["studentTranscriptAssociation"].ensureIndex({"metaData.tenantId":1,"metaData.edOrgs":1});
db["teacherSchoolAssociation"].ensureIndex({"metaData.tenantId":1,"metaData.edOrgs":1});
db["teacherSectionAssociation"].ensureIndex({"metaData.tenantId":1,"metaData.edOrgs":1});


//profiled - ingestion
db["attendance"].ensureIndex({"metaData.tenantId":1,"body.studentId":1,"body.schoolId":1});
db["educationOrganization"].ensureIndex({"metaData.tenantId":1,"body.stateOrganizationId":1});
db["section"].ensureIndex({"studentSectionAssociation._id":1});
db["student"].ensureIndex({"metaData.tenantId":1,"body.studentUniqueStateId":1});


//oprhan detection - this should be removed when done in API
db["educationOrganization"].ensureIndex({"metaData.tenantId":1,"metaData.isOrphaned":1});
