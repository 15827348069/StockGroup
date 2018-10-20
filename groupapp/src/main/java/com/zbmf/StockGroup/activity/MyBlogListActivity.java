package com.zbmf.StockGroup.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.adapter.MyblogAdapter;
import com.zbmf.StockGroup.api.AppUrl;
import com.zbmf.StockGroup.api.JSONHandler;
import com.zbmf.StockGroup.api.WebBase;
import com.zbmf.StockGroup.beans.BlogBean;
import com.zbmf.StockGroup.constans.IntentKey;
import com.zbmf.StockGroup.constans.RequestCode;
import com.zbmf.StockGroup.utils.SettingDefaultsManager;
import com.zbmf.StockGroup.utils.ShowActivity;
import com.zbmf.StockGroup.view.PullToRefreshBase;
import com.zbmf.StockGroup.view.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.zbmf.StockGroup.R.id.my_blog_list;

public class MyBlogListActivity extends BaseActivity {
    private List<BlogBean> infolist;
    private MyblogAdapter adapter;
    private PullToRefreshListView blog_list;
    private int page, pages;
    private LinearLayout ll_none;
    private TextView no_message_text,right_button,left_button;
    private boolean isFirst=true;
    @Override
    public int getLayoutResId() {
        return R.layout.activity_my_blog_list;
    }

    @Override
    public void initView() {
        initTitle("我的观点");
        blog_list = (PullToRefreshListView) findViewById(my_blog_list);
        blog_list.setMode(PullToRefreshBase.Mode.BOTH);
        ll_none=getView(R.id.ll_none);
        no_message_text=getView(R.id.no_message_text);
        right_button=getView(R.id.tv_right_button);
        left_button=getView(R.id.tv_left_button);
        no_message_text.setText(getString(R.string.no_msg));
        right_button.setVisibility(View.VISIBLE);
        right_button.setText(getString(R.string.to_original));
        right_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putInt(IntentKey.FLAG,2);
                ShowActivity.showActivity(MyBlogListActivity.this,bundle,LookStockActivity.class);
            }
        });
        left_button.setVisibility(View.VISIBLE);
        left_button.setText(getString(R.string.to_stock));
        left_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowActivity.showActivity(MyBlogListActivity.this, LookStockActivity.class);
            }
        });
    }

    public void initData() {
        page = 1;
        pages = 0;
        if(infolist==null){
            infolist = new ArrayList<>();
        }else{
            infolist.clear();
        }
        adapter = new MyblogAdapter(this, infolist);
        blog_list.setAdapter(adapter);
        getBlog_message();
    }
    private void rush(){
        page = 1;
        pages = 0;
        infolist.clear();
        getBlog_message();
    }
    @Override
    public void addListener() {
        blog_list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                //下拉刷新
                rush();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                //上拉加载
                page += 1;
                getBlog_message();
            }
        });
        blog_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ShowActivity.showBlogDetailActivity(MyBlogListActivity.this, infolist.get(i - 1));
            }
        });
    }

    public void getBlog_message() {
        WebBase.getUserBlogs(SettingDefaultsManager.getInstance().UserId(), page, new JSONHandler(isFirst,MyBlogListActivity.this,getString(R.string.loading)) {
            @Override
            public void onSuccess(JSONObject obj) {
                JSONObject result = obj.optJSONObject("result");
                page = result.optInt("page");
                pages = result.optInt("pages");
                JSONArray blogs = result.optJSONArray("blogs");
                int size = blogs.length();
                for (int i = 0; i < size; i++) {
                    JSONObject blog = blogs.optJSONObject(i);
                    BlogBean blogBean = new BlogBean();
                    blogBean.setImg(blog.optString("cover"));
                    blogBean.setTitle(blog.optString("subject"));
                    blogBean.setDate(blog.optString("posted_at"));
                    blogBean.setBlog_id(blog.optString("blog_id"));
                    blogBean.setLook_number(blog.optJSONObject("stat").optString("views"));
                    blogBean.setPinglun(blog.optJSONObject("stat").optString("replys"));
                    blogBean.setApp_link(blog.optJSONObject("link").optString("app"));
                    blogBean.setWap_link(blog.optJSONObject("link").optString("wap"));
                    blogBean.setIs_myself(true);
                    infolist.add(blogBean);
                }
                adapter.notifyDataSetChanged();
                blog_list.onRefreshComplete();
                if (page == pages&&!isFirst) {
                    Toast.makeText(MyBlogListActivity.this, "已加载全部数据", Toast.LENGTH_SHORT);
                    blog_list.onRefreshComplete();
                }
                if (infolist.size() == 0) {
                    ll_none.setVisibility(View.VISIBLE);
                    blog_list.setVisibility(View.GONE);
                }else{
                    ll_none.setVisibility(View.GONE);
                    blog_list.setVisibility(View.VISIBLE);
                }
                if(isFirst){
                    isFirst=false;
                }
            }

            @Override
            public void onFailure(String err_msg) {
                blog_list.onRefreshComplete();
                if (infolist.size() == 0) {
                    blog_list.setVisibility(View.GONE);
                    ll_none.setVisibility(View.VISIBLE);
                }
                if(isFirst){
                    isFirst=false;
                }
            }
        });
    }
}
