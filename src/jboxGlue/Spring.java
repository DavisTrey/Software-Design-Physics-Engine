package jboxGlue;
import org.jbox2d.dynamics.Body;

import jgame.JGColor;
import jgame.JGObject;
import jgame.impl.JGEngineInterface;

// Spring extends JGObject but we don't want it to extend PhysicalObject 
// because it won't interact with the physics engine directly.
public class Spring extends JGObject{
    protected JGEngineInterface myEngine;
	public PhysicalObjectCircle myMass1;
	public PhysicalObjectCircle myMass2;
	public Spring(String massid1, String massid2, double length, double springiness){
		super("spring", true, 0, 0 ,0, null);
		myMass1 = (PhysicalObjectCircle) WorldManager.getBodies().get(massid1).getUserData();
		myMass2 = (PhysicalObjectCircle) WorldManager.getBodies().get(massid2).getUserData();
		myEngine = eng;
	}
    public void paint()
    {
        myEngine.setColor(JGColor.white);
        myEngine.drawLine(myMass1.getBody().getPosition().x, myMass1.getBody().getPosition().y,
        		myMass2.getBody().getPosition().x, myMass2.getBody().getPosition().y);;
    }
}
