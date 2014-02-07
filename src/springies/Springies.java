package springies;

import java.awt.Frame;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

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


public class Springies extends JGEngine{
	protected static final String DEFAULT_VELOCITY="0";
	protected static final String DEFAULT_MASS="1";
	protected static final String DEFAULT_REST="150";
	protected static final String DEFAULT_SPRINGCONSTANT="1";
	private static final double DEFAULT_GRAVITY = 20;
	private static final double DEFAULT_VISCOSITY = 2;
	private static final double DEFAULT_CENTEROFMASS_FORCE_CONSTANT = 2;
	private static final double DEFAULT_CENTEROFMASS_EXPONENT = 2;
	private static final double[] DEFAULT_WALL_FORCE_EXPONENTS = {2,2,2,2};
	private static final double DEFAULT_WALL_FORCE_CONSTANT[] = {100000, 100000, 100000, 100000};
	private Vec2 centerOfMass = new Vec2(0,0);
	private static PhysicalObject[] walls = new PhysicalObject[4];
	private int wallModifier = 0;
	private int assemblyNumber = 0;
	// Forces are indexed as follows: 0=gravity, 1=viscosity, 2=centerofmass, 3=top wall, 4=right wall, 5=bottom wall, 6=left wall
	private static Force[] forces = new Force[7];
	
    public Springies (){

        // set the window size
        int height = 480;
        double aspect = 16.0 / 9.0;
        initEngineComponent((int) (height * aspect), height);
    }

    @Override
    public void initCanvas (){
        // I have no idea what tiles do...
        setCanvasSettings(1, // width of the canvas in tiles
                          1, // height of the canvas in tiles
                          displayWidth(), // width of one tile
                          displayHeight(), // height of one tile
                          null,// foreground colour -> use default colour white
                          null,// background colour -> use default colour black
                          null); // standard font -> use default font
    }

    @Override
    public void initGame ()
    {
        setFrameRate(60, 2);
        // NOTE:
        //   world coordinates have y pointing down
        //   game coordinates have y pointing up
        // so gravity is up in world coords and down in game coords
        // so set all directions (e.g., forces, velocities) in world coords
        WorldManager.initWorld(this);
        WorldManager.getWorld(0).setGravity(new Vec2(0.0f, 0.0f));
        addWalls();
        addForces();
        XMLMessage();
        
    }
    public void XMLMessage(){
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
			    
			readXMLData(dataFile);

			message=new Frame();
			Object[] options={"Yes", "No"};
			int n=JOptionPane.showOptionDialog(message, "Would you like to load an enviroment file?", "XML Loading", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
			if(n==0){
				dataFile = new File("");
				chooser = new JFileChooser();
				filter = new FileNameExtensionFilter(
				        "XML Files", "xml");
				    chooser.setFileFilter(filter);
				    returnVal = chooser.showOpenDialog(getParent());
				    if(returnVal == JFileChooser.APPROVE_OPTION) {
						dataFile = chooser.getSelectedFile();
				    }
				    readEnvironmentData(dataFile);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	public void readXMLData(File dataFile){
		try {
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
					String xVeloc=element.getAttribute("vx");
					String yVeloc=element.getAttribute("vy");
					String mass=element.getAttribute("mass");
					if(mass==""){
						mass=DEFAULT_MASS;
					}
					if(yVeloc==""){
						yVeloc=DEFAULT_VELOCITY;
						xVeloc=DEFAULT_VELOCITY;
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
						String restLength=element.getAttribute("restlength");
						String springConstant=element.getAttribute("constant");
						String amplitude=element.getAttribute("amplitude");
						if(restLength==""){
							restLength=DEFAULT_REST;
						}
						if(springConstant==""){
							springConstant=DEFAULT_SPRINGCONSTANT;
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
    public void readEnvironmentData(File dataFile){
    	try {
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
						alterWall(id, magnitude, exponent);
					}
				}
    	} catch (Exception ex) {
    		ex.printStackTrace();
    	}
    }
    public void alterGravity(String direction, String magnitude){
    	double direct=Double.parseDouble(direction);
    	double mag=Double.parseDouble(magnitude);
    	forces[0] = new GravityForce(mag, direct);
    }
    public void alterViscosity(String magnitude){
    	forces[1] = new ViscosityForce(Double.parseDouble(magnitude));
    }
    public void alterCenterMass(String magnitude, String exponent){
    	forces[2] = new CenterOfMassForce(Double.parseDouble(magnitude),Double.parseDouble(exponent));
    }
    
    public void alterWall(String id, String magnitude, String exponent){
    	int wallIndex=(int)(Double.parseDouble(id)-1);
    	double mag=Double.parseDouble(magnitude);
    	double exp=Double.parseDouble(exponent);
    	forces[wallIndex+3] = new WallForce(mag, exp, wallIndex, walls[wallIndex]);
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
		new Mass(id, xPosition, yPosition, xVelocity, yVelocity, massValue, assemblyNumber);
	}
	public void createFixed(String id, String xpos, String ypos){
		double xPosition=Double.parseDouble(xpos);
		double yPosition=Double.parseDouble(ypos);
		new FixedMass(id, xPosition, yPosition, assemblyNumber);
	
	}
	private void addWalls ()
    {
        // add walls to bounce off of
        // NOTE: immovable objects must have no mass
        final double WALL_MARGIN = 10;
        final double WALL_THICKNESS = 10;
        final double WALL_WIDTH = displayWidth() + wallModifier - WALL_MARGIN * 2 + WALL_THICKNESS;
        final double WALL_HEIGHT = displayHeight() + wallModifier - WALL_MARGIN * 2 + WALL_THICKNESS;
        for(int i=0; i<WorldManager.getWorlds().size(); i++){
        walls[0] = new PhysicalObjectRect("wall", 2, JGColor.green,
                                                     WALL_WIDTH, WALL_THICKNESS, i);
        walls[0].setPos((displayWidth() + wallModifier) / 2, WALL_MARGIN);
        walls[2] = new PhysicalObjectRect("wall", 2, JGColor.green,
                                      WALL_WIDTH, WALL_THICKNESS, i);
        walls[2].setPos((displayWidth() + wallModifier) / 2, displayHeight() + wallModifier - WALL_MARGIN);
        walls[3] = new PhysicalObjectRect("wall", 2, JGColor.green,
                                      WALL_THICKNESS, WALL_HEIGHT, i);
        walls[3].setPos(WALL_MARGIN, (displayHeight() + wallModifier) / 2);
        walls[1] = new PhysicalObjectRect("wall", 2, JGColor.green,
                                      WALL_THICKNESS, WALL_HEIGHT, i);
        walls[1].setPos(displayWidth() + wallModifier - WALL_MARGIN, (displayHeight() + wallModifier) / 2);
        }
    }

    // addWalls must be called before this method is called!!!
	private void addForces() {
		forces[0] = new GravityForce(DEFAULT_GRAVITY, 90);
		forces[1] = new ViscosityForce(DEFAULT_VISCOSITY);
		forces[2] = new CenterOfMassForce(DEFAULT_CENTEROFMASS_FORCE_CONSTANT, DEFAULT_CENTEROFMASS_EXPONENT);
		forces[3] = new WallForce(DEFAULT_WALL_FORCE_CONSTANT[0], DEFAULT_WALL_FORCE_EXPONENTS[0],0,walls[0]);
		forces[4] = new WallForce(DEFAULT_WALL_FORCE_CONSTANT[1], DEFAULT_WALL_FORCE_EXPONENTS[1],1,walls[1]);
		forces[5] = new WallForce(DEFAULT_WALL_FORCE_CONSTANT[2], DEFAULT_WALL_FORCE_EXPONENTS[2],2,walls[2]);
		forces[6] = new WallForce(DEFAULT_WALL_FORCE_CONSTANT[3], DEFAULT_WALL_FORCE_EXPONENTS[3],3,walls[3]);
	}
	
	private void clearWalls(){
		for(PhysicalObject p: walls){
			p.destroy();
		}
	}

	@Override
    public void doFrame ()
    {
		checkKeyInput();
        // update game objects
    	setCenterOfMass();
    	for(int i=0; i<WorldManager.getWorlds().size(); i++){
    	for(Body b=WorldManager.getWorld(i).getBodyList(); b!=null; b=b.getNext()){
    	   applyForces(b);
    	}
        WorldManager.getWorld(i).step(1f, 1);
    	}
    	applySpringForce();
        moveObjects();
        checkCollision(2, 1);
    }

	private void checkKeyInput() {
		//Listen for key input
		if(getKey('G')){
			clearKey('G');
			forces[0].toggleForce();
		}
		if(getKey('V')){
			clearKey('V');
			forces[1].toggleForce();
		}
		if(getKey('M')){
			clearKey('M');
			forces[2].toggleForce();
		}
		if(getKey('1')){
			clearKey('1');
			forces[3].toggleForce();
		}
		if(getKey('2')){
			clearKey('2');
			forces[4].toggleForce();
		}
		if(getKey('3')){
			clearKey('3');
			forces[5].toggleForce();
		}
		if(getKey('4')){
			clearKey('4');
			forces[6].toggleForce();
		}
		if(getKey(KeyUp)){
			clearKey(KeyUp);
			wallModifier+=10;
			clearWalls();
			updateWallForce();
			addWalls();
		}
		if(getKey(KeyDown)){
			clearKey(KeyDown);
			wallModifier-=10;
			clearWalls();
			updateWallForce();
			addWalls();
		}
		if(getKey('=')||getKey('+')){
			clearKey('=');
			clearKey('+');
			incrementAmplitudes();
		}
		if(getKey('-')){
			clearKey('-');
			decrementAmplitudes();
		}
		if(getKey('N')){
			clearKey('N');
			assemblyNumber++;
		}
	}

	private void decrementAmplitudes() {
    	HashSet<Spring> springs=WorldManager.getSprings();
    	for(Spring s: springs){
    		if(s instanceof Muscle)
    			((Muscle) s).decrementAmplitude();
		}
	}

	private void incrementAmplitudes() {
    	HashSet<Spring> springs=WorldManager.getSprings();
    	for(Spring s: springs){
    		if(s instanceof Muscle)
    			((Muscle) s).incrementAmplitude();
		}
	}

	private void updateWallForce() {
		for(int i=3; i<7; i++){
			((WallForce) forces[i]).editWall(walls[i-3]);
		}
	}

	private void applyForces(Body b) {
		for(int i=0; i<7; i++){
			forces[i].applyForce(b);
		}
	}
	private void applySpringForce(){
    	HashSet<Spring> springs=WorldManager.getSprings();
    	for(Spring s: springs){
    		if(s instanceof Muscle)
    			((Muscle) s).incrementMuscle();
    		s.applyForce();
		}
    }

	private void setCenterOfMass() {
    	double initx = 0;
    	double inity = 0;
    	double totalmass = 0;
    	for(int i=0; i<WorldManager.getWorlds().size(); i++){
    	for(Body b=WorldManager.getWorld(i).getBodyList(); b!=null; b=b.getNext()){
    		initx+=(b.m_xf.position.x)*(b.getMass());
    		inity+=(b.m_xf.position.y)*(b.getMass());
    		totalmass+=b.getMass();
    	}
    	WorldManager.setCenterOfMass(new Vec2((float)(initx/totalmass),(float)(inity/totalmass)),i);
    	}
	}

    @Override
    public void paintFrame ()
    {
    	StringBuilder forceStatus = new StringBuilder();
    	if(forces[0].isOn())
    		forceStatus.append("G ");
    	if(forces[1].isOn())
    		forceStatus.append("V ");
    	if(forces[2].isOn())
    		forceStatus.append("M ");
    	if(forces[3].isOn())
    		forceStatus.append("1 ");
    	if(forces[4].isOn())
    		forceStatus.append("2 ");
    	if(forces[5].isOn())
    		forceStatus.append("3 ");
    	if(forces[6].isOn())
    		forceStatus.append("4 ");
		drawString(forceStatus.toString(), 20,20,-1);
    }

}
