package com.zbmf.StockGroup.fragment.chat;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.activity.Chat1Activity;
import com.zbmf.StockGroup.activity.ShareMessageActivity;
import com.zbmf.StockGroup.activity.SimulateOneStockCommitActivity;
import com.zbmf.StockGroup.adapter.GiftAdapter1;
import com.zbmf.StockGroup.adapter.GridAdatper;
import com.zbmf.StockGroup.adapter.LiveAdapter;
import com.zbmf.StockGroup.api.GetRedPackHandler;
import com.zbmf.StockGroup.api.JSONHandler;
import com.zbmf.StockGroup.api.OpenRedPackHandler;
import com.zbmf.StockGroup.api.WebBase;
import com.zbmf.StockGroup.beans.BlogBean;
import com.zbmf.StockGroup.beans.Gift;
import com.zbmf.StockGroup.beans.Group;
import com.zbmf.StockGroup.beans.LiveMessage;
import com.zbmf.StockGroup.beans.LiveTypeMessage;
import com.zbmf.StockGroup.beans.RedPackgedBean;
import com.zbmf.StockGroup.beans.Stock;
import com.zbmf.StockGroup.callback.ResultCallback;
import com.zbmf.StockGroup.constans.Constants;
import com.zbmf.StockGroup.constans.IntentKey;
import com.zbmf.StockGroup.db.DBManager;
import com.zbmf.StockGroup.dialog.EditTextDialog;
import com.zbmf.StockGroup.fragment.BaseFragment;
import com.zbmf.StockGroup.service.GetLiveMessage;
import com.zbmf.StockGroup.utils.DateUtil;
import com.zbmf.StockGroup.utils.ImageLoaderOptions;
import com.zbmf.StockGroup.utils.JSONParse;
import com.zbmf.StockGroup.utils.LogUtil;
import com.zbmf.StockGroup.utils.MessageType;
import com.zbmf.StockGroup.utils.SettingDefaultsManager;
import com.zbmf.StockGroup.utils.ShowActivity;
import com.zbmf.StockGroup.utils.WebClickUitl;
import com.zbmf.StockGroup.view.CustomMarqueeTextView;
import com.zbmf.StockGroup.view.MyTextView;
import com.zbmf.StockGroup.view.RoundedCornerImageView;
import com.zbmf.StockGroup.view.SendGiftProgress;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link LiveFragment#newInstance} factory method to
 * create an instance of this fragment.
 * <p>
 * 直播室Fragment
 */
public class LiveFragment extends BaseFragment implements MyTextView.OnTextClickListener, View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "GROUP_ID";
    private static final String TAG = LiveFragment.class.getSimpleName();
    private static final int UPDATE_ADAPTER = 1;
    private static final int LOAD_UPDATE_ADAPTER = 2;
    // TODO: Rename and change types of parameters
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
    private Button send_gift_button, button_layout_visible, add_tf_message;
    private ImageView scanHorizontalLineImageView, scanHorizontalLineImageView_top;

    private Dialog mGiftDialog, edit_dialog;
    private List<Gift> mGiftList = null;
    private List<GridView> views;
    private ViewPager vp_gift;
    private DBManager dbManager;
    private AlertDialog aDialog = null;
    private Group groupbean;
    private String GROUP_ID;
    private LinearLayout notice_layout;
    private boolean is_first = true;
    private boolean is_tf;
    private boolean already_add_allMessage;
    private SendGiftProgress sp;
    private TextView tv_no_fans_message;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATE_ADAPTER:
                    if (infolist.size() == 0) {
                        if (tv_no_fans_message.getVisibility() == View.GONE) {
                            if (just_look_tf) {
                                tv_no_fans_message.setText("暂无铁粉悄悄话");
                            } else {
                                tv_no_fans_message.setText("暂无直播室消息");
                            }
                            tv_no_fans_message.setVisibility(View.VISIBLE);
                        }
                    } else {
                        if (tv_no_fans_message.getVisibility() == View.VISIBLE) {
                            tv_no_fans_message.setVisibility(View.GONE);
                        }
                        adapter.notifyDataSetChanged();
                        new_live_message = adapter.getCount();
                        if (!is_first) {
                            message_list.setSelection((Integer) msg.obj);
                        } else {
                            is_first = false;
                            message_list.setSelection(message_list.getCount() - 1);
                        }
                    }
                    break;
                case LOAD_UPDATE_ADAPTER:

                    break;
            }
        }
    };

    public LiveFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
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
            GROUP_ID = groupbean.getId();
        }
    }

    @Override
    protected View setContentView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_live, null);
    }

    @Override
    protected void initView() {
        message_list = getView(R.id.live_message_list);
        infolist = new ArrayList<>();
        live_new_msg = getView(R.id.live_new_msg);
        chat_activity = (Chat1Activity) getActivity();
        send_gift_button = getView(R.id.send_gift_button);
        button_layout = getView(R.id.button_layout);
        button_layout_visible = getView(R.id.button_layout_visible);
        scanHorizontalLineImageView = getView(R.id.scanHorizontalLineImageView);
        scanHorizontalLineImageView_top = getView(R.id.scanHorizontalLineImageView_top);
        notice_layout = getView(R.id.notice_layout);
        add_tf_message = getView(R.id.add_tf_message);
        notice_text = getView(R.id.notice_text);
        tv_no_fans_message = getView(R.id.tv_no_fans_message);
        getView(R.id.send_ask_button).setOnClickListener(this);
        getView(R.id.icon_notice_close_img).setOnClickListener(this);
        getView(R.id.send_vote).setOnClickListener(this);
        getView(R.id.kf_button).setOnClickListener(this);

        adapter = new LiveAdapter(getActivity(), infolist, is_tf, this, this);
        adapter.setGroup(groupbean);
        adapter.setmListener(new LiveAdapter.OnMenuClickListener() {
            @Override
            public void onMenuClick(View view, int pos) {
                LiveMessage liveMessage = infolist.get(pos);
                String shareMessage = null;
                if (is_tf || liveMessage.getMessage_type().equals(MessageType.CHAT)) {
                    shareMessage = liveMessage.getMessage_countent();
                } else if (liveMessage.getMessage_type().equals(MessageType.FANS)) {
                    shareMessage = getResources().getString(R.string.add_to_tf);
                }
                switch (view.getId()) {
                    case R.id.tv_copy:
                        ((ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE)).setText(shareMessage);
                        showToast("已复制到剪切板");
                        break;
                    case R.id.ll_gift_layout:
                        if (mGiftList == null) {
                            initGiftList(true);
                            return;
                        }
                        if (mGiftDialog == null)
                            mGiftDialog = showGiftDialog();
                        vp_gift.setCurrentItem(0);
                        mGiftDialog.show();
                        break;
                    case R.id.tv_share:
                        groupbean.setMessageId(liveMessage.getMsg_id());
                        groupbean.setShareMessage(shareMessage);
                        groupbean.setMessageDate(DateUtil.getTime(liveMessage.getMessage_time(), Constants.yy_MM_dd_HH_mm));
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(IntentKey.GROUP, groupbean);
                        ShowActivity.showActivity(getActivity(), bundle, ShareMessageActivity.class);
                        break;
                }
            }
        });
        message_list.setAdapter(adapter);
        getGroupInfo();
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
                infolist.clear();
                getDbMessage(new Date().getTime(), false);
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
                infolist.clear();
                getDbMessage(new Date().getTime(), false);
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
        final CheckedTextView just_look_button_layout = (CheckedTextView) getView(R.id.just_look_button_layout);
//        just_look_button = (CheckBox) getView(R.id.just_look_button);
        just_look_button_layout.setChecked(just_look_tf);
        just_look_button_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ShowActivity.isLogin(getActivity())) {
                    if (just_look_tf) {
                        just_look_tf = false;
                        scanHorizontalLineImageView_top.setVisibility(View.VISIBLE);
                        scanHorizontalLineImageView_top.startAnimation(topAnimation);
                    } else {
                        just_look_tf = true;
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
                if (firstVisibleItem == 0 && !is_first) {
                    View firstVisibleItemView = message_list.getChildAt(0);
                    if (firstVisibleItemView != null && firstVisibleItemView.getTop() == 0) {
                        int no_read = totalItemCount - firstVisibleItem - visibleItemCount;
                        if (no_read > 0) {
                            is_bottom = false;
                            getDbMessage(infolist.get(0).getMessage_time(), true);
                        } else {
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
                            is_bottom = true;
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
                if (mGiftList == null) {
                    initGiftList(true);
                    return;
                }
                if (mGiftDialog == null)
                    mGiftDialog = showGiftDialog();
                vp_gift.setCurrentItem(0);
                mGiftDialog.show();
            }
        });
    }

    @Override
    protected void initData() {
        receiver = new UpdateReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.NEW_LIVE_MSG);
        getActivity().registerReceiver(receiver, filter);
        dbManager = new DBManager(getContext());
        dbManager.setUnablemessage(GROUP_ID);
        noticeUpdateReceiver = new NoticeUpdateReceiver();
        IntentFilter noticefilter = new IntentFilter();
        filter.addAction("com.zbmf.StockGroup.notice_id");
        getActivity().registerReceiver(noticeUpdateReceiver, noticefilter);
        long time = new Date().getTime();
        getGroupMessage();
        if (!dbManager.isGroupOnline(GROUP_ID) || DateUtil.MoreThanHour(SettingDefaultsManager.getInstance().getUpdataLive(GROUP_ID), time, 20 * 60)) {
            getMessageList(time, true);
        } else {
            LogUtil.e("本地取数据");
            getDbMessage(time, true);
        }
        initGiftList(false);
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

    public void getGroupInfo() {
        if (ShowActivity.isLogin(getActivity())) {
            WebBase.fansInfo(GROUP_ID, new JSONHandler(true, getActivity(), "正在加载数据...") {
                @Override
                public void onSuccess(JSONObject obj) {
                    JSONObject group = obj.optJSONObject("group");
                    Group groupbean = new Group();
                    groupbean.setId(group.optString("id"));
                    groupbean.setName(group.optString("name"));
                    groupbean.setNick_name(group.optString("nickname"));
                    groupbean.setAvatar(group.optString("avatar"));
                    groupbean.setIs_close(group.optInt("is_close"));
                    groupbean.setIs_private(group.optInt("is_private"));
                    groupbean.setRoles(group.optInt("roles"));
                    int fans_level = group.optInt("fans_level");
                    groupbean.setFans_level(fans_level);
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
                    if (fans_level >= 5) {
                        //铁粉
                        add_tf_message.setVisibility(View.VISIBLE);
                    } else {
                        boolean isShowFans = SettingDefaultsManager.getInstance().getIsShowFans();
                        if (isShowFans) {
                            add_tf_message.setVisibility(View.GONE);
                        } else {
                            add_tf_message.setVisibility(View.VISIBLE);
                        }
                    }
                }

                @Override
                public void onFailure(String err_msg) {

                }
            });
        }
    }

    public void add_to_tf() {
        if (ShowActivity.isLogin(getActivity())) {
            WebBase.fansInfo(GROUP_ID, new JSONHandler(true, getActivity(), "正在加载数据...") {
                @Override
                public void onSuccess(JSONObject obj) {
                    JSONObject group = obj.optJSONObject("group");
                    Group groupbean = new Group();
                    groupbean.setId(group.optString("id"));
                    groupbean.setName(group.optString("name"));
                    groupbean.setNick_name(group.optString("nickname"));
                    groupbean.setAvatar(group.optString("avatar"));
                    groupbean.setIs_close(group.optInt("is_close"));
                    groupbean.setIs_private(group.optInt("is_private"));
                    groupbean.setRoles(group.optInt("roles"));
                    int fans_level = group.optInt("fans_level");
                    groupbean.setFans_level(fans_level);
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
                    ShowActivity.showFansActivity(getActivity(), groupbean);
                }

                @Override
                public void onFailure(String err_msg) {

                }
            });
        }
    }

    public void setTFmessage(boolean is_fans) {
        is_tf = is_fans;
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
                    if (mGiftDialog == null) {
                        mGiftDialog = showGiftDialog();
                    }

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

        if (temps.size() > 0) {
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
                    if (!gift.isChecked())
                        gift = null;
                } else {
                    if (mGift != null)
                        here:for (GridView gridview : views) {
                            GridAdatper adapter = (GridAdatper) gridview.getAdapter();
                            for (Gift gifts : adapter.getGifts()) {
                                if (mGift.getGift_id().equals(gifts.getGift_id())) {
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
                if (mGift != null) {
                    if (!mGift.getCategory().equals("10")) {
                        double my_mfb = Double.valueOf(SettingDefaultsManager.getInstance().getPays());
                        if (sp != null) {
                            if (my_mfb < mGift.getMpays() * gift_num) {
                                sp.setText("去充值");
                            } else {
                                sp.setText("送TA");
                            }
                        }
                    } else {
                        sp.setText("送TA");
                    }
                }
                adatper.notifyDataSetChanged();
            }
        });
        return gridView;
    }

    private int gift_num = 1;

    private Dialog showGiftDialog() {
        final Dialog dialog = new Dialog(getActivity(), R.style.myDialogTheme);
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        View layout = inflater.inflate(R.layout.dialog_gift, null);
        vp_gift = (ViewPager) layout.findViewById(R.id.viewpager);
        final LinearLayout ll_dot = (LinearLayout) layout.findViewById(R.id.ll_dot);
        final TextView tv_mfb = (TextView) layout.findViewById(R.id.tv_mfb);
        final TextView tv_jf = (TextView) layout.findViewById(R.id.tv_jf);
        final TextView tv_num = (TextView) layout.findViewById(R.id.tv_num);
        final TextView tv_add = (TextView) layout.findViewById(R.id.tv_add);
        final TextView tv_delete = (TextView) layout.findViewById(R.id.tv_delete);
        final TextView tv_ten = (TextView) layout.findViewById(R.id.tv_ten);
        final TextView tv_fifty = (TextView) layout.findViewById(R.id.tv_fifty);
        final TextView tv_one_hundred = (TextView) layout.findViewById(R.id.tv_one_hundred);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                gift_num = 1;
            }
        });
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                gift_num = 1;
                tv_num.setText(String.valueOf(gift_num));
            }
        });
        tv_num.setText(String.valueOf(gift_num));
        tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gift_num++;
                tv_num.setText(String.valueOf(gift_num));
            }
        });
        tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gift_num--;
                if (gift_num < 1) {
                    gift_num = 1;
                }
                tv_num.setText(String.valueOf(gift_num));
            }
        });
        tv_ten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gift_num = 10;
                tv_num.setText(String.valueOf(gift_num));
            }
        });
        tv_fifty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gift_num = 50;
                tv_num.setText(String.valueOf(gift_num));
            }
        });
        tv_one_hundred.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gift_num = 100;
                tv_num.setText(String.valueOf(gift_num));
            }
        });
        sp = (SendGiftProgress) layout.findViewById(R.id.send_gift_progress);
        sp.setSendClickListener(new SendGiftProgress.OnSendClickListener() {
            @Override
            public void OnSendClickListener(View view) {
                if (mGift != null && ShowActivity.isLogin(getActivity())) {
                    if (mGift.getCategory().equals("10")) {
                        //积分礼物
                        if (SettingDefaultsManager.getInstance().getPoint() < mGift.getMpays() * gift_num) {
                            Toast.makeText(getActivity(), "积分不足!", Toast.LENGTH_SHORT).show();
                            sp.stopAnim();
                        } else {
                            sendGift(tv_mfb, tv_jf);
                        }
                    } else {
                        //魔方宝礼物
                        double my_mfb = Double.valueOf(SettingDefaultsManager.getInstance().getPays());
                        if (my_mfb < mGift.getMpays() * gift_num) {
                            sp.stopAnim();
                            ShowActivity.showPayDetailActivity(getActivity());
                        } else {
                            sendGift(tv_mfb, tv_jf);
                        }
                    }
                } else {
                    sp.stopAnim();
                    Toast.makeText(getContext(), "请先选择礼物", Toast.LENGTH_SHORT).show();
                }
            }
        });

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

    public void sendGift(final TextView tv_mfb, final TextView tv_jf) {
        WebBase.sendGift(GROUP_ID, mGift.getGift_id(), gift_num, new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                Toast.makeText(getContext(), "送礼成功", Toast.LENGTH_SHORT).show();
                sp.stopAnim();
                getWolle(tv_mfb, tv_jf);
            }

            @Override
            public void onFailure(String err_msg) {
                sp.stopAnim();
                Toast.makeText(getContext(), err_msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getWolle(final TextView tv_mfb, final TextView tv_jf) {
        WebBase.getWalle(new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                JSONObject pays = obj.optJSONObject("pay");
                JSONObject point = obj.optJSONObject("point");
                SettingDefaultsManager.getInstance().setPays(pays.optString("unfrozen"));
                SettingDefaultsManager.getInstance().setPoint(point.optLong("unfrozen"));
                tv_mfb.setText(SettingDefaultsManager.getInstance().getPays());
                tv_jf.setText(String.valueOf(SettingDefaultsManager.getInstance().getPoint()));
            }

            @Override
            public void onFailure(String err_msg) {
            }
        });
    }

    @Override
    public void OnTextClickListener(LiveTypeMessage message) {
        switch (message.getMessage_type()) {
            case "url":
                WebClickUitl.ShowActivity(getActivity(), message.getMessage());
                break;
            case "stock":
                WebBase.getStockRealtimeInfo(message.getMessage(), new JSONHandler() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onSuccess(JSONObject obj) {
                        Stock stock = JSONParse.getStockRealtimeInfo(obj);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(IntentKey.STOCKHOLDER, stock);
                        ShowActivity.showActivity(getActivity(), bundle, SimulateOneStockCommitActivity.class);
                    }

                    @Override
                    public void onFailure(String err_msg) {

                    }
                });
                break;
            case "tf_message":
                add_to_tf();
                break;
            case "commit_fans":
                add_to_tf();
                break;
            case "gift":
                if (mGiftList == null) {
                    initGiftList(true);
                    return;
                }
                if (mGiftDialog == null)
                    mGiftDialog = showGiftDialog();
                vp_gift.setCurrentItem(0);
                mGiftDialog.show();
                break;
            case "box_message":
                if (message.getBox_leaver() <= groupbean.getFans_level()) {
                    ShowActivity.showBoxDetailActivity(getActivity(), GROUP_ID, message.getMessage());
                } else {
                    String toast_message = "您为【***】用户，升级成为【**】即可查";
                    switch (groupbean.getFans_level()) {
                        case 0:
                            toast_message = toast_message.replace("【***】", "非铁粉");
                            break;
                        case 5:
                            toast_message = toast_message.replace("【***】", "体验铁粉");
                            break;
                        case 10:
                            toast_message = toast_message.replace("【***】", "非年粉");
                            break;
                    }
                    if (message.getBox_leaver() == 10) {
                        toast_message = toast_message.replace("【**】", "包月铁粉");
                    } else if (message.getBox_leaver() == 20) {
                        toast_message = toast_message.replace("【**】", "年粉");
                    }
                    Toast.makeText(getActivity(), toast_message, Toast.LENGTH_SHORT).show();
                }
                break;
            case "blog_message":
                getBlogDetail(message.getMessage());
                break;
        }
    }

    public void getBlogDetail(String blog_id) {
        WebBase.searchUserBlogInfo(blog_id, new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                BlogBean blogBean = new BlogBean();
                blogBean.setBlog_id(obj.optString("blog_id"));
                blogBean.setTitle(obj.optString("subject"));
                blogBean.setImg(obj.optString("cover"));
                blogBean.setLook_number(obj.optJSONObject("stat").optString("views"));
                blogBean.setPinglun(obj.optJSONObject("stat").optString("replys"));
                blogBean.setWap_link(obj.optJSONObject("link").optString("wap"));
                blogBean.setApp_link(obj.optJSONObject("link").optString("app"));
                blogBean.setDate(obj.optString("posted_at"));
                ShowActivity.showBlogDetailActivity(getActivity(), blogBean);
            }

            @Override
            public void onFailure(String err_msg) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.kf_button:
                try {
                    String qq_url = "mqqwpa://im/chat?chat_type=crm&uin=2852273339&version=1&src_type=web&web_src=http:://wpa.b.qq.com";
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(qq_url)));
                } catch (ActivityNotFoundException exception) {

                }

                break;
            case R.id.red_bag_layout:
                //抢红包
                String red_id = (String) view.getTag();
                if (red_id != null && ShowActivity.isLogin(getActivity())) {
                    WebBase.openRedPackged(red_id, new OpenRedPackHandler(true, getContext(), "正在开红包...") {
                        @Override
                        public void onSuccess(RedPackgedBean obj) {
                            RedPackgedBean redPackgedBean = obj;
                            if (redPackgedBean != null) {
                                if (redPackgedBean.getRed_status() == 4 || redPackgedBean.getRed_status() == 3) {
                                    ShowActivity.showRedBagActivity(getActivity(), redPackgedBean);
                                } else {
                                    show_red_bag_view(redPackgedBean);
                                }
                            }
                        }

                        @Override
                        public void onFailure(String err_msg) {
                            Toast.makeText(getContext(), err_msg, Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                break;
            case R.id.live_img_id:
                String url = (String) view.getTag();
                if (url != null) {
                    //查看大图
                    ShowActivity.ShowBigImage(getActivity(), url);
                }
                break;
            case R.id.send_ask_button:
                if (ShowActivity.isLogin(getActivity())) {
                    if (edit_dialog == null) {
                        edit_dialog = Editdialog1();
                    }
                    edit_dialog.show();
                }
                break;
            case R.id.icon_notice_close_img:
                notice_layout.setVisibility(View.GONE);
                break;
            case R.id.send_vote:
                Toast.makeText(getContext(), "敬请期待", Toast.LENGTH_SHORT).show();
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
            if (msg.getMessage_type().equals(MessageType.SYSTEM)) {
                if (msg != null) {
                    if (msg.getGroup_id().equals("0") || msg.getGroup_id().equals(GROUP_ID)) {
                        setLiveMessage(msg);
                    }
                }
            } else {
                if (msg.getGroup_id().equals(GROUP_ID)) {
                    setLiveMessage(msg);
                }
            }
        }
    }

    private void setLiveMessage(LiveMessage msg) {
        //当前圈子数据
        if (msg.getMessage_type().equals(MessageType.GIFT)) {
            //礼物消息
            chat_activity.showGift(msg.getGiftbean());
        }
        chat_activity.addMessage(msg);
        dbManager.setMessageUnable(msg.getMsg_id());
        if (just_look_tf) {
            addTfMessage(msg);
        } else {
            addMessage(msg);
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

    public void getMessageList(final long time, final boolean is_show) {
        if (already_add_allMessage) {
            return;
        }
        WebBase.getLiveMsg(groupbean.getId(), time, just_look_tf, new JSONHandler(is_show, getContext(), "获取聊天记录...") {
            @Override
            public void onSuccess(JSONObject obj) {
                LogUtil.d("消息：" + obj);
                groupbean.setIs_online(1);
                dbManager.addGroup(groupbean);
                SettingDefaultsManager.getInstance().setUpdataLive(time, GROUP_ID);
                if (!obj.isNull("msgs")) {
                    JSONArray msgs = obj.optJSONArray("msgs");
                    int size = msgs.length() - 1;
                    List<LiveMessage> info = new ArrayList<LiveMessage>();
                    for (int i = size; i >= 0; i--) {
                        JSONObject oj = msgs.optJSONObject(i);
                        LiveMessage lm = GetLiveMessage.getMessage(oj, true);
                        info.add(0, lm);
                    }
                    int info_size = info.size();
                    if (info_size > 0) {
                        dbManager.addAll(info, new ResultCallback<Boolean>() {
                            @Override
                            public void onSuccess(Boolean str) {
                                if (str) {
                                    getDbMessage(time, is_show);
                                }
                            }

                            @Override
                            public void onError(String message) {
                            }
                        });
                    } else {
                        already_add_allMessage = true;
                    }
                }
            }

            @Override
            public void onFailure(String err_msg) {
                Toast.makeText(getContext(), "加载失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getMoreMessageList(final long time, final boolean is_show) {
        if (already_add_allMessage) {
            if (infolist.size() == 0) {
                if (tv_no_fans_message.getVisibility() == View.GONE) {
                    if (just_look_tf) {
                        tv_no_fans_message.setText("暂无铁粉悄悄话");
                    } else {
                        tv_no_fans_message.setText("暂无直播室消息");
                    }
                    tv_no_fans_message.setVisibility(View.VISIBLE);
                }
            }
            return;
        }
        WebBase.getLiveMsg(groupbean.getId(), time, just_look_tf, new JSONHandler(is_show, getContext(), "获取聊天记录...") {
            @Override
            public void onSuccess(JSONObject obj) {

                JSONArray msgs = obj.optJSONArray("msgs");
                int size = msgs.length() - 1;
                List<LiveMessage> info = new ArrayList<LiveMessage>();
                for (int i = size; i >= 0; i--) {
                    JSONObject oj = msgs.optJSONObject(i);
                    LiveMessage lm = GetLiveMessage.getMessage(oj, true);
                    info.add(0, lm);
                }
                int info_size = info.size();
                if (info_size > 0) {
                    dbManager.addAll(info, new ResultCallback<Boolean>() {
                        @Override
                        public void onSuccess(Boolean str) {
                            if (str) {
                                getDbMessage(time, is_show);
                            }
                        }

                        @Override
                        public void onError(String message) {

                        }
                    });
                } else {
                    already_add_allMessage = true;
                    if (infolist.size() == 0) {
                        if (tv_no_fans_message.getVisibility() == View.GONE) {
                            if (just_look_tf) {
                                tv_no_fans_message.setText("暂无铁粉悄悄话");
                            } else {
                                tv_no_fans_message.setText("暂无直播室消息");
                            }
                            tv_no_fans_message.setVisibility(View.VISIBLE);
                        }
                    }
                }

            }

            @Override
            public void onFailure(String err_msg) {
                Toast.makeText(getContext(), "加载失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void UpdataAdapter(int size) {
        Message message = new Message();
        message.what = UPDATE_ADAPTER;
        message.obj = size;
        handler.sendMessage(message);
    }

    public void getDbMessage(final long time, final boolean show) {
        handler.sendEmptyMessage(LOAD_UPDATE_ADAPTER);
        dbManager.query(GROUP_ID, time, just_look_tf, new ResultCallback<List<LiveMessage>>() {
            @Override
            public void onSuccess(List<LiveMessage> info) {
                infolist.addAll(0, info);
                UpdataAdapter(info.size());
            }

            @Override
            public void onError(String message) {
                getMoreMessageList(time, show);
            }
        });

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
        if (noticeUpdateReceiver != null) {
            getActivity().unregisterReceiver(noticeUpdateReceiver);
        }
        if (dbManager != null) {
            dbManager.closeDB();
        }
        Intent intent = new Intent(Constants.NEW_LIVE_MSG_READ);
        getContext().sendBroadcast(intent);
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

    private TextView red_bag_message, have_red_bag;
    private ImageView click_red_bag_button_anim;
    private Button into_red_bag_detail;

    private void show_red_bag_view(final RedPackgedBean redbean) {
        if (aDialog == null) {
            aDialog = new AlertDialog.Builder(getActivity()).show();
        } else {
            aDialog.show();
        }
        aDialog.getWindow().setContentView(R.layout.ciclk_red_bag_layout);
        aDialog.getWindow().setGravity(Gravity.CENTER);
        RoundedCornerImageView avatar = (RoundedCornerImageView) aDialog.getWindow().findViewById(R.id.red_from_user_avatar);
        TextView red_bag_from_name = (TextView) aDialog.getWindow().findViewById(R.id.red_bag_from_username);
        red_bag_message = (TextView) aDialog.getWindow().findViewById(R.id.red_bag_user_message);
        have_red_bag = (TextView) aDialog.getWindow().findViewById(R.id.have_red_bag);
        click_red_bag_button_anim = (ImageView) aDialog.getWindow().findViewById(R.id.click_red_bag_button_anim);

//        ViewFactory.imgCircleView(getActivity(),redbean.getUser_avatar(), avatar);
        ImageLoader.getInstance().displayImage(redbean.getUser_avatar(), avatar, ImageLoaderOptions.AvatarOptions());
        final ImageButton click_red_bag_button = (ImageButton) aDialog.getWindow().findViewById(R.id.click_red_bag_button);
        ImageButton return_button = (ImageButton) aDialog.getWindow().findViewById(R.id.return_button);
        into_red_bag_detail = (Button) aDialog.getWindow().findViewById(R.id.into_red_bag_detail);
        red_bag_from_name.setText(redbean.getUser_name());

        if (redbean.getRed_status() == 0) {
            red_bag_message.setText(redbean.getRed_message());
            click_red_bag_button.setVisibility(View.VISIBLE);
            have_red_bag.setVisibility(View.VISIBLE);
            //拆红包按钮
            click_red_bag_button.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    if (ShowActivity.isLogin(getActivity())) {
                        click_red_bag_button_anim.setVisibility(View.VISIBLE);
                        click_red_bag_button.setVisibility(View.INVISIBLE);
                        AnimationDrawable animationDrawable = (AnimationDrawable) click_red_bag_button_anim.getDrawable();
                        animationDrawable.start();
                        GetRedBagMessage(redbean.getRed_id());
                    }
                }
            });
        } else {
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

    private void GetRedBagMessage(final String red_id) {
        WebBase.getRedPackged(red_id, new GetRedPackHandler() {
            @Override
            public void onSuccess(RedPackgedBean obj) {
                RedPackgedBean red = (RedPackgedBean) obj;
                if (red.getRed_status() == 4) {
                    ShowActivity.showRedBagActivity(getActivity(), red);
                    if (aDialog != null) {
                        aDialog.dismiss();
                    }
                } else if (red.getRed_status() == 3) {
                    NO_RED_BAG(red);
                }
            }

            @Override
            public void onFailure(String err_msg) {
                Toast.makeText(getContext(), err_msg, Toast.LENGTH_SHORT).show();
                aDialog.dismiss();
            }
        });
    }

    private void NO_RED_BAG(final RedPackgedBean redbean) {
        red_bag_message.setText("手慢了，红包派完了");
        have_red_bag.setVisibility(View.INVISIBLE);
        click_red_bag_button_anim.setVisibility(View.INVISIBLE);
        into_red_bag_detail.setVisibility(View.VISIBLE);
        into_red_bag_detail.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                aDialog.dismiss();
                ShowActivity.showRedBagActivity(getActivity(), redbean);
            }
        });
    }

    private boolean mIsTitleHide = false;
    private float lastX = 0;
    private float lastY = 0;

    public boolean dispatchTouchEvent(MotionEvent event) {
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
            LogUtil.d("id  :" + group_id);


            if (group_id.equals(GROUP_ID)) {
                String countent = intent.getStringExtra("message");
                notice_text.setText(countent);
                if (notice_layout.getVisibility() == View.GONE) {
                    notice_layout.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private Dialog Editdialog1() {
        return EditTextDialog.createDialog(getContext(), R.style.myDialogTheme)
                .setEditHint("写下你的问题")
                .setLeftButton("私密提问")
                .setRightButton("提问")
                .setEmailVisibility(View.GONE)
                .setSendClick(new EditTextDialog.OnSendClick() {
                    @Override
                    public void onSend(String message, int type) {
                        sendAsk(message, type);
                    }
                });
    }

    private Dialog TouPiao() {
        final Dialog dialog = new Dialog(getContext(), R.style.myDialogTheme);
        View layout = LayoutInflater.from(getContext()).inflate(R.layout.toupiao_layout, null);
        TextView toupiao_tercher_name = (TextView) layout.findViewById(R.id.toupiao_tercher_name);
        TextView month_arrow = (TextView) layout.findViewById(R.id.month_arrow);
        TextView all_arrow = (TextView) layout.findViewById(R.id.all_arrow);
        final TextView tp_need_mfb = (TextView) layout.findViewById(R.id.tp_need_mfb);
        final TextView toupiao_count_text = (TextView) layout.findViewById(R.id.toupiao_count_text);
        Button send = (Button) layout.findViewById(R.id.toupiao_tercher_button);
        Button plus_button = (Button) layout.findViewById(R.id.plus_button);
        Button add_button = (Button) layout.findViewById(R.id.add_button);
        RoundedCornerImageView toupiao_tercher_avatar = (RoundedCornerImageView) layout.findViewById(R.id.toupiao_tercher_avatar);
//        ViewFactory.imgCircleView(getActivity(),groupbean.getAvatar(), toupiao_tercher_avatar);
        ImageLoader.getInstance().displayImage(groupbean.getAvatar(), toupiao_tercher_avatar, ImageLoaderOptions.AvatarOptions());
        toupiao_tercher_name.setText(groupbean.getNick_name());
        month_arrow.setText("月排名" + 12);
        all_arrow.setText("总排名" + 122);
        plus_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = Integer.valueOf(toupiao_count_text.getText().toString());
                count -= 1;
                if (count > 1) {
                    toupiao_count_text.setText(count + "");
                    tp_need_mfb.setText(count * 2 + "");
                } else {
                    toupiao_count_text.setText("1");
                    tp_need_mfb.setText("2");
                }
            }
        });
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = Integer.valueOf(toupiao_count_text.getText().toString());
                count += 1;
                toupiao_count_text.setText(count + "");
                tp_need_mfb.setText(count * 2 + "");
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                sendAsk(blog_detail_pinglun_edit.getText().toString());
                Toast.makeText(getContext(), "投票" + toupiao_count_text.getText(), Toast.LENGTH_SHORT).show();
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
        return dialog;
    }

    private void sendAsk(String s, int flag) {
        WebBase.ask(GROUP_ID, s, flag, new JSONHandler(true, getActivity(), "正在发送...") {
            @Override
            public void onSuccess(JSONObject obj) {
                edit_dialog.dismiss();
                Toast.makeText(getContext(), "提问成功，等待老师回答!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String err_msg) {
                Toast.makeText(getContext(), "提问失败" + err_msg, Toast.LENGTH_SHORT).show();
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

    public void getGroupMessage() {
        WebBase.getGroupInfo(GROUP_ID, new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                LogUtil.d("obj:" + obj);
                if (!obj.isNull("group")) {
                    JSONObject group = obj.optJSONObject("group");
                    groupbean = JSONParse.getGroup(group);
                    if (groupbean.getFans_level() < 5) {
                        is_tf = false;
                    } else {
                        is_tf = true;
                    }
                    setTFmessage(is_tf);
                    if (!TextUtils.isEmpty(groupbean.getNotice())) {
                        notice_text.setText(groupbean.getNotice());
                        notice_layout.setVisibility(View.VISIBLE);
                    }
                    Chat1Activity activity = (Chat1Activity) getActivity();
                    if (activity != null) {
                        activity.setGroupMessage(groupbean);
                    }
                }

            }

            @Override
            public void onFailure(String err_msg) {
                Toast.makeText(getActivity(), err_msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (dbManager != null) {
            dbManager.setUnablemessage(GROUP_ID);
        }
    }
}

