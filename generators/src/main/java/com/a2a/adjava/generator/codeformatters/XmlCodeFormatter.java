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
package com.a2a.adjava.generator.codeformatters;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.FileUtils;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.a2a.adjava.codeformatter.CodeFormatter;
import com.a2a.adjava.codeformatter.GeneratedFile;
import com.a2a.adjava.messages.MessageHandler;
import com.a2a.adjava.utils.FileTypeUtils;

/**
 * Xml code formatter
 * @author lmichenaud
 *
 */
public class XmlCodeFormatter implements CodeFormatter {

	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory
			.getLogger(XmlCodeFormatter.class);

	/**
	 * Default line width
	 */
	private static final int DEFAULT_LINE_WIDTH = 90 ;
	
	/**
	 * Default indent size
	 */
	private static final int DEFAULT_INDENT_SIZE = 4 ;
	
	/**
	 * Line max width
	 */
	private int lineWidth = DEFAULT_LINE_WIDTH;

	/**
	 * Indent size
	 */
	private int indentSize = DEFAULT_INDENT_SIZE;
	
	/**
	 * {@inheritDoc}
	 */
	public void format(List<GeneratedFile> p_listGeneratedFiles, String p_sFileEncoding)
			throws Exception {
		log.debug("Xml Source code formatter.");

		if (p_listGeneratedFiles != null && !p_listGeneratedFiles.isEmpty()) {

			for (GeneratedFile oGenFile : p_listGeneratedFiles) {
				if ( this.acceptFile(oGenFile.getFileFromRoot())) {
					if ( oGenFile.getFormatOptions() != null && ((XmlFormatOptions) oGenFile.getFormatOptions()).isUseDom4j()) {
						this.indentWithDom4j(oGenFile);
					}
					else {
						this.indentXml(oGenFile);
					}
				}
			}

		} else {
			log.debug("no xml to format");
		}
	}
	
	/**
	 * Test if file must be formatted
	 * @param p_oFile file
	 * @return true if file must be formatted
	 */
	public boolean acceptFile( File p_oFile ) {
		return FileTypeUtils.isXmlFile(p_oFile.getName());
	}

	/**
	 * Indent xml file.
	 * Be carefull: attribute order is lost with this method.
	 * 
	 * @param p_oGenFile
	 *            xml file to indent
	 * @throws Exception exception
	 */
	protected void indentXml( GeneratedFile<XmlFormatOptions> p_oGenFile)
			throws Exception {
		log.debug("indent xml: {}", p_oGenFile.getFileFromRoot());
		
		OutputFormat oOutputFormat = this.computeOutputFormat(p_oGenFile);
		
		Reader oReader = new InputStreamReader(new FileInputStream(p_oGenFile.getFileFromRoot()), "UTF-8");
		try {
			DocumentBuilderFactory oDbf = DocumentBuilderFactory.newInstance();
			oDbf.setValidating(false);
			DocumentBuilder oDb = oDbf.newDocumentBuilder();
			InputSource oInputSource = new InputSource(oReader);
			Document xDoc = oDb.parse(oInputSource);
			Writer oWriter = new StringWriter();
			try {
				XMLSerializer oSerializer = new XMLSerializer(oWriter, oOutputFormat);
				oSerializer.serialize(xDoc);
				FileUtils.writeStringToFile(p_oGenFile.getFileFromRoot(), oWriter.toString(), "utf8");
			} finally {
				oWriter.close();
			}

		} catch (Exception oCause) {
			MessageHandler.getInstance().addError("Failed to indent {}",
					p_oGenFile.getFile().getAbsolutePath());
		} finally {
			oReader.close();
		}
	}
	
	
	/**
	 * Compute output format for file
	 * @param p_oGenFile generated file
	 * @return OutputFormat
	 */
	public OutputFormat computeOutputFormat( GeneratedFile<XmlFormatOptions> p_oGenFile ) {
		
		OutputFormat r_oXmlOutputFormat = new OutputFormat();
		r_oXmlOutputFormat.setLineWidth(this.lineWidth);
		r_oXmlOutputFormat.setIndent(this.indentSize);
		
		if ( p_oGenFile.getFormatOptions() != null ) { 
			r_oXmlOutputFormat.setOmitXMLDeclaration(p_oGenFile.getFormatOptions().isOmitXmlDeclaration());
		}
		
		return r_oXmlOutputFormat;
	}
	
	/**
	 * Compute output format for file
	 * @param p_oGenFile generated file
	 * @return OutputFormat
	 */
	public org.dom4j.io.OutputFormat computeDom4jOutputFormat( GeneratedFile<XmlFormatOptions> p_oGenFile ) {

		org.dom4j.io.OutputFormat r_oDom4jOutputFormat = org.dom4j.io.OutputFormat.createPrettyPrint();
		r_oDom4jOutputFormat.setIndentSize(this.indentSize);
		
		if ( p_oGenFile.getFormatOptions() != null ) { 
			r_oDom4jOutputFormat.setNewLineAfterDeclaration(p_oGenFile.getFormatOptions().isNewLineAfterDeclaration());
		}
		
		return r_oDom4jOutputFormat;
	}
	
	/**
	 * Indent using dom4j.
	 * Max line length not used by dom4j indenter.
	 * @param p_oGenFile file to indent
	 * @throws Exception failure
	 */
	protected void indentWithDom4j(GeneratedFile<XmlFormatOptions> p_oGenFile)
			throws Exception {
	
		org.dom4j.io.OutputFormat oOutputFormat = this.computeDom4jOutputFormat(p_oGenFile);
		
		SAXReader oSaxReader = new SAXReader();
		org.dom4j.Document xDoc = oSaxReader.read(p_oGenFile.getFileFromRoot());
		
		Writer oStringWriter = new StringWriter();
		try {
			XmlFormatOptions oOptions = (XmlFormatOptions) p_oGenFile.getFormatOptions();
			final org.dom4j.io.XMLWriter oWriter = new StandaloneXmlWriter(oStringWriter, oOutputFormat, oOptions.isStandalone());
			try {
				oWriter.write(xDoc);
				FileUtils.writeStringToFile(p_oGenFile.getFileFromRoot(), oStringWriter.toString());
			}
			finally {
				oWriter.close();
			}
		} finally {
			oStringWriter.close();
		}	
	}

	/**
	 * Return line width
	 * @return line width
	 */
	public int getLineWidth() {
		return lineWidth;
	}

	/**
	 * Set line width
	 * @param p_iLineWidth max line width
	 */
	public void setLineWidth(int p_iLineWidth) {
		this.lineWidth = p_iLineWidth;
	}

	/**
	 * Return indent size
	 * @return indent size
	 */
	public int getIndentSize() {
		return indentSize;
	}

	/**
	 * Set indent size
	 * @param p_iIndentSize indent size
	 */
	public void setIndentSize(int p_iIndentSize) {
		this.indentSize = p_iIndentSize;
	}
}
