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
package com.a2a.adjava.generator.core.incremental;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import javax.xml.transform.dom.DOMSource;

import org.dom4j.Document;
import org.dom4j.Element;

import com.a2a.adjava.AdjavaProperty;
import com.a2a.adjava.codeformatter.GeneratedFile;
import com.a2a.adjava.generator.core.AbstractXslGenerator;
import com.a2a.adjava.generators.DomainGeneratorContext;
import com.a2a.adjava.xmodele.IDomain;
import com.a2a.adjava.xmodele.XProject;

/**
 * Incremental generator
 * @author lmichenaud
 *
 * @param <D> domain
 */
public abstract class AbstractIncrementalGenerator<D extends IDomain<?, ?>> extends AbstractXslGenerator<D> {

	/**
	 * Non-generated bloc extractor
	 */
	private final NonGeneratedBlocExtractor nonGeneratedBlocExtractor = new NonGeneratedBlocExtractor();

	/**
	 * Import extractor
	 */
	private final IncludesExtractor importExtractor = new IncludesExtractor();
	
	/**
	 * Incremental xsl transformation
	 * 
	 * @param p_sTemplatePath template path
	 * @param p_sOutputFile output file
	 * @param p_xDoc xml document
	 * @param p_oProject project
	 * @param p_oGeneratorContext context generator
	 * @throws Exception exception
	 */
	public void doIncrementalTransform(String p_sTemplatePath, String p_sOutputFile, Document p_xDoc, XProject<D> p_oProject,
			DomainGeneratorContext p_oGeneratorContext) throws Exception {
		this.doIncrementalTransform(p_sTemplatePath, new GeneratedFile<>(p_sOutputFile), p_xDoc, p_oProject, p_oGeneratorContext);
	}
	
	/**
	 * Incremental xsl transformation
	 * 
	 * @param p_sTemplatePath template path
	 * @param p_oOutputFile output file
	 * @param p_xDoc xml document
	 * @param p_oProject project
	 * @param p_oGeneratorContext context generator
	 * @throws Exception exception
	 */
	public void doIncrementalTransform(String p_sTemplatePath, GeneratedFile p_oOutputFile, Document p_xDoc, XProject<D> p_oProject,
			DomainGeneratorContext p_oGeneratorContext) throws Exception {
		
		Map<String, NonGeneratedBloc> mapNonGeneratedBlocs = this.nonGeneratedBlocExtractor.extractBlocs(p_oOutputFile.getFile().getPath(), p_oProject);
		p_xDoc.getRootElement().add(this.nonGeneratedBlocExtractor.toXml(mapNonGeneratedBlocs));

		Element xImports = p_xDoc.getRootElement();
		for( String sImport: extractImports( p_oOutputFile.getFile().getPath(), p_oProject)) {
			xImports.addElement("import").addText(sImport);
		}

		this.doTransformToFile(p_xDoc, p_sTemplatePath, p_oOutputFile, p_oProject, p_oGeneratorContext);
	}
	
	/**
	 * Incremental xsl transformation
	 * @param p_sTemplatePath template path
	 * @param p_sOutputFile output file
	 * @param p_xDoc document
	 * @param p_oProject project
	 * @param p_oGeneratorContext generator context
	 * @throws Exception exception
	 */
	public void doIncrementalTransform(String p_sTemplatePath, String p_sOutputFile, org.w3c.dom.Document p_xDoc, XProject<D> p_oProject,
			DomainGeneratorContext p_oGeneratorContext) throws Exception {
		doIncrementalTransform( p_sTemplatePath, new GeneratedFile(p_sOutputFile), p_xDoc, p_oProject, p_oGeneratorContext);
	}
	
	/**
	 * Incremental xsl transformation
	 * @param p_sTemplatePath template path
	 * @param p_oOutputFile output file
	 * @param p_xDoc document
	 * @param p_oProject project
	 * @param p_oGeneratorContext generator context
	 * @throws Exception exception
	 */
	public void doIncrementalTransform(String p_sTemplatePath, GeneratedFile p_oOutputFile, org.w3c.dom.Document p_xDoc, XProject<D> p_oProject,
			DomainGeneratorContext p_oGeneratorContext) throws Exception {
		
		Map<String, NonGeneratedBloc> mapNonGeneratedBlocs = this.nonGeneratedBlocExtractor.extractBlocs(p_oOutputFile.getFile().getPath(), p_oProject);
		p_xDoc.getDocumentElement().appendChild(this.nonGeneratedBlocExtractor.toXml(mapNonGeneratedBlocs, p_xDoc));
		
		for( String sImport: extractImports( p_oOutputFile.getFile().getPath(), p_oProject)) {
			org.w3c.dom.Element xImport = p_xDoc.createElement("import");
			xImport.setTextContent(sImport);
			p_xDoc.appendChild(xImport);
		}
		
		this.doTransformToFile( new DOMSource(p_xDoc), p_sTemplatePath, p_oOutputFile, p_oProject, p_oGeneratorContext,false);
	}
	
	
	/**
	 * Extract imports
	 * @param p_sInputFile analyzed file
	 * @param p_oProject project
	 * @return import list
	 * @throws Exception exception
	 */
	protected Collection<String> extractImports( String p_sInputFile, XProject<D> p_oProject ) throws Exception {
		Collection<String> r_listImports;
		String sForceOverwrite = p_oProject.getDomain().getGlobalParameters().get(AdjavaProperty.IMPORT_FORCE_OVERWRITE.getName());
		if (sForceOverwrite == null || !sForceOverwrite.equalsIgnoreCase("true")) {
			r_listImports = this.importExtractor.extractIncludes(p_sInputFile, p_oProject);
		}
		else {
			r_listImports = new ArrayList<>();
		}
		return r_listImports ;
	}
}
