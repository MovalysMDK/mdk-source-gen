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
package com.a2a.adjava.languages.ios.xmodele.views;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * Editable view
 * @author lmichenaud
 *
 */
public class MIOSEditableView extends MIOSView {
	
	/**
	 * A constant value that equals to the margin  (expressed as points) 
	 * between a component and its associated cell.
	 */
	private int COMPONENT_CELL_MARGIN = 10;

	/**
	 * Constructor
	 */
	public MIOSEditableView(){
		super();
		options = new HashMap<String, String>();
	}
	
	/**
	 * Mandatory
	 */
	@XmlAttribute
	private boolean mandatory ;

	/**
	 * Label view
	 */
	@XmlAttribute
	@XmlIDREF
	private MIOSLabelView labelView ;

	/**
	 * Visible label
	 */
	@XmlAttribute
	private boolean visibleLabel = true;
	
	/**
	 * Read only
	 */
	@XmlAttribute
	private boolean readOnly;
	
	/**
	 * Cell type
	 */
	@XmlElement
	private String cellType ;
	
	/**
	 * Cell Height
	 */
	@XmlElement
	private int cellHeight;

	/**
	 * Linked type for the detail 
	 */
	@XmlElement
	private String linkedType ;


	/**
	 * Parameter Principal for the combo
	 */
	@XmlElement
	private String customParameterName ;

	
	/**
	 * Label view height
	 */
	@XmlAttribute
	private int labelViewHeight = 0 ;
	
	/**
	 * Max length of the value
	 */
	@XmlAttribute
	private int maxLength = -1;
	
	/**
	 * Name of the enum class used
	 */
	@XmlElement
	private String enumClassName ;
	
	/** 
	 * Property list
	 */
	/*@XmlElementWrapper*/
	@XmlElement(name="options")
	@XmlJavaTypeAdapter(MapAdapter.class)
	private Map<String, String> options;
	
	/**
	 * Is mandatory
	 * @return mandatory
	 */
	public boolean isMandatory() {
		return this.mandatory;
	}

	/**
	 * Set mandatory
	 * @param p_bMandatory mandatory
	 */
	public void setMandatory(boolean p_bMandatory) {
		this.mandatory = p_bMandatory;
	}

	/**
	 * Label view
	 * @return label view
	 */
	public MIOSLabelView getLabelView() {
		return this.labelView;
	}

	/**
	 * Defines labelview
	 * @param p_oLabelView label view
	 */
	public void setLabelView(MIOSLabelView p_oLabelView) {
		if(p_oLabelView != null) {
			this.labelView = p_oLabelView;
			this.labelViewHeight = p_oLabelView.getHeight() ;
		}
	}

	/**
	 * return visibility of label
	 * @return visible
	 */
	public boolean isVisibleLabel() {
		return visibleLabel;
	}

	/**
	 * sets the visibility of label
	 * @param p_bVisibleLabel visibility of the label
	 */
	public void setVisibleLabel(boolean p_bVisibleLabel) {
		this.visibleLabel = p_bVisibleLabel;
	}
	
	/**
	 * return read only attribute
	 * @return visible
	 */
	public boolean isReadOnly() {
		return readOnly;
	}

	/**
	 * sets read only attribute
	 * @param p_bReadOnly the read only attribute
	 */
	public void setReadOnly(boolean p_bReadOnly) {
		this.readOnly = p_bReadOnly;
	}

	/**
	 * Gets cell type
	 * @return cell type
	 */
	public String getCellType() {
		return this.cellType;
	}

	/**
	 * Set cell type
	 * @param p_sCellType cell type
	 */
	public void setCellType(String p_sCellType) {
		this.cellType = p_sCellType;
	}
	
	/**
	 * Return linked type of the cell
	 * @return linked type of the cell
	 */
	public String getLinkedType() {
		return linkedType;
	}
	
	/**
	 * Set linked type of the cell
	 * @param p_sLinkedType the linked type name of the cell
	 */
	public void setLinkedType(String p_sLinkedType) {
		this.linkedType = p_sLinkedType;
	}
	
	/**
	 * Returns the maximum length of a view
	 * @return the maxLength
	 */
	public int getMaxLength() {
		return maxLength;
	}

	/**
	 * Sets the maximum length of a view
	 * @param p_iMaxLength the maxLength to set
	 */
	public void setMaxLength(int p_iMaxLength) {
		this.maxLength = p_iMaxLength;
	}

	/**
	 * Custom parameter name
	 * @return return  the custom 
	 */
	public String getCustomParameterName() {
		return this.customParameterName;
	}


	/**
	 * Set parameter name
	 * @param p_sCustomParameterName the parameter name
	 */
	public void setCustomParameterName(String p_sCustomParameterName) {
		this.customParameterName = p_sCustomParameterName;
	}
	
	
	
	/**
	 * Return name of the enum class
	 * @return name of the enum class
	 */
	public String getEnumClassName() {
		return enumClassName;
	}

	/**
	 * Set name of the enum class
	 * @param p_sEnumClassName the name of the enum class
	 */
	public void setEnumClassName(String p_sEnumClassName) {
		this.enumClassName = p_sEnumClassName;
	}

	/**
	 * Copy data of argument in a new object
	 * @param p_oNewView new objected  created
	 */
	public void copyTo(MIOSEditableView p_oNewView) {
		super.copyTo(p_oNewView);
		if( this.labelView != null){
			MIOSLabelView oLabelView = new MIOSLabelView();
			this.labelView.copyTo(oLabelView);
			p_oNewView.setLabelView(oLabelView) ;
		}
		p_oNewView.setEnumClassName(this.enumClassName);
		//p_oNewView.setLabelViewHeight(this.labelViewHeight);
		p_oNewView.setReadOnly(this.readOnly);
		p_oNewView.setLinkedType(this.linkedType) ;
		p_oNewView.setCellType(this.cellType) ;
		p_oNewView.setCustomParameterName(this.customParameterName) ;
		p_oNewView.setMandatory(this.isMandatory()) ;
		p_oNewView.setVisibleLabel(this.isVisibleLabel());
		p_oNewView.setMaxLength(this.maxLength);
	}
	
	/**
	 * Returns view options
	 * @return the view options
	 */
	public Map<String, String> getOptions() {
		return options;
	}
	
	/**
	 * Add option
	 * @param p_sKey the key
	 * @param p_sOption the option
	 */
	public void addOption(String p_sKey, String p_sOption) {
		options.put(p_sKey, p_sOption);
	}
	
	/**
	 * Set view options
	 * @param p_oOptions options to set
	 */
	public void setViewOptions(Map<String, String> p_oOptions) {
		this.options = p_oOptions;
	}
	
	@Override
	public int getTotalHeight(){
		int r_iHeight = this.getHeight();
		if(this.visibleLabel){
			r_iHeight+=this.labelViewHeight;
		}
		return r_iHeight;
	}

	/**
	 * Computes the Cell height asscoiated to this editable view.
	 */
	public void computeCellHeight() {
		this.cellHeight = 2 * COMPONENT_CELL_MARGIN + getTotalHeight();
	}

}
