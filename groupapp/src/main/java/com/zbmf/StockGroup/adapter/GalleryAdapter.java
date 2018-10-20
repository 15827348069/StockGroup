package com.zbmf.StockGroup.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.beans.Group;
import com.zbmf.StockGroup.utils.ImageLoaderOptions;
import com.zbmf.StockGroup.view.RoundedCornerImageView;

import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {
    private List<Group> info;

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);

        void onAskCilck(Group group);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    private LayoutInflater mInflater;
    private Context mContext;
    public GalleryAdapter(Context context, List<Group> datats) {
        mContext=context;
        mInflater = LayoutInflater.from(context);
        info = datats;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View view) {
            super(view);
            avatar = (RoundedCornerImageView) view.findViewById(R.id.home_teacher_avatar);
            name = (TextView) view.findViewById(R.id.home_teacher_name);
            tv_ask_stock = (TextView) view.findViewById(R.id.tv_ask_stock);
        }

        RoundedCornerImageView avatar;
        TextView name, tv_ask_stock;
    }

    @Override
    public int getItemCount() {
        return info.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.home_teacher_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        final Group group = info.get(i);
//        ViewFactory.imgCircleView(mContext,group.getAvatar(), viewHolder.avatar);
        ImageLoader.getInstance().displayImage(group.getAvatar(), viewHolder.avatar, ImageLoaderOptions.AvatarOptions());
        viewHolder.name.setText(group.getNick_name());
        viewHolder.tv_ask_stock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickLitener != null) {
                    mOnItemClickLitener.onAskCilck(group);
                }
            }
        });

        viewHolder.avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickLitener != null) {
                    mOnItemClickLitener.onItemClick(viewHolder.itemView, i);
                }
            }
        });


    }

}