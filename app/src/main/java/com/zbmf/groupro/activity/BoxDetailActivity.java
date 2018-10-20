package com.zbmf.groupro.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.zbmf.groupro.R;
import com.zbmf.groupro.adapter.BoxCountentIAdapter;
import com.zbmf.groupro.api.JSONHandler;
import com.zbmf.groupro.api.WebBase;
import com.zbmf.groupro.beans.BoxBean;
import com.zbmf.groupro.beans.BoxDetailBean;
import com.zbmf.groupro.beans.LiveTypeMessage;
import com.zbmf.groupro.view.ListViewForScrollView;
import com.zbmf.groupro.view.MyTextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BoxDetailActivity extends Activity implements MyTextView.OnTextClickListener{
    private ImageButton group_title_return;
    private TextView box_detail_title,box_detail_desc,created_items;
    private ListViewForScrollView box_detail_countent;
    private BoxCountentIAdapter adapter;
    private List<BoxDetailBean.Items>info;
    private BoxBean boxBean;
    private int page=1,pages;
    private TextView box_detail_message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_box_detail);
        boxBean= (BoxBean) getIntent().getSerializableExtra("BoxBean");
        init();
        getBoxDetail();
    }

    private void init() {
        box_detail_title= (TextView) findViewById(R.id.box_detail_title);
        box_detail_desc= (TextView) findViewById(R.id.box_detail_desc);
        box_detail_countent= (ListViewForScrollView) findViewById(R.id.box_detail_countent);
        group_title_return= (ImageButton) findViewById(R.id.group_title_return);
        created_items= (TextView) findViewById(R.id.created_items);
        box_detail_message= (TextView) findViewById(R.id.box_detail_message);
//        tv_title = ((TextView) findViewById(R.id.group_title_name));
        group_title_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        info=new ArrayList<>();
        adapter=new BoxCountentIAdapter(getBaseContext(),info,this);
        box_detail_countent.setAdapter(adapter);
    }
    @Override
    public void OnTextClickListener(LiveTypeMessage message) {

    }
    public void getBoxDetail(){
        String group_id=null;
        String box_id=null;
        if(boxBean!=null){
            group_id=boxBean.getId();
            box_id=boxBean.getBox_id();
            box_detail_title.setText(boxBean.getSubject());
            box_detail_desc.setText(boxBean.getDescription());
            created_items.setText("共发布"+boxBean.getItems()+"条宝盒消息");
        }else{
            boxBean=new BoxBean();
            group_id=getIntent().getStringExtra("group_id");
            box_id=getIntent().getStringExtra("box_id");
            boxBean.setId(group_id);
            boxBean.setBox_id(box_id);
        }
        WebBase.getBoxInfo(group_id,box_id,page, new JSONHandler(){
            @Override
            public void onSuccess(JSONObject obj) {
                BoxDetailBean bb=new BoxDetailBean();
                JSONObject result=obj.optJSONObject("result");
                if(page==1){
                    JSONObject box=obj.optJSONObject("box");
                    box_detail_title.setText(box.optString("subject"));
                    box_detail_desc.setText(box.optString("description"));
                    box_detail_message.setText("本宝盒由投顾助理(证券执业资格证号："+box.optString("certificate")+")发布由"+box.optString("nickname")+"提供素材");
                }
                pages=result.optInt("pages");
                page=result.optInt("page");
                JSONArray items=result.optJSONArray("items");
                int size=items.length();
                created_items.setText("共发布"+size+"条宝盒消息");
//                for(int i=0;i<size;i++){
//                    JSONObject item=items.optJSONObject(i);
//                    info.add(bb.getItems(item.optString("item_id"),item.optString("content"),item.optString("is_pith"),item.optString("created_at")));
//                }
//                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String err_msg) {
                Toast.makeText(getBaseContext(),err_msg,Toast.LENGTH_SHORT).show();
            }
        });
    }
}
