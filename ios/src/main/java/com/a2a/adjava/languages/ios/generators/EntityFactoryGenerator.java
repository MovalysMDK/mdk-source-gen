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
import com.a2a.adjava.languages.ios.xmodele.MIOSImportDelegate;
import com.a2a.adjava.languages.ios.xmodele.MIOSModeleFactory;
import com.a2a.adjava.utils.Chrono;
import com.a2a.adjava.utils.FileTypeUtils;
import com.a2a.adjava.xmodele.IModelDictionary;
import com.a2a.adjava.xmodele.MAssociation;
import com.a2a.adjava.xmodele.MAssociation.AssociationType;
import com.a2a.adjava.xmodele.MEntityImpl;
import com.a2a.adjava.xmodele.XProject;

/**
 * Generator for factories of entities
 * @author lmichenaud
 *
 */
public class EntityFactoryGenerator extends AbstractIncrementalGenerator<MIOSDomain<MIOSDictionnary, MIOSModeleFactory>> {

	/**
	 * Logger for this class
	 */
	private static final Logger log = LoggerFactory.getLogger(EntityFactoryGenerator.class);
	
	/**
	 * Xsl template for factory interface of entities
	 */
	private static final String ENTITYFACTORY_INTERFACE_XSL = "entity-factory-interface.xsl";
	
	/**
	 * Xsl template for factory implementations of entities
	 */
	private static final String ENTITYFACTORY_IMPL_XSL = "entity-factory-impl.xsl";
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void genere(XProject<MIOSDomain<MIOSDictionnary, MIOSModeleFactory>> p_oProject, DomainGeneratorContext p_oContext) throws Exception {
		
		log.debug("> EntityFactoryGenerator.genere");
		
		Chrono oChrono = new Chrono(true);
		IModelDictionary oDictionnary = p_oProject.getDomain().getDictionnary();
		
		for (MEntityImpl oEntity : oDictionnary.getAllEntities()) {
			
			Element xEntity = oEntity.toXml();
			xEntity.addAttribute("main-project", p_oProject.getDomain().getGlobalParameters().get("mainProject"));
			
			Document xFactoryInterface = this.createFactoryInterfaceElement(oEntity, xEntity, p_oProject);
			this.createFactoryInterface(oEntity, xFactoryInterface, p_oProject, p_oContext);
			
			Document xFactoryImpl = this.createFactoryImplElement(oEntity, xEntity, p_oProject);
			this.createFactoryImpl(oEntity, xFactoryImpl, p_oProject, p_oContext);
		}
		
		log.debug("< EntityFactoryGenerator.genere: {}", oChrono.stopAndDisplay());
	}
	
	
	/**
	 * Create the xml of the interface of the entity factory
	 * @param p_oEntity entity
	 * @param p_xEntity entity node
	 * @param p_oProject project
	 * @return xml
	 */
	public Document createFactoryInterfaceElement( MEntityImpl p_oEntity, Element p_xEntity, XProject<MIOSDomain<MIOSDictionnary, MIOSModeleFactory>> p_oProject ) {
		
		Element xFactory = DocumentHelper.createElement("factory");
		xFactory.add(p_xEntity);
		MIOSImportDelegate oImportDlg = new MIOSImportDelegate(this);
		oImportDlg.addImport(MIOSImportDelegate.MIOSImportCategory.ENTITIES.name(), p_oEntity.getName());
		xFactory.add(oImportDlg.toXml());
		
		return DocumentHelper.createDocument(xFactory);
	}
	
	/**
	 * Create the xml of the implementation of the entity factory
	 * @param p_oEntity entity
	 * @param p_xEntity entity node
	 * @param p_oProject project
	 * @return xml
	 */
	public Document createFactoryImplElement( MEntityImpl p_oEntity, Element p_xEntity, XProject<MIOSDomain<MIOSDictionnary, MIOSModeleFactory>> p_oProject ) {
		
		p_xEntity.detach();
		
		Element xFactory = DocumentHelper.createElement("factory");
		xFactory.add(p_xEntity);
		MIOSImportDelegate oImportDlg = new MIOSImportDelegate(this);
		oImportDlg.addImport(MIOSImportDelegate.MIOSImportCategory.FACTORIES.name(), p_oEntity.getName() + "+Factory");
		
		if ( !p_oEntity.isTransient()) {
			oImportDlg.addImport(MIOSImportDelegate.MIOSImportCategory.DAO.name(), p_oEntity.getName() + "+Dao");
		}
		
		for( MAssociation oAssociation : p_oEntity.getAssociations()) {
			if ( !oAssociation.isSelfRef() 
				&& (oAssociation.getAssociationType().equals(AssociationType.MANY_TO_ONE) 
				|| oAssociation.getAssociationType().equals(AssociationType.ONE_TO_ONE) 
				|| oAssociation.getAssociationType().equals(AssociationType.MANY_TO_MANY))) {
				oImportDlg.addImport(MIOSImportDelegate.MIOSImportCategory.ENTITIES.name(), oAssociation.getRefClass().getName());
				if ( !oAssociation.getRefClass().isTransient()) {
					oImportDlg.addImport(MIOSImportDelegate.MIOSImportCategory.DAO.name(), oAssociation.getRefClass().getName() + "+Dao");
				}
			}
		}
		
		xFactory.add(oImportDlg.toXml());
		
		return DocumentHelper.createDocument(xFactory);
	}
	
	/**
	 * Genere interface of entity factory
	 * @param p_oEntity entity class
	 * @param p_xEntity entity node
	 * @param p_oMProject project
	 * @param p_oContext generator context
	 * @throws Exception generation failure
	 */
	private void createFactoryInterface( MEntityImpl p_oEntity, Document p_xEntity,
			XProject<MIOSDomain<MIOSDictionnary, MIOSModeleFactory>> p_oMProject, DomainGeneratorContext p_oContext) throws Exception {

		String sInterfaceFile = FileTypeUtils.computeFilenameForIOSInterface(
			"model/factory", p_oEntity.getName() + "+Factory", p_oMProject.getSourceDir());
		
		log.debug("  generate file: {}", sInterfaceFile);
		this.doIncrementalTransform(ENTITYFACTORY_INTERFACE_XSL, sInterfaceFile, p_xEntity, p_oMProject, p_oContext);
	}
	
	/**
	 * Genere implementation of entity factory
	 * @param p_oEntity entity class
	 * @param p_xEntity entity node
	 * @param p_oMProject project
	 * @param p_oContext generator context
	 * @throws Exception generation failure
	 */
	private void createFactoryImpl( MEntityImpl p_oEntity, Document p_xEntity,
			XProject<MIOSDomain<MIOSDictionnary, MIOSModeleFactory>> p_oMProject, DomainGeneratorContext p_oContext) throws Exception {

		String sImplFile = FileTypeUtils.computeFilenameForIOSImpl(
			"model/factory", p_oEntity.getName() + "+Factory", p_oMProject.getSourceDir());
		
		log.debug("  generate file: {}", sImplFile);
		this.doIncrementalTransform(ENTITYFACTORY_IMPL_XSL, sImplFile, p_xEntity, p_oMProject, p_oContext);
	}
}
