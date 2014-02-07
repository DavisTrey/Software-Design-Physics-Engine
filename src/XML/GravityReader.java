package XML;

import org.w3c.dom.Element;



public class GravityReader extends XMLReadingAbstract{

	@Override
	public void readData(Element element) {
		// TODO Auto-generated method stub
		String direction=element.getAttribute("direction");
		String magnitude=element.getAttribute("magnitude");
		mySpringies.alterGravity(direction, magnitude);
	}

}
