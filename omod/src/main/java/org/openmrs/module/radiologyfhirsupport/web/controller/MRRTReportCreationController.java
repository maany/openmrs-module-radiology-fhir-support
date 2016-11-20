package org.openmrs.module.radiologyfhirsupport.web.controller;

import org.openmrs.*;
import org.openmrs.api.context.Context;
import org.openmrs.logic.op.In;
import org.openmrs.module.radiologyfhirsupport.MRRTReport;
import org.openmrs.module.radiologyfhirsupport.MRRTTemplate;
import org.openmrs.module.radiologyfhirsupport.api.MRRTTemplateService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;

import static org.openmrs.api.context.Context.getEncounterService;

/**
 * Created by devmaany on 31/10/16.
 */
@Controller
public class MRRTReportCreationController {
    @RequestMapping(value = "/module/radiologyfhirsupport/report/new.form", method = RequestMethod.GET)
    public ModelAndView getForm(ModelMap map, @RequestParam Integer templateId){
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
        System.out.println("This is the report creation controller");
        return new ModelAndView("module/radiologyfhirsupport/create_report");
    }
    @RequestMapping(value = "/module/radiologyfhirsupport/report/new.form", method = RequestMethod.POST)
    public ModelAndView saveReport(HttpServletRequest request, @RequestParam Integer templateId, @RequestParam Integer patientId, @RequestParam Integer locationId, @RequestParam Date date, ModelMap map) {
        MRRTTemplate mrrtTemplate = Context.getService(MRRTTemplateService.class).getById(templateId);
        Patient patient = Context.getPatientService().getPatient(patientId);
        //Location location = Context.getLocationService().getLocation(locationId);
        //*****Create Encounter*********//


        // Create MRRT Report
        /*MRRTReport report = new MRRTReport();
        report.setEncounter(encounter);
        report.setXml(mrrtTemplate.getXml());
        report.setMrrtTemplate(mrrtTemplate);
        mrrtTemplate.setName(name);
        MRRTTemplateService mrrtTemplateService = Context.getService(MRRTTemplateService.class);
        mrrtTemplate.setXml(mrrtTemplateService.stringToClob(xml));
        System.out.println("Data received : " + mrrtTemplate.getName());
        System.out.println("Data received : " + mrrtTemplate.getXml());
        mrrtTemplateService.create(mrrtTemplate);
        String redirectURL = request.getContextPath() + "/" + MRRTTemplateCRUDFormController.VIEW_EDIT_REQUEST_MAPPING + "view/" + mrrtTemplate.getId() + ".form";
        */
        return new ModelAndView(new RedirectView(""));
    }
}
