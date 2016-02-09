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
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import com.a2a.adjava.languages.ios.xmodele.MIOSClass;
import com.a2a.adjava.languages.ios.xmodele.MIOSScene;
import com.a2a.adjava.languages.ios.xmodele.MIOSStoryBoard;
import com.a2a.adjava.languages.ios.xmodele.controllers.MIOSControllerType;
import com.a2a.adjava.languages.ios.xmodele.controllers.MIOSNavigationController;
import com.a2a.adjava.languages.ios.xmodele.controllers.MIOSViewController;
import com.a2a.adjava.languages.ios.xmodele.controllers.MIOSWorkspaceRole;
import com.a2a.adjava.xmodele.MPage;
import com.a2a.adjava.xmodele.MScreen;
import com.a2a.adjava.xmodele.ui.component.MWorkspaceConfig;
import com.a2a.adjava.xmodele.ui.viewmodel.ViewModelType;

/**
 * Delegate for the workspace management
 * 
 * @author abelliard
 */
public class MIOSWorkspaceDelegate extends AbstractMIOSContainerDelegate {

	/**
	 * X position of a created scene
	 */
	private static final int X_POS_SIZE = 660;
	
	/**
	 * Scene height
	 */
	private static final int Y_SCENE_SIZE = 670;
	
	/**
	 * Width of a frame
	 */
	private static final int FRAME_WIDTH = 600;
	
	/**
	 * Y margin of a scene
	 */
	private static final int Y_SCENE_MARGIN_SIZE = 140;

	/**
	 * Columns of workspace
	 */
	private Map<Integer, Column> columns = new HashMap<>();


	protected MScreen mainScreen;

	/**
	 * Constructor
	 * @param p_storyBoardExtractor storyboard extractor delegate
	 */
	public MIOSWorkspaceDelegate(StoryBoardExtractor p_storyBoardExtractor) {
		super(p_storyBoardExtractor);
	}

	@Override
	public String computeControllerName(MPage p_oPage) {
		int iColumnIndex = -1;
		for(Entry<Integer, Column> oColumnEntry : this.columns.entrySet()) {
			if(oColumnEntry.getValue().getSections().contains(p_oPage)) {
				iColumnIndex = oColumnEntry.getKey();
				break;
			}
		}

		if(iColumnIndex < 0) {
			return super.computeControllerName(p_oPage);
		}
		else {
			StringBuilder r_oControllerName = new StringBuilder(this.delegator.getPrefixForControllerName());
			r_oControllerName.append(this.mainScreen.getUmlName());
			r_oControllerName.append("DetailColumn");
			r_oControllerName.append(iColumnIndex);
			r_oControllerName.append(this.delegator.getSuffixForControllerName());
			return r_oControllerName.toString();
		}
		
	}

	/**
	 * Creates a storyboard from a screen
	 * @param p_oScreen screen from which we create the storyboard
	 * @return
	 */
	public MIOSStoryBoard createStoryBoardFromScreen(MScreen p_oScreen) {
		this.mainScreen = p_oScreen;

		MIOSStoryBoard r_oStoryBoard = this.modelFactory
				.createStoryBoard(p_oScreen.getUmlName());



		//If this screen is a the main screen, embed it in a navigation controller.
		if ( p_oScreen.isMain()) {
			MIOSScene oMainScene = this.createContainerScene(p_oScreen);
			MIOSNavigationController oNavController = this.delegator.getDomain().getXModeleFactory().createNavigationController(p_oScreen.getUmlName(), 
					oMainScene.getController());
			// Create the scene for the container
			MIOSScene oContainerScene = this.delegator.getDomain().getXModeleFactory().createScene( StringUtils.join(p_oScreen.getUmlName(), "Container"),
					oNavController);
			r_oStoryBoard.setMainScene(oMainScene);
			// Define main controller on storyboard
			r_oStoryBoard.setMainController(oNavController);

			r_oStoryBoard.addScene(oContainerScene);

			oContainerScene.addLinkTo(oMainScene);
			oMainScene.addLinkFrom(oContainerScene);
		}
		else {
			// fake scene for MFWorksaceViewController
			MIOSScene oMainScene = this.createContainerScene(p_oScreen);
			r_oStoryBoard.setMainController(oMainScene.getController());
			r_oStoryBoard.setMainScene(oMainScene);
		}


		// In Workspace each page is a ViewController (not a Section)
		for (MPage oPage : p_oScreen.getPages()) {

			// getColumnAndSection
			String sPanelType = oPage.getParameterValue(MWorkspaceConfig.PANELTYPE_PARAMETER);
			int iColumn = Integer.parseInt(oPage.getParameterValue(MWorkspaceConfig.COLUMN_PARAMETER));
			int iSection = Integer.parseInt(oPage.getParameterValue(MWorkspaceConfig.SECTION_PARAMETER));

			if (sPanelType.equals(MWorkspaceConfig.MASTER_PANELTYPE)) {
				iColumn = iColumn * (-1);
			}

			// create a column..
			Column oCurrentCol = columns.get(iColumn);
			if (oCurrentCol == null) {
				oCurrentCol = new Column();
				oCurrentCol.setColumnNumber(iColumn);
				columns.put(iColumn, oCurrentCol);
			}
			oCurrentCol.addPage(iSection, oPage);


		}

		// iterate over columns
		List<Column> lSortedColumn = new ArrayList<>(this.columns.values());
		Collections.sort( lSortedColumn );
		for ( Column oColumn : lSortedColumn ) {

			if (oColumn.getSections().size() > 1) {

				// add pages in column
				MIOSScene oScene = this.createSceneFromPages(oColumn.getSections(), r_oStoryBoard);
				r_oStoryBoard.addScene(oScene);

			} else {
				for (MPage oPage : oColumn.getSections()) {

					// add page in column
					MIOSScene oScene = this.createSceneFromPage(oPage, null, r_oStoryBoard);
					r_oStoryBoard.addScene(oScene);

				}
			}

		}

		// placing scenes
		int iNbWKSScenes = r_oStoryBoard.getScenes().size() - 1;
		int iYPosOrigin = 0 - (Y_SCENE_SIZE / 2 * (iNbWKSScenes - 1));
		for (MIOSScene oScene : r_oStoryBoard.getScenes()) {
			if (!oScene.equals(r_oStoryBoard.getMainScene())) {
				if(!oScene.getController().getControllerType().equals(MIOSControllerType.NAVIGATION)) {
					// position X fix
					oScene.setPosX(X_POS_SIZE);

					// position Y f(nbScenes, currentPosition)
					oScene.setPosY(iYPosOrigin);
					iYPosOrigin += Y_SCENE_SIZE;
				}
				else {
					// position X fix for navigation controller if exists
					oScene.setPosX(X_POS_SIZE - 2*(FRAME_WIDTH + 2*Y_SCENE_MARGIN_SIZE));
					// position Y f(nbScenes, currentPosition)
					oScene.setPosY(0);
				}

			}

		}

		return r_oStoryBoard;
	}

	/**
	 * Creates a scene from a screen
	 * @param p_oScreen screen from which we create the scene
	 */
	@Override
	protected MIOSScene createContainerScene(MScreen p_oScreen) {
		// create a scene for workspace
		MIOSScene r_oScreenScene = this.modelFactory.createScene(p_oScreen.getUmlName(), null);

		MIOSClass oClass = new MIOSClass();
		oClass.setName("MFWorkspaceViewController");

		// create the workspace ViewController
		this.containerMainController = this.modelFactory.createViewController(p_oScreen.getUmlName(), oClass);
		this.containerMainController.setControllerType(MIOSControllerType.WORKSPACE);
		this.delegator.addControllerTitleLabel(this.containerMainController.getControllerType(), p_oScreen.getUmlName(), p_oScreen.getUmlName());

		// register the WorkspaceViewController
		this.delegator.getDomain().getDictionnary().registerIOSController(this.containerMainController);

		r_oScreenScene.setController(this.containerMainController);

		return r_oScreenScene;
	}

	@Override
	protected void doAfterCreateController(MIOSViewController p_oRootViewController, ViewModelType p_oType) {
		switch (p_oType) {
		case MASTER:
			p_oRootViewController.setWorkspaceRole(MIOSWorkspaceRole.WORKSPACE_DETAIL);
			break;
		default:
			p_oRootViewController.setWorkspaceRole(MIOSWorkspaceRole.WORKSPACE_MASTER);
			break;
		}
	}

	/**
	 * Describes a column with its pages and their section number
	 */
	private class Column implements Comparable<Column> {

		private int number = Integer.MAX_VALUE;

		private Map<Integer, MPage> sections = new HashMap<>();

		/**
		 * Sets the number of columns
		 * @param p_iNumber number of columns
		 */
		public void setColumnNumber(int p_iNumber) {
			this.number = p_iNumber;
		}

		/**
		 * adds a page with a section number
		 * @param p_iSection section number
		 * @param p_oPage page
		 */
		public void addPage(int p_iSection, MPage p_oPage) {
			this.sections.put(p_iSection, p_oPage);
		}

		/**
		 * returns pages found in the sections
		 * @return
		 */
		public Collection<MPage> getSections() {
			return this.sections.values();
		}

		@Override
		public int compareTo(Column p_oColumn) {
			if (this.number < 0 && p_oColumn.number < 0) {
				// inverse
				if (this.number > p_oColumn.number) {
					return -1;
				} else if (this.number < p_oColumn.number) {
					return 1;
				} else {
					return 0;
				}
			} else if (this.number < p_oColumn.number) {
				return -1;
			} else if (this.number > p_oColumn.number) {
				return 1;
			} else {
				return 0;
			}
		}

	}

	@Override
	public boolean isViewModelInGlobalViewModelForFirstSection(MPage p_oPage) {
		for(Entry<Integer, Column> oColumnEntry : this.columns.entrySet()) {
			if(oColumnEntry.getValue().getSections().contains(p_oPage) && oColumnEntry.getKey() < 0) {
				return false;
			}
		}
		return true;
	}
	@Override
	public boolean isViewModelInGlobalViewModelForOtherSection(MPage p_oPage) {
		return true;
	}

}
