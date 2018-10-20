package com.zbmf.StockGTec.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.zbmf.StockGTec.R;
import com.zbmf.StockGTec.api.JSONHandler;
import com.zbmf.StockGTec.api.WebBase;
import com.zbmf.StockGTec.beans.BoxBean;
import com.zbmf.StockGTec.beans.BoxDetailBean;
import com.zbmf.StockGTec.utils.BoxDetailHtml;
import com.zbmf.StockGTec.utils.Constants;
import com.zbmf.StockGTec.utils.ShowActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//查看宝盒
public class BoxDetailActivity extends ExActivity implements View.OnClickListener{
    private String boxId = "";
    private int page,pages;
    private BoxBean mBoxBean;
    private WebView box_webview;
    TextView box_textView_pageStatus = null;
    TextView tv_right=null;
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
        setContentView(R.layout.activity_box_detail);

        initData();
        setupView();
    }

    private void initData() {
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            mBoxBean = (BoxBean) bundle.getSerializable("box");
            boxId = mBoxBean.getBox_id();
        }
    }

    private void setupView() {
        TextView tv_title = ((TextView) findViewById(R.id.group_title_name));
        tv_title.setText("查看宝盒");
        tv_title.setVisibility(View.VISIBLE);
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
                    ShowActivity.ShowBigImage(BoxDetailActivity.this,imageURL);
                    view.stopLoading();
                    return false;
                } else {
                    ShowActivity.showWebViewActivity(BoxDetailActivity.this,url);
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

        tv_right= (TextView) findViewById(R.id.tv_right);
        tv_right.setText("简介");
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setOnClickListener(this);
        getGroupBoxInfo(1);
    }

    private void getGroupBoxInfo(int dir) {
        WebBase.getGroupBoxInfo( boxId, dir, Constants.PER_PAGE, new JSONHandler(true,BoxDetailActivity.this,"正在获取数据...") {
            @Override
            public void onSuccess(JSONObject obj) {
                BoxDetailBean bd = new BoxDetailBean();
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
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.tv_right:
                Bundle bundle = new Bundle();
                bundle.putSerializable("box", mBoxBean);
                ShowActivity.startActivity(BoxDetailActivity.this, bundle, BoxActivity.class.getName());
                break;
            // 首页
            case R.id.box_button_firstPage:
            {
                if (page == 1)
                {
                    Toast toast = Toast.makeText(BoxDetailActivity.this, "已到达第一页", Toast.LENGTH_SHORT);
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
                    Toast toast = Toast.makeText(BoxDetailActivity.this, "已到达第一页", Toast.LENGTH_SHORT);
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
                    Toast toast = Toast.makeText(BoxDetailActivity.this, "已到达最后一页", Toast.LENGTH_SHORT);
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
                    Toast toast = Toast.makeText(BoxDetailActivity.this, "已到达最后一页", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    getGroupBoxInfo(pages);
                }
            }
            break;
        }
    }
}
