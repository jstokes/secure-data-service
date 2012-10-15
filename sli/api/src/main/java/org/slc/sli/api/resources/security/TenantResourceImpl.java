/*
 * Copyright 2012 Shared Learning Collaborative, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.slc.sli.api.resources.security;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.PostConstruct;
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

import org.apache.commons.codec.digest.DigestUtils;
import org.slc.sli.api.config.EntityDefinitionStore;
import org.slc.sli.api.constants.ParameterConstants;
import org.slc.sli.api.init.RoleInitializer;
import org.slc.sli.api.representation.EntityBody;
import org.slc.sli.api.resources.Resource;
import org.slc.sli.api.resources.v1.DefaultCrudEndpoint;
import org.slc.sli.api.security.context.resolver.RealmHelper;
import org.slc.sli.api.service.EntityService;
import org.slc.sli.api.util.SecurityUtil;
import org.slc.sli.api.util.SecurityUtil.SecurityUtilProxy;
import org.slc.sli.api.util.StreamGobbler;
import org.slc.sli.domain.NeutralCriteria;
import org.slc.sli.domain.NeutralQuery;
import org.slc.sli.domain.enums.Right;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 
 * Provides CRUD operations on registered application through the /tenants path.
 * 
 * @author
 */
@Component
@Scope("request")
@Path("tenants")
@Produces({ Resource.JSON_MEDIA_TYPE + ";charset=utf-8" })
public class TenantResourceImpl extends DefaultCrudEndpoint implements TenantResource {
    
    @Value("${sli.sandbox.enabled}")
    protected boolean isSandboxEnabled;

    protected void setSandboxEnabled(boolean isSandboxEnabled) {
        this.isSandboxEnabled = isSandboxEnabled;
    }

    @Autowired
    private EntityDefinitionStore store;

    @Autowired
    private RoleInitializer roleInitializer;

    @Value("${sli.tenant.landingZoneMountPoint}")
    private String landingZoneMountPoint;

    @Value("${sli.tenant.ingestionServers}")
    private String ingestionServers;

    @Autowired
    private RealmHelper realmHelper;

    @Autowired
    private IngestionTenantLockChecker lockChecker;
    
    @Autowired
    private SecurityUtilProxy secUtil;

    protected void setSecUtil(SecurityUtilProxy secUtil) {
        this.secUtil = secUtil;
    }

    private List<String> ingestionServerList;

    /* this is only available for unit testing */
    public void setIngestionServerList(List<String> testList) {
        ingestionServerList = testList;
    }

    // private Random random = new Random(System.currentTimeMillis());

    @PostConstruct
    protected void init() {
        ingestionServerList = Arrays.asList(ingestionServers.split(","));
    }

    public static final String UUID = "uuid";
    public static final String RESOURCE_NAME = "tenant";
    public static final String TENANT_ID = "tenantId";
    public static final String LZ = "landingZone";
    public static final String LZ_EDUCATION_ORGANIZATION = "educationOrganization";
    public static final String LZ_INGESTION_SERVER = "ingestionServer";
    public static final String LZ_PATH = "path";
    public static final String LZ_USER_NAMES = "userNames";
    public static final String LZ_DESC = "desc";
    public static final String LZ_INGESTION_SERVER_LOCALHOST = "localhost";
    public static final String LZ_PRELOAD = "preload";
    public static final String LZ_PRELOAD_FILES = "files";
    public static final String LZ_PRELOAD_STATUS = "status";
    public static final String LZ_PRELOAD_STATUS_READY = "ready";
    public static final String LZ_PRELOAD_EDORG_ID = "STANDARD-SEA";
    public static final String PRE_SPLITTING_SCRIPT = "./sli-shard-presplit.js";

    @Autowired
    public TenantResourceImpl(EntityDefinitionStore entityDefs) {
        super(entityDefs, RESOURCE_NAME);
        store = entityDefs;
    }

    @Override
    @POST
    public Response create(EntityBody newTenant, @Context HttpHeaders headers, @Context final UriInfo uriInfo) {

        // Tenants can not be created using this class. They will be created via OnboardingResource
        return SecurityUtil.forbiddenResponse();
    }

    @Override
    public LandingZoneInfo createLandingZone(String tenantId, String edOrgId, boolean isSandbox)
            throws TenantResourceCreationException {
        String newTenantId = createLandingZone(tenantId, edOrgId, null, null, isSandbox);
        EntityService tenantService = store.lookupByResourceName(RESOURCE_NAME).getService();
        EntityBody newTenant = tenantService.get(newTenantId);
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> newLzs = (List<Map<String, Object>>) newTenant.get(LZ);
        for (Map<String, Object> lz : newLzs) {
            if (edOrgId.equals(lz.get(LZ_EDUCATION_ORGANIZATION))) {
                return new LandingZoneInfo((String) lz.get(LZ_PATH), (String) lz.get(LZ_INGESTION_SERVER));
            }
        }
        throw new TenantResourceCreationException(Status.INTERNAL_SERVER_ERROR,
                "Failed to find landing zone information after creation.");
    }

    @SuppressWarnings({ "unchecked" })
    protected String createLandingZone(EntityBody newTenant, boolean isSandbox) throws TenantResourceCreationException {
        List<Map<String, Object>> newLzs = (List<Map<String, Object>>) newTenant.get(LZ);

        // NOTE: OnboardingResource may only send in one at a time
        if (1 != newLzs.size()) {
            throw new TenantResourceCreationException(Status.BAD_REQUEST,
                    "Only one landing zone may be provisioned at a time.  Please submit your requests individually.");
        }

        String tenantId = (String) newTenant.get(TENANT_ID);

        Map<String, Object> newLz = newLzs.get(0);
        String newEdOrg = (String) newLz.get(LZ_EDUCATION_ORGANIZATION);

        return createLandingZone(tenantId, newEdOrg, (String) newLz.get(LZ_DESC),
                (List<String>) newLz.get(LZ_USER_NAMES), isSandbox);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected String createLandingZone(final String tenantId, String edOrgId, String desc, List<String> userNames,
            boolean isSandbox) throws TenantResourceCreationException {

        // get the exisint tenant resource
        EntityService tenantService = store.lookupByResourceName(RESOURCE_NAME).getService();
        NeutralQuery query = new NeutralQuery();
        query.addCriteria(new NeutralCriteria(TENANT_ID, "=", tenantId));

        String ingestionServer = findLeastLoadedIngestionServer();
        File inboundDirFile = new File(landingZoneMountPoint);
        File fullPath = new File(inboundDirFile, tenantId + "/" + DigestUtils.sha256Hex(edOrgId));
        String path = fullPath.getAbsolutePath();

        // resolve localhost ingestion server to the current server name
        if (ingestionServer.equals(LZ_INGESTION_SERVER_LOCALHOST)) {
            try {
                ingestionServer = InetAddress.getLocalHost().getHostName();
            } catch (UnknownHostException e) {
                throw new TenantResourceCreationException(Status.INTERNAL_SERVER_ERROR,
                        "Failed to resolve ingestion server for " + LZ_INGESTION_SERVER_LOCALHOST + ".");
            }
        }

        // look up ids of existing tenant entries
        List<String> existingIds = new ArrayList<String>();
        for (String id : tenantService.listIds(query)) {
            existingIds.add(id);
        }

        // If no tenant already exists, create one
        if (existingIds.size() == 0) {
            EntityBody newTenant = new EntityBody();
            newTenant.put(TENANT_ID, tenantId);
            Map<String, Object> nlz = buildLandingZone(edOrgId, desc, ingestionServer, path, userNames);
            List<Map<String, Object>> newLandingZoneList = new ArrayList<Map<String, Object>>();
            newLandingZoneList.add(nlz);
            newTenant.put(LZ, newLandingZoneList);

            // In sandbox a user doesn't create a realm, so this is the only opportunity to create
            // the custom roles
            if (isSandbox) {
                roleInitializer.dropAndBuildRoles(realmHelper.getSandboxRealmId());
            }
            
            // Call the pre-splitting script for mongo
            // WARNING: Shelling out to call a javascript occurs in this block
            // of code. This should be done extremely rarely and only with
            // the explicit consent of security. This particular block of code
            // was permitted by Daniel Fiedler and requested by Daniel Shaw.
            try {
                Runtime rt = Runtime.getRuntime();
                String varString = "var num_years=1, tenant='" + tenantId + "'";
                URL resourceFile = Thread.currentThread().getContextClassLoader().getResource(PRE_SPLITTING_SCRIPT);
                Process p = rt.exec(new String[] { "mongo", "admin", "--eval", varString, resourceFile.getPath() });
                // any error message?
                StreamGobbler errorGobbler = new StreamGobbler(p.getErrorStream(), "ERROR");

                // any output?
                StreamGobbler outputGobbler = new StreamGobbler(p.getInputStream(), "OUTPUT");

                // kick them off
                errorGobbler.start();
                outputGobbler.start();

                try {
                    p.waitFor();
                } catch (InterruptedException ie) {
                    // TODO Auto-generated catch block
                    ie.printStackTrace();
                }
            } catch (IOException ioe) {
                // TODO Auto-generated catch block
                ioe.printStackTrace();
            }
            return tenantService.create(newTenant);
        }
        // If more than exists, something is wrong
        if (existingIds.size() > 1) {
            throw new RuntimeException("Internal error: multiple tenant entry with identical IDs");
        }

        String existingTenantId = existingIds.get(0);
        // combine lzs from existing tenant and new tenant entry, overwriting with values of new
        // tenant entry if there is conflict.
        TreeSet allLandingZones = new TreeSet(new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                Map<String, Object> lz1 = (Map<String, Object>) o1;
                Map<String, Object> lz2 = (Map<String, Object>) o2;
                if (!lz1.containsKey(LZ_EDUCATION_ORGANIZATION)
                        || !(lz1.get(LZ_EDUCATION_ORGANIZATION) instanceof String)) {
                    throw new RuntimeException("Badly formed tenant entry: " + lz1.toString());
                }
                if (!lz2.containsKey(LZ_EDUCATION_ORGANIZATION)
                        || !(lz2.get(LZ_EDUCATION_ORGANIZATION) instanceof String)) {
                    throw new RuntimeException("Badly formed tenant entry: " + lz2.toString());
                }
                return ((String) lz1.get(LZ_EDUCATION_ORGANIZATION)).compareTo((String) lz2
                        .get(LZ_EDUCATION_ORGANIZATION));
            }
        });

        Set<Map<String, Object>> all = allLandingZones;
        for (Map<String, Object> lz : all) {
            if (lz.get(LZ_EDUCATION_ORGANIZATION).equals(edOrgId)) {
                throw new TenantResourceCreationException(Status.CONFLICT,
                        "This tenant/educational organization combination all ready has a landing zone provisioned.");
            }
        }

        EntityBody existingBody = tenantService.get(existingTenantId);
        List existingLandingZones = (List) existingBody.get(LZ);
        allLandingZones.addAll(existingLandingZones);

        Map<String, Object> nlz = this.buildLandingZone(edOrgId, desc, ingestionServer, path, userNames);
        allLandingZones.add(nlz);

        existingBody.put(LZ, new ArrayList(allLandingZones));
        tenantService.update(existingTenantId, existingBody);
        return existingTenantId;
    }

    private Map<String, Object> buildLandingZone(String edOrgId, String desc, String ingestionServer, String path,
            List<String> userNames) {
        Map<String, Object> newLandingZone = new HashMap<String, Object>();
        newLandingZone.put(LZ_EDUCATION_ORGANIZATION, edOrgId);
        newLandingZone.put(LZ_DESC, desc);
        newLandingZone.put(LZ_INGESTION_SERVER, ingestionServer);
        newLandingZone.put(LZ_PATH, path);
        newLandingZone.put(LZ_USER_NAMES, userNames);

        return newLandingZone;
    }

    private Map<String, Object> preload(List<String> dataSets) {
        Map<String, Object> preload = new HashMap<String, Object>();
        if ((dataSets != null) && (!dataSets.isEmpty())) {
            preload.put(LZ_PRELOAD_FILES, dataSets);
            preload.put(LZ_PRELOAD_STATUS, LZ_PRELOAD_STATUS_READY);
        }
        return preload;
    }

    /**
     * TODO: add javadoc
     * 
     */
    static class MutableInt {
        int value = 0;

        public void increment() {
            ++value;
        }

        public int get() {
            return value;
        }
    }

    public String findLeastLoadedIngestionServer() {
        EntityService tenantService = store.lookupByResourceName(RESOURCE_NAME).getService();
        Iterable<EntityBody> tenants = tenantService.get(tenantService.listIds(new NeutralQuery()));
        Map<String, MutableInt> map = new HashMap<String, MutableInt>(ingestionServerList.size());

        for (String s : ingestionServerList) {
            map.put(s.toLowerCase(), new MutableInt());
        }

        for (EntityBody t : tenants) {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> currentLZs = (List<Map<String, Object>>) t.get(LZ);
            for (Map<String, Object> lz : currentLZs) {
                String server = ((String) lz.get(LZ_INGESTION_SERVER)).toLowerCase();
                MutableInt use = map.get(server);
                // only increment if we actually have that server in our list
                if (null != use) {
                    use.increment();
                }
            }
        }

        int curMin = Integer.MAX_VALUE;
        String curHost = ingestionServerList.get(0);
        for (Entry<String, MutableInt> e : map.entrySet()) {
            int i = e.getValue().get();
            if (i < curMin) {
                curMin = i;
                curHost = e.getKey();
            }
        }

        return curHost;
    }

    @Override
    @GET
    public Response readAll(
            @QueryParam(ParameterConstants.OFFSET) @DefaultValue(ParameterConstants.DEFAULT_OFFSET) final int offset,
            @QueryParam(ParameterConstants.LIMIT) @DefaultValue(ParameterConstants.DEFAULT_LIMIT) final int limit,
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        SecurityUtil.ensureAuthenticated();
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
    @Override
    @GET
    @Path("{" + UUID + "}")
    public Response read(@PathParam(UUID) String uuid, @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        SecurityUtil.ensureAuthenticated();
        if (!SecurityUtil.hasRight(Right.ADMIN_ACCESS)) {
            EntityBody body = new EntityBody();
            body.put("message", "You are not authorized to view tenants or landing zones.");
            return Response.status(Status.FORBIDDEN).entity(body).build();
        }

        return super.read(uuid, headers, uriInfo);
    }

    /**
     * Preload a landing zone with a sample data set
     * 
     * @param tenantId
     *            tenant id
     * @param dataSet
     *            the name of the data set to preload
     * @param context
     *            the uri info
     * @return
     */
    @SuppressWarnings("deprecation")
    @POST
    @Path("{" + UUID + "}" + "/preload")
    public Response preload(@PathParam(UUID) String tenantId, String dataSet, @Context UriInfo context) {
        EntityService service = getEntityDefinition("tenant").getService();
        EntityBody entity = service.get(tenantId);
        String tenantName = (String) entity.get("tenantId");
        
        if (!SecurityUtil.hasRight(Right.INGEST_DATA) || !isSandboxEnabled || !tenantName.equals(secUtil.getTenantId())) {
            EntityBody body = new EntityBody();
            body.put("message", "You are not authorized.");
            return Response.status(Status.FORBIDDEN).entity(body).build();
        }

        if (lockChecker.ingestionLocked(tenantName)) {
            // throw new TenantResourceCreationException(Status.CONFLICT,
            // "Ingestion is locked for this tenant");
            return Response.status(Status.CONFLICT).build();
        }
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> landingZones = (List<Map<String, Object>>) entity.get("landingZone");
        if (landingZones == null || landingZones.isEmpty()) {
            return Response.status(Status.BAD_REQUEST).build();
        }
        for (Map<String, Object> landingZone : landingZones) {
            if (((String) (landingZone.get("educationOrganization"))).equals(LZ_PRELOAD_EDORG_ID)) {
                landingZone.put("preload", preload(Arrays.asList(dataSet)));
                break;
            }
        }
        
        // Map<String, Object> landingZone = landingZones.get(0);
        // landingZone.put("preload", preload(Arrays.asList(dataSet)));
        service.update(tenantId, entity);
        return Response.created(context.getAbsolutePathBuilder().path("jobstatus").build()).build();
    }

    /**
     * Get the status for the preloading job
     * This functionality is not available at this point
     * 
     * @return
     */
    @GET
    @Path("{" + UUID + "}" + "/preload/jobstatus")
    public Response getPreloadJob() {
        return Response.status(Status.NOT_IMPLEMENTED).build();
    }

    @Override
    @DELETE
    @Path("{" + UUID + "}")
    public Response delete(@PathParam(UUID) String uuid, @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        SecurityUtil.ensureAuthenticated();
        if (!SecurityUtil.hasRight(Right.ADMIN_ACCESS)) {
            EntityBody body = new EntityBody();
            body.put("message", "You are not authorized to delete tenants or landing zones.");
            return Response.status(Status.FORBIDDEN).entity(body).build();
        }

        return super.delete(uuid, headers, uriInfo);
    }

    @Override
    @PUT
    @Path("{" + UUID + "}")
    public Response update(@PathParam(UUID) String uuid, EntityBody tenant, @Context HttpHeaders headers,
            @Context final UriInfo uriInfo) {
        SecurityUtil.ensureAuthenticated();
        if (!SecurityUtil.hasRight(Right.ADMIN_ACCESS)) {
            EntityBody body = new EntityBody();
            body.put("message", "You are not authorized to provision tenants or landing zones.");
            return Response.status(Status.FORBIDDEN).entity(body).build();
        }

        return super.update(uuid, tenant, headers, uriInfo);
    }

    IngestionTenantLockChecker getLockChecker() {
        return lockChecker;
    }

    void setLockChecker(IngestionTenantLockChecker lockChecker) {
        this.lockChecker = lockChecker;
    }

}
