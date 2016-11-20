package org.openmrs.module.radiologyfhirsupport.web.controller;

import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.module.radiologyfhirsupport.MRRTTemplate;
import org.openmrs.module.radiologyfhirsupport.api.MRRTTemplateService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;

/**
 * Created by devmaany on 22/6/16.
 */
@Controller
public class MRRTTemplateCreationController {
    @RequestMapping(value = "/module/radiologyfhirsupport/template/new.form", method = RequestMethod.GET)
    public String getForm(ModelMap map){
        MRRTTemplate mrrtTemplate = new MRRTTemplate();
        map.put("template",mrrtTemplate);
        System.out.println("This is the template creation controller");
        return "module/radiologyfhirsupport/create";
    }
    @RequestMapping(value = "/module/radiologyfhirsupport/template/new.form", method = RequestMethod.POST)
    public ModelAndView saveTemplate(HttpServletRequest request, ModelMap map) throws SQLException {
        String name = request.getParameter("name");
        String xml = request.getParameter("xml");
        //TODO add xml validation
        if(name.isEmpty() || xml.isEmpty()){
            throw new APIException("Validation errors");
        }
        MRRTTemplate mrrtTemplate = new MRRTTemplate();
        mrrtTemplate.setName(name);
        MRRTTemplateService mrrtTemplateService = Context.getService(MRRTTemplateService.class);
        mrrtTemplate.setXml(mrrtTemplateService.stringToClob(xml));
        System.out.println("Data received : " + mrrtTemplate.getName());
        System.out.println("Data received : " + mrrtTemplate.getXml());
        mrrtTemplateService.create(mrrtTemplate);
        String redirectURL = request.getContextPath() + "/" + MRRTTemplateCRUDFormController.VIEW_EDIT_REQUEST_MAPPING + "view/" + mrrtTemplate.getId() + ".form";
        return new ModelAndView(new RedirectView(redirectURL));
    }
}
