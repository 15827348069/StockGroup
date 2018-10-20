package com.zbmf.StocksMatch.adapter;


import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.bean.School;
import com.zbmf.StocksMatch.view.GlideOptionsManager;
import com.zbmf.worklibrary.pullrefreshrecycle.adapter.BaseListRecyclerAdapter;
import com.zbmf.worklibrary.pullrefreshrecycle.holder.RecyclerHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xuhao
 * on 2017/11/27.
 */

public class HomeSchoolAdapter extends BaseListRecyclerAdapter<School,HomeSchoolAdapter.ViewHolder> {


    public HomeSchoolAdapter(Context context) {
        super(context);
    }

    @Override
    public int getViewLayoutId(int viewType) {
        return R.layout.item_home_school_layout;
    }

    @Override
    public ViewHolder createViewHolder(View itemView, int viewType) {
        return new ViewHolder(itemView);
    }

    @Override
    public void convert(ViewHolder holder, School school, int position) {
        holder.tvSchoolName.setText(school.getName());
        Glide.with(getContext()).load(school.getLogo()).apply(GlideOptionsManager.getInstance().getRequestOptions()).into(holder.imvSchoolImg);
    }

    public class ViewHolder extends RecyclerHolder {
        @BindView(R.id.tv_school_name)
        TextView tvSchoolName;
        @BindView(R.id.imv_school_img)
        ImageView imvSchoolImg;
        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }
    }
}
