package com.zbmf.StocksMatch.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.adapter.QuatationAdapter;
import com.zbmf.StocksMatch.api.Get2Api;
import com.zbmf.StocksMatch.api.Get2ApiImpl;
import com.zbmf.StocksMatch.beans.Quotation;
import com.zbmf.StocksMatch.utils.DataLoadDirection;
import com.zbmf.StocksMatch.utils.UiCommon;
import com.zbmf.StocksMatch.widget.LoadingDialog;
import com.zbmf.StocksMatch.widget.pulltorefresh.PullToRefreshBase;
import com.zbmf.StocksMatch.widget.pulltorefresh.PullToRefreshExpandableListView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class QuotationFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private PullToRefreshExpandableListView refresh_view;
    private List<List<Quotation>> lists = new ArrayList<List<Quotation>>();
    private QuatationAdapter adapter1;
    private Get2Api server = null;
    private boolean isFirstIn = true;
    private ExpandableListView elisView;
    private ImageView iv_right,iv_refresh;
    private View rootView;
    public QuotationFragment() {
    }

    public static QuotationFragment newInstance(String param1, String param2) {
        QuotationFragment fragment = new QuotationFragment();
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
        if(rootView==null){
            rootView=inflater.inflate(R.layout.activity_quotation, null);
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
        refresh_view = (PullToRefreshExpandableListView) view.findViewById(R.id.myscrllview);
        refresh_view.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        TextView tv_title = (TextView)view.findViewById(R.id.tv_title);

        tv_title.setText(R.string.hq);
        view.findViewById(R.id.iv_back).setVisibility(View.GONE);

        iv_right = (ImageView)view.findViewById(R.id.iv_right);
        iv_right.setImageResource(R.drawable.sx);
        iv_right.setVisibility(View.VISIBLE);
        iv_refresh = (ImageView)view.findViewById(R.id.iv_refresh);
        iv_right = (ImageView)view.findViewById(R.id.iv_right);
        iv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshAnim();
            }
        });
        adapter1 = new QuatationAdapter(getActivity(),lists);
        elisView = refresh_view.getRefreshableView();
        elisView.setAdapter(adapter1);
//        if(isFirstIn)
//            UiCommon.INSTANCE.showDialog(getActivity(),R.string.quotation_getting);


        refresh_view.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ExpandableListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ExpandableListView> refreshView) {
                new GetStockRealtimeInfo2Task(getActivity()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,DataLoadDirection.Refresh);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ExpandableListView> refreshView) {

            }
        });

//        elisView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
//            @Override
//            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
//                Log.e("tag","close....");
//                adapter1.closeAllLayout();
//                return true;
//            }
//        });
        elisView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter1.closeAllLayout();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        new GetStockRealtimeInfo2Task(getActivity()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, DataLoadDirection.Refresh);
    }
    private void refreshAnim(){
        iv_refresh.setVisibility(View.VISIBLE);
        iv_right.setVisibility(View.GONE);
        RotateAnimation refreshingAnimation = (RotateAnimation) AnimationUtils.loadAnimation(getActivity(), R.anim.rotating);
        LinearInterpolator lir = new LinearInterpolator();// 添加匀速转动动画
        refreshingAnimation.setInterpolator(lir);
        iv_refresh.startAnimation(refreshingAnimation);

        refreshingAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
//                refresh_view.autoRefresh();
                new GetStockRealtimeInfo2Task(getActivity()).execute(DataLoadDirection.Refresh);
                UiCommon.INSTANCE.showDialog(getActivity(),R.string.refreshing);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                UiCommon.INSTANCE.DialogDismiss();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }


    /**
     * 获取行情数据：自选股、大盘
     */
    private class GetStockRealtimeInfo2Task extends LoadingDialog<Integer,Quotation>{
        private int operation;
        public GetStockRealtimeInfo2Task(Context activity) {
            super(activity, R.string.loading, R.string.load_fail1,false);
        }

        @Override
        public Quotation doInBackground(Integer... params) {
            Quotation temp = null;
            operation = params[0];
            if(server == null){
                server = new Get2ApiImpl();
            }

            try {
                temp = server.getStockRealtimeInfo2();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return temp;
        }

        @Override
        public void onPostExecute(Quotation ret) {
            super.onPostExecute(ret);
            refresh_view.onRefreshComplete();
            UiCommon.INSTANCE.DialogDismiss();
            iv_refresh.clearAnimation();
            iv_refresh.setVisibility(View.GONE);
            iv_right.setVisibility(View.VISIBLE);
        }

        @Override
        public void doStuffWithResult(Quotation result) {
            if (result != null && result.code != -1) {
                if (result.getStatus()==1) {
                    if(result.getLists()!=null && result.getLists().size()>0){
                        lists.clear();
                        lists.addAll(result.getLists());
                        adapter1.notifyDataSetChanged();
                        elisView.expandGroup(0);//有数据展开第一个分组
                        elisView.expandGroup(1);
                    }
                } else {
                    UiCommon.INSTANCE.showTip(result.msg);

                }
            }else {
                UiCommon.INSTANCE.showTip(getActivity().getString(R.string.load_fail));
            }
        }
    }

}
