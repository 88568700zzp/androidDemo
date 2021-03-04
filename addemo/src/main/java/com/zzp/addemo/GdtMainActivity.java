package com.zzp.addemo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.qq.e.ads.cfg.VideoOption;
import com.qq.e.ads.interstitial2.UnifiedInterstitialAD;
import com.qq.e.ads.interstitial2.UnifiedInterstitialADListener;
import com.qq.e.ads.nativ.MediaView;
import com.qq.e.ads.nativ.NativeADMediaListener;
import com.qq.e.ads.nativ.NativeADUnifiedListener;
import com.qq.e.ads.nativ.NativeUnifiedAD;
import com.qq.e.ads.nativ.NativeUnifiedADData;
import com.qq.e.ads.nativ.widget.NativeAdContainer;
import com.qq.e.ads.rewardvideo.RewardVideoAD;
import com.qq.e.ads.rewardvideo.RewardVideoADListener;
import com.qq.e.comm.constants.AdPatternType;
import com.qq.e.comm.util.AdError;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by samzhang on 2021/3/2.
 */
public class GdtMainActivity extends AppCompatActivity {

    private static final String TAG = "GdtMainActivity";

    private RewardVideoAD mRewardVideoAD;
    private UnifiedInterstitialAD mIad;
    private NativeUnifiedADData mAdData;

    // 与广告有关的变量，用来显示广告素材的UI
    private NativeUnifiedAD mAdManager;
    private MediaView mMediaView;
    private ImageView mImagePoster;
    private ImageView mImageLogo;
    private NativeAdContainer mNativeAdContainer;
    private Button mDownloadButton,mCTAButton;
    private RelativeLayout mContainer;
    private TextView mTitle,mDesc;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gdt_main);

        mMediaView = findViewById(R.id.gdt_media_view);
        mImagePoster = findViewById(R.id.img_poster);
        mImageLogo = findViewById(R.id.img_logo);
        mNativeAdContainer = findViewById(R.id.nativeAdContainer);
        mDownloadButton = findViewById(R.id.btn_download);
        mCTAButton = findViewById(R.id.btn_cta);
        mContainer = findViewById(R.id.ad_info_container);

        mTitle = findViewById(R.id.text_title);
        mDesc = findViewById(R.id.text_desc);
    }

    public void doClick(View view){
        if(view.getTag() .equals("gdt1")){
            mRewardVideoAD = new RewardVideoAD(this, "5041057855914540", new RewardVideoADListener() {
                @Override
                public void onADLoad() {
                    Log.d(TAG,"onADLoad");
                    if (!mRewardVideoAD.hasShown()) {//广告展示检查2：当前广告数据还没有展示过
                        mRewardVideoAD.showAD();
                    }
                }

                @Override
                public void onVideoCached() {
                    Log.d(TAG,"onVideoCached");
                }

                @Override
                public void onADShow() {
                    Log.d(TAG,"onADShow");
                }

                @Override
                public void onADExpose() {
                    Log.d(TAG,"onADExpose");
                }

                @Override
                public void onReward(Map<String, Object> map) {
                    Log.d(TAG,"onReward");
                }

                @Override
                public void onADClick() {
                    Log.d(TAG,"onADClick");
                }

                @Override
                public void onVideoComplete() {
                    Log.d(TAG,"onVideoComplete");
                }

                @Override
                public void onADClose() {
                    Log.d(TAG,"onADClose");
                }

                @Override
                public void onError(AdError adError) {
                    Log.d(TAG,"onError:" + adError.getErrorMsg());
                }
            }); // 有声播放
            mRewardVideoAD.loadAD();
        }else if(view.getTag() .equals("gdt2")){
            showCAd(0,"8061861532767214");
        }else if(view.getTag() .equals("gdt3")){
            showCAd(0,"5001369531906172");
        }else if(view.getTag() .equals("gdt4")){
            showNativeAd();
        }
    }

    private void showCAd(int type,String positionId){
        if(mIad != null){
            mIad.close();
            mIad.destroy();
        }
        mIad = new UnifiedInterstitialAD(this,positionId,new UnifiedInterstitialADListener(){

            @Override
            public void onADReceive() {
                Log.d(TAG,"onADReceive");
                if(mIad.isValid()){
                    if(type == 0) {
                        mIad.showAsPopupWindow(GdtMainActivity.this);
                    }else if(type == 1){
                        mIad.showFullScreenAD(GdtMainActivity.this);
                    }
                }
            }

            @Override
            public void onVideoCached() {
                Log.d(TAG,"onVideoCached");
            }

            @Override
            public void onNoAD(AdError adError) {
                Log.d(TAG,"onNoAD:" + adError.getErrorMsg());
            }

            @Override
            public void onADOpened() {
                Log.d(TAG,"onADOpened");
            }

            @Override
            public void onADExposure() {
                Log.d(TAG,"onADExposure");
            }

            @Override
            public void onADClicked() {
                Log.d(TAG,"onADClicked");
            }

            @Override
            public void onADLeftApplication() {
                Log.d(TAG,"onADLeftApplication");
            }

            @Override
            public void onADClosed() {
                Log.d(TAG,"onADClosed");
            }
        });
        if(type == 0) {
            mIad.loadAD();
        }else if(type == 1){
            mIad.loadFullScreenAD();
        }
    }

    private void showNativeAd(){
        if(mAdManager == null) {
            mAdManager = new NativeUnifiedAD(this, "3051750865539297", new NativeADUnifiedListener(){

                @Override
                public void onNoAD(AdError adError) {
                    Log.d(TAG,"onError:" + adError.getErrorMsg());
                }

                @Override
                public void onADLoaded(List<NativeUnifiedADData> list) {
                    if (list != null && list.size() > 0) {
                        mNativeAdContainer.setVisibility(View.VISIBLE);
                        for(NativeUnifiedADData data:list){
                            Log.d(TAG,"onADLoaded title:" + data.getTitle() + " ImageUrl:" + data.getImgUrl() + " iconUrl:" + data.getIconUrl() + " desc:" + data.getDesc() + " AdPatternType:" + data.getAdPatternType());
                        }

                        mAdData = list.get(0);
                        renderAdUi(mAdData);

                        List<View> clickableViews = new ArrayList<>();
                        List<View> customClickableViews = new ArrayList<>();
                        clickableViews.add(mDownloadButton);
                        if(mAdData.getAdPatternType() == AdPatternType.NATIVE_2IMAGE_2TEXT ||
                                mAdData.getAdPatternType() == AdPatternType.NATIVE_1IMAGE_2TEXT){
                            // 双图双文、单图双文：注册mImagePoster的点击事件
                            clickableViews.add(mImagePoster);
                        } else if(mAdData.getAdPatternType() != AdPatternType.NATIVE_VIDEO){
                            // 三小图广告：注册native_3img_ad_container的点击事件
                            clickableViews.add(findViewById(R.id.native_3img_ad_container));
                        }
                        //作为customClickableViews传入，点击不进入详情页，直接下载或进入落地页，视频和图文广告均生效
                        mAdData.bindAdToView(GdtMainActivity.this, mNativeAdContainer, null, clickableViews, customClickableViews);

                        if(mAdData.getAdPatternType() == AdPatternType.NATIVE_VIDEO){
                            mImagePoster.setVisibility(View.GONE);
                            mMediaView.setVisibility(View.VISIBLE);

                            mAdData.bindMediaView(mMediaView, null, new NativeADMediaListener() {
                                @Override
                                public void onVideoInit() {
                                    Log.d(TAG, "onVideoInit: ");
                                }

                                @Override
                                public void onVideoLoading() {
                                    Log.d(TAG, "onVideoLoading: ");
                                }

                                @Override
                                public void onVideoReady() {
                                    Log.d(TAG, "onVideoReady");
                                }

                                @Override
                                public void onVideoLoaded(int videoDuration) {
                                    Log.d(TAG, "onVideoLoaded: ");

                                }

                                @Override
                                public void onVideoStart() {
                                    Log.d(TAG, "onVideoStart");
                                }

                                @Override
                                public void onVideoPause() {
                                    Log.d(TAG, "onVideoPause: ");
                                }

                                @Override
                                public void onVideoResume() {
                                    Log.d(TAG, "onVideoResume: ");
                                }

                                @Override
                                public void onVideoCompleted() {
                                    Log.d(TAG, "onVideoCompleted: ");
                                }

                                @Override
                                public void onVideoError(AdError error) {
                                    Log.d(TAG, "onVideoError: ");
                                }

                                @Override
                                public void onVideoStop() {
                                    Log.d(TAG, "onVideoStop");
                                }

                                @Override
                                public void onVideoClicked() {
                                    Log.d(TAG, "onVideoClicked");
                                }
                            });
                        }else if(mAdData.getAdPatternType() == AdPatternType.NATIVE_2IMAGE_2TEXT ||
                                mAdData.getAdPatternType() == AdPatternType.NATIVE_1IMAGE_2TEXT){
                            // 双图双文、单图双文：注册mImagePoster的点击事件
                            clickableViews.add(mImagePoster);
                        }else {
                            // 三小图广告：注册native_3img_ad_container的点击事件
                            clickableViews.add(findViewById(R.id.native_3img_ad_container));
                        }

                        /**
                         * 营销组件
                         * 支持项目：智能电话（点击跳转拨号盘），外显表单
                         *  bindCTAViews 绑定营销组件监听视图，注意：bindCTAViews的视图不可调用setOnClickListener，否则SDK功能可能受到影响
                         *  ad.getCTAText 判断拉取广告是否包含营销组件，如果包含组件，展示组件按钮，否则展示download按钮
                         */
                        List<View> CTAViews = new ArrayList<>();
                        CTAViews.add(mCTAButton);
                        mAdData.bindCTAViews(CTAViews);
                        String ctaText = mAdData.getCTAText(); //获取组件文案
                        if(!TextUtils.isEmpty(ctaText)){
                            //如果拉取广告包含CTA组件，则渲染该组件
                            //当广告中有营销组件时，隐藏下载按钮，仅为demo示例所用，开发者可自行决定mDownloadButton按钮是否显示
                            mCTAButton.setText(ctaText);
                            mCTAButton.setVisibility(View.VISIBLE);
                            mDownloadButton.setVisibility(View.INVISIBLE);
                        } else {
                            mCTAButton.setVisibility(View.INVISIBLE);
                            mDownloadButton.setVisibility(View.VISIBLE);
                        }
                    }
                }
            });
            mAdManager.setVideoPlayPolicy(VideoOption.VideoPlayPolicy.AUTO); // 本次拉回的视频广告，从用户的角度看是自动播放的
        }
        mAdManager.loadData(2);
    }

    private void renderAdUi(NativeUnifiedADData ad) {
        int patternType = ad.getAdPatternType();
        if (patternType == AdPatternType.NATIVE_2IMAGE_2TEXT
                || patternType == AdPatternType.NATIVE_VIDEO) {
            mImagePoster.setVisibility(View.VISIBLE);

            Glide.with(GdtMainActivity.this).load(ad.getIconUrl()).into(mImageLogo);
            Glide.with(GdtMainActivity.this).load(ad.getImgUrl()).into(mImagePoster);
            mDesc.setText(ad.getDesc());
            mTitle.setText(ad.getTitle());
        } else if (patternType == AdPatternType.NATIVE_3IMAGE) {
            int[] imgs = new int[]{
                    R.id.img_1,
                    R.id.img_2,
                    R.id.img_3
            };
            for(int i = 0;i < imgs.length;i++){
                ImageView imageView = findViewById(imgs[i]);
                Glide.with(GdtMainActivity.this).load(ad.getImgList().get(i)).into(imageView);
            }
            //mAQuery.id(R.id.native_3img_title).text(ad.getTitle());
            //mAQuery.id(R.id.native_3img_desc).text(ad.getDesc());
        } else if (patternType == AdPatternType.NATIVE_1IMAGE_2TEXT) {
            Glide.with(GdtMainActivity.this).load(ad.getIconUrl()).into(mImageLogo);
            mDesc.setText(ad.getDesc());
            mTitle.setText(ad.getTitle());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mIad != null){
            mIad.close();
            mIad.destroy();
        }
        if(mAdData != null){
            mAdData.destroy();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mAdData != null){
            mAdData.resume();
        }
    }
}
