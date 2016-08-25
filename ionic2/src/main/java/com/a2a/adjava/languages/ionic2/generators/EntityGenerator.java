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
import com.a2a.adjava.xmodele.IDomain;
import com.a2a.adjava.xmodele.IModelDictionary;
import com.a2a.adjava.xmodele.IModelFactory;
import com.a2a.adjava.xmodele.MEntityImpl;
import com.a2a.adjava.xmodele.XProject;

/**
 * 
 * <p>
 * Generates the implementations of the beans for model entities.
 * </p>
 * 
 * <p>
 * Copyright (c) 2009
 * <p>
 * Company: Adeuza
 * 
 */
public class EntityGenerator extends AbstractIncrementalGenerator<IDomain<IModelDictionary,IModelFactory>> {
	
	/**
	 * Logger for this class
	 */
	private static final Logger log = LoggerFactory.getLogger(EntityGenerator.class);
	
	private static final String docPath = "app/data/model";
	
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void genere(
			XProject<IDomain<IModelDictionary, IModelFactory>> p_oMProject,
			DomainGeneratorContext p_oGeneratorContext) throws Exception {
		
		log.debug("> EntityImplGenerator.genere");
		Chrono oChrono = new Chrono(true);
		IModelDictionary oDictionnary = p_oMProject.getDomain().getDictionnary();
		
		
		for (MEntityImpl oClass : oDictionnary.getAllEntities()) {
			createEntity(oClass, p_oMProject, p_oGeneratorContext);
		}

		log.debug("< EntityImplGenerator.genere: {}", oChrono.stopAndDisplay());
	}
	
	

	/**
	 * Generates the implementation of the bean for the model
	 * 
	 * @param p_oClass
	 *            the implementation class of the bean
	 * @param p_oNonGeneratedBlocExtractor
	 *            the non generated blocks
	 * @param p_oProjectConfig
	 *            config adjava
	 * @param p_oXslPojoTransformer
	 *            transformer xsl
	 * @throws Exception
	 *             for generation failures
	 */
	private void createEntity(MEntityImpl p_oClass, XProject<IDomain<IModelDictionary,IModelFactory>> p_oMProject,
			DomainGeneratorContext p_oContext) throws Exception {

		Document xClass = DocumentHelper.createDocument(p_oClass.toXml());

		String sPojoFile = FileTypeUtils.computeFilenameForTS(docPath, p_oClass.getName());

		log.debug("  generation of file: {}", sPojoFile);
		this.doIncrementalTransform("entity-impl.xsl", sPojoFile, xClass, p_oMProject, p_oContext);
	}

}
