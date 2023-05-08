package com.zzp.applicationkotlin;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ClipboardManager.OnPrimaryClipChangedListener;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.Filter;
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

    private final String TAG = "WebViewActivity_";

    private WebView mWebView;
    private Switch mSwitch;
    private EditText mEditText;

    private ClipboardManager mClipboardManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        mWebView = findViewById(R.id.webview);
        mSwitch = findViewById(R.id.dialog_switch);
        mEditText = findViewById(R.id.edit_text);

        mEditText.setRawInputType(Configuration.KEYBOARD_QWERTY);

        InputFilter[] filters = new InputFilter[1];
        filters[0] = new InputFilter() {

            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                for(int i = 0;i < source.length();i++){
                    char ch = source.charAt(i);
                    Log.d("zzp1234","char:" + ch + " " +  Integer.toHexString(Integer.valueOf(ch)));
                    if(!isSymbol(ch)){
                        return "";
                    }
                }
                return source;
            }
        };

        mEditText.setFilters(filters);

        //mEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());

        WebSettings webSettings = mWebView.getSettings();

        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webSettings.setUseWideViewPort(true);

        mWebView.setWebViewClient(new WebViewClient(){

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                Log.d(TAG,"onReceivedError:" + error.getDescription() + " code:" + error.getErrorCode());
                //loadUrl("file:///android_asset/404_error.html");
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.d(TAG,"onPageStarted:" + url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.d(TAG,"onPageFinished:" + url);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
                Log.d(TAG,"onReceivedSslError:" + error.getUrl());
            }

            @Override
            public void onScaleChanged(WebView view, float oldScale, float newScale) {
                super.onScaleChanged(view, oldScale, newScale);
                Log.d(TAG,"onScaleChanged oldScale:" + oldScale + " newScale:" + newScale);
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
        findViewById(R.id.btn_share).setOnClickListener(this);
        findViewById(R.id.doSend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditText.setTransformationMethod(null);
                if(TextUtils.isEmpty(mEditText.getText())){
                    mWebView.loadUrl("javascript:javacalljs()");
                }else{
                    mWebView.loadUrl("javascript:javacalljswithargs('" + mEditText.getText() +"')");
                }
            }
        });

        mClipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        mClipboardManager.addPrimaryClipChangedListener(listener);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mClipboardManager.removePrimaryClipChangedListener(listener);
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

    private OnPrimaryClipChangedListener listener = new OnPrimaryClipChangedListener(){

        @Override
        public void onPrimaryClipChanged() {
            ClipData clipData = mClipboardManager.getPrimaryClip();
            if(clipData != null && clipData.getItemCount() > 0){
               ClipData.Item item = clipData.getItemAt(0);
                Log.d(TAG,"clipData:" + item.getText());
            }
        }
    };

    private void loadUrl(String url){
        mWebView.loadUrl(url);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_load_js){
            loadUrl("file:///android_asset/java_js.html");
        }else if(v.getId() == R.id.btn_load_net){
            loadUrl("http://jingpage.com/#/nineMail?app_key=btgddw");
        }else if(v.getId() == R.id.btn_share){
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT,"聚划算发补贴福利了，大牌正品，买贵必陪，小伙伴们快来抢购吧~  https://10sd1.kuaizhan.com/?_s=kKUX9");
            intent.setType("text/plain");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(Intent.createChooser(intent,"选择分享应用"));
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

    static boolean isSymbol(char ch)
    {
        if(0x21 <= ch && ch <= 0x7E) return true;
        return false;
    }

}
