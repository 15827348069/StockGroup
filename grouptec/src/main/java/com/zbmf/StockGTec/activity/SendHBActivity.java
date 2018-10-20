package com.zbmf.StockGTec.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.zbmf.StockGTec.R;
import com.zbmf.StockGTec.api.JSONHandler;
import com.zbmf.StockGTec.api.WebBase;
import com.zbmf.StockGTec.utils.SettingDefaultsManager;

import org.json.JSONObject;

import java.text.DecimalFormat;


public class SendHBActivity extends ExActivity implements View.OnClickListener {

    private EditText ed_mfb, ed_hongbao, ed_liuyan;
    private TextView tv_send;
    private String liuyan = "祝大家天天涨停板啊", pays, num;
    private TextView title, tv_pays;
    private ImageButton group_title_return;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_hb);

        setupView();
    }

    private void setupView() {
        ed_mfb = (EditText) findViewById(R.id.ed_mfb);
        ed_hongbao = (EditText) findViewById(R.id.ed_hongbao);
        ed_liuyan = (EditText) findViewById(R.id.ed_liuyan);
        tv_send = (TextView) findViewById(R.id.tv_send);
        tv_send.setOnClickListener(this);
        title = (TextView) findViewById(R.id.group_title_name);
        tv_pays = (TextView) findViewById(R.id.tv_pays);
        group_title_return= (ImageButton) findViewById(R.id.group_title_return);
        group_title_return.setOnClickListener(this);
        title.setText(getResources().getString(R.string.send_hb));
        title.setVisibility(View.VISIBLE);
        ed_mfb.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                pays = s.toString().trim();
                if(!TextUtils.isEmpty(pays)){
                    tv_pays.setText(cal(Double.parseDouble(pays)));
                    if(Double.parseDouble(SettingDefaultsManager.getInstance().getPays()) < Double.parseDouble(pays)){
                        Toast.makeText(SendHBActivity.this,"资金不足，可用:" + SettingDefaultsManager.getInstance().getPays(),Toast.LENGTH_SHORT).show();
                        setCheck(false);
                        return;
                    }
                    if(Integer.parseInt(pays) > 2000){
                        Toast.makeText(SendHBActivity.this,"最多2000个",Toast.LENGTH_SHORT).show();
                        setCheck(false);
                        return;
                    }
                }
                checkNull(pays, num);

            }
        });
        ed_hongbao.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                num = s.toString().trim();
                if(!TextUtils.isEmpty(num) && Integer.parseInt(num) > 100){
                    Toast.makeText(SendHBActivity.this,"最多100个",Toast.LENGTH_SHORT).show();
                    setCheck(false);
                    return;
                }
                checkNull(pays, num);
            }
        });

        ed_liuyan.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                liuyan = s.toString();
                checkNull(pays, num);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_send:
                WebBase.sendRedPackged(liuyan, pays, Integer.parseInt(num), new JSONHandler(true, SendHBActivity.this, "正在发送...") {
                    @Override
                    public void onSuccess(JSONObject obj) {
                        finish();
                    }

                    @Override
                    public void onFailure(String err_msg) {
                        Toast.makeText(SendHBActivity.this,err_msg,Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.group_title_return:
                finish();
                break;
        }
    }

    private void checkNull(String... strings) {
        int count = 0;
        int size = strings.length;
        for (String s : strings) {
            if (!TextUtils.isEmpty(s)) {
                count++;
            }
        }
        if (count >= size) {
            tv_send.setEnabled(true);
        } else tv_send.setEnabled(false);

    }
    public void setCheck(boolean can_send){
        tv_send.setEnabled(can_send);
    }
    public String cal(Double value) {
        DecimalFormat df = new DecimalFormat("######0.00");
        return df.format(value);
    }
}
