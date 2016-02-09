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
package com.a2a.adjava.utils;

/**
 * @author lmichenaud
 *
 */
public class Chrono {

	/**
	 * Start time
	 */
	private long start ;
	
	/**
	 * End time
	 */
	private long end ;
	
	/**
	 * 
	 */
	public Chrono() {
		
	}

	/**
	 * @param p_bStart start chrono
	 */
	public Chrono( boolean p_bStart ) {
		if ( p_bStart ) {
			this.start();
		}
	}
	
	/**
	 * Start chrono
	 */
	public final void start() {
		this.start = System.currentTimeMillis();
	}
	
	/**
	 * Stop chrono
	 * @return time elapsed
	 */
	public long stop() {
		this.end = System.currentTimeMillis();
		return this.getTime();
	}
	
	/**
	 * Stop and return time elapsed for display
	 * @return time elapsed in display format
	 */
	public String stopAndDisplay() {
		return this.stop() + "ms";
	}
	
	/**
	 * Return time elapsed
	 * @return return time elapsed
	 */
	public long getTime() {
		return this.end - this.start ;
	}
}
