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

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;

import com.a2a.adjava.utils.StrUtils;
import com.a2a.adjava.utils.VersionHandler;
import com.a2a.adjava.xmodele.ui.viewmodel.ViewModelType;
import com.a2a.adjava.xmodele.ui.viewmodel.ViewModelTypeConfiguration;
import com.a2a.adjava.xmodele.ui.viewmodel.mappings.IVMMappingDesc;
import com.a2a.adjava.xmodele.ui.viewmodel.mappings.MMapping;

/**
 * View model
 * @author smaitre
 *
 */
public class MViewModelImpl extends SClass<MViewModelInterface,MMethodSignature> {

	/**
	 * beginning of the method name that affect a ViewModel List 
	 */
	public static final String PREFIX_LIST_SET_ACCESSOR = "setLstVM";

	/**
	 * beginning of the method name that return a ViewModel List
	 */
	public static final String PREFIX_LIST_GET_ACCESSOR = "getLstVM";

	/**
	 * beginning of the method name that affect a ViewModel
	 */
	public static final String PREFIX_SET_ACCESSOR = "setVM";
	/**
	 * beginning of the method name that return a ViewModel
	 */
	public static final String PREFIX_GET_ACCESSOR = "getVM";
	
	/**
	 * Viewmodel type
	 */
	private ViewModelType type ;

	/**
	 * Subview model list
	 */
	private List<MViewModelImpl> subViewModels = new ArrayList<>();

	/**
	 * Entity type to update 
	 */
	private MEntityImpl entityToUpdate ;
	
	/**
	 * Visual fields
	 */
	private List<MVisualField> visualFields = new ArrayList<>();
	
	/**
	 * Mapping configuration 
	 */
	private IVMMappingDesc mappings ;
	
	/**
	 * Load Cascade 
	 */
	private List<MCascade> cascades = new ArrayList<>();
	
	/**
	 * Save cascade 
	 */
	private List<MCascade> saveCascades = new ArrayList<>();
	
	/**
	 * Cascade imports 
	 */
	private List<String> importCascades = new ArrayList<>();

	/**
	 * Other viewmodels used by viewmodel (for combo) 
	 */
	private List<MViewModelImpl> externalViewModels = new ArrayList<>();

	/**
	 * Name of the property : it is the UML name capitalized.
	 */
	private String propertyName;

	/**
	 * Base name for accessors
	 * ex: fooBar pour getFooBar et setFooBar
	 */
	private String accessorName ;

	/**
	 * Get accessor name
	 */
	private String accessorGetName ;

	/**
	 * Set accessor name
	 */
	private String accessorSetName ;

	/**
	 * Get accessor name
	 */
	private String accessorGetListName ;

	/**
	 * Set accessor name
	 */
	private String accessorSetListName ;

	/**
	 * Readonly
	 */
	private boolean readOnly = true;

	/**
	 * Parent viewmodels
	 * Used to transfer readonly on parents
	 */
	private List<MViewModelImpl> parentReferences = new ArrayList<>();

	/**
	 * Mandatory
	 */
	private boolean mandatory = false;

	/**
	 * Cache key name for selected item
	 * Used 
	 */
	private String currentItemKeyName ;

	/**
	 * Customizable VM
	 */
	private boolean customizable = false;

	/**
	 * Configuration name
	 */
	private String configName;

	/**
	 * Path to the model
	 */
	private String pathToModel ;
	
	/**
	 * multiInstance view model
	 */
	private boolean multiInstance = false;
	
	/**
	 * Page 
	 */
	private WeakReference<MPage> page = null;
	
	/**
	 * ScreenViewModel
	 */
	private boolean screenViewModel = false;
	
	/**
	 * javadoc on object
	 */
	private String documentation = "";
	
	/**
	 * Create a new view model.
	 * @param p_sName viewmodel name (including prefix/suffix)
	 * @param p_sUmlName uml name
	 * @param p_oPackage package of viewmodel
	 * @param p_sType viewmodel type
	 * @param p_oEntityToUpdate entity to update
	 * @param p_sPathToModel path to model
	 * @param p_bCustomizable customizable
	 * @param p_oMapping mapping descriptor with entity
	 */
	public MViewModelImpl(String p_sName, String p_sUmlName, MPackage p_oPackage, ViewModelType p_sType, MEntityImpl p_oEntityToUpdate, String p_sPathToModel, boolean p_bCustomizable, IVMMappingDesc p_oMapping) {
		super("viewmodel", p_sUmlName, p_sName, p_oPackage );
		this.type = p_sType;
		this.entityToUpdate = p_oEntityToUpdate;
		// Is null when viewmodel of screen
		if (p_oEntityToUpdate!=null){
			this.addImport(p_oEntityToUpdate.getMasterInterface().getFullName());
			this.addImport(p_oEntityToUpdate.getFactoryInterface().getFullName());
		}

		this.propertyName = StringUtils.uncapitalize(this.getName());
		this.accessorName = p_sName;
		this.accessorGetName = StringUtils.join(PREFIX_GET_ACCESSOR, p_sUmlName );
		this.accessorSetName = StringUtils.join(PREFIX_SET_ACCESSOR, p_sUmlName );
		this.accessorGetListName = StringUtils.join(PREFIX_LIST_GET_ACCESSOR, p_sUmlName );
		this.accessorSetListName = StringUtils.join(PREFIX_LIST_SET_ACCESSOR, p_sUmlName );
		this.customizable = p_bCustomizable;
		this.configName= this.customizable ? ViewModelType.CUSTOM : ViewModelType.DEFAULT;
		this.pathToModel = p_sPathToModel;
		this.mappings = p_oMapping ;
	}
	
	/**
	 * Return path to model
	 * @return path to model
	 */
	public String getPathToModel() {
		return this.pathToModel;
	}

	/**
	 * Returns the property name.
	 * @return Property name
	 */
	public String getPropertyName() {
		return this.propertyName;
	}

	/**
	 * Sets the property name.
	 */
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
	
	/**
	 * Get base name for accessors of viewmodel
	 * ex: fooBar for getFooBar and setFooBar.
	 * By default, value is the name of the class.
	 * @return base name for accessors of viewmodel
	 */
	public String getAccessorName() {
		return this.accessorName;
	}

	/**
	 * Define base name for accessors of viewmodel
	 * @param p_sAccessorName base name for accessors of viewmodel
	 */
	public void setAccessorName(String p_sAccessorName) {
		this.accessorName = p_sAccessorName;
	}
	
	/**
	 * Return getter name
	 * @return getter name
	 */
	public String getAccessorGetName() {
		return this.accessorGetName;
	}

	/**
	 * Define getter name
	 * @param p_sAccessorGetName getter name
	 */
	public void setAccessorGetName(String p_sAccessorGetName) {
		this.accessorGetName = p_sAccessorGetName;
	}

	/**
	 * Return setter name
	 * @return setter name
	 */
	public String getAccessorSetName() {
		return this.accessorSetName;
	}

	/**
	 * Define setter name
	 * @param p_sAccessorSetName setter name
	 */
	public void setAccessorSetName(String p_sAccessorSetName) {
		this.accessorSetName = p_sAccessorSetName;
	}

	/**
	 * Return getter name for list viewmodel
	 * @return getter name for list viewmodel
	 */
	public String getAccessorGetListName() {
		return this.accessorGetListName;
	}

	/**
	 * Define getter name for list viewmodel
	 * @param p_sAccessorGetListName getter name for list viewmodel
	 */
	public void setAccessorGetListName(String p_sAccessorGetListName) {
		this.accessorGetListName = p_sAccessorGetListName;
	}

	/**
	 * Return setter name for list viewmodel
	 * @return setter name for list viewmodel
	 */
	public String getAccessorSetListName() {
		return this.accessorSetListName;
	}

	/**
	 * Define setter name for list viewmodel
	 * @param p_sAccessorSetListName setter name for list viewmodel
	 */
	public void setAccessorSetListName(String p_sAccessorSetListName) {
		this.accessorSetListName = p_sAccessorSetListName;
	}

	/**
	 * Add a load cascade to viewmodel
	 * @param p_oCascade cascade
	 */
	public void addLoadCascade( MCascade p_oCascade) {
		if (!this.cascades.contains(p_oCascade)) {
			this.cascades.add(p_oCascade);
			if (!this.importCascades.contains(p_oCascade.getCascadeImport())) {
				this.importCascades.add(p_oCascade.getCascadeImport());
			}
		}
	}

	/**
	 * Add a save cascade to viewmodel
	 * @param p_oCascade
	 */
	public void addSaveCascade(MCascade p_oCascade) {
		if (!this.saveCascades.contains(p_oCascade)) {
			this.saveCascades.add(p_oCascade);
			if (!this.importCascades.contains(p_oCascade.getCascadeImport())) {
				this.importCascades.add(p_oCascade.getCascadeImport());
			}
		}
	}
	
	/**
	 * Return true if viewmodel is readonly
	 * @return true if viewmodel is readonly
	 */
	public boolean isReadOnly() {
		return this.readOnly;
	}

	/**
	 * Set viewmodel as readonly
	 * @param p_bReadOnly true if readonly
	 */
	public void setReadOnly(boolean p_bReadOnly) {
		if (this.readOnly && !p_bReadOnly) {
			for (MViewModelImpl oParent : this.parentReferences) {
				oParent.setReadOnly(false);
			}
		}
		this.readOnly = p_bReadOnly;
	}
	
	/**
	 * Return type description of entity to update
	 * @return type description of entity to update
	 */
	public MEntityImpl getEntityToUpdate() {
		return this.entityToUpdate;
	}

	/**
	 * Modify type description of entity to update
	 * @param p_oNewTypeEntityToUpdate new type description of entity to update
	 */
	public void setEntityToUpdate(MEntityImpl p_oNewTypeEntityToUpdate) {
		this.entityToUpdate = p_oNewTypeEntityToUpdate ;
	}
	
	/**
	 * Return list of subview models
	 * @return list of subview models
	 */
	public List<MViewModelImpl> getSubViewModels() {
		return this.subViewModels;
	}

	/**
	 * Add a subview model
	 * @param p_oSubViewModel subview model
	 */
	public void addSubViewModel(MViewModelImpl p_oSubViewModel) {
		this.subViewModels.add(p_oSubViewModel);
		p_oSubViewModel.parentReferences.add(this);
	}
	
	/**
	 * Define list of sub viewmodels
	 * @param p_oViewModelImpls la liste à affecter
	 */
	public void setSubViewModel(List<MViewModelImpl> p_oViewModelImpls){
		this.subViewModels = p_oViewModelImpls;
		if (p_oViewModelImpls != null) {
			for (MViewModelImpl oChild : p_oViewModelImpls) {
				oChild.getParentReferences().add(this);
			}
		}
	}
	
	/**
	 * Define imports for cascade
	 * @param p_listImportCascades imports for cascade
	 */
	protected void setImportCascades(List<String> p_listImportCascades){
		this.importCascades = p_listImportCascades;
	}
	
	/**
	 * Define viewmodel type
	 * @param p_oType viewmodel type
	 */
	public void setType(ViewModelType p_oType) {
		this.type = p_oType;
	}
	
	/**
	 * Add a visual field
	 * @param p_oField visual field
	 */
	public void addVisualField(MVisualField p_oField) {
		this.visualFields.add(p_oField);
	}
	
	/**
	 * Get visual fields
	 * @return visual fields
	 */
	public List<MVisualField> getVisualFields() {
		return this.visualFields;
	}
	
	/**
	 * Return the last visual field
	 * @return the last visual field
	 */
	public MVisualField getLastVisualField() {
		return this.visualFields.get( this.visualFields.size()-1);
	}
	
	/**
	 * Define visual fields
	 * @param p_oVisualFields visual fields
	 */
	protected void setVisualFields(List<MVisualField> p_oVisualFields){
		this.visualFields = p_oVisualFields;
	}
	
	/**
	 * Add an external view model
	 * @param p_oVm external view model
	 */
	public void addExternalViewModel(MViewModelImpl p_oVm) {
		this.externalViewModels.add(p_oVm);
		p_oVm.getParentReferences().add(this);
	}

	/**
	 * Return external view models
	 * @return external view models
	 */
	public List<MViewModelImpl> getExternalViewModels() {
		return this.externalViewModels;
	}
	
	/**
	 * Return viewmodel parent
	 * Can be null.
	 * @return viewmodel parent
	 */
	public MViewModelImpl getParent() {
		return this.parentReferences.isEmpty() ? null : this.parentReferences.get(0);
	}
	
	/**
	 * Return true if viewmodel of a workspace
	 * @return true if viewmodel of a workspace
	 */
	public boolean isScreenWorkspace() {
		return this.getMasterInterface().isScreenWorkspace();
	}
	
	/**
	 * Return true if viewmodel of a multi section
	 * @return true if viewmodel of a multi section
	 */
	public boolean isScreenMultiPanel() {
		return this.getMasterInterface().isScreenMultiPanel();
	}
	
	/**
	 * Compare with another viewmodel
	 * @param p_oVmi viewmodel for comparaison
	 * @return true viewmodel is equals p_oVmi
	 */
	public boolean isSame(MViewModelImpl p_oVmi) {
		boolean r_bIsSame = true;
		boolean bFind = false;
		// les attributs doivent être les mêmes les sous models aussi
		if (this.getAttributes().size() != p_oVmi.getAttributes().size()) {
			r_bIsSame = false;
		}
		if (r_bIsSame) {
			for(MAttribute oAttr : this.getAttributes()) {
				bFind = false;
				for(MAttribute oAttr2 : p_oVmi.getAttributes()) {
					if (oAttr.getName().equals(oAttr2.getName())) {
						if (oAttr.getTypeDesc()!=null && oAttr2.getTypeDesc()!=null) {
							if (oAttr.getTypeDesc().getDataType().equals(oAttr2.getTypeDesc().getDataType())) {
								bFind = true;
								break;
							}
						}
						else if (oAttr.getTypeDesc()==null && oAttr2.getTypeDesc()==null) {
							bFind = true;
							break;
						}
						else {
							bFind = false;
						}
					}
				}
				if (!bFind) {
					r_bIsSame = false;
					break;
				}
			}
		}
		if (r_bIsSame) {
			if (this.getSubViewModels().size() != p_oVmi.getSubViewModels().size()) {
				r_bIsSame = false;
			}
		}
		if (r_bIsSame) {
			for(MViewModelImpl oVm1 : this.getSubViewModels()) {
				for(MViewModelImpl oVm2 : this.getSubViewModels()) {
					if (oVm1.getName().equals(oVm2.getName())) {
						bFind = oVm1.isSame(oVm2);
						if (bFind) {
							break;
						}
					}
				}
				if (!bFind) {
					r_bIsSame = false;
					break;
				}
			}
		}
		return r_bIsSame;
	}
	
	/**
	 * Return viewmodel type
	 * @return viewmodel type
	 */
	public ViewModelType getType() {
		return this.type;
	}
	
	/**
	 * Return mapping
	 * @return mapping
	 */
	public IVMMappingDesc getMapping() {
		return this.mappings;
	}
	
	public void resetMapping() {
		this.mappings = new MMapping();
	}
	
	/**
	 * Define mappings
	 * @param p_oMapping mapping
	 */
	protected void setMapping(IVMMappingDesc p_oMapping){
		this.mappings = p_oMapping;
	}

	/**
	 * Return load cascades
	 * @return load cascades
	 */
	public List<MCascade> getLoadCascades() {
		return this.cascades;
	}
	
	/**
	 * Return imports for load cascades
	 * @return imports for load cascades
	 */
	public List<String> getImportCascades() {
		return this.importCascades;
	}
	
	/**
	 * Define imports for load cascade
	 * @param p_listCascades imports for load cascade
	 */
	protected void setCascades(List<MCascade> p_listCascades){
		this.cascades = p_listCascades;
	}
	
	/**
	 * Define save cascades
	 * @param p_listCascades cascades
	 */
	protected void setSaveCascades(List<MCascade> p_listCascades){
		this.saveCascades = p_listCascades;
	}
	
	/**
	 * Define external viewmodels
	 * @param p_listExternalViewModels external viewmodels
	 */
	protected void setExternalViewModels(List<MViewModelImpl> p_listExternalViewModels) {
		this.externalViewModels = p_listExternalViewModels;
	}
	
	/**
	 * Get first sub viewmodel
	 * @return first sub viewmodel
	 */
	public MViewModelImpl getMasterSubViewModel(){
		return this.subViewModels.get(0);
	}
	
	/**
	 * Return parent references
	 * @return parent references
	 */
	public List<MViewModelImpl> getParentReferences() {
		return parentReferences;
	}
	
	/**
	 * Return parent references
	 * @return parent references
	 */
	public void addParent(MViewModelImpl p_oParentViewModelImpl) {
		this.parentReferences.add(p_oParentViewModelImpl);
	}
	
	/**
	 * Return first parent
	 * @return first parent
	 */
	public MViewModelImpl getFirstParent() {
		MViewModelImpl r_oMViewModelImpl = null ;
		if ( !this.parentReferences.isEmpty()) {
			r_oMViewModelImpl = this.parentReferences.get(0);
		}
		return r_oMViewModelImpl;
	}

	/**
	 * Return cache key name for selected item
	 * @return cache key name for selected item
	 */
	public String getCurrentItemKeyName() {
		return this.currentItemKeyName;
	}
	
	/**
	 * Define cache key name for selected item
	 * @param p_sCurrentItemKeyName cache key name for selected item
	 */
	public void setCurrentItemKeyName(String p_sCurrentItemKeyName) {
		this.currentItemKeyName = p_sCurrentItemKeyName;
	}

	/**
	 * Return true if viewmodel is mandatory
	 * @return true if viewmodel is mandatory
	 */
	public boolean isMandatory() {
		return this.mandatory;
	}

	/**
	 * Define true if viewmodel is mandatory
	 * @param p_bMandatory true if viewmodel is mandatory
	 */
	public void setMandatory(boolean p_bMandatory) {
		this.mandatory = p_bMandatory;
	}

	/**
	 * Return true if viewmodel is customizable
	 * @return true if viewmodel is customizable
	 */
	public boolean isCustomizable() {
		return this.customizable;
	}

	/**
	 * Return config name
	 * @return config name
	 */
	public String getConfigName() {
		return this.configName;
	}

	/**
	 * Define config name 
	 * @param p_sConfigName config name
	 */
	public void setConfigName(String p_sConfigName) {
		this.configName = p_sConfigName;
	}
	

	public boolean isMultiInsance() {
		return this.multiInstance;
	}
	
	/**
	 * 
	 * @param p_bMultiInstance
	 */
	public void setMultiInstance(boolean p_bMultiInstance) {
		this.getMasterInterface().setMultiInstance(p_bMultiInstance);
		this.multiInstance = p_bMultiInstance;
	}
	
	/**
	 * Clone viewmodel
	 * @param p_oModeleFactory model factory
	 * @return a clone of viewmodel
	 */
	public MViewModelImpl clone( IModelFactory p_oModeleFactory ){
		
		MViewModelImpl r_oViewModelImpl = p_oModeleFactory.createViewModel(
			this.getName(), this.getUmlName(), this.getPackage(), 
			this.getType(), this.getEntityToUpdate(), this.pathToModel, this.customizable);
		this.copyTo(r_oViewModelImpl);
		return r_oViewModelImpl;
	}
	
	/**
	 * Copy property values to another instance of viewmodel
	 * @param p_oViewModelImpl target viewmodel
	 */
	protected void copyTo( MViewModelImpl p_oViewModelImpl ) {
		p_oViewModelImpl.setSubViewModel(new ArrayList<MViewModelImpl>(this.subViewModels));
		p_oViewModelImpl.setVisualFields(new ArrayList<MVisualField>(this.visualFields));
		p_oViewModelImpl.setMapping(this.mappings);
		p_oViewModelImpl.setCascades(new ArrayList<MCascade>(this.cascades));
		p_oViewModelImpl.setSaveCascades(new ArrayList<MCascade>(this.saveCascades));
		p_oViewModelImpl.setExternalViewModels(new ArrayList<MViewModelImpl>(this.externalViewModels));
		p_oViewModelImpl.setMasterInterface(this.getMasterInterface());
		p_oViewModelImpl.setBeanName(this.getBeanName());
		p_oViewModelImpl.setType(this.getType());
		p_oViewModelImpl.setIdentifier(this.getIdentifier());
		p_oViewModelImpl.setAttributes(this.getAttributes());
		p_oViewModelImpl.setImportCascades(this.importCascades);
		p_oViewModelImpl.setCurrentItemKeyName(this.currentItemKeyName);
		p_oViewModelImpl.setMandatory(this.mandatory);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Element toXml() {
		Element oElement = super.toXml();

		oElement.addElement("multiInstance").setText(String.valueOf(this.multiInstance));
		oElement.addElement("read-only").setText(Boolean.toString(this.readOnly));
		oElement.addElement("mandatory").setText(Boolean.toString(this.mandatory));
		oElement.addElement("workspace-vm").setText(Boolean.toString(this.isScreenWorkspace()));
		oElement.addElement("multipanel-vm").setText(Boolean.toString(this.isScreenMultiPanel()));
		oElement.addElement("customizable").setText(Boolean.toString(this.isCustomizable()));

		if ( this.entityToUpdate != null ){
			Element oTypeEntityToUpdate = oElement.addElement("entity-to-update");
			oTypeEntityToUpdate.addElement("name").setText(this.entityToUpdate.getMasterInterface().getName());
			oTypeEntityToUpdate.addElement("full-name").setText(this.entityToUpdate.getMasterInterface().getFullName());
			oTypeEntityToUpdate.addElement("factory-name").setText(this.entityToUpdate.getFactoryInterface().getName());
			oTypeEntityToUpdate.addElement("factory-full-name").setText(this.entityToUpdate.getFactory().getFullName());
		}
		ViewModelTypeConfiguration oConf = type.getParametersByConfigName(this.getConfigName(), VersionHandler.getGenerationVersion().getStringVersion());
		
		Element oType = oElement.addElement("type");
		if (oConf.getVisualComponentName()!=null){
			oType.addElement("component-name").setText(oConf.getVisualComponentName());
		}
		if (oConf.getAdapterName()!=null){
			oType.addElement("adapter").setText(oConf.getAdapterName());
		}
		
		oType.addElement("conf-name").setText(this.getConfigName());
		oType.addElement("name").setText(this.type.name());
		oType.addElement("list").setText(oConf.getListName());
		if ( oConf.getInterfaceName() != null ) {
			oType.addElement("item").setText(oConf.getInterfaceName());
		}
		if ( oConf.getImplementationName() != null ) {
			oType.addElement("item-impl").setText(oConf.getImplementationName());
		}
		
		oType.addElement("is-list").setText(Boolean.toString(this.type.isList()));

		oElement.addElement("property-name").setText(this.propertyName);
		oElement.addElement("accessor-get-name").setText(this.accessorGetName);
		oElement.addElement("accessor-set-name").setText(this.accessorSetName);
		oElement.addElement("list-accessor-get-name").setText(this.accessorGetListName);
		oElement.addElement("list-accessor-set-name").setText(this.accessorSetListName);
		if ( this.getFirstParent() != null ){
			oElement.addElement("first-parent-reference").setText(this.getFirstParent().getUmlName());
		}

		if (this.mappings != null) {
			oElement.add(this.mappings.toXml());
		}

		// external-lists node must be before subvm node (for processing //external-lists in the right order)
		if (this.externalViewModels != null && !this.externalViewModels.isEmpty()) {
			Element xRootExternalList = oElement.addElement("external-lists");
			Element xExternalList = null;
			for (MViewModelImpl oViewModelImpl: this.externalViewModels) {
				xExternalList = xRootExternalList.addElement("external-list");
				xExternalList.add(oViewModelImpl.toXml());
			}
		}		

		// add subvm
		Element oSubs = oElement.addElement("subvm");
		for(MViewModelImpl oVm : this.subViewModels) {
			oSubs.add(oVm.toXml());
		}
		Element xCascades = oElement.addElement("cascades");
		for(MCascade oCascade : this.cascades) {
			xCascades.addElement("cascade").setText(oCascade.getName());
		}
		for(String s : this.importCascades) {
			xCascades.addElement("import-cascade").setText(s);
		}
		
		Element xSaveCascades = oElement.addElement("savecascades");
		for(MCascade oSaveCascade : this.saveCascades) {
			Element xCascade = xSaveCascades.addElement("cascade");
			xCascade.setText(oSaveCascade.getName());
			xCascade.addAttribute("entity", oSaveCascade.getTargetEntity().getName());
			xCascade.addAttribute("assoName", oSaveCascade.getAssoName());
		}
		
		if ( this.currentItemKeyName != null ) {
			oElement.addElement("current-item-key-name").setText(this.currentItemKeyName);
		}
		
		if (this.pathToModel!=null && this.pathToModel.contains(StrUtils.DOT_S)) {
			String[] t_sPaths = pathToModel.split("\\.");
			Element oPaths = oElement.addElement("data-path");
			String sFullPath = StringUtils.EMPTY;
			String sSetFullPath = StringUtils.EMPTY;
			for(int iIndex = 1; iIndex< t_sPaths.length; iIndex++) {
				
				String sCapName = StringUtils.capitalize(t_sPaths[iIndex]);
				Element oPath = oPaths.addElement("path");
				oPath.addElement("name").setText(t_sPaths[iIndex]);
				oPath.addElement("get-method").setText( StringUtils.join("get", sCapName, "()"));
				oPath.addElement("set-method").setText( StringUtils.join("set", sCapName, "(VALUE)"));
				oPath.addElement("prev-full-path").setText(sFullPath);
				sSetFullPath = StringUtils.join( sFullPath, ".set", sCapName, "(VALUE)");
				sFullPath = StringUtils.join( sFullPath, ".get", sCapName, "()");
				oPath.addElement("full-path").setText(sFullPath);
				oPath.addElement("full-path-setter").setText(sSetFullPath);
			}
			oPaths.addElement("full-path").setText(sFullPath);
			oPaths.addElement("full-path-setter").setText(sSetFullPath);
		}
		oElement.addElement("is-screen-viewmodel").setText(Boolean.toString(this.screenViewModel));
			
		generateParentViewModelNode( this, oElement);

		return oElement;
	}

	private void generateParentViewModelNode( MViewModelImpl p_oViewModel, Element p_xNode ) {
		
		MViewModelImpl oParentViewModel = p_oViewModel.getParent();
		if ( oParentViewModel != null) {
			Element xParentVM = p_xNode.addElement("parent-viewmodel");
			xParentVM.addAttribute("type", oParentViewModel.getType().name());
			Element xMasterInterface = xParentVM.addElement("master-interface");
			xMasterInterface.addAttribute("name", oParentViewModel.getMasterInterface().getName());
			xMasterInterface.addAttribute("full-name", oParentViewModel.getMasterInterface().getFullName());
			generateParentViewModelNode(oParentViewModel, xParentVM);
		}
	}
	
	/**
	 * Indicates whether a view model in sTheoVmName name exists.
	 * @param sTheoVmName sTheoVmName model sought in view (the search is not recursive)
	 * @return true if a sub view model exists with that name
	 */
	public boolean existViewModelName(String sTheoVmName) {
		for(MViewModelImpl oVm : this.subViewModels) {
			if (oVm.getName().equals(sTheoVmName)) {
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

	public boolean isScreenViewModel() {
		return screenViewModel;
	}

	public void setScreenViewModel(boolean screenViewModel) {
		this.screenViewModel = screenViewModel;
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
