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
package com.a2a.adjava.languages.ionic2.generators;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.generator.core.incremental.AbstractIncrementalGenerator;
import com.a2a.adjava.generators.DomainGeneratorContext;
import com.a2a.adjava.utils.Chrono;
import com.a2a.adjava.utils.FileTypeUtils;
import com.a2a.adjava.xmodele.IModelDictionary;
import com.a2a.adjava.xmodele.MEnumeration;
import com.a2a.adjava.xmodele.ModelDictionary;
import com.a2a.adjava.xmodele.XDomain;
import com.a2a.adjava.xmodele.XModeleFactory;
import com.a2a.adjava.xmodele.XProject;

/**
 * 
 * <p>
 * Copyright (c) 2009
 * <p>
 * Company: Adeuza
 * 
 */
public class EnumGenerator extends AbstractIncrementalGenerator<XDomain<ModelDictionary,XModeleFactory>> {
	
	/**
	 * Logger for this class
	 */
	private static final Logger log = LoggerFactory.getLogger(EnumGenerator.class);
	
	private static final String docPath = "webapp/src/app/data/model";
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void genere( XProject<XDomain<ModelDictionary, XModeleFactory>> p_oMProject, DomainGeneratorContext p_oGeneratorContext) 
			throws Exception {
		log.debug("> EnumGenerator.genere");
		Chrono oChrono = new Chrono(true);
		IModelDictionary oDictionnary = p_oMProject.getDomain().getDictionnary();

		for (MEnumeration oEnumeration : oDictionnary.getAllEnumerations()) {
			createEnum(oEnumeration, p_oMProject, p_oGeneratorContext);
		}

		log.debug("< EnumGenerator.genere: {}", oChrono.stopAndDisplay());
	}


	
	/**
	 * Create impl of enumeration
	 * @param p_oEnumeration enum
	 * @param p_oProject project
	 * @param p_oContext context
	 * @throws Exception
	 */
	protected void createEnum( MEnumeration p_oEnumeration, XProject<XDomain<ModelDictionary, XModeleFactory>> p_oProject, DomainGeneratorContext p_oContext) throws Exception {
		
		Document xEnumeration = DocumentHelper.createDocument(p_oEnumeration.toXml());
		xEnumeration.getRootElement().addAttribute("main-project", p_oProject.getDomain().getGlobalParameters().get("mainProject"));

		String sEnumImplFile = FileTypeUtils.computeFilenameForJS( docPath, p_oEnumeration.getName());
		log.debug("  generate file: {}", sEnumImplFile);
		this.doIncrementalTransform("enum-impl.xsl", sEnumImplFile, xEnumeration, p_oProject, p_oContext);
	}


}
