package com.zbmf.StockGroup.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.adapter.TopicCommentTabAdapter;
import com.zbmf.StockGroup.api.JSONHandler;
import com.zbmf.StockGroup.api.WebBase;
import com.zbmf.StockGroup.beans.PointDetailBean;
import com.zbmf.StockGroup.constans.IntentKey;
import com.zbmf.StockGroup.dialog.EditTextDialog;
import com.zbmf.StockGroup.fragment.DzRemarkListFragment;
import com.zbmf.StockGroup.fragment.TopicCommentsFragment;
import com.zbmf.StockGroup.utils.EditTextUtil;
import com.zbmf.StockGroup.utils.ImageLoaderOptions;
import com.zbmf.StockGroup.view.MyScrollView;
import com.zbmf.StockGroup.view.WrapContentHeightViewPager;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class GDDetailActivity extends BaseActivity implements View.OnClickListener, EditTextDialog.FbComment,
        MyScrollView.OnScrollListener {
    private String mViewpoint_id;
    private int page = 1;
    private ImageView mAvatar;
    private TextView mUserName;
    private TextView mCommentTime;
    private TextView mUserTitle;
    //    private TextView mCommentTv;
    private TextView mTopicName;
    private PointDetailBean mPointDetailBean;
    private boolean Dzed = false;
    private EditTextDialog mDialog;
    private String[] types = {"评论0", "点赞0"};
    private TopicCommentsFragment mTopicCommentsFragment;
    private List<String> mList;
    private TopicCommentTabAdapter mHtPagerAdapter;
    private TextView mCompanyName;
    private TextView mDz_tv_btn;
    private TextView mPl_tv_btn;
    private ImageView mIv;
    private TextView mExpandable_text;
    private LinearLayout bottomTab;
    private MyScrollView mMySc;
    private List<LocalMedia> mImgs;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_gddetail;
    }

    @Override
    public void initView() {
        initTitle("详情");
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mViewpoint_id = extras.getString("viewpoint_id");
        }
        mImgs = new ArrayList<>();

        mAvatar = getView(R.id.avatarIV);
        mUserName = getView(R.id.userName);
        mCommentTime = getView(R.id.commentTime);
        mUserTitle = getView(R.id.userTitle);
        mIv = getView(R.id.iv);
        mCompanyName = getView(R.id.companyName);
//        mExpand_text_view = getView(R.id.expand_text_view);
        mExpandable_text = getView(R.id.expandable_text);
        bottomTab = getView(R.id.bottomTab);
//        mCommentTv = getView(R.id.commentTv);
        mTopicName = getView(R.id.topicName);
        mMySc = getView(R.id.my_sc);
        mMySc.setOnScrollListener(this);

        TabLayout tab_layout_view = getView(R.id.tab_layout_view);
        WrapContentHeightViewPager vp = getView(R.id.vp);
        ArrayList<Fragment> mFragmentList = new ArrayList<>();
        mTopicCommentsFragment = TopicCommentsFragment.topicCommentInstance(mViewpoint_id,mMySc,bottomTab);
        mFragmentList.add(mTopicCommentsFragment);
        mFragmentList.add(DzRemarkListFragment.dzRemarkInstance(mViewpoint_id,mMySc,bottomTab));
        mList = Arrays.asList(types);
        mHtPagerAdapter = new TopicCommentTabAdapter(getSupportFragmentManager(), mFragmentList, mList);
        vp.setAdapter(mHtPagerAdapter);

        tab_layout_view.setupWithViewPager(vp);
        vp.setCurrentItem(0);
        tab_layout_view.getTabAt(0).select();

        mDz_tv_btn = getView(R.id.dz_tv_btn);
        mPl_tv_btn = getView(R.id.pl_tv_btn);

        mDz_tv_btn.setOnClickListener(this);
        mPl_tv_btn.setOnClickListener(this);
    }

    private void refreshCommentList() {
        if (mTopicCommentsFragment.getCommentBeanList() != null) {
            mTopicCommentsFragment.getCommentBeanList().clear();
        }
        page = 1;
        Intent intent = new Intent(IntentKey.FLUSH_TOPIC_COMMENT_LIST);
        intent.putExtra("page", page);
        sendBroadcast(intent);
    }

    private void getMoreCommentList() {
        page += 1;
        Intent intent = new Intent(IntentKey.FLUSH_TOPIC_COMMENT_LIST);
        intent.putExtra("page", page);
        sendBroadcast(intent);
    }

    @Override
    public void initData() {
        //获取观点详情
        getPointDetail();
    }

    private void getPointDetail(){
        if (!TextUtils.isEmpty(mViewpoint_id)) {
            WebBase.getGDDetail(mViewpoint_id, new JSONHandler() {
                @Override
                public void onSuccess(JSONObject obj) {
                    if (obj.optString("status").equals("ok")) {
                        mPointDetailBean = new PointDetailBean(obj.optInt("viewpoint_id"), obj.optString("content")
                                , obj.optInt("topic_id"), obj.optString("title"), obj.optLong("uid"),
                                obj.optString("nickname"), obj.optString("avatar"),
                                obj.optInt("comment_count"), obj.optString("img_keys"),
                                obj.optInt("hits"), obj.optString("created_at"), obj.optString("company"),
                                obj.optString("position"));
                        setViewComtent();
                    }
                    mMySc.smoothScrollTo(0, 20);
                }

                @Override
                public void onFailure(String err_msg) {

                }
            });
        }
    }

    private void setViewComtent() {
        if (mPointDetailBean != null) {
            mList.set(0, String.format("评论%s", mPointDetailBean.getComment_count()));
            mList.set(1, String.format("赞%s", mPointDetailBean.getHits()));
            mHtPagerAdapter.notifyDataSetChanged();
            String avatar = mPointDetailBean.getAvatar();
            ImageLoader.getInstance().displayImage(mPointDetailBean.getAvatar(), mAvatar, ImageLoaderOptions.AvatarOptions());
            ImageLoader.getInstance().displayImage(mPointDetailBean.getImg_keys(), mIv, ImageLoaderOptions.ProgressOptions());
            LocalMedia localMedia = new LocalMedia();
            localMedia.setPath(mPointDetailBean.getImg_keys());
            mImgs.add(localMedia);
            mIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PictureSelector.create(GDDetailActivity.this).themeStyle(R.style.picture_default_style).openExternalPreview(0, mImgs);
                }
            });
            mUserName.setText(mPointDetailBean.getNickname());
            @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//yyyy-MM-dd :ss
            String time = format.format(new Date(Long.parseLong(mPointDetailBean.getCreated_at()) * 1000L));
            mCommentTime.setText(time);
            mCompanyName.setText(mPointDetailBean.getCompany());
            mUserTitle.setText(/*String.format("#%s",*/mPointDetailBean.getPosition()/*)*/);
            mTopicName.setText(String.format("#%s", mPointDetailBean.getTitle()));
//            mExpand_text_view.setText(EditTextUtil.getContent(this,mPointDetailBean.getContent()),mCollapsedStatus,0);
            mExpandable_text.setText(EditTextUtil.getContent(this, mPointDetailBean.getContent()));
        }
    }

    @Override
    public void addListener() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dz_tv_btn://点赞
                String dzStatus;
                if (!Dzed) {
                    dzStatus = "1";
                    if (!TextUtils.isEmpty(mViewpoint_id)) {
                        dzMethod(mViewpoint_id, dzStatus);
                    }
                    Dzed = true;
                } else {
                    dzStatus = "0";
                    if (!TextUtils.isEmpty(mViewpoint_id)) {
                        dzMethod(mViewpoint_id, dzStatus);
                    }
                    Dzed = false;
                }
                break;
            case R.id.pl_tv_btn://评论
                if (mDialog == null) {
                    mDialog = EditTextDialog.createDialog(this, R.style.myDialogTheme)
                            .setEditHint("请畅写你的评论内容")
                            .setEmailVisibility(View.VISIBLE)
                            .setRightSendMsgBtnShow(getString(R.string.send))
                            .setFbComment(this);
                }
                mDialog.show();
                break;
        }
    }

    private void dzMethod(String viewpoint_id, String status) {
        //观点点赞
        WebBase.pointDz(viewpoint_id, status, new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                if (obj.optString("status").equals("ok")) {
                    int hits = obj.optInt("hits");
                    mList.set(1, String.format("赞%s", hits));
                    mHtPagerAdapter.notifyDataSetChanged();
                    Intent intent = new Intent(IntentKey.FLUSH_TOPIC_DETAI_FLAG);
                    intent.putExtra("hits", String.valueOf(hits));
                    sendBroadcast(intent);
                    setDzBg();
                }
            }

            @Override
            public void onFailure(String err_msg) {
                showToast(err_msg);
            }
        });
    }

    private void setDzBg() {
        Drawable drawable = this.getResources().getDrawable(R.drawable.big_dz_icon);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        mDz_tv_btn.setCompoundDrawables(drawable, null, null, null);
        mDz_tv_btn.setTextColor(getResources().getColor(R.color.red_light));
    }

    private void setCommentBg() {
        Drawable drawable = this.getResources().getDrawable(R.drawable.commented_icon);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        mPl_tv_btn.setCompoundDrawables(drawable, null, null, null);
        mPl_tv_btn.setTextColor(getResources().getColor(R.color.red_light));
    }

    //发布评论
    private void fbComment(String viewpoint_id, String comment) {
        WebBase.fbComment(viewpoint_id, comment, "", new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                mDialog.dismiss();
                if (obj.optString("status").equals("ok")) {
                    int comment_number = obj.optInt("comment_number");
                    mList.set(0, String.format("评论%s", comment_number));
                    mHtPagerAdapter.notifyDataSetChanged();
                    showToast("评论发表成功");
                    Intent intent = new Intent(IntentKey.FLUSH_TOPIC_DETAI_FLAG);
                    intent.putExtra("comment_count", String.valueOf(comment_number));
                    sendBroadcast(intent);
                    setCommentBg();
                    //更新评论列表
                    refreshCommentList();
                }
            }

            @Override
            public void onFailure(String err_msg) {
                mDialog.dismiss();
                showToast(err_msg);
            }
        });
    }

    @Override
    public void fbComment(String comment) {
        if (TextUtils.isDigitsOnly(comment)) {
            showToast("请填写评论内容");
            return;
        }
        if (!TextUtils.isEmpty(mViewpoint_id)) {
            fbComment(String.valueOf(mViewpoint_id), comment);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onScroll(int scrollY) {}

    @Override
    public void scrollDirectionUp(boolean scrollUp) {
        if (scrollUp) {
            if (bottomTab.getVisibility() == View.VISIBLE) {
                bottomTab.setVisibility(View.GONE);
            }
        } else {
            if (bottomTab.getVisibility() == View.GONE) {
                bottomTab.setVisibility(View.VISIBLE);
            }
        }
    }
}
