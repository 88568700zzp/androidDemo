package com.zzp.applicationkotlin;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapException;

import com.amap.api.maps.model.LatLng;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.AMapNaviViewListener;
import com.amap.api.navi.ParallelRoadListener;
import com.amap.api.navi.enums.AMapNaviParallelRoadStatus;
import com.amap.api.navi.enums.NaviType;
import com.amap.api.navi.enums.PathPlanningStrategy;
import com.amap.api.navi.model.AMapCalcRouteResult;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapModelCross;
import com.amap.api.navi.model.AMapNaviCameraInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviRouteNotifyData;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AMapServiceAreaInfo;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;

import java.util.ArrayList;
import java.util.List;

public class NaviAMapActivity extends Activity implements AMapNaviListener, ParallelRoadListener, AMapNaviViewListener {

    private final String TAG = "NaviZzp";

    protected AMapNavi mAMapNavi;
    AMapNaviView mAMapNaviView = null;

    protected LatLng mEndLatlng;
    protected LatLng mStartLatlng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        initNavi();

        mStartLatlng = getIntent().getParcelableExtra("startPoint");
        mEndLatlng = getIntent().getParcelableExtra("endPoint");


        setContentView(R.layout.activity_navi_amap);

        //获取地图控件引用
        mAMapNaviView = (AMapNaviView) findViewById(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mAMapNaviView.onCreate(savedInstanceState);
        mAMapNaviView.setAMapNaviViewListener(this);
    }


    private void initNavi(){
        try {
            mAMapNavi = AMapNavi.getInstance(getApplicationContext());
            mAMapNavi.addAMapNaviListener(this);
            mAMapNavi.addParallelRoadListener(this);
            /**
             * isUseInnerVoice - 是否使用内部语音播报， 默认为false
             * isCallBackText - isUseInnerVoice设置为true以后，AMapNaviListener.onGetNavigationText(int, java.lang.String)接口会继续返回文字，默认为false
             */
            mAMapNavi.setUseInnerVoice(true, true);

            //设置模拟导航的行车速度
            mAMapNavi.setEmulatorNaviSpeed(75);

        } catch (AMapException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mAMapNaviView.onDestroy();
        if (mAMapNavi!=null){
            mAMapNavi.stopNavi();
            mAMapNavi.destroy();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mAMapNaviView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mAMapNaviView.onPause();
    }


    @Override
    public void onInitNaviFailure() {
        Toast.makeText(this, "init navi Failed", Toast.LENGTH_SHORT).show();
    }

    private NaviLatLng convertTo(LatLng point){
        return new NaviLatLng(point.latitude, point.longitude);
    }

    @Override
    public void onInitNaviSuccess() {
        Log.d(TAG,"onInitNaviSuccess");

        NaviLatLng startLatlng = convertTo(mStartLatlng);
        NaviLatLng endLatlng = convertTo(mEndLatlng);

        /*mAMapNavi.calculateWalkRoute(startLatlng, endLatlng);*/
        int strategy = 0;
        try {
            /**
             * avoidCongestion - 是否躲避拥堵
             * avoidHighway - 是否不走高速
             * avoidCost - 是否避免收费
             * prioritiseHighway - 是否高速优先
             * multipleRoute - 单路径or多路径，6.3版本以后该参数已经无效，统一使用多路线算路。
             */
            //再次强调，最后一个参数为true时代表多路径，否则代表单路径
            strategy = mAMapNavi.strategyConvert(true, false, false, false, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<NaviLatLng> from = new ArrayList<NaviLatLng>();
        List<NaviLatLng> to = new ArrayList<NaviLatLng>();
        List<NaviLatLng> wayPointList = new ArrayList<NaviLatLng>();

        from.add(startLatlng);
        to.add(endLatlng);
        //wayPointList.add(convertTo(AMapActivity.guangzhouta));

        /**
            from - 指定的导航起点。支持多个起点，起点列表的尾点为实际导航起点，其他坐标点为辅助信息，带有方向性，可有效避免算路到马路的另一侧；
            to - 指定的导航终点。支持多个终点，终点列表的尾点为实际导航终点，其他坐标点为辅助信息，带有方向性，可有效避免算路到马路的另一侧。
            wayPoints - 途经点，同时支持最多16个途经点的路径规划；
            strategy - 规划策略，详情参见 PathPlanningStrategy。普通客车、货车请使用"DRIVING_"开头的策略，驾车推荐使用默认策略 PathPlanningStrategy.DRIVING_MULTIPLE_ROUTES_DEFAULT， 摩托车请使用"MOTOR_"开头的策略。
         */
        mAMapNavi.calculateDriveRoute(from, to, wayPointList, strategy);
    }

    @Override
    public void onStartNavi(int i) {
        Log.d(TAG,"onStartNavi");
    }

    @Override
    public void onTrafficStatusUpdate() {
        Log.d(TAG,"onTrafficStatusUpdate");
    }

    @Override
    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {
        Log.d("getLocation","onLocationChange:" + aMapNaviLocation.getSpeed() + " " + aMapNaviLocation.getCoord().toString());
    }

    @Override
    public void onGetNavigationText(int i, String s) {
        Log.d(TAG,"onGetNavigationText:" + s);
    }

    @Override
    public void onGetNavigationText(String s) {
    }

    @Override
    public void onEndEmulatorNavi() {
        Log.d(TAG,"onEndEmulatorNavi");
        Toast.makeText(this,"导航结束",Toast.LENGTH_SHORT).show();;
    }

    @Override
    public void onArriveDestination() {
        Log.d(TAG,"onArriveDestination");
        Toast.makeText(this,"导航结束",Toast.LENGTH_SHORT).show();;
    }

    @Override
    public void onCalculateRouteFailure(int i) {

    }

    @Override
    public void onReCalculateRouteForYaw() {
        Log.d(TAG,"onReCalculateRouteForYaw");
    }

    @Override
    public void onReCalculateRouteForTrafficJam() {
        Log.d(TAG,"onReCalculateRouteForTrafficJam");
    }

    @Override
    public void onArrivedWayPoint(int i) {
        Log.d(TAG,"onArrivedWayPoint:" + i);
    }

    @Override
    public void onGpsOpenStatus(boolean b) {

    }

    @Override
    public void onNaviInfoUpdate(NaviInfo naviInfo) {
        Log.d("zzp12345","onNaviInfoUpdate:" + naviInfo.getPathRetainDistance() + " " + naviInfo.getPathRetainTime());
    }

    @Override
    public void updateCameraInfo(AMapNaviCameraInfo[] aMapNaviCameraInfos) {

    }

    @Override
    public void updateIntervalCameraInfo(AMapNaviCameraInfo aMapNaviCameraInfo, AMapNaviCameraInfo aMapNaviCameraInfo1, int i) {

    }

    @Override
    public void onServiceAreaUpdate(AMapServiceAreaInfo[] aMapServiceAreaInfos) {

    }

    @Override
    public void showCross(AMapNaviCross aMapNaviCross) {

    }

    @Override
    public void hideCross() {

    }

    @Override
    public void showModeCross(AMapModelCross aMapModelCross) {

    }

    @Override
    public void hideModeCross() {

    }

    @Override
    public void showLaneInfo(AMapLaneInfo[] aMapLaneInfos, byte[] bytes, byte[] bytes1) {

    }

    @Override
    public void showLaneInfo(AMapLaneInfo aMapLaneInfo) {

    }

    @Override
    public void hideLaneInfo() {

    }

    @Override
    public void onCalculateRouteSuccess(int[] ints) {

    }

    @Override
    public void notifyParallelRoad(int aMapNaviParallelRoadStatus) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] aMapNaviTrafficFacilityInfos) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo aMapNaviTrafficFacilityInfo) {

    }

    @Override
    public void updateAimlessModeStatistics(AimLessModeStat aimLessModeStat) {

    }

    @Override
    public void updateAimlessModeCongestionInfo(AimLessModeCongestionInfo aimLessModeCongestionInfo) {

    }

    @Override
    public void onPlayRing(int i) {

    }

    @Override
    public void onCalculateRouteSuccess(AMapCalcRouteResult aMapCalcRouteResult) {
        mAMapNavi.startNavi(NaviType.EMULATOR);
    }

    @Override
    public void onCalculateRouteFailure(AMapCalcRouteResult result) {
        //路线计算失败
        Log.e("dm", "--------------------------------------------");
        Log.i("dm", "路线计算失败：错误码=" + result.getErrorCode() + ",Error Message= " + result.getErrorDescription());
        Log.i("dm", "错误码详细链接见：http://lbs.amap.com/api/android-navi-sdk/guide/tools/errorcode/");
        Log.e("dm", "--------------------------------------------");
        Toast.makeText(this, "errorInfo：" + result.getErrorDetail() + ", Message：" + result.getErrorDescription(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNaviRouteNotify(AMapNaviRouteNotifyData aMapNaviRouteNotifyData) {

    }

    @Override
    public void onGpsSignalWeak(boolean b) {

    }

    @Override
    public void notifyParallelRoad(AMapNaviParallelRoadStatus aMapNaviParallelRoadStatus) {
        if (aMapNaviParallelRoadStatus.getmElevatedRoadStatusFlag() == 1) {
            Toast.makeText(this, "当前在高架上", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "当前在高架上");
        } else if (aMapNaviParallelRoadStatus.getmElevatedRoadStatusFlag() == 2) {
            Toast.makeText(this, "当前在高架下", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "当前在高架下");
        }

        if (aMapNaviParallelRoadStatus.getmParallelRoadStatusFlag() == 1) {
            Toast.makeText(this, "当前在主路", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "当前在主路");
        } else if (aMapNaviParallelRoadStatus.getmParallelRoadStatusFlag() == 2) {
            Toast.makeText(this, "当前在辅路", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "当前在辅路");
        }
    }

    @Override
    public void onNaviSetting() {
        Log.d(TAG,"onNaviSetting");
    }

    @Override
    public void onNaviCancel() {
        Log.d(TAG,"onNaviCancel");
        finish();
    }

    @Override
    public boolean onNaviBackClick() {
        Log.d(TAG,"onNaviBackClick");
        return false;
    }

    @Override
    public void onNaviMapMode(int i) {
        Log.d(TAG,"onNaviMapMode");
    }

    @Override
    public void onNaviTurnClick() {
        Log.d(TAG,"onNaviTurnClick");
    }

    @Override
    public void onNextRoadClick() {

    }

    @Override
    public void onScanViewButtonClick() {

    }

    @Override
    public void onLockMap(boolean b) {

    }

    @Override
    public void onNaviViewLoaded() {
        Log.d(TAG, "导航页面加载成功");
        Log.d(TAG, "请不要使用AMapNaviView.getMap().setOnMapLoadedListener();会overwrite导航SDK内部画线逻辑");
    }

    @Override
    public void onMapTypeChanged(int i) {

    }

    @Override
    public void onNaviViewShowMode(int i) {

    }
}