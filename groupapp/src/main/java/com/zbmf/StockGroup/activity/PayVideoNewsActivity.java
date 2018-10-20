package com.zbmf.StockGroup.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.api.JSONHandler;
import com.zbmf.StockGroup.api.WebBase;
import com.zbmf.StockGroup.beans.Group;
import com.zbmf.StockGroup.beans.Video;
import com.zbmf.StockGroup.beans.VideoPrice;
import com.zbmf.StockGroup.constans.IntentKey;
import com.zbmf.StockGroup.constans.RequestCode;
import com.zbmf.StockGroup.utils.SettingDefaultsManager;
import com.zbmf.StockGroup.utils.ShowActivity;

import org.json.JSONObject;

import java.text.DecimalFormat;

/**
 * Created by xuhao on 2017/7/7.
 */

public class PayVideoNewsActivity extends BaseActivity implements View.OnClickListener {
    private TextView group_title_name, tv_video_name,tv_video_teacher_name,tv_video_time,tv_series,my_tv_mfb,tv_fans_group_name,tv_series_message,add_fans_button_text,need_pay_mfb_number;
    private RelativeLayout just_this_layout,series_layout,fans_layout;
    private ImageView just_this_check,series_check,fans_check;
    private LinearLayout sure_add_fans_button,add_fans_pay_message;
    private Video video;
    private VideoPrice videoprice;
    private TextView tv_fansprice,tv_videoprice,tv_seriesprice,tv_videonewprice,tv_seriesnewprice;

    private double my_mfb,video_price,series_price;//需要支付的魔方宝
    private double need_pay_mfb;//需要充值的魔方宝
    private boolean to_wx_pay;
    private String id;//订阅ID
    private int select;//0:单个视频 1：视频集合 -1：加入铁粉

    public void initData() {
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            video= (Video) bundle.getSerializable(IntentKey.VIDEO_KEY);
            videoprice=video.getVideoPriceBean();
            setViewData();
        }
        getWolle();
    }

    @Override
    public void addListener() {

    }

    public void setViewData(){
        my_tv_mfb.setText(SettingDefaultsManager.getInstance().getPays());
        tv_video_name.setText(video.getVideoName());
        tv_video_teacher_name.setText(video.getVideoGroupname());
        tv_video_time.setText(videoprice.getVideo_start_at());
        tv_fans_group_name.setText(getString(R.string.video_fans).replace("[*]",videoprice.getGroup_name()));
        if(video.getIs_fans()){
            fans_layout.setVisibility(View.VISIBLE);
        }else{
            fans_layout.setVisibility(View.GONE);
        }
        if(video.getSeries_id()==0){
            series_layout.setVisibility(View.GONE);
        }else{
            series_layout.setVisibility(View.VISIBLE);
        }
        if(videoprice.getSeries_status()==1){
            tv_series_message.setText(getString(R.string.video_pecial_done).replace("[*]",videoprice.getSeries_phase()+""));
        }else{
            tv_series_message.setText(getString(R.string.video_pecial).replace("[*]",videoprice.getSeries_phase()+""));
        }

        tv_videoprice.setText(getDoubleormat(videoprice.getVideo_price()));
        tv_seriesprice.setText(getDoubleormat(videoprice.getSeris_price()));
//        tv_fansprice.setText(getDoubleormat(videoprice.getGroup_price()));
        tv_fansprice.setVisibility(View.GONE);

        if(videoprice.getVideo_discount()!=100){
            tv_videoprice.setVisibility(View.VISIBLE);
            tv_videoprice.getPaint().setAntiAlias(true);//抗锯齿
            tv_videoprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            video_price=videoprice.getVideo_price()*videoprice.getVideo_discount()/100;
            if(video_price==0){
                tv_videonewprice.setText("限时免费");
            }else{
                tv_videonewprice.setText(getDoubleormat(video_price));
            }
        }else{
            tv_videonewprice.setVisibility(View.GONE);
            video_price=videoprice.getVideo_price();
        }
        if(videoprice.getSeries_status()!=100){
            tv_seriesprice.setVisibility(View.VISIBLE);
            tv_seriesprice.getPaint().setAntiAlias(true);//抗锯齿
            tv_seriesprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            series_price=videoprice.getSeris_price()*videoprice.getSeries_discount()/100;
            if(series_price==0){
                tv_seriesnewprice.setText("限时免费");
            }else{
                tv_seriesnewprice.setText(getDoubleormat(series_price));
            }

        }else{
            tv_seriesprice.setVisibility(View.GONE);
            series_price=videoprice.getSeris_price();
        }
        my_mfb=Double.valueOf(SettingDefaultsManager.getInstance().getPays());
        CheckJust();
    }

    public void initView(){
        initTitle("订阅课程");
        tv_video_name=getView(R.id.tv_video_name);
        tv_video_teacher_name=getView(R.id.tv_video_teacher_name);
        tv_video_time=getView(R.id.tv_video_time);
        tv_series=getView(R.id.tv_series);
        my_tv_mfb=getView(R.id.my_tv_mfb);
        add_fans_button_text=getView(R.id.add_fans_button_text);
        need_pay_mfb_number=getView(R.id.need_pay_mfb_number);
        tv_fans_group_name=getView(R.id.tv_fans_group_name);
        tv_series_message=getView(R.id.tv_series_message);

        tv_fansprice=getView(R.id.fans_price);
        tv_videoprice=getView(R.id.video_price);
        tv_seriesprice=getView(R.id.series_price);
        tv_videonewprice=getView(R.id.video_new_price);
        tv_seriesnewprice=getView(R.id.series_new_price);

        just_this_layout=getView(R.id.just_this_layout);
        series_layout=getView(R.id.series_layout);
        fans_layout=getView(R.id.fans_layout);

        just_this_check=getView(R.id.just_this_check);
        series_check=getView(R.id.series_check);
        fans_check=getView(R.id.fans_check);

        sure_add_fans_button=getView(R.id.sure_add_fans_button);
        add_fans_pay_message=getView(R.id.add_fans_pay_message);

        tv_series.setOnClickListener(this);

        just_this_layout.setOnClickListener(this);
        series_layout.setOnClickListener(this);
        fans_layout.setOnClickListener(this);

        sure_add_fans_button.setOnClickListener(this);

    }
    public void getWolle() {
        WebBase.getWalle(new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                JSONObject pays = obj.optJSONObject("pay");
                JSONObject point = obj.optJSONObject("point");
                SettingDefaultsManager.getInstance().setPays(pays.optString("unfrozen"));
                SettingDefaultsManager.getInstance().setPoint(point.optLong("unfrozen"));
                my_tv_mfb.setText(SettingDefaultsManager.getInstance().getPays());
                my_mfb=Double.valueOf(SettingDefaultsManager.getInstance().getPays());
                switch (select){
                    case 0:
                        CheckJust();
                        break;
                    case 1:
                        CheckSeries();
                        break;
                    case -1:
                        CheckFans();
                        break;
                }
            }

            @Override
            public void onFailure(String err_msg) {
            }
        });
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_commit_video_layout;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_series:
                Bundle bundle=new Bundle();
                Video.SeriesVideo seriesVideo=new Video.SeriesVideo();
                seriesVideo.setSeries_id(video.getSeries_id()+"");
                seriesVideo.setCommit(1);
                bundle.putSerializable(IntentKey.VIDEO_SERIES,seriesVideo);
                ShowActivity.showActivity(PayVideoNewsActivity.this,bundle,SeriesVideoActivity.class);
                break;
            case R.id.just_this_layout:
                CheckJust();
                break;
            case R.id.series_layout:
                CheckSeries();
                break;
            case R.id.fans_layout:
                CheckFans();
                break;
            case R.id.sure_add_fans_button:
                if(to_wx_pay){
                    ShowActivity.showPayDetailActivity(PayVideoNewsActivity.this);
                }else{
                    if(select==-1){
                        addFans();
                    }else{
                        payVideo();
                    }
                }
                break;
        }
    }
    public void toPay(String message){
        to_wx_pay=false;
        add_fans_pay_message.setVisibility(View.GONE);
        add_fans_button_text.setText(message);
    }
    public void toWxPay(){
        to_wx_pay=true;
        add_fans_button_text.setText("去充值");
        need_pay_mfb_number.setText(getDoubleormat(need_pay_mfb));
        add_fans_pay_message.setVisibility(View.VISIBLE);
    }
    /**
     * 选中仅订阅本视频
     */
    public void CheckJust(){
        select=0;
        just_this_layout.setSelected(true);
        series_layout.setSelected(false);
        fans_layout.setSelected(false);
        just_this_check.setVisibility(View.VISIBLE);
        fans_check.setVisibility(View.GONE);
        series_check.setVisibility(View.GONE);
        need_pay_mfb=video_price-my_mfb;
        id=video.getVideoId();
        if(need_pay_mfb<=0){
            toPay(getString(R.string.pay_video));
        }else{
            toWxPay();
        }
    }
    /**
     * 选中仅订阅视频集合
     */
    public void CheckSeries(){
        select=1;
        just_this_layout.setSelected(false);
        series_layout.setSelected(true);
        fans_layout.setSelected(false);
        id=video.getSeries_id()+"";
        just_this_check.setVisibility(View.GONE);
        fans_check.setVisibility(View.GONE);
        series_check.setVisibility(View.VISIBLE);
        need_pay_mfb=series_price-my_mfb;
        if(need_pay_mfb<=0){
            toPay(getString(R.string.pay_video));
        }else{
            toWxPay();
        }
    }
    /**
     * 选中仅订阅铁粉
     */
    public void CheckFans(){
        select=-1;
        just_this_layout.setSelected(false);
        series_layout.setSelected(false);
        fans_layout.setSelected(true);
        just_this_check.setVisibility(View.GONE);
        series_check.setVisibility(View.GONE);
        fans_check.setVisibility(View.VISIBLE);
        toPay(getString(R.string.add_fans));
    }
    public void payVideo(){
        WebBase.PayVideo(select, id, new JSONHandler(true,PayVideoNewsActivity.this,getString(R.string.commit_fans)) {
            @Override
            public void onSuccess(JSONObject obj) {
                setResult(RESULT_OK);
                finish();
                Toast.makeText(getBaseContext(),"订阅成功",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String err_msg) {
                Toast.makeText(getBaseContext(),err_msg,Toast.LENGTH_SHORT).show();
            }
        });
    }

    DecimalFormat df = new DecimalFormat("");
    DecimalFormat double_df = new DecimalFormat("######0.00");
    public String getDoubleormat(double vealue) {
        if (double_df.format(vealue).contains(".00")) {
            double ve = Double.valueOf(double_df.format(vealue));
            return df.format(ve);
        } else {
            return double_df.format(vealue);
        }
    }
    public void addFans(){
        if(ShowActivity.isLogin(PayVideoNewsActivity.this)){
            WebBase.fansInfo(video.getGroup_id(), new JSONHandler(true,PayVideoNewsActivity.this,"正在加载数据...") {
                @Override
                public void onSuccess(JSONObject obj) {
                    JSONObject group=obj.optJSONObject("group");
                    Group groupbean=new Group();
                    groupbean.setId(group.optString("id"));
                    groupbean.setName(group.optString("name"));
                    groupbean.setNick_name(group.optString("nickname"));
                    groupbean.setAvatar(group.optString("avatar"));
                    groupbean.setIs_close(group.optInt("is_close"));
                    groupbean.setIs_private(group.optInt("is_private"));
                    groupbean.setRoles(group.optInt("roles"));
                    groupbean.setFans_level(group.optInt("fans_level"));
                    groupbean.setDay_mapy(group.optLong("day_mpay"));
                    groupbean.setMonth_mapy(group.optLong("month_mpay"));
                    groupbean.setEnable_day(group.optInt("enable_day"));
                    groupbean.setEnable_point(group.optInt("enable_point"));
                    groupbean.setMax_point(group.optInt("max_point"));
                    groupbean.setDescription(group.optString("fans_profile"));
                    groupbean.setFans_activity(group.optString("fans_activity"));
                    groupbean.setFans_countent(group.optString("fans_content"));
                    groupbean.setPoint_desc(group.optString("point_desc"));
                    groupbean.setMax_mpay(group.optLong("max_mpay"));
                    groupbean.setFans_date(group.optString("fans_expire_at"));
                    ShowActivity.showAddFansActivity(PayVideoNewsActivity.this,groupbean);
                }
                @Override
                public void onFailure(String err_msg) {

                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== RequestCode.ADD_FANS&&RESULT_OK==resultCode){
            setResult(RESULT_OK);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        StatService.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(this);
    }
}

