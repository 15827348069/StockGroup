package com.zbmf.StockGTec.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zbmf.StockGTec.R;
import com.zbmf.StockGTec.api.JSONHandler;
import com.zbmf.StockGTec.api.WebBase;

import org.json.JSONObject;

/**
 * 公告设置。
 */
public class PublishNoticeActivity extends ExActivity {

    private String group_id, link = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_notice);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            group_id = bundle.getString("group_id");
        }

        TextView tv_title = (TextView) findViewById(R.id.group_title_name);
        tv_title.setText("公告设置");
        TextView tv_right = (TextView) findViewById(R.id.tv_right);
        final EditText ed_msg = (EditText) findViewById(R.id.ed_msg);
        tv_title.setVisibility(View.VISIBLE);
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setText("发布");


        findViewById(R.id.group_title_return).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = ed_msg.getText().toString();
                if (TextUtils.isEmpty(content)) {

                } else {
                    WebBase.setNotice(group_id, content, new JSONHandler(true, PublishNoticeActivity.this, "正在加载...") {
                        @Override
                        public void onSuccess(JSONObject obj) {
                            Toast.makeText(PublishNoticeActivity.this, "公告已发布", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                        @Override
                        public void onFailure(String err_msg) {

                        }
                    });
                }
            }
        });

    }
}
