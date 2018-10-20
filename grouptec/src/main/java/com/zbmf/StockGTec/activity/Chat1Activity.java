package com.zbmf.StockGTec.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SwitchCompat;
import android.text.Html;
import android.text.TextUtils;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zbmf.StockGTec.R;
import com.zbmf.StockGTec.adapter.ViewPageFragmentadapter;
import com.zbmf.StockGTec.api.JSONHandler;
import com.zbmf.StockGTec.api.WebBase;
import com.zbmf.StockGTec.beans.GiftBean;
import com.zbmf.StockGTec.fragment.BlogFragment;
import com.zbmf.StockGTec.fragment.BoxFragment;
import com.zbmf.StockGTec.fragment.ChatFragment;
import com.zbmf.StockGTec.fragment.LiveFragment;
import com.zbmf.StockGTec.fragment.QuizFragment;
import com.zbmf.StockGTec.utils.SettingDefaultsManager;
import com.zbmf.StockGTec.utils.ShowActivity;
import com.zbmf.StockGTec.view.CusSeekbar;
import com.zbmf.StockGTec.view.CustomMarqueeTextView;
import com.zbmf.StockGTec.view.MagicTextView;
import com.zbmf.StockGTec.view.NoViewPager;
import com.zbmf.StockGTec.view.RoundedCornerImageView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Chat1Activity extends ExActivity implements View.OnClickListener, ViewPager.OnPageChangeListener , OnTabSelectListener {


    private static final String TAG = Chat1Activity.class.getSimpleName();
    private NoViewPager viewpager;
    private ChatFragment mChatFragment, mFansChat;
    private LiveFragment mLiveFragment;
    private QuizFragment mQuizFragment;
    private BlogFragment mBlogFragment;
    private BoxFragment mBoxFragment;
    private List<Fragment> mList;
    private List<String> title_list;
    private CustomMarqueeTextView notice_text;
    private LinearLayout notice_layout;
    private RoundedCornerImageView head_avatar;
    private TextView head_name, tv_tip1;
    private LinearLayout llgiftcontent;
    private NumAnim giftNumAnim;
    private TranslateAnimation inAnim;
    private TranslateAnimation outAnim;
    private ImageButton care_about_button, group_head_setting, group_head_msg;
    private boolean activity_pause;
    private String GROUP_ID;
    private SettingDefaultsManager sp = SettingDefaultsManager.getInstance();

    private SlidingTabLayout tablayout;
    private List<View> giftViewCollection = new ArrayList<View>();
    private PagerAdapter adapter;
    private UpdateReceiver receiver;
    private PopupWindow mPopupWindow;
    private InputMethodManager mImm;
    private UpdateAskReceiver mReceiver1;
    private String mAsk = "";
    private String is_fans;
    private int pos = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat1);
        GROUP_ID = SettingDefaultsManager.getInstance().getGroupId(); 
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            mAsk = bundle.getString("ask", "");
            is_fans = bundle.getString("is_fans");
            pos = bundle.getInt("pos");
            if ("1".equals(is_fans)) {
                visible = 3;
                if (pos != 0)
                    pos += 1;
            }
        }
        mImm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        enterGroup();
        initFragment();
        init();


        if (!TextUtils.isEmpty(mAsk))
            viewpager.setCurrentItem(2);
        if (pos != 0)
            viewpager.setCurrentItem(pos);
    }

    public void enterGroup() {
        WebBase.enterGroup(GROUP_ID, new JSONHandler(Chat1Activity.this) {
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
        WebBase.leaveGroup(GROUP_ID, new JSONHandler(Chat1Activity.this) {
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
        mLiveFragment = LiveFragment.newInstance(GROUP_ID);
        mChatFragment = ChatFragment.newInstance(ChatFragment.GROUP_CHAT);
        mFansChat = ChatFragment.newInstance(ChatFragment.FANS_CHAT);
        mQuizFragment = QuizFragment.newInstance(GROUP_ID);
        mBlogFragment = BlogFragment.newInstance();
        mBoxFragment = BoxFragment.newInstance();
        title_list.add("直播室");
        title_list.add("群聊");
        mList.add(mLiveFragment);
        mList.add(mChatFragment);
        if ("1".equals(is_fans)) {
            title_list.add("铁粉");
            mList.add(mFansChat);
        }
        if (!SettingDefaultsManager.getInstance().isManager()) {
            title_list.add("问股");
            title_list.add("宝盒");
            mList.add(mQuizFragment);
            mList.add(mBoxFragment);
        }
    }

    private void init() {
        viewpager = (NoViewPager) findViewById(R.id.viewpager);
        tablayout = (SlidingTabLayout) findViewById(R.id.tablayout);
        llgiftcontent = (LinearLayout) findViewById(R.id.llgiftcontent);
        inAnim = (TranslateAnimation) AnimationUtils.loadAnimation(this, R.anim.gift_in);
        outAnim = (TranslateAnimation) AnimationUtils.loadAnimation(this, R.anim.gift_out);


        giftNumAnim = new NumAnim();
        adapter = new ViewPageFragmentadapter(getSupportFragmentManager(), title_list, mList);
        viewpager.setAdapter(adapter);
        viewpager.setOnPageChangeListener(this);
        tablayout.setViewPager(viewpager);
        tablayout.setOnTabSelectListener(this);
        notice_layout = (LinearLayout) findViewById(R.id.notice_layout);
        notice_layout.setVisibility(View.GONE);
        notice_text = (CustomMarqueeTextView) findViewById(R.id.notice_text);

        head_avatar = (RoundedCornerImageView) findViewById(R.id.group_head_avatar);
        head_name = (TextView) findViewById(R.id.group_head_name);
        tv_tip1 = (TextView) findViewById(R.id.tv_tip1);
        head_name.setText("我的直播");
        if(sp.isManager())
            head_name.setText(SettingDefaultsManager.getInstance().getGroupName()+"的直播");
        ImageLoader.getInstance().displayImage(sp.UserAvatar(), head_avatar);
        findViewById(R.id.icon_notice_close_img).setOnClickListener(this);
        findViewById(R.id.group_head_return).setOnClickListener(this);
        group_head_msg = (ImageButton) findViewById(R.id.group_head_msg);
        group_head_msg.setOnClickListener(this);
        group_head_msg.setVisibility(View.GONE);
        group_head_setting = (ImageButton) findViewById(R.id.group_head_setting);
        group_head_setting.setOnClickListener(this);
        care_about_button = (ImageButton) findViewById(R.id.care_about_button);

        care_about_button.setOnClickListener(this);
        clearTiming();

        if (sp.isGroupManager() && viewpager.getCurrentItem() != 2) {
            tv_tip1.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.icon_notice_close_img:
                notice_layout.setVisibility(View.GONE);
                break;
            case R.id.group_head_return:
                finish();
                break;
            case R.id.group_head_setting:
                showPopupWindow();
                break;
            case R.id.group_head_msg:
                break;
            case R.id.care_about_button:
                care_about_button.setVisibility(View.GONE);
                group_head_setting.setVisibility(View.VISIBLE);
                break;
            case R.id.setting_text_size:
                if (mTextSetteingDialog == null)
                    mTextSetteingDialog = showSetText();
                mTextSetteingDialog.show();
                mPopupWindow.dismiss();
                break;
            case R.id.rl_setnotice:
                Bundle bundle1 = new Bundle();
                bundle1.putString("group_id", GROUP_ID);
                ShowActivity.startActivity(this, bundle1, PublishNoticeActivity.class.getName());
                mPopupWindow.dismiss();
                break;
            case R.id.rl_ban:
                Bundle bundle = new Bundle();
                bundle.putString("group_id", GROUP_ID);
                ShowActivity.startActivity(this, bundle, BanListActivity.class.getName());
                mPopupWindow.dismiss();
                break;
            case R.id.rl_share:
                mPopupWindow.dismiss();
                break;
        }
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    private int mPosition;
    private int visible = 2;

    @Override
    public void onPageSelected(int position) {
        mImm.hideSoftInputFromWindow(viewpager.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        mPosition = position;
        if (mPosition != visible)
            tablayout.hideMsg(position);

        switch (position) {
            case 0:
                break;
            case 1:
                mLiveFragment.onPause();
                break;
        }
        if (sp.isGroupManager() && viewpager.getCurrentItem() != 2)
            tv_tip1.setVisibility(View.VISIBLE);
        else tv_tip1.setVisibility(View.GONE);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void setlive_number_text(int position, int number) {
        if(sp.isManager()) return;
        if (position == mPosition) {
            tablayout.hideMsg(position);
        } else {
            tablayout.showDot(position);
            tablayout.setMsgMargin(position, 34, 5);
        }
    }

    public void setLiveNumbeGone(int position) {
        tablayout.hideMsg(position);
    }

    public void setShowQuestion() {
        tablayout.showDot(visible);
        tablayout.setMsgMargin(visible, 34, 5);
    }

    public void setQuestionNumbeGone() {
        tablayout.hideMsg(visible);
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
                RoundedCornerImageView picTv2 = (RoundedCornerImageView) giftView2.findViewById(R.id.crvheadimage);
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
//            RoundedCornerImageView crvheadimage = (RoundedCornerImageView) giftView.findViewById(R.id.crvheadimage);
            final MagicTextView giftNum = (MagicTextView) giftView.findViewById(R.id.giftNum);/*找到数量控件*/
            ImageView gift_img = (ImageView) giftView.findViewById(R.id.ivgift);
            TextView gift_name = (TextView) giftView.findViewById(R.id.show_gift_name);
            TextView send_gift_from_username = (TextView) giftView.findViewById(R.id.send_gift_from_username);
            String user_count = "<font color=\"#ffffff\">" + gb.getSend_from_user_name() + "</font>赠送";
            send_gift_from_username.setText(Html.fromHtml(user_count));
            String count = "<font color=\"#ffffff\">" + gb.getGift_name() + "</font>";
            gift_name.setText(Html.fromHtml(count));
            giftNum.setText("x" + gb.getSend_gift_number());/*设置礼物数量*/
            gift_name.setTag(System.currentTimeMillis());/*设置时间标记*/
            giftNum.setTag(gb.getSend_gift_number());/*给数量控件设置标记*/
//            ImageLoader.getInstance().displayImage(gb.getSend_from_user_avatar() != null ? gb.getSend_from_user_avatar() : "", crvheadimage);
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
                int count = llgiftcontent.getChildCount();
                for (int i = 0; i < count; i++) {
                    View view = llgiftcontent.getChildAt(i);
//                    RoundedCornerImageView crvheadimage = (RoundedCornerImageView) view.findViewById(R.id.crvheadimage);
                    TextView gift_name = (TextView) view.findViewById(R.id.show_gift_name);
                    long nowtime = System.currentTimeMillis();
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
                if (llgiftcontent.getChildCount() > index)
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

    @Override
    public void onTabSelect(int position) {

    }

    @Override
    public void onTabReselect(int position) {

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

    @Override
    protected void onStart() {
        super.onStart();
        receiver = new UpdateReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(getPackageName() + "notice_id");
        registerReceiver(receiver, filter);

        mReceiver1 = new UpdateAskReceiver();
        IntentFilter filter1 = new IntentFilter();
        filter1.addAction(getPackageName() + "client_ids");
        registerReceiver(mReceiver1, filter1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        leaveGroup();
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
        if (mReceiver1 != null) {
            unregisterReceiver(mReceiver1);
        }

//        SettingDefaultsManager.getInstance().setCurrentChat("");
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

    private Dialog mTextSetteingDialog = null;

    public void showPopupWindow() {
        View view = View.inflate(this, R.layout.activity_setting_layout, null);
        mPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable());

        SwitchCompat chat_switchCompat = (SwitchCompat) view.findViewById(R.id.chat_new_msg_vedio);

        View viewLine = view.findViewById(R.id.viewline);
        chat_switchCompat.setChecked(sp.isGroupManager());
        chat_switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                sp.setGroupManager(b);
                tv_tip1.setVisibility(b ? View.VISIBLE : View.GONE);
            }
        });

        if (!sp.isGroupChatManager() || sp.isGroupManager() && viewpager.getCurrentItem() == 2) {
            viewLine.setVisibility(View.GONE);
            chat_switchCompat.setVisibility(View.GONE);
        }
        view.findViewById(R.id.setting_text_size).setOnClickListener(this);
        view.findViewById(R.id.rl_setnotice).setOnClickListener(this);
        view.findViewById(R.id.rl_ban).setOnClickListener(this);
        view.findViewById(R.id.rl_share).setOnClickListener(this);

        mPopupWindow.update();
        mPopupWindow.showAsDropDown(group_head_setting, 120, 0);
    }

    public class UpdateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //获取service传过来的信息
            String group_id = intent.getStringExtra("group_id");
            if (group_id.equals(GROUP_ID)) {
                String countent = intent.getStringExtra("message");
                notice_text.setText(countent);
                if (notice_layout.getVisibility() == View.GONE) {
                    notice_layout.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    public class UpdateAskReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!sp.isManager())
                setShowQuestion();
        }
    }

    /**
     * 设置字体Dialog
     *
     * @return
     */
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
        int dimen = sp.getTextSize();
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
                        sp.setTextSize(R.dimen.live_text_size_min_small);
                        break;
                    case 1:
                        sp.setTextSize(R.dimen.live_text_size_small);
                        break;
                    case 2:
                        sp.setTextSize(R.dimen.live_text_size);
                        break;
                    case 3:
                        sp.setTextSize(R.dimen.live_text_size_big);
                        break;
                    case 4:
                        sp.setTextSize(R.dimen.live_text_size_max_big);
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


}
