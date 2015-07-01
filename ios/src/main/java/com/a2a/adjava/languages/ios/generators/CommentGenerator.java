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
import com.a2a.adjava.generators.DomainGeneratorContext;
import com.a2a.adjava.languages.ios.xmodele.MIOSDictionnary;
import com.a2a.adjava.languages.ios.xmodele.MIOSDomain;
import com.a2a.adjava.languages.ios.xmodele.MIOSModeleFactory;
import com.a2a.adjava.utils.FileTypeUtils;
import com.a2a.adjava.xmodele.MComment;
import com.a2a.adjava.xmodele.XProject;

/**
 * Controller generator
 * @author lmichenaud
 *
 */
public class CommentGenerator extends AbstractIncrementalGenerator<MIOSDomain<MIOSDictionnary, MIOSModeleFactory>> {

	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(CommentGenerator.class);
		
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.generators.ResourceGenerator#genere(com.a2a.adjava.xmodele.XProject, com.a2a.adjava.generators.DomainGeneratorContext)
	 */
	@Override
	public void genere(
			XProject<MIOSDomain<MIOSDictionnary, MIOSModeleFactory>> p_oMProject,
			DomainGeneratorContext p_oGeneratorContext) throws Exception {
		log.debug("> IOSCommentGenerator.genere");

		for(MComment oComment : p_oMProject.getDomain().getDictionnary().getAllComments()) {
			this.createComment(oComment, p_oMProject, p_oGeneratorContext);
		}
		
		log.debug("< IOSCommentGenerator.genere");
	}
	
	/**
	 * <p>Génération du nouveau Comment.</p>
	 * @param p_oComment le commentaire à générer
	 * @param p_oMProject project
	 * @param p_oContext generator context
	 * @throws Exception erreur lors de la génération
	 */
	private void createComment(MComment p_oComment, 
			XProject<MIOSDomain<MIOSDictionnary, MIOSModeleFactory>> p_oMProject, DomainGeneratorContext p_oContext) throws Exception {

		Element r_xFile = p_oComment.toXml();
		//r_xFile.addElement("master-package").setText(p_oMProject.getDomain().getRootPackage());
		Document xDoc = DocumentHelper.createDocument(r_xFile);

		String sFile = FileTypeUtils.computeFilenameForHtmlClass("resources/html", p_oComment.getFullName());
		
		String sModele = null;
		sModele = "comment.xsl";
		
		log.debug("  generation du fichier: {}", sFile);
		this.doIncrementalTransform(sModele, sFile, xDoc, p_oMProject, p_oContext);
	}	
}
