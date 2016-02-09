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
import com.a2a.adjava.xmodele.MEnumeration;
import com.a2a.adjava.xmodele.XProject;

/**
 * 
 * <p>
 * Génère les énumérations du modele
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
public class EnumGenerator extends AbstractIncrementalGenerator<IDomain<IModelDictionary,IModelFactory>> {

	/**
	 * Logger for this class
	 */
	private static final Logger log = LoggerFactory.getLogger(EnumGenerator.class);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void genere(XProject<IDomain<IModelDictionary,IModelFactory>> p_oProject, DomainGeneratorContext p_oContext) throws Exception {
		log.debug("> EnumGenerator.genere");
		Chrono oChrono = new Chrono(true);
		IModelDictionary oDictionnary = p_oProject.getDomain().getDictionnary();

		for (MEnumeration oEnumeration : oDictionnary.getAllEnumerations()) {
			createEnumeration(oEnumeration, p_oProject, p_oContext);
		}

		log.debug("< EnumGenerator.genere: {}", oChrono.stopAndDisplay());
	}

	/**
	 * Genere les enumerations du modele
	 * 
	 * @param p_oEnumeration
	 *            Enumeration a generer
	 * @param p_oNonGeneratedBlocExtractor
	 *            les blocs non generes
	 * @param p_oProjectConfig
	 *            config adjava
	 * @param p_oXslEnumTransformer
	 *            transformer xsl
	 * @throws Exception
	 *             Echec de generation
	 */
	private void createEnumeration(MEnumeration p_oEnumeration, XProject<IDomain<IModelDictionary,IModelFactory>> p_oProject,
			DomainGeneratorContext p_oContext) throws Exception {

		Document xEnumeration = DocumentHelper.createDocument(p_oEnumeration.toXml());

		String sPojoFile = FileTypeUtils.computeFilenameForJavaClass(p_oProject.getSourceDir(), p_oEnumeration.getFullName());
		
		log.debug("  generate file: {}", sPojoFile);
		this.doIncrementalTransform("enum.xsl", sPojoFile, xEnumeration, p_oProject, p_oContext);
	}
}
