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

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.generator.core.incremental.AbstractIncrementalGenerator;
import com.a2a.adjava.generators.DomainGeneratorContext;
import com.a2a.adjava.languages.ios.xmodele.MIOSDictionnary;
import com.a2a.adjava.languages.ios.xmodele.MIOSDomain;
import com.a2a.adjava.languages.ios.xmodele.MIOSModeleFactory;
import com.a2a.adjava.utils.Chrono;
import com.a2a.adjava.utils.FileTypeUtils;
import com.a2a.adjava.xmodele.IModelDictionary;
import com.a2a.adjava.xmodele.MEntityImpl;
import com.a2a.adjava.xmodele.XProject;

/**
 * Entity implementation generator
 * @author lmichenaud
 *
 */
public class EntityImplGenerator extends AbstractIncrementalGenerator<MIOSDomain<MIOSDictionnary, MIOSModeleFactory>> {

	/**
	 * Logger for this class
	 */
	private static final Logger log = LoggerFactory.getLogger(EntityImplGenerator.class);
	
	/**
	 * Xsl template for Entity protocol 
	 */
	private static final String ENTITY_IMPL_XSL = "entity-impl.xsl";
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void genere(XProject<MIOSDomain<MIOSDictionnary, MIOSModeleFactory>> p_oProject, DomainGeneratorContext p_oContext) throws Exception {
		
		log.debug("> EntityImplGenerator.genere");
		
		Chrono oChrono = new Chrono(true);
		IModelDictionary oDictionnary = p_oProject.getDomain().getDictionnary();
		
		for (MEntityImpl oClass : oDictionnary.getAllEntities()) {
			createEntityImpl(oClass, p_oProject, p_oContext);
		}
		
		log.debug("< EntityImplGenerator.genere: {}", oChrono.stopAndDisplay());
	}
	
	/**
	 * Genere implementation of entity
	 * @param p_oEntity entity class
	 * @param p_oMProject project
	 * @param p_oContext generator context
	 * @throws Exception generation failure
	 */
	private void createEntityImpl( MEntityImpl p_oEntity,
			XProject<MIOSDomain<MIOSDictionnary, MIOSModeleFactory>> p_oMProject, DomainGeneratorContext p_oContext) throws Exception {
		
		Element xEntity = p_oEntity.toXml();
		xEntity.addAttribute("main-project", p_oMProject.getDomain().getGlobalParameters().get("mainProject"));
		Document xImpl = DocumentHelper.createDocument(xEntity);

		String sImplFile = FileTypeUtils.computeFilenameForIOSImpl(
			"model", p_oEntity.getName(), p_oMProject.getSourceDir());
		
		log.debug("  generate file: {}", sImplFile);
		this.doIncrementalTransform(ENTITY_IMPL_XSL, sImplFile, xImpl, p_oMProject, p_oContext);
	}
}
