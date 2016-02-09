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
import com.a2a.adjava.xmodele.MDialog;
import com.a2a.adjava.xmodele.XProject;

/**
 * @author lmichenaud
 *
 */
public class DialogGenerator  extends AbstractIncrementalGenerator<IDomain<IModelDictionary,IModelFactory>> {

	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(DialogGenerator.class);
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void genere(XProject<IDomain<IModelDictionary,IModelFactory>> p_oProject, DomainGeneratorContext p_oContext) throws Exception {
		log.debug("> DialogGenerator.genere");
		Chrono oChrono = new Chrono(true);
		for(MDialog oDialog : p_oProject.getDomain().getDictionnary().getAllDialogs()) {
			// Don't generate dialog if search screen
			if ( !oDialog.getParent().isSearchScreen()) {
				this.createDialog(oDialog, p_oProject, p_oContext);
			}
		}
		log.debug("< DialogGenerator.genere: {}", oChrono.stopAndDisplay());
	}
	
	/**
	 * @param p_oDialog
	 * @param p_oMProject
	 * @param p_mapSession
	 * @throws Exception
	 */
	private void createDialog(MDialog p_oDialog, 
			XProject<IDomain<IModelDictionary,IModelFactory>> p_oMProject, DomainGeneratorContext p_oContext) throws Exception {

		Element r_xFile = p_oDialog.toXml();
		r_xFile.addElement("master-package").setText(p_oMProject.getDomain().getRootPackage());
		Document xDoc = DocumentHelper.createDocument(r_xFile);
	
		String sFile = FileTypeUtils.computeFilenameForJavaClass(p_oMProject.getSourceDir(), p_oDialog.getFullName());
		
		log.debug("  generation du fichier {}" + sFile);
		this.doIncrementalTransform("ui/dialog/dialog.xsl", sFile, xDoc, p_oMProject, p_oContext);
	}
}
