/*
 * Copyright 2012-2013 inBloom, Inc. and its affiliates.
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
package org.slc.sli.bulk.extract.date;

import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

import org.slc.sli.domain.Entity;

/**
 * Verifies whether or not an Attendance entity should be extracted.
 *
 * @author tke tshewchuk
 */
@Component
public class AttendanceExtractVerifier implements ExtractVerifier {

    @Override
    public boolean shouldExtract(Entity entity, DateTime upToDate) {
        String schoolYear = null;

        if (entity.getBody().containsKey("schoolYearAttendance")) {
            schoolYear = (String) ((List<Map<String, Object>>) entity.getBody().get("schoolYearAttendance")).get(0).get("schoolYear");
        } else {
            schoolYear = EntityDateHelper.retrieveDate(entity);
        }

        return EntityDateHelper.isPastOrCurrentDate(schoolYear, upToDate, entity.getType());
    }

}
