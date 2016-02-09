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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;

/**
 * <p>Classe utilitaire pour l'utilisation de JAXB</p>
 *
 * <p>Copyright (c) 2010</p>
 * <p>Company: Adeuza</p>
 *
 * @author mmadigand
 *
 */
public final class JaxbUtils {

	/**
	 * Constructeur priv
	 */
	private JaxbUtils() {

	}

	/**
	 * Serialize l'object en xml 
	 * 
	 * @param p_oObject object  serializer en xml
	 * @param p_oWriter writer dans lequel crire
	 * @throws Exception chec de la serialization
	 */
	public static void marshal( Object p_oObject, Writer p_oWriter ) throws Exception {
		JAXBContext oJabxContext = JAXBContext.newInstance(p_oObject.getClass());
		Marshaller oMarshaller = oJabxContext.createMarshaller();
		oMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		oMarshaller.marshal(p_oObject, p_oWriter);
	}

	/**
	 * Serialize l'object en xml
	 * 
	 * @param p_oObject object  serializer en xml
	 * @param p_oFile fichier
	 * @throws Exception echec de la serialization
	 */
	public static void marshal( Object p_oObject, File p_oFile ) throws Exception {
		JAXBContext oJabxContext = JAXBContext.newInstance(p_oObject.getClass());
		Marshaller oMarshaller = oJabxContext.createMarshaller();
		oMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		oMarshaller.marshal(p_oObject, p_oFile);
	}

	/**
	 * Serialize l'object en xml
	 * 
	 * @param p_oObject object  serializer en xml
	 * @return Document document correspondant
	 * @throws Exception echec de la serialization
	 */
	public static Document marshalToDocument( Object p_oObject ) throws Exception {
		JAXBContext oJabxContext = JAXBContext.newInstance(p_oObject.getClass());
		Marshaller oMarshaller = oJabxContext.createMarshaller();
		DocumentBuilderFactory oDbf = DocumentBuilderFactory.newInstance();
		Document r_xDoc = oDbf.newDocumentBuilder().newDocument();
		oMarshaller.marshal( p_oObject, r_xDoc );
		return r_xDoc ;
	}


	/**
	 * Serialize l'object en xml
	 * 
	 * @param p_oObject object  serializer en xml
	 * @return chaine contenant le xml de l'objet
	 * @throws Exception echec de la serialization
	 */
	public static String marshalToString( Object p_oObject ) throws Exception {
		String r_sXml = null ;
		StringWriter oWriter = new StringWriter();
		try {
			JAXBContext oJabxContext = JAXBContext.newInstance(p_oObject.getClass());
			Marshaller oMarshaller = oJabxContext.createMarshaller();
			oMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			oMarshaller.marshal(p_oObject, oWriter);
			r_sXml = oWriter.toString();
		} finally {
			oWriter.close();
		}
		return r_sXml ;
	}

	/**
	 * Deserialize le xml et renvoie l'object correspondant
	 * 
	 * @param p_oClass classe de l'objet  dsrialiser
	 * @param p_sXml chaine  dsrializer
	 * @param <T> classe de l'objet  dsrializer
	 * @return l'objet dsrialis
	 * @throws Exception chec de la dserialization
	 */
	public static <T> T unmarshal( Class<T> p_oClass, String p_sXml ) throws Exception {
		T r_oT = null ;
		InputStream oIs = new ByteArrayInputStream(p_sXml.getBytes());
		try {
			r_oT = unmarshal( p_oClass, oIs);
		} finally {
			oIs.close();
		}
		return r_oT ;
	}

	/**
	 * Deserialize l'objet et renvoie l'object correspondant
	 * 
	 * @param p_oClass classe de l'objet  dsrialiser
	 * @param p_oReader reader de lecture
	 * @param <T> classe de l'objet  dsrializer
	 * @return l'objet dsrialis
	 * @throws Exception chec de la serialization
	 */
	public static <T> T unmarshal(Class<T> p_oClass, Reader p_oReader) throws Exception {
		StreamSource oStream = new StreamSource(p_oReader);
		return unmarshal(p_oClass, oStream);
	}

	/**
	 * Deserialize l'objet et renvoie l'object correspondant
	 * 
	 * @param p_oClass classe de l'object  dserializer
	 * @param p_oIs inputstream de lecture du flux
	 * @param <T> classe de l'objet  dsrializer
	 * @return l'objet dsrialis
	 * @throws Exception chec de la serialization
	 */
	public static <T> T unmarshal( Class<T> p_oClass, InputStream p_oIs ) throws Exception {
		StreamSource oStream = new StreamSource(p_oIs);
		return unmarshal(p_oClass, oStream);
	}

	/**
	 * Dsrialize l'objet et renvoie l'object correspondant
	 * @param p_oClass classe de l'object  dserializer
	 * @param p_oStreamSource streamsource de lecture du xml
	 * @param <T> classe de l'objet  dsrializer
	 * @return l'objet dsrialis
	 * @throws Exception chec de la serialization
	 */
	public static <T> T unmarshal( Class<T> p_oClass, StreamSource p_oStreamSource ) throws Exception {
		JAXBContext oContext = 
			JAXBContext.newInstance( p_oClass);
		Unmarshaller oUnmarshaller = oContext.createUnmarshaller();
		JAXBElement<T> xRoot = oUnmarshaller.unmarshal( p_oStreamSource, p_oClass);
		return xRoot.getValue();
	}

	/**
	 * Deserialize l'objet et renvoie l'object correspondant
	 * 
	 * @param p_oClass classe de l'object  dserializer
	 * @param p_oFile fichier
	 * @param <T> classe de l'objet  dsrializer
	 * @return l'objet dsrialis
	 * @throws Exception chec de la serialization
	 */
	public static <T> T unmarshal( Class<T> p_oClass, File p_oFile ) throws Exception {
		JAXBContext oContext = JAXBContext.newInstance( p_oClass);
		Unmarshaller oUnmarshaller = oContext.createUnmarshaller();
		return (T)oUnmarshaller.unmarshal( p_oFile);
	}
}
