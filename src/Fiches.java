import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;


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

/**
 * Fiches
 */
public class Fiches extends Xmlers {
    String[] keyword = {
        "BE",
        "TY",
        "AU",
        "DO",
        "SD",
        "VE",
        "DF",
        "PH",
        "NT",
        "RF",
        "AR",
        "FR"
    };
	@Override
	void read(String file) throws IOException, ParserConfigurationException{
		File input = new File(file);
        BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(input),"UTF-8"));    
        String line = bf.readLine();
        boolean supplement = false;
        while(line != null){
            boolean bool = false;
            String li = line.replaceAll(":|\n|\r","").strip();
            String[] lis = li.split("\t",2);
            if(lis != null){
                if(lis.length >1)
                    {
                        if(supplement & lis[1].contains("BE"))
                            supplement=false;
                        if(supplement)
                            lis[1] += " "+"RF";
                        nodevect.add(lis[0]+"/split/"+lis[1]);
                        if(lis[1].contains("RF"))
                            supplement=true;
                    }
                else if(lis[0]!="")
                    {
                        supplement=false;
                        nodevect.add(lis[0].strip());
                    }
            }
            line = bf.readLine();
        }
    }
	@Override
	void build(String output) throws ParserConfigurationException, TransformerException {
		final DocumentBuilderFactory factory =  DocumentBuilderFactory.newInstance();
        final DocumentBuilder builder = factory.newDocumentBuilder();
        final DOMImplementation domimp = builder.getDOMImplementation();
        final Document document = domimp.createDocument(null, "FICHES", null);
        final Element racine = document.getDocumentElement();
        int i=1 ;
        Element mother = null;
        Element fiche;
            for(int j=0;j<nodevect.size();j++){
                if(nodevect.get(j).length()==2){
                    Element language = document.createElement(nodevect.get(j));
                    mother=language;
                }else{
                    String[] splitted = nodevect.get(j).split("/split/")[1].split("\\s+");
                    if(splitted[0].equals("BE")){
                        fiche = document.createElement("FICHE");
                        fiche.setAttribute("id",""+i);
                        mother = fiche;
                        racine.appendChild(fiche);
                        i++;
                    }
                    String content = "";
                    for(int k=0;k<splitted.length;k++){
                        String s = splitted[k];
                        if(!s.equals(""))
                            content = s+ (k==splitted.length-1?" | ":" : ")+content;
                        }
                    Element kid = document.createElement(content.substring(0,2)) ;
                    System.out.println(content);
                    kid.appendChild(document.createTextNode(content));
                    if(mother != null)
                        mother.appendChild(kid);
                }
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