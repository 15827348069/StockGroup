package com.zbmf.StockGTec.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.zbmf.StockGTec.R;
import com.zbmf.StockGTec.adapter.MyblogAdapter;
import com.zbmf.StockGTec.api.JSONHandler;
import com.zbmf.StockGTec.api.WebBase;
import com.zbmf.StockGTec.beans.BlogBean;
import com.zbmf.StockGTec.utils.JSONparse;
import com.zbmf.StockGTec.utils.SettingDefaultsManager;
import com.zbmf.StockGTec.utils.ShowActivity;
import com.zbmf.StockGTec.view.PullToRefreshBase;
import com.zbmf.StockGTec.view.PullToRefreshListView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MyBlogActivity extends ExActivity {

    private PullToRefreshListView mListView;
    private List<BlogBean> mNewsFeeds = new ArrayList<>();

    private int PAGE_INDEX, PAGGS;
    private MyblogAdapter dynamicAdapter;

    public static final int Refresh = 1;
    public static final int LoadMore = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_blog);

        TextView tv_title = ((TextView) findViewById(R.id.group_title_name));
        tv_title.setText("我的观点");
        tv_title.setVisibility(View.VISIBLE);
        findViewById(R.id.group_title_return).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mListView = (PullToRefreshListView)findViewById(R.id.sc_focus) ;
        dynamicAdapter = new MyblogAdapter(this, mNewsFeeds);
        mListView.setAdapter(dynamicAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BlogBean bb = mNewsFeeds.get(position-1);
                ShowActivity.showBlogDetailActivity(MyBlogActivity.this, bb);
            }
        });

        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                getUserBlogs(Refresh,false);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                getUserBlogs(LoadMore,false);
            }
        });

        getUserBlogs(Refresh,true);
    }

    private void getUserBlogs(final int direction,boolean show) {

        if (direction == Refresh)
            PAGE_INDEX = 1;
        else
            PAGE_INDEX++;

        WebBase.getUserBlogs(SettingDefaultsManager.getInstance().UserId(),PAGE_INDEX, new JSONHandler(show,this,"加载...") {
            @Override
            public void onSuccess(JSONObject obj) {
                mListView.onRefreshComplete();
                BlogBean blogBean = JSONparse.getUserBlogs(obj);


                if(blogBean != null){
                    PAGGS = blogBean.pages;
                    if (PAGE_INDEX == PAGGS){
                        mListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                    }else{
                        mListView.setMode(PullToRefreshBase.Mode.BOTH);
                    }

                    if(blogBean.getList()!= null && blogBean.getList().size()>0){
                        if (direction == Refresh) {
                            mNewsFeeds.clear();
                        }
                        mNewsFeeds.addAll(blogBean.getList());
                        dynamicAdapter.notifyDataSetChanged();
                    }else{
//                        ll_none.setVisibility(View.VISIBLE);
                        mListView.setVisibility(View.GONE);
                    }
                }else{
                    mListView.setVisibility(View.GONE);
//                    ll_none.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(String err_msg) {

            }
        });

    }




}
