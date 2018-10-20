package com.zbmf.groupro.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zbmf.groupro.R;
import com.zbmf.groupro.beans.FansPrice;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by xuhao on 2017/1/4.
 */

public class FansPriceAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<FansPrice>infolist;
    private Context context;
    DecimalFormat df=new DecimalFormat("");
    public FansPriceAdapter(Context mcontext,List<FansPrice>info){
        this.inflater=LayoutInflater.from(mcontext);
        this.infolist=info;
        this.context=mcontext;
    }
    @Override
    public int getCount() {
        return infolist.size();
    }

    @Override
    public Object getItem(int i) {
        return infolist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        PriceItem item=null;
        if(view==null){
            view=inflater.inflate(R.layout.fans_prive_item,null);
            item=new PriceItem(view);
            view.setTag(item);
        }else{
            item= (PriceItem) view.getTag();
        }
        FansPrice fp=infolist.get(i);
        item.title.setText(fp.getTitle());
        item.price.setText(df.format(fp.getPrice())+"魔方宝");
        if(fp.is_checked()){
            item.title.setTextColor(context.getResources().getColor(R.color.white));
            item.price.setTextColor(context.getResources().getColor(R.color.white));
//            item.price_check.setVisibility(View.GONE);
            item.parce_linear_layout.setBackgroundResource(R.drawable.icon_fans_prie);
        }else{
            item.title.setTextColor(context.getResources().getColor(R.color.c88));
            item.price.setTextColor(context.getResources().getColor(R.color.c88));
//            item.price_check.setVisibility(View.GONE);
            item.parce_linear_layout.setBackgroundResource(R.drawable.icon_fance_price_nomal);
        }
        return view;
    }
    public void setSelect(int position){
        for(int i=0;i<getCount();i++){
            infolist.get(i).setIs_checked(false);
        }
        infolist.get(position).setIs_checked(true);
        notifyDataSetChanged();
    }
    public class PriceItem{
        TextView title,price;
//        private ImageView price_check;
        private RelativeLayout parce_linear_layout;
        public PriceItem(View view){
            title= (TextView) view.findViewById(R.id.fans_price_title);
            price= (TextView) view.findViewById(R.id.fans_price);
            parce_linear_layout= (RelativeLayout) view.findViewById(R.id.parce_linear_layout);
//            price_check= (ImageView) view.findViewById(R.id.price_check);
        }
    }
}
