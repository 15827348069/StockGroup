package com.zbmf.StocksMatch.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.adapter.YieldAdapter;
import com.zbmf.StocksMatch.api.Get2Api;
import com.zbmf.StocksMatch.api.Get2ApiImpl;
import com.zbmf.StocksMatch.beans.Group;
import com.zbmf.StocksMatch.beans.MatchBean;
import com.zbmf.StocksMatch.beans.Yield;
import com.zbmf.StocksMatch.utils.DataLoadDirection;
import com.zbmf.StocksMatch.utils.UiCommon;
import com.zbmf.StocksMatch.widget.LoadingDialog;
import com.zbmf.StocksMatch.widget.pulltorefresh.PullToRefreshBase;
import com.zbmf.StocksMatch.widget.pulltorefresh.PullToRefreshListView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class YieldFragment extends Fragment {
    private static final String PARAM1 = "matchbean";
    private MatchBean matchBean;
    private String order;//0总    1日   7周
    private YieldAdapter adapter;
    private List<Yield> list = new ArrayList<Yield>();

    private Get2Api server = null;
    private static final int PAGE_SIZE = 50;//每页显示数量
    private static int PAGE_INDEX = 1;//当前页码

    private PullToRefreshListView content_view;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }
    public YieldFragment(){}

    public static YieldFragment newInstance(MatchBean matchBean) {

        Bundle args = new Bundle();
        args.putSerializable(PARAM1,matchBean);
        YieldFragment fragment = new YieldFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            matchBean = (MatchBean) getArguments().getSerializable(PARAM1);
        }
    }

//    public void setMatchBean(MatchBean matchBean) {
//        this.matchBean = matchBean;
//    }

    public void setOrder(String order) {
        this.order = order;
    }

;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.yield_frag,null);
        content_view = (PullToRefreshListView) view.findViewById(R.id.content_view);
        content_view.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        return  view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
      content_view.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
          @Override
          public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
              new GetYieldList(getActivity()).execute(DataLoadDirection.Refresh, 1);
          }

          @Override
          public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
              new GetYieldList(getActivity()).execute(DataLoadDirection.LoadMore,PAGE_INDEX);
          }
      });

        adapter = new YieldAdapter(getActivity());
        adapter.setList(list);
        content_view.setAdapter(adapter);
        content_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Yield yield = (Yield) parent.getItemAtPosition(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("matchbean", matchBean);
                Group group = new Group();
                group.setName(yield.getNickname());
                group.setAvatar(yield.getAvatar());
                group.setId(yield.getUser());
                group.setGid(yield.getUser());
                group.setGroup(yield.getUser());
                bundle.putSerializable("group", group);
                UiCommon.INSTANCE.showActivity(UiCommon.ACTIVITY_IDX_OPERAT, bundle);
            }
        });

        if("1".equals(order) && isFirstIn)
            UiCommon.INSTANCE.showDialog(getActivity(),R.string.loading);
        new GetYieldList(getActivity()).execute(DataLoadDirection.Refresh, 1);
    }
    private boolean isFirstIn = true;
    private class GetYieldList extends LoadingDialog<Integer, Yield> {

        private int operation;
        private int page;
        private List<Yield> tempList;

        public GetYieldList(Context activity) {
            super(activity, false, true);
        }

        @Override
        public Yield doInBackground(Integer... params) {
            operation = params[0];
            page = params[1];
            Yield ret = null;

            if(server == null){
                server = new Get2ApiImpl();
            }

            try {
                if(matchBean!=null)
                    ret = server.getYieldList(matchBean.getId(),order, page, PAGE_SIZE);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return ret;
        }

        @Override
        public void onPostExecute(Yield ret) {
            super.onPostExecute(ret);
            content_view.onRefreshComplete();
            UiCommon.INSTANCE.DialogDismiss();
        }

        @Override
        public void doStuffWithResult(Yield result) {
            if (result != null && result.code != -1) {
                if (result.getStatus()==1) {
                    tempList = result.getYields();
                    if (operation == DataLoadDirection.Refresh)
                        list.clear();
                    adapter.addList(tempList);
                } else {
                    UiCommon.INSTANCE.showTip(result.msg);
                }
            }else {
                UiCommon.INSTANCE.showTip(getString(R.string.load_fail));
            }
        }
    }
}
