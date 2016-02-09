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
package com.a2a.adjava.generator.core.xmlmerge.xa.xmlfile.analyse;


/**
 * Opération du changement
 * @author smaitre
 */
public abstract class AbstractChangeType {

	/**
	 * donne la signature d'un changement 
	 * @param p_oChange le changement en cours de traitement
	 * @return la signature du changement
	 */
	public String getSig(Change p_oChange) {
		String r_sSig = null;
		String sKey = null;
		if (p_oChange.getOldNode()!=null) {
			sKey = p_oChange.getOldNode().getUniqueXPathId();
		}
		else  {
			sKey = p_oChange.getNewNode().getUniqueXPathId();
		}
		r_sSig = p_oChange.getType().toString() + "_" + sKey + "_";
		return r_sSig;
	}
}
