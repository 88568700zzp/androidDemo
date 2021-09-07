package com.zzp.applicationkotlin;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.zzp.applicationkotlin.http.BaseProtocal;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by samzhang on 2021/9/2.
 */
public class TestDtkActivity extends AppCompatActivity {

    private final String TAG = "TestDtkActivity_";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_dtk);

    }

    public void hotWords(View view){
        BaseProtocal.getInstance().requestHotWords(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("Http","onFailure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("Http","onResponse:" + response.body().string());
            }
        });
    }

    public void superCategory(View view){
        BaseProtocal.getInstance().requestSuperCategory(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("Http","onFailure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("Http","onResponse:" + response.body().string());
            }
        });
    }

    public void parseTaokouling(View view){
        BaseProtocal.getInstance().requestTaokouling("9哈ledOXooXHMz嘻 https://m.tb.cn/h.fcXerYn?sm=eb8b15  ~金轩宝中秋月饼广式蛋黄椰蓉豆沙水果双黄白莲蓉精美礼盒装多口味(~@ ~)",new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("Http","onFailure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("Http","onResponse:" + response.body().string());
            }
        });
    }

    public void privilegeLink(View view){
        BaseProtocal.getInstance().requestPrivilegeLink("651881558653",new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("Http","onFailure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("Http","onResponse:" + response.body().string());
            }
        });
    }

    public void goodsList(View view){
        BaseProtocal.getInstance().requestGoodsList(1,new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("Http","onFailure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("Http","onResponse:" + response.body().string());
            }
        });
    }
}
