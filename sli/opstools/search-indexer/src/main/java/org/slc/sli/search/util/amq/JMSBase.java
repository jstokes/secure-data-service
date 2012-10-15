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
public abstract class JMSBase {

    private String mqHost;

    private int mqPort;

    private String mqUsername;

    private String mqPswd;

    private String queue;

    private String brokerURI;
    private Session session;
    private Connection connection;
    private Destination destination;
    private MessageType messageType;

    private boolean embeddedBroker = false;
    private static BrokerService broker = null;

    enum MessageType {
        QUEUE, TOPIC;
    }

    public JMSBase(MessageType messageType) {
        this.messageType = messageType;
    }

    public void init() throws Exception {
        if (this.embeddedBroker) {
            //start embedded broker
            if (broker == null) {
                broker = new BrokerService();
                broker.setPersistent(false);
                broker.setUseJmx(true);
                
                broker.addConnector("stomp://localhost:61613");
                broker.addConnector("tcp://localhost:61616");
                broker.start();
            }
            //use localhost and port 61616 for embedded broker to access
            this.brokerURI = "tcp://localhost:61616";

        } else {
            this.brokerURI = "tcp://" + this.mqHost + ":" + this.mqPort;
        }

        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(this.brokerURI);
        if ((this.mqUsername == null && this.mqPswd == null)||this.embeddedBroker) {
            this.connection = connectionFactory.createConnection();
        } else {
            this.connection = connectionFactory.createConnection(this.mqUsername, this.mqPswd);
        }

        this.connection.start();

        // set true if it requires acknowledge
        this.session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        if (this.messageType == MessageType.QUEUE) {
            this.destination = session.createQueue(this.queue);
        } else if (this.messageType == MessageType.TOPIC) {
            this.destination = this.session.createTopic(this.queue);
        }
    }

    public void destroy() throws JMSException {
        session.close();
        connection.close();
    }

    /**
     * get MessageProducer (for publisher)
     * 
     * @return
     * @throws JMSException
     */
    protected MessageProducer getProducer() throws JMSException {
        MessageProducer producer = session.createProducer(this.destination);
        return producer;
    }

    /**
     * get MessageConsumer (for subscriber)
     * 
     * @return
     * @throws JMSException
     */
    protected MessageConsumer getConsumer() throws JMSException {
        MessageConsumer consumer = session.createConsumer(this.destination);
        return consumer;
    }

    protected Session getSession() {
        return this.session;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }

    public void setBrokerURI(String brokerURI) {
        this.brokerURI = brokerURI;
    }

    public void setMqHost(String mqHost) {
        this.mqHost = mqHost;
    }

    public void setMqPort(int mqPort) {
        this.mqPort = mqPort;
    }

    public void setMqPswd(String mqPswd) {
        this.mqPswd = mqPswd;
    }

    public void setMqUsername(String mqUsername) {
        this.mqUsername = mqUsername;
    }

    public void setEmbeddedBroker(boolean embeddedBroker) {
        this.embeddedBroker = embeddedBroker;
    }

}
