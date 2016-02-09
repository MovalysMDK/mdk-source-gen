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
package com.adeuza.adjava.generator.indent;

/**
 * <p>Test de l'indenteur</p>
 *
 * <p>Copyright (c) 2010
 * <p>Company: Adeuza
 *
 * @author lmichenaud
 *
 */

import java.io.InputStream;
import java.util.Properties;

import junit.framework.Assert;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.jdt.internal.formatter.DefaultCodeFormatter;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.TextEdit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * <p>Classe de tests pour tester l'indenteur</p>
 *
 * L'indenteur d'eclipse peut etre appele en ligne de commande, ex :
 * 	
 *	/opt/eclipse-35+maven-dev/eclipse -vm /opt/java/jdk1.6.0_12/bin/java -application org.eclipse.jdt.core.JavaCodeFormatter \
 *	-verbose -config /home/projets/lmichenaud/workspace-2.5.0/.metadata/.plugins/org.eclipse.core.runtime/.settings/org.eclipse.jdt.core.prefs \
 *	/home/projets/lmichenaud/workspace-2.5.0/produits/fwk-backend-2.5.0/standalone/alert/src/main/java/com/adeuza/movalys/fwk/alert/core/dbgen/model/AlertAreaImpl.java
 *
 * <p>Copyright (c) 2010
 * <p>Company: Adeuza
 *
 * @author lmichenaud
 *
 */
@RunWith(JUnit4.class)
public class TestIndenteur {
		
	/**
	 * Preparation des tests de la classe
	 */
	@Before
	public void setUp() {
		// Nothing to do
	}
	
	/**
	 * Test l'indentation
	 * @throws Exception
	 */
	@Test
	public void testIndent() throws Exception {
		indentFile("/com/adeuza/adjava/indent/AlertAreaImpl.java.test");
		indentFile("/com/adeuza/adjava/indent/AlertArea.java.test");
	}
	
	/**
	 * Indent un fichier
	 * @param p_sFile chemin classpath du fichier 
	 * @throws Exception echec de l'indentation
	 */
	private void indentFile( String p_sFile ) throws Exception {
				
		String sCode = IOUtils.toString(this.getClass().getResourceAsStream(p_sFile), Charsets.UTF_8);
		Assert.assertNotNull(sCode);
		Assert.assertTrue(sCode.length() > 0 );
		
		InputStream oIs = this.getClass().getResourceAsStream("/eclipse-indent.xml");
		Assert.assertNotNull(oIs);
		Properties oFormatterOptions = new Properties();
		oFormatterOptions.load(oIs);	
		DefaultCodeFormatter oCodeFormatter = new DefaultCodeFormatter(oFormatterOptions);
		oIs.close();

		IDocument oDocument = new Document();
		oDocument.set(sCode);
		TextEdit oTextEdit = oCodeFormatter.format(CodeFormatter.K_COMPILATION_UNIT, sCode, 0, sCode.length(), 0, null);
		if (oTextEdit != null) {
			oTextEdit.apply(oDocument);
			checkCompilationUnit(oDocument.get());
		} else {
			System.err.println("Erreur de formattage");
			Assert.fail();
		}
	}
	
	/**
	 * Teste le contenu d'un fichier
	 * @param p_sContent contenu complet du fichier Java
	 */
	private void checkCompilationUnit( String p_sContent ) {
		for( String sLine : p_sContent.split(IOUtils.LINE_SEPARATOR)) {
			checkLine(sLine);
		}
	}
	
	/**
	 * Verification sur une ligne de code
	 * @param p_sLine ligne de code
	 */
	private void checkLine( String p_sLine ) {
		// Verifie que la ligne ne se termine pas par && (doit etre au debut)
		Assert.assertFalse("Une ligne de code ne peut pas terminer par && ", p_sLine.trim().endsWith("&&"));
		
		// Verifie qu'aucune ligne ne depasse les 150 caracteres
		Assert.assertFalse( p_sLine.length() > 150 );
	}
}
