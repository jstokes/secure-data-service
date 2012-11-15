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
package org.slc.sli.search.util.amq;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;

/**
 * Base class to create ActiveMQ JMS Connection for either Topic or Queue
 * 
 * @author Takashi Osako
 * 
 */
public class ActiveMQConnection {

    private String brokerUsername;

    private String brokerPswd;

    private String queue;

    private String brokerURI;
    private Session session;
    private Connection connection;

    private boolean embeddedBroker = false;
    private BrokerService broker = null;

    private String[] embeddedConnectors;
    private String embeddedBrokerURI;
    private int tempUsage;

    public enum MessageType {
        QUEUE, TOPIC;
    }

    public void init() throws Exception {
        if (this.embeddedBroker) {
            // start embedded broker
            this.broker = new BrokerService();

            for (String url : embeddedConnectors) {
                this.broker.addConnector(url);
            }
            this.brokerURI = this.embeddedBrokerURI;
            this.broker.getSystemUsage().getTempUsage().setLimit(tempUsage);
            this.broker.start();
        }

        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(this.brokerURI);
        if (this.brokerUsername == null) {
            this.connection = connectionFactory.createConnection();
        } else {
            this.connection = connectionFactory.createConnection(this.brokerUsername, this.brokerPswd);
        }

        this.connection.start();

        // set true if it requires acknowledge
        this.session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

    }

    public void destroy() throws Exception {
        if (this.session != null) {
            this.session.close();
        }
        if (this.connection != null) {
            this.connection.close();
        }
        if (broker != null) {
            broker.stop();
        }
    }

    /**
     * get MessageProducer (for publisher)
     * 
     * @return
     * @throws JMSException
     */
    public MessageProducer getProducer(MessageType messageType) throws JMSException {
        return session.createProducer(getDestination(messageType));
    }

    /**
     * get MessageConsumer (for subscriber)
     * 
     * @return
     * @throws JMSException
     */
    public MessageConsumer getConsumer(MessageType messageType) throws JMSException {
        return session.createConsumer(getDestination(messageType));
    }

    /**
     * create destination by messageType
     * 
     * @param messageType
     * @return
     * @throws JMSException
     */
    private Destination getDestination(MessageType messageType) throws JMSException {
        Destination destination = null;
        if (messageType == MessageType.QUEUE) {
            destination = session.createQueue(this.queue);
        } else if (messageType == MessageType.TOPIC) {
            destination = this.session.createTopic(this.queue);
        }
        return destination;
    }

    public Session getSession() {
        return this.session;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }

    public void setBrokerURI(String brokerURI) {
        this.brokerURI = brokerURI;
    }

    public void setBrokerPswd(String brokerPswd) {
        this.brokerPswd = brokerPswd;
    }

    public void setBrokerUsername(String brokerUsername) {
        this.brokerUsername = brokerUsername;
    }

    public void setEmbeddedBroker(boolean embeddedBroker) {
        this.embeddedBroker = embeddedBroker;
    }

    public void setEmbeddedConnectors(String[] embeddedConnectors) {
        this.embeddedConnectors = embeddedConnectors;
    }

    public void setEmbeddedBrokerURI(String embeddedBrokerURI) {
        this.embeddedBrokerURI = embeddedBrokerURI;
    }

    public void setTempUsage(int tempUsage) {
        this.tempUsage = tempUsage;
    }

}
