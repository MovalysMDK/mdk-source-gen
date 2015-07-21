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
package com.a2a.adjava.generator.core.append;

import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;

import org.apache.commons.io.FileUtils;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.dom4j.Document;
import org.dom4j.io.DocumentSource;
import org.xml.sax.InputSource;

import com.a2a.adjava.AdjavaProperty;
import com.a2a.adjava.codeformatter.FormatOptions;
import com.a2a.adjava.codeformatter.GeneratedFile;
import com.a2a.adjava.generator.core.AbstractXslGenerator;
import com.a2a.adjava.generator.core.GeneratorUtils;
import com.a2a.adjava.generators.DomainGeneratorContext;
import com.a2a.adjava.utils.FileTypeUtils;
import com.a2a.adjava.xmodele.IDomain;
import com.a2a.adjava.xmodele.XProject;

/**
 * AppendGenerator If file doesnot exist, generate the file If file do exist,
 * generate at the end inside comments
 * @author lmichenaud
 * 
 * @param <D> type of Domain
 * 
 */
public abstract class AbstractAppendGenerator<D extends IDomain<?, ?>> extends AbstractXslGenerator<D> {

	/**
	 * Start append generation
	 * @param p_xDoc xml document
	 * @param p_sTemplatePath template path
	 * @param p_oOutputFile output file
	 * @param p_oProject project
	 * @param p_oGeneratorContext session
	 * @throws Exception generation failure
	 */
	public void doAppendGeneration(Document p_xDoc, String p_sTemplatePath, File p_oOutputFile,
			XProject<D> p_oProject, DomainGeneratorContext p_oGeneratorContext) throws Exception {
		this.doAppendGeneration( new DocumentSource(p_xDoc), p_sTemplatePath, new GeneratedFile<FormatOptions>(p_oOutputFile), p_oProject, p_oGeneratorContext );
	}
	
	/**
	 * Start append generation
	 * @param p_xDoc xml document
	 * @param p_sTemplatePath template path
	 * @param p_oOutputFile output file
	 * @param p_oProject project
	 * @param p_oGeneratorContext session
	 * @throws Exception generation failure
	 */
	public void doAppendGeneration(Document p_xDoc, String p_sTemplatePath, GeneratedFile p_oOutputFile,
			XProject<D> p_oProject, DomainGeneratorContext p_oGeneratorContext) throws Exception {
		this.doAppendGeneration( new DocumentSource(p_xDoc), p_sTemplatePath, p_oOutputFile, p_oProject, p_oGeneratorContext );
	}
	
	/**
	 * Start append generation
	 * @param p_xDoc xml document
	 * @param p_sTemplatePath template path
	 * @param p_sOutputFile output file
	 * @param p_oProject project
	 * @param p_oGlobalSession session
	 * @throws Exception generation failure
	 */
	public void doAppendGeneration(org.w3c.dom.Document p_xDoc, String p_sTemplatePath, String p_sOutputFile,
			XProject<D> p_oProject, DomainGeneratorContext p_oGeneratorContext) throws Exception {
		this.doAppendGeneration(new DOMSource(p_xDoc), p_sTemplatePath, new GeneratedFile<FormatOptions>(p_sOutputFile), p_oProject, p_oGeneratorContext );
	}
	
	/**
	 * Start append generation
	 * @param p_xDoc xml document
	 * @param p_sTemplatePath template path
	 * @param p_oOutputFile output file
	 * @param p_oProject project
	 * @param p_oGlobalSession session
	 * @throws Exception generation failure
	 */
	public void doAppendGeneration(org.w3c.dom.Document p_xDoc, String p_sTemplatePath, GeneratedFile p_oOutputFile,
			XProject<D> p_oProject, DomainGeneratorContext p_oGeneratorContext) throws Exception {
		this.doAppendGeneration(new DOMSource(p_xDoc), p_sTemplatePath, p_oOutputFile, p_oProject, p_oGeneratorContext );
	}
	
	/**
	 * Start append generation
	 * @param p_oSource xml document
	 * @param p_sTemplatePath template path
	 * @param p_oOutputFile output file
	 * @param p_oProject project
	 * @param p_oGlobalSession session
	 * @throws Exception generation failure
	 */
	public void doAppendGeneration(Source p_oSource, String p_sTemplatePath, GeneratedFile p_oOutputFile,
			XProject<D> p_oProject, DomainGeneratorContext p_oGeneratorContext) throws Exception {
		
		File oFile = new File(p_oProject.getBaseDir(), p_oOutputFile.getFile().getPath());
		p_oOutputFile.setFileFromRoot(oFile);

		String sForceOverwrite = p_oProject.getDomain().getGlobalParameters().get(AdjavaProperty.APPEND_GENERATOR_FORCE_OVERWRITE.getName());
		
		if (!oFile.exists() || (sForceOverwrite != null && sForceOverwrite.equalsIgnoreCase("true"))) {
			this.doTransformToFile(p_oSource, p_sTemplatePath, p_oOutputFile, p_oProject, p_oGeneratorContext,false);
		} else {
			
			StringBuilder sNewContent = new StringBuilder();
			
			String sExistingContent = FileUtils.readFileToString(oFile, p_oProject.getDomain()
					.getFileEncoding());

			// Delete old commented generation if exist
			String sStartTagRegEx = this.getStartCommentTagRegEx(oFile);
			
			Pattern oPattern = Pattern.compile(sStartTagRegEx);
			Matcher oMatcher = oPattern.matcher(sExistingContent);

			if (oMatcher.find()) {
				sExistingContent = oMatcher.group(1);
			}
			sNewContent.append(sExistingContent);
			
			// Execute xsl transformation
			String sContent = this.doTransformToString(p_oSource, p_sTemplatePath, p_oOutputFile.getXslProperties(), p_oProject);

			// Comment source result of xsl transformation
			this.commentSource(sContent, sNewContent, oFile);
			
			// Write new  file
			FileUtils.writeStringToFile(oFile, sNewContent.toString(), p_oProject.getDomain()
					.getFileEncoding());
			
			this.addGeneratedFileToSession(p_oOutputFile, p_oGeneratorContext);
			
			if (isDebug()) {
				GeneratorUtils.writeXmlDebugFile(p_oSource, p_oOutputFile.getFile().getPath(), p_oProject);
			}
		}
	}

	/**
	 * Comment source code and write it to StringBuilder
	 * @param p_sContent code to comment
	 * @param p_sOutput StringBuilder to write to
	 * @param p_oFile file
	 * @throws Exception 
	 */
	private void commentSource(String p_sContent, StringBuilder p_sOutput, File p_oFile) throws Exception {

		p_sOutput.append(getStartCommentTag(p_oFile));
		p_sOutput.append("\n\n");

		if ( FileTypeUtils.isJavaFile(p_oFile) || FileTypeUtils.isJsonFile(p_oFile)) {
			this.commentJavaSource(p_sContent, p_sOutput);
		} else if (FileTypeUtils.isXmlFile(p_oFile)) {
			this.commentXmlSource(p_sContent, p_sOutput);
		} else if (FileTypeUtils.isHtmlFile(p_oFile)) {
			this.commentHtmlSource(p_sContent, p_sOutput);
		} else if (FileTypeUtils.isPropertiesFile(p_oFile)) {
			commentPropertiesSource(p_sContent, p_sOutput);
		} else if (FileTypeUtils.isSqlFile(p_oFile)) {
			this.commentSqlSource(p_sContent, p_sOutput);
		}
		
		p_sOutput.append("\n\n");
		p_sOutput.append(getEndCommentTag(p_oFile));
	}

	/**
	 * Comment sql source code
	 * @param p_sContent
	 * @param p_sNewContent
	 */
	private void commentSqlSource(String p_sContent, StringBuilder p_sNewContent) {
		p_sNewContent.append("--");
		p_sNewContent.append(p_sContent.replaceAll("\n", "\n-- "));
	}

	/**
	 * Comment properties source code
	 * @param p_sContent
	 * @param p_sNewContent
	 */
	private void commentPropertiesSource(String p_sContent, StringBuilder p_sNewContent) {
		p_sNewContent.append("#");
		p_sNewContent.append(p_sContent.replaceAll("\n", "\n# "));
	}

	/**
	 * Comment xml source code
	 * @param p_sContent source code to comment
	 * @param p_sNewContent append commented source code into p_sNewContent
	 * @throws Exception 
	 */
	private void commentXmlSource(String p_sContent, StringBuilder p_sNewContent) throws Exception {
		
		DocumentBuilderFactory oDbf = DocumentBuilderFactory.newInstance();
		
		DocumentBuilder oDocBuilder = oDbf.newDocumentBuilder();
		StringReader oStringReader = new StringReader(p_sContent);
		try {
			InputSource oInputSource = new InputSource(oStringReader);
			org.w3c.dom.Document xDoc = oDocBuilder.parse(oInputSource);
			OutputFormat oFormat = new OutputFormat();
			oFormat.setLineWidth(40);
			oFormat.setIndenting(true);
			oFormat.setIndent(4);
			Writer oWriter = new StringWriter();
			try {
				XMLSerializer oSerializer = new XMLSerializer(oWriter, oFormat);
				oSerializer.serialize(xDoc);
				p_sNewContent.append(oWriter);
			} finally {
				oWriter.close();
			}
		} finally {
			oStringReader.close();
		}
	}

	/**
	 * Comment html source code
	 * @param p_sContent source code to comment
	 * @param p_sNewContent append commented source code into p_sNewContent
	 * @throws Exception 
	 */
	private void commentHtmlSource(String p_sContent, StringBuilder p_sNewContent) throws Exception {
		p_sNewContent.append(p_sContent.replaceAll("-->", "-> ").replaceAll(">", ">\n "));
	}
	
	/**
	 * Comment java source code
	 * @param p_sContent
	 * @param p_sNewContent
	 */
	private void commentJavaSource(String p_sContent, StringBuilder p_sNewContent) {
		p_sNewContent.append("//");
		p_sNewContent.append(p_sContent.replaceAll("\n", "\n// "));
	}

	/**
	 * Return start marker for commented generated source code
	 * @param p_oFile
	 * @return String r_sStartTag
	 */
	private String getStartCommentTag(File p_oFile) {
		String r_sStartTag = null;
		if (FileTypeUtils.isJavaFile(p_oFile) || FileTypeUtils.isJsonFile(p_oFile)) {
			r_sStartTag = "//commented-generation-start";
		} else if (FileTypeUtils.isXmlFile(p_oFile) || FileTypeUtils.isHtmlFile(p_oFile)) {
			r_sStartTag = "<!--commented-generation-start";
		} else if (FileTypeUtils.isPropertiesFile(p_oFile)) {
			r_sStartTag = "#commented-generation-start";
		} else if (FileTypeUtils.isSqlFile(p_oFile)) {
			r_sStartTag = "--commented-generation-start";
		}
		return r_sStartTag;
	}

	/**
	 * Return start marker reg ex for commented generated source code
	 * @param p_oFile
	 * @return
	 */
	private String getStartCommentTagRegEx(File p_oFile) {
		String r_sStartTag = null;
		if (FileTypeUtils.isJavaFile(p_oFile) || FileTypeUtils.isJsonFile(p_oFile)) {
			r_sStartTag = "([\\S\\s]*)//\\s*commented-generation-start";
		} else if (FileTypeUtils.isXmlFile(p_oFile) || FileTypeUtils.isHtmlFile(p_oFile) ) {
			r_sStartTag = "([\\S\\s]*)<!--\\s*commented-generation-start";
		} else if (FileTypeUtils.isPropertiesFile(p_oFile)) {
			r_sStartTag = "([\\S\\s]*)#\\s*commented-generation-start";
		} else if (FileTypeUtils.isSqlFile(p_oFile)) {
			r_sStartTag = "([\\S\\s]*)--\\s*commented-generation-start";
		}
		return r_sStartTag;
	}
	
	/**
	 * Return end marker for commented generated source code
	 * @param p_oFile 
	 * @return String r_sStartTag
	 */
	private String getEndCommentTag(File p_oFile) {
		String r_sStartTag = null;		
		if (FileTypeUtils.isJavaFile(p_oFile) || FileTypeUtils.isJsonFile(p_oFile)) {
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
}
