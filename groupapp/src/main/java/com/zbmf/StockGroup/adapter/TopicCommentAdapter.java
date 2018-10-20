package com.zbmf.StockGroup.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.beans.CommentBean;
import com.zbmf.StockGroup.utils.EditTextUtil;
import com.zbmf.StockGroup.utils.ImageLoaderOptions;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by pq
 * on 2018/7/3.
 */

public class TopicCommentAdapter extends ListAdapter<CommentBean> {
    private SparseBooleanArray mCollapsedStatus;
    public TopicCommentAdapter(Context context) {
        super(context);
        mCollapsedStatus = new SparseBooleanArray();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.topic_comment_list_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        CommentBean item = getItem(position);
        ImageLoader.getInstance().displayImage(item.getAvatar(), holder.mAvatarComment, ImageLoaderOptions.AvatarOptions());
        holder.mUserName.setText(item.getNickname());
        holder.mUserTitle.setText(item.getPosition());
        holder.mCompanyName.setText(item.getCompany());
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//yyyy-MM-dd :ss
        String time = format.format(new Date(Long.parseLong(item.getCreated_at())*1000L));
        holder.mCommentTime.setText(time);
//        holder.mCommentTv.setText(EditTextUtil.getContent(mContext, holder.mCommentTv,item.getContent()));
        holder.mExpand_text_view.setText(EditTextUtil.getContent(mContext, item.getContent()),mCollapsedStatus,position);
        return convertView;
    }

    public class ViewHolder{
        private final ImageView mAvatarComment;
        private final TextView mUserName;
        private final TextView mCommentTime;
        private final TextView mUserTitle;
        private final TextView mCompanyName;
        private final TextView mCommentTv;
        private final ExpandableTextView mExpand_text_view;

        public ViewHolder(View view){
            mAvatarComment = view.findViewById(R.id.avatarComment);
            mUserName = view.findViewById(R.id.userName);
            mCommentTime = view.findViewById(R.id.commentTime);
            mUserTitle = view.findViewById(R.id.userTitle);
            mCompanyName = view.findViewById(R.id.companyName);
            mCommentTv = view.findViewById(R.id.commentTv);
            mExpand_text_view = view.findViewById(R.id.expand_text_view);
        }
    }
}
