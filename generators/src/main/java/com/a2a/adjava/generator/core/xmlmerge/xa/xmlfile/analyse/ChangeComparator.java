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

import java.util.Comparator;

import com.a2a.adjava.generator.core.xmlmerge.xa.xmlfile.model.XANode;

/**
 * Permet d'ordonner les changements, l'ordre dépend de la profondeur des noeuds traités
 * @author smaitre
 *
 */
public class ChangeComparator implements Comparator<Change> {
	
	/**
	 * Variable Multiplier
	 */
	private int multiplier = 1;

	/**
	 * Init multiplicateur
	 * 
	 * @param p_iMultiplier
	 */
	public ChangeComparator(int p_iMultiplier) {
		this.multiplier = p_iMultiplier;
	}

	/**
	 * Compare deux changements
	 * 
	 * @param p_oChange1
	 * @param p_oChange21
	 * 
	 * @return int
	 */
	@Override
	public int compare(Change p_oChange1, Change p_oChange2) {
		XANode oNode1 = p_oChange1.getOldNode();
		if (oNode1 == null) {
			oNode1 = p_oChange1.getNewNode();
		}
		XANode oNode2 = p_oChange2.getOldNode();
		if (oNode2 == null) {
			oNode2 = p_oChange2.getNewNode();
		}
		Integer iInt1 = oNode1.getDepth();
		Integer iInt2 = oNode2.getDepth();
		return iInt1.compareTo(iInt2) * multiplier;
	}

}
