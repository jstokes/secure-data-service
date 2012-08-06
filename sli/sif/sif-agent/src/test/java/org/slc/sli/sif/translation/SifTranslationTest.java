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

import java.util.List;

import junit.framework.Assert;
import openadk.library.ADK;
import openadk.library.ADKException;
import openadk.library.student.LEAInfo;
import openadk.library.student.OperationalStatus;
import openadk.library.student.SchoolInfo;
import openadk.library.student.Title1Status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.slc.sli.sif.domain.slientity.SliEntity;

/**
 * Integration test for the configured SIF->SLI translation.
 * @author jtully
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:/spring/test-applicationContext.xml" })
public class SifTranslationTest {

    @Autowired
    private SifTranslationManager translationManager;

    @Before
    public void setup() throws ADKException {
        try {
            ADK.initialize();
        } catch (ADKException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void shouldTranslateSchoolInfoToSchool() {
        SchoolInfo info = createSchoolInfo();
        List<SliEntity> entities = translationManager.translate(info);

        Assert.assertEquals("Should create a single SLI school entity", 1, entities.size());
        Assert.assertNotNull("NULL sli entity", entities.get(0));
        Assert.assertEquals("Mapped SLI entitiy should be of type educationOrganization",
                "school", entities.get(0).entityType());
    }

    @Test
    public void shouldTranslateLEAInfoInfoToLEA() {
        LEAInfo info = createLEAInfo();
        List<SliEntity> entities = translationManager.translate(info);

        Assert.assertEquals("Should create a single SLI LEA entity", 1, entities.size());
        Assert.assertNotNull("NULL sli entity", entities.get(0));
        Assert.assertEquals("Mapped SLI entitiy should be of type educationOrganization",
                "educationOrganization", entities.get(0).entityType());
    }

    private SchoolInfo createSchoolInfo() {
        SchoolInfo info = new SchoolInfo();

        info.setStateProvinceId("stateOrgId");
        info.setSchoolName("schoolName");
        info.setSchoolURL("schoolUrl");
        info.setTitle1Status(Title1Status.SCHOOLWIDE);

        return info;
    }

    private LEAInfo createLEAInfo() {
        LEAInfo info = new LEAInfo();

        info.setStateProvinceId("stateOrgId");
        info.setLEAName("LEAName");
        info.setLEAURL("LEAURL");
        info.setOperationalStatus(OperationalStatus.AGENCY_CLOSED);

        return info;
    }
}
