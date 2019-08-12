/**
 * ImageBrowseActivity.java
 * ImageChooser
 * <p>
 * Created by likebamboo on 2014-4-22
 * Copyright (c) 1998-2014 http://likebamboo.github.io/ All rights reserved.
 */

package com.zhn.myalbum.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.ExifInterface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.zhn.myalbum.adapter.ImagePagerAdapter;
import com.zhn.myalbum.log.L;
import com.zhn.myalbum.model.ImageInfo;
import com.zhn.myalbum.utils.DateUtil;
import com.zhn.myalbum.utils.FileSizeUtil;
import com.zhn.myalbum.utils.LocationUtil;
import com.zhn.myalbum.R;

import java.io.File;
import java.util.ArrayList;

/**
 * 大图浏览Activity
 *
 * @author likebamboo
 */
public class ImageBrowseActivity extends Activity {
    /**
     * 图片列表
     */
    public static final String EXTRA_IMAGES = "extra_images";

    /**
     * 图片额外信息列表
     */

    public static final String OHTER_INFO = "other_info";

    /**
     * 位置
     */
    public static final String EXTRA_INDEX = "extra_index";

    /**
     * 图片列表数据源
     */
    private ArrayList<String> mDatas = new ArrayList<String>();

    /**
     * 图片额外信息对象列表
     */
    private ArrayList<ImageInfo> mImageInfos = new ArrayList<>();

    /**
     * 进入到该界面时的索引
     */
    private int mPageIndex = 0;

    /**
     * 图片适配器
     */
    private ImagePagerAdapter mImageAdapter = null;

    /**
     * viewpager
     */
    private ViewPager mViewPager = null;

    /**
     * 地址解析
     */
    private String mAddr="";
    private GeocodeSearch mGeocodeSearch =null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_browse);
        initView();
        initData();


    }

    private void initData() {
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_IMAGES)) {
            mDatas = intent.getStringArrayListExtra(EXTRA_IMAGES);
            mPageIndex = intent.getIntExtra(EXTRA_INDEX, 0);
            mImageAdapter = new ImagePagerAdapter(mDatas);
            mViewPager.setAdapter(mImageAdapter);
            mViewPager.setCurrentItem(mPageIndex);
        }
        if (intent.hasExtra(OHTER_INFO)) {
            mImageInfos = intent.getParcelableArrayListExtra(OHTER_INFO);
        }


        mGeocodeSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
                RegeocodeAddress addrs=regeocodeResult.getRegeocodeAddress();
                mAddr=addrs.getFormatAddress();
            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

            }
        });
    }

    /**
     * 选项菜单
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.menu_image_view, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 菜单项被选中后的回调方法
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //调用返回键
                onBackPressed();
                break;
            case R.id.browse_info:
                showInfo();

        }
        return true;
    }

    private void showInfo() {
        StringBuffer message=getFormatInfo();
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("详细信息")
                .setMessage(message);

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create()
                .show();
    }

    public StringBuffer getFormatInfo() {
        StringBuffer stringBuffer = new StringBuffer();
        try {
            //根据路径获取图片图片Exif信息
            String path = mDatas.get(mPageIndex);
            ExifInterface exifInterface = new ExifInterface(mDatas.get(mPageIndex));
            //文件名
            int start = path.lastIndexOf("/");
            int end = path.lastIndexOf(".");
            if (start != -1 && end != -1) {
                String name = path.substring(start + 1, end);
                stringBuffer.append("标题：" + name + '\n');
            }
            //时间
            String time = exifInterface.getAttribute(ExifInterface.TAG_DATETIME);
            if (time != null) {
                stringBuffer.append("拍摄时间：" + time + '\n');
            } else {//不是手机拍摄的照片，是下载、截图、等其他方式获得的图片,用file获取
                File file = new File(path);
                time = DateUtil.formatFromLong("yyyy-MM-dd HH:mm:ss", file.lastModified());
                stringBuffer.append("创建时间：" + time + '\n');
            }
            //地址 由于Google使用的经纬度格式为DMS格式，也就是度分秒格式，如果需要使用其他格式就需要转化，代码中将其转化为DD格式。
            String log = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
            String log_ref = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
            String lat = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
            String lat_ref = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
            if (log != null && lat != null && log_ref != null && lat_ref != null) {
                float flog = LocationUtil.convertRationalLatLonToFloat(log, log_ref);
                float flat = LocationUtil.convertRationalLatLonToFloat(lat, lat_ref);
                String addr = getAddr(String.valueOf(flog), String.valueOf(flat));
                if (addr != null) {
                    stringBuffer.append("地点：" + addr + '\n');
                } else {
                    stringBuffer.append("地点：" + flog + "," + flat + '\n');
                }

            }

            //宽度
            String width = exifInterface.getAttribute(ExifInterface.TAG_IMAGE_WIDTH);
            if (null != width) {
                stringBuffer.append("宽度：" + width + '\n');
            }
            //高度
            String length = exifInterface.getAttribute(ExifInterface.TAG_IMAGE_LENGTH);
            if (null != length) {
                stringBuffer.append("高度：" + length + '\n');
            }
            //大小 byte
            File file = new File(path);
            if (file.exists() && file.isFile()) {
                long size = file.length();
                String strSize = FileSizeUtil.formatFileSize(size);
                stringBuffer.append("文件大小：" + strSize + '\n');
            }
            //焦距
            String focal_length = exifInterface.getAttribute(ExifInterface.TAG_FOCAL_LENGTH);
            if (null != focal_length) {
                stringBuffer.append("焦距：" + focal_length + '\n');
            }
            //曝光时间
            String exposure_time = exifInterface.getAttribute(ExifInterface.TAG_EXPOSURE_TIME);
            if (null != exposure_time) {
                stringBuffer.append("曝光时间：" + exposure_time + '\n');
            }
            //iso
            String iso = exifInterface.getAttribute(ExifInterface.TAG_ISO);
            if (null != iso) {
                stringBuffer.append("iso：" + iso + '\n');
            }
            //光圈
            String aperture = exifInterface.getAttribute(ExifInterface.TAG_APERTURE);
            if (null != aperture) {
                stringBuffer.append("光圈：" + aperture + '\n');
            }
            stringBuffer.append("路径：" + path);
        } catch (Exception e) {
            // 输出日志
            L.e(e);
        }

        return stringBuffer;
    }

    private String getAddr(String log, String lat) {

        //格式化
        LatLonPoint latLonPoint = new LatLonPoint(Double.parseDouble(lat),
                                                    Double.parseDouble(log));
        //异步查询
        mGeocodeSearch.getFromLocationAsyn(new RegeocodeQuery(
                latLonPoint,
                20,
                GeocodeSearch.GPS
        ));

        return mAddr;
    }

    private void initView() {
        ActionBar actionBar = getActionBar();
        // 设置是否显示应用程序图标
        actionBar.setDisplayShowHomeEnabled(true);
        // 将应用程序图标设置为可点击的按钮
        actionBar.setHomeButtonEnabled(true);
        // 将应用程序图标设置为可点击的按钮，并在图标上添加向左箭头
        actionBar.setDisplayHomeAsUpEnabled(true);
        mViewPager = (ViewPager) findViewById(R.id.image_vp);

        mGeocodeSearch =new GeocodeSearch(this);
    }



}
