package org.slc.sli.ingestion.tool;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import org.slc.sli.ingestion.BatchJob;
import org.slc.sli.ingestion.FaultsReport;
import org.slc.sli.ingestion.landingzone.ZipFileUtil;
import org.slc.sli.ingestion.landingzone.validation.ZipFileValidator;

/**
 *
 * @author npandey
 *
 */

public class ZipValidation implements MessageSourceAware {

    @Autowired
    private ZipFileValidator validator;

    private MessageSource messageSource;

    public File validate(File zipFile, BatchJob job) {

        File ctlFile = null;
        try {
            FaultsReport fr = job.getFaultsReport();

            if (validator.isValid(zipFile, fr)) {
                File dir = ZipFileUtil.extract(zipFile);
                ctlFile = ZipFileUtil.findCtlFile(dir);
                }
        } catch (IOException ex) {
            fr.error(messageSource.getMessage("SL_ERR_MSG4", new Object[] { zipFile.getName() }, null), this);
        } catch (Exception exception) {
            fr.error(ex.getMessage(), this);
        } finally {
            return ctlFile;
        }
    }

    public ZipFileValidator getValidator() {
        return validator;
    }

    public void setValidator(ZipFileValidator validator) {
        this.validator = validator;
    }

    @Override
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

}
