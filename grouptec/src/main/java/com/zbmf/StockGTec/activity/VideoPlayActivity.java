package com.zbmf.StockGTec.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bokecc.sdk.mobile.play.DWMediaPlayer;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.zbmf.StockGTec.R;
import com.zbmf.StockGTec.beans.Videos;
import com.zbmf.StockGTec.fragment.VideoInfoFragment;
import com.zbmf.StockGTec.utils.Constants;
import com.zbmf.StockGTec.utils.DisplayUtil;
import com.zbmf.StockGTec.utils.ParamsUtil;
import com.zbmf.StockGTec.utils.SettingDefaultsManager;
import com.zbmf.StockGTec.utils.ShareUtil;
import com.zbmf.StockGTec.utils.ShowActivity;
import com.zbmf.StockGTec.view.PopMenu;

import java.io.IOException;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class VideoPlayActivity extends ExActivity
        implements SurfaceHolder.Callback, MediaPlayer.OnVideoSizeChangedListener, DWMediaPlayer.OnBufferingUpdateListener,
        DWMediaPlayer.OnPreparedListener,
        DWMediaPlayer.OnInfoListener, DWMediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {

    private DWMediaPlayer player;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private ProgressBar bufferProgressBar;
    private SeekBar skbProgress;
    private ImageView backPlayList;
    private TextView videoIdText, playDuration, videoDuration;
    private TextView tvDefinition;
    private LinearLayout playerTopLayout, volumeLayout;
    private LinearLayout playerBottomLayout;
    private AudioManager audioManager;
    private int currentVolume;
    private int maxVolume;

    private RelativeLayout rlBelow, rlPlay;
    private boolean isPrepared;
    private GestureDetector detector;
    private float scrollTotalDistance;

    private WindowManager wm;
    ConnectivityManager cm;

    private ImageView ivFullscreen;
    private Videos mVideos;
    private String description = "上资本魔方炒股圈子，学技术，看直播，炒股其实很简单！";
    private String title = "，一起来看吧";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.BLACK);
        }

        detector = new GestureDetector(this, new MyGesture());
        wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        mVideos = (Videos) getIntent().getSerializableExtra("video");
        videoId = mVideos.getBokecc_id();
        title = "【" + SettingDefaultsManager.getInstance().getGroupName() + "】" + mVideos.getTitle() + title;
        initView();
        initPlayHander();
        initPlayInfo();
        initNetworkTimerTask();
    }

    ImageView ivTopMenu, iv_background, ivPlay, ivCenterPlay, lockView;
    private ShareUtil shareUtil;

    private void initView() {

        rlPlay = (RelativeLayout) findViewById(R.id.rl_play);

        rlPlay.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!isPrepared) {
                    return true;
                }

                resetHideDelayed();

                // 事件监听交给手势类来处理
                detector.onTouchEvent(event);
                return true;
            }
        });

        rlPlay.setClickable(true);
        rlPlay.setLongClickable(true);
        rlPlay.setFocusable(true);

        ivTopMenu = (ImageView) findViewById(R.id.iv_top_menu);
        iv_background = (ImageView) findViewById(R.id.iv_background);
        ivTopMenu.setOnClickListener(onClickListener);

        surfaceView = (SurfaceView) findViewById(R.id.playerSurfaceView);
        bufferProgressBar = (ProgressBar) findViewById(R.id.bufferProgressBar);

        ivCenterPlay = (ImageView) findViewById(R.id.iv_center_play);
        ivCenterPlay.setOnClickListener(onClickListener);

        backPlayList = (ImageView) findViewById(R.id.backPlayList);
        videoIdText = (TextView) findViewById(R.id.videoIdText);

        playDuration = (TextView) findViewById(R.id.playDuration);
        videoDuration = (TextView) findViewById(R.id.videoDuration);
        playDuration.setText(ParamsUtil.millsecondsToStr(0));
        videoDuration.setText(ParamsUtil.millsecondsToStr(0));

        ivPlay = (ImageView) findViewById(R.id.iv_play);
        ivPlay.setOnClickListener(onClickListener);
        tvDefinition = (TextView) findViewById(R.id.tv_definition);

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        skbProgress = (SeekBar) findViewById(R.id.skbProgress);
        skbProgress.setOnSeekBarChangeListener(onSeekBarChangeListener);

        playerTopLayout = (LinearLayout) findViewById(R.id.playerTopLayout);
        volumeLayout = (LinearLayout) findViewById(R.id.volumeLayout);
        playerBottomLayout = (LinearLayout) findViewById(R.id.playerBottomLayout);

        ivFullscreen = (ImageView) findViewById(R.id.iv_fullscreen);

        ivFullscreen.setOnClickListener(onClickListener);
        backPlayList.setOnClickListener(onClickListener);
        tvDefinition.setOnClickListener(onClickListener);

        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS); //2.3及以下使用，不然出现只有声音没有图像的问题
        surfaceHolder.setFormat(PixelFormat.TRANSLUCENT);
        surfaceHolder.addCallback(this);

        lockView = (ImageView) findViewById(R.id.iv_lock);
        lockView.setSelected(false);
        lockView.setOnClickListener(onClickListener);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        VideoInfoFragment videoInfoFragment = VideoInfoFragment.newInstance(mVideos);
        fragmentTransaction.add(R.id.content, videoInfoFragment, "videoInfoFragment").commit();
        videoIdText.setText(mVideos.getTitle());
    }


    private Timer timer = new Timer();
    private TimerTask timerTask, networkInfoTimerTask;
    private NetworkStatus currentNetworkStatus;
    private boolean networkConnected = true;

    enum NetworkStatus {
        WIFI,
        MOBILEWEB,
        NETLESS,
    }

    private void initNetworkTimerTask() {
        networkInfoTimerTask = new TimerTask() {
            @Override
            public void run() {
                parseNetworkInfo();
            }
        };

        timer.schedule(networkInfoTimerTask, 0, 600);
    }

    @Override
    public void onBackPressed() {
        if (isPortrait()) {
            super.onBackPressed();
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    private void parseNetworkInfo() {
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isAvailable()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                if (currentNetworkStatus != null && currentNetworkStatus == NetworkStatus.WIFI) {
                    return;
                } else {
                    currentNetworkStatus = NetworkStatus.WIFI;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            Toast.makeText(getApplicationContext(), "已切换至wifi", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            } else {
                if (currentNetworkStatus != null && currentNetworkStatus == NetworkStatus.MOBILEWEB) {
                    return;
                } else {
                    currentNetworkStatus = NetworkStatus.MOBILEWEB;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(VideoPlayActivity.this);
                            AlertDialog dialog = builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    finish();
                                }
                            }).setPositiveButton("继续", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).setMessage("当前为移动网络，是否继续播放？").create();

                            dialog.show();
                        }
                    });
                }
            }

            initTimerTask();
            networkConnected = true;
        } else {
            if (currentNetworkStatus != null && currentNetworkStatus == NetworkStatus.NETLESS) {
                return;
            } else {
                currentNetworkStatus = NetworkStatus.NETLESS;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "当前无网络信号，无法播放", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            cancelTimerTask();

            networkConnected = false;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) rlPlay.getLayoutParams();
        if (isPortrait()) {
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = DisplayUtil.dip2px(getBaseContext(), 220);
        } else {
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        }
        rlPlay.setLayoutParams(params);
    }

    private void initTimerTask() {
        cancelTimerTask();
        timerTask = new TimerTask() {
            @Override
            public void run() {

                if (!isPrepared) {
                    return;
                }

                playerHandler.sendEmptyMessage(0);
            }
        };
        timer.schedule(timerTask, 0, 1000);
    }

    private void cancelTimerTask() {
        if (timerTask != null) {
            timerTask.cancel();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isFreeze) {
            isFreeze = false;
            if (isPrepared) {
                player.start();
            }
        } else {
            if (isPlaying != null && isPlaying.booleanValue() && isPrepared) {
                player.start();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isPrepared) {
            // 如果播放器prepare完成，则对播放器进行暂停操作，并记录状态
            if (player.isPlaying()) {
                isPlaying = true;
            } else {
                isPlaying = false;
            }
            player.pause();
        } else {
            // 如果播放器没有prepare完成，则设置isFreeze为true
            isFreeze = true;
        }
    }

    private Handler playerHandler;
    private int lastPlayPosition, currentPlayPosition;

    private void initPlayHander() {
        playerHandler = new Handler() {
            public void handleMessage(Message msg) {
                if (player == null) {
                    return;
                }
                // 刷新字幕
//                subtitleText.setText(subtitle.getSubtitleByTime(player.getCurrentPosition()));

                // 更新播放进度
                currentPlayPosition = player.getCurrentPosition();
                int duration = player.getDuration();

                if (duration > 0) {
                    long pos = skbProgress.getMax() * currentPlayPosition / duration;
                    playDuration.setText(ParamsUtil.millsecondsToStr(player.getCurrentPosition()));
                    skbProgress.setProgress((int) pos);
                }

            }
        };
    }


    // 默认设置为普清
    private int defaultDefinition = DWMediaPlayer.NORMAL_DEFINITION;
    private String videoId;
    private Dialog dialog = null;

    private void initPlayInfo() {

        // 通过定时器和Handler来更新进度
        isPrepared = false;
        player = new DWMediaPlayer();
        player.reset();
        player.setOnErrorListener(this);
        player.setOnCompletionListener(this);
        player.setOnVideoSizeChangedListener(this);
        player.setOnInfoListener(this);

        try {
            player.setVideoPlayInfo(videoId, Constants.CC_USERID, Constants.CC_KEY, this);
            // 设置默认清晰度
            player.setDefaultDefinition(defaultDefinition);
        } catch (IllegalArgumentException e) {
            Log.e("player error", e.getMessage());
        } catch (SecurityException e) {
            Log.e("player error", e.getMessage());
        } catch (IllegalStateException e) {
            Log.e("player error", e + "");
        }
    }

    // 隐藏界面的线程
    private Runnable hidePlayRunnable = new Runnable() {
        @Override
        public void run() {
            setLayoutVisibility(View.GONE, false);
        }
    };

    // 重置隐藏界面组件的延迟时间
    private void resetHideDelayed() {
        playerHandler.removeCallbacks(hidePlayRunnable);
        playerHandler.postDelayed(hidePlayRunnable, 5000);
    }

    SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        int progress = 0;

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            if (networkConnected/* || isLocalPlay*/) {
                player.seekTo(progress);
                playerHandler.postDelayed(hidePlayRunnable, 5000);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            playerHandler.removeCallbacks(hidePlayRunnable);
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (networkConnected /*|| isLocalPlay*/) {
                this.progress = progress * player.getDuration() / seekBar.getMax();
            }
        }
    };

    View.OnClickListener onClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            resetHideDelayed();

            switch (v.getId()) {
                case R.id.group_title_return:
                    finish();
                    break;
                case R.id.backPlayList:
                    if (isPortrait()) {
                        finish();
                    } else {
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    }
                    break;
                case R.id.iv_fullscreen:
                    if (isPortrait()) {
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    } else {
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    }
                    break;
                case R.id.tv_definition:
                    break;
                case R.id.iv_lock:
                    if (lockView.isSelected()) {
                        lockView.setSelected(false);
                        setLayoutVisibility(View.VISIBLE, true);
                    } else {
                        lockView.setSelected(true);
                        setLandScapeRequestOrientation();
                        setLayoutVisibility(View.GONE, true);
                        lockView.setVisibility(View.VISIBLE);
                    }
                    break;
                case R.id.iv_center_play:
                case R.id.iv_play:
                    changePlayStatus();
                    break;
                case R.id.iv_top_menu:
                    shareUtil = new ShareUtil(VideoPlayActivity.this, title, description, mVideos.getShare_img(), mVideos.getShare_url());
                    if (dialog == null)
                        dialog = showShare();
                    dialog.show();
                    break;
                case R.id.ll_w://微信
                    shareUtil.shareToWebchat(SendMessageToWX.Req.WXSceneSession);
                    dialog.dismiss();
                    break;
                case R.id.ll_p://朋友圈
                    shareUtil.shareToWebchat(SendMessageToWX.Req.WXSceneTimeline);
                    dialog.dismiss();
                    break;
                case R.id.ll_q://QQ
                    shareUtil.shareToQQ();
                    dialog.dismiss();
                    break;
                case R.id.ll_x://新浪
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("videos", mVideos);
                    ShowActivity.startActivity(VideoPlayActivity.this, bundle, EmptyActivity.class.getName());
                    dialog.dismiss();
                    break;
            }
        }
    };

    // 设置横屏的固定方向，禁用掉重力感应方向
    private void setLandScapeRequestOrientation() {
        int rotation = wm.getDefaultDisplay().getRotation();
        // 旋转90°为横屏正向，旋转270°为横屏逆向
        if (rotation == Surface.ROTATION_90) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else if (rotation == Surface.ROTATION_270) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
        }
    }

    // 控制播放器面板显示
    private boolean isDisplay = true;
    int currentPosition;
    // 当player未准备好，并且当前activity经过onPause()生命周期时，此值为true
    private boolean isFreeze = false;
    private Boolean isPlaying;
    private Map<String, Integer> definitionMap;
    private int currentDefinitionIndex = 0;
    private PopMenu definitionMenu;
    private String[] definitionArray;


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            player.reset();
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.setOnBufferingUpdateListener(this);
            player.setOnPreparedListener(this);
            player.setDisplay(holder);
            player.setScreenOnWhilePlaying(true);

            player.setHttpsPlay(false);
            player.prepareAsync();
        } catch (Exception e) {
            Log.e("videoPlayer", "error", e);
        }
        Log.i("videoPlayer", "surface created");
    }


    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        skbProgress.setSecondaryProgress(percent);
    }


    @Override
    public void onPrepared(MediaPlayer mp) {
        initTimerTask();
        isPrepared = true;
        if (!isFreeze) {
            if (isPlaying == null || isPlaying.booleanValue()) {
                player.start();
                ivPlay.setImageResource(R.mipmap.smallstop_ic);
            }
        }

        if (currentPosition > 0) {
            player.seekTo(currentPosition);
        }
        definitionMap = player.getDefinitions();
        initDefinitionPopMenu();

        iv_background.setVisibility(View.GONE);
        bufferProgressBar.setVisibility(View.GONE);
        setSurfaceViewLayout();
        videoDuration.setText(ParamsUtil.millsecondsToStr(player.getDuration()));
    }

    private void initDefinitionPopMenu() {
        if (definitionMap.size() > 1) {
            currentDefinitionIndex = 1;
            Integer[] definitions = new Integer[]{};
            definitions = definitionMap.values().toArray(definitions);
            // 设置默认为普清，所以此处需要判断一下
            for (int i = 0; i < definitions.length; i++) {
                if (definitions[i].intValue() == defaultDefinition) {
                    currentDefinitionIndex = i;
                }
            }
        }

        definitionMenu = new PopMenu(this, R.mipmap.popdown, currentDefinitionIndex, getResources().getDimensionPixelSize(R.dimen.popmenu_height));
        // 设置清晰度列表
        definitionArray = new String[]{};
        definitionArray = definitionMap.keySet().toArray(definitionArray);

        definitionMenu.addItems(definitionArray);
        definitionMenu.setOnItemClickListener(new PopMenu.OnItemClickListener() {

            @Override
            public void onItemClick(int position) {
                try {
                    currentDefinitionIndex = position;
                    defaultDefinition = definitionMap.get(definitionArray[position]);
                    if (isPrepared) {
                        currentPosition = player.getCurrentPosition();
                        if (player.isPlaying()) {
                            isPlaying = true;
                        } else {
                            isPlaying = false;
                        }
                    }
                    isPrepared = false;
                    setLayoutVisibility(View.GONE, false);
                    bufferProgressBar.setVisibility(View.VISIBLE);
                    player.reset();
                    player.setDefinition(getApplicationContext(), defaultDefinition);
                } catch (IOException e) {
                    Log.e("player error", e.getMessage());
                }

            }
        });
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        holder.setFixedSize(width, height);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (player == null) {
            return;
        }
        if (isPrepared) {
            currentPosition = player.getCurrentPosition();
        }

        isPrepared = false;
//        isSurfaceDestroy = true;

        player.stop();
        player.reset();
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        setSurfaceViewLayout();
    }

    private int currentScreenSizeFlag = 1;
    private final String[] screenSizeArray = new String[]{"满屏", "100%", "75%", "50%"};

    /**
     * 设置surfaceview布局参数
     * 1，初始化完成时
     * 2，屏幕大小改变的时候
     */
    private void setSurfaceViewLayout() {
        RelativeLayout.LayoutParams params = getScreenSizeParams(currentScreenSizeFlag);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        surfaceView.setLayoutParams(params);
    }

    private RelativeLayout.LayoutParams getScreenSizeParams(int position) {
        currentScreenSizeFlag = position;
        int width = 600;
        int height = 400;
        if (isPortrait()) {
            width = wm.getDefaultDisplay().getWidth();
            height = wm.getDefaultDisplay().getHeight() * 2 / 5;
        } else {
            width = wm.getDefaultDisplay().getWidth();
            height = wm.getDefaultDisplay().getHeight();
        }


        String screenSizeStr = screenSizeArray[position];
        if (screenSizeStr.indexOf("%") > 0) {// 按比例缩放
            int vWidth = player.getVideoWidth();
            if (vWidth == 0) {
                vWidth = 600;
            }

            int vHeight = player.getVideoHeight();
            if (vHeight == 0) {
                vHeight = 400;
            }

            if (vWidth > width || vHeight > height) {
                float wRatio = (float) vWidth / (float) width;
                float hRatio = (float) vHeight / (float) height;
                float ratio = Math.max(wRatio, hRatio);

                width = (int) Math.ceil((float) vWidth / ratio);
                height = (int) Math.ceil((float) vHeight / ratio);
            } else {
                float wRatio = (float) width / (float) vWidth;
                float hRatio = (float) height / (float) vHeight;
                float ratio = Math.min(wRatio, hRatio);

                width = (int) Math.ceil((float) vWidth * ratio);
                height = (int) Math.ceil((float) vHeight * ratio);
            }


            int screenSize = ParamsUtil.getInt(screenSizeStr.substring(0, screenSizeStr.indexOf("%")));
            width = (width * screenSize) / 100;
            height = (height * screenSize) / 100;
        }

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
        return params;
    }


    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        switch (what) {
            case DWMediaPlayer.MEDIA_INFO_BUFFERING_START:
                if (player.isPlaying()) {
                    bufferProgressBar.setVisibility(View.VISIBLE);
                }

                if (!isBackupPlay) {
                    playerHandler.postDelayed(backupPlayRunnable, 10 * 1000);
                }

                break;
            case DWMediaPlayer.MEDIA_INFO_BUFFERING_END:
                bufferProgressBar.setVisibility(View.GONE);
                playerHandler.removeCallbacks(backupPlayRunnable);
                break;
        }
        return false;
    }

    private Runnable backupPlayRunnable = new Runnable() {

        @Override
        public void run() {
            startBackupPlay();
        }
    };

    boolean isBackupPlay = false;
    private Handler alertHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
//            Toast.makeText(VideoPlayActivity.this, "视频异常，无法播放", Toast.LENGTH_SHORT).show();
            super.handleMessage(msg);
        }

    };

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Message msg = new Message();
        msg.what = what;

        if (!isBackupPlay) {
            startBackupPlay();
        } else {
            if (alertHandler != null) {
                alertHandler.sendMessage(msg);
            }
        }
        return false;
    }

    private void startBackupPlay() {
        cancelTimerTask();

        player.setBackupPlay(true);
        isBackupPlay = true;
        player.reset();
        player.prepareAsync();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (isPrepared) {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
//                    toastInfo("切换中，请稍候……");
                    currentPlayPosition = 0;
                    currentPosition = 0;
//                    changeToNextVideo(true);
                }
            });
        }
    }

    // 手势监听器类
    private class MyGesture extends GestureDetector.SimpleOnGestureListener {

        private Boolean isVideo;
        private float scrollCurrentPosition;
        private float scrollCurrentVolume;

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return super.onSingleTapUp(e);
        }

        @Override
        public void onLongPress(MotionEvent e) {
            super.onLongPress(e);
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (lockView.isSelected()) {
                return true;
            }
            if (isVideo == null) {
                if (Math.abs(distanceX) > Math.abs(distanceY)) {
                    isVideo = true;
                } else {
                    isVideo = false;
                }
            }

            if (isVideo.booleanValue()) {
                parseVideoScroll(distanceX);
            } else {
                parseAudioScroll(distanceY);
            }

            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        private void parseVideoScroll(float distanceX) {
            if (!isDisplay) {
                setLayoutVisibility(View.VISIBLE, true);
            }

            scrollTotalDistance += distanceX;

            float duration = (float) player.getDuration();

            float width = wm.getDefaultDisplay().getWidth() * 0.75f; // 设定总长度是多少，此处根据实际调整
            //右滑distanceX为负
            float currentPosition = scrollCurrentPosition - (float) duration * scrollTotalDistance / width;

            if (currentPosition < 0) {
                currentPosition = 0;
            } else if (currentPosition > duration) {
                currentPosition = duration;
            }

            player.seekTo((int) currentPosition);

            int pos = (int) (skbProgress.getMax() * currentPosition / duration);
            skbProgress.setProgress(pos);
        }

        private void parseAudioScroll(float distanceY) {
            if (!isDisplay) {
                setLayoutVisibility(View.VISIBLE, true);
            }
            scrollTotalDistance += distanceY;

            float height = wm.getDefaultDisplay().getHeight() * 0.75f;
            // 上滑distanceY为正
            currentVolume = (int) (scrollCurrentVolume + maxVolume * scrollTotalDistance / height);

            if (currentVolume < 0) {
                currentVolume = 0;
            } else if (currentVolume > maxVolume) {
                currentVolume = maxVolume;
            }
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return super.onFling(e1, e2, velocityX, velocityY);
        }

        @Override
        public void onShowPress(MotionEvent e) {
            super.onShowPress(e);
        }

        @Override
        public boolean onDown(MotionEvent e) {
            scrollTotalDistance = 0f;
            isVideo = null;

            scrollCurrentPosition = (float) player.getCurrentPosition();
            scrollCurrentVolume = currentVolume;

            return super.onDown(e);
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if (lockView.isSelected()) {
                return true;
            }
            if (!isDisplay) {
                setLayoutVisibility(View.VISIBLE, true);
            }
            changePlayStatus();
            return super.onDoubleTap(e);
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            return super.onDoubleTapEvent(e);
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if (isDisplay) {
                setLayoutVisibility(View.GONE, false);
            } else {
                setLayoutVisibility(View.VISIBLE, true);
            }
            return super.onSingleTapConfirmed(e);
        }
    }

    private void changePlayStatus() {
        iv_background.setVisibility(View.GONE);
        if (player.isPlaying()) {
            player.pause();
            ivCenterPlay.setVisibility(View.VISIBLE);
            ivPlay.setImageResource(R.mipmap.smallbegin_ico);
        } else {
            player.start();
            ivCenterPlay.setVisibility(View.GONE);
            ivPlay.setImageResource(R.mipmap.smallstop_ic);
        }
    }


    /**
     * 是否是竖屏
     *
     * @return
     */
    private boolean isPortrait() {
        int mOrientation = getApplicationContext().getResources().getConfiguration().orientation;
        if (mOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * @param visibility 显示状态
     * @param isDisplay  是否延迟消失
     */
    private void setLayoutVisibility(int visibility, boolean isDisplay) {
        if (player == null || player.getDuration() <= 0) {
            return;
        }

        playerHandler.removeCallbacks(hidePlayRunnable);

        this.isDisplay = isDisplay;

//        if (definitionMenu != null && visibility == View.GONE) {
//            definitionMenu.dismiss();
//        }

        if (isDisplay) {
            playerHandler.postDelayed(hidePlayRunnable, 5000);
        }

        if (isPortrait()) {
//            ivFullscreen.setVisibility(visibility);

            lockView.setVisibility(View.GONE);

            volumeLayout.setVisibility(View.GONE);
            tvDefinition.setVisibility(View.GONE);
//            tvChangeVideo.setVisibility(View.GONE);
//            ivTopMenu.setVisibility(View.VISIBLE);
//            ivBackVideo.setVisibility(View.GONE);
//            ivNextVideo.setVisibility(View.GONE);
        } else {
//            ivFullscreen.setVisibility(View.GONE);

            lockView.setVisibility(visibility);
            if (lockView.isSelected()) {
                visibility = View.GONE;
            }

            tvDefinition.setVisibility(visibility);
        }


        showPlayerTopLayout(visibility);
        showBottomLayout(visibility);

        playerTopLayout.setVisibility(visibility);
        playerBottomLayout.setVisibility(visibility);
    }

    private void showPlayerTopLayout(int v) {
        if (v == View.VISIBLE)
            playerTopLayout.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_in_from_top));
        else
            playerTopLayout.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_out_to_top));
    }

    private void showBottomLayout(int v) {
        if (v == View.VISIBLE)
            playerBottomLayout.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_in_from_bottom));
        else
            playerBottomLayout.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_out_to_bottom));
    }

    private Dialog showShare() {
        final Dialog dialog = new Dialog(this, R.style.myDialogTheme);
        View layout = getLayoutInflater().inflate(R.layout.share_layout, null);

        layout.findViewById(R.id.ll_w).setOnClickListener(onClickListener);
        layout.findViewById(R.id.ll_p).setOnClickListener(onClickListener);
        layout.findViewById(R.id.ll_q).setOnClickListener(onClickListener);
        layout.findViewById(R.id.ll_x).setOnClickListener(onClickListener);

        dialog.setContentView(layout);
        Window win = dialog.getWindow();
        win.setGravity(Gravity.BOTTOM);

        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = android.view.WindowManager.LayoutParams.MATCH_PARENT;
        win.setAttributes(lp);
        win.setWindowAnimations(R.style.dialoganimstyle);
        dialog.setCancelable(true);

        return dialog;
    }

}
