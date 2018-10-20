package com.zbmf.groupro.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.zbmf.groupro.R;
import com.zbmf.groupro.activity.Chat1Activity;
import com.zbmf.groupro.api.AppUrl;
import com.zbmf.groupro.beans.ChatCatalog;
import com.zbmf.groupro.beans.ChatMessage;
import com.zbmf.groupro.beans.Group;
import com.zbmf.groupro.beans.LiveMessage;
import com.zbmf.groupro.db.DBManager;
import com.zbmf.groupro.db.Database;
import com.zbmf.groupro.utils.ActivityUtil;
import com.zbmf.groupro.utils.Constants;
import com.zbmf.groupro.utils.JSONParse;
import com.zbmf.groupro.utils.MessageType;
import com.zbmf.groupro.utils.NotificationUtil;
import com.zbmf.groupro.utils.SettingDefaultsManager;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by xuhao on 2016/12/15.
 */

public class ScoketService extends Service {
    private final IBinder binder = new LocalBinder();
    private WebSocketClient client;
    private String TAG = "WebSocketClient.class";
    private SoundPool pool;
    private boolean client_type;
    private int live_sourceid, chat_sourceid;
    private DBManager dbManager;
    private Database db;
    private boolean network_message;
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    private Handler handler = new Handler();

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (client != null && !client_type) {
                client = null;
                client_connect();
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        // 指定声音池的最大音频流数目为10，声音品质为5
        pool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
        // 载入音频流，返回在池中的id
        live_sourceid = pool.load(this, R.raw.sms, 0);
        chat_sourceid = pool.load(this, R.raw.chat, 0);
        dbManager = new DBManager(this);
        db = new Database(this);
        client_connect();
    }

    public void client_connect() {
        try {
            client = new WebSocketClient(new URI(AppUrl.URL_CHAT_LOCATION), new Draft_17()) {
                @Override
                public void onOpen(ServerHandshake serverHandshake) {
                    Log.e(TAG, "已经链接...");
                    client_type = true;
                    network_message=false;
                    handler.removeCallbacks(runnable);
                }

                @Override
                public void onMessage(String s) {
                    client_type = true;
                    try {
                        JSONObject obj = new JSONObject(s);
                        if (!obj.optString("msg_type").equals("ping")) {
                            Log.e(TAG, "收到消息..." + s);
                        }
                        switch (obj.optString("msg_type")) {
                            case "chat":
                                switch (obj.optInt("room")) {
                                    case 1:
                                        //直播室
                                        sendLiveMsgtoActivty(obj);
                                        break;
                                    case 2:
                                        //小组群聊
                                        parseJsonAndSendMsg(obj);
                                        break;
                                }
                                break;
                            case MessageType.GIFT:
                                //礼物
                                sendLiveMsgtoActivty(obj);
                                break;
                            case MessageType.FANS:
                                sendLiveMsgtoActivty(obj);
                                break;
                            case MessageType.SYSTEM:
                                sendLiveMsgtoActivty(obj);
                                break;
                            case MessageType.BOX:
                                sendLiveMsgtoActivty(obj);
                                break;
                            case MessageType.RED_PACKET:
                                sendLiveMsgtoActivty(obj);
                                break;
                            case MessageType.ANSWER:
                                sendLiveMsgtoActivty(obj);
                                break;
                            case "init":
                                sendclientMsgtoActivty(obj.optString("socket_id"));
                                break;
                            case "notice":
                                String group_id = obj.optString("to");
                                String countent = obj.optJSONObject("msg").optString("content");
                                sendNoticeMsgtoActivty(group_id, countent);
                                break;
                            case MessageType.CMD:
                                parseJsonAndSendMsg(obj);
                                break;

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onClose(int i, String s, boolean b) {
                    Log.e(TAG, "链接关闭..." + s);
                    if (client != null) {
                        client_type = false;
                        handler.postDelayed(runnable, 3000);
                    }
                }

                @Override
                public void onError(Exception e) {
                    Log.e(TAG, "异常..." + e.getMessage());
                    if(!network_message){
                        Intent intent = new Intent(Constants.NETWORK_IS_UNREACHABLE);
                        sendBroadcast(intent);
                        network_message=true;
                    }
                }
            };
            client.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void parseJsonAndSendMsg(JSONObject obj) {
        ChatMessage chatMessage = JSONParse.getChatMsgObj(obj);
        new Database(getBaseContext()).addChat("" + chatMessage.getTo(), chatMessage);
        sendMsgtoChatActivty(chatMessage);
    }

    private void sendNoticeMsgtoActivty(String group_id, String message) {
        Intent intent = new Intent("com.zbmf.StockGroup.notice_id");
        intent.putExtra("group_id", group_id);
        intent.putExtra("message", message);
        this.sendBroadcast(intent);
    }

    private void sendclientMsgtoActivty(String client_id) {
        if (client_type) {
            Intent intent = new Intent("com.zbmf.StockGroup.client_id");
            intent.putExtra("client_id", client_id);
            this.sendBroadcast(intent);
        }
        client_type = false;
    }

    /**
     * 把信息传递给直播activity
     *
     * @param
     */
    private void sendLiveMsgtoActivty(JSONObject object) {
        LiveMessage msg=GetLiveMessage.getMessage(object,false);
        Intent intent = new Intent(Constants.NEW_LIVE_MSG);
        intent.putExtra("new_live_msg", msg);
        this.sendBroadcast(intent);
        dbManager.add(msg);
        if (SettingDefaultsManager.getInstance().getNewMessageVedio(msg.getGroup_id())
                && !msg.getMessage_type().equals(MessageType.GIFT)
                && !msg.getMessage_type().equals(MessageType.SYSTEM)) {
            if (SettingDefaultsManager.getInstance().getMessageAll())
                new_live_message_vedio();
            showNoticeLayout(msg);
        }
        if(msg.getQuestion_id().equals(SettingDefaultsManager.getInstance().UserId())){
            Intent answer_intent=new Intent(Constants.USER_NEW_MESSAGE);
            answer_intent.putExtra("type",MessageType.ANSWER);
            this.sendBroadcast(answer_intent);
            NotificationUtil.showAnsterNoticeLayout(getBaseContext(),msg);
        }
    }

    /**
     * 把信息传递给直播activity
     *
     * @param msg
     */
    private void sendgiftMsgtoActivty(LiveMessage msg) {
        Intent intent = new Intent("com.zbmf.StockGroup.new_live_msg");
        intent.putExtra("new_live_msg", msg);
        this.sendBroadcast(intent);
    }

    /**
     * 把信息传递给群聊activity
     *
     * @param msg
     */
    private void sendMsgtoChatActivty(ChatMessage msg) {
        if (SettingDefaultsManager.getInstance().getCurrentChat().equals(msg.getTo()+"")) {
            Intent intent = new Intent(Constants.CHAT_MSG);
            intent.putExtra("new_chat_msg", msg);
            this.sendBroadcast(intent);
        }

        //设置关闭 并且不是自己发送的新消息  不提示

        if (SettingDefaultsManager.getInstance().getNewChatMessageVedio(msg.getTo() + "") &&
                !SettingDefaultsManager.getInstance().UserId().equals(msg.getFrom())
                ) {
            new_chat_message_vedio();
//            showChatNotice(msg);

        }

        int unreadnum = 0;
        if (!SettingDefaultsManager.getInstance().getCurrentChat().equals(msg.getTo()+"")) {
            unreadnum = db.getChatUnReadNum(msg.getTo()+"") + 1;
            Log.e(TAG, "sendMsgtoChatActivty: ----------------------" );
        }



        ChatCatalog catalog = new ChatCatalog();
        catalog.setGroup_id(msg.getTo()+"");
        catalog.setUnreadnum(unreadnum);
        catalog.setType(0);
        catalog.setMsg_id(msg.getMsg_id());
        catalog.setTime(Long.parseLong(msg.getTime()));
        db.addChatCatalog(catalog);

        Intent intent3 = new Intent();
        intent3.setAction(Constants.UNREADNUM);
        this.sendBroadcast(intent3);
    }

    /**
     * activity 调用发送消息
     *
     * @param msg
     */
    private void sendMsgtoServer(ChatMessage msg) {
        if (client != null && client.isOpen()) {

        }
    }

    public class LocalBinder extends Binder {
        public ScoketService getService() {
            return ScoketService.this;
        }

        public void sendMsgToServer(ChatMessage msg) {
            sendMsgtoServer(msg);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (client != null && client.isOpen()) {
            client.close();
            client = null;
        }
    }

    public void new_live_message_vedio() {
        float volume = 0;
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        float max = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING);
        float current = audioManager.getStreamVolume(AudioManager.STREAM_RING);
        volume = current / max;
        // 播放音频，第二个参数为左声道音量;第三个参数为右声道音量;第四个参数为优先级；第五个参数为循环次数，0不循环，-1循环; 第六个参数为速率，速率    最低0.5最高为2，1代表正常速度
        pool.play(live_sourceid, volume, volume, 0, 0, 1);
    }

    public void new_chat_message_vedio() {
        float volume = 0;
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        float max = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING);
        float current = audioManager.getStreamVolume(AudioManager.STREAM_RING);
        volume = current / max;
        // 播放音频，第二个参数为左声道音量;第三个参数为右声道音量;第四个参数为优先级；第五个参数为循环次数，0不循环，-1循环; 第六个参数为速率，速率    最低0.5最高为2，1代表正常速度
        pool.play(chat_sourceid, volume, volume, 0, 0, 1);
    }

    public void showNoticeLayout(LiveMessage message) {
        Chat1Activity chat1Activity = (Chat1Activity) ActivityUtil.getInstance().getActivity(ActivityUtil.CHATACTIVITY);
        if (ActivityUtil.getInstance().isTopActivy(ActivityUtil.CHATACTIVITY, getBaseContext()) || chat1Activity != null && !chat1Activity.GROUP_ID.equals(message.getGroup_id())) {
            return;
        }
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle(getString(R.string.app_name))//设置通知栏标题
                .setContentText("来自" + message.getMessage_name() + "直播室的新消息！") //设置通知栏显示内容
                .setContentIntent(getDefalutIntent(Notification.FLAG_AUTO_CANCEL, String.valueOf(message.getGroup_id()), 0)) //设置通知栏点击意图
                //  .setNumber(number) //设置通知集合的数量
                .setTicker("来自" + message.getMessage_name() + "直播室的新消息！") //通知首次出现在通知栏，带上升动画效果的
                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                .setPriority(Notification.PRIORITY_HIGH) //设置该通知优先级
                .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
                .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
//                .setDefaults(Notification.DEFAULT_VIBRATE)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
                //Notification.DEFAULT_ALL  Notification.DEFAULT_SOUND 添加声音 // requires VIBRATE permission
                .setSmallIcon(R.drawable.ic_launcher);//设置通知小ICON
        mNotificationManager.notify(Integer.valueOf("100000" + message.getGroup_id()), mBuilder.build());
    }

    public void showChatNotice(ChatMessage msg) {
        Chat1Activity chat1Activity = (Chat1Activity) ActivityUtil.getInstance().getActivity(ActivityUtil.CHATACTIVITY);
        if (ActivityUtil.getInstance().isTopActivy(ActivityUtil.CHATACTIVITY, getBaseContext()) || chat1Activity != null && chat1Activity.GROUP_ID.equals(msg.getTo())) {
            return;
        }
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle("来自" + msg.getTo() + "群聊的新消息！")//设置通知栏标题
                .setContentText(msg.getNickname() + ":" + msg.getContent()) //设置通知栏显示内容
                .setContentIntent(getDefalutIntent(Notification.FLAG_AUTO_CANCEL, String.valueOf(msg.getTo()), 1)) //设置通知栏点击意图
                //  .setNumber(number) //设置通知集合的数量
                .setTicker("来自" + msg.getTo() + "群聊的新消息！") //通知首次出现在通知栏，带上升动画效果的
                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                .setPriority(Notification.PRIORITY_HIGH) //设置该通知优先级
                .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
                .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                .setDefaults(Notification.DEFAULT_VIBRATE)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
                //Notification.DEFA ULT_ALL  Notification.DEFAULT_SOUND 添加声音 // requires VIBRATE permission
                .setSmallIcon(R.drawable.ic_launcher);//设置通知小ICON
        mNotificationManager.notify(Integer.valueOf("200000" + msg.getTo()), mBuilder.build());
    }

    public PendingIntent getDefalutIntent(int flags, String group_id, int select) {
        Intent intent = new Intent(getBaseContext(), Chat1Activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Group group=new Group();
        group.setId(group_id);
        intent.putExtra("GROUP", group);
        intent.putExtra("live_or_chat", select);
        PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), 0, intent, flags);
        return pendingIntent;
    }
}
