package springies;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import jboxGlue.FixedMass;
import jboxGlue.Mass;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLParser extends Springies{
	private static final String DEFAULT_VELOCITY="0";
	private static final String DEFAULT_MASS="1";
	public static final String DEFAULT_SPRINGCONSTANT="1";
	String[] Elements={"fixed", "mass"};
	String[] fixedRequired={"id", "xpos", "ypos"};
	String[] fixedOptional={};
	String[] massRequired={"id", "xpos", "ypos"};
	String[] massOptional={"xVeloc", "yVeloc", "massVal"};
	
	
	public void readXMLData(){
		try {

			File dataFile = new File("src/springies/XML.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(dataFile);
			doc.getDocumentElement().normalize();
			
			//Reading Fixed Masses
			NodeList nodes = doc.getElementsByTagName("fixed");	
			for(int i=0; i<nodes.getLength(); i++){
				Node node = nodes.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;
					for(String s: fixedRequired){
						
					}
					
					
					String id=getValue("id", element);
					String xpos=getValue("xpos", element);
					String ypos=getValue("ypos", element);
					createFixed(id, xpos, ypos);
					
					System.out.println("ID: " + id);
					System.out.println("X Position: " + xpos);
					System.out.println("Y Position: " + ypos);
					
				}
			}
			
			
			for (int i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);

				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;
					String id=getValue("id", element);
					String xpos=getValue("xpos", element);
					String ypos=getValue("ypos", element);
					createFixed(id, xpos, ypos);
					
					System.out.println("ID: " + id);
					System.out.println("X Position: " + xpos);
					System.out.println("Y Position: " + ypos);
					
				}
			}
			//Reading Regular Masses
			nodes=doc.getElementsByTagName("mass");
			for(int i=0; i <nodes.getLength(); i++){
				Node node=nodes.item(i);
				if(node.getNodeType()==Node.ELEMENT_NODE){
					Element element = (Element) node;
				
					String id=getValue("id", element);
					String xpos=getValue("xpos", element);
					String ypos=getValue("ypos", element);
					String xVeloc=null;
					String yVeloc=null;
					String mass=null;
					if(element.hasAttribute("xVeloc")){
						xVeloc=getValue("xVeloc", element);
						yVeloc=getValue("yVeloc", element);
					}
					else{
						xVeloc=DEFAULT_VELOCITY;
						yVeloc=DEFAULT_VELOCITY;
					}
					if(element.hasAttribute("massVal")){
						mass=getValue("massVal", element);
					}
					else{
						mass=DEFAULT_MASS;
					}
					createMass(id, xpos, ypos, xVeloc, yVeloc, mass);
					
					System.out.println("ID: " + id);
					System.out.println("X Position: " + xpos);
					System.out.println("Y Position: " + ypos);
					System.out.println("X Velocity: "+ xVeloc);
					System.out.println("Y Velocity: "+ yVeloc);
					System.out.println("Mass: " + mass);
				}
			}
			//Reading Springs
			nodes=doc.getElementsByTagName("spring");
			for(int i=0; i <nodes.getLength(); i++){
				Node node=nodes.item(i);
				if(node.getNodeType()==Node.ELEMENT_NODE){
					Element element = (Element) node;
				
					String id1=getValue("id1", element);
					String id2=getValue("id2", element);
					String restLength=getValue("rest", element);
					String springConstant=null;
					if(element.hasAttribute("K")){
						springConstant=getValue("K", element);
					}
					else{
						springConstant=DEFAULT_SPRINGCONSTANT;
					}
					
					System.out.println("id1: " + id1);
					System.out.println("id2: " + id2);
					System.out.println("Rest Length: " + restLength);
					System.out.println("K: " + springConstant);
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

	public void createSpring(String id1, String id2, String rest, String K){
		double restLength=Double.parseDouble(rest);
		double springConstant=Double.parseDouble(K);
		
	}
	public void createMass(String id, String xpos, String ypos, String xveloc, String yveloc, String mass){
		double xPosition=Double.parseDouble(xpos);
		double yPosition=Double.parseDouble(ypos);
		double xVelocity=Double.parseDouble(xveloc);
		double yVelocity=Double.parseDouble(yveloc);
		double massValue=Double.parseDouble(mass);
		new Mass(id, xPosition*displayWidth(), yPosition*displayHeight(), xVelocity, yVelocity, massValue);
	}
	public void createFixed(String id, String xpos, String ypos){
		double xPosition=Double.parseDouble(xpos);
		double yPosition=Double.parseDouble(ypos);
		new FixedMass(id, xPosition*displayWidth(), yPosition*displayHeight());
	
	}
}
