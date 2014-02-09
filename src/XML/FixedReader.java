package XML;

import org.w3c.dom.Element;



public class FixedReader extends XMLReadingAbstract{
	@Override
	public void readData(Element element) {
		// TODO Auto-generated method stub
		String id=element.getAttribute("id");
		String xpos=element.getAttribute("x");
		String ypos=element.getAttribute("y");
		myManager.createFixed(id, xpos, ypos);
	}

}
