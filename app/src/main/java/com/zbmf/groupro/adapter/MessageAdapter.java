package com.zbmf.groupro.adapter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
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
import com.zbmf.groupro.R;
import com.zbmf.groupro.activity.Chat1Activity;
import com.zbmf.groupro.beans.ChatMessage;
import com.zbmf.groupro.utils.DateUtil;
import com.zbmf.groupro.utils.DisplayUtil;
import com.zbmf.groupro.utils.EditTextUtil;
import com.zbmf.groupro.utils.MessageType;
import com.zbmf.groupro.utils.SettingDefaultsManager;
import com.zbmf.groupro.view.RoundedCornerImageView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.zbmf.groupro.R.id.rl_msg1;
import static com.zbmf.groupro.R.id.tv_from;

public class MessageAdapter extends BaseAdapter implements View.OnLongClickListener, View.OnClickListener {

    private List<ChatMessage> messages;
    private Context mContext;
    private ImageLoader mImageLoader;
    private View mView;
    private PopupWindow mPopupWindow;
    private OnMenuClickListener mListener;
    private TextView tv_item1,tv_item2,tv_item3;View view1,view2;

    public void setListener(OnMenuClickListener listener) {
        mListener = listener;
    }

    private static SimpleDateFormat format1 = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm");

    public MessageAdapter(Context context, List<ChatMessage> messages) {
        mContext = context;
        this.messages = messages;
        mImageLoader = ImageLoader.getInstance();

        mView = View.inflate(mContext, R.layout.long_click_view, null);
        mPopupWindow = new PopupWindow(mView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        tv_item1 = (TextView) mView.findViewById(R.id.tv_item1);
        tv_item2 = (TextView) mView.findViewById(R.id.tv_item2);
        tv_item3 = (TextView) mView.findViewById(R.id.tv_item3);
        view1 = mView.findViewById(R.id.view1);
        view2 = mView.findViewById(R.id.view2);
        tv_item1.setOnClickListener(this);
        tv_item2.setOnClickListener(this);
        tv_item3.setOnClickListener(this);
//        mView.findViewById(R.id.tv_item1).setOnClickListener(this);
//        mView.findViewById(R.id.tv_item2).setOnClickListener(this);
//        mView.findViewById(R.id.tv_item3).setOnClickListener(this);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable());
        mPopupWindow.update();
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
            holder.tv_from = (TextView) convertView.findViewById(tv_from);
            holder.tv_type = (TextView) convertView.findViewById(R.id.tv_type);
            holder.tv_content1 = (TextView) convertView.findViewById(R.id.tv_content1);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_content2 = (TextView) convertView.findViewById(R.id.tv_content2);
            holder.rl_msg1 = (RelativeLayout) convertView.findViewById(rl_msg1);
            holder.ll_msg2 = (LinearLayout) convertView.findViewById(R.id.ll_msg2);
            holder.rcv1 = (RoundedCornerImageView) convertView.findViewById(R.id.rcv1);
            holder.rcv2 = (RoundedCornerImageView) convertView.findViewById(R.id.rcv2);
            holder.progressBar1 = (ProgressBar) convertView.findViewById(R.id.progressBar1);
            holder.iv_error = (ImageView) convertView.findViewById(R.id.iv_error);
            holder.iv_type = (ImageView) convertView.findViewById(R.id.iv_type);
            holder.iv_type1 = (ImageView) convertView.findViewById(R.id.iv_type1);
            convertView.setTag(holder);
        }

        final ChatMessage message = messages.get(position);
        holder.tv_content2.setTextSize(TypedValue.COMPLEX_UNIT_PX,mContext.getResources().getDimension(SettingDefaultsManager.getInstance().getTextSize()));
        holder.tv_content1.setTextSize(TypedValue.COMPLEX_UNIT_PX,mContext.getResources().getDimension(SettingDefaultsManager.getInstance().getTextSize()));
        holder.tv_time.setVisibility(View.VISIBLE);
        long time = Long.parseLong(message.getTime());

        int role = message.getRole();

        if(position == 0){
            holder.tv_time.setVisibility(View.VISIBLE);
            holder.tv_time.setText(DateUtil.formatDateTime(format1.format(new Date(time))));
        }else{
            ChatMessage lastMessage = messages.get(position-1);
            if(lastMessage.getTime()!=null)
            if (time - Long.parseLong(lastMessage.getTime()) > 5 * 60 * 1000) {
                holder.tv_time.setVisibility(View.VISIBLE);
                holder.tv_time.setText(DateUtil.formatDateTime(format1.format(new Date(time))));
            }else {
                holder.tv_time.setVisibility(View.GONE);
            }
        }

        if(!TextUtils.isEmpty(message.getAction())){
            holder.tv_tip.setVisibility(View.VISIBLE);
            holder.rl_msg1.setVisibility(View.GONE);
            holder.ll_msg2.setVisibility(View.GONE);
//            if(MessageType.ACTION_unBan.equals(message.getAction())){
//                holder.tv_tip.setText("您已被解除禁言");
//            }else if(SettingDefaultsManager.getInstance().UserId().equals(message.getUser_id())){
//                holder.tv_tip.setText("您已被禁言");
//            }else{
//                holder.tv_tip.setText(message.getNickname() + "已被禁言");
//            }

            holder.tv_tip.setText(message.getNickname() + "已被禁言");
        }else if (SettingDefaultsManager.getInstance().UserId().equals(message.getFrom())) {//发送
            holder.rl_msg1.setVisibility(View.GONE);
            holder.ll_msg2.setVisibility(View.VISIBLE);
            holder.tv_tip.setVisibility(View.GONE);
            mImageLoader.displayImage(SettingDefaultsManager.getInstance().UserAvatar(), holder.rcv2);
            holder.tv_content2.setText(EditTextUtil.getContent(mContext, holder.tv_content2, message.getContent()));
            holder.tv_name.setText(message.getNickname());
            holder.tv_content2.setOnLongClickListener(null);
            holder.tv_content2.setTag(position);

            if(message.getState() == MessageType.UPLOADING){
                holder.progressBar1.setVisibility(View.VISIBLE);
                holder.iv_error.setVisibility(View.GONE);
            }else if(message.getState() == MessageType.UPLOAD_FAIL){
                holder.progressBar1.setVisibility(View.GONE);
                holder.iv_error.setVisibility(View.VISIBLE);
                holder.iv_error.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        message.setState(MessageType.UPLOADING);
                        mListener.onMenuClick(v,position);
                        notifyDataSetChanged();
                    }
                });
            }else{
                holder.progressBar1.setVisibility(View.GONE);
                holder.iv_error.setVisibility(View.GONE);
            }

            holder.iv_type.setVisibility(View.GONE);
            holder.iv_type1.setVisibility(View.VISIBLE);
            initRoleInfo(holder.iv_type1, role);
            holder.tv_content2.setOnLongClickListener(this);
            holder.tv_content2.setTag(position);
        } else {
            holder.rl_msg1.setVisibility(View.VISIBLE);
            holder.ll_msg2.setVisibility(View.GONE);
            holder.tv_tip.setVisibility(View.GONE);
            mImageLoader.displayImage(message.getAvatar(), holder.rcv1);
            holder.tv_from.setText(message.getNickname());
            holder.tv_content1.setText(EditTextUtil.getContent(mContext, holder.tv_content1, message.getContent()));
            holder.iv_type1.setVisibility(View.GONE);
            holder.iv_type.setVisibility(View.VISIBLE);
            initRoleInfo(holder.iv_type, role);
            holder.tv_content1.setOnLongClickListener(this);
            holder.tv_content1.setTag(position);
        }
        return convertView;
    }

    private void initRoleInfo(ImageView iv_type, int role) {
        if(role == MessageType.ROLE_100){//圈主
//                holder.tv_type.setText("圈主");
//                holder.tv_type.setBackgroundResource(R.drawable.round_rec_f4bf20);
            iv_type.setVisibility(View.VISIBLE);
            iv_type.setBackgroundResource(R.drawable.quzhu);
        }else if(role == MessageType.ROLE_50){//管理员
//                holder.tv_type.setText("管理员");
//                holder.tv_type.setBackgroundResource(R.drawable.round_rec_f4bf20);
            iv_type.setVisibility(View.VISIBLE);
            iv_type.setBackgroundResource(R.drawable.guanli);
        }else if(role == MessageType.ROLE_20){//年粉
//                holder.tv_type.setText("");
//                holder.tv_type.setBackgroundResource(R.drawable.nianfen);
            iv_type.setVisibility(View.VISIBLE);
            iv_type.setBackgroundResource(R.drawable.year);
        }else if(role == MessageType.ROLE_10){//铁粉
//                holder.tv_type.setText("");
//                holder.tv_type.setBackgroundResource(R.drawable.tief);
            iv_type.setVisibility(View.VISIBLE);
            iv_type.setBackgroundResource(R.drawable.tie);
        }else if(role == MessageType.ROLE_5){//铁粉
//                holder.tv_type.setText("");
//                holder.tv_type.setBackgroundResource(R.drawable.tief);
            iv_type.setVisibility(View.VISIBLE);
            iv_type.setBackgroundResource(R.drawable.try1);
        }else{
            iv_type.setVisibility(View.INVISIBLE);
        }
    }

    private int pos;
    @Override
    public boolean onLongClick(View v) {
        pos = (int) v.getTag();
        switch (v.getId()){
            case R.id.tv_content1:
                int[] windowPos = getPopPos(v, mView);
                tv_item1.setBackgroundResource(R.drawable.pao_sel_l);
                setchoice(View.VISIBLE);
                mPopupWindow.showAtLocation(v, Gravity.TOP | Gravity.START, windowPos[0], windowPos[1]);
                break;
            case R.id.tv_content2:
                tv_item1.setBackgroundResource(R.drawable.pao_sel_a);
                setchoice(View.GONE);
                int[] windowPos1 = getPopPos(v, mView);
                mPopupWindow.showAtLocation(v, Gravity.TOP | Gravity.START, windowPos1[0], windowPos1[1]);
                break;
        }
        return false;
    }

    private void setchoice(int gone) {
        tv_item2.setVisibility(gone);
        tv_item3.setVisibility(gone);
        view1.setVisibility(gone);
        view2.setVisibility(gone);
    }

    private int[] getPopPos(View anchorView, View popView) {
        int windowPos[] = new int[2];
        int viewLoc[] = new int[2];
        int anchorHeight = anchorView.getHeight();
        int anchorWidth = anchorView.getWidth();
        anchorView.getLocationOnScreen(viewLoc);
        int screenHeight = DisplayUtil.getScreenHeightPixels((Chat1Activity)mContext);
        popView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

        int windowHeight = popView.getMeasuredHeight();
        int windowWidth = popView.getMeasuredWidth();

        boolean isNeedDown = (screenHeight - viewLoc[1] < windowHeight);
        if (isNeedDown) {
            windowPos[0] =  viewLoc[0]+anchorWidth / 2 - windowWidth/2;
            windowPos[1] = viewLoc[1] + anchorHeight;
        } else {
            windowPos[0] =  viewLoc[0]+anchorWidth / 2 - windowWidth/2;
            windowPos[1] = viewLoc[1] - windowHeight;
        }

        return windowPos;
    }

    @Override
    public void onClick(View v) {
         mListener.onMenuClick(v,pos);
        mPopupWindow.dismiss();
    }

    static class ViewHolder {
        TextView tv_time, tv_tip, tv_from, tv_type, tv_content1, tv_name, tv_content2;
        RelativeLayout rl_msg1;
        LinearLayout ll_msg2;
        RoundedCornerImageView rcv1, rcv2;
        ProgressBar progressBar1;
        ImageView iv_error,iv_type,iv_type1;
    }

    public void addDataList(List<ChatMessage> tempChatMsgs){
        synchronized (messages) {
            for (int i = 0;i < tempChatMsgs.size(); i++) {
                messages.add(0, tempChatMsgs.get(i));
            }
            notifyDataSetChanged();
        }

    }

    public interface OnMenuClickListener{
        void onMenuClick(View view,int pos);
    }

}
