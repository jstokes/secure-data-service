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

package org.slc.sli.bulk.extract.lea;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slc.sli.bulk.extract.extractor.EntityExtractor;
import org.slc.sli.bulk.extract.files.ExtractFile;
import org.slc.sli.domain.Entity;
import org.slc.sli.domain.Repository;
import org.springframework.data.mongodb.core.query.Query;


public class StudentExtractorTest {
    
    private StudentExtractor extractor;
    @Mock
    private Repository<Entity> mockRepo;
    @Mock
    private EntityExtractor mockExtractor;
    @Mock
    private LEAExtractFileMap mockMap;
    
    @Mock
    private ExtractorHelper helper;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        Mockito.when(mockMap.getLeas()).thenReturn(new HashSet<String>(Arrays.asList("LEA")));
        extractor = new StudentExtractor(mockExtractor, mockMap, mockRepo, helper);
    }
    
    @After
    public void tearDown() {
        
    }
    
    @Test
    public void testOneExtractedEntity() {
        Entity e = Mockito.mock(Entity.class);
        Mockito.when(mockRepo.findEach(Mockito.eq("student"), Mockito.eq(new Query()))).thenReturn(Arrays.asList(e).iterator());
        Mockito.when(helper.fetchCurrentSchoolsFromStudent(Mockito.any(Entity.class))).thenReturn(
                new HashSet<String>(Arrays.asList("LEA")));
        extractor.extractEntities(null);
        Mockito.verify(mockExtractor).extractEntity(Mockito.any(Entity.class), Mockito.any(ExtractFile.class),
                Mockito.eq("student"));

    }
    
    @Test
    public void testManyExtractedEntities() {
        Entity e = Mockito.mock(Entity.class);
        Mockito.when(mockRepo.findEach(Mockito.eq("student"), Mockito.eq(new Query()))).thenReturn(
                Arrays.asList(e, e, e).iterator());
        Mockito.when(helper.fetchCurrentSchoolsFromStudent(Mockito.any(Entity.class))).thenReturn(
                new HashSet<String>(Arrays.asList("LEA")));
        extractor.extractEntities(null);
        Mockito.verify(mockExtractor, Mockito.times(3)).extractEntity(Mockito.any(Entity.class),
                Mockito.any(ExtractFile.class), Mockito.eq("student"));
    }
    
    @Test
    public void testNoExtractedEntities() {
        // Cache LEA != Student Edorgs
        Entity e = Mockito.mock(Entity.class);
        Mockito.when(mockRepo.findEach(Mockito.eq("student"), Mockito.eq(new Query()))).thenReturn(
                Arrays.asList(e).iterator());
        Mockito.when(helper.fetchCurrentSchoolsFromStudent(Mockito.any(Entity.class))).thenReturn(
                new HashSet<String>(Arrays.asList("LEA2")));
        extractor.extractEntities(null);
        Mockito.verify(mockExtractor, Mockito.never()).extractEntity(Mockito.any(Entity.class),
                Mockito.any(ExtractFile.class), Mockito.eq("student"));
        // No Edorgs
        Mockito.when(helper.fetchCurrentSchoolsFromStudent(Mockito.any(Entity.class)))
                .thenReturn(new HashSet<String>());
        extractor.extractEntities(null);
        Mockito.verify(mockExtractor, Mockito.never()).extractEntity(Mockito.any(Entity.class),
                Mockito.any(ExtractFile.class), Mockito.eq("student"));
    }

}
