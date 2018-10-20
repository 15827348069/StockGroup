package com.zbmf.groupro.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zbmf.groupro.R;
import com.zbmf.groupro.adapter.ViewPageFragmentadapter;
import com.zbmf.groupro.api.JSONHandler;
import com.zbmf.groupro.api.WebBase;
import com.zbmf.groupro.beans.GiftBean;
import com.zbmf.groupro.beans.Group;
import com.zbmf.groupro.beans.LiveMessage;
import com.zbmf.groupro.fragment.ChatFragment;
import com.zbmf.groupro.fragment.LiveFragment;
import com.zbmf.groupro.utils.ActivityUtil;
import com.zbmf.groupro.utils.Constants;
import com.zbmf.groupro.utils.DisplayUtil;
import com.zbmf.groupro.utils.ImageLoaderOptions;
import com.zbmf.groupro.utils.MessageType;
import com.zbmf.groupro.utils.SettingDefaultsManager;
import com.zbmf.groupro.utils.ShowActivity;
import com.zbmf.groupro.view.CusSeekbar;
import com.zbmf.groupro.view.MagicTextView;
import com.zbmf.groupro.view.MyTextView;
import com.zbmf.groupro.view.NoViewPager;
import com.zbmf.groupro.view.RoundedCornerImageView;
import com.zbmf.groupro.view.SwipeToFinishView;
import com.zbmf.groupro.view.TabLayout;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Chat1Activity extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener{


    private static final String TAG = Chat1Activity.class.getSimpleName();
    private NoViewPager viewpager;
    private ChatFragment mChatFragment;
    private LiveFragment mLiveFragment;
    private List<Fragment> mList;
    private List<String> title_list;


    private TabLayout tableLayout;
    private RoundedCornerImageView head_avatar;
    private TextView head_name;
    private LinearLayout llgiftcontent,live_message;
    private NumAnim giftNumAnim;
    private TranslateAnimation inAnim;
    private TranslateAnimation outAnim;
    private ImageButton care_about_button, group_head_setting, group_head_msg;
    private RelativeLayout title_layout;
    private boolean activity_pause;
    public String GROUP_ID;
    private Dialog mTextSetteingDialog = null;
    private List<View>live_message_view;
    String[] mTitles = new String[]{"直播室", "群聊"};
    public boolean is_fanse;
    private Group group;
    /**
     * 礼物数据相关
     */
    private List<View> giftViewCollection = new ArrayList<View>();
    private PagerAdapter adapter;
    public static final int ADD_VIEW=1;
    public static final int REMOVE_VIEW=2;
    private int select_tab;
    private SwipeToFinishView swipeToFinishView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat1);
        swipeToFinishView=new SwipeToFinishView(this);
        ActivityUtil.getInstance().putActivity(ActivityUtil.CHATACTIVITY, this);
        getGroupMessage();
        setLiveImageW();
        enterGroup();
        initFragment();
        init();
        removeliveview();
    }
    public void getGroupMessage(){
        group = (Group) getIntent().getSerializableExtra("GROUP");
        select_tab = getIntent().getIntExtra("live_or_chat", 0);
        GROUP_ID=group.getId();
        SettingDefaultsManager.getInstance().setCurrentChat(GROUP_ID);
        if(group.getFans_level()<5){
            is_fanse=false;
        }else{
            is_fanse=true;
        }
    }
    public void setLiveImageW(){
        int width=DisplayUtil.getScreenWidthPixels(this);
        if(width>=1000){
            SettingDefaultsManager.getInstance().setLiveImg(Constants.LIVE_IMG_500);
        }else if(width>=600&&width<1000){
            SettingDefaultsManager.getInstance().setLiveImg(Constants.LIVE_IMG_350);
        }else if(width>=320&&width<700){
            SettingDefaultsManager.getInstance().setLiveImg(Constants.LIVE_IMG_200);
        }
    }
    public void enterGroup() {
        WebBase.enterGroup(GROUP_ID,new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                Log.e("TAG", "加入圈子成功");
            }

            @Override
            public void onFailure(String err_msg) {
                Log.e("TAG", err_msg);
            }
        });
    }

    public void leaveGroup() {
        WebBase.leaveGroup(GROUP_ID,new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {

            }

            @Override
            public void onFailure(String err_msg) {
                Log.e("TAG", err_msg);
            }
        });
    }

    private void initFragment() {
        mList = new ArrayList<>();
        title_list = new ArrayList<>();
        mLiveFragment = LiveFragment.newInstance(group);
        mChatFragment = ChatFragment.newInstance(group);
        title_list.add("直播室");
        title_list.add("群聊");
        mList.add(mLiveFragment);
        mList.add(mChatFragment);
    }

    private void init() {

        viewpager = (NoViewPager) findViewById(R.id.viewpager);
        tableLayout = (TabLayout) findViewById(R.id.tablayout);
        tableLayout.setData(mTitles);
        llgiftcontent = (LinearLayout) findViewById(R.id.llgiftcontent);
        inAnim = (TranslateAnimation) AnimationUtils.loadAnimation(this, R.anim.gift_in);
        outAnim = (TranslateAnimation) AnimationUtils.loadAnimation(this, R.anim.gift_out);


        giftNumAnim = new NumAnim();
        adapter = new ViewPageFragmentadapter(getSupportFragmentManager(), title_list, mList);
        viewpager.setAdapter(adapter);
        viewpager.setOnPageChangeListener(this);
//        tableLayout.setupWithViewPager(viewpager);
        title_layout = (RelativeLayout) findViewById(R.id.title_layout);

        head_avatar = (RoundedCornerImageView) findViewById(R.id.group_head_avatar);
        ImageLoader.getInstance().displayImage(group.getAvatar(), head_avatar, ImageLoaderOptions.AvatarOptions());
        head_avatar.setOnClickListener(this);
        findViewById(R.id.group_head_return).setOnClickListener(this);
        group_head_msg = (ImageButton) findViewById(R.id.group_head_msg);
        group_head_msg.setVisibility(View.GONE);
        group_head_msg.setOnClickListener(this);
        group_head_setting = (ImageButton) findViewById(R.id.group_head_setting);
        group_head_setting.setOnClickListener(this);
        care_about_button = (ImageButton) findViewById(R.id.care_about_button);

        live_message= (LinearLayout) findViewById(R.id.live_message);
        care_about_button.setOnClickListener(this);
        tableLayout.setTabSelectListener(new TabLayout.OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                viewpager.setCurrentItem(position);
            }
        });

        clearTiming();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.group_head_return:
                finish();
                break;
            case R.id.group_head_setting:
                //设置按钮
                showSettingLayout();
                break;
            case R.id.group_head_msg:
//                Toast.makeText(getApplicationContext(),"点击消息按钮",Toast.LENGTH_SHORT).show();
                break;
            case R.id.care_about_button:
                //关注按钮
                if(ShowActivity.isLogin(Chat1Activity.this)){
                    if(group.getFans_level()>=5){
                        Toast.makeText(Chat1Activity.this,"无法取消关注",Toast.LENGTH_SHORT).show();
                    }else{
                        follow();
                    }
                }
                break;
            case R.id.group_head_avatar:
                ShowActivity.showGroupDetailActivity(this,group);
                break;
        }
    }

    public void  follow(){
        WebBase.follow(GROUP_ID, new JSONHandler(true,Chat1Activity.this,"正在关注圈主...") {
            @Override
            public void onSuccess(JSONObject obj) {
                Toast.makeText(getBaseContext(),"关注成功",Toast.LENGTH_SHORT).show();
                care_about_button.setVisibility(View.GONE);
                group_head_setting.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(String err_msg) {
                Toast.makeText(getBaseContext(),err_msg,Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 取消关注
     */
    public void  unfollow(){
        WebBase.unfollow(GROUP_ID, new JSONHandler(true,Chat1Activity.this,"正在取消关注圈主...") {
            @Override
            public void onSuccess(JSONObject obj) {
                Toast.makeText(getBaseContext(),"取消关注成功",Toast.LENGTH_SHORT).show();
                care_about_button.setVisibility(View.VISIBLE);
                group_head_setting.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(String err_msg) {
                Toast.makeText(getBaseContext(),err_msg,Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    private int mPosition;

    @Override
    public void onPageSelected(int position) {
//        tableLayout.setRedPointGone(position);
        mPosition = position;
        tableLayout.setTabSelect(position);
        tableLayout.setRedPointGone(position);
        switch (position) {
            case 0:
                if(live_message_view!=null){
                    live_message_view.clear();
                    Message message=new Message();
                    message.what=REMOVE_VIEW;
                    handler.sendMessage(message);
                }
                swipeToFinishView.setclose_activity(true);
                break;
            case 1:
                mLiveFragment.onPause();
                swipeToFinishView.setclose_activity(false);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void setlive_number_text(int position, int number) {
        //如果在当前也  隐藏未读提示
        if (position == mPosition) {
            tableLayout.setRedPointGone(position);
        } else {
            tableLayout.setRedPointVisible(position);
        }
        tableLayout.setRedPointMessage(position, number);
    }

    public void setLiveNumbeGone(int position) {
        tableLayout.setRedPointGone(position);
    }

    /**
     * 显示礼物的方法
     */
    public void showGift(GiftBean gb) {
        View giftView = llgiftcontent.findViewWithTag(gb.getSend_from_user_id() + gb.getGift_id());
        if (giftView == null) {/*该用户不在礼物显示列表*/
            if (llgiftcontent.getChildCount() > 2) {/*如果正在显示的礼物的个数超过两个，那么就移除最后一次更新时间比较长的*/
                View giftView1 = llgiftcontent.getChildAt(0);
//                RoundedCornerImageView picTv1 = (RoundedCornerImageView) giftView1.findViewById(R.id.crvheadimage);
                TextView gift_name = (TextView) giftView.findViewById(R.id.show_gift_name);
                long lastTime1 = (Long) gift_name.getTag();
                View giftView2 = llgiftcontent.getChildAt(1);
                RoundedCornerImageView picTv2 = (RoundedCornerImageView) giftView2.findViewById(R.id.ivgift);
                long lastTime2 = (Long) picTv2.getTag();
                if (lastTime1 > lastTime2) {/*如果第二个View显示的时间比较长*/
                    removeGiftView(1);
                } else {/*如果第一个View显示的时间长*/
                    removeGiftView(0);
                }
            }
            giftView = addGiftView();/*获取礼物的View的布局*/
            giftView.setTag(gb.getSend_from_user_id() + gb.getGift_id());/*设置view标识*/
            RelativeLayout rlparent = (RelativeLayout) giftView.findViewById(R.id.rlparent);
            int i = (int) (Math.random() * 5 + 1);
            switch (i) {
                case 1:
                    rlparent.setBackgroundResource(R.drawable.bg_radius_top_black);
                    break;
                case 2:
                    rlparent.setBackgroundResource(R.drawable.bg_radius_top_red);
                    break;
                case 3:
                    rlparent.setBackgroundResource(R.drawable.bg_radius_top_orange);
                    break;
                case 4:
                    rlparent.setBackgroundResource(R.drawable.bg_radius_top_green);
                    break;
                case 5:
                    rlparent.setBackgroundResource(R.drawable.bg_radius_top_blue);
                    break;
            }
            final MagicTextView giftNum = (MagicTextView) giftView.findViewById(R.id.giftNum);/*找到数量控件*/
            RoundedCornerImageView gift_img = (RoundedCornerImageView) giftView.findViewById(R.id.ivgift);
            TextView gift_name = (TextView) giftView.findViewById(R.id.show_gift_name);
            TextView send_gift_from_username = (TextView) giftView.findViewById(R.id.send_gift_from_username);
            String user_count = "<font color=\"#ffffff\">" + gb.getSend_from_user_name() + "</font>赠送";
            send_gift_from_username.setText(Html.fromHtml(user_count));
            String count = "<font color=\"#ffffff\">" + gb.getGift_name() + "</font>";
            gift_name.setText(Html.fromHtml(count));
            Log.e("getSend_gift_number()","==="+gb.getSend_gift_number());
            giftNum.setText("x" + gb.getSend_gift_number());/*设置礼物数量*/
            gift_name.setTag(System.currentTimeMillis());/*设置时间标记*/
            giftNum.setTag(gb.getSend_gift_number());/*给数量控件设置标记*/
//            ImageLoader.getInstance().displayImage(gb.getGift_icon(), gift_img);
            Glide.with(this).load(gb.getGift_icon()).centerCrop().placeholder(R.drawable.avatar_default).crossFade().into(gift_img);
            llgiftcontent.addView(giftView);/*将礼物的View添加到礼物的ViewGroup中*/
            llgiftcontent.invalidate();/*刷新该view*/
            giftView.startAnimation(inAnim);/*开始执行显示礼物的动画*/
            inAnim.setAnimationListener(new Animation.AnimationListener() {/*显示动画的监听*/
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    giftNumAnim.start(giftNum);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
        } else {/*该用户在礼物显示列表*/
//            RoundedCornerImageView crvheadimage = (RoundedCornerImageView) giftView.findViewById(R.id.crvheadimage);/*找到头像控件*/
            TextView gift_name = (TextView) giftView.findViewById(R.id.show_gift_name);
            MagicTextView giftNum = (MagicTextView) giftView.findViewById(R.id.giftNum);/*找到数量控件*/
            int showNum = (Integer) giftNum.getTag() + gb.getSend_gift_number();
            giftNum.setText("x" + (showNum));
            giftNum.setTag(showNum);
            gift_name.setTag(System.currentTimeMillis());
            giftNumAnim.start(giftNum);
        }
    }

    /**
     * 定时清除礼物
     */
    private void clearTiming() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                long nowtime = System.currentTimeMillis();
                int count = llgiftcontent.getChildCount();
                for (int i = 0; i < count; i++) {
                    View view = llgiftcontent.getChildAt(i);
                    TextView gift_name = (TextView) view.findViewById(R.id.show_gift_name);
                    long upTime = (Long) gift_name.getTag();
                    if ((nowtime - upTime) >= 2000) {
                        removeGiftView(i);
                        return;
                    }
                }
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 0, 2000);
    }
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            View view= (View) msg.obj;
            switch (msg.what){
                case ADD_VIEW:
                    live_message.removeAllViews();
                    if(view!=null){
                        view.setTag(new Date().getTime());
                        live_message.addView(view);
                    }
                    break;
                case REMOVE_VIEW:
                    live_message.removeAllViews();
                    break;
            }
        }
    };
    /**
     * 添加礼物view,(考虑垃圾回收)
     */
    private View addGiftView() {
        View view = null;
        if (giftViewCollection.size() <= 0) {
            /*如果垃圾回收中没有view,则生成一个*/
            view = LayoutInflater.from(this).inflate(R.layout.item_gift, null);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.topMargin = 5;
            view.setLayoutParams(lp);
            llgiftcontent.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                @Override
                public void onViewAttachedToWindow(View view) {
                }

                @Override
                public void onViewDetachedFromWindow(View view) {
                    giftViewCollection.add(view);
                }
            });
        } else {
            view = giftViewCollection.get(0);
            giftViewCollection.remove(view);
        }
        return view;
    }

    /**
     * 删除礼物view
     */
    private void removeGiftView(final int index) {
        final View removeView = llgiftcontent.getChildAt(index);
        outAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                llgiftcontent.removeViewAt(index);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (activity_pause) {
                    try {
                        llgiftcontent.removeViewAt(index);
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                } else {
                    removeView.startAnimation(outAnim);
                }
            }
        });
    }

    /**
     * 数字放大动画
     */
    public class NumAnim {
        private Animator lastAnimator = null;

        public void start(View view) {
            if (lastAnimator != null) {
                lastAnimator.removeAllListeners();
                lastAnimator.end();
                lastAnimator.cancel();
            }
            ObjectAnimator anim1 = ObjectAnimator.ofFloat(view, "scaleX", 1.3f, 1.0f);
            ObjectAnimator anim2 = ObjectAnimator.ofFloat(view, "scaleY", 1.3f, 1.0f);
            AnimatorSet animSet = new AnimatorSet();
            lastAnimator = animSet;
            animSet.setDuration(200);
            animSet.setInterpolator(new OvershootInterpolator());
            animSet.playTogether(anim1, anim2);
            animSet.start();
        }
    }

    public View getActivityView() {
        return getWindow().getDecorView();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        leaveGroup();
        ActivityUtil.getInstance().removeActivity(ActivityUtil.CHATACTIVITY);
        SettingDefaultsManager.getInstance().setCurrentChat("");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mPosition == 1 && (keyCode == 4 || keyCode == 67)) {
            return mChatFragment.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        super.onPause();
        activity_pause = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        activity_pause = false;

    }

    public void showSettingLayout() {
        WindowManager wm = getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        View setting_view = LayoutInflater.from(getBaseContext()).inflate(R.layout.activity_setting_layout, null);
        final PopupWindow popupWindow = new PopupWindow(setting_view, width * 3 / 5, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        setting_view.findViewById(R.id.setting_text_size).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTextSetteingDialog == null)
                    mTextSetteingDialog = showSetText();
                mTextSetteingDialog.show();
                popupWindow.dismiss();

            }
        });
        setting_view.findViewById(R.id.caue_care).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unfollow();
                popupWindow.dismiss();
            }
        });
        setting_view.findViewById(R.id.live_histtory_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowActivity.showLiveHistoryDateActivity(Chat1Activity.this,group);
            }
        });
        SwitchCompat live_switchCompat = (SwitchCompat) setting_view.findViewById(R.id.live_new_msg_vedio);
        live_switchCompat.setChecked(SettingDefaultsManager.getInstance().getNewMessageVedio(GROUP_ID));
        live_switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SettingDefaultsManager.getInstance().setNewMessageVedio(GROUP_ID, b);
            }
        });
        SwitchCompat chat_switchCompat = (SwitchCompat) setting_view.findViewById(R.id.chat_new_msg_vedio);
        chat_switchCompat.setChecked(SettingDefaultsManager.getInstance().getNewChatMessageVedio(GROUP_ID));
        chat_switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SettingDefaultsManager.getInstance().setNewChatMessageVedio(GROUP_ID, b);
            }
        });

        popupWindow.showAsDropDown(title_layout, width, 0);
    }

    private Dialog showSetText() {
        final Dialog dialog = new Dialog(this, R.style.myDialogTheme);
        View layout = getLayoutInflater().inflate(R.layout.setting_text_size, null);
        CusSeekbar seekbar = (CusSeekbar) layout.findViewById(R.id.seekbar);
        ArrayList<String> sections = new ArrayList<String>();
        for (int i = 0; i < 5; i++) {
            if (i == 1)
                sections.add("标准");
            else
                sections.add("");
        }
        seekbar.initData(sections);
        int dimen = SettingDefaultsManager.getInstance().getTextSize();
        switch (dimen) {
            case R.dimen.live_text_size_min_small:
                seekbar.setCur_sections(0);
                break;
            case R.dimen.live_text_size_small:
                seekbar.setCur_sections(1);
                break;
            case R.dimen.live_text_size:
                seekbar.setCur_sections(2);
                break;
            case R.dimen.live_text_size_big:
                seekbar.setCur_sections(3);
                break;
            case R.dimen.live_text_size_max_big:
                seekbar.setCur_sections(4);
                break;
        }

        seekbar.setResponseOnTouch(new CusSeekbar.ResponseOnTouch() {
            @Override
            public void onTouchResponse(int volume) {
                switch (volume) {
                    case 0:
                        SettingDefaultsManager.getInstance().setTextSize(R.dimen.live_text_size_min_small);
                        break;
                    case 1:
                        SettingDefaultsManager.getInstance().setTextSize(R.dimen.live_text_size_small);
                        break;
                    case 2:
                        SettingDefaultsManager.getInstance().setTextSize(R.dimen.live_text_size);
                        break;
                    case 3:
                        SettingDefaultsManager.getInstance().setTextSize(R.dimen.live_text_size_big);
                        break;
                    case 4:
                        SettingDefaultsManager.getInstance().setTextSize(R.dimen.live_text_size_max_big);
                        break;
                }
                mLiveFragment.livenotifyDataSetChanged();
                mChatFragment.livenotifyDataSetChanged();
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
        return dialog;
    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);
    }
    public void addMessage(LiveMessage cm){
        if(mPosition==0){
            return;
        }
        if(live_message_view==null){
            live_message_view=new ArrayList<>();
        }
        View view=LayoutInflater.from(getBaseContext()).inflate(R.layout.live_message_item,null);
        MyTextView textView= (MyTextView) view.findViewById(R.id.chat_new_live_message);
        ImageButton close_live_img_button= (ImageButton) view.findViewById(R.id.close_live_img_button);
        close_live_img_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                live_message_view.clear();
                live_message.removeAllViews();
            }
        });
        String countent=null;
        switch (cm.getMessage_type()){
            case MessageType.CHAT:
                if(cm.getMessage_or_img().equals(MessageType.TXT)){
                    if(!cm.getMessage_countent().trim().equals("")){
                        countent=cm.getMessage_countent();
                    }
                }else{
                    countent=cm.getMessage_countent()+"[图片消息]";
                }
                break;
            case MessageType.FANS:
                if(is_fanse){
                    if(cm.getMessage_or_img().equals(MessageType.TXT)){
                        if(!cm.getMessage_countent().trim().equals("")){
                            countent=cm.getMessage_countent();
                        }
                    }else{
                        countent=cm.getMessage_countent()+"[图片消息]";
                    }
                }else{
                    countent=getResources().getString(R.string.add_to_tf);
                }
                break;
            case MessageType.RED_PACKET:
                countent="有红包了";
                Drawable nav_up=getResources().getDrawable(R.drawable.icon_live_redbag);
                nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
                textView.setCompoundDrawables(nav_up,null,null,null);
                break;
        }
        if(countent!=null){
            textView.setMessageText(cm.getMessage_name()+":"+countent);
            live_message_view.add(view);
        }
    }
    public void removeliveview(){
        TimerTask task = new TimerTask(){
            public void run(){
                    //execute the task
                if(live_message_view!=null){
                    long nowtime =new Date().getTime();
                    Message message=new Message();
                    int size=live_message_view.size();
                    if(size>0){
                        int lve_layout=live_message.getChildCount();
                        if(lve_layout>0){
                            View view=live_message.getChildAt(0);
                            long time= (long) view.getTag();
                            if((nowtime-time)>=5000){
                                live_message_view.remove(0);
                                message.what=REMOVE_VIEW;
                                handler.sendMessage(message);
                                if(live_message_view.size()>0){
                                    message=new Message();
                                    message.what=ADD_VIEW;
                                    message.obj=live_message_view.get(0);
                                    handler.sendMessage(message);
                                }
                            }
                        }else{
                            message.what=ADD_VIEW;
                            message.obj=live_message_view.get(0);
                            handler.sendMessage(message);
                        }
                    }
                }
                   }
            };
        Timer timer = new Timer();
        timer.schedule(task, 0, 2000);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constants.FANS_MESSAGE:
                if(data!=null){
                    Bundle b=data.getExtras();
                    is_fanse=b.getBoolean("is_fans");
                    mLiveFragment.setTFmessage(is_fanse);
                }
                break;
            default:
                break;
        }
    }
    public void setGroupMessage(Group groupbean){
        this.group=groupbean;
        if(group.getFans_level()<5){
            is_fanse=false;
        }else{
            is_fanse=true;
        }
//        if(group.getRoles()==0||group.getRoles()==5||group.getRoles()==1){
//            group_head_setting.setVisibility(View.GONE);
//            care_about_button.setVisibility(View.VISIBLE);
//        }else{
//            care_about_button.setVisibility(View.GONE);
//            group_head_setting.setVisibility(View.VISIBLE);
//        }

        if(group.is_recommend()){
            group_head_setting.setVisibility(View.GONE);
            care_about_button.setVisibility(View.VISIBLE);
        }else{
            care_about_button.setVisibility(View.GONE);
            group_head_setting.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void finish() {
        super.finish();
    }


}
