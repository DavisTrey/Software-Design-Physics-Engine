package jboxGlue;

public class Muscle extends Spring{
	
	private double originalLength;
	private double myAmplitude;
	private double myTime;
	public Muscle(String massid1, String massid2, double length,
			double springiness, double amplitude) {
		super(massid1, massid2, length, springiness);
		myAmplitude = amplitude;
		double myTime = 0;
		originalLength = length;
		restLength=originalLength;
	}
	
	public void incrementMuscle(){
		myTime=myTime+.1;
		restLength=(originalLength+myAmplitude*Math.sin(myTime));
	}

}
