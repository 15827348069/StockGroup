package com.zbmf.StockGroup.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.utils.LogUtil;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by xuhao on 2017/2/15.
 */

public abstract class BaseFragment extends Fragment {
    private View view;
    private boolean already_visibleHint;//标识是否已经显示
    private boolean already_initDate;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=setContentView(inflater);
        initFragment();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    protected abstract View setContentView(LayoutInflater inflater);
    protected void setinitData(boolean already_initDate){
        this.already_initDate=already_initDate;
    }
    protected <T extends View>T getView(int resourcesId){
        return (T) view.findViewById(resourcesId);
    }
    protected void showToast(String content) {
        if(getActivity()!=null){
            Toast.makeText(getActivity(), content, Toast.LENGTH_SHORT).show();
        }
    }
    public View getFragmentView(){
        return view;
    }
    @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        LogUtil.e("isVisibleToUser"+already_visibleHint);
        if(isVisibleToUser){
            already_visibleHint=true;
            onVisible();
        }
    }
    private void onVisible(){
        //view已经显示并且不为空，没有加载过数据时去加载数据
        if(already_visibleHint&&view!=null&&!already_initDate){
            initData();
            already_initDate=true;
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
    private void initFragment(){
        initView();
        onVisible();
    }
    protected abstract void initView();
    protected abstract void initData();
    @Override
    public void onResume() {
        super.onResume();
        if(view==null){
            initFragment();
        }
        onRush();
        StatService.onPageStart(getContext(),getClass().getName());
    }
    public void dialogShow(){
        if(getView(R.id.dialog_layout)!=null){
            getView(R.id.dialog_layout).setVisibility(View.VISIBLE);
        }
    }
    public void dialogDiss(){
        if(getView(R.id.dialog_layout)!=null){
            getView(R.id.dialog_layout).setVisibility(View.GONE);
        }
    }
    public void onRush(){

    }
}
