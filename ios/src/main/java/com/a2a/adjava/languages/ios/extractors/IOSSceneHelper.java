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
package com.a2a.adjava.languages.ios.extractors;

import java.util.ArrayList;
import java.util.List;

import com.a2a.adjava.languages.ios.xmodele.MIOSScene;
import com.a2a.adjava.languages.ios.xmodele.MIOSStoryBoard;

/**
 * Helper for IOS Scene
 * @author lmichenaud
 *
 */
public final class IOSSceneHelper {

	/**
	 * scenes shift on x values
	 */
	private static final int XSHIFT = 650;
	
	/**
	 * scenes shift on y values
	 */
	private static final int YSHIFT = 860;
	
	/**
	 * Singleton instance
	 */
	private static IOSSceneHelper instance = new IOSSceneHelper();

	/**
	 * Constructor
	 */
	private IOSSceneHelper() {
		//Empty constructor
	}

	/**
	 * Return singleton instance
	 * @return singleton instance
	 */
	public static IOSSceneHelper getInstance() {
		return instance;
	}
	
	/**
	 * Compute scenes for story board
	 * @param p_oStoryBoard story board
	 */
	public void computeScenePositions( MIOSStoryBoard p_oStoryBoard ) {
		
		int iStartY = 0 ;
		int iStartX = 0 ;
		for( MIOSScene oScene : this.getRootScenes(p_oStoryBoard)) {
			this.computeScenePosition(oScene, iStartX, iStartY);
			iStartY += XSHIFT ;
		}
	}
	
	/**
	 * Compute scene position
	 * @param p_oScene scene
	 * @param p_iPosX x position
	 * @param p_iPosY y position
	 */
	public void computeScenePosition( MIOSScene p_oScene, int p_iPosX, int p_iPosY ) {
		p_oScene.setPosX(p_iPosX);
		p_oScene.setPosY(p_iPosY);
		
		for( MIOSScene oScene : p_oScene.getLinksTo()) {
			computeScenePosition(oScene, p_iPosX + YSHIFT, p_iPosY);
		}
	}
	
	/**
	 * Return root scenes of storyboard
	 * @param p_oStoryBoard storyboard
	 * @return root scenes of storyboard
	 */
	public List<MIOSScene> getRootScenes( MIOSStoryBoard p_oStoryBoard ) {
		List<MIOSScene> r_listRootScenes = new ArrayList<MIOSScene>();
		for( MIOSScene oScene : p_oStoryBoard.getScenes()) {
			if ( oScene.getLinksFrom().isEmpty()) {
				r_listRootScenes.add(oScene);
			}
		}
		return r_listRootScenes ;
	}
}
