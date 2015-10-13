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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

import com.a2a.adjava.messages.Message.MessageSeverity;
import com.google.gson.Gson;

/**
 * Message handler, used to manage generation log messages (warning, error, info, debug)
 * @author lmichenaud
 *
 */
public final class MessageHandler {

	/**
	 * Nom du fichier de sauvegarde des informations pour l'éxécution
	 */
	private static String ADJAVA_MESSAGES_OUTPUT_FILENAME = "adjavaMessages.json";
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
		List<Message> messages = getListMessages();
		for (Message oMessage : messages) {
			Object[] listParameters = new Object[1];
			listParameters[0] = oMessage.getSeverity().toString();
			if ( oMessage.getParameters() != null ) {
				for( Object oParam : oMessage.getParameters()) {
					listParameters = ArrayUtils.add(listParameters, oParam);
				}
			}
			p_oLogger.error( StringUtils.join("[{}] ", oMessage.getMessage()), listParameters);
		}
		
		try {
			writeMessages(messages);
		} catch (IOException e) {
			//TODO
		}

	}

	/**
	 * Writes ADJAVA messages to JSon file to be read by mdk-cli
	 * @param messages The messages to write
	 * @throws IOException An exception during IO.
	 */
	private void writeMessages(List<Message> messages) throws IOException {
		//Formatting message for JSON file 
		List<Message> jsonMessage = new ArrayList<>();
		for(int messageIndex = 0; messageIndex < messages.size() ; messageIndex++) {
		    FormattingTuple tp = MessageFormatter.arrayFormat(StringUtils.join("[{}] ", messages.get(messageIndex).getMessage()), messages.get(messageIndex).getParameters());
		    Message newMessage = new Message(tp.getMessage(), messages.get(messageIndex).getSeverity());
		    jsonMessage.add(newMessage);
		}
		
		//Write Json file
		Gson messagesAsJson = new Gson();
		String jsonAsString = messagesAsJson.toJson(jsonMessage);
		File oOutputFile = new File(ADJAVA_MESSAGES_OUTPUT_FILENAME);
		oOutputFile.createNewFile();
		FileOutputStream oFileOutputStream = new FileOutputStream(oOutputFile);
		OutputStreamWriter oOutputStreamWriter =new OutputStreamWriter(oFileOutputStream);
		oOutputStreamWriter.append(jsonAsString);
		oOutputStreamWriter.close();
		oFileOutputStream.close();
	}
}
