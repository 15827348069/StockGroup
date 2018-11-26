package com.zbmf.StockGroup.fragment.chat;

import android.content.BroadcastReceiver;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.activity.BindPhoneActivity;
import com.zbmf.StockGroup.activity.Chat1Activity;
import com.zbmf.StockGroup.adapter.MessageAdapter;
import com.zbmf.StockGroup.api.ChatHandler;
import com.zbmf.StockGroup.api.JSONHandler;
import com.zbmf.StockGroup.api.WebBase;
import com.zbmf.StockGroup.beans.ChatCatalog;
import com.zbmf.StockGroup.beans.ChatMessage;
import com.zbmf.StockGroup.beans.Group;
import com.zbmf.StockGroup.constans.IntentKey;
import com.zbmf.StockGroup.constans.RequestCode;
import com.zbmf.StockGroup.db.Database;
import com.zbmf.StockGroup.dialog.EditTextDialog;
import com.zbmf.StockGroup.dialog.TextDialog;
import com.zbmf.StockGroup.fragment.chat.observe.EventCar;
import com.zbmf.StockGroup.fragment.chat.observe.Observer;
import com.zbmf.StockGroup.interfaces.DialogYesClick;
import com.zbmf.StockGroup.utils.ChatMessageComparator;
import com.zbmf.StockGroup.utils.DateUtil;
import com.zbmf.StockGroup.utils.EditTextUtil;
import com.zbmf.StockGroup.utils.JSONParse;
import com.zbmf.StockGroup.utils.MessageType;
import com.zbmf.StockGroup.utils.SettingDefaultsManager;
import com.zbmf.StockGroup.utils.Utils;
import com.zbmf.StockGroup.view.PullToRefreshBase;
import com.zbmf.StockGroup.view.PullToRefreshListView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static android.view.View.VISIBLE;
import static com.zbmf.StockGroup.utils.ShowActivity.showActivityForResult;

public class ChatFragment extends Fragment implements View.OnClickListener/*, AdapterView.OnItemClickListener */
        ,Observer {
    private static final String ARG_PARAM1 = "group";
    private TextView ed_msg;
    private TextView tv_unread, tv_return, tv_reply;
    private LinearLayout ll_tip;
    private PullToRefreshListView psr;
    private ListView listview;
    private List<ChatMessage> messages = new ArrayList<>();
    private ImageView iv_close;

    private MessageAdapter messageAdapter;
    private ChatMessage mMessage = null;
    private ClipboardManager mClipboardManager;
    private UpdateReceiver receiver;

    private boolean is_bottom;
    private Database db;
    private String groupId;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        chat_activity = (Chat1Activity) getActivity();
        EventCar.getDefault().register(this);
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
        ll_tip = (LinearLayout) view.findViewById(R.id.ll_tip);
        psr = (PullToRefreshListView) view.findViewById(R.id.listview);
        listview = psr.getRefreshableView();
        ed_msg = (TextView) view.findViewById(R.id.ed_msg);
        tv_unread = (TextView) view.findViewById(R.id.tv_unread);
        tv_return = (TextView) view.findViewById(R.id.tv_return);
        tv_reply = (TextView) view.findViewById(R.id.tv_reply);
        iv_close = (ImageView) view.findViewById(R.id.iv_close);
        iv_close.setOnClickListener(this);
        tv_unread.setOnClickListener(this);
        tv_reply.setOnClickListener(this);
        view.findViewById(R.id.send_layout).setOnClickListener(this);
        view.findViewById(R.id.iv_emoji).setOnClickListener(this);
        messageAdapter = new MessageAdapter(getActivity(), messages,true);
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
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }
        });

        db = new Database(getActivity());
        getRoomMsg();

        Intent intent3 = new Intent();
        intent3.setAction("com.zbmf.StockGroup.UNREADNUM");
        getActivity().sendBroadcast(intent3);

        if(editdialog1==null){
            editdialog1=Editdialog1();
            editdialog1.show();
            editdialog1.dismiss();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        db.updateUnReadNum(groupId, 0);
    }
    private EditTextDialog editdialog1;
    private EditTextDialog Editdialog1(){
        return EditTextDialog
                .createDialog(getActivity(), R.style.myDialogTheme)
                .setRightButton(getString(R.string.send_button))
                .setEditHint("")
                .setEmailVisibility(VISIBLE)
                .setDiss(new EditTextDialog.OnDiss() {
                    @Override
                    public void onDiss(SpannableString content) {
                        ed_msg.setText(content);
                    }
                })
                .setSendClick(new EditTextDialog.OnSendClick() {
                    @Override
                    public void onSend(String content_message,int type) {
                        ChatMessage message = new ChatMessage();
                        String time = new Date().getTime() + "";
                        message.setTime(time);
                        message.setClient_msg_id(Utils.getMD5String(time));
                        message.setContent(partStr + content_message);
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
                        tv_return.setVisibility(View.GONE);
                    }
                });
    }
    private int new_live_message = 10;
    private String lastTime = DateUtil.getTimes() + "";

    private void getRoomMsg() {
        WebBase.getRoomMsg(groupId, lastTime, new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                psr.onRefreshComplete();
                ChatMessage chatMessage = JSONParse.getRoomMsg(obj);
                if (chatMessage != null && chatMessage.getMessages() != null) {
                    List<ChatMessage> tempMessages = chatMessage.getMessages();
                    Collections.sort(tempMessages, new ChatMessageComparator());
                    for (int i = 0; i < tempMessages.size(); i++) {
                        ChatMessage message = tempMessages.get(i);
                        db.addChat(groupId, message);
                    }
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


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_unread:
                listview.setSelection(listview.getBottom());
                break;
            case R.id.tv_reply:

                break;
            case R.id.iv_close:
                ll_tip.setVisibility(View.GONE);
                break;
            case R.id.iv_emoji:
                if(TextUtils.isEmpty(SettingDefaultsManager.getInstance().getUserPhone())){
                    TextDialog.createDialog(getActivity())
                            .setTitle("")
                            .setMessage("为响应国家政策，请于操作前绑定手机信息！")
                            .setLeftButton("下次再说")
                            .setRightButton("前往绑定")
                            .setRightClick(new DialogYesClick() {
                                @Override
                                public void onYseClick() {
                                    Bundle bundle=new Bundle();
                                    bundle.putInt(IntentKey.FLAG,1);
                                    showActivityForResult(getActivity(),bundle, BindPhoneActivity.class,RequestCode.BINDED);
                                }
                            })
                            .show();
                }else{
                    if(editdialog1==null){
                        editdialog1=Editdialog1();
                    }
                    editdialog1.showEmail();
                }
                break;
            case R.id.send_layout:
                if(TextUtils.isEmpty(SettingDefaultsManager.getInstance().getUserPhone())){
                    TextDialog.createDialog(getActivity())
                            .setTitle("")
                            .setMessage("为响应国家政策，请于操作前绑定手机信息！")
                            .setLeftButton("下次再说")
                            .setRightButton("前往绑定")
                            .setRightClick(new DialogYesClick() {
                                @Override
                                public void onYseClick() {
                                    Bundle bundle=new Bundle();
                                    bundle.putInt(IntentKey.FLAG,1);
                                    showActivityForResult(getActivity(),bundle, BindPhoneActivity.class,RequestCode.BINDED);
                                }
                            })
                            .show();
                }else{
                    if(editdialog1==null){
                        editdialog1=Editdialog1();
                    }
                    editdialog1.show();
                }
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
                        if(editdialog1.isShowing()){
                            editdialog1.dismiss();
                        }
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
        WebBase.complaint(msg_id, MessageType.ROOM_GROUP+"", new JSONHandler() {
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
            if (editdialog1!=null&&editdialog1.isShowing()) {
                editdialog1.dismiss();
            } else {
                getActivity().finish();
            }
        }
        return true;
    }

    @Override
    public void updata(Object object) {
      //不知道在哪里改

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
                        if(message.getUser_id().equals(SettingDefaultsManager.getInstance().UserId())){
                            ll_tip.setVisibility(View.VISIBLE);
                            tv_reply.setText("您已被禁言");
                        }else{
//                            tv_reply.setText(message.getNickname()+"已被禁言");
                        }
                } else if (MessageType.ACTION_unBan.equalsIgnoreCase(message.getAction())) {
                        if(message.getUser_id().equals(SettingDefaultsManager.getInstance().UserId())){
                            ll_tip.setVisibility(View.VISIBLE);
                            tv_reply.setText("您已被解除禁言");
                        }else{
//                            tv_reply.setText(message.getNickname()+"已被解除禁言");
                        }
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
