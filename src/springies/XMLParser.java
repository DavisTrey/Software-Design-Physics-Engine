package springies;

import java.awt.Frame;
import java.io.File;
import java.util.HashSet;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import jboxGlue.FixedMass;
import jboxGlue.Mass;
import jboxGlue.Muscle;
import jboxGlue.PhysicalObject;
import jboxGlue.PhysicalObjectCircle;
import jboxGlue.PhysicalObjectRect;
import jboxGlue.Spring;
import jboxGlue.WorldManager;
import jgame.JGColor;
import jgame.JGObject;
import jgame.platform.JGEngine;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLParser{
	public void readXMLData(){
		try {
			Frame message=new Frame();
			JOptionPane.showMessageDialog(message, "Please load your object XML File");

			File dataFile = new File("");
			JFileChooser chooser = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter(
					"XML Files", "xml");
			chooser.setFileFilter(filter);
			int returnVal = chooser.showOpenDialog(getParent());
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				dataFile = chooser.getSelectedFile();
			}
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(dataFile);
			doc.getDocumentElement().normalize();

			//Reading Fixed Masses
			NodeList nodes = doc.getElementsByTagName("fixed");	
			for (int i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);

				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;
					String id=element.getAttribute("id");
					String xpos=element.getAttribute("x");
					String ypos=element.getAttribute("y");
					createFixed(id, xpos, ypos);

					//System.out.println("ID: " + id);
					//System.out.println("X Position: " + xpos);
					//System.out.println("Y Position: " + ypos);

				}
			}
			//Reading Regular Masses
			nodes=doc.getElementsByTagName("mass");
			for(int i=0; i <nodes.getLength(); i++){
				Node node=nodes.item(i);
				if(node.getNodeType()==Node.ELEMENT_NODE){
					Element element = (Element) node;
					String id=element.getAttribute("id");
					String xpos=element.getAttribute("x");
					String ypos=element.getAttribute("y");
					String xVeloc=null;
					String yVeloc=null;
					String mass=null;
					try{
						xVeloc=element.getAttribute("vx");
						yVeloc=element.getAttribute("vy");
					}
					catch(Exception ex){
						xVeloc=DEFAULT_VELOCITY;
						yVeloc=DEFAULT_VELOCITY;
					}
					try{
						mass=element.getAttribute("mass");
					}
					catch(Exception ex){
					}
					if(mass==""){
						mass=DEFAULT_MASS;
					}
					createMass(id, xpos, ypos, xVeloc, yVeloc, mass);

					//System.out.println("ID: " + id);
					//System.out.println("X Position: " + xpos);
					//System.out.println("Y Position: " + ypos);
					//System.out.println("X Velocity: "+ xVeloc);
					//System.out.println("Y Velocity: "+ yVeloc);
					//System.out.println("Mass: " + mass);
				}
			}
			//Reading Springs and muscles
			String[] array={"spring", "muscle"};
			for(String s: array){
				nodes=doc.getElementsByTagName(s);
				for(int i=0; i <nodes.getLength(); i++){
					Node node=nodes.item(i);
					if(node.getNodeType()==Node.ELEMENT_NODE){
						Element element = (Element) node;
						String id1=element.getAttribute("a");
						String id2=element.getAttribute("b");
						String restLength=null;
						String springConstant=null;
						String amplitude=null;
						try{
							restLength=element.getAttribute("restlength");
						}
						catch(Exception ex){
						}
						if(restLength==""){
							restLength=DEFAULT_REST;
						}
						try{
							springConstant=element.getAttribute("constant");
						}
						catch(Exception ex){
							springConstant=DEFAULT_SPRINGCONSTANT;
						}
						if(springConstant==""){
							springConstant=DEFAULT_SPRINGCONSTANT;
						}
						try{
							amplitude=element.getAttribute("amplitude");
						}
						catch(Exception ex){
						}

						if(amplitude!=""){
							createMuscle(id1, id2, restLength, springConstant, amplitude);
						}
						else{
							createSpring(id1, id2, restLength, springConstant);
						}

						//System.out.println("id1: " + id1);
						//System.out.println("id2: " + id2);
						//System.out.println("Rest Length: " + restLength);
						//System.out.println("K: " + springConstant);
					}
				}
			}



		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	public void readEnvironmentData(){
		try {
			Frame message=new Frame();
			Object[] options={"Yes", "No"};
			int n=JOptionPane.showOptionDialog(message, "Would you like to load an enviroment file?", "XML Loading", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
			if(n==0){
				File dataFile = new File("");
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter(
						"XML Files", "xml");
				chooser.setFileFilter(filter);
				int returnVal = chooser.showOpenDialog(getParent());
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					dataFile = chooser.getSelectedFile();
				}
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(dataFile);
				doc.getDocumentElement().normalize();
				//Gravity
				NodeList nodes = doc.getElementsByTagName("gravity");	
				for (int i = 0; i < nodes.getLength(); i++) {
					Node node = nodes.item(i);

					if (node.getNodeType() == Node.ELEMENT_NODE) {
						Element element = (Element) node;
						String direction=element.getAttribute("direction");
						String magnitude=element.getAttribute("magnitude");
						System.out.println("Gravity--------");
						System.out.println("Direction:" + direction);
						System.out.println("Mag:" + magnitude);
						alterGravity(direction, magnitude);
					}
				}
				//Viscosity
				nodes=doc.getElementsByTagName("viscosity");
				for (int i = 0; i < nodes.getLength(); i++) {
					Node node = nodes.item(i);
					if (node.getNodeType() == Node.ELEMENT_NODE) {
						Element element = (Element) node;
						String magnitude=element.getAttribute("magnitude");
						System.out.println("Viscosity--------");
						System.out.println("Mag:" + magnitude);
						alterViscosity(magnitude);
					}
				}

				//centermass
				nodes=doc.getElementsByTagName("centermass");
				for (int i = 0; i < nodes.getLength(); i++) {
					Node node = nodes.item(i);
					if (node.getNodeType() == Node.ELEMENT_NODE) {
						Element element = (Element) node;
						String magnitude=element.getAttribute("magnitude");
						String exponent=element.getAttribute("exponent");
						System.out.println("Center--------");
						System.out.println("Mag:" + magnitude);
						System.out.println("Exponent:" + exponent);
						alterCenterMass(magnitude, exponent);
					}
				}

				//Walls
				nodes=doc.getElementsByTagName("wall");
				for (int i = 0; i < nodes.getLength(); i++) {
					Node node = nodes.item(i);
					if (node.getNodeType() == Node.ELEMENT_NODE) {
						Element element = (Element) node;
						String id=element.getAttribute("id");
						String magnitude=element.getAttribute("magnitude");
						String exponent=element.getAttribute("exponent");
						System.out.println("Wall--------");
						System.out.println("Mag:" + magnitude);
						System.out.println("Exponent:" + exponent);
						System.out.println("Id: " + id);
						alterWall(id, magnitude, exponent);
					}
				}
			}



		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	public void alterGravity(String direction, String magnitude){
		double direct=Double.parseDouble(direction);
		double mag=Double.parseDouble(magnitude);
		Vec2 vector=new Vec2((float)(mag*Math.cos(direct*3.14/180)),(float)(mag*Math.sin(direct*3.14/180)));
		WorldManager.getWorld().setGravity(vector);
	}
	public void alterViscosity(String magnitude){
		viscosity=Double.parseDouble(magnitude);
	}
	public void alterCenterMass(String magnitude, String exponent){
		CENTEROFMASS_FORCE_CONSTANT=Double.parseDouble(magnitude);
		CENTEROFMASS_EXPONENT=Double.parseDouble(exponent);	
	}
	public void alterWall(String id, String magnitude, String exponent){
		int wallIndex=(int)(Double.parseDouble(id)-1);
		double mag=Double.parseDouble(magnitude);
		double exp=Double.parseDouble(exponent);
		wallForceExponents[wallIndex]=exp; 
		WALL_FORCE_CONSTANT[wallIndex]=mag;

	}
	public void createMuscle(String id1, String id2, String rest, String K, String amp){
		double restLength=Double.parseDouble(rest);
		double springConstant=Double.parseDouble(K);
		double amplitude=Double.parseDouble(amp);
		new Muscle(id1, id2, restLength, springConstant, amplitude);
	}
	public void createSpring(String id1, String id2, String rest, String K){
		double restLength=Double.parseDouble(rest);
		double springConstant=Double.parseDouble(K);
		new Spring(id1, id2, restLength, springConstant);

	}
	public void createMass(String id, String xpos, String ypos, String xveloc, String yveloc, String mass){
		double xPosition=Double.parseDouble(xpos);
		double yPosition=Double.parseDouble(ypos);
		double xVelocity=Double.parseDouble(xveloc);
		double yVelocity=Double.parseDouble(yveloc);
		double massValue=Double.parseDouble(mass);
		new Mass(id, xPosition, yPosition, xVelocity, yVelocity, massValue);
	}
	public void createFixed(String id, String xpos, String ypos){
		double xPosition=Double.parseDouble(xpos);
		double yPosition=Double.parseDouble(ypos);
		new FixedMass(id, xPosition, yPosition);

	}
}