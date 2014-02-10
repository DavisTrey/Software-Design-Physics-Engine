package XML;

import org.w3c.dom.Element;



public class NormalMassReader extends XMLReadingAbstract{
	@Override
	public void readData(Element element) {
		// TODO Auto-generated method stub
		String id=element.getAttribute("id");
		double xpos=parseInteger("x", element);
		double ypos=parseInteger("y", element);
		double xVeloc=testForDefault("vx", element);
		double yVeloc=testForDefault("vy", element);
		double mass=testForDefault("mass", element);
		
		myManager.createMass(id, xpos, ypos, xVeloc, yVeloc, mass);
	}

}
