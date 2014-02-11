package XML;

import org.w3c.dom.Element;



public class SpringAndMuscleReader extends XMLReadingAbstract{
	
	@Override
	public void readData(Element element) {
		// TODO Auto-generated method stub
		String id1=element.getAttribute("a");
		String id2=element.getAttribute("b");
		double restLength=testForDefault("restlength", element);
		double springConstant=testForDefault("constant", element);
		String amplitude=element.getAttribute("amplitude"); //Dont use parseInteger to check for existence of amplitude
		
		if(amplitude!=""){
			myManager.createMuscle(id1, id2, restLength, springConstant, parseInteger("amplitude", element));
		}
		else{
			myManager.createSpring(id1, id2, restLength, springConstant);
		}
		
	}

}
