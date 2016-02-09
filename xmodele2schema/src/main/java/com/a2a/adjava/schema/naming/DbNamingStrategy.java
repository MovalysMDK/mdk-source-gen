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
package com.a2a.adjava.schema.naming;

import java.util.Map;

import com.a2a.adjava.schema.Field;
import com.a2a.adjava.schema.Table;

public interface DbNamingStrategy {

	/**
	 * @param p_mapOptions
	 */
	public void setOptions( Map<String,String> p_mapOptions );
	
	/**
	 * @return
	 */
	public int getIdentifierMaxLength();
	
	/**
	 * @param p_sClassName
	 * @return
	 */
	public String getTableName( String p_sClassName );
	
	
	/**
	 * @param p_sClassName
	 * @param p_sAttributeName
	 * @return
	 */
	public String getColumnName( String p_sClassName, String p_sAttributeName );


	/**
	 * @param class1
	 * @param attribute
	 * @return
	 */
	public String getSequenceName( String p_sClassName, String p_sAttributeName );


	/**
	 * @param associationEnd
	 * @return
	 */
	public String getFKColumnName( String p_sAssociationEndName, Field p_oField );
	
	/**
	 * @param associationEnd
	 * @return
	 */
	public String getFKName( String p_sAssociationEndName, Table p_oTableRef );
	
	/**
	 * @param associationEnd
	 * @return
	 */
	public String getIndexName( Table p_oTable, String p_sAssociationEndName );
	
	/**
	 * @param p_sTableName
	 * @return
	 */
	public String getPKNameFromTableName( String p_sTableName );

	/**
	 * @param p_sTableName
	 * @param p_sName
	 * @return
	 */
	public String getUniqueKeyName(String p_sTableName, String p_sColumnName);

}

