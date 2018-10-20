package com.zbmf.StocksMatch.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.adapter.SelStockAdapter;
import com.zbmf.StocksMatch.api.Get2Api;
import com.zbmf.StocksMatch.api.Get2ApiImpl;
import com.zbmf.StocksMatch.beans.Stock;
import com.zbmf.StocksMatch.db.Database;
import com.zbmf.StocksMatch.db.DatabaseImpl;
import com.zbmf.StocksMatch.utils.AssetsDatabaseManager;
import com.zbmf.StocksMatch.utils.UiCommon;
import com.zbmf.StocksMatch.widget.LoadingDialog;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class SeaStockActivity extends ExActivity implements View.OnClickListener {

    private LinearLayout ll_type;
    private ImageView iv_back;
    private EditText ed_sear;
    private ListView listview;
    private Database db;
    private Get2Api server;
    private List<Stock> list=new ArrayList<>();
    private List<Stock> hisList=new ArrayList<>();
    private SelStockAdapter adapter;
    private TextView tv_tip1,tv_tip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sea_stock);

        if(db==null)
           db = new DatabaseImpl(this);
        setupView();
        new GetHis(this).execute();
    }

    private void setupView() {
        ll_type = (LinearLayout)findViewById(R.id.ll_type);
        ll_type.setVisibility(View.GONE);
        iv_back = (ImageView)findViewById(R.id.iv_back);iv_back.setVisibility(View.VISIBLE);

        tv_tip = (TextView)findViewById(R.id.tv_tip);
        tv_tip1 = (TextView)findViewById(R.id.tv_tip1);
        tv_tip1.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        ed_sear = (EditText)findViewById(R.id.ed_sear);
        ed_sear.setHint(R.string.sear_tip3);
        findViewById(R.id.tv_right).setVisibility(View.INVISIBLE);
        listview = (ListView)findViewById(R.id.listview);
        initListener();
        ed_sear.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String keyword = s.toString();
                if (TextUtils.isEmpty(keyword)) {
//                    list.clear();
                    adapter.notifyDataSetChanged();
                    return;
                }

                if (keyword.length() > 2)
                    new SearTask(SeaStockActivity.this).execute(keyword);
                else
                    new GetHis(SeaStockActivity.this).execute();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        adapter = new SelStockAdapter(this);
        adapter.setList(list);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Stock stock = (Stock) parent.getItemAtPosition(position);
                new GetStockRealtimeInfoTask(SeaStockActivity.this,R.string.stock_detail_getting,R.string.load_fail,true).execute(stock.getSymbol());
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void initListener() {
        ed_sear.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_SEARCH:// 点击 搜索
                    {
                        String keyword = ed_sear.getText().toString();
                        if (!TextUtils.isEmpty(keyword)) {

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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_tip1://暂无历史记录 和 清除历史记录
                new DelTask(this).execute();
                break;
        }
    }

    private class DelTask extends LoadingDialog<Void,Boolean>{

        public DelTask(Context activity) {
            super(activity, false, true);
        }

        @Override
        public Boolean doInBackground(Void... params) {
            db.delStockHis();
            return true;
        }

        @Override
        public void doStuffWithResult(Boolean aBoolean) {
            if(aBoolean){
                list.clear();
                adapter.notifyDataSetChanged();
                tv_tip1.setText(R.string.sear_his_notip);
                tv_tip1.setClickable(false);
                tv_tip.setVisibility(View.GONE);
            }

        }
    }
    private class SearTask extends LoadingDialog<String, Stock> {

        public SearTask(Context activity) {
            super(activity, false, true);
        }

        @Override
        public Stock doInBackground(String... params) {
            return db.getStocks(params[0]);
        }

        @Override
        public void doStuffWithResult(Stock ret) {
            if(ret!=null && ret.getList()!=null){
                if(ret.getList().size()>0){
                    list.clear();
                    adapter.addList(ret.getList());
                    tv_tip1.setVisibility(View.GONE);
                    tv_tip.setVisibility(View.GONE);
                }else{

                }
            }else{//未搜到符合条件的
                list.clear();adapter.notifyDataSetChanged();
            }
        }
    }

    private class GetHis extends LoadingDialog<Void,List<Stock>>{

        public GetHis(Context activity) {
            super(activity, false, true);
        }

        @Override
        public List<Stock> doInBackground(Void... params) {
            return db.getStockHis(3);
        }

        @Override
        public void doStuffWithResult(List<Stock> stocks) {
            if(stocks!=null && stocks.size()>0){
                list.clear();
                adapter.addList(stocks);
                tv_tip1.setText(R.string.clear_sear_his);
                tv_tip1.setClickable(true);
                tv_tip.setVisibility(View.VISIBLE);
                tv_tip1.setVisibility(View.VISIBLE);
            }else{
                tv_tip.setVisibility(View.GONE);
                tv_tip1.setClickable(false);
                tv_tip1.setText(R.string.sear_his_notip);
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
                    db.addStockHis(result.getName(),result.getSymbol(),3);
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
