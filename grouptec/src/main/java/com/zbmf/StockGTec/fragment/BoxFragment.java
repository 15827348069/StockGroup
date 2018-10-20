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
import com.zbmf.StockGTec.adapter.BoxItemAdapter;
import com.zbmf.StockGTec.api.JSONHandler;
import com.zbmf.StockGTec.api.WebBase;
import com.zbmf.StockGTec.beans.BoxBean;
import com.zbmf.StockGTec.utils.Constants;
import com.zbmf.StockGTec.utils.JSONparse;
import com.zbmf.StockGTec.utils.SettingDefaultsManager;
import com.zbmf.StockGTec.view.PullToRefreshBase;
import com.zbmf.StockGTec.view.PullToRefreshListView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 宝盒Fragment
 */
public class BoxFragment extends Fragment {
    private String mParam1 = "group";
    private PullToRefreshListView listview;
    private List<BoxBean> boxs = new ArrayList<>();
    private int page,PAGES;
    private BoxItemAdapter adapter;
    private String groupId = SettingDefaultsManager.getInstance().UserId();
    private boolean isFirst = true;
    public BoxFragment() {

    }

    public static BoxFragment newInstance() {
        BoxFragment fragment = new BoxFragment();

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
        adapter = new BoxItemAdapter(getActivity(),boxs);
        listview.setAdapter(adapter);
        listview.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        listview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                getGroupBoxs(1,false);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                getGroupBoxs(1,false);
            }
        });

        if(isFirst)
        getGroupBoxs(1,false);
    }

    public void getGroupBoxs(int dir,boolean show){
        if(dir == 1){
            page++;
        }

        WebBase.getGroupBoxs(SettingDefaultsManager.getInstance().authToken(), page, Constants.PER_PAGE, new JSONHandler(show,getActivity(),"正在加载数据...") {
            @Override
            public void onSuccess(JSONObject object) {
                listview.onRefreshComplete();
                isFirst = false;
                BoxBean boxBean = JSONparse.getGroupBoxs(object);
                PAGES = boxBean.getPages();
                if(boxBean!=null && boxBean.getList()!=null )
                    boxs.addAll(boxBean.getList());

                if (page == PAGES){
//                    Toast.makeText(getContext(), "已加载全部数据", Toast.LENGTH_SHORT).show();
                    listview.setMode(PullToRefreshBase.Mode.DISABLED);
                }


                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String err_msg) {
                listview.onRefreshComplete();
            }
        });

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
