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
import com.a2a.adjava.uml.UmlClass;
import com.a2a.adjava.uml.UmlModel;
import com.a2a.adjava.uml.UmlUsage;
import com.a2a.adjava.xmi.XMIReader;

/**
 * Test la lecture des attributs
 * @author smaitre
 */
@RunWith(JUnit4.class)
public class TestUses {

	/** nom de la classe de tests pour les attributs */
	private static String CLASS_TESTATTRIBUTES = "TestAttributes";
	private static String CLASS_TESTA = "TestA";
	private static String CLASS_TESTB = "TestB";

	/** le modèle du fichier */
	private UmlModel modele = null;
	/** classes TestAttributes */
	private UmlClass testAttributes = null;
	/** test A */
	private UmlClass testA = null;
	/** test B */
	private UmlClass testB = null;
	/** usage 1 call instanciate */
	private UmlUsage usage1 = null;
	/** usage 2  sans nom */
	private UmlUsage usage2 = null;
	/** usage 3  use TestAttributes-TestB*/
	private UmlUsage usage3 = null;
	/** usage 4 use non name from test A*/
	private UmlUsage usage4 = null;
	/** usage 5 use TestB-TestAttributes */
	private UmlUsage usage5 = null;
	
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
				for(UmlUsage oUsage : oClass.getUsages()) {
					if ("call_instanciate".equals(oUsage.getName())) {
						usage1 = oUsage;
					}
					else if (oUsage.getName() == null) {
						usage2 = oUsage;
					}
					else if ("use TestAttributes-TestB".equals(oUsage.getName())) {
						usage3 = oUsage;
					}
				}
			}
			else if (oClass.getName().equals(CLASS_TESTA)) {
				testA = oClass;
				for(UmlUsage oUsage : oClass.getUsages()) {
					if (oUsage.getName()==null) {
						usage4 = oUsage;
					}
				}
			}
			else if (oClass.getName().equals(CLASS_TESTB)) {
				testB = oClass;
				for(UmlUsage oUsage : oClass.getUsages()) {
					if ("use TestB-TestAttributes".equals(oUsage.getName())) {
						usage5 = oUsage;
					}
				}
			}
		}
		if (testAttributes == null) {
			throw new AdjavaException("Impossible de lancer le test, la classe TestAttributes n'est pas disponible dans le fichier xmi");
		}
		if (testA == null) {
			throw new AdjavaException("Impossible de lancer le test, la classe TestA n'est pas disponible dans le fichier xmi");
		}
		if (testB == null) {
			throw new AdjavaException("Impossible de lancer le test, la classe TestB n'est pas disponible dans le fichier xmi");
		}
	}
	
	/**
	 * Test le nombre de use
	 */
	@Test
	public void testNumberOfUsage() {
		Assert.assertEquals(testAttributes.getUsages().size(), 3);
		Assert.assertEquals(testA.getUsages().size(), 1);
		Assert.assertEquals(testB.getUsages().size(), 1);
		Assert.assertEquals(5, modele.getDictionnary().getUsages().size());
	}
	
	/**
	 * Test le nom des usages
	 */
	@Test
	public void testUsageName() {
		Assert.assertNotNull(usage1);
		Assert.assertNotNull(usage2);
		Assert.assertNotNull(usage3);
		Assert.assertNotNull(usage4);
		Assert.assertNotNull(usage5);
	}
	
	/**
	 * Test le client
	 */
	@Test
	public void testUsageClient() {
		Assert.assertEquals(usage1.getClient(), testAttributes);
		Assert.assertEquals(usage2.getClient(), testAttributes);
		Assert.assertEquals(usage3.getClient(), testAttributes);
		Assert.assertEquals(usage4.getClient(), testA);
		Assert.assertEquals(usage5.getClient(), testB);
	}
	
	/**
	 * Test le fournisseur
	 */
	@Test
	public void testUsageSupplier() {
		Assert.assertEquals(usage1.getSupplier(), testA);
		Assert.assertEquals(usage2.getSupplier(), testA);
		Assert.assertEquals(usage3.getSupplier(), testB);
		Assert.assertEquals(usage4.getSupplier(), testB);
		Assert.assertEquals(usage5.getSupplier(), testAttributes);
	}
	
	/**
	 * Test le nombre de stéréotype
	 */
	@Test
	public void testUsageStereotype() {
		Assert.assertEquals(2, usage1.getStereotypes().size());
		Assert.assertEquals(0, usage2.getStereotypes().size());
		Assert.assertEquals(0, usage3.getStereotypes().size());
		Assert.assertEquals(2, usage4.getStereotypes().size());
		Assert.assertEquals(1, usage5.getStereotypes().size());
	}
}
