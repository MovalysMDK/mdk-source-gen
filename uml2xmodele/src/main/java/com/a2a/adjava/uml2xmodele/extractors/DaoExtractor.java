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
package com.a2a.adjava.uml2xmodele.extractors;

import java.beans.Introspector;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;

import com.a2a.adjava.types.ITypeDescription;
import com.a2a.adjava.uml.UmlAssociationClass;
import com.a2a.adjava.uml.UmlAssociationEnd.AggregateType;
import com.a2a.adjava.uml.UmlClass;
import com.a2a.adjava.uml.UmlModel;
import com.a2a.adjava.uml.UmlOperation;
import com.a2a.adjava.uml2xmodele.operations.UmlOperationNameParser;
import com.a2a.adjava.utils.FileTypeUtils;
import com.a2a.adjava.xmodele.MAssociation;
import com.a2a.adjava.xmodele.MAssociation.AssociationType;
import com.a2a.adjava.xmodele.MAssociationPersistableManyToMany;
import com.a2a.adjava.xmodele.MAssociationWithForeignKey;
import com.a2a.adjava.xmodele.MAttribute;
import com.a2a.adjava.xmodele.MDaoBeanRef;
import com.a2a.adjava.xmodele.MDaoImpl;
import com.a2a.adjava.xmodele.MDaoInterface;
import com.a2a.adjava.xmodele.MDaoMethodSignature;
import com.a2a.adjava.xmodele.MEntityImpl;
import com.a2a.adjava.xmodele.MIdentifierElem;
import com.a2a.adjava.xmodele.MJoinEntityImpl;
import com.a2a.adjava.xmodele.MMethodSignature;
import com.a2a.adjava.xmodele.MPackage;

/**
 * <p>
 * Dao Extractor
 * </p>
 * 
 * <p>
 * Copyright (c) 2011
 * <p>
 * Company: Adeuza
 * 
 * @author lmichenaud
 * 
 */

public class DaoExtractor extends AbstractExtractor {

	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(PojoExtractor.class);

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.uml2xmodele.extractors.MExtractor#initialize(org.dom4j.Element)
	 */
	@Override
	public void initialize( Element p_xConfig ) {
		
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.a2a.adjava.uml2xmodele.extractors.MExtractor#extract(com.a2a.adjava.uml.UmlModel)
	 */
	@Override
	public void extract(UmlModel p_oModele ) throws Exception {
		
		for (UmlClass oUmlClass : p_oModele.getDictionnary().getAllClasses()) {
			MEntityImpl oClass = getDomain().getDictionnary().getMapUmlClassToMClasses().get(oUmlClass.getFullName());
			if (oClass != null && !oClass.isTransient()) {
				// Extract Dao from classes
				MDaoImpl oDao = this.extractDaoFromClass(oUmlClass, oClass);
				this.extractDaoInterfaceFromDao(oDao, oClass);
			}
		}

		// Extract Dao from association classes
		for (UmlAssociationClass oUmlClass : p_oModele.getDictionnary().getAssociationClasses()) {
			MEntityImpl oClass = getDomain().getDictionnary().getMapUmlClassToMClasses().get(oUmlClass.getFullName());
				
			if (oClass != null && !oClass.isTransient()) {
				MDaoImpl oDao = this.extractDaoFromClass(oUmlClass, oClass);
				this.extractDaoInterfaceFromDao(oDao, oClass);
			}
		}

		// Extract dao from join classes
		for (MJoinEntityImpl oMJoinClass : getDomain().getDictionnary().getAllJoinClasses()) {
			MDaoImpl oDao = this.extractDaoFromJoinClass(oMJoinClass);
			this.extractDaoInterfaceFromDao(oDao, oMJoinClass);
		}

		// Add bean refs to Dao
		for (MDaoImpl oMDao : getDomain().getDictionnary().getAllDaos()) {
			addDaoRefsBeans(oMDao);
		}
		
		// Compute delete cascade
		for( MEntityImpl oEntity : getDomain().getDictionnary().getAllEntities()) {
			this.computeDeleteCascade(oEntity);
		}
	}

	/**
	 * @param p_oClass
	 * @param p_oPackageInterface
	 * @param p_oConfig
	 * @return
	 */
	public MDaoImpl extractDaoFromClass(UmlClass p_oUmlClass, MEntityImpl p_oClass) throws Exception {

		log.debug("> DaoExtractor.extractDao : {}", p_oClass.getName());
		MDaoImpl r_oDao = this.extractDao(p_oClass);
		this.extractOperations(p_oUmlClass, p_oClass, r_oDao);

		log.debug("< DaoExtractor.extractDao");

		return r_oDao;
	}

	/**
	 * @param p_oClass
	 * @param p_oPackageInterface
	 * @param p_oConfig
	 * @return
	 */
	public MDaoImpl extractDaoFromJoinClass(MJoinEntityImpl p_oClass) throws Exception {

		log.debug("> DaoExtractor.extractDaoFromJoinClass : {}", p_oClass.getName());

		MDaoImpl r_oDao = extractDao(p_oClass);
		
		// Lui ajoute les operations
		UmlOperationNameParser oUmlOperationNameParser = UmlOperationNameParser.getInstance();

		String sOldFk = null;
		StringBuilder sSelectOperationName = null;
		StringBuilder sDeleteOperationName = null;

		for (MAttribute oAttribute : p_oClass.getIdentifier().getElemOfTypeAttribute()) {
			if (sOldFk == null || !sOldFk.equals(oAttribute.getGeneratedFromAssocName())) {
				sOldFk = oAttribute.getGeneratedFromAssocName();

				if (sSelectOperationName != null) {
					for (MDaoMethodSignature oMDaoMethodSignature : oUmlOperationNameParser.parse(sSelectOperationName.toString(),
							p_oClass.getUmlName(), p_oClass, r_oDao.getPackage(), getDomain(), StringUtils.EMPTY)) {
						r_oDao.addMethodSignature(oMDaoMethodSignature);
					}
				}

				if (sDeleteOperationName != null) {
					for (MDaoMethodSignature oMDaoMethodSignature : oUmlOperationNameParser.parse(sDeleteOperationName.toString(),
							p_oClass.getUmlName(), p_oClass, r_oDao.getPackage(), this.getDomain(), StringUtils.EMPTY)) {
						r_oDao.addMethodSignature(oMDaoMethodSignature);
					}
				}

				sSelectOperationName = new StringBuilder("getList");
				sSelectOperationName.append(p_oClass.getUmlName());
				sSelectOperationName.append("_By");

				sDeleteOperationName = new StringBuilder("delete");
				sDeleteOperationName.append("_By");
			}
			sSelectOperationName.append('_');
			sSelectOperationName.append( StringUtils.capitalize(oAttribute.getName()));

			sDeleteOperationName.append('_');
			sDeleteOperationName.append( StringUtils.capitalize(oAttribute.getName()));
		}

		for (MDaoMethodSignature oMDaoMethodSignature : oUmlOperationNameParser.parse(sSelectOperationName.toString(), p_oClass.getUmlName(),
				p_oClass, r_oDao.getPackage(), this.getDomain(), StringUtils.EMPTY)) {
			r_oDao.addMethodSignature(oMDaoMethodSignature);
		}

		for (MDaoMethodSignature oMDaoMethodSignature : oUmlOperationNameParser.parse(sDeleteOperationName.toString(), p_oClass.getUmlName(),
				p_oClass, r_oDao.getPackage(), this.getDomain(), StringUtils.EMPTY)) {
			r_oDao.addMethodSignature(oMDaoMethodSignature);
		}

		r_oDao.addImport(p_oClass.getAssociation().getRefClass().getMasterInterface());
		r_oDao.addImport(p_oClass.getAssociation().getOppositeClass().getMasterInterface());
		r_oDao.addImport(p_oClass.getAssociation().getRefClass().getDao().getMasterInterface());
		r_oDao.addImport(p_oClass.getAssociation().getOppositeClass().getDao().getMasterInterface());
		r_oDao.addImport(p_oClass.getAssociation().getRefClass().getFactoryInterface());
		r_oDao.addImport(p_oClass.getAssociation().getOppositeClass().getFactoryInterface());
		log.debug("< DaoExtractor.extractDaoFromJoinClass");

		return r_oDao;
	}

	/**
	 * @param p_oClass
	 * @param p_oModelDictonnary
	 * @param p_oConfig
	 * @return
	 */
	private MDaoImpl extractDao(MEntityImpl p_oClass) {
		
		// Calcule le nom du Dao
		String sDaoName = this.getLngConfiguration().getDaoImplementationNamingPrefix()
				+ p_oClass.getName().substring(0, p_oClass.getName().length() - getLngConfiguration().getImplNamingSuffix().length())
				+ getLngConfiguration().getDaoImplementationNamingSuffix();
		
		String sDaoBeanName = p_oClass.getName().substring(0, p_oClass.getName().length() - getLngConfiguration().getImplNamingSuffix().length())
				+ getLngConfiguration().getDaoInterfaceNamingSuffix();
		sDaoBeanName = Introspector.decapitalize(sDaoBeanName);

		// Determine le package du dao
		MPackage oBasePackage = p_oClass.getPackage();
		if ( StringUtils.isNotEmpty(this.getLngConfiguration().getImplSubPackageName())) {
			oBasePackage = p_oClass.getPackage().getParent();
		}

		MPackage oPackageDao = oBasePackage;
		if ( StringUtils.isNotEmpty( this.getLngConfiguration().getDaoImplementationSubPackageName())) {
			oPackageDao = oBasePackage.getChildPackage( this.getLngConfiguration().getDaoImplementationSubPackageName());
			if (oPackageDao == null) {
				oPackageDao = new MPackage( this.getLngConfiguration().getDaoImplementationSubPackageName(), oBasePackage);
				oBasePackage.addPackage(oPackageDao);
			}
		}
		
		// Calcul le nom du Properties qui va contenir les requetes
		String sQueryDefinitionFile = FileTypeUtils.computeFilenameForJavaProperties(
				oPackageDao.getFullName(), p_oClass.getMasterInterface().getName().toLowerCase() + "-sql");
		
		MDaoImpl r_oDao = this.getDomain().getXModeleFactory().createDaoImpl(sDaoName, sDaoBeanName, 
			oPackageDao, p_oClass, p_oClass.getMasterInterface(), sQueryDefinitionFile);

		// Determine les imports a faire
		addImports(p_oClass, oPackageDao, r_oDao);

		log.debug("affect dao {} to class {}", r_oDao, p_oClass);
		p_oClass.setDao(r_oDao);

		getDomain().getDictionnary().registerDao(r_oDao);

		return r_oDao;
	}

	/**
	 * @param p_oUmlClass
	 * @param p_oClass
	 * @param p_oDao
	 * @param p_oProjectConfig
	 * @throws Exception
	 */
	private void extractOperations(UmlClass p_oUmlClass, MEntityImpl p_oClass, MDaoImpl p_oDao) throws Exception {
		// Lui ajoute les operations
		UmlOperationNameParser oUmlOperationNameParser = UmlOperationNameParser.getInstance();
		for (UmlOperation oOperation : p_oUmlClass.getOperations()) {
			for (MDaoMethodSignature oMethodSignature : oUmlOperationNameParser.parse(oOperation.getName(), p_oUmlClass.getName(), p_oClass,
					p_oDao.getPackage(), this.getDomain(), oOperation.getDocumentation())) {
				p_oDao.addMethodSignature(oMethodSignature);
			}
		}
	}

	/**
	 * @param dao
	 */
	public void addDaoRefsBeans(MDaoImpl p_oDao) {

		List<MAssociation> listAssociations = new ArrayList<MAssociation>();
		listAssociations.addAll(p_oDao.getMEntityImpl().getAssociations());
		listAssociations.addAll(p_oDao.getMEntityImpl().getIdentifier().getElemOfTypeAssociation());

		// Lui ajoute les beans ref
		for (MAssociation oAssociation : listAssociations) {
			if ( !oAssociation.getRefClass().isTransient() && !oAssociation.isTransient()) {
				MDaoImpl oRefDao = oAssociation.getRefClass().getDao();
	
				if (oRefDao != p_oDao) {
					addDaoBeanRef(p_oDao, oRefDao.getMasterInterface());
				}
			}
		}

		// Cas particulier des join class
		if (p_oDao.getMEntityImpl() instanceof MJoinEntityImpl) {
			MJoinEntityImpl oMJoinClass = (MJoinEntityImpl) p_oDao.getMEntityImpl();
			addDaoBeanRef(oMJoinClass.getAssociation().getRefClass().getDao(), p_oDao.getMasterInterface());
			if (oMJoinClass.getOppositeAssociation() != null) {
				addDaoBeanRef(oMJoinClass.getOppositeAssociation().getRefClass().getDao(), p_oDao.getMasterInterface());
			}
		}
	}

	/**
	 * @param p_oSourceDao
	 * @param p_oRefDao
	 * @param p_oConfig
	 */
	private void addDaoBeanRef(MDaoImpl p_oSourceDao, MDaoInterface p_oRefDaoInterface) {
		String sName = p_oRefDaoInterface.getName();
		ITypeDescription oObjectTypeDescription = this.getLngConfiguration().getTypeDescription("Object");
		String sParameterName = oObjectTypeDescription.getParameterPrefix() + sName;

		sName = sName.substring(0, 1).toLowerCase() + sName.substring(1);

		ITypeDescription oTypeDescription = (ITypeDescription) oObjectTypeDescription.clone();
		oTypeDescription.setName(p_oRefDaoInterface.getFullName());

		MDaoBeanRef oDaoBeanRef = new MDaoBeanRef(sName, oTypeDescription, sParameterName, p_oRefDaoInterface);
		p_oSourceDao.addDaoBeanRef(oDaoBeanRef);
		p_oSourceDao.getMasterInterface().addImport(oTypeDescription.getName());
	}

	/**
	 * @param p_oClass
	 * @param p_oPackageDao
	 * @param p_oDao
	 */
	private void addImports(MEntityImpl p_oClass, MPackage p_oPackageDao, MDaoImpl p_oDao) {
		if (p_oClass.getPackage() != p_oPackageDao ) {
			p_oDao.addImport(p_oClass.getFactoryInterface());
		}

		if (p_oClass.getMasterInterface() != null && p_oClass.getMasterInterface().getPackage() != p_oPackageDao) {
			p_oDao.addImport(p_oClass.getMasterInterface());
			p_oDao.addImport(p_oClass.getFactoryInterface());
		}

		for (MIdentifierElem oMIdentifierElem : p_oClass.getIdentifier().getElems()) {
			if (oMIdentifierElem instanceof MAttribute) {
				MAttribute oAttribute = (MAttribute) oMIdentifierElem;
				if (oAttribute.getTypeDesc().isPrimitif()) {
					p_oDao.addImport(oAttribute.getTypeDesc().getWrapper().getName());
				}
				if (oAttribute.getTypeDesc().isEnumeration()) {
					p_oDao.addImport(oAttribute.getTypeDesc().getName());
				}
				if (!oAttribute.getTypeDesc().isPrimitif()) {
					p_oDao.addImport(oAttribute.getTypeDesc().getName());
				}
			} else if (oMIdentifierElem instanceof MAssociationWithForeignKey) {
				MAssociationWithForeignKey oMAssociationWithForeignKey = (MAssociationWithForeignKey) oMIdentifierElem;
				p_oDao.addImport(oMAssociationWithForeignKey.getRefClass().getMasterInterface());
				p_oDao.addImport(oMAssociationWithForeignKey.getRefClass().getFactoryInterface());
			}
		}

		for (MAttribute oAttribute : p_oClass.getAttributes()) {
			ITypeDescription oTypeDesc = oAttribute.getTypeDesc();
			if (!oTypeDesc.isPrimitif()) {
				p_oDao.addImport(oAttribute.getTypeDesc().getName());
			}
		}

		for (MAssociation oMAssociation : p_oClass.getAssociations()) {
			if ( !oMAssociation.isTransient() && !oMAssociation.getRefClass().isTransient()) {
				if (oMAssociation.getAssociationType() == AssociationType.ONE_TO_MANY) {
					p_oDao.addImport(oMAssociation.getRefClass().getMasterInterface());
					p_oDao.addImport(oMAssociation.getRefClass().getFactoryInterface());
				}
				if (oMAssociation.getAssociationType() == AssociationType.MANY_TO_MANY && !oMAssociation.isTransient()) {
					MAssociationPersistableManyToMany oMAssociationManyToMany = (MAssociationPersistableManyToMany) oMAssociation;
					p_oDao.addImport(oMAssociation.getRefClass().getMasterInterface());
					p_oDao.addImport(oMAssociationManyToMany.getJoinClass().getMasterInterface());
					p_oDao.addImport(oMAssociation.getRefClass().getFactoryInterface());
					p_oDao.addImport(oMAssociationManyToMany.getJoinClass().getFactoryInterface());
				}
				if (oMAssociation instanceof MAssociationWithForeignKey) {
					// p_oDao.addImport( oMAssociation.getRefClass().getFullName());
					p_oDao.addImport(oMAssociation.getRefClass().getMasterInterface());
					p_oDao.addImport(oMAssociation.getRefClass().getFactoryInterface());
				}
			}
		}

		for (MAssociation oMAssociation : p_oClass.getNonNavigableAssociations()) {
			if (oMAssociation.getAssociationType() == AssociationType.MANY_TO_MANY &&
				!oMAssociation.isTransient() &&
				!oMAssociation.getRefClass().isTransient() ) {
				MAssociationPersistableManyToMany oMAssociationManyToMany = (MAssociationPersistableManyToMany) oMAssociation;
				p_oDao.addImport(oMAssociation.getRefClass().getMasterInterface());
				p_oDao.addImport(oMAssociationManyToMany.getJoinClass().getMasterInterface());
				p_oDao.addImport(oMAssociation.getRefClass().getFactoryInterface());
				p_oDao.addImport(oMAssociationManyToMany.getJoinClass().getFactoryInterface());
			}
		}

		for (MMethodSignature oMethodSignature : p_oDao.getMethodSignatures()) {
			if (!oMethodSignature.getReturnedType().isPrimitif()) {
				p_oDao.addImport(oMethodSignature.getReturnedType().getName());
			}
		}
	}

	/**
	 * @param p_oClass
	 * @param p_oPackageInterface
	 * @param p_oConfig
	 * @return
	 */
	public MDaoInterface extractDaoInterfaceFromDao(MDaoImpl p_oDao, MEntityImpl p_oClass) {
		
		// calcul le package de l'interface
		MPackage oBasePackage = p_oDao.getPackage();
		if ( StringUtils.isNotEmpty( this.getLngConfiguration().getImplSubPackageName())) {
			oBasePackage = p_oDao.getPackage().getParent();
		}

		MPackage oDaoInterfacePackage = oBasePackage;
		if ( StringUtils.isNotEmpty( this.getLngConfiguration().getDaoInterfaceSubPackageName())) {
			oDaoInterfacePackage = oBasePackage.getChildPackage(getLngConfiguration().getDaoInterfaceSubPackageName());
			if (oDaoInterfacePackage == null) {
				oDaoInterfacePackage = new MPackage(getLngConfiguration().getDaoInterfaceSubPackageName(), oBasePackage);
				oBasePackage.addPackage(oDaoInterfacePackage);
			}
		}

		// calcul le nom de l 'interface
		String sInterfaceName = getLngConfiguration().getDaoInterfaceNamingPrefix() + p_oDao.getMEntityImpl().getEntityName()
				+ getLngConfiguration().getDaoInterfaceNamingSuffix();

		MDaoInterface r_oDaoInterface = this.getDomain().getXModeleFactory().createDaoInterface(sInterfaceName, p_oDao.getBeanName(), oDaoInterfacePackage,
				p_oDao, p_oClass);
		addImports(p_oClass, oDaoInterfacePackage, r_oDaoInterface, p_oDao);
		p_oDao.setMasterInterface(r_oDaoInterface);
		
		this.getDomain().getDictionnary().registerDaoItf(r_oDaoInterface, p_oClass.getMasterInterface());

		return r_oDaoInterface;
	}

	
	/**
	 * @param p_oClass
	 * @param p_oDao
	 */
	private void computeDeleteCascade( MEntityImpl p_oClass ) {
		this.computeDeleteCascade(p_oClass, p_oClass, new ArrayList<MAssociation>());
	}
	
	/**
	 * Compute delete cascade
	 * @param p_oSourceClass
	 * @param p_oAnalyzedClass
	 * @param p_listTreadtedAssociations
	 */
	private void computeDeleteCascade( MEntityImpl p_oSourceClass, MEntityImpl p_oAnalyzedClass, List<MAssociation> p_listTreadtedAssociations ) {
		
		if ( !p_oAnalyzedClass.isTransient()) {
			for( MAssociation oAssociation : p_oAnalyzedClass.getAssociations()) {
				if ( oAssociation.getOppositeAggregateType() == AggregateType.COMPOSITE &&
					 !oAssociation.getRefClass().isTransient() &&
					 !oAssociation.isTransient() &&
					 !p_listTreadtedAssociations.contains(oAssociation)) {
					
					p_oSourceClass.getDao().addToDeleteCascade(oAssociation);
					p_listTreadtedAssociations.add(oAssociation);
					
					if ( p_oAnalyzedClass != oAssociation.getRefClass()) {
						this.computeDeleteCascade( p_oSourceClass, oAssociation.getRefClass(), p_listTreadtedAssociations);
					}
				}
			}
		}
	}
	
	/**
	 * @param p_oClass
	 * @param p_oPackageDao
	 * @param p_oDao
	 */
	private void addImports(MEntityImpl p_oClass, MPackage p_oPackageDao, MDaoInterface p_oDaoInterface, MDaoImpl p_oDao) {

		p_oDaoInterface.addImport(p_oClass.getMasterInterface());

		if (p_oClass.getMasterInterface() != null && p_oClass.getMasterInterface().getPackage() != p_oPackageDao) {
			p_oDaoInterface.addImport(p_oClass.getMasterInterface());
		}

		for (MIdentifierElem oMIdentifierElem : p_oClass.getIdentifier().getElems()) {
			if (oMIdentifierElem instanceof MAttribute) {
				MAttribute oAttribute = (MAttribute) oMIdentifierElem;
				if (oAttribute.getTypeDesc().isPrimitif()) {
					p_oDaoInterface.addImport(oAttribute.getTypeDesc().getWrapper().getName());
				}
				if (oAttribute.getTypeDesc().isEnumeration()) {
					p_oDaoInterface.addImport(oAttribute.getTypeDesc().getName());
				}
				if (!oAttribute.getTypeDesc().isPrimitif()) {
					p_oDaoInterface.addImport(oAttribute.getTypeDesc().getName());
				}
			} else if (oMIdentifierElem instanceof MAssociationWithForeignKey) {
				MAssociationWithForeignKey oMAssociationWithForeignKey = (MAssociationWithForeignKey) oMIdentifierElem;
				p_oDaoInterface.addImport(oMAssociationWithForeignKey.getRefClass());
			}
		}

		for (MAttribute oAttribute : p_oClass.getAttributes()) {
			ITypeDescription oTypeDesc = oAttribute.getTypeDesc();
			if (oTypeDesc.isEnumeration()) {
				p_oDaoInterface.addImport(oAttribute.getTypeDesc().getName());
			}
		}

		for (MAssociation oMAssociation : p_oClass.getAssociations()) {
			if ( !oMAssociation.isTransient()) {
				if (oMAssociation.getAssociationType() == AssociationType.ONE_TO_MANY) {
					p_oDaoInterface.addImport(oMAssociation.getRefClass().getMasterInterface());
				}
				if (oMAssociation.getAssociationType() == AssociationType.MANY_TO_MANY && !oMAssociation.isTransient()) {
					MAssociationPersistableManyToMany oMAssociationManyToMany = (MAssociationPersistableManyToMany) oMAssociation;
					p_oDaoInterface.addImport(oMAssociation.getRefClass().getMasterInterface());
					p_oDaoInterface.addImport(oMAssociationManyToMany.getJoinClass().getMasterInterface());
				}
				if (oMAssociation instanceof MAssociationWithForeignKey) {
					p_oDaoInterface.addImport(oMAssociation.getRefClass().getMasterInterface());
				}
			}
		}

		for (MAssociation oMAssociation : p_oClass.getNonNavigableAssociations()) {
			if (oMAssociation.getAssociationType() == AssociationType.MANY_TO_MANY && !oMAssociation.isTransient()) {
				MAssociationPersistableManyToMany oMAssociationManyToMany = (MAssociationPersistableManyToMany) oMAssociation;
				p_oDaoInterface.addImport(oMAssociation.getRefClass().getMasterInterface());
				p_oDaoInterface.addImport(oMAssociationManyToMany.getJoinClass().getMasterInterface());
			}
		}

		for (MDaoMethodSignature oMethodeSignature : p_oDao.getMethodSignatures()) {
			for (String sImport : oMethodeSignature.getNeededImports()) {
				p_oDaoInterface.addImport(sImport);
			}
		}

		if (p_oClass instanceof MJoinEntityImpl) {
			MJoinEntityImpl oMJoinClass = (MJoinEntityImpl) p_oClass;
			p_oDaoInterface.addImport(oMJoinClass.getAssociation().getRefClass().getMasterInterface());
			p_oDaoInterface.addImport(oMJoinClass.getAssociation().getOppositeClass().getMasterInterface());
			p_oDaoInterface.addImport(oMJoinClass.getAssociation().getRefClass().getDao().getMasterInterface());
			p_oDaoInterface.addImport(oMJoinClass.getAssociation().getOppositeClass().getDao().getMasterInterface());
		}
	}
}
