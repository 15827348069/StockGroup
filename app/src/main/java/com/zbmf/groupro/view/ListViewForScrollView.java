package com.zbmf.groupro.view;



import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.zbmf.groupro.R;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class ListViewForScrollView extends ListView {
    private View more_view;
    private AddMoreLayout add_more_layout;
    public ListViewForScrollView(Context context) {
        super(context);
        init(context);
        if (Integer.parseInt(Build.VERSION.SDK) >= 9) {
            this.setOverScrollMode(View.OVER_SCROLL_NEVER);
        }

    }
    public ListViewForScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        if (Integer.parseInt(Build.VERSION.SDK) >= 9) {
            this.setOverScrollMode(View.OVER_SCROLL_NEVER);
        }
    }
    public ListViewForScrollView(Context context, AttributeSet attrs,
                                 int defStyle) {
        super(context, attrs, defStyle);
        init(context);
        if (Integer.parseInt(Build.VERSION.SDK) >= 9) {
            this.setOverScrollMode(View.OVER_SCROLL_NEVER);
        }
    }
    @Override
    /**
     * 重写该方法，达到使ListView适应ScrollView的效果
     */
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
    public void init(Context context){
        more_view= LayoutInflater.from(context).inflate(R.layout.list_foot_view,null);
        add_more_layout= (AddMoreLayout) more_view.findViewById(R.id.add_more_layout);
    }
    public void addFootView(AddMoreLayout.OnSendClickListener listener){
        add_more_layout.setSendClickListener(listener);
        addFooterView(more_view);
    }
    public void onLoad(){
        add_more_layout.startAnim();
    }
    public void onStop(){
        add_more_layout.stopAnim();
    }
    public void addAllData(){
        add_more_layout.addAllMessage();
    }


}