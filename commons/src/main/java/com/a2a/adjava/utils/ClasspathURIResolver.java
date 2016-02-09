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
package com.a2a.adjava.utils;

import java.io.InputStream;

import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * <p>Load uri using classpath</p>
 *
 * <p>Copyright (c) 2009
 * <p>Company: Adeuza
 *
 * @author lmichenaud
 *
 */

public class ClasspathURIResolver implements URIResolver {

	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(ClasspathURIResolver.class);
	
	/**
	 * Base path
	 */
	private String base ;

	/**
	 * generationVersion
	 */
	private ItfVersion version;
	
	/**
	 * Constructeur
	 * @param p_sBase chemin de base de resolution
	 */
	public ClasspathURIResolver( String p_sBase, ItfVersion version ) {
		this.base = p_sBase ;
		this.version = version;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Source resolve(String p_sHref, String p_sBase) throws TransformerException {
		
		StringBuilder sPath = new StringBuilder();
		if ( p_sBase != null) {
			sPath.append( p_sBase );
		}
		else {
			if ( p_sHref.charAt(0) != '/') {
				sPath.append( this.base );
				if ( !p_sHref.endsWith("/")) {
					sPath.append('/');
				}
			}
		}
		
		sPath.append(p_sHref);
		String sPathVersionned = "";
		
		String[] splitPath = sPath.toString().split("/");
		if (splitPath.length>0) {
			String filenameWithExt = splitPath[splitPath.length - 1];
			String[] splittedFileName = filenameWithExt.split("\\.");
			if (splittedFileName.length == 2) {
				String filename = splittedFileName[0];
				filename = filename + "-v" + this.version.getStringVersion() + "." + splittedFileName[1];
				splitPath[splitPath.length - 1] = filename;
				sPathVersionned = StringUtils.join(splitPath, "/");
			} else {
				throw new TransformerException("there is a dot in the filename");
			}
		}
		
		InputStream oInputStream = this.getClass().getResourceAsStream( sPathVersionned );
		if ( oInputStream != null ) {
			log.debug(" the versionned path exists :{}", sPathVersionned);
			return new StreamSource(oInputStream);
		} else {
			oInputStream = this.getClass().getResourceAsStream( sPath.toString());
			if ( oInputStream == null ) {
				throw new TransformerException("ClasspathURIResolver: failed to resolve resource path : " + sPath.toString()
					+ ", base = " + this.base);
			}
			return new StreamSource(oInputStream);
		}
	}
}
