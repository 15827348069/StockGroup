package com.zbmf.StockGroup.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.beans.CouponsOrSystem;

/**
 * Created by pq
 * on 2018/6/7.
 */

public class DialogQuanAdapter extends ListAdapter<CouponsOrSystem> {
    private Context mContext;
    private ViewHolder mHolder;
    private GainOrUseBtn mGainOrUseBtn;

    public DialogQuanAdapter(Context context) {
        super(context);
        mContext = context;
    }
    public interface GainOrUseBtn{
        void mGainOrUseBtn(CouponsOrSystem couponsOrSystem);
    }
    public void setGainOrUseBtnClick(GainOrUseBtn gainOrUseBtnClick){
        mGainOrUseBtn=gainOrUseBtnClick;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.dialog_quan_item, parent, false);
            mHolder = new ViewHolder(convertView);
            convertView.setTag(mHolder);
        }else {
            mHolder = (ViewHolder) convertView.getTag();
        }
        final CouponsOrSystem couponsOrSystem = getItem(position);
        mHolder.mQuan.setText(couponsOrSystem.getSummary());
        if (couponsOrSystem.getIs_taken()==0){
            mHolder.mGainOrUseBtn.setText("领取并使用");
        }else {
            mHolder.mGainOrUseBtn.setText("使用");
        }
        mHolder.mGainOrUseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mGainOrUseBtn!=null){
                    mGainOrUseBtn.mGainOrUseBtn(couponsOrSystem);
                }
            }
        });
        return convertView;
    }

    private static class ViewHolder {
        View mView;
        private final TextView mQuan;
        private final Button mGainOrUseBtn;

        ViewHolder(View view) {
            mView = view;
            mQuan = getView(R.id.quan);
            mGainOrUseBtn = getView(R.id.gainOrUseBtn);
        }

        protected <T extends View> T getView(int resourcesId) {
            return (T) mView.findViewById(resourcesId);
        }
    }
}
