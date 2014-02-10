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
		double muscleFreq=parse("muscleFreq", element);
		double muscleAmpIncrement=parse("muscleAmpIncrement", element);
		Muscle.setPreferences(muscleFreq, muscleAmpIncrement);
		
		double massRadius=parse("massRadius", element);
		Mass.setPreferences(massRadius);
		
		double fixedRadius=parse("fixedRadius", element);
		FixedMass.setPreferences(fixedRadius);
		
		double wallIncrement=parse("wallIncrement", element);
		Springies.setPreferences(wallIncrement);
		
		double springColor1=parse("springColor1", element);
		double springColor2=parse("springColor2", element);
		Spring.setColors(springColor1, springColor2);
		
		double muscleColor1=parse("muscleColor1", element);
		double muscleColor2=parse("muscleColor2", element);
		Muscle.setColors(muscleColor1, muscleColor2);
		
		int massColor1=(int)parse("mass1", element);
		int massColor2=(int)parse("mass2", element);
		int massColor3=(int)parse("mass3", element);
		int massColor4=(int)parse("mass4", element);
		int massColor5=(int)parse("mass5", element);
		int massColor6=(int)parse("mass6", element);
		int massColor7=(int)parse("mass7", element);
		int massColor8=(int)parse("mass8", element);
		PhysicalObjectCircle.setColors(massColor1, massColor2, massColor3, massColor4, massColor5, massColor6, massColor7, massColor8);
	}

}
