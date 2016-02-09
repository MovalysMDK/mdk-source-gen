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
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.dom4j.Element;

import com.a2a.adjava.AdjavaException;


/**
 * Permet de contruire un view model
 * @author smaitre
 *
 */
public class MAdapter extends SClass<MViewModelInterface,MMethodSignature> {
	
	private String shortAdapter = null;
	
	private MViewModelImpl model = null;
	
	private String path = null; //lien du view model principale vers le view model en question
	
	private Map<String, MLayout> layouts = null;
	
	/**
	 * 
	 */
	private List<MAction> actions = null;
	
	private String checkedField = null;
	
	/**
	 * Construit un nouveau view model
	 * @param p_sName
	 * @param p_oPackage
	 */
	public MAdapter(String p_sName, MPackage p_oPackage, String p_sShortAdapterName, String p_sLongAdapterName) {
		super("adapter", null, p_sName, p_oPackage);
		this.addImport(p_sLongAdapterName);
		this.shortAdapter = p_sShortAdapterName;
		this.layouts = new LinkedHashMap<String,MLayout>(); // linked hashmap because xsl uses position to select a layout
		this.actions = new ArrayList<MAction>();
	}
	
	/**
	 * @param p_oVm
	 * @param p_opath
	 */
	public void setViewModel(MViewModelImpl p_oVm, String p_opath) {
		model = p_oVm;
		path = p_opath;
	}
	
	/**
	 * @param p_oLayout
	 */
	public void addLayout( String p_sId, MLayout p_oLayout) throws AdjavaException {
		p_oLayout.setAdapter(this);
		if ( this.layouts.containsKey(p_sId)) {
			throw new AdjavaException("Adapter '{}' already contains a layout with id : {}", this.getName(), p_sId );
		}
		else {
			this.layouts.put(p_sId, p_oLayout);
		}
	}
	
	/**
	 * @param p_oAction
	 */
	public void addAction(MAction p_oAction) {
		this.actions.add(p_oAction);
		this.addImport(p_oAction.getMasterInterface().getFullName());
	}
	
	/**
	 * @return
	 */
	public Collection<MLayout> getLayouts() {
		return layouts.values();
	}
	
	/**
	 * @return
	 */
	public MLayout getLayout( String p_sName ) {
		return layouts.get(p_sName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void toXmlInsertBeforeDocumentation(Element p_xElement) {
		super.toXmlInsertBeforeDocumentation(p_xElement);
		if ( this.shortAdapter != null ) {
			p_xElement.addElement("short-adapter").setText(this.shortAdapter);
		}
		p_xElement.add(model.toXml());
		p_xElement.addElement("path").setText(path);
		Element xLayouts = p_xElement.addElement("layouts");
		for( Entry<String,MLayout> oEntryLayout : this.layouts.entrySet()) {
			Element xLayout = oEntryLayout.getValue().toXml();
			xLayout.addAttribute("id", oEntryLayout.getKey());
			xLayouts.add(xLayout);
		}
		if (!this.actions.isEmpty()) {
			Element xActions = p_xElement.addElement("actions");
			for (MAction oAction : this.actions) {
				xActions.add(oAction.toXml());
			}
		}
		
		if (this.checkedField!=null) {
			p_xElement.addElement("checked-field").setText(checkedField);
		}
	}
	
	/**
	 * Retourne l'objet model
	 * @return Objet model
	 */
	public MViewModelImpl getViewModel() {
		return this.model;
	}

	/**
	 * Ajoute le nom du champ à checker, le nom peut être null
	 * @param fullName
	 */
	public void setFieldToChecked(String p_sName) {
		this.checkedField = p_sName;
	}
}