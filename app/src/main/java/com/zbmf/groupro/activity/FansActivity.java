package com.zbmf.groupro.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.zbmf.groupro.R;
import com.zbmf.groupro.adapter.BoxItemAdapter;
import com.zbmf.groupro.adapter.CouponsAdapter;
import com.zbmf.groupro.api.JSONHandler;
import com.zbmf.groupro.api.WebBase;
import com.zbmf.groupro.beans.BoxBean;
import com.zbmf.groupro.beans.CouponsBean;
import com.zbmf.groupro.beans.Group;
import com.zbmf.groupro.utils.Constants;
import com.zbmf.groupro.utils.JSONParse;
import com.zbmf.groupro.utils.SendBrodacast;
import com.zbmf.groupro.utils.ShowActivity;
import com.zbmf.groupro.utils.StretchTextUtil;
import com.zbmf.groupro.view.ListViewForScrollView;
import com.zbmf.groupro.view.PullToRefreshBase;
import com.zbmf.groupro.view.PullToRefreshScrollView;
import com.zbmf.groupro.view.SwipeToFinishView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuhao on 2017/1/3.
 */

public class FansActivity extends Activity implements View.OnClickListener ,CompoundButton.OnCheckedChangeListener{
    private ListViewForScrollView box_list;
    private List<BoxBean> infolist;
    private BoxItemAdapter adapter;
    private PullToRefreshScrollView fans_activity_scrollview;
    private int page,pages;
    private String GROUP_ID;
    private Dialog dialog;
    private List<CouponsBean>couponslist;
    private TextView fabs_desc_text,look_all_text,fans_profile,fans_activity_text,add_box_price_month,add_box_price_day,actitivy_text_message;
    private ImageView iv_arrow;
    private LinearLayout look_all_desc,fans_message_layout,fans_message_linear;
    private Group group_bean;
    private Button add_fans_button;
//    private RoundedCornerImageView fans_avatar_id;
    private boolean is_fans;
    private TextView group_title_name,tv_fans_type,tv_fans_day;
    DecimalFormat df=new DecimalFormat("");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fans_activity_layout);
        new SwipeToFinishView(this);
        group_bean= (Group) getIntent().getSerializableExtra("GROUP");
        GROUP_ID=group_bean.getId();
        init();
    }

    private void init() {
        box_list= (ListViewForScrollView) findViewById(R.id.box_item_message);
//        fans_avatar_id= (RoundedCornerImageView) findViewById(R.id.fans_avatar_id);
        fabs_desc_text= (TextView) findViewById(R.id.fabs_desc_text);
//        fans_profile= (TextView) findViewById(R.id.fans_profile);
        fans_activity_text= (TextView) findViewById(R.id.fans_activity_text);
        add_box_price_month= (TextView) findViewById(R.id.add_box_price_month);
        add_box_price_day= (TextView) findViewById(R.id.add_box_price_day);
        actitivy_text_message= (TextView) findViewById(R.id.actitivy_text_message);
        group_title_name= (TextView) findViewById(R.id.group_title_name);
        iv_arrow= (ImageView) findViewById(R.id.iv_arrow);
        look_all_desc= (LinearLayout) findViewById(R.id.look_all_desc);
        look_all_text= (TextView) findViewById(R.id.look_all_text);
        add_fans_button=(Button)findViewById(R.id.add_fans_button);

        findViewById(R.id.group_title_return).setOnClickListener(this);
        findViewById(R.id.coupons_countent_layout_id).setOnClickListener(this);
        findViewById(R.id.look_all_desc).setOnClickListener(this);

        fans_message_layout= (LinearLayout) findViewById(R.id.fans_message_layout);
        fans_message_linear= (LinearLayout) findViewById(R.id.fans_message_linear);
        tv_fans_type= (TextView) findViewById(R.id.tv_fans_type);
        tv_fans_day= (TextView) findViewById(R.id.tv_fans_day);

        group_title_name.setText(getString(R.string.tf_title));
        infolist=new ArrayList<>();
        adapter=new BoxItemAdapter(getBaseContext(),infolist);
        box_list.setAdapter(adapter);
        box_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(ShowActivity.isLogin(FansActivity.this)){
                    BoxBean boxBean=infolist.get(i);
                    int level = Integer.parseInt(boxBean.getBox_level());
                    if(group_bean.getFans_level()>=level){
                        boxBean.setId(GROUP_ID);
                        ShowActivity.showBoxDetailActivity(FansActivity.this,boxBean);
                    } else{
                        String message="您为【***】用户，升级成为【**】即可查";
                        switch (group_bean.getFans_level()){
                            case 0:
                                message=message.replace("【***】","非铁粉");
                                break;
                            case 5:
                                message=message.replace("【***】","体验铁粉");
                                break;
                            case 10:
                                message=message.replace("【***】","非年粉");
                                break;
                        }
                        if(level==10){
                            message=message.replace("【**】","包月铁粉");
                        }else if(level==20){
                            message=message.replace("【**】","年粉");
                        }
                        Toast.makeText(getBaseContext(),message,Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        add_fans_button.setOnClickListener(this);

        StretchTextUtil.getInstance(fabs_desc_text,look_all_text,2,look_all_desc,iv_arrow,R.drawable.icon_all_message_top,R.drawable.icon_all_messagebottom).initStretch();
        fans_activity_scrollview= (PullToRefreshScrollView) findViewById(R.id.fans_activity_scrollview);
        fans_activity_scrollview.getLoadingLayoutProxy().setPullLabel("加载更多数据");
        fans_activity_scrollview.getLoadingLayoutProxy().setRefreshingLabel("正在加载新数据...");
        fans_activity_scrollview.getLoadingLayoutProxy().setReleaseLabel("松开加载数据");
        fans_activity_scrollview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                //执行刷新函数
                page+=1;
                getBox_Message();
            }
        });
        setFansMessage();
        page=1;
        getBox_Message();
        getCouPonsList(false);
    }
    public void setFansMessage(){
        int level=group_bean.getFans_level();
        switch (level){
            case 0:
                setAddFanseMessage();
                break;
            case 5:
                setFanseMessage("体验铁粉");
                break;
            case 10:
                setFanseMessage("包月铁粉");
                break;
            case 20:
                setFanseMessage("年粉");
                break;
        }
    }
    private void setFanseMessage(String message){
        is_fans=true;
        fans_message_layout.setVisibility(View.VISIBLE);
        fans_message_linear.setVisibility(View.GONE);
        add_fans_button.setText("续费铁粉");
        fans_activity_text.setText(group_bean.getFans_activity());
        fabs_desc_text.setText(group_bean.getFans_countent());
        tv_fans_type.setText(message);
        tv_fans_day.setText("到期时间："+group_bean.getFans_date());
    }
    private void setAddFanseMessage(){
        is_fans=false;
        fans_message_layout.setVisibility(View.GONE);
        fans_message_linear.setVisibility(View.VISIBLE);
        add_fans_button.setText("加入铁粉");
        fans_activity_text.setText(group_bean.getFans_activity());
        fabs_desc_text.setText(group_bean.getFans_countent());
        add_box_price_month.setText(df.format(group_bean.getMonth_mapy())+"魔方宝/月");
        add_box_price_day.setText(df.format(group_bean.getDay_mapy())+"魔方宝/日");
        actitivy_text_message.setText(group_bean.getFans_activity());
    }
    public void getBox_Message(){
        WebBase.getGroupBoxs(GROUP_ID,"0", page, new JSONHandler(false,FansActivity.this,"正在加载数据...") {
            @Override
            public void onSuccess(JSONObject object) {
                BoxBean bb = JSONParse.getGroupBoxs(object);
                if(bb!=null && bb.getList()!=null )
                 infolist.addAll(bb.getList());
                adapter.notifyDataSetChanged();
                fans_activity_scrollview.onRefreshComplete();
                page=bb.getPage();
                pages=bb.getPages();
                if(page==pages){
                    Toast.makeText(FansActivity.this,"已加载全部数据",Toast.LENGTH_SHORT).show();
                    fans_activity_scrollview.setMode(PullToRefreshBase.Mode.DISABLED);
                }
            }

            @Override
            public void onFailure(String err_msg) {
                fans_activity_scrollview.onRefreshComplete();
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.add_fans_button:
                ShowActivity.showAddFansActivity(this,group_bean);
                break;
            case R.id.group_title_return:
                finish();
                break;
            case R.id.look_all_desc:

                break;
            case R.id.coupons_countent_layout_id:
                showDialig();
                break;
            case R.id.close_coupons_dialog:
                if (dialog!=null&&dialog.isShowing()){
                    dialog.cancel();
                }
                break;
        }
    }
    private CouponsAdapter couponsAdapter ;
    public void showDialig(){
        if(couponslist==null){
            getCouPonsList(true);
            return;
        }
        if(dialog!=null){
            dialog.show();
        }else{
            dialog=new Dialog(this,R.style.myDialogTheme);
            View view = LayoutInflater.from(this).inflate(R.layout.coupons_list_view, null);
            dialog.setContentView(view);
            Window win = dialog.getWindow();
            win.setGravity(Gravity.BOTTOM);
            ListView coupinslist= (ListView) view.findViewById(R.id.coupons_list_view_id);
            couponsAdapter=new CouponsAdapter(getBaseContext(),couponslist,this);
            coupinslist.setAdapter(couponsAdapter);
            view.findViewById(R.id.close_coupons_dialog).setOnClickListener(this);
            WindowManager.LayoutParams lp = win.getAttributes();
            lp.width = android.view.WindowManager.LayoutParams.MATCH_PARENT;
            win.setAttributes(lp);
            win.setWindowAnimations(R.style.dialoganimstyle);
            dialog.setCancelable(true);
            dialog.show();
        }
    }
    public void getCouPonsList(final boolean is_show){
        if(couponslist==null){
            couponslist=new ArrayList<>();
        }
        WebBase.getGroupCoupons(GROUP_ID, new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                if(!obj.isNull("groups")){
                    JSONArray groups=obj.optJSONArray("groups");
                    int size=groups.length();
                    for(int i=0;i<size;i++){
                        JSONObject coupon=groups.optJSONObject(i);
                        if(coupon!=null){
                            CouponsBean cb=new CouponsBean();
                            cb.setCoupon_id(coupon.optString("coupon_id"));
                            cb.setSubject(coupon.optString("subject"));
                            cb.setSumary(coupon.optString("summary"));
                            cb.setKind(coupon.optInt("kind"));
                            cb.setType("groups");
                            if(coupon.optInt("is_taken")==1){
                                //已经领取
                                cb.setIs_take(false);
                            }else{
                                cb.setIs_take(true);
                            }
                            cb.setStart_at(coupon.optString("start_at"));
                            cb.setEnd_at(coupon.optString("end_at"));
                            couponslist.add(cb);
                        }
                    }
                }
                if(!obj.isNull("systems")){
                    JSONArray groups=obj.optJSONArray("systems");
                    int size=groups.length();
                    for(int i=0;i<size;i++){
                        JSONObject coupon=groups.optJSONObject(i);
                        if(coupon!=null){
                            CouponsBean cb=new CouponsBean();
                            cb.setCoupon_id(coupon.optString("coupon_id"));
                            cb.setSubject(coupon.optString("subject"));
                            cb.setSumary(coupon.optString("summary"));
                            cb.setKind(coupon.optInt("kind"));
                            cb.setType("systems");
                            if(coupon.optInt("is_taken")==1){
                                cb.setIs_take(false);
                            }else{
                                cb.setIs_take(true);
                            }
                            cb.setStart_at(coupon.optString("start_at"));
                            cb.setEnd_at(coupon.optString("end_at"));
                            couponslist.add(cb);
                        }
                    }
                }
                if(is_show){
                    showDialig();
                }
            }
            @Override
            public void onFailure(String err_msg) {

            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if(b){
            int position= (int) compoundButton.getTag();
            takeCoupon(position);
        }
    }
    public void takeCoupon(final int positon){
        String coupons_id=couponslist.get(positon).getCoupon_id();
        WebBase.takeCoupon(coupons_id, new JSONHandler(true,this,"正在领取...") {
            @Override
            public void onSuccess(JSONObject obj) {
                SendBrodacast.send(getBaseContext(),Constants.UP_DATA_COUPONS);
                if(obj.optInt("is_taken")==0){
                    Toast.makeText(getBaseContext(),"领取成功",Toast.LENGTH_SHORT).show();
                    couponslist.get(positon).setIs_take(true);
                }else{
                    couponslist.get(positon).setIs_take(false);
                    couponslist.get(positon).setAmin_show(true);
                }
                if(couponsAdapter!=null){
                    couponsAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onFailure(String err_msg) {
                Toast.makeText(getBaseContext(),err_msg,Toast.LENGTH_SHORT).show();
                couponslist.get(positon).setIs_take(true);
                if(couponsAdapter!=null){
                    couponsAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constants.ADD_FANS:
                Bundle b=data.getExtras();
                is_fans=b.getBoolean("is_fans");
                if(is_fans){
                    add_fans_button.setText("续费铁粉");
                }else{
                    add_fans_button.setText("加入铁粉");
                }
                setFansMessage();
                break;
            default:
                break;
        }
    }
    @Override
    public void finish() {
        Intent intent=new Intent(this,Chat1Activity.class);
        Bundle bundle=new Bundle();
        bundle.putBoolean("is_fans", is_fans);
        intent.putExtras(bundle);
        setResult(Constants.ADD_FANS,intent);
        super.finish();
    }
    private void getFansInfo(){
        WebBase.fansInfo(GROUP_ID, new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                JSONObject group=obj.optJSONObject("group");
                group_bean.setId(group.optString("id"));
                group_bean.setName(group.optString("name"));
                group_bean.setNick_name(group.optString("nickname"));
                group_bean.setAvatar(group.optString("avatar"));
                group_bean.setIs_close(group.optInt("is_close"));
                group_bean.setIs_private(group.optInt("is_private"));
                group_bean.setRoles(group.optInt("roles"));
                group_bean.setFans_level(group.optInt("fans_level"));
                group_bean.setDay_mapy(group.optLong("day_mpay"));
                group_bean.setMonth_mapy(group.optLong("month_mpay"));
                group_bean.setEnable_day(group.optInt("enable_day"));
                group_bean.setEnable_point(group.optInt("enable_point"));
                group_bean.setMax_point(group.optInt("max_point"));
                group_bean.setDescription(group.optString("fans_profile"));
                group_bean.setFans_activity(group.optString("fans_activity"));
                group_bean.setFans_countent(group.optString("fans_content"));
                group_bean.setPoint_desc(group.optString("point_desc"));
                group_bean.setMax_mpay(group.optLong("max_mpay"));
                group_bean.setFans_date(group.optString("fans_expire_at"));
            }
            @Override
            public void onFailure(String err_msg) {

            }
        });
    }
}
