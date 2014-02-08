package jboxGlue;

import org.jbox2d.dynamics.Body;

public abstract class Force {
	protected double magnitude;
	protected boolean isOn;
	public Force(double mag){
		magnitude = mag;
		isOn = true;
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
	public abstract void applyForce(Body b);
}
