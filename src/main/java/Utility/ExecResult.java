package Utility;

import com.alibaba.fastjson.JSON;

public class ExecResult {
    private int resultCode ;
    private String resultStr;
    private String errorStr;

    public ExecResult() {
    }

    public ExecResult(int resultCode, String resultStr,String errorStr) {
        this.resultCode = resultCode;
        this.resultStr = resultStr;
        this.errorStr = errorStr;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultStr() {
        return resultStr;
    }

    public void setResultStr(String resultStr) {
        this.resultStr = resultStr;
    }

    public String getErrorStr() {
        return errorStr;
    }

    public void setErrorStr(String errorStr) {
        this.errorStr = errorStr;
    }

    public boolean isSuccess(){
        if(resultCode==0){
            return  true;
        }else{
            return false;
        }
    }

    public boolean isRunning() {
        if(this.resultCode==0 && this.resultStr.length()==0){return true;}
        else{return false;}

    }

    public String toJson(){
        return JSON.toJSONString(this);
    }
}
