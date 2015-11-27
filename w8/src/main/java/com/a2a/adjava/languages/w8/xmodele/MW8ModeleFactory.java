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

import java.util.List;

import com.a2a.adjava.types.ITypeDescription;
import com.a2a.adjava.types.IUITypeDescription;
import com.a2a.adjava.xmodele.IDomain;
import com.a2a.adjava.xmodele.IModelDictionary;
import com.a2a.adjava.xmodele.IModelFactory;
import com.a2a.adjava.xmodele.MAttribute;
import com.a2a.adjava.xmodele.MDaoImpl;
import com.a2a.adjava.xmodele.MDaoInterface;
import com.a2a.adjava.xmodele.MDaoMethodSignature;
import com.a2a.adjava.xmodele.MEntityImpl;
import com.a2a.adjava.xmodele.MEntityInterface;
import com.a2a.adjava.xmodele.MLabel;
import com.a2a.adjava.xmodele.MPackage;
import com.a2a.adjava.xmodele.MVisualField;
import com.a2a.adjava.xmodele.XModeleFactory;
import com.a2a.adjava.xmodele.ui.view.MVFLabelKind;
import com.a2a.adjava.xmodele.ui.view.MVFLocalization;
import com.a2a.adjava.xmodele.ui.view.MVFModifier;

public class MW8ModeleFactory extends XModeleFactory {

	// méthodes pour instancier les objets spécifiques à la plate-forme
	// et non liés au MDK.
	// Les objets spécifiques MDK vont dans MF4MDD.
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.IModelFactory#createVisualField(java.lang.String, com.a2a.adjava.types.IUITypeDescription, com.a2a.adjava.xmodele.ui.view.MVFModifier, com.a2a.adjava.xmodele.ui.view.MVFLabelKind, com.a2a.adjava.xmodele.MAttribute, com.a2a.adjava.xmodele.IDomain, java.lang.String, boolean)
	 */
	@Override
	public MVisualField createVisualField(String p_sPrefix, MLabel p_oLabel, IUITypeDescription p_oTypeVisual,
			MVFModifier p_oModifier, MVFLabelKind p_oLabelKind, MAttribute p_oAttribute,
			IDomain<IModelDictionary, IModelFactory> p_oDomain, String p_sAttributeName,
			boolean p_bMandatory) {		
		return this.createVisualField(p_sPrefix, p_oLabel, p_oTypeVisual, p_oModifier, p_oLabelKind, p_oAttribute,
				MVFLocalization.DEFAULT, p_oDomain, p_sAttributeName, p_bMandatory);
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.IModelFactory#createVisualField(java.lang.String, com.a2a.adjava.types.IUITypeDescription, com.a2a.adjava.xmodele.ui.view.MVFModifier, com.a2a.adjava.xmodele.ui.view.MVFLabelKind, com.a2a.adjava.xmodele.MAttribute, com.a2a.adjava.xmodele.ui.view.MVFLocalization, com.a2a.adjava.xmodele.IDomain, java.lang.String, boolean)
	 */
	@Override
	public MVisualField createVisualField(String p_sPrefix, MLabel p_oLabel, IUITypeDescription p_oTypeVisual,
			MVFModifier p_bMVFModifier, MVFLabelKind p_oLabelKind, MAttribute p_oAttribute,
			MVFLocalization p_oLocalisation, IDomain<IModelDictionary, IModelFactory> p_oDomain,
			String p_sAttributeName, boolean p_bMandatory) {
		return new MW8VisualField(p_sPrefix + (p_bMVFModifier == MVFModifier.READONLY ? "__value" : "__edit"), p_oLabel,
				p_oTypeVisual.getComponentType(p_bMVFModifier), p_oAttribute.getTypeDesc().getEditType(),
				p_oAttribute.getLength(), p_oAttribute.getPrecision(), p_oAttribute.getScale(),
				p_oLabelKind, p_oLocalisation, p_sAttributeName, p_bMandatory, p_oAttribute.getMEnumeration(), p_bMVFModifier == MVFModifier.READONLY, p_oTypeVisual.getUmlName());
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.IModelFactory#createVisualField(java.lang.String, java.lang.String, java.lang.String, com.a2a.adjava.xmodele.ui.view.MVFLabelKind, com.a2a.adjava.xmodele.ui.view.MVFLocalization, java.lang.String, boolean)
	 */
	@Override
	public MVisualField createVisualField(String p_sName, MLabel p_oLabel, String p_sComponent,
			MVFLabelKind p_oLabelKind, MVFLocalization p_oTarget, String p_sAttributeName,
			boolean p_bMandatoryKind) {
		return new MW8VisualField(p_sName, p_oLabel, p_sComponent, p_oLabelKind, p_oTarget,
				p_sAttributeName, p_bMandatoryKind);
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.IModelFactory#createVisualField(java.lang.String, java.lang.String, java.lang.String, com.a2a.adjava.xmodele.ui.view.MVFLabelKind, java.lang.String, boolean)
	 */
	@Override
	public MVisualField createVisualField(String p_sName, MLabel p_oLabel, String p_sComponent,
			MVFLabelKind p_oLabelKind, String p_sAttributeName, boolean p_bMandatoryKind) {
		return new MW8VisualField(p_sName, p_oLabel, p_sComponent, p_oLabelKind, p_sAttributeName,
				p_bMandatoryKind);
	}
	
	@Override
	public MVisualField createVisualField(String p_sPath, MVisualField p_oField, MLabel p_oLabel) {
		return new MW8VisualField(p_sPath, p_oField, p_oLabel);
	}
		
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.IModelFactory#createDaoSignature(String, String, String, ITypeDescription, List, List, boolean)
	 */
	
	@Override
	public MDaoMethodSignature createDaoSignature(String p_sName, String p_sVisibility, String p_sType,
			ITypeDescription p_oReturnedType, List<String> p_listReturnedProperties, List<String> p_listNeedImports,
			boolean p_bByValue) {

		return new MW8DaoMethodSignature(p_sName, p_sVisibility, p_sType, p_oReturnedType, p_listReturnedProperties, p_listNeedImports, p_bByValue);
	}
	
}
