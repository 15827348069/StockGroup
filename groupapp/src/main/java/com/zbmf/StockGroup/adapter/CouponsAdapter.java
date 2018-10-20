package com.zbmf.StockGroup.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.beans.CouponsBean;

import java.util.List;

/**
 * Created by xuhao on 2017/1/11.
 */

public class CouponsAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<CouponsBean> info;
    private Animation myAnimation;
    private OnCouponsCheck onCouponsCheck;

    public OnCouponsCheck getOnCouponsCheck() {
        return onCouponsCheck;
    }

    public void setOnCouponsCheck(OnCouponsCheck onCouponsCheck) {
        this.onCouponsCheck = onCouponsCheck;
    }

    public CouponsAdapter (Context context, List<CouponsBean>infolist){
        this.inflater=LayoutInflater.from(context);
        this.info=infolist;
        myAnimation = AnimationUtils.loadAnimation(context, R.anim.center_in_anim);
        myAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                int size=getCount();
                for(int i=0;i<size;i++){
                    info.get(i).setAmin_show(false);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
    @Override
    public int getCount() {
        return info.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        CouponsItem item=null;
        if(view==null){
            view=inflater.inflate(R.layout.coupons,null);
            item=new CouponsItem(view);
            view.setTag(item);
        }else{
            item= (CouponsItem) view.getTag();
        }
        final CouponsBean cb=info.get(i);
        item.coupons_name.setText(cb.getSubject());
        item.coupons_title.setText(cb.getSumary());
        item.coupons_date.setText(cb.getEnd_at()+"前有效");
        if(cb.is_take()){
            item.aleady_have.setVisibility(View.INVISIBLE);
            item.clcil_coupons.setVisibility(View.VISIBLE);
            item.clcil_coupons.setText("立即领取");
        }else{
            item.clcil_coupons.setText("已领取");
            item.clcil_coupons.setVisibility(View.GONE);
            item.aleady_have.setVisibility(View.VISIBLE);
            if(cb.isAmin_show()){
                item.aleady_have.startAnimation(myAnimation);
            }
        }
        item.clcil_coupons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onCouponsCheck!=null&&cb.is_take()){
                    onCouponsCheck.onCheck(i);
                }
            }
        });
        item.clcil_coupons.setChecked(cb.is_take());
        item.clcil_coupons.setEnabled(cb.is_take());
        switch (cb.getKind()){
            case 0:
                //默认  满送积分
                item.coupons_icon.setImageResource(R.drawable.icon_coupon_mansong);
                break;
            case 1:
                //满加券
                item.coupons_icon.setImageResource(R.drawable.icon_coupon_mansong);
                break;
            case 2:
                //满减券
                item.coupons_icon.setImageResource(R.drawable.icon_coupon_manjian);
                break;
            case 3:
                //折扣券
                item.coupons_icon.setImageResource(R.drawable.icon_coupon_zhekou);
                break;
            case 10:
                //粉丝券
                item.coupons_icon.setImageResource(R.drawable.icon_coupon_mansong);
                break;
            case 11:
                //年粉券
                item.coupons_icon.setImageResource(R.drawable.icon_coupon_zhekou);
                break;
            case 12:
                //体验券
                item.coupons_icon.setImageResource(R.drawable.icon_coupon_tiyan);
                break;
        }
        return view;
    }

    public class CouponsItem{
        private TextView coupons_name,coupons_date;
        private TextView coupons_title;
        private ImageView coupons_icon,aleady_have;
        private CheckBox clcil_coupons;
        public CouponsItem(View view){
            coupons_name= (TextView) view.findViewById(R.id.coupons_name);
            coupons_title= (TextView) view.findViewById(R.id.coupons_title);
            coupons_date= (TextView) view.findViewById(R.id.coupons_date);
            coupons_icon= (ImageView) view.findViewById(R.id.coupons_icon);
            aleady_have= (ImageView) view.findViewById(R.id.aleady_have);
            clcil_coupons= (CheckBox) view.findViewById(R.id.clcil_coupons);
        }
    }
    public interface OnCouponsCheck{
        void onCheck(int position);
    }
}
