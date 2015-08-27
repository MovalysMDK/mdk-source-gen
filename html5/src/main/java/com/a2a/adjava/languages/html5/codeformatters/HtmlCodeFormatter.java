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
package com.a2a.adjava.languages.html5.codeformatters;

import java.io.File;

import com.a2a.adjava.utils.FileTypeUtils;

public class HtmlCodeFormatter extends AbstractJSBeautifyCodeFormatter {

    @Override
    protected String[] getArguments() {
        return new String[] {
            "--html",
            "-r",
            "-q",
            "-s",
            "4"
        };
    }

    /**
	 * Test if file must be formatted
	 * @param p_oFile file
	 * @return true if file must be formatted
	 */
	@Override
	public boolean acceptFile( File p_oFile ) {
		return FileTypeUtils.isHtmlFile(p_oFile.getName());
	}
}
