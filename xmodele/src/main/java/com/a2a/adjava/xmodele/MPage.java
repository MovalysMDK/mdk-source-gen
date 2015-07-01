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

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.dom4j.Element;

import com.a2a.adjava.uml.UmlClass;
import com.a2a.adjava.uml.UmlStereotype;
import com.a2a.adjava.xmodele.ui.menu.MMenu;
import com.a2a.adjava.xmodele.ui.navigation.MNavigation;
import com.a2a.adjava.xmodele.ui.navigation.MNavigationType;
import com.a2a.adjava.xmodele.ui.panel.MPanelOperation;


/**
 * Class representing a panel
 */
public class MPage extends SClass<MViewModelInterface,MMethodSignature> {

	/**
	 * Constant for stereotype Mm_iOS_noTableView
	 */
	public static final String MM_IOS_NO_TABLE= "Mm_iOS_noTableView";

	/**
	 * Parent screen
	 */
	private MScreen parent ;
	
	/**
	 * View model interface
	 */
	private MViewModelInterface vm = null;
	
	/** 
	 * View Model of the panel
	 */
	private MViewModelImpl vmimpl = null;
	
	/**
	 * adapter of the page
	 */
	private MAdapter adapter = null;

	/**
	 * Adapters list used for combo
	 */
	protected Map<String, MAdapter> externalAdapters = null;

	/**
	 * Master component name
	 */
	private String masterComponentName = null;
	
	/**
	 * Master component type
	 */
	private String masterComponentType = null;
	
	/**
	 * Full master component type
	 */
	private String masterComponentTypeFull = null;
	
	/**
	 * Layout used by the panel
	 */
	private MLayout layout = null;
	
	/**
	 * Attached dialogs
	 */
	private List<MDialog> dialogs = new ArrayList<MDialog>();
	
	/**
	 * true if the panel has a title
	 */
	private boolean titled = false;
	
	/**
	 * Uml class of the panel
	 */
	private UmlClass umlClass = null;
	
	/**
	 * Pages Detail
	 */
	private List<MPage> associatedDetails = new ArrayList<MPage>();
	
	/**
	 * Actions
	 */
	private List<MAction> actions = new ArrayList<MAction>() ;


	/**
	 * Navigation
	 */
	private List<MNavigation> navigations = new ArrayList<MNavigation>();
	
	/**
	 * panel operation
	 */
	private List<MPanelOperation> panelOperations = new ArrayList<MPanelOperation>();

	/**
	 * Menus
	 */
	private List<MMenu> menus = new ArrayList<MMenu>();
	
	/**
	 * Constructor for <em>MPage</em>.</p>
	 * @param p_oParent The parent screen of the panel
	 * @param p_sPageName the name of the panel
	 * @param p_oUmlPage the Uml class of the panel
	 * @param p_oPackage the package to set
	 * @param p_oVmImpl the View model to set
	 * @param p_bTitled true if the panel should have a title
	 */
	public MPage( MScreen p_oParent, String p_sPageName, UmlClass p_oUmlPage, MPackage p_oPackage, MViewModelImpl p_oVmImpl, boolean p_bTitled) {
		super("page", p_oUmlPage.getName(), p_sPageName, p_oPackage);
		init(p_oParent, p_sPageName, p_oUmlPage, p_oPackage, p_oVmImpl, p_bTitled);
	}
	
	/**
	 * Constructor for <em>MPage</em>.</p>
	 * @param p_oScreen The parent screen of the panel
	 * @param p_sType the type of the panel
	 * @param p_sPageName the name of the panel
	 * @param p_oUmlPage the uml class of the panel
	 * @param p_oPackage the package to set
	 * @param p_oVmImpl the view model to set
	 * @param p_bTitled true if the panel should have a title
	 */
	protected MPage( MScreen p_oScreen, String p_sType, String p_sPageName, UmlClass p_oUmlPage, MPackage p_oPackage, MViewModelImpl p_oVmImpl, boolean p_bTitled) {
		super(p_sType, p_oUmlPage.getName(), p_sPageName, p_oPackage);
		init( p_oScreen, p_sPageName, p_oUmlPage, p_oPackage, p_oVmImpl, p_bTitled);
	}

	/**
	 * initializes an {@link MPage} object
	 * @param p_oScreen the parent screen of the panel
	 * @param p_sScreenName the name of the panel
	 * @param p_oUmlPage the uml class of the panel
	 * @param p_oPackage the package to set
	 * @param p_oVmImpl the view model to set
	 * @param p_bTitled true if the panel should have a title
	 */
	protected void init( MScreen p_oScreen, String p_sScreenName, UmlClass p_oUmlPage, MPackage p_oPackage, 
			MViewModelImpl p_oVmImpl, boolean p_bTitled) {
		this.parent = p_oScreen ;
		this.umlClass = p_oUmlPage;
		this.vm = p_oVmImpl.getMasterInterface();
		this.vmimpl = p_oVmImpl;
		if(this.vmimpl != null){
			vmimpl.setPage(new WeakReference<MPage>(this));
		}
		if (this.vm!=null) {
			this.addImport(this.vm.getFullName());
		}

		this.externalAdapters = new TreeMap<String, MAdapter>();
		this.titled = p_bTitled;

		// Stereotype conversion
		for (UmlStereotype oUmlStereotype : p_oUmlPage.getStereotypes()) {
			this.addStereotype(new MStereotype(oUmlStereotype.getName(), oUmlStereotype
					.getDocumentation()));
		}
	}
	
	/**
	 * Sets the layout on the panel
	 * @param p_oLayout the layout to set
	 */
	public void setLayout(MLayout p_oLayout) {
		this.layout = p_oLayout;
		if(this.layout != null)
		{
			this.layout.setPage(new WeakReference<MPage>(this));
		}
	}
	
	/**
	 * returns the set layout of the panel
	 * @return the layout of the panel
	 */
	public MLayout getLayout() {
		return this.layout;
	}
	
	/**
	 * Define the Adapter.
	 * @param p_oAdapter Object <code>MAdapter</code>
	 */
	public void setAdapter(MAdapter p_oAdapter) {
		this.adapter = p_oAdapter;
		if (this.adapter!=null) {
			this.addImport(p_oAdapter.getFullName());
		}
	}
	
	/**
	 * Return the current MAdapter object
	 * @return <code>MAdapter</code>
	 */
	public MAdapter getAdapter() {
		return this.adapter;
	}

	/**
	 * Adds an adapter to a given component in the panel
	 * @param p_oAdapter the adapter to set
	 * @param p_sComponentName the name of the component receiving the adapter 
	 */
	public void addExternalAdapter(MAdapter p_oAdapter, String p_sComponentName) {
		if (p_oAdapter != null) {
			this.externalAdapters.put(p_sComponentName, p_oAdapter);
			this.addImport(p_oAdapter.getFullName());
		}
	}
	
	/**
	 * Return the External Adapters list.
	 * @return Map<String,MAdapter>
	 */
	public Map<String, MAdapter> getExternalAdapters(){
		return externalAdapters;
	}

	/**
	 * Returns the view model of the panel
	 * @return the view model of the panel
	 */
	public MViewModelImpl getViewModelImpl() {
		return this.vmimpl;
	}
	
	/**
	 * Sets the master component details
	 * @param p_sMasterComponentType the type of the master component
	 * @param p_sMasterComponentTypeFull the full type name of the master component
	 * @param p_sName the name of the master component
	 */
	public void setMasterComponentName(String p_sMasterComponentType, String p_sMasterComponentTypeFull, String p_sName) {
		this.masterComponentName = p_sName;
		this.masterComponentType = p_sMasterComponentType;
		this.masterComponentTypeFull = p_sMasterComponentTypeFull;
	}
	
	/**
	 * Adds a dialog to the panel
	 * @param p_oDialog the dialog to add
	 */
	public void addDialog( MDialog p_oDialog ) {
		this.dialogs.add(p_oDialog);
	}
	
	/**
	 * Returns the list of dialogs held by the panel
	 * @return the list of dialogs held by the panel
	 */
	public List<MDialog> getDialogs() {
		return dialogs;
	}

	/**
	 * Adds an action to the panel
	 * @param p_oMAction the action to add
	 */
	public void addAction( MAction p_oMAction ) {
		this.actions.add(p_oMAction);
	}
	
	/**
	 * Returns the actions held by the panel
	 * @return the actions held by the panel
	 */
	public List<MAction> getActions() {
		return actions;
	}

	/**
	 * Adds a navigation option to the panel
	 * @param p_oNavigation the navigation to add
	 */
	public void addNavigation( MNavigation p_oNavigation ) {
		this.navigations.add(p_oNavigation);
	}
	
	/**
	 * Adds a menu to the panel
	 * @param p_oMenu the menu to add
	 */
	public void addMenu(MMenu p_oMenu) {
		this.menus.add(p_oMenu);
	}
	
	/**
	 * Returns the list of menu held by the panel
	 * @return the list of menu held by the panel
	 */
	public List<MMenu> getMenus() {
		return this.menus;
	}
	
	
	/**
	 * Returns the screen parent of the panel
	 * @return the screen parent of the panel
	 */
	public MScreen getParent() {
		return parent;
	}

	/**
	 * Returns the list of operations held by the panel
	 * @return the list of operations held by the panel
	 */
	public List<MPanelOperation> getPanelOperations() {
		return this.panelOperations;
	}

	/**
	 * Adds an operation to the panel
	 * @param p_oPanelOperation the operation to add
	 */
	public void addPanelOperation( MPanelOperation p_oPanelOperation ) {
		this.panelOperations.add(p_oPanelOperation);
	}
	
	/**
	 * Test if operation exists in the panel
	 * @param p_sName operation name
	 * @return true if operation exists
	 */
	public boolean hasPanelOperation( String p_sName ) {
		boolean r_bResult = false;
		for( MPanelOperation oOperation : getPanelOperations()) {
			if ( oOperation.getName().equalsIgnoreCase("create")) {
				r_bResult = true;
				break;
			}
		}
		return r_bResult ;
	}
	
	/**
	 * Returns the navigation of a given type if there is one held by the panel, null otherwise 
	 * @param p_oNavigationDetail the type of navigation to look for
	 * @return the found navigation if any, null otherwise
	 */
	public MNavigation getNavigationOfType(MNavigationType p_oNavigationDetail) {
		for( MNavigation oNav: this.navigations) {
			if ( oNav.getNavigationType().equals(p_oNavigationDetail)) {
				return oNav ; 
			}
		}
		return null;
	}

	/**
	 * Return the action of a given type if there is one hjeld by the panel, null otherwise 
	 * @param p_oType the type of action to look for
	 * @return the found action if any, null otherwise
	 */
	public MAction getActionOfType(MActionType p_oType) {
		for (MAction oAction : this.actions) {
			if (oAction.getType().equals(p_oType)) {
				return oAction;
			}
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void toXmlInsertBeforeDocumentation(Element p_xElement) {
		super.toXmlInsertBeforeDocumentation(p_xElement);
		p_xElement.addElement("name-lowercase").setText(this.getName().toLowerCase());
		p_xElement.addElement("layout-pagename").setText(this.layout.getName());
		p_xElement.add(this.layout.toXml());
		p_xElement.addElement("screenname").setText(this.layout.getName());
		
		Element xStereotype = p_xElement.addElement("stereotypes");
		for( MStereotype oStereotype : this.getStereotypes() ) {
			xStereotype.add(oStereotype.toXml());
		}
		
		this.toXmlParentScreen(p_xElement);
		
		if (this.vm!=null) {
			p_xElement.addElement("vm").setText(this.vm.getName());
			p_xElement.add(this.vm.toXml());
		}
		if (this.vmimpl!=null) {
			p_xElement.add(this.vmimpl.toXml());
		}
		if (this.masterComponentName!=null) {
			p_xElement.addElement("mastercomponentname").setText(this.masterComponentName);
		}
		if ( this.masterComponentType != null ) {
			p_xElement.addElement("mastercomponenttype").setText(this.masterComponentType);
			p_xElement.addElement("mastercomponenttypeFull").setText(this.masterComponentTypeFull);
		}
		if (this.adapter!=null) {
			p_xElement.add(this.adapter.toXml());
		}
		if (this.externalAdapters != null && !this.externalAdapters.isEmpty()) {
			Element xExternalAdapters	= p_xElement.addElement("external-adapters");
			Element xExternalAdapter	= null;
			for (Map.Entry<String, MAdapter> oAdapter : this.externalAdapters.entrySet()) {
				xExternalAdapter = oAdapter.getValue().toXml();
				xExternalAdapter.addAttribute("component-ref", oAdapter.getKey());
				xExternalAdapters.add(xExternalAdapter);
			}
		}
		Element xDialogs = p_xElement.addElement("dialogs");
		for( MDialog oDialog : this.dialogs ) {
			xDialogs.add( oDialog.toXml());
		}
		Element xActions = p_xElement.addElement("actions");
		for( MAction oAction : this.actions ) {
			xActions.add( oAction.toXml());
		}
		
		Element xMenus = p_xElement.addElement("menus");
		for (MMenu oMenu : this.menus) {
			xMenus.add(oMenu.toXml());
		}
		
		Element xNavs = p_xElement.addElement("navigations");
		for( MNavigation oNavigation : this.navigations ) {
			xNavs.add(oNavigation.toXml());
		}
		
		Element xPagesDetails = p_xElement.addElement("pages-details");
		for( MPage oPage : this.associatedDetails ) {
			Element xPage = xPagesDetails.addElement("page");
			xPage.addAttribute("name", oPage.getName());
			if ( oPage.getViewModelImpl() != null && oPage.getViewModelImpl().getMasterInterface() != null) {
				xPage.addAttribute("associated-viewmodel", oPage.getViewModelImpl().getMasterInterface().getName());
			}
		}
	}
	
	/**
	 * Returns the xml of the panel
	 * @param p_xElement the parent xml element
	 */
	protected void toXmlParentScreen(Element p_xElement) {
		p_xElement.addElement("in-multi-panel").setText(Boolean.toString(this.getParent().isMultiPanel()));
		p_xElement.addElement("in-workspace").setText(Boolean.toString(this.getParent().isWorkspace()));
	}

	/**
	 * Returns true if the panel has a title
	 * @return true if the panel has a title
	 */
	public boolean isTitled() {
		return this.titled;
	}
	
	/**
	 * Returns the umlClass of the panel
	 * @return the umlClass of the panel
	 */
	public UmlClass getUmlClass() {
		return umlClass;
	}

	public List<MPage> getAssociatedDetails() {
		return associatedDetails;
	}

	public void setAssociatedDetails(List<MPage> associatedDetails) {
		this.associatedDetails = associatedDetails;
	}

}
