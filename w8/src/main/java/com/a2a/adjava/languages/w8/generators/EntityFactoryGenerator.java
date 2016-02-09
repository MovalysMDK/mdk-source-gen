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
package com.a2a.adjava.languages.w8.generators;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.generator.core.incremental.AbstractIncrementalGenerator;
import com.a2a.adjava.generators.DomainGeneratorContext;
import com.a2a.adjava.languages.w8.xmodele.MW8Dictionary;
import com.a2a.adjava.languages.w8.xmodele.MW8Domain;
import com.a2a.adjava.languages.w8.xmodele.MW8ModeleFactory;
import com.a2a.adjava.utils.Chrono;
import com.a2a.adjava.utils.FileTypeUtils;
import com.a2a.adjava.xmodele.IModelDictionary;
import com.a2a.adjava.xmodele.MJoinEntityImpl;
import com.a2a.adjava.xmodele.MEntityImpl;
import com.a2a.adjava.xmodele.XProject;

/**
 * Generator for factories of entities
 * @author lmichenaud
 *
 */
public class EntityFactoryGenerator extends AbstractIncrementalGenerator<MW8Domain<MW8Dictionary, MW8ModeleFactory>> {

	/**
	 * Logger for this class
	 */
	private static final Logger log = LoggerFactory.getLogger(EntityFactoryGenerator.class);
	
	/**
	 * Xsl template for factory interface of entities
	 */
	private static final String ENTITYFACTORY_INTERFACE_XSL = "factory-interface.xsl";
	
	/**
	 * Xsl template for factory implementations of entities
	 */
	private static final String ENTITYFACTORY_IMPL_XSL = "factory.xsl";
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void genere(XProject<MW8Domain<MW8Dictionary, MW8ModeleFactory>> p_oProject, DomainGeneratorContext p_oContext) throws Exception {
		
		log.debug("> FactoryGenerator.genere");
		log.debug("> FactoryInterfaceGenerator.genere");
		Chrono oChrono = new Chrono(true);
		IModelDictionary oDictionnary = p_oProject.getDomain().getDictionnary();

		for (MEntityImpl oClass : oDictionnary.getAllEntities()) {
			this.genereFactory(oClass, p_oProject, p_oContext);
			this.genereFactoryInterface(oClass, p_oProject, p_oContext);
		}

		for (MJoinEntityImpl oJoinClass : oDictionnary.getAllJoinClasses()) {
			this.genereFactory(oJoinClass, p_oProject, p_oContext);
			this.genereFactoryInterface(oJoinClass, p_oProject, p_oContext);
		}
		log.debug("< FactoryGenerator.genere: {}", oChrono.stopAndDisplay());
		log.debug("< FactoryInterfaceGenerator.genere: {}", oChrono.stopAndDisplay());
	}
	
	/**
	 * Generates factory interface for the given entity
	 * @param p_oEntity entity to process
	 * @param p_oMProject current project
	 * @param p_oContext context of the process
	 * @throws Exception
	 */
	private void genereFactoryInterface(MEntityImpl p_oEntity, XProject<MW8Domain<MW8Dictionary, MW8ModeleFactory>> p_oMProject, DomainGeneratorContext p_oContext) throws Exception {
		
		Element xFactory = DocumentHelper.createElement("factory-interface");
		xFactory.addAttribute("main-project", p_oMProject.getDomain().getGlobalParameters().get("mainProject"));
		xFactory.add(p_oEntity.getFactoryInterface().toXml());
		xFactory.add(p_oEntity.getFactory().toXml());
		Document xImpl = DocumentHelper.createDocument(xFactory);

		String sInterfaceFile = FileTypeUtils.computeFilenameForCSharpImpl(
			"Model.Factories.Interfaces", p_oEntity.getFactoryInterface().getName(), p_oMProject.getSourceDir());
		
		log.debug("  generate file: {}", sInterfaceFile);
		this.doIncrementalTransform(ENTITYFACTORY_INTERFACE_XSL, sInterfaceFile, xImpl, p_oMProject, p_oContext);
	}
	
	/**
	 * Generates factory implementation for the given entity
	 * @param p_oEntity entity to process
	 * @param p_oMProject current project
	 * @param p_oContext context of the process
	 * @throws Exception
	 */
	private void genereFactory(MEntityImpl p_oEntity, XProject<MW8Domain<MW8Dictionary, MW8ModeleFactory>> p_oMProject, DomainGeneratorContext p_oContext)
			throws Exception {
		
		Element xFactory = p_oEntity.getFactory().toXml();
		xFactory.addAttribute("main-project", p_oMProject.getDomain().getGlobalParameters().get("mainProject"));
		Document xImpl = DocumentHelper.createDocument(xFactory);

		String sImplFile = FileTypeUtils.computeFilenameForCSharpImpl(
			"Model.Factories", p_oEntity.getFactory().getName(), p_oMProject.getSourceDir());
		
		log.debug("  generation du fichier: {}", sImplFile);
		this.doIncrementalTransform(ENTITYFACTORY_IMPL_XSL, sImplFile, xImpl, p_oMProject, p_oContext);
	}
}
