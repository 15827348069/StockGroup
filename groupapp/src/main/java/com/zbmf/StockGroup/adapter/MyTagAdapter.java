package com.zbmf.StockGroup.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;


import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.beans.TagBean;
import com.zbmf.StockGroup.constans.Constants;
import com.zbmf.StockGroup.utils.SettingDefaultsManager;

import java.util.List;


public class MyTagAdapter extends RecyclerView.Adapter<MyTagAdapter.ViewHolder> {

    private List<TagBean> tagList;
    private Context context;

    public interface OnItemClickLitener
    {
        void onParenItemClick(TagBean.ChildrenTag childrenTag, int position,boolean isCheck);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public MyTagAdapter(Context context,List<TagBean> tagList) {
        this.tagList = tagList;
        this.context=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tag, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final TagBean tagBean=tagList.get(position);
        holder.mTextView.setText(tagBean.getTag_name());
        holder.itemView.setTag(tagBean);
        if(position==0&&tagBean.getTag_name().endsWith("搜索记录")){
            holder.imbSearchDelete.setVisibility(View.VISIBLE);
            holder.imbSearchDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SettingDefaultsManager.getInstance().clearSearchHistory();
                }
            });
        }
        GridLayoutManager layoutManage=new GridLayoutManager(context,3);
        holder.children_tag.setLayoutManager(layoutManage);
        layoutManage.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (tagBean.getData().get(position).getName().length()> Constants.MAX)
                    return 2;
                return 1;
            }
        });
        ChildrenTagAdapter tagAdapter = new ChildrenTagAdapter(context,tagBean.getData());
        if(mOnItemClickLitener!=null){
            tagAdapter.setOnItemClickLitener(new ChildrenTagAdapter.OnItemClickLitener() {
                @Override
                public void onItemClick(TagBean.ChildrenTag childrenTag,boolean select) {
                    mOnItemClickLitener.onParenItemClick(childrenTag,position,select);
                }
            });
        }
        holder.children_tag.setAdapter(tagAdapter);
    }

    @Override
    public int getItemCount() {
        return tagList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public RecyclerView children_tag;
        public ImageButton imbSearchDelete;
        public ViewHolder(View view) {
            super(view);
            mTextView = (TextView) view.findViewById(R.id.tag_title);
            children_tag= (RecyclerView) view.findViewById(R.id.item_tag_rv);
            imbSearchDelete= (ImageButton) view.findViewById(R.id.imb_search_delete);
        }
    }
}
