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
package com.a2a.adjava.codeformatter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.a2a.adjava.codeformatter.GeneratedFile.XslOutputProperty;


/**
 * Generated file
 * @author lmichenaud
 * @param <O> format options
 */
public class GeneratedFile<O extends FormatOptions> {

	/**
	 * File to generate
	 */
	private File file ;
	
	/**
	 * 
	 */
	private File fileFromRoot ;
		
	/**
	 * Injection mode (file already exists)
	 */
	private boolean injection = false ;
	
	/**
	 * Properties
	 */
	private List<XslOutputProperty> xslProperties = new ArrayList<XslOutputProperty>();
	
	/**
	 * Format options
	 */
	private O formatOptions ;

	/**
	 * Constructor
	 * @param p_oFile file
	 */
	public GeneratedFile(File p_oFile) {
		super();
		this.file = p_oFile;
	}
	
	/**
	 * Constructor
	 * @param p_sFile file
	 */
	public GeneratedFile(String p_sFile) {
		super();
		this.file = new File(p_sFile);
	}

	/**
	 * Constructor
	 * @param p_oFile file
	 * @param p_oFormatOptions format options
	 */
	public GeneratedFile(File p_oFile, O p_oFormatOptions) {
		super();
		this.file = p_oFile;
		this.formatOptions = p_oFormatOptions;
	}

	/**
	 * File
	 * @return file
	 */
	public File getFile() {
		return this.file;
	}
	
	/**
	 * Add a property
	 * @param p_sName property name
	 * @param p_sValue property value
	 */
	public void addXslProperty( String p_sName, String p_sValue ) {
		this.xslProperties.add( new XslOutputProperty(p_sName, p_sValue));
	}

	public void addAllXslProperties( List<XslOutputProperty> list ) {
		this.xslProperties.addAll( list);
	}
	
	/**
	 * Return xsl output properties
	 * @return xsl output properties
	 */
	public List<XslOutputProperty> getXslProperties() {
		return this.xslProperties;
	}
	
	/**
	 * Return format options
	 * @return format options
	 */
	public O getFormatOptions() {
		return this.formatOptions;
	}
	
	/**
	 * Get file with relative path from project root
	 * @return file with relative path from project root
	 */
	public File getFileFromRoot() {
		return fileFromRoot;
	}

	/**
	 * Set file with relative path from project root
	 * @param p_oFileFromRoot file with relative path from project root
	 */
	public void setFileFromRoot(File p_oFileFromRoot) {
		this.fileFromRoot = p_oFileFromRoot;
	}
	
	/**
	 * Is injection mode
	 * @return true if injection mode
	 */
	public boolean isInjection() {
		return this.injection;
	}

	/**
	 * Set injection mode
	 * @param p_bInjection true
	 */
	public void setInjection(boolean p_bInjection) {
		this.injection = p_bInjection;
	}

	/**
	 * {@inheritDoc}
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		GeneratedFile oGeneratedFile = (GeneratedFile) obj ;
		return oGeneratedFile.getFileFromRoot().getPath().equals(this.fileFromRoot.getPath());
	}

	/**
	 * Xsl output property
	 * @author lmichenaud
	 *
	 */
	public static class XslOutputProperty {
		
		/**
		 * Property name
		 */
		private String key ;
		
		/**
		 * Property value
		 */
		private String value ;

		/**
		 * Constructor
		 * @param p_sKey property name
		 * @param p_sValue property value
		 */
		public XslOutputProperty( String p_sKey, String p_sValue ) {
			this.key = p_sKey ;
			this.value = p_sValue ;
		}
		
		/**
		 * Get property name
		 * @return property name
		 */
		public String getKey() {
			return this.key;
		}

		/**
		 * Get property value
		 * @return property value
		 */
		public String getValue() {
			return this.value;
		}
	}
}
