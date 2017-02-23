package org.openmrs.module.radiologyfhirsupport.web.controller;

import org.openmrs.*;
import org.openmrs.api.context.Context;
import org.openmrs.messagesource.MessageSourceService;
import org.openmrs.module.radiologyfhirsupport.MRRTReport;
import org.openmrs.module.radiologyfhirsupport.MRRTTemplate;
import org.openmrs.module.radiologyfhirsupport.api.MRRTReportService;
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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by devmaany on 13/2/17.
 */

@Controller
public class MRRTReportRUDFormController {
    public static final String VIEW_EDIT_REQUEST_MAPPING = "module/radiologyfhirsupport/report/";
    protected final Logger logger = Logger.getLogger(MRRTReportRUDFormController.class.getName());
    @RequestMapping(value = MRRTReportRUDFormController.VIEW_EDIT_REQUEST_MAPPING + "view/{reportId}.form", method = RequestMethod.GET)
    public ModelAndView viewEdit(@PathVariable Integer reportId, ModelMap map, HttpServletRequest request){
        MRRTReport report = Context.getService(MRRTReportService.class).getById(reportId);
        List<Location> locations = Context.getLocationService().getAllLocations();
        String xml = "";
        try {
            xml = Context.getService(MRRTTemplateService.class).clobToString(report.getXml());
            xml = xml.replaceAll("script","script_mrrt");
            xml = xml.replaceAll("(\\r|\\n|\\r\\n)+", "\\\\n");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Date Saved was " + report.encounter.getEncounterDatetime() + " Millis " + report.encounter.getEncounterDatetime().getTime());
        map.put("report",report);
        map.put("locations", locations);
        map.put("xml",xml);
        String deleteRedirectURL = request.getContextPath() + "/" + "patientDashboard.form?patientId=" + report.getEncounter().getPatient().getId();
        map.put("deleteRedirectURL",deleteRedirectURL);
        return new ModelAndView("module/radiologyfhirsupport/viewEditReport");
    }
    @RequestMapping(value = MRRTReportRUDFormController.VIEW_EDIT_REQUEST_MAPPING + "view/{reportId}.form", method = RequestMethod.POST)
    public ModelAndView saveReport(HttpServletRequest request, @PathVariable Integer reportId,@RequestParam String name, @RequestParam String xml, @RequestParam Integer locationId, @RequestParam String dateString, ModelMap map) throws ParseException {
        logger.info("Saving Report");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd'T'HH:mm");
        dateFormat.setTimeZone(Calendar.getInstance().getTimeZone());
        Date date = dateFormat.parse(dateString);
        logger.info("Date Received " + date + " TimeZone " + Calendar.getInstance().getTimeZone().getDisplayName() + " Millis " + date.getTime());

        /* XML modifications */
        xml = xml.replaceAll("script_mrrt","script");
        xml = xml.replaceAll("\\\\n",System.lineSeparator());

        // Update MRRT Report
        MRRTReport report=null;
        try {
            MRRTReportService mrrtReportService = Context.getService(MRRTReportService.class);
            report = mrrtReportService.getById(reportId);
            report.setName(name);
            report.setXml(Context.getService(MRRTTemplateService.class).stringToClob(xml));
            report.getEncounter().setLocation(Context.getLocationService().getLocation(locationId));
            report.getEncounter().setEncounterDatetime(date);
            report.setDateChanged(new Date());
            report.setChangedBy(Context.getAuthenticatedUser());
            mrrtReportService.saveOrUpdate(report);
            map.put("message","Report updated successfully");
        } catch (SQLException e) {
            e.printStackTrace();
            map.put("message", "Error saving the report.Please contact the developer");
        }
//        String redirectURL = request.getContextPath() + "/" + MRRTReportRUDFormController.VIEW_EDIT_REQUEST_MAPPING + "view/" + report.getId() + ".form";
        String redirectURL = request.getContextPath() + "/" + "patientDashboard.form?patientId="+ report.getEncounter().getPatient().getId();
        return new ModelAndView(new RedirectView(redirectURL));
    }
    @RequestMapping(value = MRRTReportRUDFormController.VIEW_EDIT_REQUEST_MAPPING + "view/{reportId}.form", method = RequestMethod.DELETE)
    public ModelAndView deleteReport(HttpServletRequest request,@PathVariable Integer reportId,ModelMap map){
        MRRTReport report = Context.getService(MRRTReportService.class).getById(reportId);
        String redirectURL = request.getContextPath() + "/" + "patientDashboard.form?patientId=" + report.getEncounter().getPatient().getId();
        Context.getService(MRRTReportService.class).delete(report);
        logger.info("Deleted Report ");
        return new ModelAndView(new RedirectView(redirectURL));
    }
}
