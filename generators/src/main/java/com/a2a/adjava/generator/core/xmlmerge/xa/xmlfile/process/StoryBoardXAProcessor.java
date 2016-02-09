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

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

import com.a2a.adjava.AdjavaProperty;
import com.a2a.adjava.generator.core.GeneratorUtils;
import com.a2a.adjava.xmodele.IDomain;
import com.a2a.adjava.xmodele.XProject;

public class StoryBoardXAProcessor<D extends IDomain<?, ?>> implements XAProcessor<D> {


	private static final Logger log = LoggerFactory.getLogger(StoryBoardXAProcessor.class);

	@Override
	public void process(File oldGeneratedXmlFile, File fileToProcess, XProject<D> p_oProject) throws Exception {

		//récupération du fichier au format Document(XML)
		Document storyBoardXml = parseFileToXml(fileToProcess);

		//récupération de la map des cellules modifiées par l'utilisateur
		HashMap<String, String> tableViewCellIdsMap = fixModifiedCellIds(storyBoardXml);

		//Modification et récupération du fichier avec les ids des cellules modifiées mis à jours
		String fileContent = readAndFixFileContent(fileToProcess, tableViewCellIdsMap);

		String sStoryboardMergeExpertMode = p_oProject.getDomain().getGlobalParameters().get(AdjavaProperty.STORYBOARD_MERGE_EXPERT_MODE.getName());
		boolean isExpertMode = (sStoryboardMergeExpertMode != null) && sStoryboardMergeExpertMode.equalsIgnoreCase("true");

		//récupération du nouveau storyboard corrigé
		Document newStoryboardXml = parseFileToXml(writeFileContent(fileToProcess, fileContent));

		if(!isExpertMode) {
			//récupération de l'ancien storyboard
			Document oldStoryboardXml = parseFileToXml(oldGeneratedXmlFile);

			//Ajout des cellules framework manquantes
			newStoryboardXml = addMissingFrameworkCells(oldStoryboardXml, newStoryboardXml, tableViewCellIdsMap);
		}

		GeneratorUtils.clearEmptyNodes(newStoryboardXml);
		GeneratorUtils.writeXml(newStoryboardXml, fileToProcess, p_oProject);
	}

	/**
	 * Parse un fichier donné et le renvoie au format Document(XML)
	 * @param p_oFileToProcess
	 * @return
	 * @throws Exception
	 */
	private Document parseFileToXml(File p_oFileToProcess) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		builder.setEntityResolver(new EntityResolver() {

			@Override
			public InputSource resolveEntity (String publicId, String systemId)
			{
				return new InputSource(new ByteArrayInputStream(new byte[]{}));

			}
		});
		Document storyBoardXml = null;
		int count = 0;
		Exception exception = null;
		while(storyBoardXml == null && count < 10) {
			log.debug("[StoryBoardXAProcessor#process] FILEPATH : "+ p_oFileToProcess.getAbsolutePath());
			try {
				storyBoardXml = builder.parse(p_oFileToProcess);
			}
			catch (Exception e) {
				exception = e;
				log.debug("[StoryBoardXAProcessor#process] catch parse exception");

			}
			finally {
				count++;
				log.debug("**********");
			}

		}
		if(storyBoardXml == null) {
			throw exception;
		}
		return storyBoardXml;
	}

	/**
	 * Analyse les cellules modifiées par l'utilisateur (correspondance id/reuseIdentifier de la cellule cassée),
	 * et retourne un dictionnaire de paires anciens ids/ids corrigés
	 * @param p_oStoryBoardXml
	 * @return
	 * @throws IOException
	 */
	private HashMap<String, String> fixModifiedCellIds(Document p_oStoryBoardXml) throws IOException {
		/* 
		 * On vérifie si l'utilisateur a modifié une cellule Framework existante. 
		 * Pour cela, on regarde si on a perdu la correspondance entre le resueIdentifier et l'id de la cellule
		 * S'il n'y a plus correspondance, on met à jour l'id de la nouvelle cellule en fonction du nouveau reuseIdentifier.
		 */
		HashMap<String, String> tableViewCellIdsMap = new HashMap<String, String>();
		NodeList tableViewCellList = p_oStoryBoardXml.getElementsByTagName("tableViewCell");
		for(int tableViewCellNodeIndex = 0 ; tableViewCellNodeIndex < tableViewCellList.getLength(); tableViewCellNodeIndex++) {
			Node tableViewCellNode = tableViewCellList.item(tableViewCellNodeIndex);
			if(tableViewCellNode instanceof Element) {
				Element tableViewCellElement = (Element)tableViewCellNode;
				String currentTableViewCellId = tableViewCellElement.getAttribute("id");

				//Framework cell
				if(currentTableViewCellId.startsWith("FWK")) {
					String currentTableViewCellReuseIdentifier = tableViewCellElement.getAttribute("reuseIdentifier");

					//Cell has been changed by the user.
					if(!currentTableViewCellId.endsWith(currentTableViewCellReuseIdentifier)) {
						String newTableViewCellId = buildNewTableViewCellId(currentTableViewCellId, currentTableViewCellReuseIdentifier);
						tableViewCellIdsMap.put(currentTableViewCellId, newTableViewCellId);
					}
				}
			}
		}
		return tableViewCellIdsMap;
	}

	/**
	 * Corrige le storyboard à écrire avec les nouveaux ids des cellules modifiées par l'utilisateur
	 * @param p_oFileToProcess
	 * @param p_oTableViewCellIdsMap
	 * @return
	 * @throws IOException
	 */
	private String readAndFixFileContent(File p_oFileToProcess, HashMap<String, String> p_oTableViewCellIdsMap) throws IOException {
		StringBuilder newFileContent = new StringBuilder();

		try {
			BufferedReader buffer = new BufferedReader(new FileReader(p_oFileToProcess));

			String line;
			try {
				while((line = buffer.readLine()) != null ) {
					for(Entry<String, String> idsEntry : p_oTableViewCellIdsMap.entrySet()) {
						line = line.replaceAll(idsEntry.getKey(), idsEntry.getValue());
					}
					newFileContent.append(line);
				}
			} catch (IOException e) {
				log.debug("[StoryBoardXAProcessor#process] I/O ERROR : "+ p_oFileToProcess.getAbsolutePath() + "\n EXCEPTION : " + e.getMessage());
			} finally {
				buffer.close();
			}

		} catch (FileNotFoundException  e) {
			log.debug("[StoryBoardXAProcessor#process] FILE NOT FOUND : "+ p_oFileToProcess.getAbsolutePath());
		}
		return newFileContent.toString();
	}


	/**
	 * Ecrit le fichier.
	 * @param p_oFileToProcess
	 * @param p_sFileContent
	 * @return
	 * @throws IOException
	 */
	private File writeFileContent(File p_oFileToProcess, String p_sFileContent) throws IOException {
		try {
			OutputStreamWriter oOutputStreamWriter = new OutputStreamWriter(new FileOutputStream(p_oFileToProcess),Charsets.UTF_8.name());

			try {
				oOutputStreamWriter.write(p_sFileContent);
			} catch (IOException e) {
				log.debug("[StoryBoardXAProcessor#process] I/O ERROR : "+ p_oFileToProcess.getAbsolutePath() + "\n EXCEPTION : " + e.getMessage());
			} finally {
				if(oOutputStreamWriter != null) {
					oOutputStreamWriter.close();
				}
			}

		} catch (FileNotFoundException  e) {
			log.debug("[StoryBoardXAProcessor#process] FILE NOT FOUND : "+ p_oFileToProcess.getAbsolutePath());
		}
		return p_oFileToProcess;
	}



	private String buildNewTableViewCellId(String p_oCurrentId, String p_oCurrentReuseIdentifier) {
		String[] currentTableViewCellIdComponents = p_oCurrentId.split("-");
		StringBuilder newIdBuilder = new StringBuilder();
		newIdBuilder.append(currentTableViewCellIdComponents[0]);
		newIdBuilder.append('-');
		newIdBuilder.append(currentTableViewCellIdComponents[1]);
		newIdBuilder.append('-');
		newIdBuilder.append(p_oCurrentReuseIdentifier);
		return newIdBuilder.toString();
	}

	/**
	 * Ajout les cellules framework dont les ids ont été modifiés par l'utilisateur
	 * @param p_oOldStorybaord
	 * @param p_oNewStoryboard
	 * @param p_oModifiedCellsId
	 * @return
	 */
	private Document addMissingFrameworkCells(Document p_oOldStorybaord, Document p_oNewStoryboard, HashMap<String, String> p_oModifiedCellsId) {
		NodeList oldTableViewCells = p_oOldStorybaord.getElementsByTagName("tableViewCell");
		for(Entry<String, String> modifiedCellId : p_oModifiedCellsId.entrySet()) {
			for(int oldTableViewCellIndex = 0 ; oldTableViewCellIndex < oldTableViewCells.getLength() ; oldTableViewCellIndex++) {
				Node oldTableViewCell = oldTableViewCells.item(oldTableViewCellIndex);
				if(oldTableViewCell instanceof Element) {
					Element oldTableViewCellElement = (Element)oldTableViewCell;
					if(oldTableViewCellElement.getAttribute("id").equals(modifiedCellId.getKey())) {
						insertCellInNewStoryboard(oldTableViewCellElement, p_oNewStoryboard);
					}
				}
			}

		}
		return p_oNewStoryboard;
	}

	/**
	 * Insère une cellule dans le nouveau storyboard
	 * @param p_oCellToInsert
	 * @param p_oNewStoryboard
	 */
	private void insertCellInNewStoryboard(Element p_oCellToInsert,Document p_oNewStoryboard) {
		p_oCellToInsert = (Element) p_oNewStoryboard.importNode(p_oCellToInsert, true);
		String[] cellToInsertIdComponents = p_oCellToInsert.getAttribute("id").split("-");
		NodeList tablesViews = p_oNewStoryboard.getElementsByTagName("tableView");

		Element destTableViewElement = null;
		for(int oldTableViewIndex = 0 ; oldTableViewIndex < tablesViews.getLength() ; oldTableViewIndex++) {
			Node tableView = tablesViews.item(oldTableViewIndex);
			if(tableView instanceof Element) {
				Element tableViewElement =  (Element) tableView;
				if(tableViewElement.getAttribute("id").equals(cellToInsertIdComponents[1]+"-TV")) {
					destTableViewElement = tableViewElement;
					break;
				}
			}
		}

		if(destTableViewElement != null) {
			NodeList prototypesNodes = destTableViewElement.getElementsByTagName("prototypes");
			Node prototypesNode = prototypesNodes.item(0);
			if(prototypesNode instanceof Element) {
				((Element)prototypesNode).appendChild(p_oCellToInsert);
			}
		}
	}
}
