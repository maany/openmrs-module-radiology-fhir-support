package org.openmrs.module.radiologyfhirsupport.api.util;

import ca.uhn.fhir.model.dstu2.resource.DiagnosticReport;
import org.dom4j.*;
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
    private String encounterUuid;
    public XPathMapper(String id, String xml, Map<String, String> xPathMappings) {
        this.xPathMappings = xPathMappings;
        this.xml = xml;
        encounterUuid = id;
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
                diagnosticReportMRRTAdapter.setId(encounterUuid);
            }

            if(fhirDiagnosticReportFieldName.equals("status")){
                String mrrtStatus = getInnerHTML(document,mrrtTemplateFieldName);
                diagnosticReportMRRTAdapter.setStatus(mrrtStatus);
            }
            /* Choose a value from here */
            if(fhirDiagnosticReportFieldName.equals("category")){
                String serviceCategory = getInnerHTML(document,mrrtTemplateFieldName);
                diagnosticReportMRRTAdapter.setServiceCategory(serviceCategory);
            }
        }

        return adapter.getDiagnosticReport();
    }
    private Document loadDomFromXml(String xml){
        SAXReader saxReader = new SAXReader();
        saxReader.setIgnoreComments(false);
        InputStream xmlStream = new ByteArrayInputStream(xml.getBytes());
        Document document = null;

        try {
            document = saxReader.read(xmlStream);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        unCommentAndPushTemplateAttributes(document);
        return document;
    }
    private void unCommentAndPushTemplateAttributes(Document doc){
        Node node = doc.selectSingleNode("//html/head/script/comment()");

        if (node!=null && node.getText().contains("template_attributes")) {
            Element parent = node.getParent();
            String content = node.asXML();
            content = content.trim().replaceFirst("<!--","").trim();
            int endIndex = content.lastIndexOf("-->");
            content = content.substring(0,endIndex).trim();
            node.detach();
            Document subDoc = loadDomFromXml(content);
            parent.add(subDoc.getRootElement());
        }
    }
    private String getInnerHTML(Document document, String xPath){
        Node element = document.selectSingleNode(xPath);
        return element.getText();
    }


}
