package org.slc.sli.ingestion.handler;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Set;

import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.IOUtils;
import org.milyn.Smooks;
import org.milyn.SmooksException;
import org.milyn.container.ExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import org.slc.sli.ingestion.FileProcessStatus;
import org.slc.sli.ingestion.NeutralRecordFileWriter;
import org.slc.sli.ingestion.landingzone.IngestionFileEntry;
import org.slc.sli.ingestion.landingzone.LandingZone;
import org.slc.sli.ingestion.landingzone.LocalFileSystemLandingZone;
import org.slc.sli.ingestion.smooks.NonSilentErrorReport;
import org.slc.sli.ingestion.smooks.SliSmooksFactory;
import org.slc.sli.ingestion.validation.ErrorReport;

/**
 * smooks handler for edfi files
 *
 * @author dduran
 *
 */
@Component
public class SmooksFileHandler extends AbstractIngestionHandler<IngestionFileEntry, IngestionFileEntry> implements MessageSourceAware {

    private static final Logger LOG = LoggerFactory.getLogger(SmooksFileHandler.class);

    @Autowired
    private SliSmooksFactory sliSmooksFactory;
    private Set<String> filteredAttributes;
    private MessageSource messageSource;

    @Override
    protected IngestionFileEntry doHandling(IngestionFileEntry fileEntry, ErrorReport errorReport,
            FileProcessStatus fileProcessStatus) {
        try {

            generateNeutralRecord(fileEntry, errorReport, fileProcessStatus);

        } catch (IOException e) {
            LOG.error("IOException", e.getLocalizedMessage());
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
        LandingZone landingZone = new LocalFileSystemLandingZone(new File(
                ingestionFileEntry.getTopLevelLandingZonePath()));
        String lzDirectory = "";
        if (landingZone != null && landingZone.getLZId() != null) {
            lzDirectory = landingZone.getLZId();
        } else {
            lzDirectory = ingestionFileEntry.getFile().getParent();
        }
        File neutralRecordOutFile = createTempFile(lzDirectory);

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

            ExecutionContext ctx = smooks.createExecutionContext();
            ctx.setEventListener(new NonSilentErrorReport(filteredAttributes, messageSource, errorReport));

            smooks.filterSource(ctx, new StreamSource(inputStream));
        } catch (SmooksException se) {
            LOG.error("smooks exception encountered converting " + ingestionFileEntry.getFile().getName() + " to "
                    + neutralRecordOutFile.getName() + "\n", se.getLocalizedMessage());
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

    private static File createTempFile(String lzDirectory) throws IOException {
        File landingZone = new File(lzDirectory);
        File outputFile = landingZone.exists() ? File.createTempFile("neutralRecord_", ".tmp", landingZone) : File
                .createTempFile("neutralRecord_", ".tmp");
        return outputFile;
    }

    public void setFilteredAttributes(Set<String> filteredAttributes) {
        this.filteredAttributes = filteredAttributes;
    }

    @Override
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

}
