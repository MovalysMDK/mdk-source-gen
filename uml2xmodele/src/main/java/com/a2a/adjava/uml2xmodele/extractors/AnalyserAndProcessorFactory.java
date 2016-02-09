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

import com.a2a.adjava.extractors.IAdditionalLayoutProcessor;
import com.a2a.adjava.extractors.IAnalyserAndProcessorFactory;
import com.a2a.adjava.extractors.ICUDActionProcessor;
import com.a2a.adjava.extractors.IExpandableTypeProcessor;
import com.a2a.adjava.uml2xmodele.ui.screens.AdditionalLayoutProcessor;
import com.a2a.adjava.uml2xmodele.ui.screens.CUDActionProcessor;

public class AnalyserAndProcessorFactory implements IAnalyserAndProcessorFactory {

	protected IExpandableTypeProcessor instanceExpandableTypeProcessor;
	protected IAdditionalLayoutProcessor instanceAdditionalLayoutProcessor;
	protected ICUDActionProcessor instanceCUDActionProcessor;
	
	@Override
	public IAdditionalLayoutProcessor createAdditionalLayoutProcessor() {
		if(instanceAdditionalLayoutProcessor == null){
			instanceAdditionalLayoutProcessor = new AdditionalLayoutProcessor();
		}
		return instanceAdditionalLayoutProcessor;
	}

	@Override
	public IExpandableTypeProcessor createExpandableTypesProcessor() {
		if(instanceExpandableTypeProcessor == null){
			instanceExpandableTypeProcessor = new ExpandableTypeProcessor();
		}
		return instanceExpandableTypeProcessor;
	}

	@Override
	public ICUDActionProcessor createCUDActionProcessor() {
		if(instanceCUDActionProcessor == null){
			instanceCUDActionProcessor = new CUDActionProcessor();
		}
		return instanceCUDActionProcessor;
	}
	

}
