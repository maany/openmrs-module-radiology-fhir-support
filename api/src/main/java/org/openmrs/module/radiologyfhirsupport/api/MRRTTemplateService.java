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
package org.openmrs.module.radiologyfhirsupport.api;

import org.openmrs.User;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.radiologyfhirsupport.MRRTTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import static java.sql.Types.CLOB;

/**
 * This service exposes module's core functionality. It is a Spring managed bean which is configured in moduleApplicationContext.xml.
 * <p>
 * It can be accessed only via Context:<br>
 * <code>
 * Context.getService(MRRTTemplateService.class).someMethod();
 * </code>
 * 
 * @see org.openmrs.api.context.Context
 */
@Transactional
public interface MRRTTemplateService extends OpenmrsService {

    /**
     *
     * @param id
     * @return
     */
    MRRTTemplate getById(int id);

    /**
     *
     * @return
     */
    /**
     *
     * @return
     */
    List<MRRTTemplate> getAll();

    /**
     *
     * @param encounterUUID
     * @return
     */
    MRRTTemplate getByEncounterUUID(String encounterUUID);
    /**
     *
     * @param template
     * @return
     */
    int saveOrUpdate(MRRTTemplate template);

    /**
     *
     * @param template
     * @return uuid of the {@link org.openmrs.Encounter} created
     */
    String create(MRRTTemplate template);
    /**
     *
     * @param id
     * @return
     */
    /**
     *
     * @param id
     * @return
     */
    MRRTTemplate delete(int id);

    /**
     *
     * @param template
     * @return
     */
    MRRTTemplate delete(MRRTTemplate template);

    /**
     *
     * @param clob
     * @return
     */
    String clobToString(Clob clob) throws SQLException, IOException;

    /**
     *
     * @param xml
     * @return
     */
    Clob stringToClob(String xml) throws SQLException;

    /**
     *
     * @param templateId
     * @param voidedBy
     * @param voidedOn
     * @param voidReason
     */
    void retire(int templateId, User voidedBy, Date voidedOn, String voidReason);

}