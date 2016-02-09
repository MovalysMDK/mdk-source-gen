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
package com.a2a.adjava.generator.impl;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.generator.core.override.AbstractOverrideGenerator;
import com.a2a.adjava.generators.DomainGeneratorContext;
import com.a2a.adjava.utils.Chrono;
import com.a2a.adjava.xmodele.IDomain;
import com.a2a.adjava.xmodele.IModelDictionary;
import com.a2a.adjava.xmodele.IModelFactory;
import com.a2a.adjava.xmodele.XProject;

/**
 * 
 * <p>Cree le schema SQL correspondant au modele</p>
 *
 * <p>Copyright (c) 2009
 * <p>Company: Adeuza
 *
 * @author mmadigand
 * @author lmichenaud
 *
 */
public class SchemaGenerator extends AbstractOverrideGenerator<IDomain<IModelDictionary,IModelFactory>> {

	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(SchemaGenerator.class);
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.generators.ResourceGenerator#genere(com.a2a.adjava.xmodele.XProject, com.a2a.adjava.generators.DomainGeneratorContext)
	 */
	@Override
	public void genere( XProject<IDomain<IModelDictionary,IModelFactory>> p_oProject, DomainGeneratorContext p_oContext) throws Exception {
		log.debug("> SchemaGenerator.genere");
		Chrono oChrono = new Chrono(true);
		Document xSchema = DocumentHelper.createDocument(p_oProject.getDomain().getSchema().toXml());

		String sSchemaFile = "";
		if(p_oProject.getDdlDir() != null && ! p_oProject.getDdlDir().trim().isEmpty())
			sSchemaFile = p_oProject.getDdlDir() + "/";		
		sSchemaFile+="oracle.sql" ;
		
		log.debug("  generation du fichier {}", sSchemaFile);
		this.doOverrideTransform("schema.xsl", sSchemaFile, xSchema, p_oProject, p_oContext);
		
		log.debug("< SchemaGenerator.genere: {}", oChrono.stopAndDisplay());
	}
}
