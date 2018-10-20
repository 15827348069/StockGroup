package com.zbmf.groupro.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zbmf.groupro.R;
import com.zbmf.groupro.adapter.HeadMessageAdapter;
import com.zbmf.groupro.api.JSONHandler;
import com.zbmf.groupro.api.WebBase;
import com.zbmf.groupro.beans.BlogBean;
import com.zbmf.groupro.utils.ShowActivity;
import com.zbmf.groupro.view.PullToRefreshBase;
import com.zbmf.groupro.view.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuhao on 2017/2/24.
 */

public class UserCollectsBlogFragment extends BaseFragment {
    private PullToRefreshListView user_collects_blog_list;
    private HeadMessageAdapter adapter;
    private List<BlogBean>infolist;
    private int page,pages;
    private LinearLayout ll_none;
    private TextView no_message_text;
    public static UserCollectsBlogFragment newInstance() {
        UserCollectsBlogFragment fragment = new UserCollectsBlogFragment();
        return fragment;
    }

    @Override
    protected View setContentView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.usercollectsbloglayout,null);
    }

    @Override
    protected void initView() {
        user_collects_blog_list=getView(R.id.user_collects_blog_list);
        ll_none=getView(R.id.ll_none);
        no_message_text=getView(R.id.no_message_text);
        infolist=new ArrayList<>();
        adapter=new HeadMessageAdapter(getContext(),infolist);
        user_collects_blog_list.setAdapter(adapter);
        user_collects_blog_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                BlogBean bb=infolist.get(i-1);
                if(bb.is_delete()){
                    showToast("该博文已被删除");
                }else{
                    ShowActivity.showBlogDetailActivity(getActivity(),bb);
                }
            }
        });
        user_collects_blog_list.setMode(PullToRefreshBase.Mode.BOTH);
        user_collects_blog_list.getLoadingLayoutProxy().setPullLabel("加载更多数据");
        user_collects_blog_list.getLoadingLayoutProxy().setRefreshingLabel("正在加载新数据...");
        user_collects_blog_list.getLoadingLayoutProxy().setReleaseLabel("松开加载数据");
        user_collects_blog_list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                //下拉刷新
                initData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                //上拉加载
                page+=1;
                getBlog_message();
            }
        });
    }

    @Override
    protected void initData() {
        page=1;
        pages=0;
        infolist.clear();
        getBlog_message();
    }
    public void getBlog_message(){
        WebBase.getUserCollects(page,new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                JSONObject result=obj.optJSONObject("result");
                page=result.optInt("page");
                pages=result.optInt("pages");
                JSONArray blogs=result.optJSONArray("collects");
                int size=blogs.length();
                for(int i=0;i<size;i++){
                    JSONObject blog_detail=blogs.optJSONObject(i);
                    BlogBean blogBean=new BlogBean();
                    if(blog_detail.optInt("is_delete")==0){
                        JSONObject blog=blog_detail.optJSONObject("object");
                        blogBean.setImg(blog.optString("cover"));
                        blogBean.setTitle(blog.optString("subject"));
                        blogBean.setDate(blog.optString("posted_at"));
                        blogBean.setBlog_id(blog.optString("blog_id"));
                        blogBean.setApp_link(blog.optJSONObject("link").optString("app"));
                        blogBean.setWap_link(blog.optJSONObject("link").optString("wap"));
                        if(!blog.isNull("user")){
                            blogBean.setAvatar(blog.optJSONObject("user").optString("avatar"));
                            blogBean.setName(blog.optJSONObject("user").optString("nickname"));
                        }
                        blogBean.setIs_collects(false);
                    }else{
                        blogBean.setIs_delete(true);
                    }
                    infolist.add(blogBean);
                }
                adapter.notifyDataSetChanged();
                user_collects_blog_list.onRefreshComplete();
                if(page==pages){
                    showToast("已加载全部数据");
                    user_collects_blog_list.onRefreshComplete();
                    if(infolist.size()==0){
                        ll_none.setVisibility(View.VISIBLE);
                        no_message_text.setText("暂无收藏");
                    }else{
                        ll_none.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(String err_msg) {
                showToast(err_msg);
                user_collects_blog_list.onRefreshComplete();
            }
        });
    }
}
