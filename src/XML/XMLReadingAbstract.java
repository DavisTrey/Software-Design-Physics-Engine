package XML;
import java.util.HashMap;
import java.util.Map;

import jboxGlue.FixedMass;
import jboxGlue.Mass;
import jboxGlue.Muscle;
import jboxGlue.PhysicalObjectCircle;
import jboxGlue.Spring;
import springies.*;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public abstract class XMLReadingAbstract{
	protected XMLManager myManager;
	protected Map<String, Double> defaultMap=new HashMap<String, Double>();
	protected static final double DEFAULT_VELOCITY=0;
	protected static final double DEFAULT_MASS=1;
	protected static final double DEFAULT_REST=(double)150;
	protected static final double DEFAULT_SPRINGCONSTANT=(double)1;
	
	public void readNodes(String tag, Document doc, XMLManager m){
		initializeDefaultMap();
		myManager=m;
    	NodeList nodes=doc.getElementsByTagName(tag);
    	for(int i=0; i<nodes.getLength(); i++){
    		Node node=nodes.item(i);
    		if(node.getNodeType()==Node.ELEMENT_NODE){
    			Element element=(Element) node;
    			readData(element);
    		}
    	}
    }
	public double parseInteger(String s, Element e){
		return Double.parseDouble(e.getAttribute(s));
	}
	public void initializeDefaultMap(){
		defaultMap.put("vx", DEFAULT_VELOCITY);
		defaultMap.put("vy", DEFAULT_VELOCITY);
		defaultMap.put("mass", DEFAULT_MASS);
		defaultMap.put("restlength", DEFAULT_REST);
		defaultMap.put("constant", DEFAULT_SPRINGCONSTANT);
	}
	public double testForDefault(String s, Element e){
		if(e.getAttribute(s)==""){
			return defaultMap.get(s);
		}
		else{
			return Double.parseDouble(e.getAttribute(s));
		}
	}
	public abstract void readData(Element element);
}