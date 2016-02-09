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
package com.a2a.adjava.languages.ios.xmodele.views;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlIDREF;

import com.a2a.adjava.languages.ios.xmodele.MIOSStoryBoard;
import com.a2a.adjava.languages.ios.xmodele.connections.MIOSConnection;

/**
 * Button View
 * @author lmichenaud
 *
 */
public class MIOSButtonView extends MIOSView {

	/**
	 * Constructor
	 */
	public MIOSButtonView(){
		super();
	}
	/**
	 * Target story board (on button click)
	 */
	@XmlIDREF
	private MIOSStoryBoard targetStoryBoard ;
	
	/**
	 * Button label
	 */
	@XmlElement
	private String label ;
	
	/**
	 * Connections
	 */
	@XmlElementWrapper
	@XmlElement(name="connection")
	private List<MIOSConnection> connections = new ArrayList<MIOSConnection>();

	/**
	 * Return target storyboard
	 * @return target storyboard
	 */
	public MIOSStoryBoard getTargetStoryBoard() {
		return this.targetStoryBoard;
	}

	/**
	 * Define target storyboard
	 * @param p_oTargetStoryBoard target storyboard
	 */
	public void setTargetStoryBoard(MIOSStoryBoard p_oTargetStoryBoard) {
		this.targetStoryBoard = p_oTargetStoryBoard;
	}

	/**
	 * Return label
	 * @return label
	 */
	public String getLabel() {
		return this.label;
	}

	/**
	 * Define label
	 * @param p_sLabel label
	 */
	public void setLabel(String p_sLabel) {
		this.label = p_sLabel;
	}
	
	/**
	 * Add a connection
	 * @param p_oIOSConnection ios connection
	 */
	public void addConnection( MIOSConnection p_oIOSConnection ) {
		this.connections.add(p_oIOSConnection);
	}

	/**
	 * Return connections
	 * @return connections
	 */
	public List<MIOSConnection> getConnections() {
		return connections;
	}
}
