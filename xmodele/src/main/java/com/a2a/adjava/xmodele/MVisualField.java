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

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;

import com.a2a.adjava.utils.VersionHandler;
import com.a2a.adjava.xmodele.ui.component.MMultiPanelConfig;
import com.a2a.adjava.xmodele.ui.component.MWorkspaceConfig;
import com.a2a.adjava.xmodele.ui.view.MVFLabelKind;
import com.a2a.adjava.xmodele.ui.view.MVFLocalization;
import com.a2a.adjava.xmodele.ui.viewmodel.ViewModelType;

/**
 * <p>
 * Element représentant un champ visualisable sur le mobile.
 * </p>
 * 
 * <p>
 * Copyright (c) 2011
 * </p>
 * <p>
 * Company: Adeuza
 * </p>
 * 
 * @author fbourlieux
 * @since MF-Annapurna
 */
public class MVisualField extends SGeneratedElement {
	
	/**
	 * Component class name
	 */
	private String component ;
	
	/**
	 * Create label 
	 */
	private boolean createLabel = true;
	
	/**
	 * Nom de l'attribut lié dans le modèle de données
	 */
	private String entityLinkedAttributeName;
	
	/**
	 * Label name 
	 */
	private MLabel label;
	
	/**
	 * type of the visual component
	 */
	private String editType;
	
	/**
	 * max length of the visual component
	 */
	private int maxLength = -1;
	
	/**
	 * precision of the visual component (number of digits in a number field
	 * (including the scale : number of digits after ",")
	 */
	private int precision = -1;
	
	/**
	 * scale of the visual component (included in precision)
	 */
	private int scale = -1;
	
	/**
	 * Localization
	 */
	private MVFLocalization localization;
	
	/**
	 * liste de paramètres liés aux composants
	 **/
	private Map<String, Object> visualParameters = new HashMap<>();
	
	/** 
	 * Champ obligatoire ou non
	 */
	private boolean mandatory;

	/**
	 * Champ en lecture seule ou non
	 */
	private boolean readOnly;
	
	/**
	 * Nom de la propriété du viewModel
	 */
	private String viewModelProperty;
	
	/**
	 * Nom du viewModel
	 */
	private String viewModelName;
	
	/**
	 * position du label dans la grid
	 */
	private String labelPosition;
	
	/**
	 * position du composant dans la grid
	 */
	private String componentPosition;

	
	/**
	 * Construit un nouvel objet de type <em>MVisualField</em>.
	 * @param p_sName name of the field
	 * @param p_sLabelName label name before the field
	 * @param p_sComponent component to use
	 * @param p_sEditType the input type of the field (to param the keyboard)
	 * @param p_iMaxLength the max length of the field (for string type for
	 *            example)
	 * @param p_iPrecision the pecision of a number field (in oracle style)
	 * @param p_iScale the scale of a number field (oracle style)
	 * @param p_bCreateLabel is the label has to be shown
	 * @param p_oTarget the localisation
	 * @param p_oEnum enum of allowed values (optional)
	 */
	protected MVisualField(String p_sName, MLabel p_oLabel, String p_sComponent, String p_sEditType,
			int p_iMaxLength, int p_iPrecision, int p_iScale, MVFLabelKind p_oLabelKind, MVFLocalization p_oTarget,
			String p_sEntityAttributeName, boolean p_bMandatoryKind, MEnumeration p_oEnum, String p_sUmlName ) {
		super("visualfield", null, p_sName);
		
		this.component = p_sComponent;
		this.createLabel = p_oLabelKind.equals(MVFLabelKind.WITH_LABEL);
		this.editType = p_sEditType;
		this.maxLength = p_iMaxLength;
		this.precision = p_iPrecision;
		this.scale = p_iScale;
		this.label = p_oLabel;
		this.localization = p_oTarget;
		this.entityLinkedAttributeName = p_sEntityAttributeName;
		this.mandatory = p_bMandatoryKind;
		this.umlName = p_sUmlName; 
		
		// add enum values
		if (p_oEnum != null) {
			addParameter("enum", p_oEnum.getName());
			addParameter("values", StringUtils.trim(StringUtils.join(p_oEnum.getEnumValues(), ' ')));
		}
	}

	/**
	 * constructor for component with more than one field, for simple field used
	 * the complete one (
	 * {@link #MVisualField(String, String, String, String, int, int, int, boolean)}
	 * @param p_sName name of the field
	 * @param p_sLabelName label name before the field
	 * @param p_sComponent component to use
	 * @param p_bCreateLabel is the label has to be shown
	 * @param p_oTarget the localisation
	 */
	protected MVisualField(String p_sName, MLabel p_oLabel, String p_sComponent, MVFLabelKind p_oLabelKind,
			MVFLocalization p_oTarget, String p_sEntityAttributeName, boolean p_bMandatoryKind) {
		this(p_sName, p_oLabel, p_sComponent, StringUtils.EMPTY, -1, -1, -1, p_oLabelKind, p_oTarget,
				p_sEntityAttributeName, p_bMandatoryKind, null, null);
	}

	/**
	 * Construit un nouvel objet de type <em>MVisualField</em>.
	 * @param p_sName le nom du champ
	 * @param p_sLabelName le label
	 * @param p_sComponent l'emplacement du champ et son type sous la forme
	 *            d'une classe + package.
	 * @param p_bCreateLabel est ce qu'on affiche le label du champ
	 */
	protected MVisualField(String p_sName, MLabel p_oLabel, String p_sComponent, MVFLabelKind p_oLabelKind,
			String p_sEntityAttributeName, boolean p_bMandatoryKind) {
		this(p_sName, p_oLabel, p_sComponent, StringUtils.EMPTY, -1, -1, -1, p_oLabelKind, MVFLocalization.DEFAULT,
				p_sEntityAttributeName, p_bMandatoryKind, null, null);
	}

	/**
	 * Construit un nouvel objet de type <em>MVisualField</em>.
	 * @param p_sName name of the field
	 * @param p_sLabelName label name before the field
	 * @param p_sComponent component to use
	 * @param p_sEditType the input type of the field (to param the keyboard)
	 * @param p_iMaxLength the max length of the field (for string type for
	 *            example)
	 * @param p_iPrecision the pecision of a number field (in oracle style)
	 * @param p_iScale the scale of a number field (oracle style)
	 * @param p_bCreateLabel is the label has to be shown
	 * @param p_oTarget the localisation
	 * @param p_oEnum enum of allowed values (optional)
	 * @param p_bReadOnly is the field only readable
	 */
	protected MVisualField(String p_sName, MLabel p_oLabel, String p_sComponent, String p_sEditType,
			int p_iMaxLength, int p_iPrecision, int p_iScale, MVFLabelKind p_oLabelKind, MVFLocalization p_oTarget,
			String p_sEntityAttributeName, boolean p_bMandatoryKind, MEnumeration p_oEnum, boolean p_bReadOnly, String p_sUmlName ) {
		this(p_sName, p_oLabel, p_sComponent, StringUtils.EMPTY, p_iMaxLength, -1, -1, p_oLabelKind, p_oTarget,
				p_sEntityAttributeName, p_bMandatoryKind, p_oEnum, p_sUmlName);
		this.readOnly = p_bReadOnly;
	}
	
	/**
	 * Construit un nouvel objet de type <em>MVisualField</em> à partir d'un
	 * autre champ.
	 * @param p_sPath le chemin du champ.
	 * @param p_oVisualField le champ à partir duquel on veut créer le clone
	 */
	protected MVisualField(String p_sPath, MVisualField p_oVisualField, MLabel p_oLabel ) {
		// do not concat "_" if there is no path
		super("visualfield", null, (p_sPath!=null && p_sPath.length()>0 ? p_sPath+"_" : "") + p_oVisualField.getName());
			
		this.component = p_oVisualField.getComponent();
		this.createLabel = p_oVisualField.createLabel;
		this.editType = p_oVisualField.editType;
		this.maxLength = p_oVisualField.maxLength;
		this.precision = p_oVisualField.precision;
		this.scale = p_oVisualField.scale;
		this.entityLinkedAttributeName = p_oVisualField.entityLinkedAttributeName;
		this.viewModelProperty = p_oVisualField.viewModelProperty;
		this.viewModelName = p_oVisualField.viewModelName;
		this.mandatory = p_oVisualField.mandatory;
		this.readOnly = p_oVisualField.readOnly;
		this.label = p_oLabel ;
		this.localization = p_oVisualField.getLocalization();
		for (Entry<String, String> oEntry : p_oVisualField.getParameters().entrySet()) {
			this.addParameter(oEntry.getKey(), oEntry.getValue());
		}
		for (Entry<String, Object> oEntry : p_oVisualField.getVisualParameters().entrySet()) {
			this.addVisualParameter(oEntry.getKey(), oEntry.getValue());
		}
		
		this.umlName = p_oVisualField.getUmlName();
	}

	/**
	 * Construit un nouvel objet de type <em>MVisualField</em> à partir d'un
	 * autre champ.
	 * @param p_oVisualField le champ à partir duquel on veut créer le clone
	 */
	public MVisualField(MVisualField p_oVisualField) {
		super("visualfield", null, p_oVisualField.getName());
				
		this.component = p_oVisualField.getComponent();
		this.createLabel = p_oVisualField.createLabel;
		this.editType = p_oVisualField.editType;
		this.maxLength = p_oVisualField.maxLength;
		this.precision = p_oVisualField.precision;
		this.scale = p_oVisualField.scale;
		this.entityLinkedAttributeName = p_oVisualField.entityLinkedAttributeName;
		this.viewModelProperty = p_oVisualField.viewModelProperty;
		this.viewModelName = p_oVisualField.viewModelName;
		this.mandatory = p_oVisualField.mandatory;
		this.readOnly = p_oVisualField.readOnly;
		if (p_oVisualField.label != null) {
			this.label = p_oVisualField.label;
		}
		this.localization = p_oVisualField.getLocalization();
		for (Entry<String, String> oEntry : p_oVisualField.getParameters().entrySet()) {
			this.addParameter(oEntry.getKey(), oEntry.getValue());
		}
		for (Entry<String, Object> oEntry : p_oVisualField.getVisualParameters().entrySet()) {
			this.addVisualParameter(oEntry.getKey(), oEntry.getValue());
		}
		
		this.umlName = p_oVisualField.getUmlName();
	}

	public void setComponent(String p_sComponent) {
		this.component = p_sComponent;
	}

	/**
	 * Ajoute un nouveau paramètre visuel au champ.
	 * @param p_sKey la clé du paramètre
	 * @param p_sValue la valeur du paramètre. Ce doit être une chaîne
	 */
	public void addVisualParameter(String p_sKey, Object p_sValue) {
		this.visualParameters.put(p_sKey, p_sValue);
	}
	
	/**
	 * Retourne la map de parametres 
	 * @return la map de parametres 
	 */
	public Map<String, Object> getVisualParameters() {
		return visualParameters;
	}

	public void setLocalization(MVFLocalization localization) {
		this.localization = localization;
	}

	/**
	 * Retourne l'emplacement du champ.
	 * @see Localization
	 * @return objet de type <em>Localization</em>
	 */
	public MVFLocalization getLocalization() {
		return this.localization;
	}

	/**
	 * @return
	 */
	public String getEntityLinkedAttributeName() {
		return entityLinkedAttributeName;
	}

	/**
	 * retourne le nom du label du champ.
	 * @return champ de type string
	 */
	public MLabel getLabel() {
		return this.label;
	}

	/**
	 * Retourne l'emplacement de la classe représentant le champ.
	 * @return Package + classe sous la forme d'une chaîne
	 */
	public String getComponent() {
		return this.component;
	}

	/**
	 * @return the editType
	 */
	public String getEditType() {
		return editType;
	}

	/**
	 * @return the maxLength
	 */
	public int getMaxLength() {
		return maxLength;
	}

	/**
	 * @return the precision
	 */
	public int getPrecision() {
		return precision;
	}

	/**
	 * @return the scale
	 */
	public int getScale() {
		return scale;
	}
	
	/**
	 * @param p_sParameterName
	 * @return
	 */
	public <T> T getVisualParameter( String p_sParameterName ) {
		return (T) this.visualParameters.get( p_sParameterName );
	}

	/**
	 * Set create label
	 * 
	 */
	public void setCreateLabel(boolean createLabel) {
		this.createLabel = createLabel;
	}
	
	
	/**
	 * Create label
	 * @return true if label must be created
	 */
	public boolean isCreateLabel() {
		return this.createLabel;
	}
	
	/**
	 * Return true if mandatory
	 * @return true if mandatory
	 */
	public boolean isMandatory() {
		return this.mandatory;
	}
	
	/**
	 * Return true if read only
	 * @return true if read only
	 */
	public boolean isReadOnly() {
		return this.readOnly;
	}

	/**
	 * Set read only
	 * @return true if read only
	 */
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}
	
	/**
	 * Modifieur du nom complet du champ
	 * @param p_sName nouveau nom complet  du champ
	 */
	@Override
	public void setFullName(String p_sName) {
		super.setFullName(p_sName);
	}
	
	/**
	 * Modifieur du nom du champ
	 * @param p_sLabelName nouveau nom du champ
	 */
	public void setLabel(MLabel p_oLabel) {
		this.label = p_oLabel;
	}
	
	/**
	 * Modifieur du nom du champ
	 * @param p_sLabelName nouveau nom du champ
	 */
	public String getViewModelProperty() {
		return viewModelProperty;
	}

	/**
	 * Modifieur du nom de la propriété du viewModel
	 * @param p_sViewModelProperty nouveau nom de la propriété du viewModel
	 */
	public void setViewModelProperty(String p_sViewModelProperty) {
		this.viewModelProperty = p_sViewModelProperty;
	}
	
	/**
	 * Modifieur du nom de l'interface du viewModel
	 * @param p_sViewModelProperty nouveau nom de la propriété du viewModel
	 */
	public String getViewModelName() {
		return viewModelName;
	}

	/**
	 * Modifieur du nom de l'interface du viewModel
	 * @param p_sViewModelProperty nouveau nom de la propriété du viewModel
	 */
	public void setViewModelName(String viewModelName) {
		this.viewModelName = viewModelName;
	}
	
	

	public void setLabelPosition(String labelPosition) {
		this.labelPosition = labelPosition;
	}

	public void setComponentPosition(String componentPosition) {
		this.componentPosition = componentPosition;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void toXmlInsertBeforeDocumentation(Element p_xElement) {
		super.toXmlInsertBeforeDocumentation(p_xElement);
		
		if ( this.component != null ) {
			p_xElement.addElement("component").setText(this.component);
			
			if ( this.component.equals(
				ViewModelType.WORKSPACE_MASTERDETAIL.getParametersByConfigName(ViewModelType.DEFAULT, VersionHandler.getGenerationVersion().getStringVersion()).getVisualComponentNameFull()) ||
				this.component.equals(
				ViewModelType.WORKSPACE_DETAIL.getParametersByConfigName(ViewModelType.DEFAULT, VersionHandler.getGenerationVersion().getStringVersion()).getVisualComponentNameFull())) {
				
				p_xElement.addElement("field-type-workspace").setText("true");
				MWorkspaceConfig oWorkspaceConfig = this.getVisualParameter(MWorkspaceConfig.VISUALFIELD_PARAMETER);
				p_xElement.add(oWorkspaceConfig.toXml());
			
			} else {
				p_xElement.addElement("field-type-workspace").setText("false");
			}
			
			if ( this.component.equals(
					ViewModelType.MULTIPANEL.getParametersByConfigName(ViewModelType.DEFAULT, VersionHandler.getGenerationVersion().getStringVersion()).getVisualComponentNameFull())) {			
				MMultiPanelConfig oMMultiPanelConfig = this.getVisualParameter(MMultiPanelConfig.VISUALFIELD_PARAMETER);
				p_xElement.add(oMMultiPanelConfig.toXml());
			}
		}
		
		p_xElement.addElement("create-label").setText(Boolean.toString(this.createLabel));
				
		if (label != null) {
			p_xElement.addElement("label").setText(label.getKey());
		}
		if (editType != null) {
			p_xElement.addElement("edit-type").setText(this.editType);
		}
		if (maxLength != -1) {
			p_xElement.addElement("max-length").setText(String.valueOf(maxLength));
		}
		
		if ( maxLength == -1 ) {
			if (precision != -1) {
				p_xElement.addElement("precision").setText(String.valueOf(precision));
			}
			if (scale != -1) {
				p_xElement.addElement("scale").setText(String.valueOf(scale));
			}
		}
		if (entityLinkedAttributeName != null) {
			p_xElement.addElement("entity").setText(entityLinkedAttributeName);
		}
		if (viewModelProperty != null) {
			p_xElement.addElement("property-name").setText(viewModelProperty);
		}
		if (viewModelName != null) {
			p_xElement.addElement("viewmodel-interface-name").setText(viewModelName);
		}
		if (labelPosition != null && labelPosition != "") {
			p_xElement.addElement("label-position").setText(labelPosition);
		}
		if (componentPosition != null && componentPosition != "") {
			p_xElement.addElement("component-position").setText(componentPosition);
		}
		p_xElement.addElement("mandatory").setText(String.valueOf(this.mandatory));
		p_xElement.addElement("readonly").setText(String.valueOf(this.readOnly));
	}
}