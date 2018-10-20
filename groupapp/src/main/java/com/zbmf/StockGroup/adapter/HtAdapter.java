package com.zbmf.StockGroup.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.beans.MyTopicData;
import com.zbmf.StockGroup.utils.ImageLoaderOptions;

/**
 * Created by pq
 * on 2018/7/2.
 * 话题Adapter
 */

public class HtAdapter extends ListAdapter<MyTopicData> {
    private HtItemClick mHtItemClick;
    public void setHtItemClick(HtItemClick htItemClick){
        mHtItemClick=htItemClick;
    }
    public HtAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(convertView==null){
            convertView=LayoutInflater.from(parent.getContext()).inflate(R.layout.ht_item, parent, false);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        final MyTopicData topicList = getItem(position);
        holder.mTopicTitle.setText(topicList.getName());
        holder.mTopicName.setText(String.format("#%s",topicList.getTitle()));
        holder.mGdCount.setText(String.format(topicList.getVp_number()+"%s","观点"));
        holder.mGzCount.setText(String.format(topicList.getUsers()+"%s","关注"));
        ImageLoader.getInstance().displayImage(topicList.getImg(),holder.mTopicImg, ImageLoaderOptions.AvatarOptions());
        holder.mHtItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mHtItemClick!=null){
                    mHtItemClick.itemClick(topicList.getTopic_id(),topicList.getImg(),topicList.getStatus());
                }
            }
        });
        return convertView;
    }

    public class ViewHolder {
        private final ImageView mTopicImg;
        private final TextView mTopicName;
        private final TextView mTopicTitle;
        private final TextView mGzCount;
        private final TextView mGdCount;
        private final LinearLayout mHtItem;

        public ViewHolder(View view){
            mTopicImg = view.findViewById(R.id.topicImg);
            mTopicName = view.findViewById(R.id.topicName);
            mTopicTitle = view.findViewById(R.id.topicTitle);
            mGzCount = view.findViewById(R.id.gzCount);
            mGdCount = view.findViewById(R.id.gdCount);
            mHtItem = view.findViewById(R.id.htItem);
        }
    }

    public interface HtItemClick{
        void itemClick(int topic_id,String avatar,int status);
    }
}
