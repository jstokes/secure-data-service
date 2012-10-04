package org.slc.sli.search.process.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slc.sli.search.config.IndexEntityConfigStore;
import org.slc.sli.search.util.MockMongoTemplateFactory;

/**
 * current student.json for mock data has 192 records
 * 
 * @author tosako
 * 
 */
public class ExtractorImplTest {

    private static final String INBOX = "inbox-test";
    private IndexEntityConfigStore indexEntityConfigStore;
    private ExtractorImpl extractor = new ExtractorImpl();

    @Before
    public void init() throws IOException {
        (new File(INBOX)).mkdirs();
        deleteFolder(INBOX);
        indexEntityConfigStore = new IndexEntityConfigStore("index-config-test.json");
        extractor.setMongoTemplate(MockMongoTemplateFactory.create());
        extractor.setIndexEntityConfigStore(indexEntityConfigStore);
        extractor.setInboxDir(INBOX);
    }
    
    @After
    public void destroy() {
        deleteFolder(INBOX);
    }

    @Test
    public void testFileCounts() throws Exception {
        // set max lines per file is 10
        extractor.setMaxLinePerFile(10);
        extractor.extractCollection("student", indexEntityConfigStore.getFields("student"));

        File[] files = listFiles(INBOX);
        Assert.assertEquals(20, files.length);
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

}
