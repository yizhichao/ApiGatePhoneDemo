package com.hik.apigatephonedemo;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hik.apigatephonedemo.bean.ControlUnit;

import java.util.List;

/**
 * Created by wangkuilin on 2017/9/22.
 */

public class ControlUnitAdapter extends BaseAdapter {

    private static String TAG = "ControlUnitAdapter";
    private Context mContext;

    public void setList(List<ControlUnit> mList) {
        this.mList = mList;
    }

    private List<ControlUnit> mList;

    public ControlUnitAdapter(Context context, List<ControlUnit> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public int getCount() {
        if (null == mList){
            return 0;
        }

        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        if (null == mList || position < 0 || position >= mList.size()){
            return 0;
        }

        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == mList || position < 0 || position >= mList.size()){
            return null;
        }

        View view;
        ViewHolder viewHolder;
        if (null == convertView){
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.controlunit_item, parent, false);
            viewHolder.textName = (TextView) view.findViewById(R.id.textItem);
            view.setTag(viewHolder);
        }else {
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();
        }

        ControlUnit controlUnit = mList.get(position);
        String strPrefix = "";                                       // 前缀
        for(int i=0; i<controlUnit.getUnitLevel(); i++){
            strPrefix += "  ";
        }

        viewHolder.textName.setText(strPrefix + controlUnit.getName());
        // 组织黑色，区域红色
        if (controlUnit.getUnitType() == ControlUnit.UNIT_TYPE_QUYU){
            viewHolder.textName.setTextColor(Color.RED);
        }else  if (controlUnit.getUnitType() == ControlUnit.UNIT_TYPE_ZUZHI){
            viewHolder.textName.setTextColor(Color.GRAY);
        }else {
            viewHolder.textName.setTextColor(Color.BLUE);
        }

        return view;
    }

    class ViewHolder{
        TextView textName;
    }
}
