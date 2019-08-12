/**
 * ImageLoadTask.java
 * ImageSelector
 * 
 * Created by likebamboo on 2014-4-22
 * Copyright (c) 1998-2014 http://likebamboo.github.io/ All rights reserved.
 */

package com.zhn.myalbum.task;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore.Images.Media;

import com.zhn.myalbum.log.L;
import com.zhn.myalbum.model.ImageGroup;
import com.zhn.myalbum.model.ImageInfo;
import com.zhn.myalbum.listener.OnTaskResultListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 使用contentProvider扫描图片异步任务
 * 
 * @author likebamboo
 */
public class ImageLoadTask extends BaseTask {

    /**
     * 上下文对象
     */
    private Context mContext = null;

    /**
     * 存放图片<文件夹,该文件夹下的图片列表>键值
     * 对
     * 图片组对象的列表
     */
    private ArrayList<ImageGroup> mGruopList = new ArrayList<ImageGroup>();

    public ImageLoadTask(Context context) {
        super();
        mContext = context;
        result = mGruopList;
    }

    public ImageLoadTask(Context context, OnTaskResultListener listener) {
        super();
        mContext = context;
        result = mGruopList;
        setOnResultListener(listener);
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
        selection.append(Media.MIME_TYPE).append("=?");
        selection.append(" or ");
        selection.append(Media.MIME_TYPE).append("=?");

        Cursor mCursor = null;
        try {
            // 初始化游标
            mCursor = mContentResolver.query(mImageUri, null, selection.toString(), new String[] {
                    "image/jpeg", "image/png"
            }, Media.DATE_TAKEN);
            // 遍历结果
            while (mCursor.moveToNext()) {
                // 获取图片的路径
                String path = mCursor.getString(mCursor.getColumnIndex(Media.DATA));

                //获取图片其他信息
                ImageInfo imageInfo=setImageInfo(path);

                //创建文件对象
                File file = new File(path);
                // 获取该图片的所在文件夹的路径
                String parentName = "";
                if (file.getParentFile() != null) {
                    parentName = file.getParentFile().getName();
                } else {
                    parentName = file.getName();
                }
                // 构建一个imageGroup对象
                ImageGroup item = new ImageGroup();
                // 设置imageGroup的文件夹名称
                item.setDirName(parentName);

                // 文件夹名相同的imageGroup对象在mGroup是否已经存在
                int searchIdx = mGruopList.indexOf(item);
                if (searchIdx >= 0) {   //存在
                    // 如果是，该组的图片数量+1
                    ImageGroup imageGroup = mGruopList.get(searchIdx);
                    //添加路径和信息
                    imageGroup.addImage(path);
                    imageGroup.addImageInfo(imageInfo);
                } else {
                    // 否则，将该对象加入到mGroupList中
                    // 添加路径和信息
                    item.addImage(path);
                    item.addImageInfo(imageInfo);
                    mGruopList.add(item);
                }
            }
        } catch (Exception e) {
            // 输出日志
            L.e(e);
            return false;
        } finally {
            // 关闭游标
            if (mCursor != null && !mCursor.isClosed()) {
                mCursor.close();
            }
        }
        return true;
    }

    /**
     *
     * @param path
     * @return
     * @throws IOException
     */
    public ImageInfo setImageInfo(String path) throws IOException {
        //根据路径获取图片图片Exif信息
        ExifInterface exifInterface=new ExifInterface(path);
        ImageInfo imageInfo=new ImageInfo();
        //文件名
        int start=path.lastIndexOf("/");
        int end=path.lastIndexOf(".");
        if (start!=-1 && end!=-1) {
            String name=path.substring(start+1, end);
            imageInfo.setName(name);
        }
        //时间
        if (exifInterface.getAttribute(ExifInterface.TAG_DATETIME)!=null){
            imageInfo.setTAG_DATETIME(exifInterface.getAttribute(ExifInterface.TAG_DATETIME));
        }
        //宽度
        String image_width = exifInterface.getAttribute(ExifInterface.TAG_IMAGE_WIDTH);
        if (null!=image_width){
            imageInfo.setTAG_IMAGE_WIDTH(image_width);
        }
        //高度
        String image_length = exifInterface.getAttribute(ExifInterface.TAG_IMAGE_LENGTH);
        if (null!= image_length){
            imageInfo.setTAG_IMAGE_LENGTH(image_length);
        }
        //大小 byte
        File file=new File(path);
        if (file.exists() && file.isFile()){
            long size=file.length();
            imageInfo.setSize(size);
        }
        //焦距
        String focal_length = exifInterface.getAttribute(ExifInterface.TAG_FOCAL_LENGTH);
        if (null!= focal_length){
            imageInfo.setTAG_FOCAL_LENGTH(focal_length);
        }
        //曝光时间
        String exposure_time = exifInterface.getAttribute(ExifInterface.TAG_EXPOSURE_TIME);
        if (null!= exposure_time){
            imageInfo.setTAG_EXPOSURE_TIME(exposure_time);
        }
        //iso
        String iso = exifInterface.getAttribute(ExifInterface.TAG_ISO);
        if (null!= iso){
            imageInfo.setTAG_ISO(iso);
        }
        //光圈
        String aperture = exifInterface.getAttribute(ExifInterface.TAG_APERTURE);
        if (null!= aperture){
            imageInfo.setTAG_APERTURE(aperture);
        }
        return imageInfo;
    }
}
