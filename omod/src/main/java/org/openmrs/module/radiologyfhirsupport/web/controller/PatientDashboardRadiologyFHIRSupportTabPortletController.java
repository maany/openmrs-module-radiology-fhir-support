package org.openmrs.module.radiologyfhirsupport.web.controller;

import org.openmrs.web.controller.PortletController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by devmaany on 18/10/16.
 */
@Controller
@RequestMapping(PatientDashboardRadiologyFHIRSupportTabPortletController.PATIENT_DASHBOARD_PORTLET_CONTROLLER)
public class PatientDashboardRadiologyFHIRSupportTabPortletController extends PortletController {
    public static final String PATIENT_DASHBOARD_PORTLET_CONTROLLER = "**/patientDashboardRadiologyFHIRSupportTab.portlet";
    @Override
    protected void populateModel(HttpServletRequest request, Map<String, Object> model) {
        model.put("message","Yo ho Yo ho a pirates life for me!");
    }
}
