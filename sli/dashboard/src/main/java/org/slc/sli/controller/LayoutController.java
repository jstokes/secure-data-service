package org.slc.sli.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import org.slc.sli.entity.util.StudentProgramUtil;


/**
 * Layout controller for all types of requests. 
 * NOTE: This controller was introduced after the student list and app selector controllers. 
 * 
 * @author dwu
 */
@Controller
@RequestMapping(value = "/service/layout/")
public class LayoutController extends GenericLayoutController {
    private static final String TABBED_ONE_COL = "tabbed_one_col";

    /**
     * Controller for student profile
     * 
     * @param panelIds
     * @return
     */
    @RequestMapping(value = "/student", method = RequestMethod.GET)
    public ModelAndView handleStudentProfile(@RequestParam String id, HttpServletRequest request) {
        ModelMap model = getPopulatedModel("studentProfile", id, request);
        // TODO: get rid of StudentProgramUtil - instead enrich student entity with relevant programs 
        model.addAttribute("programUtil", new StudentProgramUtil());
        return getModelView(TABBED_ONE_COL, model);
    }
    
    /**
     * Generic layout handler
     * @param id
     * @param request
     * @return
     */
    @RequestMapping(value = "{componentId}", method = RequestMethod.GET)
    public ModelAndView handleListOfStudents(@PathVariable String componentId, @RequestParam(required = false) String id, HttpServletRequest request) {
        return getModelView(TABBED_ONE_COL, getPopulatedModel(componentId, id, request));
    }
}
