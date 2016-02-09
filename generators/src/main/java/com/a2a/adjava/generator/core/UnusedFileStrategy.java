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
package com.a2a.adjava.generator.core;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Map;

import com.a2a.adjava.generators.GeneratorContext;

/**
 * Strategy to manage files than are not anymore generated (compared to last generation)
 * @author lmichenaud
 *
 */
public interface UnusedFileStrategy {

	/**
	 * Treat unsuned files
	 * @param p_listFiles unused files
	 * @param p_oGenerationDate generation date
	 * @throws Exception
	 */
	public void treatUnusedFiles( Collection<String> p_listFiles, Timestamp p_oGenerationDate, GeneratorContext p_oContext ) throws Exception ;
	
	/**
	 * Called after adjava initialization 
	 * @param p_mapGlobalProperties properties of adjava maven plugin
	 * @throws Exception
	 */
	public void afterInitialization(Map<String,String> p_mapGlobalProperties ) throws Exception ;
}
