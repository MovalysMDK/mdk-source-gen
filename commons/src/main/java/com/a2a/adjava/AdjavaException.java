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

import com.a2a.adjava.messages.slf4j.MessageFormatter;

/**
 * @author lmichenaud
 *
 */
public class AdjavaException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1398357889411841836L;

	/**
	 * @param p_sMessage
	 * @param p_listParameters
	 */
	public AdjavaException( String p_sMessage, Object... p_listParameters ) {
		super(MessageFormatter.arrayFormat(p_sMessage, p_listParameters).getMessage());
	}
	
	/**
	 * @param p_sMessage
	 * @param p_oException
	 * @param p_listParameters
	 */
	public AdjavaException( String p_sMessage, Exception p_oException, Object... p_listParameters ) {
		super(MessageFormatter.arrayFormat(p_sMessage, p_listParameters).getMessage(), p_oException);
	}
}
