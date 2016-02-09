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
package com.a2a.adjava;

public enum AdjavaProperty {
	BASE_PACKAGE("basePackage"),
	MB_ANDROID_PACKAGE("mbandroidPackage"),
	APPEND_GENERATOR_FORCE_OVERWRITE("appendGeneratorForceOverwrite"),
	IMPORT_FORCE_OVERWRITE("importForceOverwrite"),
	STORYBOARD_MERGE_EXPERT_MODE("storyboardMergeExpertMode"),
	UNUSED_FILE_STRATEGY_CLASS("unusedFileStrategyClass"),
	MAIN_PROJECT("mainProject"),
	
	@Deprecated
	UNUSED_FILE_BACKUP_DIR("unusedFileBackupDir"),
	/**
	 * default value : (ROOT)/mdkHistory
	 */
	BACKUP_DIR("backupDir");
	
	private String name;
	
	AdjavaProperty(String _name){
		name = _name;
	}
	
	public String getName(){
		return name;
	}

	public String toString(){
		return name;
	}
}
