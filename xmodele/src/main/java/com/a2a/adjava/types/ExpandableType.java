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
package com.a2a.adjava.types;

/**
 * ExpandableType values are used inside complex language type descriptions to describe the way entity data is  stored,
 * and how the entities tree is generated.
 */
public enum ExpandableType {

	/**
	 * Data is stored inside the entity containing the attribute. The attribute type is a plain object (stored inside
	 * the Movalys Framework, or any other object).
	 * Example: On the Android platform, AddressLocation is handled this way.
	 * By default complex types are initialized to NONE.
	 */
	NONE,

	/**
	 * Data is stored in a table generated specifically for this kind of object. The database mapping is described
	 * inside the type description. In the table a list id is added, which is a foreign key in the entity containing
	 * the attribute. A full entity is generated in order to handle the attribute (with factory, dao,...).
	 *
	 * Example: On the Android platform, PhotoList is handled this way.
	 */
	ONE_TO_MANY,

	/**
	 * Data is stored in a table generated specifically for this kind of object. The database mapping is described
	 * inside the type description. The primary key of this table is used as a foreign key in the entity containing
	 * the attribute. A full entity is generated in order to handle the attribute (with factory, dao,...).
	 *
	 * Example: On the IOS platform, AddressLocation is handled this way.
	 */
	ONE_TO_ONE,

	/**
	 * Data is stored inside the entity containing the attribute. An entity is generated in order to handle the
	 * attribute. Data binding is left to be handled in a platform specific approach.
	 */
	EMBEDDED
}
