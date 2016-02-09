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
import java.util.List;

import org.dom4j.Element;

/**
 * Viewmodel interface
 * @author smaitre
 *
 */
public class MViewModelInterface extends SInterface {

	private String subVM ;
	private String sub ;
	
	/**
	 * Entity type to update 
	 */
	private MEntityInterface entityToUpdate ;

	/**
	 * Viewmodel of workspace
	 */
	private boolean workspaceViewModel = false;
	
	/**
	 * Viewmodel of multi section
	 */
	private boolean multiPanelViewModel = false ;
	
	/**
	 * List of sub viewmodels
	 */
	private List<MViewModelInterface> subvmi = new ArrayList<MViewModelInterface>();

	/**
	 * multiInstance view model
	 */
	private boolean multiInstance = false;
	
	/**
	 * javadoc on object
	 */
	private String documentation = "";
	
	/**
	 * Construit une nouvelle interface
	 * @param p_sName
	 * @param p_oPackage
	 */
	public MViewModelInterface(String p_sName, MPackage p_oPackage) {
		super("viewmodel-interface", null, p_sName, p_oPackage);
	}
	
	/**
	 * @param p_oMMethodSignature
	 */
	@Override
	public void addMethodSignature(MMethodSignature p_oMMethodSignature) {
		for( MMethodSignature oMethod : getMethodSignatures()) {
			if ( oMethod.getName().equals(p_oMMethodSignature.getName())) {
				throw new RuntimeException("Method " + p_oMMethodSignature.getName() + " already exists on " + this.getName());
			}
		}
		super.addMethodSignature(p_oMMethodSignature);
	}
	
	/**
	 * Permet d'ajouter un subview model
	 * @param p_oSubVm le subviewmodel à ajouter
	 */
	public void addSubViewModel(MViewModelInterface p_oSubVm) {
		this.subvmi.add(p_oSubVm);
	}
	
	/**
	 * Retourne true si le view model courant correspond au Screen principal du workspace.
	 * @return Objet screenWorkspace true si le VM est celui du Workspace, false sinon.
	 */
	public boolean isScreenWorkspace() {
		return this.workspaceViewModel;
	}
	
	/**
	 * Retourne true si le view model courant correspond au Screen principal du workspace.
	 * @return Objet screenWorkspace true si le VM est celui du Workspace, false sinon.
	 */
	public boolean isScreenMultiPanel() {
		return this.multiPanelViewModel;
	}

	/**
	 * Permet de définir si le ViewModel courant correspond au Screen principal du Workspace.
	 * @param p_bScreenWorkspace true ou false
	 */
	public void setScreenWorkspace(boolean p_bScreenWorkspace) {
		this.workspaceViewModel = p_bScreenWorkspace;
	}
	
	/**
	 * Permet de définir si le ViewModel courant correspond au Screen principal du Workspace.
	 * @param p_bScreenWorkspace true ou false
	 */
	public void setScreenMultiPanel(boolean p_bScreenMultiPanel) {
		this.multiPanelViewModel = p_bScreenMultiPanel;
	}

	public void setSubVM(String name, String fullName) {
		this.subVM = name;
		this.addImport(fullName);
	}
	
	public void setSub(String name, String fullName) {
		this.sub = name;
		this.addImport(fullName);
	}
	
	/**
	 * @param p_oTypeToUpdate
	 */
	public void setEntityToUpdate(MEntityInterface p_oEntityToUpdate) {
		this.entityToUpdate = p_oEntityToUpdate;
		this.addImport(p_oEntityToUpdate.getFullName());
	}
	
	public MEntityInterface getEntityToUpdate() {
		return this.entityToUpdate;
	}
	
	public String getSubVM() {
		return subVM;
	}
	
	public String getSub(){
		return sub;
	}

	@Override
	public void addLinkedInterface(MLinkedInterface p_oInterface) {
		super.addLinkedInterface(p_oInterface);
	}

	public void setMultiInstance(boolean p_bMultiInstance) {
		this.multiInstance = p_bMultiInstance;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void toXmlInsertBeforeDocumentation(Element p_xElement) {
		super.toXmlInsertBeforeDocumentation(p_xElement);
		p_xElement.addElement("multiInstance").setText(String.valueOf(this.multiInstance));
		if (this.sub!=null) {
			p_xElement.addElement("subvm").setText(subVM);
			p_xElement.addElement("sub").setText(sub);
		}
		if (!this.subvmi.isEmpty()) {
			Element oSubvmis = p_xElement.addElement("subvmis");
			Element oSubvmi = null;
			for(MViewModelInterface oSub : this.subvmi) {
				oSubvmi = oSubvmis.addElement("subvmi");
				oSubvmi.addElement("name").setText(oSub.getName());
			}
		}
		if (this.entityToUpdate!=null) {
			p_xElement.addElement("type-to-update").setText(this.entityToUpdate.getName());
		}
	}

	/**
	 * Test is this method signature name already exists in this MV
	 */
	public boolean hasMethodeSignature(String computeGetterNameForParentVm) {
		for (MMethodSignature signature : getMethodSignatures())
		{
			if(signature.getName().equals(computeGetterNameForParentVm))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * sets the javadoc on the view model
	 * @param p_sDocumentation the javadoc to set
	 */
	public void setDocumentation(String p_sDocumentation) {
		super.setDocumentation(p_sDocumentation);
		this.documentation = p_sDocumentation;
	}
	
	/**
	 * gets the javadoc on the view model
	 * @return the javadoc on the view model
	 */
	public String getDocumentation() {
		return this.documentation;
	}
}
