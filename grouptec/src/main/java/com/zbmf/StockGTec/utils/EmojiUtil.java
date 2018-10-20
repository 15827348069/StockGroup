package com.zbmf.StockGTec.utils;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zbmf.StockGTec.R;
import com.zbmf.StockGTec.adapter.EmojiAdapter1;
import com.zbmf.StockGTec.adapter.GiftAdapter1;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iMac on 2017/1/6.
 */

public class EmojiUtil {

    private Activity cxt;
    private final View root;
    private ViewPager viewPager;
    private LinearLayout ll_dot;
    private final int itemWidth;
    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public EmojiUtil(Activity cxt){
        this.cxt = cxt;
        root = View.inflate(cxt, R.layout.show_emoji_layout,null);
        viewPager = (ViewPager) root.findViewById(R.id.viewpager);
        ll_dot = (LinearLayout)root.findViewById(R.id.ll_dot);

        int item_spacing = DisplayUtil.dip2px(cxt, 8);
        int width = DisplayUtil.getScreenWidthPixels(cxt);
        itemWidth = (width - item_spacing * 8) / 7;
    }

    public View getEmojiView(){
        List<GridView> gridviews = new ArrayList<>();
        List<String> temps = new ArrayList<>();
        for (String name : Constants.getEmojiIconMaps().keySet()) {
            temps.add(name);
            if (temps.size() == 20) {
                gridviews.add(getGridView(itemWidth, temps));
                temps = new ArrayList<>();
            }
        }

        if (temps.size() != 0) {
            gridviews.add(getGridView(itemWidth, temps));
        }

        GiftAdapter1 adapter1 = new GiftAdapter1(cxt, gridviews);
        viewPager.setAdapter(adapter1);

        for (int i = 0; i < gridviews.size(); i++) {
            ImageView point = new ImageView(cxt);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            params.rightMargin = 10;
            if (i == gridviews.size() - 1)
                params.rightMargin = 0;
//          point.setBackgroundResource(R.drawable.point_bg);
            if (i == 0) {
                point.setImageResource(R.drawable.selp);
            } else {
                point.setImageResource(R.drawable.unselp);
            }
            ll_dot.addView(point, params);
        }

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int childCount = ll_dot.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    ImageView view = (ImageView) ll_dot.getChildAt(i);
                    if (i == position) {
                        view.setImageResource(R.drawable.selp);
                    } else {
                        view.setImageResource(R.drawable.unselp);
                    }
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return root;
    }

    @NonNull
    private GridView getGridView(int itemWidth, List<String> temps) {
        GridView gv = (GridView) View.inflate(cxt, R.layout.emoji_layout, null);
        EmojiAdapter1 emojiAdapter1 = new EmojiAdapter1(cxt, temps, itemWidth);
        gv.setAdapter(emojiAdapter1);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(mOnItemClickListener!=null){
                    mOnItemClickListener.onItemClick(parent,view,position,id);
                }
            }
        });
        return gv;
    }

    public interface OnItemClickListener{
        void onItemClick(AdapterView<?> parent, View view, int position, long id);
    }

}
