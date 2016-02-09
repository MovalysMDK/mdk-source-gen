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

import com.a2a.adjava.languages.ios.project.IOSUITypeDescription;
import com.a2a.adjava.languages.ios.xmodele.MIOSDictionnary;
import com.a2a.adjava.languages.ios.xmodele.MIOSDomain;
import com.a2a.adjava.languages.ios.xmodele.MIOSModeleFactory;
import com.a2a.adjava.languages.ios.xmodele.MIOSScene;
import com.a2a.adjava.languages.ios.xmodele.MIOSStoryBoard;
import com.a2a.adjava.languages.ios.xmodele.controllers.MIOSFormViewController;
import com.a2a.adjava.languages.ios.xmodele.controllers.MIOSListViewController;
import com.a2a.adjava.languages.ios.xmodele.controllers.MIOSViewController;
import com.a2a.adjava.languages.ios.xmodele.controllers.MIOSWorkspaceRole;
import com.a2a.adjava.languages.ios.xmodele.views.MIOSButtonView;
import com.a2a.adjava.languages.ios.xmodele.views.MIOSSection;
import com.a2a.adjava.languages.ios.xmodele.views.MIOSView;
import com.a2a.adjava.xmodele.MAction;
import com.a2a.adjava.xmodele.MActionType;
import com.a2a.adjava.xmodele.MPage;
import com.a2a.adjava.xmodele.MScreen;
import com.a2a.adjava.xmodele.ui.component.MAbstractButton;
import com.a2a.adjava.xmodele.ui.component.MButtonType;
import com.a2a.adjava.xmodele.ui.component.MNavigationButton;
import com.a2a.adjava.xmodele.ui.navigation.MNavigation;
import com.a2a.adjava.xmodele.ui.navigation.MNavigationType;

/**
 * IOS Navigation helper
 * 
 * @author lmichenaud
 * 
 */
public final class IOSNavigationHelper {
	/**
	 * Ui type description for button
	 */
	private static final String BUTTON_UI_TYPE_DESC = "Button";

	/**
	 * Space between buttons
	 */
	private static final int SPACE_BETWEEN_BUTTONS = 5;
	
	/**
	 * Singleton instance
	 */
	private static IOSNavigationHelper instance = new IOSNavigationHelper();

	/**
	 * Constructor
	 */
	private IOSNavigationHelper() {
		// Empty constructor
	}

	/**
	 * Return singleton instance
	 * 
	 * @return singleton instance
	 */
	public static IOSNavigationHelper getInstance() {
		return instance;
	}

	/**
	 * Compute navigation between storyboards
	 * @param p_oScreen screen
	 * @param p_oScreenConfig screen config
	 * @param p_oDomain domain
	 * @throws Exception
	 */
	public void computeNavigation(MScreen p_oScreen,
			ScreenConfig p_oScreenConfig,
			MIOSDomain<MIOSDictionnary, MIOSModeleFactory> p_oDomain)
			throws Exception {

		if (!p_oScreen.hasMasterPage()) {
			this.computeNavigationForMenuScreen(p_oScreen, p_oScreenConfig,
					p_oDomain);
		}

		if (p_oScreen.hasMasterPage()
				&& p_oScreen.getMasterPage().getViewModelImpl().getType()
						.isList()) {

			MIOSStoryBoard oStoryBoard = p_oDomain.getDictionnary()
					.getIOSStoryBoardByScreen(p_oScreen);

			MNavigation oDetailNavigation = p_oScreen.getMasterPage()
					.getNavigationOfType(MNavigationType.NAVIGATION_DETAIL);

			if (oDetailNavigation != null) {
				MScreen oDetailScreen = oDetailNavigation.getTarget();

				MIOSStoryBoard oDetailStoryBoard = p_oDomain.getDictionnary()
						.getIOSStoryBoardByScreen(oDetailScreen);

				MIOSListViewController oListController = (MIOSListViewController) p_oDomain.getDictionnary().getControllerBiId(oStoryBoard
						.getMainScene().getController().getId());
				oListController.setDetailStoryboard(oDetailStoryBoard);
								
				
				// Add delete action on list viewcontroller if available
				MAction oDeleteAction = oDetailScreen.getMasterPage().getActionOfType(MActionType.DELETEDETAIL);
				if ( oDeleteAction != null ) {
					oListController.setDeleteAction(oDeleteAction.getName());
				}

				MIOSFormViewController oDetailController = ((MIOSFormViewController) oDetailStoryBoard
						.getMainScene().getController());
				oListController.setDetailViewController(oDetailController);
			} else if (p_oScreen.isWorkspace()) {
				MIOSListViewController oMainController = null;
				MAction oDeleteAction = null;
				
				for (MPage oPage : p_oScreen.getPages()) {
					if (oPage.getActionOfType(MActionType.DELETEDETAIL) != null) {
						oDeleteAction = oPage.getActionOfType(MActionType.DELETEDETAIL);
					}
				}
				
				if (oDeleteAction != null) { 
					for (MIOSScene oScene : oStoryBoard.getScenes()) {
						if (((MIOSViewController)oScene.getController()).getWorkspaceRole().equals(MIOSWorkspaceRole.WORKSPACE_MASTER)) {
							oMainController = ((MIOSListViewController)oScene.getController());
						}
					}
				
					if (oMainController != null) {
						oMainController.setDeleteAction(oDeleteAction.getName());
					}
				}
			}
		}
	}

	/**
	 * Compute navigation for menu screen
	 * 
	 * @param p_oScreen
	 *            screen
	 * @param p_oScreenConfig
	 *            screen config
	 * @param p_oDomain
	 *            domain
	 */
	protected void computeNavigationForMenuScreen(MScreen p_oScreen,
			ScreenConfig p_oScreenConfig,
			MIOSDomain<MIOSDictionnary, MIOSModeleFactory> p_oDomain) {
		MIOSStoryBoard oStoryBoard = p_oDomain.getDictionnary()
				.getIOSStoryBoardByScreen(p_oScreen);

		MIOSSection oSection = ((MIOSViewController)oStoryBoard.getMainScene().getController()).getFirstSection();
		if ( oSection == null ) {
			oSection = p_oDomain.getXModeleFactory().createSection(p_oScreen.getName());
			p_oDomain.getDictionnary().registerIOSSection(oSection);
			((MIOSViewController) oStoryBoard.getScenes().get(0).getController())
				.addSection(oSection);
		}
		// Loop over navigation buttons
		for (MAbstractButton oMAbstractButton : p_oScreen.getLayout()
				.getButtons()) {
			if (oMAbstractButton.getButtonType().equals(MButtonType.NAVIGATION)) {
				MNavigationButton oNavButton = (MNavigationButton) oMAbstractButton;
				IOSUITypeDescription oUITypeDesc = (IOSUITypeDescription) p_oDomain
						.getLanguageConf().getUiTypeDescriptions()
						.get(BUTTON_UI_TYPE_DESC);

				// create the ios button
				MIOSButtonView oButtonView = new MIOSButtonView();
				oButtonView.setId(this.computeButtonId(oNavButton.getName()));
				oButtonView.setCustomClass(oUITypeDesc.getROComponentType());
				MIOSStoryBoard oTargetStoryBoard = p_oDomain.getDictionnary()
						.getIOSStoryBoardByScreen(
								oNavButton.getNavigation().getTarget());
				oButtonView.setTargetStoryBoard(oTargetStoryBoard);
				oButtonView.setLabel(oNavButton.getLabelValue());
				oButtonView.setWidth(oUITypeDesc.getRoComponentWidth());
				oButtonView.setHeight(oUITypeDesc.getRoComponentHeight());
				p_oDomain.getXModeleFactory()
						.createPressedConnectionForNavButton(oButtonView,
								oNavButton.getNavigation());

				// TODO: Trouver un truc plus évident pour identifier la scene
				oSection.addSubView(oButtonView);
			}
		}

		this.computeButtonPositions(((MIOSViewController) oStoryBoard
				.getScenes().get(0).getController()), p_oScreenConfig,
				p_oDomain);
	}

	protected void computeNavigationDetail() {

	}

	/**
	 * Compute ios button id
	 * 
	 * @param p_sBaseName
	 *            button id
	 * @return name for ios button
	 */
	private String computeButtonId(String p_sBaseName) {
		// underscore in button ids causes troubles in xcode.
		return p_sBaseName.replaceAll("_", "-");
	}

	/**
	 * Compute positions of navigation buttons for a menu screen.
	 * 
	 * @param p_oViewController
	 *            view controller
	 * @param p_oScreenConfig
	 *            screen config
	 * @param p_oDomain
	 *            domain
	 */
	private void computeButtonPositions(MIOSViewController p_oViewController,
			ScreenConfig p_oScreenConfig,
			MIOSDomain<MIOSDictionnary, MIOSModeleFactory> p_oDomain) {

		IOSUITypeDescription oUiType = (IOSUITypeDescription) p_oDomain
				.getLanguageConf().getUiTypeDescriptions()
				.get(BUTTON_UI_TYPE_DESC);
		int iButtonHeight = oUiType.getRoComponentHeight();
		int iButtonWidth = oUiType.getRoComponentWidth();

		// a menu screen has only one section
		int iNbButton = p_oViewController.getFirstSection().subviewCount();

		int iPosX = (p_oScreenConfig.getWidth() - iButtonWidth) / 2;

		int iSpaceBetweenButtons = SPACE_BETWEEN_BUTTONS;
		if ((iNbButton * iButtonHeight) < p_oScreenConfig.getHeight()) {
			iSpaceBetweenButtons = (p_oScreenConfig.getHeight() - (iNbButton * iButtonHeight))
					/ (iNbButton + 1);
		}

		int iCurrentY = iSpaceBetweenButtons;
		for (MIOSView oMIOSView : p_oViewController.getFirstSection()
				.getSubViews()) {
			oMIOSView.setPosX(iPosX);
			oMIOSView.setPosY(iCurrentY);

			iCurrentY += iButtonHeight + iSpaceBetweenButtons;
		}
	}
}
