package com.zbmf.StockGroup.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.igexin.sdk.PushManager;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.api.JSONHandler;
import com.zbmf.StockGroup.api.WebBase;
import com.zbmf.StockGroup.utils.EditTextUtil;
import com.zbmf.StockGroup.utils.ImageLoaderOptions;
import com.zbmf.StockGroup.utils.SettingDefaultsManager;
import com.zbmf.StockGroup.view.RoundedCornerImageView;

import org.json.JSONObject;


public class ThirdPartyLogin extends Activity implements View.OnClickListener{
    private EditText ed_phone1;
    private EditText ed_yzm;
    private EditText ed_pwd1;
    private TextView tv_yzm_tip, tv_phone_tip, tv_pwd_tip, tv_yzm;
    private Button commit_phone;
    private RoundedCornerImageView third_avatar_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.third_party_login_layout);
        init();
        setListtent();
    }

    private void init() {
        ed_phone1= (EditText) findViewById(R.id.ed_phone1);
        ed_yzm= (EditText) findViewById(R.id.ed_yzm);
        ed_pwd1= (EditText) findViewById(R.id.ed_pwd1);
        tv_yzm= (TextView) findViewById(R.id.tv_yzm);
        commit_phone= (Button) findViewById(R.id.commit_phone);

        tv_yzm_tip = (TextView) findViewById(R.id.tv_yzm_tip);
        tv_phone_tip = (TextView) findViewById(R.id.tv_phone_tip);
        tv_pwd_tip = (TextView) findViewById(R.id.tv_pwd_tip);
        third_avatar_id= (RoundedCornerImageView) findViewById(R.id.third_avatar_id);
    }
    public void setListtent(){
//        ViewFactory.imgCircleView(this,SettingDefaultsManager.getInstance().UserAvatar(),third_avatar_id);
        ImageLoader.getInstance().displayImage(SettingDefaultsManager.getInstance().UserAvatar(),third_avatar_id, ImageLoaderOptions.AvatarOptions());
        commit_phone.setOnClickListener(this);
        tv_yzm.setOnClickListener(this);
        ed_phone1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length()==11){
                    PhoneMessage();
                }
            }
        });
        ed_yzm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length()==6){
                    YzmMessage();
                }
            }
        });
        ed_pwd1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length()>5&&!EditTextUtil.checkNum(editable.toString())){
                    tv_phone_tip.setVisibility(View.VISIBLE);
                }else{
                    tv_phone_tip.setVisibility(View.INVISIBLE);
                }
            }
        });
    }
    public boolean PhoneMessage(){
        boolean can_press;
        String phone=ed_phone1.getText().toString();
        if(phone.length()!=11&&!EditTextUtil.isMobileNO(phone)){
            tv_phone_tip.setVisibility(View.VISIBLE);
            tv_phone_tip.setText("手机号格式错误");
            can_press=false;
        }else{
            tv_phone_tip.setVisibility(View.INVISIBLE);
            can_press=true;
        }
        return can_press;
    }
    public boolean YzmMessage(){
        boolean can_press;
        String yzm=ed_yzm.getText().toString();
        if(yzm.length()!=6){
            tv_yzm_tip.setVisibility(View.VISIBLE);
            can_press=false;
        }else{
            tv_yzm_tip.setVisibility(View.INVISIBLE);
            can_press=true;
        }
        return can_press;
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.commit_phone:
                //提交绑定手机
                if(!PhoneMessage()){
                    Toast.makeText(getBaseContext(),"请输入正确的手机号",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!YzmMessage()){
                    Toast.makeText(getBaseContext(),"请输入正确的验证码",Toast.LENGTH_SHORT).show();
                    return;
                }
                WebBase.bindPhone(ed_phone1.getText().toString(), ed_yzm.getText().toString(), ed_pwd1.getText().toString(), PushManager.getInstance().getClientid(getBaseContext()), new JSONHandler(true, ThirdPartyLogin.this, "绑定手机号") {
                    @Override
                    public void onSuccess(JSONObject obj) {

                    }

                    @Override
                    public void onFailure(String err_msg) {

                    }
                });
                break;
            case R.id.tv_yzm:
                //获取验证码
                if(!PhoneMessage()){
                    Toast.makeText(getBaseContext(),"请输入正确的手机号",Toast.LENGTH_SHORT).show();
                    return;
                }
                WebBase.send_code(ed_phone1.getText().toString(), SettingDefaultsManager.getInstance().PUSH_CILENT_ID(), new JSONHandler(true, ThirdPartyLogin.this, "获取验证码") {
                    @Override
                    public void onSuccess(JSONObject obj) {
                        if (timecount == null) {
                            timecount = new TimeCount(60000, 1000);
                        }
                        timecount.start();
                    }
                    @Override
                    public void onFailure(String err_msg) {
                        tv_phone_tip.setText(err_msg);
                        Toast.makeText(getBaseContext(), err_msg, Toast.LENGTH_SHORT).show();
                    }
                });
                break;
        }
    }
    private TimeCount timecount;

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            tv_yzm.setText("重新发送");
            tv_yzm.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
            tv_yzm.setClickable(false);
            tv_yzm.setText(millisUntilFinished / 1000 + "秒");
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        StatService.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(this);
    }
}
