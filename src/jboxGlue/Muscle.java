package jboxGlue;

import jgame.JGColor;

public class Muscle extends Spring{
	
	private double originalLength;
	private double myAmplitude;
	private double myTime;
	private static final double MUSCLE_INCREMENTATION_CONSTANT= .05;
	public Muscle(PhysicalObjectCircle mass1, PhysicalObjectCircle mass2, double length,
			double springiness, double amplitude) {
		super(mass1, mass2, length, springiness);
		myAmplitude = amplitude;
		double myTime = 0;
		originalLength = length;
		restLength=originalLength;
	}
	
	@Override
	protected void setColor() {
		myEngine.setColor(JGColor.white);
		if(Math.sin(myTime)>0){
			myEngine.setColor(new JGColor((int)(255*Math.sin(myTime)), (int)(255*Math.sin(myTime)), 255));
		}
		if(Math.sin(myTime)<0){
			myEngine.setColor(new JGColor(255, (int)(-255*Math.sin(myTime)), (int)(-255*Math.sin(myTime))));
		}
	}
	public void incrementMuscle(){
		myTime=myTime+=MUSCLE_INCREMENTATION_CONSTANT;
		restLength=(originalLength+myAmplitude*Math.sin(myTime));
	}
	
	public void incrementAmplitude(){
		myAmplitude+=10;
	}
	public void decrementAmplitude(){
		myAmplitude-=10;
	}
}
