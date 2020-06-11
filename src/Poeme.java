import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Set;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Poeme extends Xmlers {
    // <String,String> nodevect = new <String,String>();
    // Vector<String> nodevect = new Vector<String>();
    @Override
    public void read(String file) throws IOException{
        File input = new File(file);
        BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(input),"UTF-8"));    
        String line = bf.readLine();
        
        boolean first = true;
        while(true){
            if(line.equals(""))
                {nodevect.add("/estrofa");
                nodevect.add("estrofa");
                while(line != null && line.equals(""))
                    line = bf.readLine();}
            if (line==null || line.equals("\n"))
                break;
            if(first){
                nodevect.add(line);
                nodevect.add("estrofa");
                first=false;
                line = bf.readLine();
                while(line != null && line.equals(""))
                    line = bf.readLine();
            }else
                {nodevect.add(line);
            line = bf.readLine();}
        }
        System.out.println(nodevect.size());
    }
    @Override
    public void build(String output) throws ParserConfigurationException, TransformerException{
        final DocumentBuilderFactory factory =  DocumentBuilderFactory.newInstance();
        final DocumentBuilder builder = factory.newDocumentBuilder();
        final DOMImplementation domimp = builder.getDOMImplementation();
        final Document document = domimp.createDocument(null, "poema", null);
        final Element racine = document.getDocumentElement();
        final Element sous_racine1 = document.createElement("titulo");
        sous_racine1.appendChild(document.createTextNode(this.nodevect.get(0)));
        racine.appendChild(sous_racine1);
        Element sous_racine2=null;
        for(int i = 1;i<nodevect.size()-1;i++){
            if(nodevect.get(i).equals("estrofa")){
                sous_racine2 = document.createElement("estrofa");
                racine.appendChild(sous_racine2);
            }else if(nodevect.get(i).equals("/estrofa")){
                continue;
            }else if(sous_racine2 != null){
                Element verso = document.createElement("verso");
                verso.appendChild(document.createTextNode(this.nodevect.get(i)));
                sous_racine2.appendChild(verso);
            }
        }
        final TransformerFactory tfactory = TransformerFactory.newInstance();
        final Transformer transformer = tfactory.newTransformer();
        final DOMSource source = new DOMSource(document);
        final StreamResult sortie = new StreamResult(new File(output));
        transformer.setOutputProperty(OutputKeys.VERSION,"1.0");
        transformer.setOutputProperty(OutputKeys.ENCODING,"UTF-8");
        transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,"neruda.dtd");
        transformer.setOutputProperty(OutputKeys.STANDALONE,"no");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        transformer.transform(source, sortie);
        }

    public void showvect(){
        for (int i=0;i<nodevect.size();i++){
            System.out.println(nodevect.get(i));
        }
    }
}