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
package com.a2a.adjava.uml2xmodele.extractors;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.messages.MessageHandler;
import com.a2a.adjava.uml.UmlAssociation;
import com.a2a.adjava.uml.UmlAssociationEnd;
import com.a2a.adjava.uml.UmlClass;
import com.a2a.adjava.uml.UmlDictionary;
import com.a2a.adjava.uml.UmlModel;
import com.a2a.adjava.uml.UmlUsage;
import com.a2a.adjava.uml2xmodele.ui.screens.CUDActionProcessor;
import com.a2a.adjava.uml2xmodele.ui.screens.ScreenAggregationPanelProcessor;
import com.a2a.adjava.uml2xmodele.ui.screens.ScreenContext;
import com.a2a.adjava.uml2xmodele.ui.screens.ScreenDependencyProcessor;
import com.a2a.adjava.xmodele.IDomain;
import com.a2a.adjava.xmodele.IModelDictionary;
import com.a2a.adjava.xmodele.IModelFactory;
import com.a2a.adjava.xmodele.MPackage;
import com.a2a.adjava.xmodele.MScreen;
import com.a2a.adjava.xmodele.MStereotype;

/**
 * <p>
 * Classe de type Extractor permettant de récupérer dans le flux xml, les
 * données correspondant aux différents Screen.
 * </p>
 * 
 * <p>
 * Copyright (c) 2011
 * </p>
 * <p>
 * Company: Adeuza
 * </p>
 * 
 * @author smaitre
 * @since MF-Annapurna
 */
public class ScreenExtractor extends AbstractExtractor<IDomain<IModelDictionary,IModelFactory>> {

	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(ScreenExtractor.class);

	/**
	 * Screen Extractor Context
	 */
	protected ScreenContext screenContext;

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initialize(Element p_xConfig) throws Exception {
		String sStereotypes = this.getParameters().getValue("stereotypes");
		List<String> listScreenStereotypes = new ArrayList<String>();
		for (String sStereotype : StringUtils.split(sStereotypes, ',')) {
			sStereotype = sStereotype.trim();
			listScreenStereotypes.add(sStereotype);
		}
		this.screenContext = new ScreenContext(listScreenStereotypes, this.getParameters().getValue("stereotype-title"), getParameters(), getDomain());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void extract(UmlModel p_oModele) throws Exception {

 		UmlDictionary oUmlDict = p_oModele.getDictionnary();
				
		// Extract screen from uml class
		// createNewViewModel: CustomerMulti
		for (UmlClass oUmlScreenClass : this.screenContext.getScreenUmlClasses(oUmlDict)) {
			if (this.checkUmlScreen(oUmlScreenClass)) {
				// Create MScreen
				MScreen oScreen = this.createScreen(oUmlScreenClass);
				// Process panel aggregations
				ScreenAggregationPanelProcessor.getInstance().processPanelAggregations(
						oScreen, oUmlScreenClass, this.screenContext );
				
				this.getDomain().getDictionnary().getViewModelCreator().add(oScreen);
				this.getDomain().getDictionnary().registerScreen(oScreen);
			}
		}

		this.treatScreenRelations(oUmlDict);

		// Create CUD Operations (CREATE UPDATE DELETE)
		this.getDomain().getAnalyserAndProcessorFactory().createCUDActionProcessor().treatCUDOperations(this.screenContext, p_oModele.getDictionnary());
		
		this.postCheck();
	}

	/**
	 * 2ème partie on traite les relations entre écrans. Même si on
	 * re-boucle sur les classes screen, on ne peut pas
	 * remonter ce traitement dans la boucle ci-dessus car il faut que tous
	 * les MScreen soient créés pour pouvoir
	 * traiter les dépendances.
	 * @param oUmlDict
	 * @throws Exception
	 */
	protected void treatScreenRelations(UmlDictionary oUmlDict) throws Exception {
		ScreenDependencyProcessor.getInstance().treatScreenRelations(this.screenContext, oUmlDict );
	}
	
	private boolean checkUmlScreen(UmlClass p_oUmlScreenClass) {
		boolean r_bCheck = true;
		UmlClass oTarget = null;
		//il ne peut pas y avoir d'usage  entre un écran et un panel
		for(UmlUsage oUsage : p_oUmlScreenClass.getUsages()) {
			if (oUsage.getClient().equals(p_oUmlScreenClass)) {
				oTarget = oUsage.getSupplier();
			}
			else {
				oTarget = oUsage.getClient();
			}
			
			if (!(this.getScreenContext().isScreen(oTarget) || this.getScreenContext().isScreen(oTarget)
					|| this.getScreenContext().isSearchScreen(oTarget) || this.getScreenContext().isWorkspaceScreen(oTarget)
					|| this.getDomain().getExtractor(MenuExtractor.class).isMenu(oTarget))) {
				r_bCheck = false;
				MessageHandler.getInstance().addError("Usage between {}({}) and {}({}) is not allowed.", p_oUmlScreenClass.getFullName(),p_oUmlScreenClass.getStereotypeNames(),oTarget.getFullName(),oTarget.getStereotypeNames());
			}
		}
		return r_bCheck;
	}
	
	private void postCheck() {
		boolean bHasMainScreen = false;
		for( MScreen oScreen : getDomain().getDictionnary().getAllScreens()) {
			if ( oScreen.isMain()) {
				bHasMainScreen = true ;
				break;
			}
		}
		
		if ( !bHasMainScreen) {
			MessageHandler.getInstance().addWarning("No main screen defined. Add stereotype {} on one of the screen.", 
					this.screenContext.getScreenRootStereotype());
		}
	}
	
	/**
	 * @param p_oScreenUmlClass
	 * @return
	 * @throws Exception
	 */
	protected MScreen createScreen( UmlClass p_oScreenUmlClass ) throws Exception {

		log.debug("ScreenExtractor.createScreen : {}", p_oScreenUmlClass.getName());
		
		// Master package is the uml entity package.
		MPackage oMasterPackage = this.screenContext.computeMasterPackage(p_oScreenUmlClass);
		
		// Compute package for screen
		MPackage oScreenPackage = this.screenContext.computeScreenPackage( oMasterPackage );
		
		// création du Screen principal
		MScreen r_oScreen = this.getDomain().getXModeleFactory().createScreen(
			p_oScreenUmlClass.getName(), p_oScreenUmlClass.getName(), oScreenPackage);			
		
		// si l'écran est page ppale de l'application on lui définie une
		// action d'affichage
		r_oScreen.setMain(this.screenContext.isScreenRoot(p_oScreenUmlClass));
					
		// boolean pour savoir si la classe courante est de type workspace
		r_oScreen.setWorkspace(this.screenContext.isWorkspaceScreen(p_oScreenUmlClass));
		
		r_oScreen.setSearchScreen(this.screenContext.isSearchScreen(p_oScreenUmlClass));
		
		r_oScreen.setDocumentation(p_oScreenUmlClass.getDocumentation());
		return r_oScreen ;
	}
	
	/**
	 * @return
	 */
	public ScreenContext getScreenContext() {
		return this.screenContext;
	}
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.uml2xmodele.extractors.AbstractExtractor#preValidate(com.a2a.adjava.uml.UmlModel)
	 */
	@Override
	public void preValidate(UmlModel p_oModele) throws Exception {
		UmlDictionary oUmlDict = p_oModele.getDictionnary();

		for (UmlClass oUmlScreenClass : this.screenContext.getScreenUmlClasses(oUmlDict)) {
			this.getStereotypesValidator().verifyStereotypesOfObject(oUmlScreenClass);
			List<UmlAssociationEnd> associations = oUmlScreenClass.getAssociations();
			if (associations.size() > 1) {
				Hashtable<String, Integer> table = new Hashtable<String, Integer>();
				for (UmlAssociationEnd oAssociationEnd : associations) {
					UmlAssociation oAsso = oAssociationEnd.getAssociation();
					String assoName = oAsso.getName();
					if (assoName == null || assoName.isEmpty() || table.containsKey(assoName)) {
						MessageHandler.getInstance().addError("Relation" + ((assoName == null || assoName.isEmpty())  ? "{}" : " ({})") + " between {} and {} must have an unique and nonempty name.",
								assoName,
								oUmlScreenClass.getName(),
								oAssociationEnd.getRefClass().getName());
					} else {
						if (oAsso.hasAnyStereotype( this.screenContext.getAggregationPanelStereotype(), 
								this.screenContext.getAggregationPanelWksStereotype())) {
							this.getStereotypesValidator().verifyStereotypesOfObject(oAssociationEnd.getRefClass());
						}
					}
				}
			} else if (associations.size() == 1) {
				UmlAssociationEnd oAssociationEnd = associations.get(0);
				UmlAssociation oAsso = oAssociationEnd.getAssociation();
				if (oAsso.hasAnyStereotype( this.screenContext.getAggregationPanelStereotype(), 
						this.screenContext.getAggregationPanelWksStereotype())) {
					this.getStereotypesValidator().verifyStereotypesOfObject(oAssociationEnd.getRefClass());
				}
			}
		}
	}
}
