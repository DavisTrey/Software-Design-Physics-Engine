package jboxGlue;

import jgame.JGColor;

public class Muscle extends Spring{
	
	private double originalLength;
	private double myAmplitude;
	private static final double DEFAULT_FREQUENCY=1;
	private static final double DEFAULT_AMP_INCREMENT=1;
	private static double myFrequency=DEFAULT_FREQUENCY; //preference
	private static double myAmpIncrement=DEFAULT_AMP_INCREMENT;  //preference
	private double myTime;
	private static final double DEFAULT_INCREMENT=.05;
	private static final double MUSCLE_INCREMENTATION_CONSTANT= DEFAULT_INCREMENT;
	private static String musclePositiveColor="red";
	private static String muscleNegativeColor="blue";
	public Muscle(PhysicalObjectCircle mass1, PhysicalObjectCircle mass2, double length,
			double springiness, double amplitude) {
		super(mass1, mass2, length, springiness);
		myAmplitude = amplitude;
		myTime = 0;
		originalLength = length;
		restLength=originalLength;
	}
	
	public static void setPreferences(double frequency, double ampIncrement){
		myFrequency=frequency;
		myAmpIncrement=ampIncrement;
	}
	@Override
	protected void setColor() {
		myEngine.setColor(JGColor.white);
		if(Math.sin(myTime)>0){
			color(musclePositiveColor, 255*Math.sin(myTime));
		}
		if(Math.sin(myTime)<0){
			color(muscleNegativeColor, -255*Math.sin(myTime));
		}
	}
	public void incrementMuscle(){
		myTime=myTime+=MUSCLE_INCREMENTATION_CONSTANT;
		restLength=(originalLength+myAmplitude*Math.sin(myFrequency*myTime));
	}
	
	public void incrementAmplitude(){
		myAmplitude+=myAmpIncrement;
	}
	public void decrementAmplitude(){
		myAmplitude-=myAmpIncrement;
	}
	public static void setColors(String springColor1, String springColor2) {
		musclePositiveColor = springColor1;
		muscleNegativeColor = springColor2;
	}
	
}
