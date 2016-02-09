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
import java.util.List;

import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.languages.LanguageConfiguration;
import com.a2a.adjava.messages.MessageHandler;
import com.a2a.adjava.types.ITypeDescription;
import com.a2a.adjava.types.IUITypeDescription;
import com.a2a.adjava.uml.UmlClass;
import com.a2a.adjava.uml.UmlDictionary;
import com.a2a.adjava.uml.UmlModel;
import com.a2a.adjava.xmodele.IDomain;
import com.a2a.adjava.xmodele.IModelDictionary;
import com.a2a.adjava.xmodele.IModelFactory;

public class CheckExtractor extends AbstractExtractor<IDomain<IModelDictionary,IModelFactory>> {

	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(ScreenExtractor.class);

	private List<String> forbiddenClassNames;
	
	@Override
	public void initialize(Element p_xConfig) throws Exception {
	}
	
	@Override
	public void extract(UmlModel p_oModele) throws Exception {
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.uml2xmodele.extractors.AbstractExtractor#preValidate(com.a2a.adjava.uml.UmlModel)
	 */
	@Override
	public void preValidate(UmlModel p_oModele) throws Exception {
		UmlDictionary oUmlDict = p_oModele.getDictionnary();
		addDataTypesToForbiddenClassNameList();
		
		for(UmlClass classfound : oUmlDict.getAllClasses())
		{
			if(forbiddenClassNames.contains(classfound.getName()))
			{
				MessageHandler.getInstance().addError("The name \"{}\" is not allowed as a class name (already used by the system). Please rename this class before the generation.", classfound.getName());
			}
		}
	}
	
	
	/**
	 * This function is used to add all Data Type name to the list of the forbidden names for a class.
	 */
	public void addDataTypesToForbiddenClassNameList()
	{
		if(null!=getDomain().getLanguageConf())
		{
			LanguageConfiguration oLanguageConf = getDomain().getLanguageConf();
			if(null != oLanguageConf.getUiTypeDescriptions())
			{
				for(IUITypeDescription iu : oLanguageConf.getUiTypeDescriptions().values())
				{
					addForbiddenName(iu.getUmlName());
				}
			}
			if(null != oLanguageConf.getTypeDescriptions())
			{
				for(ITypeDescription td : oLanguageConf.getTypeDescriptions().values())
				{
					addForbiddenName(td.getUmlName());
				}
			}
		}
	}
	
	
	/**
	 * Add any String into the list of forbidden names for a class.
	 * @param value
	 */
	public void addForbiddenName(String value)
	{
		if(null == forbiddenClassNames)
		{
			forbiddenClassNames = new ArrayList<String>();
		}
		if(!forbiddenClassNames.contains(value))
		{
			forbiddenClassNames.add(value);
		}
	}
	
}
