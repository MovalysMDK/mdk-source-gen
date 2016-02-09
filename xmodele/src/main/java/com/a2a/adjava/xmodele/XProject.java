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
package com.a2a.adjava.xmodele;

import java.util.ArrayList;
import java.util.List;

import com.a2a.adjava.AdjavaException;
import com.a2a.adjava.AdjavaProperty;
import com.a2a.adjava.generators.ResourceGenerator;

/**
 * <p>Project</p>
 *
 * <p>Copyright (c) 2011
 * <p>Company: Adeuza
 *
 * @author lmichenaud
 *
 */

public class XProject<D extends IDomain<? extends IModelDictionary, ? extends IModelFactory>> {

	/**
	 * Name of project
	 */
	private String name ;
	
	/**
	 * Base directory of project
	 */
	private String baseDir ;
	
	/**
	 * Domain of project
	 */
	private D domain ;
	
	/**
	 * Source directory
	 */
	private String sourceDir ;
	
	/**
	 * DDL Directory
	 */
	private String ddlDir;
	
	/**
	 * Assets directory
	 */
	private String assetsDir ;
	
	/**
	 * Layout directory
	 */
	private String layoutDir;
	
	/**
	 * Directory for strings
	 */
	private String stringDir;
	
	/**
	 * Generators
	 */
	private List<ResourceGenerator> generators = new ArrayList<ResourceGenerator>();

	public XProject() {
		
	}
	
	/**
	 * Retourne l'objet name
	 * @return Objet name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Affecte l'objet name 
	 * @param p_oName Objet name
	 */
	public void setName(String p_oName) {
		this.name = p_oName;
	}

	/**
	 * Retourne l'objet baseDir
	 * @return Objet baseDir
	 */
	public String getBaseDir() {
		return this.baseDir;
	}

	/**
	 * Affecte l'objet baseDir 
	 * @param p_oBaseDir Objet baseDir
	 */
	public void setBaseDir(String p_oBaseDir) {
		this.baseDir = p_oBaseDir;
	}
	
	/**
	 * Set directory for layout
	 * @param p_sDir layout dir
	 */
	public void setLayoutDir(String p_sDir) {
		this.layoutDir = p_sDir;
	}
	
	/**
	 * Set directory for strings
	 * @param p_sDir string dir
	 */
	public void setStringDir(String p_sDir) {
		this.stringDir = p_sDir;
	}

	/**
	 * Retourne l'objet generators
	 * @return Objet generators
	 */
	public List<ResourceGenerator> getGenerators() {
		return this.generators;
	}

	/**
	 * Affecte l'objet generators 
	 * @param p_oGenerators Objet generators
	 */
	public void setGenerators(List<ResourceGenerator> p_oGenerators) {
		this.generators = p_oGenerators;
	}

	/**
	 * Retourne l'objet domain
	 * @return Objet domain
	 */
	public D getDomain() {
		return this.domain;
	}

	/**
	 * Affecte l'objet domain 
	 * @param p_oDomain Objet domain
	 */
	public void setDomain(D p_oDomain) {
		this.domain = p_oDomain;
	}

	/**
	 * Retourne l'objet sourceDir
	 * @return Objet sourceDir
	 */
	public String getSourceDir() {
		return this.sourceDir;
	}

	/**
	 * Affecte l'objet sourceDir 
	 * @param p_oSourceDir Objet sourceDir
	 */
	public void setSourceDir(String p_oSourceDir) {
		this.sourceDir = p_oSourceDir;
	}

	/**
	 * Retourne l'objet ddlDir
	 * @return Objet ddlDir
	 */
	public String getDdlDir() {
		return this.ddlDir;
	}

	/**
	 * Affecte l'objet ddlDir 
	 * @param p_oDdlDir Objet ddlDir
	 */
	public void setDdlDir(String p_oDdlDir) {
		this.ddlDir = p_oDdlDir;
	}

	/**
	 * Retourne l'objet assetsDir
	 * @return Objet assetsDir
	 */
	public String getAssetsDir() {
		return this.assetsDir;
	}
	
	public String getLayoutDir() {
		return this.layoutDir;
	}
	
	public String getStringDir() {
		return this.stringDir;
	}

	/**
	 * Affecte l'objet assetsDir 
	 * @param p_oAssetsDir Objet assetsDir
	 */
	public void setAssetsDir(String p_oAssetsDir) {
		this.assetsDir = p_oAssetsDir;
	}
	
	
	public String getBackupDir() throws AdjavaException {

		if(this.getDomain().getGlobalParameters().get(AdjavaProperty.BACKUP_DIR.getName()) != null)
			return this.getDomain().getGlobalParameters().get(AdjavaProperty.BACKUP_DIR.getName());		
		else
			throw new AdjavaException("The property "+AdjavaProperty.BACKUP_DIR+" is missing in POM.XML");
	}

}
