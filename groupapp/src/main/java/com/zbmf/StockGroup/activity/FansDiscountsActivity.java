package com.zbmf.StockGroup.activity;

import android.widget.ListView;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.adapter.FansDiscountsAdapter;
import com.zbmf.StockGroup.api.JSONHandler;
import com.zbmf.StockGroup.api.WebBase;
import com.zbmf.StockGroup.beans.BoxBean;
import com.zbmf.StockGroup.beans.Group;
import com.zbmf.StockGroup.beans.NewsFeed;
import com.zbmf.StockGroup.interfaces.OnFansClick;
import com.zbmf.StockGroup.utils.JSONParse;
import com.zbmf.StockGroup.utils.ShowActivity;
import com.zbmf.StockGroup.view.PullToRefreshBase;
import com.zbmf.StockGroup.view.PullToRefreshListView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuhao on 2017/8/28.
 */

public class FansDiscountsActivity extends BaseActivity implements OnFansClick {
    private PullToRefreshListView discounts_list;
    private List<Group>infolist;
    private FansDiscountsAdapter adapter;
    private int page,pages;
    private boolean isRush,isFirst=true;
    @Override
    public int getLayoutResId() {
        return R.layout.activity_discounts_layout;
    }

    @Override
    public void initView() {
        initTitle(getString(R.string.fans_discounts));
        discounts_list=getView(R.id.lv_discounts);
        discounts_list.setMode(PullToRefreshBase.Mode.BOTH);
    }

    @Override
    public void initData() {
        infolist=new ArrayList<>();
        adapter=new FansDiscountsAdapter(FansDiscountsActivity.this,infolist);
        adapter.setAddFans(this);
        discounts_list.setAdapter(adapter);
        Rush();
    }

    @Override
    public void addListener() {
        discounts_list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                Rush();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page+=1;
                getDiscounts();
            }
        });
    }
    private void Rush(){
        page=1;
        isRush=true;
        getDiscounts();
    }
    private void getDiscounts(){
        WebBase.coupon(page, new JSONHandler(isFirst,FansDiscountsActivity.this,getString(R.string.loading)) {
            @Override
            public void onSuccess(JSONObject obj) {
                if(!obj.isNull("result")){
                    JSONObject result=obj.optJSONObject("result");
                    page=result.optInt("page");
                    pages=result.optInt("pages");
                    if(isRush){
                        isRush=false;
                        infolist.clear();
                    }
                    if(!result.isNull("groups")){
                        infolist.addAll(JSONParse.getGroupList(result.optJSONArray("groups")));
                        adapter.notifyDataSetChanged();
                    }
                }
                if(isFirst){
                    isFirst=false;
                }
                discounts_list.onRefreshComplete();
            }

            @Override
            public void onFailure(String err_msg) {
                showToast(err_msg);
                discounts_list.onRefreshComplete();
                if(isFirst){
                    isFirst=false;
                }
            }
        });

    }

    @Override
    public void onBox(BoxBean boxBean) {

    }

    @Override
    public void onFans(String groupId) {
        if(ShowActivity.isLogin(this)){
            WebBase.fansInfo(groupId, new JSONHandler() {
                @Override
                public void onSuccess(JSONObject obj) {
                    if(!obj.isNull("group")){
                        Group group = JSONParse.getGroup(obj.optJSONObject("group"));
                        ShowActivity.showFansActivity(FansDiscountsActivity.this, group);
                    }
                }
                @Override
                public void onFailure(String err_msg) {
                    showToast(err_msg);
                }
            });
        }
    }

    @Override
    public void onGroup(String  user) {
        ShowActivity.showGroupDetailActivity(FansDiscountsActivity.this,user);
    }
}
