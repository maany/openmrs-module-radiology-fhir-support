package org.openmrs.module.radiologyfhirsupport.web.controller;

import org.openmrs.api.context.Context;
import org.openmrs.module.radiologyfhirsupport.MRRTTemplate;
import org.openmrs.module.radiologyfhirsupport.api.MRRTTemplateService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
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
public class MRRTTemplateCRUDFormController {
    protected final Logger logger = Logger.getLogger(MRRTTemplateCRUDFormController.class.getName());
    public static final String VIEW_EDIT_FORM_VIEW = "/module/radiologyfhirsupport/viewEdit";
    public static final String VIEW_EDIT_REQUEST_MAPPING = "module/radiologyfhirsupport/template/";

    @RequestMapping(value = MRRTTemplateCRUDFormController.VIEW_EDIT_REQUEST_MAPPING + "view/{templateId}.form", method = RequestMethod.GET)
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

    @RequestMapping(value = MRRTTemplateCRUDFormController.VIEW_EDIT_REQUEST_MAPPING + "view/{templateId}", method = RequestMethod.POST)
    public ModelAndView editForm(@PathVariable Integer templateId, HttpServletRequest request,@Valid @ModelAttribute("template") MRRTTemplate template, BindingResult errors, ModelMap map) {
        String name = request.getParameter("name");
        String xml = request.getParameter("xml");
        xml = xml.replaceAll("script_mrrt","script");
        xml = xml.replaceAll("\\\\n",System.lineSeparator());
        MRRTTemplate mrrtTemplate = getService().getById(templateId);
        mrrtTemplate.setName(name);
        try {
            mrrtTemplate.setXml(getService().stringToClob(xml));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        getService().saveOrUpdate(mrrtTemplate);
        logger.info("Making edits for template with id " + mrrtTemplate.getId());
        return new ModelAndView(new RedirectView("/openmrs/" + MRRTTemplateIndexController.INDEX_CONTROLLER));
    }

    @RequestMapping(value = MRRTTemplateCRUDFormController.VIEW_EDIT_REQUEST_MAPPING + "/view/{templateId}", method = RequestMethod.DELETE)
    public ModelAndView deleteTemplate(HttpServletRequest request, @ModelAttribute MRRTTemplate mrrtTemplate, @PathVariable Integer templateId) {
        System.out.println("We can Delete stuff now!!");
        String redirectURI = request.getContextPath() + "/" + MRRTTemplateIndexController.INDEX_CONTROLLER;
       /* EncounterService encounterService = Context.getEncounterService();
        MRRTTemplate template = getService().getById(templateId);
        Encounter encounter = encounterService.getEncounterByUuid(template.encounterUuid);
        if(encounter!=null){
            System.out.println("Encounter found. uuid below");
            System.out.println(encounter.getUuid());
        }else{
            System.out.println("Encounter not found");
        }
        encounterService.purgeEncounter(encounter);*///,"Voided by " + Context.getAuthenticatedUser() + " using Radiology FHIR Support module");
        mrrtTemplate = getService().delete(templateId);
        logger.log(Level.INFO,"MRRT Template Unregistered : " + mrrtTemplate.getName());
        return new ModelAndView(new RedirectView(redirectURI));

    }

    private MRRTTemplateService getService(){
        return Context.getService(MRRTTemplateService.class);
    }

}
