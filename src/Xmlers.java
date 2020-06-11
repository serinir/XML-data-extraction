import java.io.IOException;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

abstract class Xmlers {
    Vector<String> nodevect = new Vector<String>();
    abstract void read(String file)throws IOException, ParserConfigurationException, SAXException ;
    abstract void build(String file)throws ParserConfigurationException, TransformerException ;
    public void showvect(){
        for(int i =0;i<nodevect.size();i++)
            System.out.println(nodevect.get(i));
    }
}