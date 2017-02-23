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
import org.openmrs.*;
import org.openmrs.api.APIException;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.openmrs.messagesource.MessageSourceService;
import org.openmrs.module.ModuleActivator;
import org.openmrs.module.ModuleFactory;
import org.openmrs.module.fhir.api.DiagnosticReportService;
import org.openmrs.module.radiologyfhirsupport.api.handler.MRRTReportHandler;
import org.openmrs.module.radiologyfhirsupport.api.handler.MRRTTemplateHandler;
import org.openmrs.util.OpenmrsUtil;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class contains the logic that is run every time this module is either started or stopped.
 */
@Component
public class RadiologyFHIRSupportActivator implements ModuleActivator {
	public Logger logger = Logger.getLogger(RadiologyFHIRSupportActivator.class.getName());
	protected Log log = LogFactory.getLog(getClass());
	/**
	 * @see ModuleActivator#willRefreshContext()
	 */
	public void willRefreshContext() {
		logger.log(Level.INFO,"Refreshing Radiology FHIR Support Module");
	}
	
	/**
	 * @see ModuleActivator#contextRefreshed()
	 */
	public void contextRefreshed() {
		logger.log(Level.INFO,"Radiology FHIR Support Module refreshed");
	}
	
	/**
	 * @see ModuleActivator#willStart()
	 */
	public void willStart() {
		logger.log(Level.INFO,"Starting Radiology FHIR Support Module");
	}
	
	/**
	 * @see ModuleActivator#started()
	 */
	public void started() {
		logger.log(Level.INFO,"Radiology FHIR Support Module started");
		activateModule();
		debugClassPath();
	}
	
	/**
	 * @see ModuleActivator#willStop()
	 */
	public void willStop() {
		logger.log(Level.INFO,"Stopping Radiology FHIR Support Module");
	}
	
	/**
	 * @see ModuleActivator#stopped()
	 */
	public void stopped() {
		logger.log(Level.INFO,"Radiology FHIR Support Module stopped");
	}

	/**
	 * Initialize connection with FHIR module
	 */
	public void activateModule(){
		/* TODO FIX BUG : https://issues.jboss.org/browse/JBSEAM-4840*/
		MessageSourceService messageSourceService = Context.getMessageSourceService();
		/*Step 1 */
		if(ModuleFactory.isModuleStarted("fhir")) {
			try {
				registerFHIRDiagnosticReportHandler(messageSourceService);
			} catch (APIException ex) {
				ex.printStackTrace();
			}
		} else {
			throw new APIException("FHIR module 0.91 not started/ installed");
		}
		/*Step 2*/
		registerEncounterType(messageSourceService);
		/*Data initialization */
		String addDefaultProvider = messageSourceService.getMessage("radiologyfhirsupport.addDefaultProvider");
		if(addDefaultProvider.equals("true")) {
			addDefaultProvider(messageSourceService);
		}

		String addDefaultEncounterRole = messageSourceService.getMessage("radiologyfhirsupport.addDefaultEncounterRole");
		if(addDefaultEncounterRole.equals("true")){
			addDefaultEncounterRole(messageSourceService);
		}

		String addDefaultLocation = messageSourceService.getMessage("radiologyfhirsupport.addDefaultLocation");
		if(addDefaultLocation.equals("true")){
			addDefaultLocation(messageSourceService);

		}
		String addDemoPatient = messageSourceService.getMessage("radiologyfhirsupport.addDemoPatient");
		if(addDemoPatient.equals("true")){
			addDemoPatient(messageSourceService);
		}
		String addDemoMRRTReports = messageSourceService.getMessage("radiologyfhirsupport.addDemoMRRTReports");
		if(addDemoMRRTReports.equals("true")){
			addDemoMRRTReports(messageSourceService);
		}
	}

	public void addDemoPatient(MessageSourceService messageSourceService){
		logger.log(Level.INFO,"Adding demo patient for RADFHIR module");
		String personName = messageSourceService.getMessage("radiologyfhirsupport.personName");
		String patientIdentifierString = messageSourceService.getMessage("radiologyfhirsupport.patientIdentifier");
		List<Patient> patients = Context.getPatientService().getPatients(personName,patientIdentifierString, null, true);
		if(patients==null || patients.size()<1){
			logger.log(Level.INFO,"Creating demo patient for RADFHIR module. Name is : " + personName);
			PatientService patientService = Context.getService(PatientService.class);
			String personLastName = messageSourceService.getMessage("radiologyfhirsupport.personLastName");

			Patient patient = new Patient();

			PersonName pName = new PersonName();
			pName.setGivenName(personName);
			pName.setFamilyName(personLastName);
			patient.addName(pName);

			PersonAddress pAddress = new PersonAddress();
			pAddress.setAddress1("123 My street");
			pAddress.setAddress2("Apt 402");
			pAddress.setCityVillage("Anywhere city");
			pAddress.setCountry("Some Country");
			Set<PersonAddress> pAddressList = patient.getAddresses();
			pAddressList.add(pAddress);
			patient.setAddresses(pAddressList);
			patient.addAddress(pAddress);
			// patient.removeAddress(pAddress);

			patient.setBirthdate(new Date());
			patient.setBirthdateEstimated(true);
			patient.setGender("male");

			List<PatientIdentifierType> patientIdTypes = patientService.getAllPatientIdentifierTypes();
			PatientIdentifier patientIdentifier = new PatientIdentifier();
			patientIdentifier.setIdentifier(patientIdentifierString);
			patientIdentifier.setIdentifierType(patientIdTypes.get(0));
			patientIdentifier.setLocation(new Location(1));
			patientIdentifier.setPreferred(true);

			Set<PatientIdentifier> patientIdentifiers = new LinkedHashSet<PatientIdentifier>();
			patientIdentifiers.add(patientIdentifier);

			patient.setIdentifiers(patientIdentifiers);

			patientService.savePatient(patient);
		}else {
			logger.log(Level.INFO,"Demo patient already exists for RADFHIR module. Name : " + personName);
		}

	}
	public void registerFHIRDiagnosticReportHandler(MessageSourceService messageSourceService){
		String mrrtReportHandlerName = messageSourceService.getMessage("radiologyfhirsupport.encounterType");
		logger.log(Level.INFO,"Registering FHIR Diagnostic Report Handler for MRRT templates. Handler name is : " + mrrtReportHandlerName);
		DiagnosticReportService diagnosticReportService = Context.getService(DiagnosticReportService.class);
		if(!diagnosticReportService.getHandlers().containsKey(mrrtReportHandlerName)) {
			logger.log(Level.INFO,"Creating new FHIR Diagnostic Report Handler for MRRT templates");
			diagnosticReportService.registerHandler(mrrtReportHandlerName, new MRRTReportHandler());
		}else {
			logger.log(Level.INFO,"FHIR Diagnostic Report Handler for MRRT Reports already exists");
		}

	}
	public void registerFHIRDiagnosticReportHandlerForMRRTTemplates(MessageSourceService messageSourceService){
		String mrrtTemplateHandlerName = messageSourceService.getMessage("radiologyfhirsupport.handlerNameForTemplates"); /* change to encounterType if you want templates to work*/
		logger.log(Level.INFO,"Registering FHIR Diagnostic Report Handler for MRRT templates. Handler name is : " + mrrtTemplateHandlerName);
		DiagnosticReportService diagnosticReportService = Context.getService(DiagnosticReportService.class);
		if(!diagnosticReportService.getHandlers().containsKey(mrrtTemplateHandlerName)) {
			logger.log(Level.INFO,"Creating new FHIR Diagnostic Report Handler for MRRT templates");
			diagnosticReportService.registerHandler(mrrtTemplateHandlerName, new MRRTTemplateHandler());
		}else {
			logger.log(Level.INFO,"FHIR Diagnostic Report Handler for MRRT templates already exists");
		}

	}
	public void registerEncounterType(MessageSourceService messageSourceService){
		String encounterTypeNameString = messageSourceService.getMessage("radiologyfhirsupport.encounterType");
		String encounterTypeDescription = messageSourceService.getMessage("radiologyfhirsupport.encounterTypeDescription");
		if(Context.getEncounterService().getEncounterType(encounterTypeNameString)==null) {
			logger.log(Level.INFO,"Registering a new EncounterType for MRRTTemplates. This will be used for lookup by FHIR module : " + encounterTypeNameString);

			EncounterType mrrtFhirEncounterType = new EncounterType();
			mrrtFhirEncounterType.setName(encounterTypeNameString);
			mrrtFhirEncounterType.setDescription(encounterTypeDescription);
			mrrtFhirEncounterType.setCreator(Context.getAuthenticatedUser());
			mrrtFhirEncounterType.setDateCreated(new Date());
			mrrtFhirEncounterType.setRetired(false);
			Context.getEncounterService().saveEncounterType(mrrtFhirEncounterType);
		} else {
			logger.log(Level.INFO,"EncounterType for MRRTTemplates was already registered. This will be used for lookup by FHIR module : " + encounterTypeNameString);
		}

	}
	public void addDefaultLocation(MessageSourceService messageSourceService){
		logger.log(Level.INFO,"Adding default location for MRRT templates");

		String locationName = messageSourceService.getMessage("radiologyfhirsupport.locationName");
		String locationDescription = messageSourceService.getMessage("radiologyfhirsupport.locationDescription");
		String locationAddress1 = messageSourceService.getMessage("radiologyfhirsupport.locationAddress1");
		Location location = Context.getLocationService().getLocation(locationName);
		if(location==null){
			logger.log(Level.INFO,"Creating demo location to be used for MRRT templates. Location Name : " + locationName);
			location = new Location();
			location.setName(locationName);
			location.setDescription(locationDescription);
			location.setAddress1(locationAddress1);
			location.setCreator(Context.getAuthenticatedUser());
			location.setDateCreated(new Date());
			location.setRetired(false);
			Context.getLocationService().saveLocation(location);
		}else{
			logger.log(Level.INFO,"Demo Location already exists and will be used for MRRT Templates. Location Name : " + locationName);
		}
	}
	public void addDefaultEncounterRole(MessageSourceService messageSourceService){
		logger.log(Level.INFO,"Adding default encounter role for radiology fhir module");
		String encounterRoleName = messageSourceService.getMessage("radiologyfhirsupport.encounterRoleName");
		String encounterRoleDescription = messageSourceService.getMessage("radiologyfhirsupport.encounterRoleDescription");
		if (Context.getEncounterService().getEncounterRoleByName(encounterRoleName)==null){
			logger.log(Level.INFO,"Creating default encounter role for radiologyfhirsupport module");
			EncounterRole encounterRole = new EncounterRole();
			encounterRole.setRetired(false);
			encounterRole.setName(encounterRoleName);
			encounterRole.setDateCreated(new Date());
			encounterRole.setCreator(Context.getAuthenticatedUser());
			encounterRole.setDescription(encounterRoleDescription);
			Context.getEncounterService().saveEncounterRole(encounterRole);
		} else {
			logger.log(Level.INFO,"Default Encounter Role for radiologyfhirsupport module was already present with name : " + encounterRoleName );
		}
	}
	public void addDefaultProvider(MessageSourceService messageSourceService){
		logger.log(Level.INFO,"Adding default Provider for radiology fhir module");
		String providerIdentifier = messageSourceService.getMessage("radiologyfhirsupport.providerIdentifier");
		String providerName = messageSourceService.getMessage("radiologyfhirsupport.providerName");

		if (Context.getProviderService().getProviderByIdentifier(providerIdentifier) == null) {
			logger.log(Level.INFO,"Creating a new Encounter Provider Type for Radiology FHIR Support Module : " + providerIdentifier);

			Provider provider = new Provider();
			provider.setName(providerName);
			provider.setIdentifier(providerIdentifier);
			provider.setCreator(Context.getAuthenticatedUser());
			provider.setDateCreated(new Date());
			provider.setRetired(false);
			Context.getProviderService().saveProvider(provider);
		} else {
			logger.log(Level.INFO,"Encounter Provider was already registered with Identifier : " + providerIdentifier);
		}
	}
	public void addDemoMRRTReports(MessageSourceService messageSourceService){

	}
	private void debugClassPath(){
		ClassLoader cl = ClassLoader.getSystemClassLoader();

		URL[] urls = ((URLClassLoader)cl).getURLs();

		for(URL url: urls){
			System.out.println(url.getFile());
		}

		System.out.println(OpenmrsUtil.getApplicationDataDirectory());

	}
}
