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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.a2a.adjava.xmodele.XProject;

/**
 * Extractor of non-generated blocs
 * 
 * @author lmichenaud
 */
public class NonGeneratedBlocExtractor {

	/**
	 * Extract blocs of non-generated source code
	 * @param p_sFile file name
	 * @param p_oProject project
	 * @return map of non-generated source code
	 * @throws Exception exception
	 */
	public Map<String,NonGeneratedBloc> extractBlocs(String p_sFile, XProject<?> p_oProject) throws Exception {

		Map<String,NonGeneratedBloc> r_mapBlocs = null;

		File oPojoFile = new File(p_oProject.getBaseDir(), p_sFile);
		if (oPojoFile.exists()) {
			List<String> listLines = FileUtils.readLines(oPojoFile, p_oProject.getDomain().getFileEncoding());
			r_mapBlocs = this.extractBlocs(listLines, p_oProject);
		}
		else {
			r_mapBlocs = new HashMap<String,NonGeneratedBloc>();
		}

		return r_mapBlocs;
	}

	/**
	 * Extract blocs of non-generated source code
	 * @param p_listLines lines to analyze
	 * @param p_oProject project
	 * @return map of non-generated source code
	 */
	protected Map<String,NonGeneratedBloc> extractBlocs( List<String> p_listLines, XProject<?> p_oProject ) {
		
		Map<String,NonGeneratedBloc> r_mapBlocs = new HashMap<String,NonGeneratedBloc>();
		NonGeneratedBloc oNonGeneratedBloc = null ;
		boolean bKeepLine = false ;
		String sBlocId = null ;
		for( String sLine : p_listLines ) {
			String sTrimLine = sLine.trim();
			
			String sXmlNonGenStart = p_oProject.getDomain().getLanguageConf().getNonGeneratedStartMarker();
			String sXmlNonGenEnd = p_oProject.getDomain().getLanguageConf().getNonGeneratedEndMarker();
			
			Pattern oPattern = Pattern.compile(sXmlNonGenStart);
			Matcher oMatcher = oPattern.matcher(sTrimLine);
			if ( !bKeepLine && oMatcher.find() && oMatcher.groupCount() > 0) {
				sBlocId = oMatcher.group(1);
				bKeepLine = true ;
				oNonGeneratedBloc = new NonGeneratedBloc(sBlocId);
				
				oNonGeneratedBloc.setAllowOverride(oMatcher.group(2).contains("[X]"));
			}
			else
			if ( bKeepLine ) {
				oPattern = Pattern.compile(sXmlNonGenEnd);
				oMatcher = oPattern.matcher(sTrimLine);
				if (oMatcher.find()) {
					bKeepLine = false ;
					r_mapBlocs.put(sBlocId,oNonGeneratedBloc);
					sBlocId = null ;
					oNonGeneratedBloc = null ;
				}
				else {
					oNonGeneratedBloc.addLine(sLine);
				}
			}
		}
		return r_mapBlocs ;
	}
	
	/**
	 * Transform map of non-generated source code to xml
	 * @param p_mapBlocs map of non-generated source code
	 * @return xml element
	 */
	public Element toXml( Map<String,NonGeneratedBloc> p_mapBlocs ) {
		Element r_xNonGenerated = DocumentHelper.createElement("non-generated");
		for(NonGeneratedBloc oNonGeneratedBloc : p_mapBlocs.values()) {
			Element xBloc = r_xNonGenerated.addElement("bloc");
			xBloc.addAttribute("id", oNonGeneratedBloc.getId());
			xBloc.addAttribute("allow-override", Boolean.toString(oNonGeneratedBloc.isAllowOverride()));
			xBloc.setText(oNonGeneratedBloc.getLines());
		}
		return r_xNonGenerated ;
	}
	
	/**
	 * Transform map of non-generated source code to xml
	 * @param p_mapBlocs map of non-generated source code
	 * @param p_xDoc document for creating instance of nodes
	 * @return xml element
	 */
	public org.w3c.dom.Element toXml( Map<String,NonGeneratedBloc> p_mapBlocs, org.w3c.dom.Document p_xDoc ) {
		org.w3c.dom.Element r_xNonGenerated = p_xDoc.createElement("non-generated");
		for(NonGeneratedBloc oNonGeneratedBloc : p_mapBlocs.values()) {
			org.w3c.dom.Element xBloc = p_xDoc.createElement("bloc");			
			xBloc.setAttribute("id", oNonGeneratedBloc.getId());
			xBloc.setAttribute("allow-override", Boolean.toString(oNonGeneratedBloc.isAllowOverride()));
			xBloc.setTextContent(oNonGeneratedBloc.getLines());
			r_xNonGenerated.appendChild( xBloc);
		}
		return r_xNonGenerated ;
	}
}
