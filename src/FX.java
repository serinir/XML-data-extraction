import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class FX extends Xmlers{

	@Override
	void read(String file) throws IOException, ParserConfigurationException, SAXException {
		final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(false);
        factory.setValidating(false);
        factory.setFeature("http://xml.org/sax/features/namespaces", false);
        factory.setFeature("http://xml.org/sax/features/validation", false);
        factory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
        factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        final DocumentBuilder builder = factory.newDocumentBuilder();
        final Document document = builder.parse(file);
        final NodeList racine = document.getElementsByTagName("GridPane");
        parcours(racine);
        // extractAttribute(racine.getAttributes());
    }
    void parcours(NodeList n){
        if(n!=null)
            for(int i=0;i<n.getLength();i++){
                if(n.item(i).getAttributes()!=null)
                    extractAttribute(n.item(i).getAttributes());
                if(n.item(i).getChildNodes()!=null)
                    parcours(n.item(i).getChildNodes());
            }
    }
    private void extractAttribute(NamedNodeMap n){
        for(int i=0;i<n.getLength();i++)
            nodevect.add(n.item(i).getNodeName()+","+n.item(i).getNodeValue());
    }
	@Override
	void build(String output) throws ParserConfigurationException, TransformerException {
		final DocumentBuilderFactory factory =  DocumentBuilderFactory.newInstance();
        final DocumentBuilder builder = factory.newDocumentBuilder();
        final DOMImplementation domimp = builder.getDOMImplementation();
        final Document document = domimp.createDocument(null, "Racine", null);
        final Element racine = document.getDocumentElement();
        racine.setAttribute("xmlns:fx", "http://javafx.com/fxml");
        for(int i=0;i<nodevect.size();i++){
            String name = nodevect.get(i).split(",")[0];
            String value = nodevect.get(i).split(",")[1];
            Element node = document.createElement("text");
            // Attr attname = document.createAttribute(name);

            node.setAttribute(name,"x");
            node.appendChild(document.createTextNode(value));
            racine.appendChild(node);
        }
        final TransformerFactory tfactory = TransformerFactory.newInstance();
        final Transformer transformer = tfactory.newTransformer();
        final DOMSource source = new DOMSource(document);
        final StreamResult sortie = new StreamResult(new File(output));
        transformer.setOutputProperty(OutputKeys.VERSION,"1.0");
        transformer.setOutputProperty(OutputKeys.ENCODING,"UTF-8");
        transformer.setOutputProperty(OutputKeys.STANDALONE,"yes");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        transformer.transform(source, sortie);
		
	}
    

}