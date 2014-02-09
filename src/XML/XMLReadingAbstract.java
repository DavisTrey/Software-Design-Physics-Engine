package XML;
import java.util.HashMap;

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
	public void readNodes(String tag, Document doc, XMLManager m){
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
	public double parse(String s, Element e){
		return Double.parseDouble(e.getAttribute(s));
	}
	public abstract void readData(Element element);
}