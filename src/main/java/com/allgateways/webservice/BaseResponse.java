package com.allgateways.webservice;

import java.io.Serializable;

/**
 * Created by Seven on 9/6/16.
 */
public class BaseResponse<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private String resCode;
    private String resDesc;
    private T data;


    public String getResCode() {
        return resCode;
    }

    public void setResCode(String resCode) {
        this.resCode = resCode;
    }

    public String getResDesc() {
        return resDesc;
    }

    public void setResDesc(String resDesc) {
        this.resDesc = resDesc;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
