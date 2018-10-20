package com.zbmf.StockGTec.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zbmf.StockGTec.R;
import com.zbmf.StockGTec.beans.Gift;

import java.util.List;

/**
 * Created by iMac on 2016/12/22.
 */

public class GridAdatper extends BaseAdapter {

    private static final String TAG = GridAdatper.class.getSimpleName();
    private List<Gift> gifts;
    private Context mContext;
    private LayoutInflater mInflater;

    public List<Gift> getGifts() {
        return gifts;
    }

    public GridAdatper(Context context, List<Gift> gifts) {
        mContext = context;
        this.gifts = gifts;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return gifts.size();
    }

    @Override
    public Object getItem(int position) {
        return gifts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        GiftItem item=null;
        if(view==null){
            view = mInflater.inflate(R.layout.item_gift1, null);
            item=new GiftItem(view);
            view.setTag(item);
        }else{
            item= (GiftItem) view.getTag();
        }
//        RelativeLayout rl_gift = (RelativeLayout) view.findViewById(R.id.rl_gift);
         Gift gift = gifts.get(position);
       item.tv_gift.setText(gift.getName());
        item.tv_price.setText(gift.getMpays() + "");
        if(gift.getCategory()=="10"){
            item.iv_type.setImageDrawable(mContext.getResources().getDrawable(R.drawable.jf1));
        }else{
            item.iv_type.setImageDrawable(mContext.getResources().getDrawable(R.drawable.mfb1));
        }
        Glide.with(mContext).load(gift.getIcon()).centerCrop().placeholder(R.drawable.loading).crossFade().into(item.iv_gift);
        if (gift.isChecked())
            item.iv_check.setVisibility(View.VISIBLE);
        else
            item.iv_check.setVisibility(View.INVISIBLE);

        return view;
    }
    public class GiftItem{
        private ImageView iv_check,iv_gift,iv_type;
        private TextView tv_gift,tv_price;
        public GiftItem(View view){
            iv_check=(ImageView) view.findViewById(R.id.iv_check);
            iv_gift=(ImageView) view.findViewById(R.id.iv_gift);
            iv_type=(ImageView) view.findViewById(R.id.iv_type);
             tv_gift = (TextView) view.findViewById(R.id.tv_gift);//礼物的名称
             tv_price = (TextView) view.findViewById(R.id.tv_price);//礼物的价格
        }
    }
}

