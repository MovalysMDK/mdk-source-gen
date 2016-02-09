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
package com.a2a.adjava.languages.ios.generators;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.generator.core.injection.AbstractInjectionGenerator;
import com.a2a.adjava.generator.core.injection.FilePartGenerationConfig;
import com.a2a.adjava.generators.DomainGeneratorContext;
import com.a2a.adjava.languages.ios.extractors.UniqueMethodDaoExtractor;
import com.a2a.adjava.utils.Chrono;
import com.a2a.adjava.utils.FileTypeUtils;
import com.a2a.adjava.xmodele.IDomain;
import com.a2a.adjava.xmodele.IModelDictionary;
import com.a2a.adjava.xmodele.IModelFactory;
import com.a2a.adjava.xmodele.MAttribute;
import com.a2a.adjava.xmodele.MEntityImpl;
import com.a2a.adjava.xmodele.XProject;

/**
 * Generates the core data error validation files
 * @author lmichenaud
 *
 */
public class ErrorConfigGenerator extends AbstractInjectionGenerator<IDomain<IModelDictionary,IModelFactory>>{

	/**
	 * Logger for this class
	 */
	private static final Logger log = LoggerFactory.getLogger(ErrorConfigGenerator.class);
	
	/**
	 * 
	 * @see com.a2a.adjava.generators.ResourceGenerator#genere(com.a2a.adjava.xmodele.XProject, com.a2a.adjava.generators.DomainGeneratorContext)
	 */
	@Override
	public void genere(
			XProject<IDomain<IModelDictionary,IModelFactory>> p_oProject,
			DomainGeneratorContext p_oGeneratorContext) throws Exception {
		log.debug("> ErrorConfigGenerator.genere");
		Chrono oChrono = new Chrono(true);
		
		IModelDictionary oDictionnary = p_oProject.getDomain().getDictionnary();
		Collection<MEntityImpl> listEntities = oDictionnary.getAllEntities();
		Element xRoot = DocumentHelper.createElement("errors");
		Document xDoc = DocumentHelper.createDocument(xRoot);
		
		// Entities
		for( MEntityImpl oEntity : listEntities) {
			this.createErrorCodeForLengthConstraints(oEntity, xRoot);
			this.createErrorCodeForUniqueConstraints(oEntity, xRoot, p_oProject);
		}	
		
		xRoot.addAttribute("main-project", p_oProject.getDomain().getGlobalParameters().get("mainProject"));
		String sErrorConfigFile = FileTypeUtils.computeFilenameForIOSInterface(StringUtils.EMPTY, "MFErrorConfig", p_oProject.getSourceDir());
			
		FilePartGenerationConfig oFilePartGenerationConfig = new FilePartGenerationConfig(
				"gen-errorconfig", "errorconfig.xsl", xDoc );
		
		log.debug("  generate file: {}", sErrorConfigFile);
		this.doInjectionTransform(sErrorConfigFile, p_oProject, p_oGeneratorContext, oFilePartGenerationConfig);
		
		log.debug("< ErrorConfigGenerator.genere: {}", oChrono.stopAndDisplay());
	}

	/**
	 * Create error codes for length constraints
	 * @param p_oEntity entity
	 * @param p_xErrors errors
	 */
	private void createErrorCodeForLengthConstraints( MEntityImpl p_oEntity, Element p_xErrors ) {
		for( MAttribute oAttr: p_oEntity.getAttributes()) {
			if ( oAttr.getLength() != -1 ) {
				String sErrorKey = StringUtils.join( "ERROR_CORE_DATA_VALIDATION_",  p_oEntity.getName().toUpperCase(), "_",
						oAttr.getName().toUpperCase(), "_TOO_LONG" );
				p_xErrors.add(createErrorElement(sErrorKey));
			}
		}
	}
	
	/**
	 * Create error codes for unique constraints of entity
	 * @param p_oEntity entity
	 * @param p_xErrors errors node
	 * @param p_oProject project
	 */
	private void createErrorCodeForUniqueConstraints(MEntityImpl p_oEntity, Element p_xErrors, XProject<IDomain<IModelDictionary,IModelFactory>> p_oProject) {
		UniqueMethodDaoExtractor oUniqueMethodDaoExtractor = p_oProject.getDomain().getExtractor(UniqueMethodDaoExtractor.class);
		for( Entry<String,List<String>> oEntry : oUniqueMethodDaoExtractor.createMapOfUniqueContraints(p_oEntity).entrySet()) {
			StringBuffer sErrorCode = new StringBuffer();
			sErrorCode.append("ERROR_CORE_DATA_VALIDATION_");
			sErrorCode.append(p_oEntity.getName().toUpperCase());
			sErrorCode.append('_');
			sErrorCode.append(oEntry.getKey().toUpperCase(Locale.getDefault()));
			sErrorCode.append("_NOT_UNIQUE");
			p_xErrors.add( this.createErrorElement(sErrorCode.toString()));
		}
	}
	
	/**
	 * Create an error node
	 * @param p_sErrorName error name
	 * @return xml node of error
	 */
	private Element createErrorElement( String p_sErrorName) {
		Element r_xError = DocumentHelper.createElement("error");
		r_xError.addAttribute("key", p_sErrorName);
		
		if (p_sErrorName.hashCode() > Integer.MIN_VALUE) {
			r_xError.addAttribute("value", Integer.toString(Math.abs(p_sErrorName.hashCode())));
		} else {
			// in this case, the value is -2147483648 
			// Math.abs applied to that value will return -2147483648
			r_xError.addAttribute("value", "0");
		}
		
		return r_xError ;
	}
}
