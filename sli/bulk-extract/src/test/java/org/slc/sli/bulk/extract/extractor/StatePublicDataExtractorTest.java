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
package org.slc.sli.bulk.extract.extractor;

import java.io.File;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.slc.sli.bulk.extract.BulkExtractMongoDA;
import org.slc.sli.common.constants.EntityNames;
import org.slc.sli.domain.Entity;
import org.slc.sli.domain.MongoEntity;
import org.slc.sli.domain.NeutralQuery;
import org.slc.sli.domain.Repository;

/**
 * JUnit test for StatePublicDataExtractor.
 * @author npandey
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/applicationContext-test.xml" })
public class StatePublicDataExtractorTest {

    @Autowired
    @InjectMocks
    private StatePublicDataExtractor statePublicDataExtractor;

    @Mock
    private BulkExtractMongoDA bulkExtractMongoDA;

    @Mock
    private Repository<Entity> entityRepository;

    /**
     * Initialization for tests.
     */
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Test one SEA.
     */
    @Test
    public void testOneSEA() {
        List<Entity> seaList = new ArrayList<Entity>();
        seaList.add(new MongoEntity(EntityNames.EDUCATION_ORGANIZATION, "123_id", null, null));
        Mockito.when(entityRepository.findAll(Mockito.eq(EntityNames.EDUCATION_ORGANIZATION), Mockito.any(NeutralQuery.class))).thenReturn(seaList);

        String id = statePublicDataExtractor.retrieveSEAId();

        Assert.assertEquals("123_id", id);
    }

    /**
     * Test no SEA.
     */
    @Test
    public void testNoSEA() {
        List<Entity> seaList = null;
        Mockito.when(entityRepository.findAll(Mockito.eq(EntityNames.EDUCATION_ORGANIZATION), Mockito.any(NeutralQuery.class))).thenReturn(seaList);

        String id = statePublicDataExtractor.retrieveSEAId();

        Assert.assertNull(id);
    }

    /**
     * Test Empty SEA List.
     */
    @Test
    public void testEmptySEA() {
        List<Entity> seaList = new ArrayList<Entity>();
        Mockito.when(entityRepository.findAll(Mockito.eq(EntityNames.EDUCATION_ORGANIZATION), Mockito.any(NeutralQuery.class))).thenReturn(seaList);

        String id = statePublicDataExtractor.retrieveSEAId();

        Assert.assertNull(id);
    }

    /**
     * Test multiple SEA.
     */
    @Test
    public void testMultipleSEA() {
        List<Entity> seaList = new ArrayList<Entity>();
        seaList.add(new MongoEntity(EntityNames.EDUCATION_ORGANIZATION, "123_id", null, null));
        seaList.add(new MongoEntity(EntityNames.EDUCATION_ORGANIZATION, "456_id", null, null));
        Mockito.when(entityRepository.findAll(Mockito.eq(EntityNames.EDUCATION_ORGANIZATION), Mockito.any(NeutralQuery.class))).thenReturn(seaList);

        String id = statePublicDataExtractor.retrieveSEAId();

        Assert.assertNull(id);
    }

    /**
     * Test when the SEA condition fails.
     */
    @Test
    public void testExecuteInvalidSEA() {
        StatePublicDataExtractor extractor = Mockito.spy(statePublicDataExtractor);
        Mockito.when(extractor.retrieveSEAId()).thenReturn(null);

        extractor.execute("tenant", new File("temp"), new DateTime());

        Mockito.verify(bulkExtractMongoDA, Mockito.never()).getAppPublicKeys();
        Mockito.verify(extractor, Mockito.never()).extractPublicData(Mockito.anyString(), Mockito.anyMap());
    }

    /**
     * Test when the app condition fails.
     */
    @Test
    public void testExecuteNoValidApp() {
        StatePublicDataExtractor extractor = Mockito.spy(statePublicDataExtractor);
        String seaId = "123_id";
        Mockito.doReturn(seaId).when(extractor).retrieveSEAId();
        Mockito.when(bulkExtractMongoDA.getAppPublicKeys()).thenReturn(new HashMap<String, PublicKey>());

        extractor.execute("tenant", new File("temp"), new DateTime());

        Mockito.verify(extractor, Mockito.never()).extractPublicData(Mockito.anyString(), Mockito.anyMap());
    }

    /**
     * Test valid case when all pre conditions are met.
     */
    @Test
    public void testValidExecutionConditions() {
        StatePublicDataExtractor extractor = Mockito.spy(statePublicDataExtractor);
        String seaId = "123_id";
        Map<String, PublicKey> clientKeys = new HashMap<String, PublicKey>();
        PublicKey key = Mockito.mock(PublicKey.class);
        clientKeys.put("app_id", key);

        Mockito.doReturn(seaId).when(extractor).retrieveSEAId();
        Mockito.doReturn(clientKeys).when(bulkExtractMongoDA).getAppPublicKeys();

        extractor.execute("tenant", new File("temp"), new DateTime());

        Mockito.verify(extractor, Mockito.atLeastOnce()).extractPublicData(Mockito.anyString(), Mockito.anyMap());
    }
}
