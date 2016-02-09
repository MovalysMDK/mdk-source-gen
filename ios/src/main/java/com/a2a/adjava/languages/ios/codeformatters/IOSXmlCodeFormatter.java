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
package com.a2a.adjava.languages.ios.codeformatters;

import java.io.File;

import org.apache.commons.io.FilenameUtils;

import com.a2a.adjava.generator.codeformatters.XmlCodeFormatter;
import com.a2a.adjava.languages.ios.generators.CoreDataModelGenerator;
import com.a2a.adjava.languages.ios.generators.PListGenerator;
import com.a2a.adjava.languages.ios.generators.StoryBoardGenerator;

/**
 * Xml code formatter for ios
 * 
 * @author lmichenaud
 * 
 */
public class IOSXmlCodeFormatter extends XmlCodeFormatter {
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean acceptFile(File p_oFile) {
		return super.acceptFile(p_oFile)
				|| p_oFile.getName().equals(
						CoreDataModelGenerator.CONTENTS_FILE)
				|| FilenameUtils.isExtension(p_oFile.getName(), StoryBoardGenerator.STORYBOARD_EXTENSION)
				|| FilenameUtils.isExtension(p_oFile.getName(), PListGenerator.PLIST_EXTENSION);
	}
}
