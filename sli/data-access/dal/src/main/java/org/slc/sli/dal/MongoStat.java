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

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 * User: pghosh
 * Date: 8/24/12
 * Time: 4:27 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
@Scope(value = "thread")
public class MongoStat {


    private int dbHitCount;

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
}
