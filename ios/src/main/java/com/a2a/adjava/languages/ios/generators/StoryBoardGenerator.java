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
package com.a2a.adjava.languages.ios.generators;

import java.io.File;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import com.a2a.adjava.codeformatter.GeneratedFile;
import com.a2a.adjava.generator.codeformatters.XmlFormatOptions;
import com.a2a.adjava.generator.core.XslTemplate;
import com.a2a.adjava.generator.core.xmlmerge.AbstractXmlMergeGenerator;
import com.a2a.adjava.generator.core.xmlmerge.xa.configuration.XaConfFile;
import com.a2a.adjava.generators.DomainGeneratorContext;
import com.a2a.adjava.languages.ios.xmodele.MIOSDictionnary;
import com.a2a.adjava.languages.ios.xmodele.MIOSDomain;
import com.a2a.adjava.languages.ios.xmodele.MIOSModeleFactory;
import com.a2a.adjava.languages.ios.xmodele.MIOSStoryBoard;
import com.a2a.adjava.utils.Chrono;
import com.a2a.adjava.utils.JaxbUtils;
import com.a2a.adjava.utils.StrUtils;
import com.a2a.adjava.xmodele.XProject;

/**
 * Story Board generator
 * @author lmichenaud
 *
 */
public class StoryBoardGenerator extends AbstractXmlMergeGenerator<MIOSDomain<MIOSDictionnary, MIOSModeleFactory>> {

	/**
	 * Storyboard extension
	 */
	public static final String STORYBOARD_EXTENSION = "storyboard";
	
	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(StoryBoardGenerator.class);

	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.generators.ResourceGenerator#genere(com.a2a.adjava.xmodele.XProject, com.a2a.adjava.generators.DomainGeneratorContext)
	 */
	@Override
	public void genere(
			XProject<MIOSDomain<MIOSDictionnary, MIOSModeleFactory>> p_oMProject,
			DomainGeneratorContext p_oGeneratorContext) throws Exception {
		
		log.debug("> StoryBoardGenerator.genere");
		Chrono oChrono = new Chrono(true);
		
		for (MIOSStoryBoard oStoryBoard : p_oMProject.getDomain().getDictionnary().getAllIOSStoryBoards()) {
		
			Document xDoc = JaxbUtils.marshalToDocument(oStoryBoard);
			
			String sStoryBoardFileName = StringUtils.join(oStoryBoard.getName(), StrUtils.DOT_S, STORYBOARD_EXTENSION);
			File oStoryBoardFile = new File("resources/storyboard", sStoryBoardFileName);
			log.debug("  generate: {}", oStoryBoardFile.getAbsolutePath());			
			
			XmlFormatOptions oFormatOptions = new XmlFormatOptions();
			oFormatOptions.setUseDom4j(true);
			oFormatOptions.setStandalone(false);
			oFormatOptions.setNewLineAfterDeclaration(false);
			GeneratedFile<XmlFormatOptions> oGenFile = new GeneratedFile<XmlFormatOptions>(oStoryBoardFile, oFormatOptions);
			
			this.doXmlMergeGeneration(xDoc, XslTemplate.STORYBOARD, oGenFile, p_oMProject, p_oGeneratorContext,XaConfFile.IOS_STORYBOARD);
		}
		
		log.debug("< StoryBoardGenerator.genere: {}", oChrono.stopAndDisplay());
	}
}
