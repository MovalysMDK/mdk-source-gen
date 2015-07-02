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
package com.a2a.adjava.languages.html5.generators;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.generator.core.incremental.AbstractIncrementalGenerator;
import com.a2a.adjava.generators.DomainGeneratorContext;
import com.a2a.adjava.languages.html5.xmodele.MH5Dictionary;
import com.a2a.adjava.languages.html5.xmodele.MH5ImportDelegate;
import com.a2a.adjava.languages.html5.xmodele.MH5ModeleFactory;
import com.a2a.adjava.utils.Chrono;
import com.a2a.adjava.utils.FileTypeUtils;
import com.a2a.adjava.xmodele.IDomain;
import com.a2a.adjava.xmodele.IModelDictionary;
import com.a2a.adjava.xmodele.MAssociation;
import com.a2a.adjava.xmodele.MDaoImpl;
import com.a2a.adjava.xmodele.XProject;

/**
 * 
 * <p>
 * Generates the implementations of the beans for DaoMapping, DaoProxy and DaoSql.
 * </p>
 * 
 * <p>
 * Copyright (c) 2009
 * <p>
 * Company: Adeuza
 * 
 */
public class DaoGenerator extends AbstractIncrementalGenerator<IDomain<MH5Dictionary,MH5ModeleFactory>> {
	
	/**
	 * Logger for this class
	 */
	private static final Logger log = LoggerFactory.getLogger(DaoGenerator.class);
	
	private static final String docPathDaoMapping = "webapp/src/app/data/dao/mappings";
	private static final String docPathDaoProxy   = "webapp/src/app/data/dao";
	private static final String docPathDaoSql     = "webapp/src/app/data/dao/sql";
	private static final String docPathDaoNoSql     = "webapp/src/app/data/dao/nosql";
	
	private static final String docSuffixMapping = "Mapping";
	private static final String docSuffixProxy   = "Proxy";
	private static final String docSuffixSql     = "Sql";
	private static final String docSuffixNoSql     = "NoSql";


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void genere(
			XProject<IDomain<MH5Dictionary,MH5ModeleFactory>> p_oMProject,
			DomainGeneratorContext p_oGeneratorContext) throws Exception {
		
		log.debug("> DaoGenerator.genere");
		Chrono oChrono = new Chrono(true);
		IModelDictionary oDictionnary = p_oMProject.getDomain().getDictionnary();
		
		
		/* DaoMapping */
		for (MDaoImpl oClass : oDictionnary.getAllDaos()) {			
			
			String sNameBefore = oClass.getName();
			//set the first letter of the class in uppercase.
			sNameBefore = sNameBefore.substring(0,1).toUpperCase() + sNameBefore.substring(1);
			
			oClass.setName(sNameBefore+docSuffixMapping);
			
			createDaoMapping(oClass, p_oMProject, p_oGeneratorContext);
			
			oClass.setName(sNameBefore);
		}
		
		
		/* DaoProxy */
		for (MDaoImpl oClass : oDictionnary.getAllDaos()) {			
			
			String sNameBefore = oClass.getName();
			oClass.setName(sNameBefore+docSuffixProxy);
			
			createDaoProxy(oClass, p_oMProject, p_oGeneratorContext);
			
			oClass.setName(sNameBefore);
		}
		
		
		/* DaoSql */
		for (MDaoImpl oClass : oDictionnary.getAllDaos()) {
			
			String sNameBefore = oClass.getName();
			oClass.setName(sNameBefore+docSuffixSql);
			
			createDaoSql(oClass, p_oMProject, p_oGeneratorContext);
			
			oClass.setName(sNameBefore);
		}
		
		
		/* DaoNoSql */
		for (MDaoImpl oClass : oDictionnary.getAllDaos()) {
			
			String sNameBefore = oClass.getName();
			oClass.setName(sNameBefore+docSuffixNoSql);
			
			createDaoNoSql(oClass, p_oMProject, p_oGeneratorContext);
			
			oClass.setName(sNameBefore);
		}
		
		
		log.debug("< DaoGenerator.genere: {}", oChrono.stopAndDisplay());
	}
	
	

	/**
	 * Generates the implementation of the bean for DaoMapping
	 * 
	 * @param p_oClass
	 *            the implementation class of the bean
	 * @param p_oNonGeneratedBlocExtractor
	 *            the non generated blocks
	 * @param p_oProjectConfig
	 *            config adjava
	 * @param p_oXslPojoTransformer
	 *            transformer xsl
	 * @throws Exception
	 *             for generation failures
	 */
	private void createDaoMapping(MDaoImpl p_oClass, XProject<IDomain<MH5Dictionary,MH5ModeleFactory>> p_oMProject,
			DomainGeneratorContext p_oContext) throws Exception {

		Document xClass = DocumentHelper.createDocument(p_oClass.toXml());

		String sPojoFile = FileTypeUtils.computeFilenameForJS(docPathDaoMapping, p_oClass.getName());
		
		log.debug("  generation of file: {}", sPojoFile);
		this.doIncrementalTransform("dao/dao-mapping.xsl", sPojoFile, xClass, p_oMProject, p_oContext);
	}
	
	
	/**
	 * Generates the implementation of the bean for DaoProxy
	 * 
	 * @param p_oClass
	 *            the implementation class of the bean
	 * @param p_oNonGeneratedBlocExtractor
	 *            the non generated blocks
	 * @param p_oProjectConfig
	 *            config adjava
	 * @param p_oXslPojoTransformer
	 *            transformer xsl
	 * @throws Exception
	 *             for generation failures
	 */
	private void createDaoProxy(MDaoImpl p_oClass, XProject<IDomain<MH5Dictionary,MH5ModeleFactory>> p_oMProject,
			DomainGeneratorContext p_oContext) throws Exception {

		Document xClass = DocumentHelper.createDocument(p_oClass.toXml());

		String sPojoFile = FileTypeUtils.computeFilenameForJS(docPathDaoProxy, p_oClass.getName());
		
		log.debug("  generation of file: {}", sPojoFile);
		this.doIncrementalTransform("dao/dao-proxy.xsl", sPojoFile, xClass, p_oMProject, p_oContext);
	}
	
	
	/**
	 * Generates the implementation of the bean for DaoSql
	 * 
	 * @param p_oClass
	 *            the implementation class of the bean
	 * @param p_oNonGeneratedBlocExtractor
	 *            the non generated blocks
	 * @param p_oProjectConfig
	 *            config adjava
	 * @param p_oXslPojoTransformer
	 *            transformer xsl
	 * @throws Exception
	 *             for generation failures
	 */
	private void createDaoSql(MDaoImpl p_oClass, XProject<IDomain<MH5Dictionary,MH5ModeleFactory>> p_oMProject,
			DomainGeneratorContext p_oContext) throws Exception {

		Element r_xFile = p_oClass.toXml();
		
		MH5ImportDelegate oMH5ImportDelegate = p_oMProject.getDomain().getXModeleFactory().createImportDelegate(this);
		this.computeImportForDaoSql(oMH5ImportDelegate, p_oClass, p_oMProject, p_oContext);
		r_xFile.add(oMH5ImportDelegate.toXml());
		
		Document xClass = DocumentHelper.createDocument(r_xFile);

		String sPojoFile = FileTypeUtils.computeFilenameForJS(docPathDaoSql, p_oClass.getName());
		
		log.debug("  generation of file: {}", sPojoFile);
		this.doIncrementalTransform("dao/dao-sql.xsl", sPojoFile, xClass, p_oMProject, p_oContext);
	}
	
	
	/**
	 * Generates the implementation of the bean for DaoNoSql
	 * 
	 * @param p_oClass
	 *            the implementation class of the bean
	 * @param p_oNonGeneratedBlocExtractor
	 *            the non generated blocks
	 * @param p_oProjectConfig
	 *            config adjava
	 * @param p_oXslPojoTransformer
	 *            transformer xsl
	 * @throws Exception
	 *             for generation failures
	 */
	private void createDaoNoSql(MDaoImpl p_oClass, XProject<IDomain<MH5Dictionary,MH5ModeleFactory>> p_oMProject,
			DomainGeneratorContext p_oContext) throws Exception {

		Element r_xFile = p_oClass.toXml();
		
		
		//		For now we need the same import than the DaoSql classes, may come to change
		MH5ImportDelegate oMH5ImportDelegate = p_oMProject.getDomain().getXModeleFactory().createImportDelegate(this);
		this.computeImportForDaoSql(oMH5ImportDelegate, p_oClass, p_oMProject, p_oContext);
		r_xFile.add(oMH5ImportDelegate.toXml());
		
		Document xClass = DocumentHelper.createDocument(r_xFile);

		String sPojoFile = FileTypeUtils.computeFilenameForJS(docPathDaoNoSql, p_oClass.getName());
		
		log.debug("  generation of file: {}", sPojoFile);
		this.doIncrementalTransform("dao/dao-no-sql.xsl", sPojoFile, xClass, p_oMProject, p_oContext);
	}
	
	
	
	
	
	/**
	 * Compute imports for Dao sql implementation
	 * @param p_oClass Dao Impl
	 * @param p_oMProject project
	 * @param p_oContext context
	 * @throws Exception
	 */
	protected void computeImportForDaoSql( MH5ImportDelegate p_oMH5ImportDelegate,
			MDaoImpl p_oClass, XProject<IDomain<MH5Dictionary,MH5ModeleFactory>> p_oMProject,
			DomainGeneratorContext p_oContext) throws Exception {

			p_oMH5ImportDelegate.addImport(p_oClass.getMasterInterface().getName()+"Mapping");
			
			for(MAssociation assos : p_oClass.getMEntityImpl().getAssociations()){
				if(!assos.isTransient() &&
					assos.getRefClass()!=null &&
					assos.getRefClass().getDao()!=null){
					p_oMH5ImportDelegate.addImport(assos.getRefClass().getDao().getName() + "Proxy");
				}
			}	
	}

}
