package org.slc.sli.test.edfi.entities.relations;

public class SectionMeta {
    public final String id;
    public final String schoolId;
    public final String courseId;
    public final String sessionId;

    public final String simpleId;

    public SectionMeta(String id, SchoolMeta schoolMeta, CourseMeta courseMeta, SessionMeta sessionMeta) {

        String schoolIdNoAlpha = schoolMeta.id.replaceAll("[a-z]", "");
        String sessionIdNoAlpha = courseMeta.simpleId.replaceAll("[a-z]", "");
        String courseIdNoAlpha = sessionMeta.simpleId.replaceAll("[a-z]", "");

        this.id = schoolIdNoAlpha + "-" + sessionIdNoAlpha + "-" + courseIdNoAlpha + "-" + id;
        this.schoolId = schoolMeta.id;
        this.courseId = courseMeta.id;
        this.sessionId = sessionMeta.id;

        this.simpleId = id;
    }

    @Override
    public String toString() {
        return "SectionMeta [id=" + id + ", schoolId=" + schoolId + ", courseId=" + courseId + ", sessionId="
                + sessionId + "]";
    }

}
