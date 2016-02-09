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
package com.a2a.adjava.types;

import java.util.List;

public class TypeConvertion implements ITypeConvertion {

	private String to = null;
	private String formula = null;
	private List<String> imports = null;

	@Override
	public String getTo() {
		return this.to;
	}

	@Override
	public void setTo(String p_sTo) {
		this.to = p_sTo;
	}

	@Override
	public void setFormula(String p_sFormula) {
		this.formula = p_sFormula;
	}

	public String getFormula() {
		return formula;
	}

	@Override
	public String applyFormula(String p_sValue) {
		return this.formula.replaceAll("VALUE", p_sValue);
	}

	@Override
	public void setImports(List<String> p_lstImports) {
		this.imports = p_lstImports;
	}

	@Override
	public List<String> getImports() {
		return this.imports;
	}
}
