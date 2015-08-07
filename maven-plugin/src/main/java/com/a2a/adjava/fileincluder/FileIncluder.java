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
package com.a2a.adjava.fileincluder;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.impl.StaticLoggerBinder;






import com.a2a.adjava.AdjavaMavenPlugin;
import com.a2a.adjava.utils.FileTypeUtils;

/** 
 * @goal file-includer
 * @phase initialize
 */
public class FileIncluder extends AbstractMojo {

	/**
	 * @parameter expression="${project}"
	 * @required 
	 * @readonly
	 */
	private MavenProject project;
	
	/**
	 * @parameter expression="${csprojPath}"
	 * @required 
	 * @readonly
	 */
	private List<String> csprojPath;
	
	private static Map<File, List<File>> fileMap = new HashMap<File, List<File>>();

    private static List<String> excludeExtension = new ArrayList<String>();
	
	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		excludeExtension.add("shproj");
		excludeExtension.add("projitems");
		for(String path : csprojPath){
			File file = new File(path);	
			fileMap.put(file, Getfiles(GetPathWithoutFileName(file)));
		}
		AddToXml();	
	}
	
	
	private void AddToXml()
	{
		for(Entry<File, List<File>> dicEntry : fileMap.entrySet())
		{
			boolean isShared = false;
			if(GetFullExtension(dicEntry.getKey()).equals("projitems")){
				isShared = true;
			}
		    SAXReader reader = new SAXReader();
		    Document document;
			try {
				document = reader.read(dicEntry.getKey().getPath());
				Element elem = document.getRootElement();
				List<Element> myChildNode = (List<Element>)elem.elements("ItemGroup");
				for(int i = 0; i < 5; i++){
					myChildNode.get(i).clearContent();
				}
		        for(File file : dicEntry.getValue())
		        {
                    if (file.getAbsolutePath().contains("bin\\") || file.getAbsolutePath().contains("obj\\")) {
                        continue;
                    }

		        	String absolutepath = file.getPath().replace(GetPathWithoutFileName(dicEntry.getKey()), "");
					if(FileTypeUtils.XAML.equals(FilenameUtils.getExtension(file.getName()))){
		
						if(new File(file.getPath()+".cs").exists())
						{
			                Element compileNode = myChildNode.get(0).addElement("Compile", "http://schemas.microsoft.com/developer/msbuild/2003");
			                if(isShared)
			                {
				                compileNode.addAttribute("Include", "$(MSBuildThisFileDirectory)"+ absolutepath + ".cs");
			                }else
			                {
				                compileNode.addAttribute("Include", absolutepath + ".cs");
			                }
			                Element dependentUponNode = compileNode.addElement("DependentUpon","http://schemas.microsoft.com/developer/msbuild/2003");
			                dependentUponNode.setText(file.getName());
						}
	
						if("App.xaml".equals(file.getName()))
						{
							Element pageNode = myChildNode.get(1).addElement("ApplicationDefinition", "http://schemas.microsoft.com/developer/msbuild/2003");			                
							if(isShared)
			                {
								pageNode.addAttribute("Include", "$(MSBuildThisFileDirectory)"+ absolutepath);
			                }else
			                {
				                pageNode.addAttribute("Include", absolutepath);
			                }
			                Element subTypeNode = pageNode.addElement("SubType", "http://schemas.microsoft.com/developer/msbuild/2003");
			                subTypeNode.setText("Designer");
			                Element generatorNode = pageNode.addElement("Generator", "http://schemas.microsoft.com/developer/msbuild/2003");
			                generatorNode.setText("MSBuild:Compile");
						}
						else
						{
							Element pageNode = myChildNode.get(1).addElement("Page", "http://schemas.microsoft.com/developer/msbuild/2003");			                
							if(isShared)
			                {
								pageNode.addAttribute("Include", "$(MSBuildThisFileDirectory)"+ absolutepath);
			                }else
			                {
				                pageNode.addAttribute("Include", absolutepath);
			                }
			                Element subTypeNode = pageNode.addElement("SubType", "http://schemas.microsoft.com/developer/msbuild/2003");
			                subTypeNode.setText("Designer");
			                Element generatorNode = pageNode.addElement("Generator", "http://schemas.microsoft.com/developer/msbuild/2003");
			                generatorNode.setText("MSBuild:Compile");
						}
					}
					else if(FileTypeUtils.CSHARP.equals(FilenameUtils.getExtension(file.getName())) && GetFullExtension(file).indexOf('.') == -1){
		                Element compileNode = myChildNode.get(0).addElement("Compile", "http://schemas.microsoft.com/developer/msbuild/2003");			                
						if(isShared)
		                {
							compileNode.addAttribute("Include", "$(MSBuildThisFileDirectory)"+ absolutepath);
		                }else
		                {
			                compileNode.addAttribute("Include", absolutepath);
		                }
					}
					else if(FileTypeUtils.XML.equals(FilenameUtils.getExtension(file.getName()))){
		                Element none = myChildNode.get(2).addElement("Content", "http://schemas.microsoft.com/developer/msbuild/2003");			                
						if(isShared)
		                {
							none.addAttribute("Include", "$(MSBuildThisFileDirectory)"+ absolutepath);
		                }else
		                {
			                none.addAttribute("Include", absolutepath);
		                }
		                Element subTypeNode = none.addElement("SubType","http://schemas.microsoft.com/developer/msbuild/2003");
		                subTypeNode.setText("Designer");
					}
					else if(FileTypeUtils.RESX.equals(FilenameUtils.getExtension(file.getName()))){
		                Element compileNode = myChildNode.get(0).addElement("Compile", "http://schemas.microsoft.com/developer/msbuild/2003");			                
						if(isShared)
		                {
							compileNode.addAttribute("Include", "$(MSBuildThisFileDirectory)"+ absolutepath.replace(FileTypeUtils.RESX, "Designer.cs"));
		                }else
		                {
			                compileNode.addAttribute("Include", absolutepath.replace(FileTypeUtils.RESX, "Designer.cs"));
		                }
		                Element dependentUponNode = compileNode.addElement("DependentUpon","http://schemas.microsoft.com/developer/msbuild/2003");
		                dependentUponNode.setText(file.getName());
		                
		                Element embeddedResource = myChildNode.get(4).addElement("EmbeddedResource", "http://schemas.microsoft.com/developer/msbuild/2003");			                
						if(isShared)
		                {
							embeddedResource.addAttribute("Include", "$(MSBuildThisFileDirectory)"+ absolutepath);
		                }else
		                {
			                embeddedResource.addAttribute("Include", absolutepath);
		                }
		                Element generator = embeddedResource.addElement("Generator","http://schemas.microsoft.com/developer/msbuild/2003");
		                generator.setText("PublicResXFileCodeGenerator");
		                Element lastGenOutput = embeddedResource.addElement("LastGenOutput","http://schemas.microsoft.com/developer/msbuild/2003");
		                lastGenOutput.setText(GetFileNameWithoutExtension(file) + ".Designer.cs");
					}					
					else if(FileTypeUtils.RESW.equals(FilenameUtils.getExtension(file.getName()))){
		                Element content = myChildNode.get(3).addElement("PRIResource", "http://schemas.microsoft.com/developer/msbuild/2003");			                
						if(isShared)
		                {
							content.addAttribute("Include", "$(MSBuildThisFileDirectory)"+ absolutepath);
		                }else
		                {
			                content.addAttribute("Include", absolutepath);
		                }
					}
					else if(GetFullExtension(file).indexOf('.') == -1 && !excludeExtension.contains(GetFullExtension(file)))
					{		                
						if("Package.appxmanifest".equals(file.getName()))
						{
							Element content = myChildNode.get(3).addElement("AppxManifest", "http://schemas.microsoft.com/developer/msbuild/2003");			                
							if(isShared)
			                {
								content.addAttribute("Include", "$(MSBuildThisFileDirectory)"+ absolutepath);
			                }else
			                {
				                content.addAttribute("Include", "$(MSBuildThisFileDirectory)"+ absolutepath);
			                }
			                Element subTypeNode = content.addElement("SubType","http://schemas.microsoft.com/developer/msbuild/2003");
			                subTypeNode.setText("Designer");
						}
						else
						{
							Element content = myChildNode.get(3).addElement("Content", "http://schemas.microsoft.com/developer/msbuild/2003");			                
							if(isShared)
			                {
								content.addAttribute("Include", "$(MSBuildThisFileDirectory)"+ absolutepath);
			                }else
			                {
				                content.addAttribute("Include", "$(MSBuildThisFileDirectory)"+ absolutepath);
			                }
			                Element copyToOutputDirectory = content.addElement("CopyToOutputDirectory","http://schemas.microsoft.com/developer/msbuild/2003");
			                copyToOutputDirectory.setText("PreserveNewest");
						}
					}
		        }  
				try{
				    XMLWriter output = new XMLWriter(
				            new FileWriter(dicEntry.getKey()));
				        output.write( document );
				        output.close();
				        System.out.println("Completed");
				        }
			     catch(IOException e){System.out.println(e.getMessage());}
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private ArrayList<File> Getfiles(String path)
	{
		ArrayList<File> filesList = new ArrayList<File>();
		
		File folder = new File(path);
		Collection<File> files = FileUtils.listFiles(folder, new RegexFileFilter("^(?:(?!delete-safely).)*\\.(?!csproj)[^.]+$"), new RegexFileFilter("^((?!debug).)*$") );
		for(File file : files)
		{
			filesList.add(file);
		}
		return filesList;
	}
 	
 	private String GetFullExtension(File file)
	{
		String name = file.getName();
		int dot = name.indexOf('.');
		String extension = (dot == -1) ? "" : name.substring(dot+1);
		return extension;
	}
 	
 	private String GetFileNameWithoutExtension(File file)
	{
		String name = file.getName();
		int dot = name.lastIndexOf('.');
		String baseName = (dot == -1) ? "" : name.substring(0, dot);
		return baseName;
	}
	
	private String GetPathWithoutFileName(File file)
	{
		String name = file.getAbsolutePath();
		int dot = name.lastIndexOf(File.separatorChar);
		String path = (dot == -1) ? "" : name.substring(0, dot+1);

        System.out.println(path);
        
        return path;
	}
	

}
