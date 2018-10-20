package com.zbmf.StockGTec.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zbmf.StockGTec.R;
import com.zbmf.StockGTec.beans.Videos;
import com.zbmf.StockGTec.utils.DateUtil;

import java.util.List;


public class VideoInfoFragment extends Fragment {

    private TextView tv_video_info, tv_price1, tv_price2, tv_subscribe, tv_start_at,tv_fans;
    private LinearLayout ll_video_info;
    private Videos mVideos;

    public VideoInfoFragment() {
    }

    public static VideoInfoFragment newInstance(Videos videos) {
        VideoInfoFragment fragment = new VideoInfoFragment();
        Bundle args = new Bundle();
        args.putSerializable("video",videos);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mVideos = (Videos) getArguments().getSerializable("video");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_video_info, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tv_video_info = (TextView) view.findViewById(R.id.tv_video_info);
        ll_video_info = (LinearLayout) view.findViewById(R.id.ll_video_info);
        tv_price1 = (TextView) view.findViewById(R.id.tv_price1);
        tv_price2 = (TextView) view.findViewById(R.id.tv_price2);
        tv_subscribe = (TextView) view.findViewById(R.id.tv_subscribe);
        tv_start_at = (TextView)view. findViewById(R.id.tv_start_at);
        tv_fans = (TextView)view. findViewById(R.id.tv_fans);

        StringBuilder videoInfo = new StringBuilder();
        List<Videos.ContentBean> content = mVideos.getContent();
        for (int i = 0; i < content.size(); i++) {
            Videos.ContentBean contentBean = content.get(i);
            String title = contentBean.getTitle();
            String desc = contentBean.getDesc() + "";
            videoInfo.append("【").append(title).append("】\n").append(desc + "\n\n");

            TextView tv_title = new TextView(getActivity());
            tv_title.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
            tv_title.setTextSize(15);
            LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(-1, -2);
            titleParams.topMargin = 60;
            titleParams.leftMargin = -20;
            tv_title.setLayoutParams(titleParams);
            tv_title.setText("【"+title+"】");
            ll_video_info.addView(tv_title);

            TextView tv_desc = new TextView(getActivity());
            tv_desc.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
            tv_desc.setTextSize(14);
            LinearLayout.LayoutParams descParams = new LinearLayout.LayoutParams(-1, -2);
            descParams.topMargin = 20;
            descParams.leftMargin = 6;
            tv_desc.setLineSpacing(16,1);
            tv_desc.setLayoutParams(descParams);
            tv_desc.setText(desc + "");
            ll_video_info.addView(tv_desc);
        }

        tv_video_info.setText(videoInfo.toString());
        String price = mVideos.getPrice() + "";
        String discount = (int)(mVideos.getPrice() * mVideos.getDiscount() * 0.01) + "";
        SpannableString ss = new SpannableString(price);
        ss.setSpan(new StrikethroughSpan(), 0, price.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_price1.setText(ss);
        tv_price2.setText(discount);
        tv_subscribe.setText("参与人数：" + mVideos.getSubscribe());

        tv_start_at.setText("直播开始时间：" + DateUtil.getDate3(mVideos.getStart_at() * 1000));

        if(1 == mVideos.getIs_fans()){
            tv_fans.setText("圈子铁粉免费参与：是");
        }else{
            tv_fans.setText("圈子铁粉免费参与：否");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
