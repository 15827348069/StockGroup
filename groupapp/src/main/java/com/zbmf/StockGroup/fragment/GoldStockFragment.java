package com.zbmf.StockGroup.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.Tencent;
import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.activity.GoldStockActivity;
import com.zbmf.StockGroup.api.JSONHandler;
import com.zbmf.StockGroup.api.WebBase;
import com.zbmf.StockGroup.beans.Dictum;
import com.zbmf.StockGroup.callback.ResultCallback;
import com.zbmf.StockGroup.constans.Constants;
import com.zbmf.StockGroup.constans.IntentKey;
import com.zbmf.StockGroup.utils.BitmapUtil;
import com.zbmf.StockGroup.utils.DateUtil;
import com.zbmf.StockGroup.utils.FileUtils;
import com.zbmf.StockGroup.utils.ImageLoaderOptions;
import com.zbmf.StockGroup.utils.JSONParse;
import com.zbmf.StockGroup.utils.ShowActivity;
import com.zbmf.StockGroup.view.CustomProgressDialog;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.zbmf.StockGroup.utils.BitmapUtil.bmpToByteArray;
import static com.zbmf.StockGroup.utils.BitmapUtil.compressImage;
import static com.zbmf.StockGroup.utils.BitmapUtil.saveBitmap;

/**
 * Created by xuhao on 2017/11/8.
 */

public class GoldStockFragment extends Fragment implements View.OnClickListener {
    private TextView tv_content, tv_date, tv_name, tv_desc, tv_live, tv_mf_content, tv_num, tv_share;
    private ImageView imv_group_avatar;
    private LinearLayout share_content_layout;
    private String id;
    public Bitmap bitmap;
    private View view;
    private String group_id;
    private boolean already_initdata;

    private final int SHARE_CLIENT = 1;
    private final int SHARE_ALL_IN_ONE = 2;
    private WbShareHandler shareHandler;
    private int mShareType = SHARE_ALL_IN_ONE;
    private IWXAPI iwxapi;
    private Tencent mTencent;
    private CustomProgressDialog progressDialog;
    public static GoldStockFragment newInstance(String id) {
        GoldStockFragment fragment = new GoldStockFragment();
        Bundle bundle = new Bundle();
        bundle.putString(IntentKey.FLAG, id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_stock_layout, null);
        }
        initView();
        return view;
    }

    protected void initView() {
        tv_content = getView(R.id.tv_content);
        tv_date = getView(R.id.tv_date);
        tv_name = getView(R.id.tv_name);
        tv_desc = getView(R.id.tv_desc);
        tv_live = getView(R.id.tv_live);
        tv_mf_content = getView(R.id.tv_mf_content);
        tv_num = getView(R.id.tv_num);
        imv_group_avatar = getView(R.id.imv_group_avatar);
        tv_share = getView(R.id.tv_share);
        share_content_layout = getView(R.id.share_content_layout);
        tv_share.setOnClickListener(this);
        tv_live.setOnClickListener(this);
        getView(R.id.tv_cause).setOnClickListener(this);
        getView(R.id.share_wx_friend_circle).setOnClickListener(this);
        getView(R.id.share_wx_friend).setOnClickListener(this);
        getView(R.id.share_sina).setOnClickListener(this);
        getView(R.id.share_qq).setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!already_initdata) {
            initData();
            initShare();
        }
    }
    private void initShare(){
        if(iwxapi==null){
            iwxapi = WXAPIFactory.createWXAPI(getActivity(), Constants.WEI_APK_KEY, true);
        }
        if(shareHandler==null){
            this.shareHandler = new WbShareHandler(getActivity());
            this.shareHandler.registerApp();
        }
       if(mTencent==null){
           mTencent = Tencent.createInstance(Constants.TencentSDKAppKey, getActivity());
       }
    }
    protected void initData() {
        already_initdata = true;
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            id = bundle.getString(IntentKey.FLAG);
            WebBase.getDictumByIds(id, new JSONHandler() {
                @Override
                public void onSuccess(JSONObject obj) {
                    if (obj.has("result")) {
                        Dictum dictum = JSONParse.getDictum(obj.optJSONObject("result"));
                        group_id = dictum.getUser_id();
                        tv_content.setText(dictum.getUser_advice());
                        tv_date.setText(DateUtil.getTime(dictum.getShowtime(), Constants.yy年MM月dd日HHmmss));
                        tv_name.setText(dictum.getNickname());
                        tv_desc.setText(dictum.getTags());
//                        tv_live.setText(dictum.getOnline_status()==1?"正在直播":"圈主已离线");
                        tv_mf_content.setText(dictum.getZbmf_advice());
                        String str = "-第[*]期 总[**]期-";
                        tv_num.setText(str.replace("[*]", String.valueOf(dictum.getDictum_num())).replace("[**]", String.valueOf(dictum.getDictum_total())));
//                        ViewFactory.imgCircleView(getActivity(),dictum.getAvatar(), imv_group_avatar);
                        ImageLoader.getInstance().displayImage(dictum.getAvatar(), imv_group_avatar, ImageLoaderOptions.AvatarOptions());
                    }
                }

                @Override
                public void onFailure(String err_msg) {

                }
            });
        }
    }

    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHH");
        return dateFormat.format(date) + "_" + id + ".jpg";
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_share:
                getView(R.id.tv_share).setVisibility(View.GONE);
                getView(R.id.share_layout).setVisibility(View.VISIBLE);
                getView(R.id.tv_cause).setVisibility(View.VISIBLE);
                break;
            case R.id.tv_cause:
                getView(R.id.share_layout).setVisibility(View.GONE);
                getView(R.id.tv_cause).setVisibility(View.GONE);
                getView(R.id.tv_share).setVisibility(View.VISIBLE);
                break;
            case R.id.tv_live:
                if (group_id != null) {
                    ShowActivity.showGroupDetailActivity(getActivity(), group_id);
                }
                break;
            case R.id.share_wx_friend_circle:
            case R.id.share_wx_friend:
            case R.id.share_sina:
            case R.id.share_qq:
                if (bitmap == null) {
                    shareImg(v);
                } else {
                    shareImgToFriend(v);
                }
                break;
        }
    }
    private void shareImg(final View v) {
        if (progressDialog == null) {
            progressDialog = CustomProgressDialog.createDialog(getActivity());
            progressDialog.setMessage(getString(R.string.loading_img));
        }
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
        BitmapUtil.takeScreenShot(share_content_layout, new ResultCallback<Bitmap>() {
            @Override
            public void onSuccess(final Bitmap bit) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                bitmap = bit;
                shareImgToFriend(v);
            }

            @Override
            public void onError(String message) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void shareImgToFriend(View v) {
        switch (v.getId()) {
            case R.id.share_wx_friend_circle:
                sharetoWx(SendMessageToWX.Req.WXSceneTimeline);
                break;
            case R.id.share_wx_friend:
                sharetoWx(SendMessageToWX.Req.WXSceneSession);
                break;
            case R.id.share_sina:
                WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
                weiboMessage.imageObject = getSinaImageObject();
                weiboMessage.textObject= getSinaTextObject();
                shareHandler.shareMessage(weiboMessage, mShareType == SHARE_CLIENT);
                break;
            case R.id.share_qq:
                String img_url = FileUtils.getIntence().DEFAULT_DATA_IMAGEPATH + getPhotoFileName();
                String url=saveBitmap(img_url, bitmap);
                final Bundle params = new Bundle();
                params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
                params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, url);
                params.putString(QQShare.SHARE_TO_QQ_APP_NAME, getString(R.string.app_name));
                mTencent.shareToQQ(getActivity(), params, ((GoldStockActivity)getActivity()).getiUiListener());
                break;
        }
    }
    private void sharetoWx(int scene) {
        WXImageObject wxImageObject = new WXImageObject(bitmap);
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = wxImageObject;
        //设置缩略图
        Bitmap mBp = Bitmap.createScaledBitmap(bitmap, 120, 120, true);
        msg.thumbData = bmpToByteArray(mBp, true);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");//  transaction字段用
        req.message = msg;
        req.scene = scene;
        iwxapi.sendReq(req);
    }
    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
    private ImageObject getSinaImageObject() {
        ImageObject imageObject = new ImageObject();
        imageObject.setImageObject(bitmap);
        imageObject.setThumbImage(compressImage(bitmap));
        return imageObject;
    }
    private TextObject getSinaTextObject(){
        TextObject textObject=new TextObject();
        textObject.text=getActivitySharedText();
        return textObject;
    }
    private String getActivitySharedText() {
        String format = getString(R.string.weibosdk_share_webpage_template);
        String text = String.format(format,getString(R.string.app_name)+"分享【金股金句】");
        return text;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(bitmap!=null){
            bitmap.recycle();
        }
    }

    protected <T extends View> T getView(int resourcesId) {
        return (T) view.findViewById(resourcesId);
    }



}
