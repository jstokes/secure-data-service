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
package org.slc.sli.search.transform;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slc.sli.search.config.IndexConfigStore;
import org.slc.sli.search.entity.IndexEntity;
import org.slc.sli.search.entity.IndexEntity.Action;
import org.slc.sli.search.process.impl.LoaderImpl;
import org.slc.sli.search.transform.impl.GenericEntityConverter;
import org.slc.sli.search.util.IndexEntityUtil;
import org.slc.sli.search.util.SearchIndexerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IndexEntityConverterTest {
    private final IndexEntityConverter indexEntityConverter = new IndexEntityConverter();
    private final EntityConverterFactory entityConverterFactory = new EntityConverterFactory();
    private final GenericEntityConverter genericEntityConverter = new GenericEntityConverter();

    private static final Logger LOG = LoggerFactory.getLogger(LoaderImpl.class);
    
    @Before
    public void setup() throws Exception{
        indexEntityConverter.setDecrypt(false);
        genericEntityConverter.setIndexConfigStore(new IndexConfigStore("index-config-test.json"));
        entityConverterFactory.setGenericEntityConverter(genericEntityConverter);
        indexEntityConverter.setEntityConverterFactory(entityConverterFactory);
    }
    
    @Test
    public void testToIndexEntity() throws Exception {
        String entity = "{\"_id\": \"1\", \"type\": \"test\", \"body\":{\"name\":\"a\", \"a\":\"1\", \"b\":\"x\"}, \"metaData\": {\"tenantId\": \"tenant\"}}";
        List<IndexEntity> indexEntities = indexEntityConverter.fromEntityJson("tenant", entity);
        IndexEntity indexEntity = indexEntities.get(0);
        Assert.assertEquals("a", indexEntity.getId());
        Assert.assertEquals("tenant", indexEntity.getIndex());
        Assert.assertEquals("student", indexEntity.getType());
        Assert.assertEquals("{\"a\":\"1\",\"metaData\":{\"contextId\":\"x\"},\"append\":\"ALL\"}", IndexEntityUtil.getBody(Action.INDEX, indexEntity.getBody()));
    }
    
    @Test
    public void testFilter() throws Exception {
        String entity = "{\"_id\": \"1\", \"type\": \"test\", \"body\":{\"name\":\"a\", \"a\":\"1\", \"b\":\"x\"}, \"test\": { \"filter\": \"notnull\"}, \"metaData\": {\"tenantId\": \"tenant\"}}";
        List<IndexEntity> indexEntities = indexEntityConverter.fromEntityJson("tenant", entity);
        Assert.assertEquals(0, indexEntities.size());
    }
    
    @Test
    public void testException() throws Exception {
        String entity = "{\"_id\": \"1\", \"type\": \"test\", \"body\":{\"b\":\"x\"}}";
        try {
          indexEntityConverter.fromEntityJson(null, entity);
          Assert.fail("Does not include metaData - should fail");
        } catch (SearchIndexerException sie) {
            LOG.info("There was a SearchIndexerException exception.", sie);
        }
    }
    
}
