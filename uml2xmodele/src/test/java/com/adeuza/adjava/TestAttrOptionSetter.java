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
package com.adeuza.adjava;

import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.a2a.adjava.datatypes.DataType;
import com.a2a.adjava.uml2xmodele.attrconvert.UmlAttributeOptionParser;

/**
 * <p>Teste les options sur les attributs</p>
 *
 * <p>Copyright (c) 2009
 * <p>Company: Adeuza
 *
 * @author lmichenaud
 *
 */

@RunWith(JUnit4.class)
public class TestAttrOptionSetter {
	
	/**
	 * Teste les options sur les attributs
	 */
	@Test
	public void testOptionSetter() {
		String sModelChaine = "0_R";
		String sDefaultOptions = "_L10";
		
		Map<String,?> mapOptions = UmlAttributeOptionParser.getInstance().parse(sModelChaine, sDefaultOptions, DataType.ALPHANUMERIC);		
		Assert.assertTrue( mapOptions.containsKey("R"));
		Assert.assertTrue( mapOptions.containsKey("L"));
		Assert.assertTrue( mapOptions.containsKey("init"));
		Assert.assertEquals( mapOptions.get("R"), "");
		Assert.assertEquals( mapOptions.get("L"), "10");
		Assert.assertEquals( mapOptions.get("init"), "0");
	}
}
