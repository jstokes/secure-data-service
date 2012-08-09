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


package org.slc.sli.api.init;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slc.sli.domain.Entity;
import org.slc.sli.domain.NeutralCriteria;
import org.slc.sli.domain.NeutralQuery;
import org.slc.sli.domain.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Class for bootstrapping the initial SLI realm that must always exist into mongo.
 *
 */
@Component
public class RealmInitializer {

    @Value("${bootstrap.admin.realm.name}")
    private String adminRealmName;

    @Value("${bootstrap.admin.realm.tenantId}")
    private String adminTenantId;

    @Value("${bootstrap.admin.realm.idpId}")
    private String adminIdpId;

    @Value("${bootstrap.admin.realm.redirectEndpoint}")
    private String adminRedirectEndpoint;

    @Value("${bootstrap.sandbox.realm.uniqueId}")
    private String sandboxUniqueId;

    @Value("${bootstrap.sandbox.realm.name}")
    private String sandboxRealmName;

    @Value("${bootstrap.sandbox.realm.idpId}")
    private String sandboxIdpId;

    @Value("${bootstrap.sandbox.realm.redirectEndpoint}")
    private String sandboxRedirectEndpoint;

    @Value("${bootstrap.sandbox.createSandboxRealm}")
    private boolean createSandboxRealm;

    @Autowired
    @Qualifier("validationRepo")
    private Repository<Entity> repository;

    protected static final String REALM_RESOURCE = "realm";

    //This is what we use to look up the existing admin realm.  If this changes, we might end up with extra realms
    public static final String ADMIN_REALM_ID = "Shared Learning Infrastructure";

    @PostConstruct
    public void bootstrap() {
        // boostrap the admin realm
        Entity existingAdminRealm = findRealm(ADMIN_REALM_ID);
        Map<String, Object> bootstrapAdminRealmBody = createAdminRealmBody();
        if (existingAdminRealm != null) {
            info("Admin realm already exists --> updating if necessary.");
            updateRealmIfNecessary(existingAdminRealm, bootstrapAdminRealmBody);
        } else {
            info("Creating Admin realm.");
            repository.create(REALM_RESOURCE, bootstrapAdminRealmBody);
        }

        // if sandbox mode, bootstrap the sandbox realm
        if (createSandboxRealm) {
            Entity existingSandboxRealm = findRealm(sandboxUniqueId);
            Map<String, Object> bootstrapSandboxRealmBody = createSandboxRealmBody();
            if (existingSandboxRealm != null) {
                info("Sandbox realm already exists --> updating if necessary.");
                updateRealmIfNecessary(existingSandboxRealm, bootstrapSandboxRealmBody);
            } else {
                info("Creating Sandbox realm.");
                repository.create(REALM_RESOURCE, bootstrapSandboxRealmBody);
            }
        }
    }
    
    /**
     * We only want to update the realm if it has changed.
     * It's a bad idea to drop the admin realm without checking
     * because we could potentially have multiple API machines all
     * hitting the same mongo instance and reinitializing the realm
     * @param existingRealm
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void updateRealmIfNecessary(Entity existingRealm, Map<String, Object> newRealmBody) {
        Map oldBody = existingRealm.getBody();
        long oldHash = InitializerUtils.checksum(oldBody);
        long newHash = InitializerUtils.checksum(newRealmBody);
        if (oldHash != newHash) {
            existingRealm.getBody().clear();
            existingRealm.getBody().putAll(newRealmBody);
            if (repository.update(REALM_RESOURCE, existingRealm)) {
                info("Successfully updated realm: {}", new Object[] {newRealmBody.get("name")});
            } else {
                warn("Failed to update realm: {}", new Object[] {newRealmBody.get("name")});
            }            
        } else {
            info("No need to update realm: {}", new Object[] {existingRealm.getBody().get("name")});
        }
    }

    protected Map<String, Object> createAdminRealmBody() {
        Map<String, Object> body = createRealmBody(ADMIN_REALM_ID, adminRealmName, adminTenantId, "fakeab32-b493-999b-a6f3-sliedorg1234",
                true, adminIdpId, adminRedirectEndpoint);
        Map<String, Object> saml = new HashMap<String, Object>();
        saml.put("field", getAdminFields());
        body.put("saml", saml);
        return body;
    }

    protected Map<String, Object> createSandboxRealmBody() {
        Map<String, Object> body = createRealmBody(sandboxUniqueId, sandboxRealmName, "", null, false, sandboxIdpId,
                sandboxRedirectEndpoint);
        Map<String, Object> saml = new HashMap<String, Object>();
        saml.put("field", getSandboxFields());
        body.put("saml", saml);
        return body;
    }

    public Map<String, Object> createRealmBody(String tenantId, String uniqueId, String realmName, String idpId, String redirectEndpoint) {
        Map<String, Object> body = createRealmBody(uniqueId, realmName, tenantId, null, false, idpId, redirectEndpoint);
        Map<String, Object> saml = new HashMap<String, Object>();
        saml.put("field", getSandboxFields());
        body.put("saml", saml);
        return body;
    }
    
    private Map<String, Object> createRealmBody(String uniqueId, String name, String tenantId, String edOrg,
            boolean admin, String idpId, String redirectEndpoint) {
        Map<String, Object> body = new HashMap<String, Object>();
        body.put("name", name);
        body.put("uniqueIdentifier", uniqueId);
        if (tenantId != null) {
            body.put("tenantId", tenantId);
        }
        if (edOrg != null) {
            body.put("edOrg", edOrg);
        }
        body.put("admin", admin);

        Map<String, Object> idp = new HashMap<String, Object>();
        idp.put("id", idpId);
        idp.put("redirectEndpoint", redirectEndpoint);
        body.put("idp", idp);
        return body;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private List getAdminFields() {
        List toReturn = new ArrayList();
        toReturn.add(createField("roles", "(.+)"));
        toReturn.add(createField("tenant", "(.+)"));
        toReturn.add(createField("edOrg", "(.+)"));
        toReturn.add(createField("userId", "(.+)"));
        toReturn.add(createField("userName", "(.+)"));
        return toReturn;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private List getSandboxFields() {
        List toReturn = new ArrayList();
        toReturn.add(createField("roles", "(.+)"));
        toReturn.add(createField("tenant", "(.+)"));
        toReturn.add(createField("userId", "(.+)"));
        toReturn.add(createField("userName", "(.+)"));
        return toReturn;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private Map createField(String name, String transform) {
        Map toReturn = new HashMap();
        toReturn.put("clientName", name);
        toReturn.put("sliName", name);
        toReturn.put("transform", transform);
        return toReturn;
    }

    /**
     * Find a realm.
     *
     * @return the realm entity, or null if not found
     */
    private Entity findRealm(String realmUniqueId) {
        return repository.findOne(REALM_RESOURCE, new NeutralQuery(new NeutralCriteria("uniqueIdentifier",
                NeutralCriteria.OPERATOR_EQUAL, realmUniqueId)));
    }
}
