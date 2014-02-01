package jboxGlue;

import jgame.JGColor;
import jboxGlue.PhysicalObject;
import jboxGlue.PhysicalObjectCircle;
import jboxGlue.PhysicalObjectRect;
import jboxGlue.WorldManager;
import jgame.JGColor;
import jgame.JGObject;
import jgame.platform.JGEngine;
import org.jbox2d.common.Vec2;


public class FixedMass extends PhysicalObjectCircle {
	private static final double DEFAULT_MASS = 1;
	private static final double DEFAULT_RADIUS = 10;
	public FixedMass(String id, double x, double y){
		super(id, 1, JGColor.red, DEFAULT_RADIUS, DEFAULT_MASS);
		this.setPos(100, 100);
	}
}
