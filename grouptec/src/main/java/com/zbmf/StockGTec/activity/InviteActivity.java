package com.zbmf.StockGTec.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.tauth.Tencent;
import com.zbmf.StockGTec.R;
import com.zbmf.StockGTec.adapter.InviteAdapter;
import com.zbmf.StockGTec.api.JSONHandler;
import com.zbmf.StockGTec.api.WebBase;
import com.zbmf.StockGTec.beans.Invite;
import com.zbmf.StockGTec.utils.JSONparse;
import com.zbmf.StockGTec.utils.ShareUtil;
import com.zbmf.StockGTec.utils.ShowActivity;
import com.zbmf.StockGTec.view.PullToRefreshBase;
import com.zbmf.StockGTec.view.PullToRefreshListView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class InviteActivity extends ExActivity implements View.OnClickListener {

    private List<Invite> list = new ArrayList<>();

    private int page, PAGES;
    private InviteAdapter mAdapter;
    private PullToRefreshListView listview;
    private TextView tv_may;
    private TextView tv_total;
    private TextView tv_valid, tv_null;

    private ShareUtil shareUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);

        shareUtil = new ShareUtil(this);
        findViewById(R.id.group_title_return).setOnClickListener(this);
        findViewById(R.id.ll_w).setOnClickListener(this);
        findViewById(R.id.ll_p).setOnClickListener(this);
        findViewById(R.id.ll_q).setOnClickListener(this);
        findViewById(R.id.ll_x).setOnClickListener(this);
        TextView tv_title = (TextView) findViewById(R.id.group_title_name);
        tv_title.setText("我的邀请");
        tv_title.setVisibility(View.VISIBLE);
        tv_null = (TextView) findViewById(R.id.tv_null);
        tv_may = (TextView) findViewById(R.id.tv_mpay);
        tv_total = (TextView) findViewById(R.id.tv_total);
        tv_valid = (TextView) findViewById(R.id.tv_valid);
        listview = (PullToRefreshListView) findViewById(R.id.listview);
        mAdapter = new InviteAdapter(this, list);
        listview.setAdapter(mAdapter);
        listview.setMode(PullToRefreshBase.Mode.BOTH);
        listview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                getInviteList(1, false);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                getInviteList(2, false);
            }
        });
        getInviteList(1, false);


    }

    private void getInviteList(final int dir, boolean show) {
        if (dir == 2) {
            page++;
        } else
            page = 1;

        WebBase.getInviteList(page, new JSONHandler(show, InviteActivity.this, "正在加载...") {
            @Override
            public void onSuccess(JSONObject obj) {
                listview.onRefreshComplete();
                Invite invite = JSONparse.getInviteList(obj);
                PAGES = invite.pages;
                if (invite != null && invite.getInvite_lists() != null)
                    if (dir == 1)
                        list.clear();
                list.addAll(invite.getInvite_lists());

                if (page == PAGES) {
//                    Toast.makeText(InviteActivity.this, "已加载全部数据", Toast.LENGTH_SHORT).show();
                    listview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                } else
                    listview.setMode(PullToRefreshBase.Mode.BOTH);

                if (list.size() == 0) {
                    listview.setVisibility(View.GONE);
                    tv_null.setVisibility(View.VISIBLE);
                }else{
                    listview.setVisibility(View.VISIBLE);
                    tv_null.setVisibility(View.GONE);
                }
                tv_may.setText(invite.getStat().getMpay() + "魔方宝");
                tv_total.setText((int) invite.getStat().getTotal() + "人");
                tv_valid.setText((int) invite.getStat().getValid() + "人");
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String err_msg) {
                listview.onRefreshComplete();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.group_title_return:
                finish();
                break;
            case R.id.ll_w://微信
                shareUtil.shareToWebchat(SendMessageToWX.Req.WXSceneSession);
                break;
            case R.id.ll_p://朋友圈
                shareUtil.shareToWebchat(SendMessageToWX.Req.WXSceneTimeline);
                break;
            case R.id.ll_q://QQ
                shareUtil.shareToQQ();
                break;
            case R.id.ll_x://新浪
                ShowActivity.showActivity(this, EmptyActivity.class);
                break;
        }
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Tencent.onActivityResultData(requestCode, resultCode, data, ShareUtil.listener);
    }
}
