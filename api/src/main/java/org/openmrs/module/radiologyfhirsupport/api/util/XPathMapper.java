package org.openmrs.module.radiologyfhirsupport.api.util;

import ca.uhn.fhir.model.dstu2.resource.DiagnosticReport;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;

/**
 * Created by devmaany on 9/6/16.
 */
public class XPathMapper {
    private Map<String,String> xPathMappings;
    private String xml;
    public XPathMapper(String xml, Map<String, String> xPathMappings) {
        this.xPathMappings = xPathMappings;
        this.xml = xml;
    }

    public DiagnosticReport getDiagnosticReport() {
        DiagnosticReport diagnosticReport = new DiagnosticReport();
        diagnosticReport = parseXMLAndMapFields(diagnosticReport);
        return diagnosticReport;
    }

    /**
     * This method decides how to perform the Mapping
     * @param diagnosticReport
     * @return
     */
    private DiagnosticReport parseXMLAndMapFields(DiagnosticReport diagnosticReport){

        Document document = loadDomFromXml(xml);
        DiagnosticReportMRRTAdapter adapter = new DiagnosticReportMRRTAdapter(diagnosticReport);
        for(Map.Entry<String,String> xPathMapping :xPathMappings.entrySet()){
            String mrrtTemplateFieldName= xPathMapping.getKey(); // //html/head/title
            String fhirDiagnosticReportFieldName = xPathMapping.getValue(); // title
            DiagnosticReportMRRTAdapter diagnosticReportMRRTAdapter = new DiagnosticReportMRRTAdapter(diagnosticReport);
            if (fhirDiagnosticReportFieldName.equals("id")) {
                String encounterUuid = getInnerHTML(document, mrrtTemplateFieldName);
                diagnosticReportMRRTAdapter.setId(encounterUuid);
            }
        }

        return adapter.getDiagnosticReport;
    }
    private Document loadDomFromXml(String xml){
        SAXReader saxReader = new SAXReader();
        InputStream xmlStream = new ByteArrayInputStream(xml.getBytes());
        Document document = null;

        try {
            document = saxReader.read(xmlStream);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return document;
    }
    private String getInnerHTML(Document document, String xPath){
        Node element = document.selectSingleNode(xPath);
        return element.getText();
    }


}
