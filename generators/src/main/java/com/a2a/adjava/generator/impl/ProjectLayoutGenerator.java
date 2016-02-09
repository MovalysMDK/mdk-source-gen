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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.generator.core.AbstractGenerator;
import com.a2a.adjava.generators.DomainGeneratorContext;
import com.a2a.adjava.utils.Chrono;
import com.a2a.adjava.xmodele.IDomain;
import com.a2a.adjava.xmodele.IModelDictionary;
import com.a2a.adjava.xmodele.IModelFactory;
import com.a2a.adjava.xmodele.XProject;

/**
 * 
 * <p>
 * Cree l'arborescence des repertoires
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
public class ProjectLayoutGenerator extends AbstractGenerator<IDomain<IModelDictionary,IModelFactory>> {

	/**
	 * Logger for this class
	 */
	private static final Logger log = LoggerFactory.getLogger(ProjectLayoutGenerator.class);

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.generators.ResourceGenerator#genere(com.a2a.adjava.xmodele.XProject, com.a2a.adjava.generators.DomainGeneratorContext)
	 */
	@Override
	public void genere(XProject<IDomain<IModelDictionary,IModelFactory>> p_oProject, DomainGeneratorContext p_oContext) throws Exception {
		log.debug("> ProjectLayoutGenerator.createDirectories");
		Chrono oChrono = new Chrono(true);
		
		List<File> listDirectories = new ArrayList<File>();
		if ( p_oProject.getSourceDir() != null ) {
			listDirectories.add( new File(p_oProject.getBaseDir(), p_oProject.getSourceDir()));
		}
		if ( p_oProject.getDdlDir() != null ) {
			listDirectories.add( new File(p_oProject.getBaseDir(), p_oProject.getDdlDir()));
		}
		if ( p_oProject.getAssetsDir() != null ) {
			listDirectories.add( new File(p_oProject.getBaseDir() , p_oProject.getAssetsDir()));
		}

		for (File oDir : listDirectories) {
			log.debug("  create folder: {}", oDir.getPath());
			FileUtils.forceMkdir(oDir);
		}

		log.debug("< ProjectLayoutGenerator.createDirectories: {}", oChrono.stopAndDisplay());
	}
}
