package jboxGlue;

import org.jbox2d.dynamics.Body;

public abstract class Force {
	protected double magnitude;
	protected boolean isOn;
	private char myID;
	public Force(double mag, char ID){
		magnitude = mag;
		isOn = true;
		myID = ID;
	}
	public void toggleForce(){
		isOn = !isOn;
	}
	public boolean isOn(){
		return isOn;
	}
	public void doForce(Body b){
		if(isOn){
			applyForce(b);
		}
	}
	public char getID(){
		return myID;
	}
	public abstract void applyForce(Body b);
}
