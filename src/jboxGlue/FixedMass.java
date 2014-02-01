package jboxGlue;

import jgame.JGColor;

public class FixedMass extends PhysicalObjectCircle {
	
	public FixedMass(String id, double x, double y, double mass){
		super(id, 1, JGColor.red, 10, mass);
		this.setPos(x, y);
	}
	public void move(){
		
	}
}
