package XML;

import java.awt.Frame;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

import springies.Springies;

import java.awt.Frame;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import XML.*;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import jboxGlue.CenterOfMass;
import jboxGlue.CenterOfMassForce;
import jboxGlue.FixedMass;
import jboxGlue.Force;
import jboxGlue.GravityForce;
import jboxGlue.Mass;
import jboxGlue.Muscle;
import jboxGlue.PhysicalObject;
import jboxGlue.PhysicalObjectCircle;
import jboxGlue.PhysicalObjectRect;
import jboxGlue.Spring;
import jboxGlue.ViscosityForce;
import jboxGlue.Wall;
import jboxGlue.WallForce;
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


public class XMLManager {
	Springies mySpringies;
	Map<Integer, HashMap<String, PhysicalObjectCircle>> myBodies;
	int assemblyNumber;
	Set<PhysicalObjectCircle> fullBodyList;
	Set<Spring> mySprings;
	Map<Character, Force> myForces;
	public XMLManager(Springies s, int assemblyNumber, Map<Integer, HashMap<String, PhysicalObjectCircle>> bodies, Set<PhysicalObjectCircle> fullBodyList, Set<Spring> mySprings,  Map<Character, Force> myForces){
		mySpringies=s;
		myBodies=bodies;
		this.assemblyNumber=assemblyNumber;
		this.fullBodyList=fullBodyList;
		this.mySprings=mySprings;
		this.myForces=myForces;
		
	}
	public void XMLPreferences(){
    	Frame message=new Frame();
    	Object[] options={"Yes", "No"};
    	int n=JOptionPane.showOptionDialog(message, "Would you like to loead a preference file?", "XML Loading", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
    	if(n==0){
    		File dataFile=fileRetriever();
    		readPreferences(dataFile);
    	}
    	
    }
    public void readPreferences(File datafile){
    	try {
    		Document doc=DocumentBuilder(datafile);
    		new MusclePreferenceReader().readNodes("muscle", doc, this);


    	} catch (Exception ex) {
    		ex.printStackTrace();
    	}
    }

    public void XMLMessage(){
		try {
			Frame message=new Frame();
			JOptionPane.showMessageDialog(message, "Please load your object XML File");
			File dataFile=fileRetriever();
			readObjectData(dataFile);

			message=new Frame();
			Object[] options={"Yes", "No"};
			int n=JOptionPane.showOptionDialog(message, "Would you like to load an enviroment file?", "XML Loading", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
			if(n==0){
					dataFile=fileRetriever();
				    readEnvironmentData(dataFile);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
    public File fileRetriever(){
    	File dataFile = new File("");
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
		        "XML Files", "xml");
		    chooser.setFileFilter(filter);
		    int returnVal = chooser.showOpenDialog(mySpringies.getParent());
		    if(returnVal == JFileChooser.APPROVE_OPTION) {
				dataFile = chooser.getSelectedFile();
		    }
		    return dataFile;
    }
    
    public Document DocumentBuilder(File dataFile){
    	try{
    		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
    		Document doc = dBuilder.parse(dataFile);
    		doc.getDocumentElement().normalize();	
    		return doc;
    	} catch (Exception ex) {
		ex.printStackTrace();
	}
    	return null; //To fix Eclipse error with returns in try{}
    }
    public void readObjectData(File dataFile){
		try {
			Document doc=DocumentBuilder(dataFile);
			new FixedReader().readNodes("fixed", doc, this);
			new NormalMassReader().readNodes("mass", doc, this);
			new SpringAndMuscleReader().readNodes("spring", doc, this);
			new SpringAndMuscleReader().readNodes("muscle", doc, this);
					
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	public void readEnvironmentData(File dataFile){
		try {
			Document doc=DocumentBuilder(dataFile);
			new GravityReader().readNodes("gravity", doc, this);
			new ViscosityReader().readNodes("viscosity", doc, this);
			new CenterOfMassReader().readNodes("centermass", doc, this);
			new WallReader().readNodes("wall", doc, this);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	public void createMuscle(String id1, String id2, String rest, String K, String amp){
		double restLength=Double.parseDouble(rest);
		double springConstant=Double.parseDouble(K);
		double amplitude=Double.parseDouble(amp);
		PhysicalObjectCircle mass1 = (PhysicalObjectCircle) myBodies.get(assemblyNumber).get(id1);
		PhysicalObjectCircle mass2 = (PhysicalObjectCircle) myBodies.get(assemblyNumber).get(id2);
		mySprings.add(new Muscle(mass1, mass2, restLength, springConstant, amplitude));
	}
	public void createSpring(String id1, String id2, String rest, String K){
		double restLength=Double.parseDouble(rest);
		double springConstant=Double.parseDouble(K);
		PhysicalObjectCircle mass1 = (PhysicalObjectCircle) myBodies.get(assemblyNumber).get(id1);
		PhysicalObjectCircle mass2 = (PhysicalObjectCircle) myBodies.get(assemblyNumber).get(id2);
		mySprings.add(new Spring(mass1, mass2, restLength, springConstant));
		
	}
	public void createMass(String id, String xpos, String ypos, String xveloc, String yveloc, String mass){
		double xPosition=Double.parseDouble(xpos);
		double yPosition=Double.parseDouble(ypos);
		double xVelocity=Double.parseDouble(xveloc);
		double yVelocity=Double.parseDouble(yveloc);
		double massValue=Double.parseDouble(mass);
		if(!myBodies.containsKey(assemblyNumber))
			myBodies.put(assemblyNumber, new HashMap<String, PhysicalObjectCircle>());
		myBodies.get(assemblyNumber).put(id, new Mass(id, xPosition, yPosition, xVelocity, yVelocity, massValue, assemblyNumber));
		fullBodyList.add(myBodies.get(assemblyNumber).get(id));
	}
	public void createFixed(String id, String xpos, String ypos){
		double xPosition=Double.parseDouble(xpos);
		double yPosition=Double.parseDouble(ypos);
		if(!myBodies.containsKey(assemblyNumber))
			myBodies.put(assemblyNumber, new HashMap<String, PhysicalObjectCircle>());
		myBodies.get(assemblyNumber).put(id, new FixedMass(id, xPosition, yPosition, assemblyNumber));
		fullBodyList.add(myBodies.get(assemblyNumber).get(id));
	}
	public void alterGravity(String direction, String magnitude){
    	double direct=Double.parseDouble(direction);
    	double mag=Double.parseDouble(magnitude);
    	myForces.put('G', new GravityForce(mag, direct));
    }
    public void alterViscosity(String magnitude){
    	myForces.put('V', new ViscosityForce(Double.parseDouble(magnitude)));
    }
    public void alterCenterMass(String magnitude, String exponent){
    	myForces.put('M', new CenterOfMassForce(Double.parseDouble(magnitude),Double.parseDouble(exponent), mySpringies.returnCenterOfMass()));
    }
    
    public void alterWall(String id, String magnitude, String exponent){
    	int wallIndex=(int)(Double.parseDouble(id)-1);
    	double mag=Double.parseDouble(magnitude);
    	double exp=Double.parseDouble(exponent);
    	myForces.put((char)(wallIndex+49), new WallForce(mag, exp, wallIndex, mySpringies.returnWalls()[wallIndex]));
    }
}