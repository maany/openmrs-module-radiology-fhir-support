package org.openmrs.module.radiologyfhirsupport.api;

import ca.uhn.fhir.model.dstu2.resource.DiagnosticReport;
import org.openmrs.module.radiologyfhirsupport.MRRTTemplate;

import java.util.Map;

/**
 * Created by devmaany on 8/6/16.
 */
public interface MRRTToFHIRService {
    void useDefaultMappings();
    void setMapping(Map<String,String> mapping);
    DiagnosticReport convertMRRTToFHIRDiagnosticReport(MRRTTemplate template);
}
