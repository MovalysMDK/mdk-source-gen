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

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;

import com.a2a.adjava.AdjavaProperty;
import com.a2a.adjava.codeformatter.CodeFormatter;
import com.a2a.adjava.codeformatter.GeneratedFile;
import com.a2a.adjava.commons.init.InitializingBean;
import com.a2a.adjava.generator.core.fiximport.FixImport;
import com.a2a.adjava.generators.DomainGeneratorContext;
import com.a2a.adjava.generators.GeneratorContext;
import com.a2a.adjava.generators.ResourceGenerator;
import com.a2a.adjava.messages.MessageHandler;
import com.a2a.adjava.projectupgrader.ProjectUpgrader;
import com.a2a.adjava.projectupgrader.ProjectUpgrader.ProjectUpgraderMode;
import com.a2a.adjava.projectupgrader.ProjectUpgraderComparator;
import com.a2a.adjava.utils.JaxbUtils;
import com.a2a.adjava.utils.ListUtils;
import com.a2a.adjava.utils.VersionHandler;
import com.a2a.adjava.versions.GenerationDescriptor;
import com.a2a.adjava.versions.GenerationHistory;
import com.a2a.adjava.versions.GenerationHistoryHolder;
import com.a2a.adjava.versions.ProjectUpgraderDescriptor;
import com.a2a.adjava.xmodele.IDomain;
import com.a2a.adjava.xmodele.IModelDictionary;
import com.a2a.adjava.xmodele.IModelFactory;
import com.a2a.adjava.xmodele.XDomainRegistry;
import com.a2a.adjava.xmodele.XProject;

/**
 * <p>
 * GeneratorExecutor
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
public class GeneratorExecutor implements InitializingBean {
	
	/**
	 * Report file name
	 */
	private static final String ADJAVA_REPORT_FILE = "adjava-report.txt";
	
	/**
	 * Add files report file name
	 */
	private static final String ADJAVA_XMLREPORT_FILE_ADD = "adjava-add-report.xml";
	
	/**
	 * Remove files report file name
	 */
	private static final String ADJAVA_XMLREPORT_FILE_REMOVE = "adjava-remove-report.xml";
	
	/**
	 * Existing files report file name
	 */
	private static final String ADJAVA_XMLREPORT_FILE_EXIST = "adjava-exist-report.xml";

	/**
	 * File that lists the generated files
	 */
	private static final String ADJAVA_GENERATEDFILES_FILE = "adjava-generated-files.xml";
	
	/**
	 * Debug
	 */
	private boolean debug;
	
	/**
	 * Class that handles files that were not generated compared to the last generation.
	 */
	private UnusedFileStrategy unusedFileStrategy;

	/**
	 * Execute generators of all domains
	 * @param p_oDomainRegistry domain registry
	 * @throws Exception exception
	 */
	public void execute(XDomainRegistry p_oDomainRegistry) throws Exception {

		GeneratorContext oGlobalGeneratorContext = new GeneratorContext();
		
		for (IDomain<IModelDictionary,IModelFactory> oDomain : p_oDomainRegistry.getDomains()) {

			// Read file list of generated files from last generation
			List<GeneratedFile> listOldGeneratedFiles = this.readOldGeneratedFileList( oDomain );
			DomainGeneratorContext oDomainGenContext = new DomainGeneratorContext(oGlobalGeneratorContext, listOldGeneratedFiles);
	
			FixImport oFixImport = new FixImport();
			oFixImport.extractOldTypes(oDomainGenContext);
			
			// Initialise les generateurs
			List<ResourceGenerator<?>> listResGen = new ArrayList<ResourceGenerator<?>>();
			for (XProject<IDomain<IModelDictionary,IModelFactory>> oProject : oDomain.getProjects()) {
				for (ResourceGenerator<?> oGen : oProject.getGenerators()) {
					oGen.initialize();
					oGen.setDebug(this.debug);
					listResGen.add(oGen);
				}
			}

			// Lance les generateurs
			for (XProject<IDomain<IModelDictionary,IModelFactory>> oProject : oDomain.getProjects()) {
				for (ResourceGenerator oGen : oProject.getGenerators()) {
					oGen.genere(oProject, oDomainGenContext);
					if (MessageHandler.getInstance().hasErrors()) {
						break;
					}
				}
			}
			
			// Fix imports
			oFixImport.fixImports(oDomainGenContext);
			
			//Application des upgraders post-generation. (outils de migration).
			GeneratorExecutor.applyUpgraders(ProjectUpgraderMode.PROJECT_UPGRADER_MODE_AFTER_GEN, p_oDomainRegistry, oDomainGenContext);
			
			
			// Execute code formatters
			for (CodeFormatter oCodeFormatter : oDomain.getCodeFormatters()) {
				oCodeFormatter.format(oDomainGenContext.getGeneratedFiles(), oDomain.getFileEncoding());
				if (MessageHandler.getInstance().hasErrors()) {
					break;
				}
			}
			
			//log report
			this.writeGeneratedFileList(oDomainGenContext, oDomain);
			
		}
				
		if ( !MessageHandler.getInstance().hasErrors()) {
			
			// treat files that were not generated compared to the last genereation
			Timestamp oTimestamp = new Timestamp(System.currentTimeMillis());
			if ( this.unusedFileStrategy != null ) {
				this.treatUnusedFiles(oGlobalGeneratorContext.getOldGeneratedFiles(), 
					oGlobalGeneratorContext.getGeneratedFiles(), oTimestamp, oGlobalGeneratorContext );
			}
			
			this.genReport(oGlobalGeneratorContext.getOldGeneratedFiles(), oGlobalGeneratorContext.getGeneratedFiles(), oTimestamp);
		}
	}

	/**
	 * Read file that contains the old list of generated files
	 * @param p_oDomain domain
	 * @return old list of generated files
	 * @throws Exception 
	 */
	private List<GeneratedFile> readOldGeneratedFileList( IDomain<IModelDictionary, IModelFactory> p_oDomain) throws Exception {
		List<GeneratedFile> listFiles = new ArrayList<GeneratedFile>();
		File oFile = new File( p_oDomain.getName() + "-" + ADJAVA_GENERATEDFILES_FILE);
		if ( oFile.exists() && oFile.canRead()) {
			for( UnitFileReport oFileReport : JaxbUtils.unmarshal(FileReport.class, oFile).getFiles()) {
				GeneratedFile oGenFile = new GeneratedFile(oFileReport.getFullName());
				oGenFile.setFileFromRoot(new File(oFileReport.getFullName()));
				oGenFile.setInjection(oFileReport.isInjection());
				listFiles.add(oGenFile);
			}
		}
		
		return listFiles;
	}
	
	/**
	 * Write the file that contains the new generated file list
	 * @param p_oContext domain generator context
	 * @param p_oDomain domain
	 * @throws Exception 
	 */
	private void writeGeneratedFileList( DomainGeneratorContext p_oContext, IDomain<IModelDictionary, IModelFactory> p_oDomain ) throws Exception {	
		FileReport oReport = new FileReport();
		for(GeneratedFile oFile : p_oContext.getGeneratedFiles()) {
			oReport.addFile(oFile.getFileFromRoot().getPath(), oFile.isInjection());
		}
		JaxbUtils.marshal(oReport, new File(StringUtils.join(p_oDomain.getName(), "-", ADJAVA_GENERATEDFILES_FILE)));
	}
	
	/**
	 * Treat unused files
	 * @param p_listOldGeneratedFiles old list of generated files
	 * @param p_listNewGeneratedFiles new list of generated files
	 * @param p_oGenerationDate date of generation
	 * @param p_oContext generator context
	 * @throws Exception exception
	 */
	private void treatUnusedFiles( List<GeneratedFile> p_listOldGeneratedFiles, List<GeneratedFile> p_listNewGeneratedFiles, Timestamp p_oGenerationDate, GeneratorContext p_oContext ) throws Exception {
		Collection<GeneratedFile> listCanBeDeletedFiles = ListUtils.subtract( p_listOldGeneratedFiles, p_listNewGeneratedFiles);
		List<String> listUnusedFiles = new ArrayList<String>();
		for( GeneratedFile oUnusedFile : listCanBeDeletedFiles) {
			listUnusedFiles.add(oUnusedFile.getFileFromRoot().getPath());
		}
		this.unusedFileStrategy.treatUnusedFiles(listUnusedFiles, p_oGenerationDate, p_oContext);
		
	}
	
	/**
	 * Generate report
	 * @param p_listOldGeneratedFiles old list of generated files
	 * @param p_listNewGeneratedFiles new list of generated files
	 * @param p_oGenerationDate date of generation
	 * @throws Exception exception
	 */
	private void genReport( List<GeneratedFile> p_listOldGeneratedFiles, List<GeneratedFile> p_listNewGeneratedFiles,
			Timestamp p_oGenerationDate ) throws Exception {
		StringBuilder oReport = new StringBuilder(300);
		SimpleDateFormat oSdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault());
		oReport.append("----------------------------------------------------\n");
		oReport.append("* Adjava Report\n");
		oReport.append("*\n");
		oReport.append("* date: ");
		oReport.append(oSdf.format(p_oGenerationDate));
		oReport.append("\n");
		oReport.append("* number of generated files: ");
		oReport.append(p_listNewGeneratedFiles.size());
		oReport.append("\n");
		
		oReport.append("*\n");
		
		Collection<GeneratedFile> listCanBeDeletedFiles = ListUtils.subtract( p_listOldGeneratedFiles, p_listNewGeneratedFiles);
		Collection<GeneratedFile> listAddedFiles = ListUtils.subtract(p_listNewGeneratedFiles, p_listOldGeneratedFiles);
		Collection<GeneratedFile> listExistedFiles = ListUtils.subtract(p_listNewGeneratedFiles, listAddedFiles);
		if ( listCanBeDeletedFiles != null && !listCanBeDeletedFiles.isEmpty()) {
			oReport.append("* Following files were not generated (compared to last generation): \n");
			for( GeneratedFile oFile: listCanBeDeletedFiles) {
				oReport.append("*   ");
				oReport.append(oFile.getFileFromRoot());
				oReport.append("\n");
			}
		}
		oReport.append("----------------------------------------------------\n");
		
		FileUtils.write(new File(ADJAVA_REPORT_FILE), oReport.toString(), true);
		
		this.createXMLReport(ADJAVA_XMLREPORT_FILE_ADD, listAddedFiles);
		this.createXMLReport(ADJAVA_XMLREPORT_FILE_EXIST, listExistedFiles);
		this.createXMLReport(ADJAVA_XMLREPORT_FILE_REMOVE, listCanBeDeletedFiles);
	}
		
	/**
	 * Construct a new xml report
	 * @param p_sFileName file name to use
	 * @param p_sFilesToRender list of file to add in report
	 * @throws Exception 
	 */
	private void createXMLReport(String p_sFileName, Collection<GeneratedFile> p_sFilesToRender) throws Exception {
		FileReport oReport = new FileReport();
		for(GeneratedFile oFile : p_sFilesToRender) {
			oReport.addFile(oFile.getFileFromRoot().getPath(), oFile.isInjection());
		}
		JaxbUtils.marshal(oReport, new File(p_sFileName));
	} 
	
	/**
	 * Report.
	 * A report is a list of files.
	 * @author smaitre
	 */
	@XmlRootElement(name="report")
	@XmlAccessorType(XmlAccessType.FIELD)
	private static class FileReport {
		
		/**
		 * Treated file list
		 */
		@XmlElementWrapper
		@XmlElement(name="file")
		private List<UnitFileReport> files = new ArrayList<UnitFileReport>();
		
		/**
		 * Add a file to report
		 * @param p_sFileName file to add to the report
		 * @param p_bInjection injection mode
		 */
		public void addFile(String p_sFileName, boolean p_bInjection) {
			this.files.add( new UnitFileReport(p_sFileName, p_bInjection ));
		}

		/**
		 * Get file report list
		 * @return file report list
		 */
		public List<UnitFileReport> getFiles() {
			return this.files;
		}
	}
	
	/**
	 * Report for a generated file
	 * @author smaitre
	 *
	 */
	private static class UnitFileReport {
		
		/**
		 * File name
		 */
		@XmlElement
		private String fullName ;
		
		/**
		 * Short file name
		 */
		@XmlElement
		private String shortName ;
		
		/**
		 * Folder name
		 */
		@XmlElement
		private String dirName ;
		
		/**
		 * Injection mode 
		 */
		@XmlElement
		private boolean injection = false ;
		
		/**
		 * Split path
		 */
		@XmlElementWrapper
		@XmlElement(name="path")
		private List<String> paths = new ArrayList<String>();
		
		/**
		 * Constructor for jaxb
		 */
		public UnitFileReport() {
			// empty constructor for jaxb
		}
		
		/**
		 * Constructor
		 * @param p_sName file name
		 */
		public UnitFileReport(String p_sName, boolean p_bInjection ) {
			this();
			this.fullName = p_sName;
			this.shortName = StringUtils.substringAfterLast(p_sName,"/");
			this.dirName =  StringUtils.substringBeforeLast(p_sName,"/");
			this.injection = p_bInjection ;
			if (dirName.startsWith("./")) {
				this.dirName = dirName.substring(2);
			}
			for(String sPath : this.dirName.split("/")) {
				this.paths.add(sPath);
			}
		}

		/**
		 * Get full name
		 * @return full name
		 */
		public String getFullName() {
			return this.fullName;
		}

		/**
		 * Is injection mode
		 * @return injection mode
		 */
		public boolean isInjection() {
			return this.injection;
		}
	}
	
	/**
	 * Set debug mode
	 * 
	 * @param p_bDebug debug
	 */
	public void setDebug(boolean p_bDebug) {
		this.debug = p_bDebug;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.a2a.adjava.commons.init.InitializingBean#initialize(org.dom4j.Element)
	 */
	@Override
	public void initialize(Element p_xElement, Map<String,String> p_mapGlobalProperties) throws Exception {
		String sDebug = p_xElement.elementText("debug");
		if (sDebug != null) {
			this.debug = Boolean.parseBoolean(sDebug);
		}
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.commons.init.InitializingBean#afterInitialization()
	 */
	@Override
	public void afterInitialization(  Map<String,String> p_mapGlobalProperties ) throws Exception {
		
		String sClass = p_mapGlobalProperties.get(AdjavaProperty.UNUSED_FILE_STRATEGY_CLASS.getName());
		if ( sClass != null && !sClass.isEmpty()) {
			this.unusedFileStrategy = (UnusedFileStrategy) Class.forName(sClass).newInstance();
			this.unusedFileStrategy.afterInitialization(p_mapGlobalProperties);
		}
	}
	
	public static void applyUpgraders(ProjectUpgraderMode p_oProjectUpgraderMode, XDomainRegistry p_oDomainRegistry, DomainGeneratorContext p_oGeneratedContext) throws Exception {
		GenerationHistoryHolder.getInstance();
		GenerationHistory oGenerationHistory = GenerationHistoryHolder.generationHistory;
		GenerationDescriptor oCurrentGeneration = oGenerationHistory.getGenerations().get(oGenerationHistory.getGenerations().size()-1);
		if(!oCurrentGeneration.getExecutionMetadataList().getInfoSaveBeanList().get(0).getExecutionId().equalsIgnoreCase(GenerationDescriptor.FIRST_GENERATION_ATTRIBUTE_VALUE)) {
			String sLastMDKGenerationVersion = VersionHandler.getLastMDKGenerationVersion().split("-")[0];
			String sCurrentMDKGenerationVersion = VersionHandler.getCurrentMDKGenerationVersion().split("-")[0];

			// Apply Upgraders
			if (!MessageHandler.getInstance().hasErrors() && !sLastMDKGenerationVersion.equals(sCurrentMDKGenerationVersion)) {
				ProjectUpgraderComparator oProjectUpgraderComparator = new ProjectUpgraderComparator();
				ProjectUpgraderDescriptor oTemporaryDescriptor = new ProjectUpgraderDescriptor();
				for( IDomain<IModelDictionary,IModelFactory> oDomain : p_oDomainRegistry.getDomains()) {

					Map<String, DomainGeneratorContext> oMapSession = new HashMap<String, DomainGeneratorContext>();
					oMapSession.put("generatedContext", p_oGeneratedContext);
					List<String> oAppliedProjectUpgraders = new ArrayList<String>();

					//Tri des project upgraders par numéro de versions
					Collections.sort(oDomain.getProjectUpgraders(), oProjectUpgraderComparator);

					for( ProjectUpgrader oProjectUpgrader : oDomain.getProjectUpgraders()) {

						//On vérifie si le projectUpgrader n'est pas destiné une version du MDK inférieure à la version de la
						//précédente génération. Si c'est le cas on ne doit pas l'appliquer.
						boolean bShouldApplyProjectUpgrader = !oProjectUpgraderComparator.isAnteriorToLastGeneration(oProjectUpgrader);

						//On vérifie qu'un projectUpgrader similaire n'a pas déjà été appliqué lors de CETTE génération
						for(String sSimilarProjectUpgrader : oProjectUpgrader.getSimilarProjectUpgraders()) {
							if(sSimilarProjectUpgrader.equalsIgnoreCase(oProjectUpgrader.getClass().getName())) {
								bShouldApplyProjectUpgrader = false;
								break;
							}
						}

						//On vérifie que ce project Upgrader n'a pas déja été applique lors d'une précédente génération
						//NE DEVRAIT JAMAIS ARRIVER
						for(ProjectUpgraderDescriptor oAlreadyAppliedDescriptor : oCurrentGeneration.getProjectUpgraderList().getProjectUpgraders()) {
							oTemporaryDescriptor.setParametersFromMap(oProjectUpgrader.getParametersMap());
							if(oAlreadyAppliedDescriptor.equals(oTemporaryDescriptor)) {
								System.out.println("[ProjectUpgraders] - Le project upgrader: "+ oProjectUpgrader.getClass().getName() + 
										"a déjà été appliqué lors d'une précédente génération, le "+ oAlreadyAppliedDescriptor.getDate());
								bShouldApplyProjectUpgrader = false;
								break;
							}
						}

						bShouldApplyProjectUpgrader = bShouldApplyProjectUpgrader 
								&& oProjectUpgrader.getMode().equals(p_oProjectUpgraderMode);

						//Application du projectUpgrader et ajout à la liste des PU déja appliqués.
						if(bShouldApplyProjectUpgrader) {
							oProjectUpgrader.execute(oDomain, oMapSession);
							oAppliedProjectUpgraders.add(oProjectUpgrader.getClass().getName());
							//Ajout du project-upgrader à l'historique des project-upgarders pour cette génération.
							oCurrentGeneration.addProjectUpgraderDescriptor(oProjectUpgrader.getParametersMap());
						}

						if (MessageHandler.getInstance().hasErrors()) {
							break;
						}
					}
					if (MessageHandler.getInstance().hasErrors()) {
						break;
					}
					System.out.println("[ProjectUpgraders] - post generation - applied : "+ oAppliedProjectUpgraders );
				}
			}
		}
	}
}
