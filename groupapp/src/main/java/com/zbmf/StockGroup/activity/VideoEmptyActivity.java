package com.zbmf.StockGroup.activity;

import android.os.Bundle;
import com.bokecc.sdk.mobile.live.DWLive;
import com.bokecc.sdk.mobile.live.DWLiveLoginListener;
import com.bokecc.sdk.mobile.live.Exception.DWLiveException;
import com.bokecc.sdk.mobile.live.pojo.PublishInfo;
import com.bokecc.sdk.mobile.live.pojo.RoomInfo;
import com.bokecc.sdk.mobile.live.pojo.TemplateInfo;
import com.bokecc.sdk.mobile.live.pojo.Viewer;
import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.api.JSONHandler;
import com.zbmf.StockGroup.api.WebBase;
import com.zbmf.StockGroup.beans.Video;
import com.zbmf.StockGroup.constans.IntentKey;
import com.zbmf.StockGroup.constans.RequestCode;
import com.zbmf.StockGroup.constans.Constants;
import com.zbmf.StockGroup.utils.JSONParse;
import com.zbmf.StockGroup.utils.SettingDefaultsManager;
import com.zbmf.StockGroup.utils.ShowActivity;

import org.json.JSONObject;

/**
 * Created by xuhao on 2017/8/29.
 */

public class VideoEmptyActivity extends BaseActivity{
    @Override
    public int getLayoutResId() {
        return R.layout.activity_empty_layout;
    }

    @Override
    public void initView() {
        initTitle();
    }

    @Override
    public void initData() {
        String video_id= getIntent().getStringExtra(IntentKey.VIDEO_KEY);
        WebBase.GetVideoDetail(video_id, new JSONHandler(true,VideoEmptyActivity.this,getString(R.string.loading)) {
            @Override
            public void onSuccess(JSONObject obj) {
                if(!obj.isNull("video")){
                    JSONObject jsonObject = obj.optJSONObject("video");
                    final Video video = JSONParse.getVideo(jsonObject);
                    if(video.getIs_live()){
                        DWLive.getInstance().setDWLiveLoginParams(new DWLiveLoginListener() {
                            @Override
                            public void onLogin(TemplateInfo templateInfo, Viewer viewer, RoomInfo roomInfo, PublishInfo publishInfo) {
                               runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Bundle bundle=new Bundle();
                                        bundle.putSerializable(IntentKey.VIDEO_KEY,video);
                                        ShowActivity.showActivityForResult(VideoEmptyActivity.this,bundle, VideoPlayActivity.class, RequestCode.COMIT_VIDEO);
                                        finish();
                                             }
                                });
                            }
                            @Override
                            public void onException(final DWLiveException e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Bundle bundle=new Bundle();
                                        bundle.putSerializable(IntentKey.VIDEO_KEY,video);
                                        ShowActivity.showActivityForResult(VideoEmptyActivity.this,bundle, VideoPlayActivity.class, RequestCode.COMIT_VIDEO);
                                        finish();
                                        finish();
                                    }
                                });
                            }
                        }, Constants.CC_USERID,video.getBokecc_id()+"", SettingDefaultsManager.getInstance().NickName(),"");
                        DWLive.getInstance().startLogin();

                    }else{
                        Bundle bundle=new Bundle();
                        bundle.putSerializable(IntentKey.VIDEO_KEY,video);
                        ShowActivity.showActivity(VideoEmptyActivity.this,bundle, VideoPlayActivity.class);
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(String err_msg) {
                finish();
            }
        });
    }

    @Override
    public void addListener() {

    }
}
