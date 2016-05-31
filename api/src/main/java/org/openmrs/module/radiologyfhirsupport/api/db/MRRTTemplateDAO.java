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
package org.openmrs.module.radiologyfhirsupport.api.db;

import org.openmrs.module.radiologyfhirsupport.MRRTTemplate;
import org.openmrs.module.radiologyfhirsupport.api.MRRTFHIRServiceService;

import java.util.List;

/**
 *  Database methods for {@link MRRTFHIRServiceService}.
 */
public interface MRRTTemplateDAO {
	
	/*
	 * Add DAO methods here
	 */

    /**
     *
     * @param id
     * @return
     */
    public MRRTTemplate getById(int id);

    /**
     *
     * @param uuid
     * @return
     */
    public MRRTTemplate getByUUID(String uuid);

    /**
     *
     * @param template
     */
    public List<MRRTTemplate> getAll();
    public int saveOrUpdate(MRRTTemplate template);

    /**
     *
     * @param id
     */
    public boolean deleteById(Integer id);

    /**
     *
     * @param template
     */
    public boolean delete(MRRTTemplate template);
}