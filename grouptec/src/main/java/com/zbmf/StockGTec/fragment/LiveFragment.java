package com.zbmf.StockGTec.fragment;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zbmf.StockGTec.R;
import com.zbmf.StockGTec.activity.Chat1Activity;
import com.zbmf.StockGTec.activity.LiveMessageActivity;
import com.zbmf.StockGTec.activity.SendHBActivity;
import com.zbmf.StockGTec.adapter.LiveAdapter;
import com.zbmf.StockGTec.api.GetRedPackHandler;
import com.zbmf.StockGTec.api.JSONHandler;
import com.zbmf.StockGTec.api.OpenRedPackHandler;
import com.zbmf.StockGTec.api.WebBase;
import com.zbmf.StockGTec.beans.BlogBean;
import com.zbmf.StockGTec.beans.LiveMessage;
import com.zbmf.StockGTec.beans.LiveTypeMessage;
import com.zbmf.StockGTec.beans.RedPackgedBean;
import com.zbmf.StockGTec.db.DBManager;
import com.zbmf.StockGTec.service.GetLiveMessage;
import com.zbmf.StockGTec.utils.ImageLoaderOptions;
import com.zbmf.StockGTec.utils.MessageType;
import com.zbmf.StockGTec.utils.SettingDefaultsManager;
import com.zbmf.StockGTec.utils.ShowActivity;
import com.zbmf.StockGTec.view.MyTextView;
import com.zbmf.StockGTec.view.RoundedCornerImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LiveFragment extends Fragment implements MyTextView.OnTextClickListener, View.OnClickListener {
    private static final String ARG_PARAM1 = "GROUP_ID";
    private static final String TAG = LiveFragment.class.getSimpleName();

    private View mfragment;
    private ListView message_list;
    private LiveAdapter adapter;
    private List<LiveMessage> infolist;
    private UpdateReceiver receiver;
    private boolean is_bottom;
    private TextView live_new_msg;
    private int new_live_message;

    private CheckBox just_look_button;
    private Chat1Activity chat_activity;

    private LinearLayout button_layout;
    private Button  button_layout_visible;

    private DBManager dbManager;
    private AlertDialog aDialog = null;
    private String GROUP_ID;
    private boolean isFirst = true;

    public LiveFragment() {
    }

    public static LiveFragment newInstance(String group_id) {
        LiveFragment fragment = new LiveFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, group_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            GROUP_ID = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mfragment == null) {
            mfragment = inflater.inflate(R.layout.fragment_live, container, false);
        }
        receiver = new UpdateReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(getContext().getPackageName() + "new_live_msg");
        getActivity().registerReceiver(receiver, filter);
        dbManager = new DBManager(getContext());
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (isFirst) {
            message_list = (ListView) mfragment.findViewById(R.id.live_message_list);
            infolist = new ArrayList<>();
            adapter = new LiveAdapter(getActivity(), infolist, true, this, this);
            message_list.setAdapter(adapter);
            live_new_msg = (TextView) mfragment.findViewById(R.id.live_new_msg);
            chat_activity = (Chat1Activity) getActivity();
            button_layout = (LinearLayout) mfragment.findViewById(R.id.button_layout);
            button_layout_visible = (Button) mfragment.findViewById(R.id.button_layout_visible);
            Button add_tf_message = (Button) mfragment.findViewById(R.id.add_tf_message);
            mfragment.findViewById(R.id.send_ask_button).setOnClickListener(this);
            mfragment.findViewById(R.id.iv_hongbao).setOnClickListener(this);
            mfragment.findViewById(R.id.iv_img).setOnClickListener(this);
            mfragment.findViewById(R.id.iv_fasong).setOnClickListener(this);

            message_list.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView absListView, int i) {

                }

                @Override
                public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                    if (i == 0) {
                        View firstVisibleItemView = message_list.getChildAt(0);
                        if (firstVisibleItemView != null && firstVisibleItemView.getTop() == 0) {
                            if (infolist != null && infolist.size() > 0) {
                                long time = infolist.get(0).getMessage_time();
                                getMessageList(time, true);
                            }
                        }
                    }
                }
            });

            getMessageList(new Date().getTime(), true);
            isFirst = false;

            if (SettingDefaultsManager.getInstance().isManager()) {
                mfragment.findViewById(R.id.iv_hongbao).setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void OnTextClickListener(LiveTypeMessage message) {
        switch (message.getMessage_type()) {
            case "url":
                ShowActivity.showWebViewActivity(getActivity(), message.getMessage());
                break;
            case "blog_message":
                getBlogDetail(message.getMessage());
                break;
        }
    }

    public void getBlogDetail(String blog_id) {
        WebBase.searchUserBlogInfo(blog_id, new JSONHandler(getActivity()) {
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
            case R.id.red_bag_layout:
                //抢红包
                String red_id = (String) view.getTag();
                if (red_id != null) {
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
            case R.id.iv_hongbao:
                ShowActivity.showActivity(getActivity(), SendHBActivity.class);
                break;
            case R.id.iv_fasong:
                ShowActivity.showActivity(getActivity(), LiveMessageActivity.class);
                break;
        }
    }

    public class UpdateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //获取service传过来的信息
            LiveMessage msg = (LiveMessage) intent.getSerializableExtra("new_live_msg");
            if (msg.getGroup_id().equals(GROUP_ID) || msg.getMessage_type().equals(MessageType.SYSTEM)) {
                if (msg.getMessage_type().equals(MessageType.GIFT)) {
                    //礼物消息
                    chat_activity.showGift(msg.getGiftbean());
                } else {
                    //直播新消息
                    dbManager.add(msg);
                    addMessage(msg);
                    if (is_bottom) {
                        message_list.smoothScrollToPosition(adapter.getCount());
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
    }


    public void addMessage(LiveMessage cm) {
        infolist.add(cm);
        adapter.notifyDataSetChanged();
        message_list.smoothScrollToPosition(adapter.getCount());
    }

    public void getMessageList(final long time, boolean is_show) {
        WebBase.getLiveMsg(GROUP_ID, time, new JSONHandler(is_show, getContext(), "获取聊天记录...") {
            @Override
            public void onSuccess(JSONObject obj) {
                JSONArray msgs = obj.optJSONArray("msgs");
                int size = msgs.length() - 1;
                List<LiveMessage> info = new ArrayList<LiveMessage>();
                for (int i = size; i >= 0; i--) {
                    JSONObject oj = msgs.optJSONObject(i);
                    LiveMessage lm = GetLiveMessage.getMessage(oj);
                    info.add(lm);
                }
                int info_size = info.size();
                if (info_size > 0) {
                    infolist.addAll(0, info);
                    adapter.notifyDataSetChanged();
                    message_list.setSelection(info.size());
                }
            }

            @Override
            public void onFailure(String err_msg) {
                Toast.makeText(getContext(), err_msg, Toast.LENGTH_SHORT).show();
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
                    click_red_bag_button_anim.setVisibility(View.VISIBLE);
                    click_red_bag_button.setVisibility(View.INVISIBLE);
                    AnimationDrawable animationDrawable = (AnimationDrawable) click_red_bag_button_anim.getDrawable();
                    animationDrawable.start();
                    GetRedBagMessage(redbean.getRed_id());
                }
            });
        } else {
            NO_RED_BAG(redbean);
        }
        return_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                aDialog.dismiss();
            }
        });
    }

    private void GetRedBagMessage(String red_id) {
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
                aDialog.dismiss();
                ShowActivity.showRedBagActivity(getActivity(), redbean);
            }
        });
    }
}
