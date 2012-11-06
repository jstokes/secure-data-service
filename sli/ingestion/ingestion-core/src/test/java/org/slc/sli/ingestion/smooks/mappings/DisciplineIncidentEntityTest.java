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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.xml.sax.SAXException;

import org.slc.sli.ingestion.NeutralRecord;
import org.slc.sli.ingestion.util.EntityTestUtils;

/**
 * Test the smooks mappings for Program entity.
 *
 * @author vmcglaughlin
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/applicationContext-test.xml" })
public class DisciplineIncidentEntityTest {

    @Value("${sli.ingestion.recordLevelDeltaEntities}")
    private String recordLevelDeltaEnabledEntityNames;


    /**
     * Test that Ed-Fi program is correctly mapped to a NeutralRecord.
     * @throws IOException
     * @throws SAXException
     */
    @Test
    public final void mapEdfiXmlToNeutralRecordTest() throws IOException, SAXException {

        String smooksXmlConfigFilePath = "smooks_conf/smooks-all-xml.xml";

        String targetSelector = "InterchangeStudentDiscipline/DisciplineIncident";

        String edfiXml = null;

        try {
            edfiXml = EntityTestUtils.readResourceAsString("smooks/unitTestData/DisciplineIncidentEntity.xml");
        } catch (FileNotFoundException e) {
            System.err.println(e);
            Assert.fail();
        }

        NeutralRecord neutralRecord = EntityTestUtils
                .smooksGetSingleNeutralRecord(smooksXmlConfigFilePath,
                        targetSelector, edfiXml, recordLevelDeltaEnabledEntityNames);

        checkValidNeutralRecord(neutralRecord);
    }

    private void checkValidNeutralRecord(NeutralRecord neutralRecord) {
        assertEquals("Expecting different record type", "disciplineIncident", neutralRecord.getRecordType());

        Map<String, Object> attributes = neutralRecord.getAttributes();

        //@@@ debug
        for(String k : attributes.keySet()) {
	    System.out.println (k + ": " + attributes.get(k));
	}

        assertEquals("Expected different number of attributes", 14, attributes.size());

        assertEquals("Expected different id", "whack-a-mole", attributes.get("id"));
        assertEquals("Expected different IncidentIdentifier", "Whack-a-mole", attributes.get("IncidentIdentifier"));
        assertEquals("Expected different IncidentDate", "2011-02-01", attributes.get("IncidentDate"));
        assertEquals("Expected different IncidentTime", "15:23:02", attributes.get("IncidentTime"));
        assertEquals("Expected different IncidentLocation", "On School", attributes.get("IncidentLocation"));
        assertEquals("Expected different ReporterDescription", "Student", attributes.get("ReporterDescription"));
        assertEquals("Expected different ReporterName", "Gold Finger", attributes.get("ReporterName"));
        assertEquals("Expected different ReportedToLawEnforcement", false, attributes.get("ReportedToLawEnforcement"));
        assertEquals("Expected different CaseNumber", "Case Number 1", attributes.get("CaseNumber"));

        //behaviors
        @SuppressWarnings("unchecked")
        List<List<Map<String, Object>>> behaviors = (List<List<Map<String, Object>>>) attributes.get("Behaviors");
        assertNotNull("Expected non-null list of behaviors", behaviors);
        assertEquals("Expected two behaviors", 2, behaviors.size());

        List<Map<String, Object>> behavior = behaviors.get(0);
        assertNotNull("Expected non-null behavior", behavior);
        assertEquals("Expected three choices", 3, behavior.size());

        Map<String, Object> choice = behavior.get(0);
        assertNotNull("Expected non-null behavior choice", choice);
        assertEquals("Expected different behavior code value", "Code Value 1", choice.get("CodeValue"));

        choice = behavior.get(1);
        assertNotNull("Expected non-null behavior choice", choice);
        assertEquals("Expected different short description of behavior", "Short Description 1", choice.get("ShortDescription"));

        choice = behavior.get(2);
        assertNotNull("Expected non-null behavior choice", choice);
        assertEquals("Expected different description of behavior", "Description 1", choice.get("Description"));

        behavior = behaviors.get(1);
        assertNotNull("Expected non-null behavior", behavior);
        assertEquals("Expected three choices", 3, behavior.size());

        choice = behavior.get(0);
        assertNotNull("Expected non-null behavior choice", choice);
        assertEquals("Expected different behavior code value", "Code Value 2", choice.get("CodeValue"));

        choice = behavior.get(1);
        assertNotNull("Expected non-null behavior choice", choice);
        assertEquals("Expected different short description of behavior", "Short Description 2", choice.get("ShortDescription"));

        choice = behavior.get(2);
        assertNotNull("Expected non-null behavior choice", choice);
        assertEquals("Expected different description of behavior", "Description 2", choice.get("Description"));

        //secondary behaviors
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> secondaryBehaviors = (List<Map<String, Object>>) attributes.get("SecondaryBehaviors");
        assertNotNull("Expected non-null list of secondaryBehaviors", secondaryBehaviors);
        assertEquals("Expected two secondaryBehaviors", 2, secondaryBehaviors.size());

        Map<String, Object> secondaryBehavior = secondaryBehaviors.get(0);
        assertNotNull("Expected non-null secondaryBehavior", secondaryBehavior);
        assertEquals("Expected different secondaryBehavior category", "School Code of Conduct", secondaryBehavior.get("BehaviorCategory"));
        assertEquals("Expected different secondaryBehavior", "Mischief", secondaryBehavior.get("SecondaryBehavior"));

        secondaryBehavior = secondaryBehaviors.get(1);
        assertNotNull("Expected non-null secondaryBehavior", secondaryBehavior);
        assertEquals("Expected different secondaryBehavior category", "State Law Crime", secondaryBehavior.get("BehaviorCategory"));
        assertEquals("Expected different secondaryBehavior", "Hair Pulling", secondaryBehavior.get("SecondaryBehavior"));

        //weapons
        @SuppressWarnings("unchecked")
        Map<String, List<String>> weapons = (Map<String, List<String>>) attributes.get("Weapons");
        assertNotNull("Expected non-null weapons", weapons);

        List<String> weaponsList = weapons.get("Weapon");
        assertNotNull("Expected non-null list of weapons", weaponsList);
        assertEquals("Expected two weapons", 2, weaponsList.size());

        assertEquals("Expected different weapon", "Club", weaponsList.get(0));
        assertEquals("Expected different weapon", "Non-Illegal Knife", weaponsList.get(1));

        assertEquals("Expected different CaseNumber", "Case Number 1", attributes.get("CaseNumber"));

        //school
        @SuppressWarnings("unchecked")
        Map<String, Map<String, Object>> schoolReference = (Map<String, Map<String, Object>>) attributes.get("SchoolReference");
        assertNotNull("Expected non-null school reference", schoolReference);

        Map<String, Object> educationalOrgIdentity = schoolReference.get("EducationalOrgIdentity");
        assertNotNull("Expected non-null educational org identity", educationalOrgIdentity);

        @SuppressWarnings("unchecked")
        String stateOrganizationId =(String) educationalOrgIdentity.get("StateOrganizationId");
        assertNotNull("Expected non-null state organization ids", stateOrganizationId);
        assertEquals("Expected different state organization id", "State Organization Id", stateOrganizationId);

        //staff
        @SuppressWarnings("unchecked")
        Map<String, Object> staffReference = (Map<String, Object>) attributes.get("StaffReference");
        assertNotNull("Expected non-null staff reference", staffReference);

        @SuppressWarnings("unchecked")
        Map<String, Object> staffIdentity = (Map<String, Object>) staffReference.get("StaffIdentity");
        assertNotNull("Expected non-null staff identity", staffIdentity);

        assertEquals("Expected different staff unique state id", "Staff Unique State Id", staffIdentity.get("StaffUniqueStateId"));

        @SuppressWarnings("unchecked")
        List<Map<String, String>> staffIdentificationCodes = (List<Map<String, String>>) staffIdentity.get("StaffIdentificationCode");
        assertNotNull("Expected non-null staff identification codes", staffIdentificationCodes);

        Map<String, String> staffIdentificationCode = staffIdentificationCodes.get(0);
        assertNotNull("Expected non-null staff identification code", staffIdentificationCode);
        assertEquals("Expected different identification system", "Canadian SIN", staffIdentificationCode.get("IdentificationSystem"));
        assertEquals("Expected different AssigningOrganizationCode", "Assigning Organization Code 1", staffIdentificationCode.get("AssigningOrganizationCode"));
        assertEquals("Expected different ID", "Staff Identification Code 1", staffIdentificationCode.get("ID"));

        staffIdentificationCode = staffIdentificationCodes.get(1);
        assertNotNull("Expected non-null staff identification code", staffIdentificationCode);
        assertEquals("Expected different AssigningOrganizationCode", "Assigning Organization Code 2", staffIdentificationCode.get("AssigningOrganizationCode"));
        assertEquals("Expected different ID", "Staff Identification Code 2", staffIdentificationCode.get("ID"));

        @SuppressWarnings("unchecked")
        Map<String, String> name = (Map<String, String>) staffIdentity.get("Name");
        assertNotNull("Expected non-null name", name);
        assertEquals("Expected different Verification", "Baptismal or church certificate", name.get("Verification"));
        assertEquals("Expected different PersonalTitlePrefix", "Colonel", name.get("PersonalTitlePrefix"));
        assertEquals("Expected different FirstName", "FN1", name.get("FirstName"));
        assertEquals("Expected different MiddleName", "MN1", name.get("MiddleName"));
        assertEquals("Expected different LastSurname", "LN1", name.get("LastSurname"));
        assertEquals("Expected different GenerationCodeSuffix", "Jr", name.get("GenerationCodeSuffix"));
        assertEquals("Expected different MaidenName", "Nee", name.get("MaidenName"));

        @SuppressWarnings("unchecked")
        List<Map<String, String>> otherNames = (List<Map<String, String>>) staffIdentity.get("OtherName");
        assertNotNull("Expected non-null other names", otherNames);
        assertEquals("Expected two OtherNames", 2, otherNames.size());

        Map<String, String> otherName = otherNames.get(0);
        assertNotNull("Expected non-null OtherName", otherName);
        assertEquals("Expected different OtherNameType", "Previous Legal Name", otherName.get("OtherNameType"));
        assertEquals("Expected different PersonalTitlePrefix", "Sister", otherName.get("PersonalTitlePrefix"));
        assertEquals("Expected different FirstName", "FN2", otherName.get("FirstName"));
        assertEquals("Expected different MiddleName", "MN2", otherName.get("MiddleName"));
        assertEquals("Expected different LastSurname", "LN2", otherName.get("LastSurname"));
        assertEquals("Expected different GenerationCodeSuffix", "Sr", otherName.get("GenerationCodeSuffix"));

        otherName = otherNames.get(1);
        assertNotNull("Expected non-null OtherName", otherName);
        assertEquals("Expected different OtherNameType", "Alias", otherName.get("OtherNameType"));
        assertEquals("Expected different PersonalTitlePrefix", "Sr", otherName.get("PersonalTitlePrefix"));
        assertEquals("Expected different FirstName", "FN3", otherName.get("FirstName"));
        assertEquals("Expected different MiddleName", "MN3", otherName.get("MiddleName"));
        assertEquals("Expected different LastSurname", "LN3", otherName.get("LastSurname"));
        assertEquals("Expected different GenerationCodeSuffix", "VIII", otherName.get("GenerationCodeSuffix"));

        assertEquals("Expected different sex", "Female", staffIdentity.get("Sex"));
        assertEquals("Expected different birth date", "05-27-1980", staffIdentity.get("BirthDate"));
        assertEquals("Expected different hispanic latino ethnicity", false, staffIdentity.get("HispanicLatinoEthnicity"));

        @SuppressWarnings("unchecked")
        Map<String, List<String>> race = (Map<String, List<String>>) staffIdentity.get("Race");
        assertNotNull("Expected non-null race", race);

        List<String> racialCategories = race.get("RacialCategory");
        assertNotNull("Expected non-null RacialCategories", racialCategories);
        assertEquals("Expected two RacialCategories", 2, racialCategories.size());
        assertEquals("Expected different RacialCategory", "American Indian - Alaskan Native", racialCategories.get(0));
        assertEquals("Expected different RacialCategory", "Native Hawaiian - Pacific Islander", racialCategories.get(1));

        @SuppressWarnings("unchecked")
        List<Map<String, String>> telephones = (List<Map<String, String>>) staffIdentity.get("Telephone");
        assertNotNull("Expected non-null telephones", telephones);
        assertEquals("Expected two Telephones", 2, telephones.size());

        Map<String, String> telephone = telephones.get(0);
        assertNotNull("Expected non-null telephone", telephone);
        assertEquals("Expected different TelephoneNumberType", "Emergency 1", telephone.get("TelephoneNumberType"));
        assertEquals("Expected different PrimaryTelephoneNumberIndicator", false, telephone.get("PrimaryTelephoneNumberIndicator"));
        assertEquals("Expected different TelephoneNumber", "555-555-1234", telephone.get("TelephoneNumber"));

        telephone = telephones.get(1);
        assertNotNull("Expected non-null telephone", telephone);
        assertEquals("Expected different TelephoneNumberType", "Work", telephone.get("TelephoneNumberType"));
        assertEquals("Expected different PrimaryTelephoneNumberIndicator", true, telephone.get("PrimaryTelephoneNumberIndicator"));
        assertEquals("Expected different TelephoneNumber", "555-555-4321", telephone.get("TelephoneNumber"));

        @SuppressWarnings("unchecked")
        List<Map<String, String>> electronicMails = (List<Map<String, String>>) staffIdentity.get("ElectronicMail");
        assertNotNull("Expected non-null electronic mails", electronicMails);
        assertEquals("Expected two ElectronicMails", 2, electronicMails.size());

        Map<String, String> electronicMail = electronicMails.get(0);
        assertNotNull("Expected non-null ElectronicMail", electronicMail);
        assertEquals("Expected different EmailAddressType", "Work", electronicMail.get("EmailAddressType"));
        assertEquals("Expected different EmailAddress", "1@example.com", electronicMail.get("EmailAddress"));

        electronicMail = electronicMails.get(1);
        assertNotNull("Expected non-null ElectronicMail", electronicMail);
        assertEquals("Expected different EmailAddressType", "Home/Personal", electronicMail.get("EmailAddressType"));
        assertEquals("Expected different EmailAddress", "2@example.com", electronicMail.get("EmailAddress"));
    }
}