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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.AdjavaException;
import com.a2a.adjava.codeformatter.CodeFormatter;
import com.a2a.adjava.commons.init.InitializingBean;
import com.a2a.adjava.extractors.ExtractorParams;
import com.a2a.adjava.extractors.IAnalyserAndProcessorFactory;
import com.a2a.adjava.extractors.MExtractor;
import com.a2a.adjava.generators.ResourceGenerator;
import com.a2a.adjava.languages.LanguageConfiguration;
import com.a2a.adjava.languages.LanguageRegistry;
import com.a2a.adjava.messages.MessageHandler;
import com.a2a.adjava.mupdater.MUpdater;
import com.a2a.adjava.projectupgrader.ProjectUpgrader;
import com.a2a.adjava.projectupgrader.ProjectUpgrader.ProjectUpgraderMode;
import com.a2a.adjava.umlupdater.UmlUpdater;
import com.a2a.adjava.utils.BeanUtils;

/**
 * <p>
 * MDomain registry
 * </p>
 * 
 * <p>
 * Copyright (c) 2011
 * <p>
 * Company: Adeuza
 * 
 * @author lmichenaud
 * 
 */

public class XDomainRegistry implements InitializingBean {

	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(XDomainRegistry.class);

	/**
	 * Domains
	 */
	private List<IDomain> domains = new ArrayList<IDomain>();

	/**
	 * Domains by name
	 */
	private Map<String, IDomain> mapDomains = new HashMap<String, IDomain>();

	/**
	 * Register of languages
	 */
	private LanguageRegistry languageRegistry;
	
	/**
	 * Constructor
	 * @param p_oLanguageRegistry language registry
	 */
	public XDomainRegistry(LanguageRegistry p_oLanguageRegistry) {
		this.languageRegistry = p_oLanguageRegistry;
	}

	/**
	 * Return domains
	 * 
	 * @return domain list
	 */
	public List<IDomain> getDomains() {
		return this.domains;
	}

	/**
	 * Add a domain to registry
	 * 
	 * @param p_oDomain
	 *            domain to add
	 */
	private void addDomain(IDomain<? extends IModelDictionary, ? extends IModelFactory> p_oDomain) {
		this.domains.add(p_oDomain);
		this.mapDomains.put(p_oDomain.getName(), p_oDomain);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.a2a.adjava.project.InitializingBean#initialize(org.dom4j.Element)
	 */
	@Override
	public void initialize(Element p_xRoot, Map<String,String> p_mapGlobalProperties) throws Exception {

		Element xDomains = p_xRoot.element("domains");
		if (xDomains != null) {
			for (Element xDomain : (List<Element>) xDomains.elements("domain")) {
				String sDomainName = xDomain.attributeValue("name");
				IDomain oDomain = mapDomains.get(sDomainName);
				if (oDomain == null) {
					String sDomainClass = xDomain.attributeValue("class");
					if ( sDomainClass == null ) {
						oDomain = new XDomain<ModelDictionary,XModeleFactory>();
					}
					else {
						oDomain = (IDomain<IModelDictionary, IModelFactory>) Class.forName(sDomainClass).newInstance();
					}
					oDomain.setName(sDomainName);
					oDomain.setGlobalParameters(p_mapGlobalProperties);
					this.addDomain(oDomain);
				}

				BeanUtils.setStringIfNotNull(oDomain, "template", xDomain.elementText("template"));
				BeanUtils.setStringIfNotNull(oDomain, "fileEncoding", xDomain.elementText("file-encoding"));

				String sLanguageRef = xDomain.elementText("language-ref");
				if (sLanguageRef != null) {
					LanguageConfiguration oLngConf = this.languageRegistry.getLanguageConfiguration(sLanguageRef);
					if (oLngConf != null) {
						oDomain.setLanguageConf(oLngConf);
					} else {
						throw new AdjavaException("Can't find language configuration named '{}' for domain '{}'", sLanguageRef, sDomainName );
					}

				}

				// factory xmodele for the domain
				String sModeleFactoryClass = xDomain.elementText("xmodele-factory");
				if (sModeleFactoryClass != null) {
					sModeleFactoryClass = sModeleFactoryClass.trim();
					oDomain.setXModeleFactory((IModelFactory) Class.forName(sModeleFactoryClass).newInstance());
				}
				
				// factory analyser and processoe for the domain
				String sAnalyserAndProcessorClass = xDomain.elementText("analyser-and-processor-factory");
				if (sAnalyserAndProcessorClass == null){
					sAnalyserAndProcessorClass = "com.a2a.adjava.uml2xmodele.extractors.AnalyserAndProcessorFactory";
				}
				if (sAnalyserAndProcessorClass != null) {
					sAnalyserAndProcessorClass = sAnalyserAndProcessorClass.trim();
					oDomain.setAnalyserAndProcessorFactory((IAnalyserAndProcessorFactory) Class.forName(sAnalyserAndProcessorClass).newInstance());
				}
				
				this.readUmlUpdaters(xDomain, oDomain);
				this.readMUpdaters(xDomain, oDomain);
				this.readExtractors(xDomain, oDomain);
				this.readProjects(xDomain, oDomain );
				this.readCodeFormatters(xDomain, oDomain);
				this.readProjectUpgraders(xDomain, oDomain);
			}
		}
	}

	/**
	 * Read uml updaters of domain
	 * @param p_xRoot domain node
	 * @param p_oDomain domain
	 * @throws Exception failure
	 */
	private void readUmlUpdaters(Element p_xRoot, IDomain<? extends IModelDictionary, ? extends IModelFactory> p_oDomain) throws Exception {
		
		Element xUmlUpdaters = p_xRoot.element("uml-updaters");
		if (xUmlUpdaters != null) {

			String sReplace = xUmlUpdaters.attributeValue("replace");
			if (sReplace != null && Boolean.parseBoolean(sReplace)) {
				p_oDomain.getUmlUpdaters().clear();
			}

			for (Element xUmlUpdater : (List<Element>) xUmlUpdaters.elements("uml-updater")) {
				String sGenClass = xUmlUpdater.attributeValue("class");
				UmlUpdater oUmlUpdater = (UmlUpdater) Class.forName(sGenClass).newInstance();
				Map<String, String> oParametersMap = new HashMap<String, String>();
				for (Element xProperty : (List<Element>) xUmlUpdater.elements("property")) {
					String sName = xProperty.attributeValue("name");
					String sValue = xProperty.attributeValue("value");
					oParametersMap.put(sName, sValue);
				}
				oUmlUpdater.setParametersMap(oParametersMap);
				p_oDomain.getUmlUpdaters().add(oUmlUpdater);
			}
		}
	}
	
	/**
	 * Read configuration of updaters
	 * 
	 * @param p_xRoot xml node of domain
	 * @param p_oDomain domain
	 * @throws Exception exception
	 */
	private void readMUpdaters(Element p_xRoot, IDomain<? extends IModelDictionary, ? extends IModelFactory> p_oDomain) throws Exception {
		Element xMUpdaters = p_xRoot.element("m-updaters");
		if (xMUpdaters != null) {

			String sReplace = xMUpdaters.attributeValue("replace");
			if (sReplace != null && Boolean.parseBoolean(sReplace)) {
				p_oDomain.getMUpdaters().clear();
			}

			for (Element xMUpdater : (List<Element>) xMUpdaters.elements("m-updater")) {
				String sGenClass = xMUpdater.attributeValue("class");
				MUpdater oMUpdater;
				try {
					oMUpdater = (MUpdater) Class.forName(sGenClass).newInstance();
				} catch (ClassNotFoundException oClassNotFoundException) {
					throw new AdjavaException("Can't find mupdater class: {}", sGenClass);
				}
				Map<String, String> oParametersMap = new HashMap<String, String>();
				for (Element xProperty : (List<Element>) xMUpdater.elements("property")) {
					String sName = xProperty.attributeValue("name");
					String sValue = xProperty.attributeValue("value");
					oParametersMap.put(sName, sValue);
				}
				oMUpdater.setParametersMap(oParametersMap);
				p_oDomain.getMUpdaters().add(oMUpdater);
			}
		}
	}

	/**
	 * Read configuration of extractors
	 * 
	 * @param p_xRoot xml node
	 * @param p_oDomain domain
	 * @throws Exception exception
	 */
	private void readExtractors(Element p_xRoot, IDomain<? extends IModelDictionary, ? extends IModelFactory> p_oDomain) throws Exception {
		Element xExtractors = p_xRoot.element("extractors");
		if (xExtractors != null) {

			String sReplace = xExtractors.attributeValue("replace");
			if (sReplace != null && Boolean.parseBoolean(sReplace)) {
				p_oDomain.getExtractors().clear();
			}

			for (Element xExtractor : (List<Element>) xExtractors.elements("extractor")) {
				String sGenClass = xExtractor.attributeValue("class");
				MExtractor oExtractor;
				try {
					oExtractor = (MExtractor) Class.forName(sGenClass).newInstance();
				} catch (ClassNotFoundException oClassNotFoundException) {
					throw new AdjavaException("Can't find mupdater class: {}", sGenClass);
				}
				ExtractorParams oExtractorParams = new ExtractorParams();
				for (Element xProperty : (List<Element>) xExtractor.elements("property")) {
					String sName = xProperty.attributeValue("name");
					String sValue = xProperty.attributeValue("value");
					oExtractorParams.setValue(sName, sValue);
				}
				oExtractor.setParameters(oExtractorParams);
				oExtractor.setDomain(p_oDomain);
				oExtractor.setDomainRegistry(this);
				oExtractor.initialize(xExtractor.element("configuration"));
				p_oDomain.getExtractors().add(oExtractor);
			}
		}
	}

	/**
	 * Read configuration of projects
	 * 
	 * @param p_xDomain xml node of domain
	 * @param p_oDomain domain
	 * @throws Exception exception
	 */
	private void readProjects(Element p_xDomain, IDomain<? extends IModelDictionary, ? extends IModelFactory> p_oDomain) throws Exception {
		Element xProjects = p_xDomain.element("projects");
		if (xProjects != null) {
			// Test si on doit completer la liste, ou si on doit la remplacer entierement
			String sReplace = xProjects.attributeValue("replace");
			if (sReplace != null && Boolean.parseBoolean(sReplace)) {
				p_oDomain.removeProjects();
			}
			
			for (Element xProject : (List<Element>) xProjects.elements("project")) {
				String sProjectName = xProject.attributeValue("name");
				String sRemove = xProject.attributeValue("remove");
				boolean bRemove = sRemove != null && Boolean.parseBoolean(sRemove);
				if (!bRemove) {
					XProject oProject = p_oDomain.getProject(sProjectName);
					if ( oProject == null ) {
						oProject = p_oDomain.addProject(sProjectName);
					}
										
					BeanUtils.setIfNotNull( oProject, "baseDir", p_oDomain.getStrSubstitutor().replace(xProject.attributeValue("base-dir")));
					BeanUtils.setIfNotNull( oProject, "sourceDir", xProject.elementText("source-dir"));
					BeanUtils.setIfNotNull( oProject, "assetsDir", xProject.elementText("assets-dir"));
					BeanUtils.setIfNotNull( oProject,"stringDir", xProject.elementText("string-dir"));
					BeanUtils.setIfNotNull( oProject, "ddlDir", xProject.elementText("ddl-dir"));
					BeanUtils.setIfNotNull( oProject, "layoutDir", xProject.elementText("layout-dir"));
					this.readGenerators(xProject, oProject);
				}
				else {
					p_oDomain.removeProject(sProjectName);
				}
			}
		}
	}

	/**
	 * Read configuration of generators
	 * @param p_xProject xml node of project
	 * @param p_oProject project
	 * @throws Exception exception
	 */
	private void readGenerators(Element p_xProject, XProject p_oProject ) throws Exception {
		Element xGenerators = p_xProject.element("resource-generators");
		if (xGenerators != null) {

			// Test si on doit completer la liste, ou si on doit la remplacer entierement
			String sReplace = xGenerators.attributeValue("replace");
			if (sReplace != null && Boolean.parseBoolean(sReplace)) {
				p_oProject.getGenerators().clear();
			}

			for (Element xGenerator : (List<Element>) xGenerators.elements("generator")) {
				String sGenClass = xGenerator.attributeValue("class");
				String sRemove = xGenerator.attributeValue("remove");
				
				boolean bRemove = sRemove != null && Boolean.parseBoolean(sRemove);
				if (!bRemove) {
					ResourceGenerator<?> oResourceGenerator = (ResourceGenerator<?>) Class.forName(sGenClass).newInstance();
					Map<String, String> oParametersMap = new HashMap<String, String>();
					for (Element xProperty : (List<Element>) xGenerator.elements("property")) {
						String sName = xProperty.attributeValue("name");
						String sValue = xProperty.attributeValue("value");
						oParametersMap.put(sName, sValue);
					}
					oResourceGenerator.setParametersMap(oParametersMap);
					p_oProject.getGenerators().add(oResourceGenerator);
				} else {
					if (!removeResourceGenerator(sGenClass, p_oProject)) {
						log.warn("The generator '{}' has not been found and could not be disabled", sGenClass);
					}
				}
			}
		}
	}

	/**
	 * Read configuration of code formatters
	 * 
	 * @param p_xRoot xml node
	 * @param p_oDomain domain
	 * @throws Exception exception
	 */
	private void readCodeFormatters(Element p_xRoot, IDomain<? extends IModelDictionary, ? extends IModelFactory> p_oDomain) throws Exception {
		Element xGenerators = p_xRoot.element("code-formatters");
		if (xGenerators != null) {

			// Test si on doit completer la liste, ou si on doit la remplacer entierement
			String sReplace = xGenerators.attributeValue("replace");
			if (sReplace != null && Boolean.parseBoolean(sReplace)) {
				p_oDomain.getCodeFormatters().clear();
			}

			for (Element xGenerator : (List<Element>) xGenerators.elements("code-formatter")) {
				String sGenClass = xGenerator.attributeValue("class");
				if ( sGenClass != null ) {
					CodeFormatter oCodeFormatter = (CodeFormatter) Class.forName(sGenClass).newInstance();
					p_oDomain.getCodeFormatters().add(oCodeFormatter);
					
					for (Element xProperty : (List<Element>) xGenerator.elements("property")) {
						String sName = xProperty.attributeValue("name");
						String sValue = xProperty.attributeValue("value");
						BeanUtils.setStringIfNotNull(oCodeFormatter, sName, sValue);
					}
				}
			}
		}
	}	
	
	/**
	 * Read configuration for project upgraders
	 * @param p_xRoot xml node
	 * @param p_oDomain domain
	 * @throws Exception exception
	 */
	private void readProjectUpgraders(Element p_xRoot, IDomain<? extends IModelDictionary, ? extends IModelFactory> p_oDomain) throws Exception {
		Element xGenerators = p_xRoot.element("project-upgraders");
		if (xGenerators != null) {

			// Test si on doit completer la liste, ou si on doit la remplacer entierement
			String sReplace = xGenerators.attributeValue("replace");
			if (sReplace != null && Boolean.parseBoolean(sReplace)) {
				p_oDomain.getProjectUpgraders().clear();
			}

			for (Element xGenerator : (List<Element>) xGenerators.elements("project-upgrader")) {
				String sGenClass = xGenerator.attributeValue("class");
				String sMode = xGenerator.attributeValue("mode");
				if ( sGenClass != null ) {
					ProjectUpgrader oProjectUpgrader = (ProjectUpgrader) Class.forName(sGenClass).newInstance();
					if(sMode == null || sMode.isEmpty() || sMode.equals(ProjectUpgraderMode.PROJECT_UPGRADER_MODE_BEFORE_GEN.getCode())) {
						oProjectUpgrader.setMode(ProjectUpgraderMode.PROJECT_UPGRADER_MODE_BEFORE_GEN);
					}
					else if(sMode.equals(ProjectUpgraderMode.PROJECT_UPGRADER_MODE_AFTER_GEN.getCode())) {
						oProjectUpgrader.setMode(ProjectUpgraderMode.PROJECT_UPGRADER_MODE_AFTER_GEN);
					}
					p_oDomain.getProjectUpgraders().add(oProjectUpgrader);
					
					Map<String, String> oParametersMap = new HashMap<String, String>();
					for (Element xProperty : (List<Element>) xGenerator.elements("property")) {
						String sName = xProperty.attributeValue("name");
						String sValue = xProperty.attributeValue("value");
						if(sName.equalsIgnoreCase("similar-upgrade-versions")) {
							 List<Element> items = xProperty.elements();
							 List<String> similarProjectUpgraders = new ArrayList<String>();
							 for (Element xUpgrader : (List<Element>) xGenerator.elements("upgrader")) {
								 similarProjectUpgraders.add(xUpgrader.attributeValue("class"));
							 }
							 oProjectUpgrader.setSimilarProjectUpgraders(similarProjectUpgraders);
						}
						else {
							oParametersMap.put(sName, sValue);
						}
					}
					oProjectUpgrader.setParametersMap(oParametersMap);
				}
			}
		}
	}	
	
	
	/**
	 * Remove a resource generator from project
	 * 
	 * @param p_sGenClass generator class
	 * @param p_oProject project
	 * @return true if removed
	 */
	public boolean removeResourceGenerator(String p_sGenClass, XProject p_oProject) {
		boolean r_bTrouve = false;
		Iterator<ResourceGenerator<?>> iterGen = p_oProject.getGenerators().iterator();
		ResourceGenerator<?> oResGen = null;
		while (iterGen.hasNext() && !r_bTrouve) {
			oResGen = iterGen.next();
			r_bTrouve = oResGen.getClass().getName().equals(p_sGenClass);
		}
		if (r_bTrouve) {
			p_oProject.getGenerators().remove(oResGen);
		}
		return r_bTrouve;
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.commons.init.InitializingBean#afterInitialization()
	 */
	@Override
	public void afterInitialization( Map<String,String> p_mapGlobalProperties) throws Exception {
		
		for( IDomain<? extends IModelDictionary, ? extends IModelFactory> oDomain: getDomains()) {
			
			if ( oDomain.getLanguageConf() == null ) {
				MessageHandler.getInstance().addError("<language-ref> has not been defined for domain : {}", oDomain.getName());
			}
			if ( oDomain.getFileEncoding() == null ) {
				MessageHandler.getInstance().addError("<file-encoding> has not been defined for domain : {}", oDomain.getName());
			}
			if ( oDomain.getTemplate() == null ) {
				MessageHandler.getInstance().addError("<template> has not been defined for domain : {}", oDomain.getName());
			}
			if ( oDomain.getXModeleFactory() == null ) {
				MessageHandler.getInstance().addError("<xmodele-factory> has not been defined for domain : {}", oDomain.getName());
			}
			
			String sDomainPackage = p_mapGlobalProperties.get( StringUtils.join(oDomain.getName(), "Package"));
			if ( sDomainPackage != null ) {
				oDomain.setRootPackage(sDomainPackage);
			}
			else {
				MessageHandler.getInstance().addError("no root package is defined for domain : {}", oDomain.getName());
			}
		}
	}
}
