package com.zbmf.StocksMatch.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.bean.TraderDeals;
import com.zbmf.worklibrary.adapter.ListAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pq
 * on 2018/3/26.
 */

public class DealsTitleAdapter extends ListAdapter<TraderDeals.Result.Deals> {
    public DealsTitleAdapter(Activity context) {
        super(context);
//        inflater = LayoutInflater.from(mContext);
    }

    @Override
    protected int getLayout() {
        return R.layout.item_match_hold_name;
    }

    @Override
    public View getHolderView(int position, View convertView, TraderDeals.Result.Deals deals) {
        ViewHold hold= (ViewHold) convertView.getTag();
        if(hold==null){
            hold=new ViewHold(convertView);
            convertView.setTag(hold);
        }
        hold.trustsCellName.setText(deals.getName());
        convertView.setBackgroundDrawable(position%2==0?mContext.getResources()
                .getDrawable(R.drawable.list_item_seletor_gray):mContext.getResources()
                .getDrawable(R.drawable.list_item_seletor_white));
        return convertView;
    }
    public class ViewHold{
        @BindView(R.id.trusts_cell_name)
        TextView trustsCellName;
        public ViewHold(View view){
            ButterKnife.bind(this, view);
        }
    }


}
