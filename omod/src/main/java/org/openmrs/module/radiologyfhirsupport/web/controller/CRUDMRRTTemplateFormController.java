package org.openmrs.module.radiologyfhirsupport.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.radiologyfhirsupport.MRRTTemplate;
import org.openmrs.module.radiologyfhirsupport.api.MRRTTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by devmaany on 20/6/16.
 */
@Controller
//@RequestMapping(value = CRUDMRRTTemplateFormController.VIEW_EDIT_REQUEST_MAPPING)
public class CRUDMRRTTemplateFormController {
    protected final Logger logger = Logger.getLogger(CRUDMRRTTemplateFormController.class.getName());
    public static final String VIEW_EDIT_FORM_VIEW = "/module/radiologyfhirsupport/viewEdit";
    public static final String VIEW_EDIT_REQUEST_MAPPING = "module/radiologyfhirsupport/template/";

    @RequestMapping(value = CRUDMRRTTemplateFormController.VIEW_EDIT_REQUEST_MAPPING + "view/{templateId}.form", method = RequestMethod.GET)
    public ModelAndView viewEdit(@PathVariable Integer templateId, ModelMap map) {
        MRRTTemplate mrrtTemplate = getService().getById(templateId);
        map.addAttribute("template", mrrtTemplate);
        try {
            String xml =  Context.getService(MRRTTemplateService.class).clobToString(mrrtTemplate.getXml());
            xml = xml.replaceAll("script","script_mrrt");
            xml = xml.replaceAll("(\\r|\\n|\\r\\n)+", "\\\\n");

            map.addAttribute("xml",xml);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Testing");
        return new ModelAndView("module/radiologyfhirsupport/viewEdit");
    }

    @RequestMapping(value = CRUDMRRTTemplateFormController.VIEW_EDIT_REQUEST_MAPPING + "view/{templateId}", method = RequestMethod.POST)
    public String editForm(@PathVariable Integer templateId, @Valid @ModelAttribute("template") MRRTTemplate template, BindingResult errors, ModelMap map) {
        if (errors.hasErrors()) {
            //TODO return error view
            logger.info("Binding errors found");
            return VIEW_EDIT_FORM_VIEW;
        }
        getService().saveOrUpdate(template);
        logger.info("Making edits for template with id" + template.getId());
        return VIEW_EDIT_FORM_VIEW;
    }

    @RequestMapping(value = CRUDMRRTTemplateFormController.VIEW_EDIT_REQUEST_MAPPING + "/{templateId}", method = RequestMethod.DELETE)
    public ModelAndView deleteTemplate(HttpServletRequest request, @ModelAttribute MRRTTemplate mrrtTemplate, @PathVariable Integer templateId) {
        mrrtTemplate = getService().delete(templateId);
        //TODO send message that client is unregistered
        String redirectURI = request.getContextPath() + "/" + MRRTIndexController.INDEX_CONTROLLER;
        logger.log(Level.INFO,"MRRT Template Unregistered : " + mrrtTemplate.getName());
        return new ModelAndView(new RedirectView(redirectURI));
    }

    private MRRTTemplateService getService(){
        return Context.getService(MRRTTemplateService.class);
    }

}
