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

package org.slc.sli.sif.translation;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import openadk.library.common.EntryType;
import openadk.library.common.ExitType;
import openadk.library.common.GradeLevel;
import openadk.library.student.StudentSchoolEnrollment;

import org.springframework.beans.factory.annotation.Autowired;

import org.slc.sli.sif.domain.converter.EntryTypeConverter;
import org.slc.sli.sif.domain.converter.ExitTypeConverter;
import org.slc.sli.sif.domain.converter.GradeLevelsConverter;
import org.slc.sli.sif.domain.converter.SchoolYearConverter;
import org.slc.sli.sif.domain.slientity.StudentSchoolAssociationEntity;
import org.slc.sli.sif.slcinterface.SifIdResolver;

/**
 * Translation task for translating StudentSchoolEnrollment SIF data objects
 * to StudentSchoolAssociation SLI entities.
 */
public class StudentSchoolEnrollmentTranslationTask extends
        AbstractTranslationTask<StudentSchoolEnrollment, StudentSchoolAssociationEntity> {

    @Autowired
    SifIdResolver sifIdResolver;

    @Autowired
    SchoolYearConverter schoolYearConverter;

    @Autowired
    GradeLevelsConverter gradeLevelsConverter;

    @Autowired
    EntryTypeConverter entryTypeConverter;

    @Autowired
    ExitTypeConverter exitTypeConverter;

    public StudentSchoolEnrollmentTranslationTask() {
        super(StudentSchoolEnrollment.class);
    }

    @Override
    public List<StudentSchoolAssociationEntity> doTranslate(StudentSchoolEnrollment sifData) {
        StudentSchoolEnrollment sse = sifData;
        StudentSchoolAssociationEntity result = new StudentSchoolAssociationEntity();

        String sifStudentRefId = sse.getStudentPersonalRefId();
        String sliStudentGuid = sifIdResolver.getSliGuid(sifStudentRefId);
        result.setStudentId(sliStudentGuid);
        String sifSchoolRefId = sse.getSchoolInfoRefId();
        String sliSchoolGuid = sifIdResolver.getSliGuid(sifSchoolRefId);
        result.setSchoolId(sliSchoolGuid);

        Integer schoolYear = sse.getSchoolYear();
        result.setSchoolYear(schoolYearConverter.convert(schoolYear));
        Calendar entryDate = sse.getEntryDate();
        if (entryDate != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            result.setEntryDate(dateFormat.format(entryDate.getTime()));
        }
        GradeLevel gradeLevel = sse.getGradeLevel();
        result.setEntryGradeLevel(gradeLevelsConverter.convert(gradeLevel));

        EntryType entryType = sse.getEntryType();
        result.setEntryType(entryTypeConverter.convert(entryType));

        Calendar exitDate = sse.getExitDate();
        if (exitDate != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            result.setExitWithdrawDate(dateFormat.format(exitDate.getTime()));
        }

        ExitType exitType = sse.getExitType();
        result.setExitWithdrawType(exitTypeConverter.convert(exitType));

        return Arrays.asList(result);
    }
}
