package com.zbmf.StockGroup.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.adapter.EmailAdapter;
import com.zbmf.StockGroup.adapter.EmojiAdapter;
import com.zbmf.StockGroup.constans.Constants;
import com.zbmf.StockGroup.utils.DisplayUtil;

import java.util.ArrayList;
import java.util.List;


public class EmojiView extends LinearLayout{
    private Context cxt;
    private OnItemClickListener mOnItemClickListener;
    public EmojiView(Context context) {
        super(context);
        initView(context);
    }

    public EmojiView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public EmojiView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void initView(Context cxt){
        this.cxt = cxt;
        View root = View.inflate(cxt, R.layout.show_emoji_layout,null);
        getEmojiView(root);
        addView(root, -1, -2);
    }

    private void getEmojiView(View view){
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        final LinearLayout ll_dot = (LinearLayout)view.findViewById(R.id.ll_dot);
        int item_spacing = DisplayUtil.dip2px(cxt, 8);
        int width = DisplayUtil.getScreenWidthPixels(cxt);
        int itemWidth = (width - item_spacing * 8) / 7;
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

        EmailAdapter adapter1 = new EmailAdapter(gridviews);
        viewPager.setAdapter(adapter1);

        for (int i = 0; i < gridviews.size(); i++) {
            ImageView point = new ImageView(cxt);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            params.rightMargin = 10;
            if (i == gridviews.size() - 1)
                params.rightMargin = 0;
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
    }

    @NonNull
    private GridView getGridView(int itemWidth, List<String> temps) {
        GridView gv = (GridView) View.inflate(cxt, R.layout.emoji_layout, null);
        EmojiAdapter emojiAdapter1 = new EmojiAdapter(cxt, temps, itemWidth);
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
