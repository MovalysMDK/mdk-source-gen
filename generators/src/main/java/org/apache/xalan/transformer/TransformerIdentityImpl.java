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
/*
 * DONT DELETE THIS FILE.
 * This is a patch version that fixes a xalan bug when file path contains a space. 
 */

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the  "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/*
 * $Id: TransformerIdentityImpl.java 575747 2007-09-14 16:28:37Z kcormier $
 */
package org.apache.xalan.transformer;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Hashtable;
import java.util.Properties;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.xalan.res.XSLMessages;
import org.apache.xalan.res.XSLTErrorResources;
import org.apache.xalan.templates.OutputProperties;
import org.apache.xml.serializer.Serializer;
import org.apache.xml.serializer.SerializerFactory;
import org.apache.xml.serializer.Method;
import org.apache.xml.utils.DOMBuilder;
import org.apache.xml.utils.XMLReaderManager;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.ext.LexicalHandler;

/**
 * This class implements an identity transformer for
 * {@link javax.xml.transform.sax.SAXTransformerFactory#newTransformerHandler()}
 * and {@link javax.xml.transform.TransformerFactory#newTransformer()}.  It
 * simply feeds SAX events directly to a serializer ContentHandler, if the
 * result is a stream.  If the result is a DOM, it will send the events to
 * {@link org.apache.xml.utils.DOMBuilder}.  If the result is another
 * content handler, it will simply pass the events on.
 */
public class TransformerIdentityImpl extends Transformer
        implements TransformerHandler, DeclHandler
{

  /**
   * Constructor TransformerIdentityImpl creates an identity transform.
   *
   */
  public TransformerIdentityImpl(boolean p_bSecureProcessing)
  {
    m_outputFormat = new OutputProperties(Method.XML);
    m_isSecureProcessing = p_bSecureProcessing;
  }

  /**
   * Constructor TransformerIdentityImpl creates an identity transform.
   *
   */
  public TransformerIdentityImpl()
  {
    this(false);
  }

  /**
   * Enables the user of the TransformerHandler to set the
   * to set the Result for the transformation.
   *
   * @param p_oResult A Result instance, should not be null.
   *
   * @throws IllegalArgumentException if result is invalid for some reason.
   */
  public void setResult(Result p_oResult) throws IllegalArgumentException
  {
    if(null == p_oResult)
      throw new IllegalArgumentException(XSLMessages.createMessage(XSLTErrorResources.ER_RESULT_NULL, null)); //"Result should not be null");        
    m_result = p_oResult;
  }

  /**
   * Set the base ID (URI or system ID) from where relative
   * URLs will be resolved.
   * @param p_sSystemID Base URI for the source tree.
   */
  public void setSystemId(String p_sSystemID)
  {
    m_systemID = p_sSystemID;
  }

  /**
   * Get the base ID (URI or system ID) from where relative
   * URLs will be resolved.
   * @return The systemID that was set with {@link #setSystemId}.
   */
  public String getSystemId()
  {
    return m_systemID;
  }

  /**
   * Get the Transformer associated with this handler, which
   * is needed in order to set parameters and output properties.
   *
   * @return non-null reference to the transformer.
   */
  public Transformer getTransformer()
  {
    return this;
  }

  /**
   * Reset the status of the transformer.
   */
  public void reset()
  {
    m_flushedStartDoc = false;
    m_foundFirstElement = false;
    m_outputStream = null;
    clearParameters();
    m_result = null;
    m_resultContentHandler = null;
    m_resultDeclHandler = null;
    m_resultDTDHandler = null;
    m_resultLexicalHandler = null;
    m_serializer = null;
    m_systemID = null;
    m_URIResolver = null;
    m_outputFormat = new OutputProperties(Method.XML);
  }

  /**
   * Create a result ContentHandler from a Result object, based
   * on the current OutputProperties.
   *
   * @param p_oOutputTarget Where the transform result should go,
   * should not be null.
   *
   * @return A valid ContentHandler that will create the
   * result tree when it is fed SAX events.
   *
   * @throws TransformerException
   */
  private void createResultContentHandler(Result p_oOutputTarget)
          throws TransformerException
  {

    if (p_oOutputTarget instanceof SAXResult)
    {
      SAXResult oSaxResult = (SAXResult) p_oOutputTarget;

      m_resultContentHandler = oSaxResult.getHandler();
      m_resultLexicalHandler = oSaxResult.getLexicalHandler();

      if (m_resultContentHandler instanceof Serializer)
      {

        // Dubious but needed, I think.
        m_serializer = (Serializer) m_resultContentHandler;
      }
    }
    else if (p_oOutputTarget instanceof DOMResult)
    {
      DOMResult oDomResult = (DOMResult) p_oOutputTarget;
      Node oOutputNode = oDomResult.getNode();
      Node oNextSibling = oDomResult.getNextSibling();
      Document oDoc;
      short iType;

      if (null != oOutputNode)
      {
    	  iType = oOutputNode.getNodeType();
    	  oDoc = (Node.DOCUMENT_NODE == iType)
              ? (Document) oOutputNode : oOutputNode.getOwnerDocument();
      }
      else
      {
        try
        {
          DocumentBuilderFactory oDbf = DocumentBuilderFactory.newInstance();

          oDbf.setNamespaceAware(true);

          if (m_isSecureProcessing)
          {
            try
            {
            	oDbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            }
            catch (ParserConfigurationException oPce) {
            	// We don't care.
            }
          }

          DocumentBuilder oDb = oDbf.newDocumentBuilder();

          oDoc = oDb.newDocument();
        }
        catch (ParserConfigurationException oPce)
        {
          throw new TransformerException(oPce);
        }

        oOutputNode = oDoc;
        iType = oOutputNode.getNodeType();

        ((DOMResult) p_oOutputTarget).setNode(oOutputNode);
      }

      DOMBuilder oDomBuilder =
        (Node.DOCUMENT_FRAGMENT_NODE == iType)
        ? new DOMBuilder(oDoc, (DocumentFragment) oOutputNode)
        : new DOMBuilder(oDoc, oOutputNode);
      
      if (oNextSibling != null)
    	  oDomBuilder.setNextSibling(oNextSibling);
      
      m_resultContentHandler = oDomBuilder;
      m_resultLexicalHandler = oDomBuilder;
    }
    else if (p_oOutputTarget instanceof StreamResult)
    {
      StreamResult oStreamResult = (StreamResult) p_oOutputTarget;

      try
      {
        Serializer oSerializer =
          SerializerFactory.getSerializer(m_outputFormat.getProperties());

        m_serializer = oSerializer;

        if (null != oStreamResult.getWriter())
        	oSerializer.setWriter(oStreamResult.getWriter());
        else if (null != oStreamResult.getOutputStream())
        	oSerializer.setOutputStream(oStreamResult.getOutputStream());
        else if (null != oStreamResult.getSystemId())
        {
          //PATCH
          //String fileURL = sresult.getSystemId();
           String sFileURL = URLDecoder.decode(oStreamResult.getSystemId(), "UTF-8");

          if (sFileURL.startsWith("file:///")) {
            if (sFileURL.substring(8).indexOf(":") >0) {
            	sFileURL = sFileURL.substring(8);
            } else  {
            	sFileURL = sFileURL.substring(7);
            }
          } else if (sFileURL.startsWith("file:/")) {
            if (sFileURL.substring(6).indexOf(":") >0) {
            	sFileURL = sFileURL.substring(6);
            } else {
            	sFileURL = sFileURL.substring(5);
            }
          }

          m_outputStream = new java.io.FileOutputStream(sFileURL);
          oSerializer.setOutputStream(m_outputStream);
        }
        else
          throw new TransformerException(XSLMessages.createMessage(XSLTErrorResources.ER_NO_OUTPUT_SPECIFIED, null)); //"No output specified!");

        m_resultContentHandler = oSerializer.asContentHandler();
      }
      catch (IOException oIoe)
      {
        throw new TransformerException(oIoe);
      }
    }
    else
    {
      throw new TransformerException(XSLMessages.createMessage(XSLTErrorResources.ER_CANNOT_TRANSFORM_TO_RESULT_TYPE, new Object[]{p_oOutputTarget.getClass().getName()})); //"Can't transform to a Result of type "
                                    // + outputTarget.getClass().getName()
                                    // + "!");
    }

    if (m_resultContentHandler instanceof DTDHandler)
      m_resultDTDHandler = (DTDHandler) m_resultContentHandler;
    
    if (m_resultContentHandler instanceof DeclHandler)
      m_resultDeclHandler = (DeclHandler) m_resultContentHandler;

    if (m_resultContentHandler instanceof LexicalHandler)
      m_resultLexicalHandler = (LexicalHandler) m_resultContentHandler;
  }

  /**
   * Process the source tree to the output result.
   * @param p_oSource  The input for the source tree.
   *
   * @param p_oOutputTarget The output target.
   *
   * @throws TransformerException If an unrecoverable error occurs
   * during the course of the transformation.
   */
  public void transform(Source p_oSource, Result p_oOutputTarget)
          throws TransformerException
  {

    createResultContentHandler(p_oOutputTarget);
    
    /*
     * According to JAXP1.2, new SAXSource()/StreamSource()
     * should create an empty input tree, with a default root node. 
     * new DOMSource()creates an empty document using DocumentBuilder.
     * newDocument(); Use DocumentBuilder.newDocument() for all 3 situations,
     * since there is no clear spec. how to create an empty tree when
     * both SAXSource() and StreamSource() are used.
     */
    if ((p_oSource instanceof StreamSource && p_oSource.getSystemId()==null &&
       ((StreamSource)p_oSource).getInputStream()==null &&
       ((StreamSource)p_oSource).getReader()==null)||
       (p_oSource instanceof SAXSource &&
       ((SAXSource)p_oSource).getInputSource()==null &&
       ((SAXSource)p_oSource).getXMLReader()==null )||
       (p_oSource instanceof DOMSource && ((DOMSource)p_oSource).getNode()==null)){
      try {
        DocumentBuilderFactory oBuilderF = DocumentBuilderFactory.newInstance();
        DocumentBuilder oBuilder = oBuilderF.newDocumentBuilder();
        String sSystemID = p_oSource.getSystemId();
        p_oSource = new DOMSource(oBuilder.newDocument());

        // Copy system ID from original, empty Source to new Source
        if (sSystemID != null) {
        	p_oSource.setSystemId(sSystemID);
        }
      } catch (ParserConfigurationException oPce){
        throw new TransformerException(oPce.getMessage());
      }           
    }
    
    try
    {
      if (p_oSource instanceof DOMSource)
      {
        DOMSource oDomSource = (DOMSource) p_oSource;
  
        m_systemID = oDomSource.getSystemId();
  
        Node dNode = oDomSource.getNode();
  
        if (null != dNode)
        {
          try
          {
            if(dNode.getNodeType() == Node.ATTRIBUTE_NODE)
              this.startDocument();
            try
            {
              if(dNode.getNodeType() == Node.ATTRIBUTE_NODE)
              {
                String sData = dNode.getNodeValue();
                char[] mapChars = sData.toCharArray();
                characters(mapChars, 0, mapChars.length);
              }
              else
              { 
                org.apache.xml.serializer.TreeWalker oWalker;
                oWalker = new org.apache.xml.serializer.TreeWalker(this, m_systemID);
                oWalker.traverse(dNode);
              }
            }
            finally
            {
              if(dNode.getNodeType() == Node.ATTRIBUTE_NODE)
                this.endDocument();
            }
          }
          catch (SAXException oSe)
          {
            throw new TransformerException(oSe);
          }
  
          return;
        }
        else
        {
          String sMessageStr = XSLMessages.createMessage(
            XSLTErrorResources.ER_ILLEGAL_DOMSOURCE_INPUT, null);
  
          throw new IllegalArgumentException(sMessageStr);
        }
      }
  
      InputSource oXmlSource = SAXSource.sourceToInputSource(p_oSource);
  
      if (null == oXmlSource)
      {
        throw new TransformerException(XSLMessages.createMessage(XSLTErrorResources.ER_CANNOT_TRANSFORM_SOURCE_TYPE, new Object[]{p_oSource.getClass().getName()})); //"Can't transform a Source of type "
                                       //+ source.getClass().getName() + "!");
      }
  
      if (null != oXmlSource.getSystemId())
        m_systemID = oXmlSource.getSystemId();
  
      XMLReader oReader = null;
      boolean bManagedReader = false;
  
      try
      {
        if (p_oSource instanceof SAXSource) {
        	oReader = ((SAXSource) p_oSource).getXMLReader();
        }
          
        if (null == oReader) {
          try {
        	  oReader = XMLReaderManager.getInstance().getXMLReader();
            bManagedReader = true;
          } catch (SAXException oSe) {
            throw new TransformerException(oSe);
          }
        } else {
          try {
        	  oReader.setFeature("http://xml.org/sax/features/namespace-prefixes",
                              true);
          } catch (org.xml.sax.SAXException oSe) {
            // We don't care.
          }
        }

        // Get the input content handler, which will handle the 
        // parse events and create the source tree. 
        ContentHandler oInputHandler = this;
  
        oReader.setContentHandler(oInputHandler);
  
        if (oInputHandler instanceof org.xml.sax.DTDHandler) {
        	oReader.setDTDHandler((org.xml.sax.DTDHandler) oInputHandler);
        }
        
        try
        {
          if (oInputHandler instanceof org.xml.sax.ext.LexicalHandler) {
        	  oReader.setProperty("http://xml.org/sax/properties/lexical-handler",
            		oInputHandler);
          }
          if (oInputHandler instanceof org.xml.sax.ext.DeclHandler) {
        	  oReader.setProperty(
              "http://xml.org/sax/properties/declaration-handler",
              oInputHandler);
          }
        }
        catch (org.xml.sax.SAXException oSe){
        	// We don't care.
        }
  
        try
        {
          if (oInputHandler instanceof org.xml.sax.ext.LexicalHandler)
        	  oReader.setProperty("http://xml.org/sax/handlers/LexicalHandler",
            		oInputHandler);
  
          if (oInputHandler instanceof org.xml.sax.ext.DeclHandler)
        	  oReader.setProperty("http://xml.org/sax/handlers/DeclHandler",
            		oInputHandler);
        }
        catch (org.xml.sax.SAXNotRecognizedException oSnre){
        	// We don't care.
        }
  
        oReader.parse(oXmlSource);
      }
      catch (org.apache.xml.utils.WrappedRuntimeException oWre)
      {
        Throwable oThrowable = oWre.getException();
  
        while (oThrowable
               instanceof org.apache.xml.utils.WrappedRuntimeException)
        {
        	oThrowable =
            ((org.apache.xml.utils.WrappedRuntimeException) oThrowable).getException();
        }
  
        throw new TransformerException(oWre.getException());
      }
      catch (org.xml.sax.SAXException oSe)
      {
        throw new TransformerException(oSe);
      }
      catch (IOException oIoe)
      {
        throw new TransformerException(oIoe);
      } finally {
        if (bManagedReader) {
          XMLReaderManager.getInstance().releaseXMLReader(oReader);
        }
      }
    }
    finally
    {
      if(null != m_outputStream)
      {
        try
        {
          m_outputStream.close();
        }
        catch(IOException oIoe){
        	// We don't care.
        }
        m_outputStream = null;
      }
    }
  }

  /**
   * Add a parameter for the transformation.
   *
   * <p>Pass a qualified name as a two-part string, the namespace URI
   * enclosed in curly braces ({}), followed by the local name. If the
   * name has a null URL, the String only contain the local name. An
   * application can safely check for a non-null URI by testing to see if the first
   * character of the name is a '{' character.</p>
   * <p>For example, if a URI and local name were obtained from an element
   * defined with &lt;xyz:foo xmlns:xyz="http://xyz.foo.com/yada/baz.html"/&gt;,
   * then the qualified name would be "{http://xyz.foo.com/yada/baz.html}foo". Note that
   * no prefix is used.</p>
   *
   * @param p_sName The name of the parameter, which may begin with a namespace URI
   * in curly braces ({}).
   * @param p_oValue The value object.  This can be any valid Java object. It is
   * up to the processor to provide the proper object coersion or to simply
   * pass the object on for use in an extension.
   */
  public void setParameter(String p_sName, Object p_oValue)
  {
    if (p_oValue == null) {
      throw new IllegalArgumentException(XSLMessages.createMessage(XSLTErrorResources.ER_INVALID_SET_PARAM_VALUE, new Object[]{p_sName}));
    }
    
    if (null == m_params)
    {
      m_params = new Hashtable();
    }

    m_params.put(p_sName, p_oValue);
  }

  /**
   * Get a parameter that was explicitly set with setParameter
   * or setParameters.
   *
   * <p>This method does not return a default parameter value, which
   * cannot be determined until the node context is evaluated during
   * the transformation process.
   *
   *
   * @param p_sName Name of the parameter.
   * @return A parameter that has been set with setParameter.
   */
  public Object getParameter(String p_sName)
  {

    if (null == m_params)
      return null;

    return m_params.get(p_sName);
  }

  /**
   * Clear all parameters set with setParameter.
   */
  public void clearParameters()
  {

    if (null == m_params)
      return;

    m_params.clear();
  }

  /**
   * Set an object that will be used to resolve URIs used in
   * document().
   *
   * <p>If the resolver argument is null, the URIResolver value will
   * be cleared, and the default behavior will be used.</p>
   *
   * @param p_oResolver An object that implements the URIResolver interface,
   * or null.
   */
  public void setURIResolver(URIResolver p_oResolver)
  {
    m_URIResolver = p_oResolver;
  }

  /**
   * Get an object that will be used to resolve URIs used in
   * document(), etc.
   *
   * @return An object that implements the URIResolver interface,
   * or null.
   */
  public URIResolver getURIResolver()
  {
    return m_URIResolver;
  }

  /**
   * Set the output properties for the transformation.  These
   * properties will override properties set in the Templates
   * with xsl:output.
   *
   * <p>If argument to this function is null, any properties
   * previously set are removed, and the value will revert to the value
   * defined in the templates object.</p>
   *
   * <p>Pass a qualified property key name as a two-part string, the namespace URI
   * enclosed in curly braces ({}), followed by the local name. If the
   * name has a null URL, the String only contain the local name. An
   * application can safely check for a non-null URI by testing to see if the first
   * character of the name is a '{' character.</p>
   * <p>For example, if a URI and local name were obtained from an element
   * defined with &lt;xyz:foo xmlns:xyz="http://xyz.foo.com/yada/baz.html"/&gt;,
   * then the qualified name would be "{http://xyz.foo.com/yada/baz.html}foo". Note that
   * no prefix is used.</p>
   *
   * @param p_oFormat A set of output properties that will be
   * used to override any of the same properties in affect
   * for the transformation.
   *
   * @see javax.xml.transform.OutputKeys
   * @see java.util.Properties
   *
   * @throws IllegalArgumentException if any of the argument keys are not
   * recognized and are not namespace qualified.
   */
  public void setOutputProperties(Properties p_oFormat)
          throws IllegalArgumentException
  {

    if (null != p_oFormat)
    {

      // See if an *explicit* method was set.
      String sMethod = (String) p_oFormat.get(OutputKeys.METHOD);

      if (null != sMethod) {
        m_outputFormat = new OutputProperties(sMethod);
      } else {
        m_outputFormat = new OutputProperties();
      }

      m_outputFormat.copyFrom(p_oFormat);
    }
    else {
      // if oformat is null JAXP says that any props previously set are removed
      // and we are to revert back to those in the templates object (i.e. Stylesheet).
      m_outputFormat = null;
    }
  }

  /**
   * Get a copy of the output properties for the transformation.
   *
   * <p>The properties returned should contain properties set by the user,
   * and properties set by the stylesheet, and these properties
   * are "defaulted" by default properties specified by <a href="http://www.w3.org/TR/xslt#output">section 16 of the
   * XSL Transformations (XSLT) W3C Recommendation</a>.  The properties that
   * were specifically set by the user or the stylesheet should be in the base
   * Properties list, while the XSLT default properties that were not
   * specifically set should be the default Properties list.  Thus,
   * getOutputProperties().getProperty(String key) will obtain any
   * property in that was set by {@link #setOutputProperty},
   * {@link #setOutputProperties}, in the stylesheet, <em>or</em> the default
   * properties, while
   * getOutputProperties().get(String key) will only retrieve properties
   * that were explicitly set by {@link #setOutputProperty},
   * {@link #setOutputProperties}, or in the stylesheet.</p>
   *
   * <p>Note that mutation of the Properties object returned will not
   * effect the properties that the transformation contains.</p>
   *
   * <p>If any of the argument keys are not recognized and are not
   * namespace qualified, the property will be ignored.  In other words the
   * behaviour is not orthogonal with setOutputProperties.</p>
   *
   * @return A copy of the set of output properties in effect
   * for the next transformation.
   *
   * @see javax.xml.transform.OutputKeys
   * @see java.util.Properties
   */
  public Properties getOutputProperties()
  {
    return (Properties) m_outputFormat.getProperties().clone();
  }

  /**
   * Set an output property that will be in effect for the
   * transformation.
   *
   * <p>Pass a qualified property name as a two-part string, the namespace URI
   * enclosed in curly braces ({}), followed by the local name. If the
   * name has a null URL, the String only contain the local name. An
   * application can safely check for a non-null URI by testing to see if the first
   * character of the name is a '{' character.</p>
   * <p>For example, if a URI and local name were obtained from an element
   * defined with &lt;xyz:foo xmlns:xyz="http://xyz.foo.com/yada/baz.html"/&gt;,
   * then the qualified name would be "{http://xyz.foo.com/yada/baz.html}foo". Note that
   * no prefix is used.</p>
   *
   * <p>The Properties object that was passed to {@link #setOutputProperties} won't
   * be effected by calling this method.</p>
   *
   * @param p_sName A non-null String that specifies an output
   * property name, which may be namespace qualified.
   * @param p_sValue The non-null string value of the output property.
   *
   * @throws IllegalArgumentException If the property is not supported, and is
   * not qualified with a namespace.
   *
   * @see javax.xml.transform.OutputKeys
   */
  public void setOutputProperty(String p_sName, String p_sValue)
          throws IllegalArgumentException
  {

    if (!OutputProperties.isLegalPropertyKey(p_sName))
      throw new IllegalArgumentException(XSLMessages.createMessage(XSLTErrorResources.ER_OUTPUT_PROPERTY_NOT_RECOGNIZED, new Object[]{p_sName})); //"output property not recognized: "
                                         //+ name);

    m_outputFormat.setProperty(p_sName, p_sValue);
  }

  /**
   * Get an output property that is in effect for the
   * transformation.  The property specified may be a property
   * that was set with setOutputProperty, or it may be a
   * property specified in the stylesheet.
   *
   * @param p_sName A non-null String that specifies an output
   * property name, which may be namespace qualified.
   *
   * @return The string value of the output property, or null
   * if no property was found.
   *
   * @throws IllegalArgumentException If the property is not supported.
   *
   * @see javax.xml.transform.OutputKeys
   */
  public String getOutputProperty(String p_sName) throws IllegalArgumentException
  {

    String sValue = null;
    OutputProperties oProps = m_outputFormat;

    sValue = oProps.getProperty(p_sName);

    if (null == sValue)
    {
      if (!OutputProperties.isLegalPropertyKey(p_sName)) {
        throw new IllegalArgumentException(XSLMessages.createMessage(XSLTErrorResources.ER_OUTPUT_PROPERTY_NOT_RECOGNIZED, new Object[]{p_sName})); //"output property not recognized: "
                                          // + name);
      }
    }

    return sValue;
  }

  /**
   * Set the error event listener in effect for the transformation.
   *
   * @param p_oListener The new error listener.
   * @throws IllegalArgumentException if listener is null.
   */
  public void setErrorListener(ErrorListener p_oListener)
          throws IllegalArgumentException
  {
      if (p_oListener == null)
        throw new IllegalArgumentException(XSLMessages.createMessage(XSLTErrorResources.ER_NULL_ERROR_HANDLER, null));
      else
        m_errorListener = p_oListener;
  }

  /**
   * Get the error event handler in effect for the transformation.
   *
   * @return The current error handler, which should never be null.
   */
  public ErrorListener getErrorListener()
  {
    return m_errorListener;
  }

  ////////////////////////////////////////////////////////////////////
  // Default implementation of DTDHandler interface.
  ////////////////////////////////////////////////////////////////////

  /**
   * Receive notification of a notation declaration.
   *
   * <p>By default, do nothing.  Application writers may override this
   * method in a subclass if they wish to keep track of the notations
   * declared in a document.</p>
   *
   * @param p_sName The notation name.
   * @param p_sPublicId The notation public identifier, or null if not
   *                 available.
   * @param p_sSystemId The notation system identifier.
   * @throws org.xml.sax.SAXException Any SAX exception, possibly
   *            wrapping another exception.
   * @see org.xml.sax.DTDHandler#notationDecl
   *
   * @throws SAXException
   */
  public void notationDecl(String p_sName, String p_sPublicId, String p_sSystemId)
          throws SAXException
  {
    if (null != m_resultDTDHandler)
      m_resultDTDHandler.notationDecl(p_sName, p_sPublicId, p_sSystemId);
  }

  /**
   * Receive notification of an unparsed entity declaration.
   *
   * <p>By default, do nothing.  Application writers may override this
   * method in a subclass to keep track of the unparsed entities
   * declared in a document.</p>
   *
   * @param p_sName The entity name.
   * @param p_sPublicId The entity public identifier, or null if not
   *                 available.
   * @param p_sSystemId The entity system identifier.
   * @param p_sNotationName The name of the associated notation.
   * @throws org.xml.sax.SAXException Any SAX exception, possibly
   *            wrapping another exception.
   * @see org.xml.sax.DTDHandler#unparsedEntityDecl
   *
   * @throws SAXException
   */
  public void unparsedEntityDecl(
          String p_sName, String p_sPublicId, String p_sSystemId, String p_sNotationName)
            throws SAXException
  {

    if (null != m_resultDTDHandler)
      m_resultDTDHandler.unparsedEntityDecl(p_sName, p_sPublicId, p_sSystemId,
    		  p_sNotationName);
  }

  ////////////////////////////////////////////////////////////////////
  // Default implementation of ContentHandler interface.
  ////////////////////////////////////////////////////////////////////

  /**
   * Receive a Locator object for document events.
   *
   * <p>By default, do nothing.  Application writers may override this
   * method in a subclass if they wish to store the locator for use
   * with other document events.</p>
   *
   * @param p_oLocator A locator for all SAX document events.
   * @see org.xml.sax.ContentHandler#setDocumentLocator
   * @see org.xml.sax.Locator
   */
  public void setDocumentLocator(Locator p_oLocator)
  {
    try
    {
      if (null == m_resultContentHandler)
        createResultContentHandler(m_result);
    }
    catch (TransformerException oTe)
    {
      throw new org.apache.xml.utils.WrappedRuntimeException(oTe);
    }

    m_resultContentHandler.setDocumentLocator(p_oLocator);
  }

  /**
   * Receive notification of the beginning of the document.
   *
   * <p>By default, do nothing.  Application writers may override this
   * method in a subclass to take specific actions at the beginning
   * of a document (such as allocating the root node of a tree or
   * creating an output file).</p>
   *
   * @throws org.xml.sax.SAXException Any SAX exception, possibly
   *            wrapping another exception.
   * @see org.xml.sax.ContentHandler#startDocument
   *
   * @throws SAXException
   */
  public void startDocument() throws SAXException
  {

    try
    {
      if (null == m_resultContentHandler)
        createResultContentHandler(m_result);
    }
    catch (TransformerException oTe)
    {
      throw new SAXException(oTe.getMessage(), oTe);
    }

    // Reset for multiple transforms with this transformer.
    m_flushedStartDoc = false;
    m_foundFirstElement = false;
  }
  
  boolean m_flushedStartDoc = false;
  
  protected final void flushStartDoc()
     throws SAXException
  {
    if(!m_flushedStartDoc)
    {
      if (m_resultContentHandler == null)
      {
        try
        {
          createResultContentHandler(m_result);
        }
        catch(TransformerException oTte)
        {
            throw new SAXException(oTte);
        }
      }
      m_resultContentHandler.startDocument();
      m_flushedStartDoc = true;
    }
  }

  /**
   * Receive notification of the end of the document.
   *
   * <p>By default, do nothing.  Application writers may override this
   * method in a subclass to take specific actions at the end
   * of a document (such as finalising a tree or closing an output
   * file).</p>
   *
   * @throws org.xml.sax.SAXException Any SAX exception, possibly
   *            wrapping another exception.
   * @see org.xml.sax.ContentHandler#endDocument
   *
   * @throws SAXException
   */
  public void endDocument() throws SAXException
  {
    flushStartDoc();
    m_resultContentHandler.endDocument();
  }

  /**
   * Receive notification of the start of a Namespace mapping.
   *
   * <p>By default, do nothing.  Application writers may override this
   * method in a subclass to take specific actions at the start of
   * each Namespace prefix scope (such as storing the prefix mapping).</p>
   *
   * @param p_sPrefix The Namespace prefix being declared.
   * @param p_sUri The Namespace URI mapped to the prefix.
   * @throws org.xml.sax.SAXException Any SAX exception, possibly
   *            wrapping another exception.
   * @see org.xml.sax.ContentHandler#startPrefixMapping
   *
   * @throws SAXException
   */
  public void startPrefixMapping(String p_sPrefix, String p_sUri)
          throws SAXException
  {
    flushStartDoc();
    m_resultContentHandler.startPrefixMapping(p_sPrefix, p_sUri);
  }

  /**
   * Receive notification of the end of a Namespace mapping.
   *
   * <p>By default, do nothing.  Application writers may override this
   * method in a subclass to take specific actions at the end of
   * each prefix mapping.</p>
   *
   * @param p_sPrefix The Namespace prefix being declared.
   * @throws org.xml.sax.SAXException Any SAX exception, possibly
   *            wrapping another exception.
   * @see org.xml.sax.ContentHandler#endPrefixMapping
   *
   * @throws SAXException
   */
  public void endPrefixMapping(String p_sPrefix) throws SAXException
  {
    flushStartDoc();
    m_resultContentHandler.endPrefixMapping(p_sPrefix);
  }

  /**
   * Receive notification of the start of an element.
   *
   * <p>By default, do nothing.  Application writers may override this
   * method in a subclass to take specific actions at the start of
   * each element (such as allocating a new tree node or writing
   * output to a file).</p>
   *
   * @param p_sUri The Namespace URI, or the empty string if the
   *        element has no Namespace URI or if Namespace
   *        processing is not being performed.
   * @param p_sLocalName The local name (without prefix), or the
   *        empty string if Namespace processing is not being
   *        performed.
   * @param p_sQname The qualified name (with prefix), or the
   *        empty string if qualified names are not available.
   * @param p_oAttributes The specified or defaulted attributes.
   * @throws org.xml.sax.SAXException Any SAX exception, possibly
   *            wrapping another exception.
   * @see org.xml.sax.ContentHandler#startElement
   *
   * @throws SAXException
   */
  public void startElement(
          String p_sUri, String p_sLocalName, String p_sQname, Attributes p_oAttributes)
            throws SAXException
  {

    if (!m_foundFirstElement && null != m_serializer)
    {
      m_foundFirstElement = true;

      Serializer sNewSerializer;

      try
      {
    	  sNewSerializer = SerializerSwitcher.switchSerializerIfHTML(p_sUri,
    			  p_sLocalName, m_outputFormat.getProperties(), m_serializer);
      }
      catch (TransformerException oTte)
      {
        throw new SAXException(oTte);
      }

      if (!sNewSerializer.equals(m_serializer))
      {
        try
        {
          m_resultContentHandler = sNewSerializer.asContentHandler();
        }
        catch (IOException oIoe)  // why?
        {
          throw new SAXException(oIoe);
        }

        if (m_resultContentHandler instanceof DTDHandler)
          m_resultDTDHandler = (DTDHandler) m_resultContentHandler;

        if (m_resultContentHandler instanceof LexicalHandler)
          m_resultLexicalHandler = (LexicalHandler) m_resultContentHandler;

        m_serializer = sNewSerializer;
      }
    }
    flushStartDoc();
    m_resultContentHandler.startElement(p_sUri, p_sLocalName, p_sQname, p_oAttributes);
  }

  /**
   * Receive notification of the end of an element.
   *
   * <p>By default, do nothing.  Application writers may override this
   * method in a subclass to take specific actions at the end of
   * each element (such as finalising a tree node or writing
   * output to a file).</p>
   *
   * @param p_sUri The Namespace URI, or the empty string if the
   *        element has no Namespace URI or if Namespace
   *        processing is not being performed.
   * @param p_sLocalName The local name (without prefix), or the
   *        empty string if Namespace processing is not being
   *        performed.
   * @param p_sQName The qualified name (with prefix), or the
   *        empty string if qualified names are not available.
   *
   * @throws org.xml.sax.SAXException Any SAX exception, possibly
   *            wrapping another exception.
   * @see org.xml.sax.ContentHandler#endElement
   *
   * @throws SAXException
   */
  public void endElement(String p_sUri, String p_sLocalName, String p_sQName)
          throws SAXException
  {
    m_resultContentHandler.endElement(p_sUri, p_sLocalName, p_sQName);
  }

  /**
   * Receive notification of character data inside an element.
   *
   * <p>By default, do nothing.  Application writers may override this
   * method to take specific actions for each chunk of character data
   * (such as adding the data to a node or buffer, or printing it to
   * a file).</p>
   *
   * @param p_ch The characters.
   * @param p_iStart The start position in the character array.
   * @param p_iLength The number of characters to use from the
   *               character array.
   * @throws org.xml.sax.SAXException Any SAX exception, possibly
   *            wrapping another exception.
   * @see org.xml.sax.ContentHandler#characters
   *
   * @throws SAXException
   */
  public void characters(char[] p_ch, int p_iStart, int p_iLength) throws SAXException
  {
    flushStartDoc();
    m_resultContentHandler.characters(p_ch, p_iStart, p_iLength);
  }

  /**
   * Receive notification of ignorable whitespace in element content.
   *
   * <p>By default, do nothing.  Application writers may override this
   * method to take specific actions for each chunk of ignorable
   * whitespace (such as adding data to a node or buffer, or printing
   * it to a file).</p>
   *
   * @param p_ch The whitespace characters.
   * @param p_iStart The start position in the character array.
   * @param p_iLength The number of characters to use from the
   *               character array.
   * @throws org.xml.sax.SAXException Any SAX exception, possibly
   *            wrapping another exception.
   * @see org.xml.sax.ContentHandler#ignorableWhitespace
   *
   * @throws SAXException
   */
  public void ignorableWhitespace(char[] p_ch, int p_iStart, int p_iLength)
          throws SAXException
  {
    m_resultContentHandler.ignorableWhitespace(p_ch, p_iStart, p_iLength);
  }

  /**
   * Receive notification of a processing instruction.
   *
   * <p>By default, do nothing.  Application writers may override this
   * method in a subclass to take specific actions for each
   * processing instruction, such as setting status variables or
   * invoking other methods.</p>
   *
   * @param p_sTarget The processing instruction target.
   * @param p_sData The processing instruction data, or null if
   *             none is supplied.
   * @throws org.xml.sax.SAXException Any SAX exception, possibly
   *            wrapping another exception.
   * @see org.xml.sax.ContentHandler#processingInstruction
   *
   * @throws SAXException
   */
  public void processingInstruction(String p_sTarget, String p_sData)
          throws SAXException
  {
    flushStartDoc();
    m_resultContentHandler.processingInstruction(p_sTarget, p_sData);
  }

  /**
   * Receive notification of a skipped entity.
   *
   * <p>By default, do nothing.  Application writers may override this
   * method in a subclass to take specific actions for each
   * processing instruction, such as setting status variables or
   * invoking other methods.</p>
   *
   * @param p_sName The name of the skipped entity.
   * @throws org.xml.sax.SAXException Any SAX exception, possibly
   *            wrapping another exception.
   * @see org.xml.sax.ContentHandler#processingInstruction
   *
   * @throws SAXException
   */
  public void skippedEntity(String p_sName) throws SAXException
  {
    flushStartDoc();
    m_resultContentHandler.skippedEntity(p_sName);
  }

  /**
   * Report the start of DTD declarations, if any.
   *
   * <p>Any declarations are assumed to be in the internal subset
   * unless otherwise indicated by a {@link #startEntity startEntity}
   * event.</p>
   *
   * <p>Note that the start/endDTD events will appear within
   * the start/endDocument events from ContentHandler and
   * before the first startElement event.</p>
   *
   * @param p_sName The document type name.
   * @param p_sPublicId The declared public identifier for the
   *        external DTD subset, or null if none was declared.
   * @param p_sSystemId The declared system identifier for the
   *        external DTD subset, or null if none was declared.
   * @throws SAXException The application may raise an
   *            exception.
   * @see #endDTD
   * @see #startEntity
   */
  public void startDTD(String p_sName, String p_sPublicId, String p_sSystemId)
          throws SAXException
  {
    flushStartDoc();
    if (null != m_resultLexicalHandler)
      m_resultLexicalHandler.startDTD(p_sName, p_sPublicId, p_sSystemId);
  }

  /**
   * Report the end of DTD declarations.
   *
   * @throws SAXException The application may raise an exception.
   * @see #startDTD
   */
  public void endDTD() throws SAXException
  {
    if (null != m_resultLexicalHandler)
      m_resultLexicalHandler.endDTD();
  }

  /**
   * Report the beginning of an entity in content.
   *
   * <p><strong>NOTE:</entity> entity references in attribute
   * values -- and the start and end of the document entity --
   * are never reported.</p>
   *
   * <p>The start and end of the external DTD subset are reported
   * using the pseudo-name "[dtd]".  All other events must be
   * properly nested within start/end entity events.</p>
   *
   * <p>Note that skipped entities will be reported through the
   * {@link org.xml.sax.ContentHandler#skippedEntity skippedEntity}
   * event, which is part of the ContentHandler interface.</p>
   *
   * @param p_sName The name of the entity.  If it is a parameter
   *        entity, the name will begin with '%'.
   * @throws SAXException The application may raise an exception.
   * @see #endEntity
   * @see org.xml.sax.ext.DeclHandler#internalEntityDecl
   * @see org.xml.sax.ext.DeclHandler#externalEntityDecl
   */
  public void startEntity(String p_sName) throws SAXException
  {
    if (null != m_resultLexicalHandler)
      m_resultLexicalHandler.startEntity(p_sName);
  }

  /**
   * Report the end of an entity.
   *
   * @param p_sName The name of the entity that is ending.
   * @throws SAXException The application may raise an exception.
   * @see #startEntity
   */
  public void endEntity(String p_sName) throws SAXException
  {
    if (null != m_resultLexicalHandler)
      m_resultLexicalHandler.endEntity(p_sName);
  }

  /**
   * Report the start of a CDATA section.
   *
   * <p>The contents of the CDATA section will be reported through
   * the regular {@link org.xml.sax.ContentHandler#characters
   * characters} event.</p>
   *
   * @throws SAXException The application may raise an exception.
   * @see #endCDATA
   */
  public void startCDATA() throws SAXException
  {
    if (null != m_resultLexicalHandler)
      m_resultLexicalHandler.startCDATA();
  }

  /**
   * Report the end of a CDATA section.
   *
   * @throws SAXException The application may raise an exception.
   * @see #startCDATA
   */
  public void endCDATA() throws SAXException
  {
    if (null != m_resultLexicalHandler)
      m_resultLexicalHandler.endCDATA();
  }

  /**
   * Report an XML comment anywhere in the document.
   *
   * <p>This callback will be used for comments inside or outside the
   * document element, including comments in the external DTD
   * subset (if read).</p>
   *
   * @param p_ch An array holding the characters in the comment.
   * @param p_iStart The starting position in the array.
   * @param p_iLength The number of characters to use from the array.
   * @throws SAXException The application may raise an exception.
   */
  public void comment(char[] p_ch, int p_iStart, int p_iLength) throws SAXException
  {
    flushStartDoc();
    if (null != m_resultLexicalHandler)
      m_resultLexicalHandler.comment(p_ch, p_iStart, p_iLength);
  }
  
  // Implement DeclHandler
  
  /**
     * Report an element type declaration.
     *
     * <p>The content model will consist of the string "EMPTY", the
     * string "ANY", or a parenthesised group, optionally followed
     * by an occurrence indicator.  The model will be normalized so
     * that all whitespace is removed,and will include the enclosing
     * parentheses.</p>
     *
     * @param p_sName The element type name.
     * @param p_sModel The content model as a normalized string.
     * @exception SAXException The application may raise an exception.
     */
    public void elementDecl (String p_sName, String p_sModel)
        throws SAXException
    {
                        if (null != m_resultDeclHandler)
                                m_resultDeclHandler.elementDecl(p_sName, p_sModel);
    }


    /**
     * Report an attribute type declaration.
     *
     * <p>Only the effective (first) declaration for an attribute will
     * be reported.  The type will be one of the strings "CDATA",
     * "ID", "IDREF", "IDREFS", "NMTOKEN", "NMTOKENS", "ENTITY",
     * "ENTITIES", or "NOTATION", or a parenthesized token group with 
     * the separator "|" and all whitespace removed.</p>
     *
     * @param p_sEname The name of the associated element.
     * @param p_sAname The name of the attribute.
     * @param p_sType A string representing the attribute type.
     * @param p_sValueDefault A string representing the attribute default
     *        ("#IMPLIED", "#REQUIRED", or "#FIXED") or null if
     *        none of these applies.
     * @param p_sValue A string representing the attribute's default value,
     *        or null if there is none.
     * @exception SAXException The application may raise an exception.
     */
    public void attributeDecl (String p_sEname,
                                        String p_sAname,
                                        String p_sType,
                                        String p_sValueDefault,
                                        String p_sValue)
        throws SAXException
    {
      if (null != m_resultDeclHandler)
                                m_resultDeclHandler.attributeDecl(p_sEname, p_sAname, p_sType, p_sValueDefault, p_sValue);
    }


    /**
     * Report an internal entity declaration.
     *
     * <p>Only the effective (first) declaration for each entity
     * will be reported.</p>
     *
     * @param p_sName The name of the entity.  If it is a parameter
     *        entity, the name will begin with '%'.
     * @param p_sValue The replacement text of the entity.
     * @exception SAXException The application may raise an exception.
     * @see #externalEntityDecl
     * @see org.xml.sax.DTDHandler#unparsedEntityDecl
     */
    public void internalEntityDecl (String p_sName, String p_sValue)
        throws SAXException
    {
      if (null != m_resultDeclHandler)
                                m_resultDeclHandler.internalEntityDecl(p_sName, p_sValue); 
    }


    /**
     * Report a parsed external entity declaration.
     *
     * <p>Only the effective (first) declaration for each entity
     * will be reported.</p>
     *
     * @param p_sName The name of the entity.  If it is a parameter
     *        entity, the name will begin with '%'.
     * @param p_sPublicId The declared public identifier of the entity, or
     *        null if none was declared.
     * @param p_sSystemId The declared system identifier of the entity.
     * @exception SAXException The application may raise an exception.
     * @see #internalEntityDecl
     * @see org.xml.sax.DTDHandler#unparsedEntityDecl
     */
    public void externalEntityDecl (String p_sName, String p_sPublicId,
                                             String p_sSystemId)
        throws SAXException
    {
      if (null != m_resultDeclHandler)
                                m_resultDeclHandler.externalEntityDecl(p_sName, p_sPublicId, p_sSystemId);
    }
  
  /**
   * This is null unless we own the stream.
   */
  private java.io.FileOutputStream m_outputStream = null;

  /** The content handler where result events will be sent. */
  private ContentHandler m_resultContentHandler;

  /** The lexical handler where result events will be sent. */
  private LexicalHandler m_resultLexicalHandler;

  /** The DTD handler where result events will be sent. */
  private DTDHandler m_resultDTDHandler;
  
  /** The Decl handler where result events will be sent. */
  private DeclHandler m_resultDeclHandler;

  /** The Serializer, which may or may not be null. */
  private Serializer m_serializer;

  /** The Result object. */
  private Result m_result;

  /**
   * The system ID, which is unused, but must be returned to fullfill the
   *  TransformerHandler interface.
   */
  private String m_systemID;

  /**
   * The parameters, which is unused, but must be returned to fullfill the
   *  Transformer interface.
   */
  private Hashtable m_params;

  /** The error listener for TrAX errors and warnings. */
  private ErrorListener m_errorListener =
    new org.apache.xml.utils.DefaultErrorHandler(false);

  /**
   * The URIResolver, which is unused, but must be returned to fullfill the
   *  TransformerHandler interface.
   */
  URIResolver m_URIResolver;

  /** The output properties. */
  private OutputProperties m_outputFormat;

  /** Flag to set if we've found the first element, so we can tell if we have 
   *  to check to see if we should create an HTML serializer.      */
  boolean m_foundFirstElement;
  
  /**
   * State of the secure processing feature.
   */
  private boolean m_isSecureProcessing = false;
}