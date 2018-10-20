package com.zbmf.StockGroup.activity;

import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.beans.MatchAnnouncements;
import com.zbmf.StockGroup.constans.IntentKey;


/**
 * Created by xuhao on 2018/1/5.
 */
public class AnnouncementActivity extends BaseActivity {
    TextView tvTitle;
    TextView tvDate;
    TextView tvContent;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_announcenment_layout;
    }

    @Override
    public void initView() {
        initTitle("公告");
        tvTitle=getView(R.id.tv_title);
        tvDate=getView(R.id.tv_date);
        tvContent=getView(R.id.tv_content);
    }

    @Override
    public void initData() {
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            MatchAnnouncements matchAnnouncements=bundle.getParcelable(IntentKey.MATCHANNOUNCEMENTS);
            tvTitle.setText(matchAnnouncements.getSubject());
            tvDate.setText(matchAnnouncements.getPosted_at());
            tvContent.setText(Html.fromHtml(matchAnnouncements.getContent()));
        }
    }

    @Override
    public void addListener() {

    }
}
