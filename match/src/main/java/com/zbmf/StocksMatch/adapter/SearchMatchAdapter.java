package com.zbmf.StocksMatch.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.bean.SearchMatchBean;
import com.zbmf.worklibrary.adapter.ListAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pq
 * on 2018/3/21.
 */

public class SearchMatchAdapter extends ListAdapter<SearchMatchBean.Result.Matches> {
    public SearchMatchAdapter(Activity context) {
        super(context);
    }

    @Override
    protected int getLayout() {
        return R.layout.search_match_list_item;
    }

    @Override
    public View getHolderView(int position, View convertView, SearchMatchBean.Result.Matches matches) {
        ViewHolder holder = (ViewHolder) convertView.getTag();
        if (holder == null) {
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        holder.searchItemTv.setText(matches.getMatch_name());
        return convertView;
    }


    public class ViewHolder{
        @BindView(R.id.search_item_tv)
        TextView searchItemTv;
        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
