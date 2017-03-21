package org.openmrs.module.radiologyfhirsupport.web.controller;

import org.openmrs.api.context.Context;
import org.openmrs.module.radiologyfhirsupport.MRRTReport;
import org.openmrs.module.radiologyfhirsupport.MRRTTemplate;
import org.openmrs.module.radiologyfhirsupport.api.MRRTReportService;
import org.openmrs.module.radiologyfhirsupport.api.MRRTTemplateService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by devmaany on 20/3/17.
 */
@Controller
public class ManageController {
    @RequestMapping(value = "/module/radiologyfhirsupport/manage.form", method = RequestMethod.GET)
    public ModelAndView showData(ModelMap map){
        map.put("message","hi");
        MRRTTemplateService mrrtTemplateService = Context.getService(MRRTTemplateService.class);
        List<MRRTTemplate> templates = mrrtTemplateService.getAll();
        List<MRRTTemplate> voidTemplates = new ArrayList<MRRTTemplate>();
        List<List<MRRTReport>> reports = new ArrayList<List<MRRTReport>>();
        MRRTReportService mrrtReportService = Context.getService(MRRTReportService.class);
        for(MRRTTemplate template : templates){
            if(template.getVoided()) {
                voidTemplates.add(template);
                List<MRRTReport> reportList = mrrtReportService.getByTemplate(template);
                reports.add(reportList);
            }
        }
        for(MRRTTemplate template: voidTemplates){
            System.out.println("Void Template : " + template.getName());
        }
        for(List<MRRTReport> report: reports){
            System.out.println("Size : " + report.size());
            for(MRRTReport singleReport : report){
                System.out.println(" Reports: " + singleReport.getName());
            }
        }
        map.put("voidTemplates",voidTemplates);
        map.put("reports",reports);
        return new ModelAndView("module/radiologyfhirsupport/manage");
    }
}
