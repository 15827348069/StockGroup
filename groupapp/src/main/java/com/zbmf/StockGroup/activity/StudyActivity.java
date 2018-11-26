package com.zbmf.StockGroup.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.constans.Constants;
import com.zbmf.StockGroup.fragment.study.StudyChouseFragment;
import com.zbmf.StockGroup.fragment.study.StudyVideoFragment;
import com.zbmf.StockGroup.fragment.study.StudyVideoSeriesFragment;
import com.zbmf.StockGroup.utils.LogUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xuhao
 * on 2017/2/13.
 */

public class StudyActivity extends BaseActivity implements  RadioGroup.OnCheckedChangeListener,
        StudyVideoFragment.GroupRadioGone, StudyChouseFragment.GroupRadioGone{
    private RadioGroup video_select_radio;
    private StudyVideoFragment studyVideoFragment;
    private StudyVideoSeriesFragment studyVideoSeriesFragment;
    private StudyChouseFragment studyChouseFragment;
    private Map<String,String> param;
    private UpdateReceiver receiver;
    private RadioButton radioButton;
    @Override
    public int getLayoutResId() {
        return R.layout.study_fragment_layout;
    }

    @Override
    public void initView() {
        initTitle("学院");
        //初始化控件
        video_select_radio=getView(R.id.video_select_radio);
        radioButton=getView(R.id.all_video);
        video_select_radio.setOnCheckedChangeListener(this);
    }
    private void setDefaultFragment(){
        if (studyChouseFragment == null) {
            studyChouseFragment=StudyChouseFragment.newInstance();
            studyChouseFragment.setGroupRadioGone(this);
            studyChouseFragment.setOnMoreVideo(new StudyChouseFragment.onMoreVideo() {
                @Override
                public void onMoreVideo() {
                    LogUtil.e("点击更多按钮");
                    if(video_select_radio.getCheckedRadioButtonId()!=R.id.all_video){
                        radioButton.toggle();
                    }
                }
            });
        }
        if (studyVideoFragment == null) {
            studyVideoFragment = StudyVideoFragment.newInstance();
            studyVideoFragment.setGroupRadioGone(this);
        }
        if (studyVideoSeriesFragment == null) {
            studyVideoSeriesFragment = StudyVideoSeriesFragment.newInstance();
        }
    }
    public void setStudyVideoFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_right_in,R.anim.slide_left_out);
        LogUtil.e("是否加载"+studyVideoFragment.isAdded());
        if(studyVideoFragment.isAdded()){
            fragmentTransaction.hide(studyVideoSeriesFragment).hide(studyChouseFragment).show(studyVideoFragment).commit();
            studyVideoFragment.rushVideo(true,param);
        }else{
            fragmentTransaction.add(R.id.study_framelayout,studyVideoFragment).commit();
        }
    }
    public void setVideoSeriesFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_right_in,R.anim.slide_left_out);
        if(studyVideoSeriesFragment.isAdded()){
            fragmentTransaction.hide(studyChouseFragment).hide(studyVideoFragment).show(studyVideoSeriesFragment).commit();
        }else{
            fragmentTransaction.add(R.id.study_framelayout, studyVideoSeriesFragment).commit();
        }
    }
    public void setChouseFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_right_in,R.anim.slide_left_out);
        if(studyChouseFragment.isAdded()){
            fragmentTransaction.hide(studyVideoSeriesFragment).hide(studyVideoFragment).show(studyChouseFragment).commit();
        }else{
            fragmentTransaction.add(R.id.study_framelayout, studyChouseFragment).commit();
        }
    }
    @Override
    public void initData() {
        //初始化数据
        if(param==null){
            param=new HashMap<>();
        }else{
            param.clear();
        }
        setDefaultFragment();
        setChouseFragment();
        receiver = new UpdateReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.UPDATE_VIDEO_LIST);
        registerReceiver(receiver, filter);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //结束服务
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
    }
    @Override
    public void addListener() {

    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId){
            case R.id.live_video:
                setChouseFragment();
                break;
            case R.id.all_video:
                param.clear();
                setStudyVideoFragment();
                break;
            case R.id.video_you:
                param.put("discount",50+"");
                setStudyVideoFragment();
                break;
            case R.id.time_you:
                param.put("discount",0+"");
                setStudyVideoFragment();
                break;
            case R.id.series_video:
                setVideoSeriesFragment();
                break;
        }
    }
    @Override
    public void onGone() {
        if(video_select_radio.getVisibility()==View.VISIBLE){
            video_select_radio.setVisibility(View.GONE);
        }
    }

    @Override
    public void onVisible() {
        if(video_select_radio.getVisibility()==View.GONE){
            video_select_radio.setVisibility(View.VISIBLE);
        }
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        onGone();
//    }
//
//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        onVisible();
//    }

    private class UpdateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case Constants.UPDATE_VIDEO_LIST:
                    if(studyVideoFragment!=null&&studyVideoFragment.isAdded()){
                        studyVideoFragment.rushVideo(true,param);
                    }
                    if(studyVideoSeriesFragment!=null&&studyVideoSeriesFragment.isAdded()){
                        studyVideoSeriesFragment.rushMessage();
                    }
                    if(studyChouseFragment!=null&&studyChouseFragment.isAdded()){
                        studyChouseFragment.OnRushMessage();
                    }
                    break;
            }
        }
    }
}
