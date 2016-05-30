package org.openmrs.module.radiologyfhirsupport.api.handler;

import ca.uhn.fhir.model.dstu2.resource.DiagnosticReport;
import org.openmrs.module.fhir.api.diagnosticreport.DiagnosticReportHandler;
import org.openmrs.module.fhir.api.diagnosticreport.handler.AbstractHandler;

import java.util.List;

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
        return null;
    }

    @Override
    public List<DiagnosticReport> getFHIRDiagnosticReportBySubjectName(String name) {
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
