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

import java.util.List;

import org.dom4j.Element;


/// !!!
/// !!! SMA : Attention avant cette classe s'appelait MJoinClass
/// !!!
public class MJoinEntityImpl extends MEntityImpl {

	// relation owner de la join class
	private MAssociationPersistableManyToMany association ;
	private MAssociationPersistableManyToMany oppositeAssociation ; 
	private List<MAttribute> leftKeyAttrs ;
	private List<MAttribute> rightKeyAttrs ;
	
	/**
	 * 
	 */
	public MJoinEntityImpl(
			String p_sName,
			MPackage p_oPackage,
			String p_sUmlName,
			String p_sEntityName,
			MAssociationPersistableManyToMany p_oMAssociationManyToMany, 
			List<MAttribute> p_listLeftKeyAttrs,
			List<MAttribute> p_listRightKeyAttrs ) {
		super(p_sName,p_oPackage,p_sUmlName, p_sEntityName);
		
		this.association = p_oMAssociationManyToMany ;
		this.leftKeyAttrs = p_listLeftKeyAttrs ;
		this.rightKeyAttrs = p_listRightKeyAttrs ; 
		
		for( MAttribute oAttribute : p_listLeftKeyAttrs ) {
			getIdentifier().addElem(oAttribute);
		}
		
		for( MAttribute oAttribute : p_listRightKeyAttrs ) {
			getIdentifier().addElem(oAttribute);
		}
	}	

	/**
	 * @return
	 */
	public List<MAttribute> getLeftKeyAttrs() {
		return leftKeyAttrs;
	}

	/**
	 * @return
	 */
	public List<MAttribute> getRightKeyAttrs() {
		return rightKeyAttrs;
	}

	/**
	 * Accessor to the association
	 * @return association
	 */
	public MAssociationManyToMany getAssociation() {
		return association;
	}

	/**
	 * @param p_oManyToManyAssociation
	 */
	public void setOppositionAssociation(
			MAssociationPersistableManyToMany p_oManyToManyAssociation) {
		this.oppositeAssociation = p_oManyToManyAssociation ;
	}

	/**
	 * Accessor
	 * @return
	 */
	public MAssociationManyToMany getOppositeAssociation() {
		return this.oppositeAssociation;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Element toXml() {
		Element r_xJoinClass = super.toXml();
		r_xJoinClass.addAttribute("join-class", "true");
		
		this.leftAssociationToXml(r_xJoinClass);
		this.rightAssociationToXml(r_xJoinClass);
		
		return r_xJoinClass ;
	}
	/**
	 * Transform in XML the left association
	 * @param r_xJoinClass root element of the description
	 */
	public void leftAssociationToXml(Element r_xJoinClass){
		Element xLeftAsso = r_xJoinClass.addElement("left-association");
		
		this.associationToXml(xLeftAsso, this.association);
		
		for( MAttribute oAttr : this.rightKeyAttrs ) {
			xLeftAsso.addElement("attr").addAttribute("name", oAttr.getName());
		}
	}
	/**
	 * Transform in XML the right association 
	 * @param r_xJoinClass root element of the description
	 */
	public void rightAssociationToXml(Element r_xJoinClass){
		Element xRightAsso = r_xJoinClass.addElement("right-association");
		
		this.associationToXml(xRightAsso, this.oppositeAssociation);
		
		for( MAttribute oAttr : this.leftKeyAttrs ) {
			xRightAsso.addElement("attr").addAttribute("name", oAttr.getName());
		}
	}
	/**
	 * Transform in XML an association : name , method name ,parameter , variables
	 * @param r_xJoinClass root element of the description
	 * @param p_oAssociation many to many association to transform in XML
	 */
	private void associationToXml(Element r_xJoinClass , MAssociationPersistableManyToMany p_oAssociation){
		r_xJoinClass.addElement("name").setText( p_oAssociation.getName());
		r_xJoinClass.addElement("method-name").setText( "save" 
				+ p_oAssociation.getName().substring(0,1).toUpperCase()
				+ p_oAssociation.getName().substring(1));
		r_xJoinClass.addElement("name-for-join-class").setText( p_oAssociation.getNameForJoinClass());
		r_xJoinClass.addElement("parameter-name").setText( p_oAssociation.getParameterName());
		r_xJoinClass.addElement("variable-name").setText( p_oAssociation.getVariableName());
		r_xJoinClass.addElement("variable-list-name").setText( p_oAssociation.getVariableListName());		
		r_xJoinClass.addElement("real-name-for-join-class").setText( p_oAssociation.getRefClass().getEntityName() );
	}
}
