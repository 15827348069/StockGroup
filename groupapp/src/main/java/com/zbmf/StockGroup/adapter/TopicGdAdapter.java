package com.zbmf.StockGroup.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.beans.PointsBean;
import com.zbmf.StockGroup.utils.EditTextUtil;
import com.zbmf.StockGroup.utils.ImageLoaderOptions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by pq
 * on 2018/7/2.
 */

public class TopicGdAdapter extends ListAdapter<PointsBean> {
    private Context context;
    private ItemClickSkip mItemClickSkip;
    private DZClick mDZClick;
    private boolean Dzed = false;
    private String dzStatus = "1";//默认为点赞加一
    private Activity mActivity;
    private final int mXdH;
    private final int mGDW;
    private List<LocalMedia> mImgs;

    public void setDZClick(DZClick dzClick) {
        mDZClick = dzClick;
    }

    public void setItemClickSkip(ItemClickSkip itemClickSkip) {
        mItemClickSkip = itemClickSkip;
    }

    public TopicGdAdapter(Context context, SparseBooleanArray collapsedStatus, Activity activity,List<LocalMedia> img) {
        super(context);
        this.context = context;
        mActivity=activity;
        this.mImgs=img;
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int WP = dm.widthPixels;
        mXdH = WP * 2 / 3;
        mGDW = WP / 2;
    }

    @SuppressLint({"SimpleDateFormat", "RtlHardcoded"})
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ht_list_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final PointsBean pointsBean = getItem(position);
//        LocalMedia localMedia = new LocalMedia();
//        localMedia.setPath(pointsBean.getImg_keys());
//        Log.i("--TAG","--   图片的路径   "+pointsBean.getImg_keys());
//        mImgs.add(localMedia);
//        Log.i("--TAG","--  图片的数量  "+mImgs.size());


        ImageLoader.getInstance().displayImage(pointsBean.getAvatar(), holder.mAvatar_id, ImageLoaderOptions.AvatarOptions());
        holder.mUserName.setText(pointsBean.getNickname());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//yyyy-MM-dd :ss
        String time = format.format(new Date(Long.parseLong(pointsBean.getCreated_at()) * 1000L));
        holder.mCommentTime.setText(time);
        holder.mUserTitle.setText(pointsBean.getPosition());
        holder.mCompanyName.setText(pointsBean.getCompany());
//        holder.mCommentTv.setText(EditTextUtil.getContent(context, holder.mCommentTv,pointsBean.getContent()));
//        holder.mExpand_text_view.setText(EditTextUtil.getContent(context, pointsBean.getContent()), mCollapsedStatus, position);
        holder.mExpandable_text.setText(EditTextUtil.getContent(context, holder.mCommentTv,pointsBean.getContent()));
        if (!TextUtils.isEmpty(pointsBean.getImg_keys())) {
            /*int height = pointsBean.getHeight();
            int width = pointsBean.getWidth();
            DisplayMetrics dm = context.getResources().getDisplayMetrics();
            int wP = dm.widthPixels;
            int hP = dm.heightPixels;
            int wPix = (int) (width * wP / hP);
            int hPix = (int) (height * wP / hP);
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)holder.mImgGd.getLayoutParams();
            lp.width = wPix;
            lp.height = hPix;
            lp.gravity= Gravity.LEFT;
            holder.mImgGd.setLayoutParams(lp);*/
            int height = pointsBean.getHeight();
//            int width = pointsBean.getWidth();
            if (height>mXdH){
                //如果图片的高大于屏幕的2/3,则将图片设置为边长是屏幕一般的正方形
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)holder.mImgGd.getLayoutParams();
                lp.width = mGDW;
                lp.height = mGDW;
                holder.mImgGd.setLayoutParams(lp);
            }
            ImageLoader.getInstance().displayImage(pointsBean.getImg_keys(), holder.mImgGd, ImageLoaderOptions.ProgressOptions());
            holder.mImgGd.setVisibility(View.VISIBLE);
            holder.mImgGd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PictureSelector.create(mActivity).themeStyle(R.style.picture_default_style)
                            .isZoomAnim(true).compress(false).sizeMultiplier(0.5f)
                            .previewEggs(false).openExternalPreview(position, mImgs);
                }
            });
        } else {
            holder.mImgGd.setVisibility(View.GONE);
        }
        int is_hit = pointsBean.getIs_hit();//是否点赞
        if (is_hit == 0) {
            Drawable drawable = context.getResources().getDrawable(R.drawable.dz_xx_icon);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.mDzBtn.setCompoundDrawables(drawable, null, null, null);
        } else {
            setDzBg(holder);
        }
        holder.mDzBtn.setText(String.valueOf(pointsBean.getHits()));
        holder.mCommentBtn.setText(String.valueOf(pointsBean.getComment_count()));
        holder.mTopicName.setText(String.format("#%s", pointsBean.getTitle()));
        holder.mGd_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickSkip != null) {
                    mItemClickSkip.skipNext(String.valueOf(pointsBean.getViewpoint_id()), holder);
                }
            }
        });
        holder.mDzBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDZClick != null) {
                    if (!Dzed) {
                        dzStatus = "1";
                        mDZClick.dzClick(String.valueOf(pointsBean.getViewpoint_id()), dzStatus,holder);
                        Dzed = true;
                    } else {
                        dzStatus = "0";
                        mDZClick.dzClick(String.valueOf(pointsBean.getViewpoint_id()), dzStatus,holder);
                        Dzed = false;
                    }
                }
            }
        });
        return convertView;
    }

    //设置点赞数
    public void setDzCount(String hits, TopicGdAdapter.ViewHolder mHolder) {
        if (mHolder != null) {
            mHolder.mDzBtn.setText(hits);
            setDzBg(mHolder);
        }
    }

    //设置评论数
    public void setCommentCount(String comment_count, TopicGdAdapter.ViewHolder mHolder) {
        if (mHolder != null) {
            mHolder.mCommentBtn.setText(comment_count);
        }
    }

    private void setDzBg(TopicGdAdapter.ViewHolder mHolder) {
        Drawable drawable = context.getResources().getDrawable(R.drawable.dz_icon);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        mHolder.mDzBtn.setCompoundDrawables(drawable, null, null, null);
    }

    public class ViewHolder {

        private final ImageView mAvatar_id;
        private final TextView mUserName;
        private final TextView mCommentTime;
        private final TextView mUserTitle;
        private final TextView mCompanyName;
        private final TextView mCommentTv;
        private final TextView mTopicName;
        private final TextView mDzBtn;
        private final TextView mCommentBtn;
        private final LinearLayout mGd_item;
        private final ImageView mImgGd;
//        private final ExpandableTextView mExpand_text_view;
//        private final ImageButton mExpand_collapse;
                private final TextView mExpandable_text;

        public ViewHolder(View view) {
            mAvatar_id = view.findViewById(R.id.avatar_id);
            mUserName = view.findViewById(R.id.userName);
            mCommentTime = view.findViewById(R.id.commentTime);
            mUserTitle = view.findViewById(R.id.userTitle);
            mCompanyName = view.findViewById(R.id.companyName);
            mCommentTv = view.findViewById(R.id.commentTv);
            mTopicName = view.findViewById(R.id.topicName);
            mDzBtn = view.findViewById(R.id.dzBtn);
            mCommentBtn = view.findViewById(R.id.commentBtn);
            mGd_item = view.findViewById(R.id.gd_item);
            mImgGd = view.findViewById(R.id.imgGd);
//            mExpand_text_view = view.findViewById(R.id.expand_text_view);
//            mExpand_collapse = view.findViewById(R.id.expand_collapse);
//            mExpand_collapse.setMinimumHeight(1);
            mExpandable_text = view.findViewById(R.id.expandable_text);
        }
    }

    public interface ItemClickSkip {
        void skipNext(String viewpoint_id, ViewHolder holder);
    }

    public interface DZClick {
        void dzClick(String viewpoint_id, String status, ViewHolder holder);
    }
}
