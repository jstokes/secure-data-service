package org.slc.sli.ingestion.model;

import org.springframework.data.mongodb.core.index.Indexed;

public final class RecordHash {

    public String _id;
    public String timestamp;

    @Indexed
    public String tenantId;

    public RecordHash() {
        this._id = "";
        this.timestamp = "";
        this.tenantId = "";
    }

}
