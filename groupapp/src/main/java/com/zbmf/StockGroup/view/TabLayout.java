package com.zbmf.StockGroup.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zbmf.StockGroup.R;

import java.util.List;


public class TabLayout extends LinearLayout implements ViewPager.OnPageChangeListener {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<String> mTitles;
    private OnTabSelectListener mListener;//点击回调
    private SparseArray<View> mTitleViews;//保存一下我们的textView;
    private SparseArray<View> mLineViews;//保存一下我们的背景图


    public TabLayout(Context context) {
        this(context, null);

    }

    public TabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mTitleViews = new SparseArray<>();
        mLineViews = new SparseArray<>();
        this.setOrientation(HORIZONTAL);
        this.setGravity(Gravity.CENTER_HORIZONTAL);
    }

    public void setTabSelectListener(OnTabSelectListener listener) {
        mListener = listener;
    }

    public void setData(List<String>titles) {
        mTitles = titles;
        layoutViews();

    }

    private void layoutViews() {

        if (mTitles != null && mTitles.size() != 0) {
            for (int i = 0; i < mTitles.size(); i++) {
                final View childView = mInflater.inflate(R.layout.tab_layout_item, this, false);
                final CheckBox titleView = (CheckBox) childView.findViewById(R.id.hotel_tab_tv_title);
                if (i == 0) {
                    childView.setBackgroundResource( R.drawable.tab_left_background);
                    titleView.setChecked(true);
                    childView.setSelected(true);
                }else if(i==mTitles.size()-1){
                    childView.setBackgroundResource( R.drawable.tab_right_background);
                }else{
                    childView.setBackgroundResource( R.drawable.tab_center_background);
                }
                titleView.setText(mTitles.get(i));
                this.addView(childView);
                final int position = i;
                childView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListener != null) {
                            mListener.onTabSelect(position);
                            setTabTitleColor(position);
                        }
                    }
                });
                mTitleViews.put(i, titleView);
                mLineViews.put(i, childView);
            }

        }


    }

    private void setTabTitleColor(int position) {

        if (mTitleViews != null && mTitleViews.size() != 0) {
            for (int key = 0; key < mTitleViews.size(); key++) {
                CheckBox titleView = (CheckBox) mTitleViews.get(key);
                View lineView =  mLineViews.get(key);
                titleView.setChecked(position==key);
                lineView.setSelected(position == key);
            }
        }


    }

    public void setTabSelect(int position) {
        setTabTitleColor(position);
        if (mListener != null) {
            mListener.onTabSelect(position);
        }
    }


    public void setRedPointVisible(int position) {
        View childView = this.getChildAt(position);
        TextView billView = (TextView) childView.findViewById(R.id.hotel_tab_tv_first_bill);
        billView.setVisibility(VISIBLE);
    }
    public void setRedPointMessage(int position,int message){
        View childView = this.getChildAt(position);
        TextView billView = (TextView) childView.findViewById(R.id.hotel_tab_tv_first_bill);
        if(message>99){
            billView.setText(String.valueOf("99+"));
        }else{
            billView.setText(String.valueOf(message));
        }
    }
    public void setRedPointGone(int position) {
        View childView = this.getChildAt(position);
        View billView = childView.findViewById(R.id.hotel_tab_tv_first_bill);
        billView.setVisibility(INVISIBLE);

    }

    public void setupWithViewPager(ViewPager viewpager) {
        viewpager.setOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        setTabSelect(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public interface OnTabSelectListener {
        void onTabSelect(int position);
    }


}
