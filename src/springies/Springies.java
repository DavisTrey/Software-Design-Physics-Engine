package springies;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import jboxGlue.FixedMass;
import jboxGlue.Mass;
import jboxGlue.PhysicalObject;
import jboxGlue.PhysicalObjectCircle;
import jboxGlue.PhysicalObjectRect;
import jboxGlue.WorldManager;
import jgame.JGColor;
import jgame.JGObject;
import jgame.platform.JGEngine;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class Springies extends JGEngine{
	public static double viscosity = 30;
	private static final String DEFAULT_VELOCITY="0";
	private static final String DEFAULT_MASS="1";
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
        readData();
    }
    public void readData(){
		try {

			File dataFile = new File("src/springies/XML.xml");
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
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	private String getValue(String tag, Element element) {
		NodeList nodes = element.getElementsByTagName(tag).item(0).getChildNodes();
		Node node = (Node) nodes.item(0);
		return node.getNodeValue();
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
	
    public void addBall ()
    {
        // add a bouncy ball
        // NOTE: you could make this into a separate class, but I'm lazy
        PhysicalObject ball = new PhysicalObjectCircle("ball", 1, JGColor.blue, 10, 5) {
            @Override
            public void hit (JGObject other)
            {
                // we hit something! bounce off it!
                Vec2 velocity = myBody.getLinearVelocity();
                // is it a tall wall?
                final double DAMPING_FACTOR = 0.8;
                boolean isSide = other.getBBox().height > other.getBBox().width;
                if (isSide) {
                    velocity.x *= -DAMPING_FACTOR;
                }
                else {
                    velocity.y *= -DAMPING_FACTOR;
                }
                // apply the change
                myBody.setLinearVelocity(velocity);
            }
        };
        ball.setPos(displayWidth() / 2, displayHeight() / 2);
        ball.setForce(8000, -10000);
    }

    private void addWalls ()
    {
        // add walls to bounce off of
        // NOTE: immovable objects must have no mass
        final double WALL_MARGIN = 10;
        final double WALL_THICKNESS = 10;
        final double WALL_WIDTH = displayWidth() - WALL_MARGIN * 2 + WALL_THICKNESS;
        final double WALL_HEIGHT = displayHeight() - WALL_MARGIN * 2 + WALL_THICKNESS;
        PhysicalObject wall = new PhysicalObjectRect("wall", 2, JGColor.green,
                                                     WALL_WIDTH, WALL_THICKNESS);
        wall.setPos(displayWidth() / 2, WALL_MARGIN);
        wall = new PhysicalObjectRect("wall", 2, JGColor.green,
                                      WALL_WIDTH, WALL_THICKNESS);
        wall.setPos(displayWidth() / 2, displayHeight() - WALL_MARGIN);
        wall = new PhysicalObjectRect("wall", 2, JGColor.green,
                                      WALL_THICKNESS, WALL_HEIGHT);
        wall.setPos(WALL_MARGIN, displayHeight() / 2);
        wall = new PhysicalObjectRect("wall", 2, JGColor.green,
                                      WALL_THICKNESS, WALL_HEIGHT);
        wall.setPos(displayWidth() - WALL_MARGIN, displayHeight() / 2);
    }

    @Override
    public void doFrame ()
    {
        // update game objects
    	for(Body b=WorldManager.getWorld().getBodyList(); b!=null; b=b.getNext()){
    		b.applyForce(new Vec2((float)viscosity*-1*b.getLinearVelocity().x, (float)viscosity*-1*b.getLinearVelocity().y), b.m_xf.position);
    	}
        WorldManager.getWorld().step(1f, 1);
        moveObjects();
        //checkCollision(1 + 2, 1);
    }

    @Override
    public void paintFrame ()
    {
        // nothing to do
        // the objects paint themselves
    }
}
