package com.zbmf.StocksMatch.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.bean.Adverts;
import com.zbmf.StocksMatch.listener.SponsorAdsClick;
import com.zbmf.StocksMatch.view.GlideOptionsManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pq
 * on 2018/5/15.
 */

public class SponsorAdapter extends Adapter<SponsorAdapter.ViewHolder> {
    private Context mContext;
    private ViewHolder mViewHolder;
    private List<Adverts> sponsor;
    private SponsorAdsClick mSponsorAdsClick;
    public void setSponsorAdsClick(SponsorAdsClick adsClick){
        this.mSponsorAdsClick=adsClick;
    }

    public SponsorAdapter(Context context) {
        mContext = context;
        sponsor = new ArrayList<>();
    }

    public List<Adverts> getSponsorList() {
        return sponsor;
    }

    public void addList(List<Adverts> newSponsor) {
        if (newSponsor != null && newSponsor.size() > 0) {
            sponsor.addAll(newSponsor);
            notifyDataSetChanged();
        }
    }

    public void clearList() {
        if (sponsor != null && sponsor.size() > 0) {
            sponsor.clear();
            notifyDataSetChanged();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.sponsor_item, parent, false);
        mViewHolder = new ViewHolder(view);
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Adverts adverts = sponsor.get(position);
        Glide.with(mContext)
                .load(sponsor.get(position).getImg_url())
                .apply(GlideOptionsManager.getInstance().getBannerOptions(0))
                .into(mViewHolder.sponsorIv);
        holder.sponsorIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSponsorAdsClick!=null){
                    mSponsorAdsClick.sponsor(position,adverts);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return sponsor == null ? 0 : sponsor.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView sponsorIv;

        public ViewHolder(View itemView) {
            super(itemView);
            sponsorIv = itemView.findViewById(R.id.sponsorIv);
        }
    }
}
