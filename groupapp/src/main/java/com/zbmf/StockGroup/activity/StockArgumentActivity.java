package com.zbmf.StockGroup.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.adapter.StockAdapter;
import com.zbmf.StockGroup.api.JSONHandler;
import com.zbmf.StockGroup.api.WebBase;

import org.json.JSONObject;

public class StockArgumentActivity extends BaseActivity implements View.OnClickListener {
    private TextView mSubmitPeriod;
    private AutoCompleteTextView mEt_stock;
    private EditText mEt_stock_argument;
    private Button mSubmitStockBtn;
    private String mRound_id;
    private StockAdapter mAdapter;

   /* @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_argument);
    }*/

    @Override
    public int getLayoutResId() {
        return R.layout.activity_stock_argument;
    }

    @Override
    public void initView() {
        initTitle(getString(R.string.select_stock_match_zbmf));
        mSubmitPeriod = getView(R.id.submitPeriod);
        TextView submitObject = getView(R.id.submitObject);
        mEt_stock = getView(R.id.et_stock);
        mEt_stock_argument = getView(R.id.et_stock_argument);
        mSubmitStockBtn = getView(R.id.submitStockBtn);

        mAdapter = new StockAdapter(getApplicationContext());
        mEt_stock.setAdapter(mAdapter);
    }

    @Override
    public void initData() {
        nextSelectStockDetail();

    }

    @Override
    public void addListener() {
        mSubmitStockBtn.setOnClickListener(this);
        mEt_stock.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mAdapter.getSymbolAtPosition(position);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.submitStockBtn) {
            String etStock = mEt_stock.getText().toString();
            String etStockArgument = mEt_stock_argument.getText().toString();
            if (TextUtils.isEmpty(etStock)) {
                showToast("提交的股票代码不能为空!");
                return;
            }
            if (TextUtils.isEmpty(etStockArgument)) {
                showToast("提交的选股理由不能为空!");
                return;
            }
            submitStock(etStock,etStockArgument);
        }
    }

    //获取下次选股详情
    private void nextSelectStockDetail() {
        WebBase.nextSelectStockDetail(new JSONHandler() {

            @Override
            public void onSuccess(JSONObject obj) {
                if (obj.optString("status").equals("ok")) {
                    JSONObject round = obj.optJSONObject("round");
                    if (round != null) {
                        //周期ID
                        mRound_id = round.optString("round_id");
                        String round1 = round.optString("round");//第几周
                        String start_apply = round.optString("start_apply");//选股开始提交日期
                        String end_apply = round.optString("end_apply");//选股结束日期
                        String start_at = round.optString("start_at");//比赛开始日期
                        String end_at = round.optString("end_at");//比赛结束日期

                        mSubmitPeriod.setText(String.format("提交周期:%s", start_apply + "~" + end_apply));

                    }
                }
            }

            @Override
            public void onFailure(String err_msg) {
                showToast(err_msg);
            }
        });
    }

    private void submitStock(String symbol, String stockArguments) {
        WebBase.submitStock(symbol, stockArguments, new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                if (obj.optString("status").equals("ok")){
                    showToast("选股提交成功!");
                }
            }

            @Override
            public void onFailure(String err_msg) {
                if (err_msg.equals("非选股时间")){
                    showToast("每周选股时间为周五0:00-周日24:00");
                    return;
                }
                showToast(err_msg+"选股提交失败!");
            }
        });
    }

}
