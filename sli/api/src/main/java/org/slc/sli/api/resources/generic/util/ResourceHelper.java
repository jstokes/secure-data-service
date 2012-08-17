package org.slc.sli.api.resources.generic.util;

import org.slc.sli.api.service.query.UriInfoToApiQueryConverter;

import javax.ws.rs.core.UriInfo;
import java.util.List;

/**
 * @author jstokes
 */
public interface ResourceHelper {
    public String getResourceName(UriInfo uriInfo, ResourceTemplate template);
    public List<String> getIds(UriInfo uriInfo,ResourceTemplate template);
    public String getBaseName(UriInfo uriInfo, ResourceTemplate template);
    public String getResourcePath(UriInfo uriInfo, ResourceTemplate template);
}
