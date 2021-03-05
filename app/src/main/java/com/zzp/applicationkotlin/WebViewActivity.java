package com.zzp.applicationkotlin;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

/**
 * Created by samzhang on 2021/3/5.
 */
public class WebViewActivity extends AppCompatActivity implements View.OnClickListener{

    private final String TAG = "WebViewActivity";

    private WebView mWebView;
    private Switch mSwitch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        mWebView = findViewById(R.id.webview);
        mSwitch = findViewById(R.id.dialog_switch);

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient(){

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                loadUrl("file:///android_asset/404_error.html");
            }


        });
        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                Log.d(TAG,"onJsAlert message:"+ message);
                if(mSwitch.isChecked()){
                    AlertDialog alertDialog = new AlertDialog.Builder(WebViewActivity.this).setMessage(message).setNegativeButton("确定",null).create();
                    alertDialog.show();
                    result.confirm();
                    return true;
                }
                return super.onJsAlert(view, url, message, result);

            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
                Log.d(TAG,"onJsConfirm message:"+ message);
                if(mSwitch.isChecked()){
                    AlertDialog alertDialog = new AlertDialog.Builder(WebViewActivity.this).setMessage(message).setNegativeButton("取消",null).setPositiveButton("确定",null).create();
                    alertDialog.show();
                    result.confirm();
                    return true;
                }
                return super.onJsConfirm(view, url, message, result);
            }

            /**
             * 处理prompt弹出框
             */
            @Override
            public boolean onJsPrompt(WebView view, String url, String message,
                                      String defaultValue, JsPromptResult result) {
                Log.d(TAG,"onJsPrompt:"+message);
                result.confirm();
                return super.onJsPrompt(view, url, message, message, result);
            }



        });
        mWebView.addJavascriptInterface(new MyJavascriptInterface(),"injectedObject");

        findViewById(R.id.btn_load_net).setOnClickListener(this);
        findViewById(R.id.btn_load_js).setOnClickListener(this);
        findViewById(R.id.doSend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = findViewById(R.id.edit_text);
                if(TextUtils.isEmpty(editText.getText())){
                    mWebView.loadUrl("javascript:javacalljs()");
                }else{
                    mWebView.loadUrl("javascript:javacalljswithargs('" + editText.getText() +"')");
                }
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(mWebView.canGoBack()){
                mWebView.goBack();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void loadUrl(String url){
        mWebView.loadUrl(url);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_load_js){
            loadUrl("file:///android_asset/java_js.html");
        }else if(v.getId() == R.id.btn_load_net){
            loadUrl("http://www.baidu.com");
        }
    }

    public class MyJavascriptInterface {

        /**
         * 前端代码嵌入js：
         * imageClick 名应和js函数方法名一致
         *
         * @param src 图片的链接
         */
        @JavascriptInterface
        public void imageClick(String src) {
            Log.e("imageClick", "----点击了图片");
            Log.e("src", src);
        }

        /**
         * 前端代码嵌入js
         * 遍历<li>节点
         *
         * @param type    <li>节点下type属性的值
         * @param item_pk item_pk属性的值
         */
        @JavascriptInterface
        public void textClick(String type, String item_pk) {
            if (!TextUtils.isEmpty(type) && !TextUtils.isEmpty(item_pk)) {
                Log.e("textClick", "----点击了文字");
                Log.e("type", type);
                Log.e("item_pk", item_pk);
            }
        }

        /**
         * 网页使用的js，方法无参数
         */
        @JavascriptInterface
        public void startFunction() {
            Toast.makeText(getApplicationContext(),"startFunction",Toast.LENGTH_LONG).show();
            Log.e("startFunction", "----无参");
        }

        /**
         * 网页使用的js，方法有参数，且参数名为data
         *
         * @param data 网页js里的参数名
         */
        @JavascriptInterface
        public void startFunction(String data) {
            Toast.makeText(getApplicationContext(),"startFunction:" + data,Toast.LENGTH_LONG).show();
            Log.e("startFunction", "----有参" + data);
        }
    }
}
