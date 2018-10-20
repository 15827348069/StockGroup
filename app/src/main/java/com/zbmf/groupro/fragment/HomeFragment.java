package com.zbmf.groupro.fragment;


import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.zbmf.groupro.GroupActivity;
import com.zbmf.groupro.R;
import com.zbmf.groupro.activity.RecommendActivity;
import com.zbmf.groupro.activity.SearchActivity;
import com.zbmf.groupro.adapter.CycleViewPager;
import com.zbmf.groupro.adapter.GalleryAdapter;
import com.zbmf.groupro.adapter.HeadMessageAdapter;
import com.zbmf.groupro.api.AppUrl;
import com.zbmf.groupro.api.JSONHandler;
import com.zbmf.groupro.api.WebBase;
import com.zbmf.groupro.beans.BlogBean;
import com.zbmf.groupro.beans.Group;
import com.zbmf.groupro.beans.HomeImage;
import com.zbmf.groupro.utils.ShowActivity;
import com.zbmf.groupro.utils.TimeOnItemClickListener;
import com.zbmf.groupro.utils.ViewFactory;
import com.zbmf.groupro.view.ListViewForScrollView;
import com.zbmf.groupro.view.PullToRefreshBase;
import com.zbmf.groupro.view.PullToRefreshScrollView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuhao on 2017/2/13.
 */

public class HomeFragment extends GroupBaseFragment implements View.OnClickListener{
    private CycleViewPager cycleViewPager;
    private List<HomeImage> url_list;
    private List<ImageView> views;
    private List<Group>infolist;
    private List<BlogBean>bloglist;
    private RecyclerView home_tuijian;
    private GalleryAdapter adapter;
    private HeadMessageAdapter headadapter;
    private ListViewForScrollView home_mf_head_message;
    private PullToRefreshScrollView home_scrollview;
    private int page,pages;
    public boolean first_onload_url,first_onload_infolist,first_onload_bloglist;
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    protected View setContentView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.home_fragment_layout,null);
    }

    @Override
    protected void initView() {
        //初始化控件
        home_tuijian=getView(R.id.homt_tuijian);
        home_mf_head_message=getView(R.id.home_mf_head_message);
        home_scrollview=getView(R.id.home_scrollview);
        getView(R.id.more_teacher_button).setOnClickListener(this);
        getView(R.id.search_button).setOnClickListener(this);
//        getView(R.id.stock_live).setOnClickListener(this);
//        getView(R.id.stock_study).setOnClickListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        home_tuijian.setLayoutManager(linearLayoutManager);
        views = new ArrayList<ImageView>();
        url_list=new ArrayList<>();
        infolist=new ArrayList<>();
        bloglist=new ArrayList<>();
        adapter=new GalleryAdapter(getActivity(),infolist);
        adapter.setOnItemClickLitener(new GalleryAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                getGroupInfo(infolist.get(position));
                ((GroupActivity)getActivity()).updateCareData();
            }
        });
        home_tuijian.setAdapter(adapter);
        headadapter=new HeadMessageAdapter(getActivity(),bloglist);
        home_mf_head_message.setAdapter(headadapter);
        home_mf_head_message.setOnItemClickListener(new TimeOnItemClickListener() {
            @Override
            public void onNoDoubleClick(int position) {
                ShowActivity.showBlogDetailActivity(getActivity(),bloglist.get(position));
            }
        });
        home_scrollview.setMode(PullToRefreshBase.Mode.BOTH);
        home_scrollview.getLoadingLayoutProxy().setPullLabel("加载更多数据");
        home_scrollview.getLoadingLayoutProxy().setRefreshingLabel("正在加载新数据...");
        home_scrollview.getLoadingLayoutProxy().setReleaseLabel("松开加载数据");
        home_scrollview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                //下拉刷新
                first_onload_url=true;
                first_onload_bloglist=true;
                first_onload_infolist=true;
                initData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                //上拉加载
                page+=1;
                getBlog_message();
            }
        });
    }

    @Override
    protected void initData() {
        //初始化数据
        page=1;
        pages=0;
        getImageUrl();
        getHomeTeacher();
        getBlog_message();
    }
    public void rushData(){
        if(!home_scrollview.isRefreshing()){
            home_scrollview.getRefreshableView().fullScroll(ScrollView.FOCUS_UP);
            home_scrollview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
            home_scrollview.setRefreshing();
            first_onload_url=true;
            first_onload_bloglist=true;
            first_onload_infolist=true;
            initData();
            home_scrollview.setMode(PullToRefreshBase.Mode.BOTH);
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.more_teacher_button:
                ShowActivity.showActivity(getActivity(), RecommendActivity.class);
                break;
            case R.id.search_button:
                ShowActivity.showActivity(getActivity(), SearchActivity.class);
                break;
            case R.id.stock_live:
                ShowActivity.showWebViewActivity(getActivity(), AppUrl.STOCK_LIVE);
                break;
            case R.id.stock_study:
                GroupActivity groupActivity= (GroupActivity) getActivity();
                groupActivity.selectViewPage(2);
                break;
        }
    }
    public void getImageUrl(){
        WebBase.getAdverts(new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                JSONArray adverts=obj.optJSONArray("adverts");
                int size=adverts.length();
                if(first_onload_url){
                    first_onload_url=false;
                    url_list.clear();
                }
                for(int i=0;i<size;i++){
                    JSONObject oj=adverts.optJSONObject(i);
                    if(oj!=null){
                        HomeImage h=new HomeImage();
                        h.setType("link");
                        h.setId(oj.optString("advert_id"));
                        h.setTitle(oj.optString("subject"));
                        h.setLink_url(oj.optString("jump_url"));
                        h.setImg_url(oj.optString("img_url"));
                        url_list.add(h);
                    }
                    up_data_img();
                }
            }

            @Override
            public void onFailure(String err_msg) {

            }
        });
    }
    public void up_data_img() {
        if(cycleViewPager==null&&getActivity().getFragmentManager()!=null)
        {
            cycleViewPager =(CycleViewPager) getActivity().getFragmentManager().findFragmentById(R.id.fragment_cycle_viewpager_content);
        }
        views.clear();
        for (int i = 0; i < url_list.size(); i++) {
            views.add(ViewFactory.getImageView(getActivity(), url_list.get(i).getImg_url()));
        }
        cycleViewPager.setCycle(true);
        cycleViewPager.setData(views, url_list, mAdCycleViewListener);
        cycleViewPager.setWheel(true);
        cycleViewPager.setTime(2000);
        cycleViewPager.setIndicatorCenter();
    }
    private CycleViewPager.ImageCycleViewListener mAdCycleViewListener = new CycleViewPager.ImageCycleViewListener() {
        @Override
        public void onImageClick(HomeImage info, int position, View imageView) {
            if (cycleViewPager.isCycle()) {
                position = position - 1;
            }
            String url=info.getLink_url();
            String[] web_url = url.split("/");
            if(url.startsWith("mqqwpa://")){
                //跳转到客服qq
                String qq_url="mqqwpa://im/chat?chat_type=crm&uin=2852273339&version=1&src_type=web&web_src=http:://wpa.b.qq.com";
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(qq_url)));
            }else if(web_url.length==4&&url.startsWith("app://group/")){
                //进入圈子
                Group gooup=new Group();
                gooup.setId(web_url[3]);
                getGroupInfo(gooup);
            }else if(web_url.length==7&&url.startsWith("app://app/people/")){
                //进入博文
                getBlogDetail(web_url[4]+"-"+web_url[6]);
            }else{
                ShowActivity.showWebViewActivity(getActivity(),info.getLink_url());
            }
        }
    };
    public void getGroupInfo(Group groupbean){
        //已关注进入直播室，未关注进入简介
//        if(!groupbean.is_recommend()){
//            ShowActivity.showChatActivity(getActivity(),groupbean);
//        }else{
//            ShowActivity.showGroupDetailActivity(getActivity(),groupbean);
//        }

        ShowActivity.showGroupDetailActivity(getActivity(),groupbean);
    }
    public void getBlogDetail(String blog_id){
        WebBase.searchUserBlogInfo(blog_id, new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                BlogBean blogBean=new BlogBean();
                blogBean.setBlog_id(obj.optString("blog_id"));
                blogBean.setTitle(obj.optString("subject"));
                blogBean.setImg(obj.optString("cover"));
                blogBean.setLook_number(obj.optJSONObject("stat").optString("views"));
                blogBean.setPinglun(obj.optJSONObject("stat").optString("replys"));
                blogBean.setWap_link(obj.optJSONObject("link").optString("wap"));
                blogBean.setApp_link(obj.optJSONObject("link").optString("app"));
                blogBean.setDate(obj.optString("posted_at"));
                ShowActivity.showBlogDetailActivity(getActivity(),blogBean);
            }
            @Override
            public void onFailure(String err_msg) {

            }
        });
    }
    private void getHomeTeacher(){
        WebBase.recommend(new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                JSONArray recommend=obj.optJSONObject("result").optJSONArray("groups");
                int size=recommend.length();
                if(first_onload_infolist){
                    first_onload_infolist=false;
                    infolist.clear();
                }
                for(int i=0;i<size;i++){
                    JSONObject oj=recommend.optJSONObject(i);
                    Group group=new Group();
                    group.setId(oj.optString("id"));
                    group.setName(oj.optString("name"));
                    group.setNick_name(oj.optString("nickname"));
                    group.setAvatar(oj.optString("avatar"));
                    group.setDescription(oj.optString("content"));
                    group.setIs_private(oj.optInt("is_private"));
                    group.setIs_close(oj.optInt("is_close"));

                    if(oj.optInt("is_followed")==0){
                        //未关注
                        group.setIs_recommend(true);
                    }else{
                        //已关注
                        group.setIs_recommend(false);
                    }
                    infolist.add(group);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String err_msg) {

            }
        });
    }
    private void getBlog_message(){
        if(page==pages){
            showToast("已加载全部数据");
            home_scrollview.onRefreshComplete();
            return;
        }
        WebBase.searchUserBlogs(page, new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                JSONObject result=obj.optJSONObject("result");
                page=result.optInt("page");
                pages=result.optInt("pages");
                if(first_onload_bloglist){
                    first_onload_bloglist=false;
                    bloglist.clear();
                }
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
                    blogBean.setAvatar(blog.optJSONObject("user").optString("avatar"));
                    blogBean.setName(blog.optJSONObject("user").optString("nickname"));
                    blogBean.setApp_link(blog.optJSONObject("link").optString("app"));
                    blogBean.setWap_link(blog.optJSONObject("link").optString("wap"));
                    blogBean.setBlog_id(blog.optString("blog_id"));
                    bloglist.add(blogBean);
                }
                headadapter.notifyDataSetChanged();
                home_scrollview.onRefreshComplete();
            }
            @Override
            public void onFailure(String err_msg) {
                showToast(err_msg);
                home_scrollview.onRefreshComplete();
            }
        });
    }
}
