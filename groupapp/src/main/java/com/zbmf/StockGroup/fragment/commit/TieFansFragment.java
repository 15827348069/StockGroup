package com.zbmf.StockGroup.fragment.commit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.activity.FansDiscountsActivity;
import com.zbmf.StockGroup.activity.FindTeacherActivity;
import com.zbmf.StockGroup.adapter.BoxItemAdapter;
import com.zbmf.StockGroup.api.JSONHandler;
import com.zbmf.StockGroup.api.WebBase;
import com.zbmf.StockGroup.beans.BoxBean;
import com.zbmf.StockGroup.constans.RequestCode;
import com.zbmf.StockGroup.fragment.BaseFragment;
import com.zbmf.StockGroup.utils.JSONParse;
import com.zbmf.StockGroup.utils.ShowActivity;
import com.zbmf.StockGroup.view.PullToRefreshBase;
import com.zbmf.StockGroup.view.PullToRefreshListView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * 铁粉
 */
public class TieFansFragment extends BaseFragment {
    private int page,pages;
    private List<BoxBean>infolist;
    private BoxItemAdapter adapter;
    private PullToRefreshListView listview;
    private boolean isFirst=true;
    private LinearLayout ll_none;
    private TextView no_message_text,right_button,left_button;
    public static TieFansFragment newInstance() {
        TieFansFragment fragment = new TieFansFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected View setContentView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_coupon_used, null);
    }

    @Override
    protected void initView() {

        ll_none=getView(R.id.ll_none);
        no_message_text=getView(R.id.no_message_text);
        right_button=getView(R.id.tv_right_button);
        left_button=getView(R.id.tv_left_button);
        no_message_text.setText(getString(R.string.no_msg));
        right_button.setVisibility(View.VISIBLE);
        right_button.setText(getString(R.string.fans_coupons));
        right_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowActivity.showActivity(getActivity(), FansDiscountsActivity.class);
            }
        });
        left_button.setVisibility(View.VISIBLE);
        left_button.setText(getString(R.string.find_teacher));
        left_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowActivity.showActivityForResult(getActivity(),null, FindTeacherActivity.class, RequestCode.STUDY);
            }
        });
        listview=getView(R.id.listview);
        infolist=new ArrayList<>();
        adapter=new BoxItemAdapter(getContext(),infolist);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ShowActivity.showBoxDetailActivity(getActivity(),infolist.get(position-1));
            }
        });
        listview.setMode(PullToRefreshBase.Mode.BOTH);
        listview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                //下拉刷新
                initData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                //上拉加载
                page+=1;
                getFans();
            }
        });
    }

    @Override
    protected void initData() {
        if(infolist==null){
            infolist=new ArrayList<>();
        }else{
            infolist.clear();
        }
        page=1;
        getFans();
    }
    public void getFans(){
        WebBase.getUserBoxs("0", "0", page, new JSONHandler(isFirst,getActivity(),getString(R.string.loading)) {
            @Override
            public void onSuccess(JSONObject obj) {
                BoxBean bb = JSONParse.getGroupBoxs(obj);
                if(bb!=null && bb.getList()!=null ){
                    infolist.addAll(bb.getList());
                }
                adapter.notifyDataSetChanged();
                listview.onRefreshComplete();
                if(infolist.size()==0){
                    page=bb.getPage();
                    pages=bb.getPages();
                    if(ll_none.getVisibility()==View.GONE){
                        ll_none.setVisibility(View.VISIBLE);
                    }
                }else{
                    if(ll_none.getVisibility()==View.VISIBLE){
                        ll_none.setVisibility(View.GONE);
                    }
                }
                if(isFirst){
                    isFirst=false;
                }
            }

            @Override
            public void onFailure(String err_msg) {
                if(isFirst){
                    isFirst=false;
                }
            }
        });
    }
    @Override
    public void onDetach() {
        super.onDetach();
    }

}
