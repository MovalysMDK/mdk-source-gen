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

import org.dom4j.Element;

/**
 * 
 * <p>Interface for Dao</p>
 *
 * <p>Copyright (c) 2009
 * <p>Company: Adeuza
 *
 * @author lmichenaud
 *
 */
public class MDaoInterface extends SInterface {

	/**
	 * Dao implementation
	 */
	private MDaoImpl mdao;
	
	/**
	 * Entity class 
	 */
	private MEntityImpl mclass;
	
	/**
	 * Constructor
	 * @param p_sName name of dao interface
	 * @param p_sBeanName bean name for dao interface
	 * @param p_oPackage package for dao interface
	 * @param p_oMDao dao implementation
	 * @param p_oClass entity implementation
	 */
	public MDaoInterface( String p_sName, String p_sBeanName, MPackage p_oPackage, MDaoImpl p_oMDao, MEntityImpl p_oClass ) {
		super("dao-interface", null, p_sName, p_oPackage);
		this.mdao = p_oMDao;
		this.mclass = p_oClass;
		this.setBeanName(p_sBeanName);
	}
		
	/**
	 * Get entity implementation
	 * @return entity implementation
	 */
	public MEntityImpl getMEntityImpl() {
		return this.mclass;
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.SWithMethodElement#toXmlInsertBeforeDocumentation(org.dom4j.Element)
	 */
	@Override
	protected void toXmlInsertBeforeDocumentation(Element p_xElement) {
		super.toXmlInsertBeforeDocumentation(p_xElement);
		p_xElement.add(this.mdao.toXml());
	}
}
