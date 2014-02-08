package jboxGlue;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

public class GravityForce extends Force{
	private double direction;
	private Vec2 myForce;
	public GravityForce(double mag, double dir) {
		super(mag);
		direction = dir;
		myForce = new Vec2((float)(magnitude*Math.cos(direction*3.14/180)),(float)(magnitude*Math.sin(direction*3.14/180)));
	}
	public void applyForce(Body b) {
		b.applyForce(myForce, b.m_xf.position);
	}
}
