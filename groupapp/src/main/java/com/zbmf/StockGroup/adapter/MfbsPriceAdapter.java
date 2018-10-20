package com.zbmf.StockGroup.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.beans.FansPrice;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by xuhao on 2017/1/4.
 */

public class MfbsPriceAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<FansPrice>infolist;
    private Context context;
    DecimalFormat df=new DecimalFormat("");
    DecimalFormat double_df =new DecimalFormat("######0.00");
    public MfbsPriceAdapter(Context mcontext, List<FansPrice>info){
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
        if(fp.getPrice()!=0){
            item.title.setText(fp.getTitle());
            item.price.setText("￥"+getDoubleormat(fp.getPrice()));
            item.price.setVisibility(View.VISIBLE);
        }else{
            item.title.setText(fp.getTitle());
            item.price.setVisibility(View.GONE);
        }

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
        private RelativeLayout parce_linear_layout;
        public PriceItem(View view){
            title= (TextView) view.findViewById(R.id.fans_price_title);
            price= (TextView) view.findViewById(R.id.fans_price);
            parce_linear_layout= (RelativeLayout) view.findViewById(R.id.parce_linear_layout);
        }
    }
    public String getDoubleormat(double vealue){
        if(double_df.format(vealue).contains(".00")){
            double ve=Double.valueOf(double_df.format(vealue));
            return df.format(ve);
        }else{
            return double_df.format(vealue);
        }
    }
}
