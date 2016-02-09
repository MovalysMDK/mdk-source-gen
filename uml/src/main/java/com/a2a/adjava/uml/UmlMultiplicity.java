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
package com.a2a.adjava.uml;

/**
 * Uml multiplicity for association end
 * @author lmichenaud
 *
 */
public class UmlMultiplicity {

	/**
	 * Lower.
	 */
	private Integer lower ;
	
	/**
	 * Upper
	 * -1 for unlimited
	 */
	private Integer upper ;

	/**
	 * Return lower
	 * @return lower
	 */
	public Integer getLower() {
		return this.lower;
	}

	/**
	 * Define lower
	 * @param p_iLower lower
	 */
	public void setLower(Integer p_iLower) {
		this.lower = p_iLower;
	}

	/**
	 * Return upper
	 * @return upper
	 */
	public Integer getUpper() {
		return this.upper;
	}

	/**
	 * Define upper
	 * @param p_iUpper upper
	 */
	public void setUpper(Integer p_iUpper) {
		this.upper = p_iUpper;
	}
}
