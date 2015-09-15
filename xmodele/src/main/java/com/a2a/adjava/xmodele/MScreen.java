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

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;

import com.a2a.adjava.AdjavaException;
import com.a2a.adjava.messages.MessageHandler;
import com.a2a.adjava.xmodele.ui.component.MWorkspaceType;
import com.a2a.adjava.xmodele.ui.menu.MMenu;
import com.a2a.adjava.xmodele.ui.menu.MMenuActionItem;
import com.a2a.adjava.xmodele.ui.menu.MMenuItem;

/**
 * <p>
 * 	Représentation d'un écran dans Movlays Framework.
 * 	Un écran contient toujours une page par défaut. 
 * 	Cette page est considérée comme la page principale de l'écran.
 * 	Un écran peut avoir plusieurs pages affichées, c'est le cas lors de l'utilisation d'un composant de type 
 * 	<em>Workspace</em> par exemple.
 * </p>
 *
 * <p>Copyright (c) 2011</p>
 * <p>Company: Adeuza</p>
 *
 * @author fbourlieux
 * @since MF-Annapurna
 */
public class MScreen extends SClass<MViewModelInterface,MMethodSignature> {

	/**
	 * Panel list
	 */
	private List<MPage> pages = new ArrayList<MPage>();
	
	/**
	 * Panel map. 
	 */
	private Map<String,MPage> pagesMap = new HashMap<String, MPage>();
	
	/**
	 * Menu map
	 */
	private Map<String,MMenu> menus = new HashMap<>();

	/**
	 * True if main screen of application
	 */
	private boolean main = false;
	
	/**
	 * Screen layout
	 */
	private MLayout layout ;

	/**
	 * Screen View Model
	 */
	private MViewModelImpl viewModel ;

	/**
	 * True if screen is a workspace
	 */
	private boolean workspace = false;
	
	/**
	 * Type of workspace
	 */
	private MWorkspaceType workspaceType = MWorkspaceType.DETAIL ;
	
	/**
	 * True if screen is a search screen.
	 */
	private boolean searchScreen = false;
	
	/**
	 * True if comment in the screen
	 */
	private boolean comment = false;

	/**
	 * Contructor.
	 * @param p_sUmlName uml name
	 * @param p_sName name
	 * @param p_oPackage screen package
	 */
	protected MScreen(String p_sUmlName, String p_sName, MPackage p_oPackage) {
		super("screen", p_sUmlName, p_sName, p_oPackage);
	}
	
	/**
	 * Return the object <em>workspace</em>.
	 * @return Objet workspace
	 */
	public boolean isWorkspace() {
		return this.workspace;
	}

	/**
	 * Set the object <em>workspace</em>.
	 * @param p_oWorkspace Objet workspace
	 */
	public void setWorkspace(boolean p_oWorkspace) {
		this.workspace = p_oWorkspace;
	}

	/**
	 * Return the object <em>comment</em>.
	 * @return Objet comment
	 */
	public boolean isComment() {
		return this.comment;
	}

	/**
	 * Set the object <em>comment</em>.
	 * @param p_oComment Objet comment
	 */
	public void setComment(boolean p_oComment) {
		this.comment = p_oComment;
	}

	/**
	 * @return
	 */
	public boolean isSearchScreen() {
		return this.searchScreen;
	}

	/**
	 * @param p_bSearchScreen
	 */
	public void setSearchScreen(boolean p_bSearchScreen) throws AdjavaException {
		if ( this.workspace && p_bSearchScreen ) {
			throw new AdjavaException("A workspace screen can not be a search screen. Screen: {}", this.getUmlName());
		}
		else {
			this.searchScreen = p_bSearchScreen;			
		}
	}

	/**
	 * Définis si l'écran est le principal ou non dans l'application
	 * @param p_bMain true = écran principal, false sinon
	 */
	public void setMain(boolean p_bMain) {
		this.main = p_bMain;
	}
	
	/**
	 * Retourne le menu du screen courant dont l'identifiant correspond à celui envoyé en paramètre.
	 * @param p_sId l'identifiant pour réaliser la recherche.
	 * @return le Menu correspondant si on en trouve un, null sinon
	 */
	public MMenu getMenu( String p_sId ) {
		return this.menus.get(p_sId);
	}
	
	/**
	 * Retourne la liste des menus à ajouter au Screen courant.
	 * @return une collection d'objet MMenu
	 */
	public Collection<MMenu> getMenus() {
		return this.menus.values();
	}
	
	/**
	 * Ajoute un nouveau menu au screen.
	 * @param p_oMenu l'objet MMenu à ajouter
	 */
	public void addMenu( MMenu p_oMenu ) {
		this.menus.put(p_oMenu.getId(), p_oMenu);
		for( MMenuItem oMenuItem: p_oMenu.getMenuItems()) {
			if (! (oMenuItem instanceof MMenuActionItem) ) {
				this.addImport(oMenuItem.getNavigation().getTarget().getFullName());
			}
		}
	}

	/**
	 * Ajoute une page à l'écran.if (this.main) {
			p_xElement.addElement("main").setText("true");
		}
		else {
			p_xElement.addElement("main").setText("false");
		}
	 * @param p_oPage Objet de type <em>MPage</em>
	 */
	public void add(MPage p_oPage){
		if (this.pagesMap.containsKey(p_oPage.getName())) {
			MessageHandler.getInstance().addError(
				"Erreur de modélisation : La page '{}' existe déjà dans la liste d'objets MPage de l'écran", 
					p_oPage.getName());
		}else{
			this.pages.add(p_oPage);
			this.pagesMap.put(p_oPage.getName(), p_oPage);
		}
	}
	
	/**
	 * Ajoute une page à l'écran en première position.
	 * @param p_oPage Objet de type <em>MPage</em>
	 */
	public void insert(MPage p_oPage, int p_iPos) {
		if (this.pagesMap.containsKey(p_oPage.getName())) {
			MessageHandler.getInstance().addError(
				"Erreur de modélisation : La page '{}' existe déjà dans la liste d'objets MPage de l'écran", 
					p_oPage.getName());
		}else{
			this.pages.add(p_iPos, p_oPage);
			this.pagesMap.put(p_oPage.getName(), p_oPage);
		}
	}
	
	/**
	 * @return
	 */
	public boolean hasMasterPage() {
		return !this.pages.isEmpty();
	}
	
	/**
	 * <p>
	 * 	Retourne le nombre de pages contenues dans un écran.
	 * 	Attention, il ne peux pas y avoir moins de 1 page.
	 * </p>
	 * @return entier de 1 à n
	 */
	public int getPageCount() {
		return this.pages.size();
	}
	
	/**
	 * <p>
	 * 	Retourne la page principale de l'écran.
	 * 	Il ne peut pas ne pas y avoir de page principale car l'écran contient à minima une page.
	 * 	La page principale sera toujours le premier objet <em>MPage</em> de la liste.
	 * </p>
	 * @return Objet de type <em>Mpage</em>
	 */
	public MPage getMasterPage(){
		return this.pages.get(0);
	}
	
	/**
	 * Retourne la page correspondant au nom envoyé en paramètre.
	 * @param p_sUmlName le nom de la page définis dans le modèle UML.
	 * @return objet de type <em>MPage</em>
	 */
	public MPage getPageByUmlName(String p_sUmlName){
		return this.pagesMap.get(p_sUmlName);
	}
	
	/**
	 * retourne la liste des pages affichées dans l'écran.
	 * @return une ArrayList d'objets <em>MPage</em>
	 */
	public List<MPage> getPages(){
		return this.pages;
	}
	
	/**
	 * retourne la liste des pages affichées dans l'écran.
	 * @return une ArrayList d'objets <em>MPage</em>
	 */
	public MPage getPanel( int p_iIndex ){
		return this.pages.get(p_iIndex);
	}
	
	/**
	 * <p>
	 * 	Supprime toutes les pages de l'écran.
	 * 	Attention, cette méthode doit obligatoirement être suivie de l'ajout d'une page dans l'écran.
	 * 	Un écran DOIT TOUJOURS avoir une page par défaut.
	 * </p>
	 */
	public void resumePageList(){
		this.pages = new ArrayList<MPage>();
		this.pagesMap = new HashMap<String, MPage>();
	}
	
	/**
	 * Return the object <em>layout</em>.
	 * @return Objet layout
	 */
	public MLayout getLayout() {
		return this.layout;
	}

	/**
	 * Set the object <em>layout</em>.
	 * @param p_oLayout Objet layout
	 */
	public void setLayout(MLayout p_oLayout) {
		this.layout = p_oLayout;
		if(this.layout != null)
		{
			this.layout.setScreen(new WeakReference<MScreen>(this));
		}
	}
	
	/**
	 * Return the object <em>viewModel</em>.
	 * @return Objet viewModel
	 */
	public MViewModelImpl getViewModel() {
		return this.viewModel;
	}

	/**
	 * Set the object <em>viewModel</em>.
	 * @param p_oViewModel Objet viewModel
	 */
	public void setViewModel(MViewModelImpl p_oViewModel) {
		this.viewModel = p_oViewModel;
	}

	/**
	 * @return
	 */
	public boolean isMain() {
		return this.main;
	}
	
	/**
	 * @return
	 */
	public boolean isMultiPanel() {
		return this.getPageCount() > 1 ;
	}
	
	/**
	 * Return type of workspace
	 * @return
	 */
	public MWorkspaceType getWorkspaceType() {
		return this.workspaceType;
	}

	/**
	 * Set type of workspace
	 * @param p_oWorkspaceType workspace type
	 */
	public void setWorkspaceType(MWorkspaceType p_oWorkspaceType) {
		this.workspaceType = p_oWorkspaceType;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void toXmlInsertBeforeDocumentation(Element p_xElement) {
		super.toXmlInsertBeforeDocumentation(p_xElement);
		if (this.main) {
			p_xElement.addElement("main").setText("true");
		} else {
			p_xElement.addElement("main").setText("false");
		}
		if (this.workspace) {
			p_xElement.addElement("workspace").setText("true");
			p_xElement.addElement("workspace-type").setText(this.workspaceType.name());
		} else {
			p_xElement.addElement("workspace").setText("false");
		}
		p_xElement.addElement("comment").setText(Boolean.toString(this.comment));
		if (this.isMultiPanel()){
			p_xElement.addElement("multi-panel").setText("true");
		} else {
			p_xElement.addElement("multi-panel").setText("false");
		}
		p_xElement.addElement("search-screen").setText(Boolean.toString(this.searchScreen));
		p_xElement.addElement("name-lowercase").setText(this.getName().toLowerCase());

		if (this.layout != null) {
			p_xElement.addElement("screenname").setText(this.layout.getName());
		}

		if (this.viewModel!=null) {
			p_xElement.add(this.viewModel.toXml());
			if (this.viewModel.getMasterInterface()!=null) {
				p_xElement.addElement("vm").setText(this.viewModel.getMasterInterface().getName());
				p_xElement.add(this.viewModel.getMasterInterface().toXml());
			}
		}
		Element xPages = p_xElement.addElement("pages");
		int iPos = 1;
		for (MPage oCurrentPage : this.pages){
			Element xPage = oCurrentPage.toXml();
			//Element xPage = xPages.addElement("page");
			xPage.addElement("pagename").setText(oCurrentPage.getName());
			xPage.addAttribute("pos", String.valueOf(iPos));
			xPages.add(xPage);
			iPos++;
		}
		Element xMenus = p_xElement.addElement("menus");
		for( MMenu oMenu : this.menus.values()) {
			xMenus.add(oMenu.toXml());
		}
	}
}
