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
package com.a2a.adjava.uml2xmodele.extractors.viewmodel.associations;

import java.util.Map;

import com.a2a.adjava.uml.UmlAssociationEnd;
import com.a2a.adjava.uml.UmlAttribute;
import com.a2a.adjava.uml.UmlClass;
import com.a2a.adjava.uml.UmlDictionary;
import com.a2a.adjava.uml2xmodele.extractors.viewmodel.VMPathContext;
import com.a2a.adjava.xmodele.IDomain;
import com.a2a.adjava.xmodele.IModelDictionary;
import com.a2a.adjava.xmodele.IModelFactory;
import com.a2a.adjava.xmodele.MViewModelImpl;

/**
 * Treat a association path of an attribute of the uml viewmodel.
 * Example:
 * Treat b of "a.b.c" and b matches an uml association end.
 * @author lmichenaud
 *
 */
public interface VMAssoPathProcessor {

	/**
	 * Return true if the processor can treat the current path.
	 * @param p_oTarget uml target
	 * @param p_oAsso1 associationEnd 1 of the association
	 * @param p_oAsso2 associationEnd 2 of the association
	 * @param p_sPathElement current element of the attribute path
	 * @param p_sAssoName splitted association end 2 name
	 * @param p_oDictionnary uml dictionnary
	 * @return
	 */
	public boolean isValid(UmlClass p_oTarget, UmlAssociationEnd p_oAsso1, UmlAssociationEnd p_oAsso2,
			String p_sPathElement, String[] p_sAssoName, UmlDictionary p_oDictionnary);

	/**
	 * Apply treatment on the association path.
	 * isValid must be called first to know whether the processor can treat or not this path.
	 * @param p_oVMPathContext VM path context
	 * @param p_oAsso1 associationEnd 1 of the association
	 * @param p_oAsso2 associationEnd 2 of the association
	 * @param p_oAsso1Name splitted name of associationEnd 1
	 * @param p_oAsso2Name splitted name of associationEnd 2
	 * @param p_oUmlAttribute uml attribute of the view model
	 * @param p_oDictionnary uml dictionnary
	 * @param p_oVMCache viewmodel cache
	 * @param p_oDomain domain
	 */
	public void apply(VMPathContext p_oVMPathContext, UmlAssociationEnd p_oAsso1, UmlAssociationEnd p_oAsso2,
			String[] p_oAsso1Name, String[] p_oAsso2Name, UmlAttribute p_oUmlAttribute, UmlDictionary p_oDictionnary,
			Map<String, MViewModelImpl> p_oVMCache, IDomain<IModelDictionary, IModelFactory> p_oDomain) throws Exception ;
}
