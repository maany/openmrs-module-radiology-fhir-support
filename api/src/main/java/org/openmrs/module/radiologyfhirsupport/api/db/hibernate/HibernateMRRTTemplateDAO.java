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
package org.openmrs.module.radiologyfhirsupport.api.db.hibernate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.api.APIException;
import org.openmrs.api.EncounterService;
import org.openmrs.api.context.Context;
import org.openmrs.messagesource.MessageSourceService;
import org.openmrs.module.radiologyfhirsupport.MRRTTemplate;
import org.openmrs.module.radiologyfhirsupport.api.db.MRRTTemplateDAO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * It is a default implementation of  {@link MRRTTemplateDAO}.
 */
@PropertySource("classpath:message.properties")
public class HibernateMRRTTemplateDAO implements MRRTTemplateDAO {
	protected final Log log = LogFactory.getLog(this.getClass());
	@Value("${project.parent.artifactId}.handlerName")
	private String encounterType;
	@Value("${project.parent.artifactId}.saveEncounterError")
	private String saveEncounterError;
	@Value("${project.parent.artifactId}.saveEncounterTypeError}")
	private String saveEncounterTypeError;
	private SessionFactory sessionFactory;

	public HibernateMRRTTemplateDAO() {
		MessageSourceService messageSourceService = Context.getMessageSourceService();
		encounterType = messageSourceService.getMessage("radiologyfhirsupport.handlerName");
		saveEncounterError = messageSourceService.getMessage("radiologyfhirsupport.saveEncounterError");
		saveEncounterTypeError = messageSourceService.getMessage("radiologyfhirsupport.saveEncounterTypeError");
	}

	/**
     * @param sessionFactory the sessionFactory to set
     */
    public void setSessionFactory(SessionFactory sessionFactory) {
	    this.sessionFactory = sessionFactory;
    }
    
	/**
     * @return the sessionFactory
     */
    public SessionFactory getSessionFactory() {
	    return sessionFactory;
    }

	@Override
	public MRRTTemplate getById(int id) {
		return (MRRTTemplate) sessionFactory.getCurrentSession().get(MRRTTemplate.class, id);
	}
	//TODO implement this method
	@Override
	public MRRTTemplate getByUUID(String uuid) {
		return null;
	}

	@Override
	public List<MRRTTemplate> getAll() {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(MRRTTemplate.class);
		return criteria.list();
	}

	@Override
	public int saveOrUpdate(MRRTTemplate template) {
		EncounterService encounterService = Context.getEncounterService();
		Encounter encounter = createOpenmrsEncounter(encounterService);
		encounterService.saveEncounter(encounter);
		/* Store encounter's uuid for lookup */
		String encounterUuid = encounter.getUuid();
		if(encounterUuid.length()<1)
			throw new APIException(saveEncounterError);
		template.setEncounterUuid(encounter.getUuid());
		sessionFactory.getCurrentSession().saveOrUpdate(template);
		return template.getId();
	}

	@Override
	public MRRTTemplate deleteById(Integer id) {
		MRRTTemplate template = getById(id);
		return delete(template);
	}

	@Override
	public MRRTTemplate delete(MRRTTemplate template) {
		boolean flag = false;
		sessionFactory.getCurrentSession().delete(template);
		return template;
	}

	/**
	 * Create a new {@link Encounter} Object for the MRRTTemplate
	 * @return
	 * @param encounterService
     */
	private Encounter createOpenmrsEncounter(EncounterService encounterService){
		Encounter encounter = new Encounter();
		System.out.println("Encounter Type is " + encounterType);
		EncounterType encounterType = encounterService.getEncounterType(this.encounterType);
		if(encounterType==null || encounterType.getName().length()<1)
			throw new APIException(saveEncounterTypeError);
		encounter.setEncounterType(encounterType);
		return encounter;
	}
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
		return new PropertySourcesPlaceholderConfigurer();
	}
}