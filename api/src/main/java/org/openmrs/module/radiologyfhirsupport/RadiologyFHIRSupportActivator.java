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
package org.openmrs.module.radiologyfhirsupport;


import org.apache.commons.logging.Log; 
import org.apache.commons.logging.LogFactory;
import org.openmrs.EncounterType;
import org.openmrs.api.context.Context;
import org.openmrs.module.ModuleActivator;
import org.openmrs.module.fhir.api.DiagnosticReportService;
import org.openmrs.module.radiologyfhirsupport.api.handler.MRRTTemplateHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * This class contains the logic that is run every time this module is either started or stopped.
 */
@Component
public class RadiologyFHIRSupportActivator implements ModuleActivator {
	
	protected Log log = LogFactory.getLog(getClass());
	@Value("${project.parent.artifactId}.handlerName}")
	String mrrtTemplateHandlerName;
	@Value("${project.parent.artifactId}.handlerDescription")
	String mrrtTemplateHandlerDescription;
	/**
	 * @see ModuleActivator#willRefreshContext()
	 */
	public void willRefreshContext() {
		log.info("Refreshing Radiology FHIR Support Module");
	}
	
	/**
	 * @see ModuleActivator#contextRefreshed()
	 */
	public void contextRefreshed() {
		log.info("Radiology FHIR Support Module refreshed");
	}
	
	/**
	 * @see ModuleActivator#willStart()
	 */
	public void willStart() {
		log.info("Starting Radiology FHIR Support Module");
	}
	
	/**
	 * @see ModuleActivator#started()
	 */
	public void started() {
		log.info("Radiology FHIR Support Module started");
		activateModule();
	}
	
	/**
	 * @see ModuleActivator#willStop()
	 */
	public void willStop() {
		log.info("Stopping Radiology FHIR Support Module");
	}
	
	/**
	 * @see ModuleActivator#stopped()
	 */
	public void stopped() {
		log.info("Radiology FHIR Support Module stopped");
	}

	/**
	 * Initialize connection with FHIR module
	 */
	public void activateModule(){

		DiagnosticReportService diagnosticReportService = Context.getService(DiagnosticReportService.class);
		log.info("Registering FHIR Diagnostic Report Handler for MRRT templates. Handler name is : " + mrrtTemplateHandlerName);
		diagnosticReportService.registerHandler(mrrtTemplateHandlerName,new MRRTTemplateHandler());
		log.info("Registering a new EncounterType for MRRTTemplates. This will be used for lookup by FHIR module : " + mrrtTemplateHandlerDescription);
		EncounterType mrrtFhirEncounterType = new EncounterType(mrrtTemplateHandlerName, mrrtTemplateHandlerDescription);
		mrrtFhirEncounterType.setCreator(Context.getAuthenticatedUser());
		mrrtFhirEncounterType.setDateCreated(new Date());
		mrrtFhirEncounterType.setRetired(false);
		Context.getEncounterService().saveEncounterType(mrrtFhirEncounterType);
	}
		
}
