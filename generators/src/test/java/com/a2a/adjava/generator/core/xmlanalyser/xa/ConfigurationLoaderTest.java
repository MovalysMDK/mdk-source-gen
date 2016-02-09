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
package com.a2a.adjava.generator.core.xmlanalyser.xa;

import java.nio.file.Paths;

import junit.framework.Assert;

import org.junit.Test;

import com.a2a.adjava.AdjavaException;
import com.a2a.adjava.generator.core.xmlmerge.xa.configuration.loader.ConfigurationLoader;
import com.a2a.adjava.generator.core.xmlmerge.xa.configuration.model.XAConfiguration;
import com.a2a.adjava.generator.core.xmlmerge.xa.configuration.model.XAConfigurationNode;



public class ConfigurationLoaderTest {
	
	@Test
	public void testConfigurationLoader() throws AdjavaException {
		XAConfiguration.getInstance().clear();
		ConfigurationLoader oLoaderTest = new ConfigurationLoader(Paths.get(ConstantPaths.ROOT_CONF));
		oLoaderTest.load();
		Assert.assertFalse(oLoaderTest.hasError());
		ConfigurationLoader oLoaderSrc = new ConfigurationLoader(Paths.get(ConstantPaths.ROOT_CONF_MAIN));
		oLoaderSrc.load();
		Assert.assertFalse(oLoaderSrc.hasError());
		
		XAConfigurationNode oNodeF = XAConfiguration.getInstance().getNode("conf.xml","testF");
		Assert.assertNotNull(oNodeF);
		Assert.assertEquals("idF", oNodeF.getIdAttribute()[0]);
	}

}
