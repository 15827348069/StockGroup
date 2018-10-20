package com.zbmf.StocksMatch.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zbmf.StocksMatch.R;

public class AboutUsActivity extends ExActivity {
    private TextView text_view,text_view2,tv_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        text_view=(TextView) findViewById(R.id.about_us_message_url);
        text_view2=(TextView) findViewById(R.id.about_us_message_url2);
        tv_title = (TextView)findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.about_us));
       findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               finish();
           }
       });
        text_view.setText(Html.fromHtml("<a href='http://www.7878.com'>www.7878.com </a>"));
        text_view2.setText(Html.fromHtml("<a href='http://www.zbmf.com'>www.zbmf.com</a>"));
        text_view.setMovementMethod(LinkMovementMethod.getInstance());
        text_view2.setMovementMethod(LinkMovementMethod.getInstance());

    }
}
