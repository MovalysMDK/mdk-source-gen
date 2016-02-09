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
package com.a2a.adjava.generator.impl;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.generator.core.incremental.AbstractIncrementalGenerator;
import com.a2a.adjava.generators.DomainGeneratorContext;
import com.a2a.adjava.utils.Chrono;
import com.a2a.adjava.utils.FileTypeUtils;
import com.a2a.adjava.xmodele.IDomain;
import com.a2a.adjava.xmodele.IModelDictionary;
import com.a2a.adjava.xmodele.IModelFactory;
import com.a2a.adjava.xmodele.MEntityImpl;
import com.a2a.adjava.xmodele.MJoinEntityImpl;
import com.a2a.adjava.xmodele.XProject;

/**
 * <p>
 * Generation des interfaces des factory
 * </p>
 * 
 * <p>
 * Copyright (c) 2009
 * <p>
 * Company: Adeuza
 * 
 * @author mmadigand
 * @author lmichenaud
 * 
 */
public class FactoryInterfaceGenerator extends AbstractIncrementalGenerator<IDomain<IModelDictionary,IModelFactory>> {

	/**
	 * 
	 */
	private static final Logger log = LoggerFactory.getLogger(FactoryInterfaceGenerator.class);

	/**
	 * @see com.a2a.adjava.generator.ResourceGenerator#genere(com.a2a.adjava.project.ProjectConfig, com.a2a.adjava.xmodele.MModele,
	 *      com.a2a.adjava.schema.Schema, java.util.Map)
	 */
	@Override
	public void genere(XProject<IDomain<IModelDictionary,IModelFactory>> p_oProject, DomainGeneratorContext p_oContext) throws Exception {
		log.debug("> FactoryInterfaceGenerator.genere");
		Chrono oChrono = new Chrono(true);
		IModelDictionary oDictionnary = p_oProject.getDomain().getDictionnary();

		for (MEntityImpl oClass : oDictionnary.getAllEntities()) {
			this.genereFactoryInterface(oClass, p_oProject, p_oContext);
		}

		for (MJoinEntityImpl oJoinClass : oDictionnary.getAllJoinClasses()) {
			this.genereFactoryInterface(oJoinClass, p_oProject, p_oContext);
		}
		log.debug("< FactoryInterfaceGenerator.genere: {}", oChrono.stopAndDisplay());
	}

	/**
	 * @param p_oProjectConfig
	 * @param p_oClass
	 * @param p_oXslFactoryTransformer
	 * @param p_sProjectPackage
	 * @throws Exception
	 */
	private void genereFactoryInterface(MEntityImpl p_oClass, XProject<IDomain<IModelDictionary,IModelFactory>> p_oMProject, DomainGeneratorContext p_oContext) throws Exception {

		Document xFactorysDoc = DocumentHelper.createDocument(p_oClass.getFactoryInterface().toXml());

		String sFactoryFile = FileTypeUtils.computeFilenameForJavaClass(p_oMProject.getSourceDir(), p_oClass.getFactoryInterface().getFullName());
		
		log.debug("  generate file: {}", sFactoryFile);
		this.doIncrementalTransform("factory-interface.xsl", sFactoryFile, xFactorysDoc, p_oMProject, p_oContext);
	}
}
