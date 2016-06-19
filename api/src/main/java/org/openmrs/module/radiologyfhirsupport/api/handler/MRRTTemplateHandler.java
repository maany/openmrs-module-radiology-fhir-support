package org.openmrs.module.radiologyfhirsupport.api.handler;

import ca.uhn.fhir.model.dstu2.resource.DiagnosticReport;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.messagesource.MessageSourceService;
import org.openmrs.module.fhir.api.diagnosticreport.DiagnosticReportHandler;
import org.openmrs.module.fhir.api.diagnosticreport.handler.AbstractHandler;
import org.openmrs.module.radiologyfhirsupport.MRRTTemplate;
import org.openmrs.module.radiologyfhirsupport.api.MRRTTemplateService;
import org.openmrs.module.radiologyfhirsupport.api.MRRTToFHIRService;

import java.util.List;
import java.util.Map;

/**
 * Created by devmaany on 31/5/16.
 */
public class MRRTTemplateHandler extends AbstractHandler implements DiagnosticReportHandler {
    private static final String ServiceCategory = "RAD-MRRT";
    @Override
    public String getServiceCategory() {
        return ServiceCategory;
    }

    @Override
    public DiagnosticReport getFHIRDiagnosticReportById(String id) {
        MRRTTemplateService mrrtTemplateService = Context.getService(MRRTTemplateService.class);
        MRRTTemplate mrrtTemplate = mrrtTemplateService.getByEncounterUUID(id);
        if(mrrtTemplate==null) {
            String templateNotFoundError = Context.getMessageSourceService().getMessage("radiologyfhirsupport.templateNotFoundError");
            throw new APIException(templateNotFoundError);
        }
        /*TODOne get Map*/
        MRRTToFHIRService mrrtToFHIRService = Context.getService(MRRTToFHIRService.class);
        DiagnosticReport diagnosticReport = Context.getService(MRRTToFHIRService.class).convertMRRTToFHIRViaXPath(mrrtTemplate, mrrtToFHIRService.getDefaultMapping());
        return diagnosticReport;
    }

    @Override
    public List<DiagnosticReport> getFHIRDiagnosticReportBySubjectName(String name) {
        //TODO think on how to implement this function
        return null;
    }

    @Override
    public DiagnosticReport saveFHIRDiagnosticReport(DiagnosticReport diagnosticReport) {
        String saveDiagnosticReportError = Context.getService(MessageSourceService.class).getMessage("radiologyfhirsupport.saveDiagnosticReportError");
        throw new APIException(saveDiagnosticReportError);
    }

    @Override
    public DiagnosticReport updateFHIRDiagnosticReport(DiagnosticReport diagnosticReport, String s) {
        String saveDiagnosticReportError = Context.getService(MessageSourceService.class).getMessage("radiologyfhirsupport.saveDiagnosticReportError");
        throw new APIException(saveDiagnosticReportError);
    }

    @Override
    public void retireFHIRDiagnosticReport(String s) {
        String saveDiagnosticReportError = Context.getService(MessageSourceService.class).getMessage("radiologyfhirsupport.saveDiagnosticReportError");
        throw new APIException(saveDiagnosticReportError);
    }

}
