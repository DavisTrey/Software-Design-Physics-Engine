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
		double muscleFreq=parse("muscleFreq", element);
		double muscleAmpIncrement=parse("muscleAmpIncrement", element);
		Muscle.setPreferences(muscleFreq, muscleAmpIncrement);
		
		double massRadius=parse("massRadius", element);
		Mass.setPreferences(massRadius);
		
		double fixedRadius=parse("fixedRadius", element);
		FixedMass.setPreferences(fixedRadius);
		
		double wallIncrement=parse("wallIncrement", element);
		Springies.setPreferences(wallIncrement);
		
	}

}
