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
import com.a2a.adjava.generator.core.incremental.NonGeneratedBlocExtractor;
import com.a2a.adjava.generators.DomainGeneratorContext;
import com.a2a.adjava.utils.Chrono;
import com.a2a.adjava.utils.FileTypeUtils;
import com.a2a.adjava.xmodele.IDomain;
import com.a2a.adjava.xmodele.IModelDictionary;
import com.a2a.adjava.xmodele.IModelFactory;
import com.a2a.adjava.xmodele.MDaoImpl;
import com.a2a.adjava.xmodele.XProject;

/**
 * 
 * <p>
 * Generation des implémentations des Dao
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
public class DaoGenerator extends AbstractIncrementalGenerator<IDomain<IModelDictionary,IModelFactory>> {

	/**
	 * Logger for this class
	 */
	private static final Logger log = LoggerFactory.getLogger(DaoGenerator.class);

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.a2a.adjava.generator.ResourceGenerator#genere(com.a2a.adjava.project.ProjectConfig, com.a2a.adjava.xmodele.MModele,
	 *      com.a2a.adjava.schema.Schema, java.util.Map)
	 */
	public void genere( XProject<IDomain<IModelDictionary,IModelFactory>> p_oProject, DomainGeneratorContext p_oContext) throws Exception {
		log.debug("> DaoGenerator.genere");
		Chrono oChrono = new Chrono(true);
		
		NonGeneratedBlocExtractor oNonGeneratedBlocExtractor = new NonGeneratedBlocExtractor();

		for (MDaoImpl oDao : p_oProject.getDomain().getDictionnary().getAllDaos()) {
			this.createDao(oDao, oNonGeneratedBlocExtractor, p_oProject, p_oContext );
		}

		log.debug("< DaoGenerator.genere: {}", oChrono.stopAndDisplay());
	}

	/**
	 * @param p_oClass
	 * @throws Exception
	 */
	private void createDao(MDaoImpl p_oDao, NonGeneratedBlocExtractor p_oNonGeneratedBlocExtractor, 
			XProject<IDomain<IModelDictionary,IModelFactory>> p_oProject, DomainGeneratorContext p_oContext) throws Exception {

		Document xDao = DocumentHelper.createDocument(p_oDao.toXml());

		String sDaoFile = FileTypeUtils.computeFilenameForJavaClass(p_oProject.getSourceDir(), p_oDao.getFullName());

		log.debug("  generation du fichier {}", sDaoFile);
		this.doIncrementalTransform("dao.xsl", sDaoFile, xDao, p_oProject, p_oContext);
	}
}
