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
package com.a2a.adjava.languages.ios.xmodele;

import com.a2a.adjava.uml.UmlClass;
import com.a2a.adjava.xmodele.MPackage;
import com.a2a.adjava.xmodele.MPage;
import com.a2a.adjava.xmodele.MScreen;
import com.a2a.adjava.xmodele.MViewModelImpl;

/**
 * Specific class for a iOS Page.
 * It does nothing but it allows to identify a MPage for iOS
 * @author qLagarde
 *
 */
public class MIOSPage extends MPage {
	
	public MIOSPage(MScreen p_oParent, String p_sPageName, UmlClass p_oUmlPage,
			MPackage p_oPackage, MViewModelImpl p_oVmImpl, boolean p_bTitled) {
		super(p_oParent, p_sPageName, p_oUmlPage, p_oPackage, p_oVmImpl, p_bTitled);
		// TODO Auto-generated constructor stub
	}

	public static final String MM_IOS_NO_TABLE= "Mm_iOS_noTableView";


}
