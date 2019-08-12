/**
 * ImageLoadTask.java
 * ImageSelector
 * <p>
 * Created by likebamboo on 2014-4-22
 * Copyright (c) 1998-2014 http://likebamboo.github.io/ All rights reserved.
 */

package com.zhn.myalbum.task;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.zhn.myalbum.R;
import com.zhn.myalbum.log.L;
import com.zhn.myalbum.model.TimeImageGroup;
import com.zhn.myalbum.utils.DateUtil;
import com.zhn.myalbum.listener.OnTaskResultListener;
import com.zhn.myalbum.utils.LocationUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

/**
 * 使用contentProvider扫描图片异步任务
 *
 * @author likebamboo
 */
public class ImageTimeLoadTask extends BaseTask {

    /**
     * 上下文对象
     */
    private Context mContext = null;

    /**
     * 对图片组对象(日期 图片列表)的列表
     */
    private ArrayList<TimeImageGroup> mGruopList = new ArrayList<>();

    /**
     * 解析图片城市
     * @param context
     */
    private GeocodeSearch mGeocodeSearch=null;

    /**
     * 城市
     */
    private String mCity="武汉市";

    public ImageTimeLoadTask(Context context) {
        super();
        mContext = context;
        result = mGruopList;
    }

    public ImageTimeLoadTask(Context context, OnTaskResultListener listener) {
        super();
        mContext = context;
        result = mGruopList;
        setOnResultListener(listener);

        mGeocodeSearch=new GeocodeSearch(mContext);
        mGeocodeSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
                RegeocodeAddress addrs=regeocodeResult.getRegeocodeAddress();
                String city=addrs.getCity();
                mCity=city;
                /*
                * */
            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

            }
        });
    }

    /*
     * (non-Javadoc)
     * @see android.os.AsyncTask#doInBackground(Params[])
     */
    @Override
    protected Boolean doInBackground(Void... params) {
        Uri mImageUri = Media.EXTERNAL_CONTENT_URI;
        ContentResolver mContentResolver = mContext.getContentResolver();
        // 构建查询条件，且只查询jpeg和png的图片
        StringBuilder selection = new StringBuilder();
        selection.append(MediaStore.Images.Media.MIME_TYPE).append("=?");
        selection.append(" or ");
        selection.append(MediaStore.Images.Media.MIME_TYPE).append("=?");
        Cursor mCursor = null;
        try {
            // 初始化游标
            mCursor = mContentResolver.query(mImageUri, null, selection.toString(), new String[]{
                    "image/jpeg", "image/png"
            }, Media.DATE_TAKEN);
            // 遍历结果
            while (mCursor.moveToNext()) {
                // 获取图片的路径
                String path = mCursor.getString(mCursor.getColumnIndex(Media.DATA));
                //根据路径获取图片图片Exif信息
                ExifInterface exifInterface = new ExifInterface(path);

                /**
                 * 时间
                 */
                String time = exifInterface.getAttribute(ExifInterface.TAG_DATETIME);
                if (null != time) {
                    //String yyyy:MM:dd HH:mm:ss
                    time = DateUtil.getDay(time);
                } else { //用file对象获取创建时间
                    File file = new File(path);
                    //long
                    time = DateUtil.getDay(file.lastModified());
                }

                /**
                 * 地点
                 */
                //地址 由于Google使用的经纬度格式为DMS格式，也就是度分秒格式，如果需要使用其他格式就需要转化，代码中将其转化为DD格式。
                String[] addr=new String[]{"","",""};
                String log = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
                String log_ref = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
                String lat = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
                String lat_ref = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
                if (log != null && lat != null && log_ref != null && lat_ref != null) {
                    //格式化

                    float flat = LocationUtil.convertRationalLatLonToFloat(lat, lat_ref);
                    float flog = LocationUtil.convertRationalLatLonToFloat(log, log_ref);
                    addr[0]=String.valueOf(flat);
                    addr[1]=String.valueOf(flog);
                    addr[2]=decodeAddr(addr[0],addr[1]);
                }

                //创建TimeImageGroupd对象
                TimeImageGroup item = new TimeImageGroup();

                //设置日期
                item.setDay(time);
                //判断日期相同的TimeImageGroup对象是否已经存在
                int searchIdx = mGruopList.indexOf(item);
                if (searchIdx >= 0) {  //存在
                    //取得存在的对象
                    TimeImageGroup timeImageGroup = mGruopList.get(searchIdx);
                    /**
                     * 添加地点映射（如果有）
                     */
                    if (addr[0].equals("")||addr[1].equals("")){
                        //不添加
                    }else {
                        timeImageGroup.addAddr(path,addr);
                    }
                    //直接添加图片
                    timeImageGroup.addImage(path);
                } else { //不存在，创建并添加
                    /**
                     * 添加地点映射（如果有）
                     */
                    if (addr[0].equals("")||addr[1].equals("")){
                        //不添加
                    }else {
                        item.addAddr(path,addr);
                    }
                    item.addImage(path);
                    mGruopList.add(item);
                }
            }
        } catch (Exception e) {
            // 输出日志
            L.e(e);
        } finally {
            // 关闭游标
            if (mCursor != null && !mCursor.isClosed()) {
                mCursor.close();
            }
            //反转
            Collections.reverse(mGruopList);
            for (int i = 0; i < mGruopList.size(); i++) {
                //反转
                Collections.reverse(mGruopList.get(i).getImages());
            }
        }
        return true;
    }

    private String decodeAddr(String lat,String lot) {
        LatLonPoint latLonPoint = new LatLonPoint(
                Double.parseDouble(lat),
                Double.parseDouble(lot));
        //异步查询
        mGeocodeSearch.getFromLocationAsyn(new RegeocodeQuery(
                latLonPoint,
                20,
                GeocodeSearch.GPS
        ));
        return mCity;
    }

}
