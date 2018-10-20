package com.zbmf.StocksMatch.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.bean.MatchNewAllBean;
import com.zbmf.StocksMatch.listener.FlowDialogItemListener;
import com.zbmf.worklibrary.adapter.ListAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pq
 * on 2018/4/27.
 */

public class FlowDialogAdapter extends ListAdapter<MatchNewAllBean.Result.Matches>{

    private FlowDialogItemListener mFlowDialogItemListener;
    public void setFlowDialogItemListener(FlowDialogItemListener listener){
        this.mFlowDialogItemListener=listener;
    }

    public FlowDialogAdapter(Activity context) {
        super(context);
    }

    @Override
    protected int getLayout() {
        return R.layout.dialog_flow_item;
    }

    @Override
    public View getHolderView(int position, View convertView, final MatchNewAllBean.Result.Matches matches) {
        ViewHolder mViewHolder = (ViewHolder)convertView.getTag();
        if (mViewHolder==null){
            mViewHolder = new ViewHolder(convertView);
            convertView.setTag(mViewHolder);
        }
        mViewHolder.matchTitle.setText(matches.getMatch_name());
        mViewHolder.flowItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFlowDialogItemListener!=null){
                    mFlowDialogItemListener.flowItemClick(matches);
                }
            }
        });
        mViewHolder.flowBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFlowDialogItemListener!=null){
                    mFlowDialogItemListener.flowItemClick(matches);
                }
            }
        });
        return convertView;
    }

    public class ViewHolder{
        @BindView(R.id.flow_item)
        LinearLayout flowItem;
        @BindView(R.id.matchTitle)
        TextView matchTitle;
        @BindView(R.id.flowBuy)
        ImageView flowBuy;
        public ViewHolder(View view){
            ButterKnife.bind(this,view);
        }
    }
}
