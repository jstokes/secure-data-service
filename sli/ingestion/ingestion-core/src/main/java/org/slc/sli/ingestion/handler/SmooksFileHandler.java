package org.slc.sli.ingestion.handler;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Hashtable;

import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.IOUtils;
import org.milyn.Smooks;
import org.milyn.SmooksException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import org.slc.sli.ingestion.FileProcessStatus;
import org.slc.sli.ingestion.NeutralRecordFileWriter;
import org.slc.sli.ingestion.landingzone.IngestionFileEntry;
import org.slc.sli.ingestion.smooks.SliSmooksFactory;
import org.slc.sli.ingestion.validation.ErrorReport;

/**
 * smooks handler for edfi files
 *
 * @author dduran
 *
 */
public class SmooksFileHandler extends AbstractIngestionHandler<IngestionFileEntry, IngestionFileEntry> {

    private static final Logger LOG = LoggerFactory.getLogger(SmooksFileHandler.class);

    private SliSmooksFactory sliSmooksFactory;

    private String lzDirectory;

    @Override
    IngestionFileEntry doHandling(IngestionFileEntry fileEntry, ErrorReport errorReport,
            FileProcessStatus fileProcessStatus) {
        try {

            generateNeutralRecord(fileEntry, errorReport, fileProcessStatus);

        } catch (IOException e) {
            LOG.error("IOException", e);
            errorReport.fatal("Could not instantiate smooks, unable to read configuration file.",
                    SmooksFileHandler.class);
        } catch (SAXException e) {
            errorReport.fatal("Could not instantiate smooks, problem parsing configuration file.",
                    SmooksFileHandler.class);
        }

        return fileEntry;
    }

    void generateNeutralRecord(IngestionFileEntry ingestionFileEntry, ErrorReport errorReport,
            FileProcessStatus fileProcessStatus) throws IOException, SAXException {

        File neutralRecordOutFile = createTempFile();

        fileProcessStatus.setOutputFilePath(neutralRecordOutFile.getAbsolutePath());
        fileProcessStatus.setOutputFileName(neutralRecordOutFile.getName());

        NeutralRecordFileWriter nrFileWriter = new NeutralRecordFileWriter(neutralRecordOutFile);

        // set the IngestionFileEntry NeutralRecord file we just wrote
        ingestionFileEntry.setNeutralRecordFile(neutralRecordOutFile);

        // create instance of Smooks (with visitors already added)
        Smooks smooks = sliSmooksFactory.createInstance(ingestionFileEntry, nrFileWriter, errorReport);

        InputStream inputStream = new BufferedInputStream(new FileInputStream(ingestionFileEntry.getFile()));
        try {
            // filter fileEntry inputStream, converting into NeutralRecord entries as we go
            smooks.filterSource(new StreamSource(inputStream));
        } catch (SmooksException se) {
            LOG.error("smooks exception encountered converting " + ingestionFileEntry.getFile().getName() + " to "
                    + neutralRecordOutFile.getName() + ": " + se.getMessage() + "\n"
                    + Arrays.toString(se.getStackTrace()));
            errorReport.error("SmooksException encountered while filtering input.", SmooksFileHandler.class);
        } finally {
            IOUtils.closeQuietly(inputStream);

            long count = 0L;
            Hashtable<String, Long> counts = nrFileWriter.getNRCount();
            for (String type : counts.keySet()) {
                count += counts.get(type);
            }

            fileProcessStatus.setTotalRecordCount(count);

            nrFileWriter.close();
        }
    }

    private File createTempFile() throws IOException {
        File landingZone = new File(lzDirectory);
        File outputFile = landingZone.exists() ? File.createTempFile("camel_", ".tmp", landingZone) : File
                .createTempFile("camel_", ".tmp");
        return outputFile;
    }

    public void setSliSmooksFactory(SliSmooksFactory sliSmooksFactory) {
        this.sliSmooksFactory = sliSmooksFactory;
    }

    /**
     * @return the lzDirectory
     */
    public String getLzDirectory() {
        return lzDirectory;
    }

    /**
     * @param lzDirectory
     *            the lzDirectory to set
     */
    public void setLzDirectory(String lzDirectory) {
        this.lzDirectory = lzDirectory;
    }

}
