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
package com.a2a.adjava.generator.core.injection;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;

import com.a2a.adjava.AdjavaException;
import com.a2a.adjava.codeformatter.FormatOptions;
import com.a2a.adjava.codeformatter.GeneratedFile;
import com.a2a.adjava.generator.core.AbstractXslGenerator;
import com.a2a.adjava.generators.DomainGeneratorContext;
import com.a2a.adjava.generators.ResourceGenerator;
import com.a2a.adjava.utils.FileTypeUtils;
import com.a2a.adjava.xmodele.IDomain;
import com.a2a.adjava.xmodele.XProject;

/**
 * @author lmichenaud
 *
 * @param <D>
 */
public abstract class AbstractInjectionGenerator<D extends IDomain<?, ?>> extends AbstractXslGenerator<D> implements ResourceGenerator<D> {

	/**
	 * @param p_sTemplatePath
	 * @param p_sOutputFile
	 * @param p_xElem
	 * @param p_oProject
	 * @param p_oGlobalSession
	 * @param p_listFilePartGenerationConfig
	 * @throws Exception
	 */
	public void doInjectionTransform(String p_sOutputFile, XProject<D> p_oProject,
			DomainGeneratorContext p_oGeneratorContext, FilePartGenerationConfig... p_listFilePartGenerationConfig)
			throws Exception {
		this.doInjectionTransform( new GeneratedFile<FormatOptions>(p_sOutputFile), p_oProject, p_oGeneratorContext, p_listFilePartGenerationConfig);
	}
	
	/**
	 * @param p_sTemplatePath
	 * @param p_oOutputFile
	 * @param p_xElem
	 * @param p_oProject
	 * @param p_oGlobalSession
	 * @param p_listFilePartGenerationConfig
	 * @throws Exception
	 */
	public void doInjectionTransform(GeneratedFile p_oOutputFile, XProject<D> p_oProject,
			DomainGeneratorContext p_oGeneratorContext, FilePartGenerationConfig... p_listFilePartGenerationConfig)
			throws Exception {

		Map<String, String> mapGeneratedParts = new HashMap<String, String>();

		p_oOutputFile.setInjection(true);
		
		File oOutputFile = new File(p_oProject.getBaseDir(), p_oOutputFile.getFile().getPath());
		p_oOutputFile.setFileFromRoot(oOutputFile);
		if (!oOutputFile.exists()) {
			throw new AdjavaException("Can't find file : {}", oOutputFile.getAbsolutePath());
		}

		// Genere all parts
		for (FilePartGenerationConfig oPartGenConfig : p_listFilePartGenerationConfig) {
			String sContent = doTransformToString(oPartGenConfig.getXml(), oPartGenConfig.getTemplatePath(), p_oOutputFile.getXslProperties(), p_oProject );
			mapGeneratedParts.put(oPartGenConfig.getId(), sContent);
		}

		// Read file to update (error if doesnot exist)
		String sContent = FileUtils.readFileToString(oOutputFile, p_oProject.getDomain().getFileEncoding());

		// Reconstruct file with generated parts
		String sNewContent = rebuildContent(sContent, mapGeneratedParts, oOutputFile);

		// Write file
		FileUtils.writeStringToFile(oOutputFile, sNewContent, p_oProject.getDomain().getFileEncoding());

		// Add generated file to session
		addGeneratedFileToSession(p_oOutputFile, p_oGeneratorContext);
	}

	/**
	 * Rebuild file content with generated parts
	 * @param p_sContent file content
	 * @param p_mapGeneratedParts generated parts
	 * @return new file content
	 */
	private String rebuildContent(String p_sContent, Map<String, String> p_mapGeneratedParts, File p_oFile) throws Exception {

		String r_sNewContent = p_sContent;

		for (Entry<String, String> oEntry : p_mapGeneratedParts.entrySet()) {
			String sKey = oEntry.getKey();
			String sStartXTag = getGeneratedXStartTag(sKey, p_oFile);
			String sStartTag = getGeneratedStartTag(sKey, p_oFile);
			String sEndTag = getGeneratedEndTag(p_oFile);
			int iStartPos = r_sNewContent.indexOf(sStartTag);
			int iStartXPos = r_sNewContent.indexOf(sStartXTag);
			if (iStartXPos != -1) {
				//présence de la balise generated + [X] donc on écrase
				int iEndPos = r_sNewContent.indexOf(sEndTag, iStartXPos + sStartXTag.length());
				if (iEndPos != -1) {
					r_sNewContent = r_sNewContent.substring(0, iStartXPos + sStartXTag.length() + 1) + oEntry.getValue() + r_sNewContent.substring(iEndPos);
				} 
				else {
					throw new AdjavaException("Can't find generated-part end tag with id : '{}' in file {}", sKey, p_oFile.getAbsolutePath());
				}
			} 
			else if (iStartPos != -1) {
				//présence de la balise generated sans le [X] donc on fait une sauvegarde
				int iEndPos = r_sNewContent.indexOf(sEndTag, iStartPos + sStartTag.length());
				if (iEndPos != -1) {
					String sBefore = r_sNewContent.substring(0, Math.min(iEndPos + sEndTag.length() + 1, r_sNewContent.length()));
					String sAfter = r_sNewContent.substring(Math.min(iEndPos + sEndTag.length() + 1, r_sNewContent.length()));
					if(getStartCommentTag(p_oFile) == null) {
						return r_sNewContent;
					}
					if (sAfter != null  && sAfter.contains(getStartCommentTag(p_oFile))){
						sAfter = sAfter.substring(sAfter.indexOf(getEndCommentTag(p_oFile))+getEndCommentTag(p_oFile).length()+1);
					}
					r_sNewContent = sBefore
							+ getStartCommentTag(p_oFile)
							+"\n"
							+ oEntry.getValue() 
							+ getEndCommentTag(p_oFile)
							+"\n"
							+ sAfter;
				}
				else {
					throw new AdjavaException("Can't find generated-part end tag with id : '{}' in file {}", sKey, p_oFile.getAbsolutePath());
				}
			}
			else {
				throw new AdjavaException("Can't find generated-part start tag with id : '{}' in file {}", sKey, p_oFile.getAbsolutePath());
			}
		}

		return r_sNewContent;
	}
	
	/**
	 * Return start marker for commented generated source code
	 * @param p_oFile
	 * @return
	 */
	private String getStartCommentTag(File p_oFile) {
		String r_sStartTag = null;
		if ( FileTypeUtils.isJavaFile(p_oFile) || FileTypeUtils.isIosFile(p_oFile) || FileTypeUtils.isJavascriptFile(p_oFile) || FileTypeUtils.isJsonFile(p_oFile)){
			r_sStartTag = "//commented-generation-start";
		} else if ( FileTypeUtils.isXmlFile(p_oFile) || FileTypeUtils.isHtmlFile(p_oFile)) {
			r_sStartTag = "<!--commented-generation-start";
		} else if ( FileTypeUtils.isPropertiesFile(p_oFile)) {
			r_sStartTag = "#commented-generation-start";
		} else if ( FileTypeUtils.isSqlFile(p_oFile)) {
			r_sStartTag = "--commented-generation-start";
		}
		return r_sStartTag;
	}

	/**
	 * Return end marker for commented generated source code
	 * @param p_oFile
	 * @return
	 */
	private String getEndCommentTag(File p_oFile) {
		String r_sStartTag = null;
		if (FileTypeUtils.isJavaFile(p_oFile) || FileTypeUtils.isIosFile(p_oFile) || FileTypeUtils.isJavascriptFile(p_oFile) || FileTypeUtils.isJsonFile(p_oFile)) {
			r_sStartTag = "//commented-generation-end";
		} else if (FileTypeUtils.isXmlFile(p_oFile) || FileTypeUtils.isHtmlFile(p_oFile)) {
			r_sStartTag = "commented-generation-start-->";
		} else if (FileTypeUtils.isPropertiesFile(p_oFile)) {
			r_sStartTag = "#commented-generation-end";
		} else if (FileTypeUtils.isSqlFile(p_oFile)) {
			r_sStartTag = "--commented-generation-end";
		}
		return r_sStartTag;
	}
	
	/**
	 * Return code-generated start tag
	 * @param p_sKey key of the code generated bloc
	 * @param p_oFile file
	 * @return
	 * @throws AdjavaException failure
	 */
	protected String getGeneratedXStartTag( String p_sKey, File p_oFile ) throws AdjavaException {
		String r_sStartTag = null ;
		if ( FileTypeUtils.isJavaFile(p_oFile) || FileTypeUtils.isIosFile(p_oFile) || FileTypeUtils.isJsonFile(p_oFile) || FileTypeUtils.isJavascriptFile(p_oFile) || FileTypeUtils.isIosStringFile(p_oFile) || FileTypeUtils.isScssFile(p_oFile)) {
			r_sStartTag = "//@generated-start[" + p_sKey + "][X]";
		}
		else if ( FileTypeUtils.isXmlFile(p_oFile) || FileTypeUtils.isHtmlFile(p_oFile)) {
			r_sStartTag = "<!--//@generated-start[" + p_sKey + "][X]-->";
		}
		else {
			throw new AdjavaException("Can't find start tag: '{}' for generated code for file {}", r_sStartTag, p_oFile.getPath());
		}
		return r_sStartTag ;
	}
}
