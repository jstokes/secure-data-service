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
package org.slc.sli.search.connector.impl;

import java.util.Arrays;

import org.apache.commons.codec.binary.Base64;
import org.slc.sli.search.connector.SearchEngineConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestOperations;

/**
 * Indexer is responsible for building elastic search index requests and
 * sending them to the elastic search server for processing.
 * 
 * @author dwu
 * 
 */
public class SearchEngineConnectorImpl implements SearchEngineConnector {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    private String bulkUri;

    private String mGetUri;
    
    private String mUpdateUri;
    
    private String indexUri;
    
    private String indexTypeUri;
    
    private RestOperations searchTemplate;
    
    private String esUsername;
    
    private String esPassword;

    private String esUrl;
    
    /**
     * Send REST query to elasticsearch server
     * 
     * @param query
     * @return
     */
    private ResponseEntity<String> sendRESTCall(HttpMethod method, String url, String query, Object... uriParams) {
        HttpHeaders headers = new HttpHeaders();
        
        // Basic Authentication when username and password are provided
        if (esUsername != null && esPassword != null) {
            headers.set("Authorization",
                    "Basic " + Base64.encodeBase64String((esUsername + ":" + esPassword).getBytes()));
        }
        HttpEntity<String> entity = new HttpEntity<String>(query, headers);
        if (logger.isDebugEnabled())
            logger.debug(String.format("%s Request: %s, [%s]", method.name(), url, Arrays.asList(uriParams)));
        
        // make the REST call
        try {
            return searchTemplate.exchange(url, method, entity, String.class, uriParams);
        } catch (HttpClientErrorException rce) {
            return new ResponseEntity<String>(rce.getStatusCode());
        }
    }
    

    public String executeGet(String url, Object... uriParams) {
        return sendRESTCall(HttpMethod.GET, url, null, uriParams).getBody();
    }

    public HttpStatus executeDelete(String url, Object... uriParams) {
        return sendRESTCall(HttpMethod.DELETE, url, null, uriParams).getStatusCode();
    }

    public HttpStatus executeHead(String url, Object... uriParams) {
        return sendRESTCall(HttpMethod.HEAD, url, null, uriParams).getStatusCode();
    }
    
    public String executePost(String url, String body, Object... uriParams) {
        return sendRESTCall(HttpMethod.POST, url, body, uriParams).getBody();
    }
    
    public HttpStatus executePut(String url, String body, Object... uriParams) {
        return sendRESTCall(HttpMethod.PUT, url, body, uriParams).getStatusCode();
    }
    
    public void setSearchUrl(String esUrl) {
        this.esUrl = esUrl;
        this.bulkUri = esUrl + "/_bulk";
        this.mGetUri = esUrl + "/_mget";
        this.indexUri = esUrl + "/{index}";
        this.indexTypeUri = esUrl + "/{index}/{type}";
        this.mUpdateUri = indexTypeUri + "/{id}/_update";
    }
    
    public String getBaseUrl() {
        return esUrl;
    }

    public String getBulkUri() {
        return bulkUri;
    }

    public String getMGetUri() {
        return mGetUri;
    }

    public String getUpdateUri() {
        return mUpdateUri;
    }

    public String getIndexUri() {
        return indexUri;
    }

    public String getIndexTypeUri() {
        return indexTypeUri;
    }
    
    public void setSearchUsername(String esUsername) {
        this.esUsername = esUsername;
    }
    
    public void setSearchPassword(String esPassword) {
        this.esPassword = esPassword;
    }
    
    public void setSearchTemplate(RestOperations searchTemplate) {
        this.searchTemplate = searchTemplate;
    }
}
