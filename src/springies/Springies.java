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
import jboxGlue.MouseManager;
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
	private static final double DEFAULT_WALL_INCREMENT=10;
	private static double myWallIncrement=DEFAULT_WALL_INCREMENT; //preference
	protected static final String DEFAULT_VELOCITY="0";
	protected static final String DEFAULT_MASS="1";
	protected static final String DEFAULT_REST="150";
	protected static final String DEFAULT_SPRINGCONSTANT="1";
	private static final char GRAVITY = 'G';
	private static final char VISCOSITY = 'V';
	private static final char CENTEROFMASS = 'M';
	private static final char NORTHWALL = '1';
	private static final char EASTWALL = '2';
	private static final char SOUTH = '3';
	private static final char WESTWALL = '4';
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
	private int wallModifier = 0;
	protected int assemblyNumber = 0;
	private MouseManager myMouse;
	private Map<Character, Force> myForces = new HashMap<Character, Force>();
	
	public static void setPreferences(double wallIncrement){
		myWallIncrement=wallIncrement;
	}
	public Set<Spring> returnSprings(){
		return mySprings;
	}
	public Set<PhysicalObjectCircle> returnFullBodyList(){
		return fullBodyList;
	}
	public Map<Integer, HashMap<String, PhysicalObjectCircle>> returnBodyMap(){
		return myBodies;
	}
	public List<CenterOfMass> returnCenterOfMass(){
		return myCentersOfMass;
	}
	public Wall[] returnWalls(){
		return myWalls;
	}
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
        myMouse = new MouseManager();
        for(int i=0; i<4; i++){
        	myWalls[i] = new Wall();
        }
        addWalls();
        addForces();
       XMLManager xml=new XMLManager(this, assemblyNumber, myBodies, fullBodyList, mySprings, myForces);
       xml.XMLPreferences();
       xml.loadObjectAndEnvironmentData();
    }
 
	// addWalls must be called before this method is called!!!
	private void addForces() {
		myForces.put('G', new GravityForce(DEFAULT_GRAVITY, 90));
		myForces.put('V', new ViscosityForce(DEFAULT_VISCOSITY));
		myForces.put('M', new CenterOfMassForce(DEFAULT_CENTEROFMASS_FORCE_CONSTANT, DEFAULT_CENTEROFMASS_EXPONENT, myCentersOfMass));
		myForces.put('1', new WallForce(DEFAULT_WALL_FORCE_CONSTANT[0], DEFAULT_WALL_FORCE_EXPONENTS[0],0,myWalls[0]));
		myForces.put('2', new WallForce(DEFAULT_WALL_FORCE_CONSTANT[1], DEFAULT_WALL_FORCE_EXPONENTS[1],1,myWalls[1]));
		myForces.put('3', new WallForce(DEFAULT_WALL_FORCE_CONSTANT[2], DEFAULT_WALL_FORCE_EXPONENTS[2],2,myWalls[2]));
		myForces.put('4', new WallForce(DEFAULT_WALL_FORCE_CONSTANT[3], DEFAULT_WALL_FORCE_EXPONENTS[3],3,myWalls[3]));
	}

	private void addWalls ()
    {
        // add walls to bounce off of
        // NOTE: immovable objects must have no mass
        final double WALL_MARGIN = 10;
        final double WALL_THICKNESS = 10;
        final double WALL_WIDTH = displayWidth() + 2*wallModifier - WALL_MARGIN * 2 + WALL_THICKNESS;
        final double WALL_HEIGHT = displayHeight() + 2*wallModifier - WALL_MARGIN * 2 + WALL_THICKNESS;
        for(int i=0; i<WorldManager.getWorlds().size(); i++){
        myWalls[0].addWall(new PhysicalObjectRect("wall", 2, JGColor.green,
                                                     WALL_WIDTH, WALL_THICKNESS, i));
        myWalls[0].setPos((displayWidth()) / 2, WALL_MARGIN - wallModifier);
        myWalls[2].addWall(new PhysicalObjectRect("wall", 2, JGColor.green,
                                      WALL_WIDTH, WALL_THICKNESS, i));
        myWalls[2].setPos((displayWidth()) / 2, displayHeight() + wallModifier - WALL_MARGIN);
        myWalls[3].addWall(new PhysicalObjectRect("wall", 2, JGColor.green,
                                      WALL_THICKNESS, WALL_HEIGHT, i));
        myWalls[3].setPos(WALL_MARGIN - wallModifier, (displayHeight()) / 2);
        myWalls[1].addWall(new PhysicalObjectRect("wall", 2, JGColor.green,
                                      WALL_THICKNESS, WALL_HEIGHT, i));
        myWalls[1].setPos(displayWidth() + wallModifier - WALL_MARGIN, (displayHeight()) / 2);
        }
    }

    private void clearWalls(){
		for(Wall w: myWalls){
			w.destroy();
		}
	}

	private void updateWallForce() {
		for(int i=0; i<4; i++){
			((WallForce) myForces.get((char)(i+49))).editWall(myWalls[i]);
		}
	}

	@Override
    public void doFrame ()
    {
		checkKeyInput();
		checkMouseInput();
		setCentersOfMass();
    	applyAllForces();
    	applySpringForce();
        moveObjects();
        checkCollision(2, 1);
    }

	private void checkKeyInput() {
		//Listen for key input
		for(Character c: myForces.keySet()){
			if(getKey(myForces.get(c).getID())){
				clearKey(myForces.get(c).getID());
				myForces.get(c).toggleForce();
			}
		}
		if(getKey(KeyUp)){
			clearKey(KeyUp);
			wallModifier+=myWallIncrement;
			clearWalls();
			addWalls();
			updateWallForce();
		}
		if(getKey(KeyDown)){
			clearKey(KeyDown);
			wallModifier-=myWallIncrement;
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
			clearWalls();
	        addWalls();
	        new XMLManager(this, assemblyNumber, myBodies, fullBodyList, mySprings, myForces).loadObjectAndEnvironmentData();
			
		}
		if(getKey('C')){
			clearKey('C');
			for(PhysicalObjectCircle c: fullBodyList){
				c.destroy();
			}
			for(Spring s: mySprings){
				s.destroy();
			}
			mySprings.clear();
			fullBodyList.clear();
			myCentersOfMass.clear();
		}
	}

	private void checkMouseInput() {
		if(getMouseButton(1)){
			if(!myMouse.isOn()&&!fullBodyList.isEmpty()){
				turnOnMouseListener();
			}
			if(myMouse.isOn())
				myMouse.setPos(getMouseX(), getMouseY());
		}
		if(!getMouseButton(1)){
			if(myMouse.isOn())
				myMouse.turnOff();
		}
	}
	private void turnOnMouseListener() {
		Mass targetMass = null;
		for(PhysicalObjectCircle p : fullBodyList){
			if(p instanceof Mass){
			if(targetMass==null){
				targetMass = (Mass) p;
			}
			else{
				if(Math.pow(Math.pow(p.getBody().m_xf.position.x-getMouseX(), 2)+Math.pow(p.getBody().m_xf.position.y-getMouseY(), 2), .5)
						<Math.pow(Math.pow(targetMass.getBody().m_xf.position.x-getMouseX(), 2)+Math.pow(targetMass.getBody().m_xf.position.y-getMouseY(), 2), .5))
					targetMass = (Mass)p;
			}
			}
		}
		Mass clickMass = new Mass("-1", (double)getMouseX(), getMouseY(), (double)0, (double)0, (double).1, targetMass.getWorldID());
		Spring clickSpring = new Spring(clickMass, targetMass, 
				Math.pow(Math.pow(targetMass.getBody().m_xf.position.x-getMouseX(), 2)+Math.pow(targetMass.getBody().m_xf.position.y-getMouseY(), 2), .5), 2);
		myMouse = new MouseManager(targetMass, clickMass, clickSpring);
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
	private void setCentersOfMass() {
		for(CenterOfMass c: myCentersOfMass){
			c.setCenterOfMass();

		}
	}

	private void applyAllForces() {
		for(int i=0; i<WorldManager.getWorlds().size(); i++){
    	for(Body b=WorldManager.getWorld(i).getBodyList(); b!=null; b=b.getNext()){
    	   applyForces(b);
    	}
        WorldManager.getWorld(i).step(1f, 1);
    	}
	}

	private void applyForces(Body b) {
		for(Character c: myForces.keySet()){
			myForces.get(c).doForce(b);
		}
	}

	private void applySpringForce(){
		for(Spring s: mySprings){
			if(s instanceof Muscle)
				((Muscle) s).incrementMuscle();
			s.applyForce();
		}
		if(myMouse.isOn())
			myMouse.applyForce();
	}


	@Override
    public void paintFrame ()
    {
    	StringBuilder forceStatus = new StringBuilder();
    	for(Character c: myForces.keySet()){
    		if(myForces.get(c).isOn()){
    			forceStatus.append(myForces.get(c).getID());
    			forceStatus.append(' ');
    		}
    	}
		drawString(forceStatus.toString(), 20,20,-1);
    }

}
