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

package org.slc.sli.api.security.context.resolver;

import org.slc.sli.domain.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Contains helper methods to traverse to the sections of a given user
 *
 */
@Component
public class SectionHelper {

    @Autowired
    private TeacherStudentResolver teacherStudentResolver;
    /**
     * Traverse the edorg hierarchy and find all the SEAs the user is associated with, directly or indirectly.
     *
     * @param teacher
     * @return a list of sections for the teacher
     */
    public List<String> getTeachersSections(Entity teacher) {
        return teacherStudentResolver.getTeachersSectionIds(teacher);
    }


}
