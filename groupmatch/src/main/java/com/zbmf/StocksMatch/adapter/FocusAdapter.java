package com.zbmf.StocksMatch.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.beans.Group;
import com.zbmf.StocksMatch.widget.CircleImageView;

/**
 * Created by lulu on 16/1/9.
 */
public class FocusAdapter extends ListAdapter<Group>{

    private ImageLoader imageLoader;
    DisplayImageOptions options;
    public FocusAdapter(Activity context) {
        super(context);
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.default_avatar)
                .showImageForEmptyUri(R.drawable.default_avatar)
                .showImageOnFail(R.drawable.default_avatar)
                .cacheInMemory(true).cacheOnDisc(true)
                .bitmapConfig(Bitmap.Config.ARGB_8888) // 设置图片的解码类型
                .displayer(new RoundedBitmapDisplayer(20)).build();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView != null){
            holder = (ViewHolder) convertView.getTag();
        }else{
            convertView = LayoutInflater.from(mContext).inflate(R.layout.focus_item, null);
            holder = new ViewHolder();
            holder.tv_username = (TextView) convertView.findViewById(R.id.tv_username);
            holder.civ = (CircleImageView)convertView.findViewById(R.id.civ);
            holder.iv_right = (ImageView) convertView.findViewById(R.id.iv_right);
            convertView.setTag(holder);
        }
        final Group group =  mList.get(position);

        holder.tv_username.setText(group.getName());
            imageLoader.displayImage(group.getAvatar(),holder.civ,options);

        holder.iv_right.setVisibility(View.GONE);
        return convertView;
    }


    static class ViewHolder{
        TextView tv_username;
        CircleImageView civ;
        ImageView iv_right;
    }


}
