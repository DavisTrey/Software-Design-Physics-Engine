package jboxGlue;

import jgame.JGColor;
import jboxGlue.PhysicalObjectCircle;

public class FixedMass extends PhysicalObjectCircle {
	private static final double DEFAULT_MASS = 1;
	private static final double DEFAULT_RADIUS = 10;
	public FixedMass(String id, double x, double y){
		super(id, 1, JGColor.red, DEFAULT_RADIUS, DEFAULT_MASS);
		this.setPos(x, y);
	}
}
