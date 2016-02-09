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
import com.a2a.adjava.xmodele.IDomain;
import com.a2a.adjava.xmodele.IModelDictionary;
import com.a2a.adjava.xmodele.IModelFactory;
import com.a2a.adjava.xmodele.MEntityImpl;
import com.a2a.adjava.xmodele.MEntityInterface;
import com.a2a.adjava.xmodele.MJoinEntityImpl;
import com.a2a.adjava.xmodele.XProject;
/**
 * 
 * @author dtriboulin
 *
 */
public class EntityInterfaceGenerator extends AbstractIncrementalGenerator<IDomain<IModelDictionary,IModelFactory>> {

	/**
	 * Logger for this class
	 */
	private static final Logger log = LoggerFactory.getLogger(EntityInterfaceGenerator.class);
	
	/**
	 * Xsl template for Entity protocol 
	 */
	private static final String ENTITY_INTERFACE_XSL = "entity-interface.xsl";
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void genere(XProject<IDomain<IModelDictionary,IModelFactory>> p_oProject, DomainGeneratorContext p_oContext) throws Exception {
		log.debug("> EntityInterfaceGenerator.genere");
		
		Chrono oChrono = new Chrono(true);
		
		IModelDictionary oDictionnary = p_oProject.getDomain().getDictionnary();

		for (MEntityImpl oClass : oDictionnary.getAllEntities()) {
			createEntityInterface(oClass.getMasterInterface(), oClass, p_oProject, p_oContext);
		}

		for (MJoinEntityImpl oJoinClass : oDictionnary.getAllJoinClasses()) {
			createEntityInterface(oJoinClass.getMasterInterface(), oJoinClass, p_oProject, p_oContext);
		}
		
		log.debug("< EntityInterfaceGenerator.genere: {}", oChrono.stopAndDisplay());
	}
	
	/**
	 * @param p_oInterface
	 * @param p_oClass
	 * @param p_oMProject
	 * @param p_oContext
	 * @throws Exception
	 */
	private void createEntityInterface(MEntityInterface p_oInterface, MEntityImpl p_oClass,
			XProject<IDomain<IModelDictionary,IModelFactory>> p_oMProject, DomainGeneratorContext p_oContext) throws Exception {
				
		Document xInterfaceDoc = this.createDocument(p_oInterface, p_oClass, p_oMProject);

		String sInterfaceFile = FileTypeUtils.computeFilenameForCSharpImpl(
				"Model.Interfaces", p_oInterface.getName(), p_oMProject.getSourceDir());
		
		log.debug("  generation du fichier: {}", sInterfaceFile);
		this.doIncrementalTransform(ENTITY_INTERFACE_XSL, sInterfaceFile, xInterfaceDoc, p_oMProject, p_oContext);
	}
	
	/**
	 * Create document
	 * @param p_oInterface entity interface
	 * @param p_oClass entity
	 * @param p_oMProject project
	 * @return xml of entity implementation
	 */
	protected Document createDocument(MEntityInterface p_oInterface, MEntityImpl p_oClass,
			XProject<IDomain<IModelDictionary, IModelFactory>> p_oMProject) {

		Element r_xInterfaceFile = DocumentHelper.createElement("pojo");
		r_xInterfaceFile.add(p_oInterface.toXml());
		r_xInterfaceFile.addAttribute("main-project", p_oMProject.getDomain().getGlobalParameters().get("mainProject"));
		r_xInterfaceFile.add(p_oClass.toXml());
		Document xInterfaceDoc = DocumentHelper.createDocument(r_xInterfaceFile);
		return xInterfaceDoc;
	}

}
