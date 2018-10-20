package com.zbmf.StockGroup.fragment.care;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.adapter.DynamicAdapter;
import com.zbmf.StockGroup.api.AppUrl;
import com.zbmf.StockGroup.api.JSONHandler;
import com.zbmf.StockGroup.api.WebBase;
import com.zbmf.StockGroup.beans.BlogBean;
import com.zbmf.StockGroup.beans.NewsFeed;
import com.zbmf.StockGroup.constans.HtmlUrl;
import com.zbmf.StockGroup.constans.IntentKey;
import com.zbmf.StockGroup.fragment.BaseFragment;
import com.zbmf.StockGroup.interfaces.LoadFinish;
import com.zbmf.StockGroup.constans.Constants;
import com.zbmf.StockGroup.utils.JSONParse;
import com.zbmf.StockGroup.utils.ShowActivity;
import com.zbmf.StockGroup.view.CustomViewpager;
import com.zbmf.StockGroup.view.ListViewForScrollView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuhao on 2017/8/17.
 */

public class CareBlogFragment extends BaseFragment{
    private ListViewForScrollView lv_tief;
    private CustomViewpager careFragments;
    private DynamicAdapter dynamicAdapter;
    private int flag;
    private List<NewsFeed> mNewsFeeds = new ArrayList<NewsFeed>();
    private int page,pages;
    private boolean isFirst=true,isRush;
    private LinearLayout ll_none;
    private TextView no_message_text,right_button,left_button;

    public void setCustomViewPage(CustomViewpager careFragments) {
        this.careFragments = careFragments;
    }
    public static CareBlogFragment newInstance(int flag){
        CareBlogFragment fragment=new CareBlogFragment();
        Bundle bundle=new Bundle();
        bundle.putInt(IntentKey.FLAG,flag);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            flag=getArguments().getInt(IntentKey.FLAG);
        }
    }

    @Override
    protected View setContentView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_care_fans_layout,null);
    }

    @Override
    protected void initView() {
        lv_tief=getView(R.id.lv_focus);
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
                ShowActivity.showWebViewActivity(getActivity(), HtmlUrl.STOCK_LIVE);
            }
        });
        left_button.setVisibility(View.VISIBLE);
        left_button.setText(getString(R.string.to_stock));
        left_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowActivity.showWebViewActivity(getActivity(), HtmlUrl.STOCK_LIVE);
            }
        });

        dynamicAdapter=new DynamicAdapter(getContext(),mNewsFeeds);
        dynamicAdapter.setFlag(flag);
        lv_tief.setAdapter(dynamicAdapter);
        lv_tief.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NewsFeed newsFeed = mNewsFeeds.get(position);
                BlogBean blog = new BlogBean();
                blog.setBlog_id(newsFeed.getBlog_id());
                blog.setApp_link(newsFeed.getLink().getApp());
                blog.setWap_link(newsFeed.getLink().getWap());
                blog.setImg(newsFeed.getCover());
                blog.setTitle(newsFeed.getSubject());
                blog.setLook_number(newsFeed.getStat().getViews());
                blog.setPinglun(newsFeed.getStat().getReplys());
                blog.setDate(newsFeed.getChanged_at());
                ShowActivity.showBlogDetailActivity(getActivity(), blog);
            }
        });
    }

    @Override
    protected void initData() {
        isFirst=true;
        setViewPageHeight();
        RushList();
    }
    public void RushList(){
        page=1;
        isRush=true;
        switch (flag){
            case Constants.BLOG_FRAGMENT:
                getBlog();
                break;
            case Constants.MFWW:
                getUserBlog(Constants.MFWW_ID);
                break;
            case Constants.ZBMFTT:
                getUserBlog(Constants.ZBMFTT_ID);
                break;
        }
    }
    public void getMoreList(){
        page+=1;
        switch (flag){
            case Constants.MFWW:
                getUserBlog(Constants.MFWW_ID);
                break;
            case Constants.ZBMFTT:
                getUserBlog(Constants.ZBMFTT_ID);
                break;
        }
    }
    private void getUserBlog(String user_id){
        if(isFirst){
            dialogShow();
        }
        WebBase.getUserBlogs(user_id,page, new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                JSONObject result=obj.optJSONObject("result");
                if(isRush){
                    isRush=false;
                    mNewsFeeds.clear();
                }
                NewsFeed blog = JSONParse.blog(result);
                if (blog != null&&blog.getList()!=null) {
                    mNewsFeeds.addAll(blog.getList());
                }
                dynamicAdapter.notifyDataSetChanged();
                page=result.optInt("page");
                pages=result.optInt("pages");
                if(mNewsFeeds.size()==0){
                    ll_none.setVisibility(View.VISIBLE);
                }else{
                    ll_none.setVisibility(View.GONE);
                }
                if(page==pages&&!isFirst){
                    showToast("已加载全部数据");
                }
                if(isFirst){
                    isFirst=false;
                }
                if(loadFinish!=null){
                    loadFinish.onFinish();
                }
                setViewPageHeight();
                dialogDiss();
            }
            @Override
            public void onFailure(String err_msg) {
                if(loadFinish!=null){
                    loadFinish.onFinish();
                }
                if(isFirst){
                    isFirst=false;
                }
                setViewPageHeight();
                dialogDiss();
            }
        });
    }
    public void setViewPageHeight(){
        if(careFragments!=null){
            switch (flag){
                case Constants.BLOG_FRAGMENT:
                    careFragments.setObjectForPosition(getFragmentView(),1);
                    careFragments.resetHeight(1);
                    break;
                case Constants.ZBMFTT:
                    careFragments.setObjectForPosition(getFragmentView(),2);
                    careFragments.resetHeight(2);
                    break;
                case Constants.MFWW:
                    careFragments.setObjectForPosition(getFragmentView(),3);
                    careFragments.resetHeight(3);
                    break;
            }
        }
    }
    /**
     * 获取博文
     */
    private void getBlog(){
        if(isFirst){
            dialogShow();
        }
        WebBase.blog(0, new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                mNewsFeeds.clear();
                NewsFeed blog = JSONParse.blog(obj);
                if (blog != null&&blog.getList().size()>0) {
                    mNewsFeeds.addAll(blog.getList());
                    dynamicAdapter.notifyDataSetChanged();
                }else{
                    if(!isFirst){
                        showToast("已加载全部数据");
                    }
                }
                if(mNewsFeeds.size()==0){
                    ll_none.setVisibility(View.VISIBLE);
                }else{
                    ll_none.setVisibility(View.GONE);
                }
                if(loadFinish!=null){
                    loadFinish.onFinish();
                }
                setViewPageHeight();
                dialogDiss();
                if (isFirst){
                    isFirst=false;
                }
            }

            @Override
            public void onFailure(String err_msg) {
                if(loadFinish!=null){
                    loadFinish.onFinish();
                }
                setViewPageHeight();
                dialogDiss();
                if (isFirst){
                    isFirst=false;
                }
            }
        });
    }
    private LoadFinish loadFinish;

    public void setLoadFinish(LoadFinish loadFinish) {
        this.loadFinish = loadFinish;
    }

}
