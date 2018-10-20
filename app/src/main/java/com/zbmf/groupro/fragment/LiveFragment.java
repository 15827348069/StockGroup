package com.zbmf.groupro.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zbmf.groupro.R;
import com.zbmf.groupro.activity.Chat1Activity;
import com.zbmf.groupro.adapter.GiftAdapter1;
import com.zbmf.groupro.adapter.GridAdatper;
import com.zbmf.groupro.adapter.LiveAdapter;
import com.zbmf.groupro.api.GetRedPackHandler;
import com.zbmf.groupro.api.JSONHandler;
import com.zbmf.groupro.api.OpenRedPackHandler;
import com.zbmf.groupro.api.WebBase;
import com.zbmf.groupro.beans.BoxBean;
import com.zbmf.groupro.beans.Gift;
import com.zbmf.groupro.beans.Group;
import com.zbmf.groupro.beans.LiveMessage;
import com.zbmf.groupro.beans.LiveTypeMessage;
import com.zbmf.groupro.beans.RedPackgedBean;
import com.zbmf.groupro.db.DBManager;
import com.zbmf.groupro.service.GetLiveMessage;
import com.zbmf.groupro.utils.Constants;
import com.zbmf.groupro.utils.ImageLoaderOptions;
import com.zbmf.groupro.utils.MessageType;
import com.zbmf.groupro.utils.NotificationUtil;
import com.zbmf.groupro.utils.SettingDefaultsManager;
import com.zbmf.groupro.utils.ShowActivity;
import com.zbmf.groupro.view.CustomMarqueeTextView;
import com.zbmf.groupro.view.MyTextView;
import com.zbmf.groupro.view.RoundedCornerImageView;
import com.zbmf.groupro.view.SendGiftProgress;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link LiveFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LiveFragment extends Fragment implements MyTextView.OnTextClickListener,View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "GROUP_ID";
    private static final String TAG = LiveFragment.class.getSimpleName();

    // TODO: Rename and change types of parameters
    private View mfragment;
    private ListView message_list;
    private LiveAdapter adapter;
    private List<LiveMessage> infolist;
    private Animation verticalAnimation;
    private Animation topAnimation;
    private UpdateReceiver receiver;
    private NoticeUpdateReceiver noticeUpdateReceiver;
    private boolean is_bottom, just_look_tf, animation_is_running;
    private TextView live_new_msg;
    private int new_live_message;
    private CustomMarqueeTextView notice_text;
    private Chat1Activity chat_activity;

    private LinearLayout button_layout;
    private Button send_gift_button, button_layout_visible,add_tf_message;
    private ImageView scanHorizontalLineImageView, scanHorizontalLineImageView_top;

    private Dialog mGiftDialog,edit_dialog,tp_dialog;
    private List<Gift> mGiftList = null;
    private List<GridView> views;
    private ViewPager vp_gift;
    private DBManager dbManager;
    private AlertDialog aDialog=null;
    private Group groupbean;
    private String GROUP_ID;
    private LinearLayout notice_layout;
    private boolean is_tf;
    private boolean already_add_allMessage;
    private SendGiftProgress sp;
    private EditText mBlogDetailPinglunEdit;

    public LiveFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment LiveFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LiveFragment newInstance(Group group) {
        LiveFragment fragment = new LiveFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, group);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            groupbean = (Group) getArguments().getSerializable(ARG_PARAM1);
            GROUP_ID=groupbean.getId();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (mfragment == null) {
            mfragment = inflater.inflate(R.layout.fragment_live,container,false);
        }
        receiver = new UpdateReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.NEW_LIVE_MSG);
        getActivity().registerReceiver(receiver, filter);
        dbManager=new DBManager(getContext());
        dbManager.setUnablemessage(GROUP_ID);
        noticeUpdateReceiver = new NoticeUpdateReceiver();
        IntentFilter noticefilter = new IntentFilter();
        filter.addAction("com.zbmf.StockGroup.notice_id");
        getActivity().registerReceiver(noticeUpdateReceiver, noticefilter);
        return mfragment;
    }

    public void livenotifyDataSetChanged() {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
            if (is_bottom) {
                message_list.smoothScrollToPosition(adapter.getCount());
            }
        }
    }

    public void setButton_layout_visible() {
        button_layout.setVisibility(View.VISIBLE);
        button_layout_visible.setVisibility(View.GONE);
    }

    public void setButton_layout_gone() {
        button_layout.setVisibility(View.GONE);
        button_layout_visible.setVisibility(View.VISIBLE);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        message_list = (ListView) mfragment.findViewById(R.id.live_message_list);
        infolist = new ArrayList<>();
        live_new_msg = (TextView) mfragment.findViewById(R.id.live_new_msg);
        chat_activity = (Chat1Activity) getActivity();
        send_gift_button = (Button) mfragment.findViewById(R.id.send_gift_button);
        button_layout = (LinearLayout) mfragment.findViewById(R.id.button_layout);
        button_layout_visible = (Button) mfragment.findViewById(R.id.button_layout_visible);
        scanHorizontalLineImageView = (ImageView) mfragment.findViewById(R.id.scanHorizontalLineImageView);
        scanHorizontalLineImageView_top = (ImageView) mfragment.findViewById(R.id.scanHorizontalLineImageView_top);
        notice_layout = (LinearLayout) mfragment.findViewById(R.id.notice_layout);
        add_tf_message = (Button) mfragment.findViewById(R.id.add_tf_message);
        notice_text = (CustomMarqueeTextView) mfragment.findViewById(R.id.notice_text);

        mfragment.findViewById(R.id.send_ask_button).setOnClickListener(this);
        mfragment.findViewById(R.id.icon_notice_close_img).setOnClickListener(this);
        mfragment.findViewById(R.id.send_vote).setOnClickListener(this);
        mfragment.findViewById(R.id.kf_button).setOnClickListener(this);

        adapter = new LiveAdapter(getActivity(), infolist,is_tf,this,this);
        message_list.setAdapter(adapter);
        add_tf_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_to_tf();
            }
        });
        // 从上到下的平移动画
        verticalAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.bottom_in_anim);
        // 从上到下的平移动画
        topAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.top_in_anim);
        verticalAnimation.setAnimationListener(new Animation.AnimationListener() {/*显示动画的监听*/
            @Override
            public void onAnimationStart(Animation animation) {
                animation_is_running = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                scanHorizontalLineImageView.setVisibility(View.GONE);
                animation_is_running = false;
                getTFmessage(new Date().getTime());
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        topAnimation.setAnimationListener(new Animation.AnimationListener() {/*显示动画的监听*/
            @Override
            public void onAnimationStart(Animation animation) {
                animation_is_running = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                scanHorizontalLineImageView_top.setVisibility(View.GONE);
                animation_is_running = false;
                getTFmessage(new Date().getTime());
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        button_layout_visible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setButton_layout_visible();
            }
        });
        final CheckedTextView just_look_button_layout = (CheckedTextView) mfragment.findViewById(R.id.just_look_button_layout);
//        just_look_button = (CheckBox) mfragment.findViewById(R.id.just_look_button);
        just_look_button_layout.setChecked(just_look_tf);
        just_look_button_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ShowActivity.isLogin(getActivity())){
                    if (just_look_tf) {
                        just_look_tf=false;
                        scanHorizontalLineImageView_top.setVisibility(View.VISIBLE);
                        scanHorizontalLineImageView_top.startAnimation(topAnimation);
                    } else {
                        just_look_tf=true;
                        scanHorizontalLineImageView.setVisibility(View.VISIBLE);
                        scanHorizontalLineImageView.startAnimation(verticalAnimation);
                    }
                    just_look_button_layout.setChecked(just_look_tf);
                }
            }
        });
        message_list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem == 0) {
                    View firstVisibleItemView = message_list.getChildAt(0);
                    if (firstVisibleItemView != null && firstVisibleItemView.getTop() == 0) {
                        int no_read = totalItemCount - firstVisibleItem - visibleItemCount;
                        if(no_read>0){
                            is_bottom = false;
                            getDbMessage(infolist.get(0).getMessage_time(),true);
                        }else{
                            is_bottom = true;
                        }
                    }
                } else if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
                    View lastVisibleItemView = message_list.getChildAt(message_list.getChildCount() - 1);
                    if (lastVisibleItemView != null && lastVisibleItemView.getBottom() == message_list.getHeight()) {
                        is_bottom = true;
                        new_live_message = 0;
                        live_new_msg.setVisibility(View.INVISIBLE);
                        chat_activity.setLiveNumbeGone(0);
                    }
                } else {
                    is_bottom = false;
                    //向上滑动隐藏按钮
                    if (new_live_message != 0 && firstVisibleItem + visibleItemCount > new_live_message) {
                        int no_read = totalItemCount - firstVisibleItem - visibleItemCount;
                        new_live_message = firstVisibleItem + visibleItemCount;
                        if (no_read == 0) {
                            is_bottom=true;
                            live_new_msg.setVisibility(View.GONE);
                            return;
                        }
                        live_new_msg.setText(String.valueOf(no_read));
                        chat_activity.setlive_number_text(0, no_read);
                    }
                }

            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }
        });
        live_new_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message_list.setSelection(new_live_message);
            }
        });
        send_gift_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mGiftList == null ) {
                    initGiftList(true);
                    return;
                }
                if (mGiftDialog == null)
                    mGiftDialog = showGiftDialog();
                vp_gift.setCurrentItem(0);
                mGiftDialog.show();
            }
        });
        long time=new Date().getTime();
        getGroupMessage();
        getMessageList(time,true);
        initGiftList(false);

    }
    public void add_to_tf(){
        if(ShowActivity.isLogin(getActivity())){
            WebBase.fansInfo(GROUP_ID, new JSONHandler(true,getActivity(),"正在加载数据...") {
                @Override
                public void onSuccess(JSONObject obj) {
                    JSONObject group=obj.optJSONObject("group");
                    Group groupbean=new Group();
                    groupbean.setId(group.optString("id"));
                    groupbean.setName(group.optString("name"));
                    groupbean.setNick_name(group.optString("nickname"));
                    groupbean.setAvatar(group.optString("avatar"));
                    groupbean.setIs_close(group.optInt("is_close"));
                    groupbean.setIs_private(group.optInt("is_private"));
                    groupbean.setRoles(group.optInt("roles"));
                    groupbean.setFans_level(group.optInt("fans_level"));
                    groupbean.setDay_mapy(group.optLong("day_mpay"));
                    groupbean.setMonth_mapy(group.optLong("month_mpay"));
                    groupbean.setEnable_day(group.optInt("enable_day"));
                    groupbean.setEnable_point(group.optInt("enable_point"));
                    groupbean.setMax_point(group.optInt("max_point"));
                    groupbean.setDescription(group.optString("fans_profile"));
                    groupbean.setFans_activity(group.optString("fans_activity"));
                    groupbean.setFans_countent(group.optString("fans_content"));
                    groupbean.setPoint_desc(group.optString("point_desc"));
                    groupbean.setMax_mpay(group.optLong("max_mpay"));
                    groupbean.setFans_date(group.optString("fans_expire_at"));
                    ShowActivity.showFansActivity(getActivity(),groupbean);
                }
                @Override
                public void onFailure(String err_msg) {

                }
            });
        }
    }
    public void setTFmessage(boolean is_fans){
        is_tf=is_fans;
        adapter.add_tf(is_fans);
    }
    private void initGiftList(final boolean show) {
        WebBase.getGiftList(new JSONHandler(show, getActivity(), "获取礼物列表...") {
            @Override
            public void onSuccess(JSONObject obj) {
                JSONArray gifts = obj.optJSONArray("gifts");
                mGiftList = new ArrayList<>();
                for (int i = 0; i < gifts.length(); i++) {
                    JSONObject ojb = gifts.optJSONObject(i);
                    Gift gift = new Gift();
                    gift.setGift_id(ojb.optString("gift_id"));
                    gift.setName(ojb.optString("name"));
                    gift.setIcon(ojb.optString("icon"));
                    gift.setMpays(ojb.optInt("pays"));
                    gift.setPoints(ojb.optInt("points"));
                    gift.setCategory(ojb.optString("category"));
                    mGiftList.add(gift);
                }
                initViews();
                if (show) {
                    if (mGiftDialog == null)
                        mGiftDialog = showGiftDialog();
                    mGiftDialog.show();
                }
            }

            @Override
            public void onFailure(String err_msg) {
                Toast.makeText(getActivity(), err_msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initViews() {
        views = new ArrayList<>();
        List<Gift> temps = new ArrayList<Gift>();
        for (Gift gift : mGiftList) {
            temps.add(gift);
            if (temps.size() == 4) {
                views.add(getView(temps));
                temps = new ArrayList<Gift>();
            }
        }

        if (temps.size() > 0){
            views.add(getView(temps));
        }
    }
    private Gift mGift;
    private GridView getView(final List<Gift> temps) {
        GridView gridView = (GridView) View.inflate(getActivity(), R.layout.gift_layout, null);
        final GridAdatper adatper = new GridAdatper(getActivity(), temps);
        gridView.setAdapter(adatper);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Gift gift = (Gift) parent.getItemAtPosition(position);
                if (mGift != null && gift.getGift_id().equals(mGift.getGift_id())) {
                    gift.setChecked(!gift.isChecked());
                    if(!gift.isChecked())
                        gift = null;
                } else {
                    if(mGift!=null)
                        here: for(GridView gridview : views){
                            GridAdatper adapter = (GridAdatper)gridview.getAdapter();
                            for(Gift gifts : adapter.getGifts()){
                                if(mGift.getGift_id().equals(gifts.getGift_id())){
                                    gifts.setChecked(false);
                                    adapter.notifyDataSetChanged();
//                                    mGift = null;
                                    break here;
                                }
                            }
                        }
                    gift.setChecked(true);
                }
                mGift = gift;
                if(mGift!=null){
                    if(!mGift.getCategory().equals("10")){
                        double my_mfb= Double.valueOf(SettingDefaultsManager.getInstance().getPays());
                       if(sp!=null){
                           if(my_mfb<mGift.getMpays()){
                               sp.setText("去充值");
                           }else{
                               sp.setText("送TA");
                           }
                       }
                    }else{
                        sp.setText("送TA");
                    }
                }
                adatper.notifyDataSetChanged();
            }
        });
        return gridView;
    }

    private Dialog showGiftDialog() {
        final Dialog dialog = new Dialog(getActivity(), R.style.myDialogTheme);
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        View layout = inflater.inflate(R.layout.dialog_gift, null);
        vp_gift = (ViewPager) layout.findViewById(R.id.viewpager);
        final LinearLayout ll_dot = (LinearLayout) layout.findViewById(R.id.ll_dot);
        final TextView tv_mfb = (TextView) layout.findViewById(R.id.tv_mfb);
        final TextView tv_jf = (TextView) layout.findViewById(R.id.tv_jf);
        sp= (SendGiftProgress) layout.findViewById(R.id.send_gift_progress);
        sp.setSendClickListener(new SendGiftProgress.OnSendClickListener() {
            @Override
            public void OnSendClickListener(View view) {
                if(mGift!=null&&ShowActivity.isLogin(getActivity())){
                    if(mGift.getCategory().equals("10")){
                        //积分礼物
                        if(SettingDefaultsManager.getInstance().getPoint()<mGift.getPoints()){
                            Toast.makeText(getActivity(),"积分不足",Toast.LENGTH_SHORT).show();
                            sp.stopAnim();
                        }else{
                            sendGift(tv_mfb,tv_jf);
                        }
                    }else{
                        //魔方宝礼物
                        double my_mfb= Double.valueOf(SettingDefaultsManager.getInstance().getPays());
                        if(my_mfb<mGift.getMpays()){
                            sp.stopAnim();
                            ShowActivity.showPayDetailActivity(getActivity());
                        }else{
                            sendGift(tv_mfb,tv_jf);
                        }
                    }
                }else{
                    sp.stopAnim();
                    Toast.makeText(getContext(),"请先选择礼物",Toast.LENGTH_SHORT).show();
                }
            }
        } );

        tv_mfb.setText(SettingDefaultsManager.getInstance().getPays());
        tv_jf.setText(String.valueOf(SettingDefaultsManager.getInstance().getPoint()));
        for (int i = 0; i < views.size(); i++) {
            ImageView point = new ImageView(getActivity());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            params.rightMargin = 10;
            if (i == views.size() - 1)
                params.rightMargin = 0;
//          point.setBackgroundResource(R.drawable.point_bg);
            if (i == 0) {
                point.setImageResource(R.drawable.selp);
            } else {
                point.setImageResource(R.drawable.unselp);
            }
            ll_dot.addView(point, params);
        }

        GiftAdapter1 giftAdapter1 = new GiftAdapter1(getActivity(), views);
        vp_gift.setAdapter(giftAdapter1);
        vp_gift.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int childCount = ll_dot.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    ImageView view = (ImageView) ll_dot.getChildAt(i);
                    if (i == position) {
                        view.setImageResource(R.drawable.selp);
                    } else {
                        view.setImageResource(R.drawable.unselp);
                    }
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

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
    public void sendGift(final TextView tv_mfb,final TextView tv_jf){
        WebBase.sendGift(GROUP_ID,mGift.getGift_id(),new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                Toast.makeText(getContext(),"送礼成功",Toast.LENGTH_SHORT).show();
                sp.stopAnim();
                getWolle(tv_mfb,tv_jf);
            }
            @Override
            public void onFailure(String err_msg) {
                sp.stopAnim();
                Toast.makeText(getContext(),err_msg,Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getWolle(final TextView tv_mfb,final TextView tv_jf){
        WebBase.getWalle(new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                JSONObject pays=obj.optJSONObject("pay");
                JSONObject point=obj.optJSONObject("point");
                SettingDefaultsManager.getInstance().setPays(pays.optString("unfrozen"));
                SettingDefaultsManager.getInstance().setPoint(point.optLong("unfrozen"));
                tv_mfb.setText(SettingDefaultsManager.getInstance().getPays());
                tv_jf.setText(String.valueOf(SettingDefaultsManager.getInstance().getPoint()));
            }
            @Override
            public void onFailure(String err_msg) {
                Log.e(">>>>","错误"+err_msg);
            }
        });
    }
    @Override
    public void OnTextClickListener(LiveTypeMessage message) {
        switch (message.getMessage_type()){
            case "url":
                ShowActivity.showWebViewActivity(getActivity(),message.getMessage());
                break;
            case "stock":
                Toast.makeText(getContext(),"点击股票消息"+message.getMessage(),Toast.LENGTH_SHORT).show();
                break;
            case "tf_message":
                add_to_tf();
                break;
            case "box_message":
                if(message.getBox_leaver()<=groupbean.getFans_level()){
                    ShowActivity.showBoxDetailActivity(getActivity(),GROUP_ID,message.getMessage());
                }else{
                    String toast_message="您为【***】用户，升级成为【**】即可查";
                    switch (groupbean.getFans_level()){
                        case 0:
                            toast_message=toast_message.replace("【***】","非铁粉");
                            break;
                        case 5:
                            toast_message=toast_message.replace("【***】","体验铁粉");
                            break;
                        case 10:
                            toast_message=toast_message.replace("【***】","非年粉");
                            break;
                    }
                    if(message.getBox_leaver()==10){
                        toast_message=toast_message.replace("【**】","包月铁粉");
                    }else if(message.getBox_leaver()==20){
                        toast_message=toast_message.replace("【**】","年粉");
                    }
                    Toast.makeText(getActivity(),toast_message,Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.kf_button:
                String qq_url="mqqwpa://im/chat?chat_type=crm&uin=2852273339&version=1&src_type=web&web_src=http:://wpa.b.qq.com";
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(qq_url)));
                break;
            case  R.id.red_bag_layout:
                //抢红包
                String red_id= (String) view.getTag();
                if(red_id!=null&&ShowActivity.isLogin(getActivity())){
                    WebBase.openRedPackged(red_id, new OpenRedPackHandler(true,getContext(),"正在开红包...") {
                        @Override
                        public void onSuccess(RedPackgedBean obj) {
                            RedPackgedBean redPackgedBean=obj;
                            if(redPackgedBean!=null){
                                if(redPackgedBean.getRed_status()==4||redPackgedBean.getRed_status()==3){
                                   ShowActivity.showRedBagActivity(getActivity(),redPackgedBean);
                                }else{
                                    show_red_bag_view(redPackgedBean);
                                }
                            }
                        }

                        @Override
                        public void onFailure(String err_msg) {
                            Toast.makeText(getContext(),err_msg,Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                break;
            case R.id.live_img_id:
                String url= (String) view.getTag();
                if(url!=null){
                    //查看大图
                    ShowActivity.ShowBigImage(getActivity(),url);
                }
                break;
            case R.id.send_ask_button:
                if(ShowActivity.isLogin(getActivity())){
                    if(edit_dialog==null){
                        edit_dialog=Editdialog1();
                    }
                    mBlogDetailPinglunEdit.setText("");
                    edit_dialog.show();
                }
                break;
            case R.id.icon_notice_close_img:
                notice_layout.setVisibility(View.GONE);
                break;
            case R.id.send_vote:
                Toast.makeText(getContext(),"敬请期待",Toast.LENGTH_SHORT).show();
//                if(tp_dialog==null){
//                    tp_dialog=TouPiao();
//                }
//                tp_dialog.show();
                break;
        }
    }
    public class UpdateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //获取service传过来的信息
            LiveMessage msg = (LiveMessage) intent.getSerializableExtra("new_live_msg");
            if(msg.getGroup_id().equals(GROUP_ID)||msg.getMessage_type().equals(MessageType.SYSTEM)){
                //当前圈子数据
                if (msg.getMessage_type().equals(MessageType.GIFT)) {
                    //礼物消息
                    chat_activity.showGift(msg.getGiftbean());
                }
                if(msg.getMessage_type().equals(MessageType.ANSWER)
                        &&msg.getQuestion_id().equals(SettingDefaultsManager.getInstance().UserId())){
                    //提问消息
                    addMessage(msg);
                }else{
                    chat_activity.addMessage(msg);
                    dbManager.setMessageUnable(msg.getMsg_id());
                    if (just_look_tf) {
                        addTfMessage(msg);
                    } else {
                        addMessage(msg);
                    }
                }
                if (is_bottom) {
                    //动画正在执行不滑动listview
                    if (!animation_is_running) {
                        message_list.smoothScrollToPosition(adapter.getCount());
                    }
                } else {
                    if (new_live_message == 0) {
                        new_live_message = adapter.getCount() - 1;
                    }
                    int number = adapter.getCount() - new_live_message;
                    live_new_msg.setText(String.valueOf(number));
                    if (live_new_msg.getVisibility() == View.INVISIBLE) {
                        live_new_msg.setVisibility(View.VISIBLE);
                    }
                    chat_activity.setlive_number_text(0, number);
                }
            }
        }
    }
    public void addTfMessage(LiveMessage cm) {
        if (cm.getMessage_type().equals(MessageType.FANS)) {
            infolist.add(cm);
        }
        adapter.notifyDataSetChanged();
    }

    public void addMessage(LiveMessage cm) {
        infolist.add(cm);
        adapter.notifyDataSetChanged();
    }
    public void getMessageList( final long time,final boolean is_show) {
        if(already_add_allMessage){
            return;
        }
        WebBase.getLiveMsg(groupbean.getId(),time,just_look_tf,new JSONHandler(is_show,getContext(),"获取聊天记录...") {
            @Override
            public void onSuccess(JSONObject obj) {

                if(!obj.isNull("msgs")){
                    JSONArray msgs=obj.optJSONArray("msgs");
                    int size=msgs.length()-1;
                    List<LiveMessage> info=new ArrayList<LiveMessage>();
                    for(int i=size;i>=0;i--){
                        JSONObject oj=msgs.optJSONObject(i);
                        LiveMessage lm= GetLiveMessage.getMessage(oj,true);
                        info.add(0,lm);
                    }
                    int info_size=info.size();
                    if(info_size>0){
                        dbManager.addAll(info);
                        getDbMessage(time,is_show);
                    }else{
                        already_add_allMessage=true;
                    }
                }
            }
            @Override
            public void onFailure(String err_msg) {
//                getDbMessage(time,is_show);
                Toast.makeText(getContext(),"加载失败",Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void getMoreMessageList( final long time,final boolean is_show) {
        if(already_add_allMessage){
            return;
        }
        WebBase.getLiveMsg(groupbean.getId(),time,just_look_tf,new JSONHandler(is_show,getContext(),"获取聊天记录...") {
            @Override
            public void onSuccess(JSONObject obj) {
                JSONArray msgs=obj.optJSONArray("msgs");
                int size=msgs.length()-1;
                List<LiveMessage> info=new ArrayList<LiveMessage>();
                for(int i=size;i>=0;i--){
                    JSONObject oj=msgs.optJSONObject(i);
                    LiveMessage lm= GetLiveMessage.getMessage(oj,true);
                    info.add(0,lm);
                }
                int info_size=info.size();
                if(info_size>0){
                    dbManager.addAll(info);
                    infolist.addAll(0,info);
                    adapter.notifyDataSetChanged();
                    message_list.setSelection(info_size);
                }else{
                    already_add_allMessage=true;
                }

            }
            @Override
            public void onFailure(String err_msg) {
                Toast.makeText(getContext(),"加载失败",Toast.LENGTH_SHORT).show();
            }
        });
    }
    /**
     * 切换铁粉悄悄话
     * @param time
     */
    public void getTFmessage(long time){
        already_add_allMessage=false;
        List<LiveMessage> info=dbManager.query(GROUP_ID,time,just_look_tf);
        int list_size=info.size();
        infolist.clear();
        if(list_size>0){
            infolist.addAll(0,info);
            adapter.notifyDataSetChanged();
            new_live_message = adapter.getCount();
        }
        if(list_size==0){
            message_list.setSelection(adapter.getCount());
        }else{
            message_list.setSelection(list_size);
        }
        new_live_message = adapter.getCount();
    }

    public void getDbMessage(long time,boolean show){
        List<LiveMessage> info=dbManager.query(GROUP_ID,time,just_look_tf);
        int list_size=info.size();
        if(list_size>0){
            infolist.addAll(0,info);
            adapter.notifyDataSetChanged();
            message_list.setSelection(list_size);
        }else{
            getMoreMessageList(time,show);
        }
        new_live_message = adapter.getCount();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            getActivity().unregisterReceiver(receiver);
        }
        if(noticeUpdateReceiver!=null){
            getActivity().unregisterReceiver(noticeUpdateReceiver);
        }
        Intent intent = new Intent(Constants.NEW_LIVE_MSG_READ);
        getContext().sendBroadcast(intent);
        dbManager.closeDB();
    }
    public void RushScrollview(){
//        if(main_scrollview!=null){
////            main_scrollview.getLoadingLayoutProxy().setLastUpdatedLabel("");
//            main_scrollview.onRefreshComplete();
//        }
    }

    @Override
    public void onResume() {
        super.onResume();
        is_bottom = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        is_bottom = false;
    }
    private TextView red_bag_message,have_red_bag;
    private ImageView click_red_bag_button_anim;
    private Button into_red_bag_detail;
    private void show_red_bag_view(final RedPackgedBean redbean){
        if(aDialog==null){
            aDialog = new AlertDialog.Builder(getActivity()).show();
        }else{
            aDialog.show();
        }
        aDialog.getWindow().setContentView(R.layout.ciclk_red_bag_layout);
        aDialog.getWindow().setGravity(Gravity.CENTER);
        RoundedCornerImageView avatar=(RoundedCornerImageView) aDialog.getWindow().findViewById(R.id.red_from_user_avatar);
        TextView red_bag_from_name=(TextView) aDialog.getWindow().findViewById(R.id.red_bag_from_username);
        red_bag_message=(TextView) aDialog.getWindow().findViewById(R.id.red_bag_user_message);
        have_red_bag=(TextView) aDialog.getWindow().findViewById(R.id.have_red_bag);
        click_red_bag_button_anim=(ImageView) aDialog.getWindow().findViewById(R.id.click_red_bag_button_anim);

        ImageLoader.getInstance().displayImage(redbean.getUser_avatar(), avatar, ImageLoaderOptions.AvatarOptions());
        final ImageButton click_red_bag_button=(ImageButton) aDialog.getWindow().findViewById(R.id.click_red_bag_button);
        ImageButton return_button=(ImageButton) aDialog.getWindow().findViewById(R.id.return_button);
        into_red_bag_detail=(Button) aDialog.getWindow().findViewById(R.id.into_red_bag_detail);
        red_bag_from_name.setText(redbean.getUser_name());

        if(redbean.getRed_status()==0){
            red_bag_message.setText(redbean.getRed_message());
            click_red_bag_button.setVisibility(View.VISIBLE);
            have_red_bag.setVisibility(View.VISIBLE);
            //拆红包按钮
            click_red_bag_button.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    if(ShowActivity.isLogin(getActivity())){
                        click_red_bag_button_anim.setVisibility(View.VISIBLE);
                        click_red_bag_button.setVisibility(View.INVISIBLE);
                        AnimationDrawable animationDrawable = (AnimationDrawable) click_red_bag_button_anim.getDrawable();
                        animationDrawable.start();
                        GetRedBagMessage(redbean.getRed_id());
                    }
                }
            });
        }else{
            NO_RED_BAG(redbean);
        }
        return_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                aDialog.dismiss();
            }
        });
    }
    private void GetRedBagMessage(final String red_id){
        WebBase.getRedPackged(red_id, new GetRedPackHandler() {
            @Override
            public void onSuccess(RedPackgedBean obj) {
                RedPackgedBean red=(RedPackgedBean)obj;
                if(red.getRed_status()==4){
                    ShowActivity.showRedBagActivity(getActivity(),red);
                    if(aDialog!=null){
                        aDialog.dismiss();
                    }
                }else if(red.getRed_status()==3){
                    NO_RED_BAG(red);
                }
            }

            @Override
            public void onFailure(String err_msg) {
                Toast.makeText(getContext(),err_msg,Toast.LENGTH_SHORT).show();
                aDialog.dismiss();
            }
        });
    }
    private void NO_RED_BAG(final RedPackgedBean redbean){
        red_bag_message.setText("手慢了，红包派完了");
        have_red_bag.setVisibility(View.INVISIBLE);
        click_red_bag_button_anim.setVisibility(View.INVISIBLE);
        into_red_bag_detail.setVisibility(View.VISIBLE);
        into_red_bag_detail.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                aDialog.dismiss();
                ShowActivity.showRedBagActivity(getActivity(),redbean);
            }
        });
    }
    private boolean mIsTitleHide = false;
    private float lastX = 0;
    private float lastY = 0;
    public boolean dispatchTouchEvent(MotionEvent event)
    {
//        if (mIsAnim) {
//            return false;
//        }
        final int action = event.getAction();

        float x = event.getX();
        float y = event.getY();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                lastY = y;
                lastX = x;
                return false;
            case MotionEvent.ACTION_MOVE:
                float dY = Math.abs(y - lastY);
                float dX = Math.abs(x - lastX);
                boolean down = y > lastY ? true : false;
                lastY = y;
                lastX = x;
                if (dX < 8 && dY > 8 && !mIsTitleHide && !down) {
                    button_layout.setVisibility(View.VISIBLE);
                } else if (dX < 8 && dY > 8 && mIsTitleHide && down) {
                    button_layout.setVisibility(View.GONE);
                } else {
                    return false;
                }
                mIsTitleHide = !mIsTitleHide;
                break;
            default:
                return false;
        }
        return false;
    }
    public class NoticeUpdateReceiver extends BroadcastReceiver {
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
    private Dialog Editdialog1(){
        final Dialog dialog = new Dialog(getContext(), R.style.myDialogTheme);
        final View layout = LayoutInflater.from(getContext()).inflate(R.layout.blog_detail_pinglun_layout, null);
        mBlogDetailPinglunEdit = (EditText) layout.findViewById(R.id.blog_detail_pinglun_edit);
        final Button send_private_layout= (Button) layout.findViewById(R.id.send_private_layout);
        final Button send= (Button) layout.findViewById(R.id.send_pinglun_layout);
        send_private_layout.setVisibility(View.VISIBLE);
        send_private_layout.setEnabled(false);
        send_private_layout.setAlpha(0.5f);
        send.setText("提问");
        send.setEnabled(false);
        send.setAlpha(0.5f);
        mBlogDetailPinglunEdit.setHint("写下你的问题");
        mBlogDetailPinglunEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.equals("")||charSequence.length()==0){
                    send.setEnabled(false);
                    send.setAlpha(0.5f);
                    send_private_layout.setEnabled(false);
                    send_private_layout.setAlpha(0.5f);
                }else{
                    send.setEnabled(true);
                    send.setAlpha(1.0f);
                    send_private_layout.setEnabled(true);
                    send_private_layout.setAlpha(1.0f);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        send_private_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //私密提问
                sendAsk(mBlogDetailPinglunEdit.getText().toString(),1);
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendAsk(mBlogDetailPinglunEdit.getText().toString(),0);
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
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mBlogDetailPinglunEdit.getWindowToken(),0); //强制隐藏键盘
            }
        });
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                showSoftInputFromWindow(mBlogDetailPinglunEdit);
            }
        });

        return  dialog;
    }
    private Dialog TouPiao(){
        final Dialog dialog = new Dialog(getContext(), R.style.myDialogTheme);
        View layout = LayoutInflater.from(getContext()).inflate(R.layout.toupiao_layout, null);
        TextView toupiao_tercher_name=(TextView) layout.findViewById(R.id.toupiao_tercher_name);
        TextView month_arrow=(TextView) layout.findViewById(R.id.month_arrow);
        TextView all_arrow=(TextView) layout.findViewById(R.id.all_arrow);
        final TextView tp_need_mfb=(TextView) layout.findViewById(R.id.tp_need_mfb);
        final TextView toupiao_count_text=(TextView) layout.findViewById(R.id.toupiao_count_text);
        Button send= (Button) layout.findViewById(R.id.toupiao_tercher_button);
        Button plus_button= (Button) layout.findViewById(R.id.plus_button);
        Button add_button= (Button) layout.findViewById(R.id.add_button);
        RoundedCornerImageView toupiao_tercher_avatar= (RoundedCornerImageView) layout.findViewById(R.id.toupiao_tercher_avatar);
        ImageLoader.getInstance().displayImage(groupbean.getAvatar(),toupiao_tercher_avatar,ImageLoaderOptions.AvatarOptions());
        toupiao_tercher_name.setText(groupbean.getNick_name());
        month_arrow.setText("月排名"+12);
        all_arrow.setText("总排名"+122);
        plus_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count=Integer.valueOf(toupiao_count_text.getText().toString());
                count-=1;
                if(count>1){
                    toupiao_count_text.setText(count+"");
                    tp_need_mfb.setText(count*2+"");
                }else{
                    toupiao_count_text.setText("1");
                    tp_need_mfb.setText("2");
                }
            }
        });
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count=Integer.valueOf(toupiao_count_text.getText().toString());
                count+=1;
                toupiao_count_text.setText(count+"");
                tp_need_mfb.setText(count*2+"");
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                sendAsk(blog_detail_pinglun_edit.getText().toString());
                Toast.makeText(getContext(),"投票"+toupiao_count_text.getText(),Toast.LENGTH_SHORT).show();
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
        return  dialog;
    }
    private void sendAsk(String s,int flag) {
        WebBase.ask(GROUP_ID, s, flag,new JSONHandler(true,getActivity(),"正在发送...") {
            @Override
            public void onSuccess(JSONObject obj) {
                edit_dialog.dismiss();
                mBlogDetailPinglunEdit.setText("");
                Toast.makeText(getContext(),"提问成功，等待老师回答!",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String err_msg) {
                Toast.makeText(getContext(),"提问失败"+err_msg,Toast.LENGTH_SHORT).show();
            }
        });
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

    public void getGroupMessage(){
        WebBase.getGroupInfo(GROUP_ID, new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                JSONObject group=obj.optJSONObject("group");
                groupbean.setId(group.optString("id"));
                groupbean.setName(group.optString("name"));
                groupbean.setNick_name(group.optString("nickname"));
                groupbean.setAvatar(group.optString("avatar"));
                groupbean.setIs_close(group.optInt("is_close"));
                groupbean.setIs_private(group.optInt("is_private"));
                groupbean.setRoles(group.optInt("roles"));
                groupbean.setFans_level(group.optInt("fans_level"));
                groupbean.setNotice(group.optString("notice"));
                if(group.optInt("is_followed")==0){
                    //未关注
                    groupbean.setIs_recommend(true);
                }else{
                    //已关注
                    groupbean.setIs_recommend(false);
                }
                if(groupbean.getFans_level()<5){
                    is_tf=false;
                }else{
                    is_tf=true;
                }
                setTFmessage(is_tf);
                if(groupbean.getNotice()!=null&& !TextUtils.isEmpty(groupbean.getNotice())){
                    notice_text.setText(groupbean.getNotice());
                    notice_layout.setVisibility(View.VISIBLE);
                }
                Chat1Activity activity= (Chat1Activity) getActivity();
                if(activity!=null){
                    activity.setGroupMessage(groupbean);
                }
            }
            @Override
            public void onFailure(String err_msg) {
                Toast.makeText(getActivity(),err_msg,Toast.LENGTH_SHORT).show();
            }
        });
    }
}

