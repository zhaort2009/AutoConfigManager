package Service;

import Utility.Constant;
import Utility.Utility;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class ServiceXmlParse {

    private static ServiceXmlParse instance = new ServiceXmlParse();

    private ServiceXmlParse(){}

    public  Document parse() throws DocumentException {
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


    public  List<ServiceInstance> parseXml(){
        List<ServiceInstance> list = new ArrayList<ServiceInstance>();
        try {
            Element root = parse().getRootElement();
            for (Iterator<Element> it = root.elementIterator(); it.hasNext();) {
                Element serviceElement = it.next();
                String name = serviceElement.element("NAME").getTextTrim();
                String startPath =serviceElement.element("STARTPATH").getTextTrim();
                String checkPath =serviceElement.element("CHECKPATH").getTextTrim();
                String stopPath =serviceElement.element("STOPPATH").getTextTrim();
                String configPath =serviceElement.element("CONFIGPATH").getTextTrim();
                String configTypeStr =serviceElement.element("CONFIGTYPE").getTextTrim();
                boolean isValidate = isValidateXml(name,startPath,checkPath,stopPath,configPath,configTypeStr);
                if(isValidate){
                    ServiceInstance serviceInstance = ServiceInstance.builder()
                                                      .name(name)
                                                      .startPath(startPath)
                                                      .checkPath(checkPath)
                                                      .stopPath(stopPath)
                                                      .configPath(configPath)
                                                      .configType(Utility.Constant.ConfigType.getType(configTypeStr))
                                                      .build();



                    list.add(serviceInstance);
                }else{

                }
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  list;
    }


    public static ServiceXmlParse getInstance(){
        return instance;
    }
    public boolean isValidateXml(String name, String startPath,String checkPath,String stopPath,String configPath,String configTypeStr){
        boolean isValidate = true;
        isValidate = isValidate && ((name==null||name.length()==0)?false:true);
        isValidate = isValidate && Utility.isFileExits(startPath);
        isValidate = isValidate && Utility.isFileExits(checkPath);
        isValidate = isValidate && Utility.isFileExits(stopPath);
        isValidate = isValidate && Utility.isFileExits(configPath);
        isValidate = isValidate && Constant.ConfigType.getType(configTypeStr)==null?false:true;
        return  isValidate;
    }

}
