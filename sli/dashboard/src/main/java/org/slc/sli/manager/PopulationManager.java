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


package org.slc.sli.manager;

import java.util.List;

import org.slc.sli.entity.Config;
import org.slc.sli.entity.GenericEntity;
import org.slc.sli.manager.Manager.EntityMapping;
import org.slc.sli.manager.Manager.EntityMappingManager;

/**
 *
 * @author dwu
 *
 */
@EntityMappingManager
public interface PopulationManager {

    /**
     * Get assessments taken by a group of students
     * @param token Toekn used to authenticate
     * @param studentSummaries Student information
     * @return unique set of assessment entities
     */
    public abstract List<GenericEntity> getAssessments(String token,
            List<GenericEntity> studentSummaries);

    /**
     * Get the list of student summaries identified by the student id list and authorized for the
     * security token
     *
     * @param token
     *            - the principle authentication token
     * @param studentIds
     *            - the student id list
     * @param sessionId
     *            - The id of the current session so you can get historical context.
     * @return studentList
     *         - the student summary entity list
     */
    public abstract List<GenericEntity> getStudentSummaries(String token,
            List<String> studentIds, String sessionId,
            String sectionId);

    /**
     * Get data for the list of students
     *
     * @return
     */
    @EntityMapping("listOfStudents")
    public abstract GenericEntity getListOfStudents(String token,
            Object sectionId, Config.Data config);

    public abstract void setEntityManager(EntityManager entityManager);

    /**
     * Get student entity
     *
     * @param token
     * @param studentId
     * @return
     */
    public abstract GenericEntity getStudent(String token, String studentId);

    /**
     * Get enriched student entity
     *
     * @param token
     * @param studentId
     * @param config
     * @return
     */
    @EntityMapping("student")
    public abstract GenericEntity getStudent(String token, Object studentId,
            Config.Data config);

    @EntityMapping("studentAttendance")
    public abstract GenericEntity getAttendance(String token,
            Object studentIdObj, Config.Data config);


    @EntityMapping("studentSearch")
    public abstract GenericEntity getStudentsBySearch(String token, Object nameQuery, Config.Data config);

    @EntityMapping("studentAssessment")
    public GenericEntity getAssessments(String token, Object id, Config.Data config);

    public abstract List<String> getSessionDates(String token, String sessionId);

}

