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
package com.a2a.adjava.generator.core.jsonmerge;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.transform.Source;

import org.dom4j.Document;
import org.dom4j.io.DocumentSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.AdjavaException;
import com.a2a.adjava.AdjavaProperty;
import com.a2a.adjava.codeformatter.CodeFormatter;
import com.a2a.adjava.codeformatter.FormatOptions;
import com.a2a.adjava.codeformatter.GeneratedFile;
import com.a2a.adjava.generator.core.AbstractXslGenerator;
import com.a2a.adjava.generators.DomainGeneratorContext;
import com.a2a.adjava.messages.MessageHandler;
import com.a2a.adjava.xmodele.IDomain;
import com.a2a.adjava.xmodele.XProject;

/**
 * AbstractJsonMergeGenerator 
 * This class will handle cases where no merge is needed.
 * When an update is needed it has to be done by subclasses 
 * 
 * @author mlaffargue
 * 
 * @param <D> type of Domain
 * 
 */
public abstract class AbstractJsonMergeGenerator<D extends IDomain<?, ?>> extends AbstractXslGenerator<D> {
	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(AbstractJsonMergeGenerator.class);
	

	/**
	 * Given the old generation, the new generation and the current project file(oldModified) :
	 * Merge and write the NewMergedFile.
	 * @param p_oOldModifiedXmlFile The current file potentially modified by the user
	 * @param p_oOldGeneratedXmlFile The old generated file
	 * @param p_oNewGeneratedXmlFile The new generated file
	 * @param p_oNewMergedFile The new merged file
	 * @throws AdjavaException An exception that can be thrown during the merge
	 */
	abstract protected void processJSONMerging(Path p_oOldModifiedXmlFile, Path p_oOldGeneratedXmlFile, Path p_oNewGeneratedXmlFile, Path p_oNewMergedFile) throws AdjavaException;
	
	/**
	 * Start Json merge generation
	 * @param p_oSource xml document
	 * @param p_sTemplatePath template path
	 * @param p_oOutputFile output file
	 * @param p_oProject project
	 * @param p_oGlobalSession session
	 * @throws Exception generation failure
	 */
	public void doJsonMergeGeneration(Document p_xDoc, String p_sTemplatePath, File p_oFile,
			XProject<D> p_oProject, DomainGeneratorContext p_oGeneratorContext) throws Exception {
		Source source = new DocumentSource(p_xDoc);
		GeneratedFile<FormatOptions> outputFileContainer = new GeneratedFile<FormatOptions>(p_oFile);
		
		File oFile = new File(p_oProject.getBaseDir(), outputFileContainer.getFile().getPath());
		outputFileContainer.setFileFromRoot(oFile);

		String sForceOverwrite = p_oProject.getDomain().getGlobalParameters().get(AdjavaProperty.APPEND_GENERATOR_FORCE_OVERWRITE.getName());
		
		if (!oFile.exists() || (sForceOverwrite != null && sForceOverwrite.equalsIgnoreCase("true"))) {
			// First generation
			this.doTransformToFile(source, p_sTemplatePath, outputFileContainer, p_oProject, p_oGeneratorContext,false);
		} else {
			this.doJsonMerge(source, p_sTemplatePath, outputFileContainer, p_oProject, p_oGeneratorContext);
		}
	}

	/**
	 * This method will retrieve the old generation, the new generation and the current project file.
	 * If no merge is needed it'll generate the new file, otherwise the merging will be treated 
	 * by the subclass  
	 * 
	 * @param p_oSource
	 * @param p_sTemplatePath
	 * @param p_oOutputFile
	 * @param p_oProject
	 * @param p_oGeneratorContext
	 * @throws AdjavaException
	 * @throws IOException
	 */
	private void doJsonMerge(Source p_oSource, String p_sTemplatePath, GeneratedFile p_oOutputFile,
			XProject<D> p_oProject, DomainGeneratorContext p_oGeneratorContext) throws AdjavaException, IOException {
		log.debug("[AbstractAppendGenerator#doJsonMerge] XML Merge generation - beginning");
		// Define location of the JSON file (in SRC)
		File outputGenerationFile = new File(p_oProject.getBaseDir(), p_oOutputFile.getFile().getPath());
		p_oOutputFile.setFileFromRoot(outputGenerationFile);
		
		//-------------------------------
		//  *** OLD GENERATED JSON ***
		//-------------------------------
		log.debug("[AbstractAppendGenerator#doJsonMerge] ------------------ OLD GENERATED JSON ------------------");
		Path oldGeneratedJsonPath = getOldGeneratedJsonPath(p_oProject, outputGenerationFile);
		boolean existOldGeneratedJson = (oldGeneratedJsonPath != null);
		log.debug("[AbstractXmlMergeGenerator#doXmlMergeGeneration] OLD GENERATED XML = "+oldGeneratedJsonPath+" (exists="+existOldGeneratedJson+")");
		
		//-------------------------------
		//  *** NEW GENERATED XML ***
		//-------------------------------
		log.debug("[AbstractXmlMergeGenerator#doXmlMergeGeneration] ------------------ NEW GENERATED XML ------------------");
		Path newGeneratedJsonPath = getNewGeneratedJsonPath(p_oSource, p_sTemplatePath, p_oOutputFile, p_oProject, p_oGeneratorContext, outputGenerationFile);
		File newGeneratedJsonFile = newGeneratedJsonPath.toFile();
		boolean existNewGeneratedJson = newGeneratedJsonFile.exists() && newGeneratedJsonFile.isFile() && newGeneratedJsonFile.length() > 0;
		log.debug("[AbstractXmlMergeGenerator#doXmlMergeGeneration] NEW GENERATED JSON = "+newGeneratedJsonPath+" (exists="+existNewGeneratedJson+")");

		if(!existOldGeneratedJson){
			log.debug("[AbstractXmlMergeGenerator#doXmlMergeGeneration] OLD GENERATED JSON not found => get the new generated JSON instead");
			oldGeneratedJsonPath = newGeneratedJsonPath;
			existOldGeneratedJson = existNewGeneratedJson;
		}
		
		//-----------------------------------------------------
		//  *** MODIFIED JSON ***
		//    save file modified by the user in backup dir
		//    MOVE   src/com/sopra/.../modifiedFile.xml   INTO   backupDir/beforeAutoMerge/modifiedFiles/CURRENT_DATE/com/sopra/.../modifiedFile.xml
		//-----------------------------------------------------
		log.debug("[AbstractJsonMergeGenerator#doJsonMergeGeneration] ------------------ OLD MODIFIED JSON ------------------");

		boolean existOldModifiedJson = outputGenerationFile.exists() && outputGenerationFile.isFile() && outputGenerationFile.length() > 0;
		
		Path backupDirForOldModifiedJson = Paths.get(this.getMergeBackupAbsolutePath(p_oProject), "modifiedFiles", AbstractXslGenerator.generationDate,outputGenerationFile.getParentFile().getPath());
		Files.createDirectories(backupDirForOldModifiedJson.toAbsolutePath());
		Path oldModifiedJsonPath = backupDirForOldModifiedJson.resolve(outputGenerationFile.getName());

		if (existOldModifiedJson) {
			Files.move(outputGenerationFile.toPath(), oldModifiedJsonPath, StandardCopyOption.REPLACE_EXISTING);

			log.debug("[AbstractJsonMergeGenerator#doJsonMergeGeneration] OLD MODIFIED JSON = "+oldModifiedJsonPath+" (exists=true)");
		} 
		else {
			log.warn("[AbstractJsonMergeGenerator#doJsonMergeGeneration]  OLD MODIFIED JSON not found in   "+outputGenerationFile.getAbsolutePath());
		}
	
		//-----------------------------------------------------
		//  *** MERGED JSON ***
		//-----------------------------------------------------
		// Merge to do between generatedJson and modifiedJson

		log.debug("[AbstractJsonMergeGenerator#doJsonMergeGeneration] ------------------ NEW MERGED JSON ------------------");
		Files.createDirectories(outputGenerationFile.toPath().getParent());

		if (existOldModifiedJson && existNewGeneratedJson && existOldGeneratedJson) {
			log.debug("[AbstractJsonMergeGenerator#doJsonMergeGeneration]  will merge JSON...");
			processJSONMerging(oldModifiedJsonPath , oldGeneratedJsonPath , newGeneratedJsonPath, outputGenerationFile.toPath());
			// log.debug("[AbstractJsonMergeGenerator#doJsonMergeGeneration] NEW MERGED JSON = "+newGeneratedJsonPath);
		} else if(!existOldModifiedJson){
			log.debug("[AbstractJsonMergeGenerator#doJsonMergeGeneration]  NO OLD MODIFIED FILE => NO MERGE TO DO but use new generated file");
			log.debug("[AbstractJsonMergeGenerator#doJsonMergeGeneration]  COPY: "+newGeneratedJsonPath+" ==> "+outputGenerationFile.toPath());
			Files.copy(newGeneratedJsonPath, outputGenerationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
		} else if(!existNewGeneratedJson){
			log.debug("[AbstractJsonMergeGenerator#doJsonMergeGeneration]  NO GENERATED FILE => NO MERGE TO DO but use old modified file");
			log.debug("[AbstractJsonMergeGenerator#doJsonMergeGeneration]  COPY: "+oldModifiedJsonPath+" ==> "+outputGenerationFile.toPath());
			Files.copy(oldModifiedJsonPath, outputGenerationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
		} else {
			 throw new AdjavaException("[AbstractJsonMergeGenerator#doJsonMergeGeneration] Unexpected case for merging JSON");
		}

		if(outputGenerationFile.exists() && outputGenerationFile.isFile() && outputGenerationFile.length() > 0){
			log.debug("[AbstractJsonMergeGenerator#doJsonMergeGeneration]  NEW MERGED JSON = "+outputGenerationFile.getAbsolutePath());
			//   Add new merged file to session
			addGeneratedFileToSession(p_oOutputFile, p_oGeneratorContext);			
		}
		else {
			throw new AdjavaException("[AbstractJsonMergeGenerator#doJsonMergeGeneration] This file has not been correctly merged : "+outputGenerationFile.toPath());			
		}

			
		log.debug("[AbstractJsonMergeGenerator#doJsonMerge] JSON Merge generation - end");
	}

	/**
	 * Retrieve the newly generated file with the current model. 
	 * @param p_oSource
	 * @param p_sTemplatePath
	 * @param p_oOutputFile
	 * @param p_oProject
	 * @param p_oGeneratorContext
	 * @param srcJsonFile
	 * @return the new generated file path
	 * @throws AdjavaException
	 * @throws IOException
	 */
	private Path getNewGeneratedJsonPath(Source p_oSource,
			String p_sTemplatePath, GeneratedFile<FormatOptions> p_oOutputFile,
			XProject<D> p_oProject, DomainGeneratorContext p_oGeneratorContext,
			File srcJsonFile) throws AdjavaException, IOException {
		Path dirForNewGeneratedJson = Paths.get(this.getMergeBackupAbsolutePath(p_oProject), "generatedFiles", AbstractXslGenerator.generationDate,srcJsonFile.getParentFile().getPath());
		Files.createDirectories(dirForNewGeneratedJson.toAbsolutePath());
		Path newGeneratedJsonPath = dirForNewGeneratedJson.resolve(srcJsonFile.getName());
		
		log.debug("[AbstractXmlMergeGenerator#doXmlMergeGeneration]  transformToFile - apply XSL rules and write file");
		try {
			GeneratedFile<FormatOptions> newGeneratedFileContainer = new GeneratedFile<FormatOptions>(newGeneratedJsonPath.toFile(), p_oOutputFile.getFormatOptions());
			newGeneratedFileContainer.setInjection(p_oOutputFile.isInjection());
			newGeneratedFileContainer.addAllXslProperties(p_oOutputFile.getXslProperties());
			newGeneratedFileContainer.setFileFromRoot(new File(p_oProject.getBaseDir(), p_oOutputFile.getFile().getPath()));
			
			this.doTransformToFile(p_oSource, p_sTemplatePath, newGeneratedFileContainer, p_oProject, p_oGeneratorContext, true);
			
			// Execute code formatters
			for (CodeFormatter oCodeFormatter : p_oProject.getDomain().getCodeFormatters()) {
				List<GeneratedFile> onefileList = new ArrayList<GeneratedFile>();
				onefileList.add(newGeneratedFileContainer);
				oCodeFormatter.format(onefileList, p_oProject.getDomain().getFileEncoding());
				if (MessageHandler.getInstance().hasErrors()) {
					break;
				}
			}
		} catch (Exception e) {
			throw new AdjavaException("Error while writing file",e.getMessage());
		}
		
		return newGeneratedJsonPath;
	}

	/**
	 * Try to find the last generated JSON for this file.
	 * 
	 * @param p_oProject
	 * @param srcJsonFile
	 * @param oldGeneratedJsonPath
	 * @return the path if found, null otherwise
	 * @throws AdjavaException
	 */
	private Path getOldGeneratedJsonPath(XProject<D> p_oProject, File srcJsonFile) throws AdjavaException {
		Path oldGeneratedJsonPath = null;
		Path generatedFilesDir = Paths.get(this.getMergeBackupAbsolutePath(p_oProject),"generatedFiles");
		String[] generatedFilesSubDirNames = generatedFilesDir.toFile().list(new FilenameFilter() {
			@Override
			public boolean accept(File p_oDir, String p_oName) {
				return new File(p_oDir, p_oName).isDirectory();
			}
		});
		
		if(generatedFilesSubDirNames != null && generatedFilesSubDirNames.length > 0){
			Arrays.sort(generatedFilesSubDirNames); // sort by creation date

			int generatedFilesSubDirIndex = generatedFilesSubDirNames.length-1;
			
			while(oldGeneratedJsonPath == null && generatedFilesSubDirIndex >=0){
				Path filePath = generatedFilesDir.resolve(generatedFilesSubDirNames[generatedFilesSubDirIndex]).resolve(srcJsonFile.getPath());
				File oldGeneratedJsonFile = filePath.toFile();
				generatedFilesSubDirIndex--;
				if (oldGeneratedJsonFile.exists() && oldGeneratedJsonFile.isFile() && oldGeneratedJsonFile.length() > 0) {
					oldGeneratedJsonPath = filePath;
				};
			}
		}
		return oldGeneratedJsonPath;
	}
	
}
