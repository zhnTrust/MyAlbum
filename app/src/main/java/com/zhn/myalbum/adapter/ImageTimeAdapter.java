package com.zhn.myalbum.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.zhn.myalbum.utils.DateUtil;
import com.zhn.myalbum.R;
import com.zhn.myalbum.model.TimeImageGroup;
import com.zhn.myalbum.widget.MyGirdView;

public class ImageTimeAdapter extends BaseAdapter {

    /**
     * 回调接口 interface
     */
    private GirdViewIdx mGirdViewIdx=null;
    private OnClickListener mOnClickListener=null;

    /**
     * 上下文对象
     */
    private Context mContext = null;

    /**
     * 对象（日期，图片列表）列表
     */
    private List<TimeImageGroup> mDataList = null;

    /**
     * 容器
     */
    private View mContainer = null;



    public ImageTimeAdapter(Context mContext, List<TimeImageGroup> mDataList, View mContainer) {

        /*移下？！*/

        this.mContext = mContext;
        this.mDataList = mDataList;
        this.mContainer = mContainer;



        //初始化回调接口
        this.mGirdViewIdx=(GirdViewIdx)mContext;
        this.mOnClickListener= (OnClickListener) mContext;
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public TimeImageGroup getItem(int position) {
        if (position < 0 || position > mDataList.size()) {
            return null;
        }
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolderTvItem holder = null;
        if (view == null) {
            holder = new ViewHolderTvItem();
            view = LayoutInflater.from(mContext).inflate(R.layout.list_item_time, null);
            holder.mTextView = view.findViewById(R.id.time_title);
            holder.mTvAddr = view.findViewById(R.id.time_addr);
            holder.mGirdView = view.findViewById(R.id.time_image);
            view.setTag(holder);
        } else {
            holder = (ViewHolderTvItem) view.getTag();
        }


        /**
         * 取出一个对象
         */
        TimeImageGroup item = getItem(i);

        if (item != null) {
            //日期
            String day = item.getDay();
            //获取当前时间 解析出今天
            String today=DateUtil.getDay(DateUtil.formatFromDate(new Date()));
            if (day.equals(today)){
                holder.mTextView.setText(R.string.today);
            }else {
                holder.mTextView.setText(day);
            }

            /**
             * 遍历地址，寻找地址信息
             */
            HashMap<String,String[]> addrs=item.getAddrs();
            if (addrs.isEmpty()){

                holder.mTvAddr.setText("");
            }else {

                /**
                 * 自能用遍历
                 */
                String[] point={"",""};
                for (String key:addrs.keySet()){
                    point=addrs.get(key);
                }

//                holder.mTvAddr.setText(point[0]+","+point[1]);
                holder.mTvAddr.setText(point[2]);

            }

            final ArrayList<String> list = item.getImages();
            MyTimeImageGvItemAdapter adapter = new MyTimeImageGvItemAdapter(mContext,list,holder.mGirdView);
            holder.mGirdView.setAdapter(adapter);

            //网格表项点击事件监听
            holder.mGirdView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    /**
                     * 向activity传递数据
                     */
                    mGirdViewIdx.sendImage(i,list);
                }
            });
            /**
             * 获取地址映射信息
             */
            final HashMap<String, String[]> finalHm = item.getAddrs();


            //地址文本框设置监听
            holder.mTvAddr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /**
                     * 向activity传递数据
                     *
                     */
                    if (finalHm.isEmpty()){
                        mOnClickListener.onClick((TextView) v,null);
                    }else {
                        mOnClickListener.onClick((TextView) v,finalHm);
                    }

                }
            });
        }


        return view;
    }


    static class ViewHolderTvItem {
        public TextView mTextView,mTvAddr;
        public MyGirdView mGirdView;
    }

    /**
     * 自定义回调接口 回调选中图片
     */
    public interface GirdViewIdx{
        public void sendImage(int i,ArrayList<String> images);
    }


    /**
     * 自定义回调接口 回调TextView点击事件
     */
    public interface OnClickListener{
        //int 是 按钮的ID
        public void onClick(TextView textview,HashMap<String,String[]> addrs);
    }


}
