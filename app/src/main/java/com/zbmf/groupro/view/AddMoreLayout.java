package com.zbmf.groupro.view;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.zbmf.groupro.R;


/**
 * Created by xuhao on 2017/1/12.
 */

public class AddMoreLayout extends RelativeLayout implements View.OnClickListener{
    private ImageView ivSun;
    private Button add_more;
    private RelativeLayout send_gift_layout_id;
    private OnSendClickListener clickListener;//点击回调
    public AddMoreLayout(Context context) {
        super(context);
        init(context);
    }

    public AddMoreLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public AddMoreLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    private void init(Context context){
        View layout = LayoutInflater.from(context).inflate(R.layout.add_more_button, null);
        addView(layout, -1, -2);
        ivSun = (ImageView) findViewById(R.id.loading_image);
        send_gift_layout_id= (RelativeLayout) findViewById(R.id.add_more_rea);
        send_gift_layout_id.setEnabled(false);
        send_gift_layout_id.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //点击动画不执行操作，覆盖itemclicks事件
            }
        });
        add_more= (Button) findViewById(R.id.add_more);
        add_more.setOnClickListener(this);
        add_more.setEnabled(true);
        //获取动画
    }
    /** * 开启动画 */
    public void startAnim(){
        AnimationDrawable animationDrawable = (AnimationDrawable) ivSun.getDrawable();
        animationDrawable.start();
        add_more.setVisibility(View.GONE);
        send_gift_layout_id.setVisibility(View.VISIBLE);
    }
    /** * 关闭动画 */
    public void stopAnim(){
        add_more.setVisibility(View.VISIBLE);
        send_gift_layout_id.setVisibility(View.GONE);
        AnimationDrawable animationDrawable = (AnimationDrawable) ivSun.getDrawable();
        animationDrawable.stop();
    }
    public void addAllMessage(){
        stopAnim();
        add_more.setText(getResources().getString(R.string.already_add_all));
        add_more.setEnabled(false);
    }
    @Override
    public void onClick(View view) {
        if(clickListener==null){
            return;
        }
        startAnim();
        clickListener.OnSendClickListener(view);
    }

    public interface OnSendClickListener {
        void OnSendClickListener(View view);
    }
    public void setSendClickListener(OnSendClickListener listener) {
        clickListener = listener;
    }
}
