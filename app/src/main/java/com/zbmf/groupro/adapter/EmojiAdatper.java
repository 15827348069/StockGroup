package com.zbmf.groupro.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.zbmf.groupro.R;
import com.zbmf.groupro.utils.Constants;

import java.util.List;

/**
 * Created by iMac on 2016/12/22.
 */

public class EmojiAdatper extends BaseAdapter {

    private List<String> emojiNames;
    private Context mContext;
    private int itemWidth;
    private int padding;

    public EmojiAdatper(Context context, List<String> emojiNames, int itemWidth) {
        mContext = context;
        this.emojiNames = emojiNames;
        this.itemWidth = itemWidth;
        padding = itemWidth / 8;
    }

    @Override
    public int getCount() {
        return emojiNames.size();
    }

    @Override
    public Object getItem(int position) {
        return emojiNames.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FrameLayout f = new FrameLayout(mContext);
        ImageView iv = new ImageView(mContext);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(itemWidth, itemWidth);
        if (position % 7 == 0)
            params.setMargins(padding, 0, 0, 0);
        else if ((position + 1) % 7 == 0)
            params.setMargins(-padding, 0, 0, 0);
        else
            params.setMargins(0, 0, 0, 0);
        iv.setPadding(padding, padding, padding, padding);
        iv.setLayoutParams(params);
        iv.setBackgroundResource(R.drawable.emoji_click);
        iv.setImageResource(Constants.getEmojiIconMaps().get(emojiNames.get(position)));
        f.addView(iv);
        return f;
    }
}

