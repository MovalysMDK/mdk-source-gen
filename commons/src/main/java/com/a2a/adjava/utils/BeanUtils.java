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

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * <p>TODO Décrire la classe BeanUtils</p>
 *
 * <p>Copyright (c) 2011
 * <p>Company: Adeuza
 *
 * @author lmichenaud
 *
 */

public class BeanUtils {

	/**
	 * 
	 */
	private BeanUtils() {
		// Utility class
	}
	
	/**
	 * Value une propriete si celle-ci n'est pas null
	 * @param p_oObject objet contenant la propriete a valuer
	 * @param p_sPropertyName nom de la propriete de l'object
	 * @param p_oValue valeur a affecter
	 * @throws IllegalAccessException acces illegal a la propriete
	 * @throws InvocationTargetException echec d'invocation
	 * @throws NoSuchMethodException echec, methode non trouvee
	 */
	public static void setIfNotNull( Object p_oObject, String p_sPropertyName, Object p_oValue ) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		if ( p_oValue != null ) {
			PropertyUtils.setProperty(p_oObject, p_sPropertyName, p_oValue);
		}
	}
	
	/**
	 * Value une propriete si celle-ci n'est pas null
	 * @param p_oObject objet contenant la propriete a valuer
	 * @param p_sPropertyName nom de la propriete de l'object
	 * @param p_sValue valeur a affecter
	 * @throws IllegalAccessException acces illegal a la propriete
	 * @throws InvocationTargetException echec d'invocation
	 * @throws NoSuchMethodException echec, methode non trouvee
	 */
	public static void setStringIfNotNull( Object p_oObject, String p_sPropertyName, String p_sValue ) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		if ( p_sValue != null ) {
			PropertyDescriptor oPropDesc = PropertyUtils.getPropertyDescriptor(p_oObject, p_sPropertyName);
			if ( int.class.isAssignableFrom(oPropDesc.getReadMethod().getReturnType())) {
				PropertyUtils.setProperty(p_oObject, p_sPropertyName, Integer.parseInt(p_sValue));
			}
			else
			if ( long.class.isAssignableFrom(oPropDesc.getReadMethod().getReturnType())) {
				PropertyUtils.setProperty(p_oObject, p_sPropertyName, Long.parseLong(p_sValue));
			}
			else
			if ( Boolean.class.isAssignableFrom(oPropDesc.getReadMethod().getReturnType())) {
				PropertyUtils.setProperty(p_oObject, p_sPropertyName, Boolean.parseBoolean(p_sValue));
			}
			else {
				PropertyUtils.setProperty(p_oObject, p_sPropertyName, p_sValue.trim());
			}
		}
	}
	
	/**
	 * @param p_oObject
	 * @param p_sPropertyName
	 * @param p_sValue
	 * @param p_oEnumClass
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public static void setEnumIfNotNull( Object p_oObject, String p_sPropertyName, String p_sValue, Class<? extends Enum> p_oEnumClass ) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		if ( p_sValue != null ) {
			PropertyUtils.setProperty(p_oObject, p_sPropertyName, Enum.valueOf(p_oEnumClass, p_sValue.trim()));
		}
	}
	
	/**
	 * @param p_oObject
	 * @param p_sPropertyName
	 * @param p_sValue
	 * @param p_oEnumClass
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public static void setEnum( Object p_oObject, String p_sPropertyName, String p_sValue, Enum p_oValue ) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		if ( p_sValue != null ) {
			PropertyUtils.setProperty(p_oObject, p_sPropertyName, Enum.valueOf(p_oValue.getClass(), p_sValue.trim()));
		}
		else {
			PropertyUtils.setProperty(p_oObject, p_sPropertyName, p_oValue );
		}
	}
}
