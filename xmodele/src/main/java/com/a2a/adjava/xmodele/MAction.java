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
package com.a2a.adjava.xmodele;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;

import com.a2a.adjava.utils.StrUtils;

/**
 * <p>
 * A2A_DOC - Décrire la classe MAction
 * </p>
 * 
 * <p>
 * Copyright (c) 2011
 * </p>
 * <p>
 * Company: Adeuza
 * </p>
 */
public class MAction extends SClass<MActionInterface, MMethodSignature> {

	private MDaoInterface dao ;

	private MViewModelImpl vm ;

	private List<MDaoInterface> externalDaos ;

	private String creatorName ;

	/**
	 * 
	 */
	private Map<String, MAction> redirectActions = new HashMap<String, MAction>();

	/**
	 * For action of type DISPLAYLIST, a search is possible. This property is
	 * the search dialog. The action needs to know about that dialog to perform
	 * loadData with the search criterias.
	 */
	private MDialog searchDialog;

	/**
	 * Boolean that indicates if it is the first action launched after
	 * application initialization
	 */
	private boolean isRoot = false;

	/**
	 * List of the view models impacted by the action
	 */
	private List<MViewModelImpl> linkedViewModels = new ArrayList<MViewModelImpl>();

	/**
	 * <p>
	 * Construit un nouvel objet <em>MAction</em>.
	 * </p>
	 * @param p_sName
	 * @param p_bIsRoot first action after application initialization
	 * @param p_oPackage
	 * @param p_oType
	 * @param p_oViewModel
	 * @param p_oEntity
	 * @param p_oDao
	 * @param p_sCreatorName
	 */
	public MAction(String p_sName, MActionInterface p_oActionInterface, boolean p_bIsRoot, MPackage p_oPackage,
			MViewModelImpl p_oViewModel, MDaoInterface p_oDao,
			List<MDaoInterface> p_listExternalDaos, String p_sCreatorName) {
		super("action", null, p_sName, p_oPackage);
		this.setMasterInterface(p_oActionInterface);
		this.dao = p_oDao;
		this.externalDaos = p_listExternalDaos;
		this.vm = p_oViewModel;
		this.creatorName = p_sCreatorName;
		this.isRoot = p_bIsRoot;
	}

	/**
	 * @param p_oDialog
	 */
	public void setSearchDialog(MDialog p_oDialog) {
		this.searchDialog = p_oDialog;
	}

	/**
	 * A2A_DOC - Décrire la méthode isRoot de la classe MAction
	 * @return
	 */
	public boolean isRoot() {
		return this.isRoot;
	}

	/**
	 * Add a linked view model to update
	 */
	public void addLinkedViewModel(MViewModelImpl p_oViewModel) {
		this.linkedViewModels.add(p_oViewModel);
	}

	/**
	 * @param p_sKey
	 * @param p_oAction
	 */
//DELETE_DISPLAYACTION	public void addRedirectAction(String p_sKey, MAction p_oAction) {
//DELETE_DISPLAYACTION		this.redirectActions.put(p_sKey, p_oAction);
//DELETE_DISPLAYACTION	}

	/**
	 * Retourne l'objet creatorName
	 * @return Objet creatorName
	 */
	public String getCreatorName() {
		return this.creatorName;
	}

	/**
	 * Retourne l'objet dao
	 * @return Objet dao
	 */
	public MDaoInterface getDao() {
		return this.dao;
	}

	/**
	 * Retourne l'objet entity
	 * @return Objet entity
	 */
	public MEntityImpl getEntity() {
		return this.getMasterInterface().getEntity();
	}

	/**
	 * Retourne l'objet vm
	 * @return Objet vm
	 */
	public MViewModelImpl getVm() {
		return this.vm;
	}

	/**
	 * Retourne l'objet externalDaos
	 * @return Objet externalDaos
	 */
	public List<MDaoInterface> getExternalDaos() {
		return this.externalDaos;
	}

	/**
	 * Retourne l'objet redirectActions
	 * @return Objet redirectActions
	 */
	public Map<String, MAction> getRedirectActions() {
		return this.redirectActions;
	}

	/**
	 * Retourne l'objet searchDialog
	 * @return Objet searchDialog
	 */
	public MDialog getSearchDialog() {
		return this.searchDialog;
	}

	/**
	 * Retourne l'objet linkedViewModels
	 * @return Objet linkedViewModels
	 */
	public List<MViewModelImpl> getLinkedViewModels() {
		return this.linkedViewModels;
	}

	/**
	 * Retourne l'objet type
	 * @return Objet type
	 */
	public MActionType getType() {
		return this.getMasterInterface().getActionType();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void toXmlInsertBeforeDocumentation(Element p_xElement) {
		super.toXmlInsertBeforeDocumentation(p_xElement);
		p_xElement.addElement("action-type").setText( this.getMasterInterface().getActionType().name());
		Element xIn = p_xElement.addElement("in");
		if ( this.getMasterInterface().getInClass() != null ) {
			xIn.addAttribute("full-name",this.getMasterInterface().getInClass());
			xIn.addAttribute("name", StrUtils.substringAfterLastDot(this.getMasterInterface().getInClass()));
		}
		Element xOut = p_xElement.addElement("out");
		if ( this.getMasterInterface().getOutClass() != null ) {
			xOut.addAttribute("full-name",this.getMasterInterface().getOutClass());
			xOut.addAttribute("name", StrUtils.substringAfterLastDot(this.getMasterInterface().getOutClass()));
		}
		Element xStep = p_xElement.addElement("step");
		if ( this.getMasterInterface().getStepClass() != null ) {
			xStep.addAttribute("full-name", this.getMasterInterface().getStepClass());
			xStep.addAttribute("name", StrUtils.substringAfterLastDot(this.getMasterInterface().getStepClass()));
		}
		Element xProgress = p_xElement.addElement("progress");
		if ( this.getMasterInterface().getProgressClass() != null ) {
			xProgress.addAttribute("full-name", this.getMasterInterface().getProgressClass());
			xProgress.addAttribute("name", StrUtils.substringAfterLastDot(this.getMasterInterface().getProgressClass()));	
		}
		if (this.getMasterInterface().getEntity() != null) {
			p_xElement.add(this.getMasterInterface().getEntity().toXml());
		}
		if (this.dao != null) {
			p_xElement.add(this.dao.toXml());
		}
		if (this.externalDaos != null && !this.externalDaos.isEmpty()) {
			Element xExternalDaos = p_xElement.addElement("external-daos");
			int iPos = 1;
			for (MDaoInterface oExternalDao : this.externalDaos) {
				Element xExternalDao = oExternalDao.toXml();
				xExternalDao.addAttribute("position", Integer.toString(iPos));
				xExternalDaos.add(xExternalDao);
				iPos++;
			}
		}
		if (this.vm != null) {
			p_xElement.add(this.vm.toXml());
		}
		p_xElement.addElement("creator-name").setText(this.creatorName);
		if (this.isRoot) {
			p_xElement.addElement("root");
		}

		if (this.linkedViewModels != null && !this.linkedViewModels.isEmpty()) {
			Element xExternalVM = p_xElement.addElement("linked-view-models");
			for (MViewModelImpl oExternalViewModel : this.linkedViewModels) {
				xExternalVM.add(oExternalViewModel.toXml());
			}
		}

		if (this.searchDialog != null) {
			Element xDialog = this.searchDialog.toXml();
			xDialog.setName("search-dialog");
			p_xElement.add(xDialog);
		}
		
		// Redirect Actions
		Element xRedirectActions = p_xElement.addElement("redirect-actions");
		for (Entry<String, MAction> oEntry : this.redirectActions.entrySet()) {
			MAction oRedirectAction = oEntry.getValue();
			
			Element xAction = xRedirectActions.addElement("redirect-action");
			xAction.addAttribute("key", oEntry.getKey());
			Element xInterface = xAction.addElement("interface");
			xInterface.addAttribute("name", oRedirectAction.getMasterInterface().getName());
			xInterface.addAttribute("full-name", oRedirectAction.getMasterInterface().getFullName());
			
			xIn = xAction.addElement("in");
			xIn.addAttribute("full-name",oRedirectAction.getMasterInterface().getInClass());
			xIn.addAttribute("name", StrUtils.substringAfterLastDot(oRedirectAction.getMasterInterface().getInClass()));
			
			xOut = xAction.addElement("out");
			xOut.addAttribute("full-name",oRedirectAction.getMasterInterface().getOutClass());
			xOut.addAttribute("name", StrUtils.substringAfterLastDot(oRedirectAction.getMasterInterface().getOutClass()));
			
			xStep = xAction.addElement("step");
			xStep.addAttribute("full-name", oRedirectAction.getMasterInterface().getStepClass());
			xStep.addAttribute("name", StrUtils.substringAfterLastDot(oRedirectAction.getMasterInterface().getStepClass()));
			
			xProgress = xAction.addElement("progress");
			xProgress.addAttribute("full-name", oRedirectAction.getMasterInterface().getProgressClass());
			xProgress.addAttribute("name", StrUtils.substringAfterLastDot(oRedirectAction.getMasterInterface().getProgressClass()));	
		}
	}
}
