package com.zbmf.StockGroup.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.beans.StockComments;

import java.util.List;


public class LatestCommitAdapter extends BaseAdapter{

    private List<StockComments> oneStockCommits;
    private Context cxt;

    public LatestCommitAdapter(Context cxt, List<StockComments> oneStockCommits){
        this.oneStockCommits = oneStockCommits;
        this.cxt = cxt;
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public Object getItem(int position) {
        return oneStockCommits.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();

        } else {
            convertView = LayoutInflater.from(cxt).inflate(R.layout.chat_stock_commit_i, null);
            holder = new ViewHolder();

            convertView.setTag(holder);
        }


        return convertView;
    }

    static class ViewHolder{

    }
}
