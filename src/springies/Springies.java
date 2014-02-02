package springies;

import java.io.File;
import java.util.HashSet;

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


public class Springies extends JGEngine{
	public static double viscosity = 5;
	private static final String DEFAULT_VELOCITY="0";
	private static final String DEFAULT_MASS="1";
	private static final String DEFAULT_REST="150";
	public static final String DEFAULT_SPRINGCONSTANT="1";
	private static final double WALL_FORCE_CONSTANT = 100000;
	private static final double CENTEROFMASS_FORCE_CONSTANT = 50000;
	private static double centerOfMassExponent = -2;
	public static double[] wallForces = {2,2,2,2};
	public static PhysicalObject[] walls = new PhysicalObject[4];
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
        WorldManager.getWorld().setGravity(new Vec2(0.0f, 0.1f));
        addWalls();
        readXMLData();
        
    }
    public void readXMLData(){
		try {

			File dataFile = new File("src/springies/daintywalker.xml");
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
					
					System.out.println("ID: " + id);
					System.out.println("X Position: " + xpos);
					System.out.println("Y Position: " + ypos);
					System.out.println("X Velocity: "+ xVeloc);
					System.out.println("Y Velocity: "+ yVeloc);
					System.out.println("Mass: " + mass);
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
						
						System.out.println("id1: " + id1);
						System.out.println("id2: " + id2);
						System.out.println("Rest Length: " + restLength);
						System.out.println("K: " + springConstant);
					}
			}
			}
				
			
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
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
	
    private void addWalls ()
    {
        // add walls to bounce off of
        // NOTE: immovable objects must have no mass
        final double WALL_MARGIN = 10;
        final double WALL_THICKNESS = 10;
        final double WALL_WIDTH = displayWidth() - WALL_MARGIN * 2 + WALL_THICKNESS;
        final double WALL_HEIGHT = displayHeight() - WALL_MARGIN * 2 + WALL_THICKNESS;
        walls[0] = new PhysicalObjectRect("wall", 2, JGColor.green,
                                                     WALL_WIDTH, WALL_THICKNESS);
        walls[0].setPos(displayWidth() / 2, WALL_MARGIN);
        walls[2] = new PhysicalObjectRect("wall", 2, JGColor.green,
                                      WALL_WIDTH, WALL_THICKNESS);
        walls[2].setPos(displayWidth() / 2, displayHeight() - WALL_MARGIN);
        walls[3] = new PhysicalObjectRect("wall", 2, JGColor.green,
                                      WALL_THICKNESS, WALL_HEIGHT);
        walls[3].setPos(WALL_MARGIN, displayHeight() / 2);
        walls[1] = new PhysicalObjectRect("wall", 2, JGColor.green,
                                      WALL_THICKNESS, WALL_HEIGHT);
        walls[1].setPos(displayWidth() - WALL_MARGIN, displayHeight() / 2);
    }

    @Override
    public void doFrame ()
    {
        // update game objects
    	Vec2 center = findCenterOfMass();
    	for(Body b=WorldManager.getWorld().getBodyList(); b!=null; b=b.getNext()){
    	   applyViscosity(b);
    	   applyWallForce(b);
    	   applyCenterOfMassForce(b, center);
    	}
        WorldManager.getWorld().step(1f, 1);
    	applySpringForce();
        moveObjects();
        checkCollision(2, 1);
    }

	private void applyCenterOfMassForce(Body b, Vec2 centerOfMass) {
		double xComp = centerOfMass.x - b.m_xf.position.x;
		double yComp = centerOfMass.y - b.m_xf.position.y;
		double distance = Math.sqrt(Math.pow(xComp, 2)+Math.pow(yComp, 2));
		xComp = xComp/distance;
		yComp = yComp/distance;
		double magnitude = CENTEROFMASS_FORCE_CONSTANT/Math.pow(distance, Math.abs(centerOfMassExponent));
		Vec2 comForce = new Vec2((float)(magnitude*xComp),(float)(magnitude*yComp));
		if(centerOfMassExponent<0){
			b.applyForce(comForce.negate(), b.m_xf.position);
		}
		else{
			b.applyForce(comForce, b.m_xf.position);
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
	private void applyWallForce(Body b) {
		b.applyForce(new Vec2((float)0,(float)(WALL_FORCE_CONSTANT/(Math.pow(Math.abs(b.m_xf.position.y-walls[0].y),wallForces[0])))), b.m_xf.position);
		b.applyForce(new Vec2((float)((-1)*WALL_FORCE_CONSTANT/(Math.pow(Math.abs(b.m_xf.position.x-walls[1].x),wallForces[1]))), (float)0), b.m_xf.position);
		b.applyForce(new Vec2((float)0,(float)((-1)*WALL_FORCE_CONSTANT/(Math.pow(Math.abs(b.m_xf.position.y-walls[2].y),wallForces[2])))), b.m_xf.position);
		b.applyForce(new Vec2((float)(WALL_FORCE_CONSTANT/(Math.pow(Math.abs(b.m_xf.position.x-walls[3].x),wallForces[3]))), (float)0), b.m_xf.position);
	}

	private void applyViscosity(Body b) {
		b.applyForce(new Vec2((float)viscosity*-1*b.getLinearVelocity().x,
				(float)viscosity*-1*b.getLinearVelocity().y), b.m_xf.position);
	}
    private Vec2 findCenterOfMass() {
    	double initx = 0;
    	double inity = 0;
    	double totalmass = 0;
    	for(Body b=WorldManager.getWorld().getBodyList(); b!=null; b=b.getNext()){
    		initx+=(b.m_xf.position.x)*(b.getMass());
    		inity+=(b.m_xf.position.y)*(b.getMass());
    		totalmass+=b.getMass();
    	}
    	//System.out.println(initx/totalmass);
    	//System.out.println(inity/totalmass);
    	//System.out.println("");
    	return new Vec2((float)(initx/totalmass),(float)(inity/totalmass));
	}

    @Override
    public void paintFrame ()
    {
        // nothing to do
        // the objects paint themselves
    }
}
