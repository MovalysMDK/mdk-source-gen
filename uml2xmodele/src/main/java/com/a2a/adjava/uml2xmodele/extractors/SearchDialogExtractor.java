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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.messages.MessageHandler;
import com.a2a.adjava.types.IUITypeDescription;
import com.a2a.adjava.uml.UmlAssociation;
import com.a2a.adjava.uml.UmlAssociationEnd;
import com.a2a.adjava.uml.UmlAssociationEnd.AggregateType;
import com.a2a.adjava.uml.UmlClass;
import com.a2a.adjava.uml.UmlDictionary;
import com.a2a.adjava.uml.UmlModel;
import com.a2a.adjava.uml2xmodele.ui.screens.PanelAggregation;
import com.a2a.adjava.uml2xmodele.ui.screens.ScreenAggregationPanelProcessor;
import com.a2a.adjava.uml2xmodele.ui.screens.ViewModelAnalyser;
import com.a2a.adjava.utils.VersionHandler;
import com.a2a.adjava.xmodele.IDomain;
import com.a2a.adjava.xmodele.IModelDictionary;
import com.a2a.adjava.xmodele.IModelFactory;
import com.a2a.adjava.xmodele.MDialog;
import com.a2a.adjava.xmodele.MEntityImpl;
import com.a2a.adjava.xmodele.MLayout;
import com.a2a.adjava.xmodele.MPackage;
import com.a2a.adjava.xmodele.MPage;
import com.a2a.adjava.xmodele.MScreen;
import com.a2a.adjava.xmodele.MViewModelCreator;
import com.a2a.adjava.xmodele.MViewModelImpl;
import com.a2a.adjava.xmodele.MVisualField;
import com.a2a.adjava.xmodele.ui.view.MVFLabelKind;

/**
 * @author lmichenaud
 *
 */
public class SearchDialogExtractor extends AbstractExtractor<IDomain<IModelDictionary,IModelFactory>> {

	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(SearchDialogExtractor.class);

	/**
	 * Usage Stereotype for search
	 */
	private List<String> listStereotypes = new ArrayList<String>();

	/**
	 * type of button to use (MMButton or MMImageButton)
	 */
	private String sButtonType = null;

	/**
	 * (non-Javadoc)
	 * @see com.a2a.adjava.extractors.MExtractor#initialize(org.dom4j.Element)
	 */
	@Override
	public void initialize(Element p_xConfig) throws Exception {

		String sStereotypes = getParameters().getValue("stereotypes");
		for (String sStereotype : sStereotypes.split(",")) {
			sStereotype = sStereotype.trim();
			this.listStereotypes.add(sStereotype);
		}
		sButtonType = getParameters().getValue("buttontype");
	}

	/**
	 * (non-Javadoc)
	 * @see com.a2a.adjava.extractors.MExtractor#extract(com.a2a.adjava.uml.UmlModel)
	 */
	@Override
	public void extract(UmlModel p_oModele) throws Exception {

		ScreenExtractor oScreenExtractor = this.getDomain().getExtractor(ScreenExtractor.class);
		UmlDictionary oUmlDict = p_oModele.getDictionnary();

		MViewModelCreator oViewModelCreator = getDomain().getDictionnary().getViewModelCreator();
		
		// Loop over screens
		for (UmlClass oScreenUmlClass : oScreenExtractor.getScreenContext().getScreenUmlClasses(oUmlDict)) {
			
			// Search for a "panelsearch" usage
			PanelAggregation oSearchPanelAggregation = this.getSearchPanelAggregation(oScreenUmlClass);
			if (oSearchPanelAggregation != null) {
				
				// Find the viewmodel of the screen
				MPackage oMasterPackage = oScreenExtractor.getScreenContext().computeMasterPackage(oScreenUmlClass);
				MPackage oVMPackage = oScreenExtractor.getScreenContext().computeViewModelPackage(oMasterPackage);
				MViewModelImpl oSearchVM = ScreenAggregationPanelProcessor.getInstance().getViewModelByPanelAggregation(
						oSearchPanelAggregation, oVMPackage, false, true, oScreenExtractor.getScreenContext());

				// Get MScreen
				MScreen oListScreen = this.getDomain().getDictionnary().getScreen(oScreenUmlClass.getName());

				// Get Entity linked to viewmodel
				MEntityImpl oEntity = oSearchVM.getEntityToUpdate();

				// Create the MDialog
				// package is the same as the master page of the screen
				String sDialogName = oScreenUmlClass.getName() + "SearchDialog";
				log.debug("Create dialog: {}", sDialogName);
				log.debug("  master package: {}", oMasterPackage.getFullName());
				log.debug("  viewmodel package: {}", oVMPackage.getFullName());
				log.debug("  package: {}", oListScreen.getPackage().getFullName());
				MDialog oDialog = new MDialog(oListScreen, sDialogName, oScreenUmlClass, oListScreen.getPackage(),
						oSearchVM.getMasterInterface(), oSearchVM, oEntity, oViewModelCreator);
				log.debug("  package: {}", oDialog.getPackage().getFullName());
				log.debug("  dialog fullname: {}", oDialog.getFullName());

				String sPath = oScreenUmlClass.getName().toLowerCase() + "search_";
				
				// Analyse the viewmodel of the search panel
				MLayout oSearchLayout = ViewModelAnalyser.getInstance().analyseViewModel(null, null,
						oSearchVM, sPath, null, oDialog, true, false,
						oScreenExtractor.getScreenContext());
				oSearchLayout.setDialog(true);
				oDialog.setLayout(oSearchLayout);
				
				// Add Search Layout for DataTemplate
				completePageWithSearchInfo(oListScreen.getMasterPage(), oSearchLayout);

				// Register the MDialog
				getDomain().getDictionnary().registerDialog(oDialog);

				// Add action for search button
				
				// Add dialog to master page
				oListScreen.getMasterPage().addDialog(oDialog);
				getDomain().getDictionnary().getViewModelCreator().addDialog(oDialog);

				// Add search button on layout
				this.addFilterButton(oDialog, oListScreen);

				// Add ok/cancel button on search layout
				this.addValidationButtons(oSearchLayout, oListScreen.getName());	
			}
		}
	}

	/**
	 * Complete layout of search dialog with cancel/ok buttons
	 * @param oSearchLayout layout to complete
	 * @param p_sBaseName base name for button
	 */
	protected void addValidationButtons(MLayout oSearchLayout, String p_sBaseName) {
		IUITypeDescription oUiTypeDescription = getDomain().getLanguageConf().getUiTypeDescription(
				sButtonType, VersionHandler.getGenerationVersion().getStringVersion());

		String sComponentName = "button" + StringUtils.capitalize(p_sBaseName) + "CancelSearchDialog";
		String sComponent = oUiTypeDescription.getRwComponentType();

		MVisualField oVisualField = getDomain().getXModeleFactory().createVisualField(sComponentName, null,
				sComponent, MVFLabelKind.NO_LABEL, null, false);
		oVisualField.addParameter("text", "buttonCancel");
		oVisualField.addParameter("type", "Cancel");
		oVisualField.addParameter("context", "searchdialog");
		oSearchLayout.addVisualField(oVisualField);

		sComponentName = "button" + StringUtils.capitalize(p_sBaseName) + "ValidateSearchDialog";
		sComponent = oUiTypeDescription.getRwComponentType();
		oVisualField = getDomain().getXModeleFactory().createVisualField(sComponentName, null, sComponent,
				MVFLabelKind.NO_LABEL, null, false);
		oVisualField.addParameter("text", "buttonOk");
		oVisualField.addParameter("type", "Validate");
		oVisualField.addParameter("context", "searchdialog");
		oSearchLayout.addVisualField(oVisualField);

		sComponentName = "button" + StringUtils.capitalize(p_sBaseName) + "ResetSearchDialog";
		sComponent = oUiTypeDescription.getRwComponentType();
		oVisualField = getDomain().getXModeleFactory().createVisualField(sComponentName, null, sComponent,
				MVFLabelKind.NO_LABEL, null, false);
		oVisualField.addParameter("text", "buttonDefault");
		oVisualField.addParameter("type", "Reset");
		oVisualField.addParameter("context", "searchdialog");
		oSearchLayout.addVisualField(oVisualField);

		IModelFactory oModelFactory = getDomain().getXModeleFactory();
		
		getDomain().getDictionnary().addLabel( oModelFactory.createLabelForButton("buttonCancel", "Annuler"));
		getDomain().getDictionnary().addLabel( oModelFactory.createLabelForButton("buttonOk", "Valider"));
		getDomain().getDictionnary().addLabel( oModelFactory.createLabelForButton("buttonDefault", "Defaut"));

	}

	/**
	 * @param p_oListScreen
	 */
	protected void addFilterButton(MDialog p_oDialog, MScreen p_oListScreen) {

		// Get the filter button description
		IUITypeDescription oUiTypeDescription = getDomain().getLanguageConf().getUiTypeDescription(
				"FilterButton", VersionHandler.getGenerationVersion().getStringVersion());
		String sComponentName = "buttonDisplay" + StringUtils.capitalize(p_oDialog.getName());
		String sComponent = oUiTypeDescription.getRwComponentType();

		// Create the visual field for the filter button
		MVisualField oVisualField = getDomain().getXModeleFactory().createVisualField(sComponentName, null,
				sComponent, MVFLabelKind.NO_LABEL, null, false);
		oVisualField.addParameter("dialog", p_oDialog.getFullName());

		p_oListScreen.getMasterPage().getLayout().insertVisualField(0, oVisualField);
	}

	/**
	 * @param p_oScreenUmlClass
	 * @return
	 */
	public PanelAggregation getSearchPanelAggregation(UmlClass p_oScreenUmlClass) {
		PanelAggregation r_oPanelAggregation = null;

		for (UmlAssociationEnd oAssociationEnd : p_oScreenUmlClass.getAssociations()) {
			UmlAssociation oAsso = oAssociationEnd.getAssociation();
			
			if (oAsso.hasAnyStereotype( this.listStereotypes)) {
				if ( oAssociationEnd.getOppositeAssociationEnd().getAggregateType().equals(AggregateType.AGGREGATE)) {
					r_oPanelAggregation = new PanelAggregation(oAssociationEnd);
					break ;
				}
				else {
					MessageHandler.getInstance().addError(
						"Associations between Screen and Panel must be of type Aggregation. Screen: " 
								+ p_oScreenUmlClass.getName()
								+ ", Panel: " + oAssociationEnd.getRefClass().getName());
				}
			}
		}
		return r_oPanelAggregation;
	}
	
	/**
	 * @param p_oScreenUmlClass
	 * * @param p_oLayout
	 * @return
	 */
	public void completePageWithSearchInfo(MPage p_oPage, MLayout p_oLayout) {
		// Nothing to do
	}
	
}
