db["application"].ensureIndex({"body.client_id":1});
db["application"].ensureIndex({"body.client_secret":1,"body.client_id":1});
db["application"].ensureIndex({"body.authorized_ed_orgs":1});
db["applicationAuthorization"].ensureIndex({"body.authId":1,"body.authType":1});
db["assessment"].ensureIndex({"metaData.tenantId":1, "metaData.externalId":1});
db["attendance"].ensureIndex({"metaData.tenantId":1, "body.studentId":1, "body.schoolId":1});
db["attendance"].ensureIndex({"body.schoolId":1});
db["attendance"].ensureIndex({"body.studentId":1,"body.schoolId":1});
db["cohort"].ensureIndex({"body.educationOrgId":1});
db["cohort"].ensureIndex({"body.programId":1});
db["course"].ensureIndex({"metaData.tenantId":1, "metaData.externalId":1});
db["courseSectionAssociation"].ensureIndex({"body.courseId":1});
db["courseSectionAssociation"].ensureIndex({"body.sectionId":1});
db["disciplineAction"].ensureIndex({"body.studentId":1});
db["disciplineAction"].ensureIndex({"body.disciplineIncidentId":1});
db["disciplineAction"].ensureIndex({"body.staffId":1});
db["disciplineAction"].ensureIndex({"body.responsibilitySchoolId":1});
db["disciplineAction"].ensureIndex({"body.assignmentSchoolId":1});
db["disciplineIncident"].ensureIndex({"body.schoolId":1});
db["disciplineIncident"].ensureIndex({"body.staffId":1});
db["educationOrganization"].ensureIndex({"_id":1,"type":1});
db["educationOrganization"].ensureIndex({"_id":1,"type":1,"body.parentEducationAgencyReference":1});
db["educationOrganization"].ensureIndex({"body.parentEducationAgencyReference":1,"type":1});
db["educationOrganization"].ensureIndex({"metaData.tenantId":1, "metaData.externalId":1});
db["educationOrganizationAssociation"].ensureIndex({"body.educationOrganizationParentId":1});
db["educationOrganizationAssociation"].ensureIndex({"body.educationOrganizationChildId":1});
db["educationOrganizationSchoolAssociation"].ensureIndex({"body.schoolId":1});
db["educationOrganizationSchoolAssociation"].ensureIndex({"body.educationOrganizationId":1});
db["gradebookEntry"].ensureIndex({"body.sectionId":1});
db["gradebookEntry"].ensureIndex({"metaData.tenantId":1, "metaData.externalId":1});
db["learningObjective"].ensureIndex({"body.learningStandards":1});
db["learningObjective"].ensureIndex({"body.parentLearningObjective":1});
db["parent"].ensureIndex({"metaData.tenantId":1, "metaData.externalId":1});
db["parent"].ensureIndex({"body.parentUniqueStateId":1});
db["program"].ensureIndex({"metaData.tenantId":1, "metaData.externalId":1});
db["program"].ensureIndex({"metaData.tenantId":1, "body.programId":1});
db["realm"].ensureIndex({"body.idp.id":1});
db["school"].ensureIndex({"body.parentEducationAgencyReference":1});
db["school"].ensureIndex({"metaData.tenantId":1, "metaData.externalId":1});
db["schoolSessionAssociation"].ensureIndex({"body.schoolId":1});
db["schoolSessionAssociation"].ensureIndex({"body.sessionId":1});
db["section"].ensureIndex({"body.courseId":1,"metaData.tenantId":1, "metaData.externalId":1});
db["section"].ensureIndex({"body.schoolId":1,"metaData.tenantId":1, "metaData.externalId":1});
db["section"].ensureIndex({"body.sessionId":1});
db["section"].ensureIndex({"body.programReference":1});
db["section"].ensureIndex({"metaData.tenantId":1, "metaData.externalId":1});
db["sectionAssessmentAssociation"].ensureIndex({"body.sectionId":1});
db["sectionAssessmentAssociation"].ensureIndex({"body.assessmentId":1});
db["sectionSchoolAssociation"].ensureIndex({"body.sectionId":1});
db["sectionSchoolAssociation"].ensureIndex({"body.schoolId":1});
db["session"].ensureIndex({"_id":1,"body.schoolYear":1});
db["session"].ensureIndex({"metaData.tenantId":1, "metaData.externalId":1});
db["sessionCourseAssociation"].ensureIndex({"body.courseId":1});
db["sessionCourseAssociation"].ensureIndex({"body.sessionId":1});
db["staff"].ensureIndex({"_id":1,"type":1});
db["staff"].ensureIndex({"metaData.tenantId":1, "metaData.externalId":1});
db["staff"].ensureIndex({"metaData.tenantId":1,"body.staffUniqueStateId":1});
db["staffCohortAssociation"].ensureIndex({"body.staffId":1});
db["staffCohortAssociation"].ensureIndex({"body.cohortId":1});
db["staffEducationOrganizationAssociation"].ensureIndex({"body.staffReference":1});
db["staffEducationOrganizationAssociation"].ensureIndex({"body.educationOrganizationReference":1});
db["staffEducationOrganizationAssociation"].ensureIndex({"metaData.tenantId":1, "metaData.externalId":1});
db["staffProgramAssociation"].ensureIndex({"body.staffId":1});
db["staffProgramAssociation"].ensureIndex({"body.programId":1});
db["student"].ensureIndex({"metaData.tenantId":1, "metaData.externalId":1});
db["studentAcademicRecord"].ensureIndex({"body.studentId":1});
db["studentAcademicRecord"].ensureIndex({"body.sessionId":1});
db["studentAssessmentAssociation"].ensureIndex({"body.studentId":1});
db["studentAssessmentAssociation"].ensureIndex({"body.assessmentId":1});
db["studentAssessmentAssociation"].ensureIndex({"metaData.tenantId":1, "metaData.externalId":1});
db["studentCohortAssociation"].ensureIndex({"body.studentId":1});
db["studentCohortAssociation"].ensureIndex({"body.cohortId":1});
db["studentDisciplineIncidentAssociation"].ensureIndex({"body.studentId":1});
db["studentDisciplineIncidentAssociation"].ensureIndex({"body.disciplineIncidentId":1});
db["studentParentAssociation"].ensureIndex({"_id":1,"body.studentId":1});
db["studentParentAssociation"].ensureIndex({"body.parentId":1, "body.studentId":1, "metaData.tenantId":1});
db["studentParentAssociation"].ensureIndex({"body.studentId":1,"body.parentId":1});
db["studentProgramAssociation"].ensureIndex({"body.studentId":1});
db["studentProgramAssociation"].ensureIndex({"body.programId":1});
db["studentProgramAssociation"].ensureIndex({"body.educationOrganizationId":1});
db["studentSectionGradebookEntry"].ensureIndex({"body.gradebookEntryId":1});
db["studentSectionGradebookEntry"].ensureIndex({"body.sectionId":1});
db["studentSectionGradebookEntry"].ensureIndex({"body.studentId":1});
db["studentSchoolAssociation"].ensureIndex({"body.schoolId":1, "metaData.tenantId":1, "metaData.externalId":1});
db["studentSchoolAssociation"].ensureIndex({"body.studentId":1, "metaData.tenantId":1, "metaData.externalId":1});
db["studentSchoolAssociation"].ensureIndex({"metaData.tenantId":1, "metaData.externalId":1});
db["studentSectionAssociation"].ensureIndex({"body.sectionId":1, "metaData.tenantId":1, "metaData.externalId":1});
db["studentSectionAssociation"].ensureIndex({"body.studentId":1, "metaData.tenantId":1, "body.sectionId":1});
db["studentSectionAssociation"].ensureIndex({"body.studentId":1, "metaData.tenantId":1, "metaData.externalId":1});
db["studentSectionGradebookEntry"].ensureIndex({"metaData.tenantId":1, "metaData.externalId":1});
db["studentTranscriptAssociation"].ensureIndex({"body.studentAcademicRecordId":1, "metaData.tenantId":1, "body.courseId":1});
db["studentTranscriptAssociation"].ensureIndex({"body.courseId":1});
db["teacher"].ensureIndex({"metaData.tenantId":1, "metaData.externalId":1});
db["teacher"].ensureIndex({"metaData.tenantId":1,"body.staffUniqueStateId":1});
db["teacherSchoolAssociation"].ensureIndex({"body.teacherId":1});
db["teacherSchoolAssociation"].ensureIndex({"body.schoolId":1});
db["teacherSectionAssociation"].ensureIndex({"_id":1,"body.teacherId":1});
db["teacherSectionAssociation"].ensureIndex({"_id":1,"body.sectionId":1});
db["teacherSectionAssociation"].ensureIndex({"body.sectionId":1});
db["teacherSectionAssociation"].ensureIndex({"body.teacherId":1, "metaData.tenantId":1, "body.sectionId":1});
db["userSession"].ensureIndex({"body.appSession.code.expiration":1,"body.appSession.clientId":1,"body.appSession.verified":1,"body.appSession.code.value":1}, {"name":"codeExpiration_clientId_verified_codeValue"});
db["userSession"].ensureIndex({"body.appSession.samlId":1});
db["userSession"].ensureIndex({"body.appSession.token":1});
db["userSession"].ensureIndex({"body.principal.externalId":1});
db["userSession"].ensureIndex({"body.expiration":1,"body.hardLogout":1,"body.appSession.token":1});
