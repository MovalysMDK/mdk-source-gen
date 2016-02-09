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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
 * <p>
 * 	Domaine de l'application.
 * 	On peut avoir un domaine Android et un domaine Back office avec une configuration propre, etc.
 * </p>
 *
 * <p>Copyright (c) 2011</p>
 * <p>Company: Adeuza</p>
 *
 * @author lmichenaud
 * @param <MD> Model dictionnary
 * @param <MDF> Model dictionnary factory
 */
public class XDomain<MD extends IModelDictionary, MDF extends IModelFactory> implements IDomain<MD, MDF> {

	/**
	 * Domain name
	 */
	private String name ;

	/**
	 * Domain model
	 */
	private XModele<MD> model ;
	
	/**
	 * Schema
	 */
	private Schema schema ;
	
	/**
	 * Template 
	 */
	private String template ;
	
	/**
	 * File Encoding
	 */
	private String fileEncoding ;
	
	/**
	 * Root package for domain
	 */
	private String rootPackage ;
	
	/**
	 * Uml updaters
	 */
	private List<UmlUpdater> umlUpdaters = new ArrayList<UmlUpdater>();
	
	/**
	 * MUpdater list 
	 */
	private List<MUpdater> mupdaters = new ArrayList<MUpdater>();
	
	/**
	 * XModele factory
	 */
	private MDF xModeleFactory ;
	
	/**
	 * AnalyserAndProcessor factory
	 */
	private IAnalyserAndProcessorFactory xAnalyserAndProcessorFactory ;
	
	/**
	 * Extractors
	 */
	private List<MExtractor> extractors = new ArrayList<MExtractor>();
	
	/**
	 * Code formatter 
	 */
	private List<CodeFormatter> codeFormatters = new ArrayList<CodeFormatter>();
	
	/**
	 * Code formatter 
	 */
	private List<ProjectUpgrader> projectUpgraders = new ArrayList<ProjectUpgrader>();
	
	/**
	 * Language Configuration
	 */
	private LanguageConfiguration languageConf ;
	
	/**
	 * Project
	 */
	private Map<String,XProject> projects = new HashMap<String,XProject>(); 
	
	/**
	 * Substitutor
	 */
	private Map<String,String> substitutorParams = new HashMap<String,String>();
	
	/**
	 * Global parameters
	 */
	private Map<String, String> globalParameters ;
	
	
	/**
	 * Constructor
	 */
	public XDomain() {
		// empty constructor
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.IDomain#getName()
	 */
	@Override
	public String getName() {
		return this.name;
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.IDomain#setName(java.lang.String)
	 */
	@Override
	public void setName(String p_sName) {
		this.name = p_sName;
		this.substitutorParams.put("domain", this.name);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override	
	public XModele<MD> getModel() {
		return this.model;
	}
	
	/**
	 * Return dictionary of 
	 * @return model dictionary
	 */
	@Override
	public MD getDictionnary() {
		return this.model.getModelDictionnary();
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.IDomain#getMUpdaters()
	 */
	@Override
	public List<MUpdater> getMUpdaters() {
		return this.mupdaters;
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.IDomain#getUmlUpdaters()
	 */
	@Override
	public List<UmlUpdater> getUmlUpdaters() {
		return this.umlUpdaters;
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.IDomain#getSchema()
	 */
	@Override
	public Schema getSchema() {
		return this.schema;
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.IDomain#setSchema(com.a2a.adjava.schema.Schema)
	 */
	@Override
	public void setSchema(Schema p_oSchema) {
		this.schema = p_oSchema;
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.IDomain#getTemplate()
	 */
	@Override
	public String getTemplate() {
		return this.template;
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.IDomain#getFileEncoding()
	 */
	@Override
	public String getFileEncoding() {
		return this.fileEncoding;
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.IDomain#setFileEncoding(java.lang.String)
	 */
	@Override	
	public void setFileEncoding(String p_sFileEncoding) {
		this.fileEncoding = p_sFileEncoding;
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.IDomain#getXModeleFactory()
	 */
	@Override
	public MDF getXModeleFactory() {
		return this.xModeleFactory;
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.IDomain#setXModeleFactory(com.a2a.adjava.xmodele.IModelFactory)
	 */
	@Override
	public void setXModeleFactory(MDF p_oXModeleFactory) {
		this.xModeleFactory = p_oXModeleFactory;
		this.model = (XModele<MD>) this.xModeleFactory.createXModele();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<MExtractor> getExtractors() {
		return this.extractors;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T extends MExtractor<?>> T getExtractor( Class<T> p_oClass ) {
		T r_oExtractor = null ;
		for( MExtractor oExtractor : this.extractors ) {
			if ( p_oClass.isAssignableFrom(oExtractor.getClass()))  {
				r_oExtractor = (T) oExtractor ;
				break ;
			}
		}
		return r_oExtractor ;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<CodeFormatter> getCodeFormatters() {
		return this.codeFormatters;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<ProjectUpgrader> getProjectUpgraders() {
		return this.projectUpgraders;
	}

	/**
	 * Define list of code formatters 
	 * @param p_listCodeFormatters list of code formatters
	 */
	public void setCodeFormatters(List<CodeFormatter> p_listCodeFormatters) {
		this.codeFormatters = p_listCodeFormatters;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LanguageConfiguration getLanguageConf() {
		return this.languageConf;
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.IDomain#setLanguageConf(com.a2a.adjava.languages.LanguageConfiguration)
	 */
	@Override
	public void setLanguageConf(LanguageConfiguration p_oLanguageConf) {
		this.languageConf = p_oLanguageConf;
	}

	/**
	 * Define template
	 * @param p_sTemplate template
	 */
	public void setTemplate(String p_sTemplate) {
		this.template = p_sTemplate;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<XProject> getProjects() {
		return this.projects.values();
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.IDomain#getProject(java.lang.String)
	 */
	@Override
	public XProject getProject( String p_sName ) {
		return this.projects.get(p_sName);
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.IDomain#addProject(java.lang.String)
	 */
	@Override
	public XProject addProject( String p_sName ) {
		XProject r_xProject = this.createProject();
		r_xProject.setName(p_sName);
		r_xProject.setDomain(this);
		this.projects.put(p_sName, r_xProject);
		return r_xProject;
	}
	
	/**
	 * Create an new project instance
	 * @return new project instance
	 */
	protected XProject createProject() {
		return new XProject<IDomain<? extends IModelDictionary,? extends IModelFactory>>();
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.IDomain#removeProject(java.lang.String)
	 */
	@Override
	public void removeProject(String p_sProjectName) {
		this.projects.remove(p_sProjectName);
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.IDomain#removeProjects()
	 */
	@Override	
	public void removeProjects() {
		this.projects.clear();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public StrSubstitutor getStrSubstitutor() {
		return new StrSubstitutor(this.substitutorParams);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getRootPackage() {
		return this.rootPackage;
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.IDomain#setRootPackage(java.lang.String)
	 */
	@Override
	public void setRootPackage(String p_sRootPackage) {
		this.rootPackage = p_sRootPackage;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<String, String> getGlobalParameters() {
		return this.globalParameters;
	}

	/**
	 * {@inheritDoc}
	 * @param p_mapGlobalParameters global properties
	 */
	@Override
	public void setGlobalParameters(Map<String, String> p_mapGlobalParameters) {
		this.globalParameters = p_mapGlobalParameters;
		for( Entry<String,String> oParam: p_mapGlobalParameters.entrySet()) {
			this.substitutorParams.put(oParam.getKey(), oParam.getValue());
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.IDomain#toXml(org.dom4j.Element)
	 */
	@Override	
	public void toXml(Element p_xElement) {
		Element xDomain = p_xElement.addElement("domain");
		xDomain.addElement("name").setText(this.getName());
		xDomain.addElement("rootPackage").setText(this.getRootPackage());
		xDomain.addElement("fileEncoding").setText(this.getFileEncoding());
	}

	/**
	 * Define model of domain
	 * @see com.a2a.adjava.xmodele.IDomain#setModel(com.a2a.adjava.xmodele.XModele)
	 */
	@Override
	public void setModel(XModele<MD> p_oModel) {
		this.model = p_oModel;
	} 

	@Override
	public IAnalyserAndProcessorFactory getAnalyserAndProcessorFactory() {
		return xAnalyserAndProcessorFactory;
	}

	@Override
	public void setAnalyserAndProcessorFactory(
			IAnalyserAndProcessorFactory xAnalyserAndProcessorFactory) {
		this.xAnalyserAndProcessorFactory = xAnalyserAndProcessorFactory;
	}
}
