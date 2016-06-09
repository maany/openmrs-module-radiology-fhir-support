package org.openmrs.module.radiologyfhirsupport.api.impl;

import ca.uhn.fhir.model.dstu2.resource.DiagnosticReport;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.module.radiologyfhirsupport.MRRTTemplate;
import org.openmrs.module.radiologyfhirsupport.api.MRRTTemplateService;
import org.openmrs.module.radiologyfhirsupport.api.MRRTToFHIRService;
import org.openmrs.module.radiologyfhirsupport.api.util.XPathMapper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by devmaany on 8/6/16.
 */
public class MRRTToFHIRServiceImpl implements MRRTToFHIRService {
//    Map<String,String> xPathMappings;
    @Override
    public void useDefaultMappings() {

    }

    @Override
    public void setMapping(Map<String, String> mapping) {

    }

    @Override
    public DiagnosticReport convertMRRTToFHIRDiagnosticReport(MRRTTemplate template) {
        /* Get MRRTTemplate and extract the xml representation */
        MRRTTemplateService mrrtTemplateService = Context.getService(MRRTTemplateService.class);
        String xml = null;
        try {
            xml = mrrtTemplateService.clobToString(template.getXml());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String xmlConversionError = Context.getMessageSourceService().getMessage("radiologyfhirsupport.xmlConversionError");
        if(xml==null )
            throw new APIException(xmlConversionError);
        /* TODO Get XPath Mappings*/
        Map<String, String> xPathMappings = null;
        XPathMapper xPathMapper = new XPathMapper(xml,xPathMappings);
        DiagnosticReport diagnosticReport = xPathMapper.getDiagnosticReport();

        return diagnosticReport;
    }


}
