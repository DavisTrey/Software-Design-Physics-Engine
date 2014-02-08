package springies;

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


public class Springies extends JGEngine{
	protected static final String DEFAULT_VELOCITY="0";
	protected static final String DEFAULT_MASS="1";
	protected static final String DEFAULT_REST="150";
	protected static final String DEFAULT_SPRINGCONSTANT="1";
	private static final double DEFAULT_GRAVITY = 20;
	private static final double DEFAULT_VISCOSITY = 2;
	private static final double DEFAULT_CENTEROFMASS_FORCE_CONSTANT = 20;
	private static final double DEFAULT_CENTEROFMASS_EXPONENT = 2;
	private static final double[] DEFAULT_WALL_FORCE_EXPONENTS = {2,2,2,2};
	private static final double DEFAULT_WALL_FORCE_CONSTANT[] = {100000, 100000, 100000, 100000};
    private Map<Integer, HashMap<String, PhysicalObjectCircle>> myBodies = new HashMap<Integer, HashMap<String, PhysicalObjectCircle>>();
    private Set<PhysicalObjectCircle> fullBodyList = new HashSet<PhysicalObjectCircle>();
    private Set<Spring> mySprings=new HashSet<Spring>();
    private List<CenterOfMass> myCentersOfMass = new ArrayList<CenterOfMass>();
    private Wall[] myWalls= new Wall[4];
	//protected static PhysicalObject[] walls = new PhysicalObject[4];
	private int wallModifier = 0;
	protected int assemblyNumber = 0;
	// Forces are indexed as follows: 0=gravity, 1=viscosity, 2=centerofmass, 3=top wall, 4=right wall, 5=bottom wall, 6=left wall
	protected static Force[] forces = new Force[7];
	
	
    public Springies (){

        // set the window size
        int height = 700;
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
        myCentersOfMass.add(new CenterOfMass(WorldManager.getWorld(0)));
        for(int i=0; i<4; i++){
        	myWalls[i] = new Wall();
        }
        addWalls();
        addForces();
        XMLMessage();
        
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
		    int returnVal = chooser.showOpenDialog(getParent());
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
    public void alterGravity(String direction, String magnitude){
    	double direct=Double.parseDouble(direction);
    	double mag=Double.parseDouble(magnitude);
    	forces[0] = new GravityForce(mag, direct);
    }
    public void alterViscosity(String magnitude){
    	forces[1] = new ViscosityForce(Double.parseDouble(magnitude));
    }
    public void alterCenterMass(String magnitude, String exponent){
    	forces[2] = new CenterOfMassForce(Double.parseDouble(magnitude),Double.parseDouble(exponent), myCentersOfMass);
    }
    
    public void alterWall(String id, String magnitude, String exponent){
    	int wallIndex=(int)(Double.parseDouble(id)-1);
    	double mag=Double.parseDouble(magnitude);
    	double exp=Double.parseDouble(exponent);
    	forces[wallIndex+3] = new WallForce(mag, exp, wallIndex, myWalls[wallIndex]);
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
	

	private void addWalls ()
    {
        // add walls to bounce off of
        // NOTE: immovable objects must have no mass
        final double WALL_MARGIN = 10;
        final double WALL_THICKNESS = 10;
        final double WALL_WIDTH = displayWidth() + wallModifier - WALL_MARGIN * 2 + WALL_THICKNESS;
        final double WALL_HEIGHT = displayHeight() + wallModifier - WALL_MARGIN * 2 + WALL_THICKNESS;
        for(int i=0; i<WorldManager.getWorlds().size(); i++){
        myWalls[0].addWall(new PhysicalObjectRect("wall", 2, JGColor.green,
                                                     WALL_WIDTH, WALL_THICKNESS, i));
        myWalls[0].setPos((displayWidth() + wallModifier) / 2, WALL_MARGIN);
        myWalls[2].addWall(new PhysicalObjectRect("wall", 2, JGColor.green,
                                      WALL_WIDTH, WALL_THICKNESS, i));
        myWalls[2].setPos((displayWidth() + wallModifier) / 2, displayHeight() + wallModifier - WALL_MARGIN);
        myWalls[3].addWall(new PhysicalObjectRect("wall", 2, JGColor.green,
                                      WALL_THICKNESS, WALL_HEIGHT, i));
        myWalls[3].setPos(WALL_MARGIN, (displayHeight() + wallModifier) / 2);
        myWalls[1].addWall(new PhysicalObjectRect("wall", 2, JGColor.green,
                                      WALL_THICKNESS, WALL_HEIGHT, i));
        myWalls[1].setPos(displayWidth() + wallModifier - WALL_MARGIN, (displayHeight() + wallModifier) / 2);
        }
    }

    // addWalls must be called before this method is called!!!
	private void addForces() {
		forces[0] = new GravityForce(DEFAULT_GRAVITY, 90);
		forces[1] = new ViscosityForce(DEFAULT_VISCOSITY);
		forces[2] = new CenterOfMassForce(DEFAULT_CENTEROFMASS_FORCE_CONSTANT, DEFAULT_CENTEROFMASS_EXPONENT, myCentersOfMass);
		forces[3] = new WallForce(DEFAULT_WALL_FORCE_CONSTANT[0], DEFAULT_WALL_FORCE_EXPONENTS[0],0,myWalls[0]);
		forces[4] = new WallForce(DEFAULT_WALL_FORCE_CONSTANT[1], DEFAULT_WALL_FORCE_EXPONENTS[1],1,myWalls[1]);
		forces[5] = new WallForce(DEFAULT_WALL_FORCE_CONSTANT[2], DEFAULT_WALL_FORCE_EXPONENTS[2],2,myWalls[2]);
		forces[6] = new WallForce(DEFAULT_WALL_FORCE_CONSTANT[3], DEFAULT_WALL_FORCE_EXPONENTS[3],3,myWalls[3]);
	}
	
	private void clearWalls(){
		for(Wall w: myWalls){
			w.destroy();
		}
	}

	@Override
    public void doFrame ()
    {
		checkKeyInput();
        // update game objects
		for(CenterOfMass c: myCentersOfMass){
			c.setCenterOfMass();
		}
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
			addWalls();
			updateWallForce();
		}
		if(getKey(KeyDown)){
			clearKey(KeyDown);
			wallModifier-=10;
			clearWalls();
			addWalls();
			updateWallForce();
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
	        WorldManager.initWorld(this);
	        myCentersOfMass.add(new CenterOfMass(WorldManager.getWorld(assemblyNumber)));
	        WorldManager.getWorld(assemblyNumber).setGravity(new Vec2(0.0f, 0.0f));
			XMLMessage();
		}
		if(getKey('C')){
			clearKey('C');
			for(PhysicalObjectCircle c: fullBodyList){
				c.destroy();
			}
			for(Spring s: mySprings){
				s.destroy();
			}
			System.out.println(myBodies.size());
			mySprings.clear();
			myCentersOfMass.clear();
		}
	}

	private void decrementAmplitudes() {
    	for(Spring s: mySprings){
    		if(s instanceof Muscle)
    			((Muscle) s).decrementAmplitude();
		}
	}

	private void incrementAmplitudes() {
    	for(Spring s: mySprings){
    		if(s instanceof Muscle)
    			((Muscle) s).incrementAmplitude();
		}
	}

	private void updateWallForce() {
		for(int i=3; i<7; i++){
			((WallForce) forces[i]).editWall(myWalls[i-3]);
		}
	}

	private void applyForces(Body b) {
		for(int i=0; i<7; i++){
			forces[i].doForce(b);
		}
	}
	private void applySpringForce(){
    	for(Spring s: mySprings){
    		if(s instanceof Muscle)
    			((Muscle) s).incrementMuscle();
    		s.applyForce();
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
