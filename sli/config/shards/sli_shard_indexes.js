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

db["assessment"].ensureIndex({"metaData.tenantId" : 1, "_id" : 1});
db["attendance"].ensureIndex({"metaData.tenantId" : 1, "_id" : 1});
db["calendarDate"].ensureIndex({"metaData.tenantId" : 1, "_id" : 1});
db["cohort"].ensureIndex({"metaData.tenantId" : 1, "_id" : 1});
db["competencyLevelDescriptor"].ensureIndex({"metaData.tenantId" : 1, "_id" : 1});
db["course"].ensureIndex({"metaData.tenantId" : 1, "_id" : 1});
db["courseOffering"].ensureIndex({"metaData.tenantId" : 1, "_id" : 1});
db["courseSectionAssociation"].ensureIndex({"metaData.tenantId" : 1, "_id" : 1});
db["disciplineAction"].ensureIndex({"metaData.tenantId" : 1, "_id" : 1});
db["disciplineIncident"].ensureIndex({"metaData.tenantId" : 1, "_id" : 1});
db["educationOrganization"].ensureIndex({"metaData.tenantId" : 1, "_id" : 1});
db["educationOrganizationAssociation"].ensureIndex({"metaData.tenantId" : 1, "_id" : 1});
db["educationOrganizationSchoolAssociation"].ensureIndex({"metaData.tenantId" : 1, "_id" : 1});
db["grade"].ensureIndex({"metaData.tenantId" : 1, "_id" : 1});
db["gradebookEntry"].ensureIndex({"metaData.tenantId" : 1, "_id" : 1});
db["gradingPeriod"].ensureIndex({"metaData.tenantId" : 1, "_id" : 1});
db["graduationPlan"].ensureIndex({"metaData.tenantId" : 1, "_id" : 1});
db["learningObjective"].ensureIndex({"metaData.tenantId" : 1, "_id" : 1});
db["learningStandard"].ensureIndex({"metaData.tenantId" : 1, "_id" : 1});
db["parent"].ensureIndex({"metaData.tenantId" : 1, "_id" : 1});
db["program"].ensureIndex({"metaData.tenantId" : 1, "_id" : 1});
db["reportCard"].ensureIndex({"metaData.tenantId" : 1, "_id" : 1});
db["section"].ensureIndex({"metaData.tenantId" : 1, "_id" : 1});
db["sectionAssessmentAssociation"].ensureIndex({"metaData.tenantId" : 1, "_id" : 1});
db["sectionSchoolAssociation"].ensureIndex({"metaData.tenantId" : 1, "_id" : 1});
db["session"].ensureIndex({"metaData.tenantId" : 1, "_id" : 1});
db["sessionCourseAssociation"].ensureIndex({"metaData.tenantId" : 1, "_id" : 1});
db["staff"].ensureIndex({"metaData.tenantId" : 1, "_id" : 1});
db["staffCohortAssociation"].ensureIndex({"metaData.tenantId" : 1, "_id" : 1});
db["staffEducationOrganizationAssociation"].ensureIndex({"metaData.tenantId" : 1, "_id" : 1});
db["staffProgramAssociation"].ensureIndex({"metaData.tenantId" : 1, "_id" : 1});
db["student"].ensureIndex({"metaData.tenantId" : 1, "_id" : 1});
db["studentAcademicRecord"].ensureIndex({"metaData.tenantId" : 1, "_id" : 1});
db["studentAssessmentAssociation"].ensureIndex({"metaData.tenantId" : 1, "_id" : 1});
db["studentCohortAssociation"].ensureIndex({"metaData.tenantId" : 1, "_id" : 1});
db["studentCompetency"].ensureIndex({"metaData.tenantId" : 1, "_id" : 1});
db["studentCompetencyObjective"].ensureIndex({"metaData.tenantId" : 1, "_id" : 1});
db["studentDisciplineIncidentAssociation"].ensureIndex({"metaData.tenantId" : 1, "_id" : 1});
db["studentParentAssociation"].ensureIndex({"metaData.tenantId" : 1, "_id" : 1});
db["studentProgramAssociation"].ensureIndex({"metaData.tenantId" : 1, "_id" : 1});
db["studentSectionAssociation"].ensureIndex({"metaData.tenantId" : 1, "_id" : 1});
db["studentSectionGradebookEntry"].ensureIndex({"metaData.tenantId" : 1, "_id" : 1});
db["studentSchoolAssociation"].ensureIndex({"metaData.tenantId" : 1, "_id" : 1});
db["studentTranscriptAssociation"].ensureIndex({"metaData.tenantId" : 1, "_id" : 1});
db["teacherSchoolAssociation"].ensureIndex({"metaData.tenantId" : 1, "_id" : 1});
db["teacherSectionAssociation"].ensureIndex({"metaData.tenantId" : 1, "_id" : 1});
