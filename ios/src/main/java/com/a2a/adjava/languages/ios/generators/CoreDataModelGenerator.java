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

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.codeformatter.GeneratedFile;
import com.a2a.adjava.generator.codeformatters.XmlFormatOptions;
import com.a2a.adjava.generator.core.XslTemplate;
import com.a2a.adjava.generator.core.xmlmerge.AbstractXmlMergeGenerator;
import com.a2a.adjava.generator.core.xmlmerge.xa.configuration.XaConfFile;
import com.a2a.adjava.generators.DomainGeneratorContext;
import com.a2a.adjava.languages.ios.xmodele.MIOSDictionnary;
import com.a2a.adjava.languages.ios.xmodele.MIOSDomain;
import com.a2a.adjava.languages.ios.xmodele.MIOSEntity;
import com.a2a.adjava.languages.ios.xmodele.MIOSModeleFactory;
import com.a2a.adjava.utils.Chrono;
import com.a2a.adjava.xmodele.MEntityImpl;
import com.a2a.adjava.xmodele.XProject;

/**
 * Generate xml file for core data model
 * file generated: ./project/project/Model.xcdatamodeld/Model.xcdatamodel/content
 * @author lmichenaud
 *
 */
public class CoreDataModelGenerator extends AbstractXmlMergeGenerator<MIOSDomain<MIOSDictionnary, MIOSModeleFactory>> {

	/**
	 * Contents file
	 */
	public static final String CONTENTS_FILE = "contents";
	
	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(CoreDataModelGenerator.class);
	
	
	/**
	 * CDATAModel directory
	 */
	private static final String XCDATAMODELD_DIR = "Model.xcdatamodeld";

	/**
	 * CDATAModel directory
	 */
	private static final String XCDATAMODEL_DIR = "Model.xcdatamodel";
	
	/**
	 * Core data model xml node
	 */
	private static final String COREDATAMODEL_NODE = "coredatamodel";
	
	/**
	 * Core data model entity basic y positionning
	 */
	private static final int BASIC_ENTITY_Y_POSITION = -211;
	
	/**
	 * Core data model entity basic y spacing
	 */
	private static final int BASIC_ENTITY_X_SPACING = 250;
			
	/**
	 * Core data model entity basic y spacing
	 */
	private static final int BASIC_ENTITY_Y_SPACING = 100;
	
	/**
	 * Core data model entity basic width
	 */
	private static final int BASIC_ENTITY_WIDTH = 128;
	
	/**
	 * Core data model entity basic height
	 */
	private static final int ENTITY_BASE_HEIGHT = 45; 
	
	/**
	 * Core data model entity basic identifier height
	 */
	private static final int ENTITY_IDENTIFIER_HEIGHT = 15;
	
	/**
	 * Core data model entity basic attribute height
	 */
	private static final int ENTITY_ATTRIBUTE_HEIGHT = 15;
	
	/**
	 * Core data model entity basic association height
	 */
	private static final int ENTITY_ASSOCIATION_HEIGHT = 15;
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.generators.ResourceGenerator#genere(com.a2a.adjava.xmodele.XProject, com.a2a.adjava.generators.DomainGeneratorContext)
	 */
	@Override
	public void genere(
			XProject<MIOSDomain<MIOSDictionnary, MIOSModeleFactory>> p_oMProject,
			DomainGeneratorContext p_oGeneratorContext) throws Exception {
		
		log.debug("> CoreDataModelGenerator.genere");
		Chrono oChrono = new Chrono(true);
		
		Element xRoot = DocumentHelper.createElement(COREDATAMODEL_NODE);
		Document xDoc = DocumentHelper.createDocument(xRoot);
		
		this.computeDiagram(p_oMProject);
		
		for (MEntityImpl oMEntityImpl : p_oMProject.getDomain().getDictionnary().getAllEntities()) {
			xRoot.add(oMEntityImpl.toXml());
		}
		
		File oModelDir = new File( XCDATAMODELD_DIR, XCDATAMODEL_DIR);
		File oContentFile = new File(oModelDir, CONTENTS_FILE);
		
		XmlFormatOptions oFormatOptions = new XmlFormatOptions();
		oFormatOptions.setUseDom4j(true);
		oFormatOptions.setStandalone(true);
		oFormatOptions.setNewLineAfterDeclaration(false);
		GeneratedFile<XmlFormatOptions> oGenFile = new GeneratedFile<XmlFormatOptions>(oContentFile, oFormatOptions);
		
		this.doXmlMergeGeneration(xDoc, XslTemplate.CORE_DATA_MODEL, oGenFile, p_oMProject, p_oGeneratorContext,XaConfFile.IOS_CORE_DATA);
		
		log.debug("< CoreDataModelGenerator.genere: {}", oChrono.stopAndDisplay());
	}

	/**
	 * Compute entity diagram
	 * @param p_oMProject project
	 */
	private void computeDiagram(
			XProject<MIOSDomain<MIOSDictionnary, MIOSModeleFactory>> p_oMProject) {
		int iCurrentPosX = 0 ;
		int iCurrentPosY = BASIC_ENTITY_Y_POSITION ;
		
		int lClassPerRow = (int) Math.ceil(Math.sqrt(p_oMProject.getDomain().getDictionnary().getAllEntities().size()));

		int iClassRank = 0 ;
		int iMaxHeight = 0 ;
		for (MEntityImpl oMEntityImpl : p_oMProject.getDomain().getDictionnary().getAllEntities()) {
			if ( !oMEntityImpl.isTransient()) {
				MIOSEntity oIOSEntity = (MIOSEntity) oMEntityImpl;
				oIOSEntity.setPosX(iCurrentPosX);
				oIOSEntity.setPosY(iCurrentPosY);
				this.computeEntityHeight(oIOSEntity);
				oIOSEntity.setWidth(BASIC_ENTITY_WIDTH);
				iCurrentPosX -= BASIC_ENTITY_X_SPACING ;
				
				if ( oIOSEntity.getHeight() > iMaxHeight) {
					iMaxHeight = oIOSEntity.getHeight();
				}
				
				iClassRank++;
				
				if ( iClassRank % lClassPerRow == 0 ) {
					iCurrentPosY += iMaxHeight + BASIC_ENTITY_Y_SPACING ;
					iMaxHeight = 0 ;
					iCurrentPosX = 0 ;
				}
			}
		}
	}

	/**
	 * Compute entity height
	 * @param p_oIOSEntity entity
	 */
	private void computeEntityHeight(MIOSEntity p_oIOSEntity) {
		p_oIOSEntity.setHeight( 
				ENTITY_BASE_HEIGHT 
				+ (p_oIOSEntity.getIdentifier().getElems().size() * ENTITY_IDENTIFIER_HEIGHT) 
				+ (p_oIOSEntity.getAttributes().size() * ENTITY_ATTRIBUTE_HEIGHT ) 
				+ (p_oIOSEntity.getAssociations().size() * ENTITY_ASSOCIATION_HEIGHT ));
	}
}
