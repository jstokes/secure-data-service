package org.slc.sli.ingestion.model;

/**
 * Model for ingestion file entries
 *
 * @author dduran
 *
 */
public final class ResourceEntry {

    private String resourceId;

    private String resourceName;

    private String resourceFormat;

    private String resourceType;

    private String checksum;

    private int recordCount;

    private int errorCount;

    // mongoTemplate requires this constructor.
    public ResourceEntry() {
    }

    public void update(String fileFormat, String fileType, String checksum, int recordCount, int errorCount) {
        if (fileFormat != null)
            this.resourceFormat = fileFormat;
        if (fileType != null)
            this.resourceType = fileType;
        if (checksum != null)
            this.checksum = checksum;
        this.recordCount = recordCount;
        this.errorCount = errorCount;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getResourceFormat() {
        return resourceFormat;
    }

    public void setResourceFormat(String resourceFormat) {
        this.resourceFormat = resourceFormat;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    public int getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(int recordCount) {
        this.recordCount = recordCount;
    }

    public int getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(int errorCount) {
        this.errorCount = errorCount;
    }
}
