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
package com.a2a.adjava.uuidgenerator;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** 
 * Generates UUID and replaces a list of tags with them <BR>
 * Used for Windows 8 projects
 * @goal uuid-generator
 */
public class UUIDGenerator extends AbstractMojo {

	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(UUIDGenerator.class);
	
	/**
	 * Windows 8 solution files' extension
	 */
	private static final String SOLUTION_EXTENSION = ".sln";
	/**
	 * Windows 8 project files' extension
	 */
	private static final String PROJECT_EXTENSION = ".csproj";
	/**
	 * XML files extension
	 */
	private static final String XML_EXTENSION = ".xml";
	/**
	 * csharp files' extension
	 */
	private static final String SOURCE_FILE_EXTENSION = ".cs";
	/**
	 * Windows 8 manifest files' extension
	 */
	private static final String MANIFEST_EXTENSION = ".appxmanifest";
	/**
	 * Windows 8 shared project files' extension
	 */
	private static final String SHARED_EXTENSION = ".shproj";
	/**
	 * Windows 8 project item files' extension
	 */
	private static final String PROJ_ITEM_EXTENSION = ".projitems";
	/**
	 * targetDirectory
	 * 
	 * @parameter expression="${project.basedir}"
	 */
	private File rootDirectory;
	
	/**
	 * tags to look for to be replaced in the generated project
	 */
	private String[] calculatedPropertiesList = {
			"\\$guidSolution\\$",
			"\\$guidProjetStore\\$",
			"\\$guidProjetPhoneId\\$",
			"\\$guidProjetPhone\\$",
			"\\$guidProjetModel\\$",
			"\\$guidProjetDao\\$",
			"\\$guidProjetDataLoader\\$",
			"\\$guidProjetViewModel\\$",
			"\\$guidProjetApplication\\$",
			"\\$guidProjetView\\$",
			"\\$guidPackage\\$",
			"\\$guidFolder\\$",
			"\\$guidProjetShared\\$",
			"\\$guidPublisher\\$"};
	
	/**
	 * Map of the tags and their generated UUIDs
	 */
	private Map<String, String> propertiesReplacement = new HashMap<String, String>();
	
	/**
	 * File filter to apply to our file query
	 */
	private FileFilter filter = new FileFilter() {
		@Override
		public boolean accept(File p_oFile) {
			return  p_oFile.isDirectory() 
					|| p_oFile.getName().toLowerCase().endsWith(SOLUTION_EXTENSION) 
					|| p_oFile.getName().toLowerCase().endsWith(PROJECT_EXTENSION) 
					|| p_oFile.getName().toLowerCase().endsWith(XML_EXTENSION) 
					|| p_oFile.getName().toLowerCase().endsWith(SOURCE_FILE_EXTENSION) 
					|| p_oFile.getName().toLowerCase().endsWith(MANIFEST_EXTENSION) 
					|| p_oFile.getName().toLowerCase().endsWith(SHARED_EXTENSION) 
					|| p_oFile.getName().toLowerCase().endsWith(PROJ_ITEM_EXTENSION);
		}
	};
	
	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		log.debug("----DEBUG processing UUIDs");
		
		initializeMap();
		
		Object[] oFilesList = discoverFiles(rootDirectory.getAbsolutePath(), filter);
		
		for (Object oFile : oFilesList) {
			processFile(((File)oFile).getAbsolutePath(), StandardCharsets.UTF_8, propertiesReplacement);
		}
	}
	
	/**
	 * Initializes the map containing the keys to find in the files and the replacement UUID to put
	 */
	private void initializeMap() {
		for (int iRank=0 ; iRank < calculatedPropertiesList.length ; iRank ++) {
			propertiesReplacement.put(calculatedPropertiesList[iRank], UUID.randomUUID().toString());
		}
	}
	
	/**
	 * Returns a list of files found in the given directory and all its subdirs
	 * @param p_sRootDirectory the root directory in which to look for the files
	 * @param p_oFilter the FileFilter to apply to look for the files 
	 * @return an array containing the list of File founds
	 */
	@SuppressWarnings("unchecked")
	private File[] discoverFiles(String p_sRootDirectory, FileFilter p_oFilter) {
		List<File> r_listFiles = new ArrayList<File>();
		
		File oRootDir = new File(p_sRootDirectory);
		
		if (oRootDir.isDirectory()) {
			for (File file : oRootDir.listFiles(p_oFilter)) {
				if (file.isDirectory()) {
					r_listFiles.addAll(Arrays.asList(discoverFiles(file.getAbsolutePath(), p_oFilter)));
				} else {
					r_listFiles.add(file);
				}
			}
		}
		
		return (File[]) r_listFiles.toArray();
	}
	
	/**
	 * Replaces from the file - found in filePath with encoding charset - the keys of the Map with their values
	 * @param p_sFilePath the path to the file
	 * @param p_oCharset charset of the file
	 * @param p_oReplacement map of the tags to find and the replacements to apply
	 */
	@SuppressWarnings("rawtypes")
	private void processFile(String p_sFilePath, Charset p_oCharset, Map<String, String> p_oReplacement) {
		try {
			log.debug("----DEBUG processFile " + p_sFilePath);
			
			Path oPath = Paths.get(p_sFilePath);
			String sContent = new String(Files.readAllBytes(oPath), p_oCharset);
			
			Iterator oIt = p_oReplacement.entrySet().iterator();
		    while (oIt.hasNext()) {
		    	
		        Map.Entry oPairs = (Map.Entry)oIt.next();
		        
		        sContent = sContent.replaceAll(oPairs.getKey().toString(), oPairs.getValue().toString());
		    }
		    
			Files.write(oPath, sContent.getBytes(p_oCharset));
		} catch (IOException e) {
			log.error(e.toString());
		}
	}
}
