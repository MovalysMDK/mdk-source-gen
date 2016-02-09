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
package com.a2a.adjava.uml2xmodele.operations;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.xmodele.IDomain;
import com.a2a.adjava.xmodele.MDaoMethodSignature;
import com.a2a.adjava.xmodele.MEntityImpl;
import com.a2a.adjava.xmodele.MPackage;

/**
 * 
 * <p>Parser pour les noms des operations</p>
 *
 * <p>Copyright (c) 2009
 * <p>Company: Adeuza
 *
 * @author mmadigand
 * @author lmichenaud
 *
 */
public final class UmlOperationNameParser {
	/**
	 * Logger for this class
	 */
	private static final Logger log = LoggerFactory
			.getLogger(UmlOperationNameParser.class);
	
	/**
	 * Instance singleton
	 */
	private static UmlOperationNameParser instance ;
	
	/**
	 * Retourne l'instance singleton
	 * @return l'instance singleton
	 */
	public static UmlOperationNameParser getInstance() {
		if ( instance == null ) {
			instance = new UmlOperationNameParser();
		}
		return instance ;
	}

	/**
	 * Constructeur
	 */
	private UmlOperationNameParser() {
	}

	/**
	 * Parse le nom de l'operation
	 * @param p_sOperationName nom de l'operation
	 * @param p_sUmlClassName nom de la classe uml de l'operation
	 * @param p_oClass classe de l'operation
	 * @parram p_oDaoPackage package du dao
	 * @param p_oProjectConfig config adjava
	 * @param p_sDocumentation documentation de l'operation
	 * @return les signatures des methodes generees
	 * @throws Exception echec de parsing
	 */
	public List<MDaoMethodSignature> parse( String p_sOperationName, String p_sUmlClassName,
			MEntityImpl p_oClass, MPackage p_oDaoPackage, IDomain p_oDomain, String p_sDocumentation ) throws Exception {

		String sOperationName = p_sOperationName ;
		
		List<MDaoMethodSignature> r_listMethodSignatures = new ArrayList<MDaoMethodSignature>();
		
		UmlOperationAnalysis oOperationAnalysis =
			new UmlOperationAnalysis( sOperationName, p_sUmlClassName,
					p_oClass, p_oDomain.getLanguageConf(), false );
		
		if ( oOperationAnalysis.analyse()) {
			String sNormalizedOperationName = oOperationAnalysis.getNormalizedOperationName();
			
			MDaoMethodSignature oMethodSignature = new MDaoMethodSignature(
					sNormalizedOperationName, "public", oOperationAnalysis.getType(),
					oOperationAnalysis.getReturnedType(), oOperationAnalysis.getReturnedProperties(),
					oOperationAnalysis.getImports(), false );
			oMethodSignature.addParameters( oOperationAnalysis.getListParameters());
			oMethodSignature.setManyToManyAssocations( oOperationAnalysis.getManyToManyAssociations());
			oMethodSignature.setDocumentation(p_sDocumentation);
			
			r_listMethodSignatures.add( oMethodSignature);
			log.debug("needed by value: {}", oOperationAnalysis.isNeedByValue());
			
			if ( oOperationAnalysis.isNeedByValue()) {
				oOperationAnalysis =
					new UmlOperationAnalysis( sOperationName, p_sUmlClassName,
							p_oClass, p_oDomain.getLanguageConf(), true );
				oOperationAnalysis.analyse();
				sNormalizedOperationName = oOperationAnalysis.getNormalizedOperationName();
				
				oMethodSignature = new MDaoMethodSignature(
						sNormalizedOperationName, "public", oOperationAnalysis.getType(),
						oOperationAnalysis.getReturnedType(), oOperationAnalysis.getReturnedProperties(),
						oOperationAnalysis.getImports(), true );
				oMethodSignature.addParameters( oOperationAnalysis.getListParameters());
				oMethodSignature.setManyToManyAssocations( oOperationAnalysis.getManyToManyAssociations());
				oMethodSignature.setDocumentation(p_sDocumentation);
				
				r_listMethodSignatures.add( oMethodSignature);
			}
		}
		
		return r_listMethodSignatures ;
	}
}
