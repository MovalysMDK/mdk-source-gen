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
package com.a2a.adjava.generator.core.concat;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.dom4j.Document;

import com.a2a.adjava.codeformatter.GeneratedFile;
import com.a2a.adjava.generator.core.AbstractXslGenerator;
import com.a2a.adjava.generator.core.GeneratorUtils;
import com.a2a.adjava.generators.DomainGeneratorContext;
import com.a2a.adjava.xmodele.IDomain;
import com.a2a.adjava.xmodele.XProject;

/**
 * <p>Classe abstraite permettant la génération dans un fichier en concaténation.</p>
 *
 * <p>Copyright (c) 2011</p>
 * <p>Company: Adeuza</p>
 *
 * @author fbourlieux
 * @since Annapurna
 * 
 * @param <D> type of Domain
 */
public abstract class AbstractConcatGenerator<D extends IDomain<?, ?>> extends AbstractXslGenerator<D> {

	/**
	 * <p>
	 * 	Génération dans un fichier déjà existant. Tout nouveau code est ajouté à la fin du fichier.
	 * </p>
	 * @param p_xDoc document xml qu'on va utiliser pour injecter les donénes dans le template
	 * @param p_sTemplatePath chemin du template
	 * @param p_oOutputFile nom du fichier de sortie
	 * @param p_oProject projet courant
	 * @param p_oGeneratorContext session
	 * @throws Exception exception globale
	 */
	public void doConcatGeneration(Document p_xDoc, String p_sTemplatePath, GeneratedFile p_oOutputFile,
			XProject<D> p_oProject, DomainGeneratorContext p_oGeneratorContext) throws Exception {
				
		File oFile = new File(p_oProject.getBaseDir() , p_oOutputFile.getFile().getPath());
		boolean bIsAppend = oFile.exists();

		// Write new  file
		String sContent = doTransformToString(p_xDoc, p_sTemplatePath, p_oOutputFile.getXslProperties(), p_oProject);
		FileUtils.writeStringToFile(oFile, sContent, p_oProject.getDomain().getFileEncoding(), bIsAppend);

		if (isDebug()) {
			GeneratorUtils.writeXmlDebugFile(p_xDoc, p_oOutputFile.getFile().getPath(), p_oProject, bIsAppend);
		}
	}

}
