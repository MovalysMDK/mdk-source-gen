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
package com.a2a.adjava.languages.uwp.codeformatters;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.Executor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.codeformatter.CodeFormatter;
import com.a2a.adjava.codeformatter.GeneratedFile;
import com.a2a.adjava.utils.FileTypeUtils;

/**
 * Class used to format csharp code
 */
public class CSharpCodeFormatter implements CodeFormatter  {

	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory
			.getLogger(CSharpCodeFormatter.class);
	
	/**
	 * Time out of formatting process
	 */
	private static final int WATCHDOG_TIMEOUT = 60000;
	
	/**
	 * astyle binary
	 */
	private String astyle = "AStyle";
	
	/**
	 * Config file
	 */
	private String configFile = "conf/astyle.cfg";
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void format(List<GeneratedFile> p_listGeneratedFiles, String p_sFileEncoding)
			throws Exception {

		log.debug("UWP Source code formatter.");

		if (p_listGeneratedFiles != null && !p_listGeneratedFiles.isEmpty()) {

			List<GeneratedFile> listAcceptedGenFiles = new ArrayList<GeneratedFile>();
			for (GeneratedFile oGenFile : p_listGeneratedFiles) {
				if ( this.acceptFile(oGenFile.getFile())) {
					listAcceptedGenFiles.add(oGenFile);
				}
			}
			try {
				this.indentFiles(listAcceptedGenFiles);
			} catch( Exception oIOException ) {
				log.error("Failed to indent uwp files");
			}
		} else {
			log.debug("UWP: no code to format");
		}
	}
	
	/**
	 * Test if file must be formatted
	 * @param p_oFile file
	 * @return true if file must be formatted
	 */
	public boolean acceptFile( File p_oFile ) {
		return FileTypeUtils.isCSharpFile(p_oFile.getName());
	}
	
	/**
	 * Indent uwp file
	 * @param p_listGenFiles list of files to indent
	 * @throws Exception indentation failure
	 */
	private void indentFiles( List<GeneratedFile> p_listGenFiles ) throws Exception {
		
		if ( !p_listGenFiles.isEmpty()) {
		
			CommandLine oCmdLine = new CommandLine(this.astyle);
			oCmdLine.addArgument("-c");
			oCmdLine.addArgument("--options="+this.configFile);
			for( GeneratedFile oGenFile: p_listGenFiles ) {
				oCmdLine.addArgument(oGenFile.getFileFromRoot().getPath());
			}
			
			DefaultExecuteResultHandler oResultHandler = new DefaultExecuteResultHandler();
	
			ExecuteWatchdog oWatchdog = new ExecuteWatchdog(WATCHDOG_TIMEOUT);
			Executor oExecutor = new DefaultExecutor();
			oExecutor.setExitValue(1);
			oExecutor.setWatchdog(oWatchdog);
			oExecutor.execute(oCmdLine, oResultHandler);
	
			oResultHandler.waitFor();
		}
	}

	/**
	 * Return astyle executable
	 * @return astyle executable
	 */
	public String getAstyle() {
		return astyle;
	}

	/**
	 * Set astyle executable
	 * @param p_sAstyle astyle executable
	 */
	public void setAstyle(String p_sAstyle) {
		this.astyle = p_sAstyle;
	}

	/**
	 * Return astyle config file
	 * @return astyle config file
	 */
	public String getConfigFile() {
		return configFile;
	}

	/**
	 * Set astyle config file
	 * @param p_sConfigFile astyle config file
	 */
	public void setConfigFile(String p_sConfigFile) {
		this.configFile = p_sConfigFile;
	}

}
