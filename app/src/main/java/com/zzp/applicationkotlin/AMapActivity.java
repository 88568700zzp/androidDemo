package com.zzp.applicationkotlin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;

import java.util.ArrayList;
import java.util.List;

public class AMapActivity extends AppCompatActivity implements AMap.OnMyLocationChangeListener,AMap.OnMapClickListener,AMap.OnMapLongClickListener, GeocodeSearch.OnGeocodeSearchListener {

    private AMap aMap;
    private MyLocationStyle myLocationStyle;
    MapView mMapView = null;
    private AMapLocationClient mlocationClient;

    private boolean isFirstLocate = true;
    private long lastLocateTime = 0L;

    private List<LatLng> mLocationPoints = new ArrayList<>();

    private GeocodeSearch geocoderSearch;
    private boolean mGeocodeSearch = false;

    private EditText edit;

    public static LatLng junjinghuayuan = new LatLng(23.122358,113.385167);
    public static LatLng guangzhouta = new LatLng(23.106414,113.324553);

    public LatLng geoResult = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amap);

        edit = findViewById(R.id.edit);

        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);

        if (aMap == null) {
            aMap = mMapView.getMap();
            setUpMap();
        }

        initLocation();
        initGeocode();

        //设置SDK 自带定位消息监听
        //aMap.setOnMyLocationChangeListener(this);

        LatLng latLng = new LatLng(23.005514, 113.349886);// 北京市经纬度

        // 绘制一个圆形
        /*Circle circle = aMap.addCircle(new CircleOptions().center(latLng)
                .radius(4000).strokeColor(Color.argb(50, 1, 1, 1))
                .fillColor(Color.argb(100, 1, 1, 0)).strokeWidth(25));
        circle.setStrokeColor(Color.parseColor("#23dd12"));*/

        PolylineOptions options = new PolylineOptions();

        for(int i = 0;i < 30;i++){
            LatLng newLatLng = new LatLng(latLng.latitude + i * 0.001,latLng.longitude+ i * 0.002);
            options.add(newLatLng);
        }

        aMap.addPolyline(options.width(18).color(Color.parseColor("#1233dd")).useGradient(false).setCustomTexture(BitmapDescriptorFactory.fromResource(R.mipmap.custtexture)));

       for(LatLng point : options.getPoints()){
           aMap.addCircle(new CircleOptions().center(point)
                   .radius(40).strokeWidth(0)
                   .fillColor(Color.parseColor("#23ff22")));
       }

    }

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {

        aMap.getUiSettings().setAllGesturesEnabled(true);

        // 如果要设置定位的默认状态，可以在此处进行设置
        myLocationStyle = new MyLocationStyle();
        aMap.setMyLocationStyle(myLocationStyle);

        aMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(false);
// 设置定位的类型为定位模式，有定位、跟随或地图根据面向方向旋转几种
        //aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);

        aMap.addOnMapLongClickListener(this);
        aMap.addOnMapClickListener(this);
    }

    private void initGeocode(){
        try {
            geocoderSearch = new GeocodeSearch(this);
            geocoderSearch.setOnGeocodeSearchListener(this);
        } catch (AMapException e) {
            e.printStackTrace();
        }
    }

    private void initLocation(){
        try {
            if (mlocationClient == null) {
                //初始化定位
                mlocationClient = new AMapLocationClient(AMapActivity.this);
                //初始化定位参数
                AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
                //设置定位回调监听
                mlocationClient.setLocationListener(new AMapLocationListener() {
                    @Override
                    public void onLocationChanged(AMapLocation amapLocation) {
                        Log.e("amap","onLocationChanged:" + amapLocation);
                        if(amapLocation.getLatitude() == 0L || amapLocation.getLongitude() == 0L){
                            return;
                        }
                        LatLng latLng = new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude());
                        if(isFirstLocate) {
                            mLocationPoints.clear();
                            isFirstLocate = false;
                            //首次定位,选择移动到地图中心点并修改级别到15级
                            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                        }
                        long currentTime = System.currentTimeMillis();
                        mLocationPoints.add(latLng);
                        if(mLocationPoints.size() > 1){
                            float distance =  AMapUtils.calculateLineDistance(mLocationPoints.get(mLocationPoints.size() - 1),mLocationPoints.get(mLocationPoints.size() - 2));
                            Log.e("amap","distance:" + distance + " time:" + (currentTime - lastLocateTime) + " speed:" + amapLocation.getSpeed());
                        }

                        lastLocateTime = currentTime;
                    }
                });
                //设置为高精度定位模式
                mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
                //设置定位参数
                mlocationClient.setLocationOption(mLocationOption);

                mlocationClient.startLocation();//启动定位
            }
        }catch (Exception e){

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
        if(null != mlocationClient){
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
        if(mlocationClient != null && !mlocationClient.isStarted()){
            mlocationClient.startLocation();
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
        if(mlocationClient != null && mlocationClient.isStarted()){
            mlocationClient.stopLocation();
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onMyLocationChange(Location location) {
        if(location != null) {
            Log.e("amap", "onMyLocationChange 定位成功， lat: " + location.getLatitude() + " lon: " + location.getLongitude());
            Bundle bundle = location.getExtras();
            if(bundle != null) {
                int errorCode = bundle.getInt(MyLocationStyle.ERROR_CODE);
                String errorInfo = bundle.getString(MyLocationStyle.ERROR_INFO);
                // 定位类型，可能为GPS WIFI等，具体可以参考官网的定位SDK介绍
                int locationType = bundle.getInt(MyLocationStyle.LOCATION_TYPE);

                /*
                errorCode
                errorInfo
                locationType
                */
                Log.e("amap", "定位信息， code: " + errorCode + " errorInfo: " + errorInfo + " locationType: " + locationType );
            } else {
                Log.e("amap", "定位信息， bundle is null ");

            }

        } else {
            Log.e("amap", "定位失败");
        }
    }

    /**
     * 响应逆地理编码
     */
    public void getAddress(String name) {
        if(mGeocodeSearch){
            return;
        }
        mGeocodeSearch = true;
        /*RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200,
                GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        geocoderSearch.getFromLocationAsyn(query);// 设置异步逆地理编码请求*/
        GeocodeQuery query = new GeocodeQuery(name, "广州市");// 第一个参数表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode，
        geocoderSearch.getFromLocationNameAsyn(query);
    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {

    }

    @Override
    public void onGeocodeSearched(GeocodeResult result, int rCode) {
        mGeocodeSearch = false;
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getGeocodeAddressList() != null
                    && result.getGeocodeAddressList().size() > 0) {
                for(GeocodeAddress address:result.getGeocodeAddressList()) {
                    if (address != null) {
                        String addressName = "经纬度值:" + address.getLatLonPoint() + "\n位置描述:"
                                + address.getFormatAddress();
                        Log.e("mapgeo", addressName);
                    }
                }
                GeocodeAddress address = result.getGeocodeAddressList().get(0);
                geoResult = new LatLng(address.getLatLonPoint().getLatitude(),address.getLatLonPoint().getLongitude());
            }
        }else{
            Log.e("mapgeo","onGeocodeSearched fail");
        }
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        Log.e("mapgeo","onMapLongClick:" + latLng);
    }


    public void onGeo(View view){
        getAddress(edit.getText().toString());
    }

    public void onSmartNavi(View view){
        Intent intent = new Intent();
        intent.setClass(this,IntelligentBroadcastActivity.class);
        startActivity(intent);
    }


    public void onNavi(View view){
        Intent intent = new Intent();
        intent.setClass(this,NaviAMapActivity.class);
        intent.putExtra("startPoint",mLocationPoints.get(mLocationPoints.size() - 1));
        if(geoResult != null){
            intent.putExtra("endPoint", geoResult);
        }else {
            intent.putExtra("endPoint", junjinghuayuan);
        }
        startActivity(intent);
    }
}