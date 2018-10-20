package com.zbmf.groupro.fragment;

import android.view.LayoutInflater;
import android.view.View;

import com.zbmf.groupro.R;

/**
 * Created by xuhao on 2017/2/24.
 */

public class UserCollectsVedioFragment extends BaseFragment {
    public static UserCollectsVedioFragment newInstance(){
        UserCollectsVedioFragment fagment=new UserCollectsVedioFragment();
        return fagment;
    }
    @Override
    protected View setContentView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.user_collects_vedio_layout,null);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }
}
