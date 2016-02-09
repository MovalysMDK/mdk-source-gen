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
package com.a2a.adjava.extractors;

import org.dom4j.Element;

import com.a2a.adjava.uml.UmlModel;
import com.a2a.adjava.xmodele.IDomain;
import com.a2a.adjava.xmodele.IModelDictionary;
import com.a2a.adjava.xmodele.IModelFactory;
import com.a2a.adjava.xmodele.XDomainRegistry;

/**
 * <p>MExtrator</p>
 *
 * <p>Copyright (c) 2011
 * <p>Company: Adeuza
 *
 * @author lmichenaud
 *
 */

public interface MExtractor<XD extends IDomain<? extends IModelDictionary, ? extends IModelFactory>> {

	/**
	 * Get Domain Registry
	 * @return
	 */
	public XDomainRegistry getDomainRegistry();
	
	/**
	 * Set Domain Registry
	 * @param p_oMDomainRegistry Domain Registry
	 */
	public void setDomainRegistry( XDomainRegistry p_oMDomainRegistry );
		
	/**
	 * Extractor initialization
	 * @param p_xConfig configuration element
	 */
	public void initialize( Element p_xConfig ) throws Exception ;
	
	/**
	 * Validation the UML Mocel before the extraction to avoid incompatibility model problem 
	 * @param p_oModele uml model
	 * @throws Exception
	 */
	public void preValidate(UmlModel p_oModele) throws Exception ;
	
	/**
	 * Do extraction
	 * @param p_oModele uml model
	 * @throws Exception
	 */
	public void extract(UmlModel p_oModele) throws Exception ;
	
	/**
	 * Define parameters map
	 * @param p_oParametersMap
	 */
	public void setParameters(ExtractorParams p_oParameters);
	
	/**
	 * Set domain of extractor
	 * @param p_oDomain
	 */
	public void setDomain( XD p_oDomain );
	
	/**
	 * Get domain of extractor
	 * @return
	 */
	public XD getDomain();
}
