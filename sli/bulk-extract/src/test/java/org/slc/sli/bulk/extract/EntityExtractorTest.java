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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.slc.sli.bulk.extract.zip.OutstreamZipFile;
import org.slc.sli.dal.repository.MongoEntityRepository;
import org.slc.sli.domain.CalculatedData;
import org.slc.sli.domain.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mongodb.DBCursor;
import com.mongodb.DBObject;

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
    
    private BulkExtractMongoDA bulkExtractMongoDA;

    @Before
    public void init() throws IOException {
        collections = new ArrayList<String>();
        collections.add("student");
        collections.add("assessment");
        collections.add("staff");
        collections.add("staffEducationOrganizationAssociation");

        mongoEntityRepository = Mockito.mock(MongoEntityRepository.class);
        zipFile = Mockito.mock(OutstreamZipFile.class);
        File file = Mockito.mock(File.class);
        Mockito.when(zipFile.getZipFile()).thenReturn(file);
        Mockito.when(file.getAbsolutePath()).thenReturn(extractDir+"/02f7abaa9764db2fa3c1ad852247cd4ff06b2c0a");
        
        bulkExtractMongoDA = Mockito.mock(BulkExtractMongoDA.class);
        extractor.setBulkExtractMongoDA(bulkExtractMongoDA);
        extractor.setEntityRepository(mongoEntityRepository);
        extractor.setTenants(tenants);
        extractor.setBaseDirectory(extractDir);
        extractor.init();
    }

    @After
    public void destroy() {
        extractor.destroy();
    }

    @Test
    public void testExtractEntity() throws IOException{
        String testTenant = "Midgar";
        String testEntity = "student";

        Iterator<Entity> cursor = Mockito.mock(Iterator.class);

        List<Entity> students = TestUtils.createStudents();

        Mockito.when(cursor.hasNext()).thenReturn(true, true, true, true, false, false);
        Mockito.when(cursor.next()).thenReturn(students.get(0), students.get(1));
        
        Mockito.when(mongoEntityRepository.findEach(Matchers.eq(testEntity), Matchers.any(Query.class))).thenReturn(cursor);
        
        extractor.extractEntity(testTenant, zipFile, testEntity);

        Mockito.verify(zipFile, Mockito.atLeast(1)).writeData(Matchers.eq(TestUtils.toJSON(students.get(0))));
        Mockito.verify(zipFile, Mockito.atLeast(1)).writeData(Matchers.eq(TestUtils.toJSON(students.get(1))));

    }
    
    @Test
    public void testinitiateExtractForEntites() {

        extractor.setEntities(collections);
        EntityExtractor ex = Mockito.spy(extractor);
        Mockito.doNothing().when(ex).extractEntity(Mockito.anyString(), Mockito.any(OutstreamZipFile.class), Mockito.anyString());
        
        ex.initiateExtractForEntites("Midgar", zipFile, new Date());
        
        for(String collection : collections) {
            Mockito.verify(ex, Mockito.times(1)).extractEntity("Midgar", zipFile, collection);
        }
        Mockito.verify(bulkExtractMongoDA, Mockito.times(1)).updateDBRecord(Mockito.anyString(), Mockito.anyString(), Mockito.any(Date.class));
    }

    List<Entity> createStudents(){
        List<Entity> res = new ArrayList<Entity>();

        Map<String, Object> student1Body = new HashMap<String, Object>();
        student1Body.put("UniqueStudentId", "1");

        Entity student1 = makeDummyEntity("student", "1", student1Body);

        Map<String, Object> student2Body = new HashMap<String, Object>();
        student1Body.put("UniqueStudentId", "2");

        Entity student2 = makeDummyEntity("student", "2", student2Body);

        res.add(student1);
        res.add(student2);

        return res;
    }
    
    public static Entity makeDummyEntity(final String type, final String id, final Map<String, Object> body) {
        return new Entity() {

            @Override
            public String getType() {
                return type;
            }

            @Override
            public Map<String, Object> getMetaData() {
                return new HashMap<String, Object>();
            }

            @Override
            public String getEntityId() {
                return id;
            }

            @Override
            public Map<String, Object> getBody() {
                return body;
            }

            @Override
            public CalculatedData<String> getCalculatedValues() {
                return null;
            }

            @Override
            public CalculatedData<Map<String, Integer>> getAggregates() {
                return null;
            }

            @Override
            public Map<String, List<Entity>> getEmbeddedData() {
                return null;
            }

            @Override
            public Map<String, List<Map<String, Object>>> getDenormalizedData() {
                return null;
            }

            @Override
            public String getStagedEntityId() {
                return null;
            }
       };
    }

}
