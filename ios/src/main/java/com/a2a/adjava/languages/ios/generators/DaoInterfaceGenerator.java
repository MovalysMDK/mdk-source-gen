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
import com.a2a.adjava.generator.core.incremental.NonGeneratedBlocExtractor;
import com.a2a.adjava.generators.DomainGeneratorContext;
import com.a2a.adjava.languages.ios.xmodele.MIOSDictionnary;
import com.a2a.adjava.languages.ios.xmodele.MIOSDomain;
import com.a2a.adjava.languages.ios.xmodele.MIOSImportDelegate;
import com.a2a.adjava.languages.ios.xmodele.MIOSImportDelegate.MIOSImportCategory;
import com.a2a.adjava.languages.ios.xmodele.MIOSModeleFactory;
import com.a2a.adjava.utils.Chrono;
import com.a2a.adjava.utils.FileTypeUtils;
import com.a2a.adjava.xmodele.MDaoImpl;
import com.a2a.adjava.xmodele.MDaoInterface;
import com.a2a.adjava.xmodele.MJoinEntityImpl;
import com.a2a.adjava.xmodele.XProject;

/**
 * Generator of dao interface
 * @author lmichenaud
 *
 */
public class DaoInterfaceGenerator extends AbstractIncrementalGenerator<MIOSDomain<MIOSDictionnary, MIOSModeleFactory>> {

	/**
	 * Logger for this class
	 */
	private static final Logger log = LoggerFactory.getLogger(DaoInterfaceGenerator.class);

	/**
	 * Template for dao interface xsl
	 */
	private static final String DAO_INTERFACE_XSL = "dao-interface.xsl";
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void genere( XProject<MIOSDomain<MIOSDictionnary,MIOSModeleFactory>> p_oProject, DomainGeneratorContext p_oContext) throws Exception {
		log.debug("> DaoInterfaceGenerator.genere");
		Chrono oChrono = new Chrono(true);
		
		NonGeneratedBlocExtractor oNonGeneratedBlocExtractor = new NonGeneratedBlocExtractor();

		for (MDaoImpl oDao : p_oProject.getDomain().getDictionnary().getAllDaos()) {
			if ( ! MJoinEntityImpl.class.isAssignableFrom(oDao.getMEntityImpl().getClass())) {
				createDaoInterface(oDao.getMasterInterface(), oNonGeneratedBlocExtractor, p_oProject, p_oContext );
			}
		}

		log.debug("< DaoInterfaceGenerator.genere: {}", oChrono.stopAndDisplay());
	}
	
	/**
	 * Genere dao interface
	 * @param p_oDao dao interface
	 * @param p_oNonGeneratedBlocExtractor extractor of non-generated blocs
	 * @param p_oProject project
	 * @param p_oContext context
	 * @throws Exception exception
	 */
	private void createDaoInterface(MDaoInterface p_oDao, NonGeneratedBlocExtractor p_oNonGeneratedBlocExtractor, 
			XProject<MIOSDomain<MIOSDictionnary,MIOSModeleFactory>> p_oProject, DomainGeneratorContext p_oContext) throws Exception {

		Element xDao = p_oDao.toXml();
		xDao.addAttribute("main-project", p_oProject.getDomain().getGlobalParameters().get("mainProject"));
		xDao.add( this.computeImports(p_oDao));
		Document xDaoDoc = DocumentHelper.createDocument(xDao);

		String sInterfaceFile = FileTypeUtils.computeFilenameForIOSInterface(
			"dao", p_oDao.getMEntityImpl().getName() + "+Dao", p_oProject.getSourceDir());
		
		log.debug("  generate file: {}", sInterfaceFile);
		this.doIncrementalTransform(DAO_INTERFACE_XSL, sInterfaceFile, xDaoDoc, p_oProject, p_oContext);
	}
	
	
	/**
	 * Compute imports for dao interface
	 * @param p_oDao dao
	 * @return xml of imports
	 */
	public Element computeImports( MDaoInterface p_oDao) {
		MIOSImportDelegate oImportDlg = new MIOSImportDelegate(this);
		oImportDlg.addImport(MIOSImportCategory.ENTITIES.name(), p_oDao.getMEntityImpl().getName());
		return oImportDlg.toXml();
	}
}
