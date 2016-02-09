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
package com.a2a.adjava.uml2xmodele.extractors.viewmodel;

import com.a2a.adjava.uml.UmlClass;
import com.a2a.adjava.xmodele.MEntityImpl;
import com.a2a.adjava.xmodele.MViewModelImpl;
import com.a2a.adjava.xmodele.ui.viewmodel.mappings.IVMMappingDesc;

/**
 * VMPathContext add the following notion to PathContext :
 * <ul>
 * 	<p>Current View Model</p>
 *  <p>Current Mapping</p>
 *  <p>composite Index</p>
 *  <p>entity target</p>
 *  <p>uml target</p>
 * </ul>
 * @author lmichenaud
 *
 */
public class VMPathContext extends PathContext {

	/**
	 * valid path
	 * Default false
	 */
	private boolean valid = false ;
	
	/**
	 * Current VM in Path Context
	 */
	private MViewModelImpl masterVM ;
	
	/**
	 * Current VM in Path Context
	 */
	private MViewModelImpl currentVM ;
	
	/**
	 * Current Mapping
	 */
	private IVMMappingDesc currentMapping ;
	
	/**
	 * Entity Target 
	 */
	private MEntityImpl entityTarget ;
	
	/**
	 * Uml target
	 */
	private UmlClass umlTarget ;
	
	/**
	 * Composite index
	 */
	private int compositeIndex = 1 ;
	
	/**
	 * @param p_sFullPath
	 */
	public VMPathContext(String p_sFullPath, MViewModelImpl p_oVM, IVMMappingDesc p_oCurrentMapping ) {
		super(p_sFullPath);
		this.currentVM = p_oVM ;
		this.masterVM = p_oVM ;
		this.currentMapping = p_oCurrentMapping ;
	}

	/**
	 * @return
	 */
	public MViewModelImpl getCurrentVM() {
		return currentVM;
	}

	/**
	 * @return
	 */
	public IVMMappingDesc getCurrentMapping() {
		return currentMapping;
	}

	/**
	 * @return
	 */
	public int getCompositeIndex() {
		return compositeIndex;
	}
	
	
	/**
	 * Increment composite index
	 */
	public int incCompositeIndex() {
		return ++this.compositeIndex;
	}

	/**
	 * @param currentVM
	 */
	public void setCurrentVM(MViewModelImpl p_oCurrentVM) {
		this.currentVM = p_oCurrentVM;
	}

	/**
	 * @param currentMapping
	 */
	public void setCurrentMapping(IVMMappingDesc p_oCurrentMapping) {
		this.currentMapping = p_oCurrentMapping;
	}

	/**
	 * @return
	 */
	public MViewModelImpl getMasterVM() {
		return masterVM;
	}

	/**
	 * @return
	 */
	public MEntityImpl getEntityTarget() {
		return entityTarget;
	}

	/**
	 * @param p_oTargetEntity
	 */
	public void setEntityTarget(MEntityImpl p_oTargetEntity) {
		this.entityTarget = p_oTargetEntity;
	}

	/**
	 * @return
	 */
	public UmlClass getUmlTarget() {
		return umlTarget;
	}

	/**
	 * @param p_oUmlTarget
	 */
	public void setUmlTarget(UmlClass p_oUmlTarget) {
		this.umlTarget = p_oUmlTarget;
	}
	
	/**
	 * @return
	 */
	public boolean isValid() {
		return this.valid;
	}
	
	/**
	 * @param p_bValid
	 */
	public void setValid(boolean p_bValid) {
		this.valid = p_bValid ;
	}
	
	
	/**
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "pathContext[cur=" + this.getCurrentPath() + ", idx=" + this.getIndex() + ", full=" + this.getFullPath() 
			+ ", umlTarget=" + this.umlTarget.getName() + "]"; 
	}
}
