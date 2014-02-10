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
	private boolean isDestroyed;
	private static int springStretchColor=2;
	private static int springCompressColor=4;
	public Spring(PhysicalObjectCircle mass1, PhysicalObjectCircle mass2, double length, double springiness){
		super("spring", true, 0, 0 ,0, null);
		restLength=length;
		springConstant=springiness;
		myMass1 = mass1;
		myMass2 = mass2;
		myEngine = eng;
	}
	public void paint(){
		if(!isDestroyed){
		setColor();
		myEngine.drawLine(myMass1.getBody().getPosition().x, myMass1.getBody().getPosition().y,
				myMass2.getBody().getPosition().x, myMass2.getBody().getPosition().y);
		}
	}
	protected void setColor() {
		myEngine.setColor(JGColor.white);
		if(restLength>getLength()){
			color(springStretchColor, (restLength-getLength())*2);
		}
		if(restLength<getLength()){
			color(springCompressColor, (getLength()-restLength)*2);
		}
	}
	protected void color(int i, double argument){
		if(i==0){
			myEngine.setColor(new JGColor(255-(int)Math.max(Math.min(255,argument), 0),
					255-(int)Math.max(Math.min(255,argument), 0),
					255-(int)Math.max(Math.min(255,argument), 0)));
		}
		if(i==1){
			myEngine.setColor(new JGColor(255,
					255-(int)Math.max(Math.min(255,argument), 0),
					255-(int)Math.max(Math.min(255,argument), 0)));
		}
		if(i==2){
			myEngine.setColor(new JGColor(255-(int)Math.max(Math.min(255,argument), 0),
					255,
					255-(int)Math.max(Math.min(255,argument), 0)));
		}
		if(i==3){
			myEngine.setColor(new JGColor(255-(int)Math.max(Math.min(255,argument), 0),
					255-(int)Math.max(Math.min(255,argument), 0),
					255));
		}
		if(i==4){
			myEngine.setColor(new JGColor(255-(int)Math.max(Math.min(255,argument), 0),
					255,
					255));
		}
		if(i==5){
			myEngine.setColor(new JGColor(255,
					255-(int)Math.max(Math.min(255,argument), 0),
					255));
		}
		if(i==6){
			myEngine.setColor(new JGColor(255-(int)Math.max(argument, 0),
					255,
					255));
		}
	}
	public void applyForce(){
		if(!isDestroyed){
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
	public void destroy(){
		isDestroyed = true;
	}
	public double getLength(){
		double xVector=myMass1.getBody().m_xf.position.x-myMass2.getBody().m_xf.position.x;
		double yVector=myMass1.getBody().m_xf.position.y-myMass2.getBody().m_xf.position.y;
		return Math.pow(Math.pow(xVector, 2)+Math.pow(yVector, 2), .5);
	}
	public static void setColors(double springColor1, double springColor2) {
		springStretchColor = (int)springColor1;
		springCompressColor = (int)springColor2;
	}
}

