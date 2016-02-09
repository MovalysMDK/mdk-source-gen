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
import org.dom4j.Element;
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
import com.a2a.adjava.xmodele.MEntityInterface;
import com.a2a.adjava.xmodele.MJoinEntityImpl;
import com.a2a.adjava.xmodele.XProject;

/**
 * 
 * <p>
 * Genere les interfaces des beans du modele
 * </p>
 * 
 * <p>
 * Copyright (c) 2009
 * <p>
 * Company: Adeuza
 * 
 * @author lmichenaud
 * 
 */
public class PojoInterfaceGenerator extends AbstractIncrementalGenerator<IDomain<IModelDictionary,IModelFactory>> {

	/**
	 * Logger for this class
	 */
	private static final Logger log = LoggerFactory.getLogger(PojoInterfaceGenerator.class);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void genere(XProject<IDomain<IModelDictionary,IModelFactory>> p_oProject, DomainGeneratorContext p_oContext) throws Exception {
		log.debug("> PojoInterfaceGenerator.genere");
		Chrono oChrono = new Chrono(true);
		
		IModelDictionary oDictionnary = p_oProject.getDomain().getDictionnary();

		for (MEntityImpl oClass : oDictionnary.getAllEntities()) {
			createInterface(oClass.getMasterInterface(), oClass, p_oProject, p_oContext);
		}

		for (MJoinEntityImpl oJoinClass : oDictionnary.getAllJoinClasses()) {
			createInterface(oJoinClass.getMasterInterface(), oJoinClass, p_oProject, p_oContext);
		}

		log.debug("< PojoGenerator.genere: {}", oChrono.stopAndDisplay());
	}

	/**
	 * 
	 * Genere le fichier java de l'interface passee en parametre
	 * 
	 * @param p_oInterface
	 *            l'interface
	 * @param p_oClass
	 *            l'implementation de l'interface
	 * @param p_oNonGeneratedBlocExtractor
	 *            les blocs non generes
	 * @param p_oProjectConfig
	 *            config adjava
	 * @param p_oXslInterfaceTransformer
	 *            le transformer xsl
	 * @throws Exception
	 *             echec de generation
	 */
	private void createInterface(MEntityInterface p_oInterface, MEntityImpl p_oClass,
			XProject<IDomain<IModelDictionary,IModelFactory>> p_oMProject, DomainGeneratorContext p_oContext) throws Exception {

		Element r_xInterfaceFile = DocumentHelper.createElement("pojo");
		r_xInterfaceFile.add(p_oInterface.toXml());
		r_xInterfaceFile.add(p_oClass.toXml());
		Document xInterfaceDoc = DocumentHelper.createDocument(r_xInterfaceFile);

		String sInterfaceFile = FileTypeUtils.computeFilenameForJavaClass(p_oMProject.getSourceDir(), p_oInterface.getFullName());
		
		log.debug("  generation du fichier: {}", sInterfaceFile);
		this.doIncrementalTransform("interface.xsl", sInterfaceFile, xInterfaceDoc, p_oMProject, p_oContext);
	}
}
