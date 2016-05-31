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

import java.io.Serializable;
import java.sql.Clob;

import org.openmrs.BaseOpenmrsObject;
import org.openmrs.BaseOpenmrsMetadata;

import javax.persistence.*;


/**
 * It is a model class. It should extend either {@link BaseOpenmrsObject} or {@link BaseOpenmrsMetadata}.
 */
@Entity
@Table(name = "radiology_mrrt_template")
public class MRRTTemplate extends BaseOpenmrsObject implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	@Lob
	@Column(name = "xml")
	private Clob xml;
	
	@Override
	public Integer getId() {
		return id;
	}
	
	@Override
	public void setId(Integer id) {
		this.id = id;
	}
	public Clob getXml() {
		return xml;
	}

	public void setXml(Clob xml) {
		this.xml = xml;
	}
	
}