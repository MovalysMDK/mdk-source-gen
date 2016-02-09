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
package com.a2a.adjava.uml2xmodele.extractors;

import java.util.List;

import com.a2a.adjava.extractors.ExtractorParams;
import com.a2a.adjava.extractors.ExtractorParams.ParameterName;
import com.a2a.adjava.extractors.MExtractor;
import com.a2a.adjava.languages.LanguageConfiguration;
import com.a2a.adjava.uml.UmlModel;
import com.a2a.adjava.uml2xmodele.extractors.validators.StereotypesCompatibilityValidator;
import com.a2a.adjava.xmodele.IDomain;
import com.a2a.adjava.xmodele.IModelDictionary;
import com.a2a.adjava.xmodele.IModelFactory;
import com.a2a.adjava.xmodele.XDomainRegistry;

/**
 * <p>AbstractGenerator</p>
 *
 * <p>Copyright (c) 2011
 * <p>Company: Adeuza
 *
 * @author lmichenaud
 *
 */

public abstract class AbstractExtractor<XD extends IDomain<? extends IModelDictionary, ? extends IModelFactory>> implements MExtractor<XD> {

	/**
	 * DomainRegistry
	 */
	private XDomainRegistry domainRegistry ;
	
	/**
	 * MDomain of extractor
	 */
	private XD domain ;
	
	/**
	 * Extractor parameters
	 */
	private ExtractorParams parameters = null;
	
	/**
	 * Stereotype validator
	 */
	private StereotypesCompatibilityValidator stereotypesValidator = new StereotypesCompatibilityValidator();
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.uml2xmodele.extractors.MExtractor#getDomainRegistry()
	 */
	@Override
	public XDomainRegistry getDomainRegistry() {
		return this.domainRegistry;
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.uml2xmodele.extractors.MExtractor#setDomainRegistry(com.a2a.adjava.uml2xmodele.XDomainRegistry)
	 */
	@Override
	public void setDomainRegistry(XDomainRegistry p_oMDomainRegistry) {
		this.domainRegistry = p_oMDomainRegistry ;
	}
	
	/**
	 * Retourne l'objet parametersMap
	 * 
	 * @return Objet parametersMap
	 */
	public ExtractorParams getParameters() {
		return this.parameters;
	}

	/**
	 * Affecte l'objet parametersMap
	 * 
	 * @param p_oParametersMap
	 *            Objet parametersMap
	 */
	@Override
	public void setParameters(ExtractorParams p_oParameters) {
		this.parameters = p_oParameters;
	}

	/**
	 * Retourne l'objet domain
	 * @return Objet domain
	 */
	public XD getDomain() {
		return this.domain;
	}

	/**
	 * Affecte l'objet domain 
	 * @param p_oDomain Objet domain
	 */
	public void setDomain(XD p_oDomain) {
		this.domain = p_oDomain;
	}

	/**
	 * Retourne l'objet lngConfiguration
	 * @return Objet lngConfiguration
	 */
	public LanguageConfiguration getLngConfiguration() {
		return this.domain.getLanguageConf();
	}
	
	/**
	 * Return a string list of parameter in configuration
	 * @param p_sName the parameter name
	 * @return
	 */
	public List<String> getParameterValues(String p_sName) {
		return this.parameters.getValues(p_sName);
	}
	
	/**
	 * Return a string list of parameter in configuration
	 * @param p_sName the parameter name
	 * @return
	 */
	public String getParameterValue(ParameterName p_oParameter) {
		return this.parameters.getValue(p_oParameter);
	}
	
	/**
	 * Return parameter value
	 * @param p_sName the parameter name
	 * @return parameter value
	 */
	public String getParameterValue(String p_sName) {
		return this.parameters.getValue(p_sName);
	}
	
	/**
	 * Return parameter value
	 * @param p_sName the parameter name
	 * @return parameter value
	 */
	public int getIntParameterValue(String p_sName) {
		return Integer.parseInt(this.parameters.getValue(p_sName));
	}
	
	/**
	 * Return stereotype validator
	 * @return stereotype validator
	 */
	public StereotypesCompatibilityValidator getStereotypesValidator() {
		return this.stereotypesValidator;
	}
	
	/**
	 * {@inheritDoc}
	 * Does nothing by default
	 * @see com.a2a.adjava.uml2xmodele.extractors.AbstractExtractor#preValidate(com.a2a.adjava.uml.UmlModel)
	 */
	@Override
	public void preValidate(UmlModel p_oModele) throws Exception {
	}
}
