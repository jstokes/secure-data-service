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

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.mongodb.hadoop.util.MongoConfigUtil;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.lib.ChainMapper;
import org.bson.BSONObject;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

/**
 * ConfigurableChainedMapper
 *
 * A chained mapper implementation that allows for the creation of a map chain via configuration
 * rather than hard-coding.
 *
 * A chained mapper requires, at a minimum, the following configuration values:
 *
 * { "map_chain" : [
 * {
 * "description" : "Describe this mapper.",
 * "map_class" : "org.slc.sli.aggregation.mapreduce..map.IDMapper",
 * "input_collection": "sli.assessment",
 * "input_key_field" : "_id",
 * "input_key_type" : "org.apache.hadoop.io.Text",
 * "input_value_type" : "org.bson.BSONObject",
 * "read_from_secondaries" : true,
 * "output_key_type" : "org.slc.sli.aggregation.mapreduce.TenantAndIdEmittableKey",
 * "output_value_type" : "org.apache.hadoop.io.LongWritable",
 * "by_value" : false,
 * "query" : { "body.assessmentIdentificationCode.ID" : "Grade 7 2011 State Math" }
 * "fields" : { "_id" : 1 },
 * "hadoop_options' : [ {"name":"value"}, ... ]
 * }, {
 * mapper 2 configuration }, ... ] }"
 *
 */
public class ConfigurableChainedMapper extends ChainMapper {

    ObjectMapper om = new ObjectMapper();
    static Logger log = Logger.getLogger("ConfigurableChainedMapper");

    /**
     * mapper_entry available keys when defining a chain mapper.
     */
    enum mapper_entry {
        DESCRIPTION, MAP_CLASS, INPUT_COLLECTION, INPUT_KEY_FIELD, INPUT_KEY_TYPE, INPUT_VALUE_TYPE,
        READ_FROM_SECONDARIES, OUTPUT_KEY_TYPE, OUTPUT_VALUE_TYPE, BY_VALUE, QUERY, FIELDS,
        HADOOP_OPTIONS;

        @Override
        public String toString() {
            return super.toString().toLowerCase();
        }

        public static mapper_entry parseValue(String s) {
            return mapper_entry.valueOf(s.toUpperCase());
        }
    }

    public static final String CHAIN_CONF = "map_chain";

    @SuppressWarnings("unchecked")
    @Override
    public void configure(JobConf conf) {
        super.configure(conf);

        String chain = conf.get(CHAIN_CONF);

        if (chain == null) {
            log.severe("Invalid map/reduce configuration detected : no '" + CHAIN_CONF + "' field was included in JobConf.");
            return;
        }
        try {
            Map<String, Object> confMap = om.readValue(chain, Map.class);
            for (Map.Entry<String, Object> mappers : confMap.entrySet()) {
                if (mappers.getKey().equals(CHAIN_CONF)) {
                    List<Map<String, Object>> chains = (List<Map<String, Object>>) mappers.getValue();
                    for (Map<String, Object> mapper : chains) {
                        parseMapper(conf, mapper);
                    }
                }
            }
        } catch (IOException e) {
            log.severe("Invalid map/reduce configuration detected : parsing failed : "
                + e.toString());
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void parseMapper(JobConf conf, Map<String, Object> mapperDef) throws IOException {

        boolean byValue = false;
        String query = null;
        String fields = null;
        Class mapper = null;
        Class inputKey = Text.class;
        Class inputValue = BSONObject.class;
        Class outputKey = Text.class;
        Class outputValue = Text.class;
        String hadoopOptions = null;
        String keyField = null;
        String collection = null;
        boolean readFromSecondaries = false;

        for (Map.Entry<String, Object> entry : mapperDef.entrySet()) {
            mapper_entry key = mapper_entry.parseValue(entry.getKey());
            String value = entry.getValue().toString();

            switch (key) {
                case MAP_CLASS:
                    try {
                        mapper = Class.forName(value).asSubclass(Mapper.class);

                    } catch (ClassNotFoundException e) {
                        log.severe(String
                            .format(
                                "Defined mapper class (%s) was not found. Is the class on your classpath?", value));

                    } catch (ClassCastException e) {
                        log.severe(String.format("Defined mapper class (%s) does not extend org.apache.hadoop.mapred.Mapper.",
                                value));
                    }
                    break;
                case INPUT_KEY_TYPE:
                    try {
                        inputKey = Class.forName(value);
                    } catch (ClassNotFoundException e) {
                        log.severe(String.format(
                            "Defined output key type (%s) does not resolve to a class.", value));
                    }
                    break;
                case INPUT_VALUE_TYPE:
                    try {
                        inputValue = Class.forName(value);
                    } catch (ClassNotFoundException e) {
                        log.severe(String.format(
                            "Defined output key type (%s) does not resolve to a class.", value));
                    }
                    break;
                case READ_FROM_SECONDARIES:
                    readFromSecondaries = Boolean.parseBoolean(value);
                    break;
                case OUTPUT_KEY_TYPE:
                    try {
                        outputKey = Class.forName(value);
                    } catch (ClassNotFoundException e) {
                        log.severe(String.format(
                            "Defined output key type (%s) does not resolve to a class.", value));
                    }
                    break;
                case OUTPUT_VALUE_TYPE:
                    try {
                        outputValue = Class.forName(value);
                    } catch (ClassNotFoundException e) {
                        log.severe(String.format(
                            "Defined output value type (%s) does not resolve to a class.", value));
                    }
                    break;
                case BY_VALUE:
                    byValue = Boolean.parseBoolean(value);
                    break;
                case QUERY:
                    query = value;
                    break;
                case FIELDS:
                    fields = value;
                    break;
                case HADOOP_OPTIONS:
                    hadoopOptions = value;
                    break;
                case INPUT_KEY_FIELD:
                    keyField = value;
                    break;
                case INPUT_COLLECTION:
                    collection = value;
                    break;
                default:
                    break;
            }
        }

        // validate we have enough to continue
        if (mapper == null) {
            log.severe("Invalid map/reduce configuration detected : no mapper class specified.");
        }
        if (keyField == null) {
            log.severe("Invalid map/reduce configuration detected : no key field specified.");
        }
        if (collection == null) {
            log.severe("Invalid map/reduce configuration detected : no mongo collection specified.");
        }
        if (query == null) {
            log.severe("Invalid map/reduce configuration detected : no mongo input query specified.");
        }
        if (fields == null) {
            log.severe("Invalid map/reduce configuration detected : no mongo input fields specified.");
        }

        JobConf mapperConf = new JobConf(conf);
        // enable speculative execution. Multiple mapper tasks are created for the same split.
        // First one to finish wins; the remaining tasks are terminated.
        mapperConf.setSpeculativeExecution(true);
        mapperConf.setJarByClass(mapper);
        mapperConf.setUseNewMapper(false);
        mapperConf.setUseNewReducer(false);
        MongoConfigUtil.setQuery(mapperConf, query);
        MongoConfigUtil.setFields(mapperConf, fields);
        MongoConfigUtil.setInputKey(mapperConf, keyField);
        MongoConfigUtil.setInputURI(mapperConf, "mongodb://" + collection);
        MongoConfigUtil.setMapperOutputKey(mapperConf, outputKey);
        MongoConfigUtil.setMapperOutputValue(mapperConf, outputValue);
        MongoConfigUtil.setOutputKey(mapperConf, outputKey);
        MongoConfigUtil.setOutputValue(mapperConf, outputValue);
        MongoConfigUtil.setReadSplitsFromSecondary(mapperConf, readFromSecondaries);

        // Mongo Hadoop uses the 'new' mapReduce functions.  We can't support these on 1.0.3
        // as the chained mapper hasn't been ported to the new map functions in this version.
        mapperConf.setClass(MongoConfigUtil.JOB_MAPPER, mapper, Mapper.class);

        /**
         * Configure how hadoop calculates splits.
         *
         * We enable input splits to avoid having the entire job executed on a single hadoop node.
         *
         * We enable shard chunk splitting to allow mongo to specify how to split the input.
         *
         * We disable read splits from shards because we want hadoop connecting to mongos, not
         * mongod directly. This avoids incorrect results in situations where data is in the process
         * of migration at the same time hadoop is trying to read it.
         *
         * TODO - determine if we also need to set the input split key pattern. This depends
         * on how well data is distributed by _id. Setting the key pattern gives finer grained
         * control over how splits are calculated.
         */
        MongoConfigUtil.setCreateInputSplits(mapperConf, true);
        MongoConfigUtil.setShardChunkSplittingEnabled(mapperConf, false);
        MongoConfigUtil.setReadSplitsFromShards(mapperConf, false);

        /**
         * Any additional hadoop options are added to the configuration as key/value pairs.
         */
        if (hadoopOptions != null) {
            List<Map<String, String>> options =
                (List<Map<String, String>>) om.readValue(hadoopOptions,
                    new TypeReference<List<Map<String, String>>>() {
                    });
            for (Map<String, String> option : options) {
                for (Map.Entry<String, String> keyValue : option.entrySet()) {
                    mapperConf.set(keyValue.getKey(), keyValue.getValue());
                }
            }
        }

        // Add this mapper to the map chain.
        addMapper(conf, mapper, inputKey, inputValue, outputKey, outputValue, byValue, mapperConf);
    }

}
