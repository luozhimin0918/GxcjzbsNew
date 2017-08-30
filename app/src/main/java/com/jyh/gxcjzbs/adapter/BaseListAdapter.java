package com.jyh.gxcjzbs.adapter;

import android.widget.BaseAdapter;

import java.util.List;


public abstract class BaseListAdapter<T> extends BaseAdapter {

    public List<T> dataList;

    public BaseListAdapter(List<T> dataList) {
        this.dataList = dataList;
    }



    @Override
    public int getCount() {
        return dataList == null ? 0 : dataList.size();
    }

    @Override
    public T getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
