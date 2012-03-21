package org.slc.sli.manager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.slc.sli.client.APIClient;
import org.slc.sli.config.ConfigPersistor;
import org.slc.sli.config.ViewConfig;
import org.slc.sli.config.ViewConfigSet;
import org.slc.sli.config.LozengeConfig;
import org.slc.sli.config.StudentFilter;
import org.slc.sli.entity.Config;
import org.slc.sli.entity.GenericEntity;
import org.slc.sli.util.DashboardException;

/**
 *
 * ConfigManager allows other classes, such as controllers, to access and persist view configurations.
 * Given a user, it will obtain view configuration at each level of the user's hierarchy, and merge
 * them into one set for the user.
 *
 * @author dwu
 */
public class ConfigManager extends ApiClientManager {

    private Logger logger = LoggerFactory.getLogger(getClass());
    ConfigPersistor persistor;
    EntityManager entityManager;
    
    private String configLocation = "config";
    private InstitutionalHierarchyManager institutionalHierarchyManager;
    
    public ConfigManager() {
        persistor = new ConfigPersistor();
    }

    @Override
    public void setApiClient(APIClient apiClient) {
        super.setApiClient(apiClient);
        persistor.setApiClient(apiClient);
    }

    /**
     * Get the view configuration set for a user
     *
     * @param userId
     * @return ViewConfigSet
     */
    public ViewConfigSet getConfigSet(String userId) {

        // get view configs for user's hierarchy (state, district, etc)
        // TODO: call ConfigPersistor with entity ids, not user id
        ViewConfigSet userViewConfigSet = null;
        try {
            userViewConfigSet = persistor.getConfigSet(userId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        // TODO: merge into one view config set for the user


        return userViewConfigSet;
    }

    /**
     * Get the configuration for one particular view, for a user
     *
     * @param userId
     * @param viewName
     * @return ViewConfig
     */
    public ViewConfig getConfig(String userId, String viewName) {

        ViewConfigSet config = getConfigSet(userId);

        if (config == null) {
            return null;
        }

        // loop through, find right config
        for (ViewConfig view : config.getViewConfig()) {
            if (view.getName().equals(viewName)) {
                return view;
            }
        }
        return null;
    }

    /**
     * Get the configuration for one particular view, for a user
     *
     * @param userId
     * @param viewName
     * @return ViewConfig
     */
    public List<LozengeConfig> getLozengeConfig(String userId) {

        // get lozenge configs for user's hierarchy (state, district, etc)
        // TODO: call ConfigPersistor with entity ids, not user id
        LozengeConfig[] userLozengeConfig = null;
        try {
            userLozengeConfig = persistor.getLozengeConfig(userId);
        } catch (Exception e) {
            return null;
        }
        return Arrays.asList(userLozengeConfig);
    }

    /**
     * Get the configuration for one particular view, for a user
     *
     * @param userId
     * @param viewName
     * @return StudentFilter list
     */
    public List<StudentFilter> getStudentFilterConfig(String userId) {

        // get student filter configs for user's hierarchy (state, district, etc)
        StudentFilter[] userStudentFilterConfig = null;
        try {
            userStudentFilterConfig = persistor.getStudentFilterConfig(userId);
        } catch (Exception e) {
            return null;
        }
        return Arrays.asList(userStudentFilterConfig);
    }

    /**
     * Get the configuration for one particular view, for a user
     *
     * @param userId
     * @param type - e.g. studentList, studentProfile, etc.
     * @return List<ViewConfig>
     */
    public List<ViewConfig> getConfigsWithType(String userId, String type) {
        
        ViewConfigSet config = getConfigSet(userId);
        List<ViewConfig> viewConfigs = null;
        
        if (config != null && config.getViewConfig() != null) {
            viewConfigs = new ArrayList<ViewConfig>();
            
            // loop through, find right type configs
            for (ViewConfig view : config.getViewConfig()) {
                if (view.getType().equals(type)) {
                    viewConfigs.add(view);
                }
            }            
        }
        return viewConfigs;
    }

    /**
     * Merges a hierarchy of configuration sets into one set
     *
     * @param configSets
     * @return ViewConfigSet
     */
    protected ViewConfigSet mergeConfigSets(List<ViewConfigSet> configSets) {
        // TODO: implement merge
        return null;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
        persistor.setEntityManager(entityManager);
    }
    
    public void setInstitutionalHierarchyManager(InstitutionalHierarchyManager institutionalHierarchyManager) {
        this.institutionalHierarchyManager = institutionalHierarchyManager;
    }
    
    public void setConfigLocation(String configLocation) {
        this.configLocation = configLocation;
    }
    
    private String getConfigLocation(String componentId) {
        return configLocation + "/" + componentId + ".json";
    }
    
    private Config applyConfigRecursively(String userId, String componentId) {
        List<GenericEntity> hierarchy = institutionalHierarchyManager.getInstHierarchy(userId);
        return null;
    }
    
    public Config getComponentConfig(String userId, String componentId) {
        //applyConfigRecursively(SecurityUtil.getToken(), componentId);
        Gson gson = new GsonBuilder().create();
        try {
            return gson.fromJson(
                    new BufferedReader(new InputStreamReader(
                            Config.class.getClassLoader().getResourceAsStream(getConfigLocation(componentId)))), Config.class);
        } catch (Throwable t) {
            logger.error("Unable to read config for " + componentId + ", for user " + userId);
            throw new DashboardException("Unable to read config for " + componentId + ", for user " + userId);
        }
   
    }

}
