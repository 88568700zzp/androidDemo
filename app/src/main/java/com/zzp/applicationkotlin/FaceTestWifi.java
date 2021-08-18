package com.zzp.applicationkotlin;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.facebook.network.connectionclass.*;
import com.zzp.applicationkotlin.view.TestSpeedView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;


public class FaceTestWifi extends Activity {

    private static final String TAG = "ConnectionClass-Sample";

    private ConnectionClassManager mConnectionClassManager;
    private DeviceBandwidthSampler mDeviceBandwidthSampler;
    private ConnectionChangedListener mListener;
    private TextView mTextView;
    private View mRunningBar;
    private TestSpeedView mTestSpeedView;

    private String mURL = "http://n.sinaimg.cn/top/10_img/upload/411dc6fe/781/w950h631/20210630/62ba-krwipas1712174.jpg";
    private int mTries = 0;
    private ConnectionQuality mConnectionClass = ConnectionQuality.UNKNOWN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_test_wifi);
        mConnectionClassManager = ConnectionClassManager.getInstance();
        mDeviceBandwidthSampler = DeviceBandwidthSampler.getInstance();
        findViewById(R.id.test_btn).setOnClickListener(testButtonClicked);
        mTextView = (TextView)findViewById(R.id.connection_class);
        mTestSpeedView = findViewById(R.id.testSpeedView);
        mTextView.setText(mConnectionClassManager.getCurrentBandwidthQuality().toString());
        mRunningBar = findViewById(R.id.runningBar);
        mRunningBar.setVisibility(View.GONE);
        mListener = new ConnectionChangedListener();

        Log.d("zzp12","operatorName:" + getOperatorName());

        mTestSpeedView.startProgress();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i = 0;i < 5;i++) {
                    try {
                        URL url = new URL(mURL);
                        //得到connection对象。
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        //设置请求方式
                        connection.setRequestMethod("GET");
                        connection.setUseCaches(false);

                        Log.d("zzp12", "connect");
                        long time = System.currentTimeMillis();
                        //连接
                        connection.connect();
                        //得到响应码
                        int responseCode = connection.getResponseCode();
                        Log.d("zzp12", "time:" + (System.currentTimeMillis() - time) + " responseCode:" + responseCode + " contentLength:" + connection.getContentLength() / 1024);
                        if (responseCode == HttpURLConnection.HTTP_OK) {
                            //得到响应流
                            InputStream inputStream = connection.getInputStream();
                            //将响应流转换成字符串
                            String result = is2String(inputStream);//将流转换为字符串。
                            Log.d("zzp12", "result=============" + result);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public String getOperatorName() {
        /*
         * getSimOperatorName()就可以直接获取到运营商的名字
         * 也可以使用IMSI获取，getSimOperator()，然后根据返回值判断，例如"46000"为移动
         * IMSI相关链接：http://baike.baidu.com/item/imsi
         */
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        // getSimOperatorName就可以直接获取到运营商的名字
        return telephonyManager.getSimOperatorName();
    }


    public String is2String(InputStream is) throws IOException {
        //连接后，创建一个输入流来读取response
        BufferedReader bufferedReader = new BufferedReader(new
                InputStreamReader(is, "utf-8"));
        String line = "";
        StringBuilder stringBuilder = new StringBuilder();
        String response = "";
        //每次读取一行，若非空则添加至 stringBuilder
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }
        //读取所有的数据后，赋值给 response
        response = stringBuilder.toString().trim();
        return response;
    }



    @Override
    protected void onPause() {
        super.onPause();
        mConnectionClassManager.remove(mListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mConnectionClassManager.register(mListener);
    }

    /**
     * Listener to update the UI upon connectionclass change.
     */
    private class ConnectionChangedListener
            implements ConnectionClassManager.ConnectionClassStateChangeListener {

        @Override
        public void onBandwidthStateChange(ConnectionQuality bandwidthState) {
            mConnectionClass = bandwidthState;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mTextView.setText(mConnectionClass.toString());
                }
            });
        }
    }

    private final View.OnClickListener testButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new DownloadImage().execute(mURL);
        }
    };

    /**
     * AsyncTask for handling downloading and making calls to the timer.
     */
    private class DownloadImage extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            mDeviceBandwidthSampler.startSampling();
            mRunningBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(String... url) {
            String imageURL = url[0];
            try {
                // Open a stream to download the image from our URL.
                URLConnection connection = new URL(imageURL).openConnection();
                connection.setUseCaches(false);
                connection.connect();
                InputStream input = connection.getInputStream();
                try {
                    byte[] buffer = new byte[1024];

                    // Do some busy waiting while the stream is open.
                    while (input.read(buffer) != -1) {
                    }
                } finally {
                    input.close();
                }
            } catch (IOException e) {
                Log.e(TAG, "Error while downloading image.");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            mDeviceBandwidthSampler.stopSampling();
            // Retry for up to 10 times until we find a ConnectionClass.
            if (mConnectionClass == ConnectionQuality.UNKNOWN && mTries < 10) {
                mTries++;
                new DownloadImage().execute(mURL);
            }
            if (!mDeviceBandwidthSampler.isSampling()) {
                mRunningBar.setVisibility(View.GONE);
            }
        }
    }
}