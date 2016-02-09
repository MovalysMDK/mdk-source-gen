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
package com.a2a.adjava.runtime;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.repository.metadata.ArtifactRepositoryMetadata;
import org.apache.maven.artifact.repository.metadata.RepositoryMetadata;
import org.apache.maven.artifact.repository.metadata.RepositoryMetadataManager;
import org.apache.maven.artifact.repository.metadata.Versioning;
import org.apache.maven.artifact.versioning.VersionRange;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Plugin;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

import com.a2a.adjava.AdjavaException;
import com.a2a.adjava.utils.JaxbUtils;
import com.a2a.adjava.versions.ExecutionMetadata;
import com.a2a.adjava.versions.GenerationDescriptor;
import com.a2a.adjava.versions.GenerationHistory;
import com.a2a.adjava.versions.GenerationHistoryHolder;
import com.a2a.adjava.versions.RuntimeDependency;

/**
 * <p>Classe permettant de déterminer si l'éxécution doit s'effectuer.</p>
 *
 * <p>Copyright (c) 2010</p>
 * <p>Company: Adeuza</p>
 *
 * @author mmadigand
 *
 */
public class RuntimeExec {
	
	/**
	 * Logger Maven
	 */
	private Log log;
	
	/**
	 * Adjava Group Id
	 */
	private String groupIdAdjava = "com.adeuza";
	
	/**
	 * Adjava Artifact Id
	 */
	private String artifactIdAdjava = "adjava-maven-plugin";
	
	/**
	 * Nom du fichier de sauvegarde des informations pour l'éxécution
	 */
	private static String GENERATION_HISTORY_FILE_NAME = "version/generationHistory.xml";
	
	/**
	 * Nom du fichier de sauvegarde des informations pour l'éxécution
	 */
	private static String LAST_GENERATION_FILE_NAME = "version/lastGeneration.xml";
	
	/**
	 * Nom SNAPSHOT
	 */
	private static String SNAPSHOT = "SNAPSHOT";	

	/**
	 * Nom du paramètre de configuration xmiFile
	 */
	private static String ARTIFACT_ID_XMI_FILE = "xmiFile";	
	
	/** 
	 * The POM from which information will be extracted to create a DOAP file. 
	 */ 
	private MavenProject project;
	
	/** 
	 * Artifact factory. 
	 */ 
	private ArtifactFactory artifactFactory;

	/** 
	 * The local repository where the artifacts are located. 
	 */ 
	private ArtifactRepository localRepository;

	/** 
	 * Used to resolve artifacts. 
	 */ 
	private RepositoryMetadataManager repositoryMetadataManager;

	/** 
	 * The remote repositories where the artifacts are located. 
	 */ 
	private List remoteRepositories;	
	
	/**
	 * targetDirectory 
	 */
	private File targetDirectory;	
	
	/**
	 * executionId
	 */
	private String executionId;
	
	/**
	 * forceGenerate
	 */
	private Boolean forceGenerate ;
	
	/**
	 * xmiFile
	 */
	private File xmiFile;
	
	/**
	 * Constructeur
	 */
	public RuntimeExec(Log p_oLog, MavenProject p_oProject, ArtifactFactory p_oArtifactFactory, ArtifactRepository p_oLocalRepository, 
			RepositoryMetadataManager p_oRepositoryMetadataManager, List p_listRemoteRepositoriesFile, File p_oTargetDirectory, String p_sExecutionId, Boolean p_oForceGenerate, 
			File p_oXmiFile) {
		super();
		this.log = p_oLog;
		this.project = p_oProject;
		this.artifactFactory = p_oArtifactFactory;
		this.localRepository = p_oLocalRepository;
		this.repositoryMetadataManager = p_oRepositoryMetadataManager;
		this.remoteRepositories = p_listRemoteRepositoriesFile;
		this.targetDirectory = p_oTargetDirectory;
		this.executionId = p_sExecutionId;
		this.forceGenerate = p_oForceGenerate;
		this.xmiFile = p_oXmiFile;
	}
	
	
	/**
	 * Lit le fichier d'information sur les précédentes générations
	 * 
	 * @return l'objet GenerationHistory
	 * 
	 * @throws Exception déclenchée lors de la lecture du fichier xml
	 */
	public GenerationHistory readGenerationHistoryFile() throws Exception {
		File oFile = new File(RuntimeExec.GENERATION_HISTORY_FILE_NAME);
		
		GenerationHistory r_oGenerationHistory = null;
		// try on filesystem
		
		if (oFile.exists()) {
			r_oGenerationHistory = JaxbUtils.unmarshal(GenerationHistory.class, oFile);
		} else{
			r_oGenerationHistory =new GenerationHistory();
		}
		GenerationHistoryHolder.getInstance();
		GenerationHistoryHolder.generationHistory = r_oGenerationHistory;
		return r_oGenerationHistory;
	}	
	
	/**
	 * Lit le fichier d'information sur la précédente génération.
	 * @return L'objet "GenerationDescriptor" qui décrit la dernière génération
	 */
	public GenerationDescriptor readLastGenerationFile() throws Exception {
		File oFile = new File(RuntimeExec.LAST_GENERATION_FILE_NAME);
		if(!oFile.exists()) {
			throw new MojoExecutionException("Le fichier "+ RuntimeExec.LAST_GENERATION_FILE_NAME +" n'a pas été trouvé.\n" +
					"Ce fichier doit être créé avec le template disponible sur la documentation en ligne.");
		}
		
		GenerationDescriptor r_oGenerationDescriptor = null;
		// try on filesystem
		r_oGenerationDescriptor = JaxbUtils.unmarshal(GenerationDescriptor.class, oFile);
		
		return r_oGenerationDescriptor;
	}
	
	public void createCurrentGenerationFile(GenerationDescriptor p_oGenerationDescriptor) throws Exception {
		File oFile = new File(RuntimeExec.LAST_GENERATION_FILE_NAME);
		
		if ( !oFile.getParentFile().exists()) {
			oFile.getParentFile().setWritable(true, true);
			oFile.getParentFile().mkdirs();
		}

		JaxbUtils.marshal(p_oGenerationDescriptor, oFile);
	}
	

	/**
	 * Sauvegarde les informations de l'éxécution
	 * 
	 * @param p_oGenerationHistory l'objet a sauvegarder
	 * 
	 * @throws Exception déclenchée lors de l'écriture du fichier xml
	 */
	public void createGenerationHistoryFile(GenerationHistory p_oGenerationHistory) throws Exception{
		File oFile = new File(RuntimeExec.GENERATION_HISTORY_FILE_NAME);
		
		if ( !oFile.getParentFile().exists()) {
			oFile.getParentFile().setWritable(true, true);
			oFile.getParentFile().mkdirs();
		}

		JaxbUtils.marshal(p_oGenerationHistory, oFile);
	}
	
	
	/**
	 * Détermine si le plugin AdJava doît être éxécuté sur le projet.
	 * 
	 * @param p_oGenerationHistory l'objet contenant les informations sur la dernière génération
	 * 
	 * @return un booléen indiquant si le plugin AdJava doît être éxécuté sur le projet.
	 * 
	 * @throws Exception déclenchée si une exception survient
	 */
	public boolean doGenerate(GenerationHistory p_oGenerationHistory) throws Exception {
		boolean r_bDoGenerate = false;
		if(this.forceGenerate!=null){
			r_bDoGenerate = this.forceGenerate;
			this.log.info(" ForceGenerate");
		}
		
		Map<String,RuntimeDependency> mapDependanceByArtifactId = this.getDependanceMap(p_oGenerationHistory);
		                                       
		List<Plugin> listPlugin = (List<Plugin>) this.project.getBuildPlugins();
		
		Plugin oAdjavaPlugin = null;
		for (Plugin oPlugin : listPlugin) {
			if(groupIdAdjava.equals(oPlugin.getGroupId()) && artifactIdAdjava.equals(oPlugin.getArtifactId())){
				oAdjavaPlugin = oPlugin;
			}
		}
		if (oAdjavaPlugin == null){
			throw new MojoExecutionException("Echec de la génération Adjava : oAdjavaPlugin == null ");
		}
		
		String sVersion = oAdjavaPlugin.getVersion();
		String sLastUpdated = "0";
		if(oAdjavaPlugin.getVersion().indexOf(RuntimeExec.SNAPSHOT)!=-1){
			Versioning oVersioning = this.getVersioning(oAdjavaPlugin.getGroupId(),oAdjavaPlugin.getArtifactId(),oAdjavaPlugin.getVersion());
			sLastUpdated = oVersioning.getLastUpdated();
		}
		r_bDoGenerate = this.isVersionningUpdated(mapDependanceByArtifactId, oAdjavaPlugin.getArtifactId(), sVersion, sLastUpdated, r_bDoGenerate);
		
		Dependency oDependency = null;

		Iterator<Dependency> oIterator = oAdjavaPlugin.getDependencies().iterator();
		while (oIterator.hasNext()){
			oDependency = oIterator.next();
			sVersion = oDependency.getVersion();
			sLastUpdated = "0";
			if(oDependency.getVersion().indexOf(RuntimeExec.SNAPSHOT)!=-1){
				Versioning oVersioning = this.getVersioning(oDependency.getGroupId(), oDependency.getArtifactId(), oDependency.getVersion());
				sLastUpdated = oVersioning.getLastUpdated();
			}
			r_bDoGenerate = this.isVersionningUpdated(mapDependanceByArtifactId, oDependency.getArtifactId(), sVersion, sLastUpdated, r_bDoGenerate);
		}

		r_bDoGenerate = this.isXmiFileUpdated(mapDependanceByArtifactId, r_bDoGenerate);
		
		this.setDependanceMapToInfoSaveBean(p_oGenerationHistory,mapDependanceByArtifactId);
		
		return r_bDoGenerate;
	}

	/**
	 * Retourne une map contenant les dépendances de la dernière éxécution
	 * 
	 * @param p_oGenerationHistory l'objet contenant les informations sur la dernière génération
	 * 
	 * @return une map contenant les dépendances de la dernière éxécution
	 */
	private Map<String,RuntimeDependency> getDependanceMap(GenerationHistory p_oGenerationHistory){
		Map<String,RuntimeDependency> r_mapDependanceByArtifactId = new HashMap<String,RuntimeDependency>();
		
		if(p_oGenerationHistory.getGenerations().get(0).getExecutionMetadataList().getInfoSaveBeanList()!=null){
			boolean bFound = false;
			Iterator<ExecutionMetadata> oInfoSaveBeanIterator = p_oGenerationHistory.getGenerations().get(0).getExecutionMetadataList().getInfoSaveBeanList().iterator();
			ExecutionMetadata oInfoSaveBean = null;
			while (oInfoSaveBeanIterator.hasNext() && !bFound) {
				oInfoSaveBean = (ExecutionMetadata) oInfoSaveBeanIterator.next();
				if(oInfoSaveBean.getExecutionId().equals(this.executionId)){
					bFound = true;
					if (oInfoSaveBean.getRuntimeDependencys()!=null){
						Iterator<RuntimeDependency> oDependanceIterator = oInfoSaveBean.getRuntimeDependencys().iterator();
						while (oDependanceIterator.hasNext()) {
							RuntimeDependency oDependance = (RuntimeDependency) oDependanceIterator.next();
							r_mapDependanceByArtifactId.put(oDependance.getArtifactId(), oDependance);
						}
					}
					
				}
			}

		}
		return r_mapDependanceByArtifactId;
	}

	/**
	 * Affecte la map contenant les dépendances d'exécution en cours à l'objet InfoSaveBeanList
	 * 
	 * @param p_oGenerationHistory L'historique des générations
	 * @param p_mapDependanceByArtifactId la map contenant les dépendances de la dernière éxécution
	 */
	private void setDependanceMapToInfoSaveBean(GenerationHistory p_oGenerationHistory, Map<String,RuntimeDependency> p_mapDependanceByArtifactId){
		List<RuntimeDependency> listDependance = new ArrayList<RuntimeDependency>();
		for (RuntimeDependency oDependance : p_mapDependanceByArtifactId.values()) {
			listDependance.add(oDependance);
		}
		ExecutionMetadata oInfoSaveBean = new ExecutionMetadata();
		oInfoSaveBean.setExecutionId(this.executionId);
		oInfoSaveBean.setRuntimeDependencys(listDependance);
		
		GenerationDescriptor oGenerationDescriptor = new GenerationDescriptor();
		oGenerationDescriptor.getExecutionMetadataList().getInfoSaveBeanList().add(oInfoSaveBean);
		p_oGenerationHistory.getGenerations().add(0, oGenerationDescriptor);
	}
	
	/**
	 * Retourne l'objet version SNAPSHOT du repository si celle ci existe
	 * 
	 * @param p_sGroupId le group-id
	 * @param p_sArtifactId l'artifact-id
	 * @param p_sVersion la version 
	 * 
	 * @return l'objet version SNAPSHOT du repository si celle ci existe
	 * 
	 * @throws Exception déclenchée si la version n'existe pas
	 */
	private Versioning getVersioning(String p_sGroupId, String p_sArtifactId, String p_sVersion) throws Exception {
		Artifact oArtifact = artifactFactory.createDependencyArtifact(p_sGroupId, p_sArtifactId,
				VersionRange.createFromVersionSpec(p_sVersion), "jar", null, null);

		RepositoryMetadata oRepositoryMetadata = new ArtifactRepositoryMetadata(oArtifact);
		repositoryMetadataManager.resolve(oRepositoryMetadata, remoteRepositories, localRepository ); 

		Versioning r_oVersioning = oRepositoryMetadata.getMetadata().getVersioning();
		
		if (r_oVersioning == null) { 
			throw new AdjavaException("No versioning ({},{},{}) was found - ignored writing <release/> tag.", p_sGroupId, p_sArtifactId, p_sVersion); 
		}
		
		boolean bVersionFound = false;
		for (String sVersion : r_oVersioning.getVersions()) {
			if(p_sVersion.equals(sVersion)){
				bVersionFound = true;
				break;
			}
		}
		
		if ( bVersionFound ) {
			List<String> listVersion = new ArrayList<String>();
			listVersion.add(p_sVersion);
			r_oVersioning.setVersions(listVersion);
		} else {
			throw new AdjavaException("No versioning {}/{}/{} was found - ignored writing <release/> tag.", p_sGroupId, p_sArtifactId, p_sVersion); 
		}

		return r_oVersioning;
	}
	
	/**
	 * Indique si le jar a été modifier depuis la dernière éxécution
	 * 
	 * @param p_mapDependanceByArtifactId la map contenant les dépendances de la dernière éxécution
	 * @param p_sArtifactId l'artifact-id
	 * @param p_sVersion la version
	 * @param p_sLastUpdated la date de dernière génération
	 * @param p_bDoGenerate un booléen indiquant si la génération doit s'éxécuter
	 * 
	 * @return un booléen indiquant si le jar a été modifier depuis la dernière éxécution
	 */
	private boolean isVersionningUpdated(Map<String,RuntimeDependency> p_mapDependanceByArtifactId, String p_sArtifactId, String p_sVersion, String p_sLastUpdated, boolean p_bDoGenerate){
		boolean r_bGenerate = p_bDoGenerate;
		RuntimeDependency oDependance = p_mapDependanceByArtifactId.get(p_sArtifactId);
		if(!r_bGenerate && (oDependance == null || !p_sVersion.equals(oDependance.getVersion()) || (p_sVersion.indexOf(RuntimeExec.SNAPSHOT) != -1 && p_sLastUpdated.compareTo(oDependance.getLastUpdated()) != 0 ))){
			r_bGenerate = true;
			this.log.info( StringUtils.join(" Change -> ", p_sArtifactId, ", ", p_sVersion));
		}
		
		oDependance = new RuntimeDependency(p_sArtifactId, p_sVersion, p_sLastUpdated);
		p_mapDependanceByArtifactId.put(p_sArtifactId, oDependance);
		
		return r_bGenerate;
	}
	
	/**
	 * Indique si le fichier xmi de la configuration a été modifier depuis la dernière éxécution
	 * 
	 * @param p_mapDependanceByArtifactId la map contenant les dépendances de la dernière éxécution
	 * @param p_bDoGenerate un booléen indiquant si la génération doit s'éxécuter
	 * 
	 * @return un booléen indiquant si le fichier xmi de la configuration a été modifier depuis la dernière éxécution
	 */
	private boolean isXmiFileUpdated(Map<String,RuntimeDependency> p_mapDependanceByArtifactId, boolean p_bDoGenerate){
		String sLastUpdated = String.valueOf(xmiFile.lastModified());
		
		RuntimeDependency oDependance = p_mapDependanceByArtifactId.get(RuntimeExec.ARTIFACT_ID_XMI_FILE);
		if(!p_bDoGenerate && (oDependance == null || sLastUpdated.compareTo(oDependance.getLastUpdated()) != 0 )){
			p_bDoGenerate = true;
			this.log.info(" Change -> "+RuntimeExec.ARTIFACT_ID_XMI_FILE);
		}
		oDependance = new RuntimeDependency(RuntimeExec.ARTIFACT_ID_XMI_FILE, "Pas de version", sLastUpdated);
		p_mapDependanceByArtifactId.put(RuntimeExec.ARTIFACT_ID_XMI_FILE, oDependance);
	
		return p_bDoGenerate;
	}


	
	
}
