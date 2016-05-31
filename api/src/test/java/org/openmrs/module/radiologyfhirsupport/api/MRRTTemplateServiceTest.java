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

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.api.context.Context;
import org.openmrs.module.radiologyfhirsupport.MRRTTemplate;
import org.openmrs.test.BaseModuleContextSensitiveTest;

import javax.sql.rowset.serial.SerialClob;
import java.sql.Clob;
import java.sql.SQLException;

/**
 * Tests {@link ${MRRTTemplateService}}.
 */
public class MRRTTemplateServiceTest extends BaseModuleContextSensitiveTest {
	protected static final String MRRT_INITIAL_DATA_XML = "MRRTTemplateDemoData.xml";
	@Before
	public void loadTestData(){
		try {
			executeDataSet(MRRT_INITIAL_DATA_XML);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Test
	public void shouldSetupContext() {
		assertNotNull(Context.getService(MRRTTemplateService.class));
	}

	@Test
	public void getById_shouldGetMRRTTemplateById(){
		MRRTTemplateService mrrtTemplateService = getService();

	}
	@Test
	public void saveOrUpdate_shouldSaveMRRTTemplate(){
		MRRTTemplateService mrrtTemplateService = getService();
		MRRTTemplate template = new MRRTTemplate();
		Clob xmlData=null;
		try {
			xmlData = new SerialClob("<html><head><title>hello</title></head><body></body></html>".toCharArray());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		template.setXml(xmlData);
		int id = mrrtTemplateService.saveOrUpdate(template);
		assertNotNull(id);
	}
	MRRTTemplateService getService(){
		return Context.getService(MRRTTemplateService.class);
	}
}
