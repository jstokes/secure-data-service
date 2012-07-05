package org.slc.sli.shtick;

import org.codehaus.jackson.map.ObjectMapper;

/**
 * @author jstokes
 */
public final class JsonLevel1Client extends AbstractLevel1Client implements Level1Client {

    public JsonLevel1Client() {
        super(new JaxRSLevel0Client(), new ObjectMapper());
    }

    @Override
    protected String getMediaType() {
        return "application/json";
    }
}
