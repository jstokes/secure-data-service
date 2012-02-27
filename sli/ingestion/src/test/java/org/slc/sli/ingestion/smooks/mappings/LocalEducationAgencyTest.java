package org.slc.sli.ingestion.smooks.mappings;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.xml.sax.SAXException;

import org.slc.sli.domain.Entity;
import org.slc.sli.ingestion.NeutralRecord;
import org.slc.sli.ingestion.util.EntityTestUtils;
import org.slc.sli.validation.EntityValidator;

/**
 * Test the smooks mappings for LocalEducationAgency entity
 *
 * @author dduran
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/applicationContext-test.xml" })
public class LocalEducationAgencyTest {

    @Autowired
    private EntityValidator validator;

    private static final String EDFI_XML = "<InterchangeEducationOrganization xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"Interchange-EducationOrganization.xsd\" xmlns=\"http://ed-fi.org/0100RFC062811\">"
            + "<LocalEducationAgency>"
            + "    <StateOrganizationId>152901001</StateOrganizationId>"
            + "    <EducationOrgIdentificationCode IdentificationSystem=\"identification system\">"
            + "        <Id>9777</Id>"
            + "    </EducationOrgIdentificationCode>"
            + "    <NameOfInstitution>Apple Alternative Elementary School</NameOfInstitution>"
            + "    <ShortNameOfInstitution>Apple</ShortNameOfInstitution>"
            + "    <OrganizationCategories>"
            + "        <OrganizationCategory>School</OrganizationCategory>"
            + "    </OrganizationCategories>"
            + "    <Address AddressType=\"Physical\">"
            + "        <StreetNumberName>123 Main Street</StreetNumberName>"
            + "        <ApartmentRoomSuiteNumber>1A</ApartmentRoomSuiteNumber>"
            + "        <BuildingSiteNumber>building site number</BuildingSiteNumber>"
            + "        <City>Lebanon</City>"
            + "        <StateAbbreviation>KS</StateAbbreviation>"
            + "        <PostalCode>66952</PostalCode>"
            + "        <NameOfCounty>Smith County</NameOfCounty>"
            + "        <CountyFIPSCode>USA123</CountyFIPSCode>"
            + "        <CountryCode>USA</CountryCode>"
            + "        <Latitude>245</Latitude>"
            + "        <Longitude>432</Longitude>"
            + "        <BeginDate>01-01-1969</BeginDate>"
            + "        <EndDate>12-12-2012</EndDate>"
            + "    </Address>"
            + "    <Telephone InstitutionTelephoneNumberType=\"Main\">"
            + "        <TelephoneNumber>(785) 667-6006</TelephoneNumber>"
            + "    </Telephone>"
            + "    <WebSite>www.a.com</WebSite>"
            + "    <OperationalStatus>running</OperationalStatus>"
            + "    <AccountabilityRatings>"
            + "        <RatingTitle>first rating</RatingTitle>"
            + "        <Rating>A</Rating>"
            + "        <RatingDate>01-01-2012</RatingDate>"
            + "        <RatingOrganization>rating org</RatingOrganization>"
            + "        <RatingProgram>rating program</RatingProgram>"
            + "    </AccountabilityRatings>"
            + "    <ProgramReference>program reference</ProgramReference>"
            + "    <StateEducationAgencyReference>SEA123</StateEducationAgencyReference>"
            + "</LocalEducationAgency>"
            + "</InterchangeEducationOrganization>";

    @Test
    public void testValidLocalEducationAgency() throws Exception {
        String smooksConfig = "smooks_conf/smooks-all-xml.xml";
        String targetSelector = "InterchangeEducationOrganization/LocalEducationAgency";

        NeutralRecord neutralRecord = EntityTestUtils.smooksGetSingleNeutralRecord(smooksConfig, targetSelector,
                EDFI_XML);

        Entity e = mock(Entity.class);
        when(e.getBody()).thenReturn(neutralRecord.getAttributes());
        when(e.getType()).thenReturn("localEducationAgency");

        Assert.assertTrue(validator.validate(e));
    }

    /*
     * TODO: rewrite CSV unit tests after CSV strategy settled
     *
     * @Test
     * public void csvStateEducationAgencyTest() throws Exception {
     *
     * String smooksConfig = "smooks_conf/smooks-localEducationAgency-csv.xml";
     *
     * String targetSelector = "csv-record";
     *
     * String csv =
     * "152901001,identification system,9777,Apple Alternative Elementary School,Apple,School,Physical,123 Main Street,1A,"
     * +
     * "building site number,Lebanon,KS,66952,Smith County,USA123,USA,245,432,01-01-1969,12-12-2012,Main,(785) 667-6006,www.a.com,running,"
     * + "first rating,A,01-01-2012,rating org,rating program,program reference";
     *
     * NeutralRecord neutralRecord = EntityTestUtils.smooksGetSingleNeutralRecord(smooksConfig,
     * targetSelector, csv);
     *
     * checkValidSEANeutralRecord(neutralRecord);
     * }
     */

    @Test
    public void edfiXmlLocalEducationAgencyTest() throws IOException, SAXException {

        String smooksXmlConfigFilePath = "smooks_conf/smooks-all-xml.xml";

        String targetSelector = "InterchangeEducationOrganization/LocalEducationAgency";

        NeutralRecord neutralRecord = EntityTestUtils.smooksGetSingleNeutralRecord(smooksXmlConfigFilePath,
                targetSelector, EDFI_XML);

        checkValidLeaNeutralRecord(neutralRecord);
    }

    @SuppressWarnings("rawtypes")
    private void checkValidLeaNeutralRecord(NeutralRecord neutralRecord) {

        assertEquals("educationOrganization", neutralRecord.getRecordType());

        assertEquals("152901001", neutralRecord.getLocalId());
        assertEquals("152901001", neutralRecord.getAttributes().get("stateOrganizationId"));

        List educationOrgIdentificationCodeList = (List) neutralRecord.getAttributes().get(
                "educationOrgIdentificationCode");
        Map educationOrgIdentificationMap = (Map) educationOrgIdentificationCodeList.get(0);
        EntityTestUtils.assertObjectInMapEquals(educationOrgIdentificationMap, "identificationSystem",
                "identification system");
        EntityTestUtils.assertObjectInMapEquals(educationOrgIdentificationMap, "ID", "9777");

        assertEquals("Apple Alternative Elementary School", neutralRecord.getAttributes().get("nameOfInstitution"));
        assertEquals("Apple", neutralRecord.getAttributes().get("shortNameOfInstitution"));

        List organizationCategoriesList = (List) neutralRecord.getAttributes().get("organizationCategories");
        assertEquals("School", organizationCategoriesList.get(0));

        List addressList = (List) neutralRecord.getAttributes().get("address");
        Map addressMap = (Map) addressList.get(0);
        EntityTestUtils.assertObjectInMapEquals(addressMap, "addressType", "Physical");
        EntityTestUtils.assertObjectInMapEquals(addressMap, "streetNumberName", "123 Main Street");
        EntityTestUtils.assertObjectInMapEquals(addressMap, "apartmentRoomSuiteNumber", "1A");
        EntityTestUtils.assertObjectInMapEquals(addressMap, "buildingSiteNumber", "building site number");
        EntityTestUtils.assertObjectInMapEquals(addressMap, "city", "Lebanon");
        EntityTestUtils.assertObjectInMapEquals(addressMap, "stateAbbreviation", "KS");
        EntityTestUtils.assertObjectInMapEquals(addressMap, "postalCode", "66952");
        EntityTestUtils.assertObjectInMapEquals(addressMap, "nameOfCounty", "Smith County");
        EntityTestUtils.assertObjectInMapEquals(addressMap, "countyFIPSCode", "USA123");
        EntityTestUtils.assertObjectInMapEquals(addressMap, "countryCode", "USA");
        EntityTestUtils.assertObjectInMapEquals(addressMap, "latitude", "245");
        EntityTestUtils.assertObjectInMapEquals(addressMap, "longitude", "432");
        EntityTestUtils.assertObjectInMapEquals(addressMap, "openDate", "01-01-1969");
        EntityTestUtils.assertObjectInMapEquals(addressMap, "closeDate", "12-12-2012");

        List telephoneList = (List) neutralRecord.getAttributes().get("telephone");
        Map telephoneMap = (Map) telephoneList.get(0);
        EntityTestUtils.assertObjectInMapEquals(telephoneMap, "institutionTelephoneNumberType", "Main");
        EntityTestUtils.assertObjectInMapEquals(telephoneMap, "telephoneNumber", "(785) 667-6006");

        assertEquals("www.a.com", neutralRecord.getAttributes().get("webSite"));
        assertEquals("running", neutralRecord.getAttributes().get("operationalStatus"));

        List accountabilityRatingsList = (List) neutralRecord.getAttributes().get("accountabilityRatings");
        Map accountabilityRatingsMap = (Map) accountabilityRatingsList.get(0);
        EntityTestUtils.assertObjectInMapEquals(accountabilityRatingsMap, "ratingTitle", "first rating");
        EntityTestUtils.assertObjectInMapEquals(accountabilityRatingsMap, "rating", "A");
        EntityTestUtils.assertObjectInMapEquals(accountabilityRatingsMap, "ratingDate", "01-01-2012");
        EntityTestUtils.assertObjectInMapEquals(accountabilityRatingsMap, "ratingOrganization", "rating org");
        EntityTestUtils.assertObjectInMapEquals(accountabilityRatingsMap, "ratingProgram", "rating program");

        List programReferenceList = (List) neutralRecord.getAttributes().get("programReference");
        assertEquals("program reference", programReferenceList.get(0));

        assertEquals("SEA123", neutralRecord.getAttributes().get("parentEducationAgencyReference"));
    }

}
