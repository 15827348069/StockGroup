package com.zbmf.StockGroup.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.beans.CouponsOrSystem;

import java.util.List;

/**
 * Created by xuhao
 * on 2017/1/11.
 */

public class UserCouponsAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<CouponsOrSystem> info;
//    private List<CouponsBean> info;
    private Animation myAnimation;
    public UserCouponsAdapter(Context context, List<CouponsOrSystem>/*List<CouponsBean>*/infolist){
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
//        CouponsBean cb=info.get(i);
        CouponsOrSystem cb = info.get(i);
        item.coupons_name.setText(cb.getSubject());
        item.coupons_title.setText(cb.getSummary());
        item.coupons_date.setText(String.format(cb.getEnd_at()+"%s","前有效"));
        if(!TextUtils.isEmpty(cb.getCouponsName())){
            item.coupons_group_name.setVisibility(View.VISIBLE);
            item.coupons_group_name.setText(cb.getCouponsName());
        }else{
            item.coupons_group_name.setVisibility(View.GONE);
        }
        boolean check = cb.isCheck();
        if(check){
            item.coupons_check.setSelected(true);
            if(cb.isAmin_show()){
                item.coupons_countent_layout.startAnimation(myAnimation);
            }
        }else{
            item.coupons_check.setSelected(false);
        }
        String couponsType = cb.getCouponsType();
        boolean canUse = cb.isCanUse();
        boolean b = cb.isCanUse();
        item.coupons_icon.setSelected(b);
        item.coupons_icon.setText(couponsType);
        if(b){
            item.coupons_name.setTextColor(Color.rgb(102,102,102));
        }else{
            item.coupons_name.setTextColor(Color.rgb(0,0,0));
        }
        return view;
    }
    public void setSelect(int select){
        for(int i=0;i<getCount();i++){
            info.get(i).setCheck(false);
        }
        info.get(select).setCheck(true);
        info.get(select).setAmin_show(true);
        notifyDataSetChanged();

    }
    public class CouponsItem{
        private TextView coupons_name,coupons_date,coupons_group_name;
        private TextView coupons_title;
        private TextView coupons_icon;
        private RelativeLayout coupons_countent_layout;
        private ImageView coupons_check;
        public CouponsItem(View view){
            coupons_name= (TextView) view.findViewById(R.id.coupons_name);
            coupons_title= (TextView) view.findViewById(R.id.coupons_title);
            coupons_date= (TextView) view.findViewById(R.id.coupons_date);
            coupons_icon= (TextView) view.findViewById(R.id.coupons_icon);
            coupons_group_name= (TextView) view.findViewById(R.id.coupons_group_name);
            coupons_countent_layout= (RelativeLayout) view.findViewById(R.id.coupons_countent_layout);
//            can_use_coupons= (TextView) view.findViewById(R.id.can_use_coupons);
            coupons_check= (ImageView) view.findViewById(R.id.coupons_check);
        }
    }
}
