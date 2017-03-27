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

import java.io.File;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import static com.a2a.adjava.utils.StrUtils.DOT;
import static com.a2a.adjava.utils.StrUtils.PATH_SEPARATOR_C;
import static com.a2a.adjava.utils.StrUtils.PATH_SEPARATOR_S;

/**
 * Utility class for file types
 * @author lmichenaud
 *
 */
public class FileTypeUtils {

	/**
	 * Extension separator
	 */
	public static final String EXTENSION_SEPARATOR = ".";

	/**
	 * Xml file extension
	 */
	public static final String XML = "xml";

	/**
	 * Java file extension
	 */
	public static final String JAVA = "java";

	/**
	 * Sql file extension
	 */
	public static final String SQL = "sql";

	/**
	 * Html file extension
	 */
	public static final String HTML = "html";

	/**
	 * Properties file extension
	 */
	public static final String PROPERTIES = "properties";

	/**
	 * Extension for ios interface
	 */
	public static final String IOS_INTERFACE_EXTENSION = "h";

	/**
	 * Extension for ios implementation
	 */
	public static final String IOS_IMPLEMENTATION_EXTENSION = "m";

	/**
	 * Extension for csharp interface
	 */
	public static final String CSHARP = "cs";

	/**
	 * Extension for xaml interface
	 */
	public static final String XAML = "xaml";

	/**
	 * Extension for xaml cs interface
	 */
	public static final String XAMLCSHARP = "xaml.cs";

	/**
	 * Extension for resw interface
	 */
	public static final String RESW = "resw";

	/**
	 * Extension for resx interface
	 */
	public static final String RESX = "resx";

	/**
	 * Extension for ios interface
	 */
	public static final String IOS_PREFIX_PCH = "Prefix.pch";

	/**
	 * Extension for ios strings resource
	 */
	public static final String IOS_STRINGS = "strings";

	/**
	 * Extension for javascript files
	 */
	public static final String JAVASCRIPT_EXTENSION = "js";

	/**
	 * Extension for typescript files
	 */
	public static final String TYPESCRIPT_EXTENSION = "ts";

	/**
	 * Extension for scss files
	 */
	public static final String SCSS_EXTENSION = "scss";

	/**
	 * Extension for json files
	 */
	public static final String JSON_EXTENSION = "json";

	/**
	 * Extension for html files
	 */
	public static final String HTML_EXTENSION = "html";

	/**
	 * Return true if file type is xml
	 * @param p_sFileName
	 * @return
	 */
	public static boolean isXmlFile( String p_sFileName ) {
		return FilenameUtils.isExtension(p_sFileName, XML);
	}

	/**
	 * Return true if file type is xaml
	 * @param p_sFileName
	 * @return
	 */
	public static boolean isXamlFile( String p_sFileName ) {
		return FilenameUtils.isExtension(p_sFileName, XAML);
	}

	/**
	 * Return true if file type is resx
	 * @param p_sFileName
	 * @return
	 */
	public static boolean isResxFile( String p_sFileName ) {
		return FilenameUtils.isExtension(p_sFileName, RESX);
	}

	/**
	 * Return true if file type is xml
	 * @param p_oFile file
	 * @return true if file type is xml
	 */
	public static boolean isXmlFile( File p_oFile ) {
		return isXmlFile(p_oFile.getName());
	}

	/**
	 * Return true if file type is html (.html)
	 * @param p_sFileName filename
	 * @return true if html file
	 */
	public static boolean isHtmlFile(File p_sFileName) {
		return isHtmlFile(p_sFileName.getName());
	}

	/**
	 * Return true if file type is scss (.scss)
	 * @param p_sFileName filename
	 * @return true if scss file
	 */
	public static boolean isScssFile(File p_sFileName) {
		return isScssFile(p_sFileName.getName());
	}

	/**
	 * Return true if file type is java
	 * @param p_sFileName
	 * @return true if java file
	 */
	public static boolean isJavaFile( String p_sFileName ) {
		return FilenameUtils.isExtension(p_sFileName, JAVA);
	}

	/**
	 * Return true if file type is java
	 * @param p_oFile file
	 * @return true if java file
	 */
	public static boolean isJavaFile(File p_oFile) {
		return isJavaFile(p_oFile.getName());
	}

	/**
	 * Return true if file type is csharp
	 * @param p_sFileName
	 * @return true if java file
	 */
	public static boolean isCSharpFile( String p_sFileName ) {
		return FilenameUtils.isExtension(p_sFileName, CSHARP);
	}

	/**
	 * Return true if file type is csharp
	 * @param p_oFile file
	 * @return true if java file
	 */
	public static boolean isCSharpFile(File p_oFile) {
		return isCSharpFile(p_oFile.getName());
	}

	/**
	 * Return true if file type is sql
	 * @param p_sFileName
	 * @return true if sql file
	 */
	public static boolean isSqlFile( String p_sFileName ) {
		return FilenameUtils.isExtension(p_sFileName, SQL);
	}

	/**
	 * Return true if file type is sql
	 * @param p_oFile file
	 * @return true if sql file
	 */
	public static boolean isSqlFile(File p_oFile) {
		return isSqlFile(p_oFile.getName());
	}

	/**
	 * Return true if file type is properties
	 * @param p_sFileName
	 * @return true if properties file
	 */
	public static boolean isPropertiesFile( String p_sFileName ) {
		return FilenameUtils.isExtension(p_sFileName, PROPERTIES);
	}

	/**
	 * Return true if file type is properties
	 * @param p_oFile file
	 * @return true if properties file
	 */
	public static boolean isPropertiesFile(File p_oFile) {
		return isPropertiesFile(p_oFile.getName());
	}

	/**
	 * Return true if file type is ios (.h or .m)
	 * @param p_sFileName filename
	 * @return true if ios file
	 */
	public static boolean isIosFile(String p_sFileName) {
		return FilenameUtils.isExtension(p_sFileName, IOS_INTERFACE_EXTENSION) || FilenameUtils.isExtension(p_sFileName, IOS_IMPLEMENTATION_EXTENSION) ||
				p_sFileName.endsWith(IOS_PREFIX_PCH);
	}


	/**
	 * Return true if file type is json (.json)
	 * @param p_sFileName filename
	 * @return true if json file
	 */
	public static boolean isJsonFile(String p_sFileName) {
		return FilenameUtils.isExtension(p_sFileName, JSON_EXTENSION);
	}


	/**
	 * Return true if file type is javascript (.js)
	 * @param p_sFileName filename
	 * @return true if js file
	 */
	public static boolean isJavascriptFile(String p_sFileName) {
		return FilenameUtils.isExtension(p_sFileName, JAVASCRIPT_EXTENSION);
	}

	/**
	 * Return true if file type is typescript (.ts)
	 * @param p_sFileName filename
	 * @return true if ts file
	 */
	public static boolean isTypescriptFile(String p_sFileName) {
		return FilenameUtils.isExtension(p_sFileName, TYPESCRIPT_EXTENSION);
	}

	/**
	 * Return true if file type is json (.json)
	 * @param p_sFileName filename
	 * @return true if json file
	 */
	public static boolean isJSONFile(String p_sFileName) {
		return FilenameUtils.isExtension(p_sFileName, JSON_EXTENSION);
	}

	/**
	 * Return true if file type is html (.html)
	 * @param p_sFileName filename
	 * @return true if html file
	 */
	public static boolean isHtmlFile(String p_sFileName) {
		return FilenameUtils.isExtension(p_sFileName, HTML_EXTENSION);
	}

	/**
	 * Return true if file type is scss (.scss)
	 * @param p_sFileName filename
	 * @return true if scss file
	 */
	public static boolean isScssFile(String p_sFileName) {
		return FilenameUtils.isExtension(p_sFileName, SCSS_EXTENSION);
	}

	/**
	 * Return true if file type is a string resource (.strings)
	 * @param p_oFile file
	 * @return true if ios string resource
	 */
	public static boolean isIosStringFile(File p_oFile) {
		return isIosStringFile(p_oFile.getName());
	}

	/**
	 * Return true if file type is a string resource (.strings)
	 * @param p_sFileName filename
	 * @return true if ios string resource
	 */
	public static boolean isIosStringFile(String p_sFileName) {
		return FilenameUtils.isExtension(p_sFileName, IOS_STRINGS);
	}

	/**
	 * Return true if file type is ios (.h or .m)
	 * @param p_oFile file
	 * @return true if file type is ios
	 */
	public static boolean isIosFile(File p_oFile) {
		return isIosFile(p_oFile.getName());
	}

	/**
	 * Return true if file type is json (.json)
	 * @param p_oFile file
	 * @return true if file type is json
	 */
	public static boolean isJsonFile(File p_oFile) {
		return isJsonFile(p_oFile.getName());
	}

	/**
	 * Return true if file type is javascript (.js)
	 * @param p_oFile file
	 * @return true if file type is javascript
	 */
	public static boolean isJavascriptFile(File p_oFile) {
		return isJavascriptFile(p_oFile.getName());
	}

	/**
	 * Return true if file type is typescript (.ts)
	 * @param p_oFile file
	 * @return true if file type is typescript
	 */
	public static boolean isTypescriptFile(File p_oFile) {
		return isTypescriptFile(p_oFile.getName());
	}

	/**
	 * Compute File for java class
	 * @param p_sSourceDir source dir
	 * @param p_sClassName class name
	 * @return file name
	 */
	public static String computeFilenameForJavaClass( String p_sSourceDir, String p_sClassName) {
		String sFilePath = StringUtils.join( p_sClassName.replace(DOT, PATH_SEPARATOR_C), EXTENSION_SEPARATOR, JAVA );
		return new File(p_sSourceDir, sFilePath).getPath();
	}

	/**
	 * Compute File for java class
	 * @param p_sPackage package name
	 * @param p_sFilebasename base filename (without extension)
	 * @return file name
	 */
	public static String computeFilenameForJavaProperties( String p_sPackage, String p_sFilebasename) {
		String sFilePath = StringUtils.join( p_sPackage.replace(DOT, PATH_SEPARATOR_C), PATH_SEPARATOR_S, p_sFilebasename, EXTENSION_SEPARATOR, PROPERTIES );
		return new File(sFilePath).getPath();
	}

	/**
	 * Compute File for xml file
	 * @param p_sSourceDir source dir
	 * @param p_sXmlName xml full name
	 * @return file name
	 */
	public static String computeFilenameForXmlClass(String p_sSourceDir, String p_sXmlName) {
		String sFilePath = StringUtils.join( p_sXmlName.replace(DOT, PATH_SEPARATOR_C), EXTENSION_SEPARATOR, XML );
		return new File(p_sSourceDir, sFilePath).getPath();
	}

	/**
	 * Compute File for resx file
	 * @param p_sSourceDir source dir
	 * @param p_sXmlName resx full name
	 * @return file name
	 */
	public static String computeFilenameForResxClass(String p_sSourceDir, String p_sXmlName) {
		String sFilePath = StringUtils.join( p_sXmlName.replace(DOT, PATH_SEPARATOR_C), EXTENSION_SEPARATOR, RESX );
		return new File(p_sSourceDir, sFilePath).getPath();
	}

	/**
	 * Compute File for resw file
	 * @param p_sSourceDir source dir
	 * @param p_sXmlName resw full name
	 * @return file name
	 */
	public static String computeFilenameForReswClass(String p_sSourceDir, String p_sXmlName) {
		String sFilePath = StringUtils.join( p_sXmlName.replace(DOT, PATH_SEPARATOR_C), EXTENSION_SEPARATOR, RESW );
		return new File(p_sSourceDir, sFilePath).getPath();
	}

	/**
	 * Compute filename for objective C interface
	 * @param p_sPackage package
	 * @param p_sBaseName class base name
	 * @param p_sSourceDir source dir
	 * @return filename
	 */
	public static String computeFilenameForIOSInterface(String p_sPackage, String p_sBaseName, String p_sSourceDir) {
		String sFilePath = StringUtils.join( p_sPackage.replace(DOT, PATH_SEPARATOR_C), PATH_SEPARATOR_S, p_sBaseName, EXTENSION_SEPARATOR, IOS_INTERFACE_EXTENSION );
		return new File(p_sSourceDir, sFilePath).getPath();
	}

	/**
	 * Compute filename for objective C implementation
	 * @param p_sPackage package
	 * @param p_sBaseName base name
	 * @param p_sSourceDir source dir
	 * @return filename
	 */
	public static String computeFilenameForIOSImpl(String p_sPackage, String p_sBaseName, String p_sSourceDir) {
		String sFilePath = StringUtils.join( p_sPackage.replace(DOT, PATH_SEPARATOR_C), PATH_SEPARATOR_S, p_sBaseName, EXTENSION_SEPARATOR, IOS_IMPLEMENTATION_EXTENSION );
		return new File(p_sSourceDir, sFilePath).getPath();
	}

	/**
	 * Compute filename for C Sharp implementation
	 * @param p_sPackage package
	 * @param p_sBaseName base name
	 * @param p_sSourceDir source dir
	 * @return filename
	 */
	public static String computeFilenameForCSharpImpl(String p_sPackage, String p_sBaseName, String p_sSourceDir) {
		String sFilePath = StringUtils.join( p_sPackage.replace(DOT, PATH_SEPARATOR_C), PATH_SEPARATOR_S, p_sBaseName, EXTENSION_SEPARATOR, CSHARP );
		return new File(p_sSourceDir, sFilePath).getPath();
	}

	/**
	 * Compute filename for C Sharp interface
	 * @param p_sPackage package
	 * @param p_sBaseName base name
	 * @param p_sSourceDir source dir
	 * @return filename
	 */
	public static String computeFilenameForCSharpInterface(String p_sPackage, String p_sBaseName, String p_sSourceDir) {
		String sFilePath = StringUtils.join( p_sPackage.replace(DOT, PATH_SEPARATOR_C), PATH_SEPARATOR_S, p_sBaseName, EXTENSION_SEPARATOR, CSHARP );
		return new File(p_sSourceDir, sFilePath).getPath();
	}

	/**
	 * Compute filename for xaml layout
	 * @param p_sPackage package
	 * @param p_sBaseName base name
	 * @param p_sSourceDir source dir
	 * @return filename
	 */
	public static String computeFilenameForXaml(String p_sPackage, String p_sBaseName, String p_sSourceDir) {
		String sFilePath = StringUtils.join( p_sPackage.replace(DOT, PATH_SEPARATOR_C), PATH_SEPARATOR_S, p_sBaseName, EXTENSION_SEPARATOR, XAML );
		return new File(p_sSourceDir, sFilePath).getPath();
	}

	/**
	 * Compute filename for JavaScript class
	 * @param p_sSourceDir source dir
	 * @param p_sClassName class name
	 * @return file name
	 */
	public static String computeFilenameForJS( String p_sSourceDir, String p_sClassName) {
		String sFilePath = StringUtils.join( p_sClassName.replace(DOT, PATH_SEPARATOR_C), EXTENSION_SEPARATOR, JAVASCRIPT_EXTENSION );
		return new File(p_sSourceDir, sFilePath).getPath();
	}

	/**
	 * Compute filename for TypeScript class
	 * @param p_sSourceDir source dir
	 * @param p_sClassName class name
	 * @return file name
	 */
	public static String computeFilenameForTS( String p_sSourceDir, String p_sClassName) {
		String sFilePath = StringUtils.join( p_sClassName.replace(DOT, PATH_SEPARATOR_C), EXTENSION_SEPARATOR, TYPESCRIPT_EXTENSION );
		return new File(p_sSourceDir, sFilePath).getPath();
	}

	/**
	 * Compute filename for partial HMTL5 class
	 * @param p_sSourceDir source dir
	 * @param p_sClassName class name
	 * @return file name
	 */
	public static String computeFilenameForHTML( String p_sSourceDir, String p_sClassName) {
		String sFilePath = StringUtils.join(p_sClassName.replace(DOT, PATH_SEPARATOR_C), EXTENSION_SEPARATOR, HTML_EXTENSION );
		return new File(p_sSourceDir, sFilePath).getPath();
	}

	/**
	 * Compute filename for scss class
	 * @param p_sSourceDir source dir
	 * @param p_sClassName class name
	 * @return file name
	 */
	public static String computeFilenameForSCSS( String p_sSourceDir, String p_sClassName) {
		String sFilePath = StringUtils.join(p_sClassName.replace(DOT, PATH_SEPARATOR_C), EXTENSION_SEPARATOR, SCSS_EXTENSION );
		return new File(p_sSourceDir, sFilePath).getPath();
	}

	/**
	 * Compute filename for xaml C Sharp implementation
	 * @param p_sPackage package
	 * @param p_sBaseName base name
	 * @param p_sSourceDir source dir
	 * @return filename
	 */
	public static String computeFilenameForXamlCSharpImpl(String p_sPackage, String p_sBaseName, String p_sSourceDir) {
		String sFilePath = StringUtils.join( p_sPackage.replace(DOT, PATH_SEPARATOR_C), PATH_SEPARATOR_S, p_sBaseName, EXTENSION_SEPARATOR, XAMLCSHARP );
		return new File(p_sSourceDir, sFilePath).getPath();
	}

	/**
	 * Compute filename without extension
	 * @param p_sPackage package
	 * @param p_sBaseName base name
	 * @param p_sSourceDir source dir
	 * @return filename
	 */
	public static String computeFilenameForSql(String p_sPackage, String p_sBaseName, String p_sSourceDir) {
		String sFilePath = StringUtils.join( p_sPackage.replace(DOT, PATH_SEPARATOR_C), PATH_SEPARATOR_S, p_sBaseName, EXTENSION_SEPARATOR, SQL );
		return new File(p_sSourceDir, sFilePath).getPath();
	}

	/**
	 * Compute File for html file
	 * @param p_sSourceDir source dir
	 * @param p_sHtmlName html full name
	 * @return file name
	 */
	public static String computeFilenameForHtmlClass(String p_sSourceDir, String p_sHtmlName) {
		String sFilePath = StringUtils.join( p_sHtmlName.replace(DOT, PATH_SEPARATOR_C), EXTENSION_SEPARATOR, HTML );
		return new File(p_sSourceDir, sFilePath).getPath();
	}
}
