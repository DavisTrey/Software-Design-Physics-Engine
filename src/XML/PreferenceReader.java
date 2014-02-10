package XML;

import org.w3c.dom.Element;

import springies.Springies;
import jboxGlue.FixedMass;
import jboxGlue.Mass;
import jboxGlue.Muscle;

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
		
	}

}
