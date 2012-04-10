package org.slc.sli.controller;

import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

import org.slc.sli.entity.ModelAndViewConfig;
import org.slc.sli.manager.component.CustomizationAssemblyFactory;
import org.slc.sli.util.Constants;
import org.slc.sli.util.JsonConverter;
import org.slc.sli.util.SecurityUtil;

/**
 * Controller for all types of requests.
 * 
 * TODO: Refactor methods to be private and mock in unit tests with PowerMockito
 * 
 * @author dwu
 */
@Controller
public abstract class GenericLayoutController {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    private static final String LAYOUT_DIR = "layout/";
    
    private CustomizationAssemblyFactory customizationAssemblyFactory;
    
    /**
     * Populate layout model according to layout defined config for a user/context domain
     * 
     * @param layoutId
     *            - unique id of the layout
     * @param entityId
     *            - entity id to pass to the child panels
     * @return
     */
    protected ModelMap getPopulatedModel(String layoutId, Object entityKey, HttpServletRequest request) {
        
        // set up model map
        ModelMap model = new ModelMap();
        ModelAndViewConfig modelAndConfig =
                customizationAssemblyFactory.getModelAndViewConfig(layoutId, entityKey);
        model.addAttribute(Constants.MM_KEY_VIEW_CONFIGS, modelAndConfig.getComponentViewConfigMap());
        model.addAttribute(Constants.MM_KEY_VIEW_CONFIGS_JSON, JsonConverter.toJson(modelAndConfig.getComponentViewConfigMap()));
        model.addAttribute(Constants.MM_KEY_LAYOUT, modelAndConfig.getLayoutItems());
        model.addAttribute(Constants.MM_KEY_DATA, modelAndConfig.getData());
        model.addAttribute(Constants.MM_KEY_DATA_JSON, JsonConverter.toJson(modelAndConfig.getData()));
        model.addAttribute(Constants.MM_KEY_WIDGET_CONFIGS_JSON, JsonConverter.toJson(customizationAssemblyFactory.getWidgetConfigs()));
        setContextPath(model, request);
        
        // TODO: refactor so the below params can be removed
        populateModelLegacyItems(model);
        return model;
    }
    
    protected void setContextPath(ModelMap model, HttpServletRequest request) {
        model.addAttribute(Constants.CONTEXT_ROOT_PATH,  request.getContextPath()); 
    }
    

    // TODO: refactor so the below params can be removed
    public void populateModelLegacyItems(ModelMap model) {
        model.addAttribute("random", new Random());
    }
    
    
    protected String getLayoutView(String layoutName) {
        return LAYOUT_DIR + layoutName;
    }
    
    protected ModelAndView getModelView(String layoutName, ModelMap model) {
        return new ModelAndView(getLayoutView(layoutName), model);
    }
    
    @Autowired
    public void setCustomizedDataFactory(CustomizationAssemblyFactory customizedDataFactory) {
        this.customizationAssemblyFactory = customizedDataFactory;
    }
    
    public String getToken() {
        return SecurityUtil.getToken();
    }
}
