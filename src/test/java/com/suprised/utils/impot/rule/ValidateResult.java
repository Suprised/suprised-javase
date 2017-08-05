package com.suprised.utils.impot.rule;

/**
 * 验证的结果
 */
public class ValidateResult {

    private String location ;
    private String message ;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    @Override
    public String toString() {
        return String.format("错误位置:%s, 错误信息：%s", location, message);
    }
}
