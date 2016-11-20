package org.openmrs.module.radiologyfhirsupport.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Encounter;
import org.openmrs.api.EncounterService;
import org.openmrs.api.context.Context;
import org.openmrs.module.radiologyfhirsupport.MRRTTemplate;
import org.openmrs.module.radiologyfhirsupport.api.MRRTTemplateService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by devmaany on 19/6/16.
 */
@Controller
@RequestMapping(value = MRRTTemplateIndexController.INDEX_CONTROLLER)
public class MRRTTemplateIndexController {
    protected final Log log = LogFactory.getLog(getClass());
    private static final String INDEX_VIEW = "/module/radiologyfhirsupport/index";
    public static final String INDEX_CONTROLLER = "module/radiologyfhirsupport/index.form";
    private EncounterService encounterService;
    @RequestMapping(method = RequestMethod.GET,value = MRRTTemplateIndexController.INDEX_VIEW)
    public String showList() {
        return INDEX_VIEW;
    }

    @ModelAttribute("templateMap")
    public Map<MRRTTemplate,Encounter> getRegisteredClients() {
        Map<MRRTTemplate,Encounter> templateMap = new HashMap<MRRTTemplate, Encounter>();
        MRRTTemplateService mrrtTemplateService = Context.getService(MRRTTemplateService.class);
        EncounterService encounterService = Context.getEncounterService();
        List<MRRTTemplate> templates = mrrtTemplateService.getAll();
        for(MRRTTemplate template: templates){
            Encounter encounter= encounterService.getEncounterByUuid(template.getEncounterUuid());
            templateMap.put(template,encounter);
        }

        return templateMap;
    }



}
