package jboxGlue;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;

public class CenterOfMass {
	World myWorld;
	Vec2 myCenter;
	public CenterOfMass(){
		myCenter = new Vec2(0,0);
	}
	public CenterOfMass(World w){
		myWorld = w;
		myCenter = new Vec2(0,0);
	}
    public Vec2 getCenterOfMass(){
    	return new Vec2(myCenter.x, myCenter.y);
    }
    public void setCenterOfMass(){
    	double initx = 0;
    	double inity = 0;
    	double totalmass = 0;
    	for(Body b=myWorld.getBodyList(); b!=null; b=b.getNext()){
    		if(b.getUserData() instanceof PhysicalObjectCircle){
    		initx+=(b.m_xf.position.x)*(b.getMass());
    		inity+=(b.m_xf.position.y)*(b.getMass());
    		totalmass+=b.getMass();
    		}
    	}
    	myCenter = new Vec2((float)(initx/totalmass),(float)(inity/totalmass));
    }
    public World getWorld(){
    	return myWorld;
    }
}
