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
package com.a2a.adjava.extractors;

import com.a2a.adjava.uml.UmlClass;
import com.a2a.adjava.uml.UmlDictionary;
import com.a2a.adjava.xmodele.MPage;

public interface ICUDActionProcessor {
	/**
	 * 
	 * @param p_oObject
	 * @param p_oUmlDict
	 * @throws Exception
	 */
	void treatCUDOperations(Object p_oObject, UmlDictionary p_oUmlDict ) throws Exception;
	
	/**
	 * 
	 * @param p_oPage
	 * @param p_oObject
	 * @throws Exception
	 */
	void treatCUDOperationsForListPanel( MPage p_oPage, Object p_oObject ) throws Exception;
	
	/**
	 * 
	 * @param p_oPage
	 * @param p_oPanelAggregation
	 * @param p_oScreenUmlClass
	 * @param p_oObject
	 * @throws Exception
	 */
	void treatCUDOperationForDetailPanel( MPage p_oPage, Object p_oPanelAggregation,
			UmlClass p_oScreenUmlClass, Object p_oObject ) throws Exception;
	
	/**
	 * 
	 * @param p_oPage
	 * @param p_oScreenUmlClass
	 * @param p_oObject
	 */
	void treatCUDOperationForSearchPanel(MPage p_oPage, UmlClass p_oScreenUmlClass, Object p_oObject);
}
