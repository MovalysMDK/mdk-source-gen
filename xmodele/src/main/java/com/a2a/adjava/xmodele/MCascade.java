/**
 * Copyright (C) 2010 Sopra Steria Group (movalys.support@soprasteria.com)
 *
 * This file is part of Movalys MDK.
 * Movalys MDK is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * Movalys MDK is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License
 * along with Movalys MDK. If not, see <http://www.gnu.org/licenses/>.
 */
package com.a2a.adjava.xmodele;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * @author lmichenaud
 *
 */
public class MCascade {

	/**
	 * 
	 */
	private String name ;
	
	/**
	 * 
	 */
	private String cascadeImport ;
	
	/**
	 * 
	 */
	private MEntityImpl targetEntity ;
	
	/**
	 * 
	 */
	private String assoName ;
	
	/**
	 * @param p_sName
	 * @param p_sCascadeImport
	 * @param p_oTargetEntity
	 */
	public MCascade(String p_sName, String p_sCascadeImport, MEntityImpl p_oTargetEntity, String p_sAssoName) {
		super();
		this.name = p_sName;
		this.cascadeImport = p_sCascadeImport;
		this.targetEntity = p_oTargetEntity;
		this.assoName = p_sAssoName;
	}

	/**
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param p_sName
	 */
	public void setName(String p_sName) {
		this.name = p_sName;
	}

	/**
	 * @return
	 */
	public String getCascadeImport() {
		return cascadeImport;
	}

	/**
	 * @param p_sCascadeImport
	 */
	public void setCascadeImport(String p_sCascadeImport) {
		this.cascadeImport = p_sCascadeImport;
	}

	/**
	 * @return
	 */
	public MEntityImpl getTargetEntity() {
		return targetEntity;
	}

	/**
	 * @param p_oTargetEntity
	 */
	public void setTargetEntity(MEntityImpl p_oTargetEntity) {
		this.targetEntity = p_oTargetEntity;
	}
	
	/**
	 * @return
	 */
	public String getAssoName() {
		return this.assoName;
	}

	/**
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object p_oObject) {
		boolean r_bEquals = false ;
		if (this.getClass() == p_oObject.getClass()) {
			MCascade oCascade = (MCascade) p_oObject;
			EqualsBuilder oEqualsBuilder = new EqualsBuilder();
			oEqualsBuilder.append(this.getName(), oCascade.getName());
			r_bEquals = oEqualsBuilder.isEquals();
		}
		return r_bEquals ;
	}

	/**
	 * @return
	 */
	public Element toXml() {
		Element r_xCascade = DocumentHelper.createElement("cascade");
		r_xCascade.addElement("name").setText(this.name);
		r_xCascade.addElement("import").setText(this.cascadeImport);
		r_xCascade.addElement("assoName").setText(this.assoName);
		Element xEntity = r_xCascade.addElement("entity");
		xEntity.addElement("name").setText(this.targetEntity.getName());
		xEntity.addElement("full-name").setText(this.targetEntity.getFullName());
		return r_xCascade ;
	}
}
