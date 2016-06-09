package org.openmrs.module.radiologyfhirsupport.api.util;

import ca.uhn.fhir.model.dstu2.resource.DiagnosticReport;
import ca.uhn.fhir.model.primitive.IdDt;

/**
 * Manages @{@link DiagnosticReport} creation from values provided by @{@link org.openmrs.module.radiologyfhirsupport.MRRTTemplate}
 * Created by devmaany on 9/6/16.
 */
public class DiagnosticReportMRRTAdapter {
    private DiagnosticReport diagnosticReport;

    public DiagnosticReportMRRTAdapter(DiagnosticReport diagnosticReport) {
        this.diagnosticReport = diagnosticReport;
    }

    public void setId(String encounterUuid){
        diagnosticReport.setId(new IdDt("DiagnosticReport",encounterUuid));
    }

    public void setStatus(String status){
    }
    public void setServiceCategory(){

    }
    public DiagnosticReport getDiagnosticReport(){
        return diagnosticReport;
    }
}
