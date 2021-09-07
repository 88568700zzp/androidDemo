package com.zzp.applicationkotlin.http;

import android.util.Log;

import java.util.TreeMap;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by samzhang on 2021/9/2.
 */
public class HttpEngine {

    private static final String TAG = "HttpHandler";

    private static HttpEngine mInstance;

    public static HttpEngine getInstance() {
        if (mInstance == null) {
            synchronized (BaseProtocal.class) {
                if (mInstance == null) {
                    mInstance = new HttpEngine();
                }
            }
        }
        return mInstance;
    }

    private OkHttpClient getClient() {
        return Holder.okHttpClient;
    }

    public static class Holder {
        static OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(
                chain -> {
                    Request userRequest = chain.request();

                    Log.d(TAG,"url:" + userRequest.toString());

                    Response response = chain.proceed(userRequest);

                    Log.d(TAG,"response:" + response.toString());
                    return response;
                }
        ).build();
    }

    public void requestGet(String url, TreeMap<String,String> param, Callback responseCallback){
        Request request = new Request.Builder().url(url + "?" + toGetParam(param)).get().build();
        getClient().newCall(request).enqueue(responseCallback);
    }

    private String toGetParam(TreeMap<String, String> paraMap) {
        StringBuilder sb = new StringBuilder();
        for (String key : paraMap.keySet()) {
            sb.append(key).append("=").append(paraMap.get(key)).append("&");
        }
        return sb.substring(0, sb.length() - 1);
    }
}
