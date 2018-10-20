package com.zbmf.StockGroup.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.bokecc.sdk.mobile.live.DWLive;
import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.api.JSONHandler;
import com.zbmf.StockGroup.api.WebBase;
import com.zbmf.StockGroup.beans.ShareBean;
import com.zbmf.StockGroup.beans.Video;
import com.zbmf.StockGroup.beans.VideoPrice;
import com.zbmf.StockGroup.constans.IntentKey;
import com.zbmf.StockGroup.constans.RequestCode;
import com.zbmf.StockGroup.dialog.VideoDialog;
import com.zbmf.StockGroup.fragment.video.LivePlayFragment;
import com.zbmf.StockGroup.fragment.video.VideoChatFragment;
import com.zbmf.StockGroup.fragment.video.VideoFragment;
import com.zbmf.StockGroup.fragment.video.VideoMobileFragment;
import com.zbmf.StockGroup.fragment.video.VideoDesclFragment;
import com.zbmf.StockGroup.constans.Constants;
import com.zbmf.StockGroup.utils.DateUtil;
import com.zbmf.StockGroup.utils.DisplayUtil;
import com.zbmf.StockGroup.utils.JSONParse;
import com.zbmf.StockGroup.utils.NetWorkUtil;
import com.zbmf.StockGroup.utils.ShowActivity;
import com.zbmf.StockGroup.utils.ShowWebShareLayout;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import static com.zbmf.StockGroup.dialog.VideoDialog.COMMIT;

/**
 * Created by xuhao on 2017/6/29.
 */

public class VideoPlayActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private Video video;
    private VideoFragment play_fragment;
    private LivePlayFragment livePlayFragment;
    private VideoDesclFragment desclFragment;
    private VideoChatFragment videoChatFragment;
    private VideoMobileFragment videoMobileFragment;
    private RadioGroup video_radio;
    private Button submit_video;
    private FrameLayout videoplay_framelayout;
    private LinearLayout time_layout;
    private boolean order;
    Timer timer;
    TimerTask task;
    private TextView tv_video_time, video_new_price, video_price;
    private ShowWebShareLayout showWebShareLayout;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (showWebShareLayout == null) {
            showWebShareLayout = new ShowWebShareLayout(this);
        }
        showWebShareLayout.onNewIntent(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.BLACK);
        }
        initShare();
        initview();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            video = (Video) bundle.getSerializable(IntentKey.VIDEO_KEY);
            initFragment();
        }
    }

    public void changeVideo(Video videos) {
        if (videos != null) {
            video = videos;
            FragmentManager fm = getSupportFragmentManager();
            if(play_fragment!=null){
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.remove(play_fragment);
                transaction.commit();
                play_fragment.onPlayDestroy();
                play_fragment=null;
            }
            if(livePlayFragment!=null){
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.remove(livePlayFragment);
                transaction.commit();
                livePlayFragment.onLiveDestroy();
                livePlayFragment=null;
            }
            if(desclFragment!=null){
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.remove(desclFragment);
                transaction.commit();
                desclFragment=null;
            }
            if(videoChatFragment!=null){
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.remove(videoChatFragment);
                transaction.commit();
                videoChatFragment=null;
            }
            if(videoMobileFragment!=null){
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.remove(videoMobileFragment);
                transaction.commit();
                videoMobileFragment=null;
            }
            initFragment();
        }
    }

    private void initFragment() {
        order = video.getOrder();
        setVideoTextView();
        getVideoPrice(false);
        setDefaultFragment();
        setPlayFragment();
    }

    public void setVideoTextView() {

        if (video.getIs_live()) {
            initTimer();
            submit_video.setVisibility(View.GONE);
            time_layout.setVisibility(View.VISIBLE);
            video_radio.setVisibility(View.VISIBLE);
        } else {
            if (!video.getOrder()) {
                submit_video.setVisibility(View.VISIBLE);
            } else {
                submit_video.setVisibility(View.GONE);
            }
            time_layout.setVisibility(View.GONE);
            video_radio.setVisibility(View.GONE);
        }
        if (video.getVideoPriceType() != 100) {
            video_price.setVisibility(View.VISIBLE);
            video_price.getPaint().setAntiAlias(true);//抗锯齿
            video_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            if (video.getVideonewPrice() == 0) {
                video_new_price.setText("限时免费");
            } else {
                video_new_price.setText(getDoubleormat(video.getVideonewPrice()));
            }
            video_price.setText(getDoubleormat(video.getVideoPrice()));
        } else {
            video_new_price.setVisibility(View.GONE);
            video_price.setText(getDoubleormat(video.getVideoPrice()));
        }
    }

    public void setPlayFragment() {
        if (video.getIs_live() && !video.getOrder()) {
            setMobile_fragment(1);
        } else {
            if (NetWorkUtil.isWifi(getBaseContext())) {
                setPlayFragment(0);
            } else {
                setMobile_fragment(0);
            }
        }
    }

    public void setPlayFragment(int commit) {
        if (commit == 1) {
            if (ShowActivity.isLogin(VideoPlayActivity.this)) {
                PayVideoNews();
            }
        } else {
            if (video.getIs_live()) {
                setLivePlayFragment();
            } else {
                setPlay_fragment();
            }
        }
    }

    public void setLiveType(String message) {
        tv_video_time.setText(message);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        ViewGroup.LayoutParams layoutParams = videoplay_framelayout.getLayoutParams();
        if (isPortrait()) {
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = DisplayUtil.dip2px(getBaseContext(), 220);
        } else {
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        }
        if (videoplay_framelayout != null) {
            videoplay_framelayout.setLayoutParams(layoutParams);
        }
        if (play_fragment != null) {
            play_fragment.setRelaySize();
        }
        if (videoMobileFragment != null) {
            videoMobileFragment.setRelaySize(isPortrait());
        }
        if (livePlayFragment != null) {
            livePlayFragment.setRlparam();
        }
    }

    private void initview() {
        video_radio = (RadioGroup) findViewById(R.id.radio_video);
        submit_video = (Button) findViewById(R.id.submit_video);
        videoplay_framelayout = (FrameLayout) findViewById(R.id.videoplay_framelayout);
        tv_video_time = (TextView) findViewById(R.id.tv_video_time);
        video_price = (TextView) findViewById(R.id.video_price);
        video_new_price = (TextView) findViewById(R.id.video_new_price);
        time_layout = (LinearLayout) findViewById(R.id.time_layout);
        submit_video.setOnClickListener(this);
        video_radio.setOnCheckedChangeListener(this);
    }

    private void setPlay_fragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        if (play_fragment == null) {
            play_fragment = VideoFragment.newInstance(video);
        } else {
            play_fragment.RushVideo(video);
        }
        transaction.replace(R.id.videoplay_framelayout, play_fragment);
        transaction.commit();
    }

    private void setMobile_fragment(int commit) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        if (videoMobileFragment == null) {
            videoMobileFragment = VideoMobileFragment.newInstance(video, commit);
        } else {
            videoMobileFragment.RushMessage(video, commit);
        }
        transaction.replace(R.id.videoplay_framelayout, videoMobileFragment);
        transaction.commit();
    }

    private void setLivePlayFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        if (livePlayFragment == null) {
            livePlayFragment = LivePlayFragment.newInstance(video);
        }
        transaction.replace(R.id.videoplay_framelayout, livePlayFragment);
        transaction.commit();
    }

    public void setDefaultFragment() {
        if (desclFragment == null) {
            desclFragment = VideoDesclFragment.newInstance(video);
        }
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.video_framelayout, desclFragment);
        transaction.commit();
    }

    public void setChatFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        if (videoChatFragment == null) {
            videoChatFragment = VideoChatFragment.newInstance(video);
        }
        transaction.replace(R.id.video_framelayout, videoChatFragment);
        transaction.commit();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (play_fragment != null) {
            play_fragment.onPlayStop();
        }
    }

    public void initTimer() {
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Date date = new Date();
                        long nowtime = date.getTime() / 1000;
                        int seconds = (int) (video.getStart_time() - nowtime);
                        if (seconds <= 0) {
                            timer.cancel();
                        } else {
                            String time_message = DateUtil.getVedioStartTime(seconds);
                            tv_video_time.setText("倒计时：" + time_message);
                        }
                    }
                });
            }
        };
        timer.schedule(task, 1000, 1000);
    }

    public void initShare() {
        if (showWebShareLayout == null) {
            showWebShareLayout = new ShowWebShareLayout(this);
        }
    }

    public void showShare() {
        showWebShareLayout.showShareLayout(
                new ShareBean(
                        getString(R.string.share_video).replace("[*]", video.getVideoGroupname()).replace("[**]", video.getVideoName())
                        , video.getShare_img(), video.getShare_url(), getString(R.string.share_video_desc)));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (play_fragment != null) {
            play_fragment.onPlayDestroy();
        }
        if (livePlayFragment != null) {
            DWLive.getInstance().onDestroy();
            livePlayFragment.onLiveDestroy();
        }
    }


    @Override
    public void onBackPressed() {
        if (isPortrait()) {
            super.onBackPressed();
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    // 获得当前屏幕的方向
    public boolean isPortrait() {
        int mOrientation = getBaseContext().getResources().getConfiguration().orientation;
        if (mOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit_video:
                if (ShowActivity.isLogin(VideoPlayActivity.this)) {
                    PayVideoNews();
                }
                break;
        }
    }

    public void getVideoPrice(final boolean show) {
        WebBase.PayVideoNews(video.getVideoId(), new JSONHandler(true, VideoPlayActivity.this, getString(R.string.loading)) {
            @Override
            public void onSuccess(JSONObject obj) {
                VideoPrice vp = JSONParse.getVideoPrice(obj);
                video.setVideoPriceBean(vp);
                if (show) {
                    PayVideoNews();
                }
            }

            @Override
            public void onFailure(String err_msg) {
                Toast.makeText(getBaseContext(), err_msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void PayVideoNews() {
        if (video.getVideoPriceBean() != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(IntentKey.VIDEO_KEY, video);
            ShowActivity.showActivityForResult(VideoPlayActivity.this, bundle, PayVideoNewsActivity.class, RequestCode.COMIT_VIDEO);
        } else {
            getVideoPrice(true);
        }
    }

    private VideoDialog dialog;

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId) {
            case R.id.rb_desc:
                //简介
                setDefaultFragment();
                break;
            case R.id.rb_chat:
                //聊天
                if (ShowActivity.isLogin(VideoPlayActivity.this)) {
                    if (order) {
                        setChatFragment();
                    } else {
                        if (dialog == null) {
                            dialog = VideoDialog.CreateDialog(VideoPlayActivity.this)
                                    .setMessage(getString(R.string.live_dialog_chat))
                                    .setOnCommitClick(new VideoDialog.OnCommitClick() {
                                        @Override
                                        public void onComint(int flag) {
                                            if (flag == COMMIT) {
                                                PayVideoNews();
                                            } else {
                                                dialog.dismiss();
                                            }
                                        }
                                    });
                        }
                        dialog.show();
                        video_radio.check(R.id.rb_desc);
                    }
                } else {
                    video_radio.check(R.id.rb_desc);
                }

                break;
        }
    }

    DecimalFormat df = new DecimalFormat("");
    DecimalFormat double_df = new DecimalFormat("######0.00");

    public String getDoubleormat(double vealue) {
        if (vealue == 0) {
            return "限时免费";
        }
        if (double_df.format(vealue).contains(".00")) {
            double ve = Double.valueOf(double_df.format(vealue));
            return df.format(ve);
        } else {
            return double_df.format(vealue);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RequestCode.COMIT_VIDEO:
                    getVideoContent();
                    Intent intent = new Intent(Constants.UPDATE_VIDEO_LIST);
                    sendBroadcast(intent);
                    desclFragment.onRush();
                    break;
                case RequestCode.VIDEO_PLAY:
                    Bundle bundle = data.getExtras();
                    video = (Video) bundle.getSerializable(IntentKey.VIDEO_KEY);
                    changeVideo(video);
                    break;
            }
        }

    }

    private void getVideoContent() {
        WebBase.GetVideoDetail(video.getVideoId(), new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                JSONObject video_obj = obj.optJSONObject("video");
                video = JSONParse.getVideo(video_obj);
                order = video.getOrder();
                setVideoTextView();
                setPlayFragment();
            }

            @Override
            public void onFailure(String err_msg) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatService.onResume(this);
        if (play_fragment != null) {
            play_fragment.onPlayResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(this);
        if (task != null) {
            task.cancel();
            task = null;
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (play_fragment != null) {
            play_fragment.onPlayPause();
        }
    }
}
