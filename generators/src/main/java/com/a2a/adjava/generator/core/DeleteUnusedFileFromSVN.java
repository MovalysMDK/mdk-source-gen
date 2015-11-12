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
package com.a2a.adjava.generator.core;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNErrorCode;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.wc.ISVNStatusHandler;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNStatus;
import org.tmatesoft.svn.core.wc.SVNStatusClient;
import org.tmatesoft.svn.core.wc.SVNStatusType;
import org.tmatesoft.svn.core.wc.SVNWCClient;

import com.a2a.adjava.AdjavaException;
import com.a2a.adjava.AdjavaProperty;
import com.a2a.adjava.generators.GeneratorContext;

/**
 * Strategy class that deletes files that are not anymore generated (compared to last generation).
 * If file is managed by svn, svn delete is executed.
 * If file is not managed by svn, file is deleted.
 * A backup of the file is made in the directory indicated in 'backupDir' property.
 * @author lmichenaud
 *
 */
public class DeleteUnusedFileFromSVN implements UnusedFileStrategy {

	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(DeleteUnusedFileFromSVN.class);
	
	/**
	 * 
	 */
	private SVNClientManager clientManager = SVNClientManager.newInstance();
	
	/**
	 * Backup directory for unused files
	 */
	private String backupDir ;
		
	/**
	 * (non-Javadoc)
	 * @see com.a2a.adjava.generator.core.UnusedFileStrategy#treatUnusedFiles(java.util.List)
	 */
	@Override
	public void treatUnusedFiles(Collection<String> p_listFiles, Timestamp p_oGenerationDate, GeneratorContext p_oContext ) throws Exception {
	
		log.debug("treatUnusedFiles");
		
		SVNWCClient oWCClient = clientManager.getWCClient();
		
		for( String sFile : p_listFiles ) {
			File oFile = new File(sFile);
			log.debug("  treat file: " + oFile.getAbsolutePath());
			
			this.backupFile(sFile, p_oGenerationDate);
			if ( isVersioned( oFile )) {
				log.debug("    versioned file, do svn delete");
				oWCClient.doDelete( oFile , true , false );	
				if((oFile.getParentFile() != null) && (oFile.getParentFile().listFiles() != null) && (oFile.getParentFile().listFiles().length == 0))
				{
					oWCClient.doDelete( oFile.getParentFile() , true , false );	
				}
			}
			else {
				log.debug("    delete file on fs");
				
				oFile.delete();
				
				if((oFile.getParentFile() != null) && (oFile.getParentFile().listFiles() != null) && (oFile.getParentFile().listFiles().length == 0))
				{
					oFile.getParentFile().delete();
				}
			}
		}
	}
	
	/**
	 * @param file
	 * @return
	 */
	public boolean isVersioned(File file) {
		boolean r_bVersioned = false ;
		SVNStatusClient oClient = clientManager.getStatusClient();

		IsVersionedStatusHandler handler = new IsVersionedStatusHandler();
		try { 
			oClient.doStatus(file, SVNRevision.UNDEFINED, SVNDepth.EMPTY, false, true, true, false, handler,
					null);
			r_bVersioned = handler.isVersioned();
		} catch (SVNException oSVNException) {	
			final int errorCode = oSVNException.getErrorMessage().getErrorCode().getCode(); 
			if (errorCode == SVNErrorCode.WC_NOT_DIRECTORY.getCode() 
					|| errorCode == SVNErrorCode.WC_NOT_FILE.getCode()
					|| errorCode == SVNErrorCode.WC_UNSUPPORTED_FORMAT.getCode()) { 
				r_bVersioned = false; 
			}
		}
		return r_bVersioned;
	}
	
	/**
	 * @param p_sFilePath
	 * @throws IOException 
	 */
	public void backupFile( String p_sFilePath, Timestamp p_oGenerationDate ) throws Exception {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HH'h'mm'm'ss");
		String sDate = sdf.format(p_oGenerationDate);
		
		File oBackupFile = new File( this.backupDir, sDate + "/" + p_sFilePath);
		if ( !oBackupFile.getParentFile().exists()) {
			oBackupFile.getParentFile().mkdirs();
		}
		File oSourceFile = new File(p_sFilePath);
		if ( oSourceFile.exists()) {
			log.debug("    backup file to: " + oBackupFile.getAbsolutePath());
			FileUtils.copyFile(oSourceFile, oBackupFile, true);
		}
	}
	
	/**
	 * @author lmichenaud
	 *
	 */
	private static class IsVersionedStatusHandler implements ISVNStatusHandler {
		
		/**
		 * 
		 */
		private boolean versioned = false ;
		
		/**
		 * {@inheritDoc}
		 * @see org.tmatesoft.svn.core.wc.ISVNStatusHandler#handleStatus(org.tmatesoft.svn.core.wc.SVNStatus)
		 */
		public void handleStatus(SVNStatus p_oStatus) throws SVNException {
			versioned = p_oStatus.getContentsStatus() != SVNStatusType.STATUS_UNVERSIONED;
		}
		
		/**
		 * @return
		 */
		public boolean isVersioned() {
			return versioned;
		}
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.generator.core.UnusedFileStrategy#afterInitialization(java.util.Map)
	 */
	@Override
	public void afterInitialization( Map<String,String> p_mapGlobalProperties ) throws Exception {
		
		this.backupDir = p_mapGlobalProperties.get(AdjavaProperty.UNUSED_FILE_BACKUP_DIR.getName());
		if ( this.backupDir == null ) {
			this.backupDir = p_mapGlobalProperties.get(AdjavaProperty.BACKUP_DIR.getName());
			if ( this.backupDir == null ) {
			throw new AdjavaException("property '{}' or '{}' is missing (configuration/properties node of adjava-maven-plugin in pom.xml).",
					AdjavaProperty.UNUSED_FILE_BACKUP_DIR.getName(),AdjavaProperty.BACKUP_DIR.getName());
			}
			else{
				this.backupDir +="/unusedFiles";
			}
		}
	}
}
