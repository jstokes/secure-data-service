/**
 *
 */
package org.slc.sli.manager.impl;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.GsonBuilder;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import org.slc.sli.client.APIClient;
import org.slc.sli.client.SDKAPIClient;
import org.slc.sli.entity.ConfigMap;
import org.slc.sli.entity.GenericEntity;

/**
 * @author tosako
 *
 */
public class UserEdOrgManagerImplTest {
    UserEdOrgManagerImpl testInstitutionalHierarchyManagerImpl = null;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {

            APIClient apiClient = new SDKAPIClient() {

            private String customConfigJson = "{}";

            @Override
            public ConfigMap getEdOrgCustomData(String token, String id) {
                return new GsonBuilder().create().fromJson(customConfigJson, ConfigMap.class);
            }

            public void putEdOrgCustomData(String token, String id, String customJson) {
                customConfigJson = customJson;
            }

            @Override
            public List<GenericEntity> getSchools(String token, List<String> schoolIds) {
                List<GenericEntity> schools = new ArrayList<GenericEntity>();
                schools.add(new GenericEntity()); // dummy GenericEntity
                return schools;
            }

            @Override
            public GenericEntity getParentEducationalOrganization(final String token, GenericEntity edOrg) {
                GenericEntity entity = new GenericEntity();
                entity.put("id", "AA");
                return entity;
            }

        };
        this.testInstitutionalHierarchyManagerImpl = new UserEdOrgManagerImpl() {
            @Override
            public String getToken() {
                return "";
            }
        };
        this.testInstitutionalHierarchyManagerImpl.setApiClient(apiClient);
    }

    /**
     * Test method for
     * {@link org.slc.sli.manager.impl.UserEdOrgManagerImpl#getUserEdOrg(java.lang.String)}
     * .
     */
    @Test
    @Ignore
    public void testGetUserDistrictId() {
        //EdOrgKey key = this.testInstitutionalHierarchyManagerImpl.getUserEdOrg("fakeToken");
        //Assert.assertEquals("my test district name", key.getDistrictId());
    }

}
