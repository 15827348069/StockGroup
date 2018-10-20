package com.zbmf.StocksMatch.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.adapter.AllMatchAdapter;
import com.zbmf.StocksMatch.adapter.PointPagerAdapter;
import com.zbmf.StocksMatch.api.Get2Api;
import com.zbmf.StocksMatch.api.Get2ApiImpl;
import com.zbmf.StocksMatch.beans.Group;
import com.zbmf.StocksMatch.beans.MatchBean;
import com.zbmf.StocksMatch.beans.RecommendPic;
import com.zbmf.StocksMatch.beans.User;
import com.zbmf.StocksMatch.utils.DataLoadDirection;
import com.zbmf.StocksMatch.utils.SharedPreferencesUtils;
import com.zbmf.StocksMatch.utils.UiCommon;
import com.zbmf.StocksMatch.widget.CustomListView;
import com.zbmf.StocksMatch.widget.LoadingDialog;
import com.zbmf.StocksMatch.widget.pulltorefresh.PullToRefreshBase;
import com.zbmf.StocksMatch.widget.pulltorefresh.PullToRefreshScrollView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private CustomListView listView;
    private PullToRefreshScrollView myscrllview;
    private ImageView iv_right;
    private LinearLayout ll_top, point_group;// 头部轮播父布局,点父布局
    private ViewPager viewpager;
    private TextView tv_num;//比赛数

    private PointPagerAdapter adapter;
    private ArrayList<ImageView> imageList = null;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private List<RecommendPic> recommandPics = new ArrayList<RecommendPic>();// 推荐图片列表
    private List<MatchBean> matchList = new ArrayList<MatchBean>();//推荐比赛列表
    private AllMatchAdapter rmAdapter;

    private Get2Api server = null;
    private SharedPreferencesUtils sp;
    private View rootView;
    DisplayImageOptions options;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.index_home)
                .showImageForEmptyUri(R.drawable.index_home)
                .showImageOnFail(R.drawable.index_home)
                .cacheInMemory(true).cacheOnDisc(true)
                .bitmapConfig(Bitmap.Config.ARGB_8888) // 设置图片的解码类型
                .displayer(new RoundedBitmapDisplayer(0)).build();
        if(rootView==null){
            rootView=inflater.inflate(R.layout.activity_home1, null);
        }
        //缓存的rootView需要判断是否已经被加过parent， 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }

        return rootView;

    }

    @Override
    public  void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (UiCommon.INSTANCE.getiUser() != null && !TextUtils.isEmpty(UiCommon.INSTANCE.getiUser().getAuth_token())){
            outState.putSerializable("user",UiCommon.INSTANCE.getiUser());
        }
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState){
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null){
            if (savedInstanceState.containsKey("user")){
                User user = (User)savedInstanceState.getSerializable("user");
                UiCommon.INSTANCE.setiUser(user);
            }
        }
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sp = new SharedPreferencesUtils(getActivity());
        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_title.setText(R.string.gc);
        view.findViewById(R.id.iv_back).setVisibility(View.GONE);
        listView = (CustomListView) view.findViewById(R.id.content_view);
        myscrllview = (PullToRefreshScrollView) view.findViewById(R.id.myscrllview);
        myscrllview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        listView.setFocusable(false);
        myscrllview.smoothScrollTo(0, 20);
        myscrllview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                getData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {

            }
        });
        ll_top = (LinearLayout) view.findViewById(R.id.ll_top);
        point_group = (LinearLayout) view.findViewById(R.id.point_group);
        viewpager = (ViewPager) view.findViewById(R.id.viewpager);
        tv_num = (TextView) view.findViewById(R.id.tv_num);
        tv_num.setText(sp.getMatchNum());

        view.findViewById(R.id.btn_all).setOnClickListener(this);
        view.findViewById(R.id.btn_classic).setOnClickListener(this);
        view.findViewById(R.id.btn_add).setOnClickListener(this);
        iv_right = (ImageView) view.findViewById(R.id.iv_right);
        iv_right.setVisibility(View.VISIBLE);
        iv_right.setOnClickListener(this);
        rmAdapter = new AllMatchAdapter(getActivity());rmAdapter.setList(matchList);
        listView.setAdapter(rmAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MatchBean matchbean = (MatchBean) parent.getItemAtPosition(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("matchbean", matchbean);
                if (matchbean.getIs_match_player().equals("1"))//已参赛
                    UiCommon.INSTANCE.showActivity(UiCommon.ACTIVITY_IDX_MATCHDESC, bundle);
                else
                    UiCommon.INSTANCE.showActivity(UiCommon.ACTIVITY_IDX_MATCHDETAIL, bundle);

            }
        });

        if(isFirst){
            recommandPics.add(new RecommendPic());
            initPiont();
        }

//        new GetRecommendPic(getActivity()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private boolean isFirst = true;
    private void getData() {
        new GetRecommendPic(getActivity()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        new GetRecommendMatch(getActivity()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, DataLoadDirection.Refresh, 1);
    }


    private void initPiont() {
        isFirst = false;
        imageList = new ArrayList<ImageView>();
        point_group.removeAllViews();
        for (int i = 0; i < recommandPics.size(); i++) {
            String picpath = recommandPics.get(i).getPic_url();
//			System.out.println(picpath);
            // 初始化图片资源
            ImageView image = new ImageView(getActivity());
            image.setTag(recommandPics.get(i));
            image.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    RecommendPic rp = (RecommendPic) v.getTag();
                    if ("match".equals(rp.getType())) {
                        MatchBean matchBean = rp.getMatch();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("matchbean", matchBean);

                        if ("1".equals(matchBean.getIs_match_player()))//已参赛
                            UiCommon.INSTANCE.showActivity(UiCommon.ACTIVITY_IDX_MATCHDESC, bundle);
                        else
                            UiCommon.INSTANCE.showActivity(UiCommon.ACTIVITY_IDX_MATCHDETAIL, bundle);
                    } else if ("link".equals(rp.getType())) {
                        Bundle bundle = new Bundle();
//                        bundle.putString("title", getString(R.string.classicmatch));
                        bundle.putString("web_url", rp.getLink_url());
                        bundle.putInt("soure_act", 2);
                        UiCommon.INSTANCE.showActivity(UiCommon.ACTIVITY_IDX_ACCOUNT_Web, bundle);
                    } else if ("users".equals(rp.getType())) {
                        Bundle bundle = new Bundle();
                        Group group = new Group();
                        group.setGroup(rp.getUser_id());
                        group.setGid(rp.getUser_id());
                        group.setId(rp.getUser_id());
                        bundle.putSerializable("group", group);
                        UiCommon.INSTANCE.showActivity(UiCommon.ACTIVITY_IDX_USER, bundle);
                    } else {

                    }
                }
            });
            image.setScaleType(ImageView.ScaleType.FIT_XY);
            imageLoader.displayImage(picpath, image,options);
            imageList.add(image);

            // 添加指示点
            ImageView point = new ImageView(getActivity());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            params.rightMargin = 10;
            if (i == recommandPics.size() - 1)
                params.rightMargin = 0;
//          point.setBackgroundResource(R.drawable.point_bg);
            if (i == 0) {
                point.setImageResource(R.drawable.img_dot1);
            } else {
                point.setImageResource(R.drawable.img_dot2);
            }
            point_group.addView(point, params);
        }

        adapter = new PointPagerAdapter(imageList);
        viewpager.setAdapter(adapter);
        viewpager.setOnPageChangeListener(this);
    }

    Handler handler = new Handler();


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                Bundle bundle1 = new Bundle();
                bundle1.putString("title",getString(R.string.addmatch));
                bundle1.putInt("soure_act", 3);
                bundle1.putString("web_url","http://m.zbmf.com/match/create/?user_id="+UiCommon.INSTANCE.getiUser().getUser_id());
                UiCommon.INSTANCE.showActivity(UiCommon.ACTIVITY_IDX_ACCOUNT_Web,bundle1);
                break;
            case R.id.btn_all:
                UiCommon.INSTANCE.showActivity(UiCommon.ACTIVITY_IDX_ALLMATCH,null);
                break;
            case R.id.btn_classic:
                Bundle bundle = new Bundle();
                bundle.putString("title",getString(R.string.classicmatch));
                bundle.putInt("soure_act", 3);
//                bundle.putInt("soure_act", 1);
                bundle.putString("web_url", "http://m.zbmf.com/match/classic");
                UiCommon.INSTANCE.showActivity(UiCommon.ACTIVITY_IDX_ACCOUNT_Web, bundle);

                break;
            case R.id.iv_right://搜索
                UiCommon.INSTANCE.showActivity(UiCommon.ACTIVITY_IDX_MATCHSEARCH,null);
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        int childCount = point_group.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ImageView view = (ImageView) point_group.getChildAt(i);
            if (i == position) {
                view.setImageResource(R.drawable.img_dot1);
            } else {
                view.setImageResource(R.drawable.img_dot2);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private class SwitchTask implements Runnable {
        @Override
        public void run() {
            //获取到当期显示的页面
            if (adapter != null) {
                int item = viewpager.getCurrentItem();
                if (item == adapter.getCount() - 1) {
                    item = 0;
                } else {
                    item++;
                }
                viewpager.setCurrentItem(item);
            }

            handler.postDelayed(this, 5000);
        }
    }

    private class GetRecommendMatch extends LoadingDialog<Integer,MatchBean>{


        public GetRecommendMatch(Context activity) {
            super(activity, false, true);
        }

        @Override
        public MatchBean doInBackground(Integer... params) {
            MatchBean ret = null;

            if(server == null){
                server = new Get2ApiImpl();
            }

            try {
                ret = server.getRecommendMatch();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return ret;
        }

        @Override
        public void onPostExecute(MatchBean ret) {
            super.onPostExecute(ret);
            myscrllview.onRefreshComplete();
        }

        @Override
        public void doStuffWithResult(MatchBean result) {
            if (result != null && result.code != -1) {
                if (result.getStatus()==1) {
                    if(result.getList()!=null && result.getList().size()>0){
                        matchList.clear();
                        rmAdapter.addList(result.getList());
                    }

                } else{
                    UiCommon.INSTANCE.showTip(result.msg);
                }
            }else {
                UiCommon.INSTANCE.showTip(getActivity().getString(R.string.load_fail));
            }
        }
    }

    private class GetRecommendPic extends LoadingDialog<Void,RecommendPic>{

        public GetRecommendPic(Context activity) {
            super(activity, R.string.loading, R.string.load_fail1,false);
        }

        @Override
        public RecommendPic doInBackground(Void... params) {
            RecommendPic recommendPic = null;
            if(server == null){
                server = new Get2ApiImpl();
            }

            try {
                recommendPic = server.getRecommendPic();

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return recommendPic;
        }

        @Override
        public void doStuffWithResult(RecommendPic result) {
            if (result != null && result.code != -1) {
                if (result.getStatus()==1) {
                    if(result.getList()!=null && result.getList().size()>0){
                        recommandPics.clear();
                        recommandPics.addAll(result.getList());
                        if(recommandPics!=null && recommandPics.size()>0){
                            initPiont();
                            StringBuilder sb = new StringBuilder();
                            for(RecommendPic rp : recommandPics){
                                sb.append(rp.getPic_url()).append("@");
                                SharedPreferences.Editor edit = sp.edit();
                                edit.putString(rp.getPic_url(),rp.getLink_url());
                                edit.commit();
                            }
                            sp.setPicUrl(sb.toString().substring(0, sb.toString().length() - 1));
                        }
                        String match_count = result.getMatch_count();
                        tv_num.setText(match_count);
                        sp.setMatchNum(match_count);
                    }
                } else {
//                    UiCommon.INSTANCE.showTip(result.msg);
                }
            }else {
                UiCommon.INSTANCE.showTip(getActivity().getString(R.string.load_fail));
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        handler.postDelayed(new SwitchTask(), 6000);
        getData();

    }

    @Override
    public void onPause() {
        handler.removeCallbacksAndMessages(null);
        super.onPause();
    }


}
