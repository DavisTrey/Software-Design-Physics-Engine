package jboxGlue;

public class MouseManager {
	private boolean isOn;
	private Mass clickMass;
	private Mass targetMass;
	private Spring clickSpring;
	public MouseManager(){
		clickMass = null;
		targetMass = null;
		clickSpring = null;
		isOn = false;
	}
	public MouseManager(Mass tMass, Mass cMass, Spring cSpring){
		clickMass = cMass;
		targetMass = tMass;
		clickSpring = cSpring;
		isOn = true;
	}
	public void setPos(double x, double y){
		clickMass.setPos(x,y);
	}
	public boolean isOn(){
		return isOn;
	}
	public void turnOff(){
		isOn=false;
		targetMass = null;
		clickMass.destroy();
		clickMass = null;
		clickSpring.destroy();
		clickSpring = null;
	}
	public void applyForce(){
		clickSpring.applyForce();
	}
}
