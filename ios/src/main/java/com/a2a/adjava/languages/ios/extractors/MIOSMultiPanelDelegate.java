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

import org.apache.commons.lang3.StringUtils;

import com.a2a.adjava.languages.ios.xmodele.MIOSClass;
import com.a2a.adjava.languages.ios.xmodele.MIOSScene;
import com.a2a.adjava.languages.ios.xmodele.MIOSStoryBoard;
import com.a2a.adjava.languages.ios.xmodele.controllers.MIOSControllerType;
import com.a2a.adjava.languages.ios.xmodele.controllers.MIOSNavigationController;
import com.a2a.adjava.languages.ios.xmodele.controllers.MIOSViewController;
import com.a2a.adjava.languages.ios.xmodele.views.MIOSSection;
import com.a2a.adjava.languages.ios.xmodele.views.MIOSView;
import com.a2a.adjava.xmodele.MScreen;
import com.a2a.adjava.xmodele.ui.viewmodel.ViewModelType;

/**
 * Multi panel delegate for ios platform
 */
public class MIOSMultiPanelDelegate extends AbstractMIOSContainerDelegate {

	/**
	 * Height of a frame
	 */
	private static final int FRAME_HEIGHT = 600;
	
	/**
	 * Width of a frame
	 */
	private static final int FRAME_WIDTH = 600;

	/**
	 * X pos of a scene
	 */
	private static final int X_POS_SIZE = 660;
	
	/**
	 * Y margin for a scene 
	 */
	private static final int Y_SCENE_MARGIN_SIZE = 140;

	/**
	 * Constructor
	 * @param p_storyBoardExtractor storyboard extractor delegate
	 */
	public MIOSMultiPanelDelegate(StoryBoardExtractor p_storyBoardExtractor) {
		super(p_storyBoardExtractor);
	}

	/**
	 * Create the storyBoard
	 * @param p_oScreen the screen
	 * @return the storyboard generated from the screen
	 */
	@Override
	public MIOSStoryBoard createStoryBoardFromScreen(MScreen p_oScreen) {

		MIOSStoryBoard r_oStoryBoard = super.createStoryBoardFromScreen(p_oScreen);

		// place every section
		int iNbScenes = r_oStoryBoard.getScenes().size()-1;
		int iYPosOrigin = 0 - ( (FRAME_HEIGHT/iNbScenes+Y_SCENE_MARGIN_SIZE) / iNbScenes );

		//If this screen is a the main screen, embed it in a navigation controller.
		if ( p_oScreen.isMain()) {
			MIOSScene oMainScene = r_oStoryBoard.getMainScene();
			MIOSNavigationController oNavController = this.delegator.getDomain().getXModeleFactory().createNavigationController(p_oScreen.getUmlName(), 
					oMainScene.getController());
			// Create the scene for the container
			MIOSScene oContainerScene = this.delegator.getDomain().getXModeleFactory().createScene( StringUtils.join(p_oScreen.getUmlName(), "Container"),
					oNavController);

			// Define main controller on storyboard
			r_oStoryBoard.setMainController(oNavController);

			r_oStoryBoard.addScene(oContainerScene);

			oContainerScene.addLinkTo(oMainScene);
			oMainScene.addLinkFrom(oContainerScene);
		}

		for (MIOSScene oScene : r_oStoryBoard.getScenes()) {
			if (oScene != r_oStoryBoard.getMainScene()) {

				if(!oScene.getController().getControllerType().equals(MIOSControllerType.NAVIGATION)) {
					oScene.setPosX(X_POS_SIZE);
					oScene.setPosY(iYPosOrigin);
					iYPosOrigin += FRAME_HEIGHT/iNbScenes+Y_SCENE_MARGIN_SIZE;
				}
				else {
					int iMainScenePosX = r_oStoryBoard.getMainScene().getPosX();
					int iMainScenePosY = r_oStoryBoard.getMainScene().getPosY();
					oScene.setPosX(iMainScenePosX - FRAME_WIDTH - Y_SCENE_MARGIN_SIZE);
					oScene.setPosY(iMainScenePosY);
				}
			}

		}

		return r_oStoryBoard;
	}


	/**
	 * create the main scene for the storyboard
	 * @param p_oScreen screen from which we create the scene
	 * @return the main scene
	 */
	protected MIOSScene createContainerScene(MScreen p_oScreen) {

		// create a scene for workspace
		MIOSScene r_oScreenScene = this.modelFactory.createScene(p_oScreen.getUmlName(), null);

		MIOSClass oClass = new MIOSClass();
		oClass.setName("MFMultiPanelViewController");

		// create the workspace ViewController
		this.containerMainController = this.modelFactory.createViewController(p_oScreen.getUmlName(), oClass);
		this.containerMainController.setControllerType(MIOSControllerType.MULTIPANEL);
		this.delegator.addControllerTitleLabel(this.containerMainController.getControllerType(), p_oScreen.getUmlName(), p_oScreen.getUmlName());

		// Comment for Screen
		this.containerMainController.setIsInCommentScreen(p_oScreen.isComment());
		
		// register the WorkspaceViewController
		this.delegator.getDomain().getDictionnary().registerIOSController(this.containerMainController);

		r_oScreenScene.setController(this.containerMainController);

		MIOSSection oSection = this.modelFactory.createSection("container");

		int iContainerHeight = FRAME_HEIGHT/p_oScreen.getPageCount();

		MIOSView oView = null;
		for (int iIndex = 0; iIndex < p_oScreen.getPageCount(); iIndex++) {

			oView = new MIOSView();
			oView.setCustomClass("containerView");

			oView.setPosX(0);
			oView.setPosY(iIndex*iContainerHeight);

			oView.setHeight(iContainerHeight);
			oView.setWidth(FRAME_WIDTH);

			oView.setId( this.containerMainController.getName()+"-container-"+iIndex );

			oSection.addSubView(oView);

		}
		//		oSection.computeSubviewsPosition();
		this.containerMainController.addSection(oSection);

		return r_oScreenScene;
	}

	@Override
	protected void doAfterCreateController(MIOSViewController p_oRootViewController, ViewModelType p_oType) {
		// nothing to do
	}

}


