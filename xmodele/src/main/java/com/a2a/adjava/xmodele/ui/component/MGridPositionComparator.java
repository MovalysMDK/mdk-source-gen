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
package com.a2a.adjava.xmodele.ui.component;

import java.util.Comparator;


/**
 * @author lmichenaud
 * 
 */
public class MGridPositionComparator implements Comparator<MGridSectionConfig> {

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(MGridSectionConfig p_oSectionConfig1,
			MGridSectionConfig p_oSectionConfig2) {

		MGridPosition oPos1 = p_oSectionConfig1.getPosition();
		MGridPosition oPos2 = p_oSectionConfig2.getPosition();

		// if max value, return 1 to be in last position
		if (oPos1.getColumn() < p_oSectionConfig2.getPosition()
				.getColumn()
				|| (oPos1.getColumn() == p_oSectionConfig2.getPosition()
						.getColumn() && oPos1.getSection() < p_oSectionConfig2
						.getPosition().getSection())) {
			return -1;
		} else if (oPos1.getColumn() > oPos2.getColumn()
				|| (oPos1.getColumn() == oPos2.getColumn() && oPos1
						.getSection() > oPos2.getSection())) {
			return 1;
		}
		return 0;
	}

}