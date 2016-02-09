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
package com.a2a.adjava.xmodele.ui.viewmodel;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;

/**
 * <p>
 * 	Énumération des différents types de ViewModels.
 * 	Elle est mise à jour à la lecture de la configuration.
 * </p>
 *
 * <p>Copyright (c) 2011</p>
 * <p>Company: Adeuza</p>
 *
 * @since MF-Annapurna
 */
public enum ViewModelType {
		
	/** liste de dimension 2 sans élément selectionné */
	MASTER(null,null,null,null,null,null,null,null,null,null, null, false, null),
	
	/** Element de type WorkspaceView */
	WORKSPACE_MASTERDETAIL(null,null,null,null,null,null,null,null,null,null,null, false, null),
	
	/** Element de type WorkspaceView */
	WORKSPACE_DETAIL(null,null,null,null,null,null,null,null,null,null,null, false, null),
	
	/** Element de type MultiPanel */
	MULTIPANEL(null,null,null,null,null,null,null,null,null,null,null, false, null),
	
	/** Item de list 1er niveau (le plus bas) */
	LISTITEM_1(null,null,null,null,null,null,null,null,null,null, null, false, null),
	
	/** Item de list 2eme niveau */
	LISTITEM_2(null,null,null,null,null,null,null,null,null,null, LISTITEM_1, false, null),
	
	/** Item de list 3eme niveau */
	LISTITEM_3(null,null,null,null,null,null,null,null,null,null, LISTITEM_2, false, null),
	
	/** liste de dimension 1 sans élément selectionné */
	LIST_1(null,null,null,null,null,null,null,null,null,null, LISTITEM_1, true, null),
	
	/** liste de dimension 2 sans élément selectionné */
	LIST_2(null,null,null,null,null,null,null,null,null,null, LISTITEM_2, true, null),
	
	/** liste de dimension 3 sans élément selectionné */
	LIST_3(null,null,null,null,null,null,null,null,null,null, LISTITEM_3, true, null), 
	
	/** liste de dimension 1 avec un élément selectionné */
	LIST_1__ONE_SELECTED(null,null,null,null,null,null,null,null,null,null, LISTITEM_1, true, null),
	
	/** liste de dimension 1 avec n élements selectionnés  */
	LIST_1__N_SELECTED(null,null,null,null,null,null,null,null,null,null, LISTITEM_1, true, null),
	
	/** item de fixed list */
	FIXED_LIST_ITEM(null,null,null,null,null,null,null,null,null,null, null, false, null),
	
	/** fixed liste */
	FIXED_LIST(null,null,null,null,null,null,null,null,null,null, FIXED_LIST_ITEM, true, null)
	;
	
	/** Map de configuration des types de <code>ViewModel</code> */
	private Map<String, ViewModelTypeConfiguration> vmTypeOptionMap = null;
	/** */
	private ViewModelType subType ;
	/** clé de l'option par défaut */
	public final static String DEFAULT = "default";
	/** clé de l'option VM customisable */
	public final static String CUSTOM = "custom";
	/** clé pour l'affichage d'un <code>MMSearchSpinner</code> */
	public final static String FILTER = "filter";
	/** clé pour l'affichage d'un <code>MMMultiSelectedListView</code> */
	public final static String MULTISELECT = "multiselect";
	
	/** indique si le view model réprésente une liste */
	private boolean isList = false;
	
	/**
	 * Création d'un Type de ViewModel.
	 * @param p_sOption 
	 * 		l'option permettant de différencier certains <code>ViewModel</code> qui ont le même type. 
	 * 		Ex <code>MMSpinner</code> et <code>MMSearchSpinner</code>.
	 * @param p_sList
	 * @param p_sFullList
	 * @param p_sInterface
	 * @param p_sInterfaceFull
	 * @param p_sVisualComponentName
	 * @param p_sVisualComponentNameFull
	 * @param p_sAdapterName
	 * @param p_sAdapterFullName
	 * @param p_sSubElementName
	 * @param p_sSubElementFullName
	 * @param p_oSubType
	 * @param p_oImports
	 */
	private ViewModelType(
			final String p_sList, final String p_sFullList, final String p_sInterface, final String p_sInterfaceFull, 
			final String p_sVisualComponentName,final String p_sVisualComponentNameFull, final String p_sAdapterName, final String p_sAdapterFullName, 
			final String p_sSubElementName, final String p_sSubElementFullName, final ViewModelType p_oSubType, final boolean p_bIsList, Map<String, String> p_sPropertyList, final String... p_oImports) {

		vmTypeOptionMap = new HashMap<String, ViewModelTypeConfiguration>();
		
		this.isList = p_bIsList;
		
		update(DEFAULT, p_sList, p_sFullList, p_sInterface, p_sInterfaceFull, p_sVisualComponentName, p_sVisualComponentNameFull, 
				p_sAdapterName, p_sAdapterFullName, p_sSubElementName, p_sSubElementFullName, 0, p_sPropertyList, p_oImports);
	
		subType=p_oSubType;
		
	}
	
	/**
	 * <p>Mise à jour de la configuration d'un <code>ViewModel</code> de composant.</p>
	 * @param p_sConfigName
	 * 		l'option permettant de différencier certains <code>ViewModel</code> qui ont le même type. 
	 * 		Ex: composants <code>MMSpinner</code> et <code>MMSearchSpinner</code>.
	 * @param p_sList
	 * @param p_sFullList
	 * @param p_sInterface
	 * @param p_sInterfaceFull
	 * @param p_sVisualComponentName
	 * @param p_sVisualComponentNameFull
	 * @param p_sAdapterName
	 * @param p_sAdapterFullName
	 * @param p_sSubElementName
	 * @param p_sSubElementFullName
	 * @param p_iMaxLevel
	 * @param p_oImports
	 */
	public void update(
			final String p_sConfigName, final String p_sList, final String p_sFullList, final String p_sInterface,  
			final String p_sInterfaceFull, final String p_sVisualComponentName, final String p_sVisualComponentNameFull, 
			final String p_sAdapterName, final String p_sAdapterFullName, final String p_sSubElementName, 
			final String p_sSubElementFullName, final int p_iMaxLevel, Map<String, String> p_sPropertyList, final String... p_oImports) {
		
		ViewModelTypeConfiguration r_oVmTypeparameters = new ViewModelTypeConfiguration();
		
		r_oVmTypeparameters.setConfigName(p_sConfigName);
		r_oVmTypeparameters.setListName(p_sList);
		r_oVmTypeparameters.setListFullName(p_sFullList);
		r_oVmTypeparameters.setVisualComponentNameFull(p_sVisualComponentNameFull);
		r_oVmTypeparameters.setVisualComponentName(p_sVisualComponentName);
		r_oVmTypeparameters.setAdapterName(p_sAdapterName);
		r_oVmTypeparameters.setAdapterFullName(p_sAdapterFullName);
		r_oVmTypeparameters.setInterfaceName(p_sInterface);
		r_oVmTypeparameters.setInterfaceFullName(p_sInterfaceFull);
		r_oVmTypeparameters.setSubElementName(p_sSubElementName);
		r_oVmTypeparameters.setSubElementFullName(p_sSubElementFullName);
		r_oVmTypeparameters.setMaxLevel(p_iMaxLevel);
		r_oVmTypeparameters.setPropertyList(p_sPropertyList);
		
		String[] oImports;
		if (r_oVmTypeparameters.getListName()!=null) {
			if (p_oImports!=null) {
				oImports = ArrayUtils.add(p_oImports, p_sFullList);
			}
			else {
				oImports = new String[] { p_sFullList };
			}
		}
		else {
			oImports = new String[0];
		}
		r_oVmTypeparameters.setImports(oImports);
		
		if (p_sConfigName!=null){
			vmTypeOptionMap.put(p_sConfigName, r_oVmTypeparameters);
		}
		
	}
	
	/**
	 * Retourne la map contenant la configuration des types de <code>ViewModel</code>.
	 * @return <code>HashMap</code> de dont la clé correspond à un String représentant l'option du Type de <code>ViewModel</code>
	 */
	public Map<String,ViewModelTypeConfiguration> getVMTypeOptionMap(){
		return vmTypeOptionMap;
	}

	/** 
	 * <p>
	 * 	Retourne la configuration correspondant à l'option envoyée en paramètre.
	 * 	Par défaut on va avoir l'option <em>default</em>.
	 * 	Cette méthode peut retourner la valeur {@code null}.
	 * </p>
	 * @param p_sConfigName
	 * 		Chaine pour identifier l'option du type de <code>ViewModel</code>
	 * @return la conf sous la forme d'un objet de type <code>ViewModelTypeOption</code>
	 */
	public ViewModelTypeConfiguration getParametersByConfigName(String p_sConfigName, String p_sVersion){
		ViewModelTypeConfiguration oType = null;
		if (p_sVersion == null) {
			oType =  this.vmTypeOptionMap.get(p_sConfigName);
		}
		else {
			oType = this.vmTypeOptionMap.get(p_sConfigName + "-" + p_sVersion);
			if (oType == null) {
				oType = this.vmTypeOptionMap.get(p_sConfigName);
			}
		}
		return oType;
	}
	
	/**
	 * TODO Décrire la méthode setSubType de la classe ViewModelTypeOption
	 * @param p_sSubType
	 */
	public void setSubType(final ViewModelType p_sSubType){
		subType=p_sSubType;
	}
	
	/**
	 * TODO Décrire la méthode getSubType de la classe ViewModelTypeOption
	 * @return
	 */

	public ViewModelType getSubType() {
		return subType;
	}
	
	/**
	 * Retourne l'objet isList
	 * @return Objet isList
	 */
	public boolean isList() {
		return this.isList;
	}
	
}
