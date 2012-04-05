package org.slc.sli.test.edfi.entities.relations;

public class SchoolMeta {

    public final String id;
    public final String leaId;

    public final String simpleId;

    public SchoolMeta(String id, LeaMeta leaMeta) {
        this.id = leaMeta.id + "-" + id;
        this.leaId = leaMeta.id;

        this.simpleId = id;
    }

    @Override
    public String toString() {
        return "SchoolMeta [id=" + id + ", leaId=" + leaId + "]";
    }
}
