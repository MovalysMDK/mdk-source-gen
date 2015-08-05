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
package com.a2a.adjava.languages.html5.extractors;

import java.util.List;

import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.languages.html5.extractors.views.ExitStateDlg;
import com.a2a.adjava.languages.html5.extractors.views.ViewBasicAttrDlg;
import com.a2a.adjava.languages.html5.extractors.views.ViewComboDlg;
import com.a2a.adjava.languages.html5.extractors.views.ViewFixedListDlg;
import com.a2a.adjava.languages.html5.extractors.views.ViewFormDlg;
import com.a2a.adjava.languages.html5.extractors.views.ViewListDlg;
import com.a2a.adjava.languages.html5.xmodele.MH5Attribute;
import com.a2a.adjava.languages.html5.xmodele.MH5Dictionary;
import com.a2a.adjava.languages.html5.xmodele.MH5Domain;
import com.a2a.adjava.languages.html5.xmodele.MH5ModeleFactory;
import com.a2a.adjava.languages.html5.xmodele.MH5PanelView;
import com.a2a.adjava.languages.html5.xmodele.MH5ScreenView;
import com.a2a.adjava.languages.html5.xmodele.MH5View;
import com.a2a.adjava.types.UITypeDescription;
import com.a2a.adjava.uml.UmlModel;
import com.a2a.adjava.uml2xmodele.extractors.AbstractExtractor;
import com.a2a.adjava.xmodele.MActionType;
import com.a2a.adjava.xmodele.MPage;
import com.a2a.adjava.xmodele.MScreen;
import com.a2a.adjava.xmodele.MStereotype;
import com.a2a.adjava.xmodele.MViewModelImpl;
import com.a2a.adjava.xmodele.MVisualField;
import com.a2a.adjava.xmodele.ui.viewmodel.ViewModelType;


/**
 * html5 view extractor
 * @author ftollec
 *
 */
public class ViewExtractor extends AbstractExtractor<MH5Domain<MH5Dictionary, MH5ModeleFactory>> {

	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(ViewExtractor.class);


	/**
	 * Delegate for computing exit state
	 */
	private ExitStateDlg exitStateDlg ;
	
	/**
	 * Delegate for fixed list
	 */
	private ViewFixedListDlg viewFixedListDlg;
	
	/**
	 * Delegate for combo
	 */
	private ViewComboDlg viewComboDlg;
	
	/**
	 * Delegate for combo
	 */
	private ViewBasicAttrDlg viewBasicAttrDlg;
	
	/**
	 * Delegate for list panel
	 */
	private ViewListDlg viewListDlg;
	
	/**
	 * Delegate for form panel
	 */
	private ViewFormDlg viewFormDlg;

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.extractors.MExtractor#initialize(org.dom4j.Element)
	 */
	@Override
	public void initialize(Element p_xConfig) throws Exception {
		this.exitStateDlg = new ExitStateDlg();
		this.viewFixedListDlg = new ViewFixedListDlg(this);
		this.viewComboDlg = new ViewComboDlg(this);
		this.viewBasicAttrDlg = new ViewBasicAttrDlg();
		this.viewListDlg = new ViewListDlg(this);
		this.viewFormDlg = new ViewFormDlg();
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.extractors.MExtractor#extract(com.a2a.adjava.uml.UmlModel)
	 */
	@Override
	public void extract(UmlModel p_oModele) throws Exception {

		// Create views from screens
		for( MScreen oScreen : this.getDomain().getDictionnary().getAllScreens()) {		
			this.createViewFromScreen(oScreen);
		}
	}

	
	/**
	 *  MH5View factory in case of a Screen View. Create and fill this view with screen data
	 *  This also triggers the creation of the MH5View of this screen panels 
	 * @param p_oScreen screen
	 * @return MH5View the view of this screen
	 */
	protected MH5View createViewFromScreen( MScreen p_oScreen ) {
		if(p_oScreen == null){
			return null;
		}
		log.debug("ViewExtractor.createViewFromScreen : {}", p_oScreen.getName());

		MH5Dictionary oMH5Dictionary = this.getDomain().getDictionnary();
		
		MH5ScreenView r_oNewView = new MH5ScreenView();
		r_oNewView.setName(p_oScreen.getName());
		r_oNewView.setViewName("view_" + p_oScreen.getName());
		r_oNewView.setIsScreen(true);
		r_oNewView.setIsMainScreen(p_oScreen.isMain());
		r_oNewView.setIsWorkspace(p_oScreen.isWorkspace());
		r_oNewView.setMainScreenName(oMH5Dictionary.getMainScreen().getName());
		
		if (p_oScreen.isWorkspace()) {
			MPage oPage = this.getFirstSaveDetail(p_oScreen);
			if (oPage != null) {
				r_oNewView.setHasSaveAction(true);
			}
		}
		
		r_oNewView.setNavigationFromScreenList(oMH5Dictionary.getNavigationsFromScreen(p_oScreen));
		r_oNewView.setNavigationToScreenList(oMH5Dictionary.getNavigationsToScreen(p_oScreen));
		this.exitStateDlg.computeExitStateForScreen(p_oScreen, r_oNewView, getDomain());
		r_oNewView.setMenuList( p_oScreen.getMenus());
		
		// add label for screen title
		oMH5Dictionary.addLabel(getDomain().getXModeleFactory().createLabelForScreen(p_oScreen));
		
		//Creation of all the panel views
		boolean markedFirst = false;
		for( MPage oPage : p_oScreen.getPages()) {
			boolean isFirstDetail = false;
			if (p_oScreen.isWorkspace() && !markedFirst && oPage.getViewModelImpl().getType().equals(ViewModelType.MASTER)) {
				isFirstDetail = true;
				markedFirst = true;
			}
			r_oNewView.addNestedSubview(oPage.getName(), oPage.getViewModelImpl().getType(), isFirstDetail);
			this.createViewFromPanel(oPage);
		}
		
		// register view in dictionary
		oMH5Dictionary.registerMH5View(r_oNewView);

		return r_oNewView ;
	}
	
	/**
	 * return the first page with a save action
	 * @param p_oScreen
	 * @return
	 */
	private MPage getFirstSaveDetail(MScreen p_oScreen) {
		MPage r_oPage = null;
		for (MPage oPage : p_oScreen.getPages()) {
			if (
				(oPage.getViewModelImpl().getType().equals(ViewModelType.MASTER) 
				|| oPage.getViewModelImpl().getType().equals(ViewModelType.DEFAULT))
				&& oPage.getActionOfType(MActionType.SAVEDETAIL) != null) {
					r_oPage = oPage;
					break;
			}
		}
		return r_oPage;
	}

	/**
	 * MH5View factory in case of a Panel View. Create and fill this view with panel data
	 * @param p_oPage
	 * @return
	 */
	protected MH5View createViewFromPanel( MPage p_oPage ) {
		log.debug("ViewExtractor.createViewFromPanel : {}", p_oPage.getName());
		
		// ----- CREATION OF THE VIEW PANEL -------
		MH5PanelView r_oNewView ;
		String sViewName = "view_" + p_oPage.getName();
		
		if(p_oPage.getViewModelImpl().getType().isList()){
			//In case of a List panel
			r_oNewView = this.viewListDlg.createPanelViewForList(p_oPage, getDomain().getDictionnary(), sViewName);			
		}
		else{
			r_oNewView = this.viewFormDlg.createPanelViewForForm(p_oPage);		
		}
		
		// ----- FILLING OF THE VIEW PANEL -------
		r_oNewView.setName(p_oPage.getName());
		getDomain().getDictionnary().addLabel(getDomain().getXModeleFactory().createLabelForPage(p_oPage));
		
		r_oNewView.setViewName(sViewName);
		r_oNewView.setIsScreen(false);
		this.exitStateDlg.computeExitStateForPanel(p_oPage, r_oNewView, getDomain().getDictionnary().getMainScreen());
		r_oNewView.setMainScreenName( this.getDomain().getDictionnary().getMainScreen().getName());
		r_oNewView.setType(p_oPage.getViewModelImpl().getType().name());
		r_oNewView.setPanelOfMultiSection(p_oPage.getParent().isMultiPanel());
		r_oNewView.setFirstPanelOfMultiSection(r_oNewView.isPanelOfMultiSection() && p_oPage.getParent().getPages().indexOf(p_oPage) ==0);
		r_oNewView.setPanelOfWorkspace(p_oPage.getParent().isWorkspace());
		
		r_oNewView.setScreenName(p_oPage.getParent().getName());
		//TODO : add screen 
		
		if(p_oPage.getViewModelImpl().getEntityToUpdate() != null)
		{	
			r_oNewView.setAttachedToEntity(true);
			for(MStereotype p_oStereotype : p_oPage.getViewModelImpl().getEntityToUpdate().getStereotypes()){
				if(p_oStereotype.getName().equalsIgnoreCase("Mm_applicationScope")){
					r_oNewView.setApplicationScopeEntityAttached(p_oPage.getViewModelImpl().getEntityToUpdate().getName());
					break;
				}else{
					r_oNewView.setApplicationScopeEntityAttached(null);
				}
			}
			
		}else{
			r_oNewView.setAttachedToEntity(false);
		}
		
		
		this.computeViewAttributes(p_oPage, r_oNewView);
		
		this.getDomain().getDictionnary().registerMH5View(r_oNewView);
		
		return r_oNewView ;
	}
	
	/**
	 * If the MH5View is a panel Create and Fill the attributes of this panel 
	 * @param p_oPage the panel corresponding to this new MH5View
	 * @param p_oNewView the MH5 view we want to fill
	 */
	private void computeViewAttributes(MPage p_oPage,
			MH5PanelView p_oNewView) {
		this.computeViewAttributes(p_oPage.getLayout().getFields(), p_oPage.getViewModelImpl(), p_oNewView.getSectionAttributes(),
				p_oNewView.getName());
	}
	
	/**
	 * Compute the list of MH5Attributes from the list of visual fields
	 * @param p_listVisualFields list of visual fields
	 * @param p_oViewModel viewmodel
	 * @param p_oMH5AttributeContainer computed attributes are added to this list
	 * @param p_sViewName view name
	 */
	public void computeViewAttributes( List<MVisualField> p_listVisualFields, MViewModelImpl p_oViewModel, 
			List<MH5Attribute> p_oMH5AttributeContainer, String p_sViewName ) {
		
		for( MVisualField oVisualF : p_listVisualFields ) {
			
			MH5Attribute oMH5Attr = this.computeMH5Attribute(oVisualF, p_oViewModel, p_sViewName);
			p_oMH5AttributeContainer.add(oMH5Attr);
		}
	}
	
	
	/**
	 * Create MH5Attribute from visual field
	 * @param p_oVisualField visual field
	 * @param p_oViewModel viewmodel owner of the visual field
	 * @param p_sViewName view name
	 * @return MH5Attribute
	 */
	private MH5Attribute computeMH5Attribute( MVisualField p_oVisualField, MViewModelImpl p_oViewModel, String p_sViewName ) {
		MH5Attribute r_oMH5Attribute = null;
		
		// Combo
		if( this.isCombo(p_oVisualField)) {
			r_oMH5Attribute = this.viewComboDlg.createMH5AttributeForCombo(p_oVisualField, p_oViewModel, p_sViewName);	
			
		}
		// Fixed List
		else if ( this.isFixedList(p_oVisualField)){
			r_oMH5Attribute = this.viewFixedListDlg.createMH5AttributeForFixedLists(p_oVisualField, p_oViewModel, p_sViewName);
			
		// List 1/2/3
		} else if ( this.isMasterList(p_oVisualField)) {
			r_oMH5Attribute = this.viewListDlg.createMH5AttributeForListAttribute(p_oVisualField, p_oViewModel, p_sViewName);
			
		// Default case
		} else {
			r_oMH5Attribute = this.viewBasicAttrDlg.createMH5AttributeForBasicAttribute(p_oVisualField, p_oViewModel, p_sViewName);
		}
		
		//  Add options from ui type. (visualfield of "list" has no uml name)
		if ( p_oVisualField.getUmlName() != null ) {
			UITypeDescription oUITypeDesc = (UITypeDescription) this.getLngConfiguration().getUiTypeDescriptionByUmlName(p_oVisualField.getUmlName());
			r_oMH5Attribute.setComplementaryOptions(oUITypeDesc.getRWLanguageTypeOptions());
		}
			
		// Create label for visual field
		if (p_oVisualField.getLabel() != null ) {
			getDomain().getDictionnary().addLabel(p_oVisualField.getLabel());
		}
		
		return r_oMH5Attribute;
	}

	/**
	 * Return true if visual field is a combo
	 * @param p_oVisualF visual field
	 * @return
	 */
	private boolean isCombo(MVisualField p_oVisualF) {
		return p_oVisualF.getParameters().get("combo") != null && p_oVisualF.getParameters().get("combo").equals("true");
	}

	/**
	 * Return true if visual field is a fixed list
	 * @param p_oVisualF visual field
	 * @return
	 */
	private boolean isFixedList(MVisualField p_oVisualF) {
		return p_oVisualF.getParameters().get("fixedList") != null && p_oVisualF.getParameters().get("fixedList").equals("true");
	}
	
	/**
	 * Return true if visual field is a master list
	 * @param p_oVisualField visual field
	 * @return
	 */
	private boolean isMasterList(MVisualField p_oVisualField) {
		return p_oVisualField.getName().endsWith("__list__value");
	}
}
