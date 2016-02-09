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
package com.a2a.adjava.messages;

/**
 * @author lmichenaud
 *
 */
public class Message {

	/**
	 * Message
	 */
	private String message ;
	
	/**
	 * Message parameters
	 */
	private Object[] parameters ;
	
	/**
	 * Message severity
	 */
	private MessageSeverity severity ;

	/**
	 * @author lmichenaud
	 *
	 */
	public enum MessageSeverity {
		ERROR, WARN, INFO ;
	}

	/**
	 * Constructeur
	 * 
	 * @param p_sMessage
	 * @param p_oSeverity
	 */
	public Message(String p_sMessage, MessageSeverity p_oSeverity) {
		this.message = p_sMessage;
		this.severity = p_oSeverity;
	}
	
	/**
	 * Constructeur
	 * 
	 * @param p_sMessage
	 * @param p_oSeverity
	 */
	public Message(String p_sMessage, MessageSeverity p_oSeverity, Object... p_oParameters ) {
		this.message = p_sMessage;
		this.severity = p_oSeverity;
		this.parameters = p_oParameters;
	}

	/**
	 * @return
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @return
	 */
	public MessageSeverity getSeverity() {
		return severity;
	}

	/**
	 * @return
	 */
	public Object[] getParameters() {
		return parameters;
	}
}
