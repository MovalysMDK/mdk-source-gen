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
import com.a2a.adjava.xmodele.MScreen;
import com.a2a.adjava.xmodele.XProject;

/**
 * <p>génération d'un nouvel écran.</p>
 *
 * <p>Copyright (c) 2011</p>
 * <p>Company: Adeuza</p>
 *
 * @since MF-Annapurna
 */
public class ScreenGenerator extends AbstractIncrementalGenerator<IDomain<IModelDictionary,IModelFactory>> {

	/** Logger pour la classe courante */
	private static final Logger log = LoggerFactory.getLogger(ScreenGenerator.class);
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void genere(XProject<IDomain<IModelDictionary,IModelFactory>> p_oProject, DomainGeneratorContext p_oContext) throws Exception {
		log.debug("> ScreenGenerator.genere");
		Chrono oChrono = new Chrono(true);
		for(MScreen oScreen : p_oProject.getDomain().getDictionnary().getAllScreens()) {
			this.createScreen(oScreen, p_oProject, p_oContext);
		}
		log.debug("< ScreenGenerator.genere: {}", oChrono.stopAndDisplay());
	}
	
	/**
	 * <p>Génération du nouveau screen.</p>
	 * @param p_oScreen le screen à générer
	 * @param p_oMProject le flux xml à utiliser avec la xsl pour la génération de l'écran
	 * @param p_mapSession la session
	 * @throws Exception erreur lors de la génération
	 */
	private void createScreen(MScreen p_oScreen, 
			XProject<IDomain<IModelDictionary,IModelFactory>> p_oMProject, DomainGeneratorContext p_oContext) throws Exception {

		Element r_xFile = p_oScreen.toXml();
		r_xFile.addElement("master-package").setText(p_oMProject.getDomain().getRootPackage());
		Document xDoc = DocumentHelper.createDocument(r_xFile);

		String sFile = FileTypeUtils.computeFilenameForJavaClass(p_oMProject.getSourceDir(), p_oScreen.getFullName());
		
		String sModele = null;
		if ((p_oScreen.getPageCount() == 1 && p_oScreen.getMasterPage().getViewModelImpl()!=null)
				|| p_oScreen.getPageCount() > 1) {
			sModele = "screen.xsl";
		}
		else {
			sModele = "simple-screen.xsl";
		}
		
		log.debug("  generation du fichier: {}", sFile);
		this.doIncrementalTransform(sModele, sFile, xDoc, p_oMProject, p_oContext);
	}
}
