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


package org.slc.sli.dashboard.web.controller;

import java.util.Collection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.google.gson.GsonBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import org.slc.sli.dashboard.entity.Config;
import org.slc.sli.dashboard.entity.ConfigMap;
import org.slc.sli.dashboard.entity.GenericEntity;
import org.slc.sli.dashboard.manager.ConfigManager;
import org.slc.sli.dashboard.manager.UserEdOrgManager;
import org.slc.sli.dashboard.util.Constants;
import org.slc.sli.dashboard.util.SecurityUtil;

/**
 *
 * DashboardConfigController
 * This controller handles the dashboard config pages which are only accessible
 * by IT Admins of a District.
 *
 */
@Controller
public class ConfigController extends GenericLayoutController {
    private static final String DASHBOARD_CONFIG_FTL = "dashboard_config.ftl";
    private static final String CONFIG_URL = "/service/config";
    private static final String CONFIG_SAVE_URL = "/service/config/ajaxSave";

    private UserEdOrgManager userEdOrgManager;
    private ConfigManager configManager;

    @Autowired
    public void setConfigManager(ConfigManager configManager) {
        this.configManager = configManager;
    }

    @Autowired
    public void setUserEdOrgManager(UserEdOrgManager userEdOrgManager) {
        this.userEdOrgManager = userEdOrgManager;
    }

    /**
     * Generic layout handler
     *
     * @param id
     * @param request
     * @return
     * @throws IllegalAccessException
     */
    @RequestMapping(value = CONFIG_URL, method = RequestMethod.GET)
    public ModelAndView getConfig(HttpServletRequest request) throws IllegalAccessException {
        ModelMap model = new ModelMap();

        String token = SecurityUtil.getToken();
        GenericEntity staffEntity = userEdOrgManager.getStaffInfo(token);

        Boolean isAdmin = SecurityUtil.isAdmin();
        if (isAdmin != null && isAdmin.booleanValue()) {
            Boolean localEducationAgency = (Boolean) staffEntity.get(Constants.LOCAL_EDUCATION_AGENCY);

            if (localEducationAgency != null && localEducationAgency.booleanValue()) {
                ConfigMap configMap = configManager.getCustomConfig(token, userEdOrgManager.getUserEdOrg(token));

                if (configMap != null) {
                    model.addAttribute("configJSON", new GsonBuilder().create().toJson(configMap));
                } else {
                    model.addAttribute("configJSON", "");
                }
            } else {
                model.addAttribute("configJSON", "nonLocalEducationAgency");

            }

            addCommonData(model, request);
            model.addAttribute(Constants.PAGE_TO_INCLUDE, DASHBOARD_CONFIG_FTL);
            return new ModelAndView(Constants.OVERALL_CONTAINER_PAGE, model);
        }
        throw new IllegalAccessException("Access Denied");
    }

    @RequestMapping(value = CONFIG_SAVE_URL, method = RequestMethod.POST)
    @ResponseBody
    public String saveConfig(@RequestBody @Valid ConfigMap configMap) {
        try {
            putCustomConfig(configMap);
        } catch (RuntimeException re) {
            logger.error("Error saving config", re);
            return "Permission Denied";
        }
        return "Success";
    }

    public void putCustomConfig(ConfigMap configMap) {
        String token = SecurityUtil.getToken();
        configManager.putCustomConfig(token, userEdOrgManager.getUserEdOrg(token), configMap);
    }

    /**
     * Controller for client side data pulls without id
     * /s/c/cfg?type=LAYOUT
     */
    @RequestMapping(value = "/s/c/cfg", method = RequestMethod.GET)
    @ResponseBody public Collection<Config> handleSearch(@RequestParam Map<String, String> params,
                                                         final HttpServletRequest request) {

        String token = SecurityUtil.getToken();
        return configManager.getConfigsByAttribute(token, userEdOrgManager.getUserEdOrg(token), params);
    }

    /**
     * Save a layout config
     *
     * @param config
     * @return
     */
    @RequestMapping(value = "/s/c/saveCfg", method = RequestMethod.POST)
    @ResponseBody
    public String saveLayoutConfig(@RequestBody @Valid Config config) {

        try {
            String token = SecurityUtil.getToken();
            configManager.putCustomConfig(token, userEdOrgManager.getUserEdOrg(token), config);
        } catch (RuntimeException re) {
            logger.error("Error saving config", re);
            return "Permission Denied";
        }
        return "Success";
    }

}
