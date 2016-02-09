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
package com.a2a.adjava.xmi.v21mduml;

import java.io.File;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.messages.MessageHandler;
import com.a2a.adjava.uml.UmlModel;
import com.a2a.adjava.xmi.XMIReader;

/**
 * @author lmichenaud
 *
 */
@RunWith(JUnit4.class)
public class TestReader {

	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(TestReader.class);
	
	/**
	 * Uml Model
	 */
	private UmlModel modele ;
	
	/**
	 * @throws Exception
	 */
	@Before
	public void loadTestFile() throws Exception {		
//		File oXmlFile = new File("src/test/resources/Sans Titre1.xml");
//		SAXReader oSaxReader = new SAXReader();
//		Document xDoc = oSaxReader.read(oXmlFile);
//		XMIReader oXmiReader = new XMI21Reader();
//		this.modele = oXmiReader.read(xDoc, null);
//		
//		MessageHandler.getInstance().logMessages(log);
	}
	
	/**
	 * 
	 */
	@Test
	public void test() {
		
	}
}
