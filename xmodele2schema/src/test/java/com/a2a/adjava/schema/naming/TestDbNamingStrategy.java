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
package com.a2a.adjava.schema.naming;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Test Unit for db naming strategy
 * @author lmichenaud
 *
 */
@RunWith(JUnit4.class)
public class TestDbNamingStrategy {

	/**
	 * Db Naming Strategy
	 */
	private DbNamingStrategy dbNamingStrategy ;
	
	/**
	 * Prepare
	 */
	@Before
	public void setUp() {
		Map<String,String> mapOptions = new HashMap<String,String>();
		mapOptions.put("tablePrefix", "T_");
		mapOptions.put("sequencePrefix", "SEQ_");
		mapOptions.put("indexPrefix", "I_");
		mapOptions.put("fkPrefix", "FK_");
		mapOptions.put("uniquePrefix", "U_");
		mapOptions.put("pkPrefix", "PK_");
		mapOptions.put("constraintNameMaxLength", "25");
		mapOptions.put("identifiantMaxLength", "30");
		mapOptions.put("tableNameMaxLength", "30");

		this.dbNamingStrategy = new DbNamingStrategyImpl();
		this.dbNamingStrategy.setOptions(mapOptions);
	}
	
	/**
	 * Test l'indentation
	 * @throws Exception failure
	 */
	@Test
	public void testTableNaming() throws Exception {
		String sTableName = this.dbNamingStrategy.getTableName("Agency");
		Assert.assertEquals(sTableName, "T_AGENCY");
	}
}
