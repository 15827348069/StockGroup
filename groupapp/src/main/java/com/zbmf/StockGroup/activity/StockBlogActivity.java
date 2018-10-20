package com.zbmf.StockGroup.activity;

import android.view.View;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.adapter.StudyStockAdapter;
import com.zbmf.StockGroup.api.JSONHandler;
import com.zbmf.StockGroup.api.WebBase;
import com.zbmf.StockGroup.beans.BlogBean;
import com.zbmf.StockGroup.beans.NewsFeed;
import com.zbmf.StockGroup.constans.StudyBlog;
import com.zbmf.StockGroup.utils.JSONParse;
import com.zbmf.StockGroup.view.ListViewForScrollView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 炒股入门入口
 * Created by xuhao on 2017/11/14.
 */

public class StockBlogActivity extends BaseActivity implements View.OnClickListener {
    private List<BlogBean>infolsit;
    private StudyStockAdapter adapter;
    private ListViewForScrollView listview;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_stock_blog;
    }

    @Override
    public void initView() {
        initTitle("炒股入门");
        listview=getView(R.id.list_stock_study);
    }

    @Override
    public void initData() {
        infolsit=new ArrayList<>();
        adapter=new StudyStockAdapter(this);
        adapter.setList(infolsit);
        listview.setAdapter(adapter);
        getUserBlogs();
    }

    @Override
    public void addListener() {
        getView(R.id.tv_jczs).setOnClickListener(this);
        getView(R.id.tv_hgfx).setOnClickListener(this);
        getView(R.id.tv_cwfx).setOnClickListener(this);
        getView(R.id.tv_xgff).setOnClickListener(this);
        getView(R.id.tv_jyzn).setOnClickListener(this);
        getView(R.id.tv_gpfx).setOnClickListener(this);
    }
    /**
     * 获取炒股入门数据
     */
    private void getUserBlogs(){
        WebBase.getUserBlogs(StudyBlog.zbmf_jczs,1,new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                JSONObject result=obj.optJSONObject("result");
                infolsit.addAll(JSONParse.getBlogBean(result.optJSONArray("")));
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onFailure(String err_msg) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_jczs:

                break;
            case R.id.tv_hgfx:

                break;
            case R.id.tv_cwfx:

                break;
            case R.id.tv_xgff:

                break;
            case R.id.tv_jyzn:

                break;
            case R.id.tv_gpfx:

                break;
        }
    }
}
