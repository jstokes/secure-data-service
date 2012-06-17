package org.slc.sli.shtick;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author jstokes
 */
public interface Level2ClientManual {

    List<Entity> getStudentsByStudentId(String token, List<String> studentIds, Map<String, Object> queryArgs)
            throws IOException, StatusCodeException;

    List<Entity> getStudents(String token, Map<String, Object> queryArgs) throws IOException, StatusCodeException;

    void deleteStudentById(final String token, final String studentId) throws IOException,
            StatusCodeException;

    String postStudent(final String token, final Entity body) throws IOException, StatusCodeException;

    void putStudent(final String token, final Entity data) throws IOException, StatusCodeException;
}
