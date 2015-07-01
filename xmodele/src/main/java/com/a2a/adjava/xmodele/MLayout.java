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

import org.dom4j.Element;

import com.a2a.adjava.utils.VersionHandler;
import com.a2a.adjava.xmodele.ui.component.MAbstractButton;
import com.a2a.adjava.xmodele.ui.viewmodel.ViewModelType;

/**
 * Class representing a layout
 */
public class MLayout extends SGeneratedElement {
	
	/**
	 * Field lists
	 */
	private List<MVisualField> fields ;
	
	/**
	 * prefix used to name the layout
	 */
	private String prefix = null;
	
	/**
	 * short name of the layout
	 */
	private String shortName = null;
	
	/**
	 * Workspace layout
	 */
	private boolean workspace = false;
	
	/**
	 * Dialog
	 */
	private boolean dialog = false;
	
	/**
	 * Title
	 */
	private String title = null;
	
	/**
	 * Action buttons 
	 */
	private List<MAbstractButton> buttons = null;

	/**
	 * Page 
	 */
	private WeakReference<MPage> page = null;
	
	/**
	 * Screen 
	 */
	private WeakReference<MScreen> screen = null;
	
	/**
	 * list of adapters used by combos
	 */
	private List<WeakReference<MAdapter>> externalAdapters = new ArrayList<WeakReference<MAdapter>>();
	
	/**
	 * adapter
	 */
	private WeakReference<MAdapter> adapter = null;
	
	/**
	 * Stereotypes
	 */
	private List<MStereotype> stereotypes = new ArrayList<>();
	
	/**
	 * {@link MLayout} constructor
	 * @param p_sName the name of the layout
	 * @param p_sPrefix the prefix of the layout
	 * @param p_sShortName the short name of the layout
	 */
	protected MLayout(String p_sName, String p_sPrefix, String p_sShortName) {
		super("layout", null, p_sName);
		this.fields = new ArrayList<MVisualField>();
		this.prefix = p_sPrefix;
		this.shortName = p_sShortName;
		this.buttons = new ArrayList<MAbstractButton>();
	}
	
	/**
	 * {@link MLayout} constructor
	 * @param p_sName the name of the layout
	 * @param p_sPrefix the prefix of the layout
	 * @param p_sShortName the short name of the layout
	 * @param p_sTitle the title of the layout
	 */
	protected MLayout(String p_sName, String p_sPrefix, String p_sShortName, String p_sTitle) {
		this(p_sName, p_sPrefix, p_sShortName);
		this.title = p_sTitle;
	}
	
	/**
	 * {@link MLayout} constructor
	 */
	private MLayout(){
		this.fields = new ArrayList<MVisualField>();
		this.buttons = new ArrayList<MAbstractButton>();
		this.externalAdapters = new ArrayList<WeakReference<MAdapter>>();
	}
	
	/**
	 * Adds a button to the layout
	 * @param p_oAbstractButton the button to add
	 */
	public void addButton( MAbstractButton p_oAbstractButton ) {
		this.buttons.add(p_oAbstractButton);
	}
	
	/**
	 * returns the list of {@link MVisualField} held by the layout
	 * @return the list of {@link MVisualField} held by the layout
	 */
	public List<MVisualField> getFields() {
		return this.fields;
	}
	
	/**
	 * returns the short name of the layout
	 * @return the short name of the layout
	 */
	public String getShortName(){
		return this.shortName;
	}
	
	/**
	 * true if the layout is a workspace
	 * @return true if it is a workspace
	 */
	public boolean isWorkspace() {
		for (MVisualField oField : fields) {
			
			if ( oField.getComponent().equals(
					ViewModelType.WORKSPACE_MASTERDETAIL.getParametersByConfigName(ViewModelType.DEFAULT, VersionHandler.getGenerationVersion().getStringVersion()).getVisualComponentNameFull()) ||
					oField.getComponent().equals(
					ViewModelType.WORKSPACE_DETAIL.getParametersByConfigName(ViewModelType.DEFAULT, VersionHandler.getGenerationVersion().getStringVersion()).getVisualComponentNameFull())) {
				return true;
			}
		}
		return this.workspace;
	}

	/**
	 * adds a visual field to the layout
	 * @param p_oVf the visual field to add
	 */
	public void addVisualField( MVisualField p_oVf ) {
		this.fields.add( p_oVf );
	}
		
	/**
	 * inserts a visual field at a given position
	 * @param p_iIndex the position of the inserted visual field
	 * @param p_oVf the visual field to insert
	 */
	public void insertVisualField( int p_iIndex, MVisualField p_oVf ) {
		this.fields.add(p_iIndex, p_oVf);
	}
		
	/**
	 * returns the list of buttons held by the layout 
	 * @return the list of buttons held by the layout 
	 */
	public List<MAbstractButton> getButtons() {
		return this.buttons;
	}
	
	/**
	 * returns the title of the layout
	 * @return  title of the layout
	 */
	public String getTitle(){
		return this.title;
	}

	/**
	 * Sets the stereotypes from the page on the layout
	 * @param p_oStereotypes the {@link MStereotype} list to set
	 */
	public void setStereotypes(List<MStereotype> p_oStereotypes) {
		this.stereotypes = p_oStereotypes;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void toXmlInsertBeforeDocumentation(Element p_xElement) {
		super.toXmlInsertBeforeDocumentation(p_xElement);
		p_xElement.addElement("prefix").setText(prefix);
		p_xElement.addElement("shortname").setText(shortName);
		
		Element xStereotype = p_xElement.addElement("stereotypes");
		for( MStereotype oStereotype : this.stereotypes ) {
			xStereotype.add(oStereotype.toXml());
		}

		if (title != null) {
			p_xElement.addElement("title").setText(title);
		}
		Element xVisualFields = p_xElement.addElement("visualfields");
		for(MVisualField oVf : fields) {
			xVisualFields.add(oVf.toXml());
		}
		Element xButtons = p_xElement.addElement("buttons");
		for(MAbstractButton oAbstractButton : this.buttons) {
			xButtons.add(oAbstractButton.toXml());
		}
	}

	/**
	 * Returns true if the layout holds a multipanel
	 * @return true if the layout holds a multipanel
	 */
	public boolean isMultiPanel() {
		
		for (MVisualField oField : fields) {
			
			if ( oField.getComponent().equals(
					ViewModelType.MULTIPANEL.getParametersByConfigName(ViewModelType.DEFAULT, VersionHandler.getGenerationVersion().getStringVersion()).getVisualComponentNameFull()) ) {
				return true;
			}
		}
		
		return false;
	}

	public WeakReference<MPage> getPage() {
		return page;
	}

	public void setPage(WeakReference<MPage> page) {
		this.page = page;
	}

	public WeakReference<MScreen> getScreen() {
		return screen;
	}

	public void setScreen(WeakReference<MScreen> screen) {
		this.screen = screen;
	}
	
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	

	public void setExternalAdapters(WeakReference<MAdapter> externalAdapter) {
		if(!externalAdapters.contains(externalAdapter))
		{
			this.externalAdapters.add(externalAdapter);
		}
	}

	public List<WeakReference<MAdapter>> getExternalAdapters() {
		return externalAdapters;
	}
	
	public MLayout cloneLayout(){
		MLayout r_oMLayout = new MLayout();
		r_oMLayout.buttons.addAll(this.buttons);
		r_oMLayout.fields.addAll(this.fields);
		r_oMLayout.externalAdapters.addAll(this.externalAdapters);
		r_oMLayout.page = this.page;
		r_oMLayout.prefix = this.prefix;
		r_oMLayout.screen = this.screen;
		r_oMLayout.shortName = this.shortName;
		r_oMLayout.title = this.title;
		r_oMLayout.umlName = this.umlName;
		r_oMLayout.workspace = this.workspace;
		r_oMLayout.adapter = new WeakReference<MAdapter>(this.adapter.get());
		this.copyTo(r_oMLayout);
		return r_oMLayout;
	}

	public WeakReference<MAdapter> getAdapter() {
		return adapter;
	}

	public void setAdapter(MAdapter adapter) {
		this.adapter = new WeakReference<MAdapter>(adapter);
	}

	public boolean isDialog() {
		return dialog;
	}

	public void setDialog(boolean dialog) {
		this.dialog = dialog;
	}
	
}
