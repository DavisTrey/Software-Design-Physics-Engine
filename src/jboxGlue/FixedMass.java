package jboxGlue;

import jgame.JGColor;

public class FixedMass extends PhysicalObjectCircle {
	
	public FixedMass(String id, int x, int y, int mass){
		super(id, 1, JGColor.red, 10, mass);
		this.setPos(x, y);
	}

}
