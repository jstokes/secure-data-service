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


package org.slc.sli.ingestion.smooks.mappings;

import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.slc.sli.domain.Entity;
import org.slc.sli.domain.Repository;
import org.slc.sli.ingestion.NeutralRecord;
import org.slc.sli.ingestion.util.EntityTestUtils;
import org.slc.sli.validation.EntityValidator;

/**
 *
 * @author mpatel
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/applicationContext-test.xml" })
public class StudentSchoolAssociationEntityTest {

    @InjectMocks
    @Autowired
    private EntityValidator validator;

    @Mock
    private Repository<Entity> mockRepository;

    @Value("${sli.ingestion.recordLevelDeltaEntities}")
    private String recordLevelDeltaEnabledEntityNames;

    String xmlTestData = "<InterchangeStudentEnrollment xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"Interchange-StudentEnrollment.xsd\" xmlns=\"http://ed-fi.org/0100RFC062811\">"
            + "<StudentSchoolAssociation>"
            + " <StudentReference>"
            + "  <StudentIdentity>"
            + "      <StudentUniqueStateId>900000001</StudentUniqueStateId>"
            + "  </StudentIdentity>"
            + " </StudentReference>"
            + " <SchoolReference>"
            + "  <EducationalOrgIdentity>"
            + "      <StateOrganizationId>990000001</StateOrganizationId>"
            + "  </EducationalOrgIdentity>"
            + " </SchoolReference>"
            + " <EntryDate>2012-01-17</EntryDate>"
            + " <EntryGradeLevel>Eighth grade</EntryGradeLevel>"
            + " <EntryType>Next year school</EntryType>"
            + " <GraduationPlan>Distinguished</GraduationPlan>"
            + " <RepeatGradeIndicator>false</RepeatGradeIndicator>"
            + " <SchoolChoiceTransfer>true</SchoolChoiceTransfer>"
            + " <ExitWithdrawDate>2011-09-12</ExitWithdrawDate>"
            + " <ExitWithdrawType>End of school year</ExitWithdrawType>"
            + " <EducationalPlans>"
            + "   <EducationalPlan>Full Time Employment</EducationalPlan>"
            + " </EducationalPlans>"
            + "</StudentSchoolAssociation>" + "</InterchangeStudentEnrollment>";

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    private void checkValidSSANeutralRecord(NeutralRecord record) {
        Map<String, Object> entity = record.getAttributes();
        Assert.assertEquals("900000001", entity.get("studentId"));
        Assert.assertEquals("990000001", entity.get("schoolId"));
        Assert.assertEquals("Eighth grade", entity.get("entryGradeLevel"));
        Assert.assertEquals("2012-01-17", entity.get("entryDate"));
        Assert.assertEquals("Next year school", entity.get("entryType"));
        Assert.assertEquals("false", entity.get("repeatGradeIndicator").toString());
        Assert.assertEquals("2011-09-12", entity.get("exitWithdrawDate"));
        Assert.assertEquals("End of school year", entity.get("exitWithdrawType"));
        Assert.assertEquals("true", entity.get("schoolChoiceTransfer").toString());
        List<?> educationalPlans = (List<?>) record.getAttributes().get("educationalPlans");
        Assert.assertTrue(educationalPlans != null);

        Assert.assertEquals("Full Time Employment", educationalPlans.get(0));

    }

    @Ignore
    @Test
    public void testValidStudentSchoolAssociationCSV() throws Exception {

        String smooksConfig = "smooks_conf/smooks-studentSchoolAssociation-csv.xml";
        String targetSelector = "csv-record";

        String testData = ",,,900000001,,,,,,,,,,,,,,,,,,,,,,,990000001,,,2012-01-17,Eighth grade,Next year school,false,true,2011-09-12,End of school year,true,Full Time Employment,Full";

        NeutralRecord record = EntityTestUtils.smooksGetSingleNeutralRecord(smooksConfig, targetSelector, testData, recordLevelDeltaEnabledEntityNames);
        checkValidSSANeutralRecord(record);
    }

    @Test
    public void testValidatorStudentSchoolAssociation() throws Exception {
        String smooksConfig = "smooks_conf/smooks-all-xml.xml";
        String targetSelector = "InterchangeStudentEnrollment/StudentSchoolAssociation";

        NeutralRecord record = EntityTestUtils.smooksGetSingleNeutralRecord(smooksConfig, targetSelector, xmlTestData, recordLevelDeltaEnabledEntityNames);

        // mock repository will simulate "finding" the references
        Mockito.when(mockRepository.exists("educationOrganization", "990000001")).thenReturn(true);
        Mockito.when(mockRepository.exists("student", "900000001")).thenReturn(true);

        EntityTestUtils.mapValidation(record.getAttributes(), "studentSchoolAssociation", validator);
    }

    @Test
    public void testValidStudentSchoolAssociationXML() throws Exception {

        String smooksConfig = "smooks_conf/smooks-all-xml.xml";
        String targetSelector = "InterchangeStudentEnrollment/StudentSchoolAssociation";

        NeutralRecord record = EntityTestUtils.smooksGetSingleNeutralRecord(smooksConfig, targetSelector, xmlTestData, recordLevelDeltaEnabledEntityNames);
        checkValidSSANeutralRecord(record);
    }

}
