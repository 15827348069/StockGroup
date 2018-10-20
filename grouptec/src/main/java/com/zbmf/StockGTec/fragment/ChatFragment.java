package com.zbmf.StockGTec.fragment;

import android.content.BroadcastReceiver;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
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

import com.zbmf.StockGTec.R;
import com.zbmf.StockGTec.activity.Chat1Activity;
import com.zbmf.StockGTec.adapter.EmojiAdatper;
import com.zbmf.StockGTec.adapter.MessageAdapter;
import com.zbmf.StockGTec.api.ChatHandler;
import com.zbmf.StockGTec.api.JSONHandler;
import com.zbmf.StockGTec.api.WebBase;
import com.zbmf.StockGTec.beans.ChatMessage;
import com.zbmf.StockGTec.db.Database;
import com.zbmf.StockGTec.utils.ChatMessageComparator;
import com.zbmf.StockGTec.utils.DateUtil;
import com.zbmf.StockGTec.utils.DisplayUtil;
import com.zbmf.StockGTec.utils.EditTextUtil;
import com.zbmf.StockGTec.utils.EmojiUtil;
import com.zbmf.StockGTec.utils.JSONparse;
import com.zbmf.StockGTec.utils.MessageType;
import com.zbmf.StockGTec.utils.SettingDefaultsManager;
import com.zbmf.StockGTec.utils.Utils;
import com.zbmf.StockGTec.view.KeyboardLayout;
import com.zbmf.StockGTec.view.PullToRefreshBase;
import com.zbmf.StockGTec.view.PullToRefreshListView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.zbmf.StockGTec.view.KeyboardLayout.KEYBOARD_STATE_HIDE;
import static com.zbmf.StockGTec.view.KeyboardLayout.KEYBOARD_STATE_SHOW;

public class ChatFragment extends Fragment implements View.OnClickListener/*, AdapterView.OnItemClickListener */ {
    private static final String ARG_PARAM1 = "param1";
    private static final String TAG = ChatFragment.class.getSimpleName();

    private String mParam1;
    private KeyboardLayout mKeyboardLayout;
    private EditText ed_msg;
    private TextView tv_send, tv_unread, tv_return;
    //    private ViewPager viewpager;
    private GridView gridview;
    private List<String> emojiNames = null;
    private EmojiAdatper adatper;
    private InputMethodManager imm;
    private LinearLayout ll_expand;
    private PullToRefreshListView psr;
    private ListView listview;
    private List<ChatMessage> messages = new ArrayList<>();

    private MessageAdapter messageAdapter;
    private ChatMessage mMessage = null;
    private ClipboardManager mClipboardManager;
    private ImageView iv_emoji;
    private UpdateReceiver receiver;
    private UpdateReceiver1 receiver1;
    private boolean is_bottom;
    private Database db;
    private String groupId = SettingDefaultsManager.getInstance().getGroupId();//TODO
    private static final int PAGE_SIZE = 20;
    private int KEYBOARD_STATE = KEYBOARD_STATE_HIDE;
    private ViewPager viewPager;
    private List<GridView> gridviews;
    private LinearLayout ll_dot;
    private boolean isFirst = false;
    public static final int GROUP_CHAT = 1;
    public static final int FANS_CHAT = 2;
    private int chatType = GROUP_CHAT;
    private String room = MessageType.ROOM_GROUP;

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

    public static ChatFragment newInstance(int chatType) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putInt("chatType", chatType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            chatType = getArguments().getInt("chatType");
            if (chatType == ChatFragment.FANS_CHAT)
                room = "3";
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

        initData();
        setupView(view);
        fragmentregisterReceiver();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void fragmentregisterReceiver() {
        receiver = new UpdateReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(getContext().getPackageName() + "chat_msg");

        receiver1 = new UpdateReceiver1();
        IntentFilter filter1 = new IntentFilter();
        filter1.addAction(getContext().getPackageName() + "chat_msg1");
        if (chatType != ChatFragment.FANS_CHAT)
            getActivity().registerReceiver(receiver, filter);
        else
            getActivity().registerReceiver(receiver1, filter1);
    }

    private void initData() {
        emojiNames = new ArrayList<>();
    }

    private void setupView(View view) {
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        ll_dot = (LinearLayout) view.findViewById(R.id.ll_dot);
        ll_expand = (LinearLayout) view.findViewById(R.id.ll_expand);
        psr = (PullToRefreshListView) view.findViewById(R.id.listview);
        listview = psr.getRefreshableView();
        mKeyboardLayout = (KeyboardLayout) view.findViewById(R.id.keyboard);
        ed_msg = (EditText) view.findViewById(R.id.ed_msg);
        tv_send = (TextView) view.findViewById(R.id.tv_send);
        tv_unread = (TextView) view.findViewById(R.id.tv_unread);
        tv_return = (TextView) view.findViewById(R.id.tv_return);
        tv_unread.setOnClickListener(this);
        tv_send.setOnClickListener(this);
//        viewpager = (ViewPager)view.findViewById(R.id.viewpager);
        gridview = (GridView) view.findViewById(R.id.gridview);

        int item_spacing = DisplayUtil.dip2px(getActivity(), 8);
        int width = DisplayUtil.getScreenWidthPixels(getActivity());
        int itemWidth = (width - item_spacing * 8) / 7;

//        initGridView(itemWidth);
        EmojiUtil emoji = new EmojiUtil(getActivity());
        ll_expand.addView(emoji.getEmojiView());
        emoji.setOnItemClickListener(new EmojiUtil.OnItemClickListener() {
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

        adatper = new EmojiAdatper(getActivity(), emojiNames, itemWidth);
        gridview.setAdapter(adatper);
        iv_emoji = (ImageView) view.findViewById(R.id.iv_emoji);
        iv_emoji.setOnClickListener(this);
        mKeyboardLayout.setOnkbdStateListener(new KeyboardLayout.onKybdsChangeListener() {
            @Override
            public void onKeyBoardStateChange(int state) {
                switch (state) {
                    case KEYBOARD_STATE_HIDE:

                        break;
                    case KEYBOARD_STATE_SHOW:
//                        ll_expand.setVisibility(View.GONE);
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
                content = s.toString().trim();
                if (content.length() > 0)
                    tv_send.setEnabled(true);
                else
                    tv_send.setEnabled(false);
            }
        });

        messageAdapter = new MessageAdapter(chatType, getActivity(), messages);
        messageAdapter.setListener(new MessageAdapter.OnMenuClickListener() {
            @Override
            public void onMenuClick(View v, int pos) {
                mMessage = messages.get(pos);
                switch (v.getId()) {
                    case R.id.tv_copy:
                        if (mClipboardManager == null)
                            mClipboardManager = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                        mClipboardManager.setText(mMessage.getContent());
                        break;
                    case R.id.tv_reply:
                        partStr = "回复" + mMessage.getNickname() + "：" + mMessage.getContent() + "\n";
                        SpannableString rebackStr = EditTextUtil.getContent(getActivity(), tv_return, partStr);
                        tv_return.setVisibility(View.VISIBLE);
                        tv_return.setText(rebackStr);
                        ed_msg.requestFocus();
                        imm.showSoftInput(ed_msg, InputMethodManager.SHOW_IMPLICIT);
                        break;
                    case R.id.tv_chat://私聊

                        break;
                    case R.id.tv_report://举报
                        complaint(mMessage.getMsg_id());
                        break;
                    case R.id.tv_del:
                        deleteMsg(mMessage.getMsg_id());
                        break;
                    case R.id.tv_gag://禁言
                        ban(mMessage.getFrom());
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
                if (messages != null && messages.size() > 0) {
                    lastTime = messages.get(0).getTime();
                    getRoomMsg();
                } else
                    psr.onRefreshComplete();

            }
        });

        listview.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
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
                        ((Chat1Activity) getActivity()).setLiveNumbeGone(1);
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
                        ((Chat1Activity) getActivity()).setlive_number_text(1, no_read);
                    }
                }

            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }
        });
        listview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
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

        if (!isFirst)
            getRoomMsg();
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
        WebBase.getRoomMsg(chatType, groupId, lastTime, new JSONHandler(getActivity()) {
            @Override
            public void onSuccess(JSONObject obj) {

                isFirst = true;
                psr.onRefreshComplete();
                ChatMessage chatMessage = JSONparse.getRoomMsg(obj);
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
                    listview.setTranscriptMode(ListView.TRANSCRIPT_MODE_DISABLED);
                    messages.addAll(0, tempMessages);
                    messageAdapter.notifyDataSetChanged();
                    listview.setSelection(tempMessages.size());

                }
            }

            @Override
            public void onFailure(String err_msg) {
                psr.onRefreshComplete();
            }
        });
    }

    private String content = "";
    private String partStr = "";

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
                message.setChat_type(MessageType.CHAT_GROUP);
                message.setType(MessageType.TXT);
                message.setState(MessageType.UPLOADING);
                message.setUrl("");

                message.setFrom(SettingDefaultsManager.getInstance().UserId());
                message.setNickname(SettingDefaultsManager.getInstance().NickName());
                message.setAvatar(SettingDefaultsManager.getInstance().UserAvatar());

                if (SettingDefaultsManager.getInstance().isGroupChatManager() && SettingDefaultsManager.getInstance().isGroupManager()) {
                    message.setFrom(SettingDefaultsManager.getInstance().getGroupId());
                    message.setNickname(SettingDefaultsManager.getInstance().getGroupName());
                    message.setAvatar(SettingDefaultsManager.getInstance().getGroupImg());
                }

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
        }
    }


    private void sendMessage(ChatMessage message) {
        String creator = "0";
        if (SettingDefaultsManager.getInstance().isGroupChatManager() && SettingDefaultsManager.getInstance().isGroupManager()) {
            creator = "1";
        }
        WebBase.sendToRoom(chatType, groupId, message.getType(), message.getContent(),
                message.getUrl(), message.getClient_msg_id(), creator, new ChatHandler(getActivity(), message.getClient_msg_id()) {
                    @Override
                    public void onSuccess(JSONObject obj) {
                        JSONObject object = obj.optJSONObject("msg");
                        ChatMessage message = JSONparse.getChatMsgObj(object);
                        updateUi(message, MessageType.UPLOAD_SUCCESS);
                    }

                    @Override
                    public void onFailure(String err_msg, String receptId) {
                        ChatMessage message = new ChatMessage();
                        message.setClient_msg_id(receptId);
                        updateUi(message, MessageType.UPLOAD_FAIL);
                    }
                });
    }

    /**
     * 举报
     *
     * @param msg_id
     */
    private void complaint(String msg_id) {
        WebBase.complaint(msg_id, MessageType.ROOM_GROUP, new JSONHandler(getActivity()) {
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
     * 禁言
     *
     * @param user_id
     */
    private void ban(String user_id) {
        WebBase.ban(groupId, user_id, new JSONHandler(getActivity()) {
            @Override
            public void onSuccess(JSONObject obj) {
                Toast.makeText(getContext(), "此人已被禁言", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String err_msg) {
                Toast.makeText(getContext(), err_msg, Toast.LENGTH_SHORT).show();
            }
        });
    }


    /**
     * 删除消息
     *
     * @param msg_id
     */
    private void deleteMsg(final String msg_id) {
        WebBase.deleteMsg(groupId, msg_id, room, new JSONHandler(getActivity()) {
            @Override
            public void onSuccess(JSONObject obj) {
                Toast.makeText(getContext(), "删除成功", Toast.LENGTH_SHORT).show();
                for (ChatMessage m : messages) {
                    if (msg_id.equals(m.getMsg_id())) {
                        messages.remove(m);
                        break;
                    }
                }

                messageAdapter.notifyDataSetChanged();

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

    public class UpdateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //获取service传过来的信息
            ChatMessage message = (ChatMessage) intent.getSerializableExtra("new_chat_msg");
            messages.add(message);
            messageAdapter.notifyDataSetChanged();
            if (is_bottom) {
                listview.smoothScrollToPosition(messageAdapter.getCount());

            } else {
                if (new_live_message == 0) {
                    new_live_message = messageAdapter.getCount() - 1;
                }
                int number = messageAdapter.getCount() - new_live_message;
                tv_unread.setText(String.valueOf(number));
                if (tv_unread.getVisibility() == View.INVISIBLE) {
                    tv_unread.setVisibility(View.VISIBLE);
                    ((Chat1Activity) getActivity()).setlive_number_text(1, number);
                }
            }

        }
    }

    public class UpdateReceiver1 extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //获取service传过来的信息
            ChatMessage message = (ChatMessage) intent.getSerializableExtra("new_chat_msg");
            messages.add(message);
            messageAdapter.notifyDataSetChanged();
            if (is_bottom) {
                listview.smoothScrollToPosition(messageAdapter.getCount());

            } else {
                if (new_live_message == 0) {
                    new_live_message = messageAdapter.getCount() - 1;
                }
                int number = messageAdapter.getCount() - new_live_message;
                tv_unread.setText(String.valueOf(number));
                if (tv_unread.getVisibility() == View.INVISIBLE) {
                    tv_unread.setVisibility(View.VISIBLE);
                    ((Chat1Activity) getActivity()).setlive_number_text(1, number);
                }
            }

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (receiver != null)
                getActivity().unregisterReceiver(receiver);
            if (receiver1 != null)
                getActivity().unregisterReceiver(receiver1);
        } catch (IllegalArgumentException e) {
            //you have died
        }
    }
}
