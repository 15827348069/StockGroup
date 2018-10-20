package com.zbmf.groupro.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zbmf.groupro.R;
import com.zbmf.groupro.adapter.MyblogAdapter;
import com.zbmf.groupro.api.JSONHandler;
import com.zbmf.groupro.api.WebBase;
import com.zbmf.groupro.beans.BlogBean;
import com.zbmf.groupro.utils.SettingDefaultsManager;
import com.zbmf.groupro.utils.ShowActivity;
import com.zbmf.groupro.view.PullToRefreshBase;
import com.zbmf.groupro.view.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.zbmf.groupro.R.id.my_blog_list;

public class MyBlogListActivity extends AppCompatActivity implements View.OnClickListener {
    private List<BlogBean> infolist;
    private MyblogAdapter adapter;
    private PullToRefreshListView blog_list;
    private int page, pages;
    private TextView no_message_text;
    private LinearLayout ll_none;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_blog_list);
        init();
        initData();
    }

    private void init() {
        infolist = new ArrayList<>();
        adapter = new MyblogAdapter(this, infolist);
        TextView group_title_name = (TextView) findViewById(R.id.group_title_name);
        group_title_name.setText(getString(R.string.my_blog_name));
        findViewById(R.id.group_title_return).setOnClickListener(this);
        blog_list = (PullToRefreshListView) findViewById(my_blog_list);

        blog_list.setAdapter(adapter);
        blog_list.setMode(PullToRefreshBase.Mode.BOTH);
        blog_list.getLoadingLayoutProxy().setPullLabel("加载更多数据");
        blog_list.getLoadingLayoutProxy().setRefreshingLabel("正在加载新数据...");
        blog_list.getLoadingLayoutProxy().setReleaseLabel("松开加载数据");
        blog_list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                //下拉刷新
                initData();
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
        no_message_text = (TextView) findViewById(R.id.no_message_text);
        ll_none = (LinearLayout) findViewById(R.id.ll_none);
        no_message_text.setText("暂无观点");
    }

    private void initData() {
        infolist.clear();
        page = 1;
        pages = 0;
        getBlog_message();
    }

    public void getBlog_message() {
        WebBase.getUserBlogs(SettingDefaultsManager.getInstance().UserId(), page, new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                JSONObject result = obj.optJSONObject("result");
                page = result.optInt("page");
                pages = result.optInt("pages");
                if (page == pages) {
                    Toast.makeText(MyBlogListActivity.this, "已加载全部数据", Toast.LENGTH_SHORT);
                    blog_list.onRefreshComplete();
                }
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
                ll_none.setVisibility(View.GONE);
                blog_list.setVisibility(View.VISIBLE);
                if (infolist.size() == 0) {
                    TextView textView = (TextView) findViewById(R.id.no_message_text);
                    textView.setText("还没有任何观点哦");
                    ll_none.setVisibility(View.VISIBLE);
                    blog_list.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(String err_msg) {
                blog_list.onRefreshComplete();
                if (infolist.size() == 0) {
                    blog_list.setVisibility(View.GONE);
                    ll_none.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.group_title_return:
                finish();
                break;
        }
    }
}
