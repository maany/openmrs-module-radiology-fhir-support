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

/**
 * Created by devmaany on 18/10/16.
 */
@Controller
@RequestMapping(PatientDashboardRadiologyFHIRSupportTabPortletController.PATIENT_DASHBOARD_PORTLET_CONTROLLER)
public class PatientDashboardRadiologyFHIRSupportTabPortletController extends PortletController {
    public static final String PATIENT_DASHBOARD_PORTLET_CONTROLLER = "**/patientDashboardRadiologyFHIRSupportTab.portlet";
    private MRRTTemplateService mrrtTemplateService=null;
    private MRRTReportService mrrtReportService=null;
    private PatientService patientService=null;
    private EncounterService encounterService=null;
    @Override
    protected void populateModel(HttpServletRequest request, Map<String, Object> model) {
        initServices();

        Integer patientId = Integer.parseInt(request.getParameter("patientId"));
        Patient patient = patientService.getPatient(patientId);
        System.out.println("Patient Name : " + patient.getGivenName());
        List<Encounter> encountersByPatient = encounterService.getEncountersByPatient(patient);
        System.out.println("Enouncters by Patient : " + Arrays.toString(encountersByPatient.toArray()));
        List<MRRTReport> existingReports = new ArrayList<MRRTReport>();
        String mrrtEncounterType = Context.getMessageSourceService().getMessage("radiologyfhirsupport.encounterType");
        for(Encounter encounter: encountersByPatient){
            if(encounter.getEncounterType().getName().equals(mrrtEncounterType)) {
                MRRTReport report = mrrtReportService.getByEncounter(encounter);
                existingReports.add(report);
            }
        }
        List<MRRTTemplate> mrrtTemplates = mrrtTemplateService.getAll();
        System.out.println("templates: " + Arrays.toString(mrrtTemplates.toArray()));

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
