package org.openmrs.module.radiologyfhirsupport.api.handler;

import ca.uhn.fhir.model.dstu2.resource.DiagnosticReport;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.module.fhir.api.diagnosticreport.DiagnosticReportHandler;
import org.openmrs.module.radiologyfhirsupport.MRRTReport;
import org.openmrs.module.radiologyfhirsupport.MRRTTemplate;
import org.openmrs.module.radiologyfhirsupport.api.MRRTReportService;
import org.openmrs.module.radiologyfhirsupport.api.MRRTTemplateService;
import org.openmrs.module.radiologyfhirsupport.api.MRRTToFHIRService;
import org.openmrs.obs.handler.AbstractHandler;

import java.util.List;

/**
 * Created by devmaany on 24/2/17.
 */
public class MRRTReportHandler extends AbstractHandler implements DiagnosticReportHandler {
    private static final String ServiceCategory = "RAD-MRRT";
    @Override
    public String getServiceCategory() {
        return ServiceCategory;
    }

    @Override
    public DiagnosticReport getFHIRDiagnosticReportById(String id) {
        MRRTReportService mrrtReportService = Context.getService(MRRTReportService.class);
        MRRTReport report = mrrtReportService.getByEncounterUUID(id);
        if(report==null) {
            String templateNotFoundError = Context.getMessageSourceService().getMessage("radiologyfhirsupport.templateNotFoundError");
            throw new APIException(templateNotFoundError);
        }
        /*TODOne get Map*/
        MRRTToFHIRService mrrtToFHIRService = Context.getService(MRRTToFHIRService.class);
        DiagnosticReport diagnosticReport = Context.getService(MRRTToFHIRService.class).convertMRRTToFHIRViaXPath(report, mrrtToFHIRService.getDefaultMapping());
        return diagnosticReport;
    }

    @Override
    public List<DiagnosticReport> getFHIRDiagnosticReportBySubjectName(String s) {
        return null;
    }

    @Override
    public DiagnosticReport saveFHIRDiagnosticReport(DiagnosticReport diagnosticReport) {
        return null;
    }

    @Override
    public DiagnosticReport updateFHIRDiagnosticReport(DiagnosticReport diagnosticReport, String s) {
        return null;
    }

    @Override
    public void retireFHIRDiagnosticReport(String s) {

    }
}
