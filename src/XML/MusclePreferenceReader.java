package XML;

import org.w3c.dom.Element;

public class MusclePreferenceReader extends XMLReadingAbstract{

	@Override
	public void readData(Element element) {
		// TODO Auto-generated method stub
		String freq=element.getAttribute("freq");
		String ampIncrement=element.getAttribute("ampIncrement");
		mySpringies.alterMusclePreferences(freq, ampIncrement);
	}

}
