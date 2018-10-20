package com.zbmf.groupro.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.zbmf.groupro.R;
import com.zbmf.groupro.api.JSONHandler;
import com.zbmf.groupro.api.WebBase;
import com.zbmf.groupro.beans.BoxBean;
import com.zbmf.groupro.beans.BoxDetailBean;
import com.zbmf.groupro.utils.BoxDetailHtml;
import com.zbmf.groupro.utils.Constants;
import com.zbmf.groupro.utils.ShowActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//查看宝盒
public class BoxDetailWebActivity extends AppCompatActivity implements View.OnClickListener{
    private String boxId = "",group_id="";
    private int page,pages;
    private BoxBean mBoxBean;
    private WebView box_webview;
    TextView box_textView_pageStatus = null,tv_title;
    Button tv_right=null;
    // 首页
    Button box_button_firstPage = null;

    // 上一页
    Button box_button_priorPage = null;

    // 下一页
    Button box_button_nextPage = null;

    // 末页
    Button box_button_endPage = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_box_web_detail);
        initData();
        setupView();
    }

    private void initData() {
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        tv_title = ((TextView) findViewById(R.id.group_title_name));
        if (bundle != null) {
            mBoxBean = (BoxBean) bundle.getSerializable("BoxBean");
            if(mBoxBean!=null){
                boxId = mBoxBean.getBox_id();
                group_id=mBoxBean.getId();
                tv_title.setText(mBoxBean.getSubject());
                tv_title.setVisibility(View.VISIBLE);
            }else{
                mBoxBean=new BoxBean();
                group_id=getIntent().getStringExtra("group_id");
                boxId=getIntent().getStringExtra("box_id");
                getBoxDetail();
            }
        }
    }
    public void getBoxDetail(){
        WebBase.getBoxInfo(group_id,boxId,page, new JSONHandler(){
            @Override
            public void onSuccess(JSONObject obj) {
                JSONObject box=obj.optJSONObject("box");
                mBoxBean.setBox_id(boxId);
                mBoxBean.setId(group_id);
                mBoxBean.setSubject(box.optString("subject"));
                tv_title.setText(mBoxBean.getSubject());
                tv_title.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(String err_msg) {
                Toast.makeText(getBaseContext(),err_msg,Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void setupView() {
        findViewById(R.id.group_title_return).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        box_webview= (WebView) findViewById(R.id.box_webView_content);
        WebSettings webSettings = box_webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSaveFormData(true);
        webSettings.setSavePassword(false);
        webSettings.setSupportZoom(false);
        box_webview.setScrollbarFadingEnabled(true);
        box_webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        box_webview.setWebViewClient(new WebViewClient()
        {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                // 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
                if (url.indexOf("tel:") == 0)
                {
                    view.stopLoading();
                    return false;
                } else if (url.indexOf("img:") == 0) {
                    String imageURL = url.substring(4);
                    if (imageURL.indexOf("file://") == 0)
                    {
                        view.stopLoading();
                        return false;
                    }
                    ShowActivity.ShowBigImage(BoxDetailWebActivity.this,imageURL);
                    view.stopLoading();
                    return false;
                } else {
                    ShowActivity.showWebViewActivity(BoxDetailWebActivity.this,url);
                }
                return true;
            }
        });

        // 页面状态
        box_textView_pageStatus = (TextView) findViewById(R.id.box_textView_pageStatus);

        // 首页
        box_button_firstPage = (Button) findViewById(R.id.box_button_firstPage);
        box_button_firstPage.setOnClickListener(this);

        // 上一页
        box_button_priorPage = (Button) findViewById(R.id.box_button_priorPage);
        box_button_priorPage.setOnClickListener(this);

        // 下一页
        box_button_nextPage = (Button) findViewById(R.id.box_button_nextPage);
        box_button_nextPage.setOnClickListener(this);

        // 末页
        box_button_endPage = (Button) findViewById(R.id.box_button_endPage);
        box_button_endPage.setOnClickListener(this);

        tv_right= (Button) findViewById(R.id.group_title_right_button);
        tv_right.setText("简介");
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setOnClickListener(this);
        getGroupBoxInfo(1);
    }

    private void getGroupBoxInfo(int dir) {
        WebBase.getGroupBoxItems(group_id,boxId, dir, Constants.PER_PAGE, new JSONHandler(true,BoxDetailWebActivity.this,"正在获取数据...") {
            @Override
            public void onSuccess(JSONObject obj) {
                try {
                    JSONObject o = obj.getJSONObject("result");
                    page=o.optInt("page");
                    pages=o.optInt("pages");
                    JSONArray arr = o.getJSONArray("items");
                    String countent= BoxDetailHtml.getBoxHtml(arr);
                    box_webview.loadDataWithBaseURL(null, countent, "text/html", "utf-8", null);
                    box_textView_pageStatus.setText(page + " / " + pages);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String err_msg) {
                Toast toast = Toast.makeText(BoxDetailWebActivity.this, err_msg, Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.group_title_right_button:
                ShowActivity.showBoxDetailDescActivity(BoxDetailWebActivity.this,  mBoxBean);
                break;
            // 首页
            case R.id.box_button_firstPage:
            {
                if (page == 1)
                {
                    Toast toast = Toast.makeText(BoxDetailWebActivity.this, "已到达第一页", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    getGroupBoxInfo(1);
                }
            }
            break;

            // 上一页
            case R.id.box_button_priorPage:
            {
                if (page == 1)
                {
                    Toast toast = Toast.makeText(BoxDetailWebActivity.this, "已到达第一页", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    getGroupBoxInfo(page - 1);
                }
            }
            break;

            // 下一页
            case R.id.box_button_nextPage:
            {
                if (page == pages)
                {
                    Toast toast = Toast.makeText(BoxDetailWebActivity.this, "已到达最后一页", Toast.LENGTH_SHORT);
                    toast.show();

                } else {
                    getGroupBoxInfo(page + 1);
                }
            }
            break;

            // 末页
            case R.id.box_button_endPage:
            {
                if (page == pages)
                {
                    Toast toast = Toast.makeText(BoxDetailWebActivity.this, "已到达最后一页", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    getGroupBoxInfo(pages);
                }
            }
            break;
        }
    }
}
