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
import org.hibernate.SessionFactory;
import org.openmrs.module.radiologyfhirsupport.MRRTTemplate;
import org.openmrs.module.radiologyfhirsupport.api.db.MRRTTemplateDAO;

import java.util.List;

/**
 * It is a default implementation of  {@link MRRTTemplateDAO}.
 */
public class HibernateMRRTTemplateDAO implements MRRTTemplateDAO {
	protected final Log log = LogFactory.getLog(this.getClass());

	private SessionFactory sessionFactory;
	
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
		return (List<MRRTTemplate>)sessionFactory.getCurrentSession().createCriteria(MRRTTemplate.class);
	}

	@Override
	public int saveOrUpdate(MRRTTemplate template) {
		sessionFactory.getCurrentSession().saveOrUpdate(template);
		return template.getId();
	}

	@Override
	public void deleteById(Integer id) {
		MRRTTemplate template = getById(id);
		delete(template);
	}

	@Override
	public void delete(MRRTTemplate template) {
		boolean flag = false;
		sessionFactory.getCurrentSession().delete(template);
	}
}