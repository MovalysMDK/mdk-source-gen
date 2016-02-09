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
package com.a2a.adjava.commons.init;

import java.util.Map;

import org.dom4j.Element;

/**
 * <p>Initializing bean with xml configuration</p>
 *
 * <p>Copyright (c) 2011
 * <p>Company: Adeuza
 *
 * @author lmichenaud
 *
 */

public interface InitializingBean {

	/**
	 * Initializing bean with xml configuration. Can be called several times if multiples configuration xml.
	 * @param p_xElement xml conf
	 * @param p_mapGlobalProperties global properties
	 * @throws Exception exception
	 */
	public void initialize( Element p_xElement, Map<String,String> p_mapGlobalProperties ) throws Exception;
	
	/**
	 * Called at the end of initialization
	 * @param p_mapGlobalProperties global properties
	 * @throws Exception exception
	 */
	public void afterInitialization( Map<String,String> p_mapGlobalProperties ) throws Exception ;
}
