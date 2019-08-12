package com.zhn.myalbum.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import com.zhn.myalbum.loader.LocalImageLoader;
import com.zhn.myalbum.utils.Util;
import com.zhn.myalbum.widget.MyImageView;
import com.zhn.myalbum.R;

/**
 * 幻灯片播放的adapter
 */
public class ImageFlashAdapter extends BaseAdapter {

    /**
     * 上下文对象
     */
    private Context mContext = null;

    /**
     * 图片列表
     */
    private ArrayList<String> mDataList = new ArrayList<String>();

    /**
     * 选中的图片列表
     */
    private ArrayList<String> mSelectedList = new ArrayList<String>();


    public ImageFlashAdapter(Context context, ArrayList<String> list) {
        mDataList = list;
        mContext = context;
        mSelectedList = Util.getSeletedImages(context);
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public String getItem(int position) {
        if (position < 0 || position > mDataList.size()) {
            return null;
        }
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.image_flash, null);
            holder.mImageView = view.findViewById(R.id.flash_cell);
            view.setTag(holder);
        } else {
            holder = (ViewHolder)view.getTag();
        }
        // 加载图片
        final String path =  getItem(position);
        holder.mImageView.setTag(path);
        // 加载图片
        // 利用NativeImageLoader类加载本地图片
        Bitmap bitmap = LocalImageLoader.getInstance().loadImage(path, holder.mImageView.getPoint(),
                LocalImageLoader.getImageListener(holder.mImageView, path, R.drawable.pic_thumb, R.drawable.pic_thumb));
        if (bitmap != null) {
            holder.mImageView.setImageBitmap(bitmap);
        } else {
            holder.mImageView.setImageResource(R.drawable.pic_thumb);
        }
        return view;


    }
    static class ViewHolder {
        public MyImageView mImageView;


    }
}
