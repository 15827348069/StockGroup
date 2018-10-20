package com.zbmf.StockGroup.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.beans.StockModeMenu;
import com.zbmf.StockGroup.beans.TagBean;
import com.zbmf.StockGroup.utils.DateUtil;

import java.util.List;


public class StockModeTimeAdapter extends RecyclerView.Adapter<StockModeTimeAdapter.ViewHolder> {

    private List<StockModeMenu.Time> tagList;
    private Context context;

    public interface OnItemClickLitener {
        void onItemClick(StockModeMenu.Time time, boolean select);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }


    public StockModeTimeAdapter(Context context, List<StockModeMenu.Time> tagList) {
        this.tagList = tagList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_stock_mode_time_tag_layout, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mTextView.setText(DateUtil.getModeTime(tagList.get(position).getTime()));
        holder.mTextView.setSelected(tagList.get(position).isSelect());
        holder.mTextView.setTextColor(tagList.get(position).isSelect()?context.getResources().getColor(R.color.colorAccent):context.getResources().getColor(R.color.black_33));
        holder.mTextView.setSelected(tagList.get(position).isSelect());
        if (mOnItemClickLitener != null) {
            holder.mTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setChecked(position);
                    mOnItemClickLitener.onItemClick(tagList.get(position), tagList.get(position).isSelect());
                }
            });
        }

        holder.itemView.setTag(tagList.get(position));
    }

    @Override
    public int getItemCount() {
        return tagList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;

        public ViewHolder(View view) {
            super(view);
            mTextView = (TextView) view.findViewById(R.id.tag_tv);
        }
    }

    private void setChecked(int position) {
        boolean ischeck = tagList.get(position).isSelect();
        for (StockModeMenu.Time tag : tagList) {
            tag.setSelect(false);
        }
        tagList.get(position).setSelect(!ischeck);
        notifyDataSetChanged();
    }

}
