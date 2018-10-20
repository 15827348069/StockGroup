package com.zbmf.StockGroup.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.adapter.StockAdapter;
import com.zbmf.StockGroup.api.JSONHandler;
import com.zbmf.StockGroup.api.WebBase;
import com.zbmf.StockGroup.constans.Commons;
import com.zbmf.StockGroup.constans.IntentKey;

import org.json.JSONObject;

/**
 * Created by xuhao on 2018/1/31.
 */

public class TimeSettingAddActivity extends BaseActivity implements View.OnClickListener, TextWatcher {
    private RadioGroup radioGroup;
    private AutoCompleteTextView autoCompleteTextView;
    private EditText editText;
    private LinearLayout tagLl;
    private Button button;
    private StockAdapter stockAdapter;
    private String searchKey;
    private boolean isStock=true;
    private ImageView imv_clear_et;
    @Override
    public int getLayoutResId() {
        return R.layout.activity_add_time_setting;
    }

    @Override
    public void initView() {
        initTitle("添加监控");
        button=getView(R.id.group_title_right_button);
        button.setText("保存");
        button.setVisibility(View.VISIBLE);
        button.setOnClickListener(this);
        radioGroup=getView(R.id.radio_group);
        autoCompleteTextView=getView(R.id.aet_stock);
        stockAdapter = new StockAdapter(this);
        imv_clear_et=getView(R.id.imv_clear_et);
        imv_clear_et.setOnClickListener(this);
        autoCompleteTextView = getView(R.id.aet_stock);
        autoCompleteTextView.setAdapter(stockAdapter);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                searchKey=stockAdapter.getSymbolAtPosition(position);
            }
        });
        editText=getView(R.id.et_tag);
        tagLl=getView(R.id.ll_tag);
    }

    @Override
    public void initData() {

    }

    @Override
    public void addListener() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (group.getCheckedRadioButtonId()){
                    case R.id.radio_stock:
                        if(tagLl.getVisibility()==View.VISIBLE){
                            tagLl.setVisibility(View.GONE);
                        }
                        if(autoCompleteTextView.getVisibility()==View.GONE){
                            autoCompleteTextView.setVisibility(View.VISIBLE);
                        }
                        isStock=true;
                        break;
                    case R.id.radio_key:
                        if(tagLl.getVisibility()==View.GONE){
                            tagLl.setVisibility(View.VISIBLE);
                        }
                        if(autoCompleteTextView.getVisibility()==View.VISIBLE){
                            autoCompleteTextView.setVisibility(View.GONE);
                        }
                        isStock=false;
                        break;
                }
            }
        });
        autoCompleteTextView.addTextChangedListener(this);
        editText.addTextChangedListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.group_title_right_button:
                if(isStock){
                    if(searchKey!=null){
                        WebBase.addStock(searchKey, new JSONHandler() {
                            @Override
                            public void onSuccess(JSONObject obj) {
                                showToast("添加成功");
                                Intent intent=new Intent();
                                intent.putExtra(IntentKey.FLAG, Commons.STOCK);
                                setResult(RESULT_OK,intent);
                                finish();
                            }

                            @Override
                            public void onFailure(String err_msg) {
                                showToast(err_msg);
                            }
                        });
                    }else{
                        showToast("请输入正确的股票信息");
                    }
                }else{
                    if(!TextUtils.isEmpty(editText.getText())){
                        WebBase.addTag(editText.getText().toString(), new JSONHandler() {
                            @Override
                            public void onSuccess(JSONObject obj) {
                                showToast("添加成功");
                                Intent intent=new Intent();
                                intent.putExtra(IntentKey.FLAG, Commons.TAGKEY);
                                setResult(RESULT_OK,intent);
                                finish();
                            }
                            @Override
                            public void onFailure(String err_msg) {
                                showToast(err_msg);
                            }
                        });
                    }else{
                        showToast("请输入关键词");
                    }
                }
                break;
            case R.id.imv_clear_et:
                if(isStock){
                    searchKey=null;
                    autoCompleteTextView.setText("");
                }else{
                    editText.setText("");
                }
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(before>=1){
            searchKey=null;
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        if(s.length()!=0){
            imv_clear_et.setVisibility(View.VISIBLE);
        }else{
            imv_clear_et.setVisibility(View.GONE);
        }
    }
}
