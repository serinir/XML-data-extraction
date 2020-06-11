import java.util.Vector;
// import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
// import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.DocumentBuilder;

public class Mx extends Xmlers {
    Vector<String> nodevect = new Vector<String>();
    String fichier;
    @Override
    public void read(String file) throws ParserConfigurationException, SAXException, IOException
    {
        if(file.contains("M457.xml")){
            this.fichier = "M457.xml";
        }else{
            this.fichier ="M674.xml";
        }
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
        int i;
        for(i=0;i<para.getLength();i++){
            Element el = (Element) para.item(i);
            if(el.hasChildNodes()){
                final NodeList childes = el.getChildNodes();
                int j;
                for(j=0;j<childes.getLength();j++){
                    if(childes.item(j).getNodeName().equals("#text")){
                        this.nodevect.add(childes.item(j).getNodeValue());
                    }
                }
            }
        }
    }
    @Override
    public void build(String output) throws ParserConfigurationException, TransformerException{
        final DocumentBuilderFactory factory =  DocumentBuilderFactory.newInstance();
        
        final DocumentBuilder builder = factory.newDocumentBuilder();
        final DOMImplementation domimp = builder.getDOMImplementation();
        final Document document = domimp.createDocument(null, "TEI_S", null);
        final Element racine = document.getDocumentElement();
        final Element sous_racine = document.createElement(this.fichier);
        for(int i=0;i<this.nodevect.size();i++){
            String formated = format(this.nodevect.get(i));
            Text content = document.createTextNode(formated);
            Element text = document.createElement("text");
            text.appendChild(content);
            sous_racine.appendChild(text);
        }
        racine.appendChild(sous_racine);

        final TransformerFactory tfactory = TransformerFactory.newInstance();
        final Transformer transformer = tfactory.newTransformer();
        final DOMSource source = new DOMSource(document);
        final StreamResult sortie = new StreamResult(new File(output));
        transformer.setOutputProperty(OutputKeys.VERSION,"1.0");
        transformer.setOutputProperty(OutputKeys.ENCODING,"UTF-8");
        transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,"dom.dtd");
        transformer.setOutputProperty(OutputKeys.STANDALONE,"no");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        transformer.transform(source, sortie);
    }
    private String format(String input){
        return input.replaceAll("\\[#text: |\\]|\\n|\\r","");
    }
    private InputSource encode(String file) throws FileNotFoundException, UnsupportedEncodingException{
        FileInputStream f = new FileInputStream(file);
        InputStreamReader inputReader = new InputStreamReader(f,"cp1252");
        InputSource inputSource = new InputSource(inputReader);
        inputSource.setEncoding("UTF-8");
        return inputSource;
    }
    public void showvect(){
        for(int i =0;i<this.nodevect.size();i++){
            System.out.println(nodevect.get(i));
        }
    }
}