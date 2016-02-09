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
package com.a2a.adjava.generator.core.xmlmerge.xa.configuration;

import com.a2a.adjava.generator.core.xmlmerge.xa.configuration.model.XAConfiguration;
import com.a2a.adjava.utils.FileTypeUtils;


public enum XaConfFile {

	LABEL("labels", null, null, null), 
	ANDROID_MANIFEST("manifest", null, null, null),
	IOS_XIB("storyboard", null, null, null),
	IOS_STORYBOARD("storyboard", "com.a2a.adjava.generator.core.xmlmerge.xa.xmlfile.process.StoryBoardXAProcessor", null, null), 
	IOS_PLIST("plist", null, null, null), 
	IOS_CORE_DATA("coredata", null, null, null), 
	IOS_TYPHOON_CONFIG("typhoon", null, null, null),
	HTML5_JSON_CONFIG("json", null, null, null);
	

	XaConfFile(String _name, String p_sPreProcessor, String p_sPreCopyFileProcessor,String p_sPostCopyFileProcessor){
		this.name=_name;
		this.preProcessor = p_sPreProcessor;
		this.preCopyFileProcessor = p_sPreCopyFileProcessor;
		this.postCopyFileProcessor = p_sPostCopyFileProcessor;
	}
	
	private String name;
	private String preCopyFileProcessor = null;
	private String postCopyFileProcessor = null;
	private String preProcessor = null;
	
	private final static String EXTENSION="xml";
	
	public String getName(){
		return name+FileTypeUtils.EXTENSION_SEPARATOR+EXTENSION;
	}

	public String toString(){
		return name+FileTypeUtils.EXTENSION_SEPARATOR+EXTENSION;
	}
	
	public boolean equals(String p_sName){
		return this.getName().equalsIgnoreCase(p_sName);
	}
	
	public static String getExtension(){
		return EXTENSION;
	}
	
	public boolean requiresSiblingKeyGrouping(){
		return XAConfiguration.getInstance().requiresSiblingKeyGrouping(this.getName());
	}

	/**
	 * Returns the name of the 'keyForSiblings' node defined in this configuration file
	 * 
	 * @return String key
	 */
	public String getKeyForSiblingsNodeName(){
		return XAConfiguration.getInstance().getKeyForSiblingsNodeName(this.getName());
	}
	
	/**
	 * Get file processor
	 * 
	 * @return String preCopyFileProcessor
	 */
	public String getPreCopyFileProcessor() {
		return preCopyFileProcessor;
	}

	/**
	 * Get file processor
	 * 
	 * @return String postCopyFileProcessor
	 */
	public String getPostCopyFileProcessor() {
		return postCopyFileProcessor;
	}

	/**
	 * Get processor
	 * 
	 * @return String preProcessor
	 */
	public String getPreProcessor() {
		return preProcessor;
	}

}
