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

package org.slc.sli.ingestion.tenant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import org.slc.sli.domain.Entity;
import org.slc.sli.domain.NeutralCriteria;
import org.slc.sli.domain.NeutralQuery;
import org.slc.sli.domain.Repository;

/**
 * Mongo implementation for access to tenant data.
 *
 * @author jtully
 */
@Component
public class TenantMongoDA implements TenantDA {

    private static final String LANDING_ZONE_PATH = "landingZone.path";
    private static final String LANDING_ZONE_INGESTION_SERVER = "landingZone.ingestionServer";
    public static final String TENANT_ID = "tenantId";
    public static final String INGESTION_SERVER = "ingestionServer";
    public static final String PATH = "path";
    public static final String LANDING_ZONE = "landingZone";
    public static final String PRELOAD_DATA = "preload";
    public static final String PRELOAD_STATUS = "status";
    public static final String PRELOAD_FILES = "files";
    public static final String TENANT_COLLECTION = "tenant";
    public static final String TENANT_TYPE = "tenant";
    public static final String EDUCATION_ORGANIZATION = "educationOrganization";
    public static final String DESC = "desc";

    private Repository<Entity> entityRepository;
    private static final NeutralCriteria PRELOAD_READY_CRITERIA = new NeutralCriteria(LANDING_ZONE + "." + PRELOAD_DATA
            + "." + PRELOAD_STATUS, "=", "ready");

    @Override
    public List<String> getLzPaths(String ingestionServer) {
        List<String> lzPaths = findTenantPathsByIngestionServer(ingestionServer);
        return lzPaths;
    }

    @Override
    public String getTenantId(String lzPath) {
        return findTenantIdByLzPath(lzPath);
    }

    @Override
    public void insertTenant(TenantRecord tenant) {
        if (entityRepository.findOne(TENANT_COLLECTION,
                new NeutralQuery(new NeutralCriteria(TENANT_ID, "=", tenant.getTenantId()))) == null) {
            entityRepository.create(TENANT_COLLECTION, getTenantBody(tenant));
        }
    }

    private Map<String, Object> getTenantBody(TenantRecord tenant) {
        Map<String, Object> body = new HashMap<String, Object>();
        body.put(TENANT_ID, tenant.getTenantId());
        List<Map<String, String>> landingZones = new ArrayList<Map<String, String>>();
        if (tenant.getLandingZone() != null) {
            for (LandingZoneRecord landingZoneRecord : tenant.getLandingZone()) {
                Map<String, String> landingZone = new HashMap<String, String>();
                landingZone.put(EDUCATION_ORGANIZATION, landingZoneRecord.getEducationOrganization());
                landingZone.put(INGESTION_SERVER, landingZoneRecord.getIngestionServer());
                landingZone.put(PATH, landingZoneRecord.getPath());
                landingZone.put(DESC, landingZoneRecord.getDesc());
                landingZones.add(landingZone);
            }
        }
        body.put(LANDING_ZONE, landingZones);
        return body;
    }

    private List<String> findTenantPathsByIngestionServer(String targetIngestionServer) {
        List<String> tenantPaths = new ArrayList<String>();

        Iterable<Entity> entities = entityRepository.findAll(TENANT_COLLECTION);

        for (Entity entity : entities) {
            @SuppressWarnings("unchecked")
            List<Map<String, String>> landingZones = (List<Map<String, String>>) entity.getBody().get(LANDING_ZONE);
            if (landingZones != null) {
                for (Map<String, String> landingZone : landingZones) {
                        String path = landingZone.get(PATH);
                        if (path != null) {
                            tenantPaths.add(path);
                    }
                }
            }
        }
        return tenantPaths;
    }

    private NeutralCriteria byServerQuery(String targetIngestionServer) {
        return new NeutralCriteria(LANDING_ZONE_INGESTION_SERVER, "=", targetIngestionServer);
    }

    private String findTenantIdByLzPath(String lzPath) {
        NeutralQuery query = new NeutralQuery(new NeutralCriteria(LANDING_ZONE_PATH, "=", lzPath));
        Entity entity = entityRepository.findOne(TENANT_COLLECTION, query);
        return (String) entity.getBody().get(TENANT_ID);
    }

    public Repository<Entity> getEntityRepository() {
        return entityRepository;
    }

    public void setEntityRepository(Repository<Entity> entityRepository) {
        this.entityRepository = entityRepository;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, List<String>> getPreloadFiles(String ingestionServer) {
        Iterable<Entity> tenants = entityRepository.findAll(
                TENANT_COLLECTION,
                new NeutralQuery(byServerQuery(ingestionServer)).addCriteria(PRELOAD_READY_CRITERIA).setIncludeFields(
                        Arrays.asList(LANDING_ZONE + "." + PRELOAD_DATA, LANDING_ZONE_PATH,
                                LANDING_ZONE_INGESTION_SERVER)));
        Map<String, List<String>> fileMap = new HashMap<String, List<String>>();
        for (Entity tenant : tenants) {
            if (readyTenant(tenant)) { // only return this if the tenant is not already in the
                                       // started state
                List<Map<String, Object>> landingZones = (List<Map<String, Object>>) tenant.getBody().get(LANDING_ZONE);
                for (Map<String, Object> landingZone : landingZones) {
                    if (landingZone.get(INGESTION_SERVER).equals(ingestionServer)) {
                        List<String> files = new ArrayList<String>();
                        Map<String, Object> preloadData = (Map<String, Object>) landingZone.get(PRELOAD_DATA);
                        if (preloadData != null) {
                            if ("ready".equals(preloadData.get(PRELOAD_STATUS))) {
                                files.addAll((Collection<? extends String>) preloadData.get(PRELOAD_FILES));
                            }
                            fileMap.put((String) landingZone.get(PATH), files);
                        }
                    }
                }
            }
        }
        return fileMap;
    }

    private boolean readyTenant(Entity tenant) {
        return entityRepository.doUpdate(
                TENANT_COLLECTION,
                new NeutralQuery().addCriteria(new NeutralCriteria("_id", "=", tenant.getEntityId())).addCriteria(
                        PRELOAD_READY_CRITERIA),
                Update.update("body." + TenantMongoDA.LANDING_ZONE + ".$." + TenantMongoDA.PRELOAD_DATA + "."
                        + TenantMongoDA.PRELOAD_STATUS, "started"));

    }
}
