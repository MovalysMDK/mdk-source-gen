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
package com.a2a.adjava.xmodele.ui.navigation;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.a2a.adjava.utils.ToXmlUtils;
import com.a2a.adjava.xmodele.MPage;
import com.a2a.adjava.xmodele.MScreen;

/**
 * Navigation between two screen
 * @author lmichenaud
 *
 */
public class MNavigation {

	/**
	 * Navigation name
	 */
	private String name ;
	
	/**
	 * Navigation type
	 */
	private MNavigationType navigationType ;
	
	/**
	 * Screen source
	 */
	private MScreen source ;

	/**
	 * The page that initiated the navigation
	 */
	private MPage sourcePage;
	
	/**
	 * Screen target
	 */
	private MScreen target ;

	/**
	 * Index of the target page
	 */
	private int targetPageIdx = 0;
	
	/**
	 * @param source
	 * @param target
	 * @param action
	 */
	public MNavigation( String p_sName, MNavigationType p_oNavigationType, MScreen source, MScreen target) {
		super();
		this.name = p_sName;
		this.source = source;
		this.target = target;
		this.navigationType = p_oNavigationType ;
	}

	/**
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return
	 */
	public MScreen getSource() {
		return source;
	}

	/**
	 * @param p_oSource
	 */
	public void setSource(MScreen p_oSource) {
		this.source = p_oSource;
	}
	
	/**
	 * @return
	 */
	public MScreen getTarget() {
		return target;
	}
	
	/**
	 * Can be null if screen has no page
	 * @return
	 */
	public MPage getTargetPage() {
		MPage r_oPage = null ;
		if ( this.target.hasMasterPage()) {
			r_oPage = this.target.getPanel(this.targetPageIdx);
		}
		return r_oPage ;
	}
	
	/**
	 * @param p_iTargetPageIdx
	 */
	public void setTargetPageIdx(int p_iTargetPageIdx) {
		this.targetPageIdx = p_iTargetPageIdx;
	}

	/**
	 * Returns the Target page index in the screen
	 * @return
	 */
	public int getTargetPageIdx() { return this.targetPageIdx; }

	/**
	 * @return
	 */
	public MNavigationType getNavigationType() {
		return navigationType;
	}

	/**
	 * @param p_oNavigationType
	 */
	public void setNavigationType(MNavigationType p_oNavigationType) {
		this.navigationType = p_oNavigationType;
	}

	/**
	 * @return
	 */
	public Element toXml() {
		Element r_xElem = DocumentHelper.createElement("navigation");
		r_xElem.addAttribute("type", this.navigationType.name());
		r_xElem.addAttribute("name", this.name);
		
		if (this.target != null) {
			r_xElem.addAttribute("internal", Boolean.toString(this.target.equals(this.source)));
		} else {
			r_xElem.addAttribute("internal", Boolean.toString(false));
		}
		
		Element xSource = r_xElem.addElement("source");
		xSource.addElement("name").setText(this.getSource().getName());
		xSource.addElement("name-lowercase").setText(this.getSource().getName().toLowerCase());
		xSource.addElement("full-name").setText(this.getSource().getFullName());
		ToXmlUtils.addImplements(xSource, this.getSource().getMasterInterface());
		
		Element xTarget = r_xElem.addElement("target");
		if (this.target != null) {
			xTarget.addElement("name").setText(this.getTarget().getName());
			xTarget.addElement("name-lc").setText(this.getTarget().getName().toLowerCase());
			xTarget.addElement("full-name").setText(this.getTarget().getFullName());
	
			if ( this.targetPageIdx != -1 ) {
				xTarget.addElement("page-idx").setText(Integer.toString(this.targetPageIdx));	
			}
			ToXmlUtils.addImplements(xTarget, this.getTarget().getMasterInterface());
		}
		
		return r_xElem;
	}

	/**
	 * Index of the source page
	 */
	public MPage getSourcePage() {
		return sourcePage;
	}

	public void setSourcePage(MPage sourcePage) {
		this.sourcePage = sourcePage;
	}
}
