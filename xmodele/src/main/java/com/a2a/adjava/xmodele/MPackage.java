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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlTransient;

import com.a2a.adjava.utils.StrUtils;


/**
 * Package
 * @author lmichenaud
 *
 */
public class MPackage extends SGeneratedElement {
	
	/**
	 * parent package
	 */
	@XmlTransient
	private MPackage parent ;
	
	/**
	 * Sub packages
	 */
	private List<MPackage> packages ;
	
	/**
	 * Entities
	 */
	@XmlTransient
	private List<MEntityImpl> entitiesImpl ;
	
	/**
	 * Join entities 
	 */
	@XmlTransient
	private List<MJoinEntityImpl> joinEntitiesImpl ;
	
	/**
	 * Entities interfaces 
	 */
	@XmlTransient
	private List<MEntityInterface> entitiesInterface ;
	
	/**
	 * Empty constructor for jaxb
	 */
	public MPackage() {
		super();
	}
	
	/**
	 * Constructor
	 * @param p_sName package name
	 * @param p_oParent parent package
	 */
	public MPackage(String p_sName, MPackage p_oParent) {
		super("package", p_sName, p_sName);
		this.parent = p_oParent ;
		if ( p_oParent != null ) {
			this.setFullName(p_oParent.getFullName() + StrUtils.DOT + p_sName);
		}
		this.packages = new ArrayList<MPackage>();
		this.entitiesImpl = new ArrayList<MEntityImpl>();
		this.joinEntitiesImpl = new ArrayList<MJoinEntityImpl>();
		this.entitiesInterface = new ArrayList<MEntityInterface>();
	}

	/**
	 * Return child packages
	 * @return child packages
	 */
	public List<MPackage> getPackages() {
		return packages ;
	}
	
	/**
	 * Add a child package
	 * @param p_oPackage package to add
	 */
	public void addPackage(MPackage p_oPackage) {
		this.packages.add( p_oPackage );
	}
	
	/**
	 * Get parent package
	 * @return parent package
	 */
	public MPackage getParent() {
		return parent;
	}
	
	/**
	 * Add a child to package
	 * @param p_oElement child
	 */
	protected void add(Object p_oElement) {
		if (p_oElement instanceof MJoinEntityImpl) {
			this.joinEntitiesImpl.add((MJoinEntityImpl) p_oElement);
		}
		else
		if (p_oElement instanceof MEntityImpl) {
			this.entitiesImpl.add((MEntityImpl) p_oElement);
		}
		else if (p_oElement instanceof MEntityInterface) {
			this.entitiesInterface.add((MEntityInterface) p_oElement);
		}
	}
	
	/**
	 * Get child package by name
	 * @param p_sName child package name
	 * @return child package
	 */
	public MPackage getChildPackage( String p_sName ) {
		MPackage r_oPackage = null ;
		Iterator<MPackage> iterPackages = packages.listIterator();
		boolean bFind = false ;
		while (iterPackages.hasNext() && !bFind) {
			MPackage oPackage = iterPackages.next();
			if ( p_sName.equals(oPackage.getName())) {
				bFind = true ;
				r_oPackage = oPackage ;
			}
		}
		return r_oPackage ;
	}
	
	/**
	 * Return join entities of package
	 * @return join entities of package
	 */
	public List<MJoinEntityImpl> getJoinEntitesImpl() {
		return this.joinEntitiesImpl;
	}

	/**
	 * Return entity interfaces of package
	 * @return entity interfaces of package
	 */
	public List<MEntityInterface> getEntitesInterface() {
		return this.entitiesInterface;
	}
	
	/**
	 * Return entities implementation of package
	 * @return entities implementation of package
	 */
	public List<MEntityImpl> getEntitiesImpl() {
		return this.entitiesImpl;
	}
}
