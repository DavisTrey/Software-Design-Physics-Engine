package XML;

import org.w3c.dom.Element;

import springies.Springies;
import jboxGlue.FixedMass;
import jboxGlue.Mass;
import jboxGlue.Muscle;
import jboxGlue.PhysicalObjectCircle;
import jboxGlue.Spring;

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
		
		/*
		int massColor1=(int)parse("mass1", element);
		int massColor2=(int)parse("mass2", element);
		int massColor3=(int)parse("mass3", element);
		int massColor4=(int)parse("mass4", element);
		int massColor5=(int)parse("mass5", element);
		int massColor6=(int)parse("mass6", element);
		int massColor7=(int)parse("mass7", element);
		int massColor8=(int)parse("mass8", element);
		PhysicalObjectCircle.setColors(massColor1, massColor2, massColor3, massColor4, massColor5, massColor6, massColor7, massColor8);
		*/
	}

}
