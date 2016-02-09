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
package com.a2a.adjava.uml2xmodele.extractors.viewmodel;

import org.apache.commons.lang3.StringUtils;

import com.a2a.adjava.languages.LanguageConfiguration;
import com.a2a.adjava.uml.UmlClass;
import com.a2a.adjava.utils.StrUtils;
import com.a2a.adjava.xmodele.IDomain;
import com.a2a.adjava.xmodele.IModelDictionary;
import com.a2a.adjava.xmodele.IModelFactory;
import com.a2a.adjava.xmodele.MPackage;
import com.a2a.adjava.xmodele.MPage;
import com.a2a.adjava.xmodele.MViewModelImpl;

/**
 * @author lmichenaud
 * 
 */
public class VMNamingHelper {

	/**
	 * Singleton instance
	 */
	private static final VMNamingHelper instance = new VMNamingHelper();

	/**
	 * Define the suffix of the name of the list view model 
	 */
	public static final String LIST_VIEW_MODEL_LIST_SUFFIX = "List" ;
	/**
	 * Constructor
	 */
	private VMNamingHelper() {
		// Empty constructor
	}

	/**
	 * Return singleton instance
	 * 
	 * @return singleton instance
	 */
	public static VMNamingHelper getInstance() {
		return instance;
	}

	/**
	 * Compute view model name
	 * 
	 * @param p_sBaseName
	 *            base name
	 * @param p_bList
	 *            true if view model of type list
	 * @param p_oLngConf
	 *            language configuration
	 * @return view model name
	 */
	public String computeViewModelImplName(String p_sBaseName, boolean p_bList,
			LanguageConfiguration p_oLngConf) {
		return this.computeName(p_sBaseName,
				p_oLngConf.getViewModelImplementationNamingPrefix(),
				p_oLngConf.getViewModelImplementationNamingSuffix(), p_bList,
				p_oLngConf);
	}

	/**
	 * @param p_sBaseName
	 * @param p_bList
	 * @param p_oLngConf
	 * @return
	 */
	public String computeViewModelInterfaceName(String p_sBaseName,
			boolean p_bList, LanguageConfiguration p_oLngConf) {
		return this.computeName(p_sBaseName,
				p_oLngConf.getViewModelInterfaceNamingPrefix(),
				p_oLngConf.getViewModelInterfaceNamingSuffix(), p_bList,
				p_oLngConf);
	}

	/**
	 * @param p_sBaseName
	 * @param p_bList
	 * @param p_oLngConf
	 * @return
	 */
	public String computeSectionImplementationName(String p_sBaseName,
			boolean p_bList, LanguageConfiguration p_oLngConf) {
		return this.computeName(p_sBaseName,
				p_oLngConf.getSectionImplementationNamingPrefix(),
				p_oLngConf.getSectionImplementationNamingSuffix(), p_bList,
				p_oLngConf);
	}

	
	/**
	 * @param p_sBaseName
	 * @param p_bList
	 * @param p_oLngConf
	 * @return
	 */
	public String computeSectionInterfaceName(String p_sBaseName,
			boolean p_bList, LanguageConfiguration p_oLngConf) {
		return this.computeName(p_sBaseName,
				p_oLngConf.getSectionInterfaceNamingPrefix(),
				p_oLngConf.getSectionInterfaceNamingSuffix(), p_bList,
				p_oLngConf);
	}

	/**
	 * @param p_sBaseName
	 * @param p_bList
	 * @param p_oLngConf
	 * @return
	 */
	public String computeDataloaderImplementationName(String p_sBaseName,
			boolean p_bList, LanguageConfiguration p_oLngConf) {
		return this.computeName(p_sBaseName,
				p_oLngConf.getDataloaderImplementationNamingPrefix(),
				p_oLngConf.getDataloaderImplementationNamingSuffix(), p_bList,
				p_oLngConf);
	}

	
	/**
	 * @param p_sBaseName
	 * @param p_bList
	 * @param p_oLngConf
	 * @return
	 */
	public String computeDataloaderInterfaceName(String p_sBaseName,
			boolean p_bList, LanguageConfiguration p_oLngConf) {
		return this.computeName(p_sBaseName,
				p_oLngConf.getDataloaderInterfaceNamingPrefix(),
				p_oLngConf.getDataloaderInterfaceNamingSuffix(), p_bList,
				p_oLngConf);
	}

	/**
	 * Compute package for viewmodel implementation
	 * @param p_oVMUmlClass uml class of panel
	 * @param p_oDomain domain
	 * @return package for the viewmodel implementation
	 */
	public MPackage computeViewModelImplPackage(UmlClass p_oVMUmlClass,
			IDomain<IModelDictionary,IModelFactory> p_oDomain) {
		String sPackageName = p_oDomain.getStrSubstitutor().replace(
				p_oVMUmlClass.getPackage().getFullName());
		MPackage oBasePackage = p_oDomain.getDictionnary().getPackage(sPackageName);
		
		String sSubPackageName = p_oDomain.getLanguageConf().getViewModelImplementationSubPackageName();

		MPackage r_oPackageBaseViewModel = null ;
		if ( sSubPackageName != null && !sSubPackageName.isEmpty()) {
			String sVMPackageName = StringUtils.join(sPackageName, StrUtils.DOT_S, sSubPackageName);
			r_oPackageBaseViewModel = p_oDomain.getDictionnary().getPackage(sVMPackageName);
			if (r_oPackageBaseViewModel == null) {
				r_oPackageBaseViewModel = new MPackage(sSubPackageName, oBasePackage);
				p_oDomain.getDictionnary().registerPackage(r_oPackageBaseViewModel);
			}
		}
		else {
			r_oPackageBaseViewModel = oBasePackage;
		}
		
		return r_oPackageBaseViewModel;
	}

	/**
	 * Compute view model name
	 * 
	 * @param p_sBaseName
	 *            base name
	 * @param p_sPrefix
	 *            prefix
	 * @param p_sSuffix
	 *            suffix
	 * @param p_bList
	 *            true if view model of type list
	 * @param p_oLngConf
	 *            language configuration
	 * @return
	 */
	private String computeName(String p_sBaseName, String p_sPrefix,
			String p_sSuffix, boolean p_bList, LanguageConfiguration p_oLngConf) {
		StringBuilder r_oViewModelInitialNameBuilder = new StringBuilder();

		if (p_sPrefix != null && !p_sPrefix.isEmpty()) {
			r_oViewModelInitialNameBuilder.append(p_sPrefix);
		}
		if (p_bList) {
			r_oViewModelInitialNameBuilder.append(LIST_VIEW_MODEL_LIST_SUFFIX);
		}
		r_oViewModelInitialNameBuilder.append(p_sBaseName);
		if (p_sSuffix != null && !p_sSuffix.isEmpty()) {
			r_oViewModelInitialNameBuilder.append(p_sSuffix);
		}

		return r_oViewModelInitialNameBuilder.toString();
	}


}
