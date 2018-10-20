package com.zbmf.StockGroup.fragment.video;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bokecc.sdk.mobile.live.DWLive;
import com.bokecc.sdk.mobile.live.DWLiveListener;
import com.bokecc.sdk.mobile.live.DWLivePlayer;
import com.bokecc.sdk.mobile.live.Exception.DWLiveException;
import com.bokecc.sdk.mobile.live.pojo.Answer;
import com.bokecc.sdk.mobile.live.pojo.ChatMessage;
import com.bokecc.sdk.mobile.live.pojo.PrivateChatInfo;
import com.bokecc.sdk.mobile.live.pojo.QualityInfo;
import com.bokecc.sdk.mobile.live.pojo.Question;
import com.bokecc.sdk.mobile.live.pojo.QuestionnaireInfo;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.activity.VideoPlayActivity;
import com.zbmf.StockGroup.beans.Video;
import com.zbmf.StockGroup.constans.IntentKey;
import com.zbmf.StockGroup.fragment.BaseFragment;
import com.zbmf.StockGroup.utils.ImageLoaderOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * Created by xuhao on 2017/7/11.
 */

public class LivePlayFragment extends BaseFragment implements TextureView.SurfaceTextureListener,
        IMediaPlayer.OnPreparedListener, IMediaPlayer.OnInfoListener,
        IMediaPlayer.OnVideoSizeChangedListener, View.OnClickListener , DWLiveListener {
    private View mRoot;
    private DWLivePlayer player;
    private DWLive dwLive ;
    private WindowManager wm;
    private Video video;
    TextureView mPlayerContainer;
    private ProgressBar pc_portrait_progressBar;
    private RelativeLayout rlPlay;
    private ImageView replay_play_icon;
    private ImageView replay_full_screen,backPlayList,video_img,iv_center_play;
    VideoPlayActivity activity;
    public static LivePlayFragment newInstance(Video video) {
        LivePlayFragment fragment = new LivePlayFragment();
        Bundle bundle=new Bundle();
        bundle.putSerializable(IntentKey.VIDEO_KEY,video);
        fragment.setArguments(bundle);
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        video= (Video) getArguments().getSerializable(IntentKey.VIDEO_KEY);
        activity= (VideoPlayActivity) getActivity();
    }

    @Override
    protected View setContentView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_live_layout,null);
    }
    public void setRlparam(){
        ViewGroup.LayoutParams layoutParams=rlPlay.getLayoutParams();
        if(activity.isPortrait()){
            layoutParams.height=  ViewGroup.LayoutParams.WRAP_CONTENT;
            layoutParams.width=  ViewGroup.LayoutParams.MATCH_PARENT;
            setReplay_full_screen(false);
        }else{
            layoutParams.height=  ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.width=  ViewGroup.LayoutParams.MATCH_PARENT;
            setReplay_full_screen(true);
        }
        rlPlay.setLayoutParams(layoutParams);
    }
    @Override
    protected void initView() {
        mPlayerContainer=getView(R.id.textureview_pc_live_play);
        pc_portrait_progressBar=getView(R.id.pc_portrait_progressBar);
        rlPlay=getView(R.id.rl_pc_live_top_layout);
        replay_play_icon=getView(R.id.replay_play_icon);
        replay_full_screen=getView(R.id.replay_full_screen);
        backPlayList=getView(R.id.backPlayList);
        video_img=getView(R.id.video_img);
        iv_center_play=getView(R.id.iv_center_play);
        getView(R.id.iv_top_menu).setOnClickListener(this);
//        ViewFactory.getImgView(getActivity(),video.getVideoImg(),video_img);
        ImageLoader.getInstance().displayImage(video.getVideoImg(),video_img, ImageLoaderOptions.BigProgressOptions());
        replay_play_icon.setOnClickListener(this);
        iv_center_play.setOnClickListener(this);
        replay_full_screen.setOnClickListener(this);
        backPlayList.setOnClickListener(this);
        replay_full_screen.setSelected(false);
        initSystem();
        initPlay();
    }

    @Override
    protected void initData() {


    }
    private void initPlay(){
        liveListener();
        dwLive= DWLive.getInstance();
        mPlayerContainer.setSurfaceTextureListener(this);
        player = new DWLivePlayer(activity);
        player.setOnPreparedListener(this);
        player.setOnInfoListener(this);
        player.setOnVideoSizeChangedListener(this);
        dwLive.setDWLivePlayParams(this, getContext(), null, player);
    }
    private void initSystem(){
        wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        mRoot =  activity.getWindow().getDecorView().findViewById(android.R.id.content);
        // 屏幕常亮
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
    private void liveListener(){
    }
    Surface surface;
    boolean isOnResumeStart = false;
    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
        surface = new Surface(surfaceTexture);
        if (isOnResumeStart) {
            return;
        }
        dwLive.start(surface);
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        surface = null;
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }
    boolean isPrepared = false;
    @Override
    public void onPrepared(IMediaPlayer iMediaPlayer) {
        isPrepared = true;
        player.start();
    }

    Runnable r;
    Handler handler = new Handler(Looper.getMainLooper());

    @Override
    public boolean onInfo(IMediaPlayer iMediaPlayer, int arg1, int i1) {
        if (arg1 == IMediaPlayer.MEDIA_INFO_BUFFERING_START) { // 开始缓冲
            r = new Runnable() {
                @Override
                public void run() {
                    dwLive.stop();
                    dwLive.start(surface);
                }
            };
            handler.postDelayed(r, 10 * 1000); // 延时定时器，此处设置的是10s，可自行设置
        } else if(arg1 == IMediaPlayer.MEDIA_INFO_BUFFERING_END) {
            if (r != null) {
                handler.removeCallbacks(r); // 如果收到了缓冲结束，那么取消延时定时器
            }
        }
        return false;
    }

    @Override
    public void onVideoSizeChanged(IMediaPlayer iMediaPlayer, int width, int height, int i2, int i3) {
        if (width == 0 || height == 0) {
            return;
        }
//        mPlayerContainer.setLayoutParams(getVideoSizeParams());
    }
    public void onLiveDestroy() {
        if (player != null) {
            player.pause();
            player.stop();
            player.release();
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.replay_play_icon:
                initPlay();
                break;
            case R.id.iv_center_play:
                initPlay();
                break;
            case R.id.replay_full_screen:
                //设置全屏
                if (activity.isPortrait()) {
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                } else {
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
                break;
            case R.id.backPlayList:
                if(activity.isPortrait()){
                    activity.finish();
                }else{
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
                break;
            case R.id.iv_top_menu:
                activity.showShare();
                break;
        }
    }
    private void setReplay_full_screen(boolean full_screen){
        replay_full_screen.setSelected(full_screen);
    }

    @Override
    public void onQuestion(Question question) {

    }

    @Override
    public void onPublishQuestion(String s) {

    }

    @Override
    public void onAnswer(Answer answer) {

    }

    @Override
    public void onLiveStatus(final DWLive.PlayStatus playStatus) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(pc_portrait_progressBar!=null){
                    pc_portrait_progressBar.setVisibility(View.GONE);
                }
                switch (playStatus) {
                    case PLAYING:
                        activity.setLiveType("正在直播...");
                        video_img.setVisibility(View.GONE);
                        iv_center_play.setVisibility(View.GONE);
                        replay_play_icon.setVisibility(View.GONE);
                        replay_play_icon.setSelected(true);
                        break;
                    case PREPARING:
                        iv_center_play.setVisibility(View.VISIBLE);
                        replay_play_icon.setSelected(false);
                        activity.setLiveType("直播未开始...");
                        break;
                }
            }
        });
    }

    @Override
    public void onStreamEnd(boolean b) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                player.pause();
                player.stop();
                player.reset();
                iv_center_play.setVisibility(View.GONE);
                activity.setLiveType("直播已结束！");
            }
        });

    }

    @Override
    public void onHistoryChatMessage(ArrayList<ChatMessage> arrayList) {

    }

    @Override
    public void onPublicChatMessage(ChatMessage chatMessage) {

    }

    @Override
    public void onSilenceUserChatMessage(ChatMessage chatMessage) {

    }

    @Override
    public void onPrivateQuestionChatMessage(ChatMessage chatMessage) {

    }

    @Override
    public void onPrivateAnswerChatMessage(ChatMessage chatMessage) {

    }

    @Override
    public void onPrivateChat(PrivateChatInfo privateChatInfo) {

    }

    @Override
    public void onPrivateChatSelf(PrivateChatInfo privateChatInfo) {

    }

    @Override
    public void onUserCountMessage(int i) {

    }

    @Override
    public void onNotification(String s) {

    }

    @Override
    public void onBroadcastMsg(String s) {

    }

    @Override
    public void onInformation(String s) {

    }

    @Override
    public void onException(DWLiveException e) {

    }

    @Override
    public void onInitFinished(int i, List<QualityInfo> list) {

    }

    @Override
    public void onKickOut() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(),"被踢出直播室",Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onLivePlayedTime(int i) {

    }

    @Override
    public void onLivePlayedTimeException(Exception e) {

    }

    @Override
    public void isPlayedBack(boolean b) {

    }

    @Override
    public void onStatisticsParams(Map<String, String> map) {

    }

    @Override
    public void onCustomMessage(String s) {

    }

    @Override
    public void onBanStream(String s) {

    }

    @Override
    public void onUnbanStream() {

    }

    @Override
    public void onAnnouncement(boolean b, String s) {

    }

    @Override
    public void onRollCall(int i) {

    }

    @Override
    public void onStartLottery(String s) {

    }

    @Override
    public void onLotteryResult(boolean b, String s, String s1, String s2) {

    }

    @Override
    public void onStopLottery(String s) {

    }

    @Override
    public void onVoteStart(int i, int i1) {

    }

    @Override
    public void onVoteStop() {

    }

    @Override
    public void onVoteResult(JSONObject jsonObject) {

    }

    @Override
    public void onQuestionnairePublish(QuestionnaireInfo questionnaireInfo) {

    }

    @Override
    public void onQuestionnaireStop(String s) {

    }
}
