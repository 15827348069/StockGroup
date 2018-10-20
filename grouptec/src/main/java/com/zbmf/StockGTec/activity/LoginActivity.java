package com.zbmf.StockGTec.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.igexin.sdk.PushManager;
import com.zbmf.StockGTec.R;
import com.zbmf.StockGTec.api.JSONHandler;
import com.zbmf.StockGTec.api.WebBase;
import com.zbmf.StockGTec.beans.LoginInfo;
import com.zbmf.StockGTec.beans.User;
import com.zbmf.StockGTec.utils.Constants;
import com.zbmf.StockGTec.utils.ObjectUtil;
import com.zbmf.StockGTec.utils.SettingDefaultsManager;
import com.zbmf.StockGTec.utils.ShowActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class LoginActivity extends ExActivity implements View.OnClickListener {

    private EditText ed_phone, ed_pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setupView();
    }

    private void setupView() {
        ed_pwd = (EditText) findViewById(R.id.ed_pwd);
        ed_phone = (EditText) findViewById(R.id.ed_phone);
        findViewById(R.id.btn_login).setOnClickListener(this);
        findViewById(R.id.tv_forget).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_forget:
                ShowActivity.showActivity(this, ForgetActivity.class);
                break;
            case R.id.btn_login:
                if (TextUtils.isEmpty(ed_phone.getText().toString())) {
                    Toast.makeText(getBaseContext(), "请输入手机号或用户名", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(ed_pwd.getText().toString())) {
                    Toast.makeText(getBaseContext(), "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }

                WebBase.login(ed_phone.getText().toString(), ed_pwd.getText().toString(), PushManager.getInstance().getClientid(getBaseContext()), new JSONHandler(true, LoginActivity.this, "正在登录...") {
                    @Override
                    public void onSuccess(JSONObject obj) {
                        LoginInfo info = new LoginInfo();
                        try {

                            User user = new User();
                            user.setAuth_token(obj.optString("auth_token"));
                            JSONObject object = obj.getJSONObject("user");
                            user.setUser_id(object.optString("user_id"));
                            user.setUsername(object.optString("username"));
                            user.setNickname(object.optString("nickname"));
                            user.setAvatar(object.optString("avatar"));
                            user.setRole(object.optString("role"));
                            user.setPhone(object.optString("phone"));
                            user.setContent(obj.optString("content"));
                            info.setUser(user);
                            JSONArray jsonArray = obj.getJSONArray("groups");
                            List<LoginInfo.Group> groups = new ArrayList<LoginInfo.Group>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                LoginInfo.Group group = new LoginInfo.Group();
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                group.setId(jsonObject.optInt("id"));
                                group.setName(jsonObject.optString("name"));
                                group.setNickname(jsonObject.optString("nickname"));
                                group.setAvatar(jsonObject.optString("avatar"));
                                group.setContent(jsonObject.optString("content"));
                                group.setIs_close(jsonObject.optInt("is_close"));
                                group.setIs_private(jsonObject.optInt("is_private"));
                                LoginInfo.Auth auth = new LoginInfo.Auth();
                                JSONObject auth1 = jsonObject.getJSONObject("auth");
                                auth.setLive(auth1.optInt("live"));
                                auth.setFans(auth1.optInt("fans"));
                                auth.setData(auth1.optInt("data"));
                                auth.setCreator(auth1.optInt("creator"));
                                group.setAuth(auth);
                                groups.add(group);
                            }
                            info.setGroups(groups);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        SettingDefaultsManager instance = SettingDefaultsManager.getInstance();
                        instance.setAuthtoken(info.getUser().getAuth_token());
                        instance.setNickName(info.getUser().getNickname());
                        instance.setUserAvatar(info.getUser().getAvatar());
                        instance.setUserId(info.getUser().getUser_id());
                        instance.setGroupContent(info.getUser().getContent());
                        instance.setGroupName(instance.NickName());
                        instance.setGroupId(instance.UserId());
                        instance.setGroupImg(instance.UserAvatar());
                        instance.setGroupContent(instance.getGroupContent());
                        instance.setIsChatManager(false);
                        instance.setGroupManager(false);
                        instance.setManager(false);
                        instance.setSayFans(false);
                        if (info.getGroups().size() > 0) {
                            LoginInfo.Group group = info.getGroups().get(0);
                            instance.setGroupName(group.getName());
                            instance.setGroupId(group.getId() + "");
                            instance.setGroupImg(group.getAvatar());
                            instance.setGroupContent(group.getContent());
                            if (group.getAuth().getCreator()== 1) {
                                instance.setIsChatManager(true);
                                instance.setGroupManager(true);
                            }

                            if(group.getAuth().getFans() == 1)
                                instance.setSayFans(true);
                            instance.setManager(true);
                        }

                        instance.setCurrentChat(instance.getGroupId());
                        ObjectUtil.saveObj(info, Constants.LOGIN_INFO);
                        ShowActivity.showActivity(LoginActivity.this, HomeActivity.class);
                        finish();
                    }

                    @Override
                    public void onFailure(String err_msg) {
                        Toast.makeText(getBaseContext(), err_msg, Toast.LENGTH_SHORT).show();
                    }
                });
                break;
        }
    }
}
