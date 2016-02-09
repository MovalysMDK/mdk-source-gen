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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.a2a.adjava.generator.core.incremental.AbstractIncrementalGenerator;
import com.a2a.adjava.generators.DomainGeneratorContext;
import com.a2a.adjava.languages.ios.xmodele.MIOSDictionnary;
import com.a2a.adjava.languages.ios.xmodele.MIOSDomain;
import com.a2a.adjava.languages.ios.xmodele.MIOSImportDelegate;
import com.a2a.adjava.languages.ios.xmodele.MIOSModeleFactory;
import com.a2a.adjava.languages.ios.xmodele.controllers.MIOSController;
import com.a2a.adjava.languages.ios.xmodele.controllers.MIOSControllerType;
import com.a2a.adjava.languages.ios.xmodele.controllers.MIOSFormViewController;
import com.a2a.adjava.languages.ios.xmodele.controllers.MIOSFixedListViewController;
import com.a2a.adjava.languages.ios.xmodele.controllers.MIOSListViewController;
import com.a2a.adjava.utils.FileTypeUtils;
import com.a2a.adjava.utils.JaxbUtils;
import com.a2a.adjava.xmodele.XProject;

/**
 * Controller generator
 * @author lmichenaud
 *
 */
public class ControllerGenerator extends AbstractIncrementalGenerator<MIOSDomain<MIOSDictionnary, MIOSModeleFactory>> {

	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(ControllerGenerator.class);
	
	/**
	 * Xsl template for controller interface
	 */
	private static final String CONTROLLER_INTERFACE_TEMPLATE = "controller-interface.xsl";
	
	/**
	 * Xsl template for controller implementation
	 */
	private static final String CONTROLLER_IMPL_TEMPLATE = "controller-impl.xsl";
	
	/**
	 * Package for controllers
	 */
	private static final String CONTROLLER_PACKAGE = "controller";
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.generators.ResourceGenerator#genere(com.a2a.adjava.xmodele.XProject, com.a2a.adjava.generators.DomainGeneratorContext)
	 */
	@Override
	public void genere(
			XProject<MIOSDomain<MIOSDictionnary, MIOSModeleFactory>> p_oMProject,
			DomainGeneratorContext p_oGeneratorContext) throws Exception {
		log.debug("> IOSControllerGenerator.genere");
		for( MIOSController oController : p_oMProject.getDomain().getDictionnary().getAllIOSControllers()) {
			log.info("for( MIOSController oController : " + oController.getName() + " hasCustomClass: " + oController.hasCustomClass());
			
			if ( oController.hasCustomClass() && 
					oController.getCustomClass().isDoGeneration() && 
					!oController.getControllerType().equals(MIOSControllerType.WORKSPACE) &&
					!oController.getControllerType().equals(MIOSControllerType.MULTIPANEL) ) {
				this.createControllerInterface( oController, p_oMProject, p_oGeneratorContext);
				this.createControllerImplementation( oController, p_oMProject, p_oGeneratorContext);
			}
		}
		log.debug("< IOSControllerGenerator.genere");
	}
	
	/**
	 * Generate controller interface
	 * @param p_xController xml of controller interface
	 * @param p_oMIOSController controller interface
	 * @param p_oMProject project
	 * @param p_oContext generator context
	 * @throws Exception
	 */
	protected void createControllerInterface( MIOSController p_oMIOSController,
			XProject<MIOSDomain<MIOSDictionnary, MIOSModeleFactory>> p_oMProject, DomainGeneratorContext p_oContext) throws Exception {

		String sFile = FileTypeUtils.computeFilenameForIOSInterface(CONTROLLER_PACKAGE, 
				p_oMIOSController.getCustomClass().getName(), p_oMProject.getSourceDir());

		Document xDoc = JaxbUtils.marshalToDocument(p_oMIOSController);
		Element rootElement = (Element) xDoc.getFirstChild();
		rootElement.setAttribute("main-project", p_oMProject.getDomain().getGlobalParameters().get("mainProject"));
		
		MIOSImportDelegate oMIOSImportDelegate = p_oMProject.getDomain().getXModeleFactory().createImportDelegate(this);
		this.computeImportForControllerInterface(oMIOSImportDelegate, p_oMIOSController, p_oMProject, p_oContext);
		xDoc.getDocumentElement().appendChild(xDoc.importNode(oMIOSImportDelegate.toDomXml().getDocumentElement(), true));
		
		log.debug("  generate file: {}", sFile);
		this.doIncrementalTransform(CONTROLLER_INTERFACE_TEMPLATE, sFile, xDoc, p_oMProject, p_oContext);
	}
	
	/**
	 * Compute import for controller interface
	 * @param p_oMIOSController controller
	 * @param p_oMProject project
	 * @param p_oContext context
	 * @throws Exception
	 */
	protected void computeImportForControllerInterface( MIOSImportDelegate p_oMIOSImportDelegate,
			MIOSController p_oMIOSController, XProject<MIOSDomain<MIOSDictionnary, MIOSModeleFactory>> p_oMProject,
			DomainGeneratorContext p_oContext) throws Exception {

	}

	/**
	 * Generate controller implementation
	 * @param p_xController xml of controller implementation
	 * @param p_oMIOSController controller implementation
	 * @param p_oMProject project
	 * @param p_oContext generator context
	 * @throws Exception
	 */
	protected void createControllerImplementation( MIOSController p_oMIOSController,
			XProject<MIOSDomain<MIOSDictionnary, MIOSModeleFactory>> p_oMProject, DomainGeneratorContext p_oContext) throws Exception {
		String sFile = FileTypeUtils.computeFilenameForIOSImpl(CONTROLLER_PACKAGE, 
				p_oMIOSController.getCustomClass().getName(), p_oMProject.getSourceDir());

		Document xDoc = JaxbUtils.marshalToDocument(p_oMIOSController);
		Element rootElement = (Element) xDoc.getFirstChild();
		rootElement.setAttribute("main-project", p_oMProject.getDomain().getGlobalParameters().get("mainProject"));
		
		MIOSImportDelegate oMIOSImportDelegate = p_oMProject.getDomain().getXModeleFactory().createImportDelegate(this);
		this.computeImportForControllerImpl(oMIOSImportDelegate, p_oMIOSController, p_oMProject, p_oContext);
		xDoc.getDocumentElement().appendChild(xDoc.importNode(oMIOSImportDelegate.toDomXml().getDocumentElement(), true));
		
		log.debug("  generate file: {}", sFile);
		this.doIncrementalTransform(CONTROLLER_IMPL_TEMPLATE, sFile, xDoc, p_oMProject, p_oContext);
	}
	
	
	/**
	 * Compute imports for controller implementation
	 * @param p_oMIOSController controller
	 * @param p_oMProject project
	 * @param p_oContext context
	 * @throws Exception
	 */
	protected void computeImportForControllerImpl( MIOSImportDelegate p_oMIOSImportDelegate,
			MIOSController p_oMIOSController, XProject<MIOSDomain<MIOSDictionnary, MIOSModeleFactory>> p_oMProject,
			DomainGeneratorContext p_oContext) throws Exception {
		
		p_oMIOSImportDelegate.addImport(MIOSImportDelegate.MIOSImportCategory.CONTROLLER.name(), p_oMIOSController.getName());
		p_oMIOSImportDelegate.addImport(MIOSImportDelegate.MIOSImportCategory.VIEWMODEL.name(), p_oMProject.getDomain().getDictionnary().getViewModelCreator().getName());
		
		if (p_oMIOSController.getControllerType() == MIOSControllerType.FIXEDLISTVIEW ) {
			MIOSFixedListViewController oMIOSController = (MIOSFixedListViewController)p_oMIOSController ;
			p_oMIOSImportDelegate.addImport(MIOSImportDelegate.MIOSImportCategory.VIEWMODEL.name(), oMIOSController.getItemViewModel() );
		}
		
		if ( p_oMIOSController.getControllerType().equals(MIOSControllerType.FORMVIEW)) {
			for( String sActionName : ((MIOSFormViewController)p_oMIOSController).getSaveActionNames()) {
				p_oMIOSImportDelegate.addImport(MIOSImportDelegate.MIOSImportCategory.ACTION.name(), sActionName );
			}
		}
		
		if ( p_oMIOSController.getControllerType().equals(MIOSControllerType.LISTVIEW) || 
				p_oMIOSController.getControllerType().equals(MIOSControllerType.LISTVIEW2D)) {
			if ( ((MIOSListViewController)p_oMIOSController).getDeleteAction() != null ) {
				p_oMIOSImportDelegate.addImport(MIOSImportDelegate.MIOSImportCategory.ACTION.name(),
					((MIOSListViewController)p_oMIOSController).getDeleteAction());
			}
		}
	}
}
