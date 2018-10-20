package com.zbmf.StocksMatch.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.api.Get2Api;
import com.zbmf.StocksMatch.api.Get2ApiImpl;
import com.zbmf.StocksMatch.beans.General;
import com.zbmf.StocksMatch.beans.Quotation;
import com.zbmf.StocksMatch.beans.Stock;
import com.zbmf.StocksMatch.utils.UiCommon;
import com.zbmf.StocksMatch.widget.LoadingDialog;
import com.zbmf.StocksMatch.widget.SwipeLayout;

import org.json.JSONException;

import java.util.HashSet;
import java.util.List;

/**
 * 行情所以数据adapter
 * Created by Administrator on 2016/1/5.
 */
public class QuatationAdapter extends BaseExpandableListAdapter {

    private List<List<Quotation>> lists = null;
    private Context cxt;
    private LayoutInflater mInflater = null;
    private String[] groups_name;
    private HashSet<SwipeLayout> mUnClosedLayouts = new HashSet<SwipeLayout>();

    public QuatationAdapter(Context cxt, List<List<Quotation>> lists){
        this.cxt = cxt;
        this.lists = lists;
        mInflater = (LayoutInflater) cxt.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        groups_name = cxt.getResources().getStringArray(R.array.groups);
    }

    @Override
    public int getGroupCount() {
        return lists.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return lists.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return lists.get(groupPosition);
    }

    @Override
    public Quotation getChild(int groupPosition, int childPosition) {
        return lists.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupHolder holder = null;
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.group_item,null);
            holder = new GroupHolder();
            holder.iv_arrow = (ImageView) convertView.findViewById(R.id.iv_arrow);
            holder.iv_add = (ImageView) convertView.findViewById(R.id.iv_add);
            holder.tv_groupname = (TextView) convertView.findViewById(R.id.tv_groupname);
            convertView.setTag(holder);
        }else{
            holder = (GroupHolder) convertView.getTag();
        }

        if(groupPosition == 0){
            holder.iv_add.setVisibility(View.VISIBLE);
            holder.iv_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("from", Constants.FROM_QUOTATION);
//                    UiCommon.INSTANCE.showActivity(UiCommon.ACTIVITY_IDX_MATCHSEARCH,bundle); //跳转到搜索
                    UiCommon.INSTANCE.showActivity(UiCommon.ACTIVITY_IDX_SELSTOCK,null);//跳转到股票搜索
                }
            });
            if(getChildrenCount(0) == 0){
//                UiCommon.INSTANCE.showTip("无自选股，请添加！");
            }
        }else
            holder.iv_add.setVisibility(View.GONE);


        if(isExpanded)
            holder.iv_arrow.setImageResource(R.drawable.a_icon_bottom);
        else
            holder.iv_arrow.setImageResource(R.drawable.a_icon_top);

        holder.tv_groupname.setText(groups_name[groupPosition]);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final Quotation q = getChild(groupPosition,childPosition);
        if(groupPosition == lists.size()-1){
            convertView = mInflater.inflate(R.layout.option_item1,null);
            ViewHolder holder = new ViewHolder();
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_buy = (TextView) convertView.findViewById(R.id.tv_buy);
            holder.tv_sell = (TextView) convertView.findViewById(R.id.tv_sell);
            holder.tv_middle = (TextView) convertView.findViewById(R.id.tv_middle);
            holder.ll_top = (LinearLayout)convertView.findViewById(R.id.ll_top);
            if(childPosition ==0)
                holder.ll_top.setVisibility(View.VISIBLE);
            else
                holder.ll_top.setVisibility(View.GONE);
            holder.tv_buy.setText(q.getBuy());
            holder.tv_sell.setText(q.getSell());
            holder.tv_middle.setText(q.getMidprice());
            holder.tv_name.setText(q.getName());

        }else{
            ViewHolder holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.option_item,null);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_current = (TextView) convertView.findViewById(R.id.tv_current);
            holder.tv_symbol = (TextView) convertView.findViewById(R.id.tv_symbol);
            holder.tv_change = (TextView) convertView.findViewById(R.id.tv_change);
            holder.sl = (SwipeLayout)convertView.findViewById(R.id.sl);
            holder.tv_del = (TextView)convertView.findViewById(R.id.tv_del);
            holder.tv_name.setText(q.getName());
            float close = Float.parseFloat(q.getClose());
            float current = Float.parseFloat(q.getCurrent());
            float percent = (current - close) / close * 100;

            holder.tv_current.setText(String.format("%6.2f", current));
            holder.tv_symbol.setText(q.getSymbol());
            String str = "";
            if (percent < 0) {
                holder.tv_change.setBackgroundColor(Color.rgb(0, 128, 1));
                str = String.format("%.2f%%", percent);
            } else if (percent >= 0) {
                holder.tv_change.setBackgroundColor(cxt.getResources().getColor(R.color.red));
                str = "+" + String.format("%.2f%%", percent);
            } else {
                holder.tv_change.setBackgroundColor(cxt.getResources().getColor(R.color.gray81));
                str = String.format("%.2f%%", percent);
            }
            holder.tv_change.setText(str);

            if(groupPosition==0){
                holder.sl.setIsScrolling(true);
                holder.tv_symbol.setVisibility(View.VISIBLE);
                holder.sl.setSwipeListener(mSwipeListener);
                holder.sl.getFrontView().setOnClickListener(new View.OnClickListener() {//条目删除
                    @Override
                    public void onClick(View v) {
//                        Bundle bundle = new Bundle();
//                        bundle.putSerializable("quotation", q);
//                        UiCommon.INSTANCE.showActivity(UiCommon.ACTIVITY_IDX_STOCKDETAIL, bundle);
                        closeAllLayout();
                        new GetStockRealtimeInfoTask(cxt,R.string.stock_detail_getting,R.string.load_fail,true).execute(q.getSymbol());
                    }
                });

                holder.tv_del.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("tag----","q.getSymbol()="+q.getSymbol());
                        new DefocusTask(cxt).execute(q.getSymbol());
                    }
                });

            }else{
                holder.sl.getFrontView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        closeAllLayout();
                    }
                });
                holder.tv_symbol.setVisibility(View.GONE);
                holder.sl.setIsScrolling(false);
            }

        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    static class GroupHolder{
        ImageView iv_arrow,iv_add;
        TextView tv_groupname;
    }

    static class ViewHolder {
        TextView tv_name, tv_current, tv_symbol, tv_change,tv_middle,tv_buy,tv_sell,tv_del;
        LinearLayout ll_top;
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

    private Get2Api server;
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
                    List<Quotation> mList = (List<Quotation>) getGroup(0);
                    for (int i = 0; i < mList.size(); i++) {
                        if(symbol.equals(mList.get(i).getSymbol())){
                            mList.remove(i);
                            break;
                        }
                    }
                    notifyDataSetChanged();
                    UiCommon.INSTANCE.showTip(cxt.getString(R.string.option_del));
                }else {
                    UiCommon.INSTANCE.showTip(result.msg);
                }
            }else{
                UiCommon.INSTANCE.showTip(cxt.getString(R.string.load_fail));
            }
        }
    }

    private class GetStockRealtimeInfoTask extends LoadingDialog<String,Stock> {

        public GetStockRealtimeInfoTask(Context activity, int loadingMsg, int failMsg, boolean Enddismiss) {
            super(activity, loadingMsg, failMsg, Enddismiss);
        }

        @Override
        public Stock doInBackground(String... params) {
            if(server == null){
                server = new Get2ApiImpl();
            }

            try {
                return  server.getStockRealtimeInfo(params[0]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void doStuffWithResult(Stock result) {
            if (result != null && result.code != -1) {
                if (result.getStatus()==1) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("stock", result);
                    UiCommon.INSTANCE.showActivity(UiCommon.ACTIVITY_IDX_STOCKDETAIL, bundle);
                } else {
                    UiCommon.INSTANCE.showTip(result.msg);
                }
            }else {
                UiCommon.INSTANCE.showTip(cxt.getString(R.string.load_fail));
            }
        }
    }

}
