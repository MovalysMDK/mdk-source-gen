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
package com.a2a.adjava.uml2xmodele.operations;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.languages.LanguageConfiguration;
import com.a2a.adjava.messages.MessageHandler;
import com.a2a.adjava.types.ITypeDescription;
import com.a2a.adjava.utils.StrUtils;
import com.a2a.adjava.xmodele.MAssociation;
import com.a2a.adjava.xmodele.MAssociationManyToMany;
import com.a2a.adjava.xmodele.MAssociationManyToOne;
import com.a2a.adjava.xmodele.MAssociationOneToOne;
import com.a2a.adjava.xmodele.MAssociationPersistableManyToMany;
import com.a2a.adjava.xmodele.MAssociationWithForeignKey;
import com.a2a.adjava.xmodele.MAttribute;
import com.a2a.adjava.xmodele.MEntityImpl;
import com.a2a.adjava.xmodele.MIdentifierElem;
import com.a2a.adjava.xmodele.MMethodCriteriaParameter;
import com.a2a.adjava.xmodele.MMethodParameter;

/**
 * <p>
 * Classe d'analyse des noms des operations du modele Uml
 * </p>
 * 
 * <p>
 * Copyright (c) 2009
 * <p>
 * Company: Adeuza
 * 
 * @author lmichenaud
 * 
 */
public class UmlOperationAnalysis {
	/**
	 * Logger for this class
	 */
	private static final Logger log = LoggerFactory.getLogger(UmlOperationAnalysis.class);

	/**
	 * Template name for method getEntity
	 */
	private static final String GET_ENTITE = "getEntite";

	/**
	 * Template name for method getListEntity
	 */
	private static final String GET_LIST_ENTITE = "getListEntite";

	/**
	 * Template name for method getPaginatedList
	 */
	private static final String GET_LISTRANGE_ENTITE = "getPaginatedList";

	/**
	 * Template name for method getChamps
	 */
	private static final String GET_CHAMPS = "getChamps";

	/**
	 * Template name for method getNbEntite
	 */
	private static final String GET_NBENTITE = "getNbEntite";

	/**
	 * Template name for method setChamps
	 */
	private static final String SET_CHAMPS = "setChamps";

	/**
	 * Template name for method existEntity
	 */
	private static final String EXISTE_ENTITE = "existEntite";

	/**
	 * Template name for method deleteEntity
	 */
	private static final String DELETE_ENTITE = "deleteEntite";

	/**
	 * Operation name
	 */
	private String operationName;

	/**
	 * Uml class name
	 */
	private String umlClassName;

	/**
	 * Normalized class name
	 */
	private String normalizedClassName;

	/**
	 * Entity class
	 */
	private MEntityImpl mClass;

	/**
	 * Method returned type
	 */
	private ITypeDescription returnedType;

	/**
	 * Parameters passed by value
	 */
	private boolean byValue;

	/**
	 * Method needs a generation with parameters passed by value
	 */
	private boolean needByValue;

	/**
	 * Type
	 */
	private String type;

	/**
	 * Parameter list
	 */
	private List<MMethodParameter> listParameters;

	/**
	 * Import list
	 */
	private List<String> importList;

	/**
	 * Returned properties.
	 * If null, all properties of the entity
	 */
	private List<String> returnedProperties;

	/**
	 * Many to many associations
	 */
	private List<MAssociationManyToMany> manyToManyAssocations;

	/**
	 * Language configuration
	 */
	private LanguageConfiguration lngConf;

	/**
	 * Constructor
	 * @param p_sOperationName operation name
	 * @param p_sUmlClassName name of uml class
	 * @param p_oClass entity class
	 * @param p_xLngConf language configuration
	 * @param p_bByValue true if parameters are passed by value
	 */
	public UmlOperationAnalysis(String p_sOperationName, String p_sUmlClassName, MEntityImpl p_oClass,
			LanguageConfiguration p_xLngConf, boolean p_bByValue) {
		super();
		this.operationName = p_sOperationName;
		this.umlClassName = p_sUmlClassName;
		this.normalizedClassName = this.umlClassName;
		this.mClass = p_oClass;
		this.byValue = p_bByValue;

		int iPos = this.normalizedClassName.indexOf('_');
		while (iPos != -1) {
			this.normalizedClassName = this.normalizedClassName.substring(0, iPos)
					+ this.normalizedClassName.substring(iPos + 1, iPos + 2).toUpperCase(Locale.getDefault())
					+ this.normalizedClassName.substring(iPos + 2);
			iPos = this.normalizedClassName.indexOf('_');
		}
		this.lngConf = p_xLngConf;
	}

	/**
	 * Analyse operation
	 * @return true if success
	 * @throws Exception exception
	 */
	public boolean analyse() throws Exception {
		log.debug("> UmlOperationAnalysis.analyse, operation name: {}", this.operationName);
		boolean r_bSuccess = this.findTypeOperation() && this.parseParameters() && this.findReturnedType();
		log.debug("< UmlOperationAnalysis.analyse");
		return r_bSuccess;
	}

	/**
	 * Return parameter list
	 * @return parameter list
	 */
	public List<MMethodParameter> getListParameters() {
		return this.listParameters;
	}

	/**
	 * Return result type
	 * @return result type
	 */
	public ITypeDescription getReturnedType() {
		return this.returnedType;
	}

	/**
	 * Return type
	 * @return type
	 */
	public String getType() {
		return this.type;
	}

	/**
	 * Return normalized operation name
	 * @return normalized operation name
	 */
	public String getNormalizedOperationName() {
		return this.operationName;
	}

	/**
	 * Return needed import list
	 * @return needed import list
	 */
	public List<String> getImports() {
		return this.importList;
	}

	/**
	 * Return returned properties
	 * @return returned properties
	 */
	public List<String> getReturnedProperties() {
		return this.returnedProperties;
	}

	/**
	 * Return true if method must be generated with parameter passed by value
	 * @return true if method must be generated with parameter passed by value
	 */
	public boolean isNeedByValue() {
		return this.needByValue;
	}

	/**
	 * Return associated many to many associations
	 * @return associated many to many associations
	 */
	public List<MAssociationManyToMany> getManyToManyAssociations() {
		return this.manyToManyAssocations;
	}

	/**
	 * Parse parameters
	 * @return true if parse success
	 */
	private boolean parseParameters() {

		boolean r_bSuccess = true;

		this.manyToManyAssocations = new ArrayList<MAssociationManyToMany>();
		this.listParameters = new ArrayList<MMethodParameter>();
		this.importList = new ArrayList<String>();
		this.returnedProperties = new ArrayList<String>();
		this.needByValue = false;

		StringBuffer sNewOperationName = new StringBuffer();

		int iPos = this.operationName.indexOf("_By_");
		if (iPos != -1) {

			sNewOperationName.append(this.operationName.substring(0, iPos));
			sNewOperationName.append("By");

			String[] t_sParameters = StringUtils.substringAfterLast(this.operationName, "_By_").split("_");
			for (int i = 0; i < t_sParameters.length; i++) {
				String sParameterName = t_sParameters[i];

				String sCriteriaName = sParameterName;
				MAttribute oAttribute = this.mClass.getAttributeByName(sCriteriaName, false);
				if (oAttribute != null) {

					if (i != 0) {
						sNewOperationName.append("And");
					}
					sNewOperationName.append(getCriteriaName(oAttribute.getName()));

					MMethodParameter oMethodParameter = new MMethodCriteriaParameter(
							oAttribute.getParameterName(), oAttribute.getTypeDesc(), oAttribute, true);
					this.listParameters.add(oMethodParameter);
					if (!oAttribute.getTypeDesc().isPrimitif()) {
						this.addImport(oAttribute.getTypeDesc().getName());
					}
					log.debug("criteria '{}' corresponds to attribute '{}'", sParameterName, oAttribute.getName());
				} else {
					MAssociation oAssociation = this.mClass.getAssociationByCritereName(sCriteriaName);

					if (oAssociation instanceof MAssociationWithForeignKey) {

						if (i != 0) {
							sNewOperationName.append("And");
						}
						sNewOperationName.append(getCriteriaName(oAssociation.getName()));

						if (oAssociation.isRelationOwner()) {

							log.debug("criteria '{}' corresponds to association '{}'", sParameterName, oAssociation.getName());
							if (!this.byValue) {
								MMethodParameter oMethodParameter = new MMethodCriteriaParameter(
										oAssociation.getParameterName(), oAssociation.getTypeDesc(),
										(MAssociationWithForeignKey) oAssociation, this.byValue);
								this.listParameters.add(oMethodParameter);
								this.addImport(oAssociation.getTypeDesc().getName());
								this.needByValue = true;

							} else {
								for (MIdentifierElem oMIdentifierElem : oAssociation.getRefClass()
										.getIdentifier().getElems()) {
									if (oMIdentifierElem instanceof MAttribute) {
										MAttribute oAttr = (MAttribute) oMIdentifierElem;
										sParameterName = oAttr.getTypeDesc().computeParameterName(oAssociation.getName() + oAttr.getName());
										MMethodParameter oMethodParameter = new MMethodCriteriaParameter(
												sParameterName, oAttr.getTypeDesc(), oAttr, this.byValue);
										this.listParameters.add(oMethodParameter);
										if (!oAttr.getTypeDesc().isPrimitif()) {
											this.addImport(oAttr.getTypeDesc().getName());
										}
									} else {
										MAssociation oIdentifierAssociation = (MAssociation) oMIdentifierElem;
										MMethodParameter oMethodParameter = new MMethodCriteriaParameter(
												oIdentifierAssociation.getParameterName(),
												oIdentifierAssociation.getTypeDesc(),
												(MAssociationWithForeignKey) oIdentifierAssociation, false);
										this.listParameters.add(oMethodParameter);
										this.addImport(oIdentifierAssociation.getTypeDesc().getName());
									}
								}
							}
						} else {
							r_bSuccess = false;
							MessageHandler.getInstance().addWarning(
								"Dans l'operation '{}' de la classe '{}', impossibilite d'utiliser le critere '{}' ( not relation owner )",
									this.operationName, this.umlClassName, sParameterName);
						}
					} else if (oAssociation instanceof MAssociationPersistableManyToMany) {

						MAssociationPersistableManyToMany oMAssociationManyToMany = (MAssociationPersistableManyToMany) oAssociation;

						this.manyToManyAssocations.add(oMAssociationManyToMany);

						if (i != 0) {
							sNewOperationName.append("And");
						}
						sNewOperationName.append(getCriteriaName(oMAssociationManyToMany
								.getNameForJoinClass()));

						log.debug("criteria '{}' corresponds to association '{}'", sParameterName, oMAssociationManyToMany.getNameForJoinClass());
						if (!this.byValue) {
							ITypeDescription oObjectType = (ITypeDescription) this.lngConf
									.getTypeDescription("Object").clone();
							oObjectType.setName(oMAssociationManyToMany.getRefClass().getMasterInterface()
									.getFullName());
							sParameterName = oObjectType.computeParameterName(oMAssociationManyToMany.getNameForJoinClass());

							MMethodParameter oMethodParameter = new MMethodCriteriaParameter(sParameterName,
									oObjectType, oMAssociationManyToMany, false);
							this.listParameters.add(oMethodParameter);
							this.addImport(oMAssociationManyToMany.getTypeDesc().getName());
							this.needByValue = true;

						} else {
							for (MAttribute oAttr : oMAssociationManyToMany.getCriteriaAttrs()) {
								sParameterName = oAttr.getTypeDesc().computeParameterName(oMAssociationManyToMany.getNameForJoinClass() + oAttr.getName());
								MMethodParameter oMethodParameter = new MMethodCriteriaParameter(
										sParameterName, oAttr.getTypeDesc(), oAttr, true);
								this.listParameters.add(oMethodParameter);
								if (!oAttr.getTypeDesc().isPrimitif()) {
									this.addImport(oAttr.getTypeDesc().getName());
								}
							}
						}
					} else {
						r_bSuccess = false;
						MessageHandler.getInstance().addError(
							"Dans l'operation '{}' de la classe '{}', impossibilite de detecter le critere '{}'",						
								this.operationName, this.umlClassName, sParameterName );
					}
				}
			}

			if (this.type.equals(GET_CHAMPS)) {
				String[] sReturnedProperties = StringUtils.substringAfter(this.operationName, "_By_").split("_");
				for (int i = 0; i < sReturnedProperties.length; i++) {
					String sParameterName = sReturnedProperties[i];

					String sCriteriaName = sParameterName.substring(0, 1).toLowerCase(Locale.getDefault())
							+ sParameterName.substring(1);
					MAttribute oAttribute = this.mClass.getAttributeByName(sCriteriaName, false);
					if (oAttribute != null) {
						this.returnedProperties.add(sCriteriaName);
					} else {
						MAssociation oAssociation = this.mClass.getAssociationByName(sCriteriaName, false);
						if (oAssociation instanceof MAssociationManyToOne
								|| oAssociation instanceof MAssociationOneToOne) {
							this.returnedProperties.add(sCriteriaName);
						} else {
							MessageHandler.getInstance().addError(
								"In operation '{}' of class '{}', can't detect criteria '{}'",							
									this.umlClassName, this.operationName, sParameterName );
							r_bSuccess = false;
						}
					}
				}
			}
		}

		if (r_bSuccess) {
			this.operationName = sNewOperationName.toString();
		}

		return r_bSuccess;
	}

	/**
	 * Find operation type
	 * @return true if success
	 * @throws Exception exception
	 */
	private boolean findTypeOperation() throws Exception {

		boolean r_bSuccess = true;

		// GetEntite
		String sChaine = StringUtils.join("(?i)get", this.umlClassName, ".*");
		if (this.operationName.matches(sChaine)) {
			normalizeOperationName("get");
			this.type = GET_ENTITE;
		} else {
			// GetListEntite
			sChaine =  StringUtils.join("(?i)get(List|Vecteur)", this.umlClassName, ".*" );
			if (this.operationName.matches(sChaine)) {
				normalizeOperationName("getList");
				this.type = GET_LIST_ENTITE;
			} else {
				// GetListEntite
				sChaine = "getPaginatedList" + this.umlClassName;
				if (this.operationName.startsWith(sChaine)) {
					this.operationName = "getPaginatedList" + this.umlClassName;
					this.type = GET_LISTRANGE_ENTITE;
				} else {
					// GetNbEntite
					sChaine = "getNb" + this.umlClassName;
					if (this.operationName.startsWith(sChaine)) {
						this.type = GET_NBENTITE;
					} else {
						// Get_Champs
						sChaine = "get_";
						if (this.operationName.startsWith(sChaine)) {
							this.type = GET_CHAMPS;
						} else {
							// SetChamps
							sChaine = "set_" + this.umlClassName;
							if (this.operationName.startsWith(sChaine)) {
								this.type = SET_CHAMPS;
							} else {
								// Existe Entite
								sChaine = "exist_By_";
								if (this.operationName.startsWith(sChaine)) {
									this.type = EXISTE_ENTITE;
								} else {
									// Delete Entite
									sChaine = "delete_By_";
									if (this.operationName.startsWith(sChaine)) {
										normalizeOperationName("delete");
										this.type = DELETE_ENTITE;
									} else {
										MessageHandler
												.getInstance()
												.addWarning(
													"Dans la classe '{}', Can't determine method type : '{}'",
														this.umlClassName, this.operationName );
										
										
										r_bSuccess = false;
									}
								}
							}
						}
					}
				}
			}
		}
		log.debug("type: {]", this.type);
		log.debug("operation normalized name: {}", this.operationName);
		return r_bSuccess;
	}


	/**
	 * Normalize operation name
	 * @param p_sPrefix prefix
	 */
	private void normalizeOperationName(String p_sPrefix) {
		String sParameter = StringUtils.EMPTY;
		int iCriteriaPos = this.operationName.indexOf("_By_");
		if (iCriteriaPos != -1) {
			sParameter = this.operationName.substring(iCriteriaPos);
		}
		this.operationName = p_sPrefix + this.normalizedClassName + sParameter;
	}

	/**
	 * Ajout un import
	 * @param p_sImport import
	 */
	public void addImport(String p_sImport) {
		if (!this.importList.contains(p_sImport) && p_sImport.lastIndexOf(StrUtils.DOT) != -1) {
			String sPackage = p_sImport.substring(0, p_sImport.lastIndexOf(StrUtils.DOT));
			if ( !sPackage.startsWith("java.lang")) {
				this.importList.add(p_sImport);
			}
		}
	}

	/**
	 * Compute criteria name
	 * @param p_sName name
	 * @return criteria name
	 */
	private String getCriteriaName(String p_sName) {
		return StringUtils.capitalize(p_sName);
	}

	/**
	 * Find the returned type
	 * @return true if success
	 */
	private boolean findReturnedType() {

		boolean r_bSuccess = true;

		if (this.type.equals(GET_ENTITE)) {
			ITypeDescription oObjectTypeDescription = this.lngConf.getTypeDescription("Object");
			this.returnedType = (ITypeDescription) oObjectTypeDescription.clone();
			this.returnedType.setName(this.mClass.getMasterInterface().getFullName());
		}

		if (this.type.equals(GET_LIST_ENTITE) || this.type.equals(GET_LISTRANGE_ENTITE)) {
			ITypeDescription oObjectTypeDescription = this.lngConf.getTypeDescription("Object");
			ITypeDescription oContainedElemType = (ITypeDescription) oObjectTypeDescription.clone();
			oContainedElemType.setName(this.mClass.getMasterInterface().getFullName());

			this.returnedType = (ITypeDescription) this.lngConf.getTypeDescription("List").clone();
			this.returnedType.setParameterizedElementType(oContainedElemType);
		}

		if (this.type.equals(GET_NBENTITE)) {
			this.returnedType = this.lngConf.getTypeDescription("int");
		}

		if (this.type.equals(GET_CHAMPS)) {
			this.returnedType = (ITypeDescription) this.lngConf.getTypeDescription("List").clone();
		}

		if (this.type.equals(EXISTE_ENTITE)) {
			this.returnedType = this.lngConf.getTypeDescription("boolean");
		}

		if (this.type.equals(SET_CHAMPS) || this.type.equals(DELETE_ENTITE)) {
			this.returnedType = null;
		}

		return r_bSuccess;
	}
}
