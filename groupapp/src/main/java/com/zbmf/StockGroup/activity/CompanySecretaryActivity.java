package com.zbmf.StockGroup.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.api.JSONHandler;
import com.zbmf.StockGroup.api.WebBase;
import com.zbmf.StockGroup.beans.DongmiBean;
import com.zbmf.StockGroup.utils.ImageLoaderOptions;
import com.zbmf.StockGroup.utils.JSONParse;

import org.json.JSONObject;

public class CompanySecretaryActivity extends BaseActivity {

    private String mSymbol;
    private ImageView mCompany_secretary_avatar;
    private TextView mCompanyName,mBelongAddress,mCompanyEnrollAddress,
            mBelongTrade,mRoleSecretary,mCompanyTelephoneNumber,mCompanyMileBox;
    private LinearLayout mStock_info_view,mNo_data_view;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_company_secretary;
    }

    @Override
    public void initView() {
        initTitle("董秘信息");
        mSymbol = getIntent().getStringExtra("symbol");

        mCompany_secretary_avatar = getView(R.id.company_secretary_avatar);
        mCompanyName = getView(R.id.companyName);
        mBelongAddress = getView(R.id.belongAddress);
        mCompanyEnrollAddress = getView(R.id.companyEnrollAddress);
        mBelongTrade = getView(R.id.belongTrade);
        mRoleSecretary = getView(R.id.roleSecretary);
        mCompanyTelephoneNumber = getView(R.id.companyTelephoneNumber);
        mCompanyMileBox = getView(R.id.companyMileBox);
        mStock_info_view = getView(R.id.stock_info_view);
        mNo_data_view = getView(R.id.no_data_view);

        mStock_info_view.setVisibility(View.GONE);
        mNo_data_view.setVisibility(View.VISIBLE);
    }

    @Override
    public void initData() {
        //获取相应的董秘信息
        if (!TextUtils.isEmpty(mSymbol)) {
            getCompanySecretaryInfo();
        }
    }

    @Override
    public void addListener() {}

    //获取相应的董秘信息
    private void getCompanySecretaryInfo() {
        WebBase.getCompanySecretaryInfo(mSymbol, new JSONHandler(true,this,getString(R.string.loading)) {
            @Override
            public void onSuccess(JSONObject obj) {
                if (obj.optString("status").equals("ok")) {
                    mStock_info_view.setVisibility(View.VISIBLE);
                    mNo_data_view.setVisibility(View.GONE);
                    DongmiBean mDongmiInfo = JSONParse.getDongmiInfo(obj);
                    setDongmiInfoWithView(mDongmiInfo);
                }else {
                    mStock_info_view.setVisibility(View.GONE);
                    mNo_data_view.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(String err_msg) {
            }
        });
    }

    public void setDongmiInfoWithView(DongmiBean mDongmiInfo){
        if (mDongmiInfo!=null){
            ImageLoader.getInstance().displayImage(mDongmiInfo.getAvatar(),mCompany_secretary_avatar, ImageLoaderOptions.AvatarOptions());
            mCompanyName.setText(mDongmiInfo.getCompany_name());
            mBelongAddress.setText(String.format("所属地区:%s",mDongmiInfo.getAddress()));
            mCompanyEnrollAddress.setText(String.format("公司注册地:%s",mDongmiInfo.getArea()));
            mBelongTrade.setText(String.format("所属行业:%s",mDongmiInfo.getIndustry()));
            mRoleSecretary.setText(String.format("董事会秘书:%s",mDongmiInfo.getSecretary()));
            mCompanyTelephoneNumber.setText(String.format("公司电话:%s",mDongmiInfo.getPhone()));
            mCompanyMileBox.setText(String.format("公司邮箱:%s",mDongmiInfo.getEmail()));
        }
    }

}
