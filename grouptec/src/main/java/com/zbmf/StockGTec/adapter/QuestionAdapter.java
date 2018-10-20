package com.zbmf.StockGTec.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zbmf.StockGTec.R;
import com.zbmf.StockGTec.beans.Ask;
import com.zbmf.StockGTec.utils.MessageType;

import java.util.List;

public class QuestionAdapter extends BaseAdapter {
    private List<Ask> info;
    private Context context;
    public static final int TYPE_ANSWERED = 0;
    public static final int TYPE_UNANSWERED = 1;

    public QuestionAdapter(Context mcontext, List<Ask> infolist) {
        this.info = infolist;
        this.context = mcontext;
    }

    @Override
    public int getCount() {
        return info.size();
    }

    @Override
    public Object getItem(int i) {
        return info.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemViewType(int position) {
        if (info.get(position).getAnswerType() == TYPE_UNANSWERED)
            return TYPE_UNANSWERED;
        else
            return TYPE_ANSWERED;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        final Ask ask = info.get(i);
        UnViewHolder holder = null;
        AnViewHolder anViewHolder = null;
        int viewType = getItemViewType(i);
        if (viewType == TYPE_UNANSWERED) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_q, null);
                holder = new UnViewHolder();
                holder.tv_time1 = (TextView) convertView.findViewById(R.id.tv_time1);
                holder.tv_nickname = (TextView) convertView.findViewById(R.id.tv_nickname);
                holder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
                holder.iv_del = (ImageView) convertView.findViewById(R.id.iv_del);
                holder.iv_private = (ImageView) convertView.findViewById(R.id.iv_private);
                holder.iv_fans = (ImageView) convertView.findViewById(R.id.iv_fans);
                holder.ll_top = (LinearLayout) convertView.findViewById(R.id.ll_top);
                convertView.setTag(holder);
            } else {
                holder = (UnViewHolder) convertView.getTag();
            }
            holder.tv_time1.setText(ask.getPosted_at());
            holder.tv_content.setText(ask.getAsk_content());
            holder.iv_del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.delete(ask.getAsk_id());
                }
            });
            holder.tv_nickname.setText(ask.getNickname());
            if (ask.isFirst())
                holder.ll_top.setVisibility(View.VISIBLE);
            else
                holder.ll_top.setVisibility(View.GONE);
            if (ask.getIs_private() == 1)
                holder.iv_private.setImageResource(R.drawable.privatea);
            else
                holder.iv_private.setImageResource(R.drawable.publica);
            initRoleInfo(holder.iv_fans, ask.getFans_level());
        } else if (viewType == TYPE_ANSWERED) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_answer, null);
                anViewHolder = new AnViewHolder();
                anViewHolder.tv_ask_date = (TextView) convertView.findViewById(R.id.tv_ask_date);
                anViewHolder.tv_nickname = (TextView) convertView.findViewById(R.id.tv_nickname);
                anViewHolder.tv_nickname1 = (TextView) convertView.findViewById(R.id.tv_nickname1);
                anViewHolder.tv_question = (TextView) convertView.findViewById(R.id.tv_question);
                anViewHolder.tv_answer_date = (TextView) convertView.findViewById(R.id.tv_answer_date);
                anViewHolder.tv_answer = (TextView) convertView.findViewById(R.id.tv_answer);
                anViewHolder.iv_del = (ImageView) convertView.findViewById(R.id.iv_del);
                anViewHolder.iv_private = (ImageView) convertView.findViewById(R.id.iv_private);
                anViewHolder.iv_private1 = (ImageView) convertView.findViewById(R.id.iv_private1);
                anViewHolder.iv_fans = (ImageView) convertView.findViewById(R.id.iv_fans);
                anViewHolder.ll_top = (LinearLayout) convertView.findViewById(R.id.ll_top);
                convertView.setTag(anViewHolder);
            } else {
                anViewHolder = (AnViewHolder) convertView.getTag();
            }
            anViewHolder.tv_ask_date.setText(ask.getPosted_at());
            anViewHolder.tv_question.setText(ask.getAsk_content());
            Ask.PostBean post = ask.getPost();
            anViewHolder.tv_answer.setText(post.getPost_content());
            anViewHolder.tv_answer_date.setText(post.getPosted_at());
            if (ask.isFirst())
                anViewHolder.ll_top.setVisibility(View.VISIBLE);
            else
                anViewHolder.ll_top.setVisibility(View.GONE);

            anViewHolder.tv_nickname.setText(ask.getNickname());
            if (ask.getIs_private() == 1)
                anViewHolder.iv_private.setImageResource(R.drawable.privatea);
            else
                anViewHolder.iv_private.setImageResource(R.drawable.publica);
            anViewHolder.iv_del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.delete(ask.getAsk_id());
                }
            });
            anViewHolder.tv_nickname1.setText(post.getNickname());
            if (post.getIs_private() == 1)
                anViewHolder.iv_private1.setImageResource(R.drawable.privatea);
            else
                anViewHolder.iv_private1.setImageResource(R.drawable.publica);
            initRoleInfo(anViewHolder.iv_fans, ask.getFans_level());
        }
        return convertView;
    }

    private void initRoleInfo(ImageView iv_type, int role) {
        if (role == MessageType.ROLE_100) {
            iv_type.setVisibility(View.VISIBLE);
            iv_type.setBackgroundResource(R.drawable.quzhu);
        } else if (role == MessageType.ROLE_50) {
            iv_type.setVisibility(View.VISIBLE);
            iv_type.setImageResource(R.drawable.guanli);
        } else if (role == MessageType.ROLE_20) {
            iv_type.setVisibility(View.VISIBLE);
            iv_type.setImageResource(R.drawable.year);
        } else if (role == MessageType.ROLE_10) {
            iv_type.setVisibility(View.VISIBLE);
            iv_type.setImageResource(R.drawable.tie);
        } else if (role == MessageType.ROLE_5) {
            iv_type.setVisibility(View.VISIBLE);
            iv_type.setImageResource(R.drawable.tryf);
        } else {
            iv_type.setVisibility(View.GONE);
        }
    }

    static class UnViewHolder {
        TextView tv_time1, tv_content, tv_nickname;
        ImageView iv_del, iv_private, iv_fans;
        LinearLayout ll_top;
    }

    static class AnViewHolder {
        TextView tv_ask_date, tv_question, tv_answer_date, tv_answer, tv_nickname,tv_nickname1;
        ImageView iv_del, iv_private, iv_fans,iv_private1;
        LinearLayout ll_top;
    }

    private DelItemListener mListener;
    public void setListener(DelItemListener delItemListener){
        mListener = delItemListener;
    }

    public interface DelItemListener{
        void delete(String askId);
    }

}
