package com.zbmf.StockGroup.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.adapter.DialogQuanAdapter;
import com.zbmf.StockGroup.beans.CouponsOrSystem;
import com.zbmf.StockGroup.beans.Group;
import com.zbmf.StockGroup.interfaces.GainQuan;
import com.zbmf.StockGroup.utils.ShowActivity;
import com.zbmf.StockGroup.view.ListViewForScrollView;

import java.util.List;

/**
 * Created by pq
 * on 2018/6/7.
 */

public class QuanBottomDialog extends Dialog {

    private DialogQuanAdapter mMDialogQuanAdapter;
    private TextView mQuan_tip;
    private ListViewForScrollView mQuan_item_message;
//    private PullToRefreshScrollView mFlowList;

    public QuanBottomDialog(@NonNull Context context) {
        super(context);
    }

    public QuanBottomDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected QuanBottomDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
    public QuanBottomDialog showDialog(Context context, final Activity activity,/*int marginBottom,*/
                                       List<CouponsOrSystem> couponList, final Group group_bean, final GainQuan gainQuan) {
//        if (mDialog == null) {
//            mDialog = new Dialog(this, R.style.quanDialogTheme);
//        }
        View dialogView = LayoutInflater.from(context).inflate(R.layout.quan_dilog_content_view, null);
        //获得dialog的window窗口
        Window window = getWindow();
        //设置dialog在屏幕底部
        assert window != null;
        window.setGravity(Gravity.BOTTOM);
        //设置dialog弹出时的动画效果，从屏幕底部向上弹出
        window.setWindowAnimations(R.style.dialogStyle);
//        DisplayMetrics dm = new DisplayMetrics();
//        window.getWindowManager().getDefaultDisplay().getMetrics(dm);
        window.getDecorView().setPadding(0, 0, 0, 0);
//        window.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.dialog_bottom_bg));
        //获得window窗口的属性
        android.view.WindowManager.LayoutParams lp = window.getAttributes();
        //设置窗口宽度为充满全屏
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        //设置窗口高度为包裹内容
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        Log.i("--TAG","--y:"+y);
        //将设置好的属性set回去
//        window.setAttributes(layoutParams);
        this.setContentView(dialogView,lp);
//        mFlowList = dialogView.findViewById(R.id.flow_list);
        mQuan_item_message = (ListViewForScrollView) dialogView.findViewById(R.id.quan_item_message);
        mQuan_tip = (TextView)dialogView.findViewById(R.id.quan_tip);
//        View transparentView = dialogView.findViewById(R.id.transparentView);
        mQuan_tip.setVisibility(View.GONE);
        mQuan_item_message.setVisibility(View.VISIBLE);
        mMDialogQuanAdapter = new DialogQuanAdapter(context);
        mMDialogQuanAdapter.setList(couponList);
        mQuan_item_message.setAdapter(mMDialogQuanAdapter);
        mMDialogQuanAdapter.setGainOrUseBtnClick(new DialogQuanAdapter.GainOrUseBtn() {
            @Override
            public void mGainOrUseBtn(CouponsOrSystem couponsOrSystem ) {
                //先领取，领取成功在跳转
                int is_taken = couponsOrSystem.getIs_taken();
                if (is_taken==0){
                    int coupon_id = couponsOrSystem.getCoupon_id();
                    if (gainQuan!=null){
                        gainQuan.gainQuanI(coupon_id);
                    }
                }else {
                    ShowActivity.showAddFansActivity(activity, group_bean);
                }
            }
        });
        /*transparentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissI();
            }
        });*/
//        mDialog.setCanceledOnTouchOutside(false);
        return this;
    }

    public void notifyAdapter(){
        if (mMDialogQuanAdapter!=null){
            mMDialogQuanAdapter.notifyDataSetChanged();
        }
    }
    public void dismissI(){
        if (isShowing()){
            this.dismiss();
        }
    }

    public void setShowOrHideView(){
        mQuan_tip.setVisibility(View.VISIBLE);
        mQuan_item_message.setVisibility(View.GONE);
    }
}
