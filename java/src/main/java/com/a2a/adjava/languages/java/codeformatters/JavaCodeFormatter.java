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
package com.a2a.adjava.languages.java.codeformatters;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.TextEdit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.codeformatter.CodeFormatter;
import com.a2a.adjava.codeformatter.GeneratedFile;
import com.a2a.adjava.generator.codeformatters.XmlCodeFormatter;
import com.a2a.adjava.utils.FileTypeUtils;

/**
 * Java code formatter
 * @author lmichenaud
 *
 */
public class JavaCodeFormatter implements CodeFormatter {

	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory
			.getLogger(XmlCodeFormatter.class);
		
	/**
	 * {@inheritDoc}
	 */
	public void format(List<GeneratedFile> p_listGeneratedFiles, String p_sFileEncoding)
			throws Exception {
		log.debug(" Source code formatter.");

		if (p_listGeneratedFiles != null && !p_listGeneratedFiles.isEmpty()) {

			List<File> listJavaFiles = new ArrayList<File>();
			for (GeneratedFile oGenFile : p_listGeneratedFiles) {
				if (FileTypeUtils.isJavaFile(oGenFile.getFile().getName())) {
					listJavaFiles.add(oGenFile.getFileFromRoot());
				}
			}

			if (!listJavaFiles.isEmpty()) {
				this.run("eclipse-indent.xml", listJavaFiles,
						p_sFileEncoding);
			}
		} else {
			log.debug("no code to format");
		}
	}

	/**
	 * Runs the Java code formatter application
	 * @param p_sConfigName config name
	 * @param p_listFileToFormat list of files to format
	 * @param p_sFileEncoding file encoding
	 * @throws Exception failure
	 */
	public void run(String p_sConfigName, List<File> p_listFileToFormat,
			String p_sFileEncoding) throws Exception {

		Properties oOptions = this.readConfig(p_sConfigName);

		// format the list of files and/or directories
		final org.eclipse.jdt.core.formatter.CodeFormatter oCodeFormatter = ToolFactory
				.createCodeFormatter(oOptions);
		for (File oFile : p_listFileToFormat) {
			if (oFile.isDirectory()) {
				this.formatDirTree(oFile, oCodeFormatter, p_sFileEncoding);
			} else if (FileTypeUtils.isJavaFile(oFile)) {
				this.formatFile(oFile, oCodeFormatter, p_sFileEncoding);
			}
		}

		log.info("Java formating done");
	}
	
	/**
	 * Recursively format the Java source code that is contained in the
	 * directory rooted at dir.
	 * 
	 * @param p_oDir
	 *            directory
	 * @param p_oCodeFormatter
	 *            code formatter
	 * @param p_sFileEncoding file encoding
	 */
	private void formatDirTree(File p_oDir, org.eclipse.jdt.core.formatter.CodeFormatter p_oCodeFormatter,
			String p_sFileEncoding) {

		File[] t_sFiles = p_oDir.listFiles();
		if (t_sFiles != null) {

			for (int iFileIndex = 0; iFileIndex < t_sFiles.length; iFileIndex++) {
				File oFile = t_sFiles[iFileIndex];
				if (oFile.isDirectory()) {
					this.formatDirTree(oFile, p_oCodeFormatter, p_sFileEncoding);
				} else if (FileTypeUtils.isJavaFile(oFile)) {
					this.formatFile(oFile, p_oCodeFormatter, p_sFileEncoding);
				}
			}
		}
	}

	/**
	 * Format the given Java source file.
	 * 
	 * @param p_oFile
	 *            file to format
	 * @param p_oCodeFormatter
	 *            code formatter
	 * @param p_sFileEncoding
	 *            file encoding
	 */
	private void formatFile(File p_oFile, org.eclipse.jdt.core.formatter.CodeFormatter p_oCodeFormatter,
			String p_sFileEncoding) {

		try {
			String sContents = FileUtils.readFileToString(p_oFile,
					p_sFileEncoding);
			IDocument oDoc = new org.eclipse.jface.text.Document();
			oDoc.set(sContents);
			TextEdit oEdit = p_oCodeFormatter.format(
					org.eclipse.jdt.core.formatter.CodeFormatter.K_COMPILATION_UNIT, sContents, 0,
					sContents.length(), 0, "\n");
			if (oEdit != null) {
				oEdit.apply(oDoc);
			} else {
				log.error("Formatting failed: {}",
						p_oFile.getAbsolutePath());
				return;
			}

			FileUtils.writeStringToFile(p_oFile, oDoc.get(),
					p_sFileEncoding);

		} catch (IOException oException) {
			log.error("Formatting failed (IOException): {}",
					p_oFile.getAbsolutePath());
		} catch (BadLocationException e) {
			log.error("Formatting failed (BadLocationException): {}",
					p_oFile.getAbsolutePath());
		}
	}

	/**
	 * Return a Java Properties file representing the options that are in the
	 * specified config file.
	 * 
	 * @param p_sFilename
	 *            config file
	 * @return Properties properties
	 * @throws IOException failure
	 */
	private Properties readConfig(String p_sFilename) throws IOException {
		InputStream oIs = this.getClass().getResourceAsStream(
				StringUtils.join("/", p_sFilename));
		Properties r_oFormatterOptions = new Properties();
		try {
			r_oFormatterOptions.load(oIs);
		} finally {
			oIs.close();
		}
		return r_oFormatterOptions;
	}
}
