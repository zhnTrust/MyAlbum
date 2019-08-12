package com.zhn.myalbum.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * TAG_APERTURE：光圈值。
 * TAG_DATETIME：拍摄时间，取决于设备设置的时间。
 * TAG_EXPOSURE_TIME：曝光时间。
 * TAG_FLASH：闪光灯。
 * TAG_FOCAL_LENGTH：焦距。
 * TAG_IMAGE_LENGTH：图片高度。
 * TAG_IMAGE_WIDTH：图片宽度。
 * TAG_ISO：ISO。
 * TAG_MAKE：设备品牌。
 * TAG_MODEL：设备型号，整形表示，在ExifInterface中有常量对应表示。
 * TAG_ORIENTATION：旋转角度，整形表示，在ExifInterface中有常量对应表示。
 */
public class ImageInfo implements Parcelable {
    private String name;
    private String TAG_APERTURE,
            TAG_DATETIME,
            TAG_EXPOSURE_TIME,
            TAG_FOCAL_LENGTH,
            TAG_IMAGE_LENGTH,
            TAG_IMAGE_WIDTH,
            TAG_ISO;
    private long size;


    public ImageInfo() {
    }

    protected ImageInfo(Parcel in) {
        name = in.readString();
        TAG_APERTURE = in.readString();
        TAG_DATETIME = in.readString();
        TAG_EXPOSURE_TIME = in.readString();
        TAG_FOCAL_LENGTH = in.readString();
        TAG_IMAGE_LENGTH = in.readString();
        TAG_IMAGE_WIDTH = in.readString();
        TAG_ISO = in.readString();
        size = in.readLong();
    }

    public static final Creator<ImageInfo> CREATOR = new Creator<ImageInfo>() {
        @Override
        public ImageInfo createFromParcel(Parcel in) {
            return new ImageInfo(in);
        }

        @Override
        public ImageInfo[] newArray(int size) {
            return new ImageInfo[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTAG_APERTURE() {
        return TAG_APERTURE;
    }

    public void setTAG_APERTURE(String TAG_APERTURE) {
        this.TAG_APERTURE = TAG_APERTURE;
    }

    public String getTAG_DATETIME() {
        return TAG_DATETIME;
    }

    public void setTAG_DATETIME(String TAG_DATETIME) {
        this.TAG_DATETIME = TAG_DATETIME;
    }

    public String getTAG_EXPOSURE_TIME() {
        return TAG_EXPOSURE_TIME;
    }

    public void setTAG_EXPOSURE_TIME(String TAG_EXPOSURE_TIME) {
        this.TAG_EXPOSURE_TIME = TAG_EXPOSURE_TIME;
    }

    public String getTAG_FOCAL_LENGTH() {
        return TAG_FOCAL_LENGTH;
    }

    public void setTAG_FOCAL_LENGTH(String TAG_FOCAL_LENGTH) {
        this.TAG_FOCAL_LENGTH = TAG_FOCAL_LENGTH;
    }

    public String getTAG_IMAGE_LENGTH() {
        return TAG_IMAGE_LENGTH;
    }

    public void setTAG_IMAGE_LENGTH(String TAG_IMAGE_LENGTH) {
        this.TAG_IMAGE_LENGTH = TAG_IMAGE_LENGTH;
    }

    public String getTAG_IMAGE_WIDTH() {
        return TAG_IMAGE_WIDTH;
    }

    public void setTAG_IMAGE_WIDTH(String TAG_IMAGE_WIDTH) {
        this.TAG_IMAGE_WIDTH = TAG_IMAGE_WIDTH;
    }

    public String getTAG_ISO() {
        return TAG_ISO;
    }

    public void setTAG_ISO(String TAG_ISO) {
        this.TAG_ISO = TAG_ISO;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "ImageInfo{" +
                "name='" + name + '\'' +
                ", TAG_APERTURE='" + TAG_APERTURE + '\'' +
                ", TAG_DATETIME='" + TAG_DATETIME + '\'' +
                ", TAG_EXPOSURE_TIME='" + TAG_EXPOSURE_TIME + '\'' +
                ", TAG_FOCAL_LENGTH='" + TAG_FOCAL_LENGTH + '\'' +
                ", TAG_IMAGE_LENGTH='" + TAG_IMAGE_LENGTH + '\'' +
                ", TAG_IMAGE_WIDTH='" + TAG_IMAGE_WIDTH + '\'' +
                ", TAG_ISO='" + TAG_ISO + '\'' +
                ", size=" + size +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(TAG_APERTURE);
        parcel.writeString(TAG_DATETIME);
        parcel.writeString(TAG_EXPOSURE_TIME);
        parcel.writeString(TAG_FOCAL_LENGTH);
        parcel.writeString(TAG_IMAGE_LENGTH);
        parcel.writeString(TAG_IMAGE_WIDTH);
        parcel.writeString(TAG_ISO);
        parcel.writeLong(size);
    }
}
