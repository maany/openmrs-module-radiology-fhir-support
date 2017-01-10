package org.openmrs.module.radiologyfhirsupport.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by devmaany on 13/2/17.
 */

@Controller
public class MRRTReportRUDFormController {
    public static final String VIEW_EDIT_REQUEST_MAPPING = "module/radiologyfhirsupport/report/";

    @RequestMapping(value = MRRTReportRUDFormController.VIEW_EDIT_REQUEST_MAPPING + "view/{reportId}.form", method = RequestMethod.GET)
    public ModelAndView viewEdit(@PathVariable Integer reportId, ModelMap map){
        return new ModelAndView("module/radiologyfhirsupport/index");
    }
}
