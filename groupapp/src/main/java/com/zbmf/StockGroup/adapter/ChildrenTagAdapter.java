package com.zbmf.StockGroup.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.beans.TagBean;

import java.util.List;


public class ChildrenTagAdapter extends RecyclerView.Adapter<ChildrenTagAdapter.ViewHolder> {

    private List<TagBean.ChildrenTag> tagList;
    private Context context;

    public interface OnItemClickLitener {
        void onItemClick(TagBean.ChildrenTag childrenTag, boolean select);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }


    public ChildrenTagAdapter(Context context, List<TagBean.ChildrenTag> tagList) {
        this.tagList = tagList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tag_layout, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mTextView.setText(tagList.get(position).getName());
        holder.mTextView.setSelected(tagList.get(position).isSelect());
        if (tagList.get(position).isSelect()) {
            holder.mTextView.setBackgroundResource(R.drawable.tag_checked_bg);
            holder.mTextView.setTextColor(context.getResources().getColor(R.color.colorAccent));
        } else {
            holder.mTextView.setBackgroundResource(R.drawable.tag_normal_bg);
            holder.mTextView.setTextColor(context.getResources().getColor(R.color.black_33));
        }
        if (mOnItemClickLitener != null) {
            holder.mTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setChecked(position);
                    mOnItemClickLitener.onItemClick(tagList.get(position), tagList.get(position).isSelect());
                }
            });
        }
        if (tagList.get(position).getIs_hot()) {
            Drawable drawable = context.getResources().getDrawable(R.drawable.icon_hot);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.mTextView.setCompoundDrawables(drawable, null, null, null);
        } else {
            holder.mTextView.setCompoundDrawables(null, null, null, null);
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
        for (TagBean.ChildrenTag tag : tagList) {
            tag.setSelect(false);
        }
        tagList.get(position).setSelect(!ischeck);
        notifyDataSetChanged();
    }

}
