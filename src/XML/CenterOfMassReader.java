package XML;

import org.w3c.dom.Element;



public class CenterOfMassReader extends XMLReadingAbstract{

	@Override
	public void readData(Element element) {
		// TODO Auto-generated method stub
		String magnitude=element.getAttribute("magnitude");
		String exponent=element.getAttribute("exponent");
		mySpringies.alterCenterMass(magnitude, exponent);
	}
	
}
