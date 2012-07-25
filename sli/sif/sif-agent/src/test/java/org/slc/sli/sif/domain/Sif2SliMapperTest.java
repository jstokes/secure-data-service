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

package org.slc.sli.sif.domain;

import java.io.IOException;

import junit.framework.Assert;
import openadk.library.ADKException;
import openadk.library.Event;
import openadk.library.EventAction;
import openadk.library.SIFDataObject;
import openadk.library.student.SchoolInfo;
import openadk.library.student.LEAInfo;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.dozer.MappingException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.slc.sli.sif.domain.slientity.SEAEntity;
import org.slc.sli.sif.domain.slientity.SchoolEntity;
import org.slc.sli.sif.domain.slientity.LEAEntity;
import openadk.library.datamodel.SEAInfo;
import org.slc.sli.sif.generator.SifEntityGenerator;


/**
 * JUnits for testing SchoolInfo Dozer Mapping.
 *
 * @author slee
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:/spring/applicationContext.xml" })
public class Sif2SliMapperTest
{
    @Autowired
    private Sif2SliTransformer xformer;

    private SchoolInfo schoolInfo;
    private LEAInfo leaInfo;
    private SEAInfo seaInfo;

    @Before
    public void preMethodSetup() {
        schoolInfo = SifEntityGenerator.generateTestSchoolInfo();
        leaInfo = SifEntityGenerator.generateTestLEAInfo();
        seaInfo = SifEntityGenerator.generateTestSEAInfo();
    }

    @Test
    public void testSchoolInfoMap2json() throws JsonProcessingException, MappingException, IOException {
        JsonNode schoolNode = xformer.transform2json(schoolInfo);
        Assert.assertEquals("Expecting 'Daybreak West High' as stateOrganizationId",
                "Daybreak West High", schoolNode.get("stateOrganizationId").asText());
        Assert.assertEquals("Expecting 'Daybreak West High' as nameOfInstitution",
                "Daybreak West High", schoolNode.get("nameOfInstitution").asText());
        Assert.assertEquals("Expecting 'Closed' as operationalStatus",
                "Closed", schoolNode.get("operationalStatus").asText());
        Assert.assertEquals("Expecting organizationCategories as an array",
                true, schoolNode.get("organizationCategories").isArray());
        Assert.assertEquals("Expecting 1 in organizationCategories",
                1, schoolNode.get("organizationCategories").size());
        Assert.assertEquals("Expecting 'School' as organizationCategories",
                "School", schoolNode.get("organizationCategories").get(0).asText());
        Assert.assertEquals("Expecting schoolCategories as an array",
                true, schoolNode.get("schoolCategories").isArray());
        Assert.assertEquals("Expecting 1 in schoolCategories",
                1, schoolNode.get("schoolCategories").size());
        Assert.assertEquals("Expecting 'High School' as schoolCategories",
                "High School", schoolNode.get("schoolCategories").get(0).asText());
        Assert.assertEquals("Expecting gradesOffered as an array",
                true, schoolNode.get("gradesOffered").isArray());
        Assert.assertEquals("Expecting 1 in gradesOffered",
                4, schoolNode.get("gradesOffered").size());
        Assert.assertEquals("Expecting 'Ninth grade' as gradesOffered",
                "Ninth grade", schoolNode.get("gradesOffered").get(0).asText());
        Assert.assertEquals("Expecting 'Tenth grade' as gradesOffered",
                "Tenth grade", schoolNode.get("gradesOffered").get(1).asText());
        Assert.assertEquals("Expecting 'Eleventh grade' as gradesOffered",
                "Eleventh grade", schoolNode.get("gradesOffered").get(2).asText());
        Assert.assertEquals("Expecting 'Twelfth grade' as gradesOffered",
                "Twelfth grade", schoolNode.get("gradesOffered").get(3).asText());
        Assert.assertEquals("Expecting telephone as an array",
                true, schoolNode.get("telephone").isArray());
        Assert.assertEquals("Expecting 1 in telephone",
                2, schoolNode.get("telephone").size());
        Assert.assertEquals("Expecting each telephone node as ContainerNode",
                true, schoolNode.get("telephone").get(0).isContainerNode());
        Assert.assertEquals("Expecting '(312) 555-1234' as telephoneNumber in the first telephone node",
                "(312) 555-1234", schoolNode.get("telephone").get(0).get("telephoneNumber").asText());
        Assert.assertEquals("Expecting 'Main' as institutionTelephoneNumberType in the first telephone node",
                "Main", schoolNode.get("telephone").get(0).get("institutionTelephoneNumberType").asText());
        Assert.assertEquals("Expecting each telephone node as ContainerNode",
                true, schoolNode.get("telephone").get(1).isContainerNode());
        Assert.assertEquals("Expecting '(312) 555-2364' as telephoneNumber in the second telephone node",
                "(312) 555-2364", schoolNode.get("telephone").get(1).get("telephoneNumber").asText());
        Assert.assertEquals("Expecting 'Fax' as institutionTelephoneNumberType in the second telephone node",
                "Fax", schoolNode.get("telephone").get(1).get("institutionTelephoneNumberType").asText());
        Assert.assertEquals("Expecting 1 in address",
                1, schoolNode.get("address").size());
        Assert.assertEquals("Expecting each address node as ContainerNode",
                true, schoolNode.get("address").get(0).isContainerNode());
        Assert.assertEquals("Expecting '1 IBM way' as streetNumberName in the first address node",
                "1 IBM way", schoolNode.get("address").get(0).get("streetNumberName").asText());
        Assert.assertEquals("Expecting 'Salt Lake City' as city in the first address node",
                "Salt Lake City", schoolNode.get("address").get(0).get("city").asText());
        Assert.assertEquals("Expecting 'IL' as stateAbbreviation in the first address node",
                "IL", schoolNode.get("address").get(0).get("stateAbbreviation").asText());
        Assert.assertEquals("Expecting '84102' as postalCode in the first address node",
                "84102", schoolNode.get("address").get(0).get("postalCode").asText());
        Assert.assertEquals("Expecting 'US' as countryCode in the first address node",
                "US", schoolNode.get("address").get(0).get("countryCode").asText());
        Assert.assertEquals("Expecting 'Mailing' as addressType in the first address node",
                "Mailing", schoolNode.get("address").get(0).get("addressType").asText());
    }

    @Test
    public void testSchoolInfoMap() {
        SchoolEntity entity = xformer.transform(schoolInfo);
        Assert.assertEquals("Expecting 2 telephone numbers", 2, entity.getTelephone().size());
        Assert.assertEquals("Expecting 'Main' as the first phone type", "Main", entity.getTelephone().get(0).getInstitutionTelephoneNumberType());
        Assert.assertEquals("Expecting '(312) 555-1234' as the first phone number", "(312) 555-1234", entity.getTelephone().get(0).getTelephoneNumber());
        Assert.assertEquals("Expecting 'Main' as the first phone type", "Fax", entity.getTelephone().get(1).getInstitutionTelephoneNumberType());
        Assert.assertEquals("Expecting '(312) 555-2364' as the first phone number", "(312) 555-2364", entity.getTelephone().get(1).getTelephoneNumber());
    }

    @Test
    public void testLEAInfoMap() {
        LEAEntity entity = xformer.transform(leaInfo);
        Assert.assertEquals("Expecting 2 telephone numbers", 2, entity.getTelephone().size());
        Assert.assertEquals("Expecting 'Main' as the first phone type", "Main", entity.getTelephone().get(0).getInstitutionTelephoneNumberType());
        Assert.assertEquals("Expecting '(312) 555-1234' as the first phone number", "(312) 555-1234", entity.getTelephone().get(0).getTelephoneNumber());
        Assert.assertEquals("Expecting 'Main' as the first phone type", "Fax", entity.getTelephone().get(1).getInstitutionTelephoneNumberType());
        Assert.assertEquals("Expecting '(312) 555-2364' as the first phone number", "(312) 555-2364", entity.getTelephone().get(1).getTelephoneNumber());
    }

    @Test
    public void testSEAInfoMap() {
        SEAEntity entity = xformer.transform(seaInfo);
        Assert.assertEquals("Expecting 2 telephone numbers", 2, entity.getTelephone().size());
        Assert.assertEquals("Expecting 'Main' as the first phone type", "Main", entity.getTelephone().get(0).getInstitutionTelephoneNumberType());
        Assert.assertEquals("Expecting '(312) 555-1234' as the first phone number", "(312) 555-1234", entity.getTelephone().get(0).getTelephoneNumber());
        Assert.assertEquals("Expecting 'Main' as the first phone type", "Fax", entity.getTelephone().get(1).getInstitutionTelephoneNumberType());
        Assert.assertEquals("Expecting '(312) 555-2364' as the first phone number", "(312) 555-2364", entity.getTelephone().get(1).getTelephoneNumber());
    }

}
