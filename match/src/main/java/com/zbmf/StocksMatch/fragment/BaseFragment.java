package com.zbmf.StocksMatch.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zbmf.StocksMatch.R;
import com.zbmf.worklibrary.baseview.BaseView;
import com.zbmf.worklibrary.dialog.CustomProgressDialog;
import com.zbmf.worklibrary.presenter.BasePresenter;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by xuhao
 * on 2017/2/15.
 */

public abstract class BaseFragment<T extends BasePresenter> extends Fragment implements BaseView{
    private View view;
    private T presenter;
    private Unbinder mUnbinder;
    private CustomProgressDialog progressDialog;
    private boolean UserVisible;
    private LinearLayout dialog_layout;

    public boolean isUserVisible() {
        return UserVisible;
    }

    public void setUserVisible(boolean userVisible) {
        UserVisible = userVisible;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        setUserVisible(isVisibleToUser);
        onResume();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(getLayout(),null);
        mUnbinder= ButterKnife.bind(this,view);
        dialog_layout=view.findViewById(R.id.dialog_layout);
        initView();
        initData();
        presenter=initPresent();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (presenter!=null){
            presenter.onStart(this);
        }
    }

    protected abstract int getLayout();
    protected abstract void initView();
    protected abstract void initData();
    protected abstract T initPresent();
    protected void showToast(String content) {
        if(getActivity()!=null){
            Toast.makeText(getActivity(), content, Toast.LENGTH_SHORT).show();
        }
    }
    public T getPresenter(){
        return presenter;
    }
    @Override
    public void onResume() {
        super.onResume();
        if(isUserVisible()&&isAdded()){
            if(presenter!=null){
                //fragment初始化加载数据
                presenter.getDatas();
            }else{
                presenter=initPresent();
            }
        }
    }
    public void setTitleMessage(String message){
        TextView textView=view.findViewById(R.id.tv_title_text);
        textView.setText(message);
    }
    public void setLoadingVis(int visi){
        if(dialog_layout!=null){
            dialog_layout.setVisibility(visi);
        }
    }

    /*-----------下面是 progressDialog------------*/
    public void ShowLoading() {
        if(progressDialog!=null&&!progressDialog.isShowing()){
            progressDialog.show();
        }
    }

    public void DissLoading() {
        if(progressDialog!=null&&progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }
    public void initProgressDialog(String tipMsg) {
        if (progressDialog == null) {
            progressDialog = CustomProgressDialog.createDialog(getActivity());
        }
    }

    @Override
    public void onRefreshComplete() {

    }

    @Override
    public void loadMore() {

    }

    @Override
    public void dissLoading() {
        DissLoading();
    }

    @Override
    public void showToastMsg(String msg) {
        showToast(msg);
    }
    @Override
    public void onDestroyView() {
        if (mUnbinder != null) {
            mUnbinder.unbind();
            mUnbinder=null;
        }
        if(progressDialog!=null){
            progressDialog=null;
        }
        if(presenter!=null){
            presenter.onDestroy();
            presenter=null;
        }
        super.onDestroyView();
    }
}
