package com.zbmf.StocksMatch.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.adapter.StockAdapter;
import com.zbmf.StocksMatch.common.IntentKey;
import com.zbmf.StocksMatch.listener.AddStockView;
import com.zbmf.StocksMatch.presenter.AddStockPresenter;
import com.zbmf.StocksMatch.util.MyActivityManager;
import com.zbmf.StocksMatch.util.StrUtils;
import com.zbmf.StocksMatch.view.ShowOrHideProgressDialog;

import butterknife.BindView;
import butterknife.OnClick;

public class AddStockActivity extends BaseActivity<AddStockPresenter> implements AddStockView {
    @BindView(R.id.tv_title_right)
    TextView tvTitleRight;
    @BindView(R.id.title_layout_id)
    LinearLayout title_layout_id;
    @BindView(R.id.add_stock_et_remark)
    EditText addStockEtRemark;
    @BindView(R.id.aet_stock)
    AutoCompleteTextView aetStock;
    @BindView(R.id.imv_clear_et)
    ImageView imvClearEt;
    @BindView(R.id.tv_qure)
    TextView tvQure;
    @BindView(R.id.imv_clear_et_remark)
    ImageView imvClearEtRemark;
    @BindView(R.id.imb_title_return)
    ImageButton imbTitleReturn;
    private AddStockPresenter mAddStockPresenter;
    private StockAdapter mStockAdapter;
    private String mSymbolAtPosition;

    @Override
    protected int getLayout() {
        return R.layout.activity_add_stock;
    }

    @Override
    protected String initTitle() {
        return getString(R.string.add_stock);
    }

    @Override
    protected void initData(Bundle bundle) {
        MyActivityManager.getMyActivityManager().pushAct(this);
        tvTitleRight.setVisibility(View.VISIBLE);
        mStockAdapter = new StockAdapter(this);
        aetStock.setAdapter(mStockAdapter);
        aetStock.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSymbolAtPosition = mStockAdapter.getSymbolAtPosition(position);
            }
        });
        aetStock.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    mSymbolAtPosition = null;
                    tvTitleRight.setSelected(false);
                    tvQure.setSelected(false);
                    imvClearEt.setVisibility(View.GONE);
                } else {
                    tvTitleRight.setSelected(true);
                    tvQure.setSelected(true);
                    imvClearEt.setVisibility(View.VISIBLE);
                    aetStock.setBackgroundResource(R.drawable.add_stock_et_bg);
                }
            }
        });
        addStockEtRemark.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    tvTitleRight.setSelected(false);
                    imvClearEtRemark.setVisibility(View.GONE);
                } else {
                    tvTitleRight.setSelected(true);
                    imvClearEtRemark.setVisibility(View.VISIBLE);
                }
            }
        });
//        add_stock_et_name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                    //搜索联想股票
//                    return true;
//                }
//                return false;
//            }
//        });
    }

    @Override
    protected AddStockPresenter initPresent() {
        mAddStockPresenter = new AddStockPresenter();
        return mAddStockPresenter;
    }

    @Override
    public void addResult(String msg) {
        ShowOrHideProgressDialog.disMissProgressDialog();
        if (!TextUtils.isEmpty(msg)) {
            if (msg.equals("ok")){
                setResult(IntentKey.ADD_STOCK_RESULT_CODE,new Intent());
                finish();
            }
        }
    }

    @Override
    public void addErro(String msg) {
        ShowOrHideProgressDialog.disMissProgressDialog();
        if (!TextUtils.isEmpty(msg)) {
            showToast(msg);
        }
    }

    @OnClick({R.id.tv_title_right, R.id.imv_clear_et,R.id.imv_clear_et_remark})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_title_right:
                addStock();
                break;
            case R.id.imv_clear_et:
                if (!TextUtils.isEmpty(aetStock.getText().toString())) {
                    aetStock.getText().clear();
                }
                mSymbolAtPosition = null;
                break;
            case R.id.imv_clear_et_remark:
                if (!TextUtils.isEmpty(addStockEtRemark.getText().toString())) {
                    addStockEtRemark.getText().clear();
                }
                break;
        }
    }

    private void addStock() {
        if (TextUtils.isEmpty(aetStock.getText().toString())) {
            aetStock.setBackgroundResource(R.drawable.add_stock_et_bg_tip);
            showToast(getString(R.string.add_stock_symbol_tip));
            return;
        }
        mAddStockPresenter.addStock(subStockCode(aetStock.getText().toString()),
                TextUtils.isEmpty(addStockEtRemark.getText().toString()) ? "" : addStockEtRemark.getText().toString());
        ShowOrHideProgressDialog.showProgressDialog(this,this,getString(R.string.stock_adding));
    }

    private String subStockCode(String stockNameCode) {
        if (stockNameCode.contains("(")&&stockNameCode.contains(")")){
            return stockNameCode.substring(stockNameCode.indexOf("(") + 1, stockNameCode.indexOf(")"));
        }else if (StrUtils.isNumeric1(stockNameCode)){
            return stockNameCode;
        }else {
            showToast(getString(R.string.err_stock_code_tip));
            aetStock.setBackgroundResource(R.drawable.add_stock_et_bg_tip);
            return "";
        }
    }
    private String subStockName(String stock){
        return stock.substring(0,stock.indexOf("(")-1);
    }
}
