package org.openmrs.module.radiologyfhirsupport.web.controller;

import org.openmrs.*;
import org.openmrs.api.context.Context;
import org.openmrs.logic.op.In;
import org.openmrs.messagesource.MessageSourceService;
import org.openmrs.module.radiologyfhirsupport.MRRTReport;
import org.openmrs.module.radiologyfhirsupport.MRRTTemplate;
import org.openmrs.module.radiologyfhirsupport.api.MRRTReportService;
import org.openmrs.module.radiologyfhirsupport.api.MRRTTemplateService;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static org.openmrs.api.context.Context.getEncounterService;

/**
 * Created by devmaany on 31/10/16.
 */
@Controller
public class MRRTReportCreationController {
    @RequestMapping(value = "/module/radiologyfhirsupport/report/new.form", method = RequestMethod.GET)
    public ModelAndView getForm(ModelMap map, @RequestParam Integer templateId, @RequestParam Integer patientId){
        MRRTReport report = new MRRTReport();
        MRRTTemplateService mrrtTemplateService = Context.getService(MRRTTemplateService.class);
        MRRTTemplate mrrtTemplate = mrrtTemplateService.getById(templateId);
        String xml = "";
        try {
            xml = mrrtTemplateService.clobToString(mrrtTemplate.getXml());
            xml = xml.replaceAll("script","script_mrrt");
            xml = xml.replaceAll("(\\r|\\n|\\r\\n)+", "\\\\n");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        map.put("report",report);
        map.put("template", mrrtTemplate);
        map.put("xml",xml );
        map.put("locations",Context.getLocationService().getAllLocations());
        System.out.println("This is the report creation controller");
        return new ModelAndView("module/radiologyfhirsupport/create_report");
    }
    @RequestMapping(value = "/module/radiologyfhirsupport/report/new.form", method = RequestMethod.POST)
    public ModelAndView saveReport(HttpServletRequest request, @RequestParam String name,@RequestParam String xml,@RequestParam Integer templateId, @RequestParam Integer patientId, @RequestParam Integer locationId, @RequestParam String dateString, ModelMap map) throws ParseException {
        /* Set MRRT Template */
        MRRTTemplate mrrtTemplate = Context.getService(MRRTTemplateService.class).getById(templateId);

        Patient patient = Context.getPatientService().getPatient(patientId);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd'T'HH:mm");
        dateFormat.setTimeZone(Calendar.getInstance().getTimeZone());
        Date date = dateFormat.parse(dateString);
        System.out.println("Date Received " + date + " TimeZone " + Calendar.getInstance().getTimeZone().getDisplayName() + " Millis " + date.getTime());
        Location location = Context.getLocationService().getLocation(locationId);
        //*****Create Encounter*********//
        Encounter encounter = new Encounter();
        encounter.setPatient(patient);
        encounter.setLocation(location);
        encounter.setEncounterDatetime(date);
        MessageSourceService messageSourceService = Context.getMessageSourceService();
        String encounterTypeNameString = messageSourceService.getMessage("radiologyfhirsupport.encounterType");
        String providerIdentifier = messageSourceService.getMessage("radiologyfhirsupport.providerIdentifier");
        String encounterRoleName = messageSourceService.getMessage("radiologyfhirsupport.encounterRoleName");
        encounter.setEncounterType(Context.getEncounterService().getEncounterType(encounterTypeNameString));
        EncounterRole encounterRole = Context.getEncounterService().getEncounterRoleByName(encounterRoleName);
        Provider provider = Context.getProviderService().getProviderByIdentifier(providerIdentifier);
        encounter.setProvider(encounterRole, provider);
        /* XML modifications */
        xml = xml.replaceAll("script_mrrt","script");
        xml = xml.replaceAll("\\\\n",System.lineSeparator());
        System.out.println(" XML Received : " + xml);
        MRRTReport report = new MRRTReport();
        // Create MRRT Report
        try {
            report.setEncounter(encounter);
            report.setXml(Context.getService(MRRTTemplateService.class).stringToClob(xml));
            report.setMrrtTemplate(mrrtTemplate);
            report.setName(name);
            report.setCreator(Context.getAuthenticatedUser());
            report.setDateCreated(new Date());
            Context.getService(MRRTReportService.class).create(report);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String redirectURL = request.getContextPath() + "/" + MRRTReportRUDFormController.VIEW_EDIT_REQUEST_MAPPING + "view/" + report.getId() + ".form";
        return new ModelAndView(new RedirectView(redirectURL));
    }
}
