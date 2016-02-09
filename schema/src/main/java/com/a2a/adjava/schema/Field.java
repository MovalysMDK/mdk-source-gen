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
package com.a2a.adjava.schema;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.a2a.adjava.datatypes.DataType;

public class Field {

	private String name; // Nom du champs
	private String type; // Type du champs
	private boolean notnull; // champs obligatoire ou pas
	private Sequence sequence ; // si le champs a une sequence affectée
	private int length ;
	private int precision ;
	private int scale ;
	private boolean unique ;
	private String uniqueKey ;
	private DataType dataType ;
	private String initialValue;

	/**
	 * Constructeur
	 */
	protected Field(String p_sName, String p_sType, boolean p_bNotNull, int p_iLength, int p_iPrecision, int p_iScale,
			boolean p_bUnique, String p_sUniqueKey, DataType p_oDataType, String p_sInitialValue ) {
		this.name = p_sName.toUpperCase();
		this.type = p_sType;
		this.notnull = p_bNotNull;
		this.length = p_iLength;
		this.precision = p_iPrecision ;
		this.scale = p_iScale ;
		this.unique = p_bUnique ;
		this.uniqueKey = p_sUniqueKey ;
		this.dataType = p_oDataType ;
		this.initialValue = p_sInitialValue;
	}

	/**
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return
	 */
	public String getType() {
		return type;
	}

	/**
	 * @return
	 */
	public boolean isNotnull() {
		return notnull;
	}

	/**
	 * @return
	 */
	public Sequence getSequence() {
		return this.sequence ;
	}
	
	/**
	 * @param p_oSequence
	 */
	public void setSequence(Sequence p_oSequence) {
		this.sequence = p_oSequence;
	}
	
	/**
	 * @return
	 */
	public boolean hasSequence() {
		return this.sequence != null ;
	}
	
	/**
	 * @return
	 */
	public int getPrecision() {
		return precision;
	}

	/**
	 * @return
	 */
	public int getScale() {
		return scale;
	}

	/**
	 * @return
	 */
	public boolean isUnique() {
		return unique;
	}

	/**
	 * @return
	 */
	public String getUniqueKey() {
		return uniqueKey;
	}

	/**
	 * @return
	 */
	public DataType getDataType() {
		return dataType;
	}
	
	/**
	 * @return
	 */
	public int getLength() {
		return length;
	}

	/**
	 * Returns the initial value of this field.
	 * @return The initial value of this field or null if it is not defined.
	 */
	public String getInitialValue() {
		return this.initialValue;
	}

	/**
	 * @return
	 */
	public Element toXml() {
		Element r_xField = DocumentHelper.createElement("field");
		r_xField.addAttribute("name", this.name );
		r_xField.addAttribute("type", this.type );
		r_xField.addAttribute("not-null", Boolean.toString(this.notnull));
		r_xField.addAttribute("data-type", this.dataType.toString());

		if (this.initialValue != null) {
			r_xField.addAttribute("default-value", this.initialValue);
		}

		if ( hasSequence()) {
			r_xField.add( this.sequence.toXml());
		}
		return r_xField ;
	}
}
