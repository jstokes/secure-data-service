package org.slc.sli.search.util;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.FileUtils;

public class AppLock {
    
    public AppLock(final String lockFile) throws IOException {
        File f = new File(lockFile);
        if (f.exists()) {
            throw new IllegalStateException("Another instance of the application is running. " +
                    "Please delete lock file " + f.getAbsolutePath() + " if not the case");
        }
        FileUtils.writeStringToFile(f, String.format("File created at %1$tm/%1$te/%1$tY at %1$tH:%1$tM:%1$tS%n", new Date()));
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                if (new File(lockFile).delete()) {
                    throw new SearchIndexerException("Unable to delete lock file " + lockFile);
                }
            }
        });
    }   
}
