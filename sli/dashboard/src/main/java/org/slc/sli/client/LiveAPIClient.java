package org.slc.sli.client;

import java.util.List;

import org.slc.sli.entity.Assessment;
import org.slc.sli.entity.CustomData;
import org.slc.sli.entity.School;
import org.slc.sli.entity.Student;
import org.slc.sli.entity.assessmentmetadata.AssessmentFamilyMetaData;


/**
 * 
 * TODO: Write Javadoc
 *
 */
public class LiveAPIClient implements APIClient {

    @Override
    public School[] getSchools(String token) {
        System.err.println("Not implemented");
        return null;
    }
    @Override
    public Student[] getStudents(final String token, List<String> studentIds) {
        System.err.println("Not implemented");
        return null;
    }
    @Override
    public Assessment[] getAssessments(final String token, List<String> studentIds) {
        System.err.println("Not implemented");
        return null;
    }
    @Override
    public CustomData[] getCustomData(final String token, String key) {
        System.err.println("Not implemented");
        return null;
    }
    @Override
    public void saveCustomData(CustomData[] src, String token, String key) {
        System.err.println("Not implemented");
    }
    @Override
    public AssessmentFamilyMetaData[] getAssessmentMetaData(final String token) {
        System.err.println("Not implemented");
        return null;
    }
}
