package jboxGlue;

public class Muscle extends Spring{
	
	private double myLength;
	private double myAmplitude;
	private int myTime;
	public Muscle(String massid1, String massid2, double length,
			double springiness, double amplitude) {
		super(massid1, massid2, length, springiness);
		myAmplitude = amplitude;
		myTime = 0;
		myLength = length;
		restLength = myLength*myAmplitude*Math.sin(myTime);
	}
	
	public void incrementMuscle(){
		myTime++;
		restLength = myLength*myAmplitude*Math.sin(myTime);
	}

}
