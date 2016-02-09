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
package com.a2a.adjava.types;

import java.util.List;

/**
 * <p>Type de données</p>
 *
 * <p>Copyright (c) 2011
 * <p>Company: Adeuza
 *
 * @author smaitre
 *
 */

public interface ITypeConvertion {

	public String getTo();
	
	public void setTo(String p_sTo);
	
	public String getFormula();
	
	public void setFormula(String p_sFormula);
	
	public String applyFormula(String p_sValue);
	
	public void setImports(List<String> p_lstImports);
	
	public List<String> getImports();
}