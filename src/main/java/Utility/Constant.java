package Utility;

public class Constant {
    public enum ConfigType{
        PROPERTIES("properties"),INI("ini"),XML("xml");
        private String typeStr;
        private ConfigType(String str){
            typeStr=str;
        }

        public String getTypeStr() {
            return typeStr;
        }
    }
}
