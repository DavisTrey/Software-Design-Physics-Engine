package springies;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import jboxGlue.FixedMass;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import jboxGlue.PhysicalObject;
import jboxGlue.PhysicalObjectCircle;
import jboxGlue.PhysicalObjectRect;
import jboxGlue.WorldManager;
import jgame.JGColor;
import jgame.JGObject;
import jgame.platform.JGEngine;
import org.jbox2d.common.Vec2;

public class XMLParser {

	public void main(String args[]) {
		try {

			File stocks = new File("src/springies/XML.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(stocks);
			doc.getDocumentElement().normalize();

			System.out.println("root of xml file " + doc.getDocumentElement().getNodeName());
			NodeList nodes = doc.getElementsByTagName("fixed");
			System.out.println("==========================");
			
			for (int i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);

				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;
					String id=getValue("id", element);
					String xpos=getValue("xpos", element);
					String ypos=getValue("ypos", element);
					createFixed(id, xpos, ypos);
					
					System.out.println("ID: " + getValue("id", element));
					System.out.println("X Position: " + getValue("xpos", element));
					System.out.println("Y Position: " + getValue("ypos", element));
					
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void readData(){
		try {

			File stocks = new File("src/springies/XML.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(stocks);
			doc.getDocumentElement().normalize();

			System.out.println("root of xml file " + doc.getDocumentElement().getNodeName());
			NodeList nodes = doc.getElementsByTagName("fixed");
			System.out.println("==========================");
			
			for (int i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);

				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;
					String id=getValue("id", element);
					String xpos=getValue("xpos", element);
					String ypos=getValue("ypos", element);
					createFixed(id, xpos, ypos);
					
					System.out.println("ID: " + getValue("id", element));
					System.out.println("X Position: " + getValue("xpos", element));
					System.out.println("Y Position: " + getValue("ypos", element));
					
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	private String getValue(String tag, Element element) {
		NodeList nodes = element.getElementsByTagName(tag).item(0).getChildNodes();
		Node node = (Node) nodes.item(0);
		return node.getNodeValue();
	}
	public void createFixed(String id, String xpos, String ypos){
		double xPosition=Double.parseDouble(xpos);
		double yPosition=Double.parseDouble(ypos);
		new FixedMass(id, xPosition, yPosition);
		
	}
}


