package org.slc.sli.api.resources.security;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import org.slc.sli.api.config.EntityDefinition;
import org.slc.sli.api.config.EntityDefinitionStore;
import org.slc.sli.api.representation.EntityBody;
import org.slc.sli.api.resources.Resource;
import org.slc.sli.api.security.oauth.TokenGenerator;
import org.slc.sli.api.service.EntityNotFoundException;
import org.slc.sli.api.service.EntityService;

/**
 * 
 * Implements the ClientDetailsService interface provided by the Spring OAuth
 * 2.0 implementation.
 * 
 * @author shalka
 */
@Component
@Scope("request")
@Path("/apps")
@Produces({ Resource.JSON_MEDIA_TYPE })
public class ApplicationResource {

    @Autowired
    private EntityDefinitionStore store;

    private EntityService service;

    private static final int CLIENT_ID_LENGTH = 10;
    private static final int CLIENT_SECRET_LENGTH = 48;
    public static final String CLIENT_ID = "client_id";
    public static final String CLIENT_SECRET = "client_secret";
    public static final String RESOURCE_NAME = "application"; 
    public static final String UUID = "uuid";
    private static final Logger   LOG        = LoggerFactory.getLogger(ApplicationResource.class);

    @PostConstruct
    public void init() {
        EntityDefinition def = store.lookupByResourceName(RESOURCE_NAME);
        this.service = def.getService();
    }

    @POST
    public Response createApplication(EntityBody newApp, @Context final UriInfo uriInfo) {
        String clientId = TokenGenerator.generateToken(CLIENT_ID_LENGTH);
        while (isDuplicateToken(clientId)) {
            clientId = TokenGenerator.generateToken(CLIENT_ID_LENGTH);
        }

        if (newApp.containsKey(CLIENT_SECRET) 
                || newApp.containsKey(CLIENT_ID) 
                || newApp.containsKey("id")) {
            EntityBody body = new EntityBody();
            body.put("message", "Auto-generated attribute (id|client_secret|client_id) specified in POST.  " 
            + "Remove attribute and try again.");
            return Response.status(Status.BAD_REQUEST).entity(body).build();
        }
        newApp.put(CLIENT_ID, clientId);
        
        String clientSecret = TokenGenerator.generateToken(CLIENT_SECRET_LENGTH);
        newApp.put(CLIENT_SECRET, clientSecret);
        service.create(newApp);

        String uri = uriToString(uriInfo) + "/" + clientId;
        return Response.status(Status.CREATED).header("Location", uri).build();
    }

    private boolean isDuplicateToken(String token) {
        return (service.list(0, 1, CLIENT_ID + "=" + token)).iterator().hasNext();
    }

    @GET
    public List<EntityBody> getApplications(@Context UriInfo info) {
        List<EntityBody> results = new ArrayList<EntityBody>();
        Iterable<String> realmList = service.list(0, 1000);
        for (String id : realmList) {
            EntityBody result = service.get(id);

            result.put("link", uriToString(info) + "/" + id);
            results.add(result);
        }
        return results;
    }
    
    private static String uriToString(UriInfo uri) {
        return uri.getBaseUri() + uri.getPath().replaceAll("/$", "");
    }

    /**
     * Looks up a specific application based on client ID, ie.
     * /api/rest/apps/<client_id>
     * 
     * @param clientId
     *            the client ID, not the "id"
     * @return the JSON data of the application, otherwise 404 if not found
     */
    @GET
    @Path("{" + UUID + "}")
    public Response getApplication(@PathParam(UUID) String uuid) {

        if (uuid != null) {
            try {
                EntityBody entityBody = service.get(uuid);
                return Response.status(Status.OK).entity(entityBody).build();
            } catch (EntityNotFoundException e) {
                LOG.debug("Could not find application with id " + uuid);
            }
        }

        return Response.status(Status.NOT_FOUND).build();

    }

    @DELETE
    @Path("{" + UUID + "}")
    public Response deleteApplication(@PathParam(UUID) String uuid) {
        
        if (uuid != null) {
            try {
                service.delete(uuid);
                return Response.status(Status.NO_CONTENT).build();
            } catch (EntityNotFoundException e) {
                LOG.debug("Could not find application with id " + uuid + " to delete");
            }
        }

        return Response.status(Status.NOT_FOUND).build();
    }
    
    @PUT
    @Path("{" + UUID + "}") 
    public Response updateApplication(@PathParam(UUID) String uuid, EntityBody app) {
        boolean status = service.update(uuid, app);
        if (status) {
            return Response.status(Status.NO_CONTENT).build();
        }
        return Response.status(Status.BAD_REQUEST).build();
    }
    

}
