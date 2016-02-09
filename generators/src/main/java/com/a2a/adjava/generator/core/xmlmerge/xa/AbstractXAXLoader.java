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
package com.a2a.adjava.generator.core.xmlmerge.xa;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import com.a2a.adjava.AdjavaException;


/**
 * Loader abastrait pour les fichiers xml
 * @author smaitre
 *
 * @param <ERROR_TYPE> le type de l'erreur
 * @param <ERROR_CLASS> la classe représentative de l'erreur
 */
public abstract class AbstractXAXLoader<ERROR_TYPE extends ErrorType, ERROR_CLASS extends AbstractXALoaderError<ERROR_TYPE>>  extends AbstractXALoader<ERROR_TYPE, ERROR_CLASS>{

	/** 
	 * Logger
	 */
	private static final Logger LOG = LoggerFactory.getLogger(AbstractXAXLoader.class);

	/**
	 *  Le chemin du répertoire où trouver les fichiers à lire
	 */
	private Path path = null;

	/** 
	 * L'extension des fichiers devant être lus
	 */
	private String extension = null;

	/**
	 * Le nom du fichier de XA configuration
	 */
	private String xaConfName=null;

	/**
	 * Constructeur
	 * @param p_sPath le chemin du répertoire où trouver les fichiers à lire
	 * @param p_sExtension l'extension des fichiers devant être lus
	 * @param p_sXaConfName Le nom du fichier de XA configuration
	 */
	public AbstractXAXLoader(Path p_sPath, String p_sExtension, String p_sXaConfName) {
		super();
		this.path = p_sPath;
		this.extension = p_sExtension;
		this.xaConfName = p_sXaConfName;
	}

	/**
	 * Traitement du répertoire : traite tous les fichiers dans le path portant l'extension extension
	 * @throws AdjavaException 
	 */
	@Override
	protected void treat() throws AdjavaException {
		File oFile = this.path.toFile();

		if (oFile.isDirectory()) {
			//LOG.debug("Traitement du répertoire : " + oFile.getAbsolutePath());
			for(File oSubFile : getFilesList(oFile,extension)) {
				//LOG.debug("Traitement du fichier : " + oSubFile.getName());
				treatFile(oSubFile);
			}
		}
		else {
			//LOG.debug(this.path + " n'est pas un répertoire.");
			if(oFile.isFile()){
				if(extension ==null || extension.equalsIgnoreCase(FilenameUtils.getExtension(oFile.getName()))){
					treatFile(oFile);					
				}
				else {
					LOG.error("The file has not the expected extension : "+this.path);
				}
			}
		}
	}

	/**
	 * Retourne la liste des fichiers d'un répertoire qui comportent un extension donnére
	 * @param p_oFile L'objet File représentant un répertoire
	 * @param p_sExtension Une extension dont on souhiate récupérer les fichiers qui la comportent
	 * @return Une liste de fichiers
	 */
	private List<File> getFilesList(File p_oFile, String p_sExtension){
		List<File> result = new ArrayList<File>();

		for(File oSubFile : p_oFile.listFiles()) {
			if(oSubFile.isFile()){
				if(p_sExtension ==null || p_sExtension.equalsIgnoreCase(FilenameUtils.getExtension(oSubFile.getName()))){
					result.add(oSubFile);
				}
			}
			else if(oSubFile.isDirectory() && !oSubFile.isHidden()){
				result.addAll(getFilesList(oSubFile,p_sExtension));
			}

		}
		return result;
	}
	
	/**
	 * Traite le fichier passé en paramètre
	 * @param p_oFile Le fichier à traiter
	 * @throws AdjavaException Exception levée durant le traitement du fichier.
	 */
	private void treatFile(File p_oFile) throws AdjavaException{
		LOG.debug("[AbstractXAXLoader#treatFile] will parse XML file '"+p_oFile.getPath()+"'... ");
		SAXReader oReader = new SAXReader(false);

		try {
			oReader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
		} catch (SAXException e1) {
			LOG.error("Erreur lors de la configuration du SAXReader : \n"+ e1.getMessage());
		}

		String sRead = null;
		FileReader oFR = null;
		BufferedReader oBR = null;
		boolean bHeader = false;
		try {
			bHeader = false;
			oFR = new FileReader(p_oFile);
			oBR = new BufferedReader(oFR);
			sRead = oBR.readLine();
			if (sRead != null) {
				sRead = sRead + oBR.readLine();
				if (sRead != null) {
					sRead = sRead + oBR.readLine();
				}
				if (sRead.contains("<?xml")) {
					bHeader = true;
				}
			}
		} catch (FileNotFoundException e) {
			LOG.error("Impossible de lire le fichier " + p_oFile.getAbsolutePath(), e);
		} catch (IOException e) {
			LOG.error("Impossible de lire le fichier " + p_oFile.getAbsolutePath(), e);
		}
		finally {
			try {					
				oFR.close();
				oBR.close();
				Document oDoc = oReader.read(p_oFile);
				this.treat( p_oFile.toPath(), oDoc, bHeader,xaConfName);
			}
			catch (DocumentException e) {
				LOG.error("Impossible de lire le fichier " + p_oFile.getAbsolutePath(), e);
			} catch (IOException e) {
				LOG.error("Impossible de lire le fichier " + p_oFile.getAbsolutePath(), e);
			}

		}



	}

	/**
	 * Traitement du noeud principal
	 * @param p_sPath Le chemin actuel du traitement (utilisé pour la localisation des erreurs)
	 * @param p_oDoc le document xml à traiter
	 * @param p_bHeader Un booléen qui indique s'il y a un header
	 * @param p_sXaConfName Le nom du fichier Xa configuration
	 * @throws AdjavaException Une exception qui peut être levée lors du traitement
	 */
	protected abstract void treat( Path p_sPath, Document p_oDoc, boolean p_bHeader, String p_sXaConfName) throws AdjavaException;


}
