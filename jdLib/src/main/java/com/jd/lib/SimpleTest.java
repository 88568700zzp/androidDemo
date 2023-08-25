package com.jd.lib;

import com.alibaba.fastjson.JSONObject;
import com.jd.lib.util.MantoCryptoUtils;


import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import netscape.javascript.JSObject;
import okhttp3.CacheControl;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SimpleTest {
    void requestJD() {
        try {
            OkHttpClient client = new OkHttpClient.Builder()
                    //.proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 8002)))
                    //.sslSocketFactory(createTrustAllSSLSocketFactory(), new TrustAllManager())
                    .build();


            RequestBody formBody = new FormBody.Builder().add("zzp","{\"flush\":\"0\"}").build();

            Request request = new Request.Builder()
                    .url("https://api.m.jd.com/client.action?functionId=isAppoint")
                    .post(formBody)
                    .addHeader("Cookie", "")
                    .addHeader("jdc-backup", "")
                    .addHeader("user-agent", "chrome")
                    .cacheControl(CacheControl.FORCE_NETWORK)
                    .build();



            try {
                Response response = client.newCall(request).execute();
                System.out.println(response.body().string());
            } catch (Exception e) {
                System.out.println(e);
            }
        }catch (Exception e){
            System.out.println(e);
        }
    }

    void request1(){
        OkHttpClient client = new OkHttpClient.Builder().build();

    }

    public static SSLSocketFactory createTrustAllSSLSocketFactory() {
        SSLSocketFactory sSLSocketFactory = null;
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllManager()}, new SecureRandom());
            sSLSocketFactory = sc.getSocketFactory();
        } catch (Exception ignored) {
        }
        return sSLSocketFactory;
    }

    private boolean isEmpty(String str){
        return str == null || "".equals(str);
    }

    public void doTest() throws UnsupportedEncodingException {
        String bodyStr = "{\"action\":\"gis\",\"appid\":\"e0a684c49d77e7749cdf7c2ab92e2d1a\",\"ifdetail\":\"0\",\"isdefaultipaddr\":\"1\",\"lat\":\"23.12705\",\"lng\":\"113.374735\"}";

        JSONObject jSONObject = new JSONObject();
        jSONObject.put("functionId","lbsapi");
        jSONObject.put("clientVersion","8.5.12");
        jSONObject.put("build","73078");
        jSONObject.put("client","android");
        jSONObject.put("brand","Xiaomi");
        jSONObject.put("d_model","M2012K11AC");
        jSONObject.put("osVersion","13");
        jSONObject.put("screen","2276*1080");
        jSONObject.put("partner","ks007");
        jSONObject.put("aid","1c6c6d648870f216");
        jSONObject.put("oaid","19baa93b587dae9b");
        jSONObject.put("openudid","1c6c6d648870f216");
        jSONObject.put("eid","eidAee068121bes7 wCbelsrT4u0qYr9SOKbYfsAnQQmkOkrXIZDJ1CwR4XaRWcUE8q63UExKC2mEnagZE8eeEb1GVnwv4L8gQMlc8A28H8Ful58dgBx");
        jSONObject.put("sdkVersion","33");
        jSONObject.put("lang","zh_CN");
        jSONObject.put("area","19_1601_3633_63243");
        jSONObject.put("networkType","wifi");
        jSONObject.put("wifiBssid","0138342a286d3695e7c8b2482e33acd6");
        jSONObject.put("uts","0f31TVRjBSvCR1jK0VPLOO1wBm1QK0Qz06haTzVgBHLIWZI/9cApvFvTRbPRb0xb7oMU8fvdKGQz+UkjvLemfkMCW9//uH5VNVeZW5DAbE44F+wXRiOv4hFy2VD08xSDNC1DSgDdmaE1IY6BWiyjhLIIHnoZbRiuSbKgpkzRIbww15iNUmcaCkQDctc02W+IH8zveeS0hf5P2NLj2NbSjw==");
        jSONObject.put("uuid","1c6c6d648870f216");
        jSONObject.put("st","1691375143768");
        jSONObject.put("sv","110");

        jSONObject.put("body",bodyStr);

        TreeMap treeMap = new TreeMap(new Comparator<String>() {
            public int compare(String str, String str2) {
                return str.compareTo(str2);
            }
        });
        if (jSONObject != null) {
            Set<Map.Entry<String, Object>> entrySet = jSONObject.entrySet();
            for(Map.Entry<String, Object> entry:entrySet){
                String key = entry.getKey();
                if (!isEmpty(jSONObject.getString(key))) {
                    treeMap.put(key, jSONObject.getString(key));
                }
            }

        }

        StringBuilder sb = new StringBuilder();
        for (Object value : treeMap.entrySet()) {
            sb.append((String) ((java.util.Map.Entry)value).getValue());
            sb.append("&");
        }
        if (sb.toString().endsWith("&")) {
            sb.setLength(sb.length() - 1);
        }


        StringBuilder sb2 = new StringBuilder();
        sb2.append("2AA64BD44C4381F31D9DA68EFE377874");
        sb2.append("7D6D16CC3D2BE89108F9DCFC9A855253");
        String cP = MantoCryptoUtils.m21640c(sb2.toString(), "616E746F");

        System.out.println("--->data:" + sb.toString());
        System.out.println("--->secretKey:" + cP);

        String a2 = MantoCryptoUtils.m3779a(sb.toString().getBytes("UTF-8"), cP.getBytes("UTF-8"));

        System.out.println("--->sign:" + a2.toLowerCase());
        System.out.println("--->length0:" + cP.length());
        System.out.println("--->length1:" + a2.toLowerCase().length());
        System.out.println("--->length2:" + "694026a659347423c21a061cc9e571da".length());

    }

    public static void request2(){
    }

    /**
     * 信任所有的证书
     */
    public static class TrustAllManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            //            //检查所有证书
//            try {
//                TrustManagerFactory factory = TrustManagerFactory.getInstance("X509");
//                factory.init((KeyStore) null);
//                for (TrustManager trustManager : factory.getTrustManagers()) {
//                    ((X509TrustManager) trustManager).checkServerTrusted(chain, authType);
//                }
//                //获取网络中的证书信息
//                X509Certificate certificate = chain[0];
//                // 证书拥有者
//                String subject = certificate.getSubjectDN().getName();
//                // 证书颁发者
//                String issuer = certificate.getIssuerDN().getName();
//
//                LogManager.getLogger().e("HHHTEST：-->", "证书拥有者：" + subject);
//                LogManager.getLogger().e("HHHTEST：-->", "证书颁发者：" + issuer);
//
//            } catch (Exception e) {
//                e.printStackTrace();
//                LogManager.getLogger().e("HHHTEST：-->", "Exception：" + e.getMessage());
//            }
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }


}
