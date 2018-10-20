package com.zbmf.StocksMatch.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.api.Get2Api;
import com.zbmf.StocksMatch.api.Get2ApiImpl;
import com.zbmf.StocksMatch.beans.General;
import com.zbmf.StocksMatch.listener.MyTextWatcher;
import com.zbmf.StocksMatch.utils.UiCommon;
import com.zbmf.StocksMatch.widget.LoadingDialog;

import org.json.JSONException;

public class FeedbackActivity extends ExActivity {

    private static final int MAX_INPUT_TEXT = 500;
    private TextView tv_title,tv_right,tv_tip;
    private EditText ed_text;
    private String content="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        setupView();

    }

    private void setupView() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_tip = (TextView) findViewById(R.id.tv_tip);
        ed_text = (EditText) findViewById(R.id.ed_text);
        tv_title.setText(getString(R.string.feedback1));
        tv_right = (TextView)findViewById(R.id.tv_right);
        tv_right.setText(getString(R.string.submit));
        ed_text.setText("#"+getString(R.string.app_name)+"Android v"+UiCommon.VERSION_NAME+"#");
        ed_text.setSelection(ed_text.getText().toString().length());
        tv_tip.setText(getString(R.string.input_tip22, ed_text.getText().toString().length()));
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        MyTextWatcher watcher = new MyTextWatcher(ed_text, MAX_INPUT_TEXT);
        watcher.setListener(new MyTextWatcher.WatchListener() {
            @Override
            public void onChanged() {
            }

            @Override
            public void afterChanged(Editable arg0) {
                content = arg0.toString();
                if (content.length() < MAX_INPUT_TEXT) {
                    tv_tip.setText(getString(R.string.input_tip22, content.length()));
                    if(content.length()>0)
                        tv_right.setVisibility(View.VISIBLE);
                    else
                        tv_right.setVisibility(View.GONE);
                }else{
                    tv_tip.setText(getString(R.string.input_tip22, MAX_INPUT_TEXT));
                }
            }
        });
        ed_text.addTextChangedListener(watcher);

        tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SuggestsTask(FeedbackActivity.this,R.string.submitting,R.string.load_fail,true).execute(content);
            }
        });
    }

    private Get2Api server;
    private class SuggestsTask extends LoadingDialog<String,General> {


        public SuggestsTask(Context activity, int loadingMsg, int failMsg, boolean Enddismiss) {
            super(activity, loadingMsg, failMsg, Enddismiss);
        }

        @Override
        public General doInBackground(String... params) {
            if(server == null){
                server = new Get2ApiImpl();
            }

            try {
                return server.suggests(params[0]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void doStuffWithResult(General result) {
            if (result != null && result.code != -1) {
                if (result.getStatus()==1) {
                    UiCommon.INSTANCE.showTip(getString(R.string.submit_tip));
                    finish();
                } else {
                    UiCommon.INSTANCE.showTip(result.msg);
                }
            }else {
                UiCommon.INSTANCE.showTip(getString(R.string.load_fail));
            }
        }
    }

}
