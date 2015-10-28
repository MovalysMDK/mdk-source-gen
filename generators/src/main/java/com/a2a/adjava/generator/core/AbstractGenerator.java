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
import java.util.Map;

import com.a2a.adjava.AdjavaException;
import com.a2a.adjava.codeformatter.GeneratedFile;
import com.a2a.adjava.generators.DomainGeneratorContext;
import com.a2a.adjava.generators.ResourceGenerator;
import com.a2a.adjava.utils.FileTypeUtils;
import com.a2a.adjava.xmodele.IDomain;
import com.a2a.adjava.xmodele.IModelDictionary;
import com.a2a.adjava.xmodele.IModelFactory;

/**
 * <p>
 * Classe abstraire pour les generators
 * </p>
 * 
 * <p>
 * Copyright (c) 2009
 * <p>
 * Company: Adeuza
 * 
 * @author mmadigand
 * 
 * @param <D> type of Domain
 * 
 */
public abstract class AbstractGenerator<D extends IDomain<? extends IModelDictionary, ? extends IModelFactory>> implements ResourceGenerator<D> {

	/**
	 * Parametre de la session globale qui contient la liste des fichiers Java a indenter
	 */
	public static final String GENERATED_FILES = "generatedFiles";

	/**
	 * Parameters on generator 
	 */
	private Map<String, String> parametersMap = null;
	
	/**
	 * Debug enabled 
	 */
	private boolean debug = false;
	
	/**
	 * Retourne l'objet parametersMap
	 * 
	 * @return Objet parametersMap
	 */
	public Map<String, String> getParametersMap() {
		return this.parametersMap;
	}

	/**
	 * Affecte l'objet parametersMap
	 * 
	 * @param p_oParametersMap
	 *            Objet parametersMap
	 */
	public void setParametersMap(Map<String, String> p_oParametersMap) {
		this.parametersMap = p_oParametersMap;
	}
	
	/**
	 * Declare a generated file for post treatment (indent)
	 * @param p_oFile file to declare
	 * @param p_oGeneratorContext the context to use
	 */
	protected void addGeneratedFileToSession( GeneratedFile p_oFile, DomainGeneratorContext p_oGeneratorContext) {
		p_oGeneratorContext.addGeneratedFile( p_oFile );
	}

	/**
	 * Return code-generated start tag
	 * @param p_sKey key of the code generated bloc
	 * @param p_oFile file
	 * @return String r_sStartTag
	 */
	protected String getGeneratedStartTag( String p_sKey, File p_oFile ) {
		String r_sStartTag = null ;
		if ( FileTypeUtils.isJavaFile(p_oFile) || FileTypeUtils.isIosFile(p_oFile) || FileTypeUtils.isJsonFile(p_oFile)
				|| FileTypeUtils.isJavascriptFile(p_oFile) || FileTypeUtils.isIosStringFile(p_oFile)
				|| FileTypeUtils.isScssFile(p_oFile) || FileTypeUtils.isCSharpFile(p_oFile)) {
			r_sStartTag = "//@generated-start[" + p_sKey + "]";
		}
		else if ( FileTypeUtils.isXmlFile(p_oFile) || FileTypeUtils.isHtmlFile(p_oFile)) {
			r_sStartTag = "<!--//@generated-start[" + p_sKey + "]-->";
		}
		return r_sStartTag ;
	}
	
	/**
	 * Return code-generated start tag
	 * @param p_oFile file
	 * @return String r_sEndTag
	 */
	protected String getGeneratedEndTag( File p_oFile ) {
		String r_sEndTag = null ;
		if ( FileTypeUtils.isJavaFile(p_oFile) || FileTypeUtils.isIosFile(p_oFile) || FileTypeUtils.isJsonFile(p_oFile)
				|| FileTypeUtils.isJavascriptFile(p_oFile) || FileTypeUtils.isIosStringFile(p_oFile)
				|| FileTypeUtils.isScssFile(p_oFile) || FileTypeUtils.isCSharpFile(p_oFile)) {
			r_sEndTag = "//@generated-end";
		}
		else if ( FileTypeUtils.isXmlFile(p_oFile) || FileTypeUtils.isHtmlFile(p_oFile)) {
			r_sEndTag = "<!--//@generated-end-->";
		}
		return r_sEndTag ;
	}
	
	/**
	 * Methode d'initialisation, peut-etre surchargee {@inheritDoc}
	 * 
	 * @throws AdjavaException
	 */
	@Override
	public void initialize() throws AdjavaException {
		// nothing to do
	}

	/**
	 * Retourne l'objet debug
	 * @return Objet debug
	 */
	protected boolean isDebug() {
		return this.debug;
	}

	/**
	 * Affecte l'objet debug 
	 * @param p_bDebug Objet debug
	 */
	public void setDebug(boolean p_bDebug) {
		this.debug = p_bDebug;
	}
}
