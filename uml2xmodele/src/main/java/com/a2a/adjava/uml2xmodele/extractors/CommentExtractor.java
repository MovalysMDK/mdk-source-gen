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
package com.a2a.adjava.uml2xmodele.extractors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.messages.MessageHandler;
import com.a2a.adjava.uml.UmlAssociation;
import com.a2a.adjava.uml.UmlAssociationEnd;
import com.a2a.adjava.uml.UmlClass;
import com.a2a.adjava.uml.UmlComment;
import com.a2a.adjava.uml.UmlDictionary;
import com.a2a.adjava.uml.UmlEnum;
import com.a2a.adjava.uml.UmlModel;
import com.a2a.adjava.uml.UmlUsage;
import com.a2a.adjava.uml2xmodele.ui.screens.CUDActionProcessor;
import com.a2a.adjava.uml2xmodele.ui.screens.ScreenAggregationPanelProcessor;
import com.a2a.adjava.uml2xmodele.ui.screens.ScreenContext;
import com.a2a.adjava.uml2xmodele.ui.screens.ScreenDependencyProcessor;
import com.a2a.adjava.xmodele.IDomain;
import com.a2a.adjava.xmodele.IModelDictionary;
import com.a2a.adjava.xmodele.IModelFactory;
import com.a2a.adjava.xmodele.MComment;
import com.a2a.adjava.xmodele.MEnumeration;
import com.a2a.adjava.xmodele.MPackage;
import com.a2a.adjava.xmodele.MScreen;

/**
 * <p>
 * Classe de type Extractor permettant de r��cup��rer dans le flux xml, les
 * donn��es correspondant aux diff��rents Comment.
 * </p>
 * 
 * <p>
 * Copyright (c) 2015
 * </p>
 * 
 * @author lbrunelliere
 */
public class CommentExtractor extends AbstractExtractor<IDomain<IModelDictionary,IModelFactory>> {

	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(CommentExtractor.class);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initialize(Element p_xConfig) throws Exception {
	}

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void extract(UmlModel p_oModele) throws Exception {

 		UmlDictionary oUmlDict = p_oModele.getDictionnary();
				
		// Extract screen from uml class
		for (UmlClass oUmlClass : oUmlDict.getAllClasses()) {
			// Create MComment
			if (oUmlClass.getComment() != null) {
				MComment oComment = this.createComment(oUmlClass);

				this.getDomain().getDictionnary().registerComment(oComment);
			}
		}
	}

	/**
	 * @param p_oScreenUmlClass
	 * @return
	 * @throws Exception
	 */
	protected MComment createComment( UmlClass p_oUmlClass ) throws Exception {

		log.debug("CommentExtractor.createComment : {}", p_oUmlClass.getName());
		
		// cr��ation du Comment
		MComment r_oComment = this.getDomain().getXModeleFactory().createComment(
				p_oUmlClass.getName(), p_oUmlClass.getName());			
		
		// Name Screen
		r_oComment.setNameScreen(p_oUmlClass.getName());
		
		// Comment
		r_oComment.setComment(p_oUmlClass.getComment().getDocumentation());
		
		return r_oComment ;
	}

}