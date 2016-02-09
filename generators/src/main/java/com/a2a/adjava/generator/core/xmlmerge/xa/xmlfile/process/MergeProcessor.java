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
package com.a2a.adjava.generator.core.xmlmerge.xa.xmlfile.process;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.Charsets;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.AdjavaException;
import com.a2a.adjava.generator.core.xmlmerge.AbstractXmlMergeGenerator;
import com.a2a.adjava.generator.core.xmlmerge.xa.xmlfile.analyse.Change;
import com.a2a.adjava.generator.core.xmlmerge.xa.xmlfile.analyse.ChangeType;
import com.a2a.adjava.generator.core.xmlmerge.xa.xmlfile.analyse.ChangesResult;
import com.a2a.adjava.generator.core.xmlmerge.xa.xmlfile.model.XAFile;
import com.a2a.adjava.generator.core.xmlmerge.xa.xmlfile.model.XAFiles;
import com.a2a.adjava.generator.core.xmlmerge.xa.xmlfile.model.XANode;

/**
 * Réalise le merge et crée un nouveau fichier xml
 * à partir d'un fichier généré A, d'un fichier modifié A2 et d'un fichier généré B
 * @author smaitre
 *
 */
public class MergeProcessor {

	/** 
	 * Le log à utiliser 
	 */
	private static final Logger LOG = LoggerFactory.getLogger(MergeProcessor.class);

	/**
	 * The name of the conflicts log file
	 */
	public final static String CONFLICTS_LOG_FILE = "merge-conflicts.log";


	/**
	 * Réalise le merge et génère le nouveau fichier
	 * @param p_oOldGeneratedFilePath le nom de l'ancien fichier
	 * @param p_oNewGeneratedFilePath le nom du nouveau fichier
	 * @param p_oOldModifiedFilePath le nom de l'ancien fichier modifié
	 * @param p_oNewMergedFilePath le chemin physique du nouveau fichier à créer (if exists, overwritten)
	 * @throws AdjavaException An AdjavaException
	 */
	public void process(Path p_oOldGeneratedFilePath, Path p_oNewGeneratedFilePath, Path p_oOldModifiedFilePath, Path p_oNewMergedFilePath) throws AdjavaException {
		//LOG.debug("[MergeProcessor#process]  p_sXAFileNameOldGen = "+oldGeneratedFile);
		//LOG.debug("[MergeProcessor#process]  p_sXAFileNameNewGen = "+newGeneratedFile);
		//LOG.debug("[MergeProcessor#process]  p_sXAFileNameMod = "+oldModifiedFilePath);
		//LOG.debug("[MergeProcessor#process]  p_sFileNameNewMod = "+newMergedFilePath); //écrasé

		XAFile newGeneratedXAFile = XAFiles.getInstance().getNewGenFile(p_oNewGeneratedFilePath);
		XAFile oldGeneratedXAFile = XAFiles.getInstance().getOldGenFile(p_oOldGeneratedFilePath);
		XAFile oldModifiedXAFile = XAFiles.getInstance().getModFile(p_oOldModifiedFilePath);

		if(newGeneratedXAFile == null || oldGeneratedXAFile == null || oldModifiedXAFile == null) {
			throw new AdjavaException("The parsing (based on a configuration file) of one of the input XML files (old generated, new generated or old modified) has failed. See previous warnings.");
		}

		LOG.debug("[MergeProcessor#process] changes between old generated and new generated...");
		ChangesResult changesBetweenOldGenAndNewGen= newGeneratedXAFile.findChanges(oldGeneratedXAFile);


		LOG.debug("[MergeProcessor#process] changes between old generated and modified...");
		ChangesResult changesBetweenOldGenAndMod = oldModifiedXAFile.findChanges(oldGeneratedXAFile);

		ChangesResult changesBetweenModAndNewGen = this.mergeChanges(changesBetweenOldGenAndNewGen, changesBetweenOldGenAndMod,p_oOldGeneratedFilePath, p_oNewGeneratedFilePath, p_oOldModifiedFilePath, p_oNewMergedFilePath);

		if(changesBetweenModAndNewGen.getChanges().size() > 0 
				|| changesBetweenOldGenAndMod.getChanges().size() > 0 
				||changesBetweenOldGenAndNewGen.getChanges().size() > 0 ) {
			LOG.debug("************************************************************************************************\n");
			LOG.debug("[MergeProcessor#process] FILEPATH : \n "+p_oOldGeneratedFilePath.toString());
			LOG.debug("***********************\n");
			LOG.debug("[MergeProcessor#process] MERGED CHANGES BETWEEN OLDGEN/NEWGEN : \n "+changesBetweenOldGenAndNewGen.toString()+"\n\n");
			LOG.debug("***********************\n");
			LOG.debug("[MergeProcessor#process] MERGED CHANGES BETWEEN OLDGEN/MOD : \n "+changesBetweenOldGenAndMod.toString()+"\n\n");
			LOG.debug("***********************\n");
			LOG.debug("[MergeProcessor#process] MERGED CHANGES BETWEEN MOD/NEWGEN : \n "+changesBetweenModAndNewGen.toString()+"\n\n");
			LOG.debug("************************************************************************************************\n");

			//			EASY DEBUG
//			if(p_oNewGeneratedFilePath.toAbsolutePath().toString().contains("section-DealsPanel")) {
//				System.out.println("************************************************************************************************\n");
//				System.out.println("[MergeProcessor#process] FILEPATH : \n "+p_oOldGeneratedFilePath.toString());
//				System.out.println("***********************\n");
//				System.out.println("[MergeProcessor#process] MERGED CHANGES BETWEEN OLDGEN/NEWGEN : \n "+changesBetweenOldGenAndNewGen.toString()+"\n\n");
//				System.out.println("***********************\n");
//				System.out.println("[MergeProcessor#process] MERGED CHANGES BETWEEN OLDGEN/MOD : \n "+changesBetweenOldGenAndMod.toString()+"\n\n");
//				System.out.println("***********************\n");
//				System.out.println("[MergeProcessor#process] MERGED CHANGES BETWEEN MOD/NEWGEN : \n "+changesBetweenModAndNewGen.toString()+"\n\n");
//				System.out.println("************************************************************************************************\n");
//			}

		}
		SAXReader oldModifiedReader = new SAXReader();
		File oldModifiedFile = p_oOldModifiedFilePath.toFile();
		File newMergedFile = p_oNewMergedFilePath.toFile();
		try {
			if (!newMergedFile.exists()) {
				newMergedFile.createNewFile();
			}
			Document oldModifiedDoc = oldModifiedReader.read(oldModifiedFile);
			Document oldModifiedDoc2 = oldModifiedReader.read(oldModifiedFile);

			//ajout des modifications sur le fichier existant
			this.applyChanges(changesBetweenModAndNewGen, oldModifiedDoc, oldModifiedDoc2);

			FileOutputStream newMergedOutputStream = new FileOutputStream(newMergedFile);
			OutputFormat prettyPrintFormat = OutputFormat.createPrettyPrint();
			prettyPrintFormat.setExpandEmptyElements(false);
			prettyPrintFormat.setTrimText(true);
			prettyPrintFormat.setIndent(true);
			prettyPrintFormat.setSuppressDeclaration(!oldModifiedXAFile.hasXmlHeader());
			XMLWriter newMergedXMLWriter = new XMLWriter(newMergedOutputStream, prettyPrintFormat);


			try {
				newMergedXMLWriter.write(oldModifiedDoc);
				newMergedXMLWriter.flush();
			}
			catch (IOException e) {
				e.printStackTrace();
				throw new AdjavaException("Error while writing the XML merged file",e);
			}
			finally {
				newMergedXMLWriter.close();
			}
		} catch (DocumentException | IOException e) {
			throw new AdjavaException("Error while reading XML files to merge them",e);
		}
	}

	/**
	 * Merge les différences entre 2 comparaisons de fichiers
	 * @param p_oChangesBetweenOldGenAndNewGen la comparaison entre les 2 premiers fichiers
	 * @param p_oChangesBetweenOldGenAndMod la comparaison entre les 2 fichiers suivants
	 * @param p_oOldGeneratedFilePath Le chemin du fichier de l'ancienne génération
	 * @param p_oNewGeneratedFilePath Le chemin du fichier de la nouvelle génération
	 * @param p_oOldModifiedFilePath Le chemin du fichier modifié par l'utilisateur avant le merge
	 * @param p_oNewMergedFilePath Le chemin du fichier de sortie après le merge
	 * @return Le merge des deux comparaisons
	 * @throws AdjavaException
	 */
	private ChangesResult mergeChanges(ChangesResult p_oChangesBetweenOldGenAndNewGen, ChangesResult p_oChangesBetweenOldGenAndMod, Path p_oOldGeneratedFilePath, Path p_oNewGeneratedFilePath, Path p_oOldModifiedFilePath, Path p_oNewMergedFilePath) throws AdjavaException {
		//Eventuellement à modifier pour mettre en place des règles de priorités

		//Ce qui nous intéresse, ce sont les modification communes entre la nouvelle génération et la modification manuelle
		// les modifications manuelles ne sont pas perdu car la génération part de cette version manuelle

		LOG.debug("[MergeProcessor#mergeChanges] ( old gen <> new gen = "+p_oChangesBetweenOldGenAndNewGen.getChanges().size()+" changes) / (old gen <> mod = "+p_oChangesBetweenOldGenAndMod.getChanges().size()+" changes)");

		List<Change> oRemovedChanges = new ArrayList<>();

		ChangesResult oResult = new ChangesResult(null, null);
		XANode oNode = null;

		for(Change oChange : p_oChangesBetweenOldGenAndNewGen.getChanges()) {

			oNode = oChange.getOldNode();
			if (oNode == null) {
				oNode = oChange.getNewNode(); //en théorie juste pour les ajouts
			}
			if (oNode.isIdentifiableWithoutPosition()) {
				//conflit possible
				Change oConflictChange = p_oChangesBetweenOldGenAndMod.getChanges(oChange.getSig());

				//évaluation du conflit
				oConflictChange = evaluateConflict(oChange, oConflictChange);

				if (oConflictChange == null) {					
					XANode oCurrentNode = oNode;
					List<Change> changeOnSameOrParentXPath = p_oChangesBetweenOldGenAndMod.getChangesByXPath(oCurrentNode.getUniqueXPathId());
					while(oCurrentNode != null && (changeOnSameOrParentXPath == null || changeOnSameOrParentXPath.isEmpty())) {
						oCurrentNode = oCurrentNode.getParent();
						if(oCurrentNode != null) {
							changeOnSameOrParentXPath = p_oChangesBetweenOldGenAndMod.getChangesByXPath(oCurrentNode.getUniqueXPathId());
						}
					}
					boolean keepChange = true;
					if(changeOnSameOrParentXPath != null) {
						for(Change oUserChange : changeOnSameOrParentXPath) {
							switch(oUserChange.getType()) {
							case REMOVE_NODE :
								// le générateur fait des modifications sur un noeud supprimé par l'utilisateur
								// l'utilisateur "a raison" on ne conserve pas le changement
								// ATTENTION : si les deux changements sont du même type, il faut en revanche le conserver
								if(!oUserChange.isSameTypeOf(oChange)) {
									keepChange = false;
									oRemovedChanges.add(oUserChange);
								}
								break;
							case ADD_NODE:
							case ADD_ATTRIBUTE :
							case MODIFY_ATTRIBUTE_VALUE :
							case MODIFY_NODE_VALUE :
							case MOVE_NODE :
							case REMOVE_ATTRIBUTE :
								break;
							default:
								break;
							}
							if (!keepChange) {
								break;
							}
						}
					}
					if (keepChange) {
						oResult.addChange(oChange);
					}
				}
				else {
					logConflict(oConflictChange, "The change between the old generation and the new generation is in conflict with a change of the same type done by the user");
				}
			}
			else {
				//pas de conflit possible
				oResult.addChange(oChange);
			}
		}

		//Certains changements on été appliqué sur un noeud qui a en fait été supprimé
		//par l'utilisateur. On nettoie alors ces changements sur des noeuds dont l'un des parents a été supprimé.
		oResult = this.clearObsoleteChanges(oResult, oRemovedChanges);
		LOG.debug("\n\n\n\n\n???????????????? ");
		LOG.debug("[DELETED REMOVE_CHANGES] Taille : "+oRemovedChanges.size()+"\n"+oRemovedChanges);
		LOG.debug("????????????????\n\n\n\n\n");

		LOG.debug("[MergeProcessor#mergeChanges]   "+oResult.getChanges().size()+" changes to apply on mod");

		return oResult;
	}


	/**
	 * Cette méthode nettoie la liste des changements.
	 * En effet, des noeuds ont pu être supprimé par l'utilisateur. Dans ce cas on n'ajoute pas le changement "REMOVE_NODE" correspondant
	 * car on considère que l'utilisateur a raison (dans le cas de la suppression). En revanche tous les noeuds plus profonds doivent également
	 * être supprimés quelques soient leur type. C'est ce que fait cette méthode.
	 * @param p_oResult La liste des changements à appliquer, avant le nettoyage
	 * @param p_oRemovedChanges La liste des changements correspondant au noeuds supprimés par l'utilisateur
	 * @return La liste des changements nettoyée.
	 */
	private ChangesResult clearObsoleteChanges(ChangesResult p_oResult, List<Change> p_oRemovedChanges) {
		List<Change> oFullChangesList = new ArrayList<>();
		List<Change> oDeletedChanges = new ArrayList<>();

		//Clone pour ne pas avoir d'accès concurrents
		for(Change oCurrentChange : p_oResult.getChanges()) {
			oFullChangesList.add(oCurrentChange);
		}

		//On supprime les changements dont la profondeur est plus important que le changement supprimé.
		for(Change oCurrentChange : oFullChangesList) {
			for(Change oRemovedChange : p_oRemovedChanges) {
				if(oCurrentChange.getOldNode() != null && oCurrentChange.getOldNode().isDeeper(oRemovedChange.getOldNode())) {
					p_oResult.removeChange(oCurrentChange);
					oDeletedChanges.add(oCurrentChange);
				}
				else if(oCurrentChange.getNewNode() != null && oCurrentChange.getNewNode().isDeeper(oRemovedChange.getOldNode())) {
					p_oResult.removeChange(oCurrentChange);
					oDeletedChanges.add(oCurrentChange);
				}
			}
		}
		LOG.debug("CLEARED CHANGES : \n"+oDeletedChanges);
		return p_oResult;
	}


	/**
	 * Permet de loguer un conflit
	 * @param p_oConflictChange Le changement à loguer
	 * @param p_sMessagePortion Un message à loguer
	 */
	public static void logConflict(Change p_oConflictChange, String p_sMessagePortion) {

		String message="\n\n******************** CONFLICT FOUND *********************";
		if(p_sMessagePortion!=null) message+= "\n"+p_sMessagePortion;
		message+= "\n ==> the change between the 2 generations is skipped while the change done by the user is kept.";
		message+="\n\n\t- Generation time: "+AbstractXmlMergeGenerator.generationDate;
		message+="\n\t- Type of change: "+p_oConflictChange.getType();

		if(p_oConflictChange.getOldNode()!=null) {
			message+="\n\t- Old node: " + p_oConflictChange.getOldNode().getUniqueXPathId();
		}
		if(p_oConflictChange.getNewNode()!=null) {
			message+="\n\t- New node: " + p_oConflictChange.getNewNode().getUniqueXPathId();
		}
		if(p_oConflictChange.getOldAttribute()!=null) {
			message+="\n\t- Old attribute: " + p_oConflictChange.getOldAttribute().getName()+"="+p_oConflictChange.getOldAttribute().getValue();
		}
		if(p_oConflictChange.getNewAttribute()!=null) {
			message+="\n\t- New attribute: " + p_oConflictChange.getNewAttribute().getName()+"="+p_oConflictChange.getNewAttribute().getValue();
		}

		message+="\n\t- Old file: "+p_oConflictChange.getOldFilePath();
		message+="\n\t- New file: "+p_oConflictChange.getNewFilePath();

		LOG.warn("[MergeProcessor#logConflict] "+message);

		OutputStreamWriter oOutputStreamWriter = null;
		try {
			oOutputStreamWriter = new OutputStreamWriter(new FileOutputStream(CONFLICTS_LOG_FILE),Charsets.UTF_8.name());
			try {
				oOutputStreamWriter.append(message);
			} catch (IOException e) {
				LOG.debug("[MergeProcessor#logConflict] I/O ERROR : "+ CONFLICTS_LOG_FILE + "\n EXCEPTION : " + e.getMessage());
			}
		} catch (UnsupportedEncodingException e1) {
			LOG.debug("[MergeProcessor#logConflict] UNSUPPORTED ENCODING : "+ CONFLICTS_LOG_FILE);
		} catch (FileNotFoundException e1) {
			LOG.debug("[MergeProcessor#logConflict] FILE NOT FOUND : "+ CONFLICTS_LOG_FILE);
		}  finally {
			if(oOutputStreamWriter != null) {
				try {
					oOutputStreamWriter.close();
				} catch (IOException e) {
					LOG.debug("[MergeProcessor#logConflict] I/O ERROR : "+ CONFLICTS_LOG_FILE + "\n EXCEPTION : " + e.getMessage());
				}
			}
		}
	}

	/**
	 * Traitement des modifications
	 * @param p_oChangesBetweenNewGenAndMod l'ensemble des modifications à apporter
	 * @param p_oDoc le document dom4j sur lequel apporter les modifications
	 * @param p_oDoc2 Le document original
	 * @throws AdjavaException  Un exception Adjava qui peut être levée lors du merge
	 */
	private void applyChanges(ChangesResult p_oChangesBetweenNewGenAndMod, Document p_oDoc, Document p_oDoc2) throws AdjavaException {

		for(Change oChange: p_oChangesBetweenNewGenAndMod.getChanges()) {
			oChange.getType().getAssociatedClass().simulateProcessChange(oChange, p_oDoc, p_oDoc2);
		}

		//Traitement des autres modifications
		for(Change oChange: p_oChangesBetweenNewGenAndMod.getChanges()) {
			LOG.debug("[MergeProcessor#applyChanges] *** " +oChange.getType().getAssociatedClass()+" ***   [ "+ oChange.getOldNode()+ " => " + oChange.getOldNode()+" ]");
			oChange.getType().getAssociatedClass().processChange(oChange, p_oDoc, p_oDoc2);
		}	
	}

	/**
	 * Permet d'évaluer un conflit. 
	 * Exemple, s'il y a conflit sur le même noeud mais que le conflit est le même (deux fois REMOVE par exemple),
	 * on ne le considère pas comme un conflit
	 * @param p_oOriginalChange Le changement original
	 * @param p_oConflictChange Le changement en conflit
	 * @return Un changement en conflit si le conflit est bien réel, null sinon
	 */
	private Change evaluateConflict(Change p_oOriginalChange,Change p_oConflictChange) {
		Change oResultChange = p_oConflictChange;
		if(p_oOriginalChange.isSameTypeOf(p_oConflictChange)) {
			//Si c'est le même changement et qu'il s'agit d'un REMOVE_NODE, ce n'est plus un conflit
			if(p_oOriginalChange.getType().equals(ChangeType.REMOVE_NODE)) {
				oResultChange = null;
			}
			//Traiter d'autres cas ici à l'avenir.
		}
		return oResultChange;
	}

}
