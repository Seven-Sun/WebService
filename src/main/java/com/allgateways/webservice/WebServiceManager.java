package com.allgateways.webservice;

import android.os.Looper;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.InputStream;
import java.util.Map;
import java.util.UUID;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Seven on 9/6/16.
 */
public class WebServiceManager {

    private static String BASE_URL = "";
    private static boolean debug = false;


    public static void init(String baseUrl,boolean isDebug){
        BASE_URL = baseUrl;
        debug = isDebug;
    }

    private static WebServiceManager instance;

    static private AsyncHttpClient client = new AsyncHttpClient();


    synchronized static public WebServiceManager getInstance(){
        if (instance == null){
            instance = new WebServiceManager();
        }
        return instance;
    }

    private WebServiceManager() {
        client.setTimeout(30000);
    }

    public <T> void asyncUpload(BaseRequest request, String fileKey, InputStream inputStream, final Class<T> responseCls, final HttpClientListener ls){
        Map<String, String> map = request.toMap();
        RequestParams params = new RequestParams(map);
        params.put(fileKey,inputStream, UUID.randomUUID().toString()+".jpg", "image/jpeg");
        client.post(getAbsoluteUrl(request.getMethod()), params, new AsyncHttpResponseHandler(Looper.getMainLooper()) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {
                    String responseString = new String(responseBody);
                    if (debug){
                        Log.v("WebService", responseString);
                    }
                    BaseResponse responseObject = JsonUtils.jsonToObject(responseString,BaseResponse.class,responseCls);//jsonToObject(responseString,BaseResponse.class,responseCls);
                    ls.onHttpResponse(responseObject,responseObject != null && responseObject.getResCode().equals("0"));
                }else{
                    ls.onHttpResponse(null,false);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                ls.onHttpResponse(null,false);
            }
        });
    }



    public <T> void asyncRequest(final BaseRequest request,final Class<T> responseCls,final HttpClientListener ls){
        Map<String, String> map = request.toMap();
        RequestParams params = new RequestParams(map);
        if (debug){
            Log.v("WebService","request:"+JsonUtils.objectToString(request));
        }
        if (request.getRequestType().equals(BaseRequest.GET)){
            client.get(getAbsoluteUrl(request.getMethod()), params, new AsyncHttpResponseHandler(Looper.getMainLooper()) {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    if (statusCode == 200){
                        String responseString = new String(responseBody);
                        if (debug){
                            Log.v("WebService",responseString);
                        }
                        BaseResponse responseObject = JsonUtils.jsonToObject(responseString,BaseResponse.class,responseCls);//jsonToObject(responseString,BaseResponse.class,responseCls);
                        ls.onHttpResponse(responseObject,responseObject != null && responseObject.getResCode().equals("0"));

                    }else {
                        ls.onHttpResponse(null,false);
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    ls.onHttpResponse(null,false);
                }
            });
        }else if (request.getRequestType().equals(BaseRequest.POST)){
            client.post(getAbsoluteUrl(request.getMethod()), params, new AsyncHttpResponseHandler(Looper.getMainLooper()) {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    if (statusCode == 200){
                        String responseString = new String(responseBody);
                        if (debug){
                            Log.v("WebService",responseString);
                        }

                        BaseResponse responseObject = JsonUtils.jsonToObject(responseString,BaseResponse.class,responseCls); //jsonToObject(responseString,BaseResponse.class,responseCls);
                        ls.onHttpResponse(responseObject,responseObject != null && responseObject.getResCode().equals("0"));

                    }else {
                        ls.onHttpResponse(null,false);
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    if (responseBody != null){
                        String responseString = new String(responseBody);
                        if (debug){
                            Log.e("WebService",responseString);
                        }
                    }
                    ls.onHttpResponse(null,false);
                }
            });
        }

    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + "/" + relativeUrl;
    }


    public interface HttpClientListener<T> {
        void onHttpResponse(BaseResponse<T> response, boolean status);
    }

}
