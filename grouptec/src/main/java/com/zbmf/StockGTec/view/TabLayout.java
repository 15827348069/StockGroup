package com.zbmf.StockGTec.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zbmf.StockGTec.R;


public class TabLayout extends LinearLayout {
    private Context mContext;
    private LayoutInflater mInflater;
    private String[] mTitles;
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

    public void setData(String[] titles) {
        mTitles = titles;
        layoutViews();

    }

    private void layoutViews() {

        if (mTitles != null && mTitles.length != 0) {
            for (int i = 0; i < mTitles.length; i++) {
                final View childView = mInflater.inflate(R.layout.tab_layout_item, this, false);
                final TextView titleView = (TextView) childView.findViewById(R.id.hotel_tab_tv_title);
//                TextView tvLine = (TextView) childView.findViewById(R.id.tv_line);
                if (i == 0) {
                    titleView.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
                    childView.setBackgroundResource( R.drawable.shape_layout_left);
//                    tvLine.setVisibility(VISIBLE);
                }else{
                    childView.setBackgroundResource( R.drawable.shape_layout_right_nomal);
                }
                titleView.setText(mTitles[i]);
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
                TextView titleView = (TextView) mTitleViews.get(key);
                View lineView =  mLineViews.get(key);
                titleView.setTextColor(position == key ? mContext.getResources().getColor(R.color.colorAccent) :
                        mContext.getResources().getColor(R.color.white));
                if(position==0){
                    lineView.setBackgroundResource(position == key ? R.drawable.shape_layout_left: R.drawable.shape_layout_right_nomal);
                }else{
                    lineView.setBackgroundResource(position == key ? R.drawable.shape_layout_right: R.drawable.shape_layout_left_nomal);
                }
//                lineView.setVisibility(position == key ? VISIBLE : GONE);
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

    public interface OnTabSelectListener {
        void onTabSelect(int position);
    }


}
