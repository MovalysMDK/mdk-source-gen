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
import com.a2a.adjava.xmodele.MDaoImpl;
import com.a2a.adjava.xmodele.MDaoInterface;
import com.a2a.adjava.xmodele.XProject;

/**
 * 
 * <p>
 * Generation des interfaces des dao
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
public class DaoInterfaceGenerator extends AbstractIncrementalGenerator<IDomain<IModelDictionary,IModelFactory>> {

	/**
	 * Logger for this class
	 */
	private static final Logger log = LoggerFactory.getLogger(DaoInterfaceGenerator.class);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void genere(XProject<IDomain<IModelDictionary,IModelFactory>> p_oMProject, DomainGeneratorContext p_oContext) throws Exception {

		log.debug("> DaoInterfaceGenerator.genereDaoInterfaces");
		Chrono oChrono = new Chrono(true);
		for (MDaoImpl oDao : p_oMProject.getDomain().getDictionnary().getAllDaos()) {
			this.createDaoInterface(oDao.getMasterInterface(), p_oMProject, p_oContext);
		}

		log.debug("< DaoInterfaceGenerator.genereDaoInterfaces: {}", oChrono.stopAndDisplay());
	}

	/**
	 * Generate dao interface
	 * @param p_oMDaoInterface dao interface
	 * @param p_oProject project
	 * @param p_oContext context
	 * @throws Exception failure
	 */
	private void createDaoInterface(MDaoInterface p_oMDaoInterface, XProject<IDomain<IModelDictionary,IModelFactory>> p_oProject,
			DomainGeneratorContext p_oContext) throws Exception {

		Document xDaoInterfaceDoc = DocumentHelper.createDocument(p_oMDaoInterface.toXml());

		String sInterfaceFile = FileTypeUtils.computeFilenameForJavaClass(p_oProject.getSourceDir(), p_oMDaoInterface.getFullName());
		
		log.debug("  generate file: {}", sInterfaceFile);
		this.doIncrementalTransform("interface-dao.xsl", sInterfaceFile, xDaoInterfaceDoc, p_oProject,
				p_oContext);
	}

}
