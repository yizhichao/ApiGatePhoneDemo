package com.hik.apigatephonedemo;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hik.apigatephonedemo.bean.RecordingInfo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 【说明】：
 *
 * @author zhangchuanyi
 * @data 2017/11/27 19:36
 */

public class RecordingAdapter extends BaseAdapter {


    private static String TAG = "RecordingAdapter";
    private Context mContext;
    private List<RecordingInfo> mList;

    public void setList(List<RecordingInfo> mList) {
        this.mList = mList;
    }

    public RecordingAdapter(Context context,List<RecordingInfo> list){
        mContext = context;
        mList = list;
    }
    @Override
    public int getCount() {
        if (mList == null) {
            return 0;
        }else {
            return mList.size();
        }
    }

    @Override
    public RecordingInfo getItem(int position) {
        if (null == mList || position < 0 || position >= mList.size()){
            return null;
        }
        return mList.get(position);    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=new ViewHolder();
        if (convertView == null) {
            convertView=View.inflate(mContext,R.layout.recording_item,null);
            viewHolder.beginTime= (TextView) convertView.findViewById(R.id.beginTime);
            viewHolder.endTime= (TextView) convertView.findViewById(R.id.endTime);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        RecordingInfo recordingInfo=mList.get(position);
        viewHolder.beginTime.setText(formatTime(recordingInfo.getBeginTime()));
        viewHolder.endTime.setText(formatTime(recordingInfo.getEndTime()));
        return convertView;
    }

    class ViewHolder {
        private TextView beginTime;
        private TextView endTime;
    }

    public String formatTime(long time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(time);
        return simpleDateFormat.format(date);
    }
}
