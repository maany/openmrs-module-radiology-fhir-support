package org.openmrs.module.radiologyfhirsupport.web.controller;

import org.openmrs.Encounter;
import org.openmrs.Patient;
import org.openmrs.api.EncounterService;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.openmrs.module.radiologyfhirsupport.MRRTReport;
import org.openmrs.module.radiologyfhirsupport.MRRTTemplate;
import org.openmrs.module.radiologyfhirsupport.api.MRRTReportService;
import org.openmrs.module.radiologyfhirsupport.api.MRRTTemplateService;
import org.openmrs.web.controller.PortletController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by devmaany on 18/10/16.
 */
@Controller
@RequestMapping(PatientDashboardRadiologyFHIRSupportTabPortletController.PATIENT_DASHBOARD_PORTLET_CONTROLLER)
public class PatientDashboardRadiologyFHIRSupportTabPortletController extends PortletController {
    public static final String PATIENT_DASHBOARD_PORTLET_CONTROLLER = "**/patientDashboardRadiologyFHIRSupportTab.portlet";
    protected final Logger logger = Logger.getLogger(PatientDashboardRadiologyFHIRSupportTabPortletController.class.getName());
    private MRRTTemplateService mrrtTemplateService=null;
    private MRRTReportService mrrtReportService=null;
    private PatientService patientService=null;
    private EncounterService encounterService=null;
    @Override
    protected void populateModel(HttpServletRequest request, Map<String, Object> model) {
        initServices();

        Integer patientId = Integer.parseInt(request.getParameter("patientId"));
        Patient patient = patientService.getPatient(patientId);
        logger.info("Patient Name : " + patient.getGivenName());
        List<Encounter> encountersByPatient = encounterService.getEncountersByPatient(patient);
        List<MRRTReport> existingReports = null;
        List<MRRTTemplate> mrrtTemplates = null;
        try {
            mrrtTemplates = mrrtTemplateService.getAll();
//            System.out.println("templates: " + Arrays.toString(mrrtTemplates.toArray()));
        }catch(Exception ex){
            ex.printStackTrace();
        }
        existingReports = mrrtReportService.getByPatientId(patientId);
        model.put("reports",existingReports);
        model.put("templates",mrrtTemplates);
        model.put("patient",patient);
        model.put("encounters",encountersByPatient);
    }
    private void initServices(){
        if(mrrtTemplateService==null)
            mrrtTemplateService = Context.getService(MRRTTemplateService.class);
        if(mrrtReportService==null)
            mrrtReportService = Context.getService(MRRTReportService.class);
        if(patientService==null)
            patientService= Context.getPatientService();
        if(encounterService==null)
            encounterService=Context.getEncounterService();
    }
}
