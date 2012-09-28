package org.slc.sli.search.process;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.apache.lucene.util.IOUtils;
import org.slc.sli.search.util.IndexEntityConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Loader implements FileAlterationListener {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    private static final String DEFAULT_DROP_OFF_DIR = "inbox";
    private static final int DEFAULT_INTERVAL_SEC = 3;
    private static final int DEFAULT_EXECUTOR_THREADS = 5;
    
    @Autowired
    private Indexer indexer;
    
    @Autowired
    IndexEntityConverter indexEntityConverter;
    
    @Autowired(required=false)
    private String inboxDir = DEFAULT_DROP_OFF_DIR;
    
    @Autowired(required=false)
    private long intervalSec = DEFAULT_INTERVAL_SEC;
    
    private FileAlterationMonitor monitor;
    
    private ExecutorService executor;
    private int executorThreads = DEFAULT_EXECUTOR_THREADS;
    
    public Loader() {
        monitor = new FileAlterationMonitor(TimeUnit.SECONDS.toMillis(intervalSec));
    }
    
    public void init() throws Exception {
        
        // create thread pool to process files
        executor = Executors.newFixedThreadPool(executorThreads);
        File inbox = new File(inboxDir);
        FileAlterationObserver observer = new FileAlterationObserver(inbox);
        monitor.addObserver(observer);
        observer.addListener(this);
        // watch directory for files
        for (File f : inbox.listFiles())
        {
            processFile(f);
        }
        monitor.start();
    }
    
    public void destroy()  throws Exception{
        monitor.stop();
        executor.shutdown();
    }
    
    /**
     * A worker to process an individual file
     *
     */
    private class LoaderWorker implements Runnable {
        
        File inFile;
        
        LoaderWorker(File inFile) {
            this.inFile = inFile;
        }
        
        public void run() {
            // read records from file
            BufferedReader br = null;
            String entity;
            try {
                br = new BufferedReader(new FileReader(inFile));
                while ((entity = br.readLine()) != null) {
                    indexer.index(indexEntityConverter.fromEntityJson(entity));
                }
                inFile.delete();
            } catch (Exception e) {
                logger.error("Error loading from file", e);
            }
            finally {
                try {
                    IOUtils.closeWhileHandlingException(br);
                    logger.info("Done processing file: " + inFile.getName());
                } catch (IOException e) {
                    logger.error("Error closing file", e);
                }
            }
        }
        
    }
    
    private void processFile(File inFile) {
        // TODO: make sure file is not being written to still
        logger.info("Processing file: " + inFile.getName());
        executor.execute(new LoaderWorker(inFile));
    }

    public void onDirectoryChange(File arg0) {
    }

    public void onDirectoryCreate(File arg0) {
    }

    public void onDirectoryDelete(File arg0) {
    }

    public void onFileChange(File arg0) {
    }

    public void onFileCreate(File inFile) {
        processFile(inFile);
    }

    public void onFileDelete(File arg0) {
    }

    public void onStart(FileAlterationObserver arg0) {
    }

    public void onStop(FileAlterationObserver arg0) {
    }
    
}