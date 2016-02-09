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
package com.a2a.adjava.schema;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class Sequence extends AbstractDBObject {

	private int initialValue;
	private String maxValue;
	private int step;
	private boolean cached;
	private boolean cycle;

	/**
	 * @param p_sName
	 * @param p_sMaxValue
	 */
	protected Sequence(String p_sName, String p_sMaxValue ) {
		this.setName(p_sName);
		this.initialValue = 1;
		this.maxValue = p_sMaxValue;
		this.step = 1;
		this.cached = false;
		this.cycle = false;
	}

	/**
	 * @return
	 */
	public int getInitialValue() {
		return initialValue;
	}

	/**
	 * @return
	 */
	public String getMaxValue() {
		return maxValue;
	}

	/**
	 * @return
	 */
	public int getStep() {
		return step;
	}

	/**
	 * @return
	 */
	public boolean isCached() {
		return cached;
	}

	/**
	 * @return
	 */
	public boolean isCycle() {
		return cycle;
	}

	/**
	 * @return
	 */
	public Element toXml() {
		Element r_xSequence = DocumentHelper.createElement("sequence");
		r_xSequence.addAttribute("name", this.getName());
		r_xSequence.addAttribute("initial-value", Integer.toString(this.initialValue));
		r_xSequence.addAttribute("max-value", this.maxValue );
		r_xSequence.addAttribute("step", Integer.toString(this.step));
		r_xSequence.addAttribute("cached", Boolean.toString(this.cached));
		r_xSequence.addAttribute("cycle", Boolean.toString(this.cycle));
		return r_xSequence;
	}
}
