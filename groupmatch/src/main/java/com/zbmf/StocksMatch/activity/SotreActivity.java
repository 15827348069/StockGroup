package com.zbmf.StocksMatch.activity;

import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zbmf.StocksMatch.R;

public class SotreActivity extends ExActivity {

    private AnimationDrawable anim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sotre);

        TextView tv_title = (TextView)findViewById(R.id.tv_title);
        ImageView iv_niu = (ImageView)findViewById(R.id.iv_niu);
        anim = (AnimationDrawable) iv_niu.getBackground();

        tv_title.setText(getString(R.string.appstore));
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView text_view2 = (TextView)findViewById(R.id.tv);
        text_view2.setText(Html.fromHtml("<a href='http://www.zbmf.com/point'>http://www.zbmf.com/point</a>"));
        text_view2.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        anim.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        anim.stop();
    }
}
