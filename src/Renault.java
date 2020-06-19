import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Renault extends Xmlers {

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
        final NodeList para = document.getElementsByTagName("p");
        for (int i=0;i<para.getLength();i++){
            NodeList li = para.item(i).getChildNodes();
            if(li.getLength() == 11){

                nodevect.add(para.item(i).getChildNodes().item(1).getTextContent().replaceAll(":|\n|\r","").strip());
                nodevect.add(para.item(i).getChildNodes().item(6).getTextContent().replaceAll(":|\n|\r","").strip());
                nodevect.add(para.item(i).getChildNodes().item(10).getTextContent().replaceAll(":|\n|\r","").strip());
            }
        }
		
	}

	@Override
	void build(String output) throws ParserConfigurationException, TransformerException {
		final DocumentBuilderFactory factory =  DocumentBuilderFactory.newInstance();
        final DocumentBuilder builder = factory.newDocumentBuilder();
        final DOMImplementation domimp = builder.getDOMImplementation();
        final Document document = domimp.createDocument(null, "Concessionnaires", null);
        document.setXmlStandalone(true);
        final Element racine = document.getDocumentElement();
        for(int i =0;i<nodevect.size();i+=3){
            Element nom = document.createElement("Nom");
            nom.appendChild(document.createTextNode(nodevect.get(i)));
            Element addr = document.createElement("Adresse");
            addr.appendChild(document.createTextNode(nodevect.get(i+1)));
            Element tel = document.createElement("Num_téléphone");
            tel.appendChild(document.createTextNode(nodevect.get(i+2)));
            racine.appendChild(nom);
            racine.appendChild(addr);
            racine.appendChild(tel);
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