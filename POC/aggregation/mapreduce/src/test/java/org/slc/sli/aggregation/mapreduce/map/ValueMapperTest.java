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

package org.slc.sli.aggregation.mapreduce.map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.hadoop.fs.ContentSummary;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapred.OutputCollector;
import org.bson.BSONObject;
import org.bson.BasicBSONObject;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;

import org.slc.sli.aggregation.mapreduce.map.key.EmittableKey;
import org.slc.sli.aggregation.mapreduce.map.key.IdFieldEmittableKey;

/**
 * ValueMapperTest
 */
public class ValueMapperTest {

    private class MockValueMapper extends ValueMapper {

        @Override
        public Writable getValue(BSONObject entity) {
            if (entity.containsField("found")) {
                return new ContentSummary(1, 2, 3);
            } else {
                return NullWritable.get();
            }
        }
    }

    @Test
    public void testMap() throws Exception {
        IdFieldEmittableKey key = new IdFieldEmittableKey();
        ValueMapper m = new MockValueMapper();
        BSONObject entity = new BasicBSONObject("found", "data");

        @SuppressWarnings("unchecked")
        OutputCollector<EmittableKey, Writable> collector = Mockito.mock(OutputCollector.class);
        PowerMockito.when(collector, "collect", Matchers.any(EmittableKey.class),
            Matchers.any(BSONObject.class)).thenAnswer(new Answer<BSONObject>() {

            @Override
            public BSONObject answer(InvocationOnMock invocation) throws Throwable {

                Object[] args = invocation.getArguments();

                assertNotNull(args);
                assertEquals(args.length, 2);

                assertTrue(args[0] instanceof IdFieldEmittableKey);
                assertTrue(args[1] instanceof ContentSummary);

                IdFieldEmittableKey id = (IdFieldEmittableKey) args[0];
                assertEquals(id.getIdField().toString(), "_id");

                ContentSummary e = (ContentSummary) args[1];
                assertEquals(e.getLength(), 1);
                assertEquals(e.getFileCount(), 2);
                assertEquals(e.getDirectoryCount(), 3);

                return null;
            }
        });

        m.map(key, entity, collector, null);
    }

    @Test
    public void testMapValueNotFound() throws Exception {
        IdFieldEmittableKey key = new IdFieldEmittableKey();
        ValueMapper m = new MockValueMapper();
        BSONObject entity = new BasicBSONObject("not_found", "data");

        @SuppressWarnings("unchecked")
        OutputCollector<EmittableKey, Writable> collector = Mockito.mock(OutputCollector.class);
        PowerMockito.when(collector, "collect", Matchers.any(EmittableKey.class),
            Matchers.any(BSONObject.class)).thenAnswer(new Answer<BSONObject>() {

            @Override
            public BSONObject answer(InvocationOnMock invocation) throws Throwable {

                Object[] args = invocation.getArguments();

                assertNotNull(args);
                assertEquals(args.length, 2);

                assertTrue(args[0] instanceof IdFieldEmittableKey);
                assertTrue(args[1] instanceof NullWritable);

                return null;
            }
        });

        m.map(key, entity, collector, null);
    }
}
