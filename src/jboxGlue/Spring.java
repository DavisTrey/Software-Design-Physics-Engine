package jboxGlue;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

import jgame.JGColor;
import jgame.JGObject;
import jgame.impl.JGEngineInterface;

// Spring extends JGObject but we don't want it to extend PhysicalObject 
// because it won't interact with the physics engine directly.
public class Spring extends JGObject{
    protected JGEngineInterface myEngine;
	private PhysicalObjectCircle myMass1;
	private PhysicalObjectCircle myMass2;
	protected double restLength;
	private double springConstant;
	public Spring(String massid1, String massid2, double length, double springiness){
		super("spring", true, 0, 0 ,0, null);
		restLength=length;
		springConstant=springiness;
		myMass1 = (PhysicalObjectCircle) WorldManager.getBodies().get(massid1).getUserData();
		myMass2 = (PhysicalObjectCircle) WorldManager.getBodies().get(massid2).getUserData();
		myEngine = eng;
		WorldManager.addSpring(this);
	}
	public void paint(){
		myEngine.setColor(JGColor.white);
		myEngine.drawLine(myMass1.getBody().getPosition().x, myMass1.getBody().getPosition().y,
				myMass2.getBody().getPosition().x, myMass2.getBody().getPosition().y);;
	}
	public void applyForce(){

		double xVector=myMass1.getBody().m_xf.position.x-myMass2.getBody().m_xf.position.x;
		double yVector=myMass1.getBody().m_xf.position.y-myMass2.getBody().m_xf.position.y;
		double magnitude=Math.pow(Math.pow(xVector, 2)+Math.pow(yVector, 2), .5);
		double displacement=magnitude-restLength;
		double Force=springConstant*displacement;
		
		double normalizedX=xVector/magnitude;
		double normalizedY=yVector/magnitude;
		Vec2 vector=new Vec2((float)(normalizedX*Math.abs(Force)), (float)(normalizedY*Math.abs(Force)));
	
		
		
		if(Force<0){
			//Pushing out
			myMass1.getBody().applyForce(vector, myMass1.getBody().m_xf.position);
			myMass2.getBody().applyForce(vector.negate(), myMass2.getBody().m_xf.position);		
		}
		else if(Force>0){
			//Pulling in
			myMass1.getBody().applyForce(vector.negate(), myMass1.getBody().m_xf.position);
			myMass2.getBody().applyForce(vector, myMass2.getBody().m_xf.position);

		}
		
	}
}

