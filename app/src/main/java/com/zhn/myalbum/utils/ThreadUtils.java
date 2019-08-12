package com.zhn.myalbum.utils;

import android.os.Handler;

public class ThreadUtils {
    //    启动普通线程
    public static void runInThread(Runnable task){
        new Thread(task).start();
    }
    //    初始化一个Handler对象
    public static Handler mhandler=new Handler();
    //    启动UI线程
    public static void runInUIThread(Runnable task){
        mhandler.post(task);
    }

}
