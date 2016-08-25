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
package com.a2a.adjava.languages.ionic2.generators;

import java.io.File;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.generator.core.append.AbstractAppendGenerator;
import com.a2a.adjava.generators.DomainGeneratorContext;
import com.a2a.adjava.languages.ionic2.xmodele.MH5Attribute;
import com.a2a.adjava.languages.ionic2.xmodele.MH5Dictionary;
import com.a2a.adjava.languages.ionic2.xmodele.MH5FixedListAttribute;
import com.a2a.adjava.languages.ionic2.xmodele.MH5ModeleFactory;
import com.a2a.adjava.languages.ionic2.xmodele.MH5PanelView;
import com.a2a.adjava.languages.ionic2.xmodele.MH5View;
import com.a2a.adjava.utils.Chrono;
import com.a2a.adjava.utils.FileTypeUtils;
import com.a2a.adjava.xmodele.IDomain;
import com.a2a.adjava.xmodele.XProject;


/**
 * <p>génération d'une nouvel partial view en HTML.</p>
 *
 */
public class PartialsGenerator extends AbstractAppendGenerator<IDomain<MH5Dictionary,MH5ModeleFactory>> {
	/** Logger pour la classe courante */
	private static final Logger log = LoggerFactory.getLogger(PartialsGenerator.class);
	
	private static final String appPath = "webapp/src/app/";
	
	private static final String docPath = appPath + "views/";

	
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void genere(XProject<IDomain<MH5Dictionary,MH5ModeleFactory>> p_oProject, DomainGeneratorContext p_oContext) throws Exception {
		log.debug("> PartialsGenerator.genere");
		Chrono oChrono = new Chrono(true);

		for(MH5View oMH5View : p_oProject.getDomain().getDictionnary().getAllMH5Views()) {
			this.createPartialHtmlFile(oMH5View, p_oProject, p_oContext);
			
			if ( oMH5View.getClass().isAssignableFrom(MH5PanelView.class)) {
				
				MH5PanelView oPanelView = (MH5PanelView) oMH5View;
				createPartialsForFixedList(oPanelView, p_oProject, p_oContext);
			}
		}

		log.debug("< PartialsGenerator.genere: {}", oChrono.stopAndDisplay());
	}
	
	/**
	 * <p>Génération du nouveau html partiel.</p>
	 * @param p_oScreen le screen de référence
	 * @param p_oMProject le flux xml à utiliser avec la xsl pour la génération de l'écran
	 * @param p_mapSession la session
	 * @throws Exception erreur lors de la génération
	 */
	private void createPartialHtmlFile(MH5View p_oMH5View, 
			XProject<IDomain<MH5Dictionary,MH5ModeleFactory>> p_oMProject, DomainGeneratorContext p_oContext) throws Exception {

		String docControllerPath = docPath + p_oMH5View.getName();

		Element r_xFile = p_oMH5View.toXml();
		r_xFile.addElement("master-package").setText(p_oMProject.getDomain().getRootPackage());
		Document xDoc = DocumentHelper.createDocument(r_xFile);

		String sFile = FileTypeUtils.computeFilenameForHTML(docControllerPath, p_oMH5View.getName());
		File oTargetFile = new File(p_oMProject.getLayoutDir(), sFile);

		String sModele = "partials/partials.xsl";

		log.debug("  generation du fichier: {}", sFile);
		this.doAppendGeneration(xDoc, sModele, oTargetFile, p_oMProject, p_oContext);

	}
	
	/**
	 * @param oPanelView
	 * @throws Exception 
	 */
	private void createPartialsForFixedList(MH5PanelView oPanelView, XProject<IDomain<MH5Dictionary,MH5ModeleFactory>> p_oMProject, DomainGeneratorContext p_oContext) throws Exception {
		for( MH5Attribute oAttr : oPanelView.getSectionAttributes()) {
			if ( MH5FixedListAttribute.class.isAssignableFrom(oAttr.getClass())) {
				createPartialForFixedList((MH5FixedListAttribute)oAttr, oPanelView, p_oMProject, p_oContext);
			}
		}
	}

	private void createPartialForFixedList(MH5FixedListAttribute p_oAttr,
			MH5PanelView p_oPanelView, XProject<IDomain<MH5Dictionary,MH5ModeleFactory>> p_oMProject, DomainGeneratorContext p_oContext) throws Exception {
		
		String sVmPropertyName = p_oAttr.getVisualFieldAttribute().getParameterValue("fixedListVmPropertyName");
		
		String sFile = appPath + p_oAttr.getDetailPartial();

		Element r_xFile = p_oPanelView.toXml();
		r_xFile.addElement("fixedList").setText(sVmPropertyName);
		File oTargetFile = new File(p_oMProject.getLayoutDir(), sFile);

		
		Document xDoc = DocumentHelper.createDocument(r_xFile);
		this.doAppendGeneration(xDoc, "partials/partials-fixedlist.xsl", oTargetFile, p_oMProject, p_oContext);

	}
}
