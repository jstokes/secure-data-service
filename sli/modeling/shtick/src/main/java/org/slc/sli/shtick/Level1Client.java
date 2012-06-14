package org.slc.sli.shtick;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import org.slc.sli.api.client.Entity;

/**
 * @author jstokes
 */
public interface Level1Client {
    // SliDataStore = http code exception
    List<Entity> getRequest(final String token, final URL url) throws URISyntaxException, IOException, RestException;

    void deleteRequest(final String token, final URL url) throws URISyntaxException, IOException, RestException;

    URL postRequest(final String token, final String data, final URL url) throws URISyntaxException, IOException, RestException;

    void putRequest(final String token, final String data, final URL url) throws URISyntaxException, IOException, RestException;

}
