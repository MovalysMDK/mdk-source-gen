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
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.io.DocumentSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.AdjavaException;
import com.a2a.adjava.codeformatter.FormatOptions;
import com.a2a.adjava.codeformatter.GeneratedFile;
import com.a2a.adjava.codeformatter.GeneratedFile.XslOutputProperty;
import com.a2a.adjava.generators.DomainGeneratorContext;
import com.a2a.adjava.utils.ClasspathURIResolver;
import com.a2a.adjava.utils.VersionHandler;
import com.a2a.adjava.xmodele.IDomain;
import com.a2a.adjava.xmodele.IModelDictionary;
import com.a2a.adjava.xmodele.IModelFactory;
import com.a2a.adjava.xmodele.XProject;

/**
 * Xsl Generator
 * 
 * @author lmichenaud
 *
 * @param <D>
 */
public abstract class AbstractXslGenerator<D extends IDomain<? extends IModelDictionary, ? extends IModelFactory>> extends AbstractGenerator<D> {

	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(AbstractXslGenerator.class);

	/**
	 * Cache des transformers
	 */
	private Map<String, Transformer> cacheTransformers = new HashMap<String, Transformer>();

	/**
	 * Factory des transformers
	 */
	private TransformerFactory transformerFactory = TransformerFactory.newInstance();

	/**
	 * Instancie le transformer a partir de son chemin dans le classpath
	 * 
	 * @param p_sTemplatePath chemin du xsl dans le classpath
	 * @return le transformer
	 * @throws Exception echec de creation du transformer
	 */
	protected Transformer getTransformer(String p_sTemplatePath, XProject<D> p_oProject) throws Exception {

		String sFullPath = StringUtils.join(p_oProject.getDomain().getTemplate(), "/", p_sTemplatePath );
		String sParent = StringUtils.substringBeforeLast(sFullPath, "/");

		Transformer r_oTransformer = this.cacheTransformers.get(sFullPath);

		if (r_oTransformer == null) {
			this.transformerFactory.setURIResolver(new ClasspathURIResolver(sParent, VersionHandler.getGenerationVersion()));
			InputStream oIs = null;
			try {
				oIs = this.getClass().getResourceAsStream(sFullPath);
				if (oIs != null) {
					r_oTransformer = this.transformerFactory.newTransformer(new StreamSource(oIs));
					r_oTransformer.setURIResolver(new ClasspathURIResolver(sParent, VersionHandler.getGenerationVersion()));
				} else {
					throw new AdjavaException("Impossible de trouver le template : {}", sFullPath);
				}
			} finally {
				if (oIs != null) {
					oIs.close();
				}
			}
			this.cacheTransformers.put(sFullPath, r_oTransformer);
		}
		return r_oTransformer;
	}

	/**
	 * Xsl Transformation, result is a string
	 * @param p_xDoc document
	 * @param p_sTemplatePath xsl template path
	 * @param p_listXslOutputProperties xsl output properties
	 * @param p_oProject project
	 * @return string
	 * @throws Exception
	 */
	protected String doTransformToString(Document p_xDoc, String p_sTemplatePath, List<XslOutputProperty> p_listXslOutputProperties, XProject<D> p_oProject)
			throws Exception {
		return this.doTransformToString(new DocumentSource(p_xDoc), p_sTemplatePath, p_listXslOutputProperties, p_oProject);
	}

	/**
	 * Xsl Transformation, result is a string
	 * @param p_xDoc document
	 * @param p_sTemplatePath xsl template path
	 * @param p_listXslOutputProperties xsl output properties
	 * @param p_oProject project
	 * @return string
	 * @throws Exception
	 */
	protected String doTransformToString(org.w3c.dom.Document p_xDoc, String p_sTemplatePath, List<XslOutputProperty> p_listXslOutputProperties, XProject<D> p_oProject)
			throws Exception {
		return this.doTransformToString(new DOMSource(p_xDoc), p_sTemplatePath, p_listXslOutputProperties, p_oProject);
	}

	/**
	 * Xsl Transformation, result is a string
	 * @param p_xDoc document
	 * @param p_sTemplatePath xsl template path
	 * @param p_listXslOutputProperties xsl output properties
	 * @param p_oProject project
	 * @return string
	 * @throws Exception
	 */
	protected String doTransformToString(Source p_oSource, String p_sTemplatePath, List<XslOutputProperty> p_listXslOutputProperties, XProject<D> p_oProject)
			throws Exception {

		Transformer oTransformer = getTransformer(p_sTemplatePath, p_oProject);
		StringWriter oResult = new StringWriter();
		try {
			StreamResult xResult = new StreamResult(oResult);
			this.applyXslProperties(p_listXslOutputProperties, oTransformer, p_oProject);
			oTransformer.transform(p_oSource, xResult);
			oResult.flush();
		} finally {
			oResult.close();
		}
		return oResult.toString();
	}

	/**
	 * Xsl Transformation, result is a file
	 * @param p_xDoc
	 * @param p_sTemplatePath
	 * @param p_oOutputFile
	 * @param p_oProject
	 * @param p_bDebug force debug or not
	 * @throws Exception
	 */
	protected void doTransformToFile(Document p_xDoc, String p_sTemplatePath, String p_sOutputFile,
			XProject<D> p_oProject, DomainGeneratorContext p_oGeneratorContext ) throws Exception {
		this.doTransformToFile(p_xDoc, p_sTemplatePath, new GeneratedFile<FormatOptions>(p_sOutputFile), p_oProject, p_oGeneratorContext);
	}

	/**
	 * Xsl Transformation, result is a file
	 * @param p_xDoc
	 * @param p_sTemplatePath
	 * @param p_oOutputFile
	 * @param p_oProject
	 * @param p_bDebug force debug or not
	 * @throws Exception
	 */
	protected void doTransformToFile(Document p_xDoc, String p_sTemplatePath, GeneratedFile p_oOutputFile,
			XProject<D> p_oProject, DomainGeneratorContext p_oGeneratorContext ) throws Exception {
		this.doTransformToFile(new DocumentSource(p_xDoc), p_sTemplatePath, p_oOutputFile, p_oProject, p_oGeneratorContext, false);
	}

	/**
	 * Xsl Transformation, result is a file
	 * @param p_oSource
	 * @param p_sTemplatePath
	 * @param p_oOutputFile
	 * @param p_oProject
	 * @param p_oGeneratorContext
	 * @param p_bTemporaryFile
	 * @throws Exception
	 */
	protected void doTransformToFile(Source p_oSource, String p_sTemplatePath, GeneratedFile p_oOutputFile,
			XProject<D> p_oProject, DomainGeneratorContext p_oGeneratorContext, boolean p_bTemporaryFile ) throws Exception {
		this.doTransformToFile(p_oSource, p_sTemplatePath, p_oOutputFile, p_oProject, p_oGeneratorContext, p_bTemporaryFile, true);
	}
	
	/**
	 * Xsl Transformation, result is a file
	 * @param p_oSource
	 * @param p_sTemplatePath
	 * @param p_XslOutputProperties
	 * @param p_oOutputFile
	 * @param p_oProject
	 * @param p_oGeneratorContext
	 * @param p_bTemporaryFile
	 * @throws Exception
	 */
	protected void doTransformToFile(Source p_oSource, String p_sTemplatePath, GeneratedFile p_oOutputFile,
			XProject<D> p_oProject, DomainGeneratorContext p_oGeneratorContext, boolean p_bTemporaryFile, boolean p_bResolve) throws Exception {

		
		File oFile = p_oOutputFile.getFile();
		if (p_bResolve) {
			oFile = Paths.get(p_oProject.getBaseDir()).resolve(p_oOutputFile.getFile().getPath()).toFile();
		}
		log.debug("[AbstractXslGenerator#doTransformToFile] processing "+oFile.getAbsolutePath()+" ...");
		p_oOutputFile.setFileFromRoot(oFile);
		
		if (!oFile.getParentFile().exists()) {
			oFile.getParentFile().mkdirs();
		}
		
		OutputStreamWriter oOutputStreamWriter = null;
		try {			
			oOutputStreamWriter  = new OutputStreamWriter(new FileOutputStream(oFile), p_oProject.getDomain().getFileEncoding());
			StreamResult xResult = new StreamResult(oOutputStreamWriter);
			
			Transformer oTransformer = this.getTransformer(p_sTemplatePath, p_oProject);
			this.applyXslProperties(p_oOutputFile.getXslProperties(), oTransformer, p_oProject);
			oTransformer.transform(p_oSource, xResult);
			if(!p_bTemporaryFile){
				this.addGeneratedFileToSession(p_oOutputFile, p_oGeneratorContext);				
			}
		} finally {
			
			if(oOutputStreamWriter != null) {
				oOutputStreamWriter.close();
			}

		}
	    
		if (isDebug() && !p_bTemporaryFile) {
			GeneratorUtils.writeXmlDebugFile(p_oSource, p_oOutputFile.getFile().getPath(), p_oProject, false, p_bResolve);
		}
	}


	/**
	 * Apply xsl output properties to transformer
	 * @param p_oXslOutputProperties xsl output properties
	 * @param p_oTransformer transformers
	 * @param p_oProject projet
	 */
	protected void applyXslProperties( List<com.a2a.adjava.codeformatter.GeneratedFile.XslOutputProperty> p_listXslOutputProperties, Transformer p_oTransformer, XProject<D> p_oProject ) {
		p_oTransformer.setOutputProperty(OutputKeys.ENCODING, p_oProject.getDomain().getFileEncoding());
		p_oTransformer.setOutputProperty(OutputKeys.INDENT, "no");
		p_oTransformer.setOutputProperty("{http://xml.apache.org/xalan}line-separator","\n");
		if ( p_listXslOutputProperties != null ) {
			for( XslOutputProperty oProperty : p_listXslOutputProperties) {
				p_oTransformer.setOutputProperty(oProperty.getKey(), oProperty.getValue());
			}
		}
	}
}
