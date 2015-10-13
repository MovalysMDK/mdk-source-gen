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
package com.a2a.adjava.languages.ios.umlupdater;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.messages.MessageHandler;
import com.a2a.adjava.uml.UmlAttribute;
import com.a2a.adjava.uml.UmlClass;
import com.a2a.adjava.uml.UmlModel;
import com.a2a.adjava.umlupdater.AbstractUmlUpdater;

/**
 * Uml forbidden properties updater
 */
public class UmlForbiddenPropertiesUpdater extends AbstractUmlUpdater {


	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(UmlForbiddenPropertiesUpdater.class);

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.umlupdater.UmlUpdater#execute(com.a2a.adjava.uml.UmlModel, java.util.Map)
	 */
	@Override
	public void execute(UmlModel p_oUmlModele, Map<String, ?> p_oGlobalSession)
			throws Exception {

		for( UmlClass oUmlClass : p_oUmlModele.getDictionnary().getAllClasses()) {			
			for( UmlAttribute oAttr : oUmlClass.getAttributes()) {
				for (String sForbiddenNameKey : this.getParametersMap().keySet()) {
					String[] oForbiddenKeyComponents = sForbiddenNameKey.split(",");
					boolean bIsWarning = oForbiddenKeyComponents[1].trim().equalsIgnoreCase("warning");
					String sForbiddenName = oForbiddenKeyComponents[0].trim();

					if (sForbiddenName.equals(oAttr.getName())
						|| oAttr.getName().endsWith("."+sForbiddenName)) {
						String sMessage = "Attribute {} is forbidden. "
								+ "You should rename it with an non-forbidden name like {}";

						if(bIsWarning) {

							sMessage = "Attribute {} is forbidden. The attribute was renamed with : {}";
							
							MessageHandler.getInstance().addWarning(sMessage,sForbiddenName,this.getParametersMap().get(sForbiddenNameKey));
							String sNewName = oAttr.getName().replaceAll(sForbiddenName, this.getParametersMap().get(sForbiddenNameKey));
							oAttr.setName(sNewName, oUmlClass);
						}
						else {
							MessageHandler.getInstance().addError(sMessage,sForbiddenName,this.getParametersMap().get(sForbiddenNameKey));
						}
					}
					else if ( ("@"+sForbiddenName).equals(oAttr.getName())) {
						if(bIsWarning) {
							oAttr.setName("@"+this.getParametersMap().get(sForbiddenNameKey), oUmlClass);
						}
					}
				}
			}
		}
	}

}
