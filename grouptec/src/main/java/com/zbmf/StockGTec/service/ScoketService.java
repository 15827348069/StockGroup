package com.zbmf.StockGTec.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.zbmf.StockGTec.R;
import com.zbmf.StockGTec.activity.Chat1Activity;
import com.zbmf.StockGTec.api.AppUrl;
import com.zbmf.StockGTec.beans.ChatMessage;
import com.zbmf.StockGTec.beans.LiveMessage;
import com.zbmf.StockGTec.db.Database;
import com.zbmf.StockGTec.utils.MessageType;
import com.zbmf.StockGTec.utils.SettingDefaultsManager;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;


public class ScoketService extends Service {
    private final IBinder binder = new LocalBinder();
    private WebSocketClient client;
    private String TAG = "WebSocketClient.class";
    private SoundPool pool;
    private boolean client_type;
    private int live_sourceid, chat_sourceid;

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
        client_connect();
    }

    public void client_connect() {
        try {
            client = new WebSocketClient(new URI(AppUrl.URL_CHAT_LOCATION), new Draft_17()) {
                @Override
                public void onOpen(ServerHandshake serverHandshake) {
                    Log.e(TAG, "已经链接...");
                    client_type = true;
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
                                        sendLiveMsgtoActivty(GetLiveMessage.getMessage(obj));
                                        break;
                                    case 2:
                                        //小组群聊
                                        ChatMessage cm = new ChatMessage();
                                        cm.setAvatar(obj.optString("avatar"));
                                        cm.setChat_type(obj.optString("chat_type"));
                                        cm.setMsg_id(obj.optString("msg_id"));
                                        cm.setRoom(obj.optInt("role"));
                                        cm.setFrom(obj.optString("from"));
                                        cm.setRole(obj.optInt("role"));
                                        cm.setNickname(obj.optString("nickname"));
                                        cm.setTo(obj.optInt("to"));
                                        cm.setType(obj.optJSONObject("msg").optString("type"));
                                        cm.setContent(obj.optJSONObject("msg").optString("content"));
                                        cm.setTime(obj.optString("time"));
                                        new Database(getBaseContext()).addChat("" + cm.getTo(), cm);
                                        sendMsgtoChatActivty(cm,2);
                                        break;
                                    case 3:
                                        //小组群聊
                                        ChatMessage cm1 = new ChatMessage();
                                        cm1.setAvatar(obj.optString("avatar"));
                                        cm1.setChat_type(obj.optString("chat_type"));
                                        cm1.setMsg_id(obj.optString("msg_id"));
                                        cm1.setRoom(obj.optInt("role"));
                                        cm1.setFrom(obj.optString("from"));
                                        cm1.setRole(obj.optInt("role"));
                                        cm1.setNickname(obj.optString("nickname"));
                                        cm1.setTo(obj.optInt("to"));
                                        cm1.setType(obj.optJSONObject("msg").optString("type"));
                                        cm1.setContent(obj.optJSONObject("msg").optString("content"));
                                        cm1.setTime(obj.optString("time"));
//                                        new Database(getBaseContext()).addChat("" + cm1.getTo(), cm);
                                        sendMsgtoChatActivty(cm1,3);
                                        break;
                                }
                                break;
                            case MessageType.GIFT:
                                //礼物
                                sendLiveMsgtoActivty(GetLiveMessage.getMessage(obj));
                                break;
                            case MessageType.FANS:
                                sendLiveMsgtoActivty(GetLiveMessage.getMessage(obj));
                                break;
                            case MessageType.SYSTEM:
                                sendLiveMsgtoActivty(GetLiveMessage.getMessage(obj));
                                break;
                            case MessageType.BOX:
                                sendLiveMsgtoActivty(GetLiveMessage.getMessage(obj));
                                break;
                            case MessageType.BLOG:
                                sendLiveMsgtoActivty(GetLiveMessage.getMessage(obj));
                                break;
                            case MessageType.MEMBER:
                                sendLiveMsgtoActivty(GetLiveMessage.getMessage(obj));
                                break;
                            case MessageType.RED_PACKET:
                                sendLiveMsgtoActivty(GetLiveMessage.getMessage(obj));
                                break;
                            case "init":
                                sendclientMsgtoActivty(obj.optString("socket_id"));
                                break;
                            case "notice":
                                String group_id = obj.optString("to");
                                String countent = obj.optJSONObject("msg").optString("content");
                                sendNoticeMsgtoActivty(group_id, countent);
                                break;
                            case "ask":
                                JSONObject askObj = obj.getJSONObject("msg").getJSONObject("ask");
                                int notify_id = askObj.optInt("ask_id");
                                showAnsterNoticeLayout(getBaseContext(), notify_id);
                                sendAskMsgtoActivty();
                                break;
                            case "answer":
//                                showAnsterNoticeLayout(getBaseContext(), GetLiveMessage.getMessage(obj));
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
                }
            };
            client.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static void showAnsterNoticeLayout(Context context, int notify_id) {
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        Intent intent = new Intent(context, Chat1Activity.class);
        Bundle bundle = new Bundle();
        bundle.putString("ask", "ask");
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentTitle(context.getString(R.string.app_name))//设置通知栏标题
                .setContentText("您有新的问题未回复") //设置通知栏显示内容
                .setContentIntent(pendingIntent) //设置通知栏点击意图
                //  .setNumber(number) //设置通知集合的数量
                .setTicker("您有新问题") //通知首次出现在通知栏，带上升动画效果的
                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                .setPriority(Notification.PRIORITY_HIGH) //设置该通知优先级
                .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
                .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                .setDefaults(Notification.DEFAULT_VIBRATE)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
                //Notification.DEFA ULT_ALL  Notification.DEFAULT_SOUND 添加声音 // requires VIBRATE permission
                .setSmallIcon(R.drawable.ic_launcher);//设置通知小ICON
        mNotificationManager.notify(notify_id, mBuilder.build());
    }

    private void sendNoticeMsgtoActivty(String group_id, String message) {
        Intent intent = new Intent(getPackageName() + "notice_id");
        intent.putExtra("group_id", group_id);
        intent.putExtra("message", message);
        this.sendBroadcast(intent);
    }

    private void sendclientMsgtoActivty(String client_id) {
        Intent intent = new Intent(getPackageName() + "client_id");
        intent.putExtra("client_id", client_id);
        this.sendBroadcast(intent);
    }

    private void sendAskMsgtoActivty() {
        Intent intent = new Intent(getPackageName() + "client_ids");
        this.sendBroadcast(intent);
    }

    /**
     * 把信息传递给直播activity
     *
     * @param msg
     */
    private void sendLiveMsgtoActivty(LiveMessage msg) {
        Intent intent = new Intent(getPackageName() + "new_live_msg");
        intent.putExtra("new_live_msg", msg);
        this.sendBroadcast(intent);
        if (SettingDefaultsManager.getInstance().getNewMessageVedio(msg.getGroup_id())) {
            new_live_message_vedio();
        }
    }

    /**
     * 把信息传递给直播activity
     *
     * @param msg
     */
    private void sendgiftMsgtoActivty(LiveMessage msg) {
        Intent intent = new Intent(getPackageName() + "new_live_msg");
        intent.putExtra("new_live_msg", msg);
        this.sendBroadcast(intent);
    }

    /**
     * 把信息传递给群聊activity
     *
     * @param msg
     */
    private void sendMsgtoChatActivty(ChatMessage msg,int room) {
        if (SettingDefaultsManager.getInstance().getCurrentChat().equals(msg.getTo() + "")) {
            String action = "chat_msg";
            if(room == 3)
                action = "chat_msg1";
            Intent intent = new Intent(getPackageName() + action);
            intent.putExtra("new_chat_msg", msg);
            this.sendBroadcast(intent);
        }
        //设置关闭 并且不是自己发送的新消息  不提示
        if (SettingDefaultsManager.getInstance().getNewChatMessageVedio(msg.getTo() + "") &&
                !SettingDefaultsManager.getInstance().UserId().equals(msg.getFrom())) {
            new_chat_message_vedio();
        }
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
}
