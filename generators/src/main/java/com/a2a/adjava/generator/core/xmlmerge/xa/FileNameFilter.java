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


import java.io.File;
import java.io.FilenameFilter;

/**
 * Filter de fichier pour ne prendre qu'une partie des fichiers d'un répertoire
 * @author smaitre
 *
 */
public class FileNameFilter implements FilenameFilter {

	/**
	 * l'extension des fichiers conservés
	 */
	private String extension;
	
	/**
	 * Constructeur
	 * @param p_sExtension
	 */
	public FileNameFilter(String p_sExtension) {
		this.extension = p_sExtension;
	}
	
	/**
	 * Filtre les fichiers se terminant par extension
	 * @return true si le fichier doit être conservé
	 */
	@Override
	public boolean accept(File dir, String name) {
		return name.endsWith(extension);
	}

}
