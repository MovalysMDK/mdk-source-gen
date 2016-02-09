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

import com.a2a.adjava.uml.UmlAssociation;
import com.a2a.adjava.uml.UmlAssociationEnd.AggregateType;
import com.a2a.adjava.uml.UmlModel;
import com.a2a.adjava.xmi.XMIReader;

/**
 * Test la lecture des associations
 * @author smaitre
 */
@RunWith(JUnit4.class)
public class TestAssociation {

	/** le modèle du fichier */
	private UmlModel modele = null;
	/** association 1 */
	private UmlAssociation asso1 = null;
	/** association 2 */
	private UmlAssociation asso2 = null;
	/** association 3 */
	private UmlAssociation asso3 = null;
	
	
	@Before
	public void loadFileTest() throws Exception {
		File xml = new File("src/test/java/com/a2a/adjava/xmi/v12vpuml/test.xmi");
		SAXReader oReader = new SAXReader();
		Document doc = oReader.read(xml);
		XMIReader reader = new XMI12Reader();
		modele = reader.read(doc);
		
		for(UmlAssociation oAssociation : modele.getDictionnary().getAssociations()) {
			if ("asso1".equals(oAssociation.getName())) {
				asso1 = oAssociation;
			}
			if ("asso2".equals(oAssociation.getName())) {
				asso2 = oAssociation;
			}
			if ("asso3".equals(oAssociation.getName())) {
				asso3 = oAssociation;
			}
		}
	}
	
	/**
	 * Test l'existance de l'asso 
	 */
	@Test
	public void testAsso() {
		Assert.assertNotNull(asso1);
		Assert.assertNotNull(asso2);
		Assert.assertNotNull(asso3);
	}
	
	
	/**
	 * Test le nombre de stéréotype
	 */
	@Test
	public void testAssoStereotype() {
		Assert.assertEquals(2, asso1.getStereotypes().size());
		Assert.assertEquals(0, asso2.getStereotypes().size());
		Assert.assertEquals(1, asso3.getStereotypes().size());
	}
	
	
	/**
	 * Test le type de terminaison
	 */
	@Test
	public void testAssoEnd() {
		Assert.assertEquals(AggregateType.AGGREGATE,asso1.getAssociationEnd1().getAggregateType());
		Assert.assertEquals(AggregateType.NONE,asso1.getAssociationEnd2().getAggregateType());
		Assert.assertEquals(AggregateType.COMPOSITE,asso2.getAssociationEnd1().getAggregateType());
		Assert.assertEquals(AggregateType.NONE,asso2.getAssociationEnd2().getAggregateType());
		Assert.assertEquals(AggregateType.NONE,asso3.getAssociationEnd1().getAggregateType());
		Assert.assertEquals(AggregateType.NONE,asso3.getAssociationEnd2().getAggregateType());
	}
}
