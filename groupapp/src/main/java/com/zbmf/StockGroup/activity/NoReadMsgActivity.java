package com.zbmf.StockGroup.activity;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.adapter.NoReadMsgAdapter;
import com.zbmf.StockGroup.api.JSONHandler;
import com.zbmf.StockGroup.api.WebBase;
import com.zbmf.StockGroup.beans.NoReadMsgBean;
import com.zbmf.StockGroup.utils.JSONParse;
import com.zbmf.StockGroup.utils.ShowActivity;
import com.zbmf.StockGroup.view.ListViewForScrollView;
import com.zbmf.StockGroup.view.PullToRefreshBase;
import com.zbmf.StockGroup.view.PullToRefreshScrollView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class NoReadMsgActivity extends BaseActivity implements NoReadMsgAdapter.SkipNextView{
    private PullToRefreshScrollView mNoReadMsgScrollview;
    private int page = 1;
    private int mTotal;
    private boolean isFirst = true;
    private LinearLayout mLl_none;
    private NoReadMsgAdapter mMsgAdapter;
    private TextView mNo_message_text;

    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_read_msg);
    }*/

    @Override
    public int getLayoutResId() {
        return R.layout.activity_no_read_msg;
    }

    @Override
    public void initView() {
        initTitle("消息");
        mLl_none = getView(R.id.ll_none);
        mNo_message_text = getView(R.id.no_message_text);
        mNo_message_text.setText("暂无消息记录");
        mLl_none.setVisibility(View.VISIBLE);

        mNoReadMsgScrollview = getView(R.id.noReadMsgScrollview);
        ListViewForScrollView noReadMsgListView = getView(R.id.noReadMsgListView);
        mMsgAdapter = new NoReadMsgAdapter(this);
        mMsgAdapter.setSkipNextView(this);
        noReadMsgListView.setAdapter(mMsgAdapter);

        mNoReadMsgScrollview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page = 1;
                getNoReadMsgList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                if (mMsgAdapter.getList() != null) {
                    if (mMsgAdapter.getList().size() == mTotal) {
                        showToast("暂无更多消息");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mNoReadMsgScrollview.onRefreshComplete();
                            }
                        },1000);
                    } else {
                        page += 1;
                        getNoReadMsgList();
                    }
                }
            }
        });

    }

    @Override
    public void initData() {
        getNoReadMsgList();
    }

    @Override
    public void addListener() {

    }

    //获取未读消息列表
    private void getNoReadMsgList() {
        WebBase.getNoReadMsgList(String.valueOf(page), new JSONHandler(isFirst, this, getString(R.string.loading)) {

            @Override
            public void onSuccess(JSONObject obj) {
                mNoReadMsgScrollview.onRefreshComplete();
                if (obj.optString("status").equals("ok")) {
                    page = obj.optInt("page");
                    mTotal = obj.optInt("total");
                    JSONArray msg = obj.optJSONArray("msg");
                    List<NoReadMsgBean> noMsgList = JSONParse.getNoMsgList(msg);
                    if (page <= 1) {
                        mMsgAdapter.setList(noMsgList);
                    } else {
                        mMsgAdapter.addList(noMsgList);
                    }
                    isFirst = false;
                    if (mTotal == 0) {
                        mLl_none.setVisibility(View.VISIBLE);
                        mNo_message_text.setText("暂无消息记录");
                    } else if (mTotal > 0) {
                        mLl_none.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(String err_msg) {
                mNoReadMsgScrollview.onRefreshComplete();
                Log.e("--TAG", err_msg);
            }
        });
    }

    @Override
    public void skipNextView(NoReadMsgBean noReadMsgBean,int type) {
        getPointDetail(noReadMsgBean,type);
    }

    private void getPointDetail(final NoReadMsgBean noReadMsgBean,final int type){
        if (!TextUtils.isEmpty(String.valueOf(noReadMsgBean.getViewpoint_id()))) {
            WebBase.getGDDetail(String.valueOf(noReadMsgBean.getViewpoint_id()),
                    new JSONHandler(true,this,"核查中...") {
                @Override
                public void onSuccess(JSONObject obj) {
                    if (type==1){
                        ShowActivity.skipGDDetailActivity(NoReadMsgActivity.this,
                                String.valueOf(noReadMsgBean.getViewpoint_id()));
                    }else if (type==2){
                        ShowActivity.skipTopicDetailActivty(NoReadMsgActivity.this,
                                String.valueOf(noReadMsgBean.getTopic_id()),"",0);
                    }
                }

                @Override
                public void onFailure(String err_msg) {
                    if (err_msg.equals("Object not found")){
                        showToast("该观点已删除");
                    }
                }
            });
        }
    }
}
