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

import com.a2a.adjava.AdjavaException;
import com.a2a.adjava.codeformatter.GeneratedFile;
import com.a2a.adjava.generator.codeformatters.XmlFormatOptions;
import com.a2a.adjava.generator.core.XslTemplate;
import com.a2a.adjava.generator.core.xmlmerge.AbstractXmlMergeGenerator;
import com.a2a.adjava.generator.core.xmlmerge.xa.configuration.XaConfFile;
import com.a2a.adjava.generator.core.incremental.NonGeneratedBlocExtractor;
import com.a2a.adjava.generators.DomainGeneratorContext;
import com.a2a.adjava.languages.ios.xmodele.MIOSDictionnary;
import com.a2a.adjava.languages.ios.xmodele.MIOSDomain;
import com.a2a.adjava.languages.ios.xmodele.MIOSExpandableListXibContainer;
import com.a2a.adjava.languages.ios.xmodele.MIOSModeleFactory;
import com.a2a.adjava.languages.ios.xmodele.MIOSXibComboContainer;
import com.a2a.adjava.languages.ios.xmodele.MIOSXibContainer;
import com.a2a.adjava.languages.ios.xmodele.MIOSXibFixedListContainer;
import com.a2a.adjava.utils.Chrono;
import com.a2a.adjava.utils.JaxbUtils;
import com.a2a.adjava.utils.StrUtils;
import com.a2a.adjava.xmodele.XProject;

/**
 * Generator of the XIB used in the fixed list to design the cell
 * 
 * <p> Copyright (c) 2013 </p> 
 * <p> Company: Adeuza </p>
 * 
 * @author spacreau
 */
public class XibItemGenerator extends AbstractXmlMergeGenerator<MIOSDomain<MIOSDictionnary, MIOSModeleFactory>> {

	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(XibItemGenerator.class);
	
	/**
	 * xib files extension
	 */
	private static final String XIB_EXTENSION = "xib";

	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.a2a.adjava.generators.ResourceGenerator#genere(com.a2a.adjava.xmodele.XProject,
	 *      com.a2a.adjava.generators.DomainGeneratorContext)
	 */
	@Override
	public void genere(XProject<MIOSDomain<MIOSDictionnary, MIOSModeleFactory>> p_oProject, DomainGeneratorContext p_oContext)
			throws Exception {
		log.debug("> XibItemGenerator.genere");
		Chrono oChrono = new Chrono(true);
		NonGeneratedBlocExtractor oNonGeneratedBlocExtractor = new NonGeneratedBlocExtractor();
		for (MIOSXibContainer oXibContainer : p_oProject.getDomain().getDictionnary().getAllIOSXibContainers()) {
			if (oXibContainer instanceof MIOSXibFixedListContainer)	
			{
				this.createXib(oXibContainer,XslTemplate.XIB_FIXED_LIST_ITEM, oNonGeneratedBlocExtractor, p_oProject, p_oContext);
			}
			else if (oXibContainer instanceof MIOSXibComboContainer)
			{
				MIOSXibComboContainer oXibComboContainer = (MIOSXibComboContainer) oXibContainer;
				if(oXibComboContainer.isSelectedItem())
				{
					this.createXib(oXibComboContainer,XslTemplate.XIB_PICKER_LIST_SELECTED_ITEM, oNonGeneratedBlocExtractor, p_oProject, p_oContext);
				}else{
					this.createXib(oXibComboContainer,XslTemplate.XIB_PICKER_LIST_ITEM, oNonGeneratedBlocExtractor, p_oProject, p_oContext);
				}
				
			}
			else if(oXibContainer instanceof MIOSExpandableListXibContainer)	
			{
				this.createXib(oXibContainer,XslTemplate.XIB_EXPANDABLE_LIST_ITEM, oNonGeneratedBlocExtractor, p_oProject, p_oContext);
			}
			else
			{
				//Pour l'instant, par défault, les xib sont générés comme ceux d'une fixed list
				throw new AdjavaException("Cannot generate this kind of XIB");
			}
			log.debug("< XibItemGenerator.genere: {}", oChrono.stopAndDisplay());
		}
	}

	/**
	 * Generate XIB of views
	 * 
	 * @param p_oXibContainer xib container
	 * @param p_oXslAdress xsl template
	 * @param p_oNonGeneratedBlocExtractor non-generated bloc extractor
	 * @param p_oProject project
	 * @param p_oContext context
	 * @throws Exception exception
	 */
	private void createXib(MIOSXibContainer p_oXibContainer, XslTemplate p_oXslAdress, NonGeneratedBlocExtractor p_oNonGeneratedBlocExtractor,
			XProject<MIOSDomain<MIOSDictionnary, MIOSModeleFactory>> p_oProject, DomainGeneratorContext p_oContext) throws Exception {

		Document xXibContainerDoc = this.createDocument(p_oXibContainer, p_oProject);

		String sXibFileName = StringUtils.join(p_oXibContainer.getName(), StrUtils.DOT_S, XIB_EXTENSION);
		File oXibFile = new File("resources/xib", sXibFileName);
		log.debug("  generate: {}", oXibFile.getAbsolutePath());

		XmlFormatOptions oFormatOptions = new XmlFormatOptions();
		oFormatOptions.setUseDom4j(true);
		oFormatOptions.setStandalone(false);
		oFormatOptions.setNewLineAfterDeclaration(false);
		GeneratedFile<XmlFormatOptions> oGenFile = new GeneratedFile<XmlFormatOptions>(oXibFile, oFormatOptions);

		this.doXmlMergeGeneration(xXibContainerDoc, p_oXslAdress, oGenFile, p_oProject, p_oContext,XaConfFile.IOS_XIB);
	}

	/**
	 * Create document
	 * 
	 * @param p_oMViewModel viewmodel
	 * @param p_oProject project
	 * @return xml of viewmodel implementation
	 * @throws Exception  in case of conversion problem of the xib container
	 */
	protected Document createDocument(MIOSXibContainer p_oMViewModel, XProject<MIOSDomain<MIOSDictionnary, 
			MIOSModeleFactory>> p_oProject) throws Exception {
		Document oXMlDoc = JaxbUtils.marshalToDocument(p_oMViewModel);
		//oXMlDoc.addElement("master-package").setText(p_oProject.getDomain().getRootPackage());
		return oXMlDoc;
	}

}
