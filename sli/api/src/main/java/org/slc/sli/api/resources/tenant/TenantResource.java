package org.slc.sli.api.resources.tenant;

import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import org.slc.sli.api.config.EntityDefinitionStore;
import org.slc.sli.api.representation.EntityBody;
import org.slc.sli.api.resources.Resource;
import org.slc.sli.api.resources.v1.DefaultCrudEndpoint;
import org.slc.sli.api.service.EntityService;
import org.slc.sli.common.constants.v1.ParameterConstants;

/**
 *
 * Provides CRUD operations on registered application through the /apps path.
 *
 * @author
 */
@Component
@Scope("request")
@Path("tenant")
@Produces({ Resource.JSON_MEDIA_TYPE })
public class TenantResource extends DefaultCrudEndpoint {

    @Autowired
    private EntityDefinitionStore store;

    @Autowired
    @Value("${sli.landingzone.mountDir}")
    private boolean mountDir;

    private EntityService service;

    private static final int TENANT_ID_LENGTH = 10; // TODO: Find true max length
    private static final int EDORG_ID_LENGTH = 48; // TODO: Find true max length
    private static final int USERNAME_LENGTH = 24; // TODO: Find true max length
    public static final String RESOURCE_NAME = "tenant";
    public static final String TENANT_ID = "tenantId";
    public static final String EDORG_ID = "educationOrganizationId";
    public static final String USERNAME = "userName";
    public static final String UUID = "uuid";

    public void setMountDir(boolean mountDir) {
        this.mountDir = mountDir;
    }

    @Autowired
    public TenantResource(EntityDefinitionStore entityDefs) {
        super(entityDefs, RESOURCE_NAME);
        store = entityDefs;
        service = store.lookupByResourceName(RESOURCE_NAME).getService();
    }

    @POST
    public Response createTenant(EntityBody newApp, @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
    	/*
        if (newApp.containsKey(TENANT_ID)
                || newApp.containsKey(CLIENT_ID)
                || newApp.containsKey("id")) {
            EntityBody body = new EntityBody();
            body.put("message", "Auto-generated attribute (id|client_secret|client_id) specified in POST.  "
                    + "Remove attribute and try again.");
            return Response.status(Status.BAD_REQUEST).entity(body).build();
        }
        if (!hasRight(Right.APP_CREATION)) {
            EntityBody body = new EntityBody();
            body.put("message", "You are not authorized to create new applications.");
            return Response.status(Status.BAD_REQUEST).entity(body).build();
        }
        String clientId = TokenGenerator.generateToken(CLIENT_ID_LENGTH);
        while (isDuplicateToken(clientId)) {
            clientId = TokenGenerator.generateToken(CLIENT_ID_LENGTH);
        }

        newApp.put(CLIENT_ID, clientId);
        SLIPrincipal principal = (SLIPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        newApp.put(CREATED_BY, principal.getExternalId());

        Map<String, Object> registration = new HashMap<String, Object>();
        registration.put(STATUS, "PENDING");
        if (autoRegister) {
            registration.put(APPROVAL_DATE, System.currentTimeMillis());
            registration.put(STATUS, "APPROVED");
            registration.put(APPROVAL_DATE, System.currentTimeMillis());
        }
        registration.put(REQUEST_DATE, System.currentTimeMillis());
        newApp.put(REGISTRATION, registration);

        String clientSecret = TokenGenerator.generateToken(CLIENT_SECRET_LENGTH);
        newApp.put(CLIENT_SECRET, clientSecret);
        return super.create(newApp, headers, uriInfo);
        */
    	return null;
    }

    @SuppressWarnings("rawtypes")
    @GET
    public Response getApplications(@QueryParam(ParameterConstants.OFFSET) @DefaultValue(ParameterConstants.DEFAULT_OFFSET) final int offset,
            @QueryParam(ParameterConstants.LIMIT) @DefaultValue(ParameterConstants.DEFAULT_LIMIT) final int limit,
            @Context
            HttpHeaders headers, @Context final UriInfo uriInfo) {
    	/*
        SLIPrincipal principal = (SLIPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (hasRight(Right.APP_CREATION)) {
            extraCriteria = new NeutralCriteria(CREATED_BY, NeutralCriteria.OPERATOR_EQUAL, principal.getExternalId());
        } else {
            debug("ED-ORG of operator/admin {}", principal.getEdOrg());
            extraCriteria = new NeutralCriteria(AUTHORIZED_ED_ORGS, NeutralCriteria.OPERATOR_EQUAL,
                    principal.getEdOrg());
        }
        Response resp = super.readAll(offset, limit, headers, uriInfo);
        filterSensitiveData((Map) resp.getEntity());
        return resp;
        */
    	return null;
    }

    /**
     * Looks up a specific application based on client ID, ie.
     * /api/rest/apps/<uuid>
     *
     * @param uuid
     *            the client ID, not the "id"
     * @return the JSON data of the application, otherwise 404 if not found
     */
    @SuppressWarnings("rawtypes")
    @GET
    @Path("{" + UUID + "}")
    public Response getApplication(@PathParam(UUID) String uuid,
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        Response resp = null;
        /*
        SLIPrincipal principal = (SLIPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (hasRight(Right.APP_CREATION)) {
            extraCriteria = new NeutralCriteria(CREATED_BY, NeutralCriteria.OPERATOR_EQUAL, principal.getExternalId());
        } else {
            extraCriteria = new NeutralCriteria(AUTHORIZED_ED_ORGS, NeutralCriteria.OPERATOR_EQUAL,
                    principal.getEdOrg());
        }
        resp = super.read(uuid, headers, uriInfo);
        filterSensitiveData((Map) resp.getEntity());
        */
        return resp;
    }


    @DELETE
    @Path("{" + UUID + "}")
    public Response deleteApplication(@PathParam(UUID) String uuid,
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
    	/*

        if (hasRight(Right.APP_CREATION)) {
            return super.delete(uuid, headers, uriInfo);
        } else {
            EntityBody body = new EntityBody();
            body.put("message", "You cannot delete this application");
            return Response.status(Status.BAD_REQUEST).entity(body).build();
        }
        */
    	return null;
    }

    @SuppressWarnings("unchecked")
    @PUT
    @Path("{" + UUID + "}")
    public Response updateApplication(@PathParam(UUID) String uuid, EntityBody app,
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
    	/*

        EntityBody oldApp = service.get(uuid);

        //The client id and secret could be null if they were filtered from the client
        String clientSecret = (String) app.get(CLIENT_SECRET);
        if (clientSecret == null)
            app.put(CLIENT_SECRET, oldApp.get(CLIENT_SECRET));
        String clientId = (String) app.get(CLIENT_ID);
        if (clientId == null)
            app.put(CLIENT_ID, oldApp.get(CLIENT_ID));

        String id = (String) app.get("id");
        Map<String, Object> oldReg = ((Map<String, Object>) oldApp.get(REGISTRATION));
        Map<String, Object> newReg = ((Map<String, Object>) app.get(REGISTRATION));
        String newRegStatus = (String) newReg.get(STATUS);
        String oldRegStatus = (String) oldReg.get(STATUS);
        List<String> changedKeys = new ArrayList<String>();

        for (Map.Entry<String, Object> entry : app.entrySet()) {
            if (oldApp.containsKey(entry.getKey()) && !oldApp.get(entry.getKey()).equals(entry.getValue())) {
                changedKeys.add(entry.getKey());
            }
        }

        if ((clientSecret != null && !clientSecret.equals(oldApp.get(CLIENT_SECRET)))
                || (clientId != null && !clientId.equals(oldApp.get(CLIENT_ID)))
                || (id != null && !id.equals(oldApp.get("id")))
                || (!registrationDatesMatch(oldReg, newReg, APPROVAL_DATE))
                || (!registrationDatesMatch(oldReg, newReg, REQUEST_DATE))) {
            EntityBody body = new EntityBody();
            body.put("message", "Cannot modify attribute (id|client_secret|client_id|request_date|approval_date) specified in PUT.  "
                    + "Remove attribute and try again.");
            return Response.status(Status.BAD_REQUEST).entity(body).build();
        }

        changedKeys.remove("registration");
        changedKeys.remove("developer_info");   //TODO: developer_info is a pain since it's nested--need to validate this hasn't changed
        changedKeys.remove("metaData");

        //Operator - can only change registration status
        if (hasRight(Right.APP_REGISTER)) {
            if (changedKeys.size() > 0) {
                EntityBody body = new EntityBody();
                body.put("message", "You are not authorized to alter applications.");
                return Response.status(Status.BAD_REQUEST).entity(body).build();
            }

            if (newRegStatus.equals("APPROVED") && oldRegStatus.equals("PENDING")) {
                debug("App approved");
                newReg.put(STATUS, "APPROVED");
                newReg.put(APPROVAL_DATE, System.currentTimeMillis());
            } else if (newRegStatus.equals("DENIED") && oldRegStatus.equals("PENDING")) {
                debug("App denied");
            } else if (newRegStatus.equals("UNREGISTERED") && oldRegStatus.equals("APPROVED")) {
                debug("App unregistered");
                newReg.remove(APPROVAL_DATE);
                newReg.remove(REQUEST_DATE);
            } else {
                EntityBody body = new EntityBody();
                body.put("message", "Invalid state change: " + oldRegStatus + " to " + newRegStatus);
                return Response.status(Status.BAD_REQUEST).entity(body).build();
            }

        } else if (hasRight(Right.APP_CREATION)) {  //App Developer
            if (!oldRegStatus.endsWith(newRegStatus)) {
                EntityBody body = new EntityBody();
                body.put("message", "You are not authorized to register applications.");
                return Response.status(Status.BAD_REQUEST).entity(body).build();
            }

            if (oldRegStatus.equals("PENDING")) {
                EntityBody body = new EntityBody();
                body.put("message", "Application cannot be modified while approval request is in Pending state.");
                return Response.status(Status.BAD_REQUEST).entity(body).build();
            }

            //when a denied or unreg'ed app is altered, it goes back into pending
            if (oldRegStatus.equals("DENIED") || oldRegStatus.equals("UNREGISTERED")) {

                //TODO: If auto approval is on, approve instead
                newReg.put(STATUS, "PENDING");
                newReg.put(REQUEST_DATE, System.currentTimeMillis());
            }
        } else {
            EntityBody body = new EntityBody();
            body.put("message", "You are not authorized to update application.");
            return Response.status(Status.BAD_REQUEST).entity(body).build();
        }

        return super.update(uuid, app, headers, uriInfo);
        */
    	return null;
    }


}
