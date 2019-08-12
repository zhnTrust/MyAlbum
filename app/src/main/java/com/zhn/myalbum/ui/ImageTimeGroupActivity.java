package com.zhn.myalbum.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.zhn.myalbum.model.SerializableHashMap;
import com.zhn.myalbum.utils.SDcardUtil;
import com.zhn.myalbum.utils.TaskUtil;
import com.zhn.myalbum.utils.ThreadUtils;
import com.zhn.myalbum.widget.MyLoadingLayout;
import com.zhn.myalbum.R;
import com.zhn.myalbum.listener.OnTaskResultListener;
import com.zhn.myalbum.model.TimeImageGroup;
import com.zhn.myalbum.task.ImageTimeLoadTask;
import com.zhn.myalbum.adapter.ImageTimeAdapter;

public class ImageTimeGroupActivity extends Activity implements
        ImageTimeAdapter.GirdViewIdx,
        ImageTimeAdapter.OnClickListener {

    /**
     * loading布局
     */
    private MyLoadingLayout mMyLoadingLayout = null;

    /**
     * 图片组ListView
     */
    private ListView mGroupTimeImagesLv = null;

    /**
     * 图片数据
     */
    private ArrayList<TimeImageGroup> mDatas = null;

    /**
     * 适配器
     */
    private ImageTimeAdapter mGroupTimeAdapter = null;

    /**
     * 图片扫描一般任务 获取所有图片
     */
    private ImageTimeLoadTask mTimeLoadTask = null;

    private int mGirdViewIdx = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_time_group);
        initView();
        loadTimeImages();
        initData();
    }

    private void initData() {

    }


    /**
     * jiazai
     */
    private void loadTimeImages() {
        mMyLoadingLayout.showLoading(true);
        if (!SDcardUtil.hasExternalStorage()) {
            mMyLoadingLayout.showEmpty(getString(R.string.donot_has_sdcard));
            return;
        }

        // 线程正在执行
        if (mTimeLoadTask != null && mTimeLoadTask.getStatus() == AsyncTask.Status.RUNNING) {
            return;
        }
        //没有线程则新建线程
        mTimeLoadTask = new ImageTimeLoadTask(this, new OnTaskResultListener() {
            @SuppressWarnings("unchecked")
            @Override
            public void onResult(boolean success, String error, Object result) {
                mMyLoadingLayout.showLoading(false);
                // 如果加载成功
                if (success && result != null && result instanceof ArrayList) {
                    /**
                     * 从task中获得按时间分类的集合
                     */
                    mDatas = (ArrayList<TimeImageGroup>) result;
                    setImageAdapter(mDatas);
                } else {
                    // 加载失败，显示错误提示
                    mMyLoadingLayout.showFailed(getString(R.string.loaded_fail));
                }
            }
        });
        TaskUtil.execute(mTimeLoadTask);
    }


    private void initView() {
        mMyLoadingLayout = (MyLoadingLayout) findViewById(R.id.loading_layout);
        mGroupTimeImagesLv = (ListView) findViewById(R.id.list_time_image);

    }

    /**
     * 构建GridView的适配器
     *
     * @param data
     */
    private void setImageAdapter(ArrayList<TimeImageGroup> data) {
        if (data == null || data.size() == 0) {
            mMyLoadingLayout.showEmpty(getString(R.string.no_images));
        }
        mGroupTimeAdapter = new ImageTimeAdapter(this, data, mGroupTimeImagesLv);
        mGroupTimeImagesLv.setAdapter(mGroupTimeAdapter);
    }


    /**
     * 回调方法 接收并处理消息
     *
     * @param i
     * @param images
     */
    @Override
    public void sendImage(int i, ArrayList<String> images) {
        mGirdViewIdx = i;
        Intent intent = new Intent(this, ImageBrowseActivity.class);
        intent.putExtra(ImageBrowseActivity.EXTRA_IMAGES, images);
        intent.putExtra(ImageBrowseActivity.EXTRA_INDEX, mGirdViewIdx);
        startActivity(intent);
    }

    @Override
    public void onClick(TextView textview, HashMap<String, String[]> addrs) {

        if (textview.getText().toString().trim().equals("")) {
            //nothing
        } else {
            SerializableHashMap myMap = new SerializableHashMap();
            myMap.setMap(addrs);
            Intent intent = new Intent(ImageTimeGroupActivity.this, MapActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(MapActivity.EXTRA_ADDRS, myMap);
            intent.putExtras(bundle);
            startActivity(intent);
        }

    }


}
