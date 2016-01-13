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

import com.a2a.adjava.uml2xmodele.extractors.viewmodel.MViewModelFactory;
import com.a2a.adjava.xmodele.*;
import org.dom4j.Element;

import com.a2a.adjava.uml.UmlModel;
import com.a2a.adjava.utils.StrUtils;

/**
 * Viewmodel extractor
 * @author lmichenaud
 *
 */
public class ViewModelCreatorExtractor extends AbstractExtractor<IDomain<IModelDictionary,IModelFactory>>{

	/**
	 * Parameter for name of viewmodel creator
	 */
	private static final String VMC_NAME_PARAMETER = "vmc-name";

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void extract(UmlModel p_oModele) throws Exception {
		// Create view model creator
		this.createViewModelCreator();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initialize(Element p_xConfig) throws Exception {
		
	}
	
	/**
	 * Create view model creator
	 */
	private void createViewModelCreator() {
		MPackage oVMCreatorPackage = this.computeVMCreatorPackage();
		
		String sVmcName = this.getParameters().getValue(VMC_NAME_PARAMETER);
		MViewModelCreator oVmc = this.getDomain().getXModeleFactory().createViewModelCreator(sVmcName, oVMCreatorPackage);
		this.getDomain().getDictionnary().registerViewModelCreator(oVmc);
	}
	
	/**
	 * Génère le package ou est présent le <em>ViewModelCreator</em> de
	 * l'application.
	 * @return Un objet de type <em>MPackage</em>
	 */
	private MPackage computeVMCreatorPackage() {

		String sMasterPackageName = this.getDomain().getRootPackage();

		String sViewModelSubPackage = this.getLngConfiguration().getViewModelImplementationSubPackageName();

		StringBuilder oPackageNameVMCreatorBuilder = new StringBuilder(sMasterPackageName).append(StrUtils.DOT)
				.append(sViewModelSubPackage);

		MPackage oPackageBaseVMCreator = getDomain().getDictionnary().getPackage(
				oPackageNameVMCreatorBuilder.toString());

		if (oPackageBaseVMCreator == null) {
			MPackage oMasterPackage = this.getDomain().getDictionnary().getPackage(sMasterPackageName);
			oPackageBaseVMCreator = new MPackage(sViewModelSubPackage, oMasterPackage);
			this.getDomain().getDictionnary().registerPackage(oPackageBaseVMCreator);
		}
		return oPackageBaseVMCreator;
	}
}
