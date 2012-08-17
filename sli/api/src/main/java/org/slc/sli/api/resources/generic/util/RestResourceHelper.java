package org.slc.sli.api.resources.generic.util;

import org.springframework.stereotype.Component;
import org.springframework.web.util.UriTemplate;

import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author jstokes
 */
@Component
public class RestResourceHelper implements ResourceHelper {
    private static final String RESOURCE_KEY = "resource";
    private static final String BASE_KEY = "base";
    private static final String VERSION_KEY = "version";
    private static final String ASSOCIATION_KEY = "association";
    private static final String SEP = "/";

    @Override
    public String getResourceName(final UriInfo uriInfo, final ResourceTemplate template) {
        final Map<String, String> matchList = getMatchList(uriInfo, template);
        return matchList.get(VERSION_KEY) + SEP + matchList.get(RESOURCE_KEY);
    }

    @Override
    public String getBaseName(final UriInfo uriInfo, final ResourceTemplate template) {
        final Map<String, String> matchList = getMatchList(uriInfo, template);
        return matchList.get(BASE_KEY);
    }

    @Override
    public String getResourcePath(final UriInfo uriInfo, final ResourceTemplate template) {
        final Map<String, String> matchList = getMatchList(uriInfo, template);
        final String path = matchList.get(VERSION_KEY) + SEP + matchList.get(RESOURCE_KEY);

        switch (template) {
            case ONE_PART:
                return path;
            case TWO_PART:
                return getTwoPartPath(path);
            case THREE_PART:
                return getThreePartPath(matchList);
            case FOUR_PART:
                return getFourPartPath(matchList);
            case CUSTOM:
                // TODO
                return "";
            default:
                throw new AssertionError("Non-valid template");
        }
    }

    @Override
    public String getAssociationName(final UriInfo uriInfo, final ResourceTemplate template) {
        final Map<String, String> matchList = getMatchList(uriInfo, template);
        return matchList.get(ASSOCIATION_KEY);
    }

    private Map<String, String> getMatchList(final UriInfo uriInfo, final ResourceTemplate template) {
        final UriTemplate uriTemplate = new UriTemplate(template.getTemplate());
        return uriTemplate.match(uriInfo.getRequestUri().toString());
    }

    private String getFourPartPath(final Map<String, String> matchList) {
        return matchList.get(VERSION_KEY) + SEP + matchList.get(BASE_KEY) + SEP + "{id}"
                + SEP + matchList.get(ASSOCIATION_KEY) + SEP + matchList.get(RESOURCE_KEY);
    }

    private String getTwoPartPath(final String path) {
        return path + SEP + "{id}";
    }

    private String getThreePartPath(final Map<String, String> matchList) {
        return matchList.get(VERSION_KEY) + SEP + matchList.get(BASE_KEY)
                + SEP + "{id}" + SEP + matchList.get(RESOURCE_KEY);
    }
}
