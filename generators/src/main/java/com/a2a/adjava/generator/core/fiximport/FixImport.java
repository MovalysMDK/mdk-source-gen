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
package com.a2a.adjava.generator.core.fiximport;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.ListUtils;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.codeformatter.GeneratedFile;
import com.a2a.adjava.generators.DomainGeneratorContext;
import com.a2a.adjava.messages.MessageHandler;

/**
 * @author lmichenaud
 *
 */
public class FixImport {

	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(FixImport.class);
	
	/**
	 * Type Visitor
	 */
	private TypeVisitor typeVisitor = new TypeVisitor();
	
	/**
	 * 
	 */
	private List<String> oldGeneratedTypes = new ArrayList<String>();
	
	/**
	 * 
	 */
	private List<String> newGeneratedTypes = new ArrayList<String>();
	
	/**
	 * 
	 */
	private Map<String,List<String>> imports = new HashMap<String,List<String>>();
	
	/**
	 * @throws Exception 
	 * 
	 */
	public void extractOldTypes( DomainGeneratorContext p_oContext ) throws Exception {
		log.debug("Extract Old types");
		long lStart = System.currentTimeMillis();
		for( GeneratedFile oGenFile : p_oContext.getOldGeneratedFiles()) {
			if ( oGenFile.getFile().getName().endsWith(".java")) {
				this.extratTypesAndImports(oGenFile.getFileFromRoot().getPath(), this.oldGeneratedTypes, null );
			}
		}
		long lEnd = System.currentTimeMillis();
		log.debug("extractOldTypes: {} ms, files : {}", (lEnd - lStart), p_oContext.getOldGeneratedFiles().size());
		log.debug("nb types: {}", oldGeneratedTypes.size());
	}
	
	/**
	 * @param p_oContext
	 * @throws Exception 
	 */
	public void fixImports( DomainGeneratorContext p_oContext ) throws Exception {

		log.debug("Extract new types");
		
		long lStart = System.currentTimeMillis();
		// Extract all types and import
		for( String sFileName : p_oContext.getGeneratedFilenames()) {
			if ( sFileName.endsWith(".java")) {
				this.extratTypesAndImports(sFileName, this.newGeneratedTypes, this.imports );
			}
		}
		long lEnd = System.currentTimeMillis();
		log.debug("extractNewTypes: {} ms, files : {}", (lEnd - lStart), p_oContext.getGeneratedFilenames().size());
		log.debug("nb types: {}", this.newGeneratedTypes.size());
		
		// No more used types
		List<String> listNoMoreUsedTypes = ListUtils.subtract(this.oldGeneratedTypes, this.newGeneratedTypes);
		log.debug("listNoMoreUsedTypes: {}", listNoMoreUsedTypes.size());
		
		lStart = System.currentTimeMillis();
		if ( !listNoMoreUsedTypes.isEmpty()) {
			for( Entry<String,List<String>> oKey : this.imports.entrySet()) {
				this.checkImports( oKey.getKey(), oKey.getValue(), listNoMoreUsedTypes);
			}
		}
		lEnd = System.currentTimeMillis();
		log.debug("checkImports: {} ms", (lEnd - lStart));
	}

	/**
	 * @param p_sKey
	 * @param p_listImports
	 */
	private void checkImports(String p_sFile, List<String> p_listImports, List<String> p_oDeletedTypes ) throws Exception {

		List<String> validImports = ListUtils.subtract(p_listImports, p_oDeletedTypes);
		
		if ( p_sFile.endsWith("ContactDetailScreen.java")) {
			log.debug("check imports: {}", p_sFile );
			
			// If file contains any of the delete import, rebuild the file
			log.debug("  original imports: {}", p_listImports.size());
			log.debug("  valid imports: {}", validImports.size());
			if ( !p_listImports.isEmpty()) {
				log.debug("  import sample: {}", p_listImports.get(0));	
			}
			if ( !p_oDeletedTypes.isEmpty()) {
				log.debug("  delete type sample: {}", p_oDeletedTypes.get(0));	
			}
		}
		
		// if validImports has not the same size than current imports, it means current imports contain invalid imports.
		if ( validImports.size() != p_listImports.size()) {
		
			boolean bImportReached = false ;
			int iPos = 0 ;
			int iImportPos = 0 ;
			List<String> newContent = new ArrayList<String>();
			List<String> transaction = new ArrayList<String>();
			
			for( String sLine : FileUtils.readLines(new File(p_sFile))) {
				String sTrimLine = sLine.trim();
				if ( sTrimLine.startsWith("import ")) {
					if ( !bImportReached) {
						iImportPos = iPos ;
						newContent.addAll(transaction);
						transaction.clear();
					}
					else {
						transaction.clear();
					}
					bImportReached = true ;
				}
				else {
					transaction.add(sLine);
				}
				iPos++;
			}
			newContent.addAll(transaction);
			
			List<String> importDecls = new ArrayList<String>();
			for( String sValidImport : validImports ) {
				importDecls.add("import " + sValidImport + ";");
			}
			
			newContent.addAll(iImportPos, importDecls);
			
			// Write new file
			FileUtils.writeLines(new File(p_sFile), newContent);
		}
	}
	
	/**
	 * @param p_sFile
	 * @return
	 */
	private void extratTypesAndImports( String p_sFile, List<String> p_listTypes, Map<String,List<String>> p_mapImports ) throws Exception {
		FileInputStream oIs = null;
		try {
			oIs = new FileInputStream(p_sFile);
			
			TypeVisitorResult oResult = new TypeVisitorResult();
			CompilationUnit oCompilUnit = JavaParser.parse(oIs);
			this.typeVisitor.visit(oCompilUnit, oResult );
			p_listTypes.addAll(oResult.getTypes());
			if ( p_mapImports != null ) {
				p_mapImports.put(p_sFile, oResult.getImports());
			}
		} catch( ParseException oParseException ) {
			MessageHandler.getInstance().addWarning("FixImport: failed to parse " + p_sFile );
		} catch( FileNotFoundException oFileException ) {
			MessageHandler.getInstance().addWarning("FixImport: failed to load the " + p_sFile + " file" );
		} finally {
			if (oIs != null) {
				oIs.close();
			}
		}
	}
}
