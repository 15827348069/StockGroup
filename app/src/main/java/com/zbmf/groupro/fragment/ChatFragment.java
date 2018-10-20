package com.zbmf.groupro.fragment;

import android.content.BroadcastReceiver;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zbmf.groupro.R;
import com.zbmf.groupro.activity.Chat1Activity;
import com.zbmf.groupro.adapter.EmojiAdapter1;
import com.zbmf.groupro.adapter.EmojiAdatper;
import com.zbmf.groupro.adapter.GiftAdapter1;
import com.zbmf.groupro.adapter.MessageAdapter;
import com.zbmf.groupro.api.ChatHandler;
import com.zbmf.groupro.api.JSONHandler;
import com.zbmf.groupro.api.WebBase;
import com.zbmf.groupro.beans.ChatCatalog;
import com.zbmf.groupro.beans.ChatMessage;
import com.zbmf.groupro.beans.Group;
import com.zbmf.groupro.db.Database;
import com.zbmf.groupro.utils.ChatMessageComparator;
import com.zbmf.groupro.utils.Constants;
import com.zbmf.groupro.utils.DateUtil;
import com.zbmf.groupro.utils.DisplayUtil;
import com.zbmf.groupro.utils.EditTextUtil;
import com.zbmf.groupro.utils.JSONParse;
import com.zbmf.groupro.utils.MessageType;
import com.zbmf.groupro.utils.SettingDefaultsManager;
import com.zbmf.groupro.utils.Utils;
import com.zbmf.groupro.view.KeyboardLayout;
import com.zbmf.groupro.view.PullToRefreshBase;
import com.zbmf.groupro.view.PullToRefreshListView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.zbmf.groupro.view.KeyboardLayout.KEYBOARD_STATE_HIDE;
import static com.zbmf.groupro.view.KeyboardLayout.KEYBOARD_STATE_SHOW;

public class ChatFragment extends Fragment implements View.OnClickListener/*, AdapterView.OnItemClickListener */ {
    private static final String ARG_PARAM1 = "group";
    private static final String TAG = ChatFragment.class.getSimpleName();

    private String mParam1;
    private KeyboardLayout mKeyboardLayout;
    private EditText ed_msg;
    private TextView tv_send, tv_unread, tv_return, tv_reply;
    //    private ViewPager viewpager;
    private GridView gridview;
    private List<String> emojiNames = new ArrayList<>();
    private EmojiAdatper adatper;
    private InputMethodManager imm;
    private LinearLayout ll_expand, ll_tip, ll_pa;
    private PullToRefreshListView psr;
    private ListView listview;
    private List<ChatMessage> messages = new ArrayList<>();
    private ImageView iv_close;

    private MessageAdapter messageAdapter;
    private ChatMessage mMessage = null;
    private ClipboardManager mClipboardManager;
    private ImageView iv_emoji;
    private UpdateReceiver receiver;

    //    private DelMsgReceiver mDelMsgReceiver;
    private boolean is_bottom;
    private Database db;
    private String groupId;
    private static final int PAGE_SIZE = 20;
    private int KEYBOARD_STATE = KEYBOARD_STATE_HIDE;
    private ViewPager viewPager;
    private List<GridView> gridviews;
    private LinearLayout ll_dot;
    private Chat1Activity chat_activity;
    private int role;


    public ChatFragment() {
    }

    public void livenotifyDataSetChanged() {
        if (messageAdapter != null) {
            messageAdapter.notifyDataSetChanged();
            if (is_bottom) {
                listview.smoothScrollToPosition(messageAdapter.getCount());
            }
        }
    }

    public static ChatFragment newInstance(Group group) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, group);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            Group group = (Group) getArguments().getSerializable(ARG_PARAM1);
            groupId = group.getId();
            role = group.getRoles();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        imm = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        chat_activity = (Chat1Activity) getActivity();

        setupView(view);
        fragmentregisterReceiver();
    }

    public void fragmentregisterReceiver() {
        receiver = new UpdateReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.zbmf.StockGroup.chat_msg");
        getActivity().registerReceiver(receiver, filter);
    }


    private void setupView(View view) {
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        ll_dot = (LinearLayout) view.findViewById(R.id.ll_dot);
        ll_expand = (LinearLayout) view.findViewById(R.id.ll_expand);
        ll_tip = (LinearLayout) view.findViewById(R.id.ll_tip);
        ll_pa = (LinearLayout) view.findViewById(R.id.ll_pa);
        psr = (PullToRefreshListView) view.findViewById(R.id.listview);
        listview = psr.getRefreshableView();
        mKeyboardLayout = (KeyboardLayout) view.findViewById(R.id.keyboard);
        ed_msg = (EditText) view.findViewById(R.id.ed_msg);
        tv_send = (TextView) view.findViewById(R.id.tv_send);
        tv_unread = (TextView) view.findViewById(R.id.tv_unread);
        tv_return = (TextView) view.findViewById(R.id.tv_return);
        tv_reply = (TextView) view.findViewById(R.id.tv_reply);
        iv_close = (ImageView) view.findViewById(R.id.iv_close);
        iv_close.setOnClickListener(this);
        tv_unread.setOnClickListener(this);
        tv_reply.setOnClickListener(this);
        tv_send.setOnClickListener(this);
//        viewpager = (ViewPager)view.findViewById(R.id.viewpager);
        gridview = (GridView) view.findViewById(R.id.gridview);

        int item_spacing = DisplayUtil.dip2px(getActivity(), 8);
        int width = DisplayUtil.getScreenWidthPixels(getActivity());
        int itemWidth = (width - item_spacing * 8) / 7;

        initGridView(itemWidth);

        adatper = new EmojiAdatper(getActivity(), emojiNames, itemWidth);
        gridview.setAdapter(adatper);
        iv_emoji = (ImageView) view.findViewById(R.id.iv_emoji);
        iv_emoji.setOnClickListener(this);
        mKeyboardLayout.setOnkbdStateListener(new KeyboardLayout.onKybdsChangeListener() {
            @Override
            public void onKeyBoardStateChange(int state) {
                switch (state) {
                    case KEYBOARD_STATE_HIDE:
                        Log.e(TAG, "onKeyBoardStateChange: KEYBOARD_STATE_HIDE");
                        ed_msg.setFocusable(false);
                        ed_msg.setFocusableInTouchMode(false);
                        break;
                    case KEYBOARD_STATE_SHOW:
//                        ll_expand.setVisibility(View.GONE);
                        Log.e(TAG, "onKeyBoardStateChange: KEYBOARD_STATE_SHOW");
                        listview.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                        break;
                }
            }
        });

        ed_msg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ll_expand.setVisibility(View.GONE);
                iv_emoji.setSelected(false);
                ed_msg.setFocusableInTouchMode(true);
                ed_msg.setFocusable(true);
                ed_msg.requestFocus();
                return false;
            }
        });
        ed_msg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                content = s.toString();
                if (content.trim().length() > 0)
                    tv_send.setEnabled(true);
                else
                    tv_send.setEnabled(false);
            }
        });

        messageAdapter = new MessageAdapter(getActivity(), messages);
        messageAdapter.setListener(new MessageAdapter.OnMenuClickListener() {
            @Override
            public void onMenuClick(View v, int pos) {
                mMessage = messages.get(pos);
                switch (v.getId()) {
                    case R.id.tv_item1:
                        if (mClipboardManager == null)
                            mClipboardManager = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                        mClipboardManager.setText(mMessage.getContent());
                        break;
                    case R.id.tv_item2:
                        partStr = "回复" + mMessage.getNickname() + "：" + mMessage.getContent() + "\n";
                        SpannableString rebackStr = EditTextUtil.getContent(getActivity(), tv_return, partStr);
                        tv_return.setVisibility(View.VISIBLE);
                        tv_return.setText(rebackStr);
                        ed_msg.requestFocus();
//                        imm.showSoftInput(ed_msg,InputMethodManager.SHOW_IMPLICIT);
                        break;
                    case R.id.tv_item3:
                        complaint(mMessage.getMsg_id());
                        break;

                    case R.id.iv_error:
                        sendMessage(mMessage);
                        break;
                }
            }
        });

        psr.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
//                new getChatMessages().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                if (messages.size() > 0)
                    lastTime = messages.get(0).getTime();
                getRoomMsg();
            }
        });

        listview.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);//设置列表始终可以滑动
        listview.setAdapter(messageAdapter);
        listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem == 0) {
                    View firstVisibleItemView = listview.getChildAt(0);
                    if (firstVisibleItemView != null && firstVisibleItemView.getTop() == 0) {
                        int no_read = totalItemCount - firstVisibleItem - visibleItemCount;
                        if (no_read > 0) {
                            is_bottom = false;
//                            lastTime = messages.get(0).getTime();
//                            getRoomMsg();
//                            new getChatMessages().execute(pageIndex);
                        } else {
                            is_bottom = true;
                        }
                    }
                } else if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
                    View lastVisibleItemView = listview.getChildAt(listview.getChildCount() - 1);
                    if (lastVisibleItemView != null && lastVisibleItemView.getBottom() == listview.getHeight()) {
                        is_bottom = true;
                        new_live_message = 0;
                        tv_unread.setVisibility(View.INVISIBLE);
                        chat_activity.setLiveNumbeGone(1);
                    }
                } else {
                    is_bottom = false;
                    //向上滑动隐藏按钮
                    if (new_live_message != 0 && firstVisibleItem + visibleItemCount > new_live_message) {
                        int no_read = totalItemCount - firstVisibleItem - visibleItemCount;
                        new_live_message = firstVisibleItem + visibleItemCount;
                        if (no_read == 0) {
                            is_bottom = true;
                            tv_unread.setVisibility(View.GONE);
                            return;
                        }
                        tv_unread.setText(String.valueOf(no_read));
                        tv_unread.setVisibility(View.VISIBLE);
                        chat_activity.setlive_number_text(1, no_read);
                    }
                }
                Log.e(TAG, "onScroll: " + is_bottom);
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }
        });
        listview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
//                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                imm.hideSoftInputFromWindow(ed_msg.getWindowToken(), 0);
                if (ll_expand.getVisibility() == View.VISIBLE) {
                    iv_emoji.setSelected(false);
                    ll_expand.setVisibility(View.GONE);
                }

                return false;
            }
        });
//        gridview.setOnItemClickListener(this);


        db = new Database(getActivity());
//        new getChatMessages().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        getRoomMsg();


        Intent intent3 = new Intent();
        intent3.setAction("com.zbmf.StockGroup.UNREADNUM");
        getActivity().sendBroadcast(intent3);
    }

    @Override
    public void onResume() {
        super.onResume();
        db.updateUnReadNum(groupId, 0);
    }

    private int no_read = 0;
    private int new_live_message = 10;
    private String lastTime = DateUtil.getTimes() + "";
    private Handler iHandler = new Handler();
    private long db_time = 0, server_time;

    class getChatMessages extends AsyncTask<Void, Void, List<ChatMessage>> {
        private int index;

        @Override
        protected List<ChatMessage> doInBackground(Void... params) {
            index = messages.size();
            ChatMessage cm = db.getLastMessage(groupId);
            if (cm != null) {
                db_time = Long.parseLong(cm.getTime());
            }

            return db.getChatMessages(groupId, index, PAGE_SIZE);
        }

        @Override
        protected void onPostExecute(List<ChatMessage> chatMessages) {
            super.onPostExecute(chatMessages);
            psr.onRefreshComplete();
            if (chatMessages != null) {
                if (index != 0) {
                    listview.setTranscriptMode(ListView.TRANSCRIPT_MODE_DISABLED);
                }
                messageAdapter.addDataList(chatMessages);
                final int pos = chatMessages.size();
                if (index != 0) {
                    iHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            listview.setSelectionFromTop(pos + 1, 0);
                        }
                    }, 1);
                } else
                    listview.setSelection(listview.getBottom());
            } else {
                getRoomMsg();
            }
        }
    }

    private void getRoomMsg() {
        WebBase.getRoomMsg(groupId, lastTime, new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                psr.onRefreshComplete();
                ChatMessage chatMessage = JSONParse.getRoomMsg(obj);
                if (chatMessage != null && chatMessage.getMessages() != null) {
                    List<ChatMessage> tempMessages = chatMessage.getMessages();
                    Collections.sort(tempMessages, new ChatMessageComparator());
//                    String time = tempMessages.get(tempMessages.size() - 1).getTime();
//                    server_time = Long.parseLong(time);
//                    if (server_time > db_time)
//                        for (int i = 0; i < tempMessages.size(); i++) {
//                            ChatMessage message = tempMessages.get(i);
//                            db.addChat(groupId, message);
//                        }
                    for (int i = 0; i < tempMessages.size(); i++) {
                        ChatMessage message = tempMessages.get(i);
                        db.addChat(groupId, message);
                    }

                    listview.setTranscriptMode(ListView.TRANSCRIPT_MODE_DISABLED);

                    messages.addAll(0, tempMessages);
                    messageAdapter.notifyDataSetChanged();
                    listview.setSelection(tempMessages.size());

                    if(messages.size()==0){
                        Toast.makeText(getActivity(),"暂无直播",0).show();
                    }
                }
            }

            @Override
            public void onFailure(String err_msg) {
                psr.onRefreshComplete();
            }
        });
    }

    @NonNull
    private ChatCatalog addChatCatalog(ChatMessage message) {
        ChatCatalog catalog = new ChatCatalog();
        catalog.setGroup_id(message.getTo() + "");
        catalog.setUnreadnum(0);
        catalog.setType(0);
        catalog.setMsg_id(message.getMsg_id());
        catalog.setTime(Long.parseLong(message.getTime()));
        return catalog;
    }

    private String content = "";
    private String partStr = "";

    private void initGridView(int itemWidth) {
        gridviews = new ArrayList<>();
        List<String> temps = new ArrayList<>();
        for (String name : Constants.getEmojiIconMaps().keySet()) {
            temps.add(name);
            if (temps.size() == 20) {
                gridviews.add(getGridView(itemWidth, temps));
                temps = new ArrayList<>();
            }
        }

        if (temps.size() != 0) {
            gridviews.add(getGridView(itemWidth, temps));
        }

        GiftAdapter1 adapter1 = new GiftAdapter1(getActivity(), gridviews);
        viewPager.setAdapter(adapter1);

        for (int i = 0; i < gridviews.size(); i++) {
            ImageView point = new ImageView(getActivity());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            params.rightMargin = 10;
            if (i == gridviews.size() - 1)
                params.rightMargin = 0;
//          point.setBackgroundResource(R.drawable.point_bg);
            if (i == 0) {
                point.setImageResource(R.drawable.selp);
            } else {
                point.setImageResource(R.drawable.unselp);
            }
            ll_dot.addView(point, params);
        }

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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

                if (position == 0) {

                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @NonNull
    private GridView getGridView(int itemWidth, List<String> temps) {
        GridView gv = (GridView) View.inflate(getActivity(), R.layout.emoji_layout, null);
        EmojiAdapter1 emojiAdapter1 = new EmojiAdapter1(getActivity(), temps, itemWidth);
        gv.setAdapter(emojiAdapter1);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == parent.getAdapter().getCount() - 1) {
                    ed_msg.dispatchKeyEvent(new KeyEvent(
                            KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
                } else {
                    String emoji = (String) parent.getItemAtPosition(position);
                    int selection = ed_msg.getSelectionStart();
                    StringBuilder sb = new StringBuilder();
                    sb.append(content.substring(0, selection)).append(emoji).append(content.substring(selection, content.length()));
                    ed_msg.setText(EditTextUtil.getContent(getActivity(), ed_msg, sb.toString()));
                    ed_msg.setSelection(selection + emoji.length());
                }
            }
        });
        return gv;
    }


//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        String emoji = (String) parent.getItemAtPosition(position);
//        int selection = ed_msg.getSelectionStart();
//        StringBuilder sb = new StringBuilder();
//        sb.append(content.substring(0, selection)).append(emoji).append(content.substring(selection, content.length()));
//        ed_msg.setText(EditTextUtil.getContent(getActivity(), ed_msg, sb.toString()));
//        ed_msg.setSelection(selection + emoji.length());
//    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_emoji:
                if (ll_expand.getVisibility() == View.VISIBLE) {
                    iv_emoji.setSelected(false);
                    ll_expand.setVisibility(View.GONE);
                } else {//设置列表始终可以滑动
                    listview.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                    ed_msg.requestFocus();
                    iv_emoji.setSelected(true);
                    imm.hideSoftInputFromWindow(ed_msg.getWindowToken(), 0);
                    ll_expand.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.tv_send:
                ChatMessage message = new ChatMessage();
                String time = new Date().getTime() + "";
                message.setTime(time);
                message.setClient_msg_id(Utils.getMD5String(time));
                message.setContent(partStr + content);
                message.setFrom(SettingDefaultsManager.getInstance().UserId());
                message.setChat_type(MessageType.CHAT_GROUP);
                message.setType(MessageType.TXT);
                message.setState(MessageType.UPLOADING);
                message.setUrl("");
                message.setTo(Integer.parseInt(groupId));
                message.setMsg_type(MessageType.CHAT);
                message.setNickname(SettingDefaultsManager.getInstance().NickName());
                message.setAvatar(SettingDefaultsManager.getInstance().UserAvatar());
                message.setRole(role);
                db.addChat(groupId, message);
                messages.add(message);
                listview.setSelection(listview.getBottom());
                sendMessage(message);

                partStr = "";
                ed_msg.setText("");
                tv_return.setVisibility(View.GONE);
                break;
            case R.id.tv_unread:
                listview.setSelection(listview.getBottom());
                break;
            case R.id.tv_reply:

                break;
            case R.id.iv_close:
                ll_tip.setVisibility(View.GONE);
                break;
        }
    }

    private void sendMessage(ChatMessage message) {
        WebBase.sendToRoom(groupId, message.getChat_type(), message.getContent(),
                message.getUrl(), message.getClient_msg_id(), new ChatHandler(getActivity(),message.getClient_msg_id()) {
                    @Override
                    public void onSuccess(JSONObject obj) {
                        JSONObject object = obj.optJSONObject("msg");
                        ChatMessage message = JSONParse.getChatMsgObj(object);
                        updateUi(message, MessageType.UPLOAD_SUCCESS);

                        db.addChatCatalog(addChatCatalog(message));
                    }

                    @Override
                    public void onFailure(String err_msg, String receptId) {
                        ChatMessage message = new ChatMessage();
                        message.setClient_msg_id(receptId);
                        updateUi(message, MessageType.UPLOAD_FAIL);
                        Toast.makeText(getActivity(), err_msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * 举报
     *
     * @param msg_id
     */
    private void complaint(String msg_id) {
        WebBase.complaint(msg_id, MessageType.ROOM_GROUP, new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                Toast.makeText(getContext(), "成功举报", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String err_msg) {
                Toast.makeText(getContext(), err_msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 更是adapter
     *
     * @param message
     */
    private void updateUi(ChatMessage message, int state) {
        db.updateChat(groupId, message);
        for (ChatMessage m : messages) {
            if (message.getClient_msg_id().equalsIgnoreCase(m.getClient_msg_id())) {
                m.setState(state);
                m.setMsg_id(message.getMsg_id());
                messageAdapter.notifyDataSetChanged();
                listview.setSelection(listview.getBottom());
                break;
            }
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (content.length() == 0 && keyCode == event.KEYCODE_DEL) {
            partStr = "";
            tv_return.setVisibility(View.GONE);
        }
        if (keyCode == event.KEYCODE_BACK) {
            if (ll_expand.getVisibility() == View.VISIBLE) {
                ll_expand.setVisibility(View.GONE);
                iv_emoji.setSelected(false);
            } else {
                getActivity().finish();
            }
        }
        return true;
    }

    public class DelMsgReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String group_id = intent.getStringExtra("group_id");//del db
            String msg_id = intent.getStringExtra("msg_id");
            for (ChatMessage message : messages) {
                if (msg_id.equals(message.getMsg_id())) {
                    messages.remove(message);
                    break;
                }
            }
            //delete from db
            messageAdapter.notifyDataSetChanged();
        }
    }


    public class UpdateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //获取service传过来的信息
            ChatMessage message = (ChatMessage) intent.getSerializableExtra("new_chat_msg");
            if (message != null) {
                if (MessageType.ACTION_deleteMsg.equalsIgnoreCase(message.getAction())) {
                    String group_id = message.getGroup_id();//del db
                    String msg_id = message.getMsg_id();
                    for (ChatMessage m : messages) {
                        if (msg_id.equals(m.getMsg_id())) {
                            messages.remove(m);
                            break;
                        }
                    }
                    //delete from db
                    messageAdapter.notifyDataSetChanged();
                } else if (MessageType.ACTION_ban.equalsIgnoreCase(message.getAction())) {
                    ll_tip.setVisibility(View.VISIBLE);
                    tv_reply.setText("您已被禁言");
                } else if (MessageType.ACTION_unBan.equalsIgnoreCase(message.getAction())) {
                    ll_tip.setVisibility(View.VISIBLE);
                    tv_reply.setText("您已被解除禁言");
                } else {
                    messages.add(message);
                    messageAdapter.notifyDataSetChanged();
                    if (is_bottom) {
                        listview.smoothScrollToPosition(listview.getBottom());
                    } else {
                        if (new_live_message == 0) {
                            new_live_message = messageAdapter.getCount() - 1;
                        }
                        int number = messageAdapter.getCount() - new_live_message;
                        tv_unread.setText(String.valueOf(number));
                        if (tv_unread.getVisibility() == View.INVISIBLE) {
                            tv_unread.setVisibility(View.VISIBLE);
                            chat_activity.setlive_number_text(1, number);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            getActivity().unregisterReceiver(receiver);
        }

//        if(mDelMsgReceiver != null){
//            getActivity().unregisterReceiver(mDelMsgReceiver);
//        }
    }
}
