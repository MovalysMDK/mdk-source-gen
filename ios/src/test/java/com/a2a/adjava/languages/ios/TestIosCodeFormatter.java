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
package com.a2a.adjava.languages.ios;

import java.io.File;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.Executor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


/**
 * Test Unit for ios code formatter
 * @author lmichenaud
 *
 */
@RunWith(JUnit4.class)
public class TestIosCodeFormatter {
	
	/**
	 * Preparation des tests de la classe
	 */
	@Before
	public void setUp() {
		
	}
	
	/**
	 * Test l'indentation
	 * @throws Exception
	 */
	@Test
	public void testIndent() throws Exception {
		this.indentFile("src/test/resources/com/a2a/adjava/languages/ios/AppDelegate.m");
	}
	
	/**
	 * Indent un fichier
	 * @param p_sFile chemin classpath du fichier 
	 * @throws Exception echec de l'indentation
	 */
	private void indentFile( String p_sFile ) throws Exception {
		
		File oExecFile = new File("/usr/bin/uncrustify");
		File oConfigFile = new File("src/main/resources/uncrustify.cfg");
		File oTestFile = new File(p_sFile);
		
		CommandLine oCmdLine = new CommandLine(oExecFile);
		oCmdLine.addArgument("-c");
		oCmdLine.addArgument(oConfigFile.getAbsolutePath());
		oCmdLine.addArgument("-l");
		oCmdLine.addArgument("OC");
		oCmdLine.addArgument("-f");
		oCmdLine.addArgument(oTestFile.getAbsolutePath());
		oCmdLine.addArgument("-o");
		oCmdLine.addArgument(oTestFile.getAbsolutePath());

		DefaultExecuteResultHandler oResultHandler = new DefaultExecuteResultHandler();

		ExecuteWatchdog oWatchdog = new ExecuteWatchdog(60*1000);
		Executor oExecutor = new DefaultExecutor();
		oExecutor.setExitValue(1);
		oExecutor.setWatchdog(oWatchdog);
		oExecutor.execute(oCmdLine, oResultHandler);

		oResultHandler.waitFor();
	}
}
