package com.zbmf.StockGTec.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zbmf.StockGTec.R;
import com.zbmf.StockGTec.beans.FansPrice;

import java.util.List;

/**
 * Created by xuhao on 2017/1/4.
 */

public class FansPriceAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<FansPrice>infolist;
    public FansPriceAdapter(Context mcontext,List<FansPrice>info){
        this.inflater=LayoutInflater.from(mcontext);
        this.infolist=info;
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
        item.price.setText(fp.getPrice()+"魔方宝");
//        if(fp.is_checked()){
//            item.title.setTextColor(Color.red(R.color.colorAccent));
//            item.price.setTextColor(Color.red(R.color.colorAccent));
//            view.setBackgroundResource(R.drawable.fans_price_checkd);
//        }else{
//            item.title.setTextColor(Color.red(R.color.black));
//            item.price.setTextColor(Color.red(R.color.black));
//            view.setBackgroundResource(R.color.transparent);
//        }
        return view;
    }
    public class PriceItem{
        TextView title,price;
        public PriceItem(View view){
            title= (TextView) view.findViewById(R.id.fans_price_title);
            price= (TextView) view.findViewById(R.id.fans_price);
        }
    }
}
