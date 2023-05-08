package com.jd.lib;

import com.jd.lib.util.MantoCryptoUtils;

import org.json.JSONObject;

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
import java.util.TreeMap;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import netscape.javascript.JSObject;
import okhttp3.CacheControl;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SimpleTest {
    void request() {
        try {
            OkHttpClient client = new OkHttpClient.Builder()
                    .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 8002)))
                    .sslSocketFactory(createTrustAllSSLSocketFactory(), new TrustAllManager())
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
        String bodyStr = "{\"abTest800\":true,\"avoidLive\":false,\"brand\":\"Redmi\",\"cityId\":1601,\"cpsNoTuan\":null,\"darkModelEnum\":3,\"districtId\":3633,\"eventId\":\"Searchlist_Productid\",\"fromType\":0,\"isDesCbc\":true,\"latitude\":\"23.133399\",\"lego\":true,\"longitude\":\"113.380193\",\"model\":\"M2012K11AC\",\"ocrFlag\":false,\"pluginVersion\":100060,\"plusClickCount\":0,\"plusLandedFatigue\":0,\"provinceId\":\"19\",\"skuId\":\"100012043978\",\"source_type\":\"search\",\"source_value\":\"飞天茅台53度\",\"townId\":63252,\"uAddrId\":\"0\",\"utmMedium\":null}";

        JSONObject jSONObject = new JSONObject();
        jSONObject.put("st","1667388635323");
        jSONObject.put("functionId","wareBusiness");
        jSONObject.put("build","89053");
        jSONObject.put("clientVersion","10.0.8");
        //jSONObject.put("appid","MessageCenter");
        jSONObject.put("client","android");
        jSONObject.put("brand","Xiaomi");
        jSONObject.put("d_model","M2012K11AC");
        jSONObject.put("osVersion","12");
        jSONObject.put("screen","2276*1080");
        jSONObject.put("partner","jdtopc");
        jSONObject.put("oaid","19baa93b587dae9b");
        jSONObject.put("eid","eidAebe68121dds69etFOUb+QCOAeHyK9MoHp/XrRr1jnI+ecPBrrbGPGxwv8oPZxtojq0hMm2bzK1cv3+defLNUJlGDLGrjEl0xS0Fi7YR5f3DaV45D");
        jSONObject.put("sdkVersion","31");
        jSONObject.put("lang","zh_CN");
        jSONObject.put("uuid","1c6c6d648870f216");
        jSONObject.put("aid","1c6c6d648870f216");
        jSONObject.put("openudid","1c6c6d648870f216");
        jSONObject.put("area","19_1601_3633_63243");
        jSONObject.put("networkType","wifi");
        jSONObject.put("wifiBssid","fbf96989d5f0c394f66ff4b28f86b490");
        jSONObject.put("uts","0f31TVRjBSt+bgdu7jx7XmiJYNLbjakrVhAWtTn2yx3FJLvWxisVXxt2ZJMQZrlcJ0nlSK0D6YSTmqsn0XNkNQPrPbofHsW4QuAZwBH2omi0e5iEANXVg8bWCcqy63orVDyze+JOPjTdbDvpww8QiI++e2NXU5xG7Qx9jhV3dkTc00yxzFGKMRlGbQVjNJUQbJpfmtFZ0oWHN0q62GSn2g==");
        jSONObject.put("uemps","0-2");
        jSONObject.put("harmonyOs","0");
        jSONObject.put("scval","100012043978");
        jSONObject.put("sv","102");

        jSONObject.put("body",bodyStr);

        TreeMap treeMap = new TreeMap(new Comparator<String>() {
            public int compare(String str, String str2) {
                return str.compareTo(str2);
            }
        });
        if (jSONObject != null) {
            Iterator keys = jSONObject.keys();
            while (keys.hasNext()) {
                String str3 = (String) keys.next();
                if (!isEmpty(jSONObject.optString(str3))) {
                    treeMap.put(str3, jSONObject.optString(str3));
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
        String cP = MantoCryptoUtils.m3783c(sb2.toString(), "616E746F");

        System.out.println("--->data:" + sb.toString());

        System.out.println("--->secretKey:" + cP);

        String a2 = MantoCryptoUtils.m3779a(sb.toString().getBytes("UTF-8"), cP.getBytes("UTF-8"));

        System.out.println("--->result:" + a2.toLowerCase());
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
