package com.zbmf.StockGTec.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.zbmf.StockGTec.R;
import com.zbmf.StockGTec.adapter.TieFAdapter;
import com.zbmf.StockGTec.api.AppUrl;
import com.zbmf.StockGTec.api.JSONHandler;
import com.zbmf.StockGTec.api.WebBase;
import com.zbmf.StockGTec.beans.Fans;
import com.zbmf.StockGTec.utils.Constants;
import com.zbmf.StockGTec.utils.JSONparse;
import com.zbmf.StockGTec.utils.SettingDefaultsManager;
import com.zbmf.StockGTec.view.PullToRefreshBase;
import com.zbmf.StockGTec.view.PullToRefreshListView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 铁粉Fragment
 */
public class TieFragment extends Fragment {
    private String mParam1;
    private int page,PAGES;
    private TieFAdapter adapter;
    private List<Fans> list = new ArrayList<>();
    private PullToRefreshListView listview;
    public static final String METHOD = AppUrl.groupFans;
    private String groupId = SettingDefaultsManager.getInstance().getGroupId();

    public TieFragment() {

    }

    public static TieFragment newInstance() {
        TieFragment fragment = new TieFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tief, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listview = (PullToRefreshListView) view.findViewById(R.id.listview);
        adapter = new TieFAdapter(getActivity(),list);
        listview.setAdapter(adapter);
        listview.setMode(PullToRefreshBase.Mode.BOTH);
        listview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                groupFans(1,false);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                groupFans(2,false);
            }
        });
        groupFans(1,false);
    }


    private void groupFans(final int dir, boolean show){
        if(dir == 2){
            page++;
        }else
            page = 1;

        WebBase.Fans(METHOD, groupId, page, Constants.PER_PAGE, new JSONHandler(show,getActivity(),"正在加载...") {
            @Override
            public void onSuccess(JSONObject obj) {
                listview.onRefreshComplete();
                Fans fans = JSONparse.Fans(obj);
                PAGES=fans.pages;
                if(fans!=null && fans.getList()!=null)
                    if(dir ==1)
                        list.clear();
                    list.addAll(fans.getList());

               if (page == PAGES) {
//                    Toast.makeText(getContext(), "已加载全部数据", Toast.LENGTH_SHORT).show();
                    listview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                }else
                    listview.setMode(PullToRefreshBase.Mode.BOTH);


                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String err_msg) {
                listview.onRefreshComplete();
            }
        });
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
