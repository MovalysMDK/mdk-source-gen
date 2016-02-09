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
package com.a2a.adjava.generator.core.incremental;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.xmodele.XProject;

/**
 * <p>TODO Décrire la classe ImportExtractor</p>
 *
 * <p>Copyright (c) 2012
 * <p>Company: Adeuza
 *
 * @author emalespine
 *
 */

public class IncludesExtractor {

	private static final Logger log = LoggerFactory.getLogger(IncludesExtractor.class);
	
	/**
	 * Default constructor
	 */
	public IncludesExtractor() {
	}

	/**
	 * @param p_oFile
	 * @return
	 */
	public List<String> extractIncludes(String p_sFile, XProject<?> p_oProject) throws Exception {

		List<String> r_listImports = new ArrayList<String>();

		File oPojoFile = new File(p_oProject.getBaseDir() + "/" + p_sFile);
		if (oPojoFile.exists()) {
			List<String> listLines = FileUtils.readLines(oPojoFile, p_oProject.getDomain().getFileEncoding());
			if (listLines != null) {
				String sStartMarker = p_oProject.getDomain().getLanguageConf().getIncludeStartMarker();
				String sEndMarker = p_oProject.getDomain().getLanguageConf().getIncludeEndMarker();
				final Pattern oRegEx = Pattern.compile('^' + sStartMarker + "(.*)" + sEndMarker + '$');
				Matcher oMatch = null;
				for( String sLine : listLines ) {
					oMatch = oRegEx.matcher(sLine.trim());
					if (oMatch != null && oMatch.matches()) {
						r_listImports.add(oMatch.group(1));
					}
				}
			}
		}

		return r_listImports;
	}
}
