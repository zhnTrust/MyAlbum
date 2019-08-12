package com.zhn.myalbum.ui;

import android.app.Activity;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.zhn.myalbum.R;
import com.zhn.myalbum.model.SerializableHashMap;
import com.zhn.myalbum.utils.FileUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class MapActivity extends Activity {

    /**
     * 地图容器和控件
     */
    private MapView mMapView = null;
    private AMap mAMap = null;

    /**
     * 定位控件
     */
    private LocationManager mLocationManager;

    /**
     * 地址解析
     */
    private GeocodeSearch mGeocodeSearch;

    /**
     * 精准定位
     */
    private AMapLocationClient mAMapLocationClient = null;
    private AMapLocationClientOption mAMapLocationClientOption = null;

    /**
     * UIL的ImageLoader 库函数
     */
    private ImageLoader mImageLoader = ImageLoader.getInstance();

    /**
     * 显示参数
     */
    private DisplayImageOptions mOptions = null;


    /**
     * View控件
     */
    private Button mBtn_locate;

    /**
     * 图片地址信息,及tag
     */
    public static final String EXTRA_ADDRS = "extra_addrs";
    private SerializableHashMap addrs = new SerializableHashMap();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);

        init();
        initView();
        initLocation();
        initdata();
        initMarker();

    }


    private void init() {
        //初始化地图控制器对象
        if (mAMap == null) {
            mAMap = mMapView.getMap();
        }

        //放大级别
        CameraUpdate zoomCu = CameraUpdateFactory.zoomTo(16);
        //设置
        mAMap.moveCamera(zoomCu);
        //倾斜度
        CameraUpdate tiltCu = CameraUpdateFactory.changeTilt(30);
        mAMap.moveCamera(tiltCu);
    }

    private void initView() {
        mBtn_locate = findViewById(R.id.locate);
    }

    private void initLocation() {
        //定位客户端
        mAMapLocationClient = new AMapLocationClient(this);

        /**
         * 参数
         */
        //定位参数
        mAMapLocationClientOption = new AMapLocationClientOption();
        //设置高精度模式
        mAMapLocationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置单次定位
        mAMapLocationClientOption.setOnceLocation(true);
        //设置定位参数
        mAMapLocationClient.setLocationOption(mAMapLocationClientOption);

        /**
         * 监听
         */
        //设置定位监听
        mAMapLocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null
                        && aMapLocation.getErrorCode() == 0) {
                    double longitude = aMapLocation.getLongitude();  //经度
                    double latitude = aMapLocation.getLatitude();     //纬度
                    LatLng location = new LatLng(latitude, longitude);
                    /**
                     * 更新地位
                     */
                    updatePositionFromLatlng(null, location);
                } else {
                    String errText = "定位失败，" + aMapLocation.getErrorCode() + ":" + aMapLocation.getErrorInfo();
                    System.out.println(errText);
                    Toast.makeText(MapActivity.this, errText, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void initdata() {
        mBtn_locate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAMapLocationClient.startLocation();
            }
        });

        mOptions = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.pic_thumb)
                .showImageForEmptyUri(R.drawable.pic_thumb).showImageOnFail(R.drawable.pic_thumb)
                .cacheInMemory(true).cacheOnDisc(true).build();
    }

    private void initMarker() {
        addrs = (SerializableHashMap) getIntent().getSerializableExtra(MapActivity.EXTRA_ADDRS);
        ArrayList<String> images = new ArrayList<>();
        ArrayList<LatLng> latLngs = new ArrayList<>();
        /**
         * 遍历
         */
        Iterator iter = addrs.getMap().keySet().iterator();
        while (iter.hasNext()) {
            Object key = iter.next();
            images.add((String) key);
            String lat = addrs.getMap().get(key)[0];
            String lot = addrs.getMap().get(key)[1];
            LatLng latLng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lot));
            latLngs.add(latLng);

        }

        updatePositionFromLatlng(images.get(0), latLngs.get(0));

        System.err.println("***************************");
        System.err.println(images.toString());
        System.err.println(latLngs.toString());
        Toast.makeText(MapActivity.this,
                "显示完毕！" + latLngs.toString(),
                Toast.LENGTH_SHORT).show();

    }


    /**
     * 从LatLng 更新地图
     */
    private void updatePositionFromLatlng(String image, final LatLng location) {
        if (image == null) {
            //创建相机
            CameraUpdate cu = CameraUpdateFactory.changeLatLng(location);

            //更新显示
            mAMap.moveCamera(cu);

            /**
             * 标记
             */
            MarkerOptions mo = new MarkerOptions();
            mo.position(location);
            mo.title("now,here");
            mo.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            mo.draggable(true);
            Marker marker = mAMap.addMarker(mo);
            marker.showInfoWindow();
        } else {
            /**
             * 图片视图
             */
            //加载图片
            mImageLoader.init(ImageLoaderConfiguration.createDefault(this));
            mImageLoader.loadImage(FileUtil.getFormatFilePath(image), new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String s, View view) {

                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {

                }

                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {

                    //创建相机
                    CameraUpdate cu = CameraUpdateFactory.changeLatLng(location);

                    //更新显示
                    mAMap.moveCamera(cu);

                    /**
                     * 标记
                     */
                    MarkerOptions mo = new MarkerOptions();
                    mo.position(location);
                    mo.title("picture");


                    //点聚合的页面布局，就是自定义的点聚合部分
                    View v = View.inflate(MapActivity.this, R.layout.view_poi_overlay_item, null);
                    //CircleImageView是自定义的圆形imageview
                    ImageView ivPoiImg = v.findViewById(R.id.iv_poi_img);
                    ivPoiImg.setImageBitmap(bitmap);


                    mo.icon(BitmapDescriptorFactory.fromView(v));
                    mo.draggable(true);
                    Marker marker = mAMap.addMarker(mo);
                    marker.showInfoWindow();
                }

                @Override
                public void onLoadingCancelled(String s, View view) {

                }
            });
        }


    }

}
