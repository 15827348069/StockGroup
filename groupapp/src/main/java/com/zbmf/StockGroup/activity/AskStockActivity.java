package com.zbmf.StockGroup.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.adapter.AskStockAdapter;
import com.zbmf.StockGroup.adapter.AskTagAdapter;
import com.zbmf.StockGroup.api.JSONHandler;
import com.zbmf.StockGroup.api.WebBase;
import com.zbmf.StockGroup.beans.Ask;
import com.zbmf.StockGroup.beans.TagBean;
import com.zbmf.StockGroup.interfaces.ToGroupClick;
import com.zbmf.StockGroup.utils.JSONParse;
import com.zbmf.StockGroup.utils.ShowActivity;
import com.zbmf.StockGroup.view.PullToRefreshAdapterViewBase;
import com.zbmf.StockGroup.view.PullToRefreshBase;
import com.zbmf.StockGroup.view.PullToRefreshListView;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuhao on 2017/8/31.
 */

public class AskStockActivity extends BaseActivity implements AskTagAdapter.OnItemClickLitener, View.OnClickListener, ToGroupClick {
    private RecyclerView ask_stock_tag;
    private PullToRefreshListView ask_list;
    private AskStockAdapter mAdapter;
    private List<Ask> asks=null;
    private List<TagBean.ChildrenTag>tags;
    private AskTagAdapter tagAdapter ;
    private int page;
    private boolean isRush,isFirst=true;
    private String tag="0";
    private TextView imbAskStock;
    @Override
    public int getLayoutResId() {
        return R.layout.activity_ask_layout;
    }

    @Override
    public void initView() {
        initTitle("提问");
        imbAskStock=getView(R.id.imb_ask_stock);
        ask_stock_tag=getView(R.id.ask_stock_tag);
        ask_list=getView(R.id.list_ask_view);
        ask_list.setMode(PullToRefreshBase.Mode.BOTH);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        ask_stock_tag.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void initData() {
        asks=new ArrayList<>();
        mAdapter=new AskStockAdapter(AskStockActivity.this,asks);
        mAdapter.setGroupClick(this);
        ask_list.setAdapter(mAdapter);
        tags=new ArrayList<>();
        tagAdapter=new AskTagAdapter(AskStockActivity.this);
        tagAdapter.setOnItemClickLitener(this);
        ask_stock_tag.setAdapter(tagAdapter);
        getTagList();
        getAskList();
    }

    @Override
    public void addListener() {
        imbAskStock.setOnClickListener(this);
        ask_list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                rushList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page+=1;
                getAskList();
            }
        });
        ask_list.setOnScroll(new PullToRefreshAdapterViewBase.onScrolls() {
            @Override
            public void scrollTop() {
                if(imbAskStock.getVisibility()==View.VISIBLE)
                {
                    imbAskStock.setVisibility(View.GONE);
                }
            }

            @Override
            public void scrollDown() {
                if(imbAskStock.getVisibility()==View.GONE)
                {
                    imbAskStock.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void scrollBottom() {
                if(imbAskStock.getVisibility()==View.VISIBLE)
                {
                    imbAskStock.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onItemClick(TagBean.ChildrenTag childrenTag, boolean select) {
        if(!tag.equals(childrenTag.getId())){
            tag=childrenTag.getId();
            rushList();
        }
    }
    private void rushList(){
        isRush=true;
        page=1;
        getAskList();
    }
    private void getTagList(){
        WebBase.getStockTag(new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                if(!obj.isNull("stocks")){
                    tags.addAll(JSONParse.getStockTag(obj.optJSONArray("stocks")));
                }
                if(tags.size()>0){
                    tagAdapter.rushAdapter(tags,0);
                }
            }

            @Override
            public void onFailure(String err_msg) {

            }
        });
    }
    private void getAskList(){
        WebBase.askList(tag, page, new JSONHandler(true,AskStockActivity.this,getString(R.string.loading)) {
            @Override
            public void onSuccess(JSONObject obj) {
                if(!obj.isNull("result")){
                    JSONObject result=obj.optJSONObject("result");
                    page=result.optInt("page");
                    if(isRush){
                        isRush=false;
                        asks.clear();
                    }
                    if(!result.isNull("asks")){
                        asks.addAll(JSONParse.getAskList(result.optJSONArray("asks"))) ;
                    }
                    mAdapter.notifyDataSetChanged();
                    if(isFirst){
                        isFirst=false;
                    }
                }
                ask_list.onRefreshComplete();
            }

            @Override
            public void onFailure(String err_msg) {
                if(isFirst){
                    isFirst=false;
                }
                ask_list.onRefreshComplete();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imb_ask_stock:
                if(ShowActivity.isLogin(this)){
                ShowActivity.showActivity(AskStockActivity.this,AskStockSendActivity.class);
            }
                break;
        }
    }

    @Override
    public void toGroup(String id) {
        ShowActivity.showGroupDetailActivity(AskStockActivity.this,id);
    }
}
