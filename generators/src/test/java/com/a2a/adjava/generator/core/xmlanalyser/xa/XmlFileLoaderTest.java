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

import static com.a2a.adjava.generator.core.xmlanalyser.xa.ConstantPaths.ROOT_DIV;

import java.nio.file.Paths;
import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.a2a.adjava.AdjavaException;
import com.a2a.adjava.generator.core.xmlmerge.xa.configuration.loader.ConfigurationLoader;
import com.a2a.adjava.generator.core.xmlmerge.xa.configuration.model.XAConfiguration;
import com.a2a.adjava.generator.core.xmlmerge.xa.xmlfile.analyse.Change;
import com.a2a.adjava.generator.core.xmlmerge.xa.xmlfile.analyse.ChangesResult;
import com.a2a.adjava.generator.core.xmlmerge.xa.xmlfile.loader.XmlFileLoader;
import com.a2a.adjava.generator.core.xmlmerge.xa.xmlfile.model.XAFile;
import com.a2a.adjava.generator.core.xmlmerge.xa.xmlfile.model.XAFiles;


public class XmlFileLoaderTest {
	@Before
	public void load() throws AdjavaException {
		XAConfiguration.getInstance().clear();
		ConfigurationLoader oLoaderTest = new ConfigurationLoader(Paths.get(ConstantPaths.ROOT_CONF));
		oLoaderTest.load();
		Assert.assertFalse(oLoaderTest.hasError());
		ConfigurationLoader oLoaderSrc = new ConfigurationLoader(Paths.get(ConstantPaths.ROOT_CONF_MAIN));
		oLoaderSrc.load();
		Assert.assertFalse(oLoaderSrc.hasError());
		
		XmlFileLoader oXmlLoaderOldGen = new XmlFileLoader(Paths.get(ROOT_DIV+"/oldgen"), XmlFileLoader.Type.OLDGEN,"xml","conf.xml");
		XmlFileLoader oXmlLoaderMod = new XmlFileLoader(Paths.get(ROOT_DIV+"/mod"),  XmlFileLoader.Type.MOD,"xml","conf.xml");
		XmlFileLoader oXmlLoaderNewGen = new XmlFileLoader(Paths.get(ROOT_DIV+"/newgen"),  XmlFileLoader.Type.NEWGEN,"xml","conf.xml");
		oXmlLoaderOldGen.load();
		Assert.assertFalse(oXmlLoaderOldGen.hasError());
		oXmlLoaderNewGen.load();
		Assert.assertFalse(oXmlLoaderNewGen.hasError());
		oXmlLoaderMod.load();
		Assert.assertFalse(oXmlLoaderMod.hasError());
	}
	
	@Test
	public void testConfigurationLoader() throws AdjavaException {
		XAFile oOldGen1 = XAFiles.getInstance().getOldGenFile(Paths.get(ROOT_DIV+"/oldgen","gen1.xml"));
		Assert.assertNotNull(oOldGen1);
		Assert.assertEquals(11, oOldGen1.size());
		XAFile oModGen1 = XAFiles.getInstance().getModFile(Paths.get(ROOT_DIV+"/mod","gen1.xml"));
		Assert.assertNotNull(oModGen1);
		Assert.assertEquals(13, oModGen1.size());
		XAFile oNewGen1 = XAFiles.getInstance().getNewGenFile(Paths.get(ROOT_DIV+"/newgen","gen1.xml"));
		Assert.assertNotNull(oNewGen1);
		Assert.assertEquals(12, oNewGen1.size());
		
		
		ChangesResult oResultModAndOld = oModGen1.findChanges(oOldGen1);
		
		List<Change> add = oResultModAndOld.getAddChanges();
		Assert.assertNotNull(add);
		Assert.assertEquals(3, add.size());	
	}
	
	@After
	public void unload() {
		XAConfiguration.getInstance().clear();
		XAFiles.getInstance().clear();
	}

}
