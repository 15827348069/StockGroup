package com.zbmf.StockGroup.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.adapter.TimeCheckAdapter;
import com.zbmf.StockGroup.api.JSONHandler;
import com.zbmf.StockGroup.api.WebBase;
import com.zbmf.StockGroup.beans.StockBean;
import com.zbmf.StockGroup.constans.Commons;
import com.zbmf.StockGroup.utils.JSONParse;
import com.zbmf.StockGroup.utils.LogUtil;
import com.zbmf.StockGroup.view.PullToRefreshBase;
import com.zbmf.StockGroup.view.PullToRefreshListView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuhao on 2018/1/31.
 */

public class TimeCheckPopWindow extends PopupWindow{
    onItemClickListener onItemClickListener;
    private PullToRefreshListView pop_list;
    private TimeCheckAdapter adapter;
    private int page;
    private List<StockBean> stockList;
    private int messageType;
    private int select=-1;
    private LinearLayout rl_layout;
    public void setOnItemClickListener(TimeCheckPopWindow.onItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public TimeCheckPopWindow(Context context) {
        super(context);
        initView(context);
    }

    public void setMessageType(int type) {
        messageType = type;
        select=-1;
        page=1;
        if (stockList != null) {
            stockList.clear();
        } else {
            stockList = new ArrayList<>();
        }
        switch (type) {
            case Commons.STOCK:
                stockList.add(new StockBean("全部个股",null));
                getStockList();
                break;
            case Commons.TAGKEY:
                stockList.add(new StockBean("全部关键词",null));
                getTagList();
                break;
        }
    }

    public int getMessageType() {
        return messageType;
    }

    private void initView(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        //绑定布局
        View view = inflater.inflate(R.layout.pop_check_layout, null);
        pop_list = (PullToRefreshListView) view.findViewById(R.id.pop_list);
        rl_layout= (LinearLayout) view.findViewById(R.id.rl_layout);
        pop_list.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        adapter = new TimeCheckAdapter(context);
        pop_list.setAdapter(adapter);
        pop_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (onItemClickListener != null) {
                    select=position-1;
                    onItemClickListener.onItemClick(adapter.getItem(position - 1));
                }
                dismiss();
            }
        });
        stockList = new ArrayList<>();
        pop_list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                page+=1;
                switch (messageType) {
                    case Commons.STOCK:
                        getStockList();
                        break;
                    case Commons.TAGKEY:
                        getTagList();
                        break;
                }
            }
        });
        setContentView(view);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setBackgroundDrawable(null);
    }

    public interface onItemClickListener<T> {
        void onItemClick(T t);
    }

    private void getTagList() {
        WebBase.tagList(page, 5, new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                if (obj.has("result")) {
                    JSONObject result = obj.optJSONObject("result");
                    page = result.optInt("page");
                    if (result.has("tags")) {
                        stockList.addAll(JSONParse.getTagList(result.optJSONArray("tags")));
                        adapter.setList(stockList);
                        setHeight();
                    }
                    if(pop_list!=null&&pop_list.isRefreshing()){
                        pop_list.onRefreshComplete();
                    }
                }
            }

            @Override
            public void onFailure(String err_msg) {
                if(pop_list!=null&&pop_list.isRefreshing()){
                    pop_list.onRefreshComplete();
                }
            }
        });
    }

    private void getStockList() {
        WebBase.stockList(page, 5, new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                if (obj.has("result")) {
                    JSONObject result = obj.optJSONObject("result");
                    page = result.optInt("page");
                    if (result.has("stocks")) {
                        stockList.addAll(JSONParse.getStockBeanList(result.optJSONArray("stocks")));
                        adapter.setList(stockList);
                        setHeight();
                    }
                    if(pop_list!=null&&pop_list.isRefreshing()){
                        pop_list.onRefreshComplete();
                    }
                }
            }

            @Override
            public void onFailure(String err_msg) {
                if(pop_list!=null&&pop_list.isRefreshing()){
                    pop_list.onRefreshComplete();
                }
            }
        });
    }

    @Override
    public void showAsDropDown(View anchor) {
        super.showAsDropDown(anchor);
        LogUtil.e("select="+select);
        adapter.checkPosition(select);
    }

    private void setHeight() {
        if(adapter.getCount()!=0){
            View listItem = adapter.getView(0, null, pop_list);
            listItem.measure(0, 0);
            int relheight = listItem.getMeasuredHeight();
            int scrheight=relheight*5;
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) pop_list.getLayoutParams();
            layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
            if(relheight * adapter.getCount()> scrheight){
                layoutParams.height = scrheight;
            }else{
                layoutParams.height =relheight * adapter.getCount();
            }
            pop_list.setLayoutParams(layoutParams);
            rl_layout.setLayoutParams(layoutParams);
        }
    }

}
