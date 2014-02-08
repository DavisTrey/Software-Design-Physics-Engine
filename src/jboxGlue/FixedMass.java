package jboxGlue;

import org.jbox2d.common.Vec2;

import jgame.JGColor;
import jboxGlue.PhysicalObjectCircle;

public class FixedMass extends PhysicalObjectCircle {
	private static final double DEFAULT_MASS = 1;
	private static final double DEFAULT_FIXEDRADIUS=4;
	private static double fixedRadius=DEFAULT_FIXEDRADIUS;
	private double myX;
	private double myY;
	public FixedMass(String id, double x, double y, int world){
		super(id, 1, JGColor.red, fixedRadius, DEFAULT_MASS, world);
		myX = x;
		myY = y;
		this.setPos(x, y);
	}
    public void move ()
    {
        // if the JGame object was deleted, remove the physical object too
        if (myBody.m_world != WorldManager.getWorld(worldID)) {
            remove();
            return;
        }
        // to ignore the effects of forces, reset fixedMass position to its proper location
        this.setPos(myX, myY);
        Vec2 position = myBody.getPosition();
        x = position.x;
        y = position.y;
        myRotation = -myBody.getAngle();
    }
}
