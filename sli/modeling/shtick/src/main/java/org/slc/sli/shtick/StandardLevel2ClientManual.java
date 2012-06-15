package org.slc.sli.shtick;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * @author jstokes
 */
public final class StandardLevel2ClientManual implements Level2ClientManual {

    private final String baseUrl;
    private final Level1Client client;

    protected StandardLevel2ClientManual(final String baseUrl, final Level1Client client) {
        if (baseUrl == null) {
            throw new NullPointerException("baseUrl");
        }

        this.client = client;
        this.baseUrl = baseUrl;
    }

    public StandardLevel2ClientManual(final String baseUrl) {
        this(baseUrl, new JsonLevel1Client());
    }

    @Override
    public List<RestEntity> getStudentsByStudentId(final String token, final List<String> studentIds,
            Map<String, Object> queryArgs) throws IOException, RestException {
        if (token == null) {
            throw new NullPointerException("token");
        }
        if (studentIds == null) {
            throw new NullPointerException("studentIds");
        }

        try {
            final URL url = URLBuilder.baseUrl(baseUrl).entityType("student").ids(studentIds).query(queryArgs).build();
            return client.getRequest(token, url);
        } catch (URISyntaxException e) {
            throw new AssertionError(e);
        }
    }

    @Override
    public List<RestEntity> getStudents(final String token, Map<String, Object> queryArgs) throws IOException,
            RestException {
        if (token == null) {
            throw new NullPointerException("token");
        }

        try {
            final URL url = URLBuilder.baseUrl(baseUrl).entityType("student").query(queryArgs).build();
            return client.getRequest(token, url);
        } catch (URISyntaxException e) {
            throw new AssertionError(e);
        }
    }

    @Override
    public void deleteStudentById(final String token, final String studentId) throws IOException,
            RestException {
        if (token == null) {
            throw new NullPointerException("token");
        }
        try {
            final URL url = URLBuilder.baseUrl(baseUrl).entityType("student").id(studentId).build();
            client.deleteRequest(token, url);
        } catch (final URISyntaxException e) {
            throw new AssertionError(e);
        }
    }

    @Override
    public String postStudent(final String token, final RestEntity entity) throws IOException, RestException {
        if (token == null) {
            throw new NullPointerException("token");
        }
        if (entity == null) {
            throw new NullPointerException("entity");
        }
        try {
            final URL url = URLBuilder.baseUrl(baseUrl).entityType("student").build();
            final URL studentURL = client.postRequest(token, entity, url);
            return URLHelper.stripId(studentURL);
        } catch (final URISyntaxException e) {
            throw new AssertionError(e);
        }
    }

    @Override
    public void putStudent(final String token, final RestEntity entity) throws IOException, RestException {
        if (token == null) {
            throw new NullPointerException("token");
        }
        if (entity == null) {
            throw new NullPointerException("entity");
        }
        try {
            final URL url = URLBuilder.baseUrl(baseUrl).entityType("student").id(entity.getId()).build();
            client.putRequest(token, entity, url);
        } catch (final URISyntaxException e) {
            throw new AssertionError(e);
        }
    }

}
