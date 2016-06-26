/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.radiologyfhirsupport.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.fhir.api.DiagnosticReportService;
import org.openmrs.module.fhir.api.diagnosticreport.DiagnosticReportHandler;
import org.openmrs.module.radiologyfhirsupport.MRRTTemplate;
import org.openmrs.module.radiologyfhirsupport.api.MRRTTemplateService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.sql.rowset.serial.SerialClob;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Map;

/**
 * The main controller.
 */
@Controller
public class  RadiologyFHIRSupportManageController {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	@RequestMapping(value = "/module/radiologyfhirsupport/manage", method = RequestMethod.GET)
	public void manage(ModelMap model) {
		model.addAttribute("user", Context.getAuthenticatedUser());
		model.addAttribute("value",Context.getMessageSourceService().getPresentation("openmrs.title", Locale.ENGLISH));
	}
	@RequestMapping(value = "/module/radiologyfhirsupport/initData", method = RequestMethod.GET)
	public String initData(ModelMap model) {
		System.out.println("Creating template");
		String encounterUUID = "encounterUUID not received";
		try {
			encounterUUID = loadMRRTTemplates();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return encounterUUID;
	}
	@RequestMapping(value = "/module/radiologyfhirsupport/listHandlers", method = RequestMethod.GET)
	public void listHandlers(ModelMap model) {
		DiagnosticReportService diagnosticReportService = Context.getService(DiagnosticReportService.class);
		Map<String, DiagnosticReportHandler> handlers = diagnosticReportService.getHandlers();
		for(Map.Entry<String,DiagnosticReportHandler> handler:handlers.entrySet()){
			System.out.println("*****");
			System.out.println(handler.getKey());
		}
	}
	String loadMRRTTemplates() throws SQLException {
		MRRTTemplateService mrrtTemplateService = Context.getService(MRRTTemplateService.class);
		if(mrrtTemplateService.getAll().size()!=0) {
			return mrrtTemplateService.getAll().get(0).getEncounterUuid();
		}
		MRRTTemplate chestXRay = new MRRTTemplate();
		String chestXRayString = "<!DOCTYPE html>\n" +
				"<html>\n" +
				"<head>\n" +
				"<meta charset='UTF-8'/>\n" +
				"<meta content='Chest Xray' name='dcterms.title'/>\n" +
				"<meta content='Chest Xray template :: Authored by Kahn CE Jr' name='dcterms.description'/>\n" +
				"<meta content='http://www.radreport.org/template/0000102' name='dcterms.identifier'/>\n" +
				"<meta content='en' name='dcterms.language'/>\n" +
				"<meta content='IMAGE_REPORT_TEMPLATE' name='dcterms.type'/>\n" +
				"<meta content='Radiological Society of North America (RSNA)' name='dcterms.publisher'/>\n" +
				"<meta content='May be used gratis, subject to license agreement' name='dcterms.rights'/>\n" +
				"<meta content='http://www.radreport.org/license.pdf' name='dcterms.license'/>\n" +
				"<meta content='2012-03-28' name='dcterms.date'/>\n" +
				"<meta content='Kahn CE Jr' name='dcterms.creator'/>\n" +
				"<meta content='Kahn CE Jr [editor]' name='dcterms.contributor'/>\n" +
				"<meta content='Medical College of Wisconsin (MCW)' name='dcterms.contributor'/>\n" +
				"<link rel='stylesheet' type='text/css' href='IHE_Template_Style.css'/>\n" +
				"<!-- The absolute link to the CSS file is http://www.radreport.org/html/IHE_Template_Style.css -->\n" +
				"<script type='text/xml'>\n" +
				"<!--\n" +
				"<template_attributes>\n" +
				"<top-level-flag>true</top-level-flag>\n" +
				"<status>ACTIVE</status>\n" +
				"<coding_schemes>\n" +
				" <coding_scheme name='RadLex' designator='2.16.840.1.113883.6.256' />\n" +
				" <coding_scheme name='SNOMEDCT' designator='2.16.840.1.113883.6.96' />\n" +
				" <coding_scheme name='LOINC' designator='2.16.840.1.113883.6.1' />\n" +
				"</coding_schemes>\n" +
				"<term>\n" +
				" <code meaning='projection radiography' value='RID10345' scheme='RadLex'/>\n" +
				"</term>\n" +
				"<term>\n" +
				" <code meaning='thorax' value='RID1243' scheme='RadLex'/>\n" +
				"</term>\n" +
				"<coded_content>\n" +
				"<entry ORIGTXT='T102_2'>\n" +
				" <term>\n" +
				"  <code meaning='procedure' value='RID1559' scheme='RadLex'/>\n" +
				" </term>\n" +
				"</entry>\n" +
				"<entry ORIGTXT='T102_7'>\n" +
				" <term>\n" +
				"  <code meaning='clinical information' value='RID13166' scheme='RadLex'/>\n" +
				" </term>\n" +
				"</entry>\n" +
				"<entry ORIGTXT='T102_12'>\n" +
				" <term>\n" +
				"  <code meaning='comparison' value='RID28483' scheme='RadLex'/>\n" +
				" </term>\n" +
				"</entry>\n" +
				"<entry ORIGTXT='T102_15'>\n" +
				" <term>\n" +
				"  <code meaning='heart' value='RID1385' scheme='RadLex'/>\n" +
				" </term>\n" +
				"</entry>\n" +
				"<entry ORIGTXT='T102_17'>\n" +
				" <term>\n" +
				"  <code meaning='lungs' value='RID13437' scheme='RadLex'/>\n" +
				" </term>\n" +
				"</entry>\n" +
				"<entry ORIGTXT='T102_20'>\n" +
				" <term>\n" +
				"  <code meaning='set of bones' value='RID28569' scheme='RadLex'/>\n" +
				" </term>\n" +
				"</entry>\n" +
				"<entry ORIGTXT='T102_23'>\n" +
				" <term>\n" +
				"  <code meaning='impression section' value='RID13170' scheme='RadLex'/>\n" +
				" </term>\n" +
				"</entry>\n" +
				"</coded_content>\n" +
				"</template_attributes>\n" +
				"-->\n" +
				"</script>\n" +
				"<title>Chest Xray</title>\n" +
				"</head>\n" +
				"<body>\n" +
				"<section data-section-name='Procedure'>\n" +
				"<header class='level1'>Chest Xray</header>\n" +
				"<p>\n" +
				"<textarea id='T102_2' name='Procedure'>PA and lateral views. </textarea>\n" +
				"</p>\n" +
				"</section>\n" +
				"<section data-section-name='Clinical information'>\n" +
				"<header class='level1'>Clinical information</header>\n" +
				"<p>\n" +
				"<textarea id='T102_7' name='Clinical information'> </textarea>\n" +
				"</p>\n" +
				"</section>\n" +
				"<section data-section-name='Comparison'>\n" +
				"<header class='level1'>Comparison</header>\n" +
				"<p>\n" +
				"<textarea id='T102_12' name='Comparison'>None. </textarea>\n" +
				"</p>\n" +
				"</section>\n" +
				"<section data-section-name='Findings'>\n" +
				"<header class='level1'>Findings</header>\n" +
				"<p>\n" +
				"<label for='T102_15'>Heart:</label>\n" +
				"<input type='text' id='T102_15' name='Heart' value='Normal.'/>\n" +
				"</p>\n" +
				"<p>\n" +
				"<label for='T102_17'>Lungs:</label>\n" +
				"<input type='text' id='T102_17' name='Lungs' value='Normal.'/>\n" +
				"</p>\n" +
				"<p>\n" +
				"<label for='T102_20'>Bones:</label>\n" +
				"<input type='text' id='T102_20' name='Bones' value='Normal.'/>\n" +
				"</p>\n" +
				"</section>\n" +
				"<section data-section-name='Impression'>\n" +
				"<header class='level1'>Impression</header>\n" +
				"<p>\n" +
				"<textarea id='T102_23' name='Impression'>No acute disease. </textarea>\n" +
				"</p>\n" +
				"</section>\n" +
				"</body>\n" +
				"</html>";
		Clob xml =  new SerialClob(chestXRayString.toCharArray());
		chestXRay.setXml(xml);
		String chestXRayEncounterUUID = mrrtTemplateService.create(chestXRay);
		return chestXRayEncounterUUID;
	}

}
