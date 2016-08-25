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
package com.a2a.adjava.languages.ionic2.extractors.views;

import java.util.HashMap;
import java.util.Map;

import com.a2a.adjava.languages.ionic2.xmodele.MH5Dictionary;
import com.a2a.adjava.languages.ionic2.xmodele.MH5Domain;
import com.a2a.adjava.languages.ionic2.xmodele.MH5ModeleFactory;
import com.a2a.adjava.languages.ionic2.xmodele.MH5ScreenView;
import com.a2a.adjava.languages.ionic2.xmodele.MH5View;
import com.a2a.adjava.xmodele.MPage;
import com.a2a.adjava.xmodele.MScreen;
import com.a2a.adjava.xmodele.ui.navigation.MNavigation;
import com.a2a.adjava.xmodele.ui.navigation.MNavigationType;
import com.a2a.adjava.xmodele.ui.viewmodel.ViewModelType;

public class ExitStateDlg {

	/**
	 * fill the "exitstate" field for screen views
	 * @param p_oPage
	 * @return
	 */
	public void computeExitStateForScreen(MScreen p_oScreen, MH5ScreenView r_oNewView, MH5Domain<MH5Dictionary, MH5ModeleFactory> p_oDomain) {
		
		StringBuilder exitState = new StringBuilder();
		Map<String, String> exitStateParams = new HashMap<>();

		if(!r_oNewView.getNavigationToScreenList().isEmpty()){
			MScreen parentScreen = r_oNewView.getNavigationToScreenList().get(0).getSource();
			
			exitState.append(parentScreen.getName());
			if(!parentScreen.getPages().isEmpty()){
				exitState.append(".content");

				exitStateParams = this.computeExitStateParams(parentScreen);
				
			}
		}
		else{
			MScreen mainscreen = p_oDomain.getDictionnary().getMainScreen();
			
			exitState.append(mainscreen.getName());
			
			if(!mainscreen.getPages().isEmpty()){
				exitState.append(".content");
				
				exitStateParams = this.computeExitStateParams(mainscreen);
				
			} else {
				boolean addSlash = false;
				if(!r_oNewView.getNestedSubview().isEmpty()){
					for(MNavigation oNavFormScreen : r_oNewView.getNavigationFromScreenList()){
						if(oNavFormScreen.getNavigationType().equals(MNavigationType.NAVIGATION_DETAIL)){
							addSlash = true;
						}
					}	
				}
				if(addSlash){
					exitState.append('/');
				}
			}
		}

		r_oNewView.setExitState(exitState.toString(), exitStateParams);
	}
	
	/**
	 * fill the "exitstate" field for panels views
	 * @param p_oPage
	 * @return
	 */
	public void computeExitStateForPanel(MPage p_oPage, MH5View r_oNewView, MScreen p_oMainScreen) {
		
		StringBuilder exitState = new StringBuilder();
		Map<String, String> exitStateParams = new HashMap<>();
		
		exitState.append(p_oMainScreen.getName());
		
		if(!p_oMainScreen.getPages().isEmpty()){
			exitState.append(".content");
			
			exitStateParams = this.computeExitStateParams(p_oMainScreen);
			
		}

		r_oNewView.setExitState(exitState.toString(), exitStateParams);	
	}
	

	/**
	 * Compute exit state params for this screen
	 * @param p_oScreen
	 * @return
	 */
	private Map<String, String> computeExitStateParams(MScreen p_oScreen) {
		Map<String, String> r_exitStateParams = new HashMap<>();
		if (!p_oScreen.isWorkspace()) {
			if(p_oScreen.getPages().size()==1){
				r_exitStateParams.put("itemId", "new");
			}
			else{
				int sectionId = 0;
				for (int i = 0; i < p_oScreen.getPages().size(); i++) {
					MPage page = p_oScreen.getPages().get(i);
					if ( !page.getViewModelImpl().getType().equals(ViewModelType.LIST_1) 
							&& !page.getViewModelImpl().getType().equals(ViewModelType.LIST_2) 
							&& !page.getViewModelImpl().getType().equals(ViewModelType.LIST_3) ) {
						sectionId++;
						r_exitStateParams.put("section"+(sectionId), page.getName());
					}
				}
			}
		}
		return r_exitStateParams;
	}
}
