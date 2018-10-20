package com.zbmf.StockGTec;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.igexin.sdk.PushManager;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zbmf.StockGTec.api.JSONHandler;
import com.zbmf.StockGTec.api.WebBase;
import com.zbmf.StockGTec.beans.Vers;
import com.zbmf.StockGTec.service.DemoIntentService;
import com.zbmf.StockGTec.service.DemoPushService;
import com.zbmf.StockGTec.service.ScoketService;
import com.zbmf.StockGTec.utils.Constants;
import com.zbmf.StockGTec.utils.DisplayUtil;
import com.zbmf.StockGTec.utils.ServiceUtil;
import com.zbmf.StockGTec.utils.SettingDefaultsManager;
import com.zbmf.StockGTec.view.RoundedCornerImageView;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView message_text;
    private RoundedCornerImageView avatar_id;
    private ScoketService.LocalBinder mBinder = null;
    private UpdateReceiver receiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initview();
        setLiveImageW();
    }
    public void setLiveImageW(){
        int width= DisplayUtil.getScreenWidthPixels(this);
        if(width>=1000){
            SettingDefaultsManager.getInstance().setLiveImg(Constants.LIVE_IMG_500);
        }else if(width>=600&&width<1000){
            SettingDefaultsManager.getInstance().setLiveImg(Constants.LIVE_IMG_350);
        }else if(width>=320&&width<700){
            SettingDefaultsManager.getInstance().setLiveImg(Constants.LIVE_IMG_200);
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        PushManager.getInstance().initialize(this.getApplicationContext(), DemoPushService.class);
        PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), DemoIntentService.class);
        //初始化时启动服务
        if(!ServiceUtil.isServiceRunning(this,ScoketService.class.getName())){
            Intent intent = new Intent(this, ScoketService.class);
            this.bindService(intent, conn, BIND_AUTO_CREATE);
        }
        receiver= new UpdateReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(getPackageName()+"client_id");
        registerReceiver(receiver, filter);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //结束服务
        if(conn!=null){
            unbindService(conn);
        }
        if(receiver!=null){
            unregisterReceiver(receiver);
        }
    }
    private void initview() {
        findViewById(R.id.chat_button).setOnClickListener(this);
        findViewById(R.id.statistics_button).setOnClickListener(this);
        message_text= (TextView) findViewById(R.id.message_text);
        avatar_id= (RoundedCornerImageView) findViewById(R.id.avatar_id);
        WebBase.vers(new JSONHandler(MainActivity.this) {
            @Override
            public void onSuccess(JSONObject obj) {
                Vers vers = new Vers();
                vers.setUpdated_at(obj.optString("updated_at"));
                vers.setIntro(obj.optString("intro"));
                vers.setSubject(obj.optString("subject"));
                vers.setVersion(obj.optString("version"));
                JSONArray array = obj.optJSONArray("logics");

                Vers.Logics logics = new Vers.Logics();
                logics.intro = array.optJSONObject(0).optString("intro");
                logics.state = array.optJSONObject(0).optString("state");
                vers.setLogics(logics);


            }

            @Override
            public void onFailure(String err_msg) {

            }
        });

    }

    @Override
    public void onClick(View view) {
    }

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
//            myService = ((ScoketService.LocalBinder) service).getService();
//            Log.e(TAG, "onServiceConnected myService: "+myService);
            mBinder = (ScoketService.LocalBinder)service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
//            Log.e(TAG, "onServiceDisconnected myService: "+name);
//            myService = null;
        }
    };

    public void goNext() {

    }

    public class UpdateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //获取service传过来的信息
            String client_id=intent.getStringExtra("client_id");
            SettingDefaultsManager.getInstance().setClientId(client_id);
                if(SettingDefaultsManager.getInstance().authToken()!=null){
                    bind_client_id();
                }
            }
    }
public void bind_client_id(){

    WebBase.Bind(new JSONHandler(MainActivity.this) {
        @Override
        public void onSuccess(JSONObject obj) {
            getWolle();
        }

        @Override
        public void onFailure(String err_msg) {
            Log.e("BindTAG",err_msg);
            message_text.setText(err_msg);
        }
    });
}
    public void getWolle(){
        WebBase.getWalle(new JSONHandler(MainActivity.this) {
            @Override
            public void onSuccess(JSONObject obj) {
                JSONObject pays=obj.optJSONObject("pay");
                JSONObject point=obj.optJSONObject("point");
                ImageLoader.getInstance().displayImage(SettingDefaultsManager.getInstance().UserAvatar(),avatar_id);
                SettingDefaultsManager.getInstance().setPays(pays.optString("unfrozen"));
                SettingDefaultsManager.getInstance().setPoint(point.optLong("unfrozen"));
                message_text.setText(SettingDefaultsManager.getInstance().NickName()+"已登陆"
                                + "\nclient_id:"+SettingDefaultsManager.getInstance().getClientId()
                                +"\n"+"用户魔方宝"+SettingDefaultsManager.getInstance().getPays()+
                                "\n"+"用户积分"+SettingDefaultsManager.getInstance().getPoint()
                );
            }
            @Override
            public void onFailure(String err_msg) {
                Log.e(">>>>","错误"+err_msg);

            }
        });
    }
}
