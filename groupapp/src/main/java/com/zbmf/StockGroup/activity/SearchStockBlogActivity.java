package com.zbmf.StockGroup.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.adapter.StudyStockAdapter;
import com.zbmf.StockGroup.api.JSONHandler;
import com.zbmf.StockGroup.api.WebBase;
import com.zbmf.StockGroup.beans.BlogBean;
import com.zbmf.StockGroup.constans.IntentKey;
import com.zbmf.StockGroup.constans.StudyBlog;
import com.zbmf.StockGroup.utils.JSONParse;
import com.zbmf.StockGroup.utils.ShowActivity;
import com.zbmf.StockGroup.view.ListViewForScrollView;
import com.zbmf.StockGroup.view.PullToRefreshBase;
import com.zbmf.StockGroup.view.PullToRefreshListView;
import com.zbmf.StockGroup.view.PullToRefreshScrollView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.zbmf.StockGroup.constans.StudyBlog.zbmf_cwfx;
import static com.zbmf.StockGroup.constans.StudyBlog.zbmf_gpfx;
import static com.zbmf.StockGroup.constans.StudyBlog.zbmf_hgfx;
import static com.zbmf.StockGroup.constans.StudyBlog.zbmf_jczs;
import static com.zbmf.StockGroup.constans.StudyBlog.zbmf_jyzn;
import static com.zbmf.StockGroup.constans.StudyBlog.zbmf_xgff;

/**
 * Created by xuhao on 2017/11/14.
 */

public class SearchStockBlogActivity extends BaseActivity {
    private EditText search_edittext;
    private RelativeLayout stock_blog_title;
    private PullToRefreshListView list_stock_study;
    private TextView tv_title,tv_num;
    private String zbmf_blog_id;
    private List<BlogBean>infolist;
    private StudyStockAdapter adapter;
    private LinearLayout ll_none;
    private TextView no_message_text;
    private int page;
    private boolean isRush;
    @Override
    public int getLayoutResId() {
        return R.layout.activity_search_stock_blog;
    }

    @Override
    public void initView() {
        initTitle("炒股入门");
        search_edittext=getView(R.id.search_edittext);
        tv_title=getView(R.id.tv_title);
        list_stock_study=getView(R.id.list_stock_study);
        stock_blog_title=getView(R.id.stock_blog_title);
        ll_none=getView(R.id.ll_none);
        no_message_text=getView(R.id.no_message_text);
        tv_num=getView(R.id.tv_num);
        no_message_text.setText("暂无内容");
        list_stock_study.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
    }

    @Override
    public void initData() {
        page=1;
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            zbmf_blog_id=bundle.getString(IntentKey.BLOG_ID,"");
            setTitleMessage();
            getUserBlogs();
        }else{
            stock_blog_title.setVisibility(View.GONE);
            search_edittext.setFocusableInTouchMode(true);
            search_edittext.setFocusable(true);
            search_edittext.requestFocus();
        }
        infolist=new ArrayList<>();
        adapter=new StudyStockAdapter(this);
        list_stock_study.setAdapter(adapter);

    }
    private void setTitleMessage(){
        stock_blog_title.setVisibility(View.VISIBLE);
        String zbmf_blog_tag="";
        switch (zbmf_blog_id){
            case zbmf_jczs:
                zbmf_blog_tag="基础知识";
                break;
            case zbmf_hgfx:
                zbmf_blog_tag="宏观分析";
                break;
            case zbmf_cwfx:
                zbmf_blog_tag="财务分析";
                break;
            case zbmf_xgff:
                zbmf_blog_tag="选股方法";
                break;
            case zbmf_jyzn:
                zbmf_blog_tag="交易指南";
                break;
            case zbmf_gpfx:
                zbmf_blog_tag="股票风险";
                break;
        }
        tv_title.setText(zbmf_blog_tag);
    }
    @Override
    public void addListener() {
        list_stock_study.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ShowActivity.showBlogDetailActivity(SearchStockBlogActivity.this, (BlogBean) adapter.getItem(position-1));
            }
        });
        list_stock_study.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                page+=1;
                if(!TextUtils.isEmpty(search_edittext.getText())){
                    serarchBlog(search_edittext.getText().toString());
                }else{
                    getUserBlogs();
                }
            }
        });
        search_edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()==0||TextUtils.isEmpty(s)){
                    if(zbmf_blog_id!=null){
                        infolist.clear();
                        getUserBlogs();
                    }else{
                        infolist.clear();
                        adapter.notifyDataSetChanged();
                        stock_blog_title.setVisibility(View.GONE);
                    }
                }
            }
        });
        search_edittext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int acition_id, KeyEvent keyEvent) {
                String s=search_edittext.getText().toString();
                if(acition_id== EditorInfo.IME_ACTION_SEARCH){
                    if(!TextUtils.isEmpty(s)&&s.length()>0){
                        page=1;
                        isRush=true;
                        serarchBlog(s.toString());
                    }else if(zbmf_blog_id!=null){
                        infolist.clear();
                        adapter.notifyDataSetChanged();
                        getUserBlogs();
                    }else{
                        infolist.clear();
                        adapter.notifyDataSetChanged();
                        stock_blog_title.setVisibility(View.GONE);
                    }
                    return true;
                }
                return false;
            }
        });
    }
    private void serarchBlog(String q){
        WebBase.searchUserBlogs(page,q, new JSONHandler(true,SearchStockBlogActivity.this,getString(R.string.loading)) {
            @Override
            public void onSuccess(JSONObject obj) {
                if(isRush){
                    isRush=false;
                    infolist.clear();
                }
                if(obj.has("result")){
                    JSONObject result=obj.optJSONObject("result");
                    page=result.optInt("page");
                    if(result.optInt("total")>0){
                        stock_blog_title.setVisibility(View.VISIBLE);
                        tv_num.setVisibility(View.VISIBLE);
                        tv_title.setText("搜索结果");
                        tv_num.setText("共"+result.optString("total")+"条");
                    }else{
                        stock_blog_title.setVisibility(View.GONE);
                    }
                    if(result!=null&&result.has("blogs")){
                        infolist.addAll(JSONParse.getBlogBean(result.optJSONArray("blogs")));
                    }
                    if(infolist.size()==0){
                        ll_none.setVisibility(View.VISIBLE);
                    }else{
                        ll_none.setVisibility(View.GONE);
                        adapter.setList(infolist);
                    }

                }
                list_stock_study.onRefreshComplete();
            }

            @Override
            public void onFailure(String err_msg) {
                list_stock_study.onRefreshComplete();
            }
        });
    }

    private void getUserBlogs(){
        tv_num.setVisibility(View.GONE);
        WebBase.getRudimentsBlogs(zbmf_blog_id,page,new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                if(obj.has("result")){
                    JSONObject result=obj.optJSONObject("result");
                    page=result.optInt("page");
                    if(result!=null&&result.has("blogs")){
                        infolist.addAll(JSONParse.getBlogBean(result.optJSONArray("blogs")));
                    }
                    if(infolist.size()==0){
                        ll_none.setVisibility(View.VISIBLE);
                    }else{
                        ll_none.setVisibility(View.GONE);
                        adapter.setList(infolist);
                    }
                    setTitleMessage();
                }
                list_stock_study.onRefreshComplete();
            }

            @Override
            public void onFailure(String err_msg) {
                list_stock_study.onRefreshComplete();
                if(infolist.size()==0){
                    ll_none.setVisibility(View.VISIBLE);
                }else{
                    ll_none.setVisibility(View.GONE);
                    adapter.setList(infolist);
                }
                setTitleMessage();
            }
        });
    }
}
