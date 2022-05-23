package com.lingrui.crm.common.domain;

/**
 * @ Description
 * 封装所有controller的方法中返回json字符串使用的实体类
 * @ Author Carmen
 * @ Date 2022-05-20 21:00
 * @ Version 1.0
 */
public class ObjectForReturn {
    private String code;//处理成功（1）或者失败（0）
    private String message;//提示信息
    private Object returnData;//如果有的话其他数据

    public ObjectForReturn() {
    }

    public ObjectForReturn(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getReturnData() {
        return returnData;
    }

    public void setReturnData(Object returnData) {
        this.returnData = returnData;
    }
}
