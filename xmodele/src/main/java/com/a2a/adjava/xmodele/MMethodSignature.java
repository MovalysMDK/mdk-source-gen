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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.a2a.adjava.types.ITypeDescription;

/**
 * 
 * <p>Method signature</p>
 *
 * <p>Copyright (c) 2009
 * <p>Company: Adeuza
 *
 * @author lmichenaud
 *
 */
public class MMethodSignature {

	/**
	 * Method name
	 */
	private String name ;
	
	/**
	 * Visibility of method 
	 */
	private String visibility ;
	
	/**
	 * Type of method 
	 */
	private String type ;
	
	/**
	 * Returned type
	 */
	private ITypeDescription returnedType ;
	
	/**
	 * Parameter list 
	 */
	private List<MMethodParameter> parameters ;
	
	/**
	 * Documentation 
	 */
	private String documentation ;
	
	/**
	 * 
	 */
	private List<MAssociationManyToMany> associationManyToMany ;
	
	/**
	 * Options
	 */
	private Map<String, String> options ;
	
	/**
	 * @param p_sName
	 * @param p_sVisibility
	 * @param p_oReturnedType
	 */
	public MMethodSignature( String p_sName, String p_sVisibility, 
			String p_sType, ITypeDescription p_oReturnedType ) {
		this.name = p_sName ;
		this.visibility = p_sVisibility ;
		this.type = p_sType ;
		this.returnedType = p_oReturnedType ;
		this.parameters = new ArrayList<MMethodParameter>();
		this.documentation = "";
		this.associationManyToMany = new ArrayList<MAssociationManyToMany>();
		this.options = new TreeMap<String, String>();
	}
	
	public void addOption(String p_sKey, String p_sValue) {
		this.options.put(p_sKey, p_sValue);
	}
	
	public String getType(){
		return this.type;
	}
	
	/**
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	public void setName(String p_sName){
		this.name = p_sName;
	}
	
	/**
	 * @return
	 */
	public ITypeDescription getReturnedType() {
		return returnedType;
	}
	
	/**
	 * @return
	 */
	public String getVisibility() {
		return visibility;
	}
	
	/**
	 * @param p_oMethodParameter
	 */
	public void addParameter( MMethodParameter p_oMethodParameter ) {
		this.parameters.add( p_oMethodParameter );
	}
	
	/**
	 * @param p_oMethodParameter
	 */
	public void addParameters( List<MMethodParameter> p_listMethodParameters ) {
		this.parameters.addAll( p_listMethodParameters );
	}
	
	/**
	 * @return
	 */
	public List<MMethodParameter> getParameters() {
		return this.parameters ;
	}
	
	/**
	 * Retourne la chaîne documentation
	 * @return Chaîne documentation
	 */
	public String getDocumentation() {
		return this.documentation;
	}

	/**
	 * Affecte la chaîne documentation 
	 * @param p_sDocumentation Chaîne documentation
	 */
	public void setDocumentation(String p_sDocumentation) {
		this.documentation = p_sDocumentation;
	}
	
	/**
	 * @param p_oJoinClass
	 */
	public void setManyToManyAssocations( List<MAssociationManyToMany> p_listAssociationManyToMany ) {
		this.associationManyToMany = p_listAssociationManyToMany ;
	}
	
	/**
	 * @return
	 */
	public Element toXml() {
		Element r_xSignature = DocumentHelper.createElement("method-signature");
		r_xSignature.addAttribute("name", this.name );
		r_xSignature.addAttribute("visibility", this.visibility );
		r_xSignature.addAttribute("type", this.type );
		for( MMethodParameter oMMethodParameter : this.parameters) {
		  r_xSignature.add(oMMethodParameter.toXml());
		  r_xSignature.addElement("javadoc").setText("@param " + oMMethodParameter.getName());
		}
		
		Element xReturnType = r_xSignature.addElement("return-type");
		if ( this.returnedType != null ) {
			xReturnType.addAttribute("name", this.returnedType.getName());
			xReturnType.addAttribute("short-name", this.returnedType.getShortName());
			r_xSignature.addElement("javadoc").setText("@return " + this.returnedType.getShortName());
			
			if ( this.returnedType.getParameterizedElementType().size() == 1 ) {
				xReturnType.addAttribute("contained-type-name", 
					this.returnedType.getParameterizedElementType().get(0).getName());
				xReturnType.addAttribute("contained-type-short-name", 
					this.returnedType.getParameterizedElementType().get(0).getShortName());
			}
			else {
				Element xPz = xReturnType.addElement("parameterized");
				Element x = null;
				int i = 0;
				for(ITypeDescription oType : this.returnedType.getParameterizedElementType()) {
					i++;
					x= xPz.addElement("param");
					x.addAttribute("pos", String.valueOf(i));
					x.addAttribute("type-name", oType.getName());
					x.addAttribute("type-short-name", oType.getShortName());
				}
			}
		}
		else {
			xReturnType.addAttribute("name", "void");
			xReturnType.addAttribute("short-name", "void");
		}
		r_xSignature.addElement("documentation").setText(this.documentation);
		
		Element xJoinTables = r_xSignature.addElement("join-tables");
		for( MAssociationManyToMany oMAssociationManyToMany : this.associationManyToMany ) {
			
			if ( oMAssociationManyToMany instanceof MAssociationPersistableManyToMany) {
				MAssociationPersistableManyToMany oPersisManyToMany = ((MAssociationPersistableManyToMany) oMAssociationManyToMany);
				MJoinEntityImpl oJoinClass = oPersisManyToMany.getJoinClass();

				Element xJoinClass = xJoinTables.addElement("join-table");
				if ( oJoinClass.getTable() != null ) {
					xJoinClass.addElement("name").setText(oJoinClass.getTable().getName());
					
					Element xKeyAttrs = xJoinClass.addElement("key-fields");
					for( MAttribute oAttribute : oPersisManyToMany.getKeyAttrs()) {
						Element xField = oAttribute.getField().toXml();
						xField.addAttribute("attr-name", oAttribute.getName());
						xField.addAttribute("method-crit-name", oAttribute.getMethodCritName());
						xKeyAttrs.add(xField);
					}
				}
				
				Element xInterface = xJoinClass.addElement("interface");
				xInterface.addElement("name").setText( oJoinClass.getMasterInterface().getName());
				
				Element xDao = xJoinClass.addElement("dao");
				xDao.addElement("bean-ref").setText(oJoinClass.getDao().getBeanName());
				xDao.addElement("name").setText(oJoinClass.getDao().getName());
				
				Element xDaoInterface = xJoinClass.addElement("dao-interface");
				xDaoInterface.addElement("bean-ref").setText(oJoinClass.getDao().getMasterInterface().getBeanName());
				xDaoInterface.addElement("name").setText(oJoinClass.getDao().getMasterInterface().getName());

			}
		}
		
		Element xOptions = r_xSignature.addElement("options");
		Element xOption = null; 
		for(Map.Entry<String, String> oEntry : this.options.entrySet()) {
			xOption = xOptions.addElement("option");
			xOption.addAttribute("name", oEntry.getKey());
			xOption.setText(oEntry.getValue());
		}
		
		return r_xSignature ;
	}
}
