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
package com.a2a.adjava.generators;

import java.util.ArrayList;
import java.util.List;

import com.a2a.adjava.codeformatter.GeneratedFile;

/**
 * Global generator context (for all domains)
 * @author lmichenaud
 *
 */
public class GeneratorContext {

	/**
	 * Domain generator context
	 */
	private List<DomainGeneratorContext> domainGeneratorContexts = new ArrayList<DomainGeneratorContext>();
	
	/**
	 * Get generated file names
	 * @return generated file names
	 */
	public List<String> getGeneratedFilenames() {
		List<String> r_listGeneratedFiles = new ArrayList<String>();
		for( DomainGeneratorContext oDomainContext : this.domainGeneratorContexts ) {
			r_listGeneratedFiles.addAll(oDomainContext.getGeneratedFilenames());
		}
		return r_listGeneratedFiles;
	}

	/**
	 * Get old list of generated files
	 * @return old list of generated files
	 */
	public List<GeneratedFile> getOldGeneratedFiles() {
		List<GeneratedFile> r_listGeneratedFiles = new ArrayList<GeneratedFile>();
		for( DomainGeneratorContext oDomainContext : this.domainGeneratorContexts ) {
			r_listGeneratedFiles.addAll(oDomainContext.getOldGeneratedFiles());
		}
		return r_listGeneratedFiles;
	}
	
	
	/**
	 * Get list of generated files
	 * @return list of generated files
	 */
	public List<GeneratedFile> getGeneratedFiles() {
		List<GeneratedFile> r_listGeneratedFiles = new ArrayList<GeneratedFile>();
		for( DomainGeneratorContext oDomainContext : this.domainGeneratorContexts ) {
			r_listGeneratedFiles.addAll(oDomainContext.getGeneratedFiles());
		}
		return r_listGeneratedFiles;
	}
	
	/**
	 * Register a domain generator context
	 * @param p_oDomainGeneratorContext domain generator context
	 */
	public void registerDomainContext(
			DomainGeneratorContext p_oDomainGeneratorContext) {
		this.domainGeneratorContexts.add(p_oDomainGeneratorContext);
	}
}
