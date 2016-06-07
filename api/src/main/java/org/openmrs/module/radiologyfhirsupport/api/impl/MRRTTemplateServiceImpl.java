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
package org.openmrs.module.radiologyfhirsupport.api.impl;

import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.api.APIException;
import org.openmrs.api.EncounterService;
import org.openmrs.api.context.Context;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.messagesource.MessageSourceService;
import org.openmrs.module.radiologyfhirsupport.MRRTTemplate;
import org.openmrs.module.radiologyfhirsupport.api.MRRTTemplateService;
import org.openmrs.module.radiologyfhirsupport.api.db.MRRTTemplateDAO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.rowset.serial.SerialClob;
import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * It is a default implementation of {@link MRRTTemplateService}.
 */
public class MRRTTemplateServiceImpl extends BaseOpenmrsService implements MRRTTemplateService {
	
	protected final Log log = LogFactory.getLog(this.getClass());
	
	private MRRTTemplateDAO dao;

    /**
     * @param dao the dao to set
     */
    public void setDao(MRRTTemplateDAO dao) {
	    this.dao = dao;
    }
    
    /**
     * @return the dao
     */
    public MRRTTemplateDAO getDao() {
	    return dao;
    }

    @Override
    @Transactional(readOnly = true)
    public MRRTTemplate getById(int id) {
        return dao.getById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MRRTTemplate> getAll() {
        return dao.getAll();
    }

    @Override
    public MRRTTemplate getByEncounterUUID(String encounterUUID) {
        return dao.getByEncounterUUID(encounterUUID);
    }

    @Override
    @Transactional()
    public int saveOrUpdate(MRRTTemplate template) {
        return dao.saveOrUpdate(template);
    }

    @Override
    @Transactional
    public String create(MRRTTemplate template) {
        MessageSourceService messageSourceService = Context.getMessageSourceService();
        String saveEncounterError = messageSourceService.getMessage("radiologyfhirsupport.saveEncounterError");
        EncounterService encounterService = Context.getEncounterService();
        Encounter encounter = createOpenmrsEncounter(encounterService);
        encounterService.saveEncounter(encounter);
		/* Store encounter's uuid for lookup */
        String encounterUuid = encounter.getUuid();
        if(encounterUuid.length()<1)
            throw new APIException(saveEncounterError);
        template.setEncounterUuid(encounter.getUuid());
        saveOrUpdate(template);
        return encounterUuid;
    }

    @Override
    @Transactional
    public MRRTTemplate delete(int id) {
        return dao.deleteById(id);
    }

    @Override
    @Transactional
    public MRRTTemplate delete(MRRTTemplate template) {
        return dao.delete(template);
    }

    @Override
    public String clobToString(Clob clob) throws SQLException, IOException {
            Reader reader = clob.getCharacterStream();
            int c = -1;
            StringBuilder sb = new StringBuilder();
            while((c = reader.read()) != -1) {
                sb.append(((char)c));
            }

            return sb.toString();
    }

    @Override
    public Clob stringToClob(String xml) throws SQLException {
        return new SerialClob(xml.toCharArray());
    }

    /**
     * Create a new {@link Encounter} Object for the MRRTTemplate
     * @return
     * @param encounterService
     */
    private Encounter createOpenmrsEncounter(EncounterService encounterService){
        MessageSourceService messageSourceService = Context.getMessageSourceService();
        String encounterType = messageSourceService.getMessage("radiologyfhirsupport.handlerName");
        String saveEncounterTypeError = messageSourceService.getMessage("radiologyfhirsupport.saveEncounterTypeError");
        Encounter encounter = new Encounter();
        log.info("Encounter Type is " + encounterType);
        EncounterType encounterTypeMRS = encounterService.getEncounterType(encounterType);
        if(encounterTypeMRS==null || encounterTypeMRS.getName().length()<1)
            throw new APIException(saveEncounterTypeError);
        encounter.setEncounterType(encounterTypeMRS);
        encounter.setDateCreated(new Date());
        //TODO parse MRRTTemplate and populate Encounter with a Patient, Provider
        return encounter;
    }
}