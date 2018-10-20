package com.zbmf.StockGTec.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zbmf.StockGTec.R;
import com.zbmf.StockGTec.api.JSONHandler;
import com.zbmf.StockGTec.api.WebBase;

import org.json.JSONObject;

public class RelDateActivity extends AppCompatActivity {

    private TextView tv_follow,tv_hot,tv_online,tv_tie,tv_jin,tv_expiring,tv_expired;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rel_date);


        findViewById(R.id.group_title_return).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        TextView tv_title = (TextView) findViewById(R.id.group_title_name);
        tv_title.setText("数据");
        tv_title.setVisibility(View.VISIBLE);
        tv_follow = (TextView) findViewById(R.id.tv_follow);
        tv_hot = (TextView) findViewById(R.id.tv_hot);
        tv_online = (TextView) findViewById(R.id.tv_online);
        tv_tie = (TextView) findViewById(R.id.tv_tie);
        tv_jin = (TextView) findViewById(R.id.tv_jin);
        tv_expiring = (TextView) findViewById(R.id.tv_expiring);
        tv_expired = (TextView) findViewById(R.id.tv_expired);


        data();
    }

    private void data() {
        WebBase.groupData(new JSONHandler(true,this,"加载中...") {
            @Override
            public void onSuccess(JSONObject obj) {
                if (!obj.isNull("data")) {
                    JSONObject object = obj.optJSONObject("data");
                    tv_follow.setText(object.optInt("follow") + "");
                    tv_hot.setText(object.optInt("hot") + "");
                    tv_online.setText(object.optInt("online") + "");
                    tv_tie.setText(object.optInt("tie") + "");
                    tv_jin.setText(object.optInt("jin") + "");
                    tv_expiring.setText(object.optInt("expiring") + "");
                    tv_expired.setText(object.optInt("expired") + "");
                }
            }

            @Override
            public void onFailure(String err_msg) {
                Toast.makeText(RelDateActivity.this, err_msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
