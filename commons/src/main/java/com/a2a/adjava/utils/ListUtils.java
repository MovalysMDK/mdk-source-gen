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
package com.a2a.adjava.utils;

import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;

/**
 * List Utils
 * @author lmichenaud
 *
 */
public class ListUtils {

	/**
	 * Substract list2 items from list1
	 * @param p_list1 list1
	 * @param p_list2 list2
	 * @return result of substraction
	 */
	public static <T> Collection<T> subtract( Collection<T> p_list1, Collection<T> p_list2 ) {
		return CollectionUtils.subtract( p_list1, p_list2);
	}
}
