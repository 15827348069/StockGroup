package com.zbmf.StocksMatch.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.bean.User;
import com.zbmf.StocksMatch.common.Constans;
import com.zbmf.StocksMatch.common.IntentKey;
import com.zbmf.StocksMatch.listener.SelectPhoto;
import com.zbmf.StocksMatch.listener.TakePhoto;
import com.zbmf.StocksMatch.model.imode.UserModeView;
import com.zbmf.StocksMatch.presenter.UserPresenter;
import com.zbmf.StocksMatch.util.MyActivityManager;
import com.zbmf.StocksMatch.view.BuyDialog;
import com.zbmf.StocksMatch.view.GlideOptionsManager;
import com.zbmf.StocksMatch.view.ShowOrHideProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class UserActivity extends BaseActivity<UserPresenter> implements UserModeView, TakePhoto, SelectPhoto {
    @BindView(R.id.userAvatar)
    ImageView userAvatar;
    @BindView(R.id.ll_avatar)
    LinearLayout ll_avatar;
    @BindView(R.id.nickName)
    EditText nickName;
    @BindView(R.id.ll_nick)
    LinearLayout ll_nick;
    @BindView(R.id.userSaveBtn)
    Button userSaveBtn;
    private BuyDialog mBuyDialog;
    private UserPresenter mUserPresenter;

    @Override
    protected int getLayout() {
        return R.layout.activity_user;
    }

    @Override
    protected String initTitle() {
        return getString(R.string.my_infomation);
    }

    @Override
    protected void initData(Bundle bundle) {
        MyActivityManager.getMyActivityManager().pushAct(this);
        iCheckPermission();
        if (mBuyDialog == null) {
            mBuyDialog = new BuyDialog(this, R.style.Buy_Dialog).createDialog();
        }
        User user = (User) bundle.getSerializable(IntentKey.USER);
        assert user != null;
        Glide.with(this)
                .load(user.getAvatar())
                .apply(GlideOptionsManager.getInstance().getRequestOptionsMatch())
                .into(userAvatar);
        nickName.setText(user.getNickname());
        nickName.setEnabled(false);
//        nickName.setFocusable(false);
    }

    @Override
    protected UserPresenter initPresent() {
        mUserPresenter = new UserPresenter();
        return mUserPresenter;
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
    }

    @OnClick({R.id.ll_avatar, R.id.ll_nick, R.id.userSaveBtn,R.id.nickName})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_avatar://上传图像/更新图像
                mBuyDialog.setTopTv(getString(R.string.select_pics)).setBottomTv(getString(R.string.tzke_pic))
                        .showI().setSellClick(this).setBuyClick(this);
                break;
            case R.id.ll_nick:
                nickName.setEnabled(true);
                break;
            case R.id.userSaveBtn:
                saveNickName();
                break;
            case R.id.nickName:
                nickName.setEnabled(true);
                break;
        }
    }

    private void saveNickName(){
        String nick = nickName.getText().toString().trim();
        if (!TextUtils.isEmpty(nick)){
            mUserPresenter.upDateUser(nick);
            ShowOrHideProgressDialog.showProgressDialog(this,this,getString(R.string.saving));
        }else {
            showToast(getString(R.string.nick_err_tip));
            nickName.setBackgroundResource(R.drawable.add_stock_et_bg_tip);
        }
    }

    private void iCheckPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Constans.permissions.length > 0) {
                for (int i = 0; i < Constans.permissions.length; i++) {
                    if (checkSelfPermission(Constans.permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                        //需要授权
                        ActivityCompat.requestPermissions(this, Constans.permissions,
                                IntentKey.REQUEST_PERMISSION_CODE);
                    }
                }
            }
        }
    }

    @Override
    public void skipTakePhoto() {
        PictureSelector.create(UserActivity.this)
                .openCamera(PictureMimeType.ofImage())
                .forResult(PictureConfig.CHOOSE_REQUEST);
        mBuyDialog.dissMissI();
    }

    @Override
    public void skipSelectPhotos() {
        PictureSelector.create(UserActivity.this)
                .openGallery(PictureMimeType.ofImage())
                .maxSelectNum(1)// 最大图片选择数量 int
                .minSelectNum(1)// 最小选择数量 int
                .imageSpanCount(3)// 每行显示个数 int
                .forResult(PictureConfig.CHOOSE_REQUEST);
        mBuyDialog.dissMissI();
    }

    @Override
    public void upAvatar(JSONObject obj) {
       if (obj!=null){
           try {
               /*String icon_key = obj.getString("icon_key");*/
               String avatar = obj.getString("avatar");
               if (!TextUtils.isEmpty(avatar)){
                   Glide.with(this)
                           .load(avatar)
                           .apply(GlideOptionsManager.getInstance().getRequestOptionsMatch())
                           .into(userAvatar);
               }
           } catch (JSONException e) {
               e.printStackTrace();
           }
       }
    }

    @Override
    public void nickName(String nick) {
        ShowOrHideProgressDialog.disMissProgressDialog();
        if (!TextUtils.isEmpty(nick)){
            showToast(getString(R.string.save_success));
            nickName.setText(nick);
            nickName.setFocusable(false);
        }
    }

    @Override
    public void upErr(String msg) {
        ShowOrHideProgressDialog.disMissProgressDialog();
        if (!TextUtils.isEmpty(msg)) {
            showToast(msg);
        }
    }

    @Override
    public void refreshAvatar() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片、视频、音频选择结果回调
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true  注意：音视频除外
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true  注意：音视频除外
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                    //优先选择上传压缩后的图片
                    if (selectList.get(0).isCompressed()) {
                        String compressPath = selectList.get(0).getCompressPath();
                        mUserPresenter.upAvatar(compressPath, this);
                    } else {
                        String path = selectList.get(0).getPath();
                        mUserPresenter.upAvatar(path, this);
                    }
                    break;
            }
        }
    }
}
