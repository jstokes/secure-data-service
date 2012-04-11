package org.slc.sli.ingestion.landingzone;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

/**
 *
 */
public class LocalFileSystemLandingZone implements LandingZone, Serializable {

    private static final long serialVersionUID = 7441095255253233611L;

    protected File directory;

    /**
     * Return the absolute local path
     */
    public String getLZId() {
        return getDirectory().getAbsolutePath();
    }
    
    /**
     * @return the directory
     */
    public File getDirectory() {
        return directory;
    }

    /**
     * @param directory
     *            the directory to set
     */
    public void setDirectory(File directory) {
        this.directory = directory;
    }

    /**
     * @return File object for the given fileName
     */
    public File getFile(String fileName) {
        File f = FileUtils.getFile(this.directory, fileName);
        if (f.exists()) {
            return f;
        } else {
            return null;
        }
    }

    /**
     * @return File object for the newly-created file
     */
    public File createFile(String fileName) throws IOException {
        File f = FileUtils.getFile(this.directory, fileName);
        if (f.exists()) {
            throw new IOException("file already exists: " + fileName);
        }
        f.createNewFile();
        return f;
    }

    /**
     * load file to local landing zone
     */
    public void loadFile(File file) throws IOException {

        File dest = FileUtils.getFile(this.directory, file.getName());
        // will overwrite if destination file exists
        FileUtils.copyFile(file, dest);
    }

    /**
     * Returns a java.io.File for a log file to be used to report BatchJob
     * status/progress.  The file will be created if it does not yet exist.
     *
     * @return File object
     */
    public File getLogFile(String jobId) throws IOException {
        String fileName = "job-" + jobId + ".log";
        File f = FileUtils.getFile(this.directory, fileName);
        if (!f.exists()) {
            f.createNewFile();
        }
        return f;
    }

    /**
     * @return md5Hex string for the given File object
     */
    public String getMd5Hex(File file) throws IOException {
        FileInputStream s = FileUtils.openInputStream(file);
        try {
            return DigestUtils.md5Hex(s);
        } finally {
            IOUtils.closeQuietly(s);
        }
    }

}
