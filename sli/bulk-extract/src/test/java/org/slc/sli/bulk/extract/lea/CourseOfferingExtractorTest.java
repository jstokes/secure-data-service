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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import junit.framework.Assert;

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

public class CourseOfferingExtractorTest {
    private CourseOfferingExtractor extractor;
    @Mock
    private Repository<Entity> mockRepo;
    @Mock
    private LEAExtractFileMap mockMap;
    @Mock
    private EntityExtractor mockExtractor;
    @Mock
    private ExtractFile mockFile;
    
    @Mock
    private EntityToLeaCache mockEdorgCache;
    
    @Mock
    private EntityToLeaCache mockCourseOfferingCache;
    
    @Mock
    private Entity mockEntity;
    private Map<String, Object> entityBody;
    
    @Mock
    private Entity mockTransitiveEntity;
    private Map<String, Object> transitiveEntityBody;
    

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        extractor = new CourseOfferingExtractor(mockExtractor, mockMap, mockRepo);
        
        entityBody = new HashMap<String, Object>();
        entityBody.put("courseId", "courseId1");
        Mockito.when(mockEntity.getBody()).thenReturn(entityBody);
        Mockito.when(mockEntity.getEntityId()).thenReturn("courseOfferingId1");
        transitiveEntityBody = new HashMap<String, Object>();
        transitiveEntityBody.put("courseId", "courseId2");
        Mockito.when(mockTransitiveEntity.getBody()).thenReturn(transitiveEntityBody);
        Mockito.when(mockTransitiveEntity.getEntityId()).thenReturn("courseOfferingId2");
        
        Mockito.when(mockMap.getExtractFileForLea("edorgId1")).thenReturn(mockFile);
        Mockito.when(mockEdorgCache.getEntriesById("edorgId1")).thenReturn(new HashSet<String>(Arrays.asList("edorgId1")));
    }
    
    @Test
    public void testWriteOneEntity() {
        Mockito.when(mockRepo.findEach(Mockito.eq("courseOffering"), Mockito.eq(new Query())))
                .thenReturn(Arrays.asList(mockEntity).iterator());
        entityBody.put("schoolId", "edorgId1");
        extractor.extractEntities(mockEdorgCache, mockCourseOfferingCache);
        Mockito.verify(mockExtractor).extractEntity(Mockito.eq(mockEntity), Mockito.eq(mockFile),
                Mockito.eq("courseOffering"));
    }
    
    @Test
    public void testWriteManyEntities() {
        Mockito.when(mockRepo.findEach(Mockito.eq("courseOffering"), Mockito.eq(new Query())))
                .thenReturn(Arrays.asList(mockEntity, mockEntity, mockEntity).iterator());
        entityBody.put("schoolId", "edorgId1");
        extractor.extractEntities(mockEdorgCache, mockCourseOfferingCache);
        Mockito.verify(mockExtractor, Mockito.times(3)).extractEntity(Mockito.eq(mockEntity), Mockito.eq(mockFile),
                Mockito.eq("courseOffering"));
    }
    
    @Test
    public void testExtractNoEntityBecauseOfLEAMiss() {
        Mockito.when(mockRepo.findEach(Mockito.eq("courseOffering"), Mockito.eq(new Query())))
                .thenReturn(Arrays.asList(mockEntity).iterator());
        Mockito.when(mockMap.getExtractFileForLea("edorgId1")).thenReturn(null);
        entityBody.put("schoolId", "edorgId1");
        extractor.extractEntities(mockEdorgCache, mockCourseOfferingCache);
        Mockito.verify(mockExtractor, Mockito.never()).extractEntity(Mockito.eq(mockEntity), Mockito.eq(mockFile),
                Mockito.eq("courseOffering"));
    }
    
    @Test
    public void testExtractNoEntityBecauseOfIdMiss() {
        entityBody.put("schoolId", "missId");
        Mockito.when(mockRepo.findEach(Mockito.eq("courseOffering"), Mockito.eq(new Query())))
                .thenReturn(Arrays.asList(mockEntity).iterator());
        Mockito.when(mockMap.getExtractFileForLea("LEA-1")).thenReturn(mockFile);
        extractor.extractEntities(mockEdorgCache, mockCourseOfferingCache);
        Mockito.verify(mockExtractor, Mockito.never()).extractEntity(Mockito.eq(mockEntity), Mockito.eq(mockFile),
                Mockito.eq("courseOffering"));
    }
    
    @Test
    public void testTransitiveNoDupes() {
        entityBody.put("schoolId", "edorgId1");;
        Mockito.when(mockRepo.findEach(Mockito.eq("courseOffering"), Mockito.eq(new Query())))
        .thenReturn(Arrays.asList(mockEntity).iterator());
        Mockito.when(mockCourseOfferingCache.getEntriesById("courseOfferingId1"))
                .thenReturn(new HashSet<String>(Arrays.asList("edorgId1")));

        extractor.extractEntities(mockEdorgCache, mockCourseOfferingCache);
        Mockito.verify(mockExtractor, Mockito.times(1)).extractEntity(Mockito.eq(mockEntity), Mockito.eq(mockFile),
                Mockito.eq("courseOffering"));
    }

    @Test
    public void testTransitiveTwoEntities() {
        entityBody.put("schoolId", "edorgId1");
        transitiveEntityBody.put("schoolId", "edorgId2");
        Mockito.when(mockRepo.findEach(Mockito.eq("courseOffering"), Mockito.eq(new Query())))
                .thenReturn(Arrays.asList(mockEntity, mockTransitiveEntity).iterator());
        
        Mockito.when(mockCourseOfferingCache.getEntriesById("courseOfferingId2"))
                .thenReturn(new HashSet<String>(Arrays.asList("edorgId2")));
        Mockito.when(mockMap.getExtractFileForLea("edorgId1")).thenReturn(mockFile);
        Mockito.when(mockMap.getExtractFileForLea("edorgId2")).thenReturn(mockFile);
        extractor.extractEntities(mockEdorgCache, mockCourseOfferingCache);
        
        Mockito.verify(mockExtractor).extractEntity(Mockito.eq(mockEntity), Mockito.eq(mockFile),
                Mockito.eq("courseOffering"));
        Mockito.verify(mockExtractor).extractEntity(Mockito.eq(mockTransitiveEntity), Mockito.eq(mockFile),
                Mockito.eq("courseOffering"));
    
        Assert.assertEquals(2, extractor.getCourseCache().getEntityIds().size());
    }
    
    @Test
    public void testTransitiveNoMatch() {
        entityBody.put("schoolId", "edorgId1");;
        transitiveEntityBody.put("schoolId", "edorgId2");
        Mockito.when(mockRepo.findEach(Mockito.eq("courseOffering"), Mockito.eq(new Query())))
                .thenReturn(Arrays.asList(mockEntity, mockTransitiveEntity).iterator());
        
        Mockito.when(mockCourseOfferingCache.getEntriesById("courseId2"))
                .thenReturn(new HashSet<String>(Arrays.asList("edorgId3")));
        Mockito.when(mockMap.getExtractFileForLea("edorgId1")).thenReturn(mockFile);
        Mockito.when(mockMap.getExtractFileForLea("edorgId2")).thenReturn(mockFile);
        extractor.extractEntities(mockEdorgCache, mockCourseOfferingCache);
        
        Mockito.verify(mockExtractor).extractEntity(Mockito.eq(mockEntity), Mockito.eq(mockFile),
                Mockito.eq("courseOffering"));
        Mockito.verify(mockExtractor, Mockito.times(0)).extractEntity(Mockito.eq(mockTransitiveEntity), Mockito.eq(mockFile),
                Mockito.eq("courseOffering"));
    }

    @Test
    public void testCourseCache() {
        entityBody.put("schoolId", "edorgId1");
        transitiveEntityBody.put("schoolId", "edorgId2");
        Mockito.when(mockRepo.findEach(Mockito.eq("courseOffering"), Mockito.eq(new Query())))
                .thenReturn(Arrays.asList(mockEntity, mockTransitiveEntity).iterator());
        
        Mockito.when(mockCourseOfferingCache.getEntriesById("courseOfferingId2"))
                .thenReturn(new HashSet<String>(Arrays.asList("edorgId2", "edorgId3")));
        Mockito.when(mockMap.getExtractFileForLea("edorgId1")).thenReturn(mockFile);
        Mockito.when(mockMap.getExtractFileForLea("edorgId2")).thenReturn(mockFile);
        extractor.extractEntities(mockEdorgCache, mockCourseOfferingCache);
        
        Mockito.verify(mockExtractor).extractEntity(Mockito.eq(mockEntity), Mockito.eq(mockFile),
                Mockito.eq("courseOffering"));
        Mockito.verify(mockExtractor).extractEntity(Mockito.eq(mockTransitiveEntity), Mockito.eq(mockFile),
                Mockito.eq("courseOffering"));
    
        Assert.assertEquals(2, extractor.getCourseCache().getEntityIds().size());
        Assert.assertEquals(1, extractor.getCourseCache().getEntriesById("courseId1").size());
        Assert.assertEquals(2, extractor.getCourseCache().getEntriesById("courseId2").size());
    }
}
