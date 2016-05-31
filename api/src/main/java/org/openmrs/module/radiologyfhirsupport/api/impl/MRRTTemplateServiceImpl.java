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

import org.openmrs.api.impl.BaseOpenmrsService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.radiologyfhirsupport.MRRTTemplate;
import org.openmrs.module.radiologyfhirsupport.api.MRRTTemplateService;
import org.openmrs.module.radiologyfhirsupport.api.db.MRRTTemplateDAO;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional()
    public int saveOrUpdate(MRRTTemplate template) {
        return dao.saveOrUpdate(template);
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
}