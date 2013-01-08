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

package org.slc.sli.dal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 * User: pghosh
 * Date: 8/24/12
 * Time: 4:27 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
@Scope(value = "thread", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class MongoStat {


    private int dbHitCount;
    private List<List<Object>> stats = new ArrayList<List<Object>>(10000); 
    private String requestId; 

    public MongoStat(int dbHitCount) {
        this.dbHitCount = dbHitCount;
    }

    public MongoStat() {
        this.dbHitCount = 0;
    }

    public void setDbHitCount(int dbHitCount) {
        this.dbHitCount = dbHitCount;
    }
    public int getDbHitCount() {
        return dbHitCount;
    }

    public void clear() {
        dbHitCount = 0;
    }
    public void incrementHitCount() {
        dbHitCount++;
    }
    
    public void startRequest(String reqestId) { 
        dbHitCount = 0; 
        requestId = requestId;
        stats = new ArrayList<List<Object>>(1000);  
    }
    
    public void addEvent(String eventId, Long timeStamp) {
        stats.add(Arrays.asList((Object) "e", Thread.currentThread().getId() + ": " +  eventId, timeStamp)); 
    }
    
    public void addMetric(String metricId, Long metric) {
        stats.add(Arrays.asList((Object) "m", Thread.currentThread().getId() + ": " +  metricId, metric)); 
    }
    
    public List<List<Object > > getStats() {
        return new ArrayList<List<Object>>(stats); 
    }
    
    public String getRequestId() { 
        return requestId; 
    }
}
