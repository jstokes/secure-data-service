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
// - learningObjective:body.parentLearningObjective
// - reportCard:body.grades
// - reportCard:body.studentCompetencyId
// - section:body.assessmentReferences
// - section:body.programReference
// - session:body.gradingPeriodReference
// - studentAcademicRecord:body.reportCards
//


//app, auth, realm
// db["adminDelegation"].ensureIndex({"metaData.tenantId":1,"_id":1});
// db["adminDelegation"].ensureIndex({"metaData.tenantId":1,"body.localEdOrgId":1,"body.appApprovalEnabled":1});  // admin rights to users

db["application"].ensureIndex({"body.admin_visible":1});  // ?
db["application"].ensureIndex({"body.allowed_for_all_edorgs":1});  //app auth per edorg
db["application"].ensureIndex({"body.authorized_ed_orgs":1});  //app auth per edorg
db["application"].ensureIndex({"body.authorized_for_all_edorgs":1});  //app auth per edorg
db["application"].ensureIndex({"body.client_id":1,"body.client_secret":1});  //app auth
// db["application"].ensureIndex({"body.name":1});

db["customRole"].ensureIndex({"body.realmId":1});  // create custom role for a realm

db["realm"].ensureIndex({"body.idp.id":1});  //oauth login
// db["realm"].ensureIndex({"body.uniqueIdentifier":1});
db["realm"].ensureIndex({"body.tenantId":1,"body.edOrg":1});  //login? saml fedaration
db["realm"].ensureIndex({"body.edOrg":1});  //create custom role for a realm

db["securityEvent"].ensureIndex({"body.targetEdOrg":1,"body.roles":1});

db["tenant"].ensureIndex({"body.landingZone.ingestionServer":1,"body.landingZone.preload.status":1});//ingestion-startup
db["tenant"].ensureIndex({"body.landingZone.path":1});  //ingestion-job start
db["tenant"].ensureIndex({"body.tenantId":1},{unique:true});  //ingestion-startup
// db["tenant"].ensureIndex({"type":1});

db["userSession"].ensureIndex({"body.appSession.code.value":1});  //oauth
db["userSession"].ensureIndex({"body.appSession.samlId":1});  //saml fedaration
// db["userSession"].ensureIndex({"body.appSession.token":1});
db["userSession"].ensureIndex({"body.expiration":1,"body.hardLogout":1,"body.appSession.token":1});  //api
db["userSession"].ensureIndex({"body.hardLogout":1,"body.expiration":1});  //api
db["userSession"].ensureIndex({"body.principal.externalId":1});  //api
// db["userSession"].ensureIndex({"body.principal.realm":1});
