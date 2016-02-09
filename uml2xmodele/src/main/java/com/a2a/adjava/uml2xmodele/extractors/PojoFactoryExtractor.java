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

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;

import com.a2a.adjava.uml.UmlModel;
import com.a2a.adjava.xmodele.IDomain;
import com.a2a.adjava.xmodele.IModelDictionary;
import com.a2a.adjava.xmodele.IModelFactory;
import com.a2a.adjava.xmodele.MEntityImpl;
import com.a2a.adjava.xmodele.MFactory;
import com.a2a.adjava.xmodele.MFactoryInterface;
import com.a2a.adjava.xmodele.MJoinEntityImpl;
import com.a2a.adjava.xmodele.MPackage;

/**
 * <p>PojoFactoryExtractor</p>
 *
 * <p>Copyright (c) 2011
 * <p>Company: Adeuza
 *
 * @author lmichenaud
 *
 */

public class PojoFactoryExtractor extends AbstractExtractor<IDomain<? extends IModelDictionary,? extends IModelFactory>> {

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.uml2xmodele.extractors.MExtractor#initialize()
	 */
	@Override
	public void initialize( Element p_xConfig ) {
		
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.uml2xmodele.extractors.MExtractor#extract(com.a2a.adjava.uml.UmlModel)
	 */
	@Override
	public void extract(UmlModel p_oModele) throws Exception {
		
		for( IDomain<IModelDictionary, IModelFactory> oDomain : getDomainRegistry().getDomains()) {
			for( MEntityImpl oClass : oDomain.getDictionnary().getAllEntities()) {
				extractFactoryFromClass( oClass);
				extractFactoryInterfaceFromClass( oClass);
			}
			
			for (MJoinEntityImpl oMJoinClass : oDomain.getDictionnary().getAllJoinClasses()) {
				extractFactoryFromClass(oMJoinClass);
				extractFactoryInterfaceFromClass(oMJoinClass);
			}
		}
	}

	/**
	 * @param p_oClass
	 * @param p_oPackageInterface
	 * @param p_oConfig
	 * @return
	 */
	private MFactory extractFactoryFromClass(MEntityImpl p_oClass) {
		
		// calcul le nom de la factory
		StringBuilder sFactoryImplementationName = new StringBuilder();
		if ( StringUtils.isNotEmpty(getLngConfiguration().getPojoFactoryImplNamingPrefix())) {
			sFactoryImplementationName.append(getLngConfiguration().getPojoFactoryImplNamingPrefix());
		}
		
		sFactoryImplementationName.append(p_oClass.getEntityName());
		
		if ( StringUtils.isNotEmpty(getLngConfiguration().getPojoFactoryImplNamingSuffix())) {
			sFactoryImplementationName.append(getLngConfiguration().getPojoFactoryImplNamingSuffix());
		}
		
		// Détermine le package de la factory
		MPackage oBasePackage = p_oClass.getMasterInterface().getPackage();
		if ( getLngConfiguration().getImplSubPackageName() != null && getLngConfiguration().getImplNamingSuffix().length() > 0 ) {
			oBasePackage = p_oClass.getMasterInterface().getPackage().getParent();
		}
		
		MPackage oPackageFactory = oBasePackage ;
		if ( getLngConfiguration().getPojoFactoryImplSubPackageName() != null && getLngConfiguration().getPojoFactoryImplSubPackageName().length() > 0) {
			oPackageFactory = oBasePackage.getChildPackage( getLngConfiguration().getPojoFactoryImplSubPackageName());
			if (oPackageFactory == null) {
				oPackageFactory = new MPackage( getLngConfiguration().getPojoFactoryImplSubPackageName(), oBasePackage);
				oBasePackage.addPackage(oPackageFactory);
			}
		}
		
		MFactory r_oFactory = new MFactory(sFactoryImplementationName.toString(),oPackageFactory,p_oClass.getMasterInterface(),p_oClass);
		p_oClass.setFactory(r_oFactory);
		
		return r_oFactory;
	}
	
	/**
	 * @param p_oClass
	 * @param p_oPackageInterface
	 * @param p_oConfig
	 * @return
	 */
	private MFactoryInterface extractFactoryInterfaceFromClass(MEntityImpl p_oClass) {
		
		// calcul le nom de l'interface de la factory
		StringBuilder sFactoryInterfaceName = new StringBuilder();
		if ( StringUtils.isNotEmpty(getLngConfiguration().getPojoFactoryInterfaceNamingPrefix())) {
			sFactoryInterfaceName.append(getLngConfiguration().getPojoFactoryInterfaceNamingPrefix());
		}
		
		sFactoryInterfaceName.append(p_oClass.getEntityName());
		
		if ( StringUtils.isNotEmpty(getLngConfiguration().getPojoFactoryInterfaceNamingSuffix())) {
			sFactoryInterfaceName.append(getLngConfiguration().getPojoFactoryInterfaceNamingSuffix());
		}

		// Détermine le package de l'interface de la factory
		MPackage oBasePackage = p_oClass.getMasterInterface().getPackage();
		if (getLngConfiguration().getImplSubPackageName() != null && getLngConfiguration().getImplNamingSuffix().length() > 0) {
			oBasePackage = p_oClass.getMasterInterface().getPackage().getParent();
		}

		MPackage oPackageFactoryInterface = oBasePackage;
		if (getLngConfiguration().getPojoFactoryInterfaceSubPackageName() != null
				&& getLngConfiguration().getPojoFactoryInterfaceSubPackageName().length() > 0) {
			oPackageFactoryInterface = oBasePackage.getChildPackage(getLngConfiguration().getPojoFactoryInterfaceSubPackageName());
			if (oPackageFactoryInterface == null) {
				oPackageFactoryInterface = new MPackage(getLngConfiguration().getPojoFactoryInterfaceSubPackageName(), oBasePackage);
				oBasePackage.addPackage(oPackageFactoryInterface);
			}
		}

		MFactoryInterface r_oFactoryInterface = new MFactoryInterface(sFactoryInterfaceName.toString(), oPackageFactoryInterface,
				p_oClass.getMasterInterface(), p_oClass);
		p_oClass.setFactoryInterface(r_oFactoryInterface);
		p_oClass.getFactory().setMasterInterface(r_oFactoryInterface);

		return r_oFactoryInterface;
	}
}
