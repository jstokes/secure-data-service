package org.slc.sli.controller;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;

import freemarker.ext.beans.BeansWrapper;

import org.slc.sli.entity.Assessment;
import org.slc.sli.entity.assessmentmetadata.AssessmentMetaData;
import org.slc.sli.entity.Student;
import org.slc.sli.entity.StudentProgramAssociation;
import org.slc.sli.manager.AssessmentManager;
import org.slc.sli.manager.ConfigManager;
import org.slc.sli.manager.StudentManager;
import org.slc.sli.manager.ViewManager;

import org.slc.sli.config.ViewConfig;
import org.slc.sli.config.LozengeConfig;

import org.slc.sli.util.Constants;
import org.slc.sli.util.SecurityUtil;

import org.slc.sli.view.AssessmentResolver;
import org.slc.sli.view.LozengeConfigResolver;
import org.slc.sli.view.StudentResolver;
import org.slc.sli.view.widget.WidgetFactory;

/**
 * Controller for showing the list of studentview.
 *
 */
public class StudentListContentController extends DashboardController {

    private ConfigManager configManager;
    private StudentManager studentManager;
    private AssessmentManager assessmentManager;

    public StudentListContentController() { }

    /**
     * Retrieves information for the student list and sends back an html table to be displayed
     *
     * @param population Don't know what this could be yet... For now, a list of student uids
     * @param model
     * @param viewIndex The selected view configuration index
     * @return a ModelAndView object
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView studentListContent(String population, Integer viewIndex,
                                           ModelMap model) throws Exception {

        UserDetails user = SecurityUtil.getPrincipal();

        // get the list of all available viewConfigs
        List<ViewConfig> viewConfigs = configManager.getConfigsWithType(user.getUsername(), Constants.VIEW_TYPE_STUDENT_LIST);

        // insert the lozenge config object into modelmap
        List<LozengeConfig> lozengeConfig = configManager.getLozengeConfig(user.getUsername());
        model.addAttribute(Constants.MM_KEY_LOZENGE_CONFIG, new LozengeConfigResolver(lozengeConfig));

        List<String> uids = null;
        if (population != null) {
            uids = Arrays.asList(population.split(","));
        }
        
        ViewManager viewManager = new ViewManager(viewConfigs, studentManager);
        List<ViewConfig> applicableViewConfigs = viewManager.getApplicableViewConfigs(uids, user);

        if (applicableViewConfigs.size() > 0) {
            // add applicable viewConfigs to model map
            model.addAttribute("viewConfigs", applicableViewConfigs);

            ViewConfig viewConfig = applicableViewConfigs.get(viewIndex);
            model.addAttribute(Constants.MM_KEY_VIEW_CONFIG, viewConfig);  

            List<Student> students = studentManager.getStudentInfo(SecurityUtil.getToken(), uids, viewConfig);
            List<StudentProgramAssociation> programs = studentManager.getStudentProgramAssociations(user.getUsername(), uids);
            model.addAttribute(Constants.MM_KEY_STUDENTS, new StudentResolver(students, programs));

            // insert the assessments object into the modelmap
            List<Assessment> assessments = assessmentManager.getAssessments(user.getUsername(), uids, viewConfig);
            List<AssessmentMetaData> assessmentsMetaData = assessmentManager.getAssessmentMetaData(user.getUsername());
            model.addAttribute(Constants.MM_KEY_ASSESSMENTS, new AssessmentResolver(assessments, assessmentsMetaData));            
        }

        // insert a widget factory into the modelmap
        model.addAttribute(Constants.MM_KEY_WIDGET_FACTORY, new WidgetFactory());

        // let template access Constants
        model.addAttribute(Constants.MM_KEY_CONSTANTS, BeansWrapper.getDefaultInstance().getStaticModels().get(Constants.class.getName()));

        return new ModelAndView("studentListContent");
    }


    /*
     * Getters and setters
     */
    public ConfigManager getConfigManager() {
        return configManager;
    }

    public void setConfigManager(ConfigManager configManager) {
        this.configManager = configManager;
    }

    public StudentManager getStudentManager() {
        return studentManager;
    }

    public void setStudentManager(StudentManager studentManager) {
        this.studentManager = studentManager;
    }

    public AssessmentManager getAssessmentManager() {
        return assessmentManager;
    }

    public void setAssessmentManager(AssessmentManager assessmentManager) {
        this.assessmentManager = assessmentManager;
    }

}
