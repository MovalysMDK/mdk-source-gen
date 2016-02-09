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
package com.a2a.adjava.xmodele2schema;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.dom4j.Element;

import com.a2a.adjava.datatypes.DataType;
import com.a2a.adjava.messages.MessageHandler;
import com.a2a.adjava.schema.Field;
import com.a2a.adjava.schema.ForeignKey;
import com.a2a.adjava.schema.Index;
import com.a2a.adjava.schema.Schema;
import com.a2a.adjava.schema.SchemaFactory;
import com.a2a.adjava.schema.Sequence;
import com.a2a.adjava.schema.Table;
import com.a2a.adjava.schema.UniqueConstraint;
import com.a2a.adjava.schema.naming.DbNamingStrategy;
import com.a2a.adjava.types.ITypeDescription;
import com.a2a.adjava.uml.UmlModel;
import com.a2a.adjava.uml2xmodele.extractors.AbstractExtractor;
import com.a2a.adjava.xmodele.IDomain;
import com.a2a.adjava.xmodele.IModelDictionary;
import com.a2a.adjava.xmodele.IModelFactory;
import com.a2a.adjava.xmodele.MAssociation;
import com.a2a.adjava.xmodele.MAssociationManyToMany;
import com.a2a.adjava.xmodele.MAssociationManyToOne;
import com.a2a.adjava.xmodele.MAssociationOneToOne;
import com.a2a.adjava.xmodele.MAssociationWithForeignKey;
import com.a2a.adjava.xmodele.MAttribute;
import com.a2a.adjava.xmodele.MEntityImpl;
import com.a2a.adjava.xmodele.MIdentifierElem;
import com.a2a.adjava.xmodele.MJoinEntityImpl;
import com.a2a.adjava.xmodele.MPackage;
import com.a2a.adjava.xmodele.XModele;

/**
 * 
 * <p>
 * Transforme un modele UML en schemas
 * </p>
 * /** FONCTION : Transforme le modele UML en schemas
 * 
 * Principe :
 * 
 * - Etape 1 : Transformation des classes persistentes en Table
 * 
 * - Etape 2 : Definition des cles primaires
 * 
 * Tant que le nombre de tables a traiter a diminue et > 0 Parcours des tables restantes a traiter Est-ce que la cle primaire de la table peut-etre
 * defini ? Si oui .Definition de la cle primaire a partir des attributs cle primaires .Definition de la cle primaire a partir de l'heritage
 * .Definition de la cle primaire a partir des extremites d'association cle primaire .Si classe d'association, Definition de la cle primaire a partir
 * des extremites Role A et Role B .Une table en moins a traiter FinSi Fin Tant Que
 * 
 * - Etape 3 : Ajout des champs et cles etrangeres restants
 * 
 * S'il reste des tables a traiter Alors => Impossible Sinon .Ajout des champs cles etrangeres a partir des extremites d'association non cle primaire
 * .Ajout des champs a partir des attributs non cle primaires FinSi
 * 
 * - Etape 4 : Contraintes verifies : Chaque table doit avoir une cle primaire.
 * <p>
 * Copyright (c) 2009
 * <p>
 * Company: Adeuza
 * 
 * @author mmadigand
 * @author lmichenaud
 * 
 */
public class SchemaExtractor extends AbstractExtractor<IDomain<IModelDictionary,IModelFactory>> {

	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(SchemaExtractor.class);

	/**
	 * Configuration du schema
	 */
	protected SchemaConfig schemaConfig = new SchemaConfig();

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.a2a.adjava.uml2xmodele.extractors.MExtractor#initialize()
	 */
	@Override
	public void initialize(Element p_xConfig) throws Exception {
		SchemaConfigFactory.loadConfiguration(p_xConfig, schemaConfig);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.a2a.adjava.uml2xmodele.extractors.MExtractor#extract(com.a2a.adjava.uml.UmlModel)
	 */
	@Override
	public void extract(UmlModel p_oUmlModele) throws Exception {

		Schema oSchema = this.schemaConfig.getSchemaFactory().createSchema();

		Map<String, MEntityImpl> mapTableClasses = new HashMap<String, MEntityImpl>();

		// Cree les tables par rapport aux classes
		for (MPackage oChildPackage : getDomain().getModel().getPackages()) {
			extractTables(oSchema, oChildPackage, mapTableClasses);
		}
		log.debug("nombre de tables : " + oSchema.countTables());

		// Calcul les cles primaires
		extractPrimaryKeys(oSchema, mapTableClasses);

		// Calcul les champs des tables
		for (Table oTable : oSchema.getTables()) {
			MEntityImpl oClass = mapTableClasses.get(oTable.getName());
			setFieldsFromAttributs(oTable, oClass);
			setFieldsFromAssociation(oTable, oClass);
			setUniqueConstraints(oTable);
		}

		// Ajout les tables liees au JoinClass
		extractTablesFromJoinClasses(getDomain().getModel(), oSchema, mapTableClasses);

		// Enleve les index qui equivalent a la cle primaire
		for (Table oTable : oSchema.getTables()) {
			deleteDuplicatedIndexes(oTable);
		}

		// Verifications sur les cles primaires
		for (Table oTable : oSchema.getTables()) {
			if (oTable.hasPrimaryKey()) {

				// Warning si une cle primaire est composee de champs de type
				// string ou date
				for (Field oField : oTable.getPrimaryKey().getFields()) {
					if (!oField.getDataType().equals(DataType.NUMERIC)) {
						// Erreur 10112
						MessageHandler.getInstance().addWarning(
								"La cle primaire de la table " + oTable.getName() + " ne devrait etre composee que de champs de type '"
										+ DataType.NUMERIC + "' ( champs : " + oField.getName() + " ).");
					}
				}
			} else {
				// Erreur 10101
				MessageHandler.getInstance().addError("La table " + oTable.getName() + " doit avoir une cle primaire.");
			}
		}
		
		// Reorder table creation 
		this.sortTables( oSchema.getTables());

		getDomain().setSchema(oSchema);
	}

	/**
	 * Convertit les classes persistentes en table
	 * 
	 * @param p_oSchema
	 *			schema a alimenter
	 * @param p_oPackage
	 *			package a traiter
	 * @param p_mapTableClasses
	 * 			map table vers classes 
	 */
	private void extractTables(Schema p_oSchema, MPackage p_oPackage, Map<String, MEntityImpl> p_mapTableClasses) {

		for (MPackage oChildPackage : p_oPackage.getPackages()) {
			this.extractTables(p_oSchema, oChildPackage, p_mapTableClasses);
		}

		// Extrait les tables a partir des classes
		for (MEntityImpl oClass : p_oPackage.getEntitiesImpl()) {
			if ( !oClass.isTransient()) {
				String sTableName = this.schemaConfig.getDbNamingStrategyClass().getTableName(oClass.getUmlName());
				String sPKName = this.schemaConfig.getDbNamingStrategyClass().getPKNameFromTableName(sTableName);
				Table oTable = this.schemaConfig.getSchemaFactory().createTable(sTableName, sPKName, false);
				p_mapTableClasses.put(oTable.getName(), oClass);
				oClass.setTable(oTable);
				p_oSchema.addTable(oTable);
			}
		}
	}

	/**
	 * @param p_oSchema schema
	 * @param p_mapTableClasses mapping table vers entities
	 */
	private void extractPrimaryKeys(Schema p_oSchema, Map<String, MEntityImpl> p_mapTableClasses) {
		log.debug("  > SchemaExtractors.extractPrimaryKeys");
		// Complete les cles primaires
		// Ajoute les cles etrangeres a partir des extremites d'association

		List<Table> listTablesWithoutPK = new ArrayList<Table>(p_oSchema.getTables());
		int iNbTableWithoutPk = listTablesWithoutPK.size();
		int iOldCountTable = -1;

		while (iNbTableWithoutPk > 0 && iNbTableWithoutPk != iOldCountTable) {
			iOldCountTable = iNbTableWithoutPk;

			List<Table> listParcours = new ArrayList<Table>(listTablesWithoutPK);
			for (Table oTableParcours : listParcours) {
				MEntityImpl oClass = p_mapTableClasses.get(oTableParcours.getName());
				if (canPrimaryKeyBeCalculated(oClass, listTablesWithoutPK)) {
					log.debug("primary key of table " + oTableParcours.getName() + " can be calculated");
					setPrimaryKey(oTableParcours, oClass);
					listTablesWithoutPK.remove(oTableParcours);
				}
			}

			// Recalcul le nombre de tables
			iNbTableWithoutPk = listTablesWithoutPK.size();
		}

		if (iNbTableWithoutPk > 0) {
			// Erreur 10100
			MessageHandler.getInstance().addError(" Impossible de definir les cles primaires des tables( circuit ? ).");
		}

		log.debug("  < SchemaExtractors.extractPrimaryKeys");
	}

	/**
	 * Test si la cle primaire peut etre complete
	 * @param p_oClass entity
	 * @param p_listTableWithoutPk tables without primary key
	 */
	private boolean canPrimaryKeyBeCalculated(MEntityImpl p_oClass, List<Table> p_listTableWithoutPk) {
		boolean r_bResult = true;

		// Test dans le cas ou les extremites d'association sont cle primaire
		// de la table
		Iterator<MIdentifierElem> iterIdentifierElems = p_oClass.getIdentifier().getElems().iterator();
		while (iterIdentifierElems.hasNext() && r_bResult) {
			MIdentifierElem oMIdentifierElem = (MIdentifierElem) iterIdentifierElems.next();
			if (oMIdentifierElem instanceof MAssociationWithForeignKey) {
				MAssociationWithForeignKey oMAssociationWithForeignKey = (MAssociationWithForeignKey) oMIdentifierElem;
				Table oRefTable = oMAssociationWithForeignKey.getRefClass().getTable();
				r_bResult = !p_listTableWithoutPk.contains(oRefTable);
			}
		}

		return r_bResult;
	}

	/**
	 * Define primary key on table
	 * @param p_oTable table
	 * @param p_oClass entity
	 */
	private void setPrimaryKey(Table p_oTable, MEntityImpl p_oClass) {

		DbNamingStrategy oDbNamingStrategy = this.schemaConfig.getDbNamingStrategyClass();
		SchemaFactory oSchemaFactory = this.schemaConfig.getSchemaFactory();

		for (MIdentifierElem oMIdentifierElem : p_oClass.getIdentifier().getElems()) {

			if (oMIdentifierElem instanceof MAttribute) {

				MAttribute oAttribute = (MAttribute) oMIdentifierElem;
				String sFieldName = oDbNamingStrategy.getColumnName(p_oClass.getUmlName(), oAttribute.getName());
				String sType = getSqlType(oAttribute);
				boolean bNotNull = oAttribute.isMandatory();
				Field oField = oSchemaFactory.createField(sFieldName, sType, bNotNull, oAttribute);

				if (oAttribute.hasSequence()) {
					String sSequenceName = oDbNamingStrategy.getSequenceName(p_oClass.getUmlName(), oAttribute.getName());
					Sequence oSequence = oSchemaFactory.createSequence(sSequenceName, getSequenceMaxValue(oField));
					oField.setSequence(oSequence);
				}

				oAttribute.setField(oField);
				p_oTable.getPrimaryKey().addField(oField);
				p_oTable.addField(oField);
			} else if (oMIdentifierElem instanceof MAssociationWithForeignKey) {

				MAssociationWithForeignKey oMAssociationWithForeignKey = (MAssociationWithForeignKey) oMIdentifierElem;

				Table oTableDest = oMAssociationWithForeignKey.getRefClass().getTable();
				List<Field> listPkFields = new ArrayList<Field>();

				for (Field oPkField : oTableDest.getPrimaryKey().getFields()) {
					String sFieldName = oDbNamingStrategy.getFKColumnName(oMAssociationWithForeignKey.getName(), oPkField);
					String sSqlType = oPkField.getType();
					boolean bNotNull = true;
					boolean bUniqueKey = false;
					String sUniqueKey = null;
					Field oField = oSchemaFactory.createField(sFieldName, sSqlType, bNotNull, bUniqueKey, sUniqueKey, oPkField);
					p_oTable.addField(oField);
					p_oTable.getPrimaryKey().addField(oField);
					listPkFields.add(oField);
					oMAssociationWithForeignKey.addField(oField);
				}

				// Instancie une cle etrangere
				String sFkName = oDbNamingStrategy.getFKName(oMAssociationWithForeignKey.getName(), oTableDest);
				ForeignKey oForeignKey = oSchemaFactory.createForeignKey(sFkName, listPkFields, oTableDest.getPrimaryKey().getFields(), oTableDest,
						false, oMAssociationWithForeignKey);
				p_oTable.addForeignKey(oForeignKey);

				// Creation de l'index sur la cle etrangere
				Index oIndex = oSchemaFactory.createIndex(oDbNamingStrategy.getIndexName(p_oTable, oMAssociationWithForeignKey.getName()),
						listPkFields, false);
				p_oTable.addIndex(oIndex);
			}
		}
	}

	/**
	 * Convertit les attributs non cle primaire en champs
	 * @param p_oTable table
	 * @param p_oClass entity
	 */
	private void setFieldsFromAttributs(Table p_oTable, MEntityImpl p_oClass) {

		DbNamingStrategy oDbNamingStrategy = this.schemaConfig.getDbNamingStrategyClass();
		SchemaFactory oSchemaFactory = this.schemaConfig.getSchemaFactory();

		for (MAttribute oAttribute : p_oClass.getAttributes()) {
			if ( !oAttribute.isTransient()) {
				if ( oAttribute.isBasic()) {
					String sFieldName = oDbNamingStrategy.getColumnName(p_oClass.getUmlName(), oAttribute.getName());
					String sType = getSqlType(oAttribute);
					boolean bNotNull = oAttribute.isMandatory();
					Field oField = oSchemaFactory.createField(sFieldName, sType, bNotNull, oAttribute);
		
					if (oAttribute.hasSequence()) {
						String sSequenceName = oDbNamingStrategy.getSequenceName(p_oClass.getUmlName(), oAttribute.getName());
						Sequence oSequence = oSchemaFactory.createSequence(sSequenceName, getSequenceMaxValue(oField));
						oField.setSequence(oSequence);
					}
			
					p_oTable.addField(oField);
					oAttribute.setField(oField);
				}
				else {
					for( MAttribute oChildAttr : oAttribute.getProperties()) {
						setFieldsFromAttributProperty( oChildAttr, oAttribute.getName(), p_oClass.getUmlName(), p_oTable );
					}
				}
			}
		}
	}

	/**
	 * Define fields on table from attribute
	 * @param p_oProperty attribute
	 * @param p_sPathName path
	 * @param p_sClassUmlName class uml name
	 * @param p_oTable table
	 */
	private void setFieldsFromAttributProperty(MAttribute p_oProperty, String p_sPathName, String p_sClassUmlName, Table p_oTable ) {
		
		DbNamingStrategy oDbNamingStrategy = this.schemaConfig.getDbNamingStrategyClass();
		SchemaFactory oSchemaFactory = this.schemaConfig.getSchemaFactory();
		
		if ( p_oProperty.isBasic()) {
			String sFieldName = oDbNamingStrategy.getColumnName(p_sClassUmlName, p_sPathName + "_" + p_oProperty.getName());
			String sType = getSqlType(p_oProperty);
			boolean bNotNull = p_oProperty.isMandatory();
			Field oField = oSchemaFactory.createField(sFieldName, sType, bNotNull, p_oProperty);

			if (p_oProperty.hasSequence()) {
				String sSequenceName = oDbNamingStrategy.getSequenceName(p_sClassUmlName, p_oProperty.getName());
				Sequence oSequence = oSchemaFactory.createSequence(sSequenceName, getSequenceMaxValue(oField));
				oField.setSequence(oSequence);
			}
	
			p_oTable.addField(oField);
			p_oProperty.setField(oField);
		}
		for( MAttribute oChildAttr : p_oProperty.getProperties()) {
			this.setFieldsFromAttributProperty( oChildAttr, p_sPathName + "_" + p_oProperty.getName(),
					p_sClassUmlName, p_oTable);
		}
	}

	/**
	 * definit les champs a partir des extremites d'association non cle primaires
	 * @param p_oTable table
	 * @param p_oClass entity
	 */
	private void setFieldsFromAssociation(Table p_oTable, MEntityImpl p_oClass) {

		DbNamingStrategy oDbNamingStrategy = this.schemaConfig.getDbNamingStrategyClass();
		SchemaFactory oSchemaFactory = this.schemaConfig.getSchemaFactory();

		for (MAssociation oAssociation : p_oClass.getAssociations()) {

			if ( !oAssociation.isTransient() && !oAssociation.getRefClass().isTransient()) {
			
				boolean bNeedForeignKey = false;
				boolean bIsNotNull = false;
				if (oAssociation instanceof MAssociationManyToOne) {
					MAssociationManyToOne oMAssociationManyToOne = (MAssociationManyToOne) oAssociation;
					bNeedForeignKey = true;
					bIsNotNull = oMAssociationManyToOne.isNotNull();
				} else if (oAssociation instanceof MAssociationOneToOne) {
					MAssociationOneToOne oMAssociationOneToOne = (MAssociationOneToOne) oAssociation;
					bNeedForeignKey = oMAssociationOneToOne.isRelationOwner();
					bIsNotNull = oMAssociationOneToOne.isNotNull();
				}
	
				if (bNeedForeignKey) {
	
					MAssociationWithForeignKey oAssociationWithForeignKey = (MAssociationWithForeignKey) oAssociation;
	
					// Recupere la table referencee
					Table oTableRef = oAssociation.getRefClass().getTable();
	
					List<Field> listFkFields = new ArrayList<Field>();
					// Pour chaque champs de la cle primaire de la table
					// destination,
					// on cree un nouveau champs pour la table en cours
					for (Field oField : oTableRef.getPrimaryKey().getFields()) {
						String sFieldName = oDbNamingStrategy.getFKColumnName(oAssociation.getName(), oField);
						String sSqlType = oField.getType();
						Field oFkField = oSchemaFactory.createField(sFieldName, sSqlType, bIsNotNull, false, oAssociationWithForeignKey.getUniqueKey(),
								oField);
						listFkFields.add(oFkField);
						p_oTable.addField(oFkField);
						oAssociationWithForeignKey.addField(oFkField);
					}
					
					// Instancie une cle etrangere
					String sFkName = oDbNamingStrategy.getFKName(oAssociation.getName(), oTableRef);
					ForeignKey oForeignKey = oSchemaFactory.createForeignKey(sFkName, listFkFields, oTableRef.getPrimaryKey().getFields(), oTableRef,
							false, oAssociationWithForeignKey);
					p_oTable.addForeignKey(oForeignKey);
	
					// Creation de l'index
					String sIndexName = oDbNamingStrategy.getIndexName(p_oTable, oAssociation.getName());
					Index oIndex = oSchemaFactory.createIndex(sIndexName, listFkFields, false);
					p_oTable.addIndex(oIndex);
				}
			}
		}
	}

	/**
	 * definit les contraintes uniques
	 * 
	 * @param p_oTable
	 *            table
	 */
	private void setUniqueConstraints(Table p_oTable) {

		log.debug(" > setUniqueConstraints");
		
		log.debug("   p_oTable name: {}", p_oTable.getName());
		log.debug("   p_oTable xml: {}", p_oTable.toXml());
		
		DbNamingStrategy oDbNamingStrategy = this.schemaConfig.getDbNamingStrategyClass();
		SchemaFactory oSchemaFactory = this.schemaConfig.getSchemaFactory();

		Map<String, List<Field>> mapComposedUniqueContraints = new HashMap<String, List<Field>>();
		
		for (Field oField : p_oTable.getFields()) {
			
			log.debug(" >> Field: {}", oField.getName());
			log.debug("     isUnique: {}", oField.isUnique());
			log.debug("     getUniqueKey: {}", oField.getUniqueKey());
			
			
			if (oField.isUnique()) {
				String sUniqueConstraintName = oDbNamingStrategy.getUniqueKeyName(p_oTable.getName(), oField.getName());
				UniqueConstraint oUniqueConstraint = oSchemaFactory.createUniqueConstraint(sUniqueConstraintName, oField);
				p_oTable.addUniqueConstraint(oUniqueConstraint);
			} else if (oField.getUniqueKey() != null && oField.getUniqueKey().length() > 0) {
				String sUniqueKeyName = oField.getUniqueKey().toUpperCase(Locale.getDefault());
				List<Field> listFields = mapComposedUniqueContraints.get(sUniqueKeyName);
				if (listFields == null) {
					listFields = new ArrayList<Field>();
					mapComposedUniqueContraints.put(sUniqueKeyName, listFields);
				}
				listFields.add(oField);
			}
		}
		
		for (String sUniqueKeyName : mapComposedUniqueContraints.keySet()) {
			log.debug(" >> UNIQUE_KEY: {}", sUniqueKeyName);
			for (Field oField : mapComposedUniqueContraints.get(sUniqueKeyName)) {
				log.debug("     fiels: {}", oField.getName());
			}
		}
		
		for (Entry<String, List<Field>> oUniqueKey : mapComposedUniqueContraints.entrySet()) {
			String sUniqueConstraintName = oDbNamingStrategy.getUniqueKeyName(p_oTable.getName(), oUniqueKey.getKey());
			UniqueConstraint oUniqueConstraint = oSchemaFactory.createUniqueConstraint(sUniqueConstraintName, oUniqueKey.getValue());
			p_oTable.addUniqueConstraint(oUniqueConstraint);
		}
	}

	/**
	 * Extrait les tables a partir des classes de jointures
	 * 
	 * @param p_oModele
	 *            modele
	 * @param p_oSchema
	 *            le schema a alimenter
	 * @param p_mapTableClasses map table vers classes
	 */
	private void extractTablesFromJoinClasses(XModele<?> p_oModele, Schema p_oSchema, Map<String, MEntityImpl> p_mapTableClasses) {

		DbNamingStrategy oDbNamingStrategy = this.schemaConfig.getDbNamingStrategyClass();
		SchemaFactory oSchemaFactory = this.schemaConfig.getSchemaFactory();

		// Extrait les tables a partir des join classes
		for (MJoinEntityImpl oJoinClass : p_oModele.getModelDictionnary().getAllJoinClasses()) {

			// Calcul le nom de la table
			String sTableName = oDbNamingStrategy.getTableName(oJoinClass.getUmlName());
			// Calcul le nom de la cle primaire
			String sPKName = oDbNamingStrategy.getPKNameFromTableName(sTableName);
			// Cr�e la table
			Table oTable = oSchemaFactory.createTable(sTableName, sPKName, true);
			p_mapTableClasses.put(sTableName, oJoinClass);
			oJoinClass.setTable(oTable);
			p_oSchema.addTable(oTable);
			// Definition de la cle primaire
			setPrimaryKey(oTable, oJoinClass);
			// Definition des attributs
			setFieldsFromAttributs(oTable, oJoinClass);

			// Ajout des cles etrangeres vers les tables de la relation n..n
			MAssociationManyToMany oMAssociationManyToMany = oJoinClass.getAssociation();

			Iterator<MAttribute> iterRightAttrs = oJoinClass.getRightKeyAttrs().iterator();
			Table oTableDest = oMAssociationManyToMany.getRefClass().getTable();
			List<Field> listFkFields = new ArrayList<Field>();
			for (Field oPkField : oTableDest.getPrimaryKey().getFields()) {
				listFkFields.add(iterRightAttrs.next().getField());
			}

			String sFkName = oDbNamingStrategy.getFKName(oMAssociationManyToMany.getName(), oTableDest);
			String sIndexName = oDbNamingStrategy.getIndexName(oTableDest, oMAssociationManyToMany.getName());
			ForeignKey oForeignKey = oSchemaFactory.createForeignKey(sFkName, listFkFields, oTableDest.getPrimaryKey().getFields(), oTableDest, true,
					null);
			Index oIndex = oSchemaFactory.createIndex(sIndexName, listFkFields, false);
			oTable.addForeignKey(oForeignKey);
			oTable.addIndex(oIndex);

			Iterator<MAttribute> iterLeftAttrs = oJoinClass.getLeftKeyAttrs().iterator();
			oTableDest = oMAssociationManyToMany.getOppositeClass().getTable();
			listFkFields = new ArrayList<Field>();
			for (Field oPkField : oTableDest.getPrimaryKey().getFields()) {
				listFkFields.add(iterLeftAttrs.next().getField());
			}

			sFkName = oDbNamingStrategy.getFKName(oMAssociationManyToMany.getOppositeName(), oTableDest);
			sIndexName = oDbNamingStrategy.getIndexName(oTableDest, oMAssociationManyToMany.getOppositeName());
			oForeignKey = oSchemaFactory.createForeignKey(sFkName, listFkFields, oTableDest.getPrimaryKey().getFields(), oTableDest, true, null);
			oIndex = oSchemaFactory.createIndex(sIndexName, listFkFields, false);
			oTable.addForeignKey(oForeignKey);
			oTable.addIndex(oIndex);
		}
	}

	/**
	 * Il est possible que l'on est cree des index qui soient identique a la cle primaire. Ceux-ci sont inutiles car un index est automatiquement cr�e
	 * pour chaque cle primaire
	 * 
	 * @param p_oTable
	 *            table a traiter
	 */
	private void deleteDuplicatedIndexes(Table p_oTable) {

		if (p_oTable.getPrimaryKey() != null) {
			List<Field> listPrimaryKeyFields = p_oTable.getPrimaryKey().getFields();
			List<Index> listIndexToRemove = new ArrayList<Index>();

			for (Index oIndex : p_oTable.getIndexes()) {
				if (oIndex.getFields().containsAll(listPrimaryKeyFields)) {
					listIndexToRemove.add(oIndex);
				}
			}

			p_oTable.getIndexes().removeAll(listIndexToRemove);

			// On cherche a enlever les index inutiles
			// cad si index1(a) et index2(a,b) -> on enleve index1
			// si index1(a,b) et index2(a,b,c) -> on enleve index1
			// etc
			// et si index1(a) et list-pk(a,b) -> on enleve index1
			// si index1(a,b) et list-pk(a,b,c) -> on enleve index1
			// etc
			listIndexToRemove = new ArrayList<Index>();

			// Pour tous les index de la table
			for (Index oIndexToCompare : p_oTable.getIndexes()) {

				// On construit la liste des index de la table sans celui sur lequel on travaille
				List<Index> listIndexForComparaison = new ArrayList<Index>();
				listIndexForComparaison.addAll(p_oTable.getIndexes());
				listIndexForComparaison.remove(oIndexToCompare);

				List<Field> listFieldsToCompare = oIndexToCompare.getFields();

				// Pour tous les index de la table sans celui sur lequel on travaille
				for (Index oIndexForComparaison : listIndexForComparaison) {
					List<Field> listFieldsForComparaison = oIndexForComparaison.getFields();

					// On verifie si la liste des champs de l'index de la liste de comparaison
					// contient tous les champs de l'index sur lequel on travaille
					if (listFieldsForComparaison.containsAll(listFieldsToCompare)) {
						boolean bAreAllIn = true;
						int iNbFields = oIndexToCompare.getFields().size();
						int i = 0;
						// On verifie que les champs sont dans le meme ordre
						while (bAreAllIn && i < iNbFields) {
							bAreAllIn = listFieldsForComparaison.get(i).getName().equalsIgnoreCase(listFieldsToCompare.get(i).getName());
							i = i + 1;
						}

						// On verifie que lorsqu'il sont dans le meme ordre, les listes ne sont pas egales
						if (bAreAllIn && listFieldsToCompare.size() != listFieldsForComparaison.size()) {
							listIndexToRemove.add(oIndexToCompare);
						}
					}
				}

				// On verifie si la liste des champs de la cle primaire
				// contient tous les champs de l'index sur lequel on travaille
				if (listPrimaryKeyFields.containsAll(listFieldsToCompare)) {
					boolean bAreAllIn = true;
					int iNbFields = oIndexToCompare.getFields().size();
					int i = 0;
					// On verifie que les champs sont dans le meme ordre
					while (bAreAllIn && i < iNbFields) {
						bAreAllIn = listPrimaryKeyFields.get(i).getName().equalsIgnoreCase(listFieldsToCompare.get(i).getName());
						i = i + 1;
					}

					// On verifie que lorsqu'il sont dans le meme ordre, les listes ne sont pas egales
					if (bAreAllIn && listFieldsToCompare.size() != listPrimaryKeyFields.size()) {
						listIndexToRemove.add(oIndexToCompare);
					}
				}
			}

			p_oTable.getIndexes().removeAll(listIndexToRemove);
		}
	}

	/**
	 * Renvoie le type SQL par rapport au type uml
	 * 
	 * @param p_oAttribute
	 *            attribut
	 * @return type SQL par rapport au type uml
	 */
	private String getSqlType(MAttribute p_oAttribute) {

		ITypeDescription oTypeDesc = p_oAttribute.getTypeDesc();
		String r_sType = oTypeDesc.getSqlType();

		String sValue1 = null;
		String sValue2 = null;

		// Si number
		if (oTypeDesc.getDataType().equals(DataType.NUMERIC)) {
			sValue1 = Integer.toString(p_oAttribute.getPrecision());
			sValue2 = Integer.toString(p_oAttribute.getScale());
		}

		if (oTypeDesc.getDataType().equals(DataType.ALPHANUMERIC)) {
			sValue1 = Integer.toString(p_oAttribute.getLength());
		}

		// Cherche un premier point d'interrogation et le remplace
		// par la valeur par default ou celle du modele
		if (sValue1 != null) {
			r_sType = r_sType.replaceFirst("\\?", sValue1);
		}

		if (sValue2 != null) {
			r_sType = r_sType.replaceFirst("\\?", sValue2);
		}

		return r_sType;
	}

	/**
	 * Retourne la valeur maximale pour la sequence
	 * 
	 * @param p_oField
	 *            field
	 * @return la valeur maximale pour la sequence
	 */
	private String getSequenceMaxValue(Field p_oField) {

		StringBuffer r_sMaxValue = new StringBuffer();
		int iNbDigits = p_oField.getPrecision();

		for (int iCpt = 0; iCpt < iNbDigits; iCpt++) {
			r_sMaxValue.append('9');
		}

		return r_sMaxValue.toString();
	}
	
	

	/**
	 * Sort table list so that table creation works
	 * @param p_listSchemaTables
	 * @throws Exception
	 */
	protected void sortTables( List<Table> p_listSchemaTables ) throws Exception {
		
		List<Table> listOrderedTables = new ArrayList<Table>();
		List<Table> listTables = new ArrayList<Table>();
		listTables.addAll(p_listSchemaTables);
		
		boolean bCycle = false ;
		
		while( !listTables.isEmpty() && !bCycle ) {
			List<Table> listLoopTables = new ArrayList<Table>();
			for( Table oTable : listTables) {
				if ( !hasNonTreatedDependency( oTable, listOrderedTables )) {
					listLoopTables.add(oTable);
					listOrderedTables.add( oTable );
				}
			}
			if ( !listLoopTables.isEmpty()) {
				listTables.removeAll(listLoopTables);
			}
			else {
				bCycle = true ;
			}
		}
		
		if ( bCycle ) {
			MessageHandler.getInstance().addWarning("A foreign key cycle has been detected in schema");
		}
		else {
			p_listSchemaTables.clear();
			p_listSchemaTables.addAll(listOrderedTables);
		}

	}

	/**
	 * Test if table has a non treated dependency
	 * @param p_oTable table
	 * @param p_listOrderedTables ordered tables
	 * @return
	 */
	private boolean hasNonTreatedDependency(Table p_oTable, List<Table> p_listOrderedTables) {
		boolean r_bHasNonTreatedDep = false ;
		
		Iterator<ForeignKey> iterFK = p_oTable.getForeignKeys().iterator();
		while( iterFK.hasNext() && !r_bHasNonTreatedDep ) {
			ForeignKey oFK = iterFK.next();
			if ( oFK.getTableRef() != p_oTable && !p_listOrderedTables.contains(oFK.getTableRef())) {
				r_bHasNonTreatedDep = true ;
			}
		}
		
		return r_bHasNonTreatedDep;
	}
}
