package com.zbmf.StockGTec.adapter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zbmf.StockGTec.R;
import com.zbmf.StockGTec.activity.Chat1Activity;
import com.zbmf.StockGTec.beans.ChatMessage;
import com.zbmf.StockGTec.fragment.ChatFragment;
import com.zbmf.StockGTec.utils.DateUtil;
import com.zbmf.StockGTec.utils.DisplayUtil;
import com.zbmf.StockGTec.utils.EditTextUtil;
import com.zbmf.StockGTec.utils.MessageType;
import com.zbmf.StockGTec.utils.SettingDefaultsManager;
import com.zbmf.StockGTec.view.RoundedCornerImageView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MessageAdapter extends BaseAdapter implements View.OnLongClickListener, View.OnClickListener {

    private List<ChatMessage> messages;
    private Context mContext;
    private ImageLoader mImageLoader;
    private View mView, rView;
    private PopupWindow mPopupWindow, rPopupWindow;
    private OnMenuClickListener mListener;
    private final ImageView iv_down;
    private int chatType = ChatFragment.GROUP_CHAT;

    public void setListener(OnMenuClickListener listener) {
        mListener = listener;
    }

    private static SimpleDateFormat format1 = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm");

    public MessageAdapter(int chatType,Context context, List<ChatMessage> messages) {
        mContext = context;
        this.messages = messages;
        this.chatType = chatType;
        mImageLoader = ImageLoader.getInstance();

        mView = View.inflate(mContext, R.layout.long_click_view, null);
        rView = View.inflate(mContext, R.layout.long_click_rview, null);
        mPopupWindow = new PopupWindow(mView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        rPopupWindow = new PopupWindow(rView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        mView.findViewById(R.id.tv_del).setOnClickListener(this);
        mView.findViewById(R.id.tv_copy).setOnClickListener(this);
        mView.findViewById(R.id.tv_reply).setOnClickListener(this);
        TextView tv_gag = (TextView) mView.findViewById(R.id.tv_gag);
        TextView tv_report = (TextView) mView.findViewById(R.id.tv_report);
        View iv_vertical = mView.findViewById(R.id.iv_vertical);
        tv_gag.setOnClickListener(this);
        tv_report.setOnClickListener(this);
        mView.findViewById(R.id.tv_chat).setOnClickListener(this);
        rView.findViewById(R.id.tv_copy).setOnClickListener(this);
        rView.findViewById(R.id.tv_del).setOnClickListener(this);
        iv_down = (ImageView) mView.findViewById(R.id.iv_down);
        if(chatType == ChatFragment.FANS_CHAT){
            tv_gag.setVisibility(View.GONE);
            iv_vertical.setVisibility(View.GONE);
            tv_report.setBackgroundResource(R.drawable.pao_sel_r);
        }
        mPopupWindow.setTouchable(true);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable());
        mPopupWindow.update();
        rPopupWindow.setTouchable(true);
        rPopupWindow.setBackgroundDrawable(new ColorDrawable());
        rPopupWindow.update();
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int position) {
        return messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_message, null);
            holder = new ViewHolder();
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tv_tip = (TextView) convertView.findViewById(R.id.tv_tip);
            holder.tv_from = (TextView) convertView.findViewById(R.id.tv_from);
            holder.tv_type = (TextView) convertView.findViewById(R.id.tv_type);
            holder.tv_content1 = (TextView) convertView.findViewById(R.id.tv_content1);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_content2 = (TextView) convertView.findViewById(R.id.tv_content2);
            holder.rl_msg1 = (RelativeLayout) convertView.findViewById(R.id.rl_msg1);
            holder.ll_msg2 = (LinearLayout) convertView.findViewById(R.id.ll_msg2);
            holder.rcv1 = (RoundedCornerImageView) convertView.findViewById(R.id.rcv1);
            holder.rcv2 = (RoundedCornerImageView) convertView.findViewById(R.id.rcv2);
            holder.progressBar1 = (ProgressBar) convertView.findViewById(R.id.progressBar1);
            holder.iv_error = (ImageView) convertView.findViewById(R.id.iv_error);
            holder.iv_type = (ImageView) convertView.findViewById(R.id.iv_type);
            convertView.setTag(holder);
        }

        final ChatMessage message = messages.get(position);
        holder.tv_content2.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContext.getResources().getDimension(SettingDefaultsManager.getInstance().getTextSize()));
        holder.tv_content1.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContext.getResources().getDimension(SettingDefaultsManager.getInstance().getTextSize()));
        holder.tv_time.setVisibility(View.VISIBLE);
        long time = Long.parseLong(message.getTime());

        if (position == 0) {
            holder.tv_time.setVisibility(View.VISIBLE);
            holder.tv_time.setText(DateUtil.formatDateTime(format1.format(new Date(time))));
        } else {
            ChatMessage lastMessage = messages.get(position - 1);
            if (lastMessage.getTime() != null)
                if (time - Long.parseLong(lastMessage.getTime()) > 5 * 60 * 1000) {
                    holder.tv_time.setVisibility(View.VISIBLE);
                    holder.tv_time.setText(DateUtil.formatDateTime(format1.format(new Date(time))));
                } else {
                    holder.tv_time.setVisibility(View.GONE);
                }
        }

        if (SettingDefaultsManager.getInstance().UserId().equals(message.getFrom())) {
            holder.rl_msg1.setVisibility(View.GONE);
            holder.ll_msg2.setVisibility(View.VISIBLE);
            mImageLoader.displayImage(message.getAvatar(), holder.rcv2);
            holder.tv_content2.setText(EditTextUtil.getContent(mContext, holder.tv_content2, message.getContent()));
            holder.tv_name.setText(message.getNickname());
            holder.tv_content2.setOnLongClickListener(this);
            holder.tv_content2.setTag(position);

            if (message.getState() == MessageType.UPLOADING) {
                holder.progressBar1.setVisibility(View.VISIBLE);
                holder.iv_error.setVisibility(View.GONE);
            } else if (message.getState() == MessageType.UPLOAD_FAIL) {
                holder.progressBar1.setVisibility(View.GONE);
                holder.iv_error.setVisibility(View.VISIBLE);
                holder.iv_error.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        message.setState(MessageType.UPLOADING);
                        mListener.onMenuClick(v, position);
                        notifyDataSetChanged();
                    }
                });
            } else {
                holder.progressBar1.setVisibility(View.GONE);
                holder.iv_error.setVisibility(View.GONE);
            }
        } else {
            holder.rl_msg1.setVisibility(View.VISIBLE);
            holder.ll_msg2.setVisibility(View.GONE);
            mImageLoader.displayImage(message.getAvatar(), holder.rcv1);
            holder.tv_from.setText(message.getNickname());
            holder.tv_content1.setText(EditTextUtil.getContent(mContext, holder.tv_content1, message.getContent()));
            int role = message.getRole();
            if (role == MessageType.ROLE_100) {
                holder.iv_type.setVisibility(View.VISIBLE);
                holder.iv_type.setBackgroundResource(R.drawable.quzhu);
            } else if (role == MessageType.ROLE_50) {
                holder.iv_type.setVisibility(View.VISIBLE);
                holder.iv_type.setBackgroundResource(R.drawable.guanli);
            } else if (role == MessageType.ROLE_20) {
                holder.iv_type.setVisibility(View.VISIBLE);
                holder.iv_type.setBackgroundResource(R.drawable.year);
            } else if (role == MessageType.ROLE_10) {
                holder.iv_type.setVisibility(View.VISIBLE);
                holder.iv_type.setBackgroundResource(R.drawable.tie);
            } else {
                holder.iv_type.setVisibility(View.INVISIBLE);
            }
            holder.tv_content1.setOnLongClickListener(this);
            holder.tv_content1.setTag(position);
        }
        return convertView;
    }

    private int pos;

    @Override
    public boolean onLongClick(View v) {
        if(chatType == 0){
            return false;
        }
        pos = (int) v.getTag();

        switch (v.getId()) {
            case R.id.tv_content1:
                int[] windowPos = getPopPos(v, mView);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) iv_down.getLayoutParams();
                params.leftMargin = windowPos[2];
                iv_down.setLayoutParams(params);
                mPopupWindow.showAtLocation(v, Gravity.TOP | Gravity.START, windowPos[0], windowPos[1]);
                break;
            case R.id.tv_content2:
                int[] windowPos1 = getPopPos(v, rView);
                rPopupWindow.showAtLocation(v, Gravity.TOP | Gravity.START, windowPos1[0], windowPos1[1]);
                break;
        }
        return false;
    }

    private int[] getPopPos(View anchorView, View popView) {
        int windowPos[] = new int[3];
        int viewLoc[] = new int[2];
        int anchorHeight = anchorView.getHeight();
        int anchorWidth = anchorView.getWidth();
        anchorView.getLocationOnScreen(viewLoc);
        int screenHeight = DisplayUtil.getScreenHeightPixels((Chat1Activity) mContext);
        popView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

        int windowHeight = popView.getMeasuredHeight();
        int windowWidth = popView.getMeasuredWidth();

        boolean isNeedDown = (screenHeight - viewLoc[1] < windowHeight);
        if (isNeedDown) {
            windowPos[0] = viewLoc[0] + anchorWidth / 2 - windowWidth / 2;
            windowPos[1] = viewLoc[1] + anchorHeight;
        } else {
            windowPos[0] = viewLoc[0] + anchorWidth / 2 - windowWidth / 2;
            windowPos[1] = viewLoc[1] - windowHeight;
        }
        windowPos[2] = viewLoc[0] + anchorWidth / 2;
        return windowPos;
    }

    @Override
    public void onClick(View v) {
        mListener.onMenuClick(v, pos);
        mPopupWindow.dismiss();
        rPopupWindow.dismiss();
    }

    static class ViewHolder {
        TextView tv_time, tv_tip, tv_from, tv_type, tv_content1, tv_name, tv_content2;
        RelativeLayout rl_msg1;
        LinearLayout ll_msg2;
        RoundedCornerImageView rcv1, rcv2;
        ProgressBar progressBar1;
        ImageView iv_error, iv_type;
    }

    public void addDataList(List<ChatMessage> tempChatMsgs) {
        synchronized (messages) {
            for (int i = 0; i < tempChatMsgs.size(); i++) {
                messages.add(0, tempChatMsgs.get(i));
            }
            notifyDataSetChanged();
        }

    }

    public interface OnMenuClickListener {
        void onMenuClick(View view, int pos);
    }

}
