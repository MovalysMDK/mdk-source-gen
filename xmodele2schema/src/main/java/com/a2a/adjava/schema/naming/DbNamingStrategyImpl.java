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
package com.a2a.adjava.schema.naming;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.messages.MessageHandler;
import com.a2a.adjava.schema.Field;
import com.a2a.adjava.schema.Table;

/**
 * Default strategy naming for database objects
 *
 * <p>Copyright (c) 2009
 * <p>Company: Adeuza
 *
 * @author lmichenaud
 *
 */
public class DbNamingStrategyImpl implements DbNamingStrategy {

	/**
	 * Default separator
	 */
	public static final String DEFAULT_SEPARATOR = "_" ;
	
	/**
	 * Field separator 
	 */
	public static final String FIELD_SEPARATOR = StringUtils.EMPTY ;
	
	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(DbNamingStrategyImpl.class);
	
	/**
	 * Singleton instance
	 */
	private static DbNamingStrategyImpl instance = null;

	/**
	 * Table prefix
	 */
	private String tablePrefix ;
	
	/**
	 * Sequence prefix 
	 */
	private String sequencePrefix ;
	
	/**
	 * Index prefix 
	 */
	private String indexPrefix ;
	
	/**
	 * Foreign key prefix 
	 */
	private String fkPrefix ;
	
	/**
	 * Unique prefix 
	 */
	private String uniquePrefix ;
	
	/**
	 * Primary key prefix 
	 */
	private String pkPrefix = null;
	
	/**
	 * Max length of constraint names
	 */
	private int constraintNameMaxLength = -1;

	/**
	 * Max length of identifier
	 */
	private int identifierMaxLength = -1;

	/**
	 * Max length of table names
	 */
	private int tableNameMaxLength = -1;

	/**
	 * Constraint names to check unicity of names
	 */
	private List<String> constraintNames = new ArrayList<String>();

	/**
	 * Constructor
	 */
	public DbNamingStrategyImpl() {
		DbNamingStrategyImpl.instance = this ;
	}

	/**
	 * Return singleton instance
	 * @return singleton instance
	 */
	public static DbNamingStrategy getInstance() {
		return DbNamingStrategyImpl.instance ;
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.schema.naming.DbNamingStrategy#setOptions(java.util.Map)
	 */
	@Override
	public void setOptions( Map<String,String> p_oOptions ) {
		this.tablePrefix = ((String) p_oOptions.get("tablePrefix"));
		this.sequencePrefix = ((String) p_oOptions.get("sequencePrefix"));
		this.indexPrefix = ((String) p_oOptions.get("indexPrefix"));
		this.fkPrefix = ((String) p_oOptions.get("fkPrefix"));
		this.uniquePrefix = ((String) p_oOptions.get("uniquePrefix"));
		this.pkPrefix = ((String) p_oOptions.get("pkPrefix"));
		this.constraintNameMaxLength = Integer.parseInt((String)p_oOptions.get("constraintNameMaxLength"));
		this.identifierMaxLength = Integer.parseInt((String)p_oOptions.get("identifiantMaxLength"));
		this.tableNameMaxLength = Integer.parseInt((String)p_oOptions.get("tableNameMaxLength"));
		if ( log.isDebugEnabled()) {
			log.debug("tablePrefix: {}", this.tablePrefix);
			log.debug("sequencePrefix: {}", this.sequencePrefix);
			log.debug("indexPrefix: {}", this.indexPrefix);
			log.debug("fkPrefix: {}", this.fkPrefix);
			log.debug("uniquePrefix: {}", this.uniquePrefix);
			log.debug("constraintNameMaxLength: {}", this.constraintNameMaxLength);
			log.debug("identifierMaxLength: {}", this.identifierMaxLength);
			log.debug("tableNameMaxLength: {}", this.tableNameMaxLength);
		}
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.schema.naming.DbNamingStrategy#getIdentifierMaxLength()
	 */
	@Override
	public int getIdentifierMaxLength(){
		return this.identifierMaxLength;
	}
	
	/**
	 * Return true if object with name exists
	 * @param p_sName object name
	 * @return true if object with name exists
	 */
	private boolean nameExist(String p_sName){
		return this.constraintNames.contains(p_sName);
	}

	/**
	 * Add a object name.
	 * A increment is added at the end of object name if object name already exists.
	 * @param p_sName name
	 * @param p_lMaxLength max length
	 * @return computed name
	 */
	private String addName(String p_sName, long p_lMaxLength){
		StringBuilder r_sNewName = new StringBuilder(p_sName);
		int iCpt = 1;
		while (this.nameExist(r_sNewName.toString())) {
			MessageHandler.getInstance().addWarning(" Utilisation du compteur pour le nommage: {} de taille: {}", r_sNewName, p_lMaxLength);
			if(r_sNewName.length()==p_lMaxLength
				|| (r_sNewName.length()!=p_lMaxLength && r_sNewName.toString().endsWith(Integer.toString(iCpt)))){
				r_sNewName.delete(r_sNewName.length() - Integer.toString(iCpt).length(), r_sNewName.length());
			}
			r_sNewName.append(iCpt);
			iCpt++;			
		}
		if(iCpt!=1){
			MessageHandler.getInstance().addWarning(" --> Utilisation du nommage : {}", r_sNewName);
		}
		this.constraintNames.add(r_sNewName.toString());
		return r_sNewName.toString();
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.hibernate.DbNamingStrategy#getColumnName(java.lang.String)
	 */
	@Override
	public String getColumnName(String p_sClassName, String p_sAttributeName) {
		return this.getName(p_sAttributeName.toUpperCase(Locale.getDefault()),StringUtils.EMPTY,this.identifierMaxLength, FIELD_SEPARATOR);
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.hibernate.DbNamingStrategy#getTableName(java.lang.String)
	 */
	@Override
	public String getTableName(String p_sClassName) {
		StringBuilder sTableName = new StringBuilder(this.tablePrefix);
		sTableName.append(
				this.getName(p_sClassName.toUpperCase(Locale.getDefault()), 
					StringUtils.EMPTY, this.tableNameMaxLength - this.tablePrefix.length(), DEFAULT_SEPARATOR));
				
		return this.addName(sTableName.toString(),this.tableNameMaxLength);
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.hibernate.DbNamingStrategy#getSequenceName(com.a2a.adjava.modele.MClass, com.a2a.adjava.modele.MAttribute)
	 */
	@Override
	public String getSequenceName(String p_sClassName, String p_sAttrName ) {
		StringBuilder oSeqName = new StringBuilder(this.sequencePrefix);
		oSeqName.append(this.tablePrefix);
		oSeqName.append(
				this.getName(p_sClassName.toUpperCase(Locale.getDefault()), p_sAttrName.toUpperCase(Locale.getDefault()),
						this.identifierMaxLength - this.sequencePrefix.length() - this.tablePrefix.length(), DEFAULT_SEPARATOR));
		return this.addName(oSeqName.toString(),this.identifierMaxLength);
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.schema.naming.DbNamingStrategy#getFKColumnName(java.lang.String, com.a2a.adjava.schema.Field)
	 */
	@Override
	public String getFKColumnName( String p_sAssociationEndName, Field p_oField ) {
		StringBuilder oFKName = new StringBuilder(
				this.getName(p_sAssociationEndName.toUpperCase(Locale.getDefault()), p_oField.getName(),this.identifierMaxLength, StringUtils.EMPTY));
		return this.addName(oFKName.toString(),this.identifierMaxLength);
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.schema.naming.DbNamingStrategy#getFKName(java.lang.String, com.a2a.adjava.schema.Table)
	 */
	@Override
	public String getFKName( String p_sAssociationEndName, Table p_oTableRef ) {
		String[] oTabs = new String[2];
		int iPosition = 0;
		for( Field oField : p_oTableRef.getPrimaryKey().getFields()) {
			oTabs[iPosition] = oField.getName().toUpperCase(Locale.getDefault());
			iPosition++;
		}
		
		StringBuilder oFKName = new StringBuilder(this.fkPrefix);
		oFKName.append(
				this.getName(p_sAssociationEndName.toUpperCase(Locale.getDefault()), oTabs[0], oTabs[1],
						this.constraintNameMaxLength - this.fkPrefix.length(), DEFAULT_SEPARATOR));
		return this.addName(oFKName.toString(),this.constraintNameMaxLength);
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.schema.naming.DbNamingStrategy#getIndexName(com.a2a.adjava.schema.Table, java.lang.String)
	 */
	@Override
	public String getIndexName( Table p_oTable, String p_sAssociationEndName ) {
		StringBuilder oFKName = new StringBuilder(this.indexPrefix);
		oFKName.append(
				this.getName(p_oTable.getName().substring(this.tablePrefix.length()), 
						p_sAssociationEndName.toUpperCase(Locale.getDefault()),
						this.constraintNameMaxLength - this.indexPrefix.length(), DEFAULT_SEPARATOR ));
		return this.addName(oFKName.toString(),this.constraintNameMaxLength);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getPKNameFromTableName(String p_sTableName) {
		StringBuilder oPKName = new StringBuilder(this.pkPrefix);
		oPKName.append(this.getName(
				p_sTableName.substring(this.tablePrefix.length()),StringUtils.EMPTY,
				this.identifierMaxLength - this.pkPrefix.length(), DEFAULT_SEPARATOR));
		return this.addName(oPKName.toString(),this.identifierMaxLength);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getUniqueKeyName(String p_sTableName, String p_sColumnName) {
		StringBuilder oUKName = new StringBuilder(this.uniquePrefix);
		oUKName.append(this.getName(p_sTableName,p_sColumnName,
				this.constraintNameMaxLength - this.uniquePrefix.length(), DEFAULT_SEPARATOR ));
		return this.addName(oUKName.toString(),this.constraintNameMaxLength);
	}

	/**
	 * Compute a name using two base names.
	 * Computed name length must be inferior to p_iMaxLength
	 * @param p_sName1 base name 1
	 * @param p_sName2 base name 2
	 * @param p_iMaxLength max length
	 * @param p_sSeparator separator
	 * @return computed name
	 */
	protected String getName(String p_sName1, String p_sName2, int p_iMaxLength, String p_sSeparator ) {
		String r_sName;

		String sName1 = p_sName1;
		String sName2 = p_sName2;
		
		if (sName1 == null) {
			if (sName2 == null) {
				r_sName = this.getName(StringUtils.EMPTY,StringUtils.EMPTY,p_iMaxLength, p_sSeparator);
			}else{
				r_sName = this.getName(sName2,StringUtils.EMPTY,p_iMaxLength, p_sSeparator );
			}
		} else if (sName2 == null) {
			r_sName = this.getName(sName1,StringUtils.EMPTY,p_iMaxLength, p_sSeparator);
		} else{
			int iNameMaxLength = p_iMaxLength - ( 1 * p_sSeparator.length());
			if(sName1.isEmpty() || sName2.isEmpty()){
				iNameMaxLength = p_iMaxLength;
			}
			int iNameMiddleMaxLength = iNameMaxLength / 2;
			if (sName1.length() + sName2.length() > iNameMaxLength) {
				int iMarge = iNameMaxLength - ( 2 * iNameMiddleMaxLength );
				if (sName1.length() < iNameMiddleMaxLength) {
					iMarge += iNameMiddleMaxLength - sName1.length();
				}
				if (sName2.length() < iNameMiddleMaxLength) {
					iMarge += iNameMiddleMaxLength - sName2.length();
				}
				//MMA
				if (sName1.length() > iNameMiddleMaxLength && sName2.length() < iNameMiddleMaxLength) {
					//Cas où le p_sName1 est le seul plus grand
					//On donne la marge à p_sName1
					sName1 = sName1.substring(0, iNameMiddleMaxLength + iMarge);
					// p_sName2 reste le même
				}else if (sName2.length() > iNameMiddleMaxLength && sName1.length() < iNameMiddleMaxLength) {
					//Cas où le p_sName2 est le seul plus grand
					//On donne la marge à p_sName2
					sName2 = sName2.substring(0, iNameMiddleMaxLength + iMarge);
				}else{
					//Cas ou les deux sont trop grand.
					//La marge doit être égale à 0
					sName1 = sName1.substring(0, iNameMiddleMaxLength);
					sName2 = sName2.substring(0, iNameMiddleMaxLength);
				}
			}
			if(sName1.isEmpty()){
				r_sName = sName2;
			}else{
				r_sName = sName1;
				if (!sName2.isEmpty()) {
					r_sName = StringUtils.join(r_sName, p_sSeparator, sName2);			
				}
			}
		}
		return r_sName;
	}

	/**
	 * Compute an object name using 3 base name.
	 * Computed name length must be inferior to max lenth.
	 * @param p_sName1 base name 1
	 * @param p_sName2 base name 2
	 * @param p_sName3 base name 3
	 * @param p_iMaxLength max length
	 * @param p_sSeparator separator
	 * @return computed name
	 */
	private String getName(String p_sName1, String p_sName2, String p_sName3, int p_iMaxLength, String p_sSeparator ) {
		String r_sName = null;

		String sName1 = p_sName1 ;
		String sName2 = p_sName2 ;
		String sName3 = p_sName3 ;
		
		if ( StringUtils.isEmpty(sName1)) {
			r_sName = this.getName(sName2, sName3,p_iMaxLength, p_sSeparator );
		} else if (sName2 == null || sName2!= null && sName2.length()==0) {
			r_sName = this.getName(sName1, sName3,p_iMaxLength, p_sSeparator);
		} else if ( StringUtils.isEmpty(sName3)) {
			r_sName = this.getName(sName1, sName2,p_iMaxLength, p_sSeparator);
		} else  {
			// -2 pour les "_" apres
			int iNameMaxLength = p_iMaxLength - ( 2 * p_sSeparator.length());
			int iNameThirdMaxLength = iNameMaxLength / 3;

			String sName12 = null;
			String sName23 = null;
			if (sName1.length() + sName2.length() + sName3.length() > iNameMaxLength) {
				int iMarge = iNameMaxLength - (3 * iNameThirdMaxLength);
				if (sName1.length() < iNameThirdMaxLength) {
					iMarge = iNameThirdMaxLength - sName1.length();
				}

				if (sName2.length() < iNameThirdMaxLength) {
					iMarge += iNameThirdMaxLength - sName2.length();
				}

				if (sName3.length() < iNameThirdMaxLength) {
					iMarge += iNameThirdMaxLength - sName3.length();
				}

				//MMA
				if (sName1.length() > iNameThirdMaxLength) {
					//Cas où p_sName1 est plus grand
					if (sName2.length() > iNameThirdMaxLength) {
						//Cas où p_sName1 et p_sName2 sont plus grand
						if (sName3.length() > iNameThirdMaxLength) {
							//Cas où p_sName1, p_sName2 et p_sName3 sont plus grand
							sName1 = sName1.substring(0, iNameThirdMaxLength+iMarge);
							sName2 = sName2.substring(0, iNameThirdMaxLength);
							sName3 = sName3.substring(0, iNameThirdMaxLength);
						}else{
							//Cas où p_sName1 et p_sName2 sont plus grand
							// p_sName3 est plus petit
							//On donne la marge à p_sName1 et p_sName2
							sName12 =  this.getName(sName1, sName2,p_iMaxLength-sName3.length()-1, p_sSeparator);
							// p_sName3 reste le même
						}
					}else{
						//Cas où p_sName1 est plus grand et p_sName2 est plus petit
						if (sName3.length() > iNameThirdMaxLength) {
							//Cas où p_sName1 et p_sName3 sont plus grand
							// p_sName2 est plus petit
							//On donne la marge à p_sName1 et p_sName3

							int iMargeFor1 = -1;
							int iMargeFor3 = -1;

							if(sName1.length() > iNameThirdMaxLength + iMarge /2){
								//Cas où p_sName1 est plus grand
								if(sName3.length() > iNameThirdMaxLength + iMarge /2){
									//Cas où p_sName1 et p_sName3 sont plus grand
									//On partage la marge équitablement à p_sName1 et p_sName3
									sName1 = sName1.substring(0, iNameThirdMaxLength + iMarge /2);
									sName3 = sName3.substring(0, iNameThirdMaxLength + iMarge /2);
								}else{
									//Cas où p_sName1 est plus grand
									// p_sName3 est plus petit
									//On donne plus de marge à p_sName1 qu'à p_sName3
									iMargeFor3 = (iNameThirdMaxLength + iMarge /2) - sName3.length();
									iMargeFor1 = iMarge - iMargeFor3;
									if(sName1.length() > iNameThirdMaxLength + iMargeFor1){
										sName1 = sName1.substring(0, iNameThirdMaxLength + iMarge /2 + iMargeFor1);
									}
									sName3 = sName3.substring(0, iNameThirdMaxLength + iMarge /2 + iMargeFor3);
								}
							}else{
								//Cas où p_sName1 est plus petit
								if(sName3.length() > iNameThirdMaxLength + iMarge /2){
									//Cas où p_sName1 est plus petit
									// p_sName3 est plus grand
									//On donne plus de marge à p_sName3 qu'à p_sName1
									iMargeFor1 = (iNameThirdMaxLength + iMarge /2) - sName1.length();
									iMargeFor3 = iMarge - iMargeFor1;
									if(sName3.length() > iNameThirdMaxLength + iMargeFor3){
										sName3 = sName3.substring(0, iNameThirdMaxLength + iMarge /2 + iMargeFor3);
									}
									sName1 = sName1.substring(0, iNameThirdMaxLength + iMarge /2 + iMargeFor1);

								}else{
									//Cas où p_sName1 et p_sName3 sont plus petit
									// N'arrive jamais ici
								}
							}
						}else{
							//Cas où p_sName1 est plus grand
							// p_sName2 et p_sName3 sont plus petit
							//On donne la marge à p_sName1
							sName1 = sName1.substring(0, iNameThirdMaxLength + iMarge);
							// p_sName2 et p_sName3 reste les mêmes
						}
					}
				}else {
					//Cas où p_sName1 est plus petit
					if (sName2.length() > iNameThirdMaxLength) {
						//Cas où p_sName1 est plus petit et p_sName2 est plus grand
						if (sName3.length() > iNameThirdMaxLength) {
							//Cas où p_sName1 est plus petit
							// p_sName2 et p_sName3 sont plus grand
							//On donne la marge à p_sName2 et p_sName3
							sName23 =  this.getName(sName2, sName3,p_iMaxLength-sName1.length()-1, p_sSeparator);
						}else{
							//Cas où p_sName1 et p_sName3 sont plus petit
							// p_sName2 est plus grand
							//On donne la marge à p_sName2
							sName2 = sName2.substring(0, iNameThirdMaxLength + iMarge);
							// p_sName1 et p_sName3 reste les mêmes
						}
					}else{
						//Cas où p_sName1 et p_sName2 sont plus petit
						if (sName3.length() > iNameThirdMaxLength) {
							//Cas où p_sName1 et p_sName2 sont plus petit
							// p_sName3 est plus grand
							//On donne la marge à p_sName3
							sName3 = sName3.substring(0, iNameThirdMaxLength + iMarge);
							// p_sName1 et p_sName2 reste les mêmes
						}else{
							//Cas où p_sName1, p_sName2 et p_sName3 sont plus petit
							// N'arrive jamais ici
						}
					}
				}
			}

			if(sName12 != null) {
				r_sName = sName12;
				if ( !sName3.isEmpty()) {
					r_sName = StringUtils.join(r_sName, p_sSeparator, sName3);
				}
			}else{
				r_sName = sName1;
				if(sName23 != null) {
					r_sName = StringUtils.join(r_sName, p_sSeparator, sName23);
				} else {
					if (!sName2.isEmpty()) {
						r_sName = StringUtils.join(r_sName, p_sSeparator, sName2);
					}
					if (!sName3.isEmpty()) {
						r_sName = StringUtils.join(r_sName, p_sSeparator, sName3);
					}
				}
			}
		}
		return r_sName;
	}
}
