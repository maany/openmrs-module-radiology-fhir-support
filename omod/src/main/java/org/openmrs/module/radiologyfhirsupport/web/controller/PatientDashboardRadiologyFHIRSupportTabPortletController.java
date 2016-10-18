package org.openmrs.module.radiologyfhirsupport.web.controller;

import org.openmrs.api.context.Context;
import org.openmrs.module.radiologyfhirsupport.MRRTTemplate;
import org.openmrs.module.radiologyfhirsupport.api.MRRTTemplateService;
import org.openmrs.web.controller.PortletController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by devmaany on 18/10/16.
 */
@Controller
@RequestMapping(PatientDashboardRadiologyFHIRSupportTabPortletController.PATIENT_DASHBOARD_PORTLET_CONTROLLER)
public class PatientDashboardRadiologyFHIRSupportTabPortletController extends PortletController {
    public static final String PATIENT_DASHBOARD_PORTLET_CONTROLLER = "**/patientDashboardRadiologyFHIRSupportTab.portlet";
    private MRRTTemplateService mrrtTemplateService=null;
    @Override
    protected void populateModel(HttpServletRequest request, Map<String, Object> model) {
        if(mrrtTemplateService==null)
            mrrtTemplateService = Context.getService(MRRTTemplateService.class);
        List<MRRTTemplate> mrrtTemplates = mrrtTemplateService.getAll();
        model.put("availableTemplates",mrrtTemplates);


    }
}
