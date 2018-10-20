package com.zbmf.StockGroup.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.adapter.HtAdapter;
import com.zbmf.StockGroup.api.JSONHandler;
import com.zbmf.StockGroup.api.WebBase;
import com.zbmf.StockGroup.beans.MyTopicData;
import com.zbmf.StockGroup.constans.IntentKey;
import com.zbmf.StockGroup.utils.ShowActivity;
import com.zbmf.StockGroup.view.ListViewForScrollView;
import com.zbmf.StockGroup.view.PullToRefreshBase;
import com.zbmf.StockGroup.view.PullToRefreshScrollView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * <p>
 * 关注Fragment
 */
public class GzFragment extends BaseFragment implements HtAdapter.HtItemClick{

    private PullToRefreshScrollView gzScrollview;
    private ListViewForScrollView gzListView;
    private HtAdapter mHtAdapter;
    private ArrayList<MyTopicData> myTopicsList = null;
    private LinearLayout mLl_none;
    private boolean isE = false;
    private boolean isFirst=true;
    private TextView mNo_message_text;

    public static GzFragment GzInstance() {
        GzFragment gzFragment = new GzFragment();
        return gzFragment;
    }

    public GzFragment() {

    }

    @Override
    protected View setContentView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_gz, null);
        mLl_none = view.findViewById(R.id.ll_none);
        mNo_message_text = view.findViewById(R.id.no_message_text);
        if (!isE) {
            mNo_message_text.setText("暂无关注内容");
            mLl_none.setVisibility(View.VISIBLE);
        }

        gzScrollview = view.findViewById(R.id.gzScrollview);
        gzListView = view.findViewById(R.id.gzListView);
        mHtAdapter = new HtAdapter(getActivity());
        mHtAdapter.setHtItemClick(this);
        gzListView.setAdapter(mHtAdapter);

        myTopicsList = new ArrayList<>();
        if (isAdded()) {
            getTopicsList();
        }

        //关注的话题接口没有分页----->此处就不做分页处理
        gzScrollview.setMode(PullToRefreshBase.Mode.DISABLED);
        gzScrollview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {

            }
        });

        //注册广播接受者
        IntentFilter filter = new IntentFilter(IntentKey.RECEIVER_FLAG);
        getActivity().registerReceiver(mBroadcastReceiver,filter);

        return view;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    public void itemClick(int topic_id,String avatar,int status) {
        ShowActivity.skipTopicDetailActivty(getActivity(),String.valueOf(topic_id),avatar,status);
    }

    public void getTopicsList() {
        //获取关注的话题列表
        WebBase.getGzTopicList(new JSONHandler(isFirst, getActivity(), getString(R.string.loading)) {
            @Override
            public void onSuccess(JSONObject obj) {
                if (obj.optString("status").equals("ok")) {
                    myTopicsList.clear();
                    JSONArray data = obj.optJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject jsonObject = data.optJSONObject(i);
                        int topic_id = jsonObject.optInt("topic_id");
                        int type_id = jsonObject.optInt("type_id");
                        int vp_number = jsonObject.optInt("vp_number");
                        int is_hot = jsonObject.optInt("is_hot");
                        int users = jsonObject.optInt("users");
                        int status = jsonObject.optInt("status");
                        String img = jsonObject.optString("img");
                        String name = jsonObject.optString("name");
                        String title = jsonObject.optString("title");
                        String body = jsonObject.optString("body");
                        String created_at = jsonObject.optString("created_at");
                        myTopicsList.add(new MyTopicData(topic_id, type_id, vp_number, is_hot, users,
                                status, img, name, title, body, created_at));
                    }
                    //关注的话题接口没有分页----->此处就不做分页处理
                    //打印日志观察返回的是否有需要显示的数据
//                    Log.i("--TAG", "----关注的话题数据----myTopicsList:" + myTopicsList.size());
                    if (myTopicsList.size() > 0) {
                        mLl_none.setVisibility(View.GONE);
                        isE=true;
                    }else {
                        mNo_message_text.setText("暂无关注内容");
                        mLl_none.setVisibility(View.VISIBLE);
                    }
//                    if (mHtAdapter.getList()==null){
                        mHtAdapter.setList(myTopicsList);
//                    }else {
//                        mHtAdapter.getList().clear();
//                        mHtAdapter.addList(myTopicsList);
//                        mHtAdapter.notifyDataSetChanged();
//                    }
                    isFirst=false;
                }
            }

            @Override
            public void onFailure(String err_msg) {
                Log.e("--TAG", err_msg);
            }
        });
    }

    //声明一个广播接受者
    BroadcastReceiver mBroadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String update = intent.getStringExtra("update");
            if (update.equals("update")){
                //调用接口,刷新数据
                getTopicsList();
//                Log.i("--TAG","----接收广播,更新数据");
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mBroadcastReceiver);
    }
}
