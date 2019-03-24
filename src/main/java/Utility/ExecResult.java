package Utility;

public class ExecResult {
    public int resultCode;
    public String resultStr;

    public ExecResult(int resultCode, String resultStr) {
        this.resultCode = resultCode;
        this.resultStr = resultStr;
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

    public boolean isSuccess(){
        if(resultCode==0){
            return  true;
        }else{
            return false;
        }
    }
}
