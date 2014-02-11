package XML;

import java.util.HashMap;

import org.w3c.dom.Element;

import springies.Springies;
import jboxGlue.FixedMass;
import jboxGlue.Mass;
import jboxGlue.Muscle;
import jboxGlue.PhysicalObjectCircle;
import jboxGlue.Spring;
import jgame.JGColor;

public class PreferenceReader extends XMLReadingAbstract{

	@Override
	public void readData(Element element) {
		// TODO Auto-generated method stub
		double muscleFreq=parseInteger("muscleFreq", element);
		double muscleAmpIncrement=parseInteger("muscleAmpIncrement", element);
		Muscle.setPreferences(muscleFreq, muscleAmpIncrement);
		
		double massRadius=parseInteger("massRadius", element);
		Mass.setPreferences(massRadius);
		
		double fixedRadius=parseInteger("fixedRadius", element);
		FixedMass.setPreferences(fixedRadius);
		
		double wallIncrement=parseInteger("wallIncrement", element);
		Springies.setPreferences(wallIncrement);
		
		String springColor1=element.getAttribute("springColor1");
		String springColor2=element.getAttribute("springColor2");
		Spring.setColors(springColor1, springColor2);
		
		String muscleColor1=element.getAttribute("muscleColor1");
		String muscleColor2=element.getAttribute("muscleColor2");
		Muscle.setColors(muscleColor1, muscleColor2);
		
		String massColor1=element.getAttribute("mass1");
		String massColor2=element.getAttribute("mass2");
		String massColor3=element.getAttribute("mass3");
		String massColor4=element.getAttribute("mass4");
		String massColor5=element.getAttribute("mass5");
		String massColor6=element.getAttribute("mass6");
		String massColor7=element.getAttribute("mass7");
		String massColor8=element.getAttribute("mass8");
		HashMap<String, JGColor> colorMap = new HashMap<String, JGColor>();
		colorMap.put("white", JGColor.white);
		colorMap.put("red", JGColor.red);
		colorMap.put("cyan", JGColor.cyan);
		colorMap.put("magenta", JGColor.magenta);
		colorMap.put("yellow", JGColor.yellow);
		colorMap.put("pink", JGColor.pink);
		colorMap.put("grey", JGColor.grey);
		colorMap.put("blue", JGColor.blue);
		PhysicalObjectCircle.setColors(colorMap.get(massColor1), colorMap.get(massColor2),
				colorMap.get(massColor3), colorMap.get(massColor4), colorMap.get(massColor5), 
				colorMap.get(massColor6), colorMap.get(massColor7), colorMap.get(massColor8));
	}

}
