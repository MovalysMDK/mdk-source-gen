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
package com.a2a.adjava.generator.codeformatters;

import java.io.IOException;
import java.io.Writer;

import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

/**
 * XmlWrite for dom4j that can handle 'standalone' attribute
 * @author lmichenaud
 *
 */
public class StandaloneXmlWriter extends XMLWriter {

	/**
	 * Standalone
	 */
	private boolean standalone = false ;
	
	/**
	 * Constructor
	 * @param p_oWriter writer
	 * @param p_oOutputFormat output format
	 * @param p_bStandalone standalone 
	 */
	public StandaloneXmlWriter(Writer p_oWriter,
			org.dom4j.io.OutputFormat p_oOutputFormat, boolean p_bStandalone) {
		super(p_oWriter, p_oOutputFormat);
		this.standalone = p_bStandalone ;
	}

	/**
	 * {@inheritDoc}
	 * @see org.dom4j.io.XMLWriter#writeDeclaration()
	 */
	@Override
	protected void writeDeclaration() throws IOException {
		OutputFormat oFormat = getOutputFormat();

		String sEncoding = oFormat.getEncoding();

		if (!oFormat.isSuppressDeclaration()) {
			if ("UTF8".equals(sEncoding)) {
				writer.write("<?xml version=\"1.0\"");

				if (!oFormat.isOmitEncoding()) {
					writer.write(" encoding=\"UTF-8\"");
				}

				if ( this.standalone) {
					writer.write(" standalone=\"yes\"");
				}
				writer.write("?>");
			} else {
				writer.write("<?xml version=\"1.0\"");

				if (!oFormat.isOmitEncoding()) {
					writer.write(" encoding=\"" + sEncoding + "\"");
				}

				if ( this.standalone) {
					writer.write(" standalone=\"yes\"");
				}
				writer.write("?>");
			}

			if (oFormat.isNewLineAfterDeclaration()) {
				println();
			}
		}
	}
}
