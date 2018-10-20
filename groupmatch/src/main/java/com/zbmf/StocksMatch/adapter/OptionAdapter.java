package com.zbmf.StocksMatch.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.api.Get2Api;
import com.zbmf.StocksMatch.api.Get2ApiImpl;
import com.zbmf.StocksMatch.beans.General;
import com.zbmf.StocksMatch.beans.Quotation;
import com.zbmf.StocksMatch.utils.UiCommon;
import com.zbmf.StocksMatch.widget.LoadingDialog;
import com.zbmf.StocksMatch.widget.SwipeLayout;

import org.json.JSONException;

import java.util.HashSet;

/**
 * 自选股Adapter
 * Created by Administrator on 2016/1/4.
 */
public class OptionAdapter extends ListAdapter<Quotation> {
    private Get2Api server = null;
    public OptionAdapter(Activity context) {
        super(context);
    }

    private HashSet<SwipeLayout> mUnClosedLayouts = new HashSet<SwipeLayout>();

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.option_item, null);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_current = (TextView) convertView.findViewById(R.id.tv_current);
            holder.tv_symbol = (TextView) convertView.findViewById(R.id.tv_symbol);
            holder.tv_change = (TextView) convertView.findViewById(R.id.tv_change);

            holder.sl = (SwipeLayout) convertView.findViewById(R.id.sl);
            holder.tv_del = (TextView) convertView.findViewById(R.id.tv_del);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.sl.close(false, false);

        final Quotation q = mList.get(position);
        holder.tv_name.setText(q.getName());
        holder.tv_symbol.setText(q.getSymbol());
        float close = Float.parseFloat(q.getClose());
        float current = Float.parseFloat(q.getCurrent());
        float percent = (current - close) / close * 100;

        holder.tv_current.setText(String.format("%6.2f", current));
        String str = "";
        if (percent < 0) {
            holder.tv_change.setBackgroundColor(Color.rgb(0, 128, 1));
            str = String.format("%.2f%%", percent);
        } else if (percent >= 0) {
            holder.tv_change.setBackgroundColor(mContext.getResources().getColor(R.color.red));
            str = "+" + String.format("%.2f%%", percent);
        } else {
            holder.tv_change.setBackgroundColor(mContext.getResources().getColor(R.color.gray81));
            str = String.format("%.2f%%", percent);
        }
        holder.tv_change.setText(str);
        holder.sl.setSwipeListener(mSwipeListener);
        holder.sl.getFrontView().setOnClickListener(new View.OnClickListener() {//条目删除
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("quotation", q);
                UiCommon.INSTANCE.showActivity(UiCommon.ACTIVITY_IDX_STOCKDETAIL, bundle);
            }
        });

        holder.tv_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("tag----","q.getSymbol()="+q.getSymbol());
                new DefocusTask(mContext).execute(q.getSymbol());
            }
        });
        return convertView;
    }


    static class ViewHolder {
        TextView tv_name, tv_current, tv_symbol, tv_change,tv_del;
        SwipeLayout sl;
    }


    SwipeLayout.SwipeListener mSwipeListener = new SwipeLayout.SwipeListener() {
        @Override
        public void onOpen(SwipeLayout swipeLayout) {
            mUnClosedLayouts.add(swipeLayout);
        }

        @Override
        public void onClose(SwipeLayout swipeLayout) {
            mUnClosedLayouts.remove(swipeLayout);
        }

        @Override
        public void onStartClose(SwipeLayout swipeLayout) {
        }

        @Override
        public void onStartOpen(SwipeLayout swipeLayout) {
            closeAllLayout();
            mUnClosedLayouts.add(swipeLayout);
        }

    };

    public void closeAllLayout() {
        if (mUnClosedLayouts.size() == 0)
            return;

        for (SwipeLayout l : mUnClosedLayouts) {
            l.close(true, false);
        }
        mUnClosedLayouts.clear();
    }

    private class DefocusTask extends LoadingDialog<String,General> {
        private String symbol;
        public DefocusTask(Context activity) {
            super(activity, false,true);
        }

        @Override
        public General doInBackground(String... params) {
            symbol = params[0];
            General temp = null;
            if(server == null){
                server = new Get2ApiImpl();
            }
            try {
                temp=  server.defocus(symbol);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return temp;
        }

        @Override
        public void doStuffWithResult(General result) {
            if (result != null && result.code != -1) {
                if (result.getStatus()==1) {
                    for (int i = 0; i < mList.size(); i++) {
                        if(symbol.equals(mList.get(i).getSymbol())){
                            mList.remove(i);
                            break;
                        }
                    }
                    notifyDataSetChanged();
                    UiCommon.INSTANCE.showTip(mContext.getString(R.string.option_del));
                }else {
                    UiCommon.INSTANCE.showTip(result.msg);
                }
            }else{
                UiCommon.INSTANCE.showTip(mContext.getString(R.string.load_fail));
            }
        }
    }

}
