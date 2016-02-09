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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.a2a.adjava.codeformatter.GeneratedFile;

/**
 * Context for generator of domains
 * @author lmichenaud
 *
 */
public class DomainGeneratorContext {

	/**
	 * Attributes
	 */
	private Map<String, Object> attributes = new HashMap<String, Object>();
	
	/**
	 * Generated files
	 */
	private List<GeneratedFile> generatedFiles = new ArrayList<GeneratedFile>();
	
	/**
	 * Old generated files (in the previous generation)
	 */
	private List<GeneratedFile> oldGeneratedFiles = new ArrayList<GeneratedFile>();
	
	/**
	 * Constructor
	 * @param p_oGeneratorContext generator context
	 * @param p_oldGeneratedFileList old list of generated files
	 */
	public DomainGeneratorContext( GeneratorContext p_oGeneratorContext, List<GeneratedFile> p_oldGeneratedFileList ) {
		p_oGeneratorContext.registerDomainContext(this);
		this.oldGeneratedFiles = p_oldGeneratedFileList ;
	}
	
	/**
	 * Get attribute by name
	 * @param p_sName attribute name
	 * @return attribute
	 */
	public Object getAttr( String p_sName ) {
		return this.attributes.get( p_sName );
	}
	
	/**
	 * Set attribute value
	 * @param p_sName attribute name
	 * @param p_oValue attribute value
	 */
	public void setAttr( String p_sName, Object p_oValue ) {
		this.attributes.put(p_sName, p_oValue);
	}
	
	/**
	 * Add a generated file
	 * @param p_oFile generated file
	 */
	public void addGeneratedFile( GeneratedFile p_oFile ) {
		this.generatedFiles.add(p_oFile);
	}
	
	/**
	 * Get generated filenames
	 * @return generated filenames
	 */
	public List<String> getGeneratedFilenames() {
		List<String> listGeneratedFiles = new ArrayList<String>();
		for( GeneratedFile oFileName : this.generatedFiles ) {
			listGeneratedFiles.add(oFileName.getFileFromRoot().getPath());
		}
		return listGeneratedFiles;
	}
	
	/**
	 * Get generated files
	 * @return generated files
	 */
	public List<GeneratedFile> getGeneratedFiles() {
		return this.generatedFiles ;
	}

	/**
	 * Get old list of generated files
	 * @return old list of generated files
	 */
	public List<GeneratedFile> getOldGeneratedFiles() {
		return this.oldGeneratedFiles;
	}
}
