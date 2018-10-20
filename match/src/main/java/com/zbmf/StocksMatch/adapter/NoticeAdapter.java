package com.zbmf.StocksMatch.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.bean.NoticeBean;
import com.zbmf.worklibrary.adapter.ListAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pq
 * on 2018/4/12.
 */

public class NoticeAdapter extends ListAdapter<NoticeBean.Result.Announcements> {
    public NoticeAdapter(Activity context) {
        super(context);
    }

    @Override
    protected int getLayout() {
        return R.layout.notice_item;
    }

    @Override
    public View getHolderView(int position, View convertView, NoticeBean.Result.Announcements announcements) {
        ViewHolder holder=(ViewHolder) convertView.getTag();
        if (holder==null){
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        Log.e(">>",""+announcements.getSubject());
        holder.noticeTitle.setText(announcements.getSubject());
        holder.noticeTime.setText(announcements.getPosted_at());
        return convertView;
    }
    public class ViewHolder{
        @BindView(R.id.noticeTitle)
        TextView noticeTitle;
        @BindView(R.id.noticeTime)
        TextView noticeTime;
        public ViewHolder(View view){
            ButterKnife.bind(this,view);
        }
    }
}
