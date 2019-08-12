/**
 * ImageListActivity.java
 * ImageChooser
 * <p>
 * Created by likebamboo on 2014-4-23
 * Copyright (c) 1998-2014 http://likebamboo.github.io/ All rights reserved.
 */

package com.zhn.myalbum.ui;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.zhn.myalbum.R;
import com.zhn.myalbum.adapter.ImageListAdapter;
import com.zhn.myalbum.model.ImageInfo;
import com.zhn.myalbum.utils.Util;

import java.util.ArrayList;

/**
 * 某个文件夹下的所有图片列表
 *
 * @author likebamboo
 */
public class ImageListActivity extends BaseActivity implements OnItemClickListener,ImageListAdapter.CheckData {

    /**
     * title
     */
    public static final String EXTRA_TITLE = "extra_title";

    /**
     * 图片列表extra
     */
    public static final String EXTRA_IMAGES_DATAS = "extra_images";

    /**
     * 图片其他信息
     */
    public static final String OTHER_IMAGE_DATAS="other_info";

    /**
     * 图片列表GridView
     */
    private GridView mImagesGv = null;

    /**
     * 图片地址数据源
     */
    private ArrayList<String> mImages = new ArrayList<String>();

    /**
     * 选中图片地址数据源
     */
    private ArrayList<String> mSecImages = new ArrayList<String>();

    /**
     * 图片额外信息对象列表
     */
    private ArrayList<ImageInfo> mImageInfos=new ArrayList<>();

    /**
     * 适配器
     */
    private ImageListAdapter mImageListAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list);
        String title = getIntent().getStringExtra(EXTRA_TITLE);
        if (!TextUtils.isEmpty(title)) {
            setTitle(title);
        }

        initView();
        if (getIntent().hasExtra(EXTRA_IMAGES_DATAS)) {
            mImages = getIntent().getStringArrayListExtra(EXTRA_IMAGES_DATAS);
            setAdapter(mImages);
        }
        if (getIntent().hasExtra(OTHER_IMAGE_DATAS)){
            mImageInfos=getIntent().getParcelableArrayListExtra(OTHER_IMAGE_DATAS);
        }
    }

    /**
     * 初始化界面元素
     */
    private void initView() {
        ActionBar actionBar = getActionBar();
        // 设置是否显示应用程序图标
        actionBar.setDisplayShowHomeEnabled(true);
        // 将应用程序图标设置为可点击的按钮
        actionBar.setHomeButtonEnabled(true);
        // 将应用程序图标设置为可点击的按钮，并在图标上添加向左箭头
        actionBar.setDisplayHomeAsUpEnabled(true);
        mImagesGv = (GridView) findViewById(R.id.images_gv);

    }

    /**
     * 构建并初始化适配器
     *
     * @param datas
     */
    private void setAdapter(ArrayList<String> datas) {
        mImageListAdapter = new ImageListAdapter(this, datas);
        mImagesGv.setAdapter(mImageListAdapter);
        mImagesGv.setOnItemClickListener(this);
    }

    /*
     * gird 单击事件
     */
    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        Intent i = new Intent(this, ImageBrowseActivity.class);
        i.putExtra(ImageBrowseActivity.EXTRA_IMAGES, mImages);
        i.putExtra(ImageBrowseActivity.EXTRA_INDEX, arg2);
        i.putParcelableArrayListExtra(ImageBrowseActivity.OHTER_INFO,mImageInfos);
        startActivity(i);
    }



    //复选
    @Override
    public void onBackPressed() {
        if (mImageListAdapter != null) {
            Util.saveSelectedImags(this, mImageListAdapter.getSelectedImgs());
        }
        super.onBackPressed();
    }

    //选项菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = new MenuInflater(this);
        menuInflater.inflate(R.menu.menu_image_list, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //选项菜单单击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //调用返回键
                onBackPressed();
                break;
            case R.id.flash_list:
                Intent intent=new Intent(this,ImageFlashActivity.class);
                if (mSecImages.size()>=1){
                    intent.putStringArrayListExtra(ImageFlashActivity.EXTRA_IMAGES,mSecImages);
                }else {
                    intent.putStringArrayListExtra(ImageFlashActivity.EXTRA_IMAGES,mImages);
                }

                startActivity(intent);

        }
        return true;
    }


    //接口 回调方法
    @Override
    public void sendCheckData(ArrayList<String> data) {
        mSecImages=data;
    }
}
