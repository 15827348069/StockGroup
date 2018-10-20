package com.zbmf.StockGroup.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.adapter.BlogPingAdapter;
import com.zbmf.StockGroup.api.JSONHandler;
import com.zbmf.StockGroup.api.WebBase;
import com.zbmf.StockGroup.beans.BlogBean;
import com.zbmf.StockGroup.beans.BlogPingBean;
import com.zbmf.StockGroup.constans.IntentKey;
import com.zbmf.StockGroup.constans.RequestCode;
import com.zbmf.StockGroup.dialog.EditTextDialog;
import com.zbmf.StockGroup.dialog.TextDialog;
import com.zbmf.StockGroup.interfaces.DialogYesClick;
import com.zbmf.StockGroup.utils.SettingDefaultsManager;
import com.zbmf.StockGroup.utils.ShowActivity;
import com.zbmf.StockGroup.view.PullToRefreshBase;
import com.zbmf.StockGroup.view.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.zbmf.StockGroup.utils.ShowActivity.showActivityForResult;

public class BlogPingActivity extends BaseActivity implements View.OnClickListener {
    private BlogPingAdapter adapter;
    private PullToRefreshListView blog_ping_list;
    private List<BlogPingBean>infolist;
    private BlogBean blogBean;
    private int page,pages;
    private Dialog edit_dialog;
    private LinearLayout no_message;
    private TextView no_message_text;
    @Override
    public int getLayoutResId() {
        return R.layout.activity_blog_ping;
    }

    @Override
    public void initView() {
        initTitle();
        blog_ping_list= (PullToRefreshListView) findViewById(R.id.my_blog_ping_list);
        no_message= (LinearLayout) findViewById(R.id.ll_none);
        no_message_text= (TextView) findViewById(R.id.no_message_text);
        blog_ping_list.setMode(PullToRefreshBase.Mode.BOTH);
//        blog_ping_list.getLoadingLayoutProxy().setPullLabel("加载更多数据");
//        blog_ping_list.getLoadingLayoutProxy().setRefreshingLabel("正在加载新数据...");
//        blog_ping_list.getLoadingLayoutProxy().setReleaseLabel("松开加载数据");

        if(edit_dialog==null){
            edit_dialog=Editdialog1();
            edit_dialog.show();
        }
        edit_dialog.dismiss();
    }

    public void initData(){
        blogBean= (BlogBean) getIntent().getSerializableExtra("blogBean");
        infolist=new ArrayList<>();
        adapter=new BlogPingAdapter(this,infolist);
        blog_ping_list.setAdapter(adapter);
        rush();
    }
    private void rush(){
        page=1;
        pages=0;
        getBlog_ping_message(true);
    }
    @Override
    public void addListener() {
        findViewById(R.id.blog_detail_pinglun).setOnClickListener(this);
        blog_ping_list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                //下拉刷新
                rush();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                //上拉加载
                page+=1;
                getBlog_ping_message(false);
            }
        });
    }

    public void getBlog_ping_message(final boolean clear){
        WebBase.getUserBlogPosts(blogBean.getBlog_id(), page, new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                JSONObject result=obj.optJSONObject("result");
                page=result.optInt("page");
                pages=result.optInt("pages");
                JSONArray posts=result.optJSONArray("posts");
                int size=posts.length();
                if(clear){
                    infolist.clear();
                }
                for(int i=0;i<size;i++){
                    JSONObject post=posts.optJSONObject(i);
                    BlogPingBean bp=new BlogPingBean();
                    bp.setContent(post.optString("content"));
                    bp.setId(post.optString("id"));
                    bp.setPost_id(post.optString("post_id"));
                    bp.setPosted_at(post.optString("posted_at"));
                    JSONObject user=post.optJSONObject("author");
                    bp.setUser_id(user.optString("id"));
                    bp.setUser_avatar(user.optString("avatar"));
                    bp.setUser_nickname(user.optString("nickname"));
                    infolist.add(bp);
                }
                if(infolist.size()==0&&page==1){
                    if(no_message.getVisibility()==View.GONE){
                        no_message.setVisibility(View.VISIBLE);
                    }
                    no_message_text.setText("暂无评论");
                }else{
                    if(no_message.getVisibility()==View.VISIBLE){
                        no_message.setVisibility(View.GONE);
                    }
                    adapter.notifyDataSetChanged();
                }
                blog_ping_list.onRefreshComplete();
            }

            @Override
            public void onFailure(String err_msg) {
                blog_ping_list.onRefreshComplete();
                Toast.makeText(getBaseContext(),err_msg,Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.blog_detail_pinglun:
                if(ShowActivity.isLogin(this)){
                    if(TextUtils.isEmpty(SettingDefaultsManager.getInstance().getUserPhone())){
                        TextDialog.createDialog(this)
                                .setTitle("")
                                .setMessage("为响应国家政策，请于操作前绑定手机信息！")
                                .setLeftButton("下次再说")
                                .setRightButton("前往绑定")
                                .setRightClick(new DialogYesClick() {
                                    @Override
                                    public void onYseClick() {
                                        Bundle bundle=new Bundle();
                                        bundle.putInt(IntentKey.FLAG,1);
                                        showActivityForResult(BlogPingActivity.this,bundle, BindPhoneActivity.class, RequestCode.BINDED);
                                    }
                                })
                                .show();
                    }else{
                        if(edit_dialog==null){
                            edit_dialog=Editdialog1();
                        }
                        edit_dialog.show();
                    }
                }
                break;
        }
    }
    private Dialog Editdialog1(){
        return EditTextDialog
                .createDialog(this, R.style.myDialogTheme)
                .setRightButton(getString(R.string.send_button))
                .setEditHint(getString(R.string.blog_message))
                .setSendClick(new EditTextDialog.OnSendClick() {
                    @Override
                    public void onSend(String message,int type) {
                        sendPing(message);
                    }
                });
    }
    public void sendPing(final String message){
        WebBase.createUserBlogPost(blogBean.getBlog_id(), message, new JSONHandler(true,BlogPingActivity.this,"正在提交...") {
            @Override
            public void onSuccess(JSONObject obj) {
                Toast.makeText(getBaseContext(),"评论成功",Toast.LENGTH_SHORT).show();
                edit_dialog.dismiss();
                rush();
            }

            @Override
            public void onFailure(String err_msg) {
                Toast.makeText(getBaseContext(),"评论失败",Toast.LENGTH_SHORT).show();
                edit_dialog.dismiss();
                rush();
            }
        });
    }
}
