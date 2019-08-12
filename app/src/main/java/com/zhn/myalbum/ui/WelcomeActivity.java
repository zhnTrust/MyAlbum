
package com.zhn.myalbum.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.zhn.myalbum.adapter.MyFragmentStatePagerAdapter;
import com.zhn.myalbum.widget.splash.MyColorAnimationView;
import com.zhn.myalbum.R;


public class WelcomeActivity extends FragmentActivity {

    //权限代码
    private static final int STORAGE_PERMISSION_CODE = 101;

    /**
     * 动画资源
     */
    private static final int[] resource = new int[]{
            R.drawable.backgroundcolor_welcome0,
            R.drawable.backgroundcolor_welcome1,
            R.drawable.backgroundcolor_welcome2,
            R.drawable.pic_03};


    /**
     * 适配器
     */
    private MyFragmentStatePagerAdapter mMyFragmentStatePagerAdapter = null;

    /**
     * 颜色渐变View
     */
    private MyColorAnimationView mMyColorAnimationView = null;

    /**
     * ViewPager
     */
    ViewPager mViewPager = null;

    /**
     * 当前页
     */
    private int mCurrentpage = 0;

    /**
     * 手指滑动起始和终点的x坐标
     */
    float startX, endX;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        initView();
        initData();

        /**
         * 首先，你必须在 设置 Viewpager的 adapter 之后在调用这个方法 第二点，setmViewPager(ViewPager
         * mViewPager,Object obj, int count, int... colors) 第一个参数 是 你需要传人的
         * viewpager 第二个参数 是
         * 一个实现了ColorAnimationView.OnPageChangeListener接口的Object,用来实现回调 第三个参数 是
         * viewpager 的 孩子数量 第四个参数 int... colors ，你需要设置的颜色变化值~~ 如何你传人
         * 空，那么触发默认设置的颜色动画
         * */
        /**
         * Frist: You need call this method after you set the Viewpager mMyFragmentStatePagerAdapter;
         * Second: setmViewPager(ViewPager mViewPager,Object obj， int count,
         * int... colors) so,you can set any length colors to make the animation
         * more cool! Third: If you call this method like below, make the colors
         * no data, it will create a change color by default.
         * */

        // Four : Also ,you can call this method like this:
        // colorAnimationView.setmViewPager(mViewPager,this,resource.length,0xffFF8080,0xff8080FF,0xffffffff,0xff80ff80);
    }

    private void initView() {
        // 初始化颜色渐变视图
        mMyColorAnimationView = (MyColorAnimationView) findViewById(R.id.ColorAnimationView);

        // 初始化ViewPager视图
        mViewPager = (ViewPager) findViewById(R.id.viewPager);

        // 初始化适配器
        mMyFragmentStatePagerAdapter = new MyFragmentStatePagerAdapter(getSupportFragmentManager());
    }

    private void initData() {
        //ViewPager添加适配器
        mViewPager.setAdapter(mMyFragmentStatePagerAdapter);

        //颜色渐变视图设置ViewPager
        mMyColorAnimationView.setmViewPager(mViewPager, resource.length);

        //颜色渐变视图设置事件监听
        mMyColorAnimationView.setmOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position,
                                       float positionOffset,
                                       int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mCurrentpage = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //颜色渐变视图设置事件监听
        mMyColorAnimationView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = motionEvent.getX();
                        break;
                    case MotionEvent.ACTION_UP:
                        endX = motionEvent.getX();

                        //获取屏幕宽度 用于判断滑动距离 1/4
                        WindowManager windowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
                        Point winSize = new Point();
                        windowManager.getDefaultDisplay().getSize(winSize);
                        float with = winSize.x;
                        float move = startX - endX;
                        //向左滑 距离合理
                        if (mCurrentpage == (resource.length - 1) && move > 0 && move >= (with / 4.0)) {
                            //检查权限
                            checkStoragePermission();
                        }
                        break;
                }
                return false;
            }


            /**
             * 静态方法
             */


        });
    }

    /**
     * 检查权限
     */
    //开始时检查存储读写权限
    private void checkStoragePermission() {
        // 检查是否有存储的读写权限
        // 检查权限的方法: ContextCompat.checkSelfPermission()两个参数分别是Context和权限名.
        // 返回PERMISSION_GRANTED是有权限，PERMISSION_DENIED没有权限
        if (ContextCompat.checkSelfPermission(WelcomeActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //没有权限，向系统请求权限
            requestPermission(STORAGE_PERMISSION_CODE);
        } else {
            // 同组的权限，只要有一个已经授权，则系统会自动授权同一组的所有权限，
            // 比如WRITE_EXTERNAL_STORAGE和READ_EXTERNAL_STORAGE
            Toast.makeText(WelcomeActivity.this, "已经获取存储读写权限", Toast.LENGTH_SHORT);
            gotoMainActivity();
        }
    }

    /**
     * 检查出无权限时，申请权限
     * @param permissioncode
     */
    private void requestPermission(final int permissioncode) {
        //根据权限码 得到对应权限
        final String permission = getPermissionString(permissioncode);
        if (!IsEmptyOrNullString(permission)) {
            // Should we show an explanation?
            /**
             *  ActivityCompat.shouldShowRequestPermissionRationale
             *  返回 true：1、以前询问过权限
             *            2、被拒绝
             *            3、且没有勾选“不在提醒”
             *  返回false：1、第一次询问该权限
             *             2、勾选了“不在提醒”
             */
            if (ActivityCompat.shouldShowRequestPermissionRationale(WelcomeActivity.this,
                    permission)) {
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                /**
                 * 根据不同的权限
                 * 弹窗提醒是否向系统申请该权限
                 */
                  if (permissioncode == STORAGE_PERMISSION_CODE) {
                    /*
                    DialogFragment newFragment = HintDialogFragment.newInstance(R.string.storage_description_title,
                            R.string.storage_description_why_we_need_the_permission,
                            permissioncode);
                    newFragment.show(getFragmentManager(), HintDialogFragment.class.getSimpleName());
                    */
                    AlertDialog builder = new AlertDialog.Builder(this)
                            .setTitle(R.string.storage_description_title)
                            .setMessage(R.string.storage_description_why_we_need_the_permission)
                            .setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int whichButton) {
                                    //经过一系列判断 正式向系统申请权限
                                    ActivityCompat.requestPermissions(WelcomeActivity.this, new String[]{permission}, permissioncode);
                                }
                            })
                            .setNegativeButton(R.string.alert_dialog_cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //do nothing
                                }
                            })
                            .create();
                    builder.show();
                }
            } else {
                Log.i("MY", "返回false 不需要解释为啥要权限，可能是第一次请求，也可能是勾选了不再询问");
                /**
                 * 第一次向系统请求
                 * 勾选了"不在询问"，"记住该选项"等，系统自动按照勾选时的选择应答
                 */
                ActivityCompat.requestPermissions(WelcomeActivity.this,new String[]{permission},permissioncode);
            }

        }
    }


    /**
     * 根据权限请求码 解析出 相应权限
     * @param requestCode
     * @return
     */
    private String getPermissionString(int requestCode) {
        String permission = "";
        switch (requestCode) {
            case STORAGE_PERMISSION_CODE:
                permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
                break;
        }
        return permission;
    }

    /**
     * 判断字符串是否为空
     * @param s
     * @return
     */
    public static boolean IsEmptyOrNullString(String s){
        return (s == null) || (s.trim().length() == 0);
    }

    /**
     * 进入主界面
     */
    private void gotoMainActivity() {
        Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);

    }


    /**
     * 授权结果处理
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case STORAGE_PERMISSION_CODE: {
                if (grantResults.length > 0 && PackageManager.PERMISSION_GRANTED==grantResults[0]) {
                    //弹窗提示，并进入主界面
                    Toast.makeText(WelcomeActivity.this, "存储读写权限已经获取", Toast.LENGTH_SHORT).show();
                    gotoMainActivity();
                } else {
                    Toast.makeText(WelcomeActivity.this, "存储读写权限被拒绝", Toast.LENGTH_SHORT).show();
                    //勾选了“不在询问”
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)){
                        AlertDialog builder=new AlertDialog.Builder(this)
                                .setTitle("是否手动设置权限")
                                .setMessage("勾选了不在询问，系统默认拒绝授权，是否手动设置？")
                                .setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //启动系统设置 授权
                                        Intent intent=new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        intent.setData(Uri.parse("package:" + getPackageName()));
                                        startActivity(intent);
                                    }
                                })
                                .setNegativeButton(R.string.alert_dialog_cancel, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //do nothing
                                    }
                                })
                                .create();
                        builder.show();
                    }
                }
            }
        }
    }

    public static int[] getResource() {
        return resource;
    }
}



