package XML;

import org.w3c.dom.Element;



public class ViscosityReader extends XMLReadingAbstract{

	@Override
	public void readData(Element element) {
		// TODO Auto-generated method stub
		String magnitude=element.getAttribute("magnitude");
		myManager.alterViscosity(magnitude);
	}

}
