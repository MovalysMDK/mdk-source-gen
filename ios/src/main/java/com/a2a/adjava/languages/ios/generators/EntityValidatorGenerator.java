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

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.generator.core.incremental.AbstractIncrementalGenerator;
import com.a2a.adjava.generators.DomainGeneratorContext;
import com.a2a.adjava.languages.ios.xmodele.MIOSImportDelegate;
import com.a2a.adjava.languages.ios.xmodele.MIOSImportDelegate.MIOSImportCategory;
import com.a2a.adjava.utils.Chrono;
import com.a2a.adjava.utils.FileTypeUtils;
import com.a2a.adjava.xmodele.IDomain;
import com.a2a.adjava.xmodele.IModelDictionary;
import com.a2a.adjava.xmodele.IModelFactory;
import com.a2a.adjava.xmodele.MAssociation;
import com.a2a.adjava.xmodele.MAssociation.AssociationType;
import com.a2a.adjava.xmodele.MEntityImpl;
import com.a2a.adjava.xmodele.XProject;

/**
 * 
 * <p>
 * Generation of entity validators
 * </p>
 * 
 * <p>
 * Copyright (c) 2013
 * <p>
 * Company: Adeuza
 * 
 * @author lmichenaud
 * 
 */
public class EntityValidatorGenerator extends
		AbstractIncrementalGenerator<IDomain<IModelDictionary, IModelFactory>> {

	/**
	 * Logger for this class
	 */
	private static final Logger log = LoggerFactory
			.getLogger(EntityValidatorGenerator.class);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void genere(
			XProject<IDomain<IModelDictionary, IModelFactory>> p_oProject,
			DomainGeneratorContext p_oContext) throws Exception {
		log.debug("> EntityValidator.genere");
		Chrono oChrono = new Chrono(true);
		IModelDictionary oDictionnary = p_oProject.getDomain().getDictionnary();

		for (MEntityImpl oEntity : oDictionnary.getAllEntities()) {
			this.createEntityValidator(oEntity, p_oProject, p_oContext);
		}

		log.debug("< EntityValidator.genere: {}", oChrono.stopAndDisplay());
	}

	/**
	 * Generation of model validators
	 * 
	 * @param p_oEntity
	 *            validator
	 * @param p_oProject
	 *            project
	 * @param p_oContext
	 *            generation context
	 * @throws Exception
	 *             failure
	 */
	private void createEntityValidator(MEntityImpl p_oEntity,
			XProject<IDomain<IModelDictionary, IModelFactory>> p_oProject,
			DomainGeneratorContext p_oContext) throws Exception {
		this.createEntityValidatorInterface(p_oEntity, p_oProject, p_oContext);
		this.createEntityValidatorImpl(p_oEntity, p_oProject, p_oContext);
	}

	/**
	 * Create interface for entity validator
	 * @param p_oEntity entity
	 * @param p_oProject project
	 * @param p_oContext context
	 * @throws Exception
	 */
	protected void createEntityValidatorInterface(MEntityImpl p_oEntity,
			XProject<IDomain<IModelDictionary, IModelFactory>> p_oProject,
			DomainGeneratorContext p_oContext) throws Exception {
		Element xValidator = DocumentHelper.createElement("validator");
		
		Element xEntity = p_oEntity.toXml();
		xEntity.addAttribute("main-project", p_oProject.getDomain().getGlobalParameters().get("mainProject"));
		xValidator.add(xEntity);

		MIOSImportDelegate oImportDlg = new MIOSImportDelegate(this);
		oImportDlg.addImport(MIOSImportDelegate.MIOSImportCategory.ENTITIES.name(), p_oEntity.getName());
		xValidator.add(oImportDlg.toXml());
		
		String sValidatorInterfaceFile = FileTypeUtils.computeFilenameForIOSInterface("model/validator", 
				p_oEntity.getName() + "+Validate", p_oProject.getSourceDir());
		
		log.debug("  generate file: {}", sValidatorInterfaceFile);
		this.doIncrementalTransform("entity-validator-interface.xsl",
				sValidatorInterfaceFile, DocumentHelper.createDocument(xValidator), p_oProject, p_oContext);
	}

	/**
	 * Crate implementation for entity validator
	 * @param p_oEntity entity
	 * @param p_oProject project
	 * @param p_oContext context
	 * @throws Exception
	 */
	protected void createEntityValidatorImpl(MEntityImpl p_oEntity,
			XProject<IDomain<IModelDictionary, IModelFactory>> p_oProject,
			DomainGeneratorContext p_oContext) throws Exception {
		
		Element xValidator = DocumentHelper.createElement("validator");
		
		Element xEntity = p_oEntity.toXml();
		xEntity.addAttribute("main-project", p_oProject.getDomain().getGlobalParameters().get("mainProject"));
		xValidator.add(xEntity);

		MIOSImportDelegate oImportDlg = new MIOSImportDelegate(this);
		oImportDlg.addImport(MIOSImportDelegate.MIOSImportCategory.ENTITIES.name(), p_oEntity.getName());
		if ( !p_oEntity.isTransient()) {
			oImportDlg.addImport(MIOSImportDelegate.MIOSImportCategory.DAO.name(), p_oEntity.getName() + "+Dao");
		}
		
		for( MAssociation oAssociation : p_oEntity.getAssociations()) {
			if ( !p_oEntity.isTransient() && !oAssociation.isSelfRef() && (oAssociation.getAssociationType().equals(AssociationType.MANY_TO_ONE) 
				|| oAssociation.getAssociationType().equals(AssociationType.ONE_TO_ONE) 
				|| oAssociation.getAssociationType().equals(AssociationType.MANY_TO_MANY))) {
				oImportDlg.addImport(MIOSImportCategory.ENTITIES.name(), oAssociation.getRefClass().getName());
			}
		}
		
		xValidator.add(oImportDlg.toXml());
		
		String sValidatorImplFile = FileTypeUtils.computeFilenameForIOSImpl(
				"model/validator", p_oEntity.getName() + "+Validate",
				p_oProject.getSourceDir());

		log.debug("  generate file: {}", sValidatorImplFile);
		this.doIncrementalTransform("entity-validator-impl.xsl",
				sValidatorImplFile, DocumentHelper.createDocument(xValidator), p_oProject, p_oContext);
	}
}
