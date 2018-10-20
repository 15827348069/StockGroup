package com.zbmf.StocksMatch.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.adapter.SearAdapter;
import com.zbmf.StocksMatch.api.Get2Api;
import com.zbmf.StocksMatch.api.Get2ApiImpl;
import com.zbmf.StocksMatch.beans.General;
import com.zbmf.StocksMatch.beans.Group;
import com.zbmf.StocksMatch.beans.MatchBean;
import com.zbmf.StocksMatch.beans.Stock;
import com.zbmf.StocksMatch.beans.User;
import com.zbmf.StocksMatch.db.Database;
import com.zbmf.StocksMatch.db.DatabaseImpl;
import com.zbmf.StocksMatch.utils.Constants;
import com.zbmf.StocksMatch.utils.UiCommon;
import com.zbmf.StocksMatch.widget.LoadingDialog;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * 比赛搜索
 *
 * @author Administrator
 */
public class SearchActivity extends ExActivity implements View.OnClickListener {

    private LinearLayout ll_title, ll_type;
    private ListView listview;
    private TextView tv_type;
    private EditText ed_sear;

    private Database db;
    private Get2Api server;
    private int searchType = 1;
    private String keyword;
//    private StockAdapter adapter;
    private SearAdapter adapter1;
    private List<General> list = new ArrayList<General>();
    private String from = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searh);

        getData();
        setupView();
    }

    private void getData() {
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            from = (String) bundle.getSerializable("from");
        }
    }

    private void setupView() {
        ll_title = (LinearLayout) findViewById(R.id.ll_title);
        ll_type = (LinearLayout) findViewById(R.id.ll_type);
        tv_type = (TextView) findViewById(R.id.tv_type);
        ed_sear = (EditText) findViewById(R.id.ed_sear);
        listview = (ListView) findViewById(R.id.listview);
        findViewById(R.id.tv_right).setOnClickListener(this);
        ll_type.setOnClickListener(this);

        adapter1 = new SearAdapter(this);
        adapter1.setList(list);
        listview.setAdapter(adapter1);

        initListener();
        ed_sear.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                keyword = s.toString();
                if(TextUtils.isEmpty(keyword)){
                    list.clear();
                    adapter1.notifyDataSetChanged();
                    return;
                }

                Log.e("tag","keyword:"+keyword+"----searchType:"+searchType);
                switch (searchType) {
                    case 1://比赛
                        new SearMatchTask(SearchActivity.this).execute(keyword);
                        break;
                    case 2://user
                        new SearUsersTask(SearchActivity.this).execute(keyword);
                        break;
                    case 3://本地搜股票
                        if (keyword.length() > 2)
                           new SearTask(SearchActivity.this).execute(keyword);
                        break;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

       /* if(Constants.FROM_QUOTATION.equals(from)){//1 match 2 user 3 quotation
            searchType = 3;
            ed_sear.setHint(R.string.sear_tip3);
            tv_type.setText(getString(R.string.quotation));
        }else */if(Constants.FROM_MATCH.equals(from)){
            searchType = 1;
            tv_type.setText(getString(R.string.match));
            ed_sear.setHint(R.string.sear_tip1);
        }

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                General t = (General) parent.getItemAtPosition(position);
                if(t instanceof User){
                    User u = (User)t;
                    Bundle bundle = new Bundle();
                    Group group = new Group();
                    group.setGroup(u.getUser_id());
                    group.setGid(u.getUser_id());
                    group.setId(u.getUser_id());
                    bundle.putSerializable("group",group);
                    UiCommon.INSTANCE.showActivity(UiCommon.ACTIVITY_IDX_USER,bundle);
                }else if(t instanceof MatchBean){
                    MatchBean m = (MatchBean)t;
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("matchbean", m);
                    if("1".equals(m.getIs_match_player()))//已参赛
                        UiCommon.INSTANCE.showActivity(UiCommon.ACTIVITY_IDX_MATCHDESC, bundle);
                    else
                        UiCommon.INSTANCE.showActivity(UiCommon.ACTIVITY_IDX_MATCHDETAIL, bundle);
                }else{
                    Stock s = (Stock)t;
//                    Bundle bundle = new Bundle();
//                    Quotation q= new Quotation();
//                    q.setSymbol(s.getSymbol());
//                    q.setName(s.getName());
//                    q.setCurrent(s.getCurrent()+"");
//                    bundle.putSerializable("quotation", q);
//                    UiCommon.INSTANCE.showActivity(UiCommon.ACTIVITY_IDX_STOCKDETAIL, bundle);
                    new GetStockRealtimeInfoTask(SearchActivity.this,R.string.stock_detail_getting,R.string.load_fail1,true).execute(s.getSymbol());
                }
            }
        });
    }
    private void initListener() {
        ed_sear.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_SEARCH:// 点击 搜索
                    {
                        keyword = ed_sear.getText().toString();
                        if (!TextUtils.isEmpty(keyword)) {
                            switch (searchType) {
                                case 1://比赛
                                    new SearMatchTask(SearchActivity.this).execute(keyword);
                                    break;
                                case 2://user
                                    new SearUsersTask(SearchActivity.this).execute(keyword);
                                    break;
                                case 3://本地搜股票
                                    if (keyword.length() > 2)
                                        new SearTask(SearchActivity.this).execute(keyword);
                                    break;
                            }
                        }
                        return true;
                    }
                    default:
                        break;
                }
                return false;
            }
        });
    }

    private void showView() {
        keyword = ed_sear.getText().toString();
        View pop = View.inflate(getApplicationContext(), R.layout.search_type, null);
        final PopupWindow pw = new PopupWindow(pop, 320, ViewGroup.LayoutParams.WRAP_CONTENT);
        pw.setBackgroundDrawable(new BitmapDrawable());
        pw.setFocusable(true);
        pw.setOutsideTouchable(false);
        pop.setAlpha(0.9f);
        int[] location = new int[2];
        final WindowManager.LayoutParams lp=getWindow().getAttributes();

        lp.alpha=0.8f;
        getWindow().setAttributes(lp);
        pop.findViewById(R.id.tv1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchType = 1;
                tv_type.setText(getString(R.string.match));
                ed_sear.setHint(R.string.sear_tip1);
                pw.dismiss();
                if (TextUtils.isEmpty(keyword)) {
                    list.clear();
                    adapter1.notifyDataSetChanged();
                    return;
                }
                new SearMatchTask(SearchActivity.this).execute(keyword);
            }
        });
        pop.findViewById(R.id.tv2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchType = 2;
                tv_type.setText(getString(R.string.user));
                ed_sear.setHint(R.string.sear_tip2);

                pw.dismiss();
                if(TextUtils.isEmpty(keyword)){
                    list.clear();
                    adapter1.notifyDataSetChanged();
                    return;
                }
                new SearUsersTask(SearchActivity.this).execute(keyword);
            }
        });
        pop.findViewById(R.id.tv3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchType = 3;
                tv_type.setText(getString(R.string.quotation));
                ed_sear.setHint(R.string.sear_tip3);
                pw.dismiss();
                if(TextUtils.isEmpty(keyword) || keyword.length()==2){
                    list.clear();
                    adapter1.notifyDataSetChanged();
                    return;
                }
                if (keyword.length() > 2)
                    new SearTask(SearchActivity.this).execute(keyword);

            }
        });
        ll_title.getLocationOnScreen(location);
        pw.showAsDropDown(ll_title, 0, 0);
        pw.update();
        pw.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                lp.alpha = 1.0f;
                getWindow().setAttributes(lp);
            }
        });

        Log.e("tag","searchType:"+searchType);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_right:
                finish();
                break;
            case R.id.ll_type:
                showView();
                break;
        }
    }

    public void clearEt(View view) {
    }

    private class SearTask extends LoadingDialog<String, Stock> {

        public SearTask(Context activity) {
            super(activity, false, true);
        }

        @Override
        public Stock doInBackground(String... params) {
            if (db == null) {
                db = new DatabaseImpl(SearchActivity.this);
            }

            return db.getStocks(params[0]);
        }

        @Override
        public void doStuffWithResult(Stock ret) {
            if(ret!=null && ret.getList()!=null){
                list.clear();
                if(ret.getList().size()>0){
                    list.addAll(ret.getList());
                }else{

                }
                adapter1.notifyDataSetChanged();
            }else{//未搜到符合条件的
                list.clear();adapter1.notifyDataSetChanged();
            }
        }
    }


    /*private class SearTask1 extends LoadingDialog<String, Cursor> {

        public SearTask1(Context activity) {
            super(activity, false, true);
        }

        @Override
        public Cursor doInBackground(String... params) {
            if (db == null) {
                db = new DatabaseImpl(SearchActivity.this);
            }

            return db.getStocks1(params[0]);
        }

        @Override
        public void doStuffWithResult(Cursor cursor) {
            if(cursor!=null){
                adapter = new StockAdapter(SearchActivity.this, cursor);
                listview.setAdapter(adapter);
            }
        }
    }*/

    private class SearUsersTask extends LoadingDialog<String, User> {

        public SearUsersTask(Context activity) {
            super(activity, R.string.loading, R.string.load_fail1,false);
        }

        @Override
        public User doInBackground(String... params) {
            User user = null;

            if (server == null) {
                server = new Get2ApiImpl();
            }
            try {
                user = server.searchUsers(params[0]);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return user;
        }

        @Override
        public void doStuffWithResult(User result) {
            if (result != null && result.code != -1) {
                if (result.getStatus() == 1) {
                    if(result.getList()!=null){
                        list.clear();
                        if(!TextUtils.isEmpty(keyword)&&result.getList().size()>0){
                            list.addAll(result.getList());
                        }else{

                        }
                        adapter1.notifyDataSetChanged();
                    }
                } else {
                    UiCommon.INSTANCE.showTip(result.msg);
                }
            } else {
                UiCommon.INSTANCE.showTip(SearchActivity.this.getString(R.string.load_fail));
            }
        }
    }

    /**
     * 搜索比赛
     */
    private class SearMatchTask extends LoadingDialog<String, MatchBean> {

        public SearMatchTask(Context activity) {
            super(activity, R.string.loading, R.string.load_fail1,false);
        }

        @Override
        public MatchBean doInBackground(String... params) {
            MatchBean matchBean = null;
            if (server == null) {
                server = new Get2ApiImpl();
            }

            try {
                matchBean = server.searchMatch(params[0]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return matchBean;
        }

        @Override
        public void doStuffWithResult(MatchBean result) {
            if (result != null && result.code != -1) {
                if (result.getStatus() == 1) {
                    if(result.getList()!=null){
                        list.clear();
                        if(!TextUtils.isEmpty(keyword)&&result.getList().size()>0){
                            list.addAll(result.getList());
                        }else{

                        }
                        adapter1.notifyDataSetChanged();
                    }
                } else {
                    UiCommon.INSTANCE.showTip(result.msg);
                }
            } else {
                UiCommon.INSTANCE.showTip(SearchActivity.this.getString(R.string.load_fail));
            }
        }
    }

    private class GetStockRealtimeInfoTask extends LoadingDialog<String,Stock> {

        public GetStockRealtimeInfoTask(Context activity, int loadingMsg, int failMsg, boolean Enddismiss) {
            super(activity, loadingMsg, failMsg, Enddismiss);
        }

        @Override
        public Stock doInBackground(String... params) {
            if(server == null){
                server = new Get2ApiImpl();
            }

            try {
                return  server.getStockRealtimeInfo(params[0]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void doStuffWithResult(Stock result) {
            if (result != null && result.code != -1) {
                if (result.getStatus()==1) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("stock", result);
                    UiCommon.INSTANCE.showActivity(UiCommon.ACTIVITY_IDX_STOCKDETAIL, bundle);
                } else {
                    UiCommon.INSTANCE.showTip(result.msg);
                }
            }else {
                UiCommon.INSTANCE.showTip(getString(R.string.load_fail));
            }
        }
    }
}
