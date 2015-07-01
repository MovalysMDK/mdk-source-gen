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
package com.a2a.adjava.languages.html5.generators;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.generator.core.incremental.AbstractIncrementalGenerator;
import com.a2a.adjava.generators.DomainGeneratorContext;
import com.a2a.adjava.languages.html5.xmodele.MH5Attribute;
import com.a2a.adjava.languages.html5.xmodele.MH5Dictionary;
import com.a2a.adjava.languages.html5.xmodele.MH5ImportDelegate;
import com.a2a.adjava.languages.html5.xmodele.MH5ListPanelView;
import com.a2a.adjava.languages.html5.xmodele.MH5ModeleFactory;
import com.a2a.adjava.languages.html5.xmodele.MH5PanelView;
import com.a2a.adjava.languages.html5.xmodele.MH5ScreenView;
import com.a2a.adjava.languages.html5.xmodele.MH5View;
import com.a2a.adjava.utils.Chrono;
import com.a2a.adjava.utils.FileTypeUtils;
import com.a2a.adjava.xmodele.IDomain;
import com.a2a.adjava.xmodele.MLinkedInterface;
import com.a2a.adjava.xmodele.XProject;

/**
 * <p>génération de controller view.</p>
 *
 */
public class ControllerGenerator extends AbstractIncrementalGenerator<IDomain<MH5Dictionary,MH5ModeleFactory>> {

	
	
	/** Logger pour la classe courante */
	private static final Logger log = LoggerFactory.getLogger(ControllerGenerator.class);
	
	private static final String docPath = "webapp/src/app/views/";

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void genere(XProject<IDomain<MH5Dictionary,MH5ModeleFactory>> p_oProject, DomainGeneratorContext p_oContext) throws Exception {
		log.debug("> ControllerGenerator.genere");
		Chrono oChrono = new Chrono(true);

		for(MH5View oMH5View : p_oProject.getDomain().getDictionnary().getAllMH5Views()) {
			this.createControlerFile(oMH5View, p_oProject, p_oContext);
		}

		log.debug("< ControllerGenerator.genere: {}", oChrono.stopAndDisplay());
	}
	
	/**
	 * <p>Génération du nouveau javascript de controller.</p>
	 * @param p_oMH5View le view de référence
	 * @param p_oMProject le flux xml à utiliser avec la xsl pour la génération de l'écran
	 * @throws Exception erreur lors de la génération
	 */
	private void createControlerFile(MH5View p_oMH5View, 
			XProject<IDomain<MH5Dictionary,MH5ModeleFactory>> p_oMProject, DomainGeneratorContext p_oContext) throws Exception {

		String docControllerPath = docPath + p_oMH5View.getName();
		
		Element r_xFile = p_oMH5View.toXml();
		r_xFile.addElement("master-package").setText(p_oMProject.getDomain().getRootPackage());
		
		MH5ImportDelegate oMH5ImportDelegate = p_oMProject.getDomain().getXModeleFactory().createImportDelegate(this);
		this.computeImportForController(oMH5ImportDelegate, p_oMH5View, p_oMProject, p_oContext);
		r_xFile.add(oMH5ImportDelegate.toXml());
		
		Document xDoc = DocumentHelper.createDocument(r_xFile);

		String sFile = FileTypeUtils.computeFilenameForJS(docControllerPath, p_oMH5View.getName()+"Ctrl");
		
		String sModele = "controller/controller-impl.xsl";

		log.debug("  generation du fichier: {}", sFile);
		this.doIncrementalTransform(sModele, sFile, xDoc, p_oMProject, p_oContext);
	}
	
	
	/**
	 * Compute imports for controller implementation
	 * @param p_oMDataLoader dataloader
	 * @param p_oMProject project
	 * @param p_oContext context
	 * @throws Exception
	 */
	protected void computeImportForController( MH5ImportDelegate p_oMH5ImportDelegate,
			MH5View p_oMH5View, XProject<IDomain<MH5Dictionary,MH5ModeleFactory>> p_oMProject,
			DomainGeneratorContext p_oContext) throws Exception {

		if(p_oMH5View instanceof MH5ScreenView){
			if (((MH5ScreenView) p_oMH5View).isWorkspace()) {
				p_oMH5ImportDelegate.addImport("MFWorkspaceScopeBuilder");
				p_oMH5ImportDelegate.addImport(p_oMH5View.getName()+"DetailDataLoader");
				if(((MH5ScreenView) p_oMH5View).hasSaveAction()) {
					p_oMH5ImportDelegate.addImport("Save"+p_oMH5View.getName()+"Action");
				}
			} else {
				p_oMH5ImportDelegate.addImport("MFViewScopeBuilder");
			}
		}
		else if( p_oMH5View instanceof MH5ListPanelView){
			if (((MH5ListPanelView) p_oMH5View).isPanelOfWorkspace()) {
				p_oMH5ImportDelegate.addImport("MFWorkspaceMasterScopeBuilder");
			} else {
				p_oMH5ImportDelegate.addImport("MFListScopeBuilder");
			}
			p_oMH5ImportDelegate.addImport(((MH5ListPanelView)p_oMH5View).getPanelListName()+"Factory");
			
			if (((MH5ListPanelView)p_oMH5View).isAttachedToEntity()) {
				p_oMH5ImportDelegate.addImport(p_oMH5View.getName() + "DataLoader");
			}
		} 
		else{
			MH5PanelView oPanelView = (MH5PanelView) p_oMH5View;
			if (oPanelView.isPanelOfWorkspace()) {
				p_oMH5ImportDelegate.addImport("MFWorkspaceDetailScopeBuilder");
			} else {
				p_oMH5ImportDelegate.addImport("MFFormScopeBuilder");
				
				if (oPanelView.isAttachedToEntity()) {
					p_oMH5ImportDelegate.addImport(p_oMH5View.getName() + "DataLoader");
				}
			}
			p_oMH5ImportDelegate.addImport(p_oMH5View.getName()+"VMFactory");
			if ( oPanelView.getSaveActionName() != null ) {
				p_oMH5ImportDelegate.addImport(oPanelView.getSaveActionName());
			}
			if ( oPanelView.getDeleteActionName() != null ) {
				p_oMH5ImportDelegate.addImport(oPanelView.getDeleteActionName());
			}
		}
		
	}
	
}
