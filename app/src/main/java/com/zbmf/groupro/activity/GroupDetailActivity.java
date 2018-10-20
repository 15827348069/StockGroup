package com.zbmf.groupro.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zbmf.groupro.R;
import com.zbmf.groupro.adapter.GroupViewPageAdapter;
import com.zbmf.groupro.api.JSONHandler;
import com.zbmf.groupro.api.WebBase;
import com.zbmf.groupro.beans.Group;
import com.zbmf.groupro.fragment.GroupDetailAskFragment;
import com.zbmf.groupro.fragment.GroupDetailBlogFragment;
import com.zbmf.groupro.fragment.GroupDetailFansFragment;
import com.zbmf.groupro.fragment.GroupDetailHistoryFragment;
import com.zbmf.groupro.utils.ImageLoaderOptions;
import com.zbmf.groupro.utils.ShowActivity;
import com.zbmf.groupro.view.CustomViewpager;
import com.zbmf.groupro.view.MyScrollView;
import com.zbmf.groupro.view.RoundedCornerImageView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuhao on 2017/2/25.
 */

public class GroupDetailActivity extends AppCompatActivity implements MyScrollView.OnScrollListener, ViewPager.OnPageChangeListener,View.OnClickListener {
    private MyScrollView group_detail_scroll;
    private LinearLayout group_detail_button_layout;
    private List<Fragment> infolist;
    private int LayoutHeight,nameHeight,LayoutBottom;
    private int LayoutTop,nameTop;
    private ImageView icon_live_id;
    private Animation wheelAnimation;
    private TextView group_title_name,group_detail_name,group_detail_tag,group_detail_number,group_detail_desc,group_detail_fans,group_detail_blog_number,group_detail_care_number,live_type_text;
    private LinearLayout group_detail_layout;
    private RadioGroup group_detail_radio,group_top_detail_radio;
    public  CustomViewpager viewPager;
    private Group group_bean;
    private RoundedCornerImageView group_detail_avatar;
    private LinearLayout live_type_layout;
    private LinearLayout group_number_layout;

    private GroupDetailBlogFragment blogFragment;
    private GroupDetailHistoryFragment groupDetailHistoryFragment;
    private GroupDetailAskFragment groupDetailAskFragment;
    private GroupDetailFansFragment groupDetailFansFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_detail_layout);
        group_bean = (Group) getIntent().getSerializableExtra("GROUP");
        init();
    }
    public void init(){
        group_detail_scroll= (MyScrollView) findViewById(R.id.group_detail_scroll);
        group_detail_button_layout= (LinearLayout) findViewById(R.id.group_detail_button_layout);
        group_detail_layout= (LinearLayout) findViewById(R.id.group_detail_layout);
        group_title_name= (TextView) findViewById(R.id.group_title_name);
        group_detail_name= (TextView) findViewById(R.id.group_detail_name);
        group_detail_tag= (TextView) findViewById(R.id.group_detail_tag);
        group_detail_number= (TextView) findViewById(R.id.group_detail_number);
        group_detail_desc= (TextView) findViewById(R.id.group_detail_desc);
        group_detail_fans= (TextView) findViewById(R.id.group_detail_fans);
        group_detail_blog_number= (TextView) findViewById(R.id.group_detail_blog_number);
        group_detail_care_number= (TextView) findViewById(R.id.group_detail_care_number);
        live_type_text= (TextView) findViewById(R.id.live_type_text);
        icon_live_id= (ImageView) findViewById(R.id.icon_live_id);
        viewPager= (CustomViewpager) findViewById(R.id.group_detail_view_page);
        group_detail_avatar= (RoundedCornerImageView) findViewById(R.id.group_detail_avatar);
        live_type_layout= (LinearLayout) findViewById(R.id.live_type_layout);
        group_number_layout= (LinearLayout) findViewById(R.id.group_number_layout);

        findViewById(R.id.blog_menu).setOnClickListener(this);
        findViewById(R.id.fans_menu).setOnClickListener(this);
        findViewById(R.id.ask_menu).setOnClickListener(this);
        findViewById(R.id.my_history).setOnClickListener(this);
        findViewById(R.id.blog_menu_top).setOnClickListener(this);
        findViewById(R.id.fans_menu_top).setOnClickListener(this);
        findViewById(R.id.ask_menu_top).setOnClickListener(this);
        findViewById(R.id.my_history_top).setOnClickListener(this);
        group_detail_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowActivity.showChatActivity(GroupDetailActivity.this,group_bean);
            }
        });
        group_detail_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowActivity.showChatActivity(GroupDetailActivity.this,group_bean);
            }
        });
        findViewById(R.id.group_title_return).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        live_type_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowActivity.showChatActivity(GroupDetailActivity.this,group_bean);
            }
        });
        wheelAnimation = AnimationUtils.loadAnimation(this, R.anim.send_gift_progress_ami);
        group_detail_radio= (RadioGroup) findViewById(R.id.group_detail_radio);
        group_top_detail_radio= (RadioGroup) findViewById(R.id.group_detail_top_radio);
        group_title_name.setAlpha(0f);
        group_detail_scroll.setOnScrollListener(this);
        initfragment();
        initData();
    }
    public void initfragment(){
        blogFragment=GroupDetailBlogFragment.newInstance(group_bean);
        groupDetailFansFragment=GroupDetailFansFragment.newInstance(group_bean);
        groupDetailAskFragment=GroupDetailAskFragment.newInstance(group_bean);
        groupDetailHistoryFragment=GroupDetailHistoryFragment.newInstance(group_bean);
        infolist=new ArrayList<>();
        infolist.add(blogFragment);
        infolist.add(groupDetailFansFragment);
        infolist.add(groupDetailAskFragment);
        infolist.add(groupDetailHistoryFragment);
        GroupViewPageAdapter adapter=new GroupViewPageAdapter(getSupportFragmentManager(),infolist);
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(this);
        viewPager.setOffscreenPageLimit(infolist.size());
        viewPager.resetHeight(0);
        group_detail_radio.check(R.id.blog_menu);
        setGroupMessage();
        setGroupNumber();
    }
    public void initData(){
        getGroupInfo();
        GroupStat();
    }
    private void setGroupMessage(){
        ImageLoader.getInstance().displayImage(group_bean.getAvatar(),group_detail_avatar, ImageLoaderOptions.AvatarOptions());
        if(group_bean.getNick_name()!=null){
            group_title_name.setText(group_bean.getNick_name());
            group_detail_name.setText(group_bean.getNick_name());
        }

        if(group_bean.getCertificate()!=null){
            group_detail_tag.setVisibility(View.VISIBLE);
            group_number_layout.setVisibility(View.VISIBLE);
            group_detail_tag.setText("投资助理");
            group_detail_number.setText(group_bean.getCertificate());
        }else{
            group_detail_tag.setVisibility(View.GONE);
            group_number_layout.setVisibility(View.GONE);
        }
       if(group_bean.getDescription()!=null){
           group_detail_desc.setText("简介："+group_bean.getDescription());
        }
    }
    public void setGroupNumber(){
        group_detail_fans.setText(group_bean.getFans()+"");
        group_detail_blog_number.setText(group_bean.getBlogs()+"");
        group_detail_care_number.setText(group_bean.getFollows()+"");
    }
    public void getGroupInfo(){
        WebBase.groupInfo(group_bean.getId(), new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj){
                JSONObject group=obj.optJSONObject("group");
                group_bean.setId(group.optString("id"));
                group_bean.setName(group.optString("name"));
                group_bean.setNick_name(group.optString("nickname"));
                group_bean.setAvatar(group.optString("avatar"));
                group_bean.setIs_close(group.optInt("is_close"));
                group_bean.setIs_private(group.optInt("is_private"));
                group_bean.setDescription(group.optString("description"));
                group_bean.setCertificate(group.optString("certificate"));
                group_bean.setRoles(group.optInt("role"));
                if((group.optInt("is_online"))==0){
                    icon_live_id.setImageResource(R.drawable.icon_live_nomal);
                    live_type_text.setText("圈主正在休息中");
                }else{
                    icon_live_id.setImageResource(R.drawable.icon_live);
                    icon_live_id.startAnimation(wheelAnimation);
                    live_type_text.setText("正在直播："+group.optString("content"));
                }
                setGroupMessage();
            }
            @Override
            public void onFailure(String err_msg) {
                Toast.makeText(GroupDetailActivity.this,err_msg,Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void GroupStat(){
        WebBase.GroupStat(group_bean.getId(), new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                group_bean.setBlogs(obj.optJSONObject("stat").optInt("blogs"));
                group_bean.setFans(obj.optJSONObject("stat").optInt("fans"));
                group_bean.setFollows(obj.optJSONObject("stat").optInt("follows"));
                setGroupNumber();
            }
            @Override
            public void onFailure(String err_msg) {

            }
        });
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){
            LayoutHeight = group_detail_button_layout.getHeight()/2;
            LayoutTop = group_detail_button_layout.getTop();
            nameHeight=group_detail_name.getHeight();
            nameTop=group_detail_name.getTop();
            LayoutBottom=group_top_detail_radio.getBottom();
        }
    }
    private void showSuspend(){
        if(group_detail_layout.getVisibility()==View.GONE){
            group_detail_layout.setVisibility(View.VISIBLE);
            group_title_name.setAlpha(1f);
            viewPager.setMinimumHeight(LayoutBottom);
        }
    }

    private void removeSuspend(){
        if(group_detail_layout.getVisibility()==View.VISIBLE){
            group_detail_layout.setVisibility(View.GONE);
            viewPager.setMinimumHeight(0);
        }
    }

    @Override
    public void onScroll(int scrollY) {
        if(scrollY>=nameTop+nameHeight&&scrollY<LayoutTop){
            float scale = (float) scrollY / (nameTop+nameHeight);
            group_title_name.setAlpha(scale);
        }else if(scrollY >=LayoutTop + LayoutHeight){
            showSuspend();
        }else if(scrollY <LayoutTop + LayoutHeight){
            removeSuspend();
        }if(scrollY==0){
            group_title_name.setAlpha(0f);
            removeSuspend();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position){
            case 0:
                group_detail_radio.check(R.id.blog_menu);
                group_top_detail_radio.check(R.id.blog_menu_top);
                break;
            case 1:
                group_detail_radio.check(R.id.fans_menu);
                group_top_detail_radio.check(R.id.fans_menu_top);
                break;
            case 2:
                group_detail_radio.check(R.id.ask_menu);
                group_top_detail_radio.check(R.id.ask_menu_top);
                break;
            case 3:
                group_detail_radio.check(R.id.my_history);
                group_top_detail_radio.check(R.id.my_history_top);
                break;
        }
        viewPager.resetHeight(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View view) {
        int index=0;
        switch (view.getId()){
            case R.id.blog_menu:
                index=0;
                break;
            case R.id.fans_menu:
                index=1;
                break;
            case R.id.ask_menu:
                index=2;
                break;
            case R.id.my_history:
                index=3;
                break;
            case R.id.blog_menu_top:
                index=0;
                break;
            case R.id.fans_menu_top:
                index=1;
                break;
            case R.id.ask_menu_top:
                index=2;
                break;
            case R.id.my_history_top:
                index=3;
                break;
        }
//        viewPager.resetHeight(index);
        viewPager.setCurrentItem(index,false);
    }
}
