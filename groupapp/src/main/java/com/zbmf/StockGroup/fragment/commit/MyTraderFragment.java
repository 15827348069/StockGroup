package com.zbmf.StockGroup.fragment.commit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.activity.SimulateStockChatActivity;
import com.zbmf.StockGroup.activity.TraderDetailActivity;
import com.zbmf.StockGroup.adapter.MyTraderAdapter;
import com.zbmf.StockGroup.api.JSONHandler;
import com.zbmf.StockGroup.api.WebBase;
import com.zbmf.StockGroup.beans.MatchInfo;
import com.zbmf.StockGroup.beans.Traders;
import com.zbmf.StockGroup.constans.IntentKey;
import com.zbmf.StockGroup.fragment.BaseFragment;
import com.zbmf.StockGroup.utils.JSONParse;
import com.zbmf.StockGroup.utils.ShowActivity;
import com.zbmf.StockGroup.view.PullToRefreshBase;
import com.zbmf.StockGroup.view.PullToRefreshListView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * 操盘高手
 */
public class MyTraderFragment extends BaseFragment {
    private List<Traders>infolist;
    private MyTraderAdapter adapter;
    private PullToRefreshListView listview;
    private boolean isFirst=true;
    private LinearLayout ll_none;
    private TextView no_message_text,right_button;
    private MatchInfo matchInfo;
    public static MyTraderFragment newInstance() {
        MyTraderFragment fragment = new MyTraderFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected View setContentView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_my_trader_layout, null);
    }

    @Override
    protected void initView() {
        ll_none=getView(R.id.ll_none);
        no_message_text=getView(R.id.no_message_text);
        right_button=getView(R.id.tv_right_button);
        no_message_text.setText(getString(R.string.no_msg));
        right_button.setVisibility(View.VISIBLE);
        right_button.setText(getString(R.string.find_trader));
        right_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putSerializable(IntentKey.SELECT,3);
                bundle.putSerializable(IntentKey.MATCH_BAEN,matchInfo);
                ShowActivity.showActivity(getActivity(),bundle,SimulateStockChatActivity.class);
            }
        });
        listview=getView(R.id.listview);
        infolist=new ArrayList<>();
        adapter=new MyTraderAdapter(getActivity());
        adapter.setList(infolist);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Traders traders= (Traders) adapter.getItem(position-1);
                Bundle bundle=new Bundle();
                bundle.putSerializable(IntentKey.MATCH_BAEN,matchInfo);
                bundle.putSerializable(IntentKey.TRADER, traders);
                ShowActivity.showActivity(getActivity(), bundle,TraderDetailActivity.class);
            }
        });
        listview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        listview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                initData();
            }
        });
    }

    @Override
    protected void initData() {
        getTrackList();
    }
    public void getTrackList(){
        WebBase.TrackList(new JSONHandler(isFirst,getActivity(),getString(R.string.loading)) {
            @Override
            public void onSuccess(JSONObject obj) {
                listview.onRefreshComplete();
                if(infolist==null){
                    infolist=new ArrayList<Traders>();
                }else{
                    infolist.clear();
                }
                if(obj.has("trackers")){
                    infolist.addAll(JSONParse.getTradersList(obj.optJSONArray("trackers")));
                }
                if(infolist.size()==0){
                    if(ll_none.getVisibility()==View.GONE){
                        ll_none.setVisibility(View.VISIBLE);
                    }
                }else{
                    adapter.notifyDataSetChanged();
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
    public void onResume() {
        super.onResume();
        getPlayerMessage();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
    private void getPlayerMessage() {
//        WebBase.getPlayer(new JSONHandler() {
        WebBase.getMatchPlayer(new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
//                 matchInfo = JSONParse.getMatchMessage(obj);
                 matchInfo = JSONParse.getMatchMessage1(obj);
            }

            @Override
            public void onFailure(String err_msg) {

            }
        });
    }

}
