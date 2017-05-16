package com.allgateways.webservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * Created by Seven on 30/04/2017.
 */

public class JsonUtils {
    private static ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).configure(DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS,false).configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT,true);

    public static <T> T jsonToObject(String json,Class<T> clazz,Class<?>... eType)
    {
        T	rspObj = null;

        try
        {
            Class<?>[] eTypes = eType;
            if(eTypes.length == 0) {
                rspObj = objectMapper.readValue(json,clazz);
            }else {
                JavaType javaType;
                if (eTypes.length == 1) {
                    javaType = objectMapper.getTypeFactory().constructParametricType(clazz, eTypes[0]);
                } else {
                    JavaType midType = objectMapper.getTypeFactory().constructParametricType(eTypes[eTypes.length - 2], eTypes[eTypes.length - 1]);
                    for (int i = eType.length - 3; i > 0; i--) {
                        midType = objectMapper.getTypeFactory().constructParametricType(eTypes[i], midType);
                    }
                    javaType = midType;
                    javaType = objectMapper.getTypeFactory().constructParametricType(clazz, midType);
                }

                rspObj = objectMapper.readValue(json, javaType);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
//            log.error("json to object failed,the json is " + json,e);
        }
        return  rspObj;
    }

    public static String objectToString(Object object){
        String json = "";
        try {
            json = objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json; //JSONObject.fromObject(object).toString();
    }

}
