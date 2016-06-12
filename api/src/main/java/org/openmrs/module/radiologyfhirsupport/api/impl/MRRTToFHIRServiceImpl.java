package org.openmrs.module.radiologyfhirsupport.api.impl;

import ca.uhn.fhir.model.dstu2.resource.DiagnosticReport;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.module.radiologyfhirsupport.MRRTTemplate;
import org.openmrs.module.radiologyfhirsupport.api.MRRTTemplateService;
import org.openmrs.module.radiologyfhirsupport.api.MRRTToFHIRService;
import org.openmrs.module.radiologyfhirsupport.api.util.XPathMapper;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by devmaany on 8/6/16.
 */
public class MRRTToFHIRServiceImpl implements MRRTToFHIRService {
    @Override
    public DiagnosticReport convertMRRTToFHIRViaXPath(MRRTTemplate template, Map<String, String> xPathMappings) {
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

        xPathMappings.put("encounterUuid","id");
        String encounterUuid = template.getEncounterUuid();
        XPathMapper xPathMapper = new XPathMapper(encounterUuid,xml,xPathMappings);
        DiagnosticReport diagnosticReport = xPathMapper.getDiagnosticReport();

        return diagnosticReport;
    }


}
