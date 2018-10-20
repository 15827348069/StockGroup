package com.zbmf.StockGroup.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.api.GetStock;
import com.zbmf.StockGroup.api.StockHandler;
import com.zbmf.StockGroup.beans.StockBean;
import com.zbmf.StockGroup.constans.IntentKey;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuhao on 2017/9/1.
 */

public class StockAdapter extends BaseAdapter implements Filterable {
    private LayoutInflater inflater;
    private List<StockBean>info;
    private SearchStock searchStock;

    public StockAdapter(Context context){
        this.inflater=LayoutInflater.from(context);
        this.info=new ArrayList<>();
    }
    @Override
    public int getCount() {
        return info.size();
    }

    @Override
    public Object getItem(int position) {
        StockBean stockBean=info.get(position);
        return stockBean.getF_symbolName()+"("+stockBean.getF_symbol()+")";
    }
    public String getSymbolAtPosition(int position){
        return info.get(position).getF_symbol();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(convertView==null){
            convertView=inflater.inflate(R.layout.item_stock_layout,null);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        final StockBean stockBean=info.get(position);
        holder.name.setText(stockBean.getF_symbolName());
        holder.symbol.setText(stockBean.getF_symbol());
        return convertView;
    }

    private class ViewHolder{
        TextView name,symbol;
        public ViewHolder(View view){
            name= (TextView) view.findViewById(R.id.stock_name);
            symbol= (TextView) view.findViewById(R.id.stock_f_symbol);
        }
    }
    @Override
    public Filter getFilter() {
        if (searchStock == null) {
            searchStock = new SearchStock();
        }
        return searchStock;
    }
    private class SearchStock extends  Filter{
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results=new FilterResults();
            results.values=constraint;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if(results.values!=null){
                    GetStock.getStock(results.values.toString(), new StockHandler() {
                        @Override
                        public void onSuccess(List<StockBean> stockList) {
                            if(info==null){
                                info=new ArrayList<>();
                            }
                            info.clear();
                            info.addAll(stockList);
                            notifyDataSetChanged();
                        }
                        @Override
                        public void onFailure(String err_msg) {

                        }
                    });
            }
        }
    }
}
