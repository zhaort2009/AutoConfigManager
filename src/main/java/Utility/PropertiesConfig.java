package Utility;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.util.Properties;
import java.util.Set;

public class PropertiesConfig implements Iupdate, Serializable {
    private String filePath;
    public PropertiesConfig(String configPath){
        filePath = configPath;
    }
    public void Update(String json) {
        if(!JSON.isValid(json)||json.equals("[]")){
            return;
        }
        JSONArray array = JSON.parseArray(json);
        if(array.size()==0){
            return;
        }
        try {
            Properties pps = new Properties();
            InputStream in = new FileInputStream(filePath) ;
            pps.load(in);
            OutputStream out = new FileOutputStream(filePath);

            for (int i = 0; i < array.size(); i++) {
                JSONObject obj = array.getJSONObject(i);
                Set<String> keys = obj.keySet();
                for (String key:keys) {
                    pps.setProperty(key.toString(), obj.get(key).toString());
                }
            }
            pps.store(out, "update");
        }catch (Exception ex){

        }
    }
}

