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


package org.slc.sli.ingestion.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

/**
 * Provides a service bean for interacting with the master / worker queue
 *
 * @author smelody
 *
 */
@Component
public interface QueueService {

    public void postItem(Map<String, Object> item);

    public Map<String, Object> reserveItem();

    /**
     * Fetch the work items that the worker with the given ID has reserved.
     *
     * @param id
     * @return
     */
    public List<Map<String, Object>> fetchItems(String workerId);

    public void purgeExpiredItems();

    /** Returns the number of items in the queue, regardless of state */
    public long count();

    /**
     * Removes all entries in the queue.
     *
     * @return
     */
    public int clear();

}
