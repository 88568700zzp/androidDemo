package com.zzp.addemo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAdSdk;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.bytedance.sdk.openadsdk.TTRewardVideoAd;
import com.zzp.addemo.constant.Constant;
import com.zzp.addemo.util.SystemUtil;

import java.util.List;

public class TTMainActivity extends AppCompatActivity{

    private TTAdNative mTTAdNative;

    private TTNativeExpressAd mTTAd;
    private TTNativeExpressAd mTTNativeExpressAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tt_main);

        mTTAdNative= TTAdSdk.getAdManager().createAdNative(this);

    }

    public void doClick(View view){
        if(view.getTag().equals("tt1")) {
            AdSlot adSlot = new AdSlot.Builder()
                    .setCodeId("945873078")
                    .setUserID(Constant.USER_ID)//tag_id
                    .setOrientation(TTAdConstant.VERTICAL) //必填参数，期望视频的播放方向：TTAdConstant.HORIZONTAL 或 TTAdConstant.VERTICAL
                    .build();
            mTTAdNative.loadRewardVideoAd(adSlot, new TTAdNative.RewardVideoAdListener() {

                @Override
                public void onError(int i, String s) {
                    Log.d("zzp", "onError:" + i + " s:" + s);
                }

                @Override
                public void onRewardVideoAdLoad(TTRewardVideoAd ttRewardVideoAd) {
                    Log.d("zzp", "onRewardVideoAdLoad");
                    ttRewardVideoAd.showRewardVideoAd(TTMainActivity.this);
                    ttRewardVideoAd.setRewardAdInteractionListener(new TTRewardVideoAd.RewardAdInteractionListener() {
                        @Override
                        public void onAdShow() {
                            Log.d("zzp","onAdShow");
                        }

                        @Override
                        public void onAdVideoBarClick() {
                            Log.d("zzp","onAdVideoBarClick");
                        }

                        @Override
                        public void onAdClose() {
                            Log.d("zzp","onAdClose");
                        }

                        @Override
                        public void onVideoComplete() {
                            Log.d("zzp","onVideoComplete");
                        }

                        @Override
                        public void onVideoError() {
                            Log.d("zzp","onVideoError");
                        }

                        @Override
                        public void onRewardVerify(boolean rewardVerify, int rewardAmount, String rewardName,int code,String msg) {
                            Log.d("zzp","onRewardVerify rewardVerify:"  + rewardVerify + " rewardAmount:" + rewardAmount + " rewardName:" + rewardName + " code:" + code);
                        }

                        @Override
                        public void onSkippedVideo() {
                            Log.d("zzp","onSkippedVideo");
                        }
                    });
                }

                @Override
                public void onRewardVideoCached() {
                    Log.d("zzp", "onRewardVideoCached");
                }
            });
        }else if(view.getTag().equals("tt2")) {
            AdSlot adSlot = new AdSlot.Builder()
                    .setCodeId("945837120")
                    .setUserID(Constant.USER_ID)//tag_id
                    .setSupportDeepLink(true)
                    .setAdCount(1) //请求广告数量为1到3条
                    .setExpressViewAcceptedSize(500,500) //期望模板广告view的size,单位dp
                    .build();
            mTTAdNative.loadInteractionExpressAd(adSlot, new TTAdNative.NativeExpressAdListener() {
                @Override
                public void onError(int i, String s) {
                    Log.d("zzp", "onError:" + i + " s:" + s);
                }

                @Override
                public void onNativeExpressAdLoad(List<TTNativeExpressAd> list) {
                    if(list != null && list.size() > 0){
                        mTTAd = list.get(0);
                        mTTAd.setExpressInteractionListener(new TTNativeExpressAd.AdInteractionListener() {
                            //广告关闭回调
                            @Override
                            public void onAdDismiss() {
                                Log.d("zzp", "setExpressInteractionListener onAdDismiss");
                            }
                            //广告点击回调
                            @Override
                            public void onAdClicked(View view, int type) {
                                Log.d("zzp", "setExpressInteractionListener onAdClicked");
                            }
                            //广告展示回调
                            @Override
                            public void onAdShow(View view, int type) {
                                Log.d("zzp", "setExpressInteractionListener onAdShow");
                            }
                            //广告渲染失败回调
                            @Override
                            public void onRenderFail(View view, String msg, int code) {
                                Log.d("zzp", "onRenderFail");
                            }
                            //广告渲染成功回调
                            @Override
                            public void onRenderSuccess(View view, float width, float height) {
                                Log.d("zzp", "onRenderSuccess");
                                mTTAd.showInteractionExpressAd(TTMainActivity.this);
                            }
                        });

                        mTTAd.render();
                    }
                }
            });
        }else if(view.getTag().equals("tt3")) {
            AdSlot adSlot = new AdSlot.Builder()
                    .setCodeId("945873060")
                    .setSupportDeepLink(true)
                    .setAdCount(1) //请求广告数量为1到3条
                    .build();
            mTTAdNative.loadNativeExpressAd(adSlot, new TTAdNative.NativeExpressAdListener() {
                @Override
                public void onError(int i, String s) {
                    Log.d("zzp", "onError:" + i + " s:" + s);
                }

                @Override
                public void onNativeExpressAdLoad(List<TTNativeExpressAd> list) {
                    if(list != null && list.size() > 0) {
                        mTTNativeExpressAd = list.get(0);
                        mTTNativeExpressAd.setExpressInteractionListener(new TTNativeExpressAd.AdInteractionListener() {
                            //广告关闭回调
                            @Override
                            public void onAdDismiss() {
                                Log.d("zzp", "setExpressInteractionListener onAdDismiss");
                            }

                            //广告点击回调
                            @Override
                            public void onAdClicked(View view, int type) {
                                Log.d("zzp", "setExpressInteractionListener onAdClicked");
                            }

                            //广告展示回调
                            @Override
                            public void onAdShow(View view, int type) {
                                Log.d("zzp", "setExpressInteractionListener onAdShow");
                            }

                            //广告渲染失败回调
                            @Override
                            public void onRenderFail(View view, String msg, int code) {
                                Log.d("zzp", "onRenderFail");
                            }

                            //广告渲染成功回调
                            @Override
                            public void onRenderSuccess(View view, float width, float height) {
                                Log.d("zzp", "onRenderSuccess width:" + width + " height:" + height);
                                addToVideoView(view,width,height);
                            }
                        });
                        mTTNativeExpressAd.render();
                    }
                }
            });
        }
    }

    private void addToVideoView(View view, float width, float height){
        FrameLayout container = findViewById(R.id.video_view);
       ViewGroup.LayoutParams layoutParams = container.getLayoutParams();
        layoutParams.width = (int) SystemUtil.dipToPx(this,width);
        layoutParams.height = (int) SystemUtil.dipToPx(this,height);
        View video = mTTNativeExpressAd.getExpressAdView();
        if (video != null) {
            container.removeAllViews();
            if (view.getParent() == null) {
                container.setVisibility(View.VISIBLE);
                container.addView(view);
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mTTAd != null){
            mTTAd.destroy();
        }
        if(mTTNativeExpressAd != null){
            mTTNativeExpressAd.destroy();
        }
    }
}