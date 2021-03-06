package jboxGlue;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

public class WallForce extends Force{
	// 0 = north 1 = right 2 = south 3 = left
	int wallId;
	Wall myWall;
	double exponent;
	public WallForce(double mag, double exp, int id, Wall wall) {
		super(mag, (char)(id+49));
		wallId = id;
		myWall = wall;
		exponent = exp;
	}

	@Override
	public void applyForce(Body b) {
		if(wallId == 0)
			b.applyForce(new Vec2((float)0,(float)(magnitude/(Math.pow(Math.abs(b.m_xf.position.y-myWall.getPhysicalWall().y),exponent)))), b.m_xf.position);
		if(wallId == 1)
			b.applyForce(new Vec2((float)((-1)*magnitude/(Math.pow(Math.abs(b.m_xf.position.x-myWall.getPhysicalWall().x),exponent))), (float)0), b.m_xf.position);
		if(wallId == 2)
			b.applyForce(new Vec2((float)0,(float)((-1)*magnitude/(Math.pow(Math.abs(b.m_xf.position.y-myWall.getPhysicalWall().y),exponent)))), b.m_xf.position);
		if(wallId == 3)
			b.applyForce(new Vec2((float)(magnitude/(Math.pow(Math.abs(b.m_xf.position.x-myWall.getPhysicalWall().x),exponent))), (float)0), b.m_xf.position);
	}
	public void editWall(Wall wall){
		myWall = wall;
	}

}
