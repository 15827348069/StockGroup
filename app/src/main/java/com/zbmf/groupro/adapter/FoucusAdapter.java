package com.zbmf.groupro.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zbmf.groupro.GroupActivity;
import com.zbmf.groupro.R;
import com.zbmf.groupro.activity.CareTeacherActivity;
import com.zbmf.groupro.beans.Group;
import com.zbmf.groupro.utils.ImageLoaderOptions;
import com.zbmf.groupro.view.RoundedCornerImageView;

import java.util.List;


/**
 * Created by iMac on 2017/2/21.
 */

public class FoucusAdapter extends BaseAdapter {

    private List<Group> mGroups;
    private Activity mContext;
    private int type;
    public static final int FOCUS_LIST = 1;//默认为通过关注进入直播
    private CompoundButton.OnCheckedChangeListener listener;

    public void setListener(CompoundButton.OnCheckedChangeListener listener) {
        this.listener = listener;
    }

    public FoucusAdapter(Context cxt, List<Group> groups, int type) {
        mGroups = groups;
        if (cxt instanceof GroupActivity)
            mContext = (GroupActivity) cxt;
        else if (cxt instanceof CareTeacherActivity)
            mContext = (CareTeacherActivity) cxt;
        this.type = type;
    }

    @Override
    public int getCount() {
        return mGroups.size();
    }

    @Override
    public Object getItem(int position) {
        return mGroups.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        UnFocusHolder holder1 = null;
        final Group group = mGroups.get(position);
        if (type == FOCUS_LIST) {
            if (convertView != null) {
                holder = (ViewHolder) convertView.getTag();
            } else {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_focus, null);
                holder = new ViewHolder();
                holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                holder.tv_text = (TextView) convertView.findViewById(R.id.tv_text);
                holder.tv_in = (TextView) convertView.findViewById(R.id.tv_in);
                holder.tv_live_text = (TextView) convertView.findViewById(R.id.tv_live_text);
                holder.tv_chat_text = (TextView) convertView.findViewById(R.id.tv_chat_text);
                holder.riv = (RoundedCornerImageView) convertView.findViewById(R.id.iv);
                holder.ll_root = (RelativeLayout) convertView.findViewById(R.id.ll_root);
                convertView.setTag(holder);
            }
            ImageLoader.getInstance().displayImage(group.getAvatar(), holder.riv, ImageLoaderOptions.AvatarOptions());
            holder.tv_name.setText(group.getNick_name());
            holder.tv_live_text.setText(group.getUnredcount() + "");
            holder.tv_chat_text.setText(group.getChat()+"");
            holder.tv_in.setVisibility(View.GONE);
        } else {
            if (convertView != null) {
                holder1 = (UnFocusHolder) convertView.getTag();
            } else {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.recommend_item, null);
                holder1 = new UnFocusHolder();
                holder1.recommend_item_button = (CheckBox) convertView.findViewById(R.id.recommend_item_button);
                holder1.tv_name = (TextView) convertView.findViewById(R.id.recommend_item_name);
                holder1.recommend_item_desc = (TextView) convertView.findViewById(R.id.recommend_item_desc);
                holder1.riv = (RoundedCornerImageView) convertView.findViewById(R.id.recommend_item_avatar);
                holder1.ll_root = (RelativeLayout) convertView.findViewById(R.id.rl_root);
                convertView.setTag(holder1);
            }
            ImageLoader.getInstance().displayImage(group.getAvatar(), holder1.riv, ImageLoaderOptions.AvatarOptions());
            holder1.tv_name.setText(group.getNick_name());

            if (group.is_recommend()) {
                holder1.recommend_item_button.setTag(position);
                holder1.recommend_item_button.setOnCheckedChangeListener(listener);
                holder1.recommend_item_button.setText("取消关注");
            } else {
                holder1.recommend_item_button.setText("----");
                holder1.recommend_item_button.setOnCheckedChangeListener(null);
            }

            holder1.recommend_item_button.setVisibility(View.GONE);
            holder1.recommend_item_desc.setVisibility(View.GONE);
        }

        return convertView;
    }


    static class ViewHolder {
        TextView tv_name, tv_text, tv_in, tv_live_text, tv_chat_text;
        RoundedCornerImageView riv;
        RelativeLayout ll_root;
    }

    static class UnFocusHolder {
        TextView tv_name, recommend_item_desc;
        RoundedCornerImageView riv;
        CheckBox recommend_item_button;
        RelativeLayout ll_root;
    }
}
