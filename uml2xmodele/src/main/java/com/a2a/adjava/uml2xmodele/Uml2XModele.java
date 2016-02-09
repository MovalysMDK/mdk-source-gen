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
package com.a2a.adjava.uml2xmodele;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.a2a.adjava.extractors.MExtractor;
import com.a2a.adjava.messages.MessageHandler;
import com.a2a.adjava.mupdater.MUpdater;
import com.a2a.adjava.projectupgrader.ProjectUpgrader;
import com.a2a.adjava.projectupgrader.ProjectUpgrader.ProjectUpgraderMode;
import com.a2a.adjava.projectupgrader.ProjectUpgraderComparator;
import com.a2a.adjava.uml.UmlModel;
import com.a2a.adjava.umlupdater.UmlUpdater;
import com.a2a.adjava.utils.VersionHandler;
import com.a2a.adjava.versions.GenerationDescriptor;
import com.a2a.adjava.versions.GenerationHistory;
import com.a2a.adjava.versions.GenerationHistoryHolder;
import com.a2a.adjava.versions.ProjectUpgraderDescriptor;
import com.a2a.adjava.xmodele.IDomain;
import com.a2a.adjava.xmodele.IModelDictionary;
import com.a2a.adjava.xmodele.IModelFactory;
import com.a2a.adjava.xmodele.XDomainRegistry;

/**
 * <p>Convert Uml model to XModele</p>
 *
 * <p>Copyright (c) 2011
 * <p>Company: Adeuza
 *
 * @author lmichenaud
 *
 */

public class Uml2XModele {


	/**
	 * Convert uml Modele to XModele for each domain
	 * @param p_oModele Uml Model
	 * @param p_oDomainRegistry Domain Registry
	 * @param p_oProjectConfig project config
	 * @throws Exception
	 */
	public void convert( UmlModel p_oModele, XDomainRegistry p_oDomainRegistry) throws Exception {

		for( IDomain<IModelDictionary,IModelFactory> oDomain : p_oDomainRegistry.getDomains()) {

			// clone uml model
			UmlModel oUmlModel = p_oModele.copy();

			// start uml updaters specific to domain
			Map<String, ?> oMapSession = new HashMap<String, Object>();
			for( UmlUpdater oUmlUpdater : oDomain.getUmlUpdaters()) {
				oUmlUpdater.execute(oUmlModel, oMapSession);
			}

			for( MExtractor<?> oExtractor : oDomain.getExtractors()) {

				// validate uml model
				oExtractor.preValidate(oUmlModel);

				// start extractors
				if (!MessageHandler.getInstance().hasErrors()) {
					oExtractor.extract(oUmlModel);
				}
			}
		}


		if (!MessageHandler.getInstance().hasErrors()) {

			for( IDomain<IModelDictionary,IModelFactory> oDomain : p_oDomainRegistry.getDomains()) {

				Map<String, ?> oMapSession = new HashMap<String, Object>();
				for( MUpdater oMUpdater : oDomain.getMUpdaters()) {
					oMUpdater.execute(oDomain, oMapSession);

					if (MessageHandler.getInstance().hasErrors()) {
						break;
					}
				}

				if (MessageHandler.getInstance().hasErrors()) {
					break;
				}
			}
		}


		//Application des upgraders (outils de migration pré-génération)
		Uml2XModele.applyUpgraders(ProjectUpgraderMode.PROJECT_UPGRADER_MODE_BEFORE_GEN, p_oModele, p_oDomainRegistry);

	}
	
	public static void applyUpgraders(ProjectUpgraderMode p_oProjectUpgraderMode, UmlModel p_oModele, XDomainRegistry p_oDomainRegistry) throws Exception {
		GenerationHistoryHolder.getInstance();
		GenerationHistory oGenerationHistory = GenerationHistoryHolder.generationHistory;
		GenerationDescriptor oCurrentGeneration = oGenerationHistory.getGenerations().get(oGenerationHistory.getGenerations().size()-1);
		if(!oCurrentGeneration.getExecutionMetadataList().getInfoSaveBeanList().get(0).getExecutionId().equalsIgnoreCase(GenerationDescriptor.FIRST_GENERATION_ATTRIBUTE_VALUE)) {
			String sLastMDKGenerationVersion = VersionHandler.getLastMDKGenerationVersion().split("-")[0];
			String sCurrentMDKGenerationVersion = VersionHandler.getCurrentMDKGenerationVersion().split("-")[0];

			// Apply Upgraders
			if (!MessageHandler.getInstance().hasErrors() && !sLastMDKGenerationVersion.equals(sCurrentMDKGenerationVersion)) {
				ProjectUpgraderComparator oProjectUpgraderComparator = new ProjectUpgraderComparator();
				ProjectUpgraderDescriptor oTemporaryDescriptor = new ProjectUpgraderDescriptor();
				for( IDomain<IModelDictionary,IModelFactory> oDomain : p_oDomainRegistry.getDomains()) {

					Map<String, UmlModel> oMapSession = new HashMap<String, UmlModel>();
					oMapSession.put("uml-model", p_oModele);
					List<String> oAppliedProjectUpgraders = new ArrayList<String>();

					//Tri des project upgraders par numéro de versions
					Collections.sort(oDomain.getProjectUpgraders(), oProjectUpgraderComparator);

					for( ProjectUpgrader oProjectUpgrader : oDomain.getProjectUpgraders()) {

						//On vérifie si le projectUpgrader n'est pas destiné une version du MDK inférieure à la version de la
						//précédente génération. Si c'est le cas on ne doit pas l'appliquer.
						boolean bShouldApplyProjectUpgrader = !oProjectUpgraderComparator.isAnteriorToLastGeneration(oProjectUpgrader);

						//On vérifie qu'un projectUpgrader similaire n'a pas déjà été appliqué lors de CETTE génération
						for(String sSimilarProjectUpgrader : oProjectUpgrader.getSimilarProjectUpgraders()) {
							if(sSimilarProjectUpgrader.equalsIgnoreCase(oProjectUpgrader.getClass().getName())) {
								bShouldApplyProjectUpgrader = false;
								break;
							}
						}

						//On vérifie que ce project Upgrader n'a pas déja été applique lors d'une précédente génération
						//NE DEVRAIT JAMAIS ARRIVER
						for(ProjectUpgraderDescriptor oAlreadyAppliedDescriptor : oCurrentGeneration.getProjectUpgraderList().getProjectUpgraders()) {
							oTemporaryDescriptor.setParametersFromMap(oProjectUpgrader.getParametersMap());
							if(oAlreadyAppliedDescriptor.equals(oTemporaryDescriptor)) {
								System.out.println("[ProjectUpgraders] - Le project upgrader: "+ oProjectUpgrader.getClass().getName() + 
										"a déjà été appliqué lors d'une précédente génération, le "+ oAlreadyAppliedDescriptor.getDate());
								bShouldApplyProjectUpgrader = false;
								break;
							}
						}

						bShouldApplyProjectUpgrader = bShouldApplyProjectUpgrader 
								&& oProjectUpgrader.getMode().equals(p_oProjectUpgraderMode);

						//Application du projectUpgrader et ajout à la liste des PU déja appliqués.
						if(bShouldApplyProjectUpgrader) {
							oProjectUpgrader.execute(oDomain, oMapSession);
							oAppliedProjectUpgraders.add(oProjectUpgrader.getClass().getName());
							//Ajout du project-upgrader à l'historique des project-upgarders pour cette génération.
							oCurrentGeneration.addProjectUpgraderDescriptor(oProjectUpgrader.getParametersMap());
						}

						if (MessageHandler.getInstance().hasErrors()) {
							break;
						}
					}
					if (MessageHandler.getInstance().hasErrors()) {
						break;
					}
					System.out.println("[ProjectUpgraders] - pre-generation - applied : "+ oAppliedProjectUpgraders );
				}
			}
		}
	}
}
