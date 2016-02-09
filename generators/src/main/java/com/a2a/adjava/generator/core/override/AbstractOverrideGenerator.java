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
package com.a2a.adjava.generator.core.override;

import org.dom4j.Document;

import com.a2a.adjava.codeformatter.FormatOptions;
import com.a2a.adjava.codeformatter.GeneratedFile;
import com.a2a.adjava.generator.core.AbstractXslGenerator;
import com.a2a.adjava.generators.DomainGeneratorContext;
import com.a2a.adjava.xmodele.IDomain;
import com.a2a.adjava.xmodele.XProject;

/**
 * Override Generator
 * 
 * @author lmichenaud
 *
 * @param <D>
 */
public abstract class AbstractOverrideGenerator<D extends IDomain<?, ?>> extends AbstractXslGenerator<D> {

	public void doOverrideTransform(String p_sTemplatePath, String p_sOutputFile, Document p_xDoc,
			XProject<D> p_oProject, DomainGeneratorContext p_oGeneratorContext ) throws Exception {

		doTransformToFile(p_xDoc, p_sTemplatePath, new GeneratedFile<FormatOptions>(p_sOutputFile), p_oProject, p_oGeneratorContext);
	}
	
	public void doOverrideTransform(String p_sTemplatePath, GeneratedFile p_oOutputFile, Document p_xDoc,
			XProject<D> p_oProject, DomainGeneratorContext p_oGeneratorContext ) throws Exception {

		doTransformToFile(p_xDoc, p_sTemplatePath, p_oOutputFile, p_oProject, p_oGeneratorContext);
	}
}
