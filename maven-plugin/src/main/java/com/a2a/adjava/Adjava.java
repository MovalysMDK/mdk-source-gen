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
package com.a2a.adjava;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.commons.init.AdjavaInitializer;
import com.a2a.adjava.generator.core.GeneratorExecutor;
import com.a2a.adjava.languages.LanguageRegistry;
import com.a2a.adjava.messages.MessageHandler;
import com.a2a.adjava.uml.UmlModel;
import com.a2a.adjava.uml2xmodele.Uml2XModele;
import com.a2a.adjava.versions.GenerationHistory;
import com.a2a.adjava.xmi.XMIFactory;
import com.a2a.adjava.xmodele.XDomainRegistry;

/**
 * 
 * <p>
 * Classe de lancement d'adjava
 * </p>
 * 
 * <p>
 * Copyright (c) 2009
 * <p>
 * Company: Adeuza
 * 
 * @author mmadigand
 * @author lmichenaud
 * 
 */
public class Adjava {

	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(Adjava.class);

	/**
	 * Launch generation
	 * 
	 * @param p_oProjectConfig
	 *            configuration adjava
	 * @param p_oGenerationHistory une liste de métadonnée sur la génération précédente et la génération en cours
	 */
	public void run( AdjavaInitializer p_oAdjavaInitializer, GenerationHistory p_oGenerationHistory ) throws Exception {
		log.debug("> Adjava");

		MessageHandler oMessageHandler = MessageHandler.getInstance();

		// Instancie les services
		XMIFactory oXMIFactory = new XMIFactory();
		LanguageRegistry oLanguageRegistry = new LanguageRegistry();
		XDomainRegistry oDomainRegistry = new XDomainRegistry(oLanguageRegistry);
		Uml2XModele oUml2XModeleConverter = new Uml2XModele();
		GeneratorExecutor oGeneratorExecutor = new GeneratorExecutor();

		p_oAdjavaInitializer.registerInitializingBean(oXMIFactory);
		p_oAdjavaInitializer.registerInitializingBean(oLanguageRegistry);
		p_oAdjavaInitializer.registerInitializingBean(oDomainRegistry);
		p_oAdjavaInitializer.registerInitializingBean(oGeneratorExecutor);
		
		try {
			p_oAdjavaInitializer.load();
			
			if (!oMessageHandler.hasErrors()) {
				UmlModel oUmlModele = oXMIFactory.load();
				if (!oMessageHandler.hasErrors()) {
					oUml2XModeleConverter.convert(oUmlModele, oDomainRegistry);
					
					if (!oMessageHandler.hasErrors()) {
						oGeneratorExecutor.execute(oDomainRegistry);
					}
				}
			}
		}catch( Exception oException){
			if ( !oMessageHandler.hasErrors() ){
				// if no error in message handler, rethrow exception.
				throw oException ;
			}
		} finally {
			oMessageHandler.logMessages(log);
		}

		log.debug("< Adjava");
	}
}
