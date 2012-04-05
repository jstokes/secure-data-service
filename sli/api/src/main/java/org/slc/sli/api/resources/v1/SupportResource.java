package org.slc.sli.api.resources.v1;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;

/**
 * Support Resource
 * 
 * This resource provides the basic contact information (E-Mail) of who to contact for
 * support issues.
 *
 *
 */
@Path(PathConstants.V1 + "/" + "system/support")
@Component
@Scope("request")
@Produces({ MediaType.APPLICATION_JSON, HypermediaType.VENDOR_SLC_JSON })
public class SupportResource {
    
    @Value("${sli.support.email}")
    private String email;
    
    @GET
    @Path("email")
    public Object getEmail() {
        Map<String, String> emailMap = new HashMap<String, String>();
        emailMap.put("email", email);
        return emailMap;
    }
    
}
