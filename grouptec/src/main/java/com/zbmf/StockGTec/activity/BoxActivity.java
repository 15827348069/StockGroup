package com.zbmf.StockGTec.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zbmf.StockGTec.R;
import com.zbmf.StockGTec.api.JSONHandler;
import com.zbmf.StockGTec.api.WebBase;
import com.zbmf.StockGTec.beans.BoxBean;
import com.zbmf.StockGTec.beans.BoxDetailBean;
import com.zbmf.StockGTec.utils.Constants;
import com.zbmf.StockGTec.view.PullToRefreshBase;
import com.zbmf.StockGTec.view.PullToRefreshScrollView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

//查看宝盒
public class BoxActivity extends ExActivity {

    private List<BoxDetailBean.Items> list = new ArrayList<>();
//    private BoxAdapter adapter;
    private TextView tv_box_name,tv_box_upde_time,tv_desc;
    private LinearLayout tag_layout;
//    private ListViewForScrollView listview;
    private ImageView iv;
    private PullToRefreshScrollView pullToRefreshScrollView;
    private String boxId = "";
    private int page,PAGES;
    private BoxBean mBoxBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_box);

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
        tv_title.setText("宝盒简介");
        tv_title.setVisibility(View.VISIBLE);
        iv = (ImageView) findViewById(R.id.box_img_id);
        tv_box_name = (TextView) findViewById(R.id.box_name);
        tv_box_upde_time = (TextView) findViewById(R.id.box_upde_time);
        tv_desc = (TextView) findViewById(R.id.tv_desc);
        tag_layout = (LinearLayout) findViewById(R.id.tag_layout);
//        listview = (ListViewForScrollView) findViewById(R.id.lv_focus);
//        adapter = new BoxAdapter(this, list);
//        listview.setAdapter(adapter);
        pullToRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.sc);
//        pullToRefreshScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
//            @Override
//            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
//                getGroupBoxInfo(1,false);
//            }
//
//            @Override
//            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
//                getGroupBoxInfo(1,false);
//            }
//        });
        findViewById(R.id.group_title_return).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if(mBoxBean != null){
            initBoxInfo(mBoxBean);
        }
        getGroupBoxInfo(1,true);
    }

    private void initBoxInfo(BoxBean bb) {
        tv_desc.setText(bb.getDescription());
        tv_box_upde_time.setText(bb.getBox_updated());
        tv_box_name.setText(bb.getSubject());
        if (bb.getTags() != null && bb.getTags().size() > 0) {
            tag_layout.removeAllViewsInLayout();
            tag_layout.setVisibility(View.VISIBLE);
            for (BoxBean.Tags tag : bb.getTags()) {
                tag_layout.addView(getTagView(tag));
            }
        } else {
            tag_layout.removeAllViewsInLayout();
            tag_layout.setVisibility(View.GONE);
        }

        switch (bb.getBox_level()) {
            case "20":
                iv.setImageDrawable(getResources().getDrawable(R.drawable.icon_year_fans_1));
                break;
            case "10":
                if (bb.getTags() != null && bb.getTags().size() > 0) {
                    if (bb.getTags().get(0).equals("投资策略")) {
                        iv.setImageDrawable(getResources().getDrawable(R.drawable.icon_fans_1));
                    } else if (bb.getTags().get(0).equals("操盘日志")) {
                        iv.setImageDrawable(getResources().getDrawable(R.drawable.icon_fans_2));
                    } else {
                        iv.setImageDrawable(getResources().getDrawable(R.drawable.icon_fans_3));
                    }
                } else {
                    iv.setImageDrawable(getResources().getDrawable(R.drawable.icon_fans_1));
                }

                break;
            default:
                iv.setImageDrawable(getResources().getDrawable(R.drawable.icon_public_fans2));
                break;
        }
    }

    public View getTagView(BoxBean.Tags tag) {
        View view = View.inflate(this,R.layout.tag_text_view, null);
        TextView tag_text = (TextView) view.findViewById(R.id.tag_layout_id);
        switch (tag.getTag_type()) {
            case 1:
                tag_text.setBackgroundResource(R.drawable.text_backound_blue);
                break;
            case 2:
                tag_text.setBackgroundResource(R.drawable.text_backound_orange);
                break;
            case 3:
                tag_text.setBackgroundResource(R.drawable.text_backound);
                break;
            default:
                tag_text.setBackgroundResource(R.drawable.text_backound_def);
                break;
        }
        tag_text.setText(tag.getName());
        return view;
    }

    private void getGroupBoxInfo(int dir,boolean show) {
        if(dir == 1){
            page++;
        }

        WebBase.getGroupBoxInfo(boxId, page, Constants.PER_PAGE, new JSONHandler(BoxActivity.this) {
            @Override
            public void onSuccess(JSONObject obj) {
                pullToRefreshScrollView.onRefreshComplete();
                BoxDetailBean bd = new BoxDetailBean();
                try {
                    JSONObject o = obj.getJSONObject("result");
                    bd.setPages(o.optInt("pages"));
                    JSONArray arr = o.getJSONArray("items");
                    List<BoxDetailBean.Items> items = new ArrayList<BoxDetailBean.Items>();
                    for(int i=0;i<arr.length();i++){
                        JSONObject object = arr.getJSONObject(i);
                        BoxDetailBean.Items item = new BoxDetailBean.Items();
                        item.setContent(object.optString("content"));
                        item.setCreated_at(object.optString("created_at"));
                        items.add(item);
                    }
                    list.addAll(items);
//                    adapter.notifyDataSetChanged();

                    JSONObject b = obj.getJSONObject("box");
                    BoxBean bb = new BoxBean();
                    bb.setBox_id(b.optString("box_id"));
                    bb.setSubject(b.optString("subject"));
                    bb.setDescription(b.optString("description"));
                    bb.setIs_stick(b.optString("is_stick"));
                    bb.setIs_stop(b.optString("is_stop"));
                    bb.setBox_level(b.optString("box_level"));
                    bb.setBox_updated(b.optString("box_updated"));
                    JSONArray brr = b.getJSONArray("tags");
                    List<BoxBean.Tags> tags = new ArrayList<BoxBean.Tags>();
                    for(int i=0;i<brr.length();i++){
                        JSONObject jsonObject = brr.getJSONObject(i);
                        tags.add(bb.getTag(jsonObject.optString("name"),i+1));
                    }
                    bb.setTags(tags);
                    initBoxInfo(bb);
                    PAGES = bd.getPages();
                    if (page == PAGES){
//                        Toast.makeText(BoxActivity.this, "已加载全部数据", Toast.LENGTH_SHORT).show();
                        pullToRefreshScrollView.setMode(PullToRefreshBase.Mode.DISABLED);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String err_msg) {
                pullToRefreshScrollView.onRefreshComplete();
            }
        });
    }
}
