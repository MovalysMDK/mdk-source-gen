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
package com.a2a.adjava.generator.core.fiximport;

import japa.parser.ast.ImportDeclaration;
import japa.parser.ast.PackageDeclaration;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.visitor.VoidVisitorAdapter;

/**
 * @author lmichenaud
 * 
 */
public class TypeVisitor extends VoidVisitorAdapter<TypeVisitorResult> {

	/**
	 * {@inheritDoc}
	 * @see japa.parser.ast.visitor.VoidVisitorAdapter#visit(japa.parser.ast.PackageDeclaration,
	 *      java.lang.Object)
	 */
	@Override
	public void visit(PackageDeclaration p_oPackageDeclaration, TypeVisitorResult p_oTypeVisitorResult) {
		p_oTypeVisitorResult.setPackageName(p_oPackageDeclaration.getName().toString());
		super.visit(p_oPackageDeclaration, p_oTypeVisitorResult);
	}

	/**
	 * {@inheritDoc}
	 * @see japa.parser.ast.visitor.VoidVisitorAdapter#visit(japa.parser.ast.ImportDeclaration, java.lang.Object)
	 */
	@Override
	public void visit(ImportDeclaration p_oImportDeclaration, TypeVisitorResult p_oTypeVisitorResult) {
		super.visit(p_oImportDeclaration, p_oTypeVisitorResult);
		p_oTypeVisitorResult.addImport(p_oImportDeclaration.getName().toString());
	}
	
	/**
	 * {@inheritDoc}
	 * @see japa.parser.ast.visitor.VoidVisitorAdapter#visit(japa.parser.ast.body.ClassOrInterfaceDeclaration,
	 *      java.lang.Object)
	 */
	@Override
	public void visit(ClassOrInterfaceDeclaration p_oTypeDeclaration, TypeVisitorResult p_oTypeVisitorResult) {
		p_oTypeVisitorResult.addType(p_oTypeDeclaration.getName());
		if (p_oTypeDeclaration.getMembers() != null) {
			p_oTypeVisitorResult.appendPath(p_oTypeDeclaration.getName());
			for (BodyDeclaration oMember : p_oTypeDeclaration.getMembers()) {
				oMember.accept(this, p_oTypeVisitorResult);
			}
			p_oTypeVisitorResult.popPath();
		}
	}
}
