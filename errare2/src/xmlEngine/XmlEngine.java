/*Errare is a free and crossplatform MMORPG project.
Copyright (C) 2006  Martin DELEMOTTE

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.*/

package xmlEngine;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import main.ResourceLocator;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XmlEngine {
	
	public final static String DIR="config/";
	
	private DocumentBuilder builder;


	public XmlEngine() throws ParserConfigurationException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		builder = factory.newDocumentBuilder();
	}
	
	
	public String get(String property) {
		String[] propertyArray = property.split("\\.");
		
		Document document;
		try {
			document = builder.parse(ResourceLocator.getRessourceAsStream(DIR+propertyArray[0]+".xml"));
		} catch (SAXException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
		
		
		Node root = document.getDocumentElement();
		int i;
		boolean found=true;
		for(i=1; i<propertyArray.length&&found; i++) {
			found=false;
			for(int j=0; j<root.getChildNodes().getLength()&&!found; j++) {
				Node child = root.getChildNodes().item(j);
				if(child.getNodeType()==Node.ELEMENT_NODE) {
					if(child.getNodeName().compareTo(propertyArray[i])==0) {
						found=true;
						root=child;
					}
				}
			}
		}
		
		if(!found) {
			i--;
		}
		
		if(i==propertyArray.length) {
			NodeList nodes = root.getChildNodes();
			for(int j=0; j<nodes.getLength(); j++) {
				if(nodes.item(j).getNodeType()!=Node.TEXT_NODE) {
					return null;
				}
			}
			return root.getTextContent();
		}
		else
			return null;
	}
	
	
	public void set(String property, Object value) throws TransformerFactoryConfigurationError, TransformerException {
		
		String[] propertyArray = property.split("\\.");
		
		Document document;
		boolean found=true;
		try {
			document = builder.parse(Thread.currentThread().getContextClassLoader().getResourceAsStream(DIR+propertyArray[0]+".xml"));
		} catch (SAXException e) {
			found=false;
			document = builder.newDocument();
		} catch (IOException e) {
			found=false;
			document = builder.newDocument();
		} 
		
		int i=1;
		Node root = document.getDocumentElement();
		if(root != null) {
			if(!(root.getNodeType()==Node.ELEMENT_NODE 
					&& root.getNodeName().compareTo(propertyArray[0])==0)) {
				throw new IllegalArgumentException("File corrupted");
			}
		}
		else {
			root = document.appendChild(document.createElement(propertyArray[0]));
		}
		if(found) {
			for(i=1; i<propertyArray.length&&found; i++) {
				found=false;
				for(int j=0; j<root.getChildNodes().getLength()&&!found; j++) {
					Node child = root.getChildNodes().item(j);
					if(child.getNodeType()==Node.ELEMENT_NODE) {
						if(child.getNodeName().compareTo(propertyArray[i])==0) {
							found=true;
							root=child;
						}
					}
				}
			}
			
			if(!found) {
				i--;
			}
		}
		
		if(i==propertyArray.length) {
			NodeList nodes = root.getChildNodes();
			for(int j=0; j<nodes.getLength(); j++) {
				if(nodes.item(j).getNodeType()!=Node.TEXT_NODE) {
					throw new IllegalArgumentException("Node contains other elements and consequently can not contain data");
				}
			}
			root.setTextContent(value.toString());
		}
		else {
			NodeList nodes = root.getChildNodes();
			for(int j=0; j<nodes.getLength(); j++) {
				if(nodes.item(j).getNodeType()==Node.TEXT_NODE) {
					String v = nodes.item(j).getNodeValue();
					v=v.trim();
					if(v.compareTo("")!=0)
						throw new IllegalArgumentException("Node contains data and consequently can not contain elements");
				}
			}
			for(; i<propertyArray.length; i++) {
				root=root.appendChild(document.createElement(propertyArray[i]));
			}
			root.setTextContent(value.toString());
		}
		
		write(document, new File(DIR+propertyArray[0]+".xml"));
		
	}
	
	public String getString(String property) {
		return get(property);
	}
	
	public int getInt(String property) {
		return Integer.parseInt(get(property));
	}
	
	public boolean getBoolean(String property) {
		return Boolean.parseBoolean(get(property));
	}
	
	public double getDouble(String property) {
		return Double.parseDouble(get(property));
	}
	
	private void write(Document document, File file) throws TransformerFactoryConfigurationError, TransformerException {
		document.getDocumentElement().normalize();
		Source source = new DOMSource(document);
		//StreamResult console = new StreamResult(System.out);
		StreamResult result = new StreamResult(file);
		
		// Write the DOM document to the file
		Transformer xformer = TransformerFactory.newInstance().newTransformer();
		xformer.setOutputProperty(OutputKeys.INDENT, "yes");
		xformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");
		//xformer.transform(source, console);
		xformer.transform(source, result);
	}
	
}
