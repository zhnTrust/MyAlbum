package com.zhn.myalbum.model;

import java.util.ArrayList;
import java.util.HashMap;

public class TimeImageGroup extends BaseModel {
    /**
     * 日期（具体到天）、第一个出现的地点
     */
    private String[] addr  = new String[]{};
    private String day = "";
    /**
     * 该天下所有图片（地址）列表
     */
    private ArrayList<String> images = new ArrayList<String>();

    /**
     * 地址对应的地点map
     */
    private HashMap<String, String[]> addrs = new HashMap<>();

    public TimeImageGroup() {
    }

    public TimeImageGroup(String day, String[] addr, ArrayList<String> images, HashMap<String, String[]> addrs) {
        this.day = day;
        this.addr = addr;
        this.images = images;
        this.addrs = addrs;
    }


    public String[] getAddr() {
        return addr;
    }

    public void setAddr(String[] addr) {
        this.addr = addr;
    }

    public HashMap<String, String[]> getAddrs() {
        return addrs;
    }


    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public ArrayList<String> getImages() {
        return images;
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
     * 添加地址映射
     */
    public void addAddr(String image, String[] point) {
        if (addrs == null) {
            addrs = new HashMap<>();
        }
        addrs.put(image, point);

    }


    /**
     * <p>
     * 重写该方法
     * <p>
     * 使只要图片所在天（day）相同就属于同一个图片组
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof TimeImageGroup)) {
            return false;
        }
        return day.equals(((TimeImageGroup) o).day);
    }

    @Override
    public String toString() {
        return "TimeImageGroup{" +
                "day='" + day + '\'' +
                ", images=" + images +
                '}';
    }
}
