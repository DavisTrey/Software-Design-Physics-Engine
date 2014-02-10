package XML;

import org.w3c.dom.Element;



public class FixedReader extends XMLReadingAbstract{
	@Override
	public void readData(Element element) {
		// TODO Auto-generated method stub
		String id=element.getAttribute("id");
		double xpos=parseInteger("x", element);
		double ypos=parseInteger("y", element);
		myManager.createFixed(id, xpos, ypos);
	}

}
