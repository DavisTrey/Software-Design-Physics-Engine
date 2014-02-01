package jboxGlue;

import org.jbox2d.common.Vec2;

import jgame.JGColor;

public class Mass extends PhysicalObjectCircle {
	private static final double DEFAULT_RADIUS = 10;
	public Mass(String id, double x, double y, double xvelo, double yvelo, double mass){
		super(id, 1, JGColor.white, DEFAULT_RADIUS, mass);
		this.setPos(x, y);
		this.setVelo(xvelo, yvelo);
		WorldManager.addBody(id, this.getBody());
	}
	
	public void setVelo(double x, double y){
        // there's no body while the game object is initializing
        if (myBody != null) { 
            // set velocity of jbox2d object, not jgame object
        	myBody.setLinearVelocity(new Vec2((float)x, (float)y));
        }
	}

}
