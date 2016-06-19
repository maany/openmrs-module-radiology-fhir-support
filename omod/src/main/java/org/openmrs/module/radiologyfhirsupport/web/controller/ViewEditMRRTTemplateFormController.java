package org.openmrs.module.radiologyfhirsupport.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

/**
 * Created by devmaany on 20/6/16.
 */
@Controller
@RequestMapping(value = ViewEditMRRTTemplateFormController.VIEW_EDIT_REQUEST_MAPPING)
public class ViewEditMRRTTemplateFormController {
    protected final Log log = LogFactory.getLog(getClass());
    public static final String VIEW_EDIT_FORM_VIEW = "/module/radiologyfhirsupport/viewEditMRRTTemplate";
    public static final String VIEW_EDIT_REQUEST_MAPPING = "module/radiologyfhirsupport/template/view";
    @RequestMapping(value = "/{templateId}", method = RequestMethod.GET)
    public String showForm(@PathVariable Integer templateId, ModelMap map) {
        MRRTTemplate mrrtTemplate = getService().getById(templateId);
        map.addAttribute("template", mrrtTemplate);
        return VIEW_EDIT_FORM_VIEW;
    }

    @RequestMapping(value = "/{templateId}", method = RequestMethod.POST)
    public String editForm(@PathVariable Integer templateId, @Valid @ModelAttribute("template") MRRTTemplate template, BindingResult errors, ModelMap map) {
        if (errors.hasErrors()) {
            //TODO return error view
            log.info("Binding errors found");
            return VIEW_EDIT_FORM_VIEW;
        }
        getService().saveOrUpdate(template);
        log.info("Making edits for template with id" + template.getId());
        return VIEW_EDIT_FORM_VIEW;
    }

    @RequestMapping(value = "/{templateId}", method = RequestMethod.DELETE)
    public ModelAndView deleteClient(HttpServletRequest request, @ModelAttribute MRRTTemplate mrrtTemplate, @PathVariable Integer templateId) {
        mrrtTemplate = getService().delete(templateId);
        //TODO send message that client is unregistered
        String redirectURI = request.getContextPath() + "/" + MRRTIndexController.INDEX_CONTROLLER;
        log.info("MRRT Template Unregistered : " + mrrtTemplate.getName());
        return new ModelAndView(new RedirectView(redirectURI));
    }
    private MRRTTemplateService getService(){
        return Context.getService(MRRTTemplateService.class);
    }
}
