package org.slc.sli.search.process;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.codec.binary.Base64;
import org.slc.sli.search.entity.IndexEntity;
import org.slc.sli.search.util.IndexEntityConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * Indexer is responsible for building elastic search index requests and
 * sending them to the elastic search server for processing.
 * 
 * @author dwu
 * 
 */
public class Indexer {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    private static final int DEFAULT_BULK_SIZE = 5000;
    private static final int MAX_AGGREGATE_PERIOD = 2000;
    
    @Autowired
    IndexEntityConverter indexEntityConverter;
    
    private String esUri;
    
    private RestTemplate searchTemplate;
    
    private String esUsername;
    
    private String esPassword;
    
    private int bulkSize = DEFAULT_BULK_SIZE;
    
    LinkedBlockingQueue<IndexEntity> indexRequests = new LinkedBlockingQueue<IndexEntity>(DEFAULT_BULK_SIZE * 2);
    ReentrantLock indexLock = new ReentrantLock();
    
    private ScheduledExecutorService queueMonitor = Executors.newScheduledThreadPool(1);
    // last message timestamp
    private long lastUpdate = 0L;

    /**
     * Issue bulk index request if reached the size or not empty and last update past tolerance period
     *
     */
    private class IndexQueueMonior implements Runnable {
        public void run() {
            try {
                indexLock.lock();
                if (indexRequests.size() >= bulkSize || 
                    (indexRequests.size() > 0 && (System.currentTimeMillis() - lastUpdate > MAX_AGGREGATE_PERIOD))) {
                    List<IndexEntity> col = new ArrayList<IndexEntity>();
                    indexRequests.drainTo(col);
                    executeBulkHttp(col);
                }
            } catch (Throwable t) {
                logger.info("Unable to index with elasticsearch", t);
            } finally {
                indexLock.unlock();
            }
        }
        
    }
    
    public void init() {
        searchTemplate = new RestTemplate();
        queueMonitor.scheduleAtFixedRate(new IndexQueueMonior(), MAX_AGGREGATE_PERIOD, MAX_AGGREGATE_PERIOD, TimeUnit.MILLISECONDS);
    }
    
    public void destroy() {
        queueMonitor.shutdown();
    }
    
    public void index(IndexEntity entity) {
        try {
            indexLock.lock();
            indexRequests.add(entity);
            lastUpdate = System.currentTimeMillis();
        } finally {
            indexLock.unlock();
        }
    }
    
    /**
     * Takes a collection of index requests, builds a bulk http message to send to elastic search
     * 
     * @param indexRequests
     */
    public void executeBulkHttp(List<IndexEntity> indexRequests) {
        
        logger.info("Sending bulk index request with " + indexRequests.size() + "records");
        // create bulk http message
        StringBuilder message = new StringBuilder();
        
        /*
         * format of message data
         * { "index" : { "_index" : "test", "_type" : "type1", "_id" : "1" } }
         * { "field1" : "value1" }
         */
        
        // add each index request to the message
        while (!indexRequests.isEmpty()) {
            message.append(indexEntityConverter.toIndexJson(indexRequests.remove(0)));
        }
        // send the message
        HttpEntity<String> response = sendRESTCall(message.toString());
        logger.info("Bulk index response: " + response.getBody());
        
        // TODO: do we need to check the response status of each part of the bulk request?
        
    }
    
    /**
     * Send REST query to elasticsearch server
     * 
     * @param query
     * @return
     */
    private HttpEntity<String> sendRESTCall(String query) {
        HttpMethod method = HttpMethod.POST;
        HttpHeaders headers = new HttpHeaders();
        
        // Basic Authentication when username and password are provided
        if (esUsername != null && esPassword != null) {
            headers.set("Authorization",
                    "Basic " + Base64.encodeBase64String((esUsername + ":" + esPassword).getBytes()));
        }
        
        HttpEntity<String> entity = new HttpEntity<String>(query, headers);
        
        // make the REST call
        try {
            return searchTemplate.exchange(esUri, method, entity, String.class);
        } catch (RestClientException rce) {
            logger.error("Error sending elastic search request!", rce);
            throw rce;
        }
    }
    
    public void setSearchUrl(String esUrl) {
        this.esUri = esUrl + "/_bulk";
    }
    
    public void setSearchUsername(String esUsername) {
        this.esUsername = esUsername;
    }
    
    public void setSearchPassword(String esPassword) {
        this.esPassword = esPassword;
    }
    
    public void setBulkSize(int bulkSize) {
        this.bulkSize = bulkSize;
    }
}
