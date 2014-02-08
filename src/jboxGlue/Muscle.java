package jboxGlue;

public class Muscle extends Spring{
	
	private double originalLength;
	private double myAmplitude;
	private double myTime;
	public Muscle(PhysicalObjectCircle mass1, PhysicalObjectCircle mass2, double length,
			double springiness, double amplitude) {
		super(mass1, mass2, length, springiness);
		myAmplitude = amplitude;
		double myTime = 0;
		originalLength = length;
		restLength=originalLength;
	}
	
	public void incrementMuscle(){
		myTime=myTime+.1;
		restLength=(originalLength+myAmplitude*Math.sin(myTime));
	}
	
	public void incrementAmplitude(){
		myAmplitude+=10;
	}
	public void decrementAmplitude(){
		myAmplitude-=10;
	}
}
