package Utility;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.util.Properties;

public class ConfigProperties {
	private Properties properties = new Properties();
	private String path;
	private static final ConfigProperties instance = new ConfigProperties();
	private ConfigProperties(){
		String runFolder = System.getProperty("user.dir");
		path = runFolder + "/config.properties";
		readProperties(path);
	}
	
	private void readProperties(String path){
		try {
			properties.load(new FileReader(path));
		} catch (Exception e) {
			//put some key and values that must be used
		}
	}
	
	public void put(String key,String value){
		properties.put(key, value);
		storeProperties();
	}
	
	public String get(String key){
		if(properties.containsKey(key))
			return properties.get(key).toString();
		else
			return "";
	}
	
	private synchronized void storeProperties(){
		try {
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path)));
			properties.store(bw, "");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static ConfigProperties getInstance(){
		return instance;
	}
	
	
}
