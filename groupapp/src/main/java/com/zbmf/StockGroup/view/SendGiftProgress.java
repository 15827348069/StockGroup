package com.zbmf.StockGroup.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.zbmf.StockGroup.R;


/**
 * Created by xuhao on 2017/1/12.
 */

public class SendGiftProgress extends RelativeLayout implements View.OnClickListener{
    private Button Send_Gift_button;
    private WaitingBar waitingBar;
    private OnSendClickListener clickListener;//点击回调
    public SendGiftProgress(Context context) {
        super(context);
        init(context);
    }

    public SendGiftProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public SendGiftProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    private void init(Context context){
        View layout = LayoutInflater.from(context).inflate(R.layout.send_gift_progress_layout, null);
        addView(layout, -1, -2);
        Send_Gift_button= (Button) findViewById(R.id.btn_send_gift);
        waitingBar= (WaitingBar) findViewById(R.id.send_gift_waitting);
        Send_Gift_button.setOnClickListener(this);
    }
    /** * 开启动画 */
    public void startAnim(){
        Send_Gift_button.setVisibility(View.GONE);
        waitingBar.setVisibility(View.VISIBLE);
    }
    /** * 关闭动画 */
    public void stopAnim(){
        waitingBar.setVisibility(View.GONE);
        Send_Gift_button.setVisibility(View.VISIBLE);

    }
    public void setText(String text){
        Send_Gift_button.setText(text);
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
