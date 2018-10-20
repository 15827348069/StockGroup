package com.zbmf.StockGroup.fragment;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.activity.StockBuyActivity;
import com.zbmf.StockGroup.activity.StockDetailActivity;
import com.zbmf.StockGroup.activity.StockModeActivity;
import com.zbmf.StockGroup.adapter.StockModeAdapter;
import com.zbmf.StockGroup.adapter.StockModeNameAdapter;
import com.zbmf.StockGroup.adapter.StockModeTimeAdapter;
import com.zbmf.StockGroup.api.JSONHandler;
import com.zbmf.StockGroup.api.WebBase;
import com.zbmf.StockGroup.beans.MatchInfo;
import com.zbmf.StockGroup.beans.StockMode;
import com.zbmf.StockGroup.beans.StockModeMenu;
import com.zbmf.StockGroup.constans.Constants;
import com.zbmf.StockGroup.constans.IntentKey;
import com.zbmf.StockGroup.constans.ModeStatus;
import com.zbmf.StockGroup.utils.DateUtil;
import com.zbmf.StockGroup.utils.JSONParse;
import com.zbmf.StockGroup.utils.ShowActivity;
import com.zbmf.StockGroup.view.ListViewForScrollView;
import com.zbmf.StockGroup.view.PullToRefreshBase;
import com.zbmf.StockGroup.view.PullToRefreshScrollView;
import com.zbmf.StockGroup.view.SyncHorizontalScrollView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * Created by xuhao on 2017/12/4.
 */

public class StockModeContentFragment extends Fragment implements View.OnClickListener, StockModeTimeAdapter.OnItemClickLitener, AdapterView.OnItemClickListener {
    TextView tvPrice;
    TextView tvYield;
    TextView tvAllYield;
    TextView tvYellowWhite;
    TextView tvNoMessage;
    SyncHorizontalScrollView contentTitleScrollView;
    SyncHorizontalScrollView contentContentScrollView;
    ListViewForScrollView contentContentListView;
    private StockModeActivity stockModeActivity;
    private PullToRefreshScrollView pullToRefreshScrollView;
    private StockModeAdapter adapter;
    private List<StockMode> infolist;
    private Drawable arrow_nomal, mode_arrow;
    private StockModeTimeAdapter stockModeTimeAdapter;
    private StockModeMenu stockModeMenu;
    private View view;
    private String mode_time, day_time;
    private RecyclerView time_recycler;
    private RadioGroup mode_tab_radiogroup;
    private View scroll_bottom_layout;
    private LinearLayout bottom_layout;
    private ImageView tv_price_img,tv_yield_img,tv_all_yield_img,tv_yellow_white_img;
    private LinearLayout no_message_layout;
    private LinearLayout dialog_layout;
    private TextView id_tv_loadingmsg;
    private ListViewForScrollView content_nameListView;
    private StockModeNameAdapter nameAdapter;
    public static StockModeContentFragment newInstance(StockModeMenu stockModeMenu) {
        StockModeContentFragment fragment = new StockModeContentFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(IntentKey.STOCK_MODE_MENU, stockModeMenu);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_stock_mode_content_layout, null);
        stockModeActivity= (StockModeActivity) getActivity();
        initView();
        day_time = DateUtil.getDay(0, Constants.YYYYMMDD);
        return view;
    }

    protected <T extends View> T getView(int resourcesId) {
        return (T) view.findViewById(resourcesId);
    }

    private void initView() {
        tvPrice = getView(R.id.tv_price);
        tvYield = getView(R.id.tv_yield);
        tvAllYield = getView(R.id.tv_all_yield);
        tvYellowWhite = getView(R.id.tv_yellow_white);
        dialog_layout=getView(R.id.dialog_layout);
        id_tv_loadingmsg=getView(R.id.id_tv_loadingmsg);
        id_tv_loadingmsg.setText("正在掘金中...");
        contentTitleScrollView = getView(R.id.content_TitleScrollView);
        contentContentScrollView = getView(R.id.content_ContentScrollView);
        contentContentScrollView.setScrollView(contentTitleScrollView);
        contentTitleScrollView.setScrollView(contentContentScrollView);
        contentContentListView = getView(R.id.content_ContentListView);

        mode_tab_radiogroup = getView(R.id.mode_tab_radiogroup);

        time_recycler = getView(R.id.time_recycler);

        scroll_bottom_layout = getView(R.id.scroll_bottom_view);
        bottom_layout = getView(R.id.bottom_layout);
        no_message_layout=getView(R.id.no_message_layout);
        tvNoMessage=getView(R.id.tv_no_mode_message);

        pullToRefreshScrollView=getView(R.id.my_scrllview);

        tv_price_img=getView(R.id.tv_price_img);
        tv_yield_img=getView(R.id.tv_yield_img);
        tv_all_yield_img=getView(R.id.tv_all_yield_img);
        tv_yellow_white_img=getView(R.id.tv_yellow_white_img);

        tvPrice.setOnClickListener(this);
        tvYield.setOnClickListener(this);
        tvAllYield.setOnClickListener(this);
        tvYellowWhite.setOnClickListener(this);
        arrow_nomal = getResources().getDrawable(R.drawable.icon_arrow_nomal);
        mode_arrow = getResources().getDrawable(R.drawable.mode_arrow);
        mode_tab_radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                clearArrow();
                if(dialog_layout!=null&&dialog_layout.getVisibility()==View.GONE){
                    dialog_layout.setVisibility(View.VISIBLE);
                }
                switch (checkedId) {
                    case R.id.mode_radio_yesterday:
                        day_time = DateUtil.getDay(-1, Constants.YYYYMMDD);
                        break;
                    case R.id.mode_radio_today:
                        day_time = DateUtil.getDay(0, Constants.YYYYMMDD);
                        break;
                }
                if(handler!=null){
                    handler.removeCallbacks(runnable);
                    handler.post(runnable);
                }
            }
        });
        pullToRefreshScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                getModeList();
            }
        });
        nameAdapter=new StockModeNameAdapter(getActivity());
        content_nameListView=getView(R.id.content_nameListView);
        content_nameListView.setAdapter(nameAdapter);
        content_nameListView.setOnItemClickListener(this);
        infolist = new ArrayList<>();
        adapter = new StockModeAdapter(getActivity());
        contentContentListView.setAdapter(adapter);
        contentContentListView.setOnItemClickListener(this);
        if (getArguments() != null) {
            stockModeMenu = (StockModeMenu) getArguments().getSerializable(IntentKey.STOCK_MODE_MENU);
            if (stockModeMenu.getTimes().size() > 1) {
                time_recycler.setVisibility(View.VISIBLE);
                List<StockModeMenu.Time> timeList = stockModeMenu.getTimes();
                if (!DateUtil.isMax(timeList.get(timeList.size() - 1).getTime())) {
                    mode_time = timeList.get(timeList.size() - 1).getTime();
                    timeList.get(timeList.size() - 1).setSelect(true);
                } else {
                    for (int i = 0; i < timeList.size(); i++) {
                        if (DateUtil.isMax(timeList.get(i).getTime())) {
                            if (i == 0) {
                                mode_time = timeList.get(0).getTime();
                                timeList.get(0).setSelect(true);
                                break;
                            }
                            mode_time = timeList.get(i - 1).getTime();
                            timeList.get(i - 1).setSelect(true);
                            break;
                        }
                    }
                }
                stockModeTimeAdapter = new StockModeTimeAdapter(getActivity(), timeList);
                stockModeTimeAdapter.setOnItemClickLitener(this);
                time_recycler.setLayoutManager(new GridLayoutManager(getContext(), timeList.size()) {
                    @Override
                    public boolean canScrollVertically() {
                        return false;
                    }

                    @Override
                    public boolean canScrollHorizontally() {
                        return true;
                    }
                });
                time_recycler.setAdapter(stockModeTimeAdapter);
            } else {
                mode_time=stockModeMenu.getTimes().get(0).getTime();
                time_recycler.setVisibility(View.GONE);
            }
        }
    }
    private Handler handler=new Handler();
    private Runnable runnable=new Runnable() {
        @Override
        public void run() {
            getModeList();
            handler.postDelayed(this,5000);
        }
    };
    private void getModeList() {
        WebBase.modelList(String.valueOf(stockModeMenu.getProduct_id()),
                day_time,
                mode_time, new JSONHandler() {
                    @Override
                    public void onSuccess(JSONObject obj) {
                        infolist.clear();
                        if (obj.has("stocks")) {
                            infolist.addAll(JSONParse.getStockModeList(obj.optJSONArray("stocks")));
                        }
                        if (infolist.size() > 0) {
                            no_message_layout.setVisibility(View.GONE);
                            contentContentListView.setVisibility(View.VISIBLE);
                            arrowList();
                        }else{
                            contentContentListView.setVisibility(View.GONE);
                            no_message_layout.setVisibility(View.VISIBLE);
                            if(DateUtil.isAfterNow(day_time+mode_time)){
                                tvNoMessage.setText(String.format(getString(R.string.no_mode_stock),DateUtil.getModeTime(mode_time)));
                            }else{
                                tvNoMessage.setText("该时段暂无数据");
                            }
                        }
                        if(pullToRefreshScrollView.isRefreshing()){
                            pullToRefreshScrollView.onRefreshComplete();
                        }
                        if(dialog_layout!=null&&dialog_layout.getVisibility()==View.VISIBLE){
                            dialog_layout.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(String err_msg) {
                        if(pullToRefreshScrollView.isRefreshing()){
                            pullToRefreshScrollView.onRefreshComplete();
                        }
                        if(dialog_layout!=null&&dialog_layout.getVisibility()==View.VISIBLE){
                            dialog_layout.setVisibility(View.GONE);
                        }
                    }
                });
    }

    private void clearArrow() {
        setDrawableLeft(tv_price_img,tvPrice, arrow_nomal);
        setDrawableLeft(tv_yield_img,tvYield, arrow_nomal);
        setDrawableLeft(tv_all_yield_img,tvAllYield, arrow_nomal);
        setDrawableLeft(tv_yellow_white_img,tvYellowWhite, arrow_nomal);
    }

    private void setDrawableLeft(ImageView imageView,TextView textView, Drawable drawable) {
        imageView.setImageDrawable(drawable);
        textView.setTextColor(Color.BLACK);
    }

    private void setTextViewSelect(final ImageView imageView,final View textView) {
        clearArrow();
        setDrawableLeft(imageView,(TextView) textView, mode_arrow);
        imageView.setSelected(!imageView.isSelected());
        ((TextView)textView).setTextColor(Color.RED);
        arrowList();
    }
    private ModeStatus status;
    private void arrowList(){
        if(status!=null){
            Collections.sort(infolist, new Comparator<StockMode>() {
                public int compare(StockMode o1, StockMode o2) {
                    switch (status) {
                        case PRICE:
                            if (tv_price_img.isSelected()) {
                                if (o1.getPrice() > o2.getPrice()) {
                                    return 1;
                                }
                            } else {
                                if (o1.getPrice() < o2.getPrice()) {
                                    return 1;
                                }
                            }
                            if (o1.getPrice() == o2.getPrice()) {
                                return 0;
                            }
                            break;
                        case YIELD:
                            if (tv_yield_img.isSelected()) {
                                if (o1.getYield() > o2.getYield()) {
                                    return 1;
                                }
                            } else {
                                if (o1.getYield() < o2.getYield()) {
                                    return 1;
                                }
                            }
                            if (o1.getYield() == o2.getYield()) {
                                return 0;
                            }
                            break;
                        case VRSI:
                            if (tv_all_yield_img.isSelected()) {
                                if (o1.getAllYield() > o2.getAllYield()) {
                                    return 1;
                                }
                            } else {
                                if (o1.getAllYield() < o2.getAllYield()) {
                                    return 1;
                                }
                            }
                            if (o1.getAllYield() == o2.getAllYield()) {
                                return 0;
                            }
                            break;
                        case YWPI:
                            if (tv_yellow_white_img.isSelected()) {
                                if (o1.getYellow() > o2.getYellow()) {
                                    return 1;
                                }
                            } else {
                                if (o1.getYellow() < o2.getYellow()) {
                                    return 1;
                                }
                            }
                            if (o1.getYellow() == o2.getYellow()) {
                                return 0;
                            }
                            break;
                    }
                    return -1;
                }
            });
        }
        adapter.setList(infolist);
        nameAdapter.setList(infolist);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_price:
                status=ModeStatus.PRICE;
                setTextViewSelect(tv_price_img,v);
                break;
            case R.id.tv_yield:
                status=ModeStatus.YIELD;
                setTextViewSelect(tv_yield_img,v);
                break;
            case R.id.tv_all_yield:
                status=ModeStatus.VRSI;
                setTextViewSelect(tv_all_yield_img,v);
                break;
            case R.id.tv_yellow_white:
                status=ModeStatus.YWPI;
                setTextViewSelect(tv_yellow_white_img,v);
                break;
        }

    }

    @Override
    public void onItemClick(StockModeMenu.Time time, boolean select) {
        this.mode_time = time.getTime();
        if(dialog_layout!=null&&dialog_layout.getVisibility()==View.GONE){
            dialog_layout.setVisibility(View.VISIBLE);
        }
        if(handler!=null){
            handler.removeCallbacks(runnable);
            handler.post(runnable);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        bottom_layout.post(new Runnable() {
            @Override
            public void run() {
                ViewGroup.LayoutParams layoutParams = scroll_bottom_layout.getLayoutParams();
                layoutParams.height = bottom_layout.getHeight() + 10;
                scroll_bottom_layout.setLayoutParams(layoutParams);
            }
        });
        if(handler!=null){
            handler.post(runnable);
        }
        if(dialog_layout!=null&&dialog_layout.getVisibility()==View.GONE){
            dialog_layout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            if(dialog_layout!=null&&dialog_layout.getVisibility()==View.GONE){
                dialog_layout.setVisibility(View.VISIBLE);
            }
            if(mode_tab_radiogroup.getCheckedRadioButtonId()!=R.id.mode_radio_today){
                mode_tab_radiogroup.check(R.id.mode_radio_today);
            }else{
                if(handler!=null){
                    handler.removeCallbacks(runnable);
                    handler.post(runnable);
                }
            }

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(handler!=null){
            handler.removeCallbacks(runnable);
            handler=null;
            runnable=null;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(handler!=null){
            handler.removeCallbacks(runnable);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(ShowActivity.isLogin(getActivity())){
            Bundle bundle=new Bundle();
            bundle.putSerializable(IntentKey.STOCKHOLDER, adapter.getItem(position));
            ShowActivity.showActivity(getActivity(),bundle,StockDetailActivity.class);
        }
//        StockModeDialog.createDialog(getActivity())
//                .setStockMode(adapter.getItem(position))
//                .setBtnClick(new StockModeDialog.OnBtnClick() {
//                    @Override
//                    public void common(StockMode stockMode) {
//
//                    }
//
//                    @Override
//                    public void buy(StockMode stockMode) {
//                        if(ShowActivity.isLogin(getActivity())){
//                            if(matchInfo==null){
//                                getMatchInfo(true,stockMode);
//                            }else{
//                                Bundle bundle=new Bundle();
//                                bundle.putSerializable(IntentKey.MATCH_BAEN, matchInfo);
//                                bundle.putSerializable(IntentKey.STOCKHOLDER, stockMode);
//                                ShowActivity.showActivity(getActivity(), bundle, StockBuyActivity.class);
//                            }
//                        }
//                    }
//                    @Override
//                    public void buyincl(StockMode stockMode) {
//                        if(ShowActivity.isLogin(getActivity())){
//                            ShowActivity.showWebViewActivity(getActivity(), HtmlUrl.TRADER_BUY+stockMode.getSymbol());
//                        }
//                    }
//                    @Override
//                    public void share(StockMode stockMode) {
//                        if(ShowActivity.isLogin(getActivity())){
//                            Bundle bundle=new Bundle();
//                            bundle.putSerializable(IntentKey.STOCKHOLDER, stockMode);
//                            ShowActivity.showActivity(getActivity(),bundle,StockModeShareAcitivity.class);
//                        }
//                    }
//                    @Override
//                    public void copy(StockMode stockMode) {
//                        ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
//                        ClipData myClip;
//                        String text =stockMode.getSymbol();
//                        myClip = ClipData.newPlainText("text", text);
//                        cm.setPrimaryClip(myClip);
//                        Toast.makeText(getActivity(),"已复制到剪切板",Toast.LENGTH_SHORT).show();
//                    }
//                })
//        .show();
    }
    private MatchInfo matchInfo;
    private void getMatchInfo(final boolean isShow,final StockMode stockMode){
//        WebBase.getPlayer(new JSONHandler() {
        WebBase.getMatchPlayer(new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
//                matchInfo=JSONParse.getMatchMessage(obj);
                matchInfo=JSONParse.getMatchMessage1(obj);
                if(isShow){
                    Bundle bundle=new Bundle();
                    bundle.putSerializable(IntentKey.MATCH_BAEN, matchInfo);
                    bundle.putSerializable(IntentKey.STOCKHOLDER, stockMode);
                    ShowActivity.showActivity(getActivity(), bundle, StockBuyActivity.class);
                }
            }

            @Override
            public void onFailure(String err_msg) {
                Toast.makeText(getActivity(),err_msg,Toast.LENGTH_SHORT).show();
            }
        });
    }

}
