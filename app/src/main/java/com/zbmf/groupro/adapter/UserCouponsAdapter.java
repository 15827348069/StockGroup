package com.zbmf.groupro.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zbmf.groupro.R;
import com.zbmf.groupro.beans.CouponsBean;

import java.util.List;

/**
 * Created by xuhao on 2017/1/11.
 */

public class UserCouponsAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<CouponsBean> info;
    private Animation myAnimation;
    public UserCouponsAdapter(Context context, List<CouponsBean>infolist){
        this.inflater=LayoutInflater.from(context);
        this.info=infolist;
        myAnimation = AnimationUtils.loadAnimation(context, R.anim.user_coupons_anim);
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        CouponsItem item=null;
        if(view==null){
            view=inflater.inflate(R.layout.user_coupons,null);
            item=new CouponsItem(view);
            view.setTag(item);
        }else{
            item= (CouponsItem) view.getTag();
        }
        CouponsBean cb=info.get(i);
        item.coupons_name.setText(cb.getSubject());
        item.coupons_title.setText(cb.getSumary());
        item.coupons_date.setText(cb.getEnd_at()+"前有效");
        if(cb.getCouponsName()!=null&&!cb.getCouponsName().equals("")){
            item.coupons_group_name.setVisibility(View.VISIBLE);
            item.coupons_group_name.setText(cb.getCouponsName());
        }else{
            item.coupons_group_name.setVisibility(View.GONE);
        }
        if(cb.is_take()){
            item.coupons_check.setVisibility(View.VISIBLE);
//            item.coupons_countent_layout.setBackgroundResource(R.drawable.shape_layout_tran_no_red);
            if(cb.isAmin_show()){
                item.coupons_countent_layout.startAnimation(myAnimation);
            }
        }else{
            item.coupons_check.setVisibility(View.GONE);
//            item.coupons_countent_layout.setBackgroundResource(R.color.white);
        }

        if(!cb.isCan_use()){
            item.coupons_name.setTextColor(Color.rgb(102,102,102));
            switch (cb.getKind()){
                case 0:
                    //默认  满送积分
                    item.coupons_icon.setImageResource(R.drawable.icon_coupon_mansong_nomal);
                    break;
                case 1:
                    //满加券
                    item.coupons_icon.setImageResource(R.drawable.icon_coupon_mansong_nomal);
                    break;
                case 2:
                    //满减券
                    item.coupons_icon.setImageResource(R.drawable.icon_coupon_manjian_nomal);
                    break;
                case 3:
                    //折扣券
                    item.coupons_icon.setImageResource(R.drawable.icon_coupon_zhekou_nomal);
                    break;
                case 10:
                    //粉丝券
                    item.coupons_icon.setImageResource(R.drawable.icon_coupon_mansong_nomal);
                    break;
                case 11:
                    //年粉券
                    item.coupons_icon.setImageResource(R.drawable.icon_coupon_zhekou_nomal);
                    break;
                case 12:
                    //体验券
                    item.coupons_icon.setImageResource(R.drawable.icon_coupon_tiyan_nomal);
                    break;
            }
        }else{
            item.coupons_name.setTextColor(Color.rgb(0,0,0));
            switch (cb.getKind()){
                case 0:
                    //满送积分
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
        }
        return view;
    }
    public void setSelect(int select){
        for(int i=0;i<getCount();i++){
            info.get(i).setIs_take(false);
        }
        info.get(select).setIs_take(true);
        info.get(select).setAmin_show(true);
        notifyDataSetChanged();

    }
    public class CouponsItem{
        private TextView coupons_name,coupons_date,coupons_group_name;
        private TextView coupons_title;
        private ImageView coupons_icon;
        private RelativeLayout coupons_countent_layout;
        private ImageView coupons_check;
        public CouponsItem(View view){
            coupons_name= (TextView) view.findViewById(R.id.coupons_name);
            coupons_title= (TextView) view.findViewById(R.id.coupons_title);
            coupons_date= (TextView) view.findViewById(R.id.coupons_date);
            coupons_icon= (ImageView) view.findViewById(R.id.coupons_icon);
            coupons_group_name= (TextView) view.findViewById(R.id.coupons_group_name);
            coupons_countent_layout= (RelativeLayout) view.findViewById(R.id.coupons_countent_layout);
//            can_use_coupons= (TextView) view.findViewById(R.id.can_use_coupons);
            coupons_check= (ImageView) view.findViewById(R.id.coupons_check);
        }
    }
}
