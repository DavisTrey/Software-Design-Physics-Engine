package jboxGlue;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

public class CenterOfMassForce extends Force {
	double exponent;
	public CenterOfMassForce(double mag, double power) {
		super(mag);
		exponent = power;
	}

	@Override
	public void applyForce(Body b) {
		Vec2 centerOfMass = WorldManager.getCenterOfMass();
		double xComp = centerOfMass.x - b.m_xf.position.x;
		double yComp = centerOfMass.y - b.m_xf.position.y;
		double distance = Math.sqrt(Math.pow(xComp, 2)+Math.pow(yComp, 2));
		xComp = xComp/distance;
		yComp = yComp/distance;
		double forceMagnitude = magnitude/Math.pow(distance, Math.abs(exponent));
		Vec2 comForce = new Vec2((float)(magnitude*xComp),(float)(magnitude*yComp));
		if(exponent<0){
			b.applyForce(comForce.negate(), b.m_xf.position);
		}
		else{
			b.applyForce(comForce, b.m_xf.position);
		}
	}

}
