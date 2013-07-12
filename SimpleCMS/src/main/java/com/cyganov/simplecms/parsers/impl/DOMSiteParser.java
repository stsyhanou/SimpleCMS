package com.cyganov.simplecms.parsers.impl;

import com.cyganov.simplecms.domain.Content;
import com.cyganov.simplecms.domain.Section;
import com.cyganov.simplecms.domain.Site;
import com.cyganov.simplecms.parsers.SiteParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Tsyhanou Siarhei
 * Date: 11.07.13
 * Time: 11:46
 */
public class DOMSiteParser implements SiteParser {

	@Override
	public Site parse(String filePath) {

		Site site = new Site();
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(new File(filePath));
			doc.getDocumentElement().normalize();
			NodeList list = doc.getChildNodes().item(0).getChildNodes();
			List<Section> sectionList = process(list, null);
			site.setSectionList(sectionList);

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return site;
	}

	private List<Section> process(NodeList list, Section parentSection){

		List<Section> children = new ArrayList<Section>();
		for (int i = 0; i < list.getLength(); i++) {

			Node node = list.item(i);
			if (node.getNodeName().equals("section")){
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;

					Section currentSection = new Section();
					currentSection.setName(element.getAttribute("name"));
					currentSection.setPublished(Boolean.parseBoolean(element.getAttribute("published")));
					currentSection.setContent(makeContent(element));
					currentSection.setParent(parentSection);

					if (element.getElementsByTagName("children").getLength() != 0){
						NodeList nodes = element.getElementsByTagName("children").item(0).getChildNodes();
						currentSection.setChildren(process(nodes,currentSection));
					}
	                children.add(currentSection);

				}
			}
		}
		return children;
	}

	private String getTagBody(Element element){
		NodeList list = element.getChildNodes();
		for (int i = 0; i<list.getLength(); i++ ){
			Node node = list.item(i);
			if (node.getNodeName().equals("content")){
				NodeList nodeList = element.getElementsByTagName("body").item(0).getChildNodes();
				Node value = nodeList.item(0);
				return value.getNodeValue();
			}
		}
		return null;
	}

	private Content makeContent(Element element){
		String body = getTagBody(element);
		if (body != null){
			Content content = new Content();
			content.setBody(getTagBody(element));
			return content;
		}
		return null;
	}

}