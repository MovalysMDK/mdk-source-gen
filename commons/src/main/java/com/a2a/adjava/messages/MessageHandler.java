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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import com.a2a.adjava.messages.Message.MessageSeverity;

/**
 * Message handler, used to manage generation log messages (warning, error, info, debug)
 * @author lmichenaud
 *
 */
public final class MessageHandler {

	/**
	 * 
	 */
	private static ThreadLocal<MessageHandler> threadLocal = new ThreadLocal<MessageHandler>();
	
	/**
	 * Message list
	 */
	private List<Message> listMessages = new ArrayList<Message>();
	
	/**
	 * True if contains one log with severity error
	 */
	private boolean hasErrors = false ;
	
	/**
	 * Constructor
	 */
	private MessageHandler() {
		// private because singleton
	}
	
	/**
	 * @return
	 */
	public static MessageHandler getInstance() {
		MessageHandler r_oMessageHandler = threadLocal.get();
		if ( r_oMessageHandler == null ) {
			r_oMessageHandler = new MessageHandler();
			threadLocal.set(r_oMessageHandler);
		}
		return r_oMessageHandler ;
	}
	
	/**
	 * @param p_sErrorMessage
	 */
	public void addError( String p_sErrorMessage ) {
		this.listMessages.add( new Message( p_sErrorMessage, MessageSeverity.ERROR ));
		this.hasErrors = true ;
	}
	
	/**
	 * @param p_sErrorMessage
	 */
	public void addError( String p_sErrorMessage, Object... p_oParameters ) {
		this.listMessages.add( new Message( p_sErrorMessage, MessageSeverity.ERROR, p_oParameters ));
		this.hasErrors = true ;
	}
	
	/**
	 * @param p_sErrorMessage
	 */
	public void addWarning( String p_sErrorMessage ) {
		this.listMessages.add( new Message( p_sErrorMessage, MessageSeverity.WARN ));
	}
	
	/**
	 * @param p_sErrorMessage
	 */
	public void addWarning( String p_sErrorMessage, Object... p_oParameters ) {
		this.listMessages.add( new Message( p_sErrorMessage, MessageSeverity.WARN, p_oParameters ));
	}
	
	/**
	 * @param p_sErrorMessage
	 */
	public void addInfo( String p_sErrorMessage ) {
		this.listMessages.add( new Message( p_sErrorMessage, MessageSeverity.INFO ));
	}

	/**
	 * @return
	 */
	public List<Message> getListMessages() {
		return this.listMessages;
	}

	/**
	 * @return
	 */
	public boolean hasErrors() {
		return this.hasErrors;
	}	
	
	/**
	 * Reset du MessageHandler
	 */
	public void reset() {
		this.hasErrors = false ;
		this.listMessages.clear();
	}
	
	/**
	 * Log messages
	 * @param p_oLogger logger to use
	 */
	public void logMessages( Logger p_oLogger ) {
		for (Message oMessage : getListMessages()) {
			Object[] listParameters = new Object[1];
			listParameters[0] = oMessage.getSeverity().toString();
			if ( oMessage.getParameters() != null ) {
				for( Object oParam : oMessage.getParameters()) {
					listParameters = ArrayUtils.add(listParameters, oParam);
				}
			}
			
			p_oLogger.error( StringUtils.join("[{}] ", oMessage.getMessage()), listParameters);
		}
	}
}
