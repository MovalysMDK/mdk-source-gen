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

import com.a2a.adjava.AdjavaException;
import com.a2a.adjava.uml.UmlAttribute;
import com.a2a.adjava.uml.UmlClass;
import com.a2a.adjava.uml.UmlModel;
import com.a2a.adjava.xmi.XMIReader;

/**
 * Test la lecture des attributs
 * @author smaitre
 */
@RunWith(JUnit4.class)
public class TestAttributes {

	/** readonly constante */
	private static String READ_ONLY = "_RO";
	/** nom de la classe de tests pour les attributs */
	private static String CLASS_TESTATTRIBUTES = "TestAttributes";
	/** nom des attributs */
	/** readonly = false, derived = false */
	private static String ATTRIBUT_1 = "attribute1";
	/** readonly = false, derived = true */
	private static String ATTRIBUT_2 = "attribute2";
	/** readonly = true, derived = false */
	private static String ATTRIBUT_3 = "attribute3";
	/** readonly = true, derived = true */
	private static String ATTRIBUT_4 = "attribute4";
	
	/** le modèle du fichier */
	private UmlModel modele = null;
	/** classes TestAttributes */
	private UmlClass testAttributes = null;
	/** attribut1 */
	private UmlAttribute attribut1 = null;
	/** attribut2 */
	private UmlAttribute attribut2 = null;
	/** attribut3 */
	private UmlAttribute attribut3 = null;
	/** attribut4 */
	private UmlAttribute attribut4 = null;
	
	
	@Before
	public void loadFileTest() throws Exception {
		File xml = new File("src/test/java/com/a2a/adjava/xmi/v12vpuml/test.xmi");
		SAXReader oReader = new SAXReader();
		Document doc = oReader.read(xml);
		XMIReader reader = new XMI12Reader();
		modele = reader.read(doc);
		// les tests portent sur la classe TestAttributes
		for(UmlClass oClass : modele.getDictionnary().getAllClasses()) {
			if (oClass.getName().equals(CLASS_TESTATTRIBUTES)) {
				testAttributes = oClass;
			}
		}
		if (testAttributes == null) {
			throw new AdjavaException("Impossible de lancer le test, la classe TestAttributes n'est pas disponible dans le fichier xmi");
		}
		for(UmlAttribute oAttribut : testAttributes.getAttributes()) {
			if (ATTRIBUT_1.equals(oAttribut.getName())) {
				attribut1 = oAttribut;
			}
			else if (ATTRIBUT_2.equals(oAttribut.getName())) {
				attribut2 = oAttribut;
			}
			else if (ATTRIBUT_3.equals(oAttribut.getName())) {
				attribut3 = oAttribut;
			}
			else if (ATTRIBUT_4.equals(oAttribut.getName())) {
				attribut4 = oAttribut;
			}
		}
	}
	
	/**
	 * Test la lecture du paramètre readonly sur les attributs
	 */
	@Test
	public void testReadOnlyAttributeParameter() {
		Assert.assertFalse(attribut1.getInitialValue().contains(READ_ONLY));
		Assert.assertFalse(attribut2.getInitialValue().contains(READ_ONLY));
		Assert.assertTrue(attribut3.getInitialValue().contains(READ_ONLY));
		Assert.assertTrue(attribut4.getInitialValue().contains(READ_ONLY));
	}
	
	/**
	 * Test la lecture du paramètre derived sur les attributs
	 */
	@Test
	public void testDerivedAttributeParameter() {
		Assert.assertFalse(attribut1.isDerived());
		Assert.assertTrue(attribut2.isDerived());
		Assert.assertFalse(attribut3.isDerived());
		Assert.assertTrue(attribut4.isDerived());
	}
}
