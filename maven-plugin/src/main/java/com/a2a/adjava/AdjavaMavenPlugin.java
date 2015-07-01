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

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.repository.metadata.RepositoryMetadataManager;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.impl.StaticLoggerBinder;

import com.a2a.adjava.commons.init.AdjavaInitializer;
import com.a2a.adjava.messages.MessageHandler;
import com.a2a.adjava.runtime.RuntimeExec;
import com.a2a.adjava.utils.ItfVersion;
import com.a2a.adjava.utils.VersionHandler;
import com.a2a.adjava.versions.ExecutionMetadataHandler;
import com.a2a.adjava.versions.GenerationDescriptor;
import com.a2a.adjava.versions.GenerationHistory;

/**
 * Adjava Maven Plugin
 * 
 * ${project.build.sourceDirectory} -> src/main/java 
 * ${project.build.directory} -> target 
 * ${project.build.outputDirectory} -> target/classes 
 * ${project.name} - the project name
 * 
 * @goal generate
 * @requiresProject
 * @requiresDependencyResolution
 */
public class AdjavaMavenPlugin extends AbstractMojo {

	/**
	 * Logger
	 */
	private Logger logger;

	/**
	 * The POM from which information will be extracted to create a DOAP file.
	 * 
	 * @parameter expression="${project}"
	 * @required
	 */
	private MavenProject project;

	/**
	 * Artifact factory.
	 * 
	 * @component
	 * @since 1.0
	 */
	private ArtifactFactory artifactFactory;

	/**
	 * The local repository where the artifacts are located.
	 * 
	 * @parameter expression="${localRepository}"
	 * @required
	 * @readonly
	 * @since 1.0
	 */
	private ArtifactRepository localRepository;

	/**
	 * Used to resolve artifacts.
	 * 
	 * @component
	 * 
	 * @since 1.0
	 */
	private RepositoryMetadataManager repositoryMetadataManager;

	/**
	 * The remote repositories where the artifacts are located.
	 * 
	 * @parameter expression="${project.remoteArtifactRepositories}"
	 * @required
	 * @readonly
	 * @since 1.0
	 */
	private List remoteRepositories;

	/**
	 * configFile
	 * 
	 * @parameter
	 * @required
	 */
	private String[] configFiles;

	/**
	 * umlExclude
	 * 
	 * @parameter
	 */
	private String[] umlExcludes;

	/**
	 * xmiFile
	 * 
	 * @parameter
	 * @required
	 */
	private File xmiFile;

	// /**
	// * sourceDirectory
	// *
	// * @parameter expression="${project.build.sourceDirectory}"
	// */
	// private File sourceDirectory;
	//
	/**
	 * targetDirectory
	 * 
	 * @parameter expression="${project.build.directory}"
	 */
	private File targetDirectory;
	//
	// /**
	// * sqlDirectory
	// *
	// * @parameter expression="${project.build.directory}/adjava/sql"
	// */
	// private File sqlDirectory;
	//
	// /**
	// * sqlDirectory
	// *
	// * @parameter expression="${project.build.directory}/adjava/lib"
	// */
	// private File libDirectory;
	//
	// /**
	// * sqlDirectory
	// *
	// * @parameter expression="${project.build.directory}/adjava/config"
	// */
	// private File configDirectory;
	//
	// /**
	// * assetsDirectory
	// *
	// * @parameter expression="${project.build.directory}/assets"
	// */
	// private File assetsDirectory;

	/**
	 * sqlDirectory
	 * 
	 * @parameter
	 */
	private Boolean debug;

	/**
	 * forceGenerate
	 * 
	 * @parameter
	 */
	private Boolean forceGenerate;

	/**
	 * executionId
	 * 
	 * @parameter
	 * @required
	 */
	private String executionId;

	/**
	 * versionClass
	 * 
	 * @parameter
	 */
	private String versionClass;

	/**
	 * generationVersion
	 * 
	 * @parameter
	 */
	private String generationVersion;

	/**
	 * executionId Allowed values listed in "AdjavaProperty"
	 * 
	 * @parameter
	 */
	private Map<String, String> properties;

	/**
	 * {@inheritDoc}
	 */
	public void execute() throws MojoExecutionException {

		try {
			StaticLoggerBinder.SINGLETON.setLog(getLog());
			logger = LoggerFactory.getLogger(AdjavaMavenPlugin.class);

			logger.info("Adjava Movalys 6.6");

			RuntimeExec oRuntimeExec = new RuntimeExec(this.getLog(),
					this.project, this.artifactFactory, this.localRepository,
					this.repositoryMetadataManager, this.remoteRepositories,
					this.targetDirectory, this.executionId, this.forceGenerate,
					this.xmiFile);


			//Lecture de l'historique des générations
			GenerationHistory oGenerationHistory = oRuntimeExec
					.readGenerationHistoryFile();
			//Lecture de la dernière génération et ajout à l'historique
			GenerationDescriptor oLastGenerationDescriptor = oRuntimeExec
					.readLastGenerationFile();
			oGenerationHistory.getGenerations().add(oLastGenerationDescriptor);

			VersionHandler.setLastMDKGenerationVersion(ExecutionMetadataHandler.getLastGenerationVersion(oLastGenerationDescriptor));
			if (oRuntimeExec.doGenerate(oGenerationHistory)) {
				this.getLog().info(" Running...");
				VersionHandler.setCurrentMDKGenerationVersion(ExecutionMetadataHandler.getLastGenerationVersion(oGenerationHistory.getGenerations().get(0)));

				//récupération du descripteur de la génération courante. On le retire de l'historique et on l'écrit
				//dans le fichier de la dernière génération
				GenerationDescriptor oCurrentGenerationDescriptor = oGenerationHistory.getGenerations().get(0);
				oCurrentGenerationDescriptor.getProjectUpgraderList().getProjectUpgraders().addAll(oLastGenerationDescriptor.getProjectUpgraderList().getProjectUpgraders());
				oGenerationHistory.getGenerations().remove(0);

				this.generate(oGenerationHistory);
				// this.generate(oExecutionMetadataList);
				if (MessageHandler.getInstance().hasErrors()) {
					throw new MojoExecutionException(
							"Errors during Adjava generation");
				}



				oRuntimeExec.createCurrentGenerationFile(oCurrentGenerationDescriptor);
				oRuntimeExec.createGenerationHistoryFile(oGenerationHistory);


				this.getLog().info(" End of running");
			} else {
				this.getLog().info(" No need to run");
			}
		} catch (Exception oException) {
			this.getLog().error("Adjava generation failed.", oException);
			throw new MojoExecutionException("Adjava generation failed.",
					oException);
		}
	}

	/**
	 * Lance la generation des fichiers.
	 * 
	 * @param p_oGenerationHistory
	 *            Un liste de métadonnées sur la génération précédent et en
	 *            cours.
	 * @throws Exception
	 *             declenchee si une erreur survient
	 */
	private void generate(GenerationHistory p_oGenerationHistory)
			throws Exception {

		for (String sFile : this.configFiles) {
			this.getLog().info("   configuration file : " + sFile);
		}
		this.getLog().info("   xmi file : " + this.xmiFile);

		Adjava oAdjava = new Adjava();

		new VersionHandler(this.generationVersion,
				(Class<ItfVersion>) Class.forName(this.versionClass));

		AdjavaInitializer oAdjavaInitializer = new AdjavaInitializer();
		oAdjavaInitializer.addConfiguration(this.configFiles);
		oAdjavaInitializer.addConfiguration(this
				.createConfFromMavenProperties());

		oAdjava.run(oAdjavaInitializer, p_oGenerationHistory);
	}

	/**
	 * @param globalProperties
	 */
	public Element createConfFromMavenProperties() throws Exception {
		Element r_xRootConf = DocumentHelper.createElement("adjava-project");

		if (debug != null) {
			r_xRootConf.addElement("debug").setText(Boolean.toString(debug));
			this.getLog().info("   debug activated : " + this.debug);
		}
		Element xXmi = r_xRootConf.addElement("xmi");
		xXmi.addElement("xmi-file").setText(this.xmiFile.getAbsolutePath());

		// Add UmlExcludes
		if (this.umlExcludes != null) {
			Element xUmlExcludes = r_xRootConf.addElement("umlExcludes");
			for (String sUmlExclude : this.umlExcludes) {
				xUmlExcludes.addElement(sUmlExclude);
			}
		}
		
		if (this.properties != null) {
			Element xProperties = r_xRootConf.addElement("properties");
			for (Entry<String, String> oEntry : this.properties.entrySet()) {
				xProperties.addElement(oEntry.getKey()).setText(
						oEntry.getValue());
			}
		}
		return r_xRootConf;
	}
}
