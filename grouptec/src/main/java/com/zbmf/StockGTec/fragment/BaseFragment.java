package com.zbmf.StockGTec.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Created by xuhao on 2017/2/15.
 */

public abstract class BaseFragment extends Fragment {
    private View view;
    private boolean already_visibleHint;//标识是否已经显示
    private boolean already_initDate;
    private LinearLayout progress_layout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=setContentView(inflater);
        initView();
        onVisible();
        return view;
    }
    public void setMinHeight(int height)
    {
        Log.e("设置最小高度>>>>","=="+height);
        view.setMinimumHeight(height);
    }
    protected abstract View setContentView(LayoutInflater inflater);
    protected void setinitData(boolean already_initDate){
        this.already_initDate=already_initDate;
    }
    protected <T extends View>T getView(int resourcesId){
        return (T) view.findViewById(resourcesId);
    }
    protected void showToast(String content) {
        Toast.makeText(getActivity(), content, Toast.LENGTH_SHORT).show();
    }
    public View getFragmentView(){
        return view;
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(getUserVisibleHint()){
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
    protected abstract void initView();
    protected abstract void initData();
}
