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
package com.a2a.adjava.languages.html5.xmodele;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.a2a.adjava.types.ITypeDescription;
import com.a2a.adjava.uml.UmlAssociationEnd;
import com.a2a.adjava.xmodele.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

import com.a2a.adjava.types.IUITypeDescription;
import com.a2a.adjava.xmodele.ui.view.MVFLabelKind;
import com.a2a.adjava.xmodele.ui.view.MVFLocalization;
import com.a2a.adjava.xmodele.ui.view.MVFModifier;
import com.a2a.adjava.xmodele.ui.viewmodel.ViewModelType;

public class MH5ModeleFactory extends XModeleFactory {


    /**
     * Create import delegate
     *
     * @param p_oDelegator delegator
     * @return delegator
     */
    public MH5ImportDelegate createImportDelegate(Object p_oDelegator) {
        return new MH5ImportDelegate(p_oDelegator);
    }

    /**
     * {@inheritDoc}
     *
     * @see com.a2a.adjava.xmodele.XModeleFactory#createVisualField(java.lang.String, java.lang.String, java.lang.String, com.a2a.adjava.xmodele.ui.view.MVFLabelKind, com.a2a.adjava.xmodele.ui.view.MVFLocalization, java.lang.String, boolean)
     */
    @Override
    public MVisualField createVisualField(String p_sName, MLabel p_oLabel,
                                          String p_sComponent, MVFLabelKind p_oLabelKind,
                                          MVFLocalization p_oTarget, String p_sAttributeName,
                                          boolean p_bMandatoryKind) {
        return new MH5VisualField(p_sName, p_oLabel, p_sComponent, p_oLabelKind, p_oTarget,
                p_sAttributeName, p_bMandatoryKind);
    }

    /**
     * {@inheritDoc}
     *
     * @see com.a2a.adjava.xmodele.XModeleFactory#createVisualField(java.lang.String, com.a2a.adjava.xmodele.MVisualField)
     */
    @Override
    public MVisualField createVisualField(String p_sPath, MVisualField p_oField, MLabel p_oLabel) {
        MH5VisualField r_oMIOSVisualField = new MH5VisualField(p_sPath, p_oField, p_oLabel);
        return r_oMIOSVisualField;
    }

    /**
     * {@inheritDoc}
     *
     * @see com.a2a.adjava.xmodele.XModeleFactory#createVisualField(java.lang.String, java.lang.String, java.lang.String, com.a2a.adjava.xmodele.ui.view.MVFLabelKind, java.lang.String, boolean)
     */
    @Override
    public MVisualField createVisualField(String p_sName, MLabel p_oLabel, String p_sComponent, MVFLabelKind p_oLabelKind,
                                          String p_sAttributeName, boolean p_bMandatoryKind) {
        return new MH5VisualField(p_sName, p_oLabel, p_sComponent, p_oLabelKind, p_sAttributeName, p_bMandatoryKind);
    }

    /**
     * {@inheritDoc}
     *
     * @see com.a2a.adjava.xmodele.XModeleFactory#createVisualField(java.lang.String, com.a2a.adjava.types.IUITypeDescription, com.a2a.adjava.xmodele.ui.view.MVFModifier, com.a2a.adjava.xmodele.ui.view.MVFLabelKind, com.a2a.adjava.xmodele.MAttribute, com.a2a.adjava.xmodele.ui.view.MVFLocalization, com.a2a.adjava.xmodele.IDomain, java.lang.String, boolean)
     */
    @Override
    public MVisualField createVisualField(String p_sPrefix, MLabel p_oLabel, IUITypeDescription p_oUiType, MVFModifier p_bMVFModifier,
                                          MVFLabelKind p_oLabelKind, MAttribute p_oAttribute, MVFLocalization p_oLocalisation,
                                          IDomain<IModelDictionary, IModelFactory> p_oDomain,
                                          String p_sAttributeName, boolean p_bMandatory) {
        MH5VisualField r_oVisualField = new MH5VisualField(p_sPrefix + "__value", p_oLabel,
                p_oUiType.getComponentType(p_bMVFModifier), p_oAttribute.getTypeDesc().getEditType(),
                p_oAttribute.getLength(), p_oAttribute.getPrecision(), p_oAttribute.getScale(),
                p_oLabelKind, p_oLocalisation, p_sAttributeName, p_bMandatory, p_oAttribute.getMEnumeration(), p_bMVFModifier == MVFModifier.READONLY, p_oUiType.getUmlName());
        return r_oVisualField;
    }

    /**
     * {@inheritDoc}
     *
     * @see com.a2a.adjava.xmodele.XModeleFactory#createVisualField(java.lang.String, com.a2a.adjava.types.IUITypeDescription, com.a2a.adjava.xmodele.ui.view.MVFModifier, com.a2a.adjava.xmodele.ui.view.MVFLabelKind, com.a2a.adjava.xmodele.MAttribute, com.a2a.adjava.xmodele.XDomain, java.lang.String, boolean)
     */
    @Override
    public MVisualField createVisualField(String p_sPrefix, MLabel p_oLabel, IUITypeDescription p_oTypeVisual, MVFModifier p_oModifier,
                                          MVFLabelKind p_oLabelKind, MAttribute p_oAttribute, IDomain<IModelDictionary, IModelFactory> p_oDomain,
                                          String p_sAttributeName, boolean p_bMandatory) {
        return this.createVisualField(p_sPrefix, p_oLabel, p_oTypeVisual, p_oModifier, p_oLabelKind, p_oAttribute,
                MVFLocalization.DEFAULT, p_oDomain, p_sAttributeName, p_bMandatory);
    }

    @Override
    public MLabel createLabel(String p_sBaseName, MViewModelImpl p_oViewModel) {

        List<String> sKeyParts = computeLabelPath(p_oViewModel);
        sKeyParts.add(p_sBaseName.toLowerCase());
        sKeyParts.add("label");

        String sLabel = createLabelKey(sKeyParts);
        String sValue = createLabelValue(p_sBaseName);

        return new MH5Label(sLabel, sValue);
    }

    @Override
    public MLabel createLabelFromVisualField(String p_sPath, MVisualField p_oVisualField) {
        return p_oVisualField.getLabel();
    }

    @Override
    public MLabel createLabelForFixedList(String p_sBaseName1, String p_sBaseName2, MViewModelImpl p_oParentVm) {
        return this.createLabel(p_sBaseName2, p_oParentVm);
    }

    @Override
    public MLabel createLabelForAttributeOfFixedList(String p_sBaseName1, String p_sBaseName2, MVFLocalization p_oLocalization,
                                                     MViewModelImpl p_oViewModel) {

        List<String> sKeyParts = computeLabelPath(p_oViewModel);
        if (p_oLocalization.equals(MVFLocalization.DETAIL)) {
            sKeyParts.add("fixedlistdetail");
        } else {
            sKeyParts.add("fixedlist");
        }
        sKeyParts.add(p_sBaseName2.toLowerCase());
        sKeyParts.add("label");

        String sKey = createLabelKey(sKeyParts);
        String sValue = createLabelValue(p_sBaseName2);
        return new MH5Label(sKey, sValue);
    }

    @Override
    public MLabel createLabelForCombo(String p_sBaseName1, String p_sBaseName2,
                                      MViewModelImpl p_oParentVm) {
        return this.createLabel(p_sBaseName2, p_oParentVm);
    }

    @Override
    public MLabel createLabelForAttributeOfCombo(String p_sName, String p_sVisualFieldName, MVFLocalization p_oLocalization,
                                                 MViewModelImpl p_oViewModel) {

        List<String> sKeyParts = computeLabelPath(p_oViewModel);
        if (p_oLocalization.equals(MVFLocalization.DETAIL)) {
            sKeyParts.add("comboselected");
        } else {
            sKeyParts.add("combolist");
        }

        sKeyParts.add(p_sName.toLowerCase());
        sKeyParts.add("label");
        String sKey = createLabelKey(sKeyParts);
        String sValue = createLabelValue(p_sName);
        return new MH5Label(sKey, sValue);
    }


    @Override
    public MLabel createLabelForScreen(MScreen p_oScreen) {
        String sLabel = createLabelKey(p_oScreen.getName().toLowerCase(), "title");
        String sValue = createLabelValue(p_oScreen.getName());
        return new MH5Label(sLabel, sValue);
    }

    @Override
    public MLabel createLabelForPage(MPage p_oPage) {
        String sLabel = createLabelKey(p_oPage.getName().toLowerCase(), "title");
        String sValue = createLabelValue(p_oPage.getName());
        return new MH5Label(sLabel, sValue);
    }

    @Override
    public Map<String, MLabel> createLabelsForEnumeration(MEnumeration p_oEnumeration) {

        Map<String, MLabel> r_listLabels = new HashMap<>();

        String sKey = createLabelKey("enum", p_oEnumeration.getName(), "FWK_NONE");
        r_listLabels.put(sKey, new MH5Label(sKey, "No selection"));

        for (String sToken : p_oEnumeration.getEnumValues()) {
            sKey = createLabelKey("enum", p_oEnumeration.getName(), sToken);
            r_listLabels.put(sKey, new MH5Label(sKey, StringUtils.capitalize(sToken.toLowerCase())));
        }

        return r_listLabels;
    }

    protected List<String> computeLabelPath(MViewModelImpl p_oViewModel) {
        MViewModelImpl oWalk = p_oViewModel;
        List<String> r_sKeyParts = new ArrayList<>();
        while (oWalk != null) {

            if (oWalk.getType().equals(ViewModelType.LISTITEM_1)) {
                r_sKeyParts.add(0, "listitem1");
            } else if (oWalk.getType().equals(ViewModelType.LISTITEM_2)) {
                r_sKeyParts.add(0, "listitem2");
            } else if (oWalk.getType().equals(ViewModelType.LISTITEM_3)) {
                r_sKeyParts.add(0, "listitem3");
            } else {
                r_sKeyParts.add(0, oWalk.getUmlName().toLowerCase());
            }

            oWalk = oWalk.getParent();
        }
        return r_sKeyParts;
    }

    @Override
    protected String createLabelKey(String... p_sBaseName) {
        String sLabel = StringUtils.join(p_sBaseName, "__");
        return sLabel;
    }

    @Override
    protected String createLabelKey(List<String> p_listParts) {
        String sLabel = StringUtils.join(p_listParts, "__");
        return sLabel;
    }

    @Override
    public String createLabelValue(String p_sValue) {
        String r_sResult = p_sValue.replaceAll("(\\p{Ll})(\\p{Lu})", "$1 $2");
        String sFirstWord = StringUtils.substringBefore(r_sResult, " ");
        String sEndOfSentence = StringUtils.substringAfter(r_sResult, " ");
        if (StringUtils.isNotBlank(sEndOfSentence)) {
            sEndOfSentence = " " + WordUtils.uncapitalize(sEndOfSentence);
        }
        return StringUtils.capitalize(sFirstWord) + sEndOfSentence;
    }

    @Override
    public MAssociationOneToOne createAssociationOneToOne(String p_sName,
                                                          MEntityImpl p_oRefClass,
                                                          MEntityImpl p_oOppositeClass,
                                                          String p_sVariableName,
                                                          String p_sVariableListName,
                                                          String p_sParameterName,
                                                          ITypeDescription p_oTypeDescription,
                                                          String p_sVisibility,
                                                          boolean p_bRelationOwner,
                                                          boolean p_bIsNotNull,
                                                          String p_sOppositeName,
                                                          UmlAssociationEnd.AggregateType p_oAggregateType,
                                                          UmlAssociationEnd.AggregateType p_oOppositeAggregateType,
                                                          boolean p_bOppositeNavigable) {
        // In order to have correct DAOMapping we need to have both sids of relation. To get mapping association on both sids we set p_bRelationOwner to p_bOppositeNavigable.
        // Case bidirectional
        if (p_bOppositeNavigable) {
            return new MAssociationOneToOne(p_sName,
                    p_oRefClass,
                    p_oOppositeClass,
                    p_sVariableName,
                    p_sVariableListName,
                    p_sParameterName,
                    p_oTypeDescription,
                    p_sVisibility,
                    p_bOppositeNavigable,
                    p_bIsNotNull,
                    p_sOppositeName,
                    p_oAggregateType,
                    p_oOppositeAggregateType,
                    p_bOppositeNavigable);
        } else {
            return new MAssociationOneToOne(p_sName,
                    p_oRefClass,
                    p_oOppositeClass,
                    p_sVariableName,
                    p_sVariableListName,
                    p_sParameterName,
                    p_oTypeDescription,
                    p_sVisibility,
                    p_bRelationOwner,
                    p_bIsNotNull,
                    p_sOppositeName,
                    p_oAggregateType,
                    p_oOppositeAggregateType,
                    p_bOppositeNavigable);
        }


    }

}
