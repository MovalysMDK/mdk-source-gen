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
package com.a2a.adjava.languages.w8.xmodele;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;

import com.a2a.adjava.xmodele.MEnumeration;
import com.a2a.adjava.xmodele.MLabel;
import com.a2a.adjava.xmodele.MVisualField;
import com.a2a.adjava.xmodele.ui.view.MVFLabelKind;
import com.a2a.adjava.xmodele.ui.view.MVFLocalization;

/**
 * IOS Visual Field
 * @author lmichenaud
 *
 */
public class MW8VisualField extends MVisualField {

	/**
	 * Create a new visual field from existing one
	 * @param p_oField existing visual field
	 */
	public MW8VisualField(MVisualField p_oField) {
		super(p_oField);
	}

	/**
	 * Create a new visual field from existing one
	 * @param p_sPath le chemin du champ.
	 * @param p_oField le champ à partir duquel on veut créer le clone
	 */
	public MW8VisualField(String p_sPath, MVisualField p_oField, MLabel p_oLabel) {
		super(p_sPath, p_oField, p_oLabel);
	}
	
	/**
	 * Create a new visual field
	 * @param p_sName name of the field
	 * @param p_sLabelName label name before the field
	 * @param p_sComponent component to use
	 * @param p_oLabelKind label kind
	 * @param p_oTarget the localisation
	 * @param p_sEntityAttributeName attribute name in the entity
	 * @param p_bMandatoryKind mandatory
	 */
	public MW8VisualField(String p_sName, MLabel p_oLabel,	String p_sComponent, MVFLabelKind p_oLabelKind,
			MVFLocalization p_oTarget, String p_sEntityAttributeName, boolean p_bMandatoryKind) {
		super(p_sName, p_oLabel, p_sComponent, p_oLabelKind, p_oTarget,
				p_sEntityAttributeName, p_bMandatoryKind);
	}

	/**
	 * Construit un nouvel objet de type <em>MVisualField</em>
	 * @param p_sName field name
	 * @param p_sLabelName label name
	 * @param p_sComponent l'emplacement du champ et son type sous la forme d'une classe + package.
	 * @param p_oLabelKind label kind
	 * @param p_sEntityAttributeName attribute name in entity
	 * @param p_bMandatoryKind mandatory
	 */
	public MW8VisualField(String p_sName, MLabel p_oLabel,	String p_sComponent, MVFLabelKind p_oLabelKind,
			String p_sEntityAttributeName, boolean p_bMandatoryKind) {
		super(p_sName, p_oLabel, p_sComponent, p_oLabelKind,
				p_sEntityAttributeName, p_bMandatoryKind);
	}

	/**
	 * @param p_sName name of the field
	 * @param p_sLabelName label name before the field
	 * @param p_sComponent component to use
	 * @param p_sEditType the input type of the field (to param the keyboard)
	 * @param p_iMaxLength the max length of the field (for string type for example)
	 * @param p_iPrecision the pecision of a number field (in oracle style)
	 * @param p_iScale the scale of a number field (oracle style)
	 * @param p_oLabelKind kind of label
	 * @param p_oTarget the localisation
	 * @param p_sEntityAttributeName attribute name in entity
	 * @param p_bMandatoryKind mandatory
	 * @param p_oEnum enumeration
	 * @param p_bReadOnly read only
	 * @param p_umlName the uml name of this visual field 
	 */
	public MW8VisualField(String p_sName, MLabel p_oLabel,
			String p_sComponent, String p_sEditType, int p_iMaxLength,
			int p_iPrecision, int p_iScale, MVFLabelKind p_oLabelKind,
			MVFLocalization p_oTarget, String p_sEntityAttributeName,
			boolean p_bMandatoryKind, MEnumeration p_oEnum, boolean p_bReadOnly, String p_sUmlName) {
		super(p_sName, p_oLabel, p_sComponent, p_sEditType, p_iMaxLength,
				p_iPrecision, p_iScale, p_oLabelKind, p_oTarget,
				p_sEntityAttributeName, p_bMandatoryKind, p_oEnum, p_bReadOnly, p_sUmlName);
	}

	@Override
	protected void toXmlInsertBeforeDocumentation(Element p_xElement) {
		super.toXmlInsertBeforeDocumentation(p_xElement);
		if (this.getViewModelProperty() != null) {
			p_xElement.addElement("property-name-c").setText( StringUtils.capitalize(this.getViewModelProperty()) );
		}
			
	}	
}
