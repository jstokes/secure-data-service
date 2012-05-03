/**
 * 
 */
package org.slc.sli.manager.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import junit.framework.Assert;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.Before;
import org.junit.Test;
import org.slc.sli.client.LiveAPIClient;
import org.slc.sli.entity.CustomConfig;
import org.slc.sli.entity.EdOrgKey;
import org.slc.sli.entity.GenericEntity;
import org.slc.sli.unit.entity.CustomConfigTest;

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
        LiveAPIClient apiClient = new LiveAPIClient() {
            
            private String customConfigJson = "{}";
            
            public CustomConfig getEdOrgCustomData(String token, String id) {
                Gson gson = new GsonBuilder().create();
                CustomConfig customConfig = gson.fromJson(customConfigJson, CustomConfig.class);
                return customConfig;
            }
            
            public void putEdOrgCustomData(String token, String id, String customJson) {
                customConfigJson = customJson;
            }

            public List<GenericEntity> getSchools(String token, List<String> schoolIds) {
                List<GenericEntity> schools = new ArrayList<GenericEntity>();
                schools.add(new GenericEntity()); // dummy GenericEntity
                return schools;
            }
            
            public GenericEntity getParentEducationalOrganization(final String token, GenericEntity edOrg) {
                GenericEntity entity = new GenericEntity();
                LinkedHashMap<String, Object> metaData = new LinkedHashMap<String, Object>();
                metaData.put("externalId", "my test district name");
                entity.put("metaData", metaData);
                return entity;
            }
            
        };
        this.testInstitutionalHierarchyManagerImpl = new UserEdOrgManagerImpl() {
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
    public void testGetUserDistrictId() {
        EdOrgKey key = this.testInstitutionalHierarchyManagerImpl.getUserEdOrg("fakeToken");
        Assert.assertEquals("my test district name", key.getDistrictId());
    }
    
    @Test
    public void testCustomConfig() {
        
        String token = "cacd9227-5b14-4685-babe-31230476cf3b";
        
        String customConfigJson = CustomConfigTest.DEFAULT_CUSTOM_CONFIG_JSON;
        this.testInstitutionalHierarchyManagerImpl.putCustomConfig(token, customConfigJson);
        CustomConfig customConfig = this.testInstitutionalHierarchyManagerImpl.getCustomConfig(token);
        Assert.assertEquals(2, customConfig.size());
        Assert.assertEquals("component_1", customConfig.get("component_1").getId());
        Assert.assertEquals(customConfigJson, customConfig.toJson());
        
    }
    
}
