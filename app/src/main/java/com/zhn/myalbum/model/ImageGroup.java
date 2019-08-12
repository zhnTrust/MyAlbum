/**
 * ImageGroup.java
 * ImageChooser
 * 
 * Created by likebamboo on 2014-4-22
 * Copyright (c) 1998-2014 http://likebamboo.github.io/ All rights reserved.
 */

package com.zhn.myalbum.model;

import java.util.ArrayList;

/**
 * 一级GridView中每个item的数据模型
 * 
 * @author likebamboo
 */
public class ImageGroup extends BaseModel {
    /**
     * 文件夹名
     */
    private String dirName = "";

    /**
     * 文件夹下所有图片（地址）
     */
    private ArrayList<String> images = new ArrayList<String>();

    /**
     * 图片对应相关信息
     */
    private ArrayList<ImageInfo> imageInfos=new ArrayList<>();

    /**
     *
     * getter & setter
     */

    public String getDirName() {
        return dirName;
    }

    public void setDirName(String dirName) {
        this.dirName = dirName;
    }

    public ArrayList<ImageInfo> getImageInfos() {
        return imageInfos;
    }

    /**
     * 获取第一张图片的路径(作为封面)
     * 
     * @return
     */
    public String getFirstImgPath() {
        if (images.size() > 0) {
            return images.get(0);
        }
        return "";
    }

    /**
     * 获取图片数量
     * 
     * @return
     */
    public int getImageCount() {
        return images.size();
    }



    /**
     * 添加一张图片
     * 
     * @param image
     */
    public void addImage(String image) {
        if (images == null) {
            images = new ArrayList<String>();
        }
        images.add(image);
    }

    /**
     * 添加对应图片的信息
     * @param imageInfo
     */
    public void addImageInfo(ImageInfo imageInfo){
        if (imageInfos==null){
            imageInfos=new ArrayList<>();
        }
        imageInfos.add(imageInfo);
    }

    public ArrayList<String> getImages() {
        return images;
    }

    @Override
    public String toString() {
        return "ImageGroup [firstImgPath=" + getFirstImgPath() + ", dirName=" + dirName
                + ", imageCount=" + getImageCount() + "]";
    }

    /**
     * <p>
     * 重写该方法
     * <p>
     * 使只要图片所在的文件夹名称(dirName)相同就属于同一个图片组
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ImageGroup)) {
            return false;
        }
        return dirName.equals(((ImageGroup)o).dirName);
    }
}
