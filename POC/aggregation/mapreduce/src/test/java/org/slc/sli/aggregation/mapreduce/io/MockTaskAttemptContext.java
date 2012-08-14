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

package org.slc.sli.aggregation.mapreduce.io;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.TaskAttemptID;

/**
 * MockTaskAttemptContext - mock for a mongo hadoop task context instance.
 */
public class MockTaskAttemptContext extends TaskAttemptContext {
    
    public MockTaskAttemptContext() throws IOException {
        super(new Configuration(), new TaskAttemptID());
        
        conf.set(MongoAggFormatter.KEY_FIELD, "_id");
        conf.set(MongoAggFormatter.UPDATE_FIELD, "body.name");
        conf.set("mongo.output.uri", "mongodb://test.server:27017/test.collection");
    }
}
