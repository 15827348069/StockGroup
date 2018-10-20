package com.zbmf.StockGroup.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.adapter.StockAdapter;
import com.zbmf.StockGroup.api.JSONHandler;
import com.zbmf.StockGroup.api.WebBase;
import com.zbmf.StockGroup.constans.IntentKey;
import com.zbmf.StockGroup.dialog.TextDialog;
import com.zbmf.StockGroup.interfaces.DialogYesClick;
import com.zbmf.StockGroup.utils.LogUtil;
import com.zbmf.StockGroup.utils.ShowActivity;

import org.json.JSONObject;


/**
 * Created by xuhao on 2017/8/31.
 */

public class AskStockSendActivity extends BaseActivity implements View.OnClickListener {
    private ImageButton send_button;
    private EditText etContent;
    private AutoCompleteTextView etStock;
    private StockAdapter adapter;
    private TextView tvInputNumber;
    private TextDialog textDialog;
    private int flag;
    private String stock_symbol,stock_name;
    @Override
    public int getLayoutResId() {
        return R.layout.activity_ask_stock_layout;
    }

    @Override
    public void initView() {
        initTitle("提问");
        send_button = getView(R.id.imb_send);
        send_button.setVisibility(View.VISIBLE);
        etStock = getView(R.id.et_input_stock);
        etContent = getView(R.id.et_input_content);
        tvInputNumber = getView(R.id.tv_input_number);
    }

    @Override
    public void initData() {
        flag=getIntent().getIntExtra(IntentKey.FLAG,0);
        stock_name=getIntent().getStringExtra(IntentKey.STOCK_NAME);
        stock_symbol=getIntent().getStringExtra(IntentKey.STOCK_SYMOL);
        if(stock_name!=null){
            etStock.setText(stock_name+"("+stock_symbol+")");
            showSoftInputFromWindow(etContent);
        }
        adapter=new StockAdapter(AskStockSendActivity.this);
        etStock.setAdapter(adapter);
    }
    private void showSoftInputFromWindow(EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }
    @Override
    public void addListener() {
        send_button.setOnClickListener(this);
        etStock.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                stock_symbol=adapter.getSymbolAtPosition(position);
            }
        });
        etStock.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(before>=1){
                    stock_symbol=null;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tvInputNumber.setText(s.length() + "/140");
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imb_send:
                String content = etContent.getText().toString();
                if (TextUtils.isEmpty(stock_symbol)) {
                    showToast("请输入正确的股票信息");
                    return;
                }
                if (TextUtils.isEmpty(content)) {
                    showToast("请输入提问内容");
                    return;
                }
                if(flag==1||flag==0){
                    //发送
                    WebBase.sendAskStock(stock_symbol, content, new JSONHandler(true, AskStockSendActivity.this, getString(R.string.commit)) {
                        @Override
                        public void onSuccess(JSONObject obj) {
                            if (textDialog == null) {
                                textDialog = getTextDialog();
                            }
                            textDialog.show();
                        }

                        @Override
                        public void onFailure(String err_msg) {
                            showToast(err_msg);
                        }
                    });
                }else if(flag==2||flag==3){
                    WebBase.stockAsk(stock_symbol, content, new JSONHandler(true, AskStockSendActivity.this, getString(R.string.commit)) {
                        @Override
                        public void onSuccess(JSONObject obj) {
                            if (textDialog == null) {
                                textDialog = getTextDialog();
                            }
                            textDialog.show();
                        }

                        @Override
                        public void onFailure(String err_msg) {
                            showToast(err_msg);
                        }
                    });
                }
                break;
        }
    }
    private TextDialog getTextDialog(){
        return  TextDialog.createDialog(AskStockSendActivity.this)
                .setLeftButton(getString(R.string.cancel))
                .setMessage(getString(R.string.ask_success))
                .setRightButton("我的提问")
                .setRightClick(new DialogYesClick() {
                    @Override
                    public void onYseClick() {
                        switch (flag){
                            case 0:
                                ShowActivity.showActivity(AskStockSendActivity.this,MyQuestionActivity.class);
                                break;
                            case 2:
                                setResult(RESULT_OK);
                                break;
                            case 3:
                                Bundle bundle=new Bundle();
                                bundle.putInt(IntentKey.SELECT,1);
                                ShowActivity.showActivity(AskStockSendActivity.this,bundle,DongAskActivity.class);
                                break;
                        }
                        finish();
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
