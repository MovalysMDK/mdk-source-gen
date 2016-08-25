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
package com.a2a.adjava.languages.ionic2.codeformatters;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.environment.EnvironmentUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.codeformatter.CodeFormatter;
import com.a2a.adjava.codeformatter.GeneratedFile;

public abstract class AbstractCodeFormatter implements CodeFormatter {

    /**
     * Logger
     */
    private static final Logger log = LoggerFactory
            .getLogger(AbstractJSBeautifyCodeFormatter.class);

    /**
     * Time out of formatting process
     */
    private static final int WATCHDOG_TIMEOUT = 60000;

    private abstract String getFormatter();

    protected abstract String[] getArguments();

    public abstract boolean acceptFile( File p_oFile );

    /**
     * {@inheritDoc}
     *
     * @see com.a2a.adjava.codeformatter.CodeFormatter#format(java.util.List,
     *      java.lang.String)
     */
    @Override
    public void format(List<GeneratedFile> p_listGeneratedFiles, String p_sFileEncoding)
            throws Exception {

        log.debug("Formatter: {}", this.getClass().getSimpleName());

        if (p_listGeneratedFiles != null && !p_listGeneratedFiles.isEmpty()) {

            List<GeneratedFile> listAcceptedGenFiles = new ArrayList<>();
            for (GeneratedFile oGenFile : p_listGeneratedFiles) {
                if ( this.acceptFile(oGenFile.getFile())) {
                    listAcceptedGenFiles.add(oGenFile);
                }
            }
            try {
                this.indentFiles(listAcceptedGenFiles);
            } catch( Exception oIOException ) {
                log.error("  Failed to indent files", oIOException);
            }
        } else {
            log.debug("  no code to format");
        }
    }

    /**
     * Indent files
     * @param p_listGenFiles files to indent
     * @throws Exception indentation failure
     */
    private void indentFiles( List<GeneratedFile> p_listGenFiles ) throws Exception {

        if ( !p_listGenFiles.isEmpty()) {
        	int fileListSize = p_listGenFiles.size();
        	final int SUBLIST_SIZE = 40; 
        	
			for (int factor = 0 ; factor*SUBLIST_SIZE < fileListSize ; factor++) {
        		int endIndex = Math.min(fileListSize, factor * SUBLIST_SIZE + SUBLIST_SIZE);
        		List<GeneratedFile> sublist = p_listGenFiles.subList(factor * SUBLIST_SIZE, endIndex);
	            CommandLine oCmdLine = null;
	            
	            if ( System.getProperty("os.name").startsWith("Windows")) {
	            	oCmdLine = new CommandLine("cmd");
	            	oCmdLine.addArgument("/c");
	            	oCmdLine.addArgument(this.getFormatter());
	            }
	            else {
	            	oCmdLine = new CommandLine(this.getFormatter());
	            }

	            for( String sCmdArgument: this.getArguments()) {
	                oCmdLine.addArgument(sCmdArgument);
	            }

	            for( GeneratedFile oGenFile: sublist ) {
	                oCmdLine.addArgument(oGenFile.getFileFromRoot().getPath());
	            }
                
	            DefaultExecuteResultHandler oResultHandler = new DefaultExecuteResultHandler();
	
	            ExecuteWatchdog oWatchdog = new ExecuteWatchdog(WATCHDOG_TIMEOUT);
	            DefaultExecutor oExecutor = new DefaultExecutor();
	            oExecutor.setExitValue(0);
	            oExecutor.setWatchdog(oWatchdog);
	            oExecutor.execute(oCmdLine, EnvironmentUtils.getProcEnvironment(), oResultHandler);
	
	            oResultHandler.waitFor();
        	}
        }
    }
}
