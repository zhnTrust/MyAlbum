package com.zhn.myalbum.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;

import java.util.ArrayList;

import com.zhn.myalbum.loader.LocalImageLoader;
import com.zhn.myalbum.widget.MyImageView;
import com.zhn.myalbum.R;

public class MyTimeImageGvItemAdapter extends BaseAdapter {

    /**
     * 上下文对象
     */
    private Context mContext = null;

    /**
     * 图片(String)列表
     */
    private ArrayList<String> mDataList = null;

    /**
     * 容器
     */
    private View mContainer = null;

    public MyTimeImageGvItemAdapter(Context mContext, ArrayList<String> mDataList, View mContainer) {
        this.mContext = mContext;
        this.mDataList = mDataList;
        this.mContainer = mContainer;
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public String getItem(int i) {
        if (i < 0 || i > mDataList.size()) {
            return null;
        }
        return mDataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolderGvItem viewHolderGvItem=null;
        if (view==null){
            viewHolderGvItem=new ViewHolderGvItem();
            view=LayoutInflater.from(mContext).inflate(R.layout.image_list_item,null);
            viewHolderGvItem.mImageIv=view.findViewById(R.id.list_item_iv);
            viewHolderGvItem.mCheckBox=view.findViewById(R.id.list_item_cb);
            viewHolderGvItem.mCheckBox.setVisibility(View.GONE);
            view.setTag(viewHolderGvItem);
        }else {
            viewHolderGvItem =(ViewHolderGvItem)view.getTag();
        }
        String path=getItem(i);
        // 加载图片
        viewHolderGvItem.mImageIv.setTag(path);
        // 利用NativeImageLoader类加载本地图片
        Bitmap bitmap = LocalImageLoader.getInstance().loadImage(path,
                viewHolderGvItem.mImageIv.getPoint(), new LocalImageLoader.ImageCallBack() {
                    @Override
                    public void onImageLoader(Bitmap bitmap, String path) {
                        ImageView mImageView = (ImageView)mContainer.findViewWithTag(path);
                        if (bitmap != null && mImageView != null) {
                            mImageView.setImageBitmap(bitmap);
                        }
                    }
                });
        if (bitmap != null) {
            viewHolderGvItem.mImageIv.setImageBitmap(bitmap);
        } else {
            viewHolderGvItem.mImageIv.setImageResource(R.drawable.pic_thumb);
        }

        return view;

    }


    static class ViewHolderGvItem{
        public MyImageView mImageIv;
        public CheckBox mCheckBox;
    }
}
