package org.slc.sli.api.resources.tenant;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

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
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import org.slc.sli.api.config.EntityDefinitionStore;
import org.slc.sli.api.representation.EntityBody;
import org.slc.sli.api.resources.Resource;
import org.slc.sli.api.resources.util.ResourceUtil;
import org.slc.sli.api.resources.v1.DefaultCrudEndpoint;
import org.slc.sli.api.service.EntityService;
import org.slc.sli.api.util.SecurityUtil;
import org.slc.sli.common.constants.v1.ParameterConstants;
import org.slc.sli.domain.NeutralCriteria;
import org.slc.sli.domain.NeutralQuery;
import org.slc.sli.domain.enums.Right;

/**
 *
 * Provides CRUD operations on registered application through the /tenants path.
 *
 * @author
 */
@Component
@Scope("request")
@Path("tenants")
@Produces({ Resource.JSON_MEDIA_TYPE })
public class TenantResource extends DefaultCrudEndpoint {

    @Autowired
    private EntityDefinitionStore store;

    public static final String UUID = "uuid";
    public static final String RESOURCE_NAME = "tenant";
    public static final String TENANT_ID = "tenantId";
    public static final String LZ = "landingZone";
    public static final String LZ_EDUCATION_ORGANIZATION = "educationOrganization";
    public static final String LZ_INGESTION_SERVER = "ingestionServer";
    public static final String LZ_PATH = "path";
    public static final String LZ_USER_NAMES = "userNames";
    public static final String LZ_DESC = "desc";

    @Autowired
    public TenantResource(EntityDefinitionStore entityDefs) {
        super(entityDefs, RESOURCE_NAME);
        store = entityDefs;
    }

    @POST
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Response createTenant(EntityBody newTenant, @Context HttpHeaders headers, @Context final UriInfo uriInfo) {

        if (!SecurityUtil.hasRight(Right.ADMIN_ACCESS)) {
            EntityBody body = new EntityBody();
            body.put("message", "You are not authorized to provision tenants or landing zones.");
            return Response.status(Status.FORBIDDEN).entity(body).build();
        }

        EntityService tenantService = store.lookupByResourceName(RESOURCE_NAME).getService();

        // look up the tenantId; if it exists, add new LZs here to the existing list; otherwise, create

        // Create the query
        if (!newTenant.containsKey(LZ) || !newTenant.containsKey(TENANT_ID)) {
            EntityBody body = new EntityBody();
            body.put("message", "Required attributes (" + LZ + "," + TENANT_ID + ") not specified in POST.  "
                    + "add attribute and try again.");
            return Response.status(Status.BAD_REQUEST).entity(body).build();
        }
        String tenantId = (String) newTenant.get(TENANT_ID);
        NeutralQuery query = new NeutralQuery();
        query.addCriteria(new NeutralCriteria(TENANT_ID, "=", tenantId));

        // look up ids of existing tenant entries
        List<String> existingIds = new ArrayList<String>();
        for (String id : tenantService.listIds(query)) {
            existingIds.add(id);
        }
        // If no tenant already exist, create
        if (existingIds.size() == 0) {
            String id = tenantService.create(newTenant);
            String uri = ResourceUtil.getURI(uriInfo, "tenants", id).toString();
            return Response.status(Status.CREATED).header("Location", uri).build();
        }
        // If more than exists, something is wrong
        if (existingIds.size() > 1) {
            throw new RuntimeException("Internal error: multiple tenant entry with identical IDs");
        }

        String existingTenantId = existingIds.get(0);
        // combine lzs from existing tenant and new tenant entry, overwriting with values of new tenant entry if there is conflict.
        TreeSet allLandingZones = new TreeSet(new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                Map<String, Object> lz1 = (Map<String, Object>) o1;
                Map<String, Object> lz2 = (Map<String, Object>) o2;
                if (!lz1.containsKey(LZ_EDUCATION_ORGANIZATION) || !(lz1.get(LZ_EDUCATION_ORGANIZATION) instanceof String)) {
                    throw new RuntimeException("Badly formed tenant entry: " + lz1.toString());
                }
                if (!lz2.containsKey(LZ_EDUCATION_ORGANIZATION) || !(lz2.get(LZ_EDUCATION_ORGANIZATION) instanceof String)) {
                    throw new RuntimeException("Badly formed tenant entry: " + lz2.toString());
                }
                return ((String) lz1.get(LZ_EDUCATION_ORGANIZATION)).compareTo((String) lz2.get(LZ_EDUCATION_ORGANIZATION));
            }
        });

        EntityBody existingBody = tenantService.get(existingTenantId);
        List existingLandingZones = (List) existingBody.get(LZ);
        allLandingZones.addAll(existingLandingZones);
        List newLandingZones = (List) newTenant.get(LZ);
        allLandingZones.addAll(newLandingZones);

        existingBody.put(LZ, new ArrayList(allLandingZones));
        tenantService.update(existingTenantId, existingBody);

        // Construct the response
        String uri = ResourceUtil.getURI(uriInfo, "tenants", existingTenantId).toString();
        return Response.status(Status.CREATED).header("Location", uri).build();
    }

    @GET
    public Response getTenants(@QueryParam(ParameterConstants.OFFSET) @DefaultValue(ParameterConstants.DEFAULT_OFFSET) final int offset,
            @QueryParam(ParameterConstants.LIMIT) @DefaultValue(ParameterConstants.DEFAULT_LIMIT) final int limit,
            @Context
            HttpHeaders headers, @Context final UriInfo uriInfo) {

        if (!SecurityUtil.hasRight(Right.ADMIN_ACCESS)) {
            EntityBody body = new EntityBody();
            body.put("message", "You are not authorized to view tenants or landing zones.");
            return Response.status(Status.FORBIDDEN).entity(body).build();
        }

        return super.readAll(offset, limit, headers, uriInfo);
    }

    /**
     * Looks up a specific application based on client ID, ie.
     * /api/rest/tenants/<tenantId>
     *
     * @param tenantId
     *            the client ID, not the "id"
     * @return the JSON data of the application, otherwise 404 if not found
     */
    @GET
    @Path("{" + UUID + "}")
    public Response getTenant(@PathParam(UUID) String uuid,
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {

        if (!SecurityUtil.hasRight(Right.ADMIN_ACCESS)) {
            EntityBody body = new EntityBody();
            body.put("message", "You are not authorized to view tenants or landing zones.");
            return Response.status(Status.FORBIDDEN).entity(body).build();
        }

        return super.read(uuid, headers, uriInfo);
    }

    @DELETE
    @Path("{" + UUID + "}")
    public Response deleteTenant(@PathParam(UUID) String uuid,
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {

        if (!SecurityUtil.hasRight(Right.ADMIN_ACCESS)) {
            EntityBody body = new EntityBody();
            body.put("message", "You are not authorized to delete tenants or landing zones.");
            return Response.status(Status.FORBIDDEN).entity(body).build();
        }

        return super.delete(uuid, headers, uriInfo);
    }

    @PUT
    @Path("{" + UUID + "}")
    public Response updateTenant(@PathParam(UUID) String uuid, EntityBody tenant,
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {

        if (!SecurityUtil.hasRight(Right.ADMIN_ACCESS)) {
            EntityBody body = new EntityBody();
            body.put("message", "You are not authorized to provision tenants or landing zones.");
            return Response.status(Status.FORBIDDEN).entity(body).build();
        }

        return super.update(uuid, tenant, headers, uriInfo);
    }

}
