package org.openmrs.module.radiologyfhirsupport.api.util;

import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu2.resource.DiagnosticReport;
import ca.uhn.fhir.model.dstu2.resource.Observation;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.valueset.DiagnosticReportStatusEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.StringDt;
import org.dom4j.Document;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.openmrs.module.fhir.api.util.FHIRPatientUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manages @{@link DiagnosticReport} creation from values provided by @{@link org.openmrs.module.radiologyfhirsupport.MRRTTemplate}
 * Created by devmaany on 9/6/16.
 */
public class DiagnosticReportMRRTAdapter {
    private DiagnosticReport diagnosticReport;
    private static Logger logger = Logger.getLogger(DiagnosticReportMRRTAdapter.class.getName());
    public DiagnosticReportMRRTAdapter(DiagnosticReport diagnosticReport) {
        this.diagnosticReport = diagnosticReport;
    }

    /**
     * The local ID assigned to the report by the order filler, usually by the Information System of the diagnostic service provider.
     * This is a business identifer, not a resource identifier
     * Using encounter uuid as id because FHIR module's @{@link org.openmrs.module.fhir.api.diagnosticreport.handler.RadiologyHandler} does the same.
     * This way Resources will have uniform values across OpenMRS
     * @param encounterUuid
     */
    public void setId(String encounterUuid){
        diagnosticReport.setId(new IdDt("DiagnosticReport",encounterUuid));
    }

    /**
     * The status of the diagnostic report as a whole
     * Acceptable values : registered | partial | final | corrected | appended | cancelled | entered-in-error
     * Read more : https://www.hl7.org/fhir/valueset-diagnostic-report-status.html
     * @param status
     */
    public void setStatus(String status){
        if(status.equals("DRAFT")){
            diagnosticReport.setStatus(DiagnosticReportStatusEnum.PARTIAL);
        }else if(status.equals("ACTIVE")){
            diagnosticReport.setStatus(DiagnosticReportStatusEnum.FINAL);
        }else if(status.equals("RETIRED")){
            diagnosticReport.setStatus(DiagnosticReportStatusEnum.CANCELLED);
        }else{
            diagnosticReport.setStatus(DiagnosticReportStatusEnum.ENTERED_IN_ERROR);
        }

    }

    /**
     * A code that classifies the clinical discipline, department or diagnostic service that created the report (e.g. cardiology, biochemistry, hematology, MRI).
     * Acceptable  values : http://hl7.org/fhir/v2/0074/index.html
     * @param serviceCategory
     */
    public void setServiceCategory(String serviceCategory){
        String matchString = serviceCategory.toLowerCase();
        if(matchString.contains("ct")){
            serviceCategory = "CT";
        }else if(matchString.contains("xray")){
            serviceCategory="RAD";
        }else {
            serviceCategory = "RAD";
        }
        List<CodingDt> serviceCategoryList = new ArrayList<CodingDt>();
        serviceCategoryList.add(new CodingDt("http://hl7.org/fhir/v2/0074",serviceCategory));
        diagnosticReport.getServiceCategory().setCoding(serviceCategoryList);
    }


    public void setEncounter(){
        diagnosticReport.setEncounter(null);
    }
    public void setPerformer(){

    }

    /**
     * from RadLex.xls, codes for
     * Patient Identifier = RID13159
     * Patient Name = RID13160
     * If patient identifier is OpenMRS patient uuid, then mapping is successful
     */
    public void setSubject(Document document,String radLexCode){
        RadLexUtil radLexUtil = new RadLexUtil(document);
        String patientIdentifier = radLexUtil.getBodyCodedContent(radLexCode);
        PatientService patientService = Context.getPatientService();
        org.openmrs.Patient patient = patientService.getPatientByUuid(patientIdentifier);
        if(patient==null){
            logger.log(Level.SEVERE,"No Patient with uuid = null found in OpenMRS records");
            return;
        }
        Patient fhirPatient = FHIRPatientUtil.generatePatient(patient);
        diagnosticReport.getSubject().setResource(fhirPatient);
        /*TODO create a patient with whatever details available */
    }
    public void setIssued(){

    }

    /**
     * TODO A LOOOOOOOTTTT of processing can be done here, create subMethods based on type of observations later
     */
    public void setRadlexResults(Document document){
        List<ResourceReferenceDt> resultReferenceDtList = new ArrayList<ResourceReferenceDt>();

        RadLexUtil radLexUtil = new RadLexUtil(document);
        Map<String,String> radLexCodes = radLexUtil.getRadLexCodes();
        for(String code:radLexCodes.keySet()){
            String observationString = radLexUtil.getBodyCodedContent(code);
            Observation observation = getObservationFromString("RadLex",code,radLexCodes.get(code),observationString);
            resultReferenceDtList.add(new ResourceReferenceDt(observation));
        }

        if (!resultReferenceDtList.isEmpty()) {
            diagnosticReport.setResult(resultReferenceDtList);
        }
    }

    /**
     * Helper method for @setRadlexResults
     * @param codingScheme
     * @param code
     * @param resultName
     * @param resultValue
     */
    private Observation getObservationFromString(String codingScheme, String code, String resultName, String resultValue){

        Observation observation = new Observation();
        CodingDt coding = observation.getCode().addCoding();
        coding.setCode(code).setSystem(codingScheme).setDisplay(resultName);

        // Create a quantity datatype
        observation.setValue(new StringDt(resultValue));
        return observation;
    }
    public void setConclusion(){

    }
    public void setImage(){

    }

    public DiagnosticReport getDiagnosticReport(){
        return diagnosticReport;
    }
}
