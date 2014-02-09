package jboxGlue;

import java.util.List;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;

public class CenterOfMassForce extends Force {
	double exponent;
	List<CenterOfMass> myCenters;
	public CenterOfMassForce(double mag, double power, List<CenterOfMass> c) {
		super(mag, 'M');
		exponent = power;
		myCenters = c;
	}
	public void applyForce(Body b) {
		World myWorld = b.getWorld();
		CenterOfMass relevantCenter = new CenterOfMass();
		for(CenterOfMass c: myCenters){
			if(myWorld==c.getWorld())
				relevantCenter = c;
		}
		Vec2 centerOfMass = relevantCenter.getCenterOfMass();
		double xComp = centerOfMass.x - b.m_xf.position.x;
		double yComp = centerOfMass.y - b.m_xf.position.y;
		double distance = Math.sqrt(Math.pow(xComp, 2)+Math.pow(yComp, 2));
		xComp = xComp/distance;
		yComp = yComp/distance;
		double forceMagnitude = magnitude/Math.pow(distance, Math.abs(exponent));
		Vec2 comForce = new Vec2((float)(forceMagnitude*xComp),(float)(forceMagnitude*yComp));
		if(exponent<0){
			b.applyForce(comForce.negate(), b.m_xf.position);
		}
		else{
			b.applyForce(comForce, b.m_xf.position);
		}
	}

}
