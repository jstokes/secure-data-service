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
package org.slc.sli.search.process.impl;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.activemq.command.ActiveMQBytesMessage;
import org.apache.activemq.util.ByteSequence;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.slc.sli.search.entity.IndexEntity;
import org.slc.sli.search.process.IncrementalLoader;
import org.slc.sli.search.process.Indexer;
import org.slc.sli.search.transform.IndexEntityConverter;
import org.slc.sli.search.util.OplogConverter;
import org.slc.sli.search.util.OplogConverter.Meta;
import org.slc.sli.search.util.SearchIndexerException;
import org.slc.sli.search.util.amq.JMSQueueConsumer;

/**
 * Listens to ActiveMQ for Sarje messages. Filters the message and passes
 * data to the Indexer to be indexed in elasticsearch.
 *
 * @author dwu
 *
 */
public class IncrementalListenerImpl implements IncrementalLoader {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private Indexer indexer;

    private IndexEntityConverter indexEntityConverter;

    private ObjectMapper mapper = new ObjectMapper();

    private JMSQueueConsumer activeMQConsumer;

    public void init() {
        listen();
    }

    public void destroy() throws Exception {
        this.activeMQConsumer.destroy();

    }

    /*
     * (non-Javadoc)
     *
     * @see org.slc.sli.search.process.impl.IncrementalListener#listen()
     */
    public void listen() {
        try {
            this.activeMQConsumer.getConsumer().setMessageListener(new MessageListener() {
                public void onMessage(Message message) {
                    process(message);
                }
            });
        } catch (JMSException jmse) {
            throw new SearchIndexerException("Unable to listen to oplog queue", jmse);
        }
    }

    /**
     * Process a message from the queue
     *
     * @param opLog
     */
    public void process(Message message) {
        if (message != null) {
            try {
                String opLog = "";
                // for now we will always receive ActiveMQBytesMessage.
                // we also support TextMessage if it ever needs to be used.
                if (message instanceof ActiveMQBytesMessage) {
                    ActiveMQBytesMessage byteMessage = (ActiveMQBytesMessage) message;
                    ByteSequence bs = byteMessage.getContent();
                    InputStream is = new ByteArrayInputStream(bs.getData());
                    BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                    String line = "";
                    StringBuilder sb = new StringBuilder();
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }
                    opLog = sb.toString();

                } else if (message instanceof TextMessage) {
                    TextMessage textMessage = (TextMessage) message;
                    opLog = textMessage.getText();
                }

                List<IndexEntity> entities = convertToEntity(opLog);
                for (IndexEntity entity : entities) {
                    sendToIndexer(entity);
                }
            } catch (Exception e) {
                logger.error("Error processing message", e);
            }
        }
    }

    /**
     * Convert oplog message to an IndexEntity, based on the action type (insert, update, delete)
     *
     * @param opLog
     * @return
     * @throws Exception
     */
    public List<IndexEntity> convertToEntity(String opLogString) throws Exception {

        List<IndexEntity> entities = new LinkedList<IndexEntity>();

        List<Map<String, Object>> opLogs = mapper.readValue(OplogConverter.preProcess(opLogString),
                new TypeReference<List<Map<String, Object>>>() {
                });
        // check action type and convert to an index entity
        for (Map<String, Object> opLog : opLogs) {
            try {
                if (OplogConverter.isInsert(opLog)) {
                    entities.add(convertInsertToEntity(opLog));
                } else if (OplogConverter.isUpdate(opLog)) {
                    entities.add(convertUpdateToEntity(opLog));
                } else if (OplogConverter.isDelete(opLog)) {
                    entities.add(convertDeleteToEntity(opLog));
                } else {
                    logger.info("Unrecognized message type. Ignoring.");
                }
            } catch (Exception e) {
                if (logger.isDebugEnabled()) {
                    logger.info("Message: " + mapper.writeValueAsString(opLogs));
                }
                throw e;
            }
        }
        return entities;
    }

    private IndexEntity convertInsertToEntity(Map<String, Object> opLogMap) throws Exception {
        Meta meta = OplogConverter.getMeta(opLogMap);
        return indexEntityConverter.fromEntity(meta.getIndex(), IndexEntity.Action.INDEX,
                OplogConverter.getEntityForInsert(opLogMap));
    }

    private IndexEntity convertUpdateToEntity(Map<String, Object> opLogMap) throws Exception {
        Meta meta = OplogConverter.getMeta(opLogMap);
        return indexEntityConverter.fromEntity(meta.getIndex(), IndexEntity.Action.UPDATE,
                OplogConverter.getEntityForUpdate(opLogMap));
    }

    private IndexEntity convertDeleteToEntity(Map<String, Object> opLogMap) throws Exception {
        // convert to index entity object
        Meta meta = OplogConverter.getMeta(opLogMap);
        return indexEntityConverter.fromEntity(meta.getIndex(), IndexEntity.Action.DELETE,
                OplogConverter.getEntityForDelete(opLogMap));
    }

    private void sendToIndexer(IndexEntity entity) {
        indexer.index(entity);
    }

    public void setIndexer(Indexer indexer) {
        this.indexer = indexer;
    }

    public void setIndexEntityConverter(IndexEntityConverter indexEntityConverter) {
        this.indexEntityConverter = indexEntityConverter;
    }

    public void setMapper(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public void setActiveMQConsumer(JMSQueueConsumer activeMQConsumer) {
        this.activeMQConsumer = activeMQConsumer;
    }

    public String getHealth() {
        return getClass() + "{}";
    }

}
