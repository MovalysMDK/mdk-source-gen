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
package com.a2a.adjava.generator.core.xmlmerge;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.AdjavaException;
import com.a2a.adjava.codeformatter.CodeFormatter;
import com.a2a.adjava.codeformatter.GeneratedFile;
import com.a2a.adjava.generator.codeformatters.XmlFormatOptions;
import com.a2a.adjava.generator.core.AbstractXslGenerator;
import com.a2a.adjava.generator.core.GeneratorUtils;
import com.a2a.adjava.generator.core.XslTemplate;
import com.a2a.adjava.generator.core.xmlmerge.xa.configuration.XaConfFile;
import com.a2a.adjava.generator.core.xmlmerge.xa.configuration.loader.ConfigurationLoader;
import com.a2a.adjava.generator.core.xmlmerge.xa.configuration.model.XAConfiguration;
import com.a2a.adjava.generator.core.xmlmerge.xa.xmlfile.loader.XmlFileLoader;
import com.a2a.adjava.generator.core.xmlmerge.xa.xmlfile.model.XAFiles;
import com.a2a.adjava.generator.core.xmlmerge.xa.xmlfile.process.MergeProcessor;
import com.a2a.adjava.generator.core.xmlmerge.xa.xmlfile.process.XAProcessor;
import com.a2a.adjava.generators.DomainGeneratorContext;
import com.a2a.adjava.messages.MessageHandler;
import com.a2a.adjava.xmodele.IDomain;
import com.a2a.adjava.xmodele.XProject;

/**
 * <p>Abstract class to generate XML files including previous user modifications</p>
 *
 * <p>Copyright (c) 2013</p>
 * <p>Company: Adeuza</p>
 *
 * @author pedubreuil
 * @since Cotopaxi
 */
public abstract class AbstractXmlMergeGenerator<D extends IDomain<?, ?>> extends AbstractXslGenerator<D> {

	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(AbstractXmlMergeGenerator.class);

	/**
	 * Path to XAConf Files
	 */
	public static final String PATH_TO_XACONF_FILES = "/config/xmlMerge";

	/**
	 * Default size for a buffer (byte[])
	 */
	private static final int BUFFER_SIZE = 1024;

	@Override
	public void initialize() throws AdjavaException{
		super.initialize();

		Path tempDirectoryForGeneratedXml;

		try {
			tempDirectoryForGeneratedXml = Files.createTempDirectory("newGeneratedXml");
			log.debug("[AbstractXmlMergeGenerator#initialize]  dir created : "+tempDirectoryForGeneratedXml);
		} catch (IOException e) {
			log.error(e.getMessage());
		}

		loadXaConfigurationFiles();
	}


	/**
	 * Do XML generation with a org.dom4j.Document source Document
	 * @param p_xDoc The source document
	 * @param p_sTemplatePath The path of XSL template
	 * @param p_oSrcXmlFileContainer The source XML File container 
	 * @param p_oProject The project
	 * @param p_oGeneratorContext The context of generator
	 * @param p_oXaConfName The XA Configuration file name
	 * @throws Exception 
	 */
	public void doXmlMergeGeneration(org.dom4j.Document p_xDoc, XslTemplate p_sTemplatePath, GeneratedFile<XmlFormatOptions> p_oSrcXmlFileContainer,
			XProject<D> p_oProject, DomainGeneratorContext p_oGeneratorContext, XaConfFile p_oXaConfName) throws Exception {
		this.doXmlMergeGeneration( new org.dom4j.io.DocumentSource(p_xDoc), p_sTemplatePath,p_oSrcXmlFileContainer, p_oProject, p_oGeneratorContext , p_oXaConfName);
	}	

	/**
	 * Do XML generation with a org.dom4j.Document source Document
	 * @param p_xDoc The source document
	 * @param p_sTemplatePath The path of XSL template
	 * @param p_oOutputFile The output file to write
	 * @param p_oProject The project
	 * @param p_oGeneratorContext The context of generator
	 * @param p_oXaConfName The XA Configuration file name
	 * @throws Exception
	 */
	public void doXmlMergeGeneration(org.dom4j.Document p_xDoc, XslTemplate p_sTemplatePath, File p_oOutputFile,
			XProject<D> p_oProject, DomainGeneratorContext p_oGeneratorContext, XaConfFile p_oXaConfName) throws Exception {
		this.doXmlMergeGeneration( new org.dom4j.io.DocumentSource(p_xDoc), p_sTemplatePath, (new GeneratedFile<XmlFormatOptions>(p_oOutputFile)), p_oProject, p_oGeneratorContext , p_oXaConfName);
	}

	/**
	 * Do XML generation with a org.w3c.dom.Document source Document
	 * @param p_xDoc The source document
	 * @param p_sTemplatePath The path of XSL template
	 * @param p_oOutputFile The output file to write
	 * @param p_oProject The project
	 * @param p_oGeneratorContext The context of generator
	 * @param p_oXaConfName The XA Configuration file name
	 * @throws Exception
	 */
	public void doXmlMergeGeneration(org.w3c.dom.Document  p_xDoc, XslTemplate p_sTemplatePath, File p_oOutputFile,
			XProject<D> p_oProject, DomainGeneratorContext p_oGeneratorContext, XaConfFile p_oXaConfName) throws Exception {
		this.doXmlMergeGeneration( new DOMSource(p_xDoc), p_sTemplatePath, (new GeneratedFile<XmlFormatOptions>(p_oOutputFile)), p_oProject, p_oGeneratorContext, p_oXaConfName );
	}

	/**
	 * Do XML generation with a org.w3c.dom.Document source Document
	 * @param p_xDoc The source document
	 * @param p_sTemplatePath The path of XSL template
	 * @param p_oOutputFile The output file to write
	 * @param p_oProject The project
	 * @param p_oGeneratorContext The context of generator
	 * @param p_oXaConfName The XA Configuration file name
	 * @throws Exception
	 */
	public void doXmlMergeGeneration(org.w3c.dom.Document  p_xDoc, XslTemplate p_sTemplatePath, GeneratedFile<XmlFormatOptions> p_oSrcXmlFileContainer,
			XProject<D> p_oProject, DomainGeneratorContext p_oGeneratorContext, XaConfFile p_oXaConfName) throws Exception {
		this.doXmlMergeGeneration( new DOMSource(p_xDoc), p_sTemplatePath, p_oSrcXmlFileContainer, p_oProject, p_oGeneratorContext , p_oXaConfName);
	}	

	/**
	 * Do XML generation with a {@link Source}  source Document
	 * @param p_oGeneratedXmlDom The source doucment
	 * @param p_sTemplatePath The path of XSL template
	 * @param p_oSrcXmlFileContainer The source XML file container
	 * @param p_oProject The project
	 * @param p_oGeneratorContext The context of generator
	 * @param p_oXaConfigurationFile The XA configuration file
	 * @throws AdjavaException 
	 * @throws IOException
	 */
	public void doXmlMergeGeneration(Source p_oGeneratedXmlDom, XslTemplate p_sTemplatePath, GeneratedFile<XmlFormatOptions> p_oSrcXmlFileContainer,
			XProject<D> p_oProject, DomainGeneratorContext p_oGeneratorContext, XaConfFile p_oXaConfigurationFile) throws AdjavaException, IOException {

		log.debug("[AbstractXmlMergeGenerator#doXmlMergeGeneration] XML Merge generation - beginning");

		// Define location of the XML file (in SRC)
		File srcXmlFile = new File(p_oProject.getBaseDir(), p_oSrcXmlFileContainer.getFile().getPath());
		p_oSrcXmlFileContainer.setFileFromRoot(srcXmlFile);

		//-----------------------------------------------------
		//  *** OLD GENERATED XML ***
		//   
		//-----------------------------------------------------

		log.debug("[AbstractXmlMergeGenerator#doXmlMergeGeneration] ------------------ OLD GENERATED XML ------------------");

		Path generatedFilesDir = Paths.get(this.getMergeBackupAbsolutePath(p_oProject),"generatedFiles");
		String[] generatedFilesSubDirNames = generatedFilesDir.toFile().list(new FilenameFilter() {
			@Override
			public boolean accept(File p_oDir, String p_oName) {
				return new File(p_oDir, p_oName).isDirectory();
			}
		});

		boolean existOldGeneratedXml=false;
		Path oldGeneratedXmlPath = null;
		File oldGeneratedXmlFile = null;

		if(generatedFilesSubDirNames != null && generatedFilesSubDirNames.length > 0){
			Arrays.sort(generatedFilesSubDirNames); // sort by creation date

			int generatedFilesSubDirIndex = generatedFilesSubDirNames.length-1;

			while(!existOldGeneratedXml && generatedFilesSubDirIndex >=0){
				oldGeneratedXmlPath = generatedFilesDir.resolve(generatedFilesSubDirNames[generatedFilesSubDirIndex]).resolve(srcXmlFile.getPath());
				oldGeneratedXmlFile = oldGeneratedXmlPath.toFile();
				existOldGeneratedXml = oldGeneratedXmlFile.exists() && oldGeneratedXmlFile.isFile() && oldGeneratedXmlFile.length() > 0;
				generatedFilesSubDirIndex--;
			}
		}

		log.debug("[AbstractXmlMergeGenerator#doXmlMergeGeneration] OLD GENERATED XML = "+oldGeneratedXmlPath+" (exists="+existOldGeneratedXml+")");


		//-----------------------------------------------------
		//  *** NEW GENERATED XML ***
		//   
		//-----------------------------------------------------

		log.debug("[AbstractXmlMergeGenerator#doXmlMergeGeneration] ------------------ NEW GENERATED XML ------------------");

		Path dirForNewGeneratedXml = Paths.get(this.getMergeBackupAbsolutePath(p_oProject), "generatedFiles", AbstractXslGenerator.generationDate,srcXmlFile.getParentFile().getPath());
		Files.createDirectories(dirForNewGeneratedXml.toAbsolutePath());
		Path newGeneratedXmlPath = dirForNewGeneratedXml.resolve(srcXmlFile.getName());
		File newGeneratedXmlFile = newGeneratedXmlPath.toFile();



		log.debug("[AbstractXmlMergeGenerator#doXmlMergeGeneration]  transformToFile - apply XSL rules and write file");
		try {
			GeneratedFile<XmlFormatOptions> newGeneratedFileContainer = new GeneratedFile<XmlFormatOptions>(newGeneratedXmlFile, p_oSrcXmlFileContainer.getFormatOptions());
			newGeneratedFileContainer.setInjection(p_oSrcXmlFileContainer.isInjection());
			newGeneratedFileContainer.addAllXslProperties(p_oSrcXmlFileContainer.getXslProperties());

			this.doTransformToFile(p_oGeneratedXmlDom, p_sTemplatePath.getFileName(), newGeneratedFileContainer, p_oProject, p_oGeneratorContext,true);
			
			// Execute code formatters
			for (CodeFormatter oCodeFormatter : p_oProject.getDomain().getCodeFormatters()) {
				List<GeneratedFile> onefileList = new ArrayList<GeneratedFile>();
				onefileList.add(newGeneratedFileContainer);
				oCodeFormatter.format(onefileList, p_oProject.getDomain().getFileEncoding());
				if (MessageHandler.getInstance().hasErrors()) {
					break;
				}
			}

			//log.debug("[AbstractXmlMergeGenerator#doXmlMergeGeneration]  Does '"+xaConfigurationFile.getName()+"' require sibling key grouping? "+xaConfigurationFile.requiresSiblingKeyGrouping());
			if(p_oXaConfigurationFile.requiresSiblingKeyGrouping()){		
				this.fromFlatXmlToGroupedXml(newGeneratedXmlFile,p_oXaConfigurationFile, p_oProject);
			}

		} catch (Exception e) {
			throw new AdjavaException("Error while applying XSL rules and writing file",e.getMessage());
		}
		boolean existNewGeneratedXml = newGeneratedXmlFile.exists() && newGeneratedXmlFile.isFile() && newGeneratedXmlFile.length() > 0;

		log.debug("[AbstractXmlMergeGenerator#doXmlMergeGeneration] NEW GENERATED XML = "+newGeneratedXmlPath+" (exists="+existNewGeneratedXml+")");



		if(!existOldGeneratedXml){

			log.debug("[AbstractXmlMergeGenerator#doXmlMergeGeneration] OLD GENERATED XML not found => get the new generated XML instead");

			oldGeneratedXmlPath = newGeneratedXmlPath;
			oldGeneratedXmlFile = newGeneratedXmlPath.toFile();
			existOldGeneratedXml =  oldGeneratedXmlFile.exists() && oldGeneratedXmlFile.isFile() && oldGeneratedXmlFile.length() > 0;
		}


		//-----------------------------------------------------
		//  *** MODIFIED XML ***
		//    save file modified by the user in backup dir
		//    MOVE   src/com/sopra/.../modifiedFile.xml   INTO   backupDir/beforeAutoMerge/modifiedFiles/CURRENT_DATE/com/sopra/.../modifiedFile.xml
		//-----------------------------------------------------

		log.debug("[AbstractXmlMergeGenerator#doXmlMergeGeneration] ------------------ OLD MODIFIED XML ------------------");

		boolean existOldModifiedXml = srcXmlFile.exists() && srcXmlFile.isFile() && srcXmlFile.length() > 0;
		if (existOldModifiedXml && p_oXaConfigurationFile.getPreProcessor() != null) {
			try {
				XAProcessor<D> processor = (XAProcessor<D>) Class.forName(p_oXaConfigurationFile.getPreProcessor()).newInstance();
				processor.process(oldGeneratedXmlFile, srcXmlFile, p_oProject);
			} catch (Exception e ) {
				throw new AdjavaException("[AbstractXmlMergeGenerator#doXmlMergeGeneration] Unexpected class XAProcessor :" + p_oXaConfigurationFile.getPreProcessor(), e);
			}
		}

		Path backupDirForOldModifiedXml = Paths.get(this.getMergeBackupAbsolutePath(p_oProject), "modifiedFiles", AbstractXslGenerator.generationDate,srcXmlFile.getParentFile().getPath());
		Files.createDirectories(backupDirForOldModifiedXml.toAbsolutePath());
		Path oldModifiedXmlPath = backupDirForOldModifiedXml.resolve(srcXmlFile.getName());
		File oldModifiedXmlFile = oldModifiedXmlPath.toFile();

		if ( existOldModifiedXml) {
			Files.move(srcXmlFile.toPath(), oldModifiedXmlPath, StandardCopyOption.REPLACE_EXISTING);

			//SMA ... TO CHANGE with pre and post CopyProcessor
			if(p_oXaConfigurationFile.requiresSiblingKeyGrouping()){		
				this.fromFlatXmlToGroupedXml(oldModifiedXmlFile,p_oXaConfigurationFile, p_oProject);
				//Files.copy(oldModifiedXmlPath, backupDirForOldModifiedXml.resolve("grouped-"+srcXmlFile.getName()), StandardCopyOption.REPLACE_EXISTING);
			}

			log.debug("[AbstractXmlMergeGenerator#doXmlMergeGeneration] OLD MODIFIED XML = "+oldModifiedXmlPath+" (exists="+existOldModifiedXml+")");
		} 
		else {
			log.warn("[AbstractXmlMergeGenerator#doXmlMergeGeneration]  OLD MODIFIED XML not found in   "+srcXmlFile.getAbsolutePath());
		}




		//-----------------------------------------------------
		//  *** MERGED XML ***
		//
		//-----------------------------------------------------
		// Merge to do between generatedXml and modifiedXml

		log.debug("[AbstractXmlMergeGenerator#doXmlMergeGeneration] ------------------ NEW MERGED XML ------------------");


		Files.createDirectories(srcXmlFile.toPath().getParent());


		if (existOldModifiedXml && existNewGeneratedXml && existOldGeneratedXml) {
			log.debug("[AbstractXmlMergeGenerator#doXmlMergeGeneration]  will merge XML...");
			processMerge(oldModifiedXmlPath , oldGeneratedXmlPath , newGeneratedXmlPath, srcXmlFile.toPath(),p_oXaConfigurationFile.getName());
			// log.debug("[AbstractXmlMergeGenerator#doXmlMergeGeneration] NEW MERGED XML = "+newGeneratedXmlPath);
		}
		else if(!existOldModifiedXml){
			log.debug("[AbstractXmlMergeGenerator#doXmlMergeGeneration]  NO OLD MODIFIED FILE => NO MERGE TO DO but use new generated file");
			log.debug("[AbstractXmlMergeGenerator#doXmlMergeGeneration]  COPY: "+newGeneratedXmlPath+" ==> "+srcXmlFile.toPath());
			Files.copy(newGeneratedXmlPath, srcXmlFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
		}
		else if(!existNewGeneratedXml){
			log.debug("[AbstractXmlMergeGenerator#doXmlMergeGeneration]  NO GENERATED FILE => NO MERGE TO DO but use old modified file");
			log.debug("[AbstractXmlMergeGenerator#doXmlMergeGeneration]  COPY: "+oldModifiedXmlPath+" ==> "+srcXmlFile.toPath());
			Files.copy(oldModifiedXmlPath, srcXmlFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
		}
		/*		else if(!existOldGeneratedXml){
			log.warn("[AbstractXmlMergeGenerator#doXmlMergeGeneration]  NO OLD GENERATED FILE (first generation of this project) =>   NO MERGE TO DO but use the new generated file instead of the one put in SRC by the template");
			log.debug("[AbstractXmlMergeGenerator#doXmlMergeGeneration]  COPY: "+newGeneratedXmlPath+" ==> "+srcXmlFile.toPath());
			Files.copy(newGeneratedXmlPath, srcXmlFile.toPath(), StandardCopyOption.REPLACE_EXISTING);			
		}
		 */		else{
			 throw new AdjavaException("[AbstractXmlMergeGenerator#doXmlMergeGeneration] Unexpected case for merging XML");
		 }

		if(srcXmlFile.exists() && srcXmlFile.isFile() && srcXmlFile.length() > 0){

			log.debug("[AbstractXmlMergeGenerator#doXmlMergeGeneration]  NEW MERGED XML = "+srcXmlFile.getAbsolutePath());

			if(p_oXaConfigurationFile.requiresSiblingKeyGrouping()){		
				this.fromGroupedXmlToFlatXml(srcXmlFile,p_oXaConfigurationFile, p_oProject);
			}

			//   Add new merged file to session
			addGeneratedFileToSession(p_oSrcXmlFileContainer, p_oGeneratorContext);			

			// Write XML files for debug
			if (isDebug()) {
				try {
					GeneratorUtils.writeXmlDebugFile(p_oGeneratedXmlDom, srcXmlFile.getAbsolutePath(), p_oProject);
				} catch (Exception e) {
					throw new AdjavaException("[AbstractXmlMergeGenerator#doXmlMergeGeneration] Cannot write debug XML file "+srcXmlFile.getAbsolutePath(),e);
				}
			}


		}
		else {
			throw new AdjavaException("[AbstractXmlMergeGenerator#doXmlMergeGeneration] This file has not been correctly merged : "+srcXmlFile.toPath());			
		}


		log.debug("[AbstractXmlMergeGenerator#doXmlMergeGeneration] XML Merge generation - end");

	}

	/**
	 * Load the Xa configuration files
	 * @throws AdjavaException Launched when an error occured during the load of XA configurations files
	 */
	private void loadXaConfigurationFiles() throws AdjavaException  {
		Path tempDirectoryForXaConf;

		XAConfiguration.getInstance().clear();
		log.debug("[AbstractXmlMergeGenerator#copyXaConfigurationFiles]  start loading...");

		try {
			tempDirectoryForXaConf = Files.createTempDirectory("XaConf");
			log.debug("[AbstractXmlMergeGenerator#copyXaConfigurationFiles]  dir created : "+tempDirectoryForXaConf);	
			copyJarFolder(PATH_TO_XACONF_FILES,tempDirectoryForXaConf);		
			log.debug("[AbstractXmlMergeGenerator#copyXaConfigurationFiles]  config files copied");

			ConfigurationLoader oLoader = new ConfigurationLoader(tempDirectoryForXaConf);
			oLoader.load();
			log.debug("[AbstractXmlMergeGenerator#copyXaConfigurationFiles]  config files loaded");

		} catch (IOException | URISyntaxException e) {
			log.error(e.getMessage());
		}

	}

	/**
	 * Process to the merge
	 * @param p_oOldModifiedXmlFile The old file potentially modifed by the user
	 * @param p_oOldGeneratedXmlFile The old generated file
	 * @param p_oNewGeneratedXmlFile The new generated file
	 * @param p_oNewMergedFile The new merged file
	 * @param p_oXaConfName The name of the XA configuration file
	 * @throws AdjavaException An exception that can be thrown during the merge
	 */
	private void processMerge(Path p_oOldModifiedXmlFile, Path p_oOldGeneratedXmlFile, Path p_oNewGeneratedXmlFile, Path p_oNewMergedFile, String p_oXaConfName) throws AdjavaException{
		log.debug("[AbstractXmlMergeGenerator#processMerge]  Loading XML files to merge...");
		XAFiles.getInstance().clear();
		String extension = FilenameUtils.getExtension(p_oOldModifiedXmlFile.getFileName().toString());
		XmlFileLoader oldModifiedXmlFileLoader = new XmlFileLoader(p_oOldModifiedXmlFile, XmlFileLoader.Type.MOD,extension,p_oXaConfName);
		oldModifiedXmlFileLoader.load();
		XmlFileLoader oldGeneratedXmlFileLoader = new XmlFileLoader(p_oOldGeneratedXmlFile, XmlFileLoader.Type.OLDGEN,extension,p_oXaConfName);
		oldGeneratedXmlFileLoader.load();
		XmlFileLoader newGeneratedXmlFileLoader = new XmlFileLoader(p_oNewGeneratedXmlFile, XmlFileLoader.Type.NEWGEN,extension,p_oXaConfName);
		newGeneratedXmlFileLoader.load();

		MergeProcessor oMp = new MergeProcessor();
		log.debug("[AbstractXmlMergeGenerator#processMerge]  Starting merge of XML files...");
		oMp.process(
				p_oOldGeneratedXmlFile,
				p_oNewGeneratedXmlFile,
				p_oOldModifiedXmlFile,
				p_oNewMergedFile
				);

	}


	/**
	 * Copies the jar folder
	 * @param p_oResourcesFolderRelativePath The relative path to the resources folder
	 * @param p_oDestinationFolder The destination folder
	 * @throws IOException I/O Exception
	 * @throws URISyntaxException URI Syntax Exception 
	 */
	private void copyJarFolder(String p_oResourcesFolderRelativePath, Path p_oDestinationFolder) throws IOException, URISyntaxException {
		if(!p_oResourcesFolderRelativePath.endsWith("/")){
			p_oResourcesFolderRelativePath +="/";
		}
		if(p_oResourcesFolderRelativePath.startsWith("/")){
			p_oResourcesFolderRelativePath = p_oResourcesFolderRelativePath.substring(1);
		}


		final int jarPathOffset = 5;
		URL resourceURL = AbstractXmlMergeGenerator.class.getResource("/"+p_oResourcesFolderRelativePath);
		if (resourceURL!=null && resourceURL.getProtocol().equals("jar")) {
			/* A JAR path */
			String jarPath = resourceURL.getPath().substring(jarPathOffset, resourceURL.getPath().indexOf("!")); //strip out only the JAR file
			log.debug("[AbstractXmlMergeGenerator#copyJarFolder]  Looping inside the dir >"+p_oResourcesFolderRelativePath+"< of the JAR >"+jarPath+"< ...");
			JarFile jar = new JarFile(URLDecoder.decode(jarPath, "UTF-8"));

			Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
			while(entries.hasMoreElements()) {
				JarEntry currentEntry = entries.nextElement();
				if (!currentEntry.isDirectory() && currentEntry.getName().startsWith(p_oResourcesFolderRelativePath)) { //filter according to the path
					copyResourceFile("/"+currentEntry.getName(),p_oDestinationFolder);
				}
			}
			jar.close();

		}
		else {
			log.debug("[AbstractXmlMergeGenerator#copyJarFolder]  Resources are not in JAR... simple copy into "+p_oDestinationFolder);
			Enumeration<URL> en=getClass().getClassLoader().getResources("/"+p_oResourcesFolderRelativePath);
			if (en.hasMoreElements()) {
				URL res=en.nextElement();
				File fileRes=new File(res.toURI());
				if(fileRes.isDirectory()){
					File[] files=fileRes.listFiles();
					for(File subFile:files){
						Files.copy(subFile.toPath(),p_oDestinationFolder.resolve(subFile.getName() ));
					}
				}
				else {
					Files.copy(fileRes.toPath(),p_oDestinationFolder.resolve(fileRes.getName() ));			    	
				}
			}

		}
	}


	/**
	 * Copies the resources files
	 * @param p_oResourceRelativePath The relative path top the resources folder
	 * @param p_oResourceDestinationFolder The path to the destination folder
	 * @return A boolean equals to true if the copy succeeded, false otherwise
	 * @throws IOException an IO exception
	 */
	private boolean copyResourceFile(String p_oResourceRelativePath, Path p_oResourceDestinationFolder) throws IOException {
		log.debug("[AbstractXmlMergeGenerator#copyResourceFile]  copying resource file >"+p_oResourceRelativePath+"< into >"+p_oResourceDestinationFolder+"< ...");

		if (p_oResourceRelativePath == null || p_oResourceDestinationFolder == null) {
			return false;
		}

		InputStream resourceReader = null;
		OutputStream resourceWriter = null;
		File resourceDestinationFile = new File(p_oResourceDestinationFolder.toFile(),(new File(p_oResourceRelativePath)).getName());
		resourceDestinationFile.getParentFile().mkdirs();
		try {
			int nLen = 0;
			resourceReader = AbstractXmlMergeGenerator.class.getResourceAsStream(p_oResourceRelativePath);
			if (resourceReader == null) {
				throw new IOException("File not found in " + p_oResourceRelativePath );
			}
			resourceWriter = new FileOutputStream(resourceDestinationFile);
			byte[] resourceBuffer = new byte[BUFFER_SIZE];
			while ((nLen = resourceReader.read(resourceBuffer)) > 0){
				resourceWriter.write(resourceBuffer, 0, nLen);
			}
			resourceWriter.flush();
		}
		catch(IOException ex) {
			log.error(ex.getMessage());
			throw ex;
		}
		finally {
			try {
				if (resourceReader != null) {
					resourceReader.close();
				}
				if (resourceWriter != null) {
					resourceWriter.close();
				}
			}
			catch (IOException eError) {
				log.error(eError.getMessage());
			}
		}
		return resourceDestinationFile.exists();
	}



	/**
	 * Transforms a flat classic PLIST Xml as a grouped XML ready for merge
	 * Attention (SMA) : à la base ce code se veut générique, mais maintentant (01/04/2014) il ne fonctionne que pour plist ios.
	 * Il faut modifier ce code pour le déporter dans des "transformers" déclarés sur chaque configuration
	 * @param p_oFileToConvert The file to convert
	 * @param p_oXaConfFile The XA configuration file
	 * @param p_oProject The project
	 * @throws AdjavaException An AdjavaException that can be thrown by the merge
	 */
	private void fromFlatXmlToGroupedXml(File p_oFileToConvert, XaConfFile p_oXaConfFile, XProject<D> p_oProject) throws AdjavaException{

		try {
			GeneratorUtils.writeXml(PlistProcessor.fromFlatXmlToGroupedXml(p_oFileToConvert, p_oXaConfFile), p_oFileToConvert, p_oProject);
			log.debug("[AbstractXmlMergeGenerator#fromFlatXmlToGroupedXml] XML converted (from flat to grouped)");
		}
		catch ( Exception e) {
			throw new AdjavaException("[AbstractXmlMergeGenerator#fromFlatXmlToGroupedXml] problem while converting the file "+p_oFileToConvert.getPath()+" from flat XML to grouped XML",e);
		}
	}

	/**
	 * Transforms a grouped merged XML to a flat PLIST Xml format
	 * @param p_oFileToConvert The file to convert
	 * @param p_oXaConfFile The XA configuration file
	 * @param p_oProject The project
	 * @throws AdjavaException An AdjavaException that can be thrown by the merge
	 */
	private void fromGroupedXmlToFlatXml(File p_oFileToConvert, XaConfFile p_oXaConfFile, XProject<D> p_oProject) throws AdjavaException{

		try {
			GeneratorUtils.writeXml(PlistProcessor.fromGroupedXmlToFlatXml(p_oFileToConvert, p_oXaConfFile), p_oFileToConvert, p_oProject);
			log.debug("[AbstractXmlMergeGenerator#fromGroupedXmlToFlatXml] XML converted (from grouped to flat)");
		}
		catch ( Exception e) {
			throw new AdjavaException("[AbstractXmlMergeGenerator#fromGroupedXmlToFlatXml] problem while converting the file "+p_oFileToConvert.getPath()+" from grouped XML to flat XML",e);
		}
	}

}
