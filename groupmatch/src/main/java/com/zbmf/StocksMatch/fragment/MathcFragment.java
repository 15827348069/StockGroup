package com.zbmf.StocksMatch.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.adapter.MatchAdapter;
import com.zbmf.StocksMatch.api.Get2Api;
import com.zbmf.StocksMatch.api.Get2ApiImpl;
import com.zbmf.StocksMatch.beans.MatchBean;
import com.zbmf.StocksMatch.utils.Constants;
import com.zbmf.StocksMatch.utils.DataLoadDirection;
import com.zbmf.StocksMatch.utils.UiCommon;
import com.zbmf.StocksMatch.widget.CustomListView;
import com.zbmf.StocksMatch.widget.LoadingDialog;
import com.zbmf.StocksMatch.widget.pulltorefresh.PullToRefreshBase;
import com.zbmf.StocksMatch.widget.pulltorefresh.PullToRefreshScrollView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的比赛
 *
 * @author lulu
 */
public class MathcFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private PullToRefreshScrollView myscrllview;
    private CustomListView listView;
    private boolean isFirstIn = true;
    private static final int PAGE_SIZE = 100;
    private static int PAGE_INDEX = 1;
    private ImageView iv_right;
    private LinearLayout ll_search;
    private List<MatchBean> list = new ArrayList<MatchBean>();
    private Get2Api server = null;
    private MatchAdapter adapter;
    private View rootView;

    public MathcFragment() {
    }

    public static MathcFragment newInstance(String param1, String param2) {
        MathcFragment fragment = new MathcFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.activity_mine_match, null);
        }
        //缓存的rootView需要判断是否已经被加过parent， 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.iv_back).setVisibility(View.GONE);
        listView = (CustomListView) view.findViewById(R.id.content_view);
        myscrllview = (PullToRefreshScrollView) view.findViewById(R.id.myscrllview);
        iv_right = (ImageView) view.findViewById(R.id.iv_right);
        iv_right.setVisibility(View.VISIBLE);
        iv_right.setOnClickListener(this);
        myscrllview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        ll_search = (LinearLayout)view.findViewById(R.id.ll_search);
        ll_search.setOnClickListener(this);
        myscrllview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                new GetMyMatch(getActivity()).execute(DataLoadDirection.Refresh, 1);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {

            }
        });

        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_title.setText(R.string.wdbs);

        adapter = new MatchAdapter(getActivity());
        adapter.setList(list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MatchBean matchbean = (MatchBean) parent.getItemAtPosition(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("matchbean", matchbean);
                UiCommon.INSTANCE.showActivity(UiCommon.ACTIVITY_IDX_MATCHDESC, bundle);
            }
        });

        if (isFirstIn) {
            UiCommon.INSTANCE.showDialog(getActivity(), R.string.m_getting);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        new GetMyMatch(getActivity()).execute(DataLoadDirection.Refresh, PAGE_INDEX);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_right://搜索
                Bundle bundle = new Bundle();
                bundle.putSerializable("from", Constants.FROM_MATCH);
                UiCommon.INSTANCE.showActivity(UiCommon.ACTIVITY_IDX_MATCHSEARCH, bundle);
                break;
            case R.id.ll_search:
                UiCommon.INSTANCE.showActivity(UiCommon.ACTIVITY_IDX_ALLMATCH, null);
                break;
        }
    }

    private class GetMyMatch extends LoadingDialog<Integer, MatchBean> {

        public GetMyMatch(Context activity) {
            super(activity, R.string.loading, R.string.load_fail1,false);
        }

        @Override
        public MatchBean doInBackground(Integer... params) {
            MatchBean ret = null;
            if (server == null) {
                server = new Get2ApiImpl();
            }

            try {
                ret = server.getRunMatches();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return ret;
        }

        @Override
        public void onPostExecute(MatchBean ret) {
            super.onPostExecute(ret);
            myscrllview.onRefreshComplete();
            UiCommon.INSTANCE.DialogDismiss();
            isFirstIn = false;
            ll_search.setVisibility(View.VISIBLE);
        }

        @Override
        public void doStuffWithResult(MatchBean result) {
            if (result != null && result.code != -1) {
                if (result.getStatus() == 1) {
                    if (result.getList() != null) {
                        list.clear();
                        adapter.addList(result.getList());
                    } else if (list.size() == 0)
                        UiCommon.INSTANCE.showTip(getString(R.string.mine_match_tip));
                } else {
                    UiCommon.INSTANCE.showTip(result.msg);
                }
            } else {
                UiCommon.INSTANCE.showTip(getString(R.string.load_fail));
            }
        }
    }

}
