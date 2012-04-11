package org.slc.sli.test.edfi.entities.relations;

import java.util.ArrayList;
import java.util.List;

public final class StudentMeta {
    public final String id;
    public final List<String> schoolIds;
    public final List<String> sectionIds;

    public final List<String> assessmentIds;

    public final String simpleId;

    public StudentMeta(String id, SchoolMeta schoolMeta) {
        this.id = schoolMeta.id + "-" + id;

        this.schoolIds = new ArrayList<String>();
        this.schoolIds.add(schoolMeta.id);

        this.sectionIds = new ArrayList<String>();

        this.assessmentIds = new ArrayList<String>();

        this.simpleId = id;
    }

    @Override
    public String toString() {
        return "StudentMeta [id=" + id + ", schoolIds=" + schoolIds + ", sectionIds=" + sectionIds + ", assessmentIds="
                + assessmentIds + ", simpleId=" + simpleId + "]";
    }

}
