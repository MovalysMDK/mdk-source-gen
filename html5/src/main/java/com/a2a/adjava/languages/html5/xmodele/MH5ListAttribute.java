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
package com.a2a.adjava.languages.html5.xmodele;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;

import com.a2a.adjava.xmodele.MVisualField;

/**
 * @author lmichenaud
 *
 */
public class MH5ListAttribute extends MH5Attribute {

	public MH5ListAttribute(MVisualField p_oVisualFieldAttribute) {
		super(p_oVisualFieldAttribute);
	}
	
	private List<MH5Attribute> level2Attrs = new ArrayList<>();
	
	private List<MH5Attribute> level3Attrs = new ArrayList<>();
	
	/**
	 * @return
	 */
	public List<MH5Attribute> getLevel2Attrs() {
		return level2Attrs;
	}

	/**
	 * @return
	 */
	public List<MH5Attribute> getLevel3Attrs() {
		return level3Attrs;
	}

	@Override
	public Element toXml() {
		Element r_xElem = super.toXml();
		
		if ( !level2Attrs.isEmpty()) {
			Element xChildAttributes = r_xElem.addElement("level2-attributes");
			for( MH5Attribute oMH5Attribute : this.level2Attrs ) {
				xChildAttributes.add( oMH5Attribute.toXml());
			}
		}
		
		if ( !level3Attrs.isEmpty()) {
			Element xChildAttributes = r_xElem.addElement("level3-attributes");
			for( MH5Attribute oMH5Attribute : this.level3Attrs ) {
				xChildAttributes.add( oMH5Attribute.toXml());
			}
		}
		
		return r_xElem;
	}
}
