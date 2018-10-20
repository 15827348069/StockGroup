package com.zbmf.groupro.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zbmf.groupro.R;
import com.zbmf.groupro.beans.Group;
import com.zbmf.groupro.utils.ImageLoaderOptions;
import com.zbmf.groupro.view.RoundedCornerImageView;

import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder>{
    private List<Group>info;
    public interface OnItemClickLitener
    {
        void onItemClick(View view, int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    private LayoutInflater mInflater;
    public GalleryAdapter(Context context, List<Group> datats)
    {
        mInflater = LayoutInflater.from(context);
        info = datats;
    }
    public static  class ViewHolder extends RecyclerView.ViewHolder
    {
        public ViewHolder(View view)
        {
            super(view);
            avatar= (RoundedCornerImageView) view.findViewById(R.id.home_teacher_avatar);
            name= (TextView) view.findViewById(R.id.home_teacher_name);
            desc= (TextView) view.findViewById(R.id.home_teacher_desc);
        }
        RoundedCornerImageView avatar;
        TextView name,desc;
    }

    @Override
    public int getItemCount()
    {
        return info.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View view = mInflater.inflate(R.layout.home_teacher_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i)
    {
        Group group=info.get(i);
        ImageLoader.getInstance().displayImage(group.getAvatar(),viewHolder.avatar, ImageLoaderOptions.AvatarOptions());
        viewHolder.name.setText(group.getNick_name());
        viewHolder.desc.setText(group.getDescription());
        if (mOnItemClickLitener != null)
        {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    mOnItemClickLitener.onItemClick(viewHolder.itemView, i);
                }
            });

        }

    }

}