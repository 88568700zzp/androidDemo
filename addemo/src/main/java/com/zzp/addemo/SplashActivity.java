package com.zzp.addemo;

import androidx.annotation.MainThread;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAdSdk;
import com.bytedance.sdk.openadsdk.TTSplashAd;
import com.qq.e.ads.splash.SplashAD;
import com.qq.e.ads.splash.SplashADListener;
import com.qq.e.comm.util.AdError;

public class SplashActivity extends Activity {

    private SplashAD mSplashAD;
    private Handler handler;
    private long fetchSplashADTime;

    private static final String SKIP_TEXT = "点击跳过 %d";

    public boolean canJump = false;
    private boolean needStartDemoList = true;

    private TextView skip_view;

    /**
     * 为防止无广告时造成视觉上类似于"闪退"的情况，设定无广告时页面跳转根据需要延迟一定时间，demo
     * 给出的延时逻辑是从拉取广告开始算开屏最少持续多久，仅供参考，开发者可自定义延时逻辑，如果开发者采用demo
     * 中给出的延时逻辑，也建议开发者考虑自定义minSplashTimeWhenNoAD的值（单位ms）
     **/
    private int minSplashTimeWhenNoAD = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        handler = new Handler(Looper.getMainLooper());

        ViewGroup mSplashContainer = findViewById(R.id.splash_container);
        skip_view = findViewById(R.id.skip_view);

        if(false) {
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

            AdSlot adSlot = new AdSlot.Builder()
                    .setCodeId("887439545")
                    .setImageAcceptedSize(displayMetrics.widthPixels, displayMetrics.heightPixels)
                    .build();

            TTAdNative mTTAdNative = TTAdSdk.getAdManager().createAdNative(this);
            mTTAdNative.loadSplashAd(adSlot, new TTAdNative.SplashAdListener() {
                //请求广告失败
                @Override
                @MainThread
                public void onError(int code, String message) {
                    //开发者处理跳转到APP主页面逻辑
                    Log.d("zzp", "onError:" + code + " message:" + message);
                }

                //请求广告超时
                @Override
                @MainThread
                public void onTimeout() {
                    //开发者处理跳转到APP主页面逻辑
                    Log.d("zzp", "onTimeout");
                }

                //请求广告成功
                @Override
                @MainThread
                public void onSplashAdLoad(TTSplashAd ad) {
                    if (ad == null) {
                        return;
                    }

                    ad.setSplashInteractionListener(new TTSplashAd.AdInteractionListener() {

                        @Override
                        public void onAdClicked(View view, int i) {
                            Log.d("zzp", "onAdClicked:" + i);
                        }

                        @Override
                        public void onAdShow(View view, int i) {
                            Log.d("zzp", "onAdShow:" + i);
                        }

                        @Override
                        public void onAdSkip() {
                            Log.d("zzp", "onAdSkip");
                            goToMainActivity();
                        }

                        @Override
                        public void onAdTimeOver() {
                            Log.d("zzp", "onAdTimeOver");
                            goToMainActivity();
                        }
                    });
                    //获取SplashView
                    View view = ad.getSplashView();
                    if (view != null && mSplashContainer != null && !SplashActivity.this.isFinishing()) {
                        mSplashContainer.removeAllViews();
                        //把SplashView 添加到ViewGroup中,注意开屏广告view：width =屏幕宽；height >=75%屏幕高
                        mSplashContainer.addView(view);
                        //设置不开启开屏广告倒计时功能以及不显示跳过按钮,如果这么设置，您需要自定义倒计时逻辑
                        //ad.setNotAllowSdkCountdown();
                    } else {
                        goToMainActivity();
                    }
                }
            }, 10 * 1000);
        }else{
            fetchSplashAD(this,mSplashContainer,skip_view,"6011151871376879",0);
        }
    }

    /**
     * 拉取开屏广告，开屏广告的构造方法有3种，详细说明请参考开发者文档。
     *
     * @param activity        展示广告的activity
     * @param adContainer     展示广告的大容器
     * @param skipContainer   自定义的跳过按钮：传入该view给SDK后，SDK会自动给它绑定点击跳过事件。SkipView的样式可以由开发者自由定制，其尺寸限制请参考activity_splash.xml或者接入文档中的说明。
     * @param posId           广告位ID
     * @param fetchDelay      拉取广告的超时时长：取值范围[3000, 5000]，设为0表示使用广点通SDK默认的超时时长。
     */
    private void fetchSplashAD(Activity activity, ViewGroup adContainer, View skipContainer, String posId,  int fetchDelay) {
        fetchSplashADTime = System.currentTimeMillis();
        mSplashAD = new SplashAD(activity, skipContainer, posId, new SplashADListener() {
            @Override
            public void onADPresent() {
                Log.i("AD_DEMO", "SplashADPresent");
            }

            @Override
            public void onADClicked() {
                Log.i("AD_DEMO", "SplashADClicked clickUrl: "
                        + (mSplashAD.getExt() != null ? mSplashAD.getExt().get("clickUrl") : ""));
            }

            /**
             * 倒计时回调，返回广告还将被展示的剩余时间。
             * 通过这个接口，开发者可以自行决定是否显示倒计时提示，或者还剩几秒的时候显示倒计时
             *
             * @param millisUntilFinished 剩余毫秒数
             */
            @Override
            public void onADTick(long millisUntilFinished) {
                Log.i("AD_DEMO", "SplashADTick " + millisUntilFinished + "ms");
                skip_view.setText(String.format(SKIP_TEXT, Math.round(millisUntilFinished / 1000f)));
            }

            @Override
            public void onADExposure() {
                Log.i("AD_DEMO", "SplashADExposure");
            }

            @Override
            public void onADLoaded(long l) {
                Log.i("AD_DEMO", "onADLoaded");
            }

            @Override
            public void onADDismissed() {
                Log.i("AD_DEMO", "SplashADDismissed");
                goToMainActivity();
            }

            @Override
            public void onNoAD(AdError error) {
                Log.i(
                        "AD_DEMO",
                        String.format("LoadSplashADFail, eCode=%d, errorMsg=%s", error.getErrorCode(),
                                error.getErrorMsg()));
                /**
                 * 为防止无广告时造成视觉上类似于"闪退"的情况，设定无广告时页面跳转根据需要延迟一定时间，demo
                 * 给出的延时逻辑是从拉取广告开始算开屏最少持续多久，仅供参考，开发者可自定义延时逻辑，如果开发者采用demo
                 * 中给出的延时逻辑，也建议开发者考虑自定义minSplashTimeWhenNoAD的值
                 **/
                long alreadyDelayMills = System.currentTimeMillis() - fetchSplashADTime;//从拉广告开始到onNoAD已经消耗了多少时间
                long shouldDelayMills = alreadyDelayMills > minSplashTimeWhenNoAD ? 0 : minSplashTimeWhenNoAD
                        - alreadyDelayMills;//为防止加载广告失败后立刻跳离开屏可能造成的视觉上类似于"闪退"的情况，根据设置的minSplashTimeWhenNoAD
                // 计算出还需要延时多久
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        goToMainActivity();
                    }
                }, shouldDelayMills);
            }
        }, fetchDelay);
        mSplashAD.fetchAndShowIn(adContainer);
    }




    private void goToMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    /** 开屏页一定要禁止用户对返回按钮的控制，否则将可能导致用户手动退出了App而广告无法正常曝光和计费 */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}