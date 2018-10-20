package com.zbmf.groupro.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
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

import com.zbmf.groupro.R;
import com.zbmf.groupro.adapter.BlogPingAdapter;
import com.zbmf.groupro.api.JSONHandler;
import com.zbmf.groupro.api.WebBase;
import com.zbmf.groupro.beans.BlogBean;
import com.zbmf.groupro.beans.BlogPingBean;
import com.zbmf.groupro.utils.ShowActivity;
import com.zbmf.groupro.view.PullToRefreshBase;
import com.zbmf.groupro.view.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BlogPingActivity extends AppCompatActivity implements View.OnClickListener {
    private BlogPingAdapter adapter;
    private PullToRefreshListView blog_ping_list;
    private List<BlogPingBean>infolist;
    private BlogBean blogBean;
    private int page,pages;
    private Dialog edit_dialog;
    private LinearLayout no_message;
    private TextView no_message_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_ping);
        blogBean= (BlogBean) getIntent().getSerializableExtra("blogBean");
        init();
        initData();
    }
    public void init(){
        blog_ping_list= (PullToRefreshListView) findViewById(R.id.my_blog_ping_list);
        no_message= (LinearLayout) findViewById(R.id.ll_none);
        no_message_text= (TextView) findViewById(R.id.no_message_text);
        findViewById(R.id.group_title_return).setOnClickListener(this);
        findViewById(R.id.blog_detail_pinglun).setOnClickListener(this);
        blog_ping_list.setMode(PullToRefreshBase.Mode.BOTH);
        blog_ping_list.getLoadingLayoutProxy().setPullLabel("加载更多数据");
        blog_ping_list.getLoadingLayoutProxy().setRefreshingLabel("正在加载新数据...");
        blog_ping_list.getLoadingLayoutProxy().setReleaseLabel("松开加载数据");

        blog_ping_list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                //下拉刷新
                initData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                //上拉加载
                page+=1;
                getBlog_ping_message(false);
            }
        });
        infolist=new ArrayList<>();
        adapter=new BlogPingAdapter(this,infolist);
        blog_ping_list.setAdapter(adapter);
        if(edit_dialog==null){
            edit_dialog=Editdialog1();
            edit_dialog.show();
        }
        edit_dialog.dismiss();
    }
    public void initData(){
        page=1;
        pages=0;
        getBlog_ping_message(true);
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
            case R.id.group_title_return:
                finish();
                break;
            case R.id.blog_detail_pinglun:
                if(ShowActivity.isLogin(this)){
                    if(edit_dialog==null){
                        edit_dialog=Editdialog1();
                    }
                    edit_dialog.show();
                }
                break;
        }
    }
    private Dialog Editdialog1(){
        final Dialog dialog = new Dialog(this, R.style.myDialogTheme);
        final View layout = LayoutInflater.from(this).inflate(R.layout.blog_detail_pinglun_layout, null);
        final EditText blog_detail_pinglun_edit=(EditText) layout.findViewById(R.id.blog_detail_pinglun_edit);
        final Button send= (Button) layout.findViewById(R.id.send_pinglun_layout);
        send.setEnabled(false);
        send.setAlpha(0.5f);
        blog_detail_pinglun_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.equals("")||charSequence.length()==0){
                    send.setEnabled(false);
                    send.setAlpha(0.5f);
                }else{
                    send.setEnabled(true);
                    send.setAlpha(1.0f);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendPing(blog_detail_pinglun_edit);
            }
        });
        dialog.setContentView(layout);
        Window win = dialog.getWindow();
        win.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = android.view.WindowManager.LayoutParams.MATCH_PARENT;
        win.setAttributes(lp);
        win.setWindowAnimations(R.style.dialoganimstyle);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(blog_detail_pinglun_edit.getWindowToken(),0); //强制隐藏键盘
            }
        });
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                showSoftInputFromWindow(blog_detail_pinglun_edit);
            }
        });

        return  dialog;
    }
    /**
     * EditText获取焦点并显示软键盘
     */
    public void showSoftInputFromWindow(EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        edit_dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }
    public void sendPing(final EditText editText){
        WebBase.createUserBlogPost(blogBean.getBlog_id(), editText.getText().toString(), new JSONHandler(true,BlogPingActivity.this,"正在提交...") {
            @Override
            public void onSuccess(JSONObject obj) {
                Toast.makeText(getBaseContext(),"评论成功",Toast.LENGTH_SHORT).show();
                edit_dialog.dismiss();
                editText.setText("");
                initData();
            }

            @Override
            public void onFailure(String err_msg) {
                Toast.makeText(getBaseContext(),"评论失败",Toast.LENGTH_SHORT).show();
                edit_dialog.dismiss();
                initData();
            }
        });
    }
}
