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
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.Charsets;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.io.DocumentSource;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.a2a.adjava.utils.FileTypeUtils;
import com.a2a.adjava.xmodele.XProject;

/**
 * 
 * <p>Adjava Generator Utils class</p>
 *
 * <p>Copyright (c) 2010</p>
 * <p>Company: Adeuza</p>
 *
 * @author mmadigand
 *
 */
public class GeneratorUtils {

	/**
	 * Folder for debug files
	 */
	private static final String DEBUG_DIR = "/debug/";

	
	/**
	 * @param p_xDoc
	 * @param p_sFileName
	 * @throws Exception
	 */
	public static void writeXmlDebugFile(org.dom4j.Document p_xDoc, String p_sFileName, XProject<?> p_oProject) throws Exception {
		writeXmlDebugFile(p_xDoc, p_sFileName, p_oProject, false);
	}
	
	/**
	 * @param p_oSource
	 * @param p_sFileName
	 * @throws Exception
	 */
	public static void writeXmlDebugFile(Source p_oSource, String p_sFileName, XProject<?> p_oProject) throws Exception {
		writeXmlDebugFile(p_oSource, p_sFileName, p_oProject, false);
	}
	
	/**
	 * @param p_xDoc
	 * @param p_sFileName
	 * @throws Exception
	 */
	public static void writeXmlDebugFile(org.dom4j.Document p_xDoc, String p_sFileName, XProject<?> p_oProject, boolean p_bAppend) throws Exception {
		writeXmlDebugFile(new DocumentSource(p_xDoc), p_sFileName, p_oProject, p_bAppend);
	}
	
	/**
	 * Write 
	 * @param p_oDocument dom document
	 * @param p_sFileName file name
	 * @param p_oProject project
	 * @param p_bAppend append to existing file
	 * @throws Exception
	 */
	public static void writeXmlDebugFile( Source p_oSource, String p_sFileName, XProject<?> p_oProject, boolean p_bAppend ) throws Exception {
		writeXmlDebugFile(p_oSource, p_sFileName, p_oProject, p_bAppend, true);
	}
	
	/**
	 * Write 
	 * @param p_oDocument dom document
	 * @param p_sFileName file name
	 * @param p_oProject project
	 * @param p_bAppend append to existing file
	 * @param p_bResolve true if the path should be resolved from project
	 * @throws Exception
	 */
	public static void writeXmlDebugFile( Source p_oSource, String p_sFileName, XProject<?> p_oProject, boolean p_bAppend, boolean p_bResolve ) throws Exception {
		
		File oFile = computeDebugFileName(p_sFileName, p_oProject, p_bResolve);
		
//		log.debug("[GeneratorUtils#writeXmlDebugFile] write debug file: "+oFile);
		
		writeXml(  p_oSource,  oFile,  p_oProject,  p_bAppend );		
	}
	
	/**
	 * @param p_xDoc
	 * @param p_sFileName
	 * @throws Exception
	 */
	public static void writeXml(org.dom4j.Document p_xDoc, File p_sFileName, XProject<?> p_oProject) throws Exception {
		writeXml(p_xDoc, p_sFileName, p_oProject, false);
	}
	
	/**
	 * @param p_oSource
	 * @param p_sFileName
	 * @throws Exception
	 */
	public static void writeXml(Source p_oSource, File p_sFileName, XProject<?> p_oProject) throws Exception {
		writeXml(p_oSource, p_sFileName, p_oProject, false);
	}
	
	
	public static void writeXml(org.w3c.dom.Document p_xDoc, File p_sFileName, XProject<?> p_oProject) throws Exception {
		writeXml(new DOMSource(p_xDoc), p_sFileName, p_oProject, false);
	}

	public static void writeXml(org.w3c.dom.Document p_xDoc, File p_sFileName, XProject<?> p_oProject, boolean p_bAppend) throws Exception {
		writeXml(new DOMSource(p_xDoc), p_sFileName, p_oProject, p_bAppend);
	}

	
	/**
	 * @param p_xDoc
	 * @param p_sFileName
	 * @throws Exception
	 */
	public static void writeXml(org.dom4j.Document p_xDoc, File p_sFileName, XProject<?> p_oProject, boolean p_bAppend) throws Exception {
		writeXml(new DocumentSource(p_xDoc), p_sFileName, p_oProject, p_bAppend);
	}
	
	public static void writeXml( Source p_oSource, File oFile, XProject<?> p_oProject, boolean p_bAppend ) throws Exception {
		
		Files.createDirectories(oFile.getParentFile().toPath());
		
	    Transformer oTransformer = TransformerFactory.newInstance().newTransformer();
	    oTransformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
	    oTransformer.setOutputProperty(OutputKeys.METHOD, FileTypeUtils.XML);
	    oTransformer.setOutputProperty(OutputKeys.ENCODING, Charsets.UTF_8.name());
	    oTransformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
	    oTransformer.setOutputProperty(OutputKeys.INDENT, "yes");
	    
	    
	    OutputStreamWriter oOutputStreamWriter = null;
		try {
			oOutputStreamWriter = new OutputStreamWriter(new FileOutputStream(oFile),p_oProject.getDomain().getFileEncoding());
			StreamResult oStreamResult = new StreamResult(oOutputStreamWriter);
			oTransformer.transform(p_oSource, oStreamResult);
		}  finally {
			if(oOutputStreamWriter != null) {
				oOutputStreamWriter.close();
			}
		}
	}
	
	public static void clearEmptyNodes(org.w3c.dom.Document doc) throws XPathExpressionException{
		  XPathFactory xpathFactory = XPathFactory.newInstance();
		  // XPath to find empty text nodes.
		  XPathExpression xpathExp = xpathFactory.newXPath().compile("//text()[normalize-space(.) = '']");  
		  NodeList emptyTextNodes = (NodeList) xpathExp.evaluate(doc, XPathConstants.NODESET);

		  // Remove each empty text node from document.
		  for (int i = 0; i < emptyTextNodes.getLength(); i++) {
		      Node emptyTextNode = emptyTextNodes.item(i);
		      emptyTextNode.getParentNode().removeChild(emptyTextNode);
		  }
	}
	
//	private static File computeDebugFileName( String p_sFileName, XProject<?> p_oProject ) {
//		return computeDebugFileName(p_sFileName, p_oProject, true);
//	}
	
	/**
	 * @param p_sFileName
	 * @param p_oProject
	 * @param p_bResolve
	 * @return
	 */
	private static File computeDebugFileName( String p_sFileName, XProject<?> p_oProject, boolean p_bResolve ) {
		
		String sFileName = new File(p_sFileName).getName();
		
		if ( !FileTypeUtils.isXmlFile(sFileName)) {
			sFileName = StringUtils.join( sFileName, FileTypeUtils.EXTENSION_SEPARATOR, FileTypeUtils.XML);
		}

		File r_oFile = Paths.get(p_sFileName).toFile();
		if (p_bResolve) {
			r_oFile = (Paths.get(p_oProject.getBaseDir()).resolve(Paths.get(p_sFileName))).toFile();
		}
		
//		log.debug("[GeneratorUtils#computeDebugFile] will write the debug file in "+r_oFile.getAbsolutePath());
		File oDebugDir = new File(r_oFile.getParentFile(), DEBUG_DIR);
		
		return new File(oDebugDir, sFileName);
	}
	

}
