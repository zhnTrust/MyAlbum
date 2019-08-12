package com.zhn.myalbum.ui;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ViewFlipper;

import java.util.ArrayList;

import com.zhn.myalbum.loader.LocalImageLoader;
import com.zhn.myalbum.widget.MyImageView;
import com.zhn.myalbum.R;
import com.zhn.myalbum.adapter.ImageFlashAdapter;

public class ImageFlashActivity extends BaseActivity {
    /**
     * 图片列表
     */
    public static final String EXTRA_IMAGES = "extra_images";

    /**
     * 位置
     */
    public static final String EXTRA_INDEX = "extra_index";

    /**
     * 图片地址数据源
     */
    private ArrayList<String> mImages = new ArrayList<>();

    /**
     * 进入到该界面时的索引
     */
    private int mPageIndex = 0;

    /**
     * mFlipper
     */
    private ViewFlipper mFlipper;
    /**
     * 适配器
     */
    private ImageFlashAdapter mAdaper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_flash);
        initView();
        initData();
        mFlipper.startFlipping();
    }


    //选项菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = new MenuInflater(this);
        menuInflater.inflate(R.menu.menu_image_flash, menu);
        return super.onCreateOptionsMenu(menu);
    }



    private void initView() {
        ActionBar actionBar = getActionBar();
        // 设置是否显示应用程序图标
        actionBar.setDisplayShowHomeEnabled(true);
        // 将应用程序图标设置为可点击的按钮
        actionBar.setHomeButtonEnabled(true);
        // 将应用程序图标设置为可点击的按钮，并在图标上添加向左箭头
        actionBar.setDisplayHomeAsUpEnabled(true);
        mFlipper = findViewById(R.id.flipper);

    }

    private void initData() {
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_IMAGES)) {
            mImages = intent.getStringArrayListExtra(EXTRA_IMAGES);
        }
        for (int i = 0; i < mImages.size(); i++) {
            mFlipper.addView(getView(mImages.get(i)));
        }

    }

    private View getView(String path) {
        MyImageView myImageView = new MyImageView(this);
        myImageView.setTag(path);
        // 利用NativeImageLoader类加载本地图片
        Bitmap bitmap = LocalImageLoader.getInstance().loadImage(path, myImageView.getPoint(),
                LocalImageLoader.getImageListener(myImageView, path, R.drawable.pic_thumb, R.drawable.pic_thumb));
        if (bitmap != null) {
            myImageView.setImageBitmap(bitmap);
        } else {
            myImageView.setImageResource(R.drawable.pic_thumb);
        }
        return myImageView;
    }

    //选项菜单单击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //调用返回键
                onBackPressed();
                break;
            case R.id.m1:
                mFlipper.stopFlipping();
                mFlipper.setInAnimation(this,R.anim.in_translate_top);
                mFlipper.setOutAnimation(this,R.anim.out_translate_top);
                mFlipper.startFlipping();
                break;
            case R.id.m2:
                mFlipper.stopFlipping();
                mFlipper.setInAnimation(this,R.anim.appear_bottom_right_in);
                mFlipper.setOutAnimation(this,R.anim.appear_bottom_right_out);
                mFlipper.startFlipping();
                break;
            case R.id.m3:
                mFlipper.stopFlipping();
                mFlipper.setInAnimation(this,R.anim.disappear_bottom_right_in);
                mFlipper.setOutAnimation(this,R.anim.disappear_bottom_right_out);
                mFlipper.startFlipping();
                break;
            case R.id.m4:
                mFlipper.stopFlipping();
                mFlipper.setInAnimation(this,R.anim.drawroll_ani_in);
                mFlipper.setOutAnimation(this,R.anim.drawroll_ani_out);
                mFlipper.startFlipping();
                break;
            case R.id.m5:
                mFlipper.stopFlipping();
                mFlipper.setInAnimation(this,R.anim.path2_rotate_in);
                mFlipper.setOutAnimation(this,R.anim.path2_rotate_out);
                mFlipper.startFlipping();
                break;
            case R.id.m6:
                mFlipper.stopFlipping();
                mFlipper.setInAnimation(this,R.anim.hyperspace_in);
                mFlipper.setOutAnimation(this,R.anim.hyperspace_out);
                mFlipper.startFlipping();
                break;
            case R.id.m7:
                mFlipper.stopFlipping();
                mFlipper.setInAnimation(this,R.anim.push_left_in);
                mFlipper.setOutAnimation(this,R.anim.push_left_out);
                mFlipper.startFlipping();
                break;
            case R.id.m8:
                mFlipper.stopFlipping();
                mFlipper.setInAnimation(this,R.anim.flip_vertical_in);
                mFlipper.setOutAnimation(this,R.anim.flip_vertical_out);
                mFlipper.startFlipping();
                break;
            case R.id.m9:
                mFlipper.stopFlipping();
                mFlipper.setInAnimation(this,R.anim.rbm_in_from_left);
                mFlipper.setOutAnimation(this,R.anim.rbm_out_to_left);
                mFlipper.startFlipping();
                break;
        }
        return true;
    }


}
