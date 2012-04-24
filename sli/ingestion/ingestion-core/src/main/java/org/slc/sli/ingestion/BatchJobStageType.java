package org.slc.sli.ingestion;

/**
 * Types of stages
 *
 * @author bsuzuki
 *
 */
public enum BatchJobStageType {

    ZIP_FILE_PROCESSOR("ZipFileProcessor"),
    CONTROL_FILE_PREPROCESSOR("ControlFilePreProcessor"),
    CONTROL_FILE_PROCESSOR("ControlFileProcessor"),
    PURGE_PROCESSOR("PurgeProcessor"),
    XML_FILE_PROCESSOR("XmlFileProcessor"),
    EDFI_PROCESSOR("EdFiProcessor"),
    TRANSFORMATION_PROCESSOR("TransformationProcessor"),
    PERSISTENCE_PROCESSOR("PersistenceProcessor"),
    JOB_REPORTING_PROCESSOR("JobReportingProcessor");

    private final String name;

    BatchJobStageType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
