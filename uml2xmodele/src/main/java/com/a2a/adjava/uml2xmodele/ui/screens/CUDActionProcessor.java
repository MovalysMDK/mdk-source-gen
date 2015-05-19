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

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.extractors.ICUDActionProcessor;
import com.a2a.adjava.messages.MessageHandler;
import com.a2a.adjava.uml.UmlClass;
import com.a2a.adjava.uml.UmlDictionary;
import com.a2a.adjava.uml.UmlUsage;
import com.a2a.adjava.uml2xmodele.ui.actions.ActionConstants;
import com.a2a.adjava.uml2xmodele.ui.actions.ActionFactory;
import com.a2a.adjava.xmodele.IDomain;
import com.a2a.adjava.xmodele.IModelDictionary;
import com.a2a.adjava.xmodele.IModelFactory;
import com.a2a.adjava.xmodele.MAction;
import com.a2a.adjava.xmodele.MActionType;
import com.a2a.adjava.xmodele.MPackage;
import com.a2a.adjava.xmodele.MPage;
import com.a2a.adjava.xmodele.MScreen;
import com.a2a.adjava.xmodele.MViewModelImpl;
import com.a2a.adjava.xmodele.ui.component.MNavigationButton;
import com.a2a.adjava.xmodele.ui.component.MWorkspaceConfig;
import com.a2a.adjava.xmodele.ui.navigation.MNavigation;
import com.a2a.adjava.xmodele.ui.navigation.MNavigationType;
import com.a2a.adjava.xmodele.ui.panel.MPanelOperation;
import com.a2a.adjava.xmodele.ui.viewmodel.ViewModelType;

/**
 * @author lmichenaud
 *
 */
public class CUDActionProcessor  implements ICUDActionProcessor {

	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(CUDActionProcessor.class);
	
	/**
	 */
	private static CUDActionProcessor instance = new CUDActionProcessor();

	/**
	 * Constructor
	 */
	public CUDActionProcessor() {
		//Empty constructor
	}

	/**
	 * Return singleton instance
	 * @return singleton instance
	 */
	public static CUDActionProcessor getInstance() {
		return instance;
	}
	
	/**
	 * Create CUD actions (CREATE/UPDATE/DELETE) 
	 */
	public void treatCUDOperations( ScreenContext p_oScreenContext, UmlDictionary p_oUmlDict ) throws Exception {
		
		IDomain oDomain = p_oScreenContext.getDomain();
		
		for (UmlClass oScreenUmlClass : p_oScreenContext.getScreenUmlClasses(p_oUmlDict)) {
			List<PanelAggregation> listPanelAggregations = p_oScreenContext.getPanelAggregations(oScreenUmlClass);
				
			//dans le cas du WorkspacePanel il y a plusieurs Panel
			for (PanelAggregation oPanelAggregation : listPanelAggregations ) {

				// List panel case
				if (p_oScreenContext.isListPanel(oPanelAggregation.getPanel())) {
					MPage oPanel = oDomain.getDictionnary().getPanel(oPanelAggregation.getPanel().getName());
					this.treatCUDOperationsForListPanel(oPanel, p_oScreenContext);
				}
				// Detail Panel case
				else {
					MScreen oScreen = oDomain.getDictionnary().getScreen(oScreenUmlClass.getName());
					if ( oScreen.hasMasterPage()) {
						MPage oPage = p_oScreenContext.getDomain().getDictionnary().getPanel(oPanelAggregation.getPanel().getName());
						this.treatCUDOperationForDetailPanel(
							oPage, oPanelAggregation, oScreenUmlClass, p_oScreenContext);
					}
				}
			}
		}
	}

	/**
	 * Create CUD actions for list panel.
	 * Possible operations are : "create" only 
	 */
	public void treatCUDOperationsForListPanel( MPage p_oPage, ScreenContext p_oScreenContext ) throws Exception {
		
		log.debug("treatCUDOperationsForListPanel, panel: {}, workspace: {}", p_oPage.getName(), p_oPage.getParent()!=null?p_oPage.getParent().isWorkspace():null);
		
		for( MPanelOperation oOperation : p_oPage.getPanelOperations()) {
			if ( oOperation.getName().equalsIgnoreCase("create")){
				
				MNavigation oNav = null ;
				
				if ( p_oPage.getParent() != null && p_oPage.getParent().isWorkspace()) {
					
					log.debug("  workspace case");
					
					// Workspace case: create a navigation to detail panel of workspace.
					oNav = p_oScreenContext.getDomain().getXModeleFactory().createNavigation(
						"navigation-oncreate", MNavigationType.NAVIGATION_WKS_SWITCHPANEL, 
						p_oPage.getParent(), p_oPage.getParent());
					oNav.setTargetPageIdx( this.findFirstDetailPagePosition(p_oPage.getParent()));
				}
				else {
					log.debug("  standard case");
					
					// Default Case: get detail navigation 
					oNav = p_oPage.getNavigationOfType(MNavigationType.NAVIGATION_DETAIL);
				}
				
				if ( oNav != null ) {
					// add the create button on page
					MPage oTargetPage = oNav.getTargetPage();
					
					MNavigationButton oNavButton = p_oScreenContext.getDomain().getXModeleFactory().createNavigationButton(
							"button_create_" + oTargetPage.getUmlName().toLowerCase(),
							"button_create_" + oTargetPage.getUmlName().toLowerCase(),
							"create " + oTargetPage.getUmlName(), oNav);
					
					addNavButton(oNavButton, oOperation, p_oPage);
				}
				else {
					MessageHandler.getInstance().addError(
						"Operation create() found on panel '{}' but no navigationdetail has been found", 
							p_oPage.getUmlName());
				}
			}
		}
	}
	
	protected void addNavButton(MNavigationButton p_oNavButton, MPanelOperation p_oOperation, MPage p_oPage)
	{
		// Test stereotype pour savoir où mettre le bouton : niveau list ou sur chaque item de la liste 
		// Si sur l'item, item le plus bas de la liste si liste à plusieurs niveaux.
		if ( !p_oOperation.hasStereotype("Mm_list") && p_oPage.getAdapter() != null
				&& (
				p_oPage.getViewModelImpl().getType() == ViewModelType.LIST_1 || 
				p_oPage.getViewModelImpl().getType() == ViewModelType.LIST_2 || 
				p_oPage.getViewModelImpl().getType() == ViewModelType.LIST_3 )) {
			p_oPage.getAdapter().getLayout("listitem1").addButton(p_oNavButton);
		}
		else {
			p_oPage.getLayout().addButton(p_oNavButton);
		}
	}
	
	/**
	 * @param p_oScreen
	 * @return
	 */
	private int findFirstDetailPagePosition( MScreen p_oScreen ) {
		int r_iPosition = 0 ;
		for( MPage oPage : p_oScreen.getPages()) {
			if ( oPage.getParameterValue(MWorkspaceConfig.PANELTYPE_PARAMETER).equals( MWorkspaceConfig.DETAIL_PANELTYPE) &&
					oPage.getParameterValue(MWorkspaceConfig.COLUMN_PARAMETER).equals("1") &&
					oPage.getParameterValue(MWorkspaceConfig.SECTION_PARAMETER).equals("1")) {
				break;
			}
			r_iPosition++;
		}
		return r_iPosition ;
	}
	
	
	/**
	 * @param p_oScreen
	 * @param p_oUmlUsage
	 * @param p_oScreenUmlClass
	 * @param p_oVmc
	 * @param p_oScreenContext
	 * @throws Exception
	 */
	public void treatCUDOperationForDetailPanel( MPage p_oPage, PanelAggregation p_oPanelAggregation,
			UmlClass p_oScreenUmlClass, ScreenContext p_oScreenContext ) throws Exception {
		
		IDomain<IModelDictionary, IModelFactory> oDomain = p_oScreenContext.getDomain();
		
		//récupération du view model d'une page classique (ou item de liste)
		MViewModelImpl oPageVm = p_oPage.getViewModelImpl();

		// Master package is the uml entity package.
		MPackage oMasterPackage = p_oScreenContext.computeMasterPackage(p_oScreenUmlClass);
		
		// Add a save detail action if view model is linked to an entity
		// and (viewmodel is not readonly or customizable)
		if (( !oPageVm.isReadOnly() || oPageVm.isCustomizable()) && oPageVm.getEntityToUpdate() != null) {
		
			// add save action and button
			StringBuilder oActionNameBuilder = new StringBuilder();
			oActionNameBuilder.append(ActionConstants.PREFIX_ACTION_SAVE).append(
					StringUtils.capitalize( p_oPanelAggregation.getPanel().getName()));
			MAction oSaveDetailAction = ActionFactory.getInstance().addAction(oDomain, false, p_oPage, null, 
					  oActionNameBuilder.toString(), oMasterPackage, 
					  MActionType.SAVEDETAIL, oDomain.getDictionnary().getViewModelCreator().getFullName());

			p_oPage.addAction(oSaveDetailAction);
			p_oPage.addImport(oSaveDetailAction.getMasterInterface().getFullName());
			p_oPage.addImport(oSaveDetailAction.getMasterInterface().getOutClass());

			if (p_oPage.getParent() == null || !p_oPage.getParent().isWorkspace()) {
				p_oPage.getLayout().addButton(
					p_oScreenContext.getDomain().getXModeleFactory().createSaveButton(
							"button_" + oActionNameBuilder.toString(), oSaveDetailAction));
			}

			// Search for navigation usage. It will be the launched screen on the save action.
			List<UmlUsage> listNavigationUsages = p_oScreenContext.getNavigationAndNavigationDetailUsages(p_oScreenUmlClass);
			MScreen oTargetScreen = null ;
			MNavigation oSuccessNavigation = null ;
			if ( !listNavigationUsages.isEmpty()) {
				if (!listNavigationUsages.isEmpty()) {
					if (listNavigationUsages.get(0).hasAnyStereotype("Mm_navigationdetail")) {
						UmlClass oTargetUmlClass = listNavigationUsages.get(0).getSupplier();						
						oTargetScreen = oDomain.getDictionnary().getScreen(oTargetUmlClass.getName());
						if (!oDomain.getDictionnary().getNavigationsBetweenScreen(
								p_oPage.getParent(), oTargetScreen).isEmpty()) {
							oSuccessNavigation = oDomain.getDictionnary().getNavigationsBetweenScreen(
									p_oPage.getParent(), oTargetScreen).get(0);
							p_oPage.addNavigation(oSuccessNavigation);
						}
						else {
							MessageHandler.getInstance().addError("A navigation link is required between {} and {}", p_oPage.getFullName(), oTargetScreen.getFullName());
						}
					}
				}
				else {
					MessageHandler.getInstance().addError("Screen {} must have one or more usage.", p_oScreenUmlClass.getFullName());
				}
			}
			
			// Traitement des opérations
			for( MPanelOperation oMPanelOperation : p_oPage.getPanelOperations()) {
								
				// Delete action
				if ( oMPanelOperation.getName().equalsIgnoreCase(MActionType.DELETEDETAIL.name())){
					StringBuilder oDeleteActionNameBuilder = new StringBuilder();
					oDeleteActionNameBuilder.append(ActionConstants.PREFIX_ACTION_DELETE);								
					oDeleteActionNameBuilder.append(StringUtils.capitalize(p_oPanelAggregation.getPanel().getName()));
					
					MActionType oType = MActionType.DELETEDETAIL ;
					MAction oDeleteAction = ActionFactory.getInstance().addAction(oDomain, false, p_oPage, null, 
							oDeleteActionNameBuilder.toString(), oMasterPackage, oType, 
							oDomain.getDictionnary().getViewModelCreator().getFullName());
					
					p_oPage.addAction(oDeleteAction);
					p_oPage.addImport(oDeleteAction.getMasterInterface().getFullName());
					p_oPage.addImport(oDeleteAction.getMasterInterface().getOutClass());
					
					p_oPage.getLayout().addButton(
						p_oScreenContext.getDomain().getXModeleFactory().createDeleteButton(
						"button_" + oDeleteActionNameBuilder.toString(), oDeleteAction));
					
					// If no navigation usage and screen is workspace, delete operation should navigate back to list
					if ( oSuccessNavigation == null && p_oPage.getParent() != null && p_oPage.getParent().isWorkspace()) {
						oSuccessNavigation = oDomain.getXModeleFactory().createNavigation("navigation-ondelete", 
								MNavigationType.NAVIGATION_WKS_SWITCHPANEL, p_oPage.getParent(), p_oPage.getParent());
						oSuccessNavigation.setTargetPageIdx(0);
						p_oPage.addNavigation(oSuccessNavigation);
					}
				}
			}
		}
	}
	
	
	/**
	 * @param p_oPage
	 * @param p_oScreenUmlClass
	 * @param p_oVmc
	 * @param p_oScreenContext
	 * @throws Exception
	 */
	public void treatCUDOperationForSearchPanel(MPage p_oPage, UmlClass p_oScreenUmlClass, ScreenContext p_oScreenContext) {
		
		IDomain<IModelDictionary, IModelFactory> oDomain = p_oScreenContext.getDomain();
		
		//récupération du view model d'une page classique (ou item de liste)
		MViewModelImpl oPageVm = p_oPage.getViewModelImpl();

		// Master package is the uml entity package.
		MPackage oMasterPackage = p_oScreenContext.computeMasterPackage(p_oScreenUmlClass);
		
			// add save action and button
			StringBuilder oActionNameBuilder = new StringBuilder();
			oActionNameBuilder.append(ActionConstants.PREFIX_ACTION_SAVE).append(
					StringUtils.capitalize(p_oPage.getName()));
			MAction oSaveDetailAction = ActionFactory.getInstance().addAction(oDomain, false, p_oPage, null, 
					  oActionNameBuilder.toString(), oMasterPackage, 
					  MActionType.SAVEDETAIL, oDomain.getDictionnary().getViewModelCreator().getFullName());

			p_oPage.addAction(oSaveDetailAction);
			p_oPage.addImport(oSaveDetailAction.getMasterInterface().getFullName());
			p_oPage.addImport(oSaveDetailAction.getMasterInterface().getOutClass());
//		}
	}

	@Override
	public void treatCUDOperations(Object p_oObject, UmlDictionary p_oUmlDict)
			throws Exception {		
		ScreenContext oScreenContext = (ScreenContext) p_oObject;
		treatCUDOperations(oScreenContext, p_oUmlDict);
	}

	@Override
	public void treatCUDOperationsForListPanel(MPage p_oPage, Object p_oObject)
			throws Exception {
		ScreenContext oScreenContext = (ScreenContext) p_oObject;
		treatCUDOperationsForListPanel(p_oPage, oScreenContext);		
	}

	@Override
	public void treatCUDOperationForDetailPanel(MPage p_oPage,
			Object p_oPanelAggregation, UmlClass p_oScreenUmlClass,
			Object p_oObject) throws Exception {
		PanelAggregation oPanelAggregation = (PanelAggregation) p_oPanelAggregation;
		ScreenContext oScreenContext = (ScreenContext) p_oObject;
		
		treatCUDOperationForDetailPanel(p_oPage, oPanelAggregation, p_oScreenUmlClass, oScreenContext);
		
	}

	@Override
	public void treatCUDOperationForSearchPanel(MPage p_oPage,
			UmlClass p_oScreenUmlClass, Object p_oObject) {
		ScreenContext oScreenContext = (ScreenContext) p_oObject;
		treatCUDOperationForSearchPanel(p_oPage, p_oScreenUmlClass, oScreenContext);
	}

}
