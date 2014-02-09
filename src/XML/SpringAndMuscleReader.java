package XML;

import org.w3c.dom.Element;



public class SpringAndMuscleReader extends XMLReadingAbstract{
	protected static final String DEFAULT_REST="150";
	protected static final String DEFAULT_SPRINGCONSTANT="1";
	@Override
	public void readData(Element element) {
		// TODO Auto-generated method stub
		String id1=element.getAttribute("a");
		String id2=element.getAttribute("b");
		String restLength=element.getAttribute("restlength");
		String springConstant=element.getAttribute("constant");
		String amplitude=element.getAttribute("amplitude");
		if(restLength==""){
			restLength=DEFAULT_REST;
		}
		if(springConstant==""){
			springConstant=DEFAULT_SPRINGCONSTANT;
		}
		if(amplitude!=""){
			
			myManager.createMuscle(id1, id2, restLength, springConstant, amplitude);
		}
		else{
			myManager.createSpring(id1, id2, restLength, springConstant);
		}
		
	}

}
