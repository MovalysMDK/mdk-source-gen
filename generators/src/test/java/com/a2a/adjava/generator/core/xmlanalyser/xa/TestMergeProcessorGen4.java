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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

import junit.framework.Assert;

import org.apache.commons.io.Charsets;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.a2a.adjava.AdjavaException;
import com.a2a.adjava.generator.core.xmlmerge.xa.configuration.loader.ConfigurationLoader;
import com.a2a.adjava.generator.core.xmlmerge.xa.configuration.model.XAConfiguration;
import com.a2a.adjava.generator.core.xmlmerge.xa.xmlfile.loader.XmlFileLoader;
import com.a2a.adjava.generator.core.xmlmerge.xa.xmlfile.model.XAFiles;
import com.a2a.adjava.generator.core.xmlmerge.xa.xmlfile.process.MergeProcessor;
import com.a2a.adjava.utils.FileTypeUtils;

public class TestMergeProcessorGen4 {
	
	@Before
	public void load() throws AdjavaException {
		XAConfiguration.getInstance().clear();
		ConfigurationLoader oLoaderTest = new ConfigurationLoader(Paths.get(ConstantPaths.ROOT_CONF));
		oLoaderTest.load();
		Assert.assertFalse(oLoaderTest.hasError());
		ConfigurationLoader oLoaderSrc = new ConfigurationLoader(Paths.get(ConstantPaths.ROOT_CONF_MAIN));
		oLoaderSrc.load();
		Assert.assertFalse(oLoaderSrc.hasError());
		
		XmlFileLoader oXmlLoaderOldGen = new XmlFileLoader(Paths.get(ROOT_DIV+"/oldgen"), XmlFileLoader.Type.OLDGEN,"xml","conf-plist.xml");
		XmlFileLoader oXmlLoaderMod = new XmlFileLoader(Paths.get(ROOT_DIV+"/mod"),  XmlFileLoader.Type.MOD,"xml","conf-plist.xml");
		XmlFileLoader oXmlLoaderNewGen = new XmlFileLoader(Paths.get(ROOT_DIV+"/newgen"),  XmlFileLoader.Type.NEWGEN,"xml","conf-plist.xml");
		oXmlLoaderOldGen.load();
		Assert.assertFalse(oXmlLoaderOldGen.hasError());
		oXmlLoaderNewGen.load();
		Assert.assertFalse(oXmlLoaderNewGen.hasError());
		oXmlLoaderMod.load();
		Assert.assertFalse(oXmlLoaderMod.hasError());
	}
	
	public void writeXml( Source p_oSource, File oFile) throws Exception {
		
		Files.createDirectories(oFile.getParentFile().toPath());
		
	    Transformer oTransformer = TransformerFactory.newInstance().newTransformer();
	    oTransformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
	    oTransformer.setOutputProperty(OutputKeys.METHOD, FileTypeUtils.XML);
	    oTransformer.setOutputProperty(OutputKeys.ENCODING, Charsets.UTF_8.name());
	    oTransformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
	    oTransformer.setOutputProperty(OutputKeys.INDENT, "yes");
	    
	    
	    OutputStreamWriter oOutputStreamWriter = null;
		try {
			oOutputStreamWriter = new OutputStreamWriter(new FileOutputStream(oFile),"UTF-8");
			StreamResult oStreamResult = new StreamResult(oOutputStreamWriter);
			oTransformer.transform(p_oSource, oStreamResult);
		}  finally {
			if(oOutputStreamWriter != null) {
				oOutputStreamWriter.close();
			}
		}
	}
	
	@Test
	public void testMergeProcessor() {
		File oFileResult = new File(ROOT_DIV+"/newmod/result4.xml");
		File oFile = new File(ROOT_DIV+"/newmod/gen4T.xml");
		if (oFile.exists()) {
			oFile.delete();
		}
		MergeProcessor oMp = new MergeProcessor();
				
		try {
//			File oFileGen4Oldgen = new File(ROOT_DIV+"/oldgen/gen4.xml");
//			File oFileGen4OldgenT = new File(ROOT_DIV+"/oldgen/gen4T.xml");
//			Document oOldgenT = PlistProcessor.fromFlatXmlToGroupedXml(oFileGen4Oldgen, XaConfFile.IOS_PLIST);
//			
//			File oFileGen4Newgen = new File(ROOT_DIV+"/newgen/gen4.xml");
//			File oFileGen4NewgenT = new File(ROOT_DIV+"/newgen/gen4T.xml");
//			Document oNewgenT = PlistProcessor.fromFlatXmlToGroupedXml(oFileGen4Newgen, XaConfFile.IOS_PLIST);
//			
//			File oFileGen4Mod = new File(ROOT_DIV+"/mod/gen4.xml");
//			File oFileGen4ModT = new File(ROOT_DIV+"/mod/gen4T.xml");
//			Document oModT = PlistProcessor.fromFlatXmlToGroupedXml(oFileGen4Mod, XaConfFile.IOS_PLIST);
//			
//			
//			try {
//				writeXml( new DOMSource(oOldgenT), oFileGen4OldgenT);
//				writeXml( new DOMSource(oNewgenT), oFileGen4NewgenT);
//				writeXml( new DOMSource(oModT), oFileGen4ModT);
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			
			oMp.process(Paths.get(ROOT_DIV+"/oldgen","gen4T.xml"), Paths.get(ROOT_DIV+"/newgen","gen4T.xml"), Paths.get(ROOT_DIV+"/mod/gen4T.xml"), Paths.get(ROOT_DIV+"/newmod/gen4T.xml"));

		} catch (AdjavaException e1) {
			e1.printStackTrace();
		}
		Assert.assertTrue(oFile.exists());
		
		String sFileResult = null;
		String sFile = null;
		try {
			FileReader oFrr = new FileReader(oFileResult);
			try {
				StringBuilder oSb = new StringBuilder();
				char c = (char) oFrr.read();
				while(c!=65535) {
					oSb.append(c);
					c = (char) oFrr.read();
				}
				sFileResult = oSb.toString().trim();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			finally {
				oFrr.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			FileReader oFrr = new FileReader(oFile);
			try {
				StringBuilder oSb = new StringBuilder();
				char c = (char) oFrr.read();
				while(c!=65535) {
					oSb.append(c);
					c = (char) oFrr.read();
				}
				sFile = oSb.toString().trim();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			finally {
				oFrr.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		sFileResult = sFileResult.replaceAll("\\n", " ");
		sFileResult = sFileResult.replaceAll(">\\s*<", "><");
		sFile = sFile.replaceAll("\\n", " ");
		sFile = sFile.replaceAll(">\\s*<", "><");
		
		Assert.assertEquals(sFileResult, sFile);
	}
	
	@After
	public void unload() {
		XAConfiguration.getInstance().clear();
		XAFiles.getInstance().clear();
	}
}
