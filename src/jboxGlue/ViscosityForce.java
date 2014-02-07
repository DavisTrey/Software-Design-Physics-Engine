package jboxGlue;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

public class ViscosityForce extends Force{

	public ViscosityForce(double mag) {
		super(mag);
	}

	public void applyForce(Body b) {
		b.applyForce(new Vec2((float)magnitude*-1*b.getLinearVelocity().x,
				(float)magnitude*-1*b.getLinearVelocity().y), b.m_xf.position);
	}

}
