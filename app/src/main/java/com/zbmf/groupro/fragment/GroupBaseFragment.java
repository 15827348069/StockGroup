package com.zbmf.groupro.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.zbmf.groupro.R;
import com.zbmf.groupro.view.SystemBarTintManager;

/**
 * Created by xuhao on 2017/2/15.
 */

public abstract class GroupBaseFragment extends Fragment {
    private View view;
    private boolean already_visibleHint;//标识是否已经显示
    private boolean already_initDate;
    private LinearLayout progress_layout;
    private View action_bar_layout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=setContentView(inflater);
        initView();
        onVisible();
        action_bar_layout=getView(R.id.action_bar_layout);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int statusBarHeight1 = -1;
            //获取status_bar_height资源的ID
            int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                //根据资源ID获取响应的尺寸值
                statusBarHeight1 = getResources().getDimensionPixelSize(resourceId);
            }
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,statusBarHeight1);
            if(action_bar_layout!=null){
                action_bar_layout.setLayoutParams(params);
            }
			setTranslucentStatus(true);
        }else{
            SystemBarTintManager tintManager = new SystemBarTintManager(getActivity());
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.transparent);
            if(action_bar_layout!=null){
                action_bar_layout.setVisibility(View.GONE);
            }

        }
        return view;
    }
    private void setTranslucentStatus(boolean on) {
        Window win = getActivity().getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
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
    @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
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
