package org.slc.sli.shtick;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
import org.codehaus.jackson.type.TypeReference;
import org.slc.sli.api.client.Entity;
import org.slc.sli.api.client.impl.GenericEntity;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jstokes
 */
public abstract class AbstractLevel1Client implements Level1Client {

    private static final String LOCATION_HEADER = "location";

    final Level0Client client;
    final ObjectMapper mapper;

    protected AbstractLevel1Client(final Level0Client client, final ObjectMapper mapper) {
        this.client = client;
        this.mapper = mapper;
    }

    @Override
    public List<Entity> getRequest(String token, URL url) throws URISyntaxException, IOException, RestException {
        if (token == null) {
            throw new NullPointerException("token");
        }
        if (url == null) {
            throw new NullPointerException("url");
        }

        final RestResponse response;

        response = client.getRequest(token, url, getMediaType());
        return deserialize(response);
    }

    @Override
    public void deleteRequest(String token, URL url) throws URISyntaxException, IOException, RestException {
        if (token == null) {
            throw new NullPointerException("token");
        }
        if (url == null) {
            throw new NullPointerException("url");
        }

        client.deleteRequest(token, url, getMediaType());
    }

    @Override
    public URL postRequest(String token, final String data, URL url) throws URISyntaxException, IOException,
            RestException {
        if (token == null) {
            throw new NullPointerException("token");
        }
        if (url == null) {
            throw new NullPointerException("url");
        }
        if (data == null) {
            throw new NullPointerException("data");
        }

        final RestResponse response;
        response = client.createRequest(token, data, url, getMediaType());

//        return new URL(response.getHeaders().getHeader(LOCATION_HEADER));
        return null;
    }

    @Override
    public void putRequest(String token, final String data, URL url) throws URISyntaxException, IOException,
            RestException {
        if (token == null) {
            throw new NullPointerException("token");
        }
        if (url == null) {
            throw new NullPointerException("url");
        }
        if (data == null) {
            throw new NullPointerException("data");
        }

        client.updateRequest(token, data, url, getMediaType());

    }

    private List<Entity> deserialize(final RestResponse response) throws IOException {
        try {
            final JsonNode element = mapper.readValue(response.getBody(), JsonNode.class);
            if (element instanceof ArrayNode) {
                return mapper.readValue(element, new TypeReference<List<GenericEntity>>() {
                });
            } else if (element instanceof ObjectNode) {
                List<Entity> list = new ArrayList<Entity>();
                list.add(mapper.readValue(element, GenericEntity.class));
                return list;
            }
        } catch (final JsonParseException e) {
            throw new RuntimeException(e);
        }
        throw new AssertionError();
    }

    protected abstract String getMediaType();
}
