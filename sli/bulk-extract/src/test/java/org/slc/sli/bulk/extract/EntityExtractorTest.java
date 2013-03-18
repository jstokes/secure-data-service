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
package org.slc.sli.bulk.extract;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.slc.sli.bulk.extract.zip.OutstreamZipFile;
import org.slc.sli.dal.repository.MongoEntityRepository;
import org.slc.sli.domain.Entity;
import org.slc.sli.domain.NeutralQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Test bulk extraction into zip files.
 *
 * @author tshewchuk
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/applicationContext-test.xml" })
public class EntityExtractorTest {

    private final String TENANT1 = "Midgar";
    private final String TENANT2 = "Hyrule";
    private final String extractDir = "data/tmp";

    private final List<String> tenants = Arrays.asList(new String[] { TENANT1, TENANT2 });

    private List<String> collections;

    @Autowired
    private  EntityExtractor extractor;

    private MongoEntityRepository mongoEntityRepository;

    private OutstreamZipFile zipFile;

    @Before
    public void init() throws IOException {
        collections = new ArrayList<String>();
        collections.add("student");
        collections.add("assessment");
        collections.add("staff");
        collections.add("staffEducationOrganizationAssociation");

//        extractor.setCollections(collections);
        mongoEntityRepository = Mockito.mock(MongoEntityRepository.class);
        zipFile = Mockito.mock(OutstreamZipFile.class);
        extractor.setEntityRepository(mongoEntityRepository);
        extractor.setTenants(tenants);
        extractor.setBaseDirectory(extractDir);
        extractor.init();
    }

    @After
    public void destroy() {
        extractor.destroy();
    }

    @SuppressWarnings("deprecation")
    //@Test
    public void testExtractEntity() throws IOException{
        String testTenant = "Midgar";
        String testEntity = "student";

        List<Entity> students = TestUtils.createStudents();
        Mockito.when(mongoEntityRepository.findByQuery(Matchers.eq(testEntity), Matchers.any(Query.class), Matchers.anyInt(), Matchers.anyInt())).thenReturn(students);

        extractor.extractEntity(testTenant, zipFile, testEntity);

        Mockito.verify(zipFile, Mockito.atLeast(1)).writeData(Matchers.eq(TestUtils.toJSON(students.get(0))));
        Mockito.verify(zipFile, Mockito.atLeast(1)).writeData(Matchers.eq(TestUtils.toJSON(students.get(1))));

    }

    @SuppressWarnings("boxing")
    @Test
    public void testExtractEntityUnhappy() throws IOException{
        String testTenant = "Midgar";
        String testEntity = "student";

        List<Entity> students = TestUtils.createStudents();

        Mockito.when(mongoEntityRepository.findAll(Matchers.eq(testEntity), Matchers.any(NeutralQuery.class))).thenReturn(students.subList(0, 1));
        Mockito.when(zipFile.writeData(Matchers.anyString())).thenThrow(new IOException());

        extractor.extractEntity(testTenant, zipFile, testEntity);

        Mockito.verify(zipFile, Mockito.atLeast(3)).writeData(Matchers.anyString());
    }
}
