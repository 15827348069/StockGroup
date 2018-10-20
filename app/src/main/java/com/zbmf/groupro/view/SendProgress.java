package com.zbmf.groupro.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.zbmf.groupro.R;


/**
 * Created by xuhao on 2017/1/12.
 */

public class SendProgress extends RelativeLayout implements View.OnClickListener{
    private ImageView ivWheel1,ivWheel2;
    private ImageView ivRider;
    private ImageView ivSun,ivBack1,ivBack2;
    private Animation wheelAnimation,sunAnimation;
    private Animation backAnimation1,backAnimation2;
    private Button add_more;
    private RelativeLayout send_gift_layout_id;
    private OnSendClickListener clickListener;//点击回调
    public SendProgress(Context context) {
        super(context);
        init(context);
    }

    public SendProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public SendProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    private void init(Context context){
        View layout = LayoutInflater.from(context).inflate(R.layout.send_progress_layout, null);
        addView(layout, -1, -2);
        ivRider = (ImageView) findViewById(R.id.iv_rider);
        ivSun = (ImageView) findViewById(R.id.ivsun);
        ivWheel1 = (ImageView) findViewById(R.id.wheel1);
        ivWheel2 = (ImageView) findViewById(R.id.wheel2);
        ivBack1 = (ImageView) findViewById(R.id.iv_back1);
        ivBack2 = (ImageView) findViewById(R.id.iv_back2);
        add_more= (Button) findViewById(R.id.add_more);
        send_gift_layout_id= (RelativeLayout) findViewById(R.id.send_gift_layout_id);
        send_gift_layout_id.setEnabled(false);
        send_gift_layout_id.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //点击动画不执行操作，覆盖itemclicks事件
            }
        });
        add_more.setOnClickListener(this);
        add_more.setEnabled(true);
        //获取动画
        wheelAnimation = AnimationUtils.loadAnimation(context, R.anim.send_gift_progress_ami);
        sunAnimation = AnimationUtils.loadAnimation(context, R.anim.send_gift_progress_ami);

        backAnimation1 = AnimationUtils.loadAnimation(context, R.anim.send_gift_image);
        backAnimation2 = AnimationUtils.loadAnimation(context, R.anim.send_gift_progress);

    }
    /** * 开启动画 */
    public void startAnim(){
        ivBack1.startAnimation(backAnimation1);
        ivBack2.startAnimation(backAnimation2);
        ivSun.startAnimation(sunAnimation);
        ivWheel1.startAnimation(wheelAnimation);
        ivWheel2.startAnimation(wheelAnimation);
        add_more.setVisibility(View.GONE);
        send_gift_layout_id.setVisibility(View.VISIBLE);
    }
    /** * 关闭动画 */
    public void stopAnim(){
        add_more.setVisibility(View.VISIBLE);
        send_gift_layout_id.setVisibility(View.GONE);
        ivBack1.clearAnimation();
        ivBack2.clearAnimation();
        ivSun.clearAnimation();
        ivWheel1.clearAnimation();
        ivWheel2.clearAnimation();
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
