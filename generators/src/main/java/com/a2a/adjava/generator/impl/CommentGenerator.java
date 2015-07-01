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
import com.a2a.adjava.xmodele.MComment;
import com.a2a.adjava.xmodele.MScreen;
import com.a2a.adjava.xmodele.XProject;

/**
 * <p>G��n��ration d'un commentaire</p>
 *
 * <p>Copyright (c) 2015</p>
 * <p>Company: Adeuza</p>
 */
public class CommentGenerator extends AbstractIncrementalGenerator<IDomain<IModelDictionary,IModelFactory>> {

	/** Logger pour la classe courante */
	private static final Logger log = LoggerFactory.getLogger(CommentGenerator.class);
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void genere(XProject<IDomain<IModelDictionary,IModelFactory>> p_oProject, DomainGeneratorContext p_oContext) throws Exception {
		log.debug("> CommentGenerator.genere");
		Chrono oChrono = new Chrono(true);
		for(MComment oComment : p_oProject.getDomain().getDictionnary().getAllComments()) {
			this.createComment(oComment, p_oProject, p_oContext);
		}
		log.debug("< CommentGenerator.genere: {}", oChrono.stopAndDisplay());
	}
	
	/**
	 * <p>G��n��ration du nouveau Comment.</p>
	 * @param p_oComment le commentaire �� g��n��rer
	 * @param p_oMProject le flux xml �� utiliser avec la xsl pour la g��n��ration de l'��cran
	 * @param p_mapSession la session
	 * @throws Exception erreur lors de la g��n��ration
	 */
	private void createComment(MComment p_oComment, 
			XProject<IDomain<IModelDictionary,IModelFactory>> p_oMProject, DomainGeneratorContext p_oContext) throws Exception {

		Element r_xFile = p_oComment.toXml();
		//r_xFile.addElement("master-package").setText(p_oMProject.getDomain().getRootPackage());
		Document xDoc = DocumentHelper.createDocument(r_xFile);

		String sFile = FileTypeUtils.computeFilenameForHtmlClass(p_oMProject.getAssetsDir(), p_oComment.getFullName());
		
		String sModele = null;
		sModele = "comment.xsl";
		
		log.debug("  generation du fichier: {}", sFile);
		this.doIncrementalTransform(sModele, sFile, xDoc, p_oMProject, p_oContext);
	}
}
