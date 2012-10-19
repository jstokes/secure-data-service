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
package org.slc.sli.search.process;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slc.sli.search.config.IndexConfigStore;
import org.slc.sli.search.entity.IndexEntity.Action;
import org.slc.sli.search.process.impl.ExtractorImpl;
import org.slc.sli.search.process.impl.ExtractorImpl.Tenant;
import org.slc.sli.search.transform.IndexEntityConverter;
import org.slc.sli.search.util.Constants;
import org.slc.sli.search.util.MockDBCursorFactory;

import com.mongodb.DBCursor;

/**
 * current student.json for mock data has 192 records
 * 
 * @author tosako
 * 
 */
public class ExtractorImplTest {

    private IndexConfigStore indexConfigStore;
    private IndexEntityConverter indexEntityConverter;
    
    private class MockExtractor extends ExtractorImpl {
        final HashSet<Action> actions = new HashSet<Action>();
        @Override
        protected DBCursor getDBCursor(String collectionName, List<String> fields) {
            // get cursor from static file
            return MockDBCursorFactory.create(collectionName);
        } 
        
        @Override
        public List<Tenant> getTenants() {
            return Arrays.asList(new Tenant[]{new Tenant("test", "test")});
        }
        
        @Override
        protected void finishProcessing(String index, File outFile, Action action, List<File> producedFiles) {
            if (outFile != null) {
                producedFiles.add(outFile);
            }
            actions.add(action);
        }
        
        public HashSet<Action> getActions() {
            return actions;
        }
        
        public void reset() {
            actions.clear();
        }
    };
    
    private final MockExtractor extractor = new MockExtractor();

    @Before
    public void init() throws IOException {
        extractor.init();
        indexConfigStore = new IndexConfigStore("index-config-test.json");
        indexEntityConverter = new IndexEntityConverter();
        indexEntityConverter.setIndexConfigStore(indexConfigStore);
        
        extractor.setIndexConfigStore(indexConfigStore);
        extractor.reset();
    }
    
    @After
    public void destroy() {
        extractor.destroy();
    }
/**
 * Test to count number of files and lines in each file
 * @throws Exception
 */
    @Test
    public void testFileCounts() throws Exception {
        // set max lines per file is 10
        extractor.setMaxLinePerFile(10);
        List<File> files = extractor.extractCollection(indexConfigStore.getConfig("student"), Action.INDEX, new Tenant("test", "test"));

        Assert.assertEquals(20, files.size());
        int totalLines=0;
        for (File file : files) {
            totalLines+=getNumberOfLine(file);
        }
        Assert.assertEquals(191, totalLines);
    }

    private void deleteFolder(String folder) {
        File[] files = listFiles(folder);
        for (File file : files) {
            file.delete();
        }
    }

    private File[] listFiles(String folder) {
        return (new File(folder)).listFiles();
    }

    private int getNumberOfLine(File file) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(file));
        int lines = 0;
        while (br.readLine() != null)
            lines++;
        return lines;
    }
    
    
    @Test
    public void testAction() throws Exception {
        extractor.execute(Action.UPDATE);
        HashSet<Action> actions = extractor.getActions();
        Assert.assertEquals(1, actions.size());
        Assert.assertEquals(Action.UPDATE, actions.iterator().next()); 
    }

}