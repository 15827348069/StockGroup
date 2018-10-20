package com.zbmf.groupro.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zbmf.groupro.R;
import com.zbmf.groupro.adapter.BoxItemAdapter;
import com.zbmf.groupro.api.JSONHandler;
import com.zbmf.groupro.api.WebBase;
import com.zbmf.groupro.beans.BoxBean;
import com.zbmf.groupro.utils.JSONParse;
import com.zbmf.groupro.utils.ShowActivity;
import com.zbmf.groupro.view.PullToRefreshBase;
import com.zbmf.groupro.view.PullToRefreshListView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * 铁粉
 */
public class TieFansFragment extends BaseFragment {
    private LinearLayout ll_none;
    private int page,pages;
    private List<BoxBean>infolist;
    private BoxItemAdapter adapter;
    private PullToRefreshListView listview;
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
        ll_none = getView(R.id.ll_none);
        getView(R.id.btn_focus).setVisibility(View.GONE);
        ll_none.setVisibility(View.VISIBLE);
        TextView textView= getView(R.id.tv_tip);
        textView.setText("还没有任何订阅内容哦");
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
        listview.getLoadingLayoutProxy().setPullLabel("加载更多数据");
        listview.getLoadingLayoutProxy().setRefreshingLabel("正在加载新数据...");
        listview.getLoadingLayoutProxy().setReleaseLabel("松开加载数据");
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
        WebBase.getUserBoxs("0", "0", page, new JSONHandler() {
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
                    if(page==pages){
                        Toast.makeText(getActivity(),"已加载全部数据",Toast.LENGTH_SHORT).show();
                    }
                    if(ll_none.getVisibility()==View.GONE){
                        ll_none.setVisibility(View.VISIBLE);
                    }
                }else{
                    if(ll_none.getVisibility()==View.VISIBLE){
                        ll_none.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(String err_msg) {

            }
        });
    }
    @Override
    public void onDetach() {
        super.onDetach();
    }
}
