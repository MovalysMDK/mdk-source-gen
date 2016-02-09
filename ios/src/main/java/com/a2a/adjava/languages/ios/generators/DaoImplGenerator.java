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
import com.a2a.adjava.xmodele.MAssociation;
import com.a2a.adjava.xmodele.MAssociation.AssociationType;
import com.a2a.adjava.xmodele.MDaoImpl;
import com.a2a.adjava.xmodele.MJoinEntityImpl;
import com.a2a.adjava.xmodele.XProject;

/**
 * Generator of dao implementation
 * @author lmichenaud
 *
 */
public class DaoImplGenerator extends AbstractIncrementalGenerator<MIOSDomain<MIOSDictionnary, MIOSModeleFactory>> {

	/**
	 * Logger for this class
	 */
	private static final Logger log = LoggerFactory.getLogger(DaoImplGenerator.class);

	/**
	 * Template for dao impl xsl
	 */
	private static final String DAO_IMPL_XSL = "dao-impl.xsl";
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void genere( XProject<MIOSDomain<MIOSDictionnary,MIOSModeleFactory>> p_oProject, DomainGeneratorContext p_oContext) throws Exception {
		log.debug("> DaoImplGenerator.genere");
		Chrono oChrono = new Chrono(true);
		
		NonGeneratedBlocExtractor oNonGeneratedBlocExtractor = new NonGeneratedBlocExtractor();

		for (MDaoImpl oDao : p_oProject.getDomain().getDictionnary().getAllDaos()) {
			// ignore join entities
			if ( ! MJoinEntityImpl.class.isAssignableFrom(oDao.getMEntityImpl().getClass())) {
				createDao(oDao, oNonGeneratedBlocExtractor, p_oProject, p_oContext );
			}
		}

		log.debug("< DaoImplGenerator.genere: {}", oChrono.stopAndDisplay());
	}
	
	/**
	 * Genere dao implementation
	 * @param p_oDao dao
	 * @param p_oNonGeneratedBlocExtractor extractor of non-generated blocs
	 * @param p_oProject project
	 * @param p_oContext context
	 * @throws Exception exception
	 */
	private void createDao(MDaoImpl p_oDao, NonGeneratedBlocExtractor p_oNonGeneratedBlocExtractor, 
			XProject<MIOSDomain<MIOSDictionnary,MIOSModeleFactory>> p_oProject, DomainGeneratorContext p_oContext) throws Exception {

		Element xDao = p_oDao.toXml();
		xDao.addAttribute("main-project", p_oProject.getDomain().getGlobalParameters().get("mainProject"));
		xDao.add(this.computeImports(p_oDao));
		Document xDaoDoc = DocumentHelper.createDocument(xDao);

		String sImplFile = FileTypeUtils.computeFilenameForIOSImpl(
				"dao", p_oDao.getMEntityImpl().getName() + "+Dao", p_oProject.getSourceDir());
		
		log.debug("  generate file: {}", sImplFile);
		this.doIncrementalTransform(DAO_IMPL_XSL, sImplFile, xDaoDoc, p_oProject, p_oContext);
	}
	
	/**
	 * Compute imports for dao interface
	 * @param p_oDao dao
	 * @return xml of imports
	 */
	public Element computeImports( MDaoImpl p_oDao) {
		MIOSImportDelegate oImportDlg = new MIOSImportDelegate(this);
		oImportDlg.addImport(MIOSImportCategory.DAO.name(), p_oDao.getMEntityImpl().getName() + "+Dao");
		
		for( MAssociation oAssociation : p_oDao.getMEntityImpl().getAssociations()) {
			if ( !oAssociation.isSelfRef() && (oAssociation.getAssociationType().equals(AssociationType.MANY_TO_ONE) 
				|| oAssociation.getAssociationType().equals(AssociationType.ONE_TO_ONE) 
				|| oAssociation.getAssociationType().equals(AssociationType.MANY_TO_MANY))) {
				oImportDlg.addImport(MIOSImportCategory.ENTITIES.name(), oAssociation.getRefClass().getName());
			}
		}
		
		return oImportDlg.toXml();
	}
}
