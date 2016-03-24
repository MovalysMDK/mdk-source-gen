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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.a2a.adjava.uml.UmlAssociationEnd;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

import com.a2a.adjava.types.ITypeDescription;
import com.a2a.adjava.types.IUITypeDescription;
import com.a2a.adjava.uml.UmlClass;
import com.a2a.adjava.xmodele.ui.component.MActionButton;
import com.a2a.adjava.xmodele.ui.component.MButtonType;
import com.a2a.adjava.xmodele.ui.component.MMultiPanelConfig;
import com.a2a.adjava.xmodele.ui.component.MNavigationButton;
import com.a2a.adjava.xmodele.ui.menu.MMenu;
import com.a2a.adjava.xmodele.ui.menu.MMenuActionItem;
import com.a2a.adjava.xmodele.ui.menu.MMenuDef;
import com.a2a.adjava.xmodele.ui.menu.MMenuItem;
import com.a2a.adjava.xmodele.ui.navigation.MNavigation;
import com.a2a.adjava.xmodele.ui.navigation.MNavigationType;
import com.a2a.adjava.xmodele.ui.view.MVFLabelKind;
import com.a2a.adjava.xmodele.ui.view.MVFLocalization;
import com.a2a.adjava.xmodele.ui.view.MVFModifier;
import com.a2a.adjava.xmodele.ui.viewmodel.ViewModelType;
import com.a2a.adjava.xmodele.ui.viewmodel.ViewModelTypeConfiguration;
import com.a2a.adjava.xmodele.ui.viewmodel.mappings.MMapping;

/**
 * <p>
 * Model Factory
 * </p>
 * <p/>
 * <p/>
 * Copyright (c) 2011
 * <p/>
 * Company: Adeuza
 *
 * @author lmichenaud
 */
public class XModeleFactory implements IModelFactory {

    /**
     * {@inheritDoc}
     *
     * @see com.a2a.adjava.xmodele.IModelFactory#createXModele()
     */
    @Override
    public XModele<? extends IModelDictionary> createXModele() {
        return new XModele<ModelDictionary>(new ModelDictionary());
    }

    /**
     * {@inheritDoc}
     *
     * @see com.a2a.adjava.xmodele.IModelFactory#createMAttribute(java.lang.String, java.lang.String, boolean, boolean, boolean, com.a2a.adjava.types.ITypeDescription, java.lang.String, java.lang.String, boolean, int, int, int, boolean, boolean, java.lang.String, java.lang.String)
     */
    @Override
    public MAttribute createMAttribute(String p_sName, String p_sVisibility, boolean p_bIdentifier, boolean p_bDerived, boolean p_bTranscient,
                                       ITypeDescription p_oTypeDescription, String p_sInitialisation, String p_sDefaultValue, boolean p_bIsMandory, int p_iLength, int p_iPrecision,
                                       int p_iScale, boolean p_bHasSequence, boolean p_bUnique, String p_sUniqueKey, String p_sDocumentation) {
        return new MAttribute(p_sName, p_sVisibility, p_bIdentifier, p_bDerived, p_bTranscient, p_oTypeDescription,
                p_sInitialisation, p_sDefaultValue, p_bIsMandory, p_iLength, p_iPrecision,
                p_iScale, p_bHasSequence, p_bUnique, p_sUniqueKey, p_sDocumentation);
    }

    /**
     * {@inheritDoc}
     *
     * @see com.a2a.adjava.xmodele.IModelFactory#createMAttribute(java.lang.String, java.lang.String, boolean, boolean, boolean, com.a2a.adjava.types.ITypeDescription, java.lang.String)
     */
    @Override
    public MAttribute createMAttribute(String p_sName, String p_sVisibility, boolean p_bIdentifier,
                                       boolean p_bDerived, boolean p_bTranscient, ITypeDescription p_oTypeDescription, String p_sDocumentation) {
        return new MAttribute(p_sName, p_sVisibility, p_bIdentifier, p_bDerived, p_bTranscient, p_oTypeDescription, p_sDocumentation);
    }

    /**
     * {@inheritDoc}
     *
     * @see com.a2a.adjava.xmodele.IModelFactory#createMAttribute(java.lang.String, java.lang.String, boolean, boolean, boolean, com.a2a.adjava.types.ITypeDescription, java.lang.String, boolean)
     */
    @Override
    public MAttribute createMAttribute(String p_sName, String p_sVisibility, boolean p_bIdentifier,
                                       boolean p_bDerived, boolean p_bTransient, ITypeDescription p_oTypeDescription, String p_sDocumentation, boolean p_bReadOnly) {
        return new MAttribute(p_sName, p_sVisibility, p_bIdentifier, p_bDerived, p_bTransient, p_oTypeDescription, p_sDocumentation, p_bReadOnly);
    }

    /**
     * {@inheritDoc}
     *
     * @see com.a2a.adjava.xmodele.IModelFactory#createPage(com.a2a.adjava.xmodele.MScreen, com.a2a.adjava.xmodele.IDomain, java.lang.String, com.a2a.adjava.uml.UmlClass, com.a2a.adjava.xmodele.MPackage, com.a2a.adjava.xmodele.MViewModelImpl, boolean)
     */
    @Override
    public MPage createPage(MScreen p_oParent, IDomain<IModelDictionary, IModelFactory> p_oDomain, String p_sPageName, UmlClass p_oUmlPage, MPackage p_oPackage, MViewModelImpl p_oVmImpl, boolean p_bTitled) {
        return new MPage(p_oParent, p_sPageName, p_oUmlPage, p_oPackage, p_oVmImpl, p_bTitled);
    }

    /**
     * {@inheritDoc}
     *
     * @see com.a2a.adjava.xmodele.IModelFactory#createScreen(java.lang.String, java.lang.String, com.a2a.adjava.xmodele.MPackage)
     */
    @Override
    public MScreen createScreen(String p_sUmlName, String p_sName, MPackage p_oScreenPackage) {
        return new MScreen(p_sUmlName, p_sName, p_oScreenPackage);
    }

    /**
     * {@inheritDoc}
     *
     * @see com.a2a.adjava.xmodele.IModelFactory#createScreen(java.lang.String, java.lang.String, com.a2a.adjava.xmodele.MPackage)
     */
    @Override
    public MComment createComment(String p_sUmlName, String p_sName) {
        return new MComment(p_sUmlName, p_sName);
    }

    /**
     * {@inheritDoc}
     *
     * @see com.a2a.adjava.xmodele.IModelFactory#createNavigation(java.lang.String, com.a2a.adjava.xmodele.ui.navigation.MNavigationType, com.a2a.adjava.xmodele.MScreen, com.a2a.adjava.xmodele.MScreen)
     */
    @Override
    public MNavigation createNavigation(String p_sNavigationName, MNavigationType p_oNavigationType, MScreen p_oScreen, MScreen p_oScreenEnd) {
        return new MNavigation(p_sNavigationName, p_oNavigationType, p_oScreen, p_oScreenEnd);
    }

    /**
     * {@inheritDoc}
     *
     * @see com.a2a.adjava.xmodele.IModelFactory#createMenuDef(java.lang.String)
     */
    @Override
    public MMenuDef createMenuDef(String p_sId) {
        return new MMenuDef(p_sId);
    }

    /**
     * {@inheritDoc}
     *
     * @see com.a2a.adjava.xmodele.IModelFactory#createMenu(java.lang.String)
     */
    @Override
    public MMenu createMenu(String p_sId) {
        return new MMenu(p_sId);
    }

    /**
     * {@inheritDoc}
     *
     * @see com.a2a.adjava.xmodele.IModelFactory#createMenuItem()
     */
    @Override
    public MMenuItem createMenuItem() {
        return new MMenuItem();
    }

    /**
     * {@inheritDoc}
     *
     * @see com.a2a.adjava.xmodele.IModelFactory#createViewModel(java.lang.String, java.lang.String, com.a2a.adjava.xmodele.MPackage, com.a2a.adjava.xmodele.ui.viewmodel.ViewModelType, com.a2a.adjava.xmodele.MEntityImpl, java.lang.String, boolean)
     */
    @Override
    public MViewModelImpl createViewModel(String p_sName, String p_sUmlName, MPackage p_oPackage, ViewModelType p_oType,
                                          MEntityImpl p_oEntityToUpdate, String p_sPathToModel, boolean p_bCustomizable) {
        return new MViewModelImpl(p_sName, p_sUmlName, p_oPackage, p_oType, p_oEntityToUpdate, p_sPathToModel, p_bCustomizable, new MMapping());
    }

    /**
     * {@inheritDoc}
     *
     * @see com.a2a.adjava.xmodele.IModelFactory#createAction(java.lang.String, com.a2a.adjava.xmodele.MActionInterface, boolean, com.a2a.adjava.xmodele.MPackage, com.a2a.adjava.xmodele.MViewModelImpl, com.a2a.adjava.xmodele.MDaoInterface, java.util.List, java.lang.String)
     */
    @Override
    public MAction createAction(String p_sName, MActionInterface p_oActionInterface, boolean p_bIsRoot, MPackage p_oPackage,
                                MViewModelImpl p_oViewModel, MDaoInterface p_oDao, List<MDaoInterface> p_listExternalDaos, String p_sCreatorName) {
        return new MAction(p_sName, p_oActionInterface, p_bIsRoot, p_oPackage, p_oViewModel, p_oDao, p_listExternalDaos, p_sCreatorName);
    }

    /**
     * {@inheritDoc}
     *
     * @see com.a2a.adjava.xmodele.IModelFactory#createActionInterface(java.lang.String, boolean, com.a2a.adjava.xmodele.MPackage, java.lang.String, java.lang.String, java.lang.String, java.lang.String, com.a2a.adjava.xmodele.MEntityImpl, com.a2a.adjava.xmodele.MActionType)
     */
    @Override
    public MActionInterface createActionInterface(String p_sName, boolean p_bRoot, MPackage p_oPackage, String p_sInNameClass, String p_sOutNameClass,
                                                  String p_sStepClass, String p_sProgressClass, MEntityImpl p_oEntity, MActionType p_oActionType) {
        return new MActionInterface(p_sName, p_bRoot, p_oPackage, p_sInNameClass, p_sOutNameClass, p_sStepClass, p_sProgressClass, p_oEntity, p_oActionType);
    }

    /**
     * {@inheritDoc}
     *
     * @see com.a2a.adjava.xmodele.IModelFactory#createNavigationButton(java.lang.String, java.lang.String, java.lang.String, com.a2a.adjava.xmodele.ui.navigation.MNavigation)
     */
    @Override
    public MNavigationButton createNavigationButton(String p_sName, String p_sLabelId, String p_sLabelValue,
                                                    MNavigation p_oNavigation) {
        return new MNavigationButton(p_sName, p_sLabelId, p_sLabelValue, MButtonType.NAVIGATION, p_oNavigation);
    }

    /**
     * {@inheritDoc}
     *
     * @see com.a2a.adjava.xmodele.IModelFactory#createNavigationButton(java.lang.String, com.a2a.adjava.xmodele.ui.navigation.MNavigation)
     */
    @Override
    public MNavigationButton createNavigationButton(String p_sName, MNavigation p_oNavigation) {
        return new MNavigationButton(p_sName, MButtonType.NAVIGATION.getLabelId(), MButtonType.NAVIGATION.getLabelValue(),
                MButtonType.NAVIGATION, p_oNavigation);
    }

    /**
     * {@inheritDoc}
     *
     * @see com.a2a.adjava.xmodele.IModelFactory#createSaveButton(java.lang.String, com.a2a.adjava.xmodele.MAction)
     */
    @Override
    public MActionButton createSaveButton(String p_sName, MAction p_oAction) {
        MActionButton r_oActionButton = new MActionButton(p_sName, MButtonType.SAVE.getLabelId(),
                MButtonType.SAVE.getLabelValue(), MButtonType.SAVE, p_oAction);
        return r_oActionButton;
    }

    /**
     * {@inheritDoc}
     *
     * @see com.a2a.adjava.xmodele.IModelFactory#createDeleteButton(java.lang.String, com.a2a.adjava.xmodele.MAction)
     */
    @Override
    public MActionButton createDeleteButton(String p_sName, MAction p_oAction) {
        MActionButton r_oActionButton = new MActionButton(p_sName,
                MButtonType.DELETE.getLabelId(), MButtonType.DELETE.getLabelValue(), MButtonType.DELETE, p_oAction);
        return r_oActionButton;
    }

    /**
     * {@inheritDoc}
     *
     * @see com.a2a.adjava.xmodele.IModelFactory#createMEntity(java.lang.String, com.a2a.adjava.xmodele.MPackage, java.lang.String, java.lang.String)
     */
    @Override
    public MEntityImpl createMEntity(String p_sName, MPackage p_oPackage,
                                     String p_sUmlName, String p_sEntityName) {
        return new MEntityImpl(p_sName, p_oPackage, p_sUmlName, p_sEntityName);
    }

    /**
     * {@inheritDoc}
     *
     * @see com.a2a.adjava.xmodele.IModelFactory#createDaoImpl(java.lang.String, java.lang.String, com.a2a.adjava.xmodele.MPackage, com.a2a.adjava.xmodele.MEntityImpl, com.a2a.adjava.xmodele.MEntityInterface, java.lang.String)
     */
    @Override
    public MDaoImpl createDaoImpl(String p_sDaoName, String p_sDaoBeanName,
                                  MPackage p_oPackageDao, MEntityImpl p_oEntity,
                                  MEntityInterface p_oEntityInterface, String p_sQueryDefinitionFile) {
        return new MDaoImpl(p_sDaoName, p_sDaoBeanName, p_oPackageDao, p_oEntity, p_oEntityInterface, p_sQueryDefinitionFile);
    }

    /**
     * {@inheritDoc}
     *
     * @see com.a2a.adjava.xmodele.IModelFactory#createDaoInterface(java.lang.String, java.lang.String, com.a2a.adjava.xmodele.MPackage, com.a2a.adjava.xmodele.MDaoImpl, com.a2a.adjava.xmodele.MEntityImpl)
     */
    @Override
    public MDaoInterface createDaoInterface(String p_sInterfaceName,
                                            String p_sBeanName, MPackage p_oDaoInterfacePackage, MDaoImpl p_oDao,
                                            MEntityImpl p_oClass) {
        return new MDaoInterface(p_sInterfaceName, p_sBeanName, p_oDaoInterfacePackage, p_oDao, p_oClass);
    }

    /**
     * {@inheritDoc}
     *
     * @see com.a2a.adjava.xmodele.IModelFactory#createDaoSignature(String, String, String, ITypeDescription, List, List, boolean)
     */

    @Override
    public MDaoMethodSignature createDaoSignature(String p_sName, String p_sVisibility, String p_sType,
                                                  ITypeDescription p_oReturnedType, List<String> p_listReturnedProperties, List<String> p_listNeedImports,
                                                  boolean p_bByValue) {

        return new MDaoMethodSignature(p_sName, p_sVisibility, p_sType, p_oReturnedType, p_listReturnedProperties, p_listNeedImports, p_bByValue);
    }

    /**
     * {@inheritDoc}
     *
     * @see com.a2a.adjava.xmodele.IModelFactory#createVisualField(java.lang.String, com.a2a.adjava.types.IUITypeDescription, com.a2a.adjava.xmodele.ui.view.MVFModifier, com.a2a.adjava.xmodele.ui.view.MVFLabelKind, com.a2a.adjava.xmodele.MAttribute, com.a2a.adjava.xmodele.IDomain, java.lang.String, boolean)
     */
    @Override
    public MVisualField createVisualField(String p_sPrefix, MLabel p_oLabel, IUITypeDescription p_oTypeVisual,
                                          MVFModifier p_oModifier, MVFLabelKind p_oLabelKind, MAttribute p_oAttribute,
                                          IDomain<IModelDictionary, IModelFactory> p_oDomain, String p_sAttributeName,
                                          boolean p_bMandatory) {
        return this.createVisualField(p_sPrefix, p_oLabel, p_oTypeVisual, p_oModifier, p_oLabelKind, p_oAttribute,
                MVFLocalization.DEFAULT, p_oDomain, p_sAttributeName, p_bMandatory);
    }

    /**
     * {@inheritDoc}
     *
     * @see com.a2a.adjava.xmodele.IModelFactory#createVisualField(java.lang.String, com.a2a.adjava.types.IUITypeDescription, com.a2a.adjava.xmodele.ui.view.MVFModifier, com.a2a.adjava.xmodele.ui.view.MVFLabelKind, com.a2a.adjava.xmodele.MAttribute, com.a2a.adjava.xmodele.ui.view.MVFLocalization, com.a2a.adjava.xmodele.IDomain, java.lang.String, boolean)
     */
    @Override
    public MVisualField createVisualField(String p_sPrefix, MLabel p_oLabel, IUITypeDescription p_oTypeVisual,
                                          MVFModifier p_bMVFModifier, MVFLabelKind p_oLabelKind, MAttribute p_oAttribute,
                                          MVFLocalization p_oLocalisation, IDomain<IModelDictionary, IModelFactory> p_oDomain,
                                          String p_sAttributeName, boolean p_bMandatory) {

        MVisualField mvf = new MVisualField(p_sPrefix + (p_bMVFModifier == MVFModifier.READONLY ? "__value" : "__edit"), p_oLabel,
                p_oTypeVisual.getComponentType(p_bMVFModifier), p_oAttribute.getTypeDesc().getEditType(),
                p_oAttribute.getLength(), p_oAttribute.getPrecision(), p_oAttribute.getScale(),
                p_oLabelKind, p_oLocalisation, p_sAttributeName, p_bMandatory, p_oAttribute.getMEnumeration(), p_bMVFModifier == MVFModifier.READONLY, p_oTypeVisual.getUmlName());

        if (p_bMVFModifier == MVFModifier.READONLY) {
            mvf.setReadOnly(true);
        }

        return mvf;

    }

    /**
     * {@inheritDoc}
     *
     * @see com.a2a.adjava.xmodele.IModelFactory#createVisualField(java.lang.String, java.lang.String, java.lang.String, com.a2a.adjava.xmodele.ui.view.MVFLabelKind, com.a2a.adjava.xmodele.ui.view.MVFLocalization, java.lang.String, boolean)
     */
    @Override
    public MVisualField createVisualField(String p_sName, MLabel p_oLabel, String p_sComponent,
                                          MVFLabelKind p_oLabelKind, MVFLocalization p_oTarget, String p_sAttributeName,
                                          boolean p_bMandatoryKind) {
        return new MVisualField(p_sName, p_oLabel, p_sComponent, p_oLabelKind, p_oTarget,
                p_sAttributeName, p_bMandatoryKind);
    }

    /**
     * {@inheritDoc}
     *
     * @see com.a2a.adjava.xmodele.IModelFactory#createVisualField(java.lang.String, java.lang.String, java.lang.String, com.a2a.adjava.xmodele.ui.view.MVFLabelKind, java.lang.String, boolean)
     */
    @Override
    public MVisualField createVisualField(String p_sName, MLabel p_oLabel, String p_sComponent,
                                          MVFLabelKind p_oLabelKind, String p_sAttributeName, boolean p_bMandatoryKind) {
        return new MVisualField(p_sName, p_oLabel, p_sComponent, p_oLabelKind, p_sAttributeName,
                p_bMandatoryKind);
    }

    /**
     * {@inheritDoc}
     *
     * @see com.a2a.adjava.xmodele.IModelFactory#createVisualField(java.lang.String, com.a2a.adjava.xmodele.MVisualField)
     */
    @Override
    public MVisualField createVisualField(String p_sPath, MVisualField p_oField, MLabel p_oLabel) {
        return new MVisualField(p_sPath, p_oField, p_oLabel);
    }

    /**
     * {@inheritDoc}
     *
     * @see com.a2a.adjava.xmodele.IModelFactory#createViewModelCreator(java.lang.String, com.a2a.adjava.xmodele.MVisualField)
     */
    @Override
    public MViewModelCreator createViewModelCreator(String p_sName, MPackage p_oPackage) {
        return new MViewModelCreator(p_sName, p_oPackage);
    }

    /**
     * {@inheritDoc}
     *
     * @see com.a2a.adjava.xmodele.IModelFactory#createMultiPanelConfig()
     */
    @Override
    public MMultiPanelConfig createMultiPanelConfig() {
        return new MMultiPanelConfig();
    }

    @Override
    public String createVisualFieldNameForFixedListCombo(
            MViewModelImpl p_oViewModel, String p_sTName) {
        return StringUtils.join("selected", p_oViewModel.getParameterValue("baseName"), "Item", "_", p_sTName);
    }

    @Override
    public MMenuActionItem createMenuActionItem(String p_sId) {
        return new MMenuActionItem(p_sId);
    }

    @Override
    public MAdapter createExternalAdapter(IDomain<IModelDictionary, IModelFactory> p_oDomain,
                                          ViewModelTypeConfiguration p_oTypeParameters, MViewModelImpl p_oVm, String p_sBaseName) {
        MAdapter oAdapter = new MAdapter(p_oDomain.getLanguageConf().getAdapterImplementationNamingPrefix()
                + p_sBaseName
                + p_oDomain.getLanguageConf().getAdapterImplementationNamingSuffix(),
                p_oVm.getPackage(),
                p_oTypeParameters.getAdapterName(),
                p_oTypeParameters.getAdapterFullName());
        return oAdapter;
    }

    @Override
    public String createPropertyNameForFixedListCombo(
            MViewModelImpl p_oViewModel, String p_sAttrName) {

        String sAttrCapitalized = StringUtils.capitalize(p_sAttrName);
        String sPropertyName = createVisualFieldNameForFixedListCombo(p_oViewModel, sAttrCapitalized);
        return sPropertyName.replace(StringUtils.join('_', sAttrCapitalized), StringUtils.join('.', sAttrCapitalized));
    }

    @Override
    public String createVmAttributeNameForCombo(
            MViewModelImpl p_oViewModel) {
        return "o" + p_oViewModel.getMasterInterface().getName();
    }

    @Override
    public MLabel createLabel(String p_sBaseName, MViewModelImpl p_oViewModel) {

        String sKey = createLabelKey(p_sBaseName, "label");
        String sValue = createLabelValue(p_sBaseName);

        return new MLabel(sKey, sValue);
    }

    @Override
    public MLabel createLabelFromVisualField(String p_sPath, MVisualField p_oVisualField) {
        String sKey = (!StringUtils.isEmpty(p_sPath) ? p_sPath + "_" : "") + p_oVisualField.getLabel().getKey();
        return new MLabel(sKey, p_oVisualField.getLabel().getValue());
    }

    @Override
    public MLabel createLabelForAttributeOfFixedList(String p_sName, String p_sViewModelUmlName, MVFLocalization p_oLocalization,
                                                     MViewModelImpl p_oViewModel) {
        // Legacy
        String sKey = createLabelKey(p_sName, "label");
        String sValue = createLabelValue(p_sName);
        return new MLabel(sKey, sValue);
    }

    @Override
    public MLabel createLabelForAttributeOfCombo(String p_sName, String p_sVisualFieldName, MVFLocalization p_oLocalization,
                                                 MViewModelImpl p_oViewModel) {
        String sBaseName = null;
        if (MVFLocalization.DETAIL.equals(p_oLocalization)) {
            sBaseName = p_sName;
        } else {
            sBaseName = p_sVisualFieldName;
        }
        String sKey = createLabelKey(sBaseName, "label");
        String sValue = createLabelValue(sBaseName);
        return new MLabel(sKey, sValue);
    }


    @Override
    public MLabel createLabelForScreen(MScreen p_oScreen) {
        String sLabel = createLabelKey(p_oScreen.getName().toLowerCase(), "title");
        String sValue = createLabelValue(p_oScreen.getName());
        return new MLabel(sLabel, sValue);
    }

    public MLabel createLabelForPage(MPage p_oPage) {
        String sLabel = createLabelKey(p_oPage.getName().toLowerCase(), "title");
        String sValue = createLabelValue(p_oPage.getName());
        return new MLabel(sLabel, sValue);
    }

    @Override
    public Map<String, MLabel> createLabelsForEnumeration(MEnumeration p_oEnumeration) {

        Map<String, MLabel> r_listLabels = new HashMap<>();

        for (String sToken : p_oEnumeration.getEnumValues()) {
            String sKey = createLabelKey("enum_" + p_oEnumeration.getName() + "_" + sToken);
            r_listLabels.put(sKey, new MLabel(sKey, sToken));
        }

        return r_listLabels;
    }

    @Override
    public MLabel createLabelForButton(String p_sKey, String p_sValue) {
        return new MLabel(p_sKey, p_sValue);
    }

    protected String createLabelKey(String... p_sBaseName) {
        String sLabel = StringUtils.join(p_sBaseName, "__");
        return sLabel;
    }

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
    public MLabel createLabelForCombo(String p_sBaseName1, String p_sBaseName2,
                                      MViewModelImpl p_oParentVm) {
        return this.createLabel(p_sBaseName1, p_oParentVm);
    }

    @Override
    public MLabel createLabelForFixedList(String p_sBaseName1, String p_sBaseName2, MViewModelImpl p_oParentVm) {
        return this.createLabel(p_sBaseName1, p_oParentVm);
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
        return new MAssociationOneToOne(p_sName, p_oRefClass, p_oOppositeClass, p_sVariableName, p_sVariableListName,
                p_sParameterName, p_oTypeDescription, p_sVisibility, p_bRelationOwner, p_bIsNotNull, p_sOppositeName,
                p_oAggregateType, p_oOppositeAggregateType, p_bOppositeNavigable);
    }

}
