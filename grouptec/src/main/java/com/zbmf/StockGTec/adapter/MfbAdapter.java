package com.zbmf.StockGTec.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zbmf.StockGTec.R;
import com.zbmf.StockGTec.beans.MFBean;
import com.zbmf.StockGTec.view.RoundedCornerImageView;

import java.util.List;


/**
 * Created by xuhao on 2017/2/20.
 */

public class MfbAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<MFBean> infolist;

    public MfbAdapter(Context context, List<MFBean> info) {
        this.inflater = LayoutInflater.from(context);
        this.infolist = info;
    }

    @Override
    public int getCount() {
        return infolist.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        mfbItem item = null;
        if (view == null) {
            view = inflater.inflate(R.layout.mfb_item_layout, null);
            item = new mfbItem(view);
            view.setTag(item);
        } else {
            item = (mfbItem) view.getTag();
        }
        MFBean pb = infolist.get(i);
        String title = pb.getTitle();
        int color = Color.parseColor("#ff8989");
        item.title.setText(title.substring(0, 2));
        item.date.setText(pb.getDate().substring(5, 10) + "\n" + pb.getDate().substring(11, 16));
        item.count.setText(pb.getCount());
        item.desc.setText(pb.getDesc());
        if (title.contains("伯乐"))
            color = Color.parseColor("#ff8989");
        else if (title.contains("使用"))
            color = Color.parseColor("#f3ad6f");
        else if (title.contains("支付"))
            color = Color.parseColor("#7db6f1");
        else if (title.contains("获得"))
            color = Color.parseColor("#c28dce");
        else if (title.contains("充值"))
            color = Color.parseColor("#cc6c6c");
        else if (title.contains("兑换"))
            color = Color.parseColor("#9b8989");
        else if (title.contains("赠送"))
            color = Color.parseColor("#55587c");
        else if (title.contains("退款"))
            color = Color.parseColor("#e86477");
        else
            color = Color.parseColor("#b7b7b7");

        item.rci.setBackgroundColor(color);
        return view;
    }

    private class mfbItem {
        private TextView title, date, count, desc;
        private RoundedCornerImageView rci;

        public mfbItem(View view) {
            this.count = (TextView) view.findViewById(R.id.item_mfb_count);
            this.date = (TextView) view.findViewById(R.id.item_mfb_date);
            this.title = (TextView) view.findViewById(R.id.item_mfb_title);
            this.desc = (TextView) view.findViewById(R.id.item_mfb_desc);
            this.rci = (RoundedCornerImageView) view.findViewById(R.id.rci);
        }
    }
}
