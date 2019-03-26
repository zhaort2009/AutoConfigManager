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

        public static ConfigType getType(String typeStr){
            switch (typeStr){
                case "properties":
                    return PROPERTIES;
                case "ini":
                    return INI;
                case "xml":
                    return XML;
                default:
            }
            return null;
        }
    }

    public static class ZNodeName{
        public static String ConfigUpdateZnode = "configUpdate";
        public static String ConfigFileZnode = "configFile";
        public static String CmdZnode = "cmd";
        public static String StatusZnode = "status";
        public static String CmdResultZnode = "cmdResult";
    }


    public static class MsgJsonKey{
        public static String PATHKEY = "path";
        public static String CMDKEY = "cmd";
        public static String CONFIGKEY = "config";
    }

}
