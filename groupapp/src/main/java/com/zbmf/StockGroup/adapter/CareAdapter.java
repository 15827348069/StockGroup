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

public class CareAdapter extends RecyclerView.Adapter<CareAdapter.ViewHolder>{
    private List<Group>info;
    private Context mContext;
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
    private int red,gray;
    public CareAdapter(Context context, List<Group> datats)
    {
        mContext=context;
        mInflater = LayoutInflater.from(context);
        info = datats;
        red=context.getResources().getColor(R.color.colorAccent);
        gray=context.getResources().getColor(R.color.black_99);
    }
    public static  class ViewHolder extends RecyclerView.ViewHolder
    {
        public ViewHolder(View view)
        {
            super(view);
            avatar= (RoundedCornerImageView) view.findViewById(R.id.home_teacher_avatar);
            name= (TextView) view.findViewById(R.id.home_teacher_name);
            live_number= (TextView) view.findViewById(R.id.live_number);
            chat_number= (TextView) view.findViewById(R.id.chat_number);

        }
        RoundedCornerImageView avatar;
        TextView name,live_number,chat_number;
    }

    @Override
    public int getItemCount()
    {
        return info.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View view = mInflater.inflate(R.layout.item_care_teacher, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i)
    {
        Group group=info.get(i);
//        ViewFactory.imgCircleView(mContext,group.getAvatar(),viewHolder.avatar);
        ImageLoader.getInstance().displayImage(group.getAvatar(),viewHolder.avatar, ImageLoaderOptions.AvatarOptions());
        viewHolder.name.setText(group.getNick_name());
        viewHolder.live_number.setTextColor(group.getUnredcount()!=0?red:gray);
        viewHolder.chat_number.setTextColor(group.getChat()!=0?red:gray);
        viewHolder.live_number.setSelected(group.getUnredcount()!=0);
        viewHolder.chat_number.setSelected(group.getChat()!=0);
        viewHolder.live_number.setText(group.getUnredcount()+"");
        viewHolder.chat_number.setText(group.getChat()+"");
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