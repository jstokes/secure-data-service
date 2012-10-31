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
package org.slc.sli.search;

import java.io.File;
import java.io.IOException;

import org.slc.sli.search.util.AppLock;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Launcher {

    private static final String DEFAULT_LOCK_DIR = "data";
    private static final String LOCK_FILE = "indexer.lock";
    
    public static final void main(String[] args) throws IOException {
        
        new AppLock(getLockLocation());
        new ClassPathXmlApplicationContext("application-context.xml");
    }
    
    private static String getLockLocation() {
        return System.getProperties().getProperty("lock.dir", DEFAULT_LOCK_DIR) + File.separator + LOCK_FILE;
    }
}
