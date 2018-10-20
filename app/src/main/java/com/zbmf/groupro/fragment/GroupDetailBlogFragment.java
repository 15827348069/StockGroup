package com.zbmf.groupro.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;

import com.zbmf.groupro.R;
import com.zbmf.groupro.activity.GroupDetailActivity;
import com.zbmf.groupro.adapter.BlogMessageAdapter;
import com.zbmf.groupro.api.JSONHandler;
import com.zbmf.groupro.api.WebBase;
import com.zbmf.groupro.beans.BlogBean;
import com.zbmf.groupro.beans.Group;
import com.zbmf.groupro.utils.ShowActivity;
import com.zbmf.groupro.view.AddMoreLayout;
import com.zbmf.groupro.view.ListViewForScrollView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuhao on 2017/2/25.
 */

public class GroupDetailBlogFragment extends BaseFragment implements AddMoreLayout.OnSendClickListener {
    private List<BlogBean> bloglist;
    private ListViewForScrollView group_detail_list;
    private int page,pages;
    private BlogMessageAdapter headadapter;
    private Group group_bean;
    public static GroupDetailBlogFragment newInstance(Group group) {
        GroupDetailBlogFragment fragment = new GroupDetailBlogFragment();
        Bundle args = new Bundle();
        args.putSerializable("GROUP", group);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            group_bean = (Group) getArguments().getSerializable("GROUP");
        }
    }

    @Override
    protected View setContentView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.group_detail_fragment,null);
    }

    @Override
    protected void initView() {
        bloglist=new ArrayList<>();
        headadapter=new BlogMessageAdapter(getContext(),bloglist);
        group_detail_list=getView(R.id.group_detail_list);
        group_detail_list.addFootView(this);
        group_detail_list.setAdapter(headadapter);
        group_detail_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ShowActivity.showBlogDetailActivity(getActivity(),bloglist.get(i));
            }
        });
    }

    @Override
    protected void initData() {
        page=1;
        pages=0;
        bloglist.clear();
        getBlog_message();
        GroupDetailActivity activity= (GroupDetailActivity) getActivity();
        activity.viewPager.setObjectForPosition(getFragmentView(),0);
    }
    private void getBlog_message(){
        group_detail_list.onLoad();
        WebBase.getUserBlogs(group_bean.getId(),page, new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                JSONObject result=obj.optJSONObject("result");
                page=result.optInt("page");
                pages=result.optInt("pages");
                JSONArray blogs=result.optJSONArray("blogs");
                int size=blogs.length();
                for(int i=0;i<size;i++){
                    JSONObject blog=blogs.optJSONObject(i);
                    BlogBean blogBean=new BlogBean();
                    blogBean.setImg(blog.optString("cover"));
                    blogBean.setTitle(blog.optString("subject"));
                    blogBean.setDate(blog.optString("posted_at"));
                    blogBean.setLook_number(blog.optJSONObject("stat").optString("views"));
                    blogBean.setPinglun(blog.optJSONObject("stat").optString("replys"));
                    blogBean.setApp_link(blog.optJSONObject("link").optString("app"));
                    blogBean.setWap_link(blog.optJSONObject("link").optString("wap"));
                    blogBean.setBlog_id(blog.optString("blog_id"));
                    bloglist.add(blogBean);
                }
                headadapter.notifyDataSetChanged();
                if(page==pages){
                    group_detail_list.addAllData();
                }else{
                    group_detail_list.onStop();
                }
            }
            @Override
            public void onFailure(String err_msg) {
                group_detail_list.onStop();
            }
        });
    }

    @Override
    public void OnSendClickListener(View view) {
        page+=1;
        getBlog_message();
    }
}
