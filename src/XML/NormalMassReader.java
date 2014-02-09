package XML;

import org.w3c.dom.Element;



public class NormalMassReader extends XMLReadingAbstract{
	protected static final String DEFAULT_VELOCITY="0";
	protected static final String DEFAULT_MASS="1";
	@Override
	public void readData(Element element) {
		// TODO Auto-generated method stub
		String id=element.getAttribute("id");
		String xpos=element.getAttribute("x");
		String ypos=element.getAttribute("y");
		String xVeloc=element.getAttribute("vx");
		String yVeloc=element.getAttribute("vy");
		String mass=element.getAttribute("mass");
		if(mass==""){
			mass=DEFAULT_MASS;
		}
		if(yVeloc==""){
			yVeloc=DEFAULT_VELOCITY;
			xVeloc=DEFAULT_VELOCITY;
		}
		myManager.createMass(id, xpos, ypos, xVeloc, yVeloc, mass);
	}

}
