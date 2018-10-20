package com.zbmf.StockGroup.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.beans.NuggetsStockBean0;
import com.zbmf.StockGroup.beans.StockMode;
import com.zbmf.StockGroup.view.ListViewForScrollView;
import com.zbmf.StockGroup.view.SyncHorizontalScrollView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by pq
 * on 2018/5/25.
 * 模型选股掘金的adapter
 */

public class NuggetsRecyclerAdapter extends RecyclerView.Adapter<NuggetsRecyclerAdapter.NuggetsHolder> {
    private Activity mActivity;
    private StockModeAdapter mContentAdapter;
    private StockModeNameAdapter mNameAdapter;
    private SyncHorizontalScrollView mContent_titleScrollView;
    private int mFlag;
    private String[] h1 = {"10:30", "11:00", "11:30", "13:30", "14:00"};
    private List<String> nuggetsClockTv = new ArrayList<>();

    //设置数据
    private List<NuggetsStockBean0> mNuggetsStockBean0s;

    public void setDataList(List<NuggetsStockBean0> datas, int mFlag) {
        List<NuggetsStockBean0> da1 = new ArrayList<>();
        List<NuggetsStockBean0> da2 = new ArrayList<>();
        int n = 0;
        int m = 0;
        if (datas != null && datas.size() > 0) {
            mNuggetsStockBean0s = datas;
            if (mFlag == 1) {
                if (datas.size() < 10) {
                    String toDayV = datas.get(0).getToDayV();
                    String toDayV2 = datas.get(datas.size() - 1).getToDayV();
                    da1.clear();
                    da2.clear();
                    for (int i = 0; i < datas.size(); i++) {
                        String toDayV1 = datas.get(i).getToDayV();
                        if (toDayV.equals(toDayV1)) {
                            da1.add(datas.get(i));
                            n += 1;
                        }
                        if (toDayV1.equals(toDayV2)) {
                            da2.add(datas.get(i));
                            m += 1;
                        }
                    }
                    //此时的n数量表示当天已有的数据
                    if (n < 5) {
                        int i = 5 - n;
                        for (int i1 = 0; i1 < i; i1++) {
                            NuggetsStockBean0 nuggetsStockBean0 = new NuggetsStockBean0();
                            nuggetsStockBean0.setToDayV(toDayV);
                            da1.add(nuggetsStockBean0);
                        }
                    }
                    if (m < 5) {
                        int i = 5 - m;

                        for (int i1 = 0; i1 < i; i1++) {
                            NuggetsStockBean0 nuggetsStockBean0 = new NuggetsStockBean0();
                            nuggetsStockBean0.setToDayV(toDayV2);
                            da2.add(nuggetsStockBean0);
                        }
                    }
                    //排序
                    if (Integer.parseInt(toDayV) > Integer.parseInt(toDayV2)) {
                        mNuggetsStockBean0s.clear();
                        mNuggetsStockBean0s.addAll(da1);
                        mNuggetsStockBean0s.addAll(da2);
                    } else {
                        mNuggetsStockBean0s.clear();
                        mNuggetsStockBean0s.addAll(da2);
                        mNuggetsStockBean0s.addAll(da1);
                    }
                }
            }
            if (mFlag == 0) {
                for (int i = 0; i < 2; i++) {
                    NuggetsStockBean0 nuggetsStockBean0 = datas.get(i);
                    String toDayV = nuggetsStockBean0.getToDayV();
                    String day = formatDay(toDayV);
                    nuggetsClockTv.add(String.format(day + "%s%s", " ", "10:00"));
                }
            } else if (mFlag == 2) {
                for (int i = 0; i < 2; i++) {
                    NuggetsStockBean0 nuggetsStockBean0 = datas.get(i);
                    String toDayV = nuggetsStockBean0.getToDayV();
                    String day = formatDay(toDayV);
                    nuggetsClockTv.add(String.format(day + "%s%s", " ", "14:30"));
                }
            } else if (mFlag == 1) {
                int size = datas.size();
                for (int i = 0; i < 10; i++) {
                    NuggetsStockBean0 nuggetsStockBean0 = i < 5 ? datas.get(0) : datas.get(size - 1);
                    String toDayV = nuggetsStockBean0.getToDayV();
                    String day = formatDay(toDayV);
                    nuggetsClockTv.add(i < 5 ? String.format(day + "%s%s", " ", h1[i]) : String.format(day + "%s%s", " ", h1[i - 5]));
                }
            }
            notifyDataSetChanged();
        }
    }

    public NuggetsRecyclerAdapter(Activity activity, int flag) {
        mActivity = activity;
        mFlag = flag;
    }

    public void clearData() {
        if (mNuggetsStockBean0s != null && mNuggetsStockBean0s.size() > 0) {
            mNuggetsStockBean0s.clear();
        }
    }

    public void setTitleView(SyncHorizontalScrollView titleScrollView) {
        mContent_titleScrollView = titleScrollView;
    }

    @Override
    public NuggetsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.nuggets_item_view, parent, false);
        NuggetsHolder nuggetsHolder = new NuggetsHolder(view);
        //设置联动
        if (mContent_titleScrollView != null) {
            setContentTitleSync(nuggetsHolder);
        }
        return nuggetsHolder;
    }

    public static final int FIRST_STICKY_VIEW = 1;
    public static final int HAS_STICKY_VIEW = 2;
    public static final int NONE_STICKY_VIEW = 3;

    @Override
    public void onBindViewHolder(NuggetsHolder holder, int position) {
        holder.mNuggetsDay.setText(nuggetsClockTv.get(position));
        holder.itemView.setContentDescription(nuggetsClockTv.get(position));
        if (position == 0) {
            holder.mSticky_header.setVisibility(View.VISIBLE);
            holder.itemView.setTag(FIRST_STICKY_VIEW);
        } else {
            if (nuggetsClockTv.get(position).equals(nuggetsClockTv.get(position - 1))) //当前Item头部与上一个Item头部相同，则隐藏头部
            {
                holder.mSticky_header.setVisibility(View.GONE);
                holder.itemView.setTag(NONE_STICKY_VIEW);
            } else {
                holder.mSticky_header.setVisibility(View.VISIBLE);
                holder.itemView.setTag(HAS_STICKY_VIEW);
            }
        }

        if (mNuggetsStockBean0s != null) {
//            if (mFlag == 0 || mFlag == 2) {
            int size = mNuggetsStockBean0s.size() - 1;
            if (size >= position) {
                if (mNuggetsStockBean0s.size() > 0) {
                    NuggetsStockBean0 nuggetsStockBean0 = mNuggetsStockBean0s.get(position);
                    int dataTimeD = Integer.parseInt(nuggetsStockBean0.getToDayV());
                    NuggetsStockBean0.ToDayModel tooDayModel = nuggetsStockBean0.getTooDayModel();
                    if (tooDayModel == null) {
                        int currentD = getCurrentD();
                        if (currentD > dataTimeD) {
                            holder.mNuggetsView.setVisibility(View.GONE);
                            holder.mStockModelWait.setVisibility(View.GONE);
                            holder.mScreenEmptyView.setVisibility(View.VISIBLE);//筛选为空
                        } else {
                            holder.mStockModelWait.setVisibility(View.VISIBLE);//等待揭晓
                            holder.mNuggetsView.setVisibility(View.GONE);
                            holder.mScreenEmptyView.setVisibility(View.GONE);
                        }

                    } else {
                        List<NuggetsStockBean0.ToDayModel.Clock_10Model> clock_10Model = tooDayModel.getClock_10Model();
//                        if (clock_10Model.size() == 0) {
//                            holder.mNuggetsView.setVisibility(View.GONE);
//                            holder.mStockModelWait.setVisibility(View.GONE);
//                            holder.mScreenEmptyView.setVisibility(View.VISIBLE);//筛选为空
//                        } else {
                        mContentAdapter = new StockModeAdapter(mActivity);
                        mNameAdapter = new StockModeNameAdapter(mActivity);
                        holder.mNuggetsView.setVisibility(View.VISIBLE);//股票列表
                        holder.mStockModelWait.setVisibility(View.GONE);
                        holder.mScreenEmptyView.setVisibility(View.GONE);//筛选为空
                        NuggetsStockBean0.ToDayModel.Clock_10Model clock_10Model1 = clock_10Model.get(0);
                        List<StockMode> stockMode = clock_10Model1.getStockMode();
                        mNameAdapter.setList(stockMode);
                        mContentAdapter.setList(stockMode);
                        holder.mContentListView.setAdapter(mContentAdapter);
                        holder.mContentNameListView.setAdapter(mNameAdapter);
//                        }
                    }
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mFlag == 0 || mFlag == 2) {
            return 2;
        } else {
            return 10;
        }
    }

    //设置联动
    private void setContentTitleSync(NuggetsHolder holder) {
        mContent_titleScrollView.setScrollView(holder.mContentScrollView);
        holder.mContentScrollView.setScrollView(mContent_titleScrollView);
    }


    //获取当前的时间 返回小时
    @SuppressLint("SimpleDateFormat")
    private Integer getCurrentH() {
        long l = System.currentTimeMillis();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        Date date = new Date(l);
        String s = format.format(date);
        String sub1 = s.substring(0, s.indexOf(":"));
        String sub2 = s.substring(s.indexOf(":") + 1, s.length());
        String s1 = sub1 + sub2;
        return Integer.parseInt(s1);
    }

    @SuppressLint("SimpleDateFormat")
    private Integer getCurrentD() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date(System.currentTimeMillis());
        Integer time = Integer.parseInt(format.format(date));
        return time;
    }

    //将格式为20180524的时间转换成2018年05月24日
    private String formatDay(String time) {
        if (time.length() == 8) {
            String year = time.substring(0, 4);
            String month = time.substring(4, 6);
            String day = time.substring(6, 8);
            return month + "月" + day + "日";
        } else {
            return "";
        }
    }

    //将格式为1000的时间转换成10点
    private String formatH(String time) {
        if (time.length() == 4) {
            String h = time.substring(0, 2);
            String minutes = time.substring(2, 4);
            return h + ":" + minutes;
        } else {
            return "";
        }
    }

    public static class NuggetsHolder extends RecyclerView.ViewHolder {
        View mItemView;
        private final ListViewForScrollView mContentNameListView, mContentListView;
        private final SyncHorizontalScrollView mContentScrollView;
        private final LinearLayout mScreenEmptyView, mStockModelWait, mNuggetsView, mSticky_header;
        private final TextView mNuggetsDay;

        NuggetsHolder(View itemView) {
            super(itemView);
            mItemView = itemView;
            mContentNameListView = getView(R.id.content_nameListView1);
            mContentScrollView = getView(R.id.content_ContentScrollView1);
            mContentListView = getView(R.id.content_ContentListView1);
            mStockModelWait = getView(R.id.stock_model_wait);
            mScreenEmptyView = getView(R.id.screen_empty_view);
            mNuggetsView = getView(R.id.nuggetsView);
            mNuggetsDay = getView(R.id.nuggets_day);
            mSticky_header = getView(R.id.sticky_header);
        }

        public <T extends View> T getView(int resourcesId) {
            return (T) mItemView.findViewById(resourcesId);
        }
    }
}
