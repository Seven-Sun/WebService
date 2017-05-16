package com.allgateways.webservice;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Seven on 9/6/16.
 */
public class BaseRequest {

    private static final long serialVersionUID = 1L;
    public static final String POST = "post";
    public static final String GET = "get";
    private String method;
    private String requestType = POST;

    public BaseRequest(){

    }

    public String getMethod() {
        return method;
    }
    protected void setMethod(String method) {
        this.method = method;
    }
    public String getRequestType() {
        return requestType;
    }
    public void setRequestType(String requestType) {
        if (!requestType.equals(POST) && !requestType.equals(GET)) {
            System.out.println("Request type unrecognize！");
        }
        this.requestType = requestType;
    }
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        Collection<Field> fields = new ArrayList<>();
        Class<?> cls = this.getClass();
        while (cls != BaseRequest.class){
            fields.addAll(Arrays.asList(cls.getDeclaredFields()));
            cls = cls.getSuperclass();
        }
        for (Field field : fields) {
            field.setAccessible(true);
            String proName = field.getName();

            Object proValue;
            try {
                proValue = field.get(this);
                if (proValue != null && !proValue.toString().equals("")) {
                    map.put(proName, proValue.toString());
                }
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return map;
    }

}
