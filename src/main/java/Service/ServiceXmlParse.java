package Service;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;


public class ServiceXmlParse {
    public Document parse() throws DocumentException {
        FileInputStream in = null;
        try {
            in  = new FileInputStream(new File(System.getProperty("user.dir")+"/service.xml"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if(in == null) return null;
        SAXReader reader = new SAXReader();
        Document document = (Document) reader.read(in);
        return document;
    }

}
