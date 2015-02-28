package shtykh.nekki;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import shtykh.nekki.db.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static shtykh.nekki.Parser.NodeName.*;

/**
 * Created by shtykh on 24/02/15.
 */
public class Parser {
	final static Logger log = Logger.getLogger(Parser.class);
	private final String dateFormat;
	private final int maxContentSize;

	public Parser(String dateFormat, int maxContentSize) {
		this.dateFormat = dateFormat;
		this.maxContentSize = maxContentSize;
	}

	public Entry parse(File file) throws InvalidXmlException, IOException, SAXException, ParserConfigurationException {
		DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = domFactory.newDocumentBuilder();
		Document document = builder.parse(new InputSource(file.getAbsolutePath()));
		return toEntry(document);

	}

	public Entry toEntry(Document document) throws InvalidXmlException{
		Element rootElement = document.getDocumentElement();
		if (!getNodeName(rootElement).equals(ENTRY)) {
			throw new InvalidXmlException(document, "root node should be named " + ENTRY);
		}
		NodeList nodes = rootElement.getChildNodes();
		String content = null;
		Date date = null;
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			if(node instanceof Element){
				Element element = (Element) node;
				NodeName nodeName = getNodeName(element);
				switch (nodeName) {
					case CONTENT:
						if (content != null) {
							throw new InvalidXmlException(document, 
									"Has duplicated \"" + nodeName + "\" element");
						}
						content = toContent(element, maxContentSize);
						break;
					case DATE:
						if (date != null) {
							throw new InvalidXmlException(document, 
									"Has duplicated \"" + nodeName + "\" element");
						}
						date = toDate(element, dateFormat);
						break;
				}
			}
		}
		if (date == null) {
			throw new InvalidXmlException(document, 
					"Has no creationDate element");
		} else if (content == null) {
			throw new InvalidXmlException(document, 
					"Has no content element");
		}
		return new Entry(content, date);
	}

	private static Date toDate(Element dateNode, 
							   String dateFormatString) throws InvalidXmlException {
		DateFormat formatter = new SimpleDateFormat(dateFormatString);
		String dateString = dateNode.getTextContent();
		try {
			return formatter.parse(dateString);
		} catch (ParseException e) {
			throw new InvalidXmlException(dateNode.getOwnerDocument(),
					e.getMessage());
		}
	}

	private static String toContent(Element contentNode, 
									int maxContentSize) throws InvalidXmlException {
		String textContent = contentNode.getTextContent();
		if (textContent.length() > maxContentSize) {
			throw new InvalidXmlException(contentNode.getOwnerDocument(),
					"Content length should not be larger that " + maxContentSize);
		}
		return textContent;
	}

	static class InvalidXmlException extends Exception {
		public InvalidXmlException(Document document, 
								   String message) {
			super((document != null ? document.getDocumentURI() + " isn't right:\n" : "") +
					message);
		}
	}
	
	static enum NodeName {
		ENTRY("Entry"), 
		CONTENT("content"), 
		DATE("creationDate");
		
		private final String name;
		
		NodeName(String name) {
			this.name = name;
		}

		public static NodeName getNodeName(Element element) throws InvalidXmlException{
			String name = element.getNodeName();
			for (NodeName nodeName : NodeName.values()) {
				if (nodeName.getName().equals(name)) {
					return nodeName;
				}
			}
			throw new InvalidXmlException(element.getOwnerDocument(), 
					name + " is not a valid XML element name!");
		}

		private String getName() {
			return name;
		}

		@Override
		public String toString() {
			return name;
		}
	}
}
