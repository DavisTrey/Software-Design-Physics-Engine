package XML;

import org.w3c.dom.Element;


public class WallReader extends XMLReadingAbstract {

	@Override
	public void readData(Element element) {
		// TODO Auto-generated method stub
		String id=element.getAttribute("id");
		String magnitude=element.getAttribute("magnitude");
		String exponent=element.getAttribute("exponent");
		myManager.alterWall(id, magnitude, exponent);
	}
	
}
