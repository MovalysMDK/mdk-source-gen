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
package com.a2a.adjava.xmodele;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.text.StrSubstitutor;
import org.dom4j.Element;

import com.a2a.adjava.codeformatter.CodeFormatter;
import com.a2a.adjava.extractors.IAnalyserAndProcessorFactory;
import com.a2a.adjava.extractors.MExtractor;
import com.a2a.adjava.languages.LanguageConfiguration;
import com.a2a.adjava.mupdater.MUpdater;
import com.a2a.adjava.projectupgrader.ProjectUpgrader;
import com.a2a.adjava.schema.Schema;
import com.a2a.adjava.umlupdater.UmlUpdater;

/**
 * @author lmichenaud
 *
 * @param <MD>
 * @param <MDF>
 */
public interface IDomain<MD extends IModelDictionary, MDF extends IModelFactory> {

	/**
	 * Return domain name
	 * @return domain name
	 */
	public String getName();
	
	/**
	 * Define domain name
	 * @param p_sName domain name
	 */
	public void setName(String p_sName);
	
	/**
	 * Return dictionary of 
	 * @return model dictionary
	 */
	public MD getDictionnary();
	
	/**
	 * Return language configuration
	 * @return language configuration
	 */
	public LanguageConfiguration getLanguageConf();

	/**
	 * Define language configuration 
	 * @param p_oLanguageConf language configuration 
	 */
	public void setLanguageConf(LanguageConfiguration p_oLanguageConf);
	
	/**
	 * Return model factory
	 * @return model factory
	 */
	public MDF getXModeleFactory();
	
	/**
	 * Define model factory 
	 * @param p_oXModeleFactory model factory 
	 */
	public void setXModeleFactory(MDF p_oXModeleFactory);
	
	/**
	 * Return analyser and processor factory
	 * @return analyser and processor factory
	 */
	public IAnalyserAndProcessorFactory getAnalyserAndProcessorFactory();
	
	/**
	 * Define analyser and processor factory 
	 * @param p_oAnalyserAndProcessorFactory analyser and processor factory 
	 */
	public void setAnalyserAndProcessorFactory(IAnalyserAndProcessorFactory p_oAnalyserAndProcessorFactory);
	
	/**
	 * Return extractors
	 * @return extractors
	 */
	public List<MExtractor> getExtractors();
	
	/**
	 * Return extractor instance
	 * @param p_oClass extractor class
	 * @param <T> subclass of extractor
	 * @return extractor instance
	 */
	public <T extends MExtractor<?>> T getExtractor( Class<T> p_oClass );
	
	/**
	 * Return model
	 * @return model
	 */
	public XModele<MD> getModel();
	
	/**
	 * Define model of domain
	 * @param p_oModel model
	 */
	public void setModel(XModele<MD> p_oModel);
	
	/**
	 * Return list of code formatters 
	 * @return list of code formatters 
	 */
	public List<CodeFormatter> getCodeFormatters();
	
	/**
	 * Return list of project upgraders 
	 * @return list of projects upgraders 
	 */
	public List<ProjectUpgrader> getProjectUpgraders();
	
	/**
	 * Return project list
	 * @return project list
	 */
	public Collection<XProject> getProjects();
	
	/**
	 * Return root package
	 * @return root package
	 */
	public String getRootPackage();
	
	/**
	 * Get string substitutor
	 * @return string substitutor
	 */
	public StrSubstitutor getStrSubstitutor();
	
	/**
	 * Transform the object in XML
	 * @param p_xElement root element to fill with domain node
	 */
	public void toXml(Element p_xElement);
	
	/**
	 * Return template
	 * @return template
	 */
	public String getTemplate();
	
	/**
	 * Return file encoding
	 * @return file encoding
	 */
	public String getFileEncoding();
	
	/**
	 * Define file encoding
	 * @param p_sFileEncoding file encoding
	 */
	public void setFileEncoding(String p_sFileEncoding);
	
	/**
	 * Return global properties
	 * 
	 * @return global properties
	 */
	public Map<String, String> getGlobalParameters();
	
	/**
	 * Define global properties
	 * 
	 * @param p_mapGlobalParameters global properties
	 */
	public void setGlobalParameters(Map<String, String> p_mapGlobalParameters);
	
	/**
	 * Return schema of domain
	 * @return schema of domain
	 */
	public Schema getSchema();
	
	/**
	 * Define schema of domain
	 * @param p_oSchema schema
	 */
	public void setSchema(Schema p_oSchema);
	
	/**
	 * Return uml updaters
	 * @return uml updaters
	 */
	public List<UmlUpdater> getUmlUpdaters();
	
	/**
	 * Return domain updaters
	 * @return domain updaters
	 */
	public List<MUpdater> getMUpdaters();
	
	/**
	 * Get project by name
	 * @param p_sName project name
	 * @return project
	 */
	public XProject getProject( String p_sName );
	
	/**
	 * Remove a project from domain
	 * @param p_sProjectName project name to remove
	 */
	public void removeProject(String p_sProjectName);
	
	/**
	 * Remove all projects 
	 */
	public void removeProjects();
	
	/**
	 * Add a project
	 * @param p_sName project name
	 * @return project
	 */
	public XProject addProject( String p_sName );
	
	/**
	 * Set root package
	 * @param p_sRootPackage root package
	 */
	public void setRootPackage(String p_sRootPackage);
}