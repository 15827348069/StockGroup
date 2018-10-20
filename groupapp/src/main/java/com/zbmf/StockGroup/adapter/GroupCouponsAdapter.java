package com.zbmf.StockGroup.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.beans.CouPonsRules;
import com.zbmf.StockGroup.beans.CouponsBean;
import com.zbmf.StockGroup.utils.DoubleFromat;
import com.zbmf.StockGroup.utils.ImageLoaderOptions;
import com.zbmf.StockGroup.utils.LogUtil;

import java.util.List;

/**
 * Created by xuhao on 2017/9/5.
 */

public class GroupCouponsAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<CouponsBean>infolst;
    private Drawable point,mfb,date,z;
    private Animation myAnimation;
    public GroupCouponsAdapter(Context context,List<CouponsBean>info){
        this.inflater=LayoutInflater.from(context);
        this.infolst=info;
        point=context.getResources().getDrawable(R.drawable.icon_coupons_point);
        point.setBounds(0, 0, point.getMinimumWidth(), point.getMinimumHeight());
        mfb=context.getResources().getDrawable(R.drawable.icon_coupons_mfb);
        mfb.setBounds(0, 0, mfb.getMinimumWidth(), mfb.getMinimumHeight());
        date=context.getResources().getDrawable(R.drawable.icon_coupons_date);
        date.setBounds(0, 0, date.getMinimumWidth(), date.getMinimumHeight());
        z=context.getResources().getDrawable(R.drawable.icon_coupons_z);
        z.setBounds(0, 0, z.getMinimumWidth(), z.getMinimumHeight());
        myAnimation = AnimationUtils.loadAnimation(context, R.anim.center_in_anim);
        myAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                int size=getCount();
                for(int i=0;i<size;i++){
                    infolst.get(i).setAmin_show(false);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
    @Override
    public int getCount() {
        return infolst.size();
    }

    @Override
    public Object getItem(int position) {
        return infolst.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(convertView==null){
            convertView=inflater.inflate(R.layout.item_coupons_layout,null);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        CouponsBean couponsBean=infolst.get(position);
        holder.desc.setText(couponsBean.getCouponsName());
        holder.title.setText(couponsBean.getSubject());
        holder.desc.setText(couponsBean.getSumary());
        holder.date.setText(couponsBean.getEnd_at()+"前有效");
        if(couponsBean.is_take()){
            holder.rule.setSelected(true);
            holder.take.setVisibility(View.VISIBLE);
            if(couponsBean.isAmin_show()){
                holder.take.startAnimation(myAnimation);
            }
        }else{
            holder.take.setVisibility(View.GONE);
            holder.rule.setSelected(false);
        }
        switch (couponsBean.getCateogry()){
            case 20:
                //魔方宝
                int mfb= (int) couponsBean.getMinimum();
                holder.use.setText("满"+mfb+"可用");
                break;
            case 30:
                //日期
                int month= (int) (couponsBean.getMinimum()/31);
                holder.use.setText("满"+month+"个月可用");
                break;
        }
        if(couponsBean.getKind()==10){
            holder.kind.setVisibility(View.VISIBLE);
            holder.kind.setImageResource(R.drawable.icon_coupons_m);
            setRoule(holder,couponsBean);
        }else if(couponsBean.getKind()==11){
            holder.kind.setVisibility(View.VISIBLE);
            holder.kind.setImageResource(R.drawable.icon_coupons_y);
            setRoule(holder,couponsBean);
        }else if(couponsBean.getKind()==3){
            holder.kind.setVisibility(View.GONE);
            holder.content.setCompoundDrawables(z,null,null,null);
            if(couponsBean.getRuls()!=null){
                holder.content.setText(DoubleFromat.getDouble((1-couponsBean.getRuls().getAmount())*10,1)+"折");
            }
        }else{
            holder.kind.setVisibility(View.GONE);
            setRoule(holder,couponsBean);
        }
//        ViewFactory.imgCircleView(parent.getContext(),couponsBean.getAvatar(),holder.avatar);
        ImageLoader.getInstance().displayImage(couponsBean.getAvatar(),holder.avatar, ImageLoaderOptions.AvatarOptions());
        return convertView;
    }
    private void setRoule(ViewHolder holder,CouponsBean couponsBean){
        CouPonsRules rules=couponsBean.getRuls();
        LogUtil.e(rules.toString());
        String amount="";
        if(rules!=null){
            switch (rules.getCateogry()){
                case 10:
                    //积分
                    amount=(int)rules.getAmount()+"";
                    holder.content.setCompoundDrawables(point,null,null,null);
                    break;
                case 20:
                    //魔方宝
                    amount=(int)(rules.getAmount())+"";
                    holder.content.setCompoundDrawables(mfb,null,null,null);
                    break;
                case 30:
                    //时间
                    if(rules.getAmount()>=31){
                        int month= (int) (rules.getAmount()/31);
                        amount=month+"月";
                    }else{
                        int day= (int) (rules.getAmount());
                        amount=day+"天";
                    }

                    holder.content.setCompoundDrawables(date,null,null,null);
                    break;
            }
            holder.content.setText(amount);
        }
    }
    private class ViewHolder{
        ImageView avatar,kind,take;
        TextView title,desc,date,content,use;
        LinearLayout rule;
        public ViewHolder(View view){
            this.avatar= (ImageView) view.findViewById(R.id.imv_coupons_avatar);
            this.title= (TextView) view.findViewById(R.id.tv_coupons_title);
            this.desc= (TextView) view.findViewById(R.id.tv_coupons_desc);
            this.date= (TextView) view.findViewById(R.id.tv_coupons_date);
            this.content= (TextView) view.findViewById(R.id.tv_coupons_content);
            this.use= (TextView) view.findViewById(R.id.tv_coupons_use);
            this.kind= (ImageView) view.findViewById(R.id.imv_coupons_kind);
            this.take= (ImageView) view.findViewById(R.id.imv_coupons_take);
            this.rule= (LinearLayout) view.findViewById(R.id.ly_coupons_rule);
        }
    }
}
