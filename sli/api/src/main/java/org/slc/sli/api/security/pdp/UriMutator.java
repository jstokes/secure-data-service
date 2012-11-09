/*
 * Copyright 2012 Shared Learning Collaborative, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.slc.sli.api.security.pdp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ws.rs.core.PathSegment;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;

import org.slc.sli.api.constants.EntityNames;
import org.slc.sli.api.constants.ParameterConstants;
import org.slc.sli.api.constants.PathConstants;
import org.slc.sli.api.constants.ResourceNames;
import org.slc.sli.api.security.context.ResponseTooLargeException;
import org.slc.sli.api.security.context.resolver.EdOrgHelper;
import org.slc.sli.api.security.context.resolver.SectionHelper;
import org.slc.sli.domain.Entity;

/**
 * Infers context about the {user,requested resource} pair, and restricts blanket API calls to
 * smaller (and generally more manageable) scope.
 */
@Component
public class UriMutator {

    @Resource
    private EdOrgHelper edOrgHelper;

    @Resource
    private SectionHelper sectionHelper;

    /**
     * Acts as a filter to determine if the requested resource, given knowledge of the user
     * requesting it, should be rewritten. Returning null indicates that the URI should NOT be
     * rewritten.
     *
     * @param segments
     *            List of Path Segments representing request URI.
     * @param queryParameters
     *            String containing query parameters.
     * @param user
     *            User requesting resource.
     * @return Pair of {String, String} representing {mutated path (if necessary), mutated
     *         parameters (if necessary)}, where path or parameters will be null if they didn't need
     *         to be rewritten.
     */
    public Pair<String, String> mutate(List<PathSegment> segments, String queryParameters, Entity user) {
        String mutatedPath = null;
        String mutatedParameters = queryParameters;

        if (segments.size() < 3) {
            if (!shouldSkipMutationToEnableSearch(segments, queryParameters)) {
                Pair<String, String> mutated = mutateBaseUri(segments.get(1).getPath(), queryParameters, user);
                mutatedPath = mutated.getLeft();
                mutatedParameters = mutated.getRight();
            }
        } else {
            Pair<String, String> mutated = mutateUriAsNecessary(segments, queryParameters, user);
            mutatedPath = mutated.getLeft();
            mutatedParameters = mutated.getRight();
        }

        return Pair.of(mutatedPath, mutatedParameters);
    }

    private static Set<String> publicResourcesThatAllowSearch;

    @PostConstruct
    void init() {
        publicResourcesThatAllowSearch = new HashSet<String>(Arrays.asList(ResourceNames.EDUCATION_ORGANIZATIONS,
                ResourceNames.SCHOOLS));
    }

    private boolean shouldSkipMutationToEnableSearch(List<PathSegment> segments, String queryParameters) {
        boolean skipMutation = false;

        if (segments.size() < 3) {

            String[] queries = queryParameters != null ? queryParameters.split("&") : new String[0];
            for (String query : queries) {
                if (!query
                        .matches("(limit|offset|expandDepth|includeFields|excludeFields|sortBy|sortOrder|views|includeCustom|selector)=.+")) {
                    int BASE_RESOURCE_INDEX = 1;
                    if (segments.size() >= 2
                            && publicResourcesThatAllowSearch.contains(segments.get(BASE_RESOURCE_INDEX).getPath())) {
                        skipMutation = true;
                        break;
                    } else {
                        debug("Search request /{}?{}", segments.get(BASE_RESOURCE_INDEX).getPath(), queryParameters);
                    }
                }
            }

        }
        return skipMutation;
    }

    /**
     * Mutates the API call (not to a base entity) to a more-specific (and generally more
     * constrained) URI.
     *
     * @param segments
     *            List of Path Segments representing request URI.
     * @param queryParameters
     *            String containing query parameters.
     * @param user
     *            User requesting resource.
     * @return Pair of {String, String} representing {mutated path (if necessary), mutated
     *         parameters (if necessary)}, where path or parameters will be null if they didn't need
     *         to be rewritten.
     */
    private Pair<String, String> mutateUriAsNecessary(List<PathSegment> segments, String queryParameters, Entity user)
            throws ResponseTooLargeException {
        String mutatedPath = null;
        String mutatedParameters = queryParameters != null ? queryParameters : "";

        List<String> stringifiedSegments = stringifyPathSegments(segments);
        if (isTeacher(user)) {
            if (stringifiedSegments.size() == 4) {
                String baseEntity = stringifiedSegments.get(1);
                String transitiveEntityId = stringifiedSegments.get(2);
                String requestedEntity = stringifiedSegments.get(3);

                String modifiedRequest = reconnectPathSegments(Arrays.asList(baseEntity, requestedEntity));
                if (modifiedRequest.equals(PathConstants.ASSESSMENTS + ";"
                        + PathConstants.STUDENT_ASSESSMENT_ASSOCIATIONS + ";")) {
                    verifySingleTransitiveId(transitiveEntityId);
                    mutatedPath = String.format("/sections/%s/studentSectionAssociations/students/studentAssessments",
                            StringUtils.join(sectionHelper.getTeachersSections(user), ","));
                    mutatedParameters = mutuateQueryParameterString("assessmentId", transitiveEntityId,
                            mutatedParameters);
                } else if (modifiedRequest.equals(PathConstants.COURSES + ";" + PathConstants.COURSE_TRANSCRIPTS + ";")) {
                    verifySingleTransitiveId(transitiveEntityId);
                    mutatedPath = String.format("/sections/%s/studentSectionAssociations/students/courseTranscripts",
                            StringUtils.join(sectionHelper.getTeachersSections(user), ","));
                    mutatedParameters = mutuateQueryParameterString("courseId", transitiveEntityId, mutatedParameters);
                } else if (modifiedRequest.equals(PathConstants.COURSE_OFFERINGS + ";" + PathConstants.SECTIONS + ";")) {
                    verifySingleTransitiveId(transitiveEntityId);
                    mutatedPath = String.format("/sections/%s/",
                            StringUtils.join(sectionHelper.getTeachersSections(user), ","));
                    mutatedParameters = mutuateQueryParameterString("courseOfferingId", transitiveEntityId,
                            mutatedParameters);
                } else if (modifiedRequest.equals(PathConstants.EDUCATION_ORGANIZATIONS + ";" + PathConstants.COHORTS
                        + ";")) {
                    verifySingleTransitiveId(transitiveEntityId);
                    mutatedPath = String.format("/teachers/%s/staffCohortAssociations/cohorts", user.getEntityId());
                    mutatedParameters = mutuateQueryParameterString("educationOrgId", transitiveEntityId,
                            mutatedParameters);
                } else if (modifiedRequest.equals(PathConstants.GRADING_PERIODS + ";" + PathConstants.GRADES + ";")) {
                    verifySingleTransitiveId(transitiveEntityId);
                    mutatedPath = String.format("/sections/%s/studentSectionAssociations/grades",
                            StringUtils.join(sectionHelper.getTeachersSections(user), ","));
                    mutatedParameters = mutuateQueryParameterString("gradingPeriodId", transitiveEntityId,
                            mutatedParameters);
                } else if (modifiedRequest.equals(PathConstants.GRADING_PERIODS + ";" + PathConstants.REPORT_CARDS
                        + ";")) {
                    verifySingleTransitiveId(transitiveEntityId);
                    mutatedPath = String.format("/sections/%s/studentSectionAssociations/students/reportCards",
                            StringUtils.join(sectionHelper.getTeachersSections(user), ","));
                    mutatedParameters = mutuateQueryParameterString("gradingPeriodId", transitiveEntityId,
                            mutatedParameters);
                } else if (modifiedRequest.equals(PathConstants.LEARNING_OBJECTIVES + ";"
                        + PathConstants.STUDENT_COMPETENCIES + ";")) {
                    verifySingleTransitiveId(transitiveEntityId);
                    mutatedPath = String.format("/sections/%s/studentSectionAssociations/studentCompetencies",
                            StringUtils.join(sectionHelper.getTeachersSections(user), ","));
                    mutatedParameters = mutuateQueryParameterString("learningObjectiveId", transitiveEntityId,
                            mutatedParameters);
                } else if (modifiedRequest.equals(PathConstants.SESSIONS + ";" + PathConstants.SECTIONS + ";")) {
                    verifySingleTransitiveId(transitiveEntityId);
                    mutatedPath = String.format("/sections/%s/",
                            StringUtils.join(sectionHelper.getTeachersSections(user), ","));
                    mutatedParameters = mutuateQueryParameterString("sessionId", transitiveEntityId, mutatedParameters);
                } else if (modifiedRequest.equals(PathConstants.SESSIONS + ";" + PathConstants.STUDENT_ACADEMIC_RECORDS
                        + ";")) {
                    verifySingleTransitiveId(transitiveEntityId);
                    mutatedPath = String.format(
                            "/sections/%s/studentSectionAssociations/students/studentAcademicRecords",
                            StringUtils.join(sectionHelper.getTeachersSections(user), ","));
                    mutatedParameters = mutuateQueryParameterString("sessionId", transitiveEntityId, mutatedParameters);
                } else if (modifiedRequest.equals(PathConstants.SCHOOLS + ";"
                        + PathConstants.STUDENT_SCHOOL_ASSOCIATIONS + ";")) {
                    mutatedPath = String.format(
                            "/sections/%s/studentSectionAssociations/students/studentSchoolAssociations",
                            StringUtils.join(sectionHelper.getTeachersSections(user), ","));
                } else if (modifiedRequest.equals(PathConstants.SCHOOLS + ";" + PathConstants.SECTIONS + ";")) {
                    mutatedPath = String.format("/teachers/%s/teacherSectionAssociations/sections", user.getEntityId());
                }
            } else if (stringifiedSegments.size() == 5) {
                String baseEntity = stringifiedSegments.get(1);
                String transitiveEntity = stringifiedSegments.get(3);
                String requestedEntity = stringifiedSegments.get(4);

                String modifiedRequest = reconnectPathSegments(Arrays.asList(baseEntity, transitiveEntity,
                        requestedEntity));
                if (modifiedRequest.equals(PathConstants.SCHOOLS + ";" + PathConstants.SECTIONS + ";"
                        + PathConstants.GRADEBOOK_ENTRIES + ";")) {
                    mutatedPath = String.format("/sections/%s/gradebookEntries",
                            StringUtils.join(sectionHelper.getTeachersSections(user), ","));
                } else if (modifiedRequest.equals(PathConstants.SCHOOLS + ";" + PathConstants.SECTIONS + ";"
                        + PathConstants.STUDENT_SECTION_ASSOCIATIONS + ";")) {
                    mutatedPath = String.format("/sections/%s/studentSectionAssociations",
                            StringUtils.join(sectionHelper.getTeachersSections(user), ","));
                } else if (modifiedRequest.equals(PathConstants.SCHOOLS + ";"
                        + PathConstants.STUDENT_SCHOOL_ASSOCIATIONS + ";" + PathConstants.STUDENTS + ";")) {
                    mutatedPath = String.format("/sections/%s/studentSectionAssociations/students",
                            StringUtils.join(sectionHelper.getTeachersSections(user), ","));
                }
            } else if (stringifiedSegments.size() == 6) {
                String baseEntity = stringifiedSegments.get(1);
                String transitiveEntity1 = stringifiedSegments.get(3);
                String transitiveEntity2 = stringifiedSegments.get(4);
                String requestedEntity = stringifiedSegments.get(5);

                String modifiedRequest = reconnectPathSegments(Arrays.asList(baseEntity, transitiveEntity1,
                        transitiveEntity2, requestedEntity));
                if (modifiedRequest.equals(PathConstants.SCHOOLS + ";" + PathConstants.SECTIONS + ";"
                        + PathConstants.STUDENT_SECTION_ASSOCIATIONS + ";" + PathConstants.GRADES + ";")) {
                    mutatedPath = String.format("/sections/%s/studentSectionAssociations/grades",
                            StringUtils.join(sectionHelper.getTeachersSections(user), ","));
                } else if (modifiedRequest.equals(PathConstants.SCHOOLS + ";" + PathConstants.SECTIONS + ";"
                        + PathConstants.STUDENT_SECTION_ASSOCIATIONS + ";" + PathConstants.STUDENT_COMPETENCIES + ";")) {
                    mutatedPath = String.format("/sections/%s/studentSectionAssociations/studentCompetencies",
                            StringUtils.join(sectionHelper.getTeachersSections(user), ","));
                } else if (modifiedRequest.equals(PathConstants.SCHOOLS + ";"
                        + PathConstants.STUDENT_SCHOOL_ASSOCIATIONS + ";" + PathConstants.STUDENTS + ";"
                        + PathConstants.ATTENDANCES + ";")) {
                    mutatedPath = String.format("/sections/%s/studentSectionAssociations/students/attendances",
                            StringUtils.join(sectionHelper.getTeachersSections(user), ","));
                } else if (modifiedRequest.equals(PathConstants.SCHOOLS + ";"
                        + PathConstants.STUDENT_SCHOOL_ASSOCIATIONS + ";" + PathConstants.STUDENTS + ";"
                        + PathConstants.COURSE_TRANSCRIPTS + ";")) {
                    mutatedPath = String.format("/sections/%s/studentSectionAssociations/students/courseTranscripts",
                            StringUtils.join(sectionHelper.getTeachersSections(user), ","));
                } else if (modifiedRequest.equals(PathConstants.SCHOOLS + ";"
                        + PathConstants.STUDENT_SCHOOL_ASSOCIATIONS + ";" + PathConstants.STUDENTS + ";"
                        + PathConstants.REPORT_CARDS + ";")) {
                    mutatedPath = String.format("/sections/%s/studentSectionAssociations/students/reportCards",
                            StringUtils.join(sectionHelper.getTeachersSections(user), ","));
                } else if (modifiedRequest.equals(PathConstants.SCHOOLS + ";"
                        + PathConstants.STUDENT_SCHOOL_ASSOCIATIONS + ";" + PathConstants.STUDENTS + ";"
                        + PathConstants.STUDENT_ACADEMIC_RECORDS + ";")) {
                    mutatedPath = String.format(
                            "/sections/%s/studentSectionAssociations/students/studentAcademicRecords",
                            StringUtils.join(sectionHelper.getTeachersSections(user), ","));
                } else if (modifiedRequest.equals(PathConstants.SCHOOLS + ";"
                        + PathConstants.STUDENT_SCHOOL_ASSOCIATIONS + ";" + PathConstants.STUDENTS + ";"
                        + PathConstants.STUDENT_ASSESSMENT_ASSOCIATIONS + ";")) {
                    mutatedPath = String.format("/sections/%s/studentSectionAssociations/students/studentAssessments",
                            StringUtils.join(sectionHelper.getTeachersSections(user), ","));
                } else if (modifiedRequest.equals(PathConstants.SCHOOLS + ";"
                        + PathConstants.STUDENT_SCHOOL_ASSOCIATIONS + ";" + PathConstants.STUDENTS + ";"
                        + PathConstants.STUDENT_GRADEBOOK_ENTRIES + ";")) {
                    mutatedPath = String.format(
                            "/sections/%s/studentSectionAssociations/students/studentGradebookEntries",
                            StringUtils.join(sectionHelper.getTeachersSections(user), ","));
                } else if (modifiedRequest.equals(PathConstants.SCHOOLS + ";"
                        + PathConstants.STUDENT_SCHOOL_ASSOCIATIONS + ";" + PathConstants.STUDENTS + ";"
                        + PathConstants.STUDENT_PARENT_ASSOCIATIONS + ";")) {
                    mutatedPath = String.format(
                            "/sections/%s/studentSectionAssociations/students/studentParentAssociations",
                            StringUtils.join(sectionHelper.getTeachersSections(user), ","));
                } else if (modifiedRequest.equals(PathConstants.SCHOOLS + ";"
                        + PathConstants.TEACHER_SCHOOL_ASSOCIATIONS + ";" + PathConstants.TEACHERS + ";"
                        + PathConstants.TEACHER_SECTION_ASSOCIATIONS + ";")) {
                    mutatedPath = String.format("/teachers/%s/teacherSectionAssociations", user.getEntityId());
                }
            } else if (stringifiedSegments.size() == 7) {
                String baseEntity = stringifiedSegments.get(1);
                String transitiveEntity1 = stringifiedSegments.get(3);
                String transitiveEntity2 = stringifiedSegments.get(4);
                String transitiveEntity3 = stringifiedSegments.get(5);
                String requestedEntity = stringifiedSegments.get(6);

                String modifiedRequest = reconnectPathSegments(Arrays.asList(baseEntity, transitiveEntity1,
                        transitiveEntity2, transitiveEntity3, requestedEntity));
                if (modifiedRequest.equals(PathConstants.SCHOOLS + ";" + PathConstants.STUDENT_SCHOOL_ASSOCIATIONS
                        + ";" + PathConstants.STUDENTS + ";" + PathConstants.STUDENT_PARENT_ASSOCIATIONS + ";"
                        + PathConstants.PARENTS + ";")) {
                    mutatedPath = String.format(
                            "/sections/%s/studentSectionAssociations/students/studentParentAssociations/parents",
                            StringUtils.join(sectionHelper.getTeachersSections(user), ","));
                }
            }
        } else if (isStaff(user)) {
            if (stringifiedSegments.size() == 4) {
                String baseEntity = stringifiedSegments.get(1);
                String transitiveEntityId = stringifiedSegments.get(2);
                String requestedEntity = stringifiedSegments.get(3);

                String modifiedRequest = reconnectPathSegments(Arrays.asList(baseEntity, requestedEntity));
                if (modifiedRequest.equals(PathConstants.ASSESSMENTS + ";"
                        + PathConstants.STUDENT_ASSESSMENT_ASSOCIATIONS + ";")) {
                    verifySingleTransitiveId(transitiveEntityId);
                    mutatedPath = String.format("/schools/%s/studentSchoolAssociations/students/studentAssessments",
                            StringUtils.join(edOrgHelper.getDirectEdOrgAssociations(user), ","));
                    mutatedParameters = mutuateQueryParameterString("assessmentId", transitiveEntityId,
                            mutatedParameters);
                } else if (modifiedRequest.equals(PathConstants.COURSES + ";" + PathConstants.COURSE_TRANSCRIPTS + ";")) {
                    verifySingleTransitiveId(transitiveEntityId);
                    mutatedPath = String.format("/schools/%s/studentSchoolAssociations/students/courseTranscripts",
                            StringUtils.join(edOrgHelper.getDirectEdOrgAssociations(user), ","));
                    mutatedParameters = mutuateQueryParameterString("courseId", transitiveEntityId, mutatedParameters);
                } else if (modifiedRequest.equals(PathConstants.COURSE_OFFERINGS + ";" + PathConstants.SECTIONS + ";")) {
                    verifySingleTransitiveId(transitiveEntityId);
                    mutatedPath = String.format("/schools/%s/sections",
                            StringUtils.join(edOrgHelper.getDirectEdOrgAssociations(user), ","));
                    mutatedParameters = mutuateQueryParameterString("courseOfferingId", transitiveEntityId,
                            mutatedParameters);
                } else if (modifiedRequest.equals(PathConstants.GRADING_PERIODS + ";" + PathConstants.GRADES + ";")) {
                    verifySingleTransitiveId(transitiveEntityId);
                    mutatedPath = String.format("/schools/%s/sections/studentSectionAssociations/grades",
                            StringUtils.join(edOrgHelper.getDirectEdOrgAssociations(user), ","));
                    mutatedParameters = mutuateQueryParameterString("gradingPeriodId", transitiveEntityId,
                            mutatedParameters);
                } else if (modifiedRequest.equals(PathConstants.GRADING_PERIODS + ";" + PathConstants.REPORT_CARDS
                        + ";")) {
                    verifySingleTransitiveId(transitiveEntityId);
                    mutatedPath = String.format("/schools/%s/studentSchoolAssociations/students/reportCards",
                            StringUtils.join(edOrgHelper.getDirectEdOrgAssociations(user), ","));
                    mutatedParameters = mutuateQueryParameterString("gradingPeriodId", transitiveEntityId,
                            mutatedParameters);
                } else if (modifiedRequest.equals(PathConstants.SESSIONS + ";" + PathConstants.SECTIONS + ";")) {
                    verifySingleTransitiveId(transitiveEntityId);
                    mutatedPath = String.format("/schools/%s/sections",
                            StringUtils.join(edOrgHelper.getDirectEdOrgAssociations(user), ","));
                    mutatedParameters = mutuateQueryParameterString("sessionId", transitiveEntityId, mutatedParameters);
                } else if (modifiedRequest.equals(PathConstants.SESSIONS + ";" + PathConstants.STUDENT_ACADEMIC_RECORDS
                        + ";")) {
                    verifySingleTransitiveId(transitiveEntityId);
                    mutatedPath = String.format(
                            "/schools/%s/studentSchoolAssociations/students/studentAcademicRecords",
                            StringUtils.join(edOrgHelper.getDirectEdOrgAssociations(user), ","));
                    mutatedParameters = mutuateQueryParameterString("sessionId", transitiveEntityId, mutatedParameters);
                }
            }
        }

        return Pair.of(mutatedPath, mutatedParameters);
    }

    /**
     * Reconnects a list of path segments and returns a string representing the path traversed.
     *
     * @param segments
     *            List of Strings representing Path Segments.
     * @return String representing the list of Path Segments.
     */
    protected String reconnectPathSegments(List<String> segments) {
        StringBuilder builder = new StringBuilder();
        for (String segment : segments) {
            builder.append(segment).append(";");
        }
        return builder.toString();
    }

    /**
     * Mutates the existing query parameter string by pre-pending the _id of the transitive entity
     * that's part of the rewritten URI.
     *
     * @param transitiveEntityField
     *            Field used to identify transitive entity.
     * @param transitiveEntityId
     *            UUID of the transitive entity.
     * @param existingParameters
     *            Existing query parameter string.
     * @return String representing new query parameter string.
     */
    protected String mutuateQueryParameterString(String transitiveEntityField, String transitiveEntityId,
            String existingParameters) {
        if (existingParameters != null) {
            return transitiveEntityField + "=" + transitiveEntityId + "&" + existingParameters;
        } else {
            return transitiveEntityField + "=" + transitiveEntityId;
        }
    }

    /**
     * Throws Response Too Large exception if there are multiple _id's specified in the transitive
     * _id path segment.
     *
     * @param id
     *            String representing transitive _id path segment.
     * @throws ResponseTooLargeException
     *             Thrown if multiple _id's are specified (only one should be specified).
     */
    protected void verifySingleTransitiveId(String id) throws ResponseTooLargeException {
        if (id.split(",").length > 1) {
            throw new ResponseTooLargeException();
        }
    }

    /**
     * Stringifies the specified list of path segments into a list of strings.
     *
     * @param segments
     *            List of Path Segments.
     * @return List of Strings representing the input list of Path Segments.
     */
    protected List<String> stringifyPathSegments(List<PathSegment> segments) {
        List<String> stringified = new ArrayList<String>();
        if (segments != null && !segments.isEmpty()) {
            for (PathSegment segment : segments) {
                stringified.add(segment.getPath());
            }
        }
        return stringified;
    }

    /**
     * Mutates the API call (to a base entity) to a more-specific (and generally more constrained)
     * URI.
     *
     * @param resource
     *            root resource being accessed.
     * @param user
     *            entity representing user making API call.
     * @return Mutated String representing new API call, or null if no mutation takes place.
     */
    public Pair<String, String> mutateBaseUri(String resource, String queryParameters, Entity user) {
        if (queryParameters == null) {
            queryParameters = "";
        }

        boolean success = true;
        boolean isMutated = false;
        String mutatedPath = null;
        String mutatedParameters = queryParameters != null ? queryParameters : "";

        String[] queries = queryParameters.split("&");
        if (!isMutated && queryParameters.matches("^studentId=.+")) {
            for (String query : queries) {
                if (query.matches("^studentId=.+")) {
                    int INDEX_OF_QUERY_VALUE = 10;
                    String ids = query.substring(INDEX_OF_QUERY_VALUE);
                    mutatedPath = "/" + ResourceNames.STUDENTS + "/" + ids + "/" + resource;
                    mutatedParameters = queryParameters.replaceFirst(query, "");
                    isMutated = true;
                }
            }
        }
        if (!isMutated && queryParameters.matches("^schoolId=.+")) {
            for (String query : queries) {
                if (query.matches("^schoolId=.+")) {
                    int INDEX_OF_QUERY_VALUE = 9;
                    String ids = query.substring(INDEX_OF_QUERY_VALUE);
                    mutatedPath = "/" + ResourceNames.SCHOOLS + "/" + ids + "/" + resource;
                    mutatedParameters = queryParameters.replaceFirst(query, "");
                    isMutated = true;
                }
            }
        }
        if (!isMutated && queryParameters.matches("^staffReference=.+")) {
            for (String query : queries) {
                if (query.matches("^staffReference=.+")) {
                    int INDEX_OF_QUERY_VALUE = 15;
                    String ids = query.substring(INDEX_OF_QUERY_VALUE);
                    mutatedPath = "/" + ResourceNames.STAFF + "/" + ids + "/" + resource;
                    mutatedParameters = queryParameters.replaceFirst(query, "");
                    isMutated = true;
                }
            }
        }
        if (!isMutated && queryParameters.matches("^teacherId=.+")) {
            for (String query : queries) {
                if (query.matches("^teacherId=.+")) {
                    int INDEX_OF_QUERY_VALUE = 10;
                    String ids = query.substring(INDEX_OF_QUERY_VALUE);
                    mutatedPath = "/" + ResourceNames.TEACHERS + "/" + ids + "/" + resource;
                    mutatedParameters = queryParameters.replaceFirst(query, "");
                    isMutated = true;
                }
            }
        }
        if (!isMutated && queryParameters.matches("^studentSectionAssociationId=.+")) {
            for (String query : queries) {
                if (query.matches("^studentSectionAssociationId=.+")) {
                    int INDEX_OF_QUERY_VALUE = 28;
                    String ids = query.substring(INDEX_OF_QUERY_VALUE);
                    mutatedPath = "/" + ResourceNames.STUDENT_SECTION_ASSOCIATIONS + "/" + ids + "/" + resource;
                    mutatedParameters = queryParameters.replaceFirst(query, "");
                    isMutated = true;
                }
            }
        }


        if (!isMutated && isTeacher(user)) {
            if (ResourceNames.ASSESSMENTS.equals(resource)
                    || ResourceNames.COMPETENCY_LEVEL_DESCRIPTORS.equals(resource)
                    || ResourceNames.COMPETENCY_LEVEL_DESCRIPTOR_TYPES.equals(resource)
                    || ResourceNames.HOME.equals(resource) || ResourceNames.LEARNINGOBJECTIVES.equals(resource)
                    || ResourceNames.LEARNINGSTANDARDS.equals(resource)) {
                mutatedPath = "/" + resource;
            } else if (ResourceNames.ATTENDANCES.equals(resource)) {
                if (mutatedParameters.contains(ParameterConstants.SECTION_ID)) {
                    return formQueryBasedOnParameter("/sections/%s/studentSectionAssociations/students/attendances",
                            mutatedParameters, ParameterConstants.SECTION_ID);
                } else {
                    mutatedPath = String.format("/sections/%s/studentSectionAssociations/students/attendances",
                            StringUtils.join(sectionHelper.getTeachersSections(user), ","));
                }
            } else if (ResourceNames.COHORTS.equals(resource)) {
                mutatedPath = String.format("/staff/%s/staffCohortAssociations/cohorts", user.getEntityId());
            } else if (ResourceNames.COURSES.equals(resource)) {
                mutatedPath = String.format("/schools/%s/courses",
                        StringUtils.join(edOrgHelper.getDirectEdOrgAssociations(user), ","));
            } else if (ResourceNames.COURSE_OFFERINGS.equals(resource)) {
                mutatedPath = String.format("/schools/%s/courseOfferings",
                        StringUtils.join(edOrgHelper.getDirectEdOrgAssociations(user), ","));
            } else if (ResourceNames.COURSE_TRANSCRIPTS.equals(resource)) {
                if (mutatedParameters.contains(ParameterConstants.SECTION_ID)) {
                    return formQueryBasedOnParameter(
                            "/sections/%s/studentSectionAssociations/students/courseTranscripts", mutatedParameters,
                            ParameterConstants.SECTION_ID);
                } else {
                    String ids = StringUtils.join(sectionHelper.getTeachersSections(user), ",");
                    mutatedPath = String.format("/sections/%s/studentSectionAssociations/students/courseTranscripts",
                            ids);
                }
            } else if (ResourceNames.DISCIPLINE_ACTIONS.equals(resource)) {
                mutatedPath = String.format("/staff/%s/disciplineActions", user.getEntityId());
            } else if (ResourceNames.DISCIPLINE_INCIDENTS.equals(resource)) {
                mutatedPath = String.format("/staff/%s/disciplineIncidents", user.getEntityId());
            } else if (ResourceNames.EDUCATION_ORGANIZATIONS.equals(resource)) {
                mutatedPath = String.format("/teachers/%s/teacherSchoolAssociations/schools", user.getEntityId());
            } else if (ResourceNames.GRADES.equals(resource)) {
                if (mutatedParameters.contains(ParameterConstants.SECTION_ID)) {
                    return formQueryBasedOnParameter("/sections/%s/studentSectionAssociations/grades",
                            mutatedParameters, ParameterConstants.SECTION_ID);
                } else {
                    String ids = StringUtils.join(sectionHelper.getTeachersSections(user), ",");
                    mutatedPath = String.format("/sections/%s/studentSectionAssociations/grades", ids);
                }
            } else if (ResourceNames.GRADING_PERIODS.equals(resource)) {
                mutatedPath = String.format("/schools/%s/sessions/gradingPeriods",
                        StringUtils.join(edOrgHelper.getDirectEdOrgAssociations(user), ","));
            } else if (ResourceNames.GRADEBOOK_ENTRIES.equals(resource)) {
                if (mutatedParameters.contains(ParameterConstants.SECTION_ID)) {
                    return formQueryBasedOnParameter("/sections/%s/gradebookEntries", mutatedParameters,
                            ParameterConstants.SECTION_ID);
                } else {
                    mutatedPath = String.format("/sections/%s/gradebookEntries",
                            StringUtils.join(sectionHelper.getTeachersSections(user), ","));
                }
            } else if (ResourceNames.PARENTS.equals(resource)) {
                if (mutatedParameters.contains(ParameterConstants.SECTION_ID)) {
                    return formQueryBasedOnParameter(
                            "/sections/%s/studentSectionAssociations/students/studentParentAssociations/parents",
                            mutatedParameters, ParameterConstants.SECTION_ID);
                } else {
                    mutatedPath = String.format(
                            "/sections/%s/studentSectionAssociations/students/studentParentAssociations/parents",
                            StringUtils.join(sectionHelper.getTeachersSections(user), ","));
                }
            } else if (ResourceNames.PROGRAMS.equals(resource)) {
                mutatedPath = String.format("/staff/%s/staffProgramAssociations/programs", user.getEntityId());
            } else if (ResourceNames.REPORT_CARDS.equals(resource)) {
                if (mutatedParameters.contains(ParameterConstants.SECTION_ID)) {
                    return formQueryBasedOnParameter("/sections/%s/studentSectionAssociations/students/reportCards",
                            mutatedParameters, ParameterConstants.SECTION_ID);
                } else {
                    String ids = StringUtils.join(sectionHelper.getTeachersSections(user), ",");
                    mutatedPath = String.format("/sections/%s/studentSectionAssociations/students/reportCards", ids);
                }
            } else if (ResourceNames.SECTIONS.equals(resource)) {
                mutatedPath = String.format("/teachers/%s/teacherSectionAssociations/sections", user.getEntityId());
            } else if (ResourceNames.SCHOOLS.equals(resource)) {
                List<String> ids = edOrgHelper.getDirectSchools(user);
                mutatedPath = String.format("/schools/%s/", StringUtils.join(edOrgHelper.getDirectSchools(user), ","));
                success = !ids.isEmpty();
            } else if (ResourceNames.SESSIONS.equals(resource)) {
                mutatedPath = String.format("/educationOrganizations/%s/sessions",
                        StringUtils.join(edOrgHelper.getDirectEdOrgAssociations(user), ","));
            } else if (ResourceNames.STAFF.equals(resource)) {
                mutatedPath = String.format("/educationOrganizations/%s/staffEducationOrgAssignmentAssociations/staff",
                        StringUtils.join(edOrgHelper.getDirectEdOrgAssociations(user), ","));
            } else if (ResourceNames.STAFF_COHORT_ASSOCIATIONS.equals(resource)) {
                mutatedPath = String.format("/staff/%s/staffCohortAssociations", user.getEntityId());
            } else if (ResourceNames.STAFF_EDUCATION_ORGANIZATION_ASSOCIATIONS.equals(resource)) {
                mutatedPath = String.format("/educationOrganizations/%s/staffEducationOrgAssignmentAssociations",
                        StringUtils.join(edOrgHelper.getDirectEdOrgAssociations(user), ","));
            } else if (ResourceNames.STAFF_PROGRAM_ASSOCIATIONS.equals(resource)) {
                mutatedPath = String.format("/staff/%s/staffProgramAssociations", user.getEntityId());
            } else if (ResourceNames.STUDENTS.equals(resource)) {
                if (mutatedParameters.contains(ParameterConstants.SECTION_ID)) {
                    return formQueryBasedOnParameter("/sections/%s/studentSectionAssociations/students",
                            mutatedParameters, ParameterConstants.SECTION_ID);
                } else {
                    String ids = StringUtils.join(sectionHelper.getTeachersSections(user), ",");
                    mutatedPath = String.format("/sections/%s/studentSectionAssociations/students", ids);
                }
            } else if (ResourceNames.STUDENT_ACADEMIC_RECORDS.equals(resource)) {
                if (mutatedParameters.contains(ParameterConstants.SECTION_ID)) {
                    return formQueryBasedOnParameter(
                            "/sections/%s/studentSectionAssociations/students/studentAcademicRecords",
                            mutatedParameters, ParameterConstants.SECTION_ID);
                } else {
                    String ids = StringUtils.join(sectionHelper.getTeachersSections(user), ",");
                    mutatedPath = String.format(
                            "/sections/%s/studentSectionAssociations/students/studentAcademicRecords", ids);
                }
            } else if (ResourceNames.STUDENT_ASSESSMENT_ASSOCIATIONS.equals(resource)) {
                if (mutatedParameters.contains(ParameterConstants.SECTION_ID)) {
                    return formQueryBasedOnParameter(
                            "/sections/%s/studentSectionAssociations/students/studentAssessments", mutatedParameters,
                            ParameterConstants.SECTION_ID);
                } else {
                    String ids = StringUtils.join(sectionHelper.getTeachersSections(user), ",");
                    mutatedPath = String.format("/sections/%s/studentSectionAssociations/students/studentAssessments",
                            ids);
                }
            } else if (ResourceNames.STUDENT_COHORT_ASSOCIATIONS.equals(resource)) {
                mutatedPath = String.format("/staff/%s/staffCohortAssociations/cohorts/studentCohortAssociations",
                        user.getEntityId());
            } else if (ResourceNames.STUDENT_DISCIPLINE_INCIDENT_ASSOCIATIONS.equals(resource)) {
                mutatedPath = String.format("/staff/%s/disciplineIncidents/studentDisciplineIncidentAssociations",
                        user.getEntityId());
            } else if (ResourceNames.STUDENT_COMPETENCIES.equals(resource)) {
                if (mutatedParameters.contains(ParameterConstants.SECTION_ID)) {
                    return formQueryBasedOnParameter("/sections/%s/studentSectionAssociations/studentCompetencies",
                            mutatedParameters, ParameterConstants.SECTION_ID);
                } else {
                    String ids = StringUtils.join(sectionHelper.getTeachersSections(user), ",");
                    mutatedPath = String.format("/sections/%s/studentSectionAssociations/studentCompetencies", ids);
                }
            } else if (ResourceNames.STUDENT_COMPETENCY_OBJECTIVES.equals(resource)) {
                mutatedPath = String.format("/educationOrganizations/%s/studentCompetencyObjectives",
                        StringUtils.join(edOrgHelper.getDirectEdOrgAssociations(user), ","));
            } else if (ResourceNames.STUDENT_GRADEBOOK_ENTRIES.equals(resource)) {
                if (mutatedParameters.contains(ParameterConstants.SECTION_ID)) {
                    return formQueryBasedOnParameter(
                            "/sections/%s/studentSectionAssociations/students/studentGradebookEntries",
                            mutatedParameters, ParameterConstants.SECTION_ID);
                } else {
                    String ids = StringUtils.join(sectionHelper.getTeachersSections(user), ",");
                    mutatedPath = String.format(
                            "/sections/%s/studentSectionAssociations/students/studentGradebookEntries", ids);
                }
            } else if (ResourceNames.STUDENT_PARENT_ASSOCIATIONS.equals(resource)) {
                if (mutatedParameters.contains(ParameterConstants.SECTION_ID)) {
                    return formQueryBasedOnParameter(
                            "/sections/%s/studentSectionAssociations/students/studentParentAssociations",
                            mutatedParameters, ParameterConstants.SECTION_ID);
                } else {
                    String ids = StringUtils.join(sectionHelper.getTeachersSections(user), ",");
                    mutatedPath = String.format(
                            "/sections/%s/studentSectionAssociations/students/studentParentAssociations", ids);
                }
            } else if (ResourceNames.STUDENT_PROGRAM_ASSOCIATIONS.equals(resource)) {
                mutatedPath = String.format("/staff/%s/staffProgramAssociations/programs/studentProgramAssociations",
                        user.getEntityId());
            } else if (ResourceNames.STUDENT_SCHOOL_ASSOCIATIONS.equals(resource)) {
                if (mutatedParameters.contains(ParameterConstants.SECTION_ID)) {
                    return formQueryBasedOnParameter(
                            "/sections/%s/studentSectionAssociations/students/studentSchoolAssociations",
                            mutatedParameters, ParameterConstants.SECTION_ID);
                } else {
                    String ids = StringUtils.join(sectionHelper.getTeachersSections(user), ",");
                    mutatedPath = String.format(
                            "sections/%s/studentSectionAssociations/students/studentSchoolAssociations", ids);
                }
            } else if (ResourceNames.STUDENT_SECTION_ASSOCIATIONS.equals(resource)) {
                if (mutatedParameters.contains(ParameterConstants.SECTION_ID)) {
                    return formQueryBasedOnParameter("/sections/%s/studentSectionAssociations", mutatedParameters,
                            ParameterConstants.SECTION_ID);
                } else {
                    mutatedPath = String.format("/sections/%s/studentSectionAssociations",
                            StringUtils.join(sectionHelper.getTeachersSections(user), ","));
                }
            } else if (ResourceNames.TEACHERS.equals(resource)) {
                mutatedPath = String.format("/schools/%s/teacherSchoolAssociations/teachers",
                        StringUtils.join(edOrgHelper.getDirectEdOrgAssociations(user), ","));
            } else if (ResourceNames.TEACHER_SCHOOL_ASSOCIATIONS.equals(resource)) {
                mutatedPath = String.format("/teachers/%s/teacherSchoolAssociations", user.getEntityId());
            } else if (ResourceNames.TEACHER_SECTION_ASSOCIATIONS.equals(resource)) {
                mutatedPath = String.format("/teachers/%s/teacherSectionAssociations", user.getEntityId());
            }
        } else if (!isMutated && isStaff(user)) {
            if (ResourceNames.ASSESSMENTS.equals(resource)
                    || ResourceNames.COMPETENCY_LEVEL_DESCRIPTORS.equals(resource)
                    || ResourceNames.COMPETENCY_LEVEL_DESCRIPTOR_TYPES.equals(resource)
                    || ResourceNames.HOME.equals(resource) || ResourceNames.LEARNINGOBJECTIVES.equals(resource)
                    || ResourceNames.LEARNINGSTANDARDS.equals(resource)) {
                mutatedPath = "/" + resource;
            } else if (ResourceNames.ATTENDANCES.equals(resource)) {
                String ids = StringUtils.join(edOrgHelper.getDirectEdOrgAssociations(user), ",");
                mutatedPath = String.format("/schools/%s/studentSchoolAssociations/students/attendances", ids);
            } else if (ResourceNames.COHORTS.equals(resource)) {
                mutatedPath = String.format("/staff/%s/staffCohortAssociations/cohorts", user.getEntityId());
            } else if (ResourceNames.COURSES.equals(resource)) {
                mutatedPath = String.format("/schools/%s/courses",
                        StringUtils.join(edOrgHelper.getDirectEdOrgAssociations(user), ","));
            } else if (ResourceNames.COURSE_OFFERINGS.equals(resource)) {
                mutatedPath = String.format("/schools/%s/courseOfferings",
                        StringUtils.join(edOrgHelper.getDirectEdOrgAssociations(user), ","));
            } else if (ResourceNames.COURSE_TRANSCRIPTS.equals(resource)) {
                String ids = StringUtils.join(edOrgHelper.getDirectEdOrgAssociations(user), ",");
                mutatedPath = String.format("/schools/%s/studentSchoolAssociations/students/courseTranscripts", ids);
            } else if (ResourceNames.DISCIPLINE_ACTIONS.equals(resource)) {
                mutatedPath = String.format("/staff/%s/disciplineActions", user.getEntityId());
            } else if (ResourceNames.DISCIPLINE_INCIDENTS.equals(resource)) {
                mutatedPath = String.format("/staff/%s/disciplineIncidents", user.getEntityId());
            } else if (ResourceNames.EDUCATION_ORGANIZATIONS.equals(resource)) {
                mutatedPath = String.format("/staff/%s/staffEducationOrgAssignmentAssociations/educationOrganizations",
                        user.getEntityId());
            } else if (ResourceNames.GRADES.equals(resource)) {
                String ids = StringUtils.join(edOrgHelper.getDirectEdOrgAssociations(user), ",");
                mutatedPath = String.format("/schools/%s/sections/studentSectionAssociations/grades", ids);
            } else if (ResourceNames.GRADING_PERIODS.equals(resource)) {
                mutatedPath = String.format("/schools/%s/sessions/gradingPeriods",
                        StringUtils.join(edOrgHelper.getDirectEdOrgAssociations(user), ","));
            } else if (ResourceNames.GRADEBOOK_ENTRIES.equals(resource)) {
                mutatedPath = String.format("/schools/%s/sections/gradebookEntries",
                        StringUtils.join(edOrgHelper.getDirectEdOrgAssociations(user), ","));
            } else if (ResourceNames.PARENTS.equals(resource)) {
                String ids = StringUtils.join(edOrgHelper.getDirectEdOrgAssociations(user), ",");
                mutatedPath = String.format(
                        "/schools/%s/studentSchoolAssociations/students/studentParentAssociations/parents", ids);
            } else if (ResourceNames.PROGRAMS.equals(resource)) {
                mutatedPath = String.format("/staff/%s/staffProgramAssociations/programs", user.getEntityId());
            } else if (ResourceNames.REPORT_CARDS.equals(resource)) {
                String ids = StringUtils.join(edOrgHelper.getDirectEdOrgAssociations(user), ",");
                mutatedPath = String.format("/schools/%s/studentSchoolAssociations/students/reportCards", ids);
            } else if (ResourceNames.SCHOOLS.equals(resource)) {
                List<String> ids = edOrgHelper.getDirectSchools(user);
                mutatedPath = String.format("/schools/%s/", StringUtils.join(edOrgHelper.getDirectSchools(user), ","));
                success = !ids.isEmpty();
            } else if (ResourceNames.SECTIONS.equals(resource)) {
                String ids = StringUtils.join(edOrgHelper.getDirectEdOrgAssociations(user), ",");
                mutatedPath = String.format("/schools/%s/sections", ids);
            } else if (ResourceNames.SESSIONS.equals(resource)) {
                if (mutatedParameters.contains(ParameterConstants.SCHOOL_ID)) {
                    return formQueryBasedOnParameter("/educationOrganizations/%s/sessions", mutatedParameters,
                            ParameterConstants.SCHOOL_ID);
                } else {
                    mutatedPath = String.format("/educationOrganizations/%s/sessions",
                            StringUtils.join(edOrgHelper.getDirectEdOrgAssociations(user), ","));
                }
            } else if (ResourceNames.STAFF.equals(resource)) {
                String ids = StringUtils.join(edOrgHelper.getDirectEdOrgAssociations(user), ",");
                mutatedPath = String.format("/educationOrganizations/%s/staffEducationOrgAssignmentAssociations/staff",
                        ids);
            } else if (ResourceNames.STAFF_COHORT_ASSOCIATIONS.equals(resource)) {
                mutatedPath = String.format("/staff/%s/staffCohortAssociations", user.getEntityId());
            } else if (ResourceNames.STAFF_EDUCATION_ORGANIZATION_ASSOCIATIONS.equals(resource)) {
                mutatedPath = String.format("/staff/%s/staffEducationOrgAssignmentAssociations", user.getEntityId());
            } else if (ResourceNames.STAFF_PROGRAM_ASSOCIATIONS.equals(resource)) {
                mutatedPath = String.format("/staff/%s/staffProgramAssociations", user.getEntityId());
            } else if (ResourceNames.STUDENTS.equals(resource)) {
                String ids = StringUtils.join(edOrgHelper.getDirectEdOrgAssociations(user), ",");
                mutatedPath = String.format("/schools/%s/studentSchoolAssociations/students", ids);
            } else if (ResourceNames.STUDENT_ACADEMIC_RECORDS.equals(resource)) {
                String ids = StringUtils.join(edOrgHelper.getDirectEdOrgAssociations(user), ",");
                mutatedPath = String.format("/schools/%s/studentSchoolAssociations/students/studentAcademicRecords",
                        ids);
            } else if (ResourceNames.STUDENT_ASSESSMENT_ASSOCIATIONS.equals(resource)) {
                String ids = StringUtils.join(edOrgHelper.getDirectEdOrgAssociations(user), ",");
                mutatedPath = String.format("/schools/%s/studentSchoolAssociations/students/studentAssessments", ids);
            } else if (ResourceNames.STUDENT_COHORT_ASSOCIATIONS.equals(resource)) {
                mutatedPath = String.format("/staff/%s/staffCohortAssociations/cohorts/studentCohortAssociations",
                        user.getEntityId());
            } else if (ResourceNames.STUDENT_COMPETENCIES.equals(resource)) {
                String ids = StringUtils.join(edOrgHelper.getDirectEdOrgAssociations(user), ",");
                mutatedPath = String.format("/schools/%s/sections/studentSectionAssociations/studentCompetencies", ids);
            } else if (ResourceNames.STUDENT_COMPETENCY_OBJECTIVES.equals(resource)) {
                mutatedPath = String.format("/educationOrganizations/%s/studentCompetencyObjectives",
                        StringUtils.join(edOrgHelper.getDirectEdOrgAssociations(user), ","));
            } else if (ResourceNames.STUDENT_DISCIPLINE_INCIDENT_ASSOCIATIONS.equals(resource)) {
                mutatedPath = String.format("/staff/%s/disciplineIncidents/studentDisciplineIncidentAssociations",
                        user.getEntityId());
            } else if (ResourceNames.STUDENT_GRADEBOOK_ENTRIES.equals(resource)) {
                String ids = StringUtils.join(edOrgHelper.getDirectEdOrgAssociations(user), ",");
                mutatedPath = String.format("/schools/%s/studentSchoolAssociations/students/studentGradebookEntries",
                        ids);
            } else if (ResourceNames.STUDENT_PARENT_ASSOCIATIONS.equals(resource)) {
                String ids = StringUtils.join(edOrgHelper.getDirectEdOrgAssociations(user), ",");
                mutatedPath = String.format("/schools/%s/studentSchoolAssociations/students/studentParentAssociations",
                        ids);
            } else if (ResourceNames.STUDENT_PROGRAM_ASSOCIATIONS.equals(resource)) {
                mutatedPath = String.format("/staff/%s/staffProgramAssociations/programs/studentProgramAssociations",
                        user.getEntityId());
            } else if (ResourceNames.STUDENT_SCHOOL_ASSOCIATIONS.equals(resource)) {
                String ids = StringUtils.join(edOrgHelper.getDirectEdOrgAssociations(user), ",");
                mutatedPath = String.format("/schools/%s/studentSchoolAssociations", ids);
            } else if (ResourceNames.STUDENT_SECTION_ASSOCIATIONS.equals(resource)) {
                String ids = StringUtils.join(edOrgHelper.getDirectEdOrgAssociations(user), ",");
                mutatedPath = String.format("/schools/%s/sections/studentSectionAssociations", ids);
            } else if (ResourceNames.TEACHERS.equals(resource)) {
                String ids = StringUtils.join(edOrgHelper.getDirectEdOrgAssociations(user), ",");
                mutatedPath = String.format("/schools/%s/teacherSchoolAssociations/teachers", ids);
            } else if (ResourceNames.TEACHER_SCHOOL_ASSOCIATIONS.equals(resource)) {
                String ids = StringUtils.join(edOrgHelper.getDirectEdOrgAssociations(user), ",");
                mutatedPath = String.format("/schools/%s/teacherSchoolAssociations", ids);
            } else if (ResourceNames.PROGRAMS.equals(resource)) {
                mutatedPath = String.format("/staff/%s/staffProgramAssociations/programs", user.getEntityId());
            } else if (ResourceNames.PARENTS.equals(resource)) {
                mutatedPath = String.format(
                        "/schools/%s/studentSchoolAssociations/students/studentParentAssociations/parents",
                        StringUtils.join(edOrgHelper.getDirectEdOrgAssociations(user), ","));
            } else if (ResourceNames.TEACHER_SECTION_ASSOCIATIONS.equals(resource)) {
                String ids = StringUtils.join(edOrgHelper.getDirectEdOrgAssociations(user), ",");
                mutatedPath = String.format(
                        "/schools/%s/teacherSchoolAssociations/teachers/teacherSectionAssociations", ids);
            }
        }

        if (!success) {
            error("Context Inferrence Failed");
            throw new ContextInferrenceFailedException();
        }

        return Pair.of(mutatedPath, mutatedParameters);
    }

    /**
     * Determines if the entity is a teacher.
     *
     * @param principal
     *            User making API call.
     * @return True if principal is a teacher, false otherwise.
     */
    private boolean isTeacher(Entity principal) {
        return principal.getType().equals(EntityNames.TEACHER);
    }

    /**
     * Determines if the entity is a staff member.
     *
     * @param principal
     *            User making API call.
     * @return True if principal is a staff member, false otherwise.
     */
    private boolean isStaff(Entity principal) {
        return principal.getType().equals(EntityNames.STAFF);
    }

    private Pair<String, String> formQueryBasedOnParameter(String path, String parameters, String parameter) {
        String mutatedPath = null;
        String mutatedParameters = null;

        String[] queryParameters = parameters.split("&");
        for (int i = 0; i < queryParameters.length; i++) {
            String queryParameter = queryParameters[i];
            String[] values = queryParameter.split("=");
            if (values.length == 2) {
                if (values[0].equals(parameter) && values[1] != null && !values[1].isEmpty()) {
                    mutatedPath = String.format(path, values[1]);
                    mutatedParameters = removeQueryParameter(parameters, parameter);
                    break;
                }
            }
        }

        return Pair.of(mutatedPath, mutatedParameters);
    }

    private String removeQueryParameter(String parameters, String queryParameterToRemove) {
        if (parameters == null || parameters.isEmpty()) {
            return parameters;
        }

        StringBuilder builder = new StringBuilder();
        String[] queryParameters = parameters.split("&");
        for (String queryParameter : queryParameters) {
            if (!queryParameter.startsWith(queryParameterToRemove)) {
                builder.append(queryParameter).append("&");
            }
        }

        if (builder.length() > 0) {
            return builder.substring(0, builder.length() - 2);
        }
        return "";
    }

    /**
     * Inject section helper (for unit testing).
     *
     * @param sectionHelper
     *            resolver for tying entity to sections.
     */
    protected void setSectionHelper(SectionHelper sectionHelper) {
        this.sectionHelper = sectionHelper;
    }

    /**
     * Inject education organization helper (for unit testing).
     *
     * @param edOrgHelper
     *            resolver for tying entity to education organizations.
     */
    protected void setEdOrgHelper(EdOrgHelper edOrgHelper) {
        this.edOrgHelper = edOrgHelper;
    }
}
