package XML;
import springies.*;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public abstract class XMLReadingAbstract{
	protected static Springies mySpringies;
	public void readNodes(String tag, Document doc, Springies sp){
		mySpringies=sp;
    	NodeList nodes=doc.getElementsByTagName(tag);
    	for(int i=0; i<nodes.getLength(); i++){
    		Node node=nodes.item(i);
    		if(node.getNodeType()==Node.ELEMENT_NODE){
    			Element element=(Element) node;
    			readData(element);
    		}
    	}
    }
	public abstract void readData(Element element);
}