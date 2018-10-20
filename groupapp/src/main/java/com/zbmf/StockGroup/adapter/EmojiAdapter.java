package com.zbmf.StockGroup.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;


import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.constans.Constants;

import java.util.List;

/**
 * Created by iMac on 2016/12/31.
 */

public class EmojiAdapter extends BaseAdapter {
    private Context mContext;
    private List<String> emojiNames;
    private int itemWidth;

    public EmojiAdapter(Context context, List<String> emojiNames, int itemWidth) {
        this.mContext = context;
        this.emojiNames = emojiNames;
        this.itemWidth = itemWidth;
    }

    @Override
    public int getCount() {
        return emojiNames.size() + 1;
    }

    @Override
    public Object getItem(int i) {
        return emojiNames.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        FrameLayout f = new FrameLayout(mContext);
        ImageView iv = new ImageView(mContext);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(itemWidth, itemWidth);
        iv.setPadding(itemWidth / 8, itemWidth / 8, itemWidth / 8, itemWidth / 8);
        iv.setLayoutParams(params);


        if (position == getCount() - 1) {//添加回退按钮
            iv.setImageResource(R.drawable.del_seletor);
            iv.setBackgroundResource(R.color.transparent);
        } else {//添加正常表情
            iv.setImageResource(Constants.getEmojiIconMaps().get(emojiNames.get(position)));
            iv.setBackgroundResource(R.drawable.emoji_click);
        }
        iv.setLayoutParams(params);
        f.addView(iv);
        return f;
    }
}
