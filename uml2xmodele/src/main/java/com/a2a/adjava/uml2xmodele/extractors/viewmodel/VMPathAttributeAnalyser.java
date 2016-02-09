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
package com.a2a.adjava.uml2xmodele.extractors.viewmodel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.messages.MessageHandler;
import com.a2a.adjava.uml.UmlAssociation;
import com.a2a.adjava.uml.UmlAssociationEnd;
import com.a2a.adjava.uml.UmlAttribute;
import com.a2a.adjava.uml.UmlClass;
import com.a2a.adjava.uml.UmlDictionary;
import com.a2a.adjava.uml.UmlUsage;
import com.a2a.adjava.uml2xmodele.extractors.PojoExtractor;
import com.a2a.adjava.uml2xmodele.extractors.viewmodel.associations.VMAssoPathProcessor;
import com.a2a.adjava.uml2xmodele.extractors.viewmodel.associations.VMAssoPathManyToManyAggregate;
import com.a2a.adjava.uml2xmodele.extractors.viewmodel.associations.VMAssoPathManyToManyComposite;
import com.a2a.adjava.uml2xmodele.extractors.viewmodel.associations.VMAssoPathManyToOneAggregate;
import com.a2a.adjava.uml2xmodele.extractors.viewmodel.associations.VMAssoPathManyToOne;
import com.a2a.adjava.uml2xmodele.extractors.viewmodel.associations.VMAssoPathOneToMany;
import com.a2a.adjava.uml2xmodele.extractors.viewmodel.associations.VMAssoPathOneToOne;
import com.a2a.adjava.xmodele.IDomain;
import com.a2a.adjava.xmodele.IModelDictionary;
import com.a2a.adjava.xmodele.IModelFactory;
import com.a2a.adjava.xmodele.MEntityImpl;
import com.a2a.adjava.xmodele.MViewModelImpl;

/**
 * Analyse uml vm attributes.
 * Uml attribute of a VM contains a path (a.b.c...) that needs to be analyzed
 * @author lmichenaud
 *
 */
public class VMPathAttributeAnalyser {

	/**
	 * Singleton instance
	 */
	private static VMPathAttributeAnalyser instance = new VMPathAttributeAnalyser();

	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(VMPathAttributeAnalyser.class);

	/**
	 * Association path processors
	 * Allow to apply treatements on path that matches a uml association.
	 */
	private List<VMAssoPathProcessor> assoPaths = new ArrayList<VMAssoPathProcessor>();

	/**
	 * Constructor
	 */
	private VMPathAttributeAnalyser() {
		this.assoPaths.add( new VMAssoPathOneToOne());
		this.assoPaths.add( new VMAssoPathOneToMany());
		this.assoPaths.add( new VMAssoPathManyToOne());
		this.assoPaths.add( new VMAssoPathManyToOneAggregate());
		this.assoPaths.add( new VMAssoPathManyToManyAggregate());
		this.assoPaths.add( new VMAssoPathManyToManyComposite());
	}

	/**
	 * Return singleton instance
	 * @return singleton instance
	 */
	public static VMPathAttributeAnalyser getInstance() {
		return instance;
	}

	/**
	 * Treat each attributes in view model uml class
	 * @param p_oMasterVM ViewModel
	 * @param p_oVMUmlClass uml class of viewmodel
	 * @param p_oAssociatedEntity entity linked to viewmodel
	 * @param p_oUmlDict uml dictionnary
	 * @throws Exception error
	 */
	public void treatViewModelAttributes( MViewModelImpl p_oMasterVM, UmlClass p_oVMUmlClass, MEntityImpl p_oAssociatedEntity, 
			UmlDictionary p_oUmlDict, Map<String, MViewModelImpl> p_otherViewModels, IDomain<IModelDictionary, IModelFactory> p_oDomain ) throws Exception {

		//analyse des attributs : 2 cas de figures
		// soit le nom de l'attribut comporte des . : c'est un chemin vers le model,
		// dans le cas contraire c'est un attribut calculé pour le view model
		for(UmlAttribute oVMUmlAttribute : p_oVMUmlClass.getAttributes()) {

			log.debug("traitement de {}", oVMUmlAttribute.getName());

			VMPathContext oVMPathContext = new VMPathContext(oVMUmlAttribute.getName(), p_oMasterVM, p_oMasterVM.getMapping());

			if ( oVMPathContext.getNbElements() == 1) {
				//le champ n'est pas un chemin
				// il faut ajouter juste une méthode get permettant de calculer l'attribut
				log.debug("ajout direct");
				if ( oVMUmlAttribute.getDataType() == null ) {
					MessageHandler.getInstance().addError(
						"Derived Attribute named '{}' of the class '{}' ({}) must have a data type.", 
							oVMUmlAttribute.getName(), p_oVMUmlClass.getName(), p_oVMUmlClass.getFullName());
				}
				VMAttributeHelper.getInstance().convertDerivedAttribute(StringUtils.EMPTY, oVMUmlAttribute, p_oMasterVM, false, false, p_oDomain);
				oVMPathContext.setEntityTarget(p_oAssociatedEntity);
				if (oVMPathContext.getEntityTarget() != null) {
					VMAttributeHelper.getInstance().addIdFieldToClass(p_oMasterVM, oVMPathContext.getEntityTarget(), StringUtils.EMPTY, p_oDomain);
				}
			}
			else {

				// il faut calculer le chemin
				log.debug("traitement d'un chemin");

				// un chemin commence forcément par usage
				// recherche de l'usage
				for(UmlUsage oUsage  : p_oVMUmlClass.getUsages()) {
					log.debug("cherche cible {}/{}", oVMPathContext.getCurrentPath(), oUsage.getName());
					String[] tCurrentUsageName = oUsage.getName().split("\\.");
					if ( oVMPathContext.getCurrentPath().equals(tCurrentUsageName[tCurrentUsageName.length-1])) {
						oVMPathContext.setUmlTarget(oUsage.getSupplier());
						oVMPathContext.setEntityTarget(p_oDomain.getDictionnary().getMapUmlClassToMClasses().get(oUsage.getSupplier().getFullName()));
						log.debug("cible trouvée : {}", oUsage.getSupplier().getName());
						oVMPathContext.setValid(true);
						VMAttributeHelper.getInstance().addIdFieldToClass(p_oMasterVM, oVMPathContext.getEntityTarget(), StringUtils.EMPTY, p_oDomain);
					}
				}

				if ( oVMPathContext.isValid()) {
					
					// Not last element
					if (!oVMPathContext.isNextLastElement()) {
	
						log.debug("not end of path, current index = {}, stop at index = ",
								oVMPathContext.getIndex(), (oVMPathContext.getNbElements()-1));
						//on n'est pas à la fin du chemin
						while( !oVMPathContext.isNextLastElement()) {
	
							oVMPathContext.moveNext();						
							log.debug("traitement de {}", oVMPathContext.toString());
	
							oVMPathContext.setValid(false);
	
							// on cherche une association qui porte le nom de tNames
							for(UmlAssociation oAssociation : getUmlAssociationsWithTarget(oVMPathContext.getUmlTarget(), p_oUmlDict)) {
								this.treatPathElementOfVMAttribute(oAssociation, 
										oVMPathContext, p_otherViewModels, p_oUmlDict, oVMUmlAttribute, p_oDomain);
								if (oVMPathContext.isValid()) {
									break;
								}
							} // end for uml association
	
							if ( !oVMPathContext.isValid()) {
								MessageHandler.getInstance().addError("Unknown relation type: {}", oVMPathContext.toString());
								log.error("  Unknown relation type: {}", oVMPathContext.toString());
							}
						} // fin while
					}
					// Last element
					else  {
						log.debug("end of path");
						oVMPathContext.setEntityTarget(p_oDomain.getDictionnary().getMapUmlClassToMClasses().get(
								oVMPathContext.getUmlTarget().getFullName()));
					}
					
					// If valid, convert attribute
					if (oVMPathContext.isValid()) {
						oVMPathContext.moveNext();
						//on recopie le champ depuis le model il faut le convertir
						VMAttributeHelper.getInstance().createVMAttributesFromUmlAttr(oVMPathContext, oVMUmlAttribute, p_oDomain);
					}
				}
				else {
					MessageHandler.getInstance().addError("Attribute '{}' of panel '{}' does not match any uml usage between the panel and an entity.",
						oVMUmlAttribute.getName(), p_oVMUmlClass.getName());
				}
			}

		} // end for
	}


	/**
	 * Treat a path element of a uml vm attribute.
	 * Ex: treat "b" element of path : "a.b.c"
	 * @param p_oAsso1
	 * @param p_oAsso2
	 * @param p_oVMPathContext.getUmlTarget target uml class of usage
	 * @param p_oMasterVM master view
	 */
	private void treatPathElementOfVMAttribute( UmlAssociation p_oAsso,
			VMPathContext p_oVMPathContext, 
			Map<String, MViewModelImpl> p_oVMCache, UmlDictionary p_oUmlDict, 
			UmlAttribute p_oUmlAttribute, IDomain<IModelDictionary, IModelFactory> p_oDomain) throws Exception {
		log.debug("  treatPathElementOfVMAttribute");

		UmlAssociationEnd oAsso1 = p_oAsso.getAssociationEnd1();
		UmlAssociationEnd oAsso2 = p_oAsso.getAssociationEnd2();

		// la classe oTarget est un bout de l'association
		// le nom des association peut être en deux parties
		String[] t_sName1 = null;
		if(oAsso1.getName() != null) {
			t_sName1 = oAsso1.getName().split("_");
			for(int i = 0; i<t_sName1.length;i++) {
				t_sName1[i] = t_sName1[i].split("$")[0]; // on enlève les options après le $
			}
		}
		
		String[] t_sName2 = null;
		if(oAsso2.getName() != null) {
			t_sName2 = oAsso2.getName().split("_");
			for(int i = 0; i<t_sName2.length;i++) {
				t_sName2[i] = t_sName2[i].split("$")[0]; // on enlève les options après le $
			}
		}

		log.debug("    analyse asso : {}/{}", oAsso1.getName(), oAsso2.getName());

		for( VMAssoPathProcessor oAssoPath: this.assoPaths ) {

			log.debug("      execute : {}", oAssoPath.getClass().getName());
			if ( oAsso2.isNavigable() && oAssoPath.isValid(p_oVMPathContext.getUmlTarget(), oAsso1, oAsso2, 
					p_oVMPathContext.getCurrentElement(), t_sName2, p_oUmlDict)) {
				controlStereotypesFromModelClasses( p_oDomain,  oAsso1, oAsso2 );
				oAssoPath.apply(p_oVMPathContext, oAsso1, oAsso2, t_sName1, t_sName2, 
						p_oUmlAttribute, p_oUmlDict, p_oVMCache, p_oDomain);
				log.debug("    processor valid: {}", oAssoPath.getClass().getName());
				p_oVMPathContext.setValid(true);
				break ;
			}
			else if ( oAsso1.isNavigable() && oAssoPath.isValid(p_oVMPathContext.getUmlTarget(), oAsso2, oAsso1, 
					p_oVMPathContext.getCurrentElement(), t_sName1, p_oUmlDict)) {
				controlStereotypesFromModelClasses( p_oDomain,  oAsso1, oAsso2 );
				oAssoPath.apply(p_oVMPathContext, oAsso2, oAsso1, t_sName2, t_sName1, 
						p_oUmlAttribute, p_oUmlDict, p_oVMCache, p_oDomain);
				log.debug("    processor valid: {}", oAssoPath.getClass().getName());
				p_oVMPathContext.setValid(true);
				break ;
			}
		}

		log.debug("    valid: {}", p_oVMPathContext.isValid());
	}

	/**
	 * Control that all classes from the model part of the UML possess a Mm_Model stereotype.
	 * @param p_oDomain
	 * @param oAsso1
	 * @param oAsso2
	 */
	private void controlStereotypesFromModelClasses(IDomain<IModelDictionary, IModelFactory> p_oDomain, UmlAssociationEnd oAsso1,UmlAssociationEnd oAsso2 )
	{
		PojoExtractor oPojoExtractor = p_oDomain.getExtractor(PojoExtractor.class);
		List<String> p_PojoStereotype =  oPojoExtractor.getPojoContext().getEntityStereotypes();
		if(oAsso1.getRefClass().hasAnyStereotype( p_PojoStereotype) && !oAsso2.getRefClass().hasAnyStereotype(p_PojoStereotype) ||
		   oAsso2.getRefClass().hasAnyStereotype(p_PojoStereotype) && !oAsso1.getRefClass().hasAnyStereotype(p_PojoStereotype))
		{
			MessageHandler.getInstance().addError("The classes \"{}\" et \"{}\" must both have a stereotype in \"{}\"", oAsso1.getRefClass().getName() ,oAsso2.getRefClass().getName(), p_PojoStereotype  );
		}
	}
	
	/**
	 * @param p_oTarget
	 * @param p_oDict
	 * @return
	 */
	private List<UmlAssociation> getUmlAssociationsWithTarget( UmlClass p_oTarget, UmlDictionary p_oDict ) {
		List<UmlAssociation> r_listASso = new ArrayList<UmlAssociation>();
		for(UmlAssociation oAssociation : p_oDict.getAssociations()) {
			UmlAssociationEnd oAsso1 = oAssociation.getAssociationEnd1();
			UmlAssociationEnd oAsso2 = oAssociation.getAssociationEnd2();
			if (oAsso1.getRefClass().equals(p_oTarget) || 
					oAsso2.getRefClass().equals(p_oTarget)) {
				r_listASso.add(oAssociation);
			}
		}
		return r_listASso ;
	}
}
