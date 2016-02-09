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
package com.a2a.adjava.uml2xmodele.ui.screens;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.utils.VersionHandler;
import com.a2a.adjava.xmodele.IDomain;
import com.a2a.adjava.xmodele.IModelDictionary;
import com.a2a.adjava.xmodele.IModelFactory;
import com.a2a.adjava.xmodele.MAdapter;
import com.a2a.adjava.xmodele.MLayout;
import com.a2a.adjava.xmodele.MPage;
import com.a2a.adjava.xmodele.MViewModelImpl;
import com.a2a.adjava.xmodele.ui.viewmodel.ViewModelType;
import com.a2a.adjava.xmodele.ui.viewmodel.ViewModelTypeConfiguration;

/**
 * @author lmichenaud
 *
 */
public class ExternalListAnalyser {

	/**
	 * Logger de classe
	 */
	private static final Logger log = LoggerFactory.getLogger(ExternalListAnalyser.class);

	/**
	 * Singleton instance
	 */
	private static ExternalListAnalyser instance = new ExternalListAnalyser();

	/**
	 * Constructor
	 */
	private ExternalListAnalyser() {
		// Empty constructor
	}

	/**
	 * Return singleton instance
	 * @return singleton instance
	 */
	public static ExternalListAnalyser getInstance() {
		return instance;
	}

	/**
	 * Analyse external lists of a viewmodel :
	 * <ul>
	 * <li>add the combo fields on the layout</li>
	 * <li>create the adapters of combo lists</li>
	 * <li>add the new adapters in the external adapters of the page</li>
	 * <li>call addAdditionalLayouts to create layouts from list item and
	 * selected item</li>
	 * </ul>
	 * @param p_oVm viewmodel
	 * @param p_oLayout layout
	 * @param p_sPath path
	 * @param p_oAdapter adapter
	 * @param p_oPage page
	 */
	public void analyseExternalList(MViewModelImpl p_oVm, MLayout p_oLayout, String p_sPath, MPage p_oPage, IDomain<IModelDictionary, IModelFactory> p_oDomain) throws Exception {
		// On ne peut pas avoir deux combo dans le même panel correspondant à la
		// même entité/VM?
		// Gérer avec une LIST_1__N_SELECTED

		for (MViewModelImpl oVm : p_oVm.getExternalViewModels()) {
			ViewModelType oType = oVm.getType();
			ViewModelTypeConfiguration oTypeParameters = oType.getParametersByConfigName(p_oVm.getConfigName(), VersionHandler.getGenerationVersion().getStringVersion());
			p_oVm.addImport(oTypeParameters.getListFullName());
			p_oVm.setReadOnly(false);

			String sComboName = p_sPath + "_" + oVm.getParameterValue("baseName") + "__edit";
			String sNameAdapter = p_oPage.getUmlName() + oVm.getUmlName();
			
			MAdapter oAdapter = p_oDomain.getXModeleFactory().createExternalAdapter(p_oDomain, oTypeParameters, oVm, sNameAdapter);

			p_oPage.addExternalAdapter(oAdapter, sComboName);

			oAdapter.setViewModel(oVm, "getLst" + oVm.getUmlName());
			p_oDomain.getDictionnary().registerAdapter(oAdapter);
			p_oDomain.getAnalyserAndProcessorFactory().createAdditionalLayoutProcessor().addAdditionalLayouts(oVm, p_sPath, oAdapter, p_oPage, p_oDomain);
		}
	}
}
