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
package com.a2a.adjava.generator.core.incremental;

/**
 * <p>Generated bloc</p>
 *
 * <p>Copyright (c) 2011
 * <p>Company: Adeuza
 *
 * @author lmichenaud
 *
 */
public class NonGeneratedBloc {

	/**
	 * Bloc id 
	 */
	private String id ;
	
	/**
	 * 
	 */
	private boolean allowOverride = false ;
	
	/**
	 * Lines of code
	 */
	private StringBuilder lines ;
	
	/**
	 * 
	 */
	public NonGeneratedBloc( String p_sId ) {
		this.lines = new StringBuilder();
		this.id = p_sId ;
	}

	/**
	 * Add a line
	 * @param p_sLine
	 */
	public void addLine( String p_sLine ) {
		this.lines.append(p_sLine);
		this.lines.append('\n');
	}
	
	/**
	 * @return
	 */
	public String getLines() {
		return this.lines.toString();
	}

	/**
	 * Retourne l'objet id
	 * @return Objet id
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * @return
	 */
	public boolean isAllowOverride() {
		return allowOverride;
	}

	/**
	 * @param p_bAllowOverride
	 */
	public void setAllowOverride(boolean p_bAllowOverride) {
		this.allowOverride = p_bAllowOverride;
	}
}
