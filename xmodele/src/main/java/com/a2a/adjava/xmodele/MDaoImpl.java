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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;


/**
 * 
 * <p>Classe représentant un Dao</p>
 *
 * <p>Copyright (c) 2009
 * <p>Company: Adeuza
 *
 * @author mmadigand
 * @author lmichenaud
 *
 */
public class MDaoImpl extends SClass<MDaoInterface, MDaoMethodSignature>{

	/**
	 * Query definition file
	 */
	private String queryDefinitionFile ;
	
	/**
	 * Entity
	 */
	private MEntityImpl mclass ;
	
	/**
	 * Entity interface 
	 */
	private MEntityInterface minterface ;
	
	/**
	 * Bean refs used by Dao
	 */
	private Map<String,MDaoBeanRef> daoBeanRefs ;
	
	/**
	 * Delete cascade 
	 */
	private List<MAssociation> deleteCascade = new ArrayList<MAssociation>();
	
	/**
	 * @param p_sName
	 * @param p_mPackage
	 * @param p_oClass
	 * @param p_oInterface
	 */
	public MDaoImpl(String p_sName, String p_sBeanName, MPackage p_oPackage, MEntityImpl p_oClass, MEntityInterface p_oInterface, String p_sQueryDefinitionFile) {
		super("dao", null, p_sName, p_oPackage);
		this.setBeanName(p_sBeanName);
		this.mclass = p_oClass ;
		this.minterface = p_oInterface ;
		this.daoBeanRefs = new HashMap<String,MDaoBeanRef>();
		this.queryDefinitionFile = p_sQueryDefinitionFile ;
	}
	
	/**
	 * @return
	 */
	public MEntityImpl getMEntityImpl() {
		return this.mclass;
	}

	/**
	 * @param p_oAssociation
	 */
	public void addToDeleteCascade( MAssociation p_oAssociation ) {
		if ( !this.deleteCascade.contains(p_oAssociation)) {
			this.deleteCascade.add( p_oAssociation );
		}
	}
	
	/**
	 * @param p_oDaoBeanRef
	 */
	public void addDaoBeanRef(MDaoBeanRef p_oDaoBeanRef) {
		if ( !this.daoBeanRefs.containsKey(p_oDaoBeanRef.getName())) {
			if ( !p_oDaoBeanRef.getTypeDesc().getShortName().equals(this.getName())) {
				this.daoBeanRefs.put(p_oDaoBeanRef.getName(), p_oDaoBeanRef );
				addImport( p_oDaoBeanRef.getTypeDesc().getName());
			}	
		}
	}

	/**
	 * @return
	 */
	public String getQueryDefinitionFile() {
		return queryDefinitionFile;
	}
	
	/**
	 * @return
	 */
	public List<MAssociation> getDeleteCascade() {
		return deleteCascade;
	}

	@Override
	protected void toXmlInsertBeforeDocumentation(Element p_xElement) {
		super.toXmlInsertBeforeDocumentation(p_xElement);
		for( MDaoBeanRef oMDaoBeanRef : this.daoBeanRefs.values()) {
			p_xElement.add(oMDaoBeanRef.toXml());
		}
		
		if ( this.mclass instanceof MJoinEntityImpl) {
			MJoinEntityImpl oJoinClass = (MJoinEntityImpl) this.mclass ;
			Element xRightClass = oJoinClass.getAssociation().getRefClass().toXml();
			xRightClass.setName("right-class");
			p_xElement.add(xRightClass);
			
			Element xLeftClass = oJoinClass.getAssociation().getOppositeClass().toXml();
			xLeftClass.setName("left-class");
			p_xElement.add(xLeftClass);
			
		}
		
		Element xElem = p_xElement.addElement("delete-cascade");
		for( MAssociation oDeleteCascade: deleteCascade) {
			Element xCascade = xElem.addElement("cascade");
			xCascade.addAttribute("name", oDeleteCascade.getName().toUpperCase());
			xCascade.addElement("entity-interface").setText(oDeleteCascade.getOppositeClass().getMasterInterface().getName());
			xCascade.addElement("entity-name").setText(oDeleteCascade.getOppositeClass().getEntityName());
		}
	}
	
	@Override
	protected void toXmlInsertBeforeImport(Element p_xElement) {
		super.toXmlInsertBeforeImport(p_xElement);
		//pour rester compatible avec les xsl existants
		Element r_xDaoInterface = DocumentHelper.createElement("dao-interface");
		r_xDaoInterface.addElement("name").setText(this.getMasterInterface().getName());
		p_xElement.add(r_xDaoInterface);
		//
		p_xElement.add( mclass.toXml());
		if ( minterface !=null ){
			p_xElement.add( minterface.toXml());
		}
	}
	
	@Override
	protected void toXmlInsertBeforePackage(Element p_xElement) {
		super.toXmlInsertBeforePackage(p_xElement);
		p_xElement.addElement("query-definition-file").setText(this.queryDefinitionFile);
	}
}
