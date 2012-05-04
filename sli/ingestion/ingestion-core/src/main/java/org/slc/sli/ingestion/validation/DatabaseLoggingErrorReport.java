package org.slc.sli.ingestion.validation;

import java.io.Serializable;

import org.slc.sli.ingestion.BatchJobStageType;
import org.slc.sli.ingestion.FaultType;
import org.slc.sli.ingestion.model.Error;
import org.slc.sli.ingestion.model.da.BatchJobDAO;
import org.slc.sli.ingestion.util.BatchJobUtils;

/**
 *
 * @author bsuzuki
 *
 */
public class DatabaseLoggingErrorReport implements Serializable, ErrorReport {

    private static final long serialVersionUID = 2190485796418439710L;

    private final String batchJobId;
    private final BatchJobStageType stage;
    private final String resourceId;
    private final BatchJobDAO batchJobDAO;

    private volatile boolean hasErrors;

    public DatabaseLoggingErrorReport(String batchJobId, BatchJobStageType stageType, String resourceId,
            BatchJobDAO batchJobDAO) {
        this.batchJobId = batchJobId;
        this.stage = stageType;
        this.resourceId = resourceId;
        this.batchJobDAO = batchJobDAO;
    }

    @Override
    public void fatal(String message, Object sender) {
        error(message, sender);
    }

    @Override
    public void error(String message, Object sender) {

        String recordIdentifier = null;
        Error error = Error.createIngestionError(batchJobId, resourceId, (stage == null) ? null : stage.getName(),
                BatchJobUtils.getHostName(), BatchJobUtils.getHostAddress(), recordIdentifier,
                FaultType.TYPE_ERROR.getName(), FaultType.TYPE_ERROR.getName(), message);
        batchJobDAO.saveError(error);

        hasErrors = true;
    }

    @Override
    public void warning(String message, Object sender) {

        String recordIdentifier = null;
        Error error = Error.createIngestionError(batchJobId, resourceId, (stage == null) ? "" : stage.getName(),
                BatchJobUtils.getHostName(), BatchJobUtils.getHostAddress(), recordIdentifier,
                FaultType.TYPE_WARNING.getName(), FaultType.TYPE_WARNING.getName(), message);
        batchJobDAO.saveError(error);
    }

    @Override
    public boolean hasErrors() {
        return hasErrors;
    }

    public String getBatchJobId() {
        return batchJobId;
    }

    public BatchJobStageType getBatchJobStage() {
        return stage;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setHasErrors(boolean hasErrors) {
        this.hasErrors = hasErrors;
    }

}
