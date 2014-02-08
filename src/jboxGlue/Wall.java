package jboxGlue;

import java.util.ArrayList;
import java.util.List;

import org.jbox2d.dynamics.Body;

public class Wall {
	private List<PhysicalObjectRect> myWalls = new ArrayList<PhysicalObjectRect>();
	public Wall(){
	}
	public void addWall(PhysicalObjectRect p){
		myWalls.add(p);
	}
	public void setPos(double x, double y){
		for(PhysicalObjectRect p: myWalls){
			p.setPos(x, y);
		}
	}
	public PhysicalObjectRect getPhysicalWall(){
		return myWalls.get(0);
	}
	public void destroy() {
		for(PhysicalObjectRect p: myWalls){
			p.destroy();
		}
		myWalls.clear();
	}
}
