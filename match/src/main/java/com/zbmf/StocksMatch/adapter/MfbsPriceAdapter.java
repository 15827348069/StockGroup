package com.zbmf.StocksMatch.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.bean.FansPrice;
import com.zbmf.worklibrary.adapter.ListAdapter;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pq
 * on 2018/4/9.
 */

public class MfbsPriceAdapter extends ListAdapter<FansPrice> {
    private Context context;
    private DecimalFormat df = new DecimalFormat("");
    private DecimalFormat double_df = new DecimalFormat("######0.00");

    public MfbsPriceAdapter(Activity context) {
        super(context);
        this.context = context;
    }

    @Override
    protected int getLayout() {
        return R.layout.fans_prive_item;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getHolderView(int position, View convertView, FansPrice fansPrice) {
        ViewHolder holder = (ViewHolder) convertView.getTag();
        if (holder == null) {
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        if (fansPrice.getPrice() != 0) {
            holder.fansPriceTitle.setText(fansPrice.getTitle());
            holder.fansPrice.setText("￥" + getDoubleormat(fansPrice.getPrice()));
            holder.fansPrice.setVisibility(View.VISIBLE);
        } else {
            holder.fansPriceTitle.setText(fansPrice.getTitle());
            holder.fansPrice.setVisibility(View.GONE);
        }
        if (fansPrice.is_checked()) {
            holder.fansPriceTitle.setTextColor(context.getResources().getColor(R.color.white));
            holder.fansPrice.setTextColor(context.getResources().getColor(R.color.white));
//            item.price_check.setVisibility(View.GONE);
            holder.parceLinearLayout.setBackgroundResource(R.drawable.icon_fans_prie);
        } else {
            holder.fansPriceTitle.setTextColor(context.getResources().getColor(R.color.c88));
            holder.fansPrice.setTextColor(context.getResources().getColor(R.color.c88));
//            item.price_check.setVisibility(View.GONE);
            holder.parceLinearLayout.setBackgroundResource(R.drawable.icon_fance_price_nomal);
        }
        return convertView;
    }

    public void setSelect(List<FansPrice> fp, int position) {
        for (int i = 0; i < getCount(); i++) {
            fp.get(i).setIs_checked(false);
        }
        fp.get(position).setIs_checked(true);
        notifyDataSetChanged();
    }

    public class ViewHolder {
        @BindView(R.id.fans_price_title)
        TextView fansPriceTitle;
        @BindView(R.id.fans_price)
        TextView fansPrice;
        @BindView(R.id.parce_linear_layout)
        RelativeLayout parceLinearLayout;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

     private String getDoubleormat(double vealue) {
        if (double_df.format(vealue).contains(".00")) {
            double ve = Double.valueOf(double_df.format(vealue));
            return df.format(ve);
        } else {
            return double_df.format(vealue);
        }
    }
}
