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
package com.a2a.adjava.xmi.v12vpuml;

import java.io.File;

import junit.framework.Assert;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.a2a.adjava.uml.UmlEnum;
import com.a2a.adjava.uml.UmlModel;
import com.a2a.adjava.xmi.XMIReader;

/**
 * Test la lecture des énumérations
 * @author smaitre
 */
@RunWith(JUnit4.class)
public class TestEnumeration {

	/** le modèle du fichier */
	private UmlModel modele = null;
	/** enumeration 1 */
	private UmlEnum enum1 = null;
	/** enumeration 2 */
	private UmlEnum enum2 = null;
	
	
	@Before
	public void loadFileTest() throws Exception {
		File xml = new File("src/test/java/com/a2a/adjava/xmi/v12vpuml/test.xmi");
		SAXReader oReader = new SAXReader();
		Document doc = oReader.read(xml);
		XMIReader reader = new XMI12Reader();
		modele = reader.read(doc);
		
		for(UmlEnum oEnum : modele.getDictionnary().getAllEnumerations()) {
			if ("MyEnum".equals(oEnum.getName())) {
				enum1 = oEnum;
			}
			if ("MyEnum2".equals(oEnum.getName())) {
				enum2 = oEnum;
			}
		}
	}
	
	/**
	 * Test l'existance de l'asso 
	 */
	@Test
	public void testEnum() {
		Assert.assertNotNull(enum1);
		Assert.assertNotNull(enum2);
	}
	
	
	/**
	 * Test le nombre de stéréotype
	 */
	@Test
	public void testEnumStereotype() {
		Assert.assertEquals(1, enum1.getStereotypes().size());
		Assert.assertEquals(0, enum2.getStereotypes().size());
	}
}
