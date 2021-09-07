package com.zzp.applicationkotlin.http;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.TreeMap;

import okhttp3.Callback;

/**
 * Created by samzhang on 2021/9/2.
 */
public class BaseProtocal {

    private static final String TAG = "HttpUtil";

    public static final String BASE_URL = "https://openapi.dataoke.com/api/";

    public static final String HOT_WORD_URL = BASE_URL + "/etc/search/list-hot-words";//淘口令解析
    public static final String SUPER_CATEGORY_URL = BASE_URL + "/category/get-super-category";//超级分类
    public static final String PARSE_TAOKOULING_URL = BASE_URL + "/tb-service/parse-taokouling";//淘口令解析
    public static final String PRIVILEGE_LINK_URL = BASE_URL + "/tb-service/get-privilege-link";//高效转链
    public static final String GOODS_LIST_URL = BASE_URL + "/goods/get-goods-list";//商品列表




    public static final String APP_SECRET = "db42b8bd2a04188d528629ca39f69365";
    public static final String APP_KEY = "61307d5a321c0";
    public static final String VERSION_CODE = "1.0.0";
    public static final String PID = "mm_2068710197_2452350055_111642550327";

    private static BaseProtocal mInstance;

    public static BaseProtocal getInstance() {
        if (mInstance == null) {
            synchronized (BaseProtocal.class) {
                if (mInstance == null) {
                    mInstance = new BaseProtocal();
                }
            }
        }
        return mInstance;
    }

    public void requestHotWords(Callback responseCallback) {
        String url = HOT_WORD_URL;
        HttpEngine.getInstance().requestGet(url,createBaseParam(),responseCallback);
    }


    public void requestSuperCategory(Callback responseCallback) {
        String url = SUPER_CATEGORY_URL;
        HttpEngine.getInstance().requestGet(url,createBaseParam(),responseCallback);
    }

    public void requestTaokouling(String content,Callback responseCallback) {
        String url = PARSE_TAOKOULING_URL;
        TreeMap<String,String> paramMap = createBaseParam();
        paramMap.put("content",content);
        HttpEngine.getInstance().requestGet(url,paramMap,responseCallback);
    }

    public void requestPrivilegeLink(String goodsId,Callback responseCallback) {
        String url = PRIVILEGE_LINK_URL;
        TreeMap<String,String> paramMap = createBaseParam();
        paramMap.put("goodsId",goodsId);
        paramMap.put("version", "1.3.1");
        paramMap.put("pid",PID);
        HttpEngine.getInstance().requestGet(url,paramMap,responseCallback);
    }

    public void requestGoodsList(int page,Callback responseCallback) {
        String url = GOODS_LIST_URL;
        TreeMap<String,String> paramMap = createBaseParam();
        paramMap.put("pageId",String.valueOf(page));
        paramMap.put("version", "1.2.4");
        HttpEngine.getInstance().requestGet(url,paramMap,responseCallback);
    }

    private TreeMap createBaseParam(){
        String nonce = "597632";
        String timer = String.valueOf(System.currentTimeMillis());
        TreeMap<String, String> paraMap = new TreeMap<>();
        paraMap.put("version", VERSION_CODE);
        paraMap.put("appKey", APP_KEY);
        paraMap.put("nonce", nonce);
        paraMap.put("timer", timer);
        String sign = createSign(nonce, timer);
        paraMap.put("signRan", sign);
        return paraMap;
    }


    private String createSign(String nonce, String timer) {
        String signStr = "appKey=" + APP_KEY + "&timer=" + timer + "&nonce=" + nonce + "&key=" + APP_SECRET;
        return toMd5(signStr).toUpperCase();
    }

    public String toMd5(String content) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(content.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("NoSuchAlgorithmException", e);
        }
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) {
                hex.append(0);
            }
            hex.append(Integer.toHexString(b & 0xff));
        }
        return hex.toString();
    }

}
